package com.example.booknumber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class ProcessActivity extends AppCompatActivity {

    String BASE_URL = "http://10.10.61.99:5000/";              //로컬
//    String BASE_URL = "http://138.2.127.172:5000/";          //리눅스서버
//    String BASE_URL = "http://192.168.115.51:5000/";          // 핫스팟
//    String BASE_URL = "http://10.10.28.227:5000/";  //컨벤션홀

    ImageView wait;

    Integer tableID;
    Uri videoURI;

    Call<ArrayList<String>> call_videoPost;
    GetImage getImage;
    Bitmap bitmap;

    // DBHelper2
    DBHelper2 helper;
    SQLiteDatabase db;

    ArrayList<String> result_list;

    // UUID
    String random_name = UUID.randomUUID().toString();

    String prev_bookNumber;
    String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        Toast.makeText(ProcessActivity.this,"동영상 분석을 시작합니다",Toast.LENGTH_SHORT).show();

        wait = (ImageView) findViewById(R.id.waiting);
        Glide.with(this).load(R.raw.wait).into(wait);

        Intent intent = getIntent();
        videoURI = intent.getParcelableExtra("videoURI");
        tableID = intent.getIntExtra("tableID", -1); //tableID 키값으로 데이터를 받는다.

        helper = new DBHelper2(ProcessActivity.this, 1);
        db = helper.getWritableDatabase();
        String path = getRealPathFromURI(videoURI);

        // Uri 타입의 파일경로를 가지는 RequestBody 객체 생성
        File videoFile = new File(path);
        RequestBody fileBody = RequestBody.create(MediaType.parse("video/mp4"), videoFile);

        // RequestBody로 Multipart.Part 객체 생성
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("video", videoFile.getName(), fileBody);
        Log.d("len",videoFile.getName());
        call_videoPost = RetrofitClient.getApiService().postVideo(filePart);
        call_videoPost.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                result_list = response.body();
                Toast.makeText(ProcessActivity.this,"분석을 마쳤습니다",Toast.LENGTH_SHORT).show();
                Log.d("len", "분석결과 : " + result_list.toString());


                Toast.makeText(ProcessActivity.this,"DB입력 작업시작",Toast.LENGTH_SHORT).show();
                //DB입력

                //청구기호 & imgURL 결과 하나씩 뽑아서 처리
                for(String result : result_list){

                    String bookNumber = result.split("&&")[0];
                    String imgURL = BASE_URL + result.split("&&")[1];
                    Log.d("len","bookNumber ==> " + bookNumber);
                    Log.d("len","imgURL ==> " + imgURL);

                    try {
                        prev_bookNumber = helper.getLast(tableID);
                        Log.d("len","prev_bookNumber : " + prev_bookNumber);
                        if (0 >= prev_bookNumber.compareTo(bookNumber)) {
                            state = "True";
                        } else {
                            state = "False";
                        }
                        Log.d("len","state : " + state);

                    } catch (Exception e) {
                        Log.d("len", e.toString());
                    }


                    Thread Thread = new Thread() {
                        @Override
                        public void run(){
                            getImage = new GetImage();
                            bitmap = getImage.bitmap(imgURL);
                        }
                    };

                    Thread.start();

                    try {
                        //join() 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다림
                        Thread.join();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }



                    random_name = UUID.randomUUID().toString();
                    try {
                        saveBitmapToJpeg(bitmap, random_name);
                        helper.insert(tableID, bookNumber, state, random_name);
                    } catch (Exception e) {
                        Log.d("len",e.toString());
                    }

                }


                Toast.makeText(ProcessActivity.this,"DB입력 작업 끝",Toast.LENGTH_SHORT).show();

                Intent backintent = new Intent(ProcessActivity.this, MainActivity.class);
                startActivity(backintent);
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Toast.makeText(ProcessActivity.this,"통신에러가 발생했습니다",Toast.LENGTH_SHORT).show();
                Log.d("len", t.toString());
            }
        });




    }



    // URI to 절대경로
    private String getRealPathFromURI(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage")) {
            return contentUri.getPath();
        }

        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = { MediaStore.Files.FileColumns.DATA };
        String selection = MediaStore.Files.FileColumns._ID + " = " + id;
        Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try {
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex);
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    public void saveBitmapToJpeg(Bitmap bitmap, String name) {
        //내부저장소 캐시 경로를 받아옵니다.
        File storage = getCacheDir();

        //저장할 파일 이름
        String fileName = name + ".jpg";

        //storage 에 파일 인스턴스를 생성합니다.
        File tempFile = new File(storage, fileName);

        try {

            // 자동으로 빈 파일을 생성합니다.
            tempFile.createNewFile();

            // 파일을 쓸 수 있는 스트림을 준비합니다.
            FileOutputStream out = new FileOutputStream(tempFile);

            // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            // 스트림 사용후 닫아줍니다.
            out.close();

        } catch (FileNotFoundException e) {
            Log.e("MyTag", "FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("MyTag", "IOException : " + e.getMessage());
        }
    }

}
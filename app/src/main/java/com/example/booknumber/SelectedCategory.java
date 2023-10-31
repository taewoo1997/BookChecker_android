package com.example.booknumber;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class SelectedCategory extends AppCompatActivity {

    String category_name;
    Integer tableID;

    Button btn_back;

    ListView table;
    MyListAdapter lAdapter;

    ArrayList<String> arrayList;

    ImageView gallery_icon;

    // DBHelper2
    DBHelper2 helper;
    SQLiteDatabase db;

    // UUID
    String random_name = UUID.randomUUID().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_category);

        Intent intent = getIntent(); //전달할 데이터를 받을 Intent
        category_name = intent.getStringExtra("category_name");
        tableID = intent.getIntExtra("tableID", -1); //tableID 키값으로 데이터를 받는다. String을 받아야 하므로 getStringExtra()를 사용

        // 실행 중 제목을 변경할 수 있습니다.
        getSupportActionBar().setTitle(category_name);

        helper = new DBHelper2(SelectedCategory.this, 1);
        db = helper.getWritableDatabase();

        Bitmap nothing = BitmapFactory.decodeResource(this.getResources(), R.drawable.convert_01);
        saveBitmapToJpeg(nothing, "nothing");

        try {
            helper.createTable(db);
        } catch(Exception e) {
//            Log.d("len",e.toString());
        }

        helper.insert(tableID, "temp", "null", "nothing");
        helper.Delete(tableID,"temp");



        arrayList = new ArrayList<>();


        if (tableID == -1) {

            try {

                Bitmap temp;
                db = helper.getWritableDatabase();


                temp = BitmapFactory.decodeResource(this.getResources(), R.drawable.convert_01);
                random_name = UUID.randomUUID().toString();
                saveBitmapToJpeg(temp, random_name);
                helper.insert(tableID, "361.43 정67ㄱ", "True", random_name);

                temp = BitmapFactory.decodeResource(this.getResources(), R.drawable.convert_02);
                random_name = UUID.randomUUID().toString();
                saveBitmapToJpeg(temp, random_name);
                helper.insert(tableID, "361.54 김18ㄱ", "True", random_name);

                temp = BitmapFactory.decodeResource(this.getResources(), R.drawable.convert_04);
                random_name = UUID.randomUUID().toString();
                saveBitmapToJpeg(temp, random_name);
                helper.insert(tableID, "361.5 U999u박 c.2", "True", random_name);

                temp = BitmapFactory.decodeResource(this.getResources(), R.drawable.convert_03);
                random_name = UUID.randomUUID().toString();
                saveBitmapToJpeg(temp, random_name);
                helper.insert(tableID, "361.5 U999u박", "False", random_name);

                temp = BitmapFactory.decodeResource(this.getResources(), R.drawable.convert_06);
                random_name = UUID.randomUUID().toString();
                saveBitmapToJpeg(temp, random_name);
                helper.insert(tableID, "361.54 S221e정", "True", random_name);

                temp = BitmapFactory.decodeResource(this.getResources(), R.drawable.convert_05);
                random_name = UUID.randomUUID().toString();
                saveBitmapToJpeg(temp, random_name);
                helper.insert(tableID, "361.54 김18ㄱ c.2", "False", random_name);

                temp = BitmapFactory.decodeResource(this.getResources(), R.drawable.convert_07);
                random_name = UUID.randomUUID().toString();
                saveBitmapToJpeg(temp, random_name);
                helper.insert(tableID, "361.54 S221e정 c.2", "True", random_name);

                temp = BitmapFactory.decodeResource(this.getResources(), R.drawable.convert_08);
                random_name = UUID.randomUUID().toString();
                saveBitmapToJpeg(temp, random_name);
                helper.insert(tableID, "361.6 이57우", "True", random_name);

            } catch (Exception e) {
                Log.d("len",e.toString());
                helper.createTable(db);
            }

        }

//        if (tableID == -1) {
//
//            try {
//                db = helper.getWritableDatabase();
//                helper.insert(tableID, "340.1 김11ㅈ2", "True");
//                helper.insert(tableID, "340.1 김11ㅈ2 c.2", "True");
//                helper.insert(tableID, "340.1 김19ㅈ", "True");
//                helper.insert(tableID, "340.1 김44ㅁ c.2", "False");
//                helper.insert(tableID, "340.1 김44ㅁ", "True");
//                helper.insert(tableID, "340.1 김49ㅈ", "True");
//                helper.insert(tableID, "340.1 김53ㅈ c.2", "True");
//                helper.insert(tableID, "340.1 김64ㅂ-2", "True");
//            } catch (Exception e) {
//                helper.createTable(db);
//            }
//
//        }

        try {
            arrayList = helper.getResult(tableID);
        } catch (Exception e) {
            Toast.makeText(SelectedCategory.this,"테이블이 아직 없습니다",Toast.LENGTH_SHORT);
        }

        table = (ListView) findViewById(R.id.list_view_DBtable);
        lAdapter = new MyListAdapter(this, arrayList);
        table.setAdapter(lAdapter);


        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        gallery_icon = (ImageView) findViewById(R.id.load_album);
        gallery_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myInetent = new Intent(SelectedCategory.this, AlbumActivity.class);
                myInetent.putExtra("tableID",tableID); // tableID 값 전송
                startActivity(myInetent);
            }
        });


    }

    // 액티비티가 화면에 나타날 때 메뉴 구성을 위해서 호출하는 메서드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // xml을 통해 메뉴를 구성할 수 있는 개체를 추출
        MenuInflater inflater = getMenuInflater();
        // xml을 이용해 메뉴를 구성한다.
        inflater.inflate(R.menu.menu_option, menu);

        return true;
    }

    // 옵션 메뉴의 항목을 터치하면 호출되는 메서드
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // 사용자가 터치한 항목 객체의 id를 추출한다.
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_all:
                arrayList = new ArrayList<>();

                arrayList = helper.getResult(tableID);

                table = (ListView) findViewById(R.id.list_view_DBtable);
                lAdapter = new MyListAdapter(SelectedCategory.this, arrayList);
                table.setAdapter(lAdapter);

                break;

            case R.id.menu_false:
                arrayList = new ArrayList<>();

                arrayList = helper.getErrorResult(tableID);

                table = (ListView) findViewById(R.id.list_view_DBtable);
                lAdapter = new MyListAdapter(SelectedCategory.this, arrayList);
                table.setAdapter(lAdapter);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class MyListAdapter extends BaseAdapter {

        private ArrayList<String> dbtable_rowArrayList;
        private Context context;

        public MyListAdapter(Context context, ArrayList<String> dbtable_rowArrayList) {
            this.context = context;
            this.dbtable_rowArrayList = dbtable_rowArrayList;
        }

        @Override
        public int getCount() {
            return dbtable_rowArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View v = LayoutInflater.from(context).inflate(R.layout.listview_table, null);

            TextView category_name = (TextView) v.findViewById(R.id.row_table);

            category_name.setText(arrayList.get(i).split(" / ")[0] + " / " + arrayList.get(i).split(" / ")[1]);

            if (arrayList.get(i).contains("False")) {
                category_name.setTextColor(Color.parseColor("#ff0000"));
            }

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(SelectedCategory.this);
                    View mView = layoutInflaterAndroid.inflate(R.layout.table_dialog_box, null);
                    AlertDialog.Builder alertDialogBuilderTable = new AlertDialog.Builder(SelectedCategory.this);
                    alertDialogBuilderTable.setView(mView);

                    final TextView subTitle = (TextView) mView.findViewById(R.id.table_dialogSubTitle);
                    final ImageView check_image = (ImageView) mView.findViewById(R.id.table_dialogImage);
                    String path = getCacheDir() + "/" + arrayList.get(i).split(" / ")[2] + ".jpg";
                    Bitmap bitmap_image = BitmapFactory.decodeFile(path);

                    subTitle.setText(dbtable_rowArrayList.get(i).split(" / ")[0] +" / " + dbtable_rowArrayList.get(i).split(" / ")[1]);
                    check_image.setImageBitmap(bitmap_image);

                    alertDialogBuilderTable
                            .setCancelable(false)
                            .setPositiveButton("오류수정", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogBox, int id) {
                                    String[] table_row = dbtable_rowArrayList.get(i).split(" / ");
                                    helper.Update(table_row[0], "True");
                                    dbtable_rowArrayList.set(i, table_row[0] + " / True");
                                    lAdapter.notifyDataSetChanged();

                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogBox, int id) {
                                    dialogBox.cancel();
                                }
                            })
                            .setNeutralButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogBox, int id) {

                                    String path = getCacheDir() + "/" + dbtable_rowArrayList.get(i).split(" / ")[2] + ".jpg";
                                    File f = new File(path);
                                    if (f.delete()) {
                                        Log.i("len", "file remove = " + f.getName() + ", 삭제 성공");
                                    } else {
                                        Log.i("len", "file remove = " + f.getName() + ", 삭제 실패");
                                    }

                                    String[] table_row = dbtable_rowArrayList.get(i).split(" / ");
                                    helper.Delete(tableID, table_row[0]); //DB삭제
                                    dbtable_rowArrayList.remove(i);
                                    lAdapter.notifyDataSetChanged();

                                }
                            });

                    AlertDialog alertDialogAndroid = alertDialogBuilderTable.create();
                    alertDialogAndroid.show();

                    return false;
                }
            });

            return v;
        }

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
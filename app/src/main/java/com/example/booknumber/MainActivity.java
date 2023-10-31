package com.example.booknumber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ListView category;
    MyListAdapter lAdapter;

    ArrayList<String> arrayList;

    ImageView btn_icon;

    // DBHelper
    DBHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 권한ID를 가져옵니다
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int permission2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);


        // 권한이 열려있는지 확인
        if (permission == PackageManager.PERMISSION_DENIED || permission2 == PackageManager.PERMISSION_DENIED) {
            // 마쉬멜로우 이상버전부터 권한을 물어본다
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 권한 체크(READ_PHONE_STATE의 requestCode를 1000으로 세팅
                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        1000);
            }
            return;
        }

        helper = new DBHelper(MainActivity.this,1);

        arrayList = new ArrayList<>();

        arrayList = helper.getResult();

        // DB 삭제
//        for(int i=15 ; i>=1 ; i--) {
//            Log.d("len", "진입"+ String.valueOf(arrayList.size()));
//            helper.Delete(i);
//            Log.d("len", String.valueOf(arrayList.size()));
//
//        }

        category = (ListView) findViewById(R.id.list_view_category);
        lAdapter = new MyListAdapter(this, arrayList);
        category.setAdapter(lAdapter);

        btn_icon = (ImageView) findViewById(R.id.add_category_img);
        btn_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
                View mView = layoutInflaterAndroid.inflate(R.layout.edit_dialog_box, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);

                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                // 추가
                                helper.insert(userInputDialogEditText.getText().toString()); //DB추가
                                arrayList.add(userInputDialogEditText.getText().toString()); //리스트추가
                                lAdapter.notifyDataSetChanged(); //리스트업데이트
                            }
                        })

                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }
        });


    }

    // 권한 체크 이후로직
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        // READ_PHONE_STATE의 권한 체크 결과를 불러온다
        super.onRequestPermissionsResult(requestCode, permissions, grandResults);
        if (requestCode == 1000) {
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            // 권한 체크에 동의를 하지 않으면 안드로이드 종료
            if (check_result == true) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            } else {
                finish();
            }
        }
    }


    public class MyListAdapter extends BaseAdapter {

        private ArrayList<String> category_rowArrayList;
        private Context context;

        public MyListAdapter(Context context, ArrayList<String> category_rowArrayList) {
            this.context = context;
            this.category_rowArrayList = category_rowArrayList;
        }

        @Override
        public int getCount() {
            return category_rowArrayList.size();
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

            View v = LayoutInflater.from(context).inflate(R.layout.listview_item, null);

            TextView category_name = (TextView) v.findViewById(R.id.row_text);

            category_name.setText(arrayList.get(i));

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
                    View mView = layoutInflaterAndroid.inflate(R.layout.edit_dialog_box, null);
                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilderUserInput.setView(mView);

                    final TextView title = (TextView) mView.findViewById(R.id.dialogTitle);
                    final TextView subTitle = (TextView) mView.findViewById(R.id.dialogSubTitle);
                    final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);

                    title.setText("카테고리 수정");
                    subTitle.setText("수정할 카테고리 이름을 입력해주세요.");

                    alertDialogBuilderUserInput
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    // 수정
                                    String name = category_rowArrayList.get(i);
                                    Integer tableID = helper.getID(name);

                                    helper.Update(userInputDialogEditText.getText().toString(), tableID); //DB수정
                                    category_rowArrayList.set(i,userInputDialogEditText.getText().toString()); //리스트수정
                                    lAdapter.notifyDataSetChanged(); //리스트업데이트
                                }
                            })

                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    dialogBox.cancel();
                                }
                            })

                            .setNeutralButton("삭제", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    // 삭제
                                    String name = category_rowArrayList.get(i);
                                    Integer tableID = helper.getID(name);

                                    helper.Delete(tableID); //DB삭제
                                    category_rowArrayList.remove(i); //리스트삭제
                                    lAdapter.notifyDataSetChanged(); //리스트업데이트
                                }
                            });

                    AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                    alertDialogAndroid.show();

                    return true;
                }
            });

            // 선택한 카테고리로 이동
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = category_rowArrayList.get(i);
                    Integer tableID = helper.getID(name);

                    Intent myInetent = new Intent(MainActivity.this, SelectedCategory.class);
                    myInetent.putExtra("category_name",name); // 카테고리 name 값 전송
                    myInetent.putExtra("tableID",tableID); // tableID 값 전송
                    startActivity(myInetent);
                }
            });


            return v;
        }

    }


}
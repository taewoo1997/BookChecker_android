package com.example.booknumber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class AlbumActivity extends AppCompatActivity {

    Integer tableID;
    VideoView videoView;
    Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        Intent intent = getIntent(); //전달할 데이터를 받을 Intent
        tableID = intent.getIntExtra("tableID", -1); //tableID 키값으로 데이터를 받는다. String을 받아야 하므로 getStringExtra()를 사용

        videoView = findViewById(R.id.videoView);
    }

    public void bt1(View view) {    // 동영상 선택 누르면 실행됨 동영상 고를 갤러리 오픈
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // 갤러리
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                MediaController mc = new MediaController(this); // 비디오 컨트롤 가능하게(일시정지, 재시작 등)
                videoView.setMediaController(mc);

                fileUri = data.getData();
                Log.d("len",fileUri.toString());
                videoView.setVideoPath(String.valueOf(fileUri));    // 선택한 비디오 경로 비디오뷰에 셋
                videoView.start();  // 비디오뷰 시작
            }
        }
    }

    public void bt3(View view) {
        try{
            Intent myInetent = new Intent(AlbumActivity.this, ProcessActivity.class);
            myInetent.putExtra("videoURI",fileUri);
            myInetent.putExtra("tableID",tableID); // tableID 값 전송
            startActivity(myInetent);
        } catch (Exception e) {
            Toast.makeText( AlbumActivity.this, "동영상을 불러와 주세요",Toast.LENGTH_SHORT).show();
        }

    }
}
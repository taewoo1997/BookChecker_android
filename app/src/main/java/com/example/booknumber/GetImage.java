package com.example.booknumber;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetImage {

    public Bitmap bitmap(String src) {
        HttpURLConnection conn = null;
        BufferedInputStream bis = null;
        Bitmap myBitmap = null;
        try {
            URL url = new URL(src);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream inputStream = conn.getInputStream();
            bis = new BufferedInputStream(inputStream);
            myBitmap = BitmapFactory.decodeStream(bis);
        } catch (Exception e) {
            Log.d("len","네트워크 오류!");
            Log.d("len",e.toString());
        }
        return myBitmap;
    }

}

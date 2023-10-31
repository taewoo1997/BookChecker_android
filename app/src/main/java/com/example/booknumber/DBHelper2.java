package com.example.booknumber;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper2 extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "book.db";

    // DBHelper2 생성자
    public DBHelper2(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    // BookNumber Table 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE BookNumber(category_id INTEGER, bookNumber TEXT, state TEXT, path String, FOREIGN KEY (category_id) REFERENCES Category (id) ON DELETE CASCADE)");
    }

    public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE BookNumber(category_id INTEGER, bookNumber TEXT, state TEXT, path String,  FOREIGN KEY (category_id) REFERENCES Category (id) ON DELETE CASCADE)");
    }

    // BookNumber Table Upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS BookNumber");
        onCreate(db);
    }

    // BookNumber Table 데이터 입력
    public void insert(Integer category_id, String bookNumber, String state, String path) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO BookNumber (category_id, bookNumber, state, path) VALUES(" + category_id + ",'" + bookNumber + "','" + state + "','" + path + "')");
        db.close();
    }

    // BookNumber Table 데이터 수정
    public void Update(String bookNumber, String state) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE BookNumber SET state = '" + state + "' WHERE bookNumber = '" + bookNumber + "'" );
        db.close();
    }

    // BookNumber Table 데이터 삭제
    public void Delete(Integer category_id, String bookNumber) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE from BookNumber WHERE category_id = " + category_id + " and " + "bookNumber = '" + bookNumber +"'" );
        db.close();
    }

    // BookNumber Table 조회
    public ArrayList<String> getResult(Integer category_id) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> result = new ArrayList<>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM BookNumber where category_id = " + category_id, null);
        while (cursor.moveToNext()) {
            result.add(cursor.getString(1) + " / " + cursor.getString(2) + " / " + cursor.getString(3));
        }

        return result;
    }

    // BookNumber Table 조회
    public ArrayList<String> getErrorResult(Integer category_id) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> result = new ArrayList<>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM BookNumber where category_id = " + category_id + " and " + "state = 'False'" , null);
        while (cursor.moveToNext()) {
            result.add(cursor.getString(1) + " / " + cursor.getString(2) + " / " + cursor.getString(3));
        }

        return result;
    }


    public String getLast(Integer category_id){
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> result = new ArrayList<>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM BookNumber where category_id = " + category_id , null);
        while (cursor.moveToNext()) {
            result.add(cursor.getString(1));
        }

        if (result.size() != 0) {
            return result.get(result.size() - 1);
        } else {
            return "";
        }

    }
}

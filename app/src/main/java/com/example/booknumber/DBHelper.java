package com.example.booknumber;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "book.db";

    // DBHelper 생성자
    public DBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    // Category Table 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Category(id INTEGER primary key, name TEXT)");
        db.execSQL("INSERT INTO Category (name) VALUES('카테고리1')");
        db.execSQL("INSERT INTO Category (name) VALUES('카테고리2')");
        db.execSQL("INSERT INTO Category (name) VALUES('카테고리3')");
    }

    // Category Table Upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Category");
        onCreate(db);
    }

    // Category Table 데이터 입력
    public void insert(String name) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO Category (name) VALUES('" + name + "')");
        db.close();
    }

    // Category Table 데이터 수정
    public void Update(String name, Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE Category SET NAME = '" + name + "' WHERE id = " + id );
        db.close();
    }

    // Category Table 데이터 삭제
    public void Delete(Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE from Category WHERE id = " + id );
        db.close();
    }

    // Category Table 조회
    public ArrayList<String> getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> result = new ArrayList<>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM Category", null);
        while (cursor.moveToNext()) {
            result.add(cursor.getString(1));
        }

        return result;
    }


    // ID값 조회
    public Integer getID(String name) {
        SQLiteDatabase db = getReadableDatabase();
        Integer result;

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT id FROM Category where NAME = '" + name + "'", null);
        cursor.moveToFirst();
        result = cursor.getInt(0);

        return result;


    }
}

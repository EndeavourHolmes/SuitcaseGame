package com.example.koffer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SQLdb extends SQLiteOpenHelper {

    public static final String RANKING_TABLE = "RANKING_TABLE";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_SCORE = "SCORE";
    public static final String COLUMN_NEEDEDTIME = "NEEDEDTIME";
    public static final String COLUMN_ID = "ID";

    public SQLdb(@Nullable Context context) {
        super(context, "ranking.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + RANKING_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_SCORE + " INT, " + COLUMN_NEEDEDTIME + " INT)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(String name, int score, int neededTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_SCORE, score);
        cv.put(COLUMN_NEEDEDTIME, neededTime);

        long insert = db.insert(RANKING_TABLE,null , cv);
        if (insert == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public List<String> getRanking(){
        List<String> returnList = new ArrayList<>();

        //String queryString = "SELECT * FROM " + RANKING_TABLE; TODO: db begrenzen
        String queryString = "SELECT * FROM " + RANKING_TABLE + " ORDER BY " + COLUMN_SCORE + " DESC, " + COLUMN_NEEDEDTIME + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        String ausgabe;

        if (cursor.moveToFirst()){
            do {
                ausgabe="";
                int userId = cursor.getInt(0);
                String name = cursor.getString(1);
                int score = cursor.getInt(2);
                int neededTime = cursor.getInt(3);

                ausgabe += name + " Score: " + score + " Time: " + neededTime;
                returnList.add(ausgabe);

            }while (cursor.moveToNext());

        }
        else {

        }
        cursor.close();
        db.close();

        return returnList;
    }
}

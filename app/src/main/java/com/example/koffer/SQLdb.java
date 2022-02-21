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

        // Check more than 10 entries in ranking
        if (countEntries()>9){

            // Check if old highscores lower than actual one
            if (compareNewHighscore(score,neededTime) == 404){
                return true;
            }

            // Update the entry (which is last one when sorted), when new highscore is better than one of the ten
            else {
                int updateID = compareNewHighscore(score,neededTime);
                String id = String.valueOf(updateID);

                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues cv = new ContentValues();

                cv.put(COLUMN_NAME, name);
                cv.put(COLUMN_SCORE, score);
                cv.put(COLUMN_NEEDEDTIME, neededTime);

                long insert = db.update(RANKING_TABLE, cv, COLUMN_ID + " = ?", new String[]{id});

                if (insert == -1){
                    db.close();
                    return false;
                }
                else {
                    db.close();
                    return true;
                }
            }
        }
        else {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(COLUMN_NAME, name);
            cv.put(COLUMN_SCORE, score);
            cv.put(COLUMN_NEEDEDTIME, neededTime);

            long insert = db.insert(RANKING_TABLE, null, cv);
            if (insert == -1) {
                db.close();
                return false;
            } else {
                db.close();
                return true;
            }
        }
    }

    public List<String> getRanking(){
        List<String> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + RANKING_TABLE + " ORDER BY " + COLUMN_SCORE + " DESC, " + COLUMN_NEEDEDTIME + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        String ausgabe;
        int position = 0;

        if (cursor.moveToFirst()){
            do {
                ausgabe="";
                position++;
                int userId = cursor.getInt(0);
                String name = cursor.getString(1);
                int score = cursor.getInt(2);
                int neededTime = cursor.getInt(3);

                ausgabe += position + ": " + name + " Score: " + score + " Time: " + neededTime;
                returnList.add(ausgabe);

            }while (cursor.moveToNext());

        }
        else {

        }
        cursor.close();
        db.close();

        return returnList;
    }

    public int countEntries(){
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT count(*) FROM " + RANKING_TABLE;

        Cursor mCount= db.rawQuery(queryString, null);
        mCount.moveToFirst();

        int count= mCount.getInt(0);

        mCount.close();
        db.close();

        return count;
    }

    public int compareNewHighscore(int newScore, int newTime){

        String queryString = "SELECT * FROM " + RANKING_TABLE + " ORDER BY " + COLUMN_SCORE + " DESC, " + COLUMN_NEEDEDTIME + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        int lowerId = 404;

        if (cursor.moveToFirst()){
            do {
                int userId = cursor.getInt(0);
                String name = cursor.getString(1);
                int score = cursor.getInt(2);
                int neededTime = cursor.getInt(3);

                if (newScore > score){
                    cursor.moveToLast();
                    userId = cursor.getInt(0);
                    lowerId = userId;
                    break;
                }
                if (newScore == score){
                    if (newTime < neededTime){
                        cursor.moveToLast();
                        userId = cursor.getInt(0);
                        lowerId = userId;
                        break;
                    }
                }

            }while (cursor.moveToNext());

        }
        else {

        }
        cursor.close();
        db.close();

        return lowerId;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + RANKING_TABLE;

        db.execSQL(queryString);
        db.close();
    }
}

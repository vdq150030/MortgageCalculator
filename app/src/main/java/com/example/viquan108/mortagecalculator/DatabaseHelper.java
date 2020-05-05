package com.example.viquan108.mortagecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by viquan108 on 9/9/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "student.db";
    public static final String TABLE_NAME = "amortization_table";
    private static final int DATABASE_VERSION = 1;
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "SURNAME";
    public static final String COL_4 = "MARKS";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
        db.execSQL("create table "+ TABLE_NAME+
                "("+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COL_2+" TEXT, "+
                COL_3+" TEXT, "+
                COL_4+" INTEGER)");
        */

        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT, " +
                COL_3 + " TEXT, " +
                COL_4 + " INTEGER)"
        );
    }





    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
            onCreate(db);
    }

    public boolean insertData(String date, String payment, String principal, String interest, String Total, String balance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,date);
        contentValues.put(COL_3,payment);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }
}

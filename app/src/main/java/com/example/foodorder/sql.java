package com.example.foodorder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class sql extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "grocerylist.db";
    public static final int DATABASE_VERSION = 1;

    public sql(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_GROCERYLIST_TABLE = "CREATE TABLE " +
                ItemContract.ItemEntry.TABLE_NAME + " (" +
                ItemContract.ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ItemContract.ItemEntry.COLUMN_NAME + " TEXT , " +
                ItemContract.ItemEntry.COLUMN_PRICE +" TEXT , "+
                ItemContract.ItemEntry.COLUMN_AMOUNT + " TEXT , " +
                ItemContract.ItemEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_GROCERYLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ItemContract.ItemEntry.TABLE_NAME);
        onCreate(db);
    }

}

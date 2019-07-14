package com.example.petheart.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoryBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "memoryBase.db";

    public MemoryBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + MemoryDbSchema.MemoryTable.NAME + "" +
                "(" + " _id integer primary key autoincrement, " +
                MemoryDbSchema.MemoryTable.Cols.UUID + ", " +
                MemoryDbSchema.MemoryTable.Cols.TITLE + ", " +
                MemoryDbSchema.MemoryTable.Cols.DATE + ", " +
                MemoryDbSchema.MemoryTable.Cols.FAVORITE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
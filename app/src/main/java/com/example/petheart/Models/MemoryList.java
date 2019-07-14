package com.example.petheart.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.petheart.database.MemoryBaseHelper;
import com.example.petheart.database.MemoryCursorWrapper;
import com.example.petheart.database.MemoryDbSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MemoryList {
    private static MemoryList sMemoryList;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static MemoryList get(Context context) {
        if(sMemoryList == null) {
            sMemoryList = new MemoryList(context);
        }

        return sMemoryList;
    }

    private MemoryList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new MemoryBaseHelper(mContext).getWritableDatabase();
    }

    public List<Memory> getMemories() {

        List<Memory> memories = new ArrayList<>();
        MemoryCursorWrapper cursor = queryMemories(null, null);

        try {

            cursor.moveToFirst();

            while(!cursor.isAfterLast()) {
                memories.add(cursor.getMemory());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return memories;
    }

    public void addMemory(Memory m) {
        ContentValues values = getContentValues(m);
        mDatabase.insert(MemoryDbSchema.MemoryTable.NAME, null, values);
    }


    public void deleteMemory(Memory m) {
        String uuidString = m.getId().toString();
        mDatabase.delete(MemoryDbSchema.MemoryTable.NAME,
                MemoryDbSchema.MemoryTable.Cols.UUID + " =?",
                new String[] { uuidString });
    }


    public Memory getMemory(UUID id) {
        MemoryCursorWrapper cursor = queryMemories(MemoryDbSchema.MemoryTable.Cols.UUID + " =?",
                new String[] { id.toString()});
        try {
            if(cursor.getCount() == 0) {
                    return null;
            }

            cursor.moveToFirst();
            return cursor.getMemory();
        }
        finally {
            cursor.close();
        }
    }

    public void updateMemory(Memory memory) {
        String uuidString = memory.getId().toString();
        ContentValues values = getContentValues(memory);

        mDatabase.update(MemoryDbSchema.MemoryTable.NAME, values,
                MemoryDbSchema.MemoryTable.Cols.UUID + " =?",
                new String[] { uuidString });
    }

    private static ContentValues getContentValues(Memory memory) {
        ContentValues values = new ContentValues();
        values.put(MemoryDbSchema.MemoryTable.Cols.UUID, memory.getId().toString());
        values.put(MemoryDbSchema.MemoryTable.Cols.TITLE, memory.getTitle());
        values.put(MemoryDbSchema.MemoryTable.Cols.DATE, memory.getDate().getTime());
        values.put(MemoryDbSchema.MemoryTable.Cols.FAVORITE, memory.isFavorite() ? 1 : 0);
        values.put(MemoryDbSchema.MemoryTable.Cols.DESCRIPTION, memory.getDescription());

        return values;
    }

    private MemoryCursorWrapper queryMemories(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(MemoryDbSchema.MemoryTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        return new MemoryCursorWrapper(cursor);
    }
}


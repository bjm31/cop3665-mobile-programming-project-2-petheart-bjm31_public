package com.example.petheart.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.petheart.Models.Memory;

import java.util.Date;
import java.util.UUID;

public class MemoryCursorWrapper extends CursorWrapper {

    public MemoryCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Memory getMemory()
    {
        String uuidString = getString(getColumnIndex(MemoryDbSchema.MemoryTable.Cols.UUID));
        String title = getString(getColumnIndex(MemoryDbSchema.MemoryTable.Cols.TITLE));
        long date = getLong(getColumnIndex(MemoryDbSchema.MemoryTable.Cols.DATE));

        Memory memory = new Memory(UUID.fromString(uuidString));
        memory.setTitle(title);
        memory.setDate(new Date(date));

        return memory;

    }
}
package com.example.tripbuddy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.tripbuddy.models.Memory;

import java.util.ArrayList;

public class MemoryDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "tripbuddy.db";
    private static final int DB_VERSION = 2;
    private static final String TABLE_MEMORIES = "memories";
    private static final String COL_ID = "id";
    private static final String COL_PHOTO_URI = "photo_uri";
    private static final String COL_AUDIO_ID = "audio_id";
    private static final String COL_DESTINATION = "destination";
    private static final String COL_START_DATE = "start_date";
    private static final String COL_END_DATE = "end_date";
    private static final String COL_NOTES = "notes";
    private static final String COL_MOOD = "mood";
    private static final String COL_BGM = "bgm";

    public MemoryDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_MEMORIES + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_PHOTO_URI + " TEXT," +
                COL_AUDIO_ID + " INTEGER," +
                COL_DESTINATION + " TEXT," +
                COL_START_DATE + " TEXT," +
                COL_END_DATE + " TEXT," +
                COL_NOTES + " TEXT," +
                COL_MOOD + " TEXT," +
                COL_BGM + " TEXT" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMORIES);
        onCreate(db);
    }

    public void insertMemory(Memory memory) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PHOTO_URI, memory.getPhotoUri().toString());
        values.put(COL_AUDIO_ID, memory.getAudioId());
        values.put(COL_DESTINATION, memory.getDestination());
        values.put(COL_START_DATE, memory.getStartDate());
        values.put(COL_END_DATE, memory.getEndDate());
        values.put(COL_NOTES, memory.getNotes());
        values.put(COL_MOOD, memory.getMood());
        values.put(COL_BGM, memory.getBgm());
        db.insert(TABLE_MEMORIES, null, values);
        db.close();
    }

    public ArrayList<Memory> getAllMemories() {
        ArrayList<Memory> memories = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEMORIES, null, null, null, null, null, COL_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                Uri photoUri = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHOTO_URI)));
                int audioId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_AUDIO_ID));
                String destination = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESTINATION));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_START_DATE));
                String endDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_END_DATE));
                String notes = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTES));
                String mood = cursor.getString(cursor.getColumnIndexOrThrow(COL_MOOD));
                String bgm = cursor.getString(cursor.getColumnIndexOrThrow(COL_BGM));

                memories.add(new Memory(id, photoUri, audioId, destination, startDate, endDate, notes, mood, bgm));
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return memories;
    }

    public int updateMemory(Memory memory) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PHOTO_URI, memory.getPhotoUri().toString());
        values.put(COL_AUDIO_ID, memory.getAudioId());
        values.put(COL_DESTINATION, memory.getDestination());
        values.put(COL_START_DATE, memory.getStartDate());
        values.put(COL_END_DATE, memory.getEndDate());
        values.put(COL_NOTES, memory.getNotes());
        values.put(COL_MOOD, memory.getMood());
        values.put(COL_BGM, memory.getBgm());
        return db.update(TABLE_MEMORIES, values, COL_ID + "=?", new String[]{String.valueOf(memory.getId())});
    }

    public void deleteMemory(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MEMORIES, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
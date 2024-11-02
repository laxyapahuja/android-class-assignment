package com.example.assignment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "health_wellness.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ASSESSMENTS = "assessments";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_BP = "blood_pressure";
    public static final String COLUMN_DATE = "date";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_ASSESSMENTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_WEIGHT
            + " text not null, " + COLUMN_HEIGHT
            + " text not null, " + COLUMN_BP
            + " text not null, " + COLUMN_DATE
            + " integer not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENTS);
        onCreate(db);
    }

    public List<Assessment> getAllAssessments() {
        List<Assessment> assessments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ASSESSMENTS, null, null, null, null, null, COLUMN_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                String weight = cursor.getString(cursor.getColumnIndex(COLUMN_WEIGHT));
                String height = cursor.getString(cursor.getColumnIndex(COLUMN_HEIGHT));
                String bp = cursor.getString(cursor.getColumnIndex(COLUMN_BP));
                long date = cursor.getLong(cursor.getColumnIndex(COLUMN_DATE));
                assessments.add(new Assessment(weight, height, bp, date));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return assessments;
    }
}

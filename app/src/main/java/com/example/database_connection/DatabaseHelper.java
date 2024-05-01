package com.example.database_connection;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "SecureCipherGuardian.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    private static final String TABLE_NAME = "SavedDetails";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_REF_NAME = "ref_name";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the database table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_REF_NAME + " TEXT)";
        db.execSQL(createTableQuery);
    }

    // Handle database upgrades (if needed)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertDetails(String encryptedPassword, String email, String refName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, encryptedPassword);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_REF_NAME, refName);
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public ArrayList<String> getAllSavedDetails() {
        ArrayList<String> savedDetailsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_PASSWORD, COLUMN_EMAIL, COLUMN_REF_NAME};
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String encryptedPassword = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
                String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                String refName = cursor.getString(cursor.getColumnIndex(COLUMN_REF_NAME));
                savedDetailsList.add("Encrypted Password: " + encryptedPassword + ", Email: " + email + ", Reference Name: " + refName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return savedDetailsList;
    }


}

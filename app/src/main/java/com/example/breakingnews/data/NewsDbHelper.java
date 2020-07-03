package com.example.breakingnews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.breakingnews.data.NewsContract.NewsEntry;

public class NewsDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "newsstack.db";
    public static final int DATABASE_VERSION = 1;

    private static final String TEXT_NOT_NULL = " TEXT NOT NULL";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NewsEntry.TABLE_NAME + " (" +
                    NewsEntry.COLUMN_ID + TEXT_NOT_NULL + ", " +
                    NewsEntry.COLUMN_TIME + TEXT_NOT_NULL + ", " +
                    NewsEntry.COLUMN_DESC + TEXT_NOT_NULL + ", " +
                    NewsEntry.COLUMN_URL + TEXT_NOT_NULL + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NewsEntry.TABLE_NAME;


    public NewsDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}

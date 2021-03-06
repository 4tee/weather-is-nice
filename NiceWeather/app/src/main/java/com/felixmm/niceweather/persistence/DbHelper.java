package com.felixmm.niceweather.persistence;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.felixmm.niceweather.persistence.DataStruct.WeatherTable;

public class DbHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "NiceWeatherDB";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // This is our table structure.
        // _id          INTEGER PRIMARY_KEY AUTOINCREMENT
        // date         INTEGER NOT NULL
        // description  TEXT NOT NULL
        // minTemp      REAL NOT NULL
        // maxTemp      REAL NOT NULL
        // humidity     INTEGER NOT NULL
        // weatherId    INTEGER NOT NULL
        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + WeatherTable.TABLE_NAME + " (" +
                WeatherTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WeatherTable.COL_DATE + " INTEGER NOT NULL, " +
                WeatherTable.COL_DESC + " TEXT NOT NULL, " +
                WeatherTable.COL_MIN_TEMP + " REAL NOT NULL, " +
                WeatherTable.COL_MAX_TEMP + " REAL NOT NULL, " +
                WeatherTable.COL_HUMIDITY + " INTEGER NOT NULL, " +
                WeatherTable.COL_WEATHER_ID + " INTEGER NOT NULL" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


}

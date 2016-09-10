package com.felixmm.niceweather.persistence;


import android.content.ContentValues;
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
        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + WeatherTable.TABLE_NAME + " (" +
                WeatherTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WeatherTable.COL_DATE + " INTEGER NOT NULL, " +
                WeatherTable.COL_DESC + " TEXT NOT NULL, " +
                WeatherTable.COL_MIN_TEMP + " REAL NOT NULL, " +
                WeatherTable.COL_MAX_TEMP + " REAL NOT NULL, " +
                WeatherTable.COL_HUMIDITY + " INTEGER NOT NULL" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    public int bulkInsert(ContentValues[] values) {
        final SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {
                long _id = db.insert(DataStruct.WeatherTable.TABLE_NAME, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return returnCount;
    }

    /**
     * Delete rows from the table.
     *
     * @param table the table to delete from
     * @param whereClause the optional WHERE clause to apply when deleting.
     *            Passing null will delete all rows.
     * @param whereArgs You may include ?s in the where clause, which
     *            will be replaced by the values from whereArgs. The values
     *            will be bound as Strings.
     * @return the number of rows affected if a whereClause is passed in, 0
     *         otherwise. To remove all rows and get a count pass "1" as the
     *         whereClause.
     */
    public int deleteRows(String table, String whereClause, String[] whereArgs) {
        final SQLiteDatabase db = getWritableDatabase();

        int rowAffected = db.delete(table, whereClause, whereArgs);
        db.close();
        return rowAffected;
    }
}

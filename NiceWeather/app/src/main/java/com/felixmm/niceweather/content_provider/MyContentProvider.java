package com.felixmm.niceweather.content_provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.felixmm.niceweather.persistence.DataStruct;
import com.felixmm.niceweather.persistence.DbHelper;


public class MyContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int WEATHER = 1; //list
    private static final int WEATHER_DATE = 2;

    private DbHelper dbHelper;

    private static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String cpAuthority = DataStruct.CONTENT_PROVIDER_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(cpAuthority, DataStruct.PATH_WEATHER, WEATHER);
        matcher.addURI(cpAuthority, DataStruct.PATH_WEATHER + "/*", WEATHER_DATE);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor cursor;
        final int matching = sUriMatcher.match(uri);
        switch (matching) {
            case WEATHER: {
                cursor = dbHelper.getReadableDatabase().query(
                        DataStruct.WeatherTable.TABLE_NAME,
                        columns,
                        selection,
                        selectionArgs,
                        null, // group by
                        null, // having
                        sortOrder
                );
                break;
            }
            case WEATHER_DATE: {

                long date = DataStruct.WeatherTable.getDateFromUri(uri);
                cursor = dbHelper.getReadableDatabase().query(
                        DataStruct.WeatherTable.TABLE_NAME,
                        columns,
                        DataStruct.WeatherTable.COL_DATE + "=?",
                        new String[] {Long.toString(date)},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default: throw new UnsupportedOperationException("Unknown URI: " +uri );
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        Log.d("MyContentProvider", "query uri:" +uri);
        return cursor;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {

        // we have two possible uri to match:
        // content://com.felixmm.niceweather/weather => List
        // content://com.felixmm.niceweather/weather/{date} => Individual
        final int matching = sUriMatcher.match(uri);
        switch (matching) {
            case WEATHER:
                return DataStruct.WeatherTable.CONTENT_TYPE;
            case WEATHER_DATE:
                return DataStruct.WeatherTable.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown URI: " +uri );
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int matching = sUriMatcher.match(uri);
        Uri returnUri;

        switch (matching) {
            case WEATHER:
            {
                long insertId = db.insert(DataStruct.WeatherTable.TABLE_NAME, null, contentValues);
                if (insertId > 0)
                    returnUri = DataStruct.WeatherTable.buildWeatherUri(insertId);
                else
                    throw new SQLiteException("Failed to insert row with uri: " + uri);
                break;
            }
            default: throw new UnsupportedOperationException("Unknown URI: " +uri );
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int matching = sUriMatcher.match(uri);
        int rowsDeleted;

        // selection = "1" will make all rows deleted and return the number of rows deleted.
        if ( null == selection ) selection = "1";
        switch (matching) {
            case WEATHER: {
                rowsDeleted = db.delete(
                        DataStruct.WeatherTable.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        // inform content resolver if there is changes
        if (rowsDeleted != 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int matching = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (matching) {
            case WEATHER: {
                rowsUpdated = db.update(DataStruct.WeatherTable.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int matching = sUriMatcher.match(uri);
        switch (matching) {
            case WEATHER:
                db.beginTransaction();
                int returnCount = 0; long lastId = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataStruct.WeatherTable.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                            lastId = _id;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                Log.d("MyContentProvider", "lastId at Bulk Insert: " + lastId);
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}

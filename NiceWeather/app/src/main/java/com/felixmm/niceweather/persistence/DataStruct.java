package com.felixmm.niceweather.persistence;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DataStruct {

    public static final String CONTENT_PROVIDER_AUTHORITY = "com.felixmm.niceweather";
    public static final String PATH_WEATHER = "WEATHER";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_PROVIDER_AUTHORITY);

    // BaseColumns contain _id & _count.
    public static final class WeatherTable implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_PROVIDER_AUTHORITY + "/" + PATH_WEATHER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_PROVIDER_AUTHORITY + "/" + PATH_WEATHER;

        public static final String TABLE_NAME = "weather";

        public static final String COL_DATE = "date";
        public static final String COL_DESC = "description";
        public static final String COL_MIN_TEMP = "minTemp";
        public static final String COL_MAX_TEMP = "maxTemp";
        public static final String COL_HUMIDITY = "humidity";
        public static final String COL_WEATHER_ID = "weatherId";

        // column index for those table column
        public static final int COL_INDEX_ID = 0;
        public static final int COL_INDEX_DATE = 1;
        public static final int COL_INDEX_DESC = 2;
        public static final int COL_INDEX_MIN_TEMP = 3;
        public static final int COL_INDEX_MAX_TEMP = 4;
        public static final int COL_INDEX_HUMIDITY = 5;
        public static final int COL_INDEX_WEATHER_ID = 6;

        public static long getDateFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }

        /**
         * This will return a list of weather info.
         * @param id insertion ID
         * @return weather Uri
         */
        public static Uri buildWeatherUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /**
         * Return a URI with weather by the date.
         * @param dt the datetime value in GMT
         * @return weather Uri
         */
        public static Uri buildWeatherUriWithDate(long dt) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(dt)).build();
        }
    }
}

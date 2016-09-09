package com.felixmm.niceweather.persistence;


import android.provider.BaseColumns;

public class DataStruct {

    // BaseColumns contain _id & _count.
    public static final class WeatherTable implements BaseColumns {

        // _id          INTEGER PRIMARY_KEY AUTOINCREMENT
        // date         INTEGER NOT NULL
        // description  TEXT NOT NULL
        // minTemp      REAL NOT NULL
        // maxTemp      REAL NOT NULL
        // humidity     INTEGER NOT NULL
        public static final String TABLE_NAME = "weather";

        public static final String COL_DATE = "date";
        public static final String COL_DESC = "description";
        public static final String COL_MIN_TEMP = "minTemp";
        public static final String COL_MAX_TEMP = "maxTemp";
        public static final String COL_HUMIDITY = "humidity";

    }
}

package com.felixmm.niceweather.swissknife;


import android.database.Cursor;

import com.felixmm.niceweather.persistence.DataStruct;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SwissKnife {

    private static String dtToReadableDate(long dt) {

        // dt to weekday: Wednesday
        Date date = new Date(dt);
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE", Locale.getDefault());
        return formatter.format(date);
    }

    public static String formatUXFormat(Cursor cursor) {

        int index = cursor.getInt(DataStruct.WeatherTable.COL_INDEX_ID);
        long dt = cursor.getLong(DataStruct.WeatherTable.COL_INDEX_DATE);
        String description = cursor.getString(DataStruct.WeatherTable.COL_INDEX_DESC);
        int low = cursor.getInt(DataStruct.WeatherTable.COL_INDEX_MIN_TEMP);
        int high = cursor.getInt(DataStruct.WeatherTable.COL_INDEX_MAX_TEMP);
        int humidity = cursor.getInt(DataStruct.WeatherTable.COL_INDEX_HUMIDITY);

        return index + "; " + dtToReadableDate(dt) + ";" + description + "; Temp:" + low
                + "-" + high + "C; Hum:" + humidity + "%";

    }

}

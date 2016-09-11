package com.felixmm.niceweather.swissknife;


import android.content.Context;
import android.database.Cursor;

import com.felixmm.niceweather.R;
import com.felixmm.niceweather.persistence.DataStruct;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class is the utility class where you store your common function such as converting datetime
 * to readable date, etc.
 */
public class SwissKnife {

    /**
     * This icon selection is based on OpenWeatherMap document about weather conditions.
     * http://openweathermap.org/weather-conditions
     *
     * It is just a demo; we just use the icon directly. For production,
     * you should add your art-icon in the relevant directory, such as hdpi, mdpi, xhdpi, etc for
     * better resolution.
     *
     * @param weatherId the ID that indicate the weather condition
     * @return an reference to drawable
     */
    public static int pickWeatherIcon(int weatherId) {

        if (weatherId >= 200 && weatherId <= 232)
            return R.drawable.icon11d;
        else if (weatherId >= 300 && weatherId <= 321)
            return R.drawable.icon09d;
        else if (weatherId >= 500 && weatherId <= 504)
            return R.drawable.icon10d;
        else if (weatherId == 511)
            return R.drawable.icon13d;
        else if (weatherId >= 520 && weatherId <= 522)
            return R.drawable.icon09d;
        else if (weatherId == 531)
            return R.drawable.icon09d;
        else if (weatherId >= 600 && weatherId <= 602)
            return R.drawable.icon13d;
        else if (weatherId >= 611 && weatherId <= 612)
            return R.drawable.icon13d;
        else if (weatherId >= 615 && weatherId <= 616)
            return R.drawable.icon13d;
        else if (weatherId >= 620 && weatherId <= 622)
            return R.drawable.icon13d;
        else if (weatherId == 701)
            return R.drawable.icon50d;
        else if (weatherId == 711)
            return R.drawable.icon50d;
        else if (weatherId == 721)
            return R.drawable.icon50d;
        else if (weatherId == 731)
            return R.drawable.icon50d;
        else if (weatherId == 741)
            return R.drawable.icon50d;
        else if (weatherId == 751)
            return R.drawable.icon50d;
        else if (weatherId >= 761 && weatherId <= 762)
            return R.drawable.icon50d;
        else if (weatherId == 771)
            return R.drawable.icon50d;
        else if (weatherId == 781)
            return R.drawable.icon50d;
        else if (weatherId == 800)
            return R.drawable.icon01d;
        else if (weatherId == 801)
            return R.drawable.icon02d;
        else if (weatherId == 802)
            return R.drawable.icon03d;
        else if (weatherId >= 803 && weatherId <= 804)
            return R.drawable.icon04d;

        return -1;
    }

    public static String formatHumidity(Context context, int humidity) {
        return context.getString(R.string.humidity_percentage_formatting, humidity);
    }

    public static String formatTemp(Context context, double temp) {
        return context.getString(R.string.temp_degree_formatting, temp);
    }


    public static String dtToReadableDate(long dt) {
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

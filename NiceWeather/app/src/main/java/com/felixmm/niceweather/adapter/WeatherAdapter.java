package com.felixmm.niceweather.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.felixmm.niceweather.R;
import com.felixmm.niceweather.persistence.DataStruct;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class WeatherAdapter extends CursorAdapter {

    public WeatherAdapter(Context context, Cursor cursor, int flags) {
        super(context,cursor,flags);
    }

    /**
     * Convert dt value into readable date format
     *
     * @param dt number represents the datetime value in GMT
     * @return readable date
     */
    private String dtToReadableDate(long dt) {

        // dt to weekday: Wednesday
        Date date = new Date(dt);
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE", Locale.getDefault());
        return formatter.format(date);
    }


    /**
     * Format the row referenced by cursor to readable UX format
     * @param cursor referenced cursor to row in database
     *
     * @return a formatted UX string
     */
    private String formatUXFormat(Cursor cursor) {

        int index = cursor.getInt(DataStruct.WeatherTable.COL_INDEX_ID);
        long dt = cursor.getLong(DataStruct.WeatherTable.COL_INDEX_DATE);
        String description = cursor.getString(DataStruct.WeatherTable.COL_INDEX_DESC);
        int low = cursor.getInt(DataStruct.WeatherTable.COL_INDEX_MIN_TEMP);
        int high = cursor.getInt(DataStruct.WeatherTable.COL_INDEX_MAX_TEMP);
        int humidity = cursor.getInt(DataStruct.WeatherTable.COL_INDEX_HUMIDITY);

        return index + "; " + dtToReadableDate(dt) + ";" + description + "; Temp:" + low
                + "-" + high + "C; Hum:" + humidity + "%";

    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_weather_list_item,
                viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view.findViewById(R.id.weather_list_item_textview);

        // Find TextView and set weather forecast on it
        textView.setText(formatUXFormat(cursor));
    }
}

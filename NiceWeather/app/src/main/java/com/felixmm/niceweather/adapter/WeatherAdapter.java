package com.felixmm.niceweather.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.felixmm.niceweather.R;
import com.felixmm.niceweather.persistence.DataStruct;
import com.felixmm.niceweather.swissknife.SwissKnife;


public class WeatherAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_BIG = 0;
    private static final int VIEW_TYPE_SMALL = 1;
    private static final int VIEW_COUNT = 2;

    public WeatherAdapter(Context context, Cursor cursor, int flags) {
        super(context,cursor,flags);
    }

    public static class ViewHolder {
        public ImageView iconImageView;
        TextView minTempTextView;
        TextView maxTempTextView;
        TextView descriptionTextView;
        TextView dayTextView;
        TextView humidityTextView;

        public ViewHolder(View view) {
            iconImageView = (ImageView) view.findViewById(R.id.list_item_icon);
            minTempTextView = (TextView) view.findViewById(R.id.list_item_min_temp_textview);
            maxTempTextView = (TextView) view.findViewById(R.id.list_item_max_temp_textview);
            descriptionTextView = (TextView) view.findViewById(R.id.list_item_weather_desp_textview);
            dayTextView = (TextView) view.findViewById(R.id.list_item_day_textview);
            humidityTextView = (TextView) view.findViewById(R.id.list_item_humidity_textview);
        }

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        int type = getItemViewType(cursor.getPosition());

        int layoutId = 0;
        // first row will use big layout and small layouts for the rest.
        if (type == VIEW_TYPE_BIG) layoutId = R.layout.fragment_weather_listitem_big;
        else if (type == VIEW_TYPE_SMALL) layoutId = R.layout.fragment_weather_listitem_small;

        View view = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int type = getItemViewType(cursor.getPosition());

        double max_temp = cursor.getDouble(DataStruct.WeatherTable.COL_INDEX_MAX_TEMP);
        viewHolder.maxTempTextView.setText(SwissKnife.formatTemp(context, max_temp));

        double min_temp = cursor.getDouble(DataStruct.WeatherTable.COL_INDEX_MIN_TEMP);
        viewHolder.minTempTextView.setText(SwissKnife.formatTemp(context, min_temp));

        String day = SwissKnife.dtToReadableDate(cursor.getLong(DataStruct.WeatherTable.COL_INDEX_DATE));
        viewHolder.dayTextView.setText(day);

        if (type == VIEW_TYPE_BIG) {
            String description = cursor.getString(DataStruct.WeatherTable.COL_INDEX_DESC);
            viewHolder.descriptionTextView.setText(description);

            int humidity = cursor.getInt(DataStruct.WeatherTable.COL_INDEX_HUMIDITY);
            viewHolder.humidityTextView.setText(SwissKnife.formatHumidity(context, humidity));
        }

//
//        switch (viewType) {
//            case VIEW_TYPE_TODAY: {
//                // Get weather icon
//                viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(
//                        cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
//                break;
//            }
//            case VIEW_TYPE_FUTURE_DAY: {
//                // Get weather icon
//                viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(
//                        cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
//                break;
//            }
//        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_BIG : VIEW_TYPE_SMALL;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_COUNT;
    }
}

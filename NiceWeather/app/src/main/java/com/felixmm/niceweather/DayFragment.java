package com.felixmm.niceweather;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.felixmm.niceweather.persistence.DataStruct;
import com.felixmm.niceweather.swissknife.SwissKnife;


public class DayFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int DAY_LOADER_ID = 2;
    private static final String TAG = DayFragment.class.getSimpleName();
    private Uri mUri;

    private TextView dayTextView;
    private ImageView dayIcon;
    private TextView dayDescriptionTextView;
    private TextView dayHumidityTextView;
    private TextView dayMinTempTextView;
    private TextView dayMaxTempTextView;

    public static Fragment newInstance(Bundle bundle) {
        DayFragment fragment = new DayFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            mUri = getArguments().getParcelable(DayActivity.DAY_CONTENT_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_day_weather, container, false);
        dayTextView = (TextView) rootView.findViewById(R.id.day_textview);
        dayIcon = (ImageView) rootView.findViewById(R.id.day_icon);
        dayDescriptionTextView = (TextView) rootView.findViewById(R.id.day_weather_desp_textview);
        dayHumidityTextView = (TextView) rootView.findViewById(R.id.day_humidity_textview);
        dayMinTempTextView = (TextView) rootView.findViewById(R.id.day_min_temp_textview);
        dayMaxTempTextView = (TextView) rootView.findViewById(R.id.day_max_temp_textview);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DAY_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.d(TAG, "mUri: " + mUri.toString());
        if (mUri != null) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    null,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor != null && cursor.moveToFirst()) {

            // weather datetime in GMT format & convert it to readable weekday
            String day = SwissKnife.dtToReadableDate(cursor.getLong(DataStruct.WeatherTable.COL_INDEX_DATE));
            dayTextView.setText(day);

            // weather Id from database & convert it to drawable resource identifier
            int weatherId = cursor.getInt(DataStruct.WeatherTable.COL_INDEX_WEATHER_ID);
            int drawableResourceId = SwissKnife.pickWeatherIcon(weatherId);

            // in case the weather id cannot locate the resource
            if (drawableResourceId != -1)
                dayIcon.setImageResource(drawableResourceId);

            String description = cursor.getString(DataStruct.WeatherTable.COL_INDEX_DESC);
            dayDescriptionTextView.setText(description.toUpperCase());

            int humidity = cursor.getInt(DataStruct.WeatherTable.COL_INDEX_HUMIDITY);
            dayHumidityTextView.setText(SwissKnife.formatHumidity(getActivity(), humidity));

            double max_temp = cursor.getDouble(DataStruct.WeatherTable.COL_INDEX_MAX_TEMP);
            String maxStr = "Max: " + SwissKnife.formatTemp(getActivity(), max_temp) + "C";
            dayMaxTempTextView.setText(maxStr);

            // weather min temperature from database
            double min_temp = cursor.getDouble(DataStruct.WeatherTable.COL_INDEX_MIN_TEMP);
            String minStr = "Min: " + SwissKnife.formatTemp(getActivity(), min_temp) + "C";
            dayMinTempTextView.setText(minStr);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Nothing to do here
    }
}

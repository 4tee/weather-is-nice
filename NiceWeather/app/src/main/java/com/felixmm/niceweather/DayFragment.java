package com.felixmm.niceweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DayFragment extends Fragment{

    public static Fragment newInstance(Bundle bundle) {
        DayFragment fragment = new DayFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_day_weather, container, false);
        TextView textView = (TextView)rootView.findViewById(R.id.textView);

        Intent intent = getActivity().getIntent();
        if (null != intent) {
            String str = intent.getStringExtra(MainActivity.DAY_INTENT_KEY);
            textView.setText(str);
        }

        return rootView;
    }
}

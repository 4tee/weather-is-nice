package com.felixmm.niceweather;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class DayActivity extends AppCompatActivity{

    public static final String DAY_CONTENT_URI = "DAY_CONTENT_URI";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_activity_container);

        loadDayFragment();
    }

    private void loadDayFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(DAY_CONTENT_URI, getIntent().getData());

        Fragment dayFragment = DayFragment.newInstance(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.dayContainer, dayFragment);
        ft.commit();
    }
}

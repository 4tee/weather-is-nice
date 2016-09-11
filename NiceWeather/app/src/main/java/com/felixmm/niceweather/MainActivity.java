package com.felixmm.niceweather;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener {

    public static final String LOCATION_KEY = "location";

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOCATION_ENABLE_CODE = 100;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final long LOCATION_UPDATE_INTERVAL_MILLISECONDS = 100000;
    private static final long FASTEST_UPDATE_INTERVAL_MILLISECONDS = 2000;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private boolean isFirstLocation = true;
    private LocationManager locationManager;

    private void loadWeatherListFragment(Location mLocation) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(LOCATION_KEY, mLocation);
        Fragment weatherListFragment = WeatherListFragment.newInstance(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainContainer, weatherListFragment);
        //ft.commit();
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOCATION_ENABLE_CODE) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                findViewById(R.id.layout_location).setVisibility(View.INVISIBLE);
                startLocationService();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_container);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        if ( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            findViewById(R.id.layout_location).setVisibility(View.VISIBLE);
            findViewById(R.id.button_location).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                       LOCATION_ENABLE_CODE);
                }
            });

        } else {
            startLocationService();
        }
    }

    private void startLocationService() {
        if (!checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermission();
        } else {
            buildGoogleApiClient();
            makeLocationRequest();
            loadWeatherListFragment(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient!= null && mGoogleApiClient.isConnected())
            startUpdatingLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient!=null && mGoogleApiClient.isConnected())
            stopUpdatingLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }


    /**
     * Start location service.
     */
    protected void startUpdatingLocation() {
        if (!checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermission();
            return;
        }

        Log.i(TAG, "startUpdatingLocation");
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                locationRequest,
                this
        );
    }

    /**
     * Stop location service.
     */
    protected void stopUpdatingLocation() {
        Log.i(TAG, "stopUpdatingLocation");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    /**
     * Construction of Location Request with its own settings.
     * Fastest update at 2 seconds; this is more suitable for accurate location tracking.
     */
    protected void makeLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(LOCATION_UPDATE_INTERVAL_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Build GoogleApiClient with LocationService API
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    private boolean checkPermission(String permission) {
        int result = ActivityCompat.checkSelfPermission(this, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                }, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startLocationService();

                } else {

                    Snackbar.make(
                            this.findViewById(R.id.mainContainer),
                            "Permission Denied, Please allow to proceed!",
                            Snackbar.LENGTH_LONG)
                            .show();
                }
                break;
        }
    }

    /**
     * Run when the GoogleApiClient object is connected.
     *
     * @param bundle Bundle of data provided to clients by Google Play services.
     *               May be null if no content is provided by the service.
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.i(TAG, "onConnected");
        if (!checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermission();
            return;
        }

        // start updating Location
        startUpdatingLocation();
    }

    /**
     * Call when the client is temporarily in a disconnected state.
     * @param cause the reason for disconnection
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "onConnectionSuspended");

        // for some reasons, in case the connection drop, reconnect.
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // connection failed
        Log.d(TAG, "connection failed: " + connectionResult.getErrorMessage());
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged");
        if (isFirstLocation) {

            isFirstLocation = false;
            loadWeatherListFragment(location);
        }
    }


}

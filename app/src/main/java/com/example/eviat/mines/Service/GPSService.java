package com.example.eviat.mines.Service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

public class GPSService extends Service implements LocationListener {

    private static final int COUNTER_DELAY = 2;
    private static final int FIRST_DELAY = 1*1000;
    private static final int SECOND_DELAY = 20*1000;
    private final IBinder mBinder = new GPSService.LocalBinder();
    private GPSListener mGPSListener;
    private LocationManager mLocationManager;
    private Location mGPS;
    private double mLng;
    private double mLat;
    private Handler handler;
    private boolean flag = true;
    private int counter = 0;
    private int delay = FIRST_DELAY;

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    public interface GPSListener {

        void getLatLng(double lat, double lng);

    }

    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler();

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        GPSTrack();

        handler.post(delaySensors);
    }

    public void GPSTrack() {

        boolean gpsProvider = mLocationManager.isProviderEnabled(mLocationManager.GPS_PROVIDER);
        boolean networkProvider = mLocationManager.isProviderEnabled(mLocationManager.NETWORK_PROVIDER);

        if (!gpsProvider && !networkProvider) {
            Log.e("no gps provider", "no network provider");
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details

            Toast.makeText(getApplicationContext(), "enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return;
        }

        if (mLocationManager != null) {

            if (networkProvider) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 60, 10, this);
                mGPS = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (gpsProvider) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60, 10, this);
                mGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if (mGPS != null) {
                mLat = mGPS.getLatitude();
                mLng = mGPS.getLongitude();
            }
        } else
            Log.e("Manager", "NULL");


        if (mGPSListener != null)
            mGPSListener.getLatLng(mLat, mLng);

    }

    private final Runnable delaySensors = new Runnable() {
        @Override
        public void run() {

            if (flag) {
                counter++;
                if (counter == COUNTER_DELAY) {
                    flag = false;
                    delay = SECOND_DELAY;
                }
            }

            GPSTrack();
            // The Runnable is posted to run again here:
            handler.postDelayed(this, delay);
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public boolean onUnbind(Intent intent) {
        handler.removeCallbacks(delaySensors);
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {

        public void registerListener(GPSService.GPSListener listener) {
            mGPSListener = listener;
        }
    }
}

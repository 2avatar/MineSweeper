package com.example.eviat.mines.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class MotionService extends Service implements SensorEventListener{

    private final IBinder mBinder = new LocalBinder();
    private MotionListener mMotionListener;
    private Sensor mAccSensor;
    private Sensor mMagSensor;
    private SensorManager mSensorManager;
    private Handler handler;
    private boolean flag = false;
    private boolean firstTime = true;
    private int secondTimeCounter = 0;
    private final int delay = 2000;
    private float[] mAccValues = new float[3];
    private float[] mMagValues = new float[3];
    private float[] mRotation = new float[9];
    private float[] mOrientation = new float[3];
    private float[] mFirstOrientation = new float[3];
    private final float deflection = 0.2f;

    public interface MotionListener {

        void didMotion();

    }

    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null &&
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

            mMagSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            mSensorManager.registerListener(this, mMagSensor, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, mAccSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        handler.post(delaySensors);
    }

    // set delay for sensors sampling
    private final Runnable delaySensors = new Runnable() {
        @Override
        public void run() {

            flag = true;

            // The Runnable is posted to run again here:
            handler.postDelayed(this, delay);
        }
    };

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            System.arraycopy(sensorEvent.values, 0, mMagValues, 0, sensorEvent.values.length);
        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            System.arraycopy(sensorEvent.values, 0, mAccValues, 0, sensorEvent.values.length);
        }

        if (flag) {

            // sampling for the first time on the second sample because the first sample is trash
            if (firstTime){

                secondTimeCounter++;

                if (secondTimeCounter == 3) {
                    firstTime = false;
                    SensorManager.getRotationMatrix(mRotation, null, mAccValues, mMagValues);
                    SensorManager.getOrientation(mRotation, mFirstOrientation);
                }
            }

            SensorManager.getRotationMatrix(mRotation, null, mAccValues, mMagValues);
            SensorManager.getOrientation(mRotation, mOrientation);

            if (    mOrientation[0] >  mFirstOrientation[0] + deflection  ||
                    mOrientation[0] <  mFirstOrientation[0] - deflection  ||
                    mOrientation[1] >  mFirstOrientation[1] + deflection  ||
                    mOrientation[1] <  mFirstOrientation[1] - deflection  ||
                    mOrientation[2] >  mFirstOrientation[2] + deflection  ||
                    mOrientation[2] <  mFirstOrientation[2] - deflection
                    ) {

                if ((mMotionListener != null) && (firstTime == false))
                    mMotionListener.didMotion();
            }
            flag = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    public boolean onUnbind(Intent intent) {
        handler.removeCallbacks(delaySensors);
        mSensorManager.unregisterListener(this);
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {

        public void registerListener (MotionListener listener){
            mMotionListener = listener;
        }
    }
}


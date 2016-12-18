package jstudio.com.glider.sensors;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import jstudio.com.glider.R;
import android.widget.TextView;


/**
 * Created by Janek on 2016-12-17.
 */

public class PressureListener implements SensorEventListener {

    private TextView pressureValue;
    private TextView altitudeValue;
    private TextView climbSpeedValue;

    private Activity activity;

    public PressureListener(Activity activity) {
        this.activity = activity;
        pressureValue = (TextView) activity.findViewById(R.id.pressureValueText);
        altitudeValue = (TextView) activity.findViewById(R.id.heightValueText);
        climbSpeedValue = (TextView) activity.findViewById(R.id.climbSpeedValueText);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        currentPressure = event.values[0];
        String pressure = String.format("%.2f",currentPressure);
        pressureValue.setText(pressure);
        curAlt = SensorManager.getAltitude(seaLevelPressure,currentPressure);
        String altitude = String.format("%.1f",curAlt);
        altitudeValue.setText(altitude);
        calculateVSpeed();
    }

    private double lastAlt = 0;
    private long lastTime = System.currentTimeMillis();
    private double curAlt = 0;
    private long curTime = 0;
    private float seaLevelPressure = 1033;
    private float currentPressure;

    private void calculateVSpeed() {
        curTime = System.currentTimeMillis();
        if(curTime-1000<lastTime) {
            return;
        }
        double speed = ((curAlt - lastAlt)/((curTime - lastTime)/1000));
        lastAlt = curAlt;
        lastTime = curTime;
        String vspeed = String.format("%.1f", speed);
        climbSpeedValue.setText(vspeed);
    }
}

package jstudio.com.glider.sensors;

import android.location.Location;
import android.location.LocationListener;
import jstudio.com.glider.MapsActivity;
import jstudio.com.glider.R;
import jstudio.com.glider.drawing.FileIO;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.mapsforge.core.model.LatLong;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GpsLocationListener implements LocationListener {
    private double lastAlt = 0;
    private long lastTime = 0;
    private double curAlt = 0;
    private long curTime = 0;
    private LatLong latLong;

    private TextView speedValue;
    private TextView pressureValue;
    private TextView altitudeValue;
    private TextView climbSpeedValue;
    private ImageView glider;

    private float bearing;
    private boolean barometer;
    private boolean isPressed = false;

    private MapsActivity activity;
    private ArrayList<String> coordinatesList;

    public GpsLocationListener(MapsActivity activity, boolean barometer) {
        this.activity = activity;
        this.barometer = barometer;
        speedValue = (TextView) activity.findViewById(R.id.speedValueText);
        glider = activity.glider;
        if(!barometer) {
            pressureValue = (TextView) activity.findViewById(R.id.pressureValueText);
            altitudeValue = (TextView) activity.findViewById(R.id.heightValueText);
            climbSpeedValue = (TextView) activity.findViewById(R.id.climbSpeedValueText);
            pressureValue.setText("N/A");
        }
        coordinatesList = new ArrayList<>();
    }

    @Override
    public void onLocationChanged(Location loc) {
        String longitude = "Longitude: " + loc.getLongitude();
        String latitude = "Latitude: " + loc.getLatitude();
        String altitude = String.format("%.1f", loc.getAltitude());

        //float b;
        String speed = String.format("%.1f", loc.getSpeed() * 3.6);
        if (loc.hasBearing()) {
            bearing = loc.getBearing();
        }
        //String bearing = "Bearing: " + b;
        String dir = "";
        String vspeed = String.format("%.1f", calculateVSpeed(loc.getAltitude()));
        speedValue.setText(speed);

        glider.setRotation(bearing);

        latLong = new LatLong(loc.getLatitude(),loc.getLongitude());

        if(loc.getSpeed()>0.3) {
            this.activity.setCenter(latLong);
        }

        if(!barometer) {
            altitudeValue.setText(altitude);
            climbSpeedValue.setText(vspeed);
        }
        recordingData(loc);
    }

    private double calculateVSpeed(double d) {
        lastAlt = curAlt;
        lastTime = curTime;
        curAlt = d;
        curTime = System.currentTimeMillis();
        return ((curAlt - lastAlt) / ((curTime - lastTime) / 1000));
    }

    public void recordingData(Location loc){
        if(isPressed == true){
            if(coordinatesList.isEmpty()){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date d = new Date();
                String date = sdf.format(d);
                coordinatesList.add(date);
            }
            coordinatesList.add(loc.getLatitude() + ", " + loc.getLongitude());
            Log.i("Recording Coordinates", "text : "+ loc.getLatitude() + ", " + loc.getLongitude() +" : end");

        }else{
            if(!coordinatesList.isEmpty()){
                FileIO fileIO = new FileIO();
                fileIO.writeDataToFile(coordinatesList);
                coordinatesList.clear();
            }
        }
    }

    public void rocordingButtonPressed(boolean press){
        isPressed = press;
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider,
                                int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}
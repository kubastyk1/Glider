package jstudio.com.glider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;

import jstudio.com.glider.sensors.GpsLocationListener;
import jstudio.com.glider.sensors.PressureButtonListener;
import jstudio.com.glider.sensors.PressureListener;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.util.MapViewerTemplate;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;


/**
 * The simplest form of creating a map viewer based on the MapViewerTemplate.
 * It also demonstrates the use simplified cleanup operation at activity exit.
 */
public class MapsActivity extends MapViewerTemplate{

    /**
     * This MapViewer uses the built-in Osmarender theme.
     *
     * @return the render theme to use
     */
    @Override
    protected XmlRenderTheme getRenderTheme() {
        return InternalRenderTheme.OSMARENDER;
    }

    /**
     * This MapViewer uses the standard xml layout in the Samples app.
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * The id of the mapview inside the layout.
     *
     * @return the id of the MapView inside the layout.
     */
    @Override
    protected int getMapViewId() {
        return R.id.mapView;
    }

    /**
     * The name of the map file.
     *
     * @return map file name
     */
    @Override
    protected String getMapFileName() {
        return "germany.map";
    }

    /**
     * Creates a simple tile renderer layer with the AndroidUtil helper.
     */
    @Override
    protected void createLayers() {
        TileRendererLayer tileRendererLayer = AndroidUtil.createTileRendererLayer(this.tileCaches.get(0),
                this.mapView.getModel().mapViewPosition, getMapFile(), getRenderTheme(), false, true, false);
        this.mapView.getLayerManager().getLayers().add(tileRendererLayer);
    }

    @Override
    protected void createMapViews() {
        super.createMapViews();

        mapView.setBuiltInZoomControls(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            boolean hardwareAcceleration = sharedPreferences.getBoolean("", true);
            if (!hardwareAcceleration) {
                mapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        }
    }

    /**
     * Creates the tile cache with the AndroidUtil helper
     */
    @Override
    protected void createTileCaches() {
        this.tileCaches.add(AndroidUtil.createTileCache(this, getPersistableId(),
                this.mapView.getModel().displayModel.getTileSize(), this.getScreenRatio(),
                this.mapView.getModel().frameBufferModel.getOverdrawFactor()));
    }

    private LocationManager locationManager;
    private static GpsLocationListener gpsLocationListener;
    private SensorManager sensorManager;
    private SensorEventListener sensorListener;
    private Sensor sensor;
    public ImageView glider;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    public void setCenter(LatLong latLong) {
        this.mapView.getModel().mapViewPosition.animateTo(latLong);
    }

    private void locationManagerInit() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsLocationListener = new GpsLocationListener(this, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, gpsLocationListener);
    }

    private void pressureListenerInit() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensorListener = new PressureListener(this);
        sensorManager.registerListener(sensorListener, sensor, 5000000);
    }

    private void sensorsInit() {
        boolean barometer;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensorListener = new PressureListener(this);
        barometer = sensorManager.registerListener(sensorListener, sensor, 5000000);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsLocationListener = new GpsLocationListener(this, barometer);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, gpsLocationListener);
    }

    public void showPressureMenu(View view){
        PressureButtonListener buttonListener = new PressureButtonListener(this);
        buttonListener.showPopupMenu(view);
    }

    private void addDrawerItems() {

        String[] osArray = { "Home","Navigate to", "History", "Settings"};
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                        MapsActivity.this.startActivity(new Intent(MapsActivity.this, MapsActivity.class));
                        break;
                    case 1:

                        break;
                    case 2:
                        MapsActivity.this.startActivity(new Intent(MapsActivity.this, HistoryActivity.class));
                        break;
                    case 3:

                        break;
                    default:
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidGraphicFactory.createInstance(this.getApplication());
        super.onCreate(savedInstanceState);
        setTitle(getClass().getSimpleName());

        glider = (ImageView) findViewById(R.id.imageViewGlider);

        mDrawerList = (ListView) findViewById(R.id.navList);
        addDrawerItems();
        sensorsInit();
    }

}

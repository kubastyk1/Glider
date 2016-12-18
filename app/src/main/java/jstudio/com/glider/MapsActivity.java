package jstudio.com.glider;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import jstudio.com.glider.sensors.GpsLocationListener;
import jstudio.com.glider.sensors.PressureListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.util.MapViewerTemplate;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;


import java.io.File;


/**
 * The simplest form of creating a map viewer based on the MapViewerTemplate.
 * It also demonstrates the use simplified cleanup operation at activity exit.
 */
public class MapsActivity extends MapViewerTemplate {

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidGraphicFactory.createInstance(this.getApplication());
        super.onCreate(savedInstanceState);
        setTitle(getClass().getSimpleName());
    }
}

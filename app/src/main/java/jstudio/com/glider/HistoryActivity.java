package jstudio.com.glider;

import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.util.MapViewerTemplate;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jstudio.com.glider.drawing.RouteDrawing;
import jstudio.com.glider.drawing.FileIO;

public class HistoryActivity extends MapViewerTemplate {

    @Override
    protected XmlRenderTheme getRenderTheme() {
        return InternalRenderTheme.OSMARENDER;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    protected int getMapViewId() {
        return R.id.mapView;
    }

    @Override
    protected String getMapFileName() {
        return "germany.map";
    }

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

    @Override
    protected void createTileCaches() {
        this.tileCaches.add(AndroidUtil.createTileCache(this, getPersistableId(),
                this.mapView.getModel().displayModel.getTileSize(), this.getScreenRatio(),
                this.mapView.getModel().frameBufferModel.getOverdrawFactor()));
    }

    /* OnCreate Zone */

    private String textFileName = "gliderApp.txt";
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    Map<String, ArrayList> coordinatesMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidGraphicFactory.createInstance(this.getApplication());
        super.onCreate(savedInstanceState);
        setTitle(getClass().getSimpleName());

        createFileIO();
        mDrawerList = (ListView) findViewById(R.id.historyList);
        addDrawerItems();
    }

    private void createFileIO(){
        try{
            FileInputStream fileIn = openFileInput(textFileName);
            FileOutputStream fileOut = openFileOutput(textFileName, MODE_PRIVATE);
            FileIO fileIO = new FileIO(mapView, fileIn, fileOut);
            fileIO.WriteDataToFile();
            coordinatesMap = fileIO.ReadDataFromFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addDrawerItems() {

        final ArrayList<RouteDrawing> drawArray = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();

        for (Map.Entry<String,ArrayList> entry : coordinatesMap.entrySet()) {
            dateList.add(entry.getKey());
        }
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dateList);
        mDrawerList.setAdapter(mAdapter);

        for(String str: dateList){
            ArrayList coordinatesArray = coordinatesMap.get(str);
            RouteDrawing routeDrawing = new RouteDrawing();
            routeDrawing.drawLines(mapView, coordinatesArray);
            routeDrawing.hideLine();
            drawArray.add(routeDrawing);
        }

                mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for(RouteDrawing routeDrawing : drawArray){
                    routeDrawing.hideLine();
                }
                drawArray.get(position).showLine();
            }
        });
    }
}

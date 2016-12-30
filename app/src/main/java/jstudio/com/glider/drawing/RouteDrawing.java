package jstudio.com.glider.drawing;

import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.overlay.Polyline;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouteDrawing {

    private Polyline polyline;

    public RouteDrawing(){
        Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(6);
        paint.setStyle(Style.STROKE);

        polyline = new Polyline(paint, AndroidGraphicFactory.INSTANCE);
    }

    public void drawLines(MapView mapView,  ArrayList<LatLong> coordinates){

        List<LatLong> coordinateList = polyline.getLatLongs();
        coordinateList.addAll(coordinates);

        mapView.getLayerManager().getLayers().add(polyline);
    }

    public void showLine(){
        polyline.setVisible(true);
    }

    public void hideLine(){
        polyline.setVisible(false);
    }


    public ArrayList<String> getSampleValues(){

        double d = 0;
        ArrayList<String> dateList = new ArrayList<>();
        dateList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(102,7,23,02,15)));
        dateList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(107,5,21,19,55)));
        dateList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(116,1,03,12,25)));
        ArrayList<LatLong> coordinatesList = new ArrayList<>();
        coordinatesList.add(new LatLong(51.709268, 19.481249));
        coordinatesList.add(new LatLong(51.727879, 19.498415));
        coordinatesList.add(new LatLong(51.752646, 19.530344));
        coordinatesList.add(new LatLong(51.76019, 19.466143));
        coordinatesList.add(new LatLong(51.749883, 19.449835));

        ArrayList<String> coordinatesStringList = new ArrayList<>();
        for(String date: dateList){
            coordinatesStringList.add(date.toString());
            for(LatLong latLong: coordinatesList) {
                Double latX = latLong.getLatitude() + d;
                Double latY = latLong.getLongitude() - d;
                coordinatesStringList.add(latX.toString() + ", " + latY.toString());
            }
            d += 0.03;
        }
        return coordinatesStringList;
    }

}

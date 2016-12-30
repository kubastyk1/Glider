package jstudio.com.glider.drawing;

import android.util.Log;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.view.MapView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileIO {

    private FileInputStream fileIn;
    private FileOutputStream fileOut;
    private MapView mapView;

    public FileIO(MapView mapView, FileInputStream fileIn, FileOutputStream fileOut){
        this.mapView = mapView;
        this.fileIn = fileIn;
        this.fileOut = fileOut;
    }

    public void WriteDataToFile() {
        try {
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);
            RouteDrawing routeDrawing = new RouteDrawing();
            ArrayList<String> coordinatesList = routeDrawing.getSampleValues();
            for(String str: coordinatesList){
                outputWriter.write(str + "\n");
            }
            outputWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, ArrayList> ReadDataFromFile() {

        Map<String, ArrayList> coordinatesMap = new LinkedHashMap<>();
        ArrayList<LatLong> coordinatesList = new ArrayList<>();
        try {
            InputStreamReader inputRead= new InputStreamReader(fileIn);
            BufferedReader reader = new BufferedReader(inputRead);

            boolean isFirstElement = true;
            String mLine;
            String date = null;
            String[] parts;

            while ((mLine = reader.readLine()) != null) {
                if(mLine.contains("-")){
                    if(!isFirstElement){
                        ArrayList<LatLong> helperList = new ArrayList<>();
                        helperList.addAll(coordinatesList);
                        coordinatesMap.put(date, helperList);
                        coordinatesList.removeAll(helperList);
                    }
                    date = mLine;
                    isFirstElement = false;
                }else{
                    parts = mLine.split(",");
                    coordinatesList.add(new LatLong(Double.parseDouble(parts[0]), Double.parseDouble(parts[1])));
                }
            }
            coordinatesMap.put(date, coordinatesList);
            for (Map.Entry<String,ArrayList> entry : coordinatesMap.entrySet()) {
                Log.i("Maping Test", "text : "+ entry.getKey() +" -- " +entry.getValue() +" : end");
            }
            inputRead.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return coordinatesMap;
    }
}

package jstudio.com.glider.drawing;

import android.os.Environment;
import android.util.Log;

import org.mapsforge.core.model.LatLong;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileIO {

    private File file;

    public FileIO(){
        file = new File(Environment.getExternalStorageDirectory(), "GliderApp.txt");
    }

    public void writeDataToFile(ArrayList<String> coordinatesList) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file, true);
            for(String str: coordinatesList){
                outputStream.write((str + "\n").getBytes());
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, ArrayList> readDataFromFile() {

        Map<String, ArrayList> coordinatesMap = new LinkedHashMap<>();
        ArrayList<LatLong> coordinatesList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
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

        } catch (IOException e) {
            e.printStackTrace();
        }

        return coordinatesMap;
    }

    public void writeSampleValuesToFile(){

        RouteDrawing routeDrawing = new RouteDrawing();
        ArrayList<String> coordinatesList = routeDrawing.getSampleValues();
        writeDataToFile(coordinatesList);
    }

    public File getFile(){
        return file;
    }
}

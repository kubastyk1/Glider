package jstudio.com.glider.sensors;

import android.app.Activity;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import jstudio.com.glider.R;

public class PressureButtonListener {

    public Activity activity;

    public PressureButtonListener(Activity activity){
        this.activity = activity;
    }

    public void showPopupMenu(View view){

        PopupMenu popup = new PopupMenu(activity, view, Gravity.CENTER_HORIZONTAL);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Button pressureButton = (Button) activity.findViewById(R.id.pressureValueText);
                //Changing  text of pressureValueText
                //pressureButton.setText(item.getTitle());
                return true;
            }
        });
        popup.show();
    }
}

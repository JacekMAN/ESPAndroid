package paho.mqtt.android.example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Created by DOM on 13.04.2017.
 */

public class RadioaktywnoscAdapter extends ArrayAdapter<Miasto>{
    public RadioaktywnoscAdapter(Context context,List<Miasto> objects) {
        super (context, 0, objects);
    }

    int uSv=0;
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Miasto miasto = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.radioaktywnosc_miasto, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.miasto_nazwa);
        TextView tvHome = (TextView) convertView.findViewById(R.id.miasto_wartosc);
        TextView tvSensor = (TextView) convertView.findViewById(R.id.miasto_sensor2);
        // Populate the data into the template view using the data object
        tvName.setText(miasto.getName());
        tvSensor.setText (miasto.getCode ());
        if(miasto.getValue ()=="ER") {
            tvHome.setText ("Czekaj");
            miasto.odczytajWartosc (getContext (), tvHome);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}

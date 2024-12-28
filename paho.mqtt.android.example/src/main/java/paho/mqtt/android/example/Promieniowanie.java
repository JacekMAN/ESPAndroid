package paho.mqtt.android.example;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DOM on 06.04.2017.
 */


public class Promieniowanie extends AppCompatActivity {


    List<Miasto> miasta = new ArrayList<>();
    String[] kod = new String[14];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promieniowanie);

        tworzMiasta();

        RadioaktywnoscAdapter adapter = new RadioaktywnoscAdapter(this, miasta);
        ListView listView = (ListView) findViewById(R.id.promieniowanie_lista);
        listView.setAdapter(adapter);

        ImageButton button = (ImageButton) findViewById(R.id.pogoda1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FrameLayout test = new FrameLayout(this);

        int textViewExampleID = 9001;


    }

    private void tworzMiasta() {
        miasta.add(new Miasto("Tychy", "12523", "ER"));
        miasta.add(new Miasto("Ostrava", "6110", "ER"));
        miasta.add(new Miasto("Warszawa", "15877", "ER"));
        miasta.add(new Miasto("Wrocław", "470", "ER"));
        miasta.add(new Miasto("Berlin", "15909", "ER"));
        miasta.add(new Miasto("Paryz", "5313", "ER"));
        miasta.add(new Miasto("Bruksela", "15923", "ER"));
        miasta.add(new Miasto("Dortmund", "5452", "ER"));
        miasta.add(new Miasto("Praga", "15422", "ER"));
        miasta.add(new Miasto("Wiedeń", "10529", "ER"));
        miasta.add(new Miasto("Moskwa", "13606", "ER"));
        miasta.add(new Miasto("Londyn", "11785", "ER"));
        miasta.add(new Miasto("Korea Poł.", "16971", "ER"));
        miasta.add(new Miasto("Tokyo", "14386", "ER"));
        miasta.add(new Miasto("Chikago", "21178", "ER"));
        miasta.add(new Miasto("Malaga", "15617", "ER"));
    }
}

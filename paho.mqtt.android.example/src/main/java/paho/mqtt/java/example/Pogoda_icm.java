package paho.mqtt.java.example;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

/**
 * Created by DOM on 03.04.2017.
 */

public class Pogoda_icm extends AppCompatActivity {

    SharedPreferences Baza;
    SharedPreferences.Editor EBaza;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pogoda_icm);

        Baza = getSharedPreferences("jacek", Context.MODE_PRIVATE);
        EBaza = Baza.edit();

        ImageButton button = (ImageButton) findViewById(R.id.pogoda1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView mi = (TextView) findViewById(R.id.miastoy);
        mi.setText(Baza.getString("miasto","Rybnik"));


        if (Baza.getString("miasto","Rybnik").equals("Rybnik")) {

            LoadImageFromUrl("http://www.meteo.pl/um/metco/mgram_pict.php?ntype=&row=465&col=207&lang=pl", (ImageView) findViewById(R.id.ICM));
        }
        if (Baza.getString("miasto","Rybnik").equals("Cieszyn")) {

            LoadImageFromUrl("http://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&row=475&col=208&lang=pl", (ImageView) findViewById(R.id.ICM));
        }
        if (Baza.getString("miasto","Rybnik").equals("Strzelce_opolskie")) {

            LoadImageFromUrl("http://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&row=453&col=203&lang=pl", (ImageView) findViewById(R.id.ICM));
        }
        if (Baza.getString("miasto","Rybnik").equals("Wroclaw")) {

            LoadImageFromUrl("http://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&row=436&col=181&lang=pl", (ImageView) findViewById(R.id.ICM));
        }
        if (Baza.getString("miasto","Rybnik").equals("Łódź")) {

            LoadImageFromUrl("http://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&row=418&col=223&lang=pl", (ImageView) findViewById(R.id.ICM));
        }
        if (Baza.getString("miasto","Rybnik").equals("Kraków")) {

            LoadImageFromUrl("http://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&row=466&col=232&lang=pl", (ImageView) findViewById(R.id.ICM));
        }
        if (Baza.getString("miasto","Rybnik").equals("Wisła")) {

            LoadImageFromUrl("http://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&row=477&col=213&lang=pl", (ImageView) findViewById(R.id.ICM));
        }
        if (Baza.getString("miasto","Rybnik").equals("Zakopane")) {

            LoadImageFromUrl("http://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&row=487&col=232&lang=pl", (ImageView) findViewById(R.id.ICM));
        }







    }

    private void LoadImageFromUrl(String url , final ImageView nazwaView) {
        ImageView obraz = nazwaView;
        Picasso.with(this).load(url).into(obraz);
    }
}
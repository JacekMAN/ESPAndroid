package paho.mqtt.android.example;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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

            LoadImageFromUrl("https://www.meteo.pl/um/metco/mgram_pict.php?ntype=&row=465&col=207&lang=pl", (ImageView) findViewById(R.id.ICM));
        }
        if (Baza.getString("miasto","Rybnik").equals("Cieszyn")) {

            LoadImageFromUrl("https://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&row=475&col=208&lang=pl", (ImageView) findViewById(R.id.ICM));
        }
        if (Baza.getString("miasto","Rybnik").equals("Strzelce_opolskie")) {

            LoadImageFromUrl("https://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&row=453&col=203&lang=pl", (ImageView) findViewById(R.id.ICM));
        }
        if (Baza.getString("miasto","Rybnik").equals("Wroclaw")) {

            LoadImageFromUrl("https://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&row=436&col=181&lang=pl", (ImageView) findViewById(R.id.ICM));
        }
        if (Baza.getString("miasto","Rybnik").equals("Łódź")) {

            LoadImageFromUrl("https://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&row=418&col=223&lang=pl", (ImageView) findViewById(R.id.ICM));
        }
        if (Baza.getString("miasto","Rybnik").equals("Kraków")) {

            LoadImageFromUrl("https://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&row=466&col=232&lang=pl", (ImageView) findViewById(R.id.ICM));
        }
        if (Baza.getString("miasto","Rybnik").equals("Wisła")) {

            LoadImageFromUrl("https://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&row=477&col=213&lang=pl", (ImageView) findViewById(R.id.ICM));
        }
        if (Baza.getString("miasto","Rybnik").equals("Zakopane")) {

            LoadImageFromUrl("https://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&row=487&col=232&lang=pl", (ImageView) findViewById(R.id.ICM));
        }







    }

    private void LoadImageFromUrl(String url , final ImageView nazwaView) {
        ImageView obraz = nazwaView;
        Picasso.with(this).load(url).into(obraz);
    }
}
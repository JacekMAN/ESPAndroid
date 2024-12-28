package paho.mqtt.android.example;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

/**
 * Created by DOM on 23.03.2017.
 */

public class Pogoda_menu extends AppCompatActivity {
    SharedPreferences Baza;
    SharedPreferences.Editor EBaza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pogoda_menu);

        Baza = getSharedPreferences("jacek", Context.MODE_PRIVATE);
        EBaza = Baza.edit();


        ImageButton button = (ImageButton) findViewById(R.id.pogoda1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FrameLayout pogo = (FrameLayout) findViewById(R.id.rybnik2);



        pogo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EBaza.putString("miasto", "Rybnik");
                EBaza.commit();
                Intent i = new Intent(Pogoda_menu.this, Pogoda_icm.class);
                startActivity(i);
            }

        });



        pogo = (FrameLayout) findViewById(R.id.cieszyn2);
        pogo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EBaza.putString("miasto", "Cieszyn");
                EBaza.commit();
                Intent i = new Intent(Pogoda_menu.this, Pogoda_icm.class);
                startActivity(i);
            }

        });


        pogo = (FrameLayout) findViewById(R.id.strzelce2);
        pogo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EBaza.putString("miasto", "Strzelce_opolskie");
                EBaza.commit();
                Intent i = new Intent(Pogoda_menu.this, Pogoda_icm.class);
                startActivity(i);
            }

        });

        pogo = (FrameLayout) findViewById(R.id.wroc2);
        pogo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EBaza.putString("miasto", "Wroclaw");
                EBaza.commit();
                Intent i = new Intent(Pogoda_menu.this, Pogoda_icm.class);
                startActivity(i);
            }

        });

        pogo = (FrameLayout) findViewById(R.id.lodz2);
        pogo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EBaza.putString("miasto", "Łódź");
                EBaza.commit();
                Intent i = new Intent(Pogoda_menu.this, Pogoda_icm.class);
                startActivity(i);
            }

        });

        pogo = (FrameLayout) findViewById(R.id.krak2);
        pogo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EBaza.putString("miasto", "Kraków");
                EBaza.commit();
                Intent i = new Intent(Pogoda_menu.this, Pogoda_icm.class);
                startActivity(i);
            }

        });
        pogo = (FrameLayout) findViewById(R.id.wisla2);
        pogo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EBaza.putString("miasto", "Wisła");
                EBaza.commit();
                Intent i = new Intent(Pogoda_menu.this, Pogoda_icm.class);
                startActivity(i);
            }

        });

        pogo = (FrameLayout) findViewById(R.id.zakopane2);
        pogo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EBaza.putString("miasto", "Zakopane");
                EBaza.commit();
                Intent i = new Intent(Pogoda_menu.this, Pogoda_icm.class);
                startActivity(i);
            }

        });



    }



}

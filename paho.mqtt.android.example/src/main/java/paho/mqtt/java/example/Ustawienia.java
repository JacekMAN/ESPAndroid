package paho.mqtt.java.example;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by DOM on 19.02.2017.
 */

public class Ustawienia extends AppCompatActivity {

    SharedPreferences Baza;
    SharedPreferences.Editor EBaza;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ustawienia);

        final CheckBox pogoda  = (CheckBox) findViewById(R.id.pogoda1);
        final CheckBox promieniowanie  = (CheckBox) findViewById(R.id.promieniowanie1);
        final CheckBox pm  = (CheckBox) findViewById(R.id.PM101);
        final CheckBox ogrzewanie  = (CheckBox) findViewById(R.id.ogrzewanie);
        final RadioButton wybor1 = (RadioButton) findViewById(R.id.out1);
        final RadioButton wybor2 = (RadioButton) findViewById(R.id.out2);
        final RadioButton wybor3 = (RadioButton) findViewById(R.id.out3);
        final RadioButton wybor11 = (RadioButton) findViewById(R.id.ddns);
        final RadioButton wybor22= (RadioButton) findViewById(R.id.wifi);
        final RadioButton orient1 = (RadioButton) findViewById(R.id.telefon);
        final RadioButton orient2 = (RadioButton) findViewById(R.id.tablet);

        Baza = getSharedPreferences("jacek", Context.MODE_PRIVATE);
        EBaza = Baza.edit();

//        promieniowanie=Baza.getString("promieniowanie","nie");
//        pm10=Baza.getString("pm10","nie");


        if (Baza.getString("Orient","1").equals("1")) {
            orient1.setChecked(true);
        }

        if (Baza.getString("Orient","1").equals("2")) {
            orient2.setChecked(true);
        }


        if (Baza.getString("ogrzewanie","1").equals("1")){
            ogrzewanie.setChecked(true);
        }

        if (Baza.getString("pogoda","nie").equals("tak")){
            pogoda.setChecked(true);
        }
        if (Baza.getString("promieniowanie","nie").equals("tak")){
            promieniowanie.setChecked(true);
        }
        if (Baza.getString("pm10","nie").equals("tak")){
            pm.setChecked(true);
        }
        if (Baza.getString("OUT","1").equals("1")){
            wybor1.setChecked(true);
        }
        if (Baza.getString("OUT","1").equals("2")){
            wybor2.setChecked(true);
        }
        if (Baza.getString("OUT","1").equals("3")){
            wybor3.setChecked(true);
        }


        if (Baza.getString("lacze","1").equals("1")){
            wybor11.setChecked(true);
        }
        else{
            wybor22.setChecked(true);
        }



        ImageButton button = (ImageButton) findViewById(R.id.ustawienia);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zapis();

                finish();
            }
        });

        TextView button2 = (TextView) findViewById(R.id.mqtt);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j = new Intent(Ustawienia.this, Setting.class);

                startActivity(j);
            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }
    @Override
    public void  onBackPressed(){ zapis();finish();}






    private void zapis() {
        final CheckBox pogoda  = (CheckBox) findViewById(R.id.pogoda1);
        final CheckBox promieniowanie  = (CheckBox) findViewById(R.id.promieniowanie1);
        final CheckBox pm  = (CheckBox) findViewById(R.id.PM101);
        final CheckBox ogrzewanie  = (CheckBox) findViewById(R.id.ogrzewanie);

        if (pogoda.isChecked()==true){EBaza.putString("pogoda", "tak");}
        else
        {EBaza.putString("pogoda", "nie"); }
        if (promieniowanie.isChecked()==true){EBaza.putString("promieniowanie", "tak");}
        else
        {EBaza.putString("promieniowanie", "nie"); }
        if (pm.isChecked()==true){EBaza.putString("pm10", "tak");}
        else
        {EBaza.putString("pm10", "nie"); }

        if (ogrzewanie.isChecked()==false)
        {
            EBaza.putString("ogrzewanie", "0");
        }
        else
        {
            EBaza.putString("ogrzewanie", "1");
        }


        EBaza.commit();

    }

    public void selectpomiar (View view)
    {
        RadioButton wybor1 = (RadioButton) findViewById(R.id.out1);
        RadioButton wybor2 = (RadioButton) findViewById(R.id.out2);
        RadioButton wybor3 = (RadioButton) findViewById(R.id.out3);
        boolean checked = ((RadioButton) view).isChecked();
        switch ( view.getId())
        {
            case R.id.out1:
                if(checked) {
                    EBaza.putString("OUT", "1");
                    wybor1.setChecked(true);
                    wybor2.setChecked(false);
                    wybor3.setChecked(false);
                }

                break;

            case R.id.out2:
                if(checked) {
                    EBaza.putString("OUT", "2");
                    wybor2.setChecked(true);
                    wybor1.setChecked(false);
                    wybor3.setChecked(false);

                }

                break;

            case R.id.out3:
                if(checked) {
                    EBaza.putString("OUT", "3");
                    wybor3.setChecked(true);
                    wybor1.setChecked(false);
                    wybor2.setChecked(false);

                }

                break;



        }
        EBaza.commit();

    }

    public void selectorjent (View view)
    {
        RadioButton wybor1 = (RadioButton) findViewById(R.id.telefon);
        RadioButton wybor2 = (RadioButton) findViewById(R.id.tablet);
        boolean checked = ((RadioButton) view).isChecked();
        switch ( view.getId())
        {
            case R.id.telefon:
                if(checked) {
                    EBaza.putString("Orient", "1");
                    wybor1.setChecked(true);
                    wybor2.setChecked(false);

                }

                break;

            case R.id.tablet:
                if(checked) {
                    EBaza.putString("Orient", "2");
                    wybor2.setChecked(true);
                    wybor1.setChecked(false);


                }

                break;




        }
        EBaza.commit();

    }


    public void lacze (View view)
    {
        RadioButton wybor11 = (RadioButton) findViewById(R.id.ddns);
        RadioButton wybor22= (RadioButton) findViewById(R.id.wifi);
        boolean checked = ((RadioButton) view).isChecked();
        switch ( view.getId())
        {
            case R.id.ddns:
                if(checked) {
                    EBaza.putString("lacze", "1");
                    wybor11.setChecked(true);
                    wybor22.setChecked(false);
                }
                else
                {
                    EBaza.putString("lacze", "2");
                    wybor11.setChecked(false);
                    wybor22.setChecked(true);
                }
                break;

            case R.id.wifi:
                if(checked) {
                    EBaza.putString("lacze", "2");
                    wybor22.setChecked(true);
                    wybor11.setChecked(false);
                }
                else
                {
                    EBaza.putString("lacze", "1");
                    wybor22.setChecked(false);
                    wybor11.setChecked(true);
                }
                break;



        }
        EBaza.commit();

    }


}

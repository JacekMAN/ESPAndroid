package paho.mqtt.android.example;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import paho.mqtt.android.example.R;

/**
 * Created by DOM on 23.02.2017.
 */

public class Alarm extends AppCompatActivity {


    SharedPreferences Baza;
    SharedPreferences.Editor EBaza;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);

        Baza = getSharedPreferences("jacek", Context.MODE_PRIVATE);
        EBaza = Baza.edit();
         final CheckBox piece1  = (CheckBox) findViewById(R.id.piec1);
        final CheckBox piece2  = (CheckBox) findViewById(R.id.piec2);
         final EditText temp1 = (EditText) findViewById(R.id.editText1);
        final EditText temp2 = (EditText) findViewById(R.id.editText2);

        if (Baza.getString("piecMAX","nie").equals("tak")) {
           piece1.setChecked(true);
        }
        if (Baza.getString("piecMIN","nie").equals("tak")) {
            piece2.setChecked(true);
        }


        temp1.setText(Baza.getString("piecMAXv","90"));
        temp2.setText(Baza.getString("piecMINv","38"));



        ImageButton powr = (ImageButton) findViewById(R.id.powr);
        powr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zapis();
                finish();
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
        final CheckBox piece1  = (CheckBox) findViewById(R.id.piec1);
        final CheckBox piece2  = (CheckBox) findViewById(R.id.piec2);
        final EditText temp1 = (EditText) findViewById(R.id.editText1);
        final EditText temp2 = (EditText) findViewById(R.id.editText2);

        if (piece1.isChecked()==true){EBaza.putString("piecMAX", "tak");}
        else
        {EBaza.putString("piecMAX", "nie"); }

        if (piece2.isChecked()==true){EBaza.putString("piecMIN", "tak");}
        else
        {EBaza.putString("piecMIN", "nie"); }

        EBaza.putString("piecMAXv", String.valueOf(temp1.getText()));

        EBaza.putString("piecMINv", String.valueOf(temp2.getText()));
        EBaza.commit();

    }


}


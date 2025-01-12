package paho.mqtt.android.example;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;


/**
 * Created by DOM on 11.01.2017.
 */



public class Setting extends Activity {


    ProgressBar woda;
    SharedPreferences Baza;
    SharedPreferences.Editor EBaza;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        Baza = getSharedPreferences("jacek",Context.MODE_PRIVATE);
        EBaza=Baza.edit();

        final EditText temp1 = (EditText) findViewById(R.id.editText1);
        temp1.setText(Baza.getString("local",""));

        final EditText temp2 = (EditText) findViewById(R.id.editText2);
        temp2.setText(Baza.getString("DNS1",""));

        final TextView temp22 = (TextView) findViewById(R.id.textView22);
        temp22.setText(Baza.getString("internet",""));


        final EditText temp3 = (EditText) findViewById(R.id.editText3);
        temp3.setText(Baza.getString("port",""));

        final EditText temp3a = (EditText) findViewById(R.id.editText2b);
        temp3a.setText(Baza.getString("DNS2",""));

        final EditText temp3b = (EditText) findViewById(R.id.editText2c);
        temp3b.setText(Baza.getString("DNS3",""));

        final EditText temp4 = (EditText) findViewById(R.id.editText4);
        temp4.setText(Baza.getString("nazwa",""));

        final EditText temp5 = (EditText) findViewById(R.id.editText5);
        temp5.setText(Baza.getString("login",""));

        final EditText temp6 = (EditText) findViewById(R.id.editText6);
        temp6.setText(Baza.getString("haslo",""));


        if  (Baza.getString("DNSX", "1").equals("1"))  { RadioButton wybor1 = (RadioButton) findViewById(R.id.p10);
            wybor1.setChecked(true);};

        if  (Baza.getString("DNSX", "1").equals("2"))  { RadioButton wybor1 = (RadioButton) findViewById(R.id.p11);
            wybor1.setChecked(true);};

        if  (Baza.getString("DNSX", "3").equals("3"))  { RadioButton wybor1 = (RadioButton) findViewById(R.id.p12);
            wybor1.setChecked(true);};


        ImageButton button = (ImageButton) findViewById(R.id.ustawienia);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





        ImageButton button1 = (ImageButton) findViewById(R.id.imageButton1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EBaza.putString("local", String.valueOf(temp1.getText()));
                EBaza.putString("DNS1", String.valueOf(temp2.getText()));
                EBaza.putString("DNS2", String.valueOf(temp3a.getText()));
                EBaza.putString("DNS3", String.valueOf(temp3b.getText()));
                EBaza.putString("port", String.valueOf(temp3.getText()));
                EBaza.putString("nazwa", String.valueOf(temp4.getText()));
                EBaza.putString("login", String.valueOf(temp5.getText()));
                EBaza.putString("haslo", String.valueOf(temp6.getText()));
                EBaza.commit();
                finish();

            }
        });



    }
    public void selectDNS(View view) {
        RadioButton wybor1 = (RadioButton) findViewById(R.id.p10);
        RadioButton wybor2 = (RadioButton) findViewById(R.id.p11);
        RadioButton wybor3 = (RadioButton) findViewById(R.id.p12);
        boolean checked = ((RadioButton) view).isChecked();
        int id = view.getId();
        if (checked) {
            if (id == R.id.p10) {
                wybor1.setChecked(true);
                wybor2.setChecked(false);
                wybor3.setChecked(false);
                String dns =Baza.getString("DNS1","");
                final TextView temp22 = (TextView) findViewById(R.id.textView22);
                temp22.setText(dns);EBaza.putString("internet", String.valueOf(dns));EBaza.putString("DNSX", String.valueOf("1"));
                EBaza.commit();

            } else if (id == R.id.p11) {
                wybor2.setChecked(true);
                wybor1.setChecked(false);
                wybor3.setChecked(false);
                String dns =Baza.getString("DNS2","");
                final TextView temp22 = (TextView) findViewById(R.id.textView22);
                temp22.setText(dns);EBaza.putString("internet", String.valueOf(dns));EBaza.putString("DNSX", String.valueOf("2"));
                EBaza.commit();

            } else if (id == R.id.p12) {
                wybor3.setChecked(true);
                wybor1.setChecked(false);
                wybor2.setChecked(false);
                String dns =Baza.getString("DNS3","");
                final TextView temp22 = (TextView) findViewById(R.id.textView22);
                temp22.setText(dns);EBaza.putString("internet", String.valueOf(dns));EBaza.putString("DNSX", String.valueOf("3"));
                EBaza.commit();
            }
        }
    }



}

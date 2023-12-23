package paho.mqtt.java.example;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

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
        temp2.setText(Baza.getString("internet",""));

        final EditText temp3 = (EditText) findViewById(R.id.editText3);
        temp3.setText(Baza.getString("port",""));

        final EditText temp4 = (EditText) findViewById(R.id.editText4);
        temp4.setText(Baza.getString("nazwa",""));

        final EditText temp5 = (EditText) findViewById(R.id.editText5);
        temp5.setText(Baza.getString("login",""));

        final EditText temp6 = (EditText) findViewById(R.id.editText6);
        temp6.setText(Baza.getString("haslo",""));



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
                EBaza.putString("internet", String.valueOf(temp2.getText()));
                EBaza.putString("port", String.valueOf(temp3.getText()));
                EBaza.putString("nazwa", String.valueOf(temp4.getText()));
                EBaza.putString("login", String.valueOf(temp5.getText()));
                EBaza.putString("haslo", String.valueOf(temp6.getText()));
                EBaza.commit();
                finish();
            }
        });



    }




}

package paho.mqtt.android.example;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DOM on 23.02.2017.
 */

public class Alarm extends AppCompatActivity {

    SharedPreferences Baza;
    SharedPreferences.Editor EBaza;
    String clientId = "";
    String serverUri = "";

    private void subscribeToTopic() {
        if (mqttAndroidClient == null || !mqttAndroidClient.isConnected()) {
            return;
        }

        String[] subscriptionTopics = {
                "zigbee2mqtt/Licznik",
                "S10/dane2",
                "S10/dane1",
                "S10/dane8",
                "Dane",
                "S11/dane2",
                "S11/dane1",

        };

        for (String topic : subscriptionTopics) {
            try {
                mqttAndroidClient.subscribe(topic, 1, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        android.util.Log.d("MQTT", "Subskrypcja OK: " + topic);
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        android.util.Log.e("MQTT", "Błąd subskrypcji: " + topic, exception);
                    }
                });
            } catch (org.eclipse.paho.client.mqttv3.MqttException e) {
                android.util.Log.e("MQTT", "Wyjątek podczas subskrypcji: " + topic, e);
            }
        }
    }


    MqttAndroidClient mqttAndroidClient;
    Commons commons = new Commons();

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);
        getWindow().setStatusBarColor(android.graphics.Color.BLACK);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(true);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(0);
        }


        ImageButton button = (ImageButton) findViewById(R.id.powr);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Baza = getSharedPreferences("jacek", Context.MODE_PRIVATE);
        EBaza = Baza.edit();
        //EBaza.putString("local","Ala");
        //EBaza.commit();
        final TextView rodzaj = (TextView) findViewById(R.id.textView2999);


        if (Baza.getString("lacze", "1").equals("2")) {
            clientId = (Baza.getString("nazwa", "") + commons.generujLosoweZnaki(10));
            serverUri = "tcp://" + Baza.getString("local", "") + ":" + Baza.getString("port", "");
            rodzaj.setText("lokalne");
        } else {
            clientId = (Baza.getString("nazwa", "") + commons.generujLosoweZnaki(10));
            serverUri = "tcp://" + Baza.getString("internet", "") + ":" + Baza.getString("port", "");
            EBaza.putString("awaryjne", "0");
            rodzaj.setText("zdalne");
            EBaza.commit();
        }




        ;


        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    addToHistory("Ponowne łączenie : " + serverURI);
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeToTopic();
                } else {
                    addToHistory("Połączony lokalnie");
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                addToHistory("Utracono połączenie.");


            }


            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                final String odebranyTemat = topic;
                final String odebranaWiadomosc =
                        new String(message.getPayload());

                runOnUiThread(() ->
                        dosalemWiadomosc(odebranyTemat, odebranaWiadomosc)
                );
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        String sprawdz = Baza.getString("login", "reyruyeru");
        if (sprawdz == "") sprawdz = "111";
        mqttConnectOptions.setUserName(sprawdz);
        mqttConnectOptions.setPassword(Baza.getString("haslo", "eerteerer").toCharArray());


        try {
            //addToHistory("Connecting to " + serverUri);
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory("Brak połączenia ! : " + serverUri);

                }
            });


        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }



    public void messageArrived(String topic, MqttMessage message) throws Exception {
        final String odebranyTemat = topic;
        final String odebranaWiadomosc =
                new String(message.getPayload());

        runOnUiThread(() ->
                dosalemWiadomosc(odebranyTemat, odebranaWiadomosc)
        );
    }


    public void publishMessage(String publishTopic, String publishMessage) {

        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(publishMessage.getBytes());
            mqttAndroidClient.publish(publishTopic, message);
            addToHistory("wiadomość Wysłana");
            if (!mqttAndroidClient.isConnected()) {
                addToHistory(mqttAndroidClient.getBufferedMessageCount() + " wiadomość  zbuforowana");
            }
        } catch (MqttException e) {
            System.err.println("Błąd wysyłania: " + e.getMessage());
            e.printStackTrace();
        }
    }






    private void addToHistory(String mainText) {
        System.out.println("LOG: " + mainText);
        // mAdapter.add(mainText);
        Snackbar.make(findViewById(android.R.id.content), mainText, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    //Wyswietlanie wiadomosci
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void dosalemWiadomosc(String topic, String message) {


        switch (topic) {


            case "S10/dane2":

                   TextView temp11 = (TextView) findViewById(R.id.moc);
                    double moc = new Double(message);
                    moc = moc/1000;

                    temp11.setText(String.format("   %.2f kW", moc));



                    temp11.setTextColor(Color.RED);
                    if (moc >= 5) {
                        temp11.setTextColor(Color.YELLOW);
                    }
                    if (moc < 5) {
                        temp11.setTextColor(Color.GREEN);

                    }
                    if (moc >= 10) {
                        temp11.setTextColor(Color.RED);
                    }

                break;
            case "S10/dane8":


                    TextView miesiac = (TextView) findViewById(R.id.miesiac);
                    miesiac.setText(message + "kW");



                break;

            case "S10/dane1":


                    TextView dzis = (TextView) findViewById(R.id.dzis);
                    dzis.setText(message + "kW");


                    double zmienna18 = new Double(message);
                    dzis.setTextColor(Color.RED);
                    if (zmienna18 >= 15) {
                        dzis.setTextColor(Color.YELLOW);
                    }
                    if (zmienna18 < 15) {
                        dzis.setTextColor(Color.GREEN);
                    }
                    if (zmienna18 > 20) {
                        dzis.setTextColor(Color.RED);
                    }

                break;

            case "zigbee2mqtt/Licznik":

                try {
                    JSONObject dane = new JSONObject(message);

                    // Napięcia faz
                    double voltA = dane.optDouble("voltage_a", 0);
                    double voltB = dane.optDouble("voltage_b", 0);
                    double voltC = dane.optDouble("voltage_c", 0);
                    double mocA = dane.optDouble("power_a", 0);
                    double mocB = dane.optDouble("power_b", 0);
                    double mocC = dane.optDouble("power_c", 0);

                    // Całkowita energia pobrana
                    double energia = dane.optDouble("energy", 0);

                    // Wyświetlanie napięć
                    TextView fazaA = findViewById(R.id.Media1);
                    TextView fazaB = findViewById(R.id.Media2);
                    TextView fazaC = findViewById(R.id.Media3);
                    TextView poleMocA = findViewById(R.id.MocA);
                    TextView poleMocB = findViewById(R.id.MocB);
                    TextView poleMocC = findViewById(R.id.MocC);
                    TextView licznik = findViewById(R.id.licznik);

                    poleMocA.setText(String.format(
                            java.util.Locale.US, "%.0f W", mocA));
                    if (mocA > 3000) {
                        poleMocA.setTextColor(android.graphics.Color.RED);
                    } else if (mocA >= 1000) {
                        poleMocA.setTextColor(android.graphics.Color.YELLOW);
                    } else {
                        poleMocA.setTextColor(android.graphics.Color.GREEN);
                    }

                    poleMocB.setText(String.format(
                            java.util.Locale.US, "%.0f W", mocB));
                    if (mocB > 3000) {
                        poleMocB.setTextColor(android.graphics.Color.RED);
                    } else if (mocB >= 1000) {
                        poleMocB.setTextColor(android.graphics.Color.YELLOW);
                    } else {
                        poleMocB.setTextColor(android.graphics.Color.GREEN);
                    }

                    poleMocC.setText(String.format(
                            java.util.Locale.US, "%.0f W", mocC));
                    if (mocC > 3000) {
                        poleMocC.setTextColor(android.graphics.Color.RED);
                    } else if (mocC >= 1000) {
                        poleMocC.setTextColor(android.graphics.Color.YELLOW);
                    } else {
                        poleMocC.setTextColor(android.graphics.Color.GREEN);
                    }

                    fazaA.setText(String.format(java.util.Locale.US, "%.1f V", voltA));
                    fazaB.setText(String.format(java.util.Locale.US, "%.1f V", voltB));
                    fazaC.setText(String.format(java.util.Locale.US, "%.1f V", voltC));

                    licznik.setText(String.format(java.util.Locale.US, "%.2f kWh", energia));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case "Dane":
                try {
                    JSONObject json = new JSONObject(message);

                    double ogrod = json.optDouble("ogrod", 0);
                    double ogrod2 = json.optDouble("ogrod2", 0);
                    double ogrod3 = json.optDouble("ogrod3", 0);

                    double basen1 = json.optDouble("basen1", 0);
                    double basen2 = json.optDouble("basen2", 0);
                    double basen3 = json.optDouble("basen3", 0);

                    double auto1 = json.optDouble("auto1", 0);
                    double auto2 = json.optDouble("aoto2", 0);
                    double auto3 = json.optDouble("auto3", 0);
                    double auto4 = json.optDouble("auto4", 0);
                    TextView og1 = (TextView) findViewById(R.id.pob1);
                    TextView og2 = (TextView) findViewById(R.id.pob2);
                    TextView og3 = (TextView) findViewById(R.id.pob3);
                    TextView bas1 = (TextView) findViewById(R.id.pob4);
                    TextView bas2 = (TextView) findViewById(R.id.pob5);
                    TextView bas3 = (TextView) findViewById(R.id.pob6);
                    TextView aut1 = (TextView) findViewById(R.id.au1);
                    TextView aut2 = (TextView) findViewById(R.id.au2);
                    TextView aut3 = (TextView) findViewById(R.id.au4);
                    TextView aut4 = (TextView) findViewById(R.id.au3);
                    og1.setText(String.format(java.util.Locale.US, "%.1f kWh", ogrod));
                    og2.setText(Math.round(ogrod2) + " kWh");
                    og3.setText(Math.round(ogrod3) + " W");
                    bas1.setText(String.format(java.util.Locale.US, "%.1f kWh", basen1));
                    bas2.setText(Math.round(basen2) + " kWh");
                    bas3.setText(Math.round(basen3) + " W");
                    aut1.setText(String.format(java.util.Locale.US, "%.0f kWh", auto1));
                    aut2.setText(String.format(java.util.Locale.US, "%.0f kWh", auto2));
                    aut4.setText(String.format(java.util.Locale.US, "%.0f kWh", auto4));
                    aut3.setText(Math.round(auto3) + " W");


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "S11/dane1":

                TextView temp17 = (TextView) findViewById(R.id.a3);

                temp17.setText(message);

                break;
            case "S11/dane2":

                TextView temp18 = (TextView) findViewById(R.id.c3);

                temp18.setText(message);

                break;



        }
    }


}

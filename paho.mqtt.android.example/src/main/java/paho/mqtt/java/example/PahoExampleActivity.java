/*******************************************************************************
 * Copyright (c) 1999, 2016 IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *   http://www.eclipse.org/org/documents/edl-v10.php.
 *
 */
package paho.mqtt.java.example;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

public class PahoExampleActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;


    MqttAndroidClient mqttAndroidClient;




    String clientId = "";
    final String subscriptionTopic = "#";

    int w1 = 0;
    int wynik ; // obliczenie procentowe wody
    int Swynik ; // obliczenie procentowe wody solary
    ProgressBar woda;
    SharedPreferences Baza;
    SharedPreferences.Editor EBaza;



    String serverUri = "";

    // Intent i= new Intent(PahoExampleActivity.this ,setting.class);




    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Baza = getSharedPreferences("jacek",Context.MODE_PRIVATE);
        EBaza=Baza.edit();
        //EBaza.putString("local","Ala");
        //EBaza.commit();
        clientId = Baza.getString("nazwa","");
        serverUri ="tcp://" +  Baza.getString("internet","")+":"+Baza.getString("port","");
        woda=(ProgressBar)findViewById(R.id.progressBar1);




        ImageButton button3 = (ImageButton) findViewById(R.id.imageButton4);
        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

               // publishMessage("S2/w1",""+w1);
                publishMessage("S2/w1","1");
            }

        });



       ImageButton button = (ImageButton) findViewById(R.id.imageButton2);
       button.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View view) {


               Intent i= new Intent(PahoExampleActivity.this ,setting.class);
                    startActivity(i);
           }

        });

        TextView button2 = (TextView) findViewById(R.id.textView25);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View view) {
                if (sprawdzCzyWifi() == false) {
                    ImageButton pol2 = (ImageButton) findViewById(R.id.imageButton1);
                    pol2.setColorFilter(Color.RED);
                    addToHistory("Jesteś poza zasięgiem - włącz WIFI");
                } else {
                    Intent i = new Intent(PahoExampleActivity.this, sterowanie.class);
                    startActivity(i);
                }
            }
        });






        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    addToHistory("Ponowne łączenie : " + serverURI);
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeToTopic();
                } else {
                    addToHistory("Połączony : " + serverURI);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                addToHistory("Utracono połączenie.");
                ImageButton pol = (ImageButton) findViewById(R.id.imageButton);
                pol.setColorFilter(Color.RED);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                dosalemWiadomosc(topic, new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        String sprawdz = Baza.getString("login","reyruyeru");
        if (sprawdz == "") sprawdz="11";
        mqttConnectOptions.setUserName(sprawdz);
        mqttConnectOptions.setPassword(Baza.getString("haslo","eerteerer").toCharArray());


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
                    addToHistory("Failed to connect to: " + serverUri);
                    ImageButton pol = (ImageButton) findViewById(R.id.imageButton);
                    pol.setColorFilter(Color.RED);
                }
            });


        } catch (MqttException ex) {
            ex.printStackTrace();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    //Wyswietlanie wiadomosci
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void dosalemWiadomosc(String topic, String message) {

        if (sprawdzCzyWifi() == false) {
            ImageButton pol2 = (ImageButton) findViewById(R.id.imageButton1);
            pol2.setColorFilter(Color.RED);
        }
        else {
            ImageButton pol2 = (ImageButton) findViewById(R.id.imageButton1);
            pol2.setColorFilter(Color.GREEN);
        }
        ImageButton pol = (ImageButton) findViewById(R.id.imageButton);
        pol.setColorFilter(Color.GREEN);



        switch (topic) {

            case "S1/dane5":
                TextView temp5 = (TextView) findViewById(R.id.textView2);
                temp5.setText(message + "`C");
                double zmienna5 = new Double(message);
                temp5.setTextColor(Color.rgb(50,100,200));
                if (zmienna5 > 0) {
                    temp5.setTextColor(Color.YELLOW);
                }
                if (zmienna5 >= 15) {
                    temp5.setTextColor(Color.GREEN);
                }


                break;
            case "S1/dane7":

                TextView temp7 = (TextView) findViewById(R.id.textView3);

                temp7.setText(message + "`C");
                double zmienna2 = new Double(message);
                temp7.setTextColor(Color.RED);
                if (zmienna2 >= 15) {
                    temp7.setTextColor(Color.YELLOW);
                }
                if (zmienna2 >= 20) {
                    temp7.setTextColor(Color.GREEN);
                }
                break;
            case "S1/dane8":
                TextView temp8 = (TextView) findViewById(R.id.textView6);

                temp8.setText(message + "`C");

                double zmienna8 = new Double(message);
                temp8.setTextColor(Color.RED);
                if (zmienna8 >= 15) {
                    temp8.setTextColor(Color.YELLOW);
                }
                if (zmienna8 >= 20) {
                    temp8.setTextColor(Color.GREEN);
                }

                break;

            case "S1/dane0":
                TextView temp0 = (TextView) findViewById(R.id.textView12);
                temp0.setText(message + "`C");
                double zmienna0 = new Double(message);
                temp0.setTextColor(Color.YELLOW);
                if (zmienna0 >= 30) {
                    temp0.setTextColor(Color.GREEN);
                }
                if (zmienna0 >= 60) {
                    temp0.setTextColor(Color.RED);
                }

                addToHistory("pobrano dane");
                break;

            case "S1/dane1":                                                 // woda czujnik 1
                TextView temp1 = (TextView) findViewById(R.id.textView14);
                temp1.setText(message + "`C");
                double zmienna11 = new Double(message);

                temp1.setTextColor(Color.YELLOW);
                if (zmienna11 >= 45) {
                    temp1.setTextColor(Color.GREEN);
                }
                zmienna11 = zmienna11 - 35;
                zmienna11 = zmienna11 * 3.5;
                if (zmienna11 >= 100) zmienna11=100;
                if(zmienna11 <= 0 ) zmienna11=0;


                wynik = (int)zmienna11;


                break;

            case "S1/dane2":                                                 // woda czujnik 2
                TextView temp2 = (TextView) findViewById(R.id.textView14);
                double zmienna12 = new Double(message);
                zmienna12 = zmienna12 - 35;
                zmienna12 = zmienna12 * 3.5;
                if (zmienna12 >= 100) zmienna12=100;
                if(zmienna12 <= 0 ) zmienna12=0;

                int wynik2 = (int)zmienna12;
                wynik= wynik+wynik2;
                wynik=wynik/2;

                woda = (ProgressBar) findViewById(R.id.progressBar1);
                woda.setProgress(wynik);
                if(wynik>50){
                    woda.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                }else{
                    woda.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
                }
                String procent = Integer.toString(wynik);
                TextView temp22 = (TextView) findViewById(R.id.textView15);
                temp22.setText(procent+"%");


                break;

            case "S1/dane3":                                                 // woda czujnik 3
                TextView temp3 = (TextView) findViewById(R.id.textView18);
                temp3.setText(message + "`C");
                double zmienna13 = new Double(message);

                temp3.setTextColor(Color.YELLOW);
                if (zmienna13 >= 45) {
                    temp3.setTextColor(Color.GREEN);
                }
                zmienna13 = zmienna13 - 35;
                zmienna13 = zmienna13 * 3.5;
                if (zmienna13 >= 100) zmienna13=100;
                if(zmienna13 <= 0 ) zmienna13=0;


                Swynik = (int)zmienna13;
                break;

            case "S1/dane4":                                                 // woda czujnik 2
                TextView temp4 = (TextView) findViewById(R.id.textView14);
                double zmienna14 = new Double(message);
                zmienna14 = zmienna14 - 35;
                zmienna14 = zmienna14 * 3.5;
                if (zmienna14 >= 100) zmienna14=100;
                if(zmienna14 <= 0 ) zmienna14=0;

                int wynik22 = (int)zmienna14;
                Swynik= Swynik+wynik22;
                Swynik=Swynik/2;
                woda = (ProgressBar) findViewById(R.id.progressBar);
                woda.setProgress(Swynik);
                String procent2 = Integer.toString(Swynik);
                TextView temp42 = (TextView) findViewById(R.id.textView19);
                temp42.setText(procent2+"%");


                break;












            case "S1/dane9":
                TextView temp9 = (TextView) findViewById(R.id.textView9);
                temp9.setText(message + "`C");
                double zmienna9 = new Double(message);

                if (zmienna9 <= 0) {
                    temp9.setTextColor(Color.BLUE);
                }
                break;
            case "S1/dane6":
                TextView temp6 = (TextView) findViewById(R.id.textView10);
                temp6.setText(message + "`C");
                double zmienna6 = new Double(message);

                if (zmienna6 <= 0) {
                    temp6.setTextColor(Color.rgb(50,100,200));
                }
                break;


        }
    }

    private void addToHistory(String mainText) {
        System.out.println("LOG: " + mainText);
        // mAdapter.add(mainText);
        Snackbar.make(findViewById(android.R.id.content), mainText, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    public void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 1, null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                           // addToHistory("Połączono z serwerem");
                            ImageButton pol = (ImageButton) findViewById(R.id.imageButton);
                            pol.setColorFilter(Color.GREEN);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            addToHistory("Failed to subscribe");
                        }
                    }
//            ,new IMqttMessageListener() {
//                        @Override
//                        public void messageArrived(String topic, MqttMessage message) throws Exception {
//                            // message Arrived!
//                            System.out.print("------------------------------\nMessage: " + topic + " : " + new String(message.getPayload()));
//                            addToHistory("------------------------------\nMessage: " + topic + " : " + new String(message.getPayload()));
//                        }
//
//            }
            );
//            // THIS DOES NOT WORK!
//            mqttAndroidClient.subscribe(subscriptionTopic, 0, new IMqttMessageListener() {
//                @Override
//                public void messageArrived(String topic, MqttMessage message) throws Exception {
//                    // message Arrived!
//                    System.out.println("Message: " + topic + " : " + new String(message.getPayload()));
//                }
//            });

        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("PahoExample Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private boolean sprawdzCzyWifi(){

        WifiManager wifiManager = (WifiManager) getSystemService (Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String ssid  = info.getSSID();

        return ssid.contains("MAX");

    }
}

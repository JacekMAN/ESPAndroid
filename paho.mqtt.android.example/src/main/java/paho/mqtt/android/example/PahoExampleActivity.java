/*******************************************************************************
 * Copyright (c) 1999, 2016 IBM Corp.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * <p>
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */
package paho.mqtt.android.example;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DecimalFormat;

public class PahoExampleActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    MqttAndroidClient mqttAndroidClient;
    Commons commons = new Commons();

    String pogoda = "";
    String promieniowanie = "";
    String pm10 = "";
    String pomiar = "";
    String clientId = "";
    final String subscriptionTopic = "#";
    double temperatura;
    int wod = 0;
    double piwnica = 0;
    int gaz = 0;
    int podswietlanie = 0;
    int w1 = 0;
    int wynik; // obliczenie procentowe wody
    int Swynik; // obliczenie procentowe wody solary
    int zamknij = 0;
    int czasowe = 1;
    int awaryjne = 0;
    ProgressBar woda;
    SharedPreferences Baza;
    SharedPreferences.Editor EBaza;
    public double mix;
    public double tempNowa;
    double otw11 = 0d;
    double otw111 = 0d;


    String serverUri = "";

    // Intent i= new Intent(PahoExampleActivity.this ,Setting.class);


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private byte[] payload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Baza = getSharedPreferences("jacek", Context.MODE_PRIVATE);
        EBaza = Baza.edit();

        TextView Obraz0 = (TextView) findViewById(R.id.Start);
        Obraz0.setVisibility(View.VISIBLE);
        FrameLayout Obraz1 = (FrameLayout) findViewById(R.id.Obraz1);
        Obraz1.setVisibility(View.INVISIBLE);
        FrameLayout Obraz2 = (FrameLayout) findViewById(R.id.Obraz2);
        Obraz2.setVisibility(View.INVISIBLE);
        FrameLayout Obraz3 = (FrameLayout) findViewById(R.id.Obraz3);
        Obraz3.setVisibility(View.INVISIBLE);


        if (Baza.getString("Orient", "1").equals("1")) {
            // To Telefon
        }

        if (Baza.getString("Orient", "1").equals("2")) {
            // To Tablet
        }


        if (Baza.getString("lacze", "1").equals("1")) {
            clientId = (Baza.getString("nazwa", "") + commons.generujLosoweZnaki(10));
            serverUri = "tcp://" + Baza.getString("internet", "") + ":" + Baza.getString("port", "");
        } else {
            clientId = (Baza.getString("nazwa", "") + "la" + commons.generujLosoweZnaki(10));
            serverUri = "tcp://" + Baza.getString("local", "") + ":" + Baza.getString("port", "");
        }
        woda = (ProgressBar) findViewById(R.id.progressBar1);


        ImageButton button3 = (ImageButton) findViewById(R.id.imageButton4);
        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // publishMessage("S2/w1",""+w1);
                if (wod == 0) {
                    publishMessage("S2/w1", "1");
                    addToHistory("Podgrzewanie wody włączone");
                } else {
                    publishMessage("S2/w1", "0");
                    addToHistory("Podgrzewanie wody wyłączone");
                }

            }

        });

        ImageButton button6 = (ImageButton) findViewById(R.id.imageButton6);
        button6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // publishMessage("S2/w1",""+w1);
                if (gaz == 0) {
                    publishMessage("S7/w2", "1");
                    addToHistory("Podgrzewanie wody włączone");
                } else {
                    publishMessage("S7/w2", "0");
                    addToHistory("Podgrzewanie wody wyłączone");
                }

            }

        });


        final ImageButton gwiazdka = (ImageButton) findViewById(R.id.gwiadka);
        gwiazdka.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (podswietlanie == 0) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    gwiazdka.setColorFilter(Color.YELLOW);
                    podswietlanie = 1;
                } else {
                    getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    gwiazdka.setColorFilter(Color.GRAY);
                    podswietlanie = 0;
                }
            }

        });
        ImageButton alarm = (ImageButton) findViewById(R.id.imageButton2);
        alarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                Intent i = new Intent(PahoExampleActivity.this, Alarm.class);
                startActivity(i);
            }

        });


        ImageButton button = (ImageButton) findViewById(R.id.ustawienia);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                Intent i = new Intent(PahoExampleActivity.this, Ustawienia.class);
                startActivity(i);
            }

        });

        FrameLayout pogo = (FrameLayout) findViewById(R.id.pogoda);
        pogo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                Intent i = new Intent(PahoExampleActivity.this, Pogoda_menu.class);
                startActivity(i);
            }

        });

        FrameLayout zuz = (FrameLayout) findViewById(R.id.zuzy);
        zuz.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                Intent i = new Intent(PahoExampleActivity.this, zuzycie.class);
                startActivity(i);
            }

        });


        FrameLayout prom = (FrameLayout) findViewById(R.id.prom);
        prom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                Intent i = new Intent(PahoExampleActivity.this, Promieniowanie.class);
                startActivity(i);
            }

        });


        TextView button2 = (TextView) findViewById(R.id.textView25);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View view) {
                if ((sprawdzCzyWifi() == false)) {
                    ImageButton pol2 = (ImageButton) findViewById(R.id.imageButton1);
                    pol2.setColorFilter(Color.RED);
                    awaryjne++;
                    addToHistory("Jesteś poza zasięgiem - włącz WIFI");
                } else {

                    Intent i = new Intent(PahoExampleActivity.this, Sterowanie.class);
                    startActivity(i);
                }

                if (awaryjne > 5)                //awaryjne wejscie do sterowania
                {
                    EBaza.putString("awaryjne", "1");
                    EBaza.commit();
                    awaryjne = 0;
                    Intent i = new Intent(PahoExampleActivity.this, Sterowanie.class);
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
        String sprawdz = Baza.getString("login", "reyruyeru");
        if (sprawdz == "") sprawdz = "11";
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
        pm10 = Baza.getString("pm10", "nie");
        wspolne();
        switch (topic) {


            case "S1/dane5":                                 //temp. na zewnątrz stary czujnik
                mix = new Double(message);
                if (Baza.getString("OUT", "1").equals("1")) {
                    TextView temp5 = (TextView) findViewById(R.id.textView2);
                    temp5.setText(message + "`C");

                    double zmienna5 = new Double(message);
                    temperatura = zmienna5;
                    temp5.setTextColor(Color.rgb(50, 100, 200));
                    if (zmienna5 > 0) {
                        temp5.setTextColor(Color.YELLOW);
                    }
                    if (zmienna5 >= 15) {
                        temp5.setTextColor(Color.GREEN);
                    }
                }
                // if (Baza.getString("OUT","1").equals("3"))
                // {
                //    mix = new Double(message);

                //}

                break;

            case "S3/dane2":

                double tempNaZewnatrz = new Double(message);//temp. na zewnątrz wiata czujnik
                tempNowa = tempNaZewnatrz;

                if (Baza.getString("OUT", "1").equals("2")) {
                    TextView temp5 = (TextView) findViewById(R.id.textView2);

                    temperatura = tempNaZewnatrz;
                    temp5.setText(temperatura + "`C");
                    temp5.setTextColor(Color.rgb(50, 100, 200));
                    if (tempNaZewnatrz > 0) {
                        temp5.setTextColor(Color.YELLOW);
                    }
                    if (tempNaZewnatrz >= 15) {
                        temp5.setTextColor(Color.GREEN);
                    }


                }


                break;

            case "S3/dane3":                                //wilgotność. na zewnątrz wiata czujnik
                if (Baza.getString("OUT", "1").equals("2")) {
                    TextView wilgotnoscView = (TextView) findViewById(R.id.wilgotnosc);
                    double zmienna5 = new Double(message);
                    temperatura = zmienna5;
                    DecimalFormat wzor = new DecimalFormat("####");
                    wilgotnoscView.setText(wzor.format(temperatura) + "%");
                } else if (Baza.getString("OUT", "1").equals("3")) {
                    TextView wilgotnoscView = (TextView) findViewById(R.id.wilgotnosc);
                    double zmienna5 = new Double(message);
                    temperatura = zmienna5;
                    DecimalFormat wzor = new DecimalFormat("####");
                    wilgotnoscView.setText(wzor.format(temperatura) + "%");
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

            case "S11/dane1":

                TextView temp17 = (TextView) findViewById(R.id.pro2);

                temp17.setText(message);

                break;

            case "S11/dane3":

                TextView temp18 = (TextView) findViewById(R.id.pro4);
                pomiar = message;

                if (piwnica != 1) {
                    temp18.setText(pomiar);
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

            case "S2/dane9":
                TextView temp118 = (TextView) findViewById(R.id.textView66);
                temp118.setText(message + "`C");


                double zmienna118 = new Double(message);
                temp118.setTextColor(Color.RED);
                if (zmienna118 >= 15) {
                    temp118.setTextColor(Color.YELLOW);
                }
                if (zmienna118 >= 20) {
                    temp118.setTextColor(Color.GREEN);

                }
                break;

            case "S10/dane1":

                if (Baza.getString("ogrzewanie", "1").equals("0")) {
                    TextView temp11 = (TextView) findViewById(R.id.textView12);
                    temp11.setText(message + "kW");


                    double zmienna18 = new Double(message);
                    temp11.setTextColor(Color.RED);
                    if (zmienna18 >= 15) {
                        temp11.setTextColor(Color.YELLOW);
                    }
                    if (zmienna18 < 15) {
                        temp11.setTextColor(Color.GREEN);
                    }
                    if (zmienna18 > 20) {
                        temp11.setTextColor(Color.RED);
                    }
                }
                break;

            case "S10/dane2":

                if (Baza.getString("ogrzewanie", "1").equals("0")) {
                    TextView temp11 = (TextView) findViewById(R.id.textView13);
                    temp11.setText("1H:    " + message + "kW");


                    double zmienna18 = new Double(message);
                    temp11.setTextColor(Color.RED);
                    if (zmienna18 >= 1) {
                        temp11.setTextColor(Color.YELLOW);
                    }
                    if (zmienna18 < 1) {
                        temp11.setTextColor(Color.GREEN);

                    }
                    if (zmienna18 >= 2) {
                        temp11.setTextColor(Color.RED);
                    }
                }
                break;

            case "S10/dane3":

                if (Baza.getString("ogrzewanie", "1").equals("0")) {
                    TextView temp11 = (TextView) findViewById(R.id.textView100);
                    temp11.setText(message + "kW");


                }
                break;

            case "S10/dane4":

                if (Baza.getString("ogrzewanie", "1").equals("0")) {
                    TextView temp11 = (TextView) findViewById(R.id.textView101);
                    temp11.setText(message + "kW");


                }
                break;

            case "S10/dane5":

                if (Baza.getString("ogrzewanie", "1").equals("0")) {
                    TextView temp11 = (TextView) findViewById(R.id.textView102);
                    temp11.setText(message + "kW");


                }
                break;

            case "S10/dane6":

                if (Baza.getString("ogrzewanie", "1").equals("0")) {
                    TextView temp11 = (TextView) findViewById(R.id.textView103);
                    temp11.setText(message + "kW");


                }
                break;

            case "S10/dane8":

                if (Baza.getString("ogrzewanie", "1").equals("0")) {
                    TextView temp11 = (TextView) findViewById(R.id.textView104);
                    temp11.setText(message + "kW");


                }
                break;


            case "S7/w1":
                TextView COstan = (TextView) findViewById(R.id.textView27);
                double otw22 = new Double(message);
                ImageButton button44 = (ImageButton) findViewById(R.id.imageButton6);
                if (otw22 == 0) {
                    COstan.setText("  ");
                    button44.setColorFilter(Color.BLACK);
                    gaz = 0;
                    FrameLayout gazowe = (FrameLayout) findViewById(R.id.gazowe);
                    gazowe.setVisibility(View.INVISIBLE);

                }
                if (otw22 == 1) {
                    COstan.setText("ON");
                    gaz = 1;
                    button44.setColorFilter(Color.GREEN);
                    FrameLayout gazowe = (FrameLayout) findViewById(R.id.gazowe);
                    gazowe.setVisibility(View.VISIBLE);

                }
                break;


            case "A0/dane9":

                //String spraw = Baza.getString("alarm", message);

                String a = message;
                String test = Baza.getString("alarm1", message);


                if (!test.equals(a)) {


                    EBaza.putString("alarm1", a);
                    EBaza.commit();
                    final Handler handler2 = new Handler();       //  =============================
                    final boolean bb = handler2.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                        @Override
                        // czasu 400 - 400mili sekund
                        public void run() {
                            MediaPlayer mp = MediaPlayer.create(PahoExampleActivity.this, R.raw.alarm);
                            mp.start();
                        }                                        //   po zadanej przerwie
                    }, 3000);// <  czas przerwy
                }

                break;


            case "S1/dane0":                                   // czujnik na Piecu OGRZEWANIE
                if (Baza.getString("ogrzewanie", "1").equals("1")) {
                    TextView temp0 = (TextView) findViewById(R.id.textView12);
                    TextView temp88 = (TextView) findViewById(R.id.textView13);
                    temp0.setText(message + "`C");
                    temp88.setText("Ogrezwanie");
                    double zmienna0 = new Double(message);


                    if (Baza.getString("piecMIN", "nie").equals("tak")) {
                        String minimumS = Baza.getString("piecMINv", "38");
                        int minimum = Integer.parseInt(minimumS);
                        if (zmienna0 < minimum) {
                            final Handler handler = new Handler();       //  =============================
                            final boolean b = handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                                @Override
                                // czasu 400 - 400mili sekund
                                public void run() {
                                    MediaPlayer mp = MediaPlayer.create(PahoExampleActivity.this, R.raw.beep);
                                    mp.start();
                                }                                        //   po zadanej przerwie
                            }, 3000);// <  czas przerwy
                        }
                    }

                    if (Baza.getString("piecMAX", "nie").equals("tak")) {
                        String maximumS = Baza.getString("piecMAXv", "10");
                        int minimum = Integer.parseInt(maximumS);
                        if (zmienna0 > minimum) {
                            final Handler handler = new Handler();       //  =============================
                            final boolean b = handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                                @Override
                                // czasu 400 - 400mili sekund
                                public void run() {
                                    MediaPlayer mp = MediaPlayer.create(PahoExampleActivity.this, R.raw.beep);
                                    mp.start();
                                }                                        //   po zadanej przerwie
                            }, 3000);// <  czas przerwy
                        }
                    }

                    temp0.setTextColor(Color.YELLOW);
                    if (zmienna0 >= 30) {
                        temp0.setTextColor(Color.GREEN);
                    }
                    if (zmienna0 >= 80) {
                        temp0.setTextColor(Color.RED);

                    }

                }
                zamknij = 0;
                pogoda = Baza.getString("pogoda", "nie");
                promieniowanie = Baza.getString("promieniowanie", "nie");
                pm10 = Baza.getString("pm10", "nie");
                //if (pogoda .equals("tak")){
                //   pobierzPOGODA();
                //}
                pobierzPOGODA();
                awaryjne = 0;       //kasowanie wejscia awaryjnego


                //if (pm10.equals("tak")) {
                //    getPM("", "7", (TextView) findViewById(R.id.nazwaPM), (TextView) findViewById(R.id.wartoscPM),
                //            (TextView) findViewById(), (TextView) findViewById(R.id.sredniaPM),
                //            (FrameLayout) findViewById(R.id.pow));
                //}


                TextView Obraz0 = (TextView) findViewById(R.id.Start);
                Obraz0.setVisibility(View.INVISIBLE);
                FrameLayout Obraz1 = (FrameLayout) findViewById(R.id.Obraz1);
                Obraz1.setVisibility(View.VISIBLE);
                FrameLayout Obraz2 = (FrameLayout) findViewById(R.id.Obraz2);
                Obraz2.setVisibility(View.VISIBLE);
                FrameLayout Obraz3 = (FrameLayout) findViewById(R.id.Obraz3);
                Obraz3.setVisibility(View.VISIBLE);
                addToHistory("pobrano dane");

                break;
            case "S8/dane5":

                TextView temp55 = (TextView) findViewById(R.id.sredniaPM);

                if (pm10.equals("tak")) {


                    temp55.setText(message);

                }
                break;


            case "S8/dane4":

                TextView temp99 = (TextView) findViewById(R.id.wartoscPM);
                TextView temp98 = (TextView) findViewById(R.id.opisPM);
                FrameLayout Obraz33 = (FrameLayout) findViewById(R.id.pow);
                if (pm10.equals("tak")) {
                    temp99.setText(message + "%");
                    double d = new Double(message);
                    Obraz33.setBackgroundColor(Color.rgb(199, 45, 45));
                    temp98.setText("DRAMAT !");
                    if (d <= 50) {
                        Obraz33.setBackgroundColor(Color.rgb(31, 135, 45));
                        temp98.setText("B.dobrze");
                        return;
                    }
                    if (d <= 100) {
                        Obraz33.setBackgroundColor(Color.rgb(31, 135, 45));
                        temp98.setText("dobrze");
                        return;
                    }
                    if (d <= 200) {
                        Obraz33.setBackgroundColor(Color.rgb(191, 179, 39));
                        temp98.setText("dostatecznie");
                        return;
                    }
                    if (d <= 500) {
                        temp98.setText("    źle ");
                        return;
                    }
                    if (d <= 999) {
                        temp98.setText("   B.źle !");
                    }


                }
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
                if (zmienna11 >= 100) zmienna11 = 100;
                if (zmienna11 <= 0) zmienna11 = 0;


                wynik = (int) zmienna11;


                break;

            case "S1/dane2":                                                 // woda czujnik 2
                TextView temp2 = (TextView) findViewById(R.id.textView14);
                double zmienna12 = new Double(message);
                zmienna12 = zmienna12 - 35;
                zmienna12 = zmienna12 * 3.5;
                if (zmienna12 >= 100) zmienna12 = 100;
                if (zmienna12 <= 0) zmienna12 = 0;

                int wynik2 = (int) zmienna12;
                wynik = wynik + wynik2;
                wynik = wynik / 2;

                woda = (ProgressBar) findViewById(R.id.progressBar1);
                woda.setProgress(wynik);
                if (wynik > 50) {
                    woda.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                } else {
                    woda.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
                }
                String procent = Integer.toString(wynik);
                TextView temp22 = (TextView) findViewById(R.id.textView15);
                temp22.setText(procent + "%");


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
                if (zmienna13 >= 100) zmienna13 = 100;
                if (zmienna13 <= 0) zmienna13 = 0;


                Swynik = (int) zmienna13;
                break;

            case "S1/dane4":                                                 // woda czujnik 2
                TextView temp4 = (TextView) findViewById(R.id.textView14);
                double zmienna14 = new Double(message);
                zmienna14 = zmienna14 - 35;
                zmienna14 = zmienna14 * 3.5;
                if (zmienna14 >= 100) zmienna14 = 100;
                if (zmienna14 <= 0) zmienna14 = 0;

                int wynik22 = (int) zmienna14;
                Swynik = Swynik + wynik22;
                Swynik = Swynik / 2;
                woda = (ProgressBar) findViewById(R.id.progressBar);
                woda.setProgress(Swynik);
                if (Swynik > 50) {
                    woda.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                } else {
                    woda.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
                }
                String procent2 = Integer.toString(Swynik);
                TextView temp42 = (TextView) findViewById(R.id.textView19);
                temp42.setText(procent2 + "%");


                break;

            case "S2/dane0":                                                 // woda czujnik 2
                TextView s20 = (TextView) findViewById(R.id.textView26);
                double zmienna20 = new Double(message);
                ImageButton button4 = (ImageButton) findViewById(R.id.imageButton4);
                if (zmienna20 > 0) {
                    s20.setText(message + " min");
                    button4.setColorFilter(Color.GREEN);
                    wod = 1;
                }
                if (zmienna20 == 0) {
                    s20.setText("  ");
                    button4.setColorFilter(Color.BLACK);
                    wod = 0;
                }


                break;
            case "S5/dane1":
                FrameLayout prom = (FrameLayout) findViewById(R.id.gar);
                //
                otw11 = new Double(message);

                if (otw11 == 1) {
                    prom.setBackgroundColor(Color.rgb(199, 45, 45));
                } else {
                    prom.setBackgroundColor(Color.rgb(31, 135, 45));
                }
                break;


            case "A0/dane0":

                FrameLayout prom1 = (FrameLayout) findViewById(R.id.prom);
                TextView pro2 = (TextView) findViewById(R.id.pro2);
                TextView pro4 = (TextView) findViewById(R.id.pro4);
                otw11 = new Double(message);

                if (otw11 == 0) {
                    if (promieniowanie.equals("tak")) {
                        pro2.setTextColor(Color.BLACK);
                        getPromieniowanie((TextView) findViewById(R.id.pro4));
                    }
                } else {
                    prom1.setBackgroundColor(Color.rgb(199, 45, 45));
                    pro2.setTextColor(Color.WHITE);
                    pro2.setText("ALARM");
                    pro4.setText("!! WŁĄCZONY !!");
                }


                break;


            case "S5/dane2":
                FrameLayout promm = (FrameLayout) findViewById(R.id.garr);
                //
                otw11 = new Double(message);

                if (otw11 == 1) {
                    promm.setBackgroundColor(Color.rgb(199, 45, 45));
                } else {
                    promm.setBackgroundColor(Color.rgb(31, 135, 45));
                }
                break;


            case "S1/dane9":
                TextView temp9 = (TextView) findViewById(R.id.textView9);
                temp9.setText(message + "`C");
                double zmienna9 = new Double(message);

                if (zmienna9 <= 0) {
                    temp9.setTextColor(Color.BLUE);
                }
                break;

            case "S7/dane1":
                TextView temp24d = (TextView) findViewById(R.id.textView24d);
                temp24d.setText(message + "`C");
                break;

            case "S7/dane6":
                TextView temp23 = (TextView) findViewById(R.id.textView23);
                temp23.setText(message + "`C");
                break;

            case "S7/dane2":
                double zmi = new Double(message);
                if (zmi == 0) {
                    RadioButton piec = (RadioButton) findViewById(R.id.piec);
                    piec.setVisibility(View.INVISIBLE);
                } else {
                    RadioButton piec = (RadioButton) findViewById(R.id.piec);
                    piec.setVisibility(View.VISIBLE);
                }

                break;

            case "S7/dane4":
                TextView temp24g = (TextView) findViewById(R.id.textView24g);

                temp24g.setText(message + " min");

                break;

            case "S7/dane5":
                TextView temp24i = (TextView) findViewById(R.id.textView24i);
                double zmienna24i = new Double(message);
                if (zmienna24i != 0) {
                    temp24i.setText(message + " min");
                } else {
                    temp24i.setText("    ");
                }

                break;
            case "S7/dane3":
                double zmiennaczasowa = new Double(message);
                if (zmiennaczasowa == 1) {
                    TextView pokaz1 = (TextView) findViewById(R.id.textView24g);
                    pokaz1.setVisibility(View.INVISIBLE);
                    TextView pokaz2 = (TextView) findViewById(R.id.textView24f);
                    pokaz2.setVisibility(View.INVISIBLE);
                    TextView pokaz3 = (TextView) findViewById(R.id.textView24i);
                    pokaz3.setVisibility(View.INVISIBLE);
                    TextView pokaz4 = (TextView) findViewById(R.id.textView24h);
                    pokaz4.setVisibility(View.INVISIBLE);
                } else {
                    TextView pokaz1 = (TextView) findViewById(R.id.textView24g);
                    pokaz1.setVisibility(View.VISIBLE);
                    TextView pokaz2 = (TextView) findViewById(R.id.textView24f);
                    pokaz2.setVisibility(View.VISIBLE);
                    TextView pokaz3 = (TextView) findViewById(R.id.textView24i);
                    pokaz3.setVisibility(View.VISIBLE);
                    TextView pokaz4 = (TextView) findViewById(R.id.textView24h);
                    pokaz4.setVisibility(View.VISIBLE);
                }


                break;


            case "S1/dane6":
                TextView temp6 = (TextView) findViewById(R.id.textView10);
                temp6.setText(message + "`C");
                double zmienna6 = new Double(message);

                if (zmienna6 <= 0) {
                    temp6.setTextColor(Color.rgb(50, 100, 200));
                }
                break;
            case "S8/dane1":
                piwnica = new Double(message);
                FrameLayout Obraz313 = (FrameLayout) findViewById(R.id.prom);
                TextView alP = (TextView) findViewById(R.id.pro4);
                TextView alP2 = (TextView) findViewById(R.id.pro2);
                if (piwnica == 1) {


                    Obraz313.setBackgroundColor(Color.RED);
                    alP.setTextColor(Color.WHITE);
                    alP2.setTextColor(Color.YELLOW);
                    alP.setText("Piwnica otwarta");
                } else {
                    Obraz313.setBackgroundColor(Color.WHITE);
                    alP.setTextColor(Color.BLACK);
                    alP.setText(pomiar);
                    alP2.setTextColor(Color.BLACK);
                }
                break;

        }
    }


    private void LoadImageFromUrl(String url) {
        ImageView obraz = (ImageView) findViewById(R.id.ikona);
        Picasso.with(this).load(url).into(obraz);


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
    public void onBackPressed() {
        if (zamknij == 0) {
            addToHistory("Napewno zamykamy ? nacisnij jeszce raz !!!");
            zamknij++;
        } else {
            System.exit(0);
        }

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
                .setUrl(Uri.parse("https://[ENTER-YOUR-URL-HERE]"))
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

    private boolean sprawdzCzyWifi() {

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();

        String ssid = info.getSSID();
        //addToHistory(ssid);
        return wifiManager.isWifiEnabled() && ssid.contains("MAX");

    }

    private void pobierzPOGODA() {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=Rybnik&units=metric&APPID=3f7686cca4450da8177abde18b20fb73";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    {
                        Double cisnienie = (Double) jsonObject.getJSONObject("main").getDouble("pressure");
                        Double wiatr = (Double) jsonObject.getJSONObject("wind").getDouble("speed");
                        Double wilgotnoscV = (Double) jsonObject.getJSONObject("main").getDouble("humidity");
                        Double chmury = (Double) jsonObject.getJSONObject("clouds").getDouble("all");
                        String obraz = (String) jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");
                        Long wschod0 = (Long) jsonObject.getJSONObject("sys").getLong("sunrise");
                        Long zachod0 = (Long) jsonObject.getJSONObject("sys").getLong("sunset");


                        Time wschod = new Time(wschod0 * 1000);

                        Time zachod = new Time(zachod0 * 1000);


                        // wyświetlanie odczytów

                        TextView wschodView = (TextView) findViewById(R.id.wschod2);
                        wschodView.setText(wschod.toString() + " ");

                        TextView zachodView = (TextView) findViewById(R.id.zachod2);
                        zachodView.setText(zachod.toString() + " ");

                        LoadImageFromUrl("https://openweathermap.org/img/w/" + obraz + ".png");

                        TextView wiatrView = (TextView) findViewById(R.id.textView56);
                        DecimalFormat twoDForm2 = new DecimalFormat("####");
                        DecimalFormat twoDForm3 = new DecimalFormat("###.#");
                        DecimalFormat twoDForm = new DecimalFormat("###");
                        wiatrView.setText(twoDForm3.format(wiatr) + " km/h");
                        if (Baza.getString("OUT", "1").equals("1")) {
                            TextView wilgotnoscView = (TextView) findViewById(R.id.wilgotnosc);
                            wilgotnoscView.setText(twoDForm2.format(wilgotnoscV) + "%");
                        }

                        TextView chmuryView = (TextView) findViewById(R.id.minimum);
                        chmuryView.setText(twoDForm2.format(chmury) + "%");

                        TextView cisnienieView = (TextView) findViewById(R.id.textView22);
                        cisnienieView.setTextColor(Color.rgb(196, 63, 63));

                        if (cisnienie < 1020) cisnienieView.setTextColor(Color.GREEN);
                        if (cisnienie < 990) cisnienieView.setTextColor(Color.rgb(50, 100, 200));
                        cisnienieView.setText(twoDForm2.format(cisnienie) + " hPa");


                    }


                    try {

                    } catch (Exception e) {

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }


    private void getPromieniowanie(final TextView textView) {

        String url = "https://radioactiveathome.org/boinc/gettrickledata.php?start=0&hostid=15877";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] linie = response.split("\\n");
                double max = 0d;
                String data = " ";
                for (String s : linie) {
                    String[] wyniki = s.split(",");
                    if (wyniki.length < 7)
                        continue;
                    Double wynik = new Double(wyniki[2]);
                    Double czas = new Double(wyniki[6]);
                    wynik = (wynik / czas) / 171.232876;
                    if (wynik > max) {
                        max = wynik;
                        data = wyniki[3];
                    }

                }

                String[] wyniki = linie[linie.length - 1].split(",");
                Double wynik = new Double(wyniki[2]);
                Double czas = new Double(wyniki[6]);
                wynik = (wynik / czas) / 171.232876;
                DecimalFormat twoDForm = new DecimalFormat("##.##");
                FrameLayout prom = (FrameLayout) findViewById(R.id.prom);
                if (wynik < 0.30) ;
                {
                    prom.setBackgroundColor(Color.rgb(31, 135, 45));
                }
                TextView pr = (TextView) findViewById(R.id.pro2);
                pr.setText("Radioaktywność");
                textView.setText(twoDForm.format(wynik) + " uSv/h");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    private void getPM(String station, final String id, final TextView nazwaView, final TextView wartoscView,
                       final TextView przekroczenieView, final TextView sredniaView, final FrameLayout frameLayout) {
        station = station.isEmpty() ? "23" : station;
        String url = "https://api.smoksmog.jkostrz.name/api/stations/" + station;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    JSONArray listaOdczytow = jsonObject.getJSONArray("particulates");
                    for (int i = 0; i < listaOdczytow.length(); i++) {
                        JSONObject odczyt = listaOdczytow.getJSONObject(i);
                        if (odczyt.getString("id").equals(id)) {
                            Double wartosc = new Double(odczyt.getString("value"));
                            Double norma = new Double(odczyt.getString("norm"));
                            Double srednia = new Double(odczyt.getString("avg"));
                            String nazwa = odczyt.getString("short_name");
                            nazwaView.setText(nazwa);
                            Double procent = (wartosc * 100d) / norma;
                            srednia = (srednia * 100d) / norma;
                            DecimalFormat twoDForm = new DecimalFormat("####");
                            wartoscView.setText(twoDForm.format(procent) + "%");
                            sredniaView.setText("Sred :" + twoDForm.format(srednia) + "%");
                            ocenZanieczysczenie(procent, przekroczenieView, frameLayout);
                            nazwaView.setText(nazwa);
                            System.out.println("LOG: \n Odczyt poprawny o godzinie : " + odczyt.getString("date"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }


    public void ocenZanieczysczenie(Double d, TextView textView, FrameLayout frameLayout) {
        frameLayout.setBackgroundColor(Color.rgb(199, 45, 45));
        textView.setText("DRAMAT !");
        if (d <= 50) {
            frameLayout.setBackgroundColor(Color.rgb(31, 135, 45));
            textView.setText("B.dobrze");
            return;
        }
        if (d <= 100) {
            frameLayout.setBackgroundColor(Color.rgb(31, 135, 45));
            textView.setText("dobrze");
            return;
        }
        if (d <= 200) {
            frameLayout.setBackgroundColor(Color.rgb(191, 179, 39));
            textView.setText("dostatecznie");
            return;
        }
        if (d <= 500) {
            textView.setText("    źle ");
            return;
        }
        if (d <= 999) {
            textView.setText("   B.źle !");
        }

    }

    public void Rybnik(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.meteo.pl/um/metco/mgram_pict.php?ntype=&row=465&col=207&lang=pl"));
        startActivity(intent);
    }

    private void wspolne() {

        if (sprawdzCzyWifi() == false) {
            ImageButton pol2 = (ImageButton) findViewById(R.id.imageButton1);
            pol2.setColorFilter(Color.RED);
        } else {
            ImageButton pol2 = (ImageButton) findViewById(R.id.imageButton1);
            pol2.setColorFilter(Color.GREEN);
        }
        ImageButton pol = (ImageButton) findViewById(R.id.imageButton);
        pol.setColorFilter(Color.GREEN);

        if (Baza.getString("OUT", "1").equals("3"))     //temp. na zewnątrz MIX z czujników
        {
            TextView temp5 = (TextView) findViewById(R.id.textView2);
            double zmienna5 = new Double(tempNowa);
            temperatura = zmienna5;

            if (temperatura > mix)    //wyliczenie temperatury dla MIX
            {
                temperatura = mix;
            }


            temp5.setText(temperatura + "`C");
            temp5.setTextColor(Color.rgb(50, 100, 200));
            if (zmienna5 > 0) {
                temp5.setTextColor(Color.YELLOW);
            }
            if (zmienna5 >= 15) {
                temp5.setTextColor(Color.GREEN);
            }

        }

    }

}


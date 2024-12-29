package paho.mqtt.android.example;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
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


public class Zuzycie extends AppCompatActivity {


    MqttAndroidClient mqttAndroidClient;
    Commons commons = new Commons();

    String clientId = "";
    double zadana = 0;

    double zadana10 = 0;
    double zadana2 = 0;
    double zadana3 = 0;
    double zadana4 = 0;
    double zadana5 = 0;
    int Czerwony = Color.parseColor("#F44336");
    int Ok = Color.parseColor("#fbfdfb");

    int zadanastop = 0;
    double p14 = 0;
    int[] parametr = new int[40];
    int pa14 = 0;
    double p13 = 0;
    double p31 = 0;
    double p32 = 0;
    double p19 = 0;
    double p24 = 0;
    double p29 = 0;
    double p12 = 0;


    String p15;
    String p16;
    String p17;
    String p18;
    String p20;
    String p21;
    String p22;
    String p23;
    String p25;
    String p26;
    String p27;
    String p28;
    String wybor2;


    String wynik = " ";

    final String subscriptionTopic = "#";


    SharedPreferences Baza;
    SharedPreferences.Editor EBaza;


    String serverUri = "";


    // Intent i= new Intent(PahoExampleActivity.this ,Setting.class);


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zuzycie);
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


        ImageButton zadplus = (ImageButton) findViewById(R.id.zadanaplus);
        zadplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zadana = zadana * 10;
                zadana = zadana + 1;
                zadana = zadana / 10;


                wynik = new Double(zadana).toString();
                String w13 = wynik.substring(0, 2);
                String w14 = wynik.substring(3);
                TextView piec13 = (TextView) findViewById(R.id.piec13);
                piec13.setText(wynik);
                publishMessage("Piec13", w13);
                publishMessage("Piec14", w14 + "0");
                zadplus.setColorFilter(Color.GREEN);
                final Handler handler = new Handler();       //  =============================
                handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                    @Override


                    // czasu 400 - 400mili sekund
                    public void run() {                      //
                        zadplus.setColorFilter(Color.GRAY); // < ta funkcja jest wykonywana
                    }                                        //   po zadanej przerwie
                }, 500);
            }
        });
        ImageButton zadminus = (ImageButton) findViewById(R.id.zadanaminus);
        zadminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zadana = zadana * 10;
                zadana = zadana - 1;
                zadana = zadana / 10;


                wynik = new Double(zadana).toString();
                String w13 = wynik.substring(0, 2);
                String w14 = wynik.substring(3);
                TextView piec13 = (TextView) findViewById(R.id.piec13);
                piec13.setText(wynik);
                publishMessage("Piec13", w13);
                publishMessage("Piec14", w14 + "0");
                zadminus.setColorFilter(Color.GREEN);
                final Handler handler = new Handler();       //  =============================
                handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                    @Override                                // czasu 400 - 400mili sekund
                    public void run() {                      //
                        zadminus.setColorFilter(Color.GRAY); // < ta funkcja jest wykonywana
                    }                                        //   po zadanej przerwie
                }, 500);
            }
        });
////uiouoiui-------------------------------------------------------------------------------------------------------


        ImageButton zadminus6 = (ImageButton) findViewById(R.id.zadanaminus6);
        zadminus6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zadana10 = zadana10 * 10;
                if (zadana10 > 1) {
                    zadana10 = zadana10 - 1;
                }
                zadana10 = zadana10 / 10;


                wynik = new Double(zadana10).toString();
                String w13 = wynik.substring(0, 1);
                String w14 = wynik.substring(2);
                TextView piec13 = (TextView) findViewById(R.id.piec31);
                piec13.setText(wynik);
                publishMessage("Piec31", w13);
                publishMessage("Piec32", w14);
                zadminus.setColorFilter(Color.GREEN);
                final Handler handler = new Handler();       //  =============================
                handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                    @Override                                // czasu 400 - 400mili sekund
                    public void run() {                      //
                        zadminus.setColorFilter(Color.GRAY); // < ta funkcja jest wykonywana
                    }                                        //   po zadanej przerwie
                }, 500);
            }
        });
        ImageButton zadplus6 = (ImageButton) findViewById(R.id.zadanaplus6);
        zadplus6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zadana10 = zadana10 * 10;
                if (zadana10 < 20) {
                    zadana10 = zadana10 + 1;
                }
                zadana10 = zadana10 / 10;


                wynik = new Double(zadana10).toString();
                String w13 = wynik.substring(0, 1);
                String w14 = wynik.substring(2);
                TextView piec13 = (TextView) findViewById(R.id.piec31);
                piec13.setText(wynik);
                publishMessage("Piec31", w13);
                publishMessage("Piec32", w14);
                zadminus.setColorFilter(Color.GREEN);
                final Handler handler = new Handler();       //  =============================
                handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                    @Override                                // czasu 400 - 400mili sekund
                    public void run() {                      //
                        zadminus.setColorFilter(Color.GRAY); // < ta funkcja jest wykonywana
                    }                                        //   po zadanej przerwie
                }, 500);
            }
        });
        ImageButton zadminus2 = (ImageButton) findViewById(R.id.zadanaminus2);
        zadminus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zadana2 = zadana2 - 1;
                String w19 = new Double(zadana2).toString();
                String w191 = w19.substring(0, 2);
                TextView piec19 = (TextView) findViewById(R.id.piec19);
                piec19.setText(w19);
                TextView piec190 = (TextView) findViewById(R.id.textpiec1);
                piec190.setText(w191);
                publishMessage("Piec19", w191);
                zadminus2.setColorFilter(Color.GREEN);
                final Handler handler = new Handler();       //  =============================
                handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                    @Override                                // czasu 400 - 400mili sekund
                    public void run() {                      //
                        zadminus2.setColorFilter(Color.GRAY); // < ta funkcja jest wykonywana
                    }                                        //   po zadanej przerwie
                }, 500);
            }
        });

        ImageButton zadplus2 = (ImageButton) findViewById(R.id.zadanaplus2);
        zadplus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zadana2 = zadana2 + 1;
                String w19 = new Double(zadana2).toString();
                String w191 = w19.substring(0, 2);
                TextView piec19 = (TextView) findViewById(R.id.piec19);
                piec19.setText(w19);
                publishMessage("Piec19", w191);
                zadplus2.setColorFilter(Color.GREEN);
                final Handler handler = new Handler();       //  =============================
                handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                    @Override                                // czasu 400 - 400mili sekund
                    public void run() {                      //
                        zadplus2.setColorFilter(Color.GRAY); // < ta funkcja jest wykonywana
                    }                                        //   po zadanej przerwie
                }, 500);
            }
        });

        ImageButton zadplus3 = (ImageButton) findViewById(R.id.zadanaplus3);
        zadplus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zadana3 = zadana3 + 1;
                String w24 = new Double(zadana3).toString();
                String w192 = w24.substring(0, 2);
                TextView piec24 = (TextView) findViewById(R.id.piec24);
                piec24.setText(w24);
                publishMessage("Piec24", w192);
                zadplus3.setColorFilter(Color.GREEN);
                final Handler handler = new Handler();       //  =============================
                handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                    @Override                                // czasu 400 - 400mili sekund
                    public void run() {                      //
                        zadplus3.setColorFilter(Color.GRAY); // < ta funkcja jest wykonywana
                    }                                        //   po zadanej przerwie
                }, 500);
            }
        });

        ImageButton zadminus3 = (ImageButton) findViewById(R.id.zadanaminus3);
        zadminus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zadana3 = zadana3 - 1;
                String w24 = new Double(zadana3).toString();
                String w192 = w24.substring(0, 2);
                TextView piec24 = (TextView) findViewById(R.id.piec24);
                piec24.setText(w24);
                publishMessage("Piec24", w192);
                zadminus3.setColorFilter(Color.GREEN);
                final Handler handler = new Handler();       //  =============================
                handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                    @Override                                // czasu 400 - 400mili sekund
                    public void run() {                      //
                        zadminus3.setColorFilter(Color.GRAY); // < ta funkcja jest wykonywana
                    }                                        //   po zadanej przerwie
                }, 500);
            }
        });

        ImageButton zadplus4 = (ImageButton) findViewById(R.id.zadanaplus4);
        zadplus4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zadana4 = zadana4 + 1;
                String w29 = new Double(zadana4).toString();
                String w191 = w29.substring(0, 2);
                TextView piec29 = (TextView) findViewById(R.id.piec29);
                piec29.setText(w29);
                publishMessage("Piec29", w191);
                zadplus4.setColorFilter(Color.GREEN);
                final Handler handler = new Handler();       //  =============================
                handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                    @Override                                // czasu 400 - 400mili sekund
                    public void run() {                      //
                        zadplus4.setColorFilter(Color.GRAY); // < ta funkcja jest wykonywana
                    }                                        //   po zadanej przerwie
                }, 500);
            }
        });
        ImageButton zadminus4 = (ImageButton) findViewById(R.id.zadanaminus4);
        zadminus4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zadana4 = zadana4 - 1;
                String w29 = new Double(zadana4).toString();
                String w191 = w29.substring(0, 2);
                TextView piec29 = (TextView) findViewById(R.id.piec29);
                piec29.setText(w29);
                publishMessage("Piec29", w191);
                zadminus4.setColorFilter(Color.GREEN);
                final Handler handler = new Handler();       //  =============================
                handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                    @Override                                // czasu 400 - 400mili sekund
                    public void run() {                      //
                        zadminus4.setColorFilter(Color.GRAY); // < ta funkcja jest wykonywana
                    }                                        //   po zadanej przerwie
                }, 500);
            }
        });

        ImageButton zadminus5 = (ImageButton) findViewById(R.id.zadanaminus5);
        zadminus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zadana5 = zadana5 - 5;
                if (zadana5 < 5) {
                    zadana5 = 5;
                }
                String w29 = new Double(zadana5).toString();
                String w191 = w29.substring(0, 2);
                TextView piec29 = (TextView) findViewById(R.id.piec30);
                piec29.setText(w191 + "min");
                publishMessage("Piec30", w191);
                zadminus5.setColorFilter(Color.GREEN);
                final Handler handler = new Handler();       //  =============================
                handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                    @Override                                // czasu 400 - 400mili sekund
                    public void run() {                      //
                        zadminus5.setColorFilter(Color.GRAY); // < ta funkcja jest wykonywana
                    }                                        //   po zadanej przerwie
                }, 500);
            }
        });
        ImageButton zadplus5 = (ImageButton) findViewById(R.id.zadanaplus5);
        zadplus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zadana5 = zadana5 + 5;
                if (zadana5 > 240) {
                    zadana5 = 240;
                }
                String w29 = new Double(zadana5).toString();
                String w191 = w29.substring(0, 2);
                TextView piec29 = (TextView) findViewById(R.id.piec30);
                piec29.setText(w191 + "min");
                publishMessage("Piec30", w191);
                zadminus5.setColorFilter(Color.GREEN);
                final Handler handler = new Handler();       //  =============================
                handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                    @Override                                // czasu 400 - 400mili sekund
                    public void run() {                      //
                        zadminus5.setColorFilter(Color.GRAY); // < ta funkcja jest wykonywana
                    }                                        //   po zadanej przerwie
                }, 500);
            }
        });


        CheckBox wy0 = (CheckBox) findViewById(R.id.czasowe0);
        wy0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (parametr[1] != 1) {
                    publishMessage("Piec8", "1");
                }
                if (parametr[1] == 1) {
                    publishMessage("Piec8", "0");
                }
            }
        });
        CheckBox wy1 = (CheckBox) findViewById(R.id.czasowe5);
        wy1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (parametr[0] != 1) {
                    publishMessage("Piec7", "1");
                }
                if (parametr[0] == 1) {
                    publishMessage("Piec7", "0");
                }
            }
        });
        CheckBox wy2 = (CheckBox) findViewById(R.id.czasowe1);
        wy2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (parametr[2] != 1) {
                    publishMessage("Piec9", "1");
                }
                if (parametr[2] == 1) {
                    publishMessage("Piec9", "0");
                }
            }
        });
        CheckBox wy3 = (CheckBox) findViewById(R.id.czasowe2);
        wy3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (parametr[3] != 1) {
                    publishMessage("Piec10", "1");
                }
                if (parametr[3] == 1) {
                    publishMessage("Piec10", "0");
                }
            }
        });
        CheckBox wy4 = (CheckBox) findViewById(R.id.czasowe3);
        wy4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (parametr[4] != 1) {
                    publishMessage("Piec11", "1");
                }
                if (parametr[4] == 1) {
                    publishMessage("Piec11", "0");
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
                                                  addToHistory("Połączony lokalnie");
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
                                      }


        );

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
                    ImageButton pol = (ImageButton) findViewById(R.id.imageButton);
                }
            });


        } catch (MqttException ex) {
            ex.printStackTrace();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


    }

    ;


    public void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 1, null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            // addToHistory("Połączono z serwerem");
                            ImageButton pol = (ImageButton) findViewById(R.id.piecsiec);
                            pol.setColorFilter(Color.GREEN);

                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            addToHistory("Błąd pobierania");
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

    //Wyswietlanie wiadomosci
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void dosalemWiadomosc(String topic, String message) {


        switch (topic) {

            case "Piec14":

                p14 = new Double(message);

                break;

            case "Piec13":


                p13 = new Double(message);


                break;

            case "Piec31":

                p31 = new Double(message);

                break;

            case "Piec32":


                p32 = new Double(message);


                break;
            case "Piec15":
                p15 = message;
                break;
            case "Piec16":
                p16 = message;
                break;
            case "Piec17":
                p17 = message;
                break;
            case "Piec18":
                p18 = message;
                break;
            case "Piec20":
                p20 = message;
                break;
            case "Piec21":
                p21 = message;
                break;
            case "Piec22":
                p22 = message;
                break;
            case "Piec23":
                p23 = message;
                break;

            case "Piec25":
                p25 = message;
                break;
            case "Piec26":
                p26 = message;
                break;
            case "Piec27":
                p27 = message;
                break;
            case "Piec28":
                p28 = message;
                break;


            case "Piec19":


                p19 = new Double(message);


                break;
            case "Piec24":


                p24 = new Double(message);


                break;
            case "Piec29":


                p29 = new Double(message);


                break;
            case "Piec12":


                p12 = new Double(message);
                RadioButton wybor1 = (RadioButton) findViewById(R.id.p1);
                RadioButton wybor2 = (RadioButton) findViewById(R.id.p2);
                RadioButton wybor3 = (RadioButton) findViewById(R.id.p3);

                if (p12 == 1) {
                    wybor3.setChecked(true);
                    wybor2.setChecked(false);
                    wybor1.setChecked(false);
                }
                ;
                if (p12 == 2) {
                    wybor2.setChecked(true);
                    wybor3.setChecked(false);
                    wybor1.setChecked(false);
                }
                ;
                if (p12 == 0) {
                    wybor1.setChecked(true);
                    wybor2.setChecked(false);
                    wybor3.setChecked(false);
                }
                ;


                break;

            case "Piec8":
                Double wybor = new Double(message);
                if (wybor == 0) {
                    parametr[1] = 0;
                    CheckBox wy0 = (CheckBox) findViewById(R.id.czasowe0);
                    wy0.setChecked(true);
                    wy0.setText("ON");
                    TextView piec13 = (TextView) findViewById(R.id.textpiec2);
                    piec13.setTextColor(Ok);
                    TextView piec30 = (TextView) findViewById(R.id.piec13);
                    piec30.setTextColor(Ok);
                }
                if (wybor != 0) {
                    parametr[1] = 1;
                    CheckBox wy1 = (CheckBox) findViewById(R.id.czasowe0);
                    wy1.setChecked(false);
                    wy1.setText("OFF");
                    TextView piec13 = (TextView) findViewById(R.id.textpiec2);
                    piec13.setTextColor(Czerwony);
                    TextView piec30 = (TextView) findViewById(R.id.piec13);
                    piec30.setTextColor(Czerwony);
                }


                break;
            case "Piec7":
                Double wybor5 = new Double(message);
                if (wybor5 == 0) {
                    parametr[0] = 0;
                    CheckBox wy0 = (CheckBox) findViewById(R.id.czasowe5);
                    wy0.setChecked(true);
                    wy0.setText("ON");
                    TextView piec13 = (TextView) findViewById(R.id.textpiec7);
                    piec13.setTextColor(Ok);
                    TextView piec30 = (TextView) findViewById(R.id.piec30);
                    piec30.setTextColor(Ok);
                }
                if (wybor5 != 0) {
                    parametr[0] = 1;
                    CheckBox wy1 = (CheckBox) findViewById(R.id.czasowe5);
                    wy1.setChecked(false);
                    wy1.setText("OFF");
                    TextView piec13 = (TextView) findViewById(R.id.textpiec7);
                    piec13.setTextColor(Czerwony);
                    TextView piec30 = (TextView) findViewById(R.id.piec30);
                    piec30.setTextColor(Czerwony);
                }

                break;
            case "Piec9":
                Double wyb1 = new Double(message);
                if (wyb1 == 0) {
                    parametr[2] = 0;
                    CheckBox wy0 = (CheckBox) findViewById(R.id.czasowe1);
                    wy0.setChecked(true);
                    wy0.setText("ON");
                    TextView piec13 = (TextView) findViewById(R.id.textpiec3);
                    piec13.setTextColor(Ok);
                    TextView piec30 = (TextView) findViewById(R.id.piec19);
                    piec30.setTextColor(Ok);
                }
                if (wyb1 != 0) {
                    parametr[2] = 1;
                    CheckBox wy0 = (CheckBox) findViewById(R.id.czasowe1);
                    wy0.setChecked(false);
                    wy0.setText("OFF");
                    TextView piec13 = (TextView) findViewById(R.id.textpiec3);
                    piec13.setTextColor(Czerwony);
                    TextView piec30 = (TextView) findViewById(R.id.piec19);
                    piec30.setTextColor(Czerwony);
                }


                break;
            case "Piec10":
                Double wyb2 = new Double(message);
                if (wyb2 == 0) {
                    parametr[3] = 0;
                    CheckBox wy0 = (CheckBox) findViewById(R.id.czasowe2);
                    wy0.setChecked(true);
                    wy0.setText("ON");
                    TextView piec13 = (TextView) findViewById(R.id.textpiec4);
                    piec13.setTextColor(Ok);
                    TextView piec30 = (TextView) findViewById(R.id.piec24);
                    piec30.setTextColor(Ok);
                }
                if (wyb2 != 0) {
                    parametr[3] = 1;
                    CheckBox wy0 = (CheckBox) findViewById(R.id.czasowe2);
                    wy0.setChecked(false);
                    wy0.setText("OFF");
                    TextView piec13 = (TextView) findViewById(R.id.textpiec4);
                    piec13.setTextColor(Czerwony);
                    TextView piec30 = (TextView) findViewById(R.id.piec24);
                    piec30.setTextColor(Czerwony);
                }


                break;

            case "Piec11":
                Double wyb3 = new Double(message);
                if (wyb3 == 0) {
                    parametr[4] = 0;
                    CheckBox wy0 = (CheckBox) findViewById(R.id.czasowe3);
                    wy0.setChecked(true);
                    wy0.setText("ON");
                    TextView piec13 = (TextView) findViewById(R.id.textpiec5);
                    piec13.setTextColor(Ok);
                    TextView piec30 = (TextView) findViewById(R.id.piec29);
                    piec30.setTextColor(Ok);
                }
                if (wyb3 != 0) {
                    parametr[4] = 1;
                    CheckBox wy0 = (CheckBox) findViewById(R.id.czasowe3);
                    wy0.setChecked(false);
                    wy0.setText("OFF");
                    TextView piec13 = (TextView) findViewById(R.id.textpiec5);
                    piec13.setTextColor(Czerwony);
                    TextView piec30 = (TextView) findViewById(R.id.piec29);
                    piec30.setTextColor(Czerwony);
                }


                break;

            case "Piec30":
                String opuznienie = message;
                zadana5 = new Double(message);
                TextView piec30 = (TextView) findViewById(R.id.piec30);
                piec30.setText(opuznienie + "min");


                break;

        }


        zadana = p13 + (p14 / 100);
        zadana10 = p31 + (p32 / 10);

        String granica = new Double(zadana10).toString();

        wynik = new Double(zadana).toString();
        String wynik1 = new Double(p19).toString();
        zadana2 = p19;
        String wynik2 = new Double(p24).toString();
        zadana3 = p24;
        String wynik3 = new Double(p29).toString();
        zadana4 = p29;
        TextView piec13 = (TextView) findViewById(R.id.piec13);
        piec13.setText(wynik);
        TextView piec31 = (TextView) findViewById(R.id.piec31);
        piec31.setText(granica);
        TextView piec19 = (TextView) findViewById(R.id.piec19);
        piec19.setText(wynik1);
        TextView piec24 = (TextView) findViewById(R.id.piec24);
        piec24.setText(wynik2);
        TextView piec29 = (TextView) findViewById(R.id.piec29);
        piec29.setText(wynik3);

        TextView godzina1 = (TextView) findViewById(R.id.godzina1);
        godzina1.setText(p15 + ":" + p16);
        TextView godzina1b = (TextView) findViewById(R.id.godzina1b);
        godzina1b.setText(p17 + ":" + p18);
        TextView godzina2 = (TextView) findViewById(R.id.godzina2);
        godzina2.setText(p20 + ":" + p21);
        TextView godzina2b = (TextView) findViewById(R.id.godzina2b);
        godzina2b.setText(p22 + ":" + p23);
        TextView godzina3 = (TextView) findViewById(R.id.godzina3);
        godzina3.setText(p25 + ":" + p26);
        TextView godzina3b = (TextView) findViewById(R.id.godzina3b);
        godzina3b.setText(p27 + ":" + p28);


    }


    private void addToHistory(String mainText) {
        System.out.println("LOG: " + mainText);
        // mAdapter.add(mainText);
        Snackbar.make(findViewById(android.R.id.content), mainText, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    public void publishMessage(String publishTopic, String publishMessage) {

        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(publishMessage.getBytes());
            message.setQos(1);
            message.setRetained(true);
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

//

    public void selectpiec(View view) {
        RadioButton wybor1 = (RadioButton) findViewById(R.id.p1);
        RadioButton wybor2 = (RadioButton) findViewById(R.id.p2);
        RadioButton wybor3 = (RadioButton) findViewById(R.id.p3);
        boolean checked = ((RadioButton) view).isChecked();
        int id = view.getId();
        if (checked) {
            if (id == R.id.p1) {
                wybor1.setChecked(true);
                wybor2.setChecked(false);
                wybor3.setChecked(false);
                publishMessage("Piec12", "0");
            } else if (id == R.id.p2) {
                wybor2.setChecked(true);
                wybor1.setChecked(false);
                wybor3.setChecked(false);
                publishMessage("Piec12", "2");
            } else if (id == R.id.p3) {
                wybor3.setChecked(true);
                wybor1.setChecked(false);
                wybor2.setChecked(false);
                publishMessage("Piec12", "1");
            }
        }
    }


    private boolean sprawdzCzyWifi() {

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String ssid = info.getSSID();

        return ssid.contains("MAX");

    }


}







package paho.mqtt.android.example;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
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
    int gaz = 0;
    int woda = 0;
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
    double harmo = 0;
    double awaryjne = 0;
    double awaryjne1 = 0;



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
             if (awaryjne ==0){    publishMessage("Piec0", "1");
                addToHistory("Piec podnosi temperature");}
             else { if (awaryjne1 == 0) { awaryjne =21; publishMessageBB("S7/awaryjne1", "21"); }
                 awaryjne1 = awaryjne1 +0.1;double str = Math.round(awaryjne1 * 10.0) / 10.0; String str2 = String.valueOf(str);;  publishMessage("S7/awaryjne1", str2);

             }



            }
        });
        ImageButton zadminus = (ImageButton) findViewById(R.id.zadanaminus);
        zadminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if (awaryjne == 0){   publishMessage("Piec0", "2");
                addToHistory("Piec obniża temperature");}

             else  { if (awaryjne1 == 0) { awaryjne =21; publishMessageBB("S7/awaryjne1", "21"); }
                 awaryjne1 = awaryjne1 -0.1;double str = Math.round(awaryjne1 * 10.0) / 10.0; String str2 = String.valueOf(str);;  publishMessage("S7/awaryjne1", str2);

             }
            }
            

        });
////uiouoiui-------------------------------------------------------------------------------------------------------




















        ImageButton button6 = (ImageButton) findViewById(R.id.imageButton6);
        button6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // publishMessage("S2/w1",""+w1);
                if (awaryjne == 0) {
                    publishMessageBB("S7/awaryjne", "1");
                    addToHistory("Ogrzewanie awaryjne ON");
                } else {
                    publishMessageBB("S7/awaryjne", "0");
                    addToHistory("Ogrzewanie OFF");
                }

            }

        });
        ImageButton button4 = (ImageButton) findViewById(R.id.imageButton4);
        button4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // publishMessage("S2/w1",""+w1);
                if (woda == 0) {
                    publishMessageBB("S7/awaryjne2", "1");
                    addToHistory("Podgrzewanie wody włączone");woda=1;
                } else {
                    publishMessage("S7/awaryjne2", "0");
                    addToHistory("Podgrzewanie wody wyłączone");woda=0;
                }

            }

        });
        ImageButton button9 = (ImageButton) findViewById(R.id.imageButton9);
        button9.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // publishMessage("S2/w1",""+w1);
                if (harmo == 0) {
                    publishMessageBB("Piec50", "1");
                    addToHistory("Harmonogram włączony");
                } else {
                    publishMessageBB("Piec50", "0");
                    addToHistory("Harmonogram wyłączony");
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

            case "S7/dane11":


                p13 = new Double(message);


                break;

            case "S7/awaryjne1":


                awaryjne1 = new Double(message);


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
            case "S7/awaryjne2":
                double zzmi = new Double(message);
                TextView COstan2 = (TextView) findViewById(R.id.textView27b);
                ImageButton button244 = (ImageButton) findViewById(R.id.imageButton4);
                RadioButton woda = (RadioButton) findViewById(R.id.woda);

                if (zzmi == 0) {
                    COstan2.setText(" ");
                    woda.setVisibility(View.INVISIBLE);
                    button244.setColorFilter(Color.BLACK);
                } else {
                    COstan2.setText("ON");
                    woda.setVisibility(View.VISIBLE);
                    button244.setColorFilter(Color.GREEN);

                }

                break;

            case "Piec50":
                TextView harmonogram = (TextView) findViewById(R.id.textView191);
                TextView harmonogr = (TextView) findViewById(R.id.textpiec22);
                ImageButton button144 = (ImageButton) findViewById(R.id.imageButton9);
                harmo = new Double(message);
                if (harmo == 0) {
                    harmonogram.setText("OFF");harmonogr.setTextColor(Color.YELLOW);
                    button144.setColorFilter(Color.BLACK);


                } else {
                    harmonogram.setText("ON");harmonogr.setTextColor(Color.WHITE);
                    button144.setColorFilter(Color.GREEN);
                }

                break;

            case "S1/dane7":

                TextView temp7 = (TextView) findViewById(R.id.zadana1);

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
                TextView temp8 = (TextView) findViewById(R.id.zadana2);
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

            case "S7/dodatkowe":
                TextView temp18 = (TextView) findViewById(R.id.zadana3);
                temp18.setText(message + "`C");


                double zmienna18 = new Double(message);
                temp18.setTextColor(Color.RED);
                if (zmienna18 >= 15) {
                    temp18.setTextColor(Color.YELLOW);
                }
                if (zmienna18 >= 20) {
                    temp18.setTextColor(Color.GREEN);

                }
                break;
            case "S2/dane0":
                double zmi3 = new Double(message);
                if (zmi3 == 0) {
                    RadioButton piec = (RadioButton) findViewById(R.id.piec);
                    piec.setVisibility(View.INVISIBLE);

                } else {
                    RadioButton piec = (RadioButton) findViewById(R.id.piec);
                    piec.setVisibility(View.VISIBLE);
                }

                break;


            case "S7/awaryjne":
                TextView COstan = (TextView) findViewById(R.id.textView27);
                awaryjne = new Double(message);
                ImageButton button44 = (ImageButton) findViewById(R.id.imageButton6);
                if (awaryjne == 0) {
                    COstan.setText("  ");
                    button44.setColorFilter(Color.BLACK);



                    FrameLayout T1 = (FrameLayout) findViewById(R.id.T1);



                }
                if (awaryjne == 1) {
                    COstan.setText("ON");
                    button44.setColorFilter(Color.GREEN);
                    publishMessageBB("S7/w22", "0");

                    FrameLayout T1 = (FrameLayout) findViewById(R.id.T1);
                    T1.setVisibility(View.VISIBLE);


                }
                break;

            case "S2/dane9":
                TextView temp118 = (TextView) findViewById(R.id.zadana0);
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



            case "Piec12":


                p12 = new Double(message);
                RadioButton wybor1 = (RadioButton) findViewById(R.id.p1);
                RadioButton wybor2 = (RadioButton) findViewById(R.id.p2);
                RadioButton wybor3 = (RadioButton) findViewById(R.id.p3);
                RadioButton wybor4 = (RadioButton) findViewById(R.id.p4);

                if (p12 == 1) {
                    wybor3.setChecked(true);
                    wybor2.setChecked(false);
                    wybor1.setChecked(false);
                    wybor4.setChecked(false);
                }
                ;
                if (p12 == 2) {
                    wybor2.setChecked(true);
                    wybor3.setChecked(false);
                    wybor1.setChecked(false);
                    wybor4.setChecked(false);
                }
                ;
                if (p12 == 0) {
                    wybor1.setChecked(true);
                    wybor2.setChecked(false);
                    wybor3.setChecked(false);
                    wybor4.setChecked(false);
                }
                ;
                if (p12 == 3) {
                    wybor4.setChecked(true);
                    wybor2.setChecked(false);
                    wybor3.setChecked(false);
                    wybor1.setChecked(false);
                }
                ;



                break;






        }








        TextView piec13 = (TextView) findViewById(R.id.piec13);
       if (awaryjne ==0) {wynik = new Double(p13).toString();} else {wynik = String.format("%.1f", awaryjne1);}
        piec13.setText(wynik);

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


    public void publishMessageBB(String publishTopic, String publishMessage) {

        try {
            MqttMessage message = new MqttMessage();
            message.setQos(1);           // QoS 1
            message.setRetained(true);   // Retain = true
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
//

    public void selectpiec(View view) {
        RadioButton wybor1 = (RadioButton) findViewById(R.id.p1);
        RadioButton wybor2 = (RadioButton) findViewById(R.id.p2);
        RadioButton wybor3 = (RadioButton) findViewById(R.id.p3);
        RadioButton wybor4 = (RadioButton) findViewById(R.id.p4);
        boolean checked = ((RadioButton) view).isChecked();
        int id = view.getId();
        if (checked) {
            if (id == R.id.p1) {
                wybor1.setChecked(true);
                wybor2.setChecked(false);
                wybor3.setChecked(false);
                wybor4.setChecked(false);
                publishMessage("Piec12", "0");
            } else if (id == R.id.p2) {
                wybor2.setChecked(true);
                wybor1.setChecked(false);
                wybor3.setChecked(false);
                wybor4.setChecked(false);
                publishMessage("Piec12", "2");
            } else if (id == R.id.p3) {
                wybor3.setChecked(true);
                wybor1.setChecked(false);
                wybor2.setChecked(false);
                wybor4.setChecked(false);
                publishMessage("Piec12", "1");
            } else if (id == R.id.p4) {
                wybor4.setChecked(true);
                wybor1.setChecked(false);
                wybor2.setChecked(false);
                wybor3.setChecked(false);
                publishMessage("Piec12", "4");
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








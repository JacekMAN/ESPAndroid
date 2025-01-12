package paho.mqtt.android.example;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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


/**
 * Created by DOM on 18.01.2017.
 */

public class Sterowanie extends AppCompatActivity {


    MqttAndroidClient mqttAndroidClient;
    Commons commons = new Commons();

    String clientId = "";
    final String subscriptionTopic = "#";
    int wiata = 10;
    int lampa = 10;
    int podlewa = 10;
    int rolety = 0; // stan

    int podswietlanie = 0;
    int w1 = 0;
    int wynik; // obliczenie procentowe wody
    int Swynik; // obliczenie procentowe wody solary
    SharedPreferences Baza;
    SharedPreferences.Editor EBaza;

    boolean garazOtwarty1 = false;
    boolean garazOtwarty2 = false;
    String serverUri = "";


    // Intent i= new Intent(PahoExampleActivity.this ,Setting.class);


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sterowanie);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Baza = getSharedPreferences("jacek", Context.MODE_PRIVATE);
        EBaza = Baza.edit();
        //EBaza.putString("local","Ala");
        //EBaza.commit();
        final TextView rodzaj = (TextView) findViewById(R.id.textView2);


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

        ImageButton brama = (ImageButton) findViewById(R.id.Brama);
        brama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishMessage("S4/w1", "1");
                addToHistory("BRAMA !!!!");
                final ImageButton button44 = (ImageButton) findViewById(R.id.imageButton44);

                button44.setColorFilter(Color.GREEN);
                final Handler handler = new Handler();       //  =============================
                handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                    @Override                                // czasu 400 - 400mili sekund
                    public void run() {                      //
                        button44.setColorFilter(Color.GRAY); // < ta funkcja jest wykonywana
                    }                                        //   po zadanej przerwie
                }, 400);                                     // <  czas przerwy
            }
        });

        ImageButton UP = (ImageButton) findViewById(R.id.UP);
        UP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rolety ==1){
                    publishMessage("S8/dane1", "6");
                    addToHistory("stop  !!!!");rolety=2;}

                if (rolety ==0){
                publishMessage("S8/dane1", "4");
                addToHistory("Rolety do góry !!!!");rolety=1;}
                if (rolety ==2) {rolety=0;}


                final ImageButton button414 = (ImageButton) findViewById(R.id.imageButton414);

                button414.setColorFilter(Color.GREEN);
                final Handler handler = new Handler();       //  =============================
                handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                    @Override                                // czasu 400 - 400mili sekund
                    public void run() {                      //
                        button414.setColorFilter(Color.GRAY); // < ta funkcja jest wykonywana
                    }                                        //   po zadanej przerwie
                }, 400);                                     // <  czas przerwy
            }
        });

        ImageButton DOWN = (ImageButton) findViewById(R.id.DOWN);
        DOWN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rolety ==1) {
                    publishMessage("S8/dane1", "6");
                    addToHistory("STOP !!!!");rolety=2;}


                if (rolety ==0) {
                publishMessage("S8/dane1", "5");
                addToHistory("Rolety na dół !!!!");rolety=1;}
                final ImageButton button414 = (ImageButton) findViewById(R.id.imageButton414);


                if (rolety ==2) {rolety=0;}
                button414.setColorFilter(Color.GREEN);
                final
                Handler handler = new Handler();       //  =============================
                handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                    @Override                                // czasu 400 - 400mili sekund
                    public void run() {                      //
                        button414.setColorFilter(Color.GRAY); // < ta funkcja jest wykonywana
                    }                                        //   po zadanej przerwie
                }, 400);                                     // <  czas przerwy
            }
        });

        ImageButton lampa2 = (ImageButton) findViewById(R.id.lampa);
        lampa2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if (lampa == 0) {
                    publishMessage("S9/w1", "1");
                    addToHistory("WJAZD - oświetlenie -  włączona");
                } else {
                    publishMessage("S9/w1", "0");
                    addToHistory("WJAZD - oświetlenie -  wyłączona");
                }

            }

        });

        ImageButton lampa33 = (ImageButton) findViewById(R.id.lampa33);
        lampa33.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if (lampa == 0) {
                    publishMessage("S6/w1", "1");
                    addToHistory("Lampa za domem - włączona");
                } else {
                    publishMessage("S6/w1", "0");
                    addToHistory("Lampa za domem - wyłączona");
                }

            }

        });

        ImageButton lampa102a = (ImageButton) findViewById(R.id.lampa102);
        lampa102a.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if (podlewa == 0) {
                    publishMessage("S9/w2", "0");
                    addToHistory("Podlewanie - włączone");
                } else {
                    publishMessage("S9/w2", "1");
                    addToHistory("Podlewanie - wyłączone");
                }

            }

        });


        ImageButton gara1 = (ImageButton) findViewById(R.id.garaz11);
        gara1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishMessage("S5/w1", "1");
                addToHistory("Garaż z prawej !!!!");
                final ImageButton button55 = (ImageButton) findViewById(R.id.imageButton55);

                button55.setColorFilter(Color.GREEN);
                final Handler handler = new Handler();       //  =============================
                handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                    @Override                                // czasu 400 - 400mili sekund
                    public void run() {
                        if (!garazOtwarty1) {
                            button55.setColorFilter(Color.GRAY);

                        } else {
                            button55.setColorFilter(Color.RED);
                        }
                    }                                        //   po zadanej przerwie
                }, 400);                                     // <  czas przerwy
            }
        });

        ImageButton gara2 = (ImageButton) findViewById(R.id.garaz22);
        gara2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishMessage("S5/w2", "1");
                addToHistory("Garaż z lewj  !!!!");
                final ImageButton button66 = (ImageButton) findViewById(R.id.imageButton66);

                button66.setColorFilter(Color.GREEN);
                final Handler handler = new Handler();       //  =============================
                handler.postDelayed(new Runnable() {         //  Funkcja wykonywana op upływie
                    @Override                                // czasu 400 - 400mili sekund
                    public void run() {
                        if (!garazOtwarty2) {
                            button66.setColorFilter(Color.GRAY); // < ta funkcja jest wykonywana
                        } else {
                            button66.setColorFilter(Color.RED);
                        }
                    }                                        //   po zadanej przerwie
                }, 400);                                     // <  czas przerwy
            }
        });


        ImageButton button = (ImageButton) findViewById(R.id.ustawienia);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        ImageButton button2 = (ImageButton) findViewById(R.id.lampa331);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Sterowanie.this, Alarmy.class);
                startActivity(i);
            }
        });


        ImageButton lampawiata = (ImageButton) findViewById(R.id.wiata);
        lampawiata.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // publishMessage("S2/w1",""+w1);
                if (wiata == 0) {
                    publishMessage("S3/w1", "1");
                    addToHistory("Lampa wiata -  włączona");
                } else {
                    publishMessage("S3/w1", "0");
                    addToHistory("Lampa wiata -  wyłączona");
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
                    ImageButton pol = (ImageButton) findViewById(R.id.imageButton);
                }
            });


        } catch (MqttException ex) {
            ex.printStackTrace();
        }

    }


    //Wyswietlanie wiadomosci
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void dosalemWiadomosc(String topic, String message) {


        switch (topic) {

            case "S5/dane1":
                TextView otw1 = (TextView) findViewById(R.id.otwarte1);
                ImageButton button55 = (ImageButton) findViewById(R.id.imageButton55);
                //
                double otw11 = new Double(message);
                if (otw11 == 0) {
                    otw1.setText(" ");
                    button55.setColorFilter(Color.GRAY);
                    garazOtwarty1 = false;
                }
                if (otw11 == 1) {
                    garazOtwarty1 = true;
                    otw1.setText("OTWARTE");
                    button55.setColorFilter(Color.RED);
                }
                break;

            case "S5/dane2":
                TextView otw2 = (TextView) findViewById(R.id.otwarte2);
                ImageButton button66 = (ImageButton) findViewById(R.id.imageButton66);
                double otw22 = new Double(message);
                if (otw22 == 0) {
                    garazOtwarty2 = false;
                    otw2.setText(" ");
                    button66.setColorFilter(Color.GRAY);
                }
                if (otw22 == 1) {
                    garazOtwarty2 = true;
                    otw2.setText("OTWARTE");
                    button66.setColorFilter(Color.RED);
                }
                break;


            case "A0/dane0":

                ImageButton button666 = (ImageButton) findViewById(R.id.imageButton331);


                double ala = new Double(message);
                if (ala != 0) {
                    button666.setColorFilter(Color.RED);
                }
                if (ala != 0) {
                    button666.setColorFilter(Color.RED);
                } else {
                    button666.setColorFilter(Color.GRAY);
                }

                break;

            case "S9/w22":

                ImageButton podle = (ImageButton) findViewById(R.id.imageButton103);


                double alan = new Double(message);
                if (alan != 0) {
                    podle.setColorFilter(Color.RED);
                    podlewa = 0;
                    TextView pod2 = (TextView) findViewById(R.id.textView101);
                    pod2.setText("OFF");
                }
                if (alan != 0) {
                    podle.setColorFilter(Color.RED);
                } else {
                    podle.setColorFilter(Color.GREEN);
                    podlewa = 1;
                    TextView pod2 = (TextView) findViewById(R.id.textView101);
                    pod2.setText("ON");
                }

                break;

            case "S3/dane0":                                                 // wiata czujnik 3
                TextView wiataczas = (TextView) findViewById(R.id.textView7b);
                double zmienna20 = new Double(message);
                ImageButton button77 = (ImageButton) findViewById(R.id.imageButton77);

                if (zmienna20 == 0) {
                    wiataczas.setText(" ");
                    button77.setColorFilter(Color.GRAY);
                    wiata = 0;
                }
                if (zmienna20 > 0) {
                    wiataczas.setText(message + " min");
                    button77.setColorFilter(Color.GREEN);
                    wiata = 1;
                }


                break;


            case "S3/dane2":

                TextView czekaj = (TextView) findViewById(R.id.czekaj);
                czekaj.setVisibility(View.INVISIBLE);
                FrameLayout brama = (FrameLayout) findViewById(R.id.brama);
                brama.setVisibility(View.VISIBLE);
                FrameLayout rolety = (FrameLayout) findViewById(R.id.rolety);
                rolety.setVisibility(View.VISIBLE);
                FrameLayout gar1 = (FrameLayout) findViewById(R.id.garaz1);
                gar1.setVisibility(View.VISIBLE);
                FrameLayout gar2 = (FrameLayout) findViewById(R.id.garaz2);
                gar2.setVisibility(View.VISIBLE);
                FrameLayout wiata = (FrameLayout) findViewById(R.id.wiata2);
                wiata.setVisibility(View.VISIBLE);
                FrameLayout wjazd = (FrameLayout) findViewById(R.id.wjazd);
                wjazd.setVisibility(View.VISIBLE);
                FrameLayout wjazd2 = (FrameLayout) findViewById(R.id.wjazd2);
                wjazd2.setVisibility(View.VISIBLE);
                FrameLayout podlew = (FrameLayout) findViewById(R.id.podlewanie);
                podlew.setVisibility(View.VISIBLE);

                FrameLayout alarm = (FrameLayout) findViewById(R.id.alarm);
                alarm.setVisibility(View.VISIBLE);


                break;


            case "S9/dane0" +
                         "":                                                 // wiata czujnik 3
                TextView lampaczas = (TextView) findViewById(R.id.textView8b);
                double zmienna30 = new Double(message);
                ImageButton lampabutton = (ImageButton) findViewById(R.id.imageButton88);

                if (zmienna30 == 0) {
                    lampaczas.setText(" ");
                    lampabutton.setColorFilter(Color.GRAY);
                    lampa = 0;
                }
                if (zmienna30 > 0) {
                    lampaczas.setText(message + " min");
                    lampabutton.setColorFilter(Color.GREEN);
                    lampa = 1;
                }

                break;

            case "S6/dane0":                                                 // wiata czujnik 3
                TextView lampaczas2 = (TextView) findViewById(R.id.textView33b);
                double zmienna300 = new Double(message);
                ImageButton lampabutton2 = (ImageButton) findViewById(R.id.imageButton3388);

                if (zmienna300 == 0) {
                    lampaczas2.setText(" ");
                    lampabutton2.setColorFilter(Color.GRAY);
                    lampa = 0;
                }
                if (zmienna300 > 0) {
                    lampaczas2.setText(message + " min");
                    lampabutton2.setColorFilter(Color.GREEN);
                    lampa = 1;
                }


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
//    public Action getIndexApiAction() {
//        Thing object = new Thing.Builder()
//                .setName("PahoExample Page") // TODO: Define a title for the content shown.
//                // TODO: Make sure this auto-generated URL is correct.
//                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
//                .build();
//        return new Action.Builder(Action.TYPE_VIEW)
//                .setObject(object)
//                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
//                .build();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        AppIndex.AppIndexApi.start(client, getIndexApiAction());
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        AppIndex.AppIndexApi.end(client, getIndexApiAction());
//        client.disconnect();
//    }
    private boolean sprawdzCzyWifi() {

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String ssid = info.getSSID();

        return ssid.contains("MAX");

    }


}

package paho.mqtt.android.example;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Alarmy extends AppCompatActivity {


    SharedPreferences Baza;
    SharedPreferences.Editor EBaza;
    String clientId = "";
    String serverUri = "";

    final String subscriptionTopic = "#";

    private GoogleApiClient client;

    MqttAndroidClient mqttAndroidClient;
    Commons commons = new Commons();

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.alarmy);


        ImageButton button = (ImageButton) findViewById (R.id.powr);
        button.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                finish ();
            }
        });


        getWindow ().addFlags (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Baza = getSharedPreferences ("jacek", Context.MODE_PRIVATE);
        EBaza = Baza.edit ();
        //EBaza.putString("local","Ala");
        //EBaza.commit();
        final TextView rodzaj = (TextView) findViewById (R.id.lokal);


        if (Baza.getString ("lacze", "1").equals ("2")) {
            clientId = (Baza.getString ("nazwa", "") + commons.generujLosoweZnaki (10));
            serverUri = "tcp://" + Baza.getString ("local", "") + ":" + Baza.getString ("port", "");
            rodzaj.setText ("lokalne");
        } else {
            clientId = (Baza.getString ("nazwa", "") + commons.generujLosoweZnaki (10));
            serverUri = "tcp://" + Baza.getString ("internet", "") + ":" + Baza.getString ("port", "");
            EBaza.putString ("awaryjne", "0");
            rodzaj.setText ("zdalne");
            EBaza.commit ();
        }


        FrameLayout walarm = (FrameLayout) findViewById (R.id.wylaczalarm);
        walarm.setOnClickListener (new View.OnClickListener () {

                @Override
                public void onClick(View view) {
                    publishMessage("A0/dane0", "0");
                    addToHistory("Alarm wyłączony  !!!!");

                        //  =============================
                            //  Funkcja wykonywana op upływie
                                                       // czasu 400 - 400mili sekund
                                                         //   po zadanej przerwie
                                                        // <  czas przerwy
                }
            });





        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended () {
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
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener () {
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    public void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 1, null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            // addToHistory("Połączono z serwerem");


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




    public void lacze2 (View view)
    {

        RadioButton walarm1 = (RadioButton) findViewById(R.id.a1);
        RadioButton walarm2 = (RadioButton) findViewById(R.id.a2);
        RadioButton walarm3 = (RadioButton) findViewById(R.id.a3);
        RadioButton walarm4 = (RadioButton) findViewById(R.id.a4);
        RadioButton walarm5 = (RadioButton) findViewById(R.id.a5);
        RadioButton walarm6 = (RadioButton) findViewById(R.id.a6);

        boolean checked = ((RadioButton) view).isChecked();
        switch ( view.getId())
        {
            case R.id.a1:
                if(checked) {
                    publishMessage("A0/dane0", "7");
                    addToHistory("Cały dom  !!!!");
                    walarm1.setChecked(true);
                    walarm2.setChecked(false);
                    walarm3.setChecked(false);
                    walarm4.setChecked(false);
                    walarm5.setChecked(false);
                    walarm6.setChecked(false);
                }
                break;
            case R.id.a2:
                if(checked) {
                    publishMessage("A0/dane0", "2");
                    addToHistory("Piwnica + garaż !!!!");
                    walarm1.setChecked(false);
                    walarm2.setChecked(true);
                    walarm3.setChecked(false);
                    walarm4.setChecked(false);
                    walarm5.setChecked(false);
                    walarm6.setChecked(false);
                }
                break;
            case R.id.a3:
                if(checked) {
                    publishMessage("A0/dane0", "4");
                    addToHistory("Cały dom (telefon)  !!!!");
                    walarm1.setChecked(false);
                    walarm2.setChecked(false);
                    walarm3.setChecked(true);
                    walarm4.setChecked(false);
                    walarm5.setChecked(false);
                    walarm6.setChecked(false);
                }
                break;
            case R.id.a4:
                if(checked) {
                    publishMessage("A0/dane0", "3");
                    addToHistory("Piwnica + parter  !!!!");
                    walarm1.setChecked(false);
                    walarm2.setChecked(false);
                    walarm3.setChecked(false);
                    walarm4.setChecked(true);
                    walarm5.setChecked(false);
                    walarm6.setChecked(false);
                }
                break;
            case R.id.a5:
                if(checked) {
                    publishMessage("A0/dane0", "5");
                    addToHistory("Piętro  (telefon)  !!!!");
                    walarm1.setChecked(false);
                    walarm2.setChecked(false);
                    walarm3.setChecked(false);
                    walarm4.setChecked(false);
                    walarm5.setChecked(true);
                    walarm6.setChecked(false);
                }
                break;
            case R.id.a6:
                if(checked) {
                    publishMessage("A0/dane0", "6");
                    addToHistory("Piętro  !!!!");
                    walarm1.setChecked(false);
                    walarm2.setChecked(false);
                    walarm3.setChecked(false);
                    walarm4.setChecked(false);
                    walarm5.setChecked(false);
                    walarm6.setChecked(true);
                }
                break;





        }
        //EBaza.commit();

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

            case "A0/dane9":
                TextView ostatni_alarm = (TextView) findViewById(R.id.textView4b);
                TextView ostatni_alarm2 = (TextView) findViewById(R.id.textView4a);
               String ostatni = message.substring(2);
                String ostatni2 = message.substring(0,1);
                double ostatni3 = new Double(ostatni2);
                String wybor=" ";
                if (ostatni3 == 1) { wybor="hol";}
                if (ostatni3 == 2) { wybor="Klatka schod.";}
                if (ostatni3 == 3) { wybor="Piwnica duża";}
                if (ostatni3 == 4) { wybor="Piwnica hol";}
                if (ostatni3 == 5) { wybor="Piwnica pokój";}
                if (ostatni3 == 6) { wybor="Garaż";}
                if (ostatni3 == 7) { wybor="Parter";}
                if (ostatni3 == 8) { wybor="Piętro";}



                ostatni_alarm.setText(ostatni);
                ostatni_alarm2.setText(wybor);
                //

                break;

            case "A0/dane1":
                TextView alarm1 = (TextView) findViewById(R.id.alarmowy01b);
                ImageButton alarm1a = (ImageButton) findViewById(R.id.imageAlarm1);
                String alarm01 = message.substring(2);
                String alarm01a = message.substring(0,1);
                double alarm01b = new Double(alarm01a);
                if (alarm01b == 0) { alarm1a.setColorFilter(Color.GRAY);}
                if (alarm01b == 1) { alarm1a.setColorFilter(Color.GREEN);}
                alarm1.setText(alarm01);
                break;

            case "A0/dane2":
                TextView alarm2 = (TextView) findViewById(R.id.alarmowy02b);
                ImageButton alarm2a = (ImageButton) findViewById(R.id.imageAlarm2);
                String alarm02 = message.substring(2);
                String alarm02a = message.substring(0,1);
                double alarm02b = new Double(alarm02a);
                if (alarm02b == 0) { alarm2a.setColorFilter(Color.GRAY);}
                if (alarm02b == 1) { alarm2a.setColorFilter(Color.GREEN);}
                alarm2.setText(alarm02);
                break;

            case "A0/dane3":
                TextView alarm3 = (TextView) findViewById(R.id.alarmowy03b);
                ImageButton alarm3a = (ImageButton) findViewById(R.id.imageAlarm3);
                String alarm03 = message.substring(2);
                String alarm03a = message.substring(0,1);
                double alarm03b = new Double(alarm03a);
                if (alarm03b == 0) { alarm3a.setColorFilter(Color.GRAY);}
                if (alarm03b == 1) { alarm3a.setColorFilter(Color.GREEN);}
                alarm3.setText(alarm03);
                break;

            case "A0/dane4":
                TextView alarm4 = (TextView) findViewById(R.id.alarmowy04b);
                ImageButton alarm4a = (ImageButton) findViewById(R.id.imageAlarm4);
                String alarm04 = message.substring(2);
                String alarm04a = message.substring(0,1);
                double alarm04b = new Double(alarm04a);
                if (alarm04b == 0) { alarm4a.setColorFilter(Color.GRAY);}
                if (alarm04b == 1) { alarm4a.setColorFilter(Color.GREEN);}
                alarm4.setText(alarm04);
                break;

            case "A0/dane5":
                TextView alarm5 = (TextView) findViewById(R.id.alarmowy05b);
                ImageButton alarm5a = (ImageButton) findViewById(R.id.imageAlarm5);
                String alarm05 = message.substring(2);
                String alarm05a = message.substring(0,1);
                double alarm05b = new Double(alarm05a);
                if (alarm05b == 0) { alarm5a.setColorFilter(Color.GRAY);}
                if (alarm05b == 1) { alarm5a.setColorFilter(Color.GREEN);}
                alarm5.setText(alarm05);
                break;

            case "A0/dane6":
                TextView alarm6= (TextView) findViewById(R.id.alarmowy06b);
                ImageButton alarm6a = (ImageButton) findViewById(R.id.imageAlarm6);
                String alarm06 = message.substring(2);
                String alarm06a = message.substring(0,1);
                double alarm06b = new Double(alarm06a);
                if (alarm06b == 0) { alarm6a.setColorFilter(Color.GRAY);}
                if (alarm06b == 1) { alarm6a.setColorFilter(Color.GREEN);}
                alarm6.setText(alarm06);
                break;

            case "A0/dane7":
                TextView alarm7= (TextView) findViewById(R.id.alarmowy07b);
                ImageButton alarm7a = (ImageButton) findViewById(R.id.imageAlarm7);
                String alarm07 = message.substring(2);
                String alarm07a = message.substring(0,1);
                double alarm07b = new Double(alarm07a);
                if (alarm07b == 0) { alarm7a.setColorFilter(Color.GRAY);}
                if (alarm07b == 1) { alarm7a.setColorFilter(Color.GREEN);}
                alarm7.setText(alarm07);
                break;

            case "A0/dane8":
                TextView alarm8 = (TextView) findViewById(R.id.alarmowy08b);
                ImageButton alarm8a = (ImageButton) findViewById(R.id.imageAlarm8);
                String alarm08 = message.substring(2);
                String alarm08a = message.substring(0,1);
                double alarm08b = new Double(alarm08a);
                if (alarm08b == 0) { alarm8a.setColorFilter(Color.GRAY);}
                if (alarm08b == 1) { alarm8a.setColorFilter(Color.GREEN);}
                alarm8.setText(alarm08);
                break;


            case "A0/dane0":

            FrameLayout wylala = (FrameLayout) findViewById(R.id.wylaczalarm);
                RadioButton walarm1 = (RadioButton) findViewById(R.id.a1);
                RadioButton walarm2 = (RadioButton) findViewById(R.id.a2);
                RadioButton walarm3 = (RadioButton) findViewById(R.id.a3);
                RadioButton walarm4 = (RadioButton) findViewById(R.id.a4);
                RadioButton walarm5 = (RadioButton) findViewById(R.id.a5);
                RadioButton walarm6 = (RadioButton) findViewById(R.id.a6);


                double ala = new Double(message);
                if (ala != 0) {wylala.setVisibility(View.VISIBLE);}
                else  {wylala.setVisibility(View.INVISIBLE);walarm1.setChecked(false);
                                    walarm2.setChecked(false);
                                    walarm3.setChecked(false);
                                    walarm4.setChecked(false);
                                    walarm5.setChecked(false);
                                    walarm6.setChecked(false);}
                if (ala ==4){walarm1.setChecked(false);
                             walarm2.setChecked(false);
                             walarm3.setChecked(true);
                             walarm4.setChecked(false);
                             walarm5.setChecked(false);
                             walarm6.setChecked(false);}

                if (ala ==5){walarm1.setChecked(false);
                    walarm2.setChecked(false);
                    walarm3.setChecked(false);
                    walarm4.setChecked(false);
                    walarm5.setChecked(true);
                    walarm6.setChecked(false);}
                if (ala ==7){walarm1.setChecked(true);
                    walarm2.setChecked(false);
                    walarm3.setChecked(false);
                    walarm4.setChecked(false);
                    walarm5.setChecked(false);
                    walarm6.setChecked(false);}
                if (ala ==2){walarm1.setChecked(false);
                    walarm2.setChecked(true);
                    walarm3.setChecked(false);
                    walarm4.setChecked(false);
                    walarm5.setChecked(false);
                    walarm6.setChecked(false);}
                if (ala ==3){walarm1.setChecked(false);
                    walarm2.setChecked(false);
                    walarm3.setChecked(false);
                    walarm4.setChecked(true);
                    walarm5.setChecked(false);
                    walarm6.setChecked(false);}
                if (ala ==6){walarm1.setChecked(false);
                    walarm2.setChecked(false);
                    walarm3.setChecked(false);
                    walarm4.setChecked(false);
                    walarm5.setChecked(false);
                    walarm6.setChecked(true);}

                break;
        }
    }



}

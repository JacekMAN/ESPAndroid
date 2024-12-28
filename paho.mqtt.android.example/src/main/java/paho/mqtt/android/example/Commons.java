package paho.mqtt.android.example;

import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by DOM on 18.03.2017.
 */

public class Commons {
    public String generujLosoweZnaki(int ile) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPRSTUWXYZ".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < ile; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);

        }
        return sb.toString();

    }

    public static void getPromieniowanie(final Miasto miasto, final TextView textView , final Context context, String code) {

        String url = "http://radioactiveathome.org/boinc/gettrickledata.php?start=0&hostid="+code;
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] linie = response.split("\\n");
                String[] wyniki = linie[linie.length - 1].split(",");
                if(wyniki.length>6) {
                    Double wynik = new Double (wyniki[2]);
                    Double czas = new Double (wyniki[6]);
                    wynik = (wynik / czas) / 171.232876;
                    DecimalFormat twoDForm = new DecimalFormat ("##.## uSv/h");
                    miasto.setValue (twoDForm.format (wynik));
                    textView.setText (twoDForm.format (wynik).replace (",", "."));
                    FrameLayout prom = (FrameLayout) textView.getParent ();


                    prom.setBackgroundColor (Color.rgb (199, 45, 45));


                    if (wynik < 0.8) {
                        prom.setBackgroundColor (Color.parseColor ("#fddd08"));
                    }

                    if (wynik < 0.3) {
                        prom.setBackgroundColor (Color.rgb (31, 135, 45));
                    }
                }else{
                    textView.setText ("brak danych");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }


}

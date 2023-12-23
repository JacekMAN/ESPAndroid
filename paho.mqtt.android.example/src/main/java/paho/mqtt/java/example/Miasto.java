package paho.mqtt.java.example;

import android.content.Context;
import android.text.NoCopySpan;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by DOM on 13.04.2017.
 */

public class Miasto {
    private String name;
    private String code;
    private String value;

    public Miasto(String name, String code, String value) {
        this.name = name;
        this.code = code;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void odczytajWartosc(Context context, TextView textView){
        Commons.getPromieniowanie (this,textView,context,code);
    }
}

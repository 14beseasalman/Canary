package co.asad.canary;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;


public class Uuid {
    static public final String CANARY_PREFS = "CANARY_PREFS";

    static public String getUUID(Context ctxt) {
        if(ctxt==null)
            return "0";
        SharedPreferences preferences = ctxt.getSharedPreferences(CANARY_PREFS, MODE_PRIVATE);

        if (!preferences.contains("uuid")) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("uuid", UUID.randomUUID().toString());
            editor.commit();
        }
        return preferences.getString("uuid", "0");
    }

    private Uuid() {
    }
}

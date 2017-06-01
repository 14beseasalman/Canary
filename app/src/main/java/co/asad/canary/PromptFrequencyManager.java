package co.asad.canary;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PromptFrequencyManager {
    private PromptFrequencyManager() {
    }


    static public final String CANARY_PREFS = "CANARY_PREFS";

    static public Float getTimeMinutes(Context ctxt) {

        SharedPreferences preferences = ctxt.getSharedPreferences(CANARY_PREFS, MODE_PRIVATE);
        return preferences.getFloat("prompt_freq", 3);
    }

    public static void setTimeMinutes(Context ctxt, Float timeMinutes) {
        SharedPreferences preferences = ctxt.getSharedPreferences(CANARY_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("prompt_freq", timeMinutes);
        editor.commit();

    }

    static public Float getTimeMilliseconds(Context ctxt) {
        return getTimeMinutes(ctxt) * 60000;
    }
}

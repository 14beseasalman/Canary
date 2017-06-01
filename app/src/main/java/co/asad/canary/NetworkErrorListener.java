package co.asad.canary;

import android.util.Log;

import com.android.volley.VolleyError;


public class NetworkErrorListener implements com.android.volley.Response.ErrorListener {
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("Error.Response", error.toString());
    }
}

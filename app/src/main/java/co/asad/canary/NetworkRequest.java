package co.asad.canary;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;



public class NetworkRequest extends StringRequest {
    private String action;

    private String uuid;
    //static final String SERVER_ENDPOINT = "https://canary-asaddev.rhcloud.com/canary.php";
    //static final String SERVER_ENDPOINT = "http://192.168.1.5/a.php";
    static final String SERVER_ENDPOINT = "http://httpbin.org/post";
    public NetworkRequest(Response.Listener<String> listener, String action, String uuid) {
        super(Request.Method.POST,SERVER_ENDPOINT, listener, new NetworkErrorListener());
        this.action = action;
        this.uuid = uuid;

    }


    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("action", action);
        params.put("id", uuid);
        return params;
    }
}

package co.asad.canary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class KeepAliveDialog extends Activity implements SweetAlertDialog.OnSweetClickListener {

    RequestQueue queue;
    StringRequest updateRequest;
    public static boolean IS_OFF = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (IS_OFF)
            finish();
        setContentView(R.layout.activity_keep_alive_dialog);
        queue = Volley.newRequestQueue(this);
        updateRequest = new NetworkRequest(
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                    }
                }, "ON", Uuid.getUUID(KeepAliveDialog.this));

        new SweetAlertDialog(KeepAliveDialog.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you okay?")
                .setConfirmText("Yes, I'm Good!")
                .setConfirmClickListener(this).show();
    }


    @Override
    public void onClick(SweetAlertDialog sDialog) {
        finish();
        sDialog.dismissWithAnimation();
        queue.add(updateRequest);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(KeepAliveDialog.this, KeepAliveDialog.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }, PromptFrequencyManager.getTimeMilliseconds(KeepAliveDialog.this).longValue());
    }
}
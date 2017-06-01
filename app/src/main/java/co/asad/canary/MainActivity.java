package co.asad.canary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ToggleSwitch.OnToggleSwitchChangeListener {

    RequestQueue queue;


    ScheduledExecutorService executor;
    @BindView(R.id.toggle_switch)
    ToggleSwitch toggle;
    @BindView(R.id.head)
    TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        signUpIfNeeded();
        toggle.setOnToggleSwitchChangeListener(this);
        queue = Volley.newRequestQueue(this);
    }

    @Override
    public void onToggleSwitchChangeListener(int position, boolean isChecked) {
        int ON = 1, OFF = 0;
        if (position == ON) {
            heading.setText(getString(R.string.stop_service));
            executor = Executors.newScheduledThreadPool(1);
            StringRequest startRequest = new NetworkRequest(
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            KeepAliveDialog.IS_OFF = false;
                            Intent i = new Intent(MainActivity.this, KeepAliveDialog.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    }, "ON", Uuid.getUUID(MainActivity.this));

            queue.add(startRequest);

        } else if (position == OFF) {
            heading.setText(getString(R.string.start_service));
            queue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
            StringRequest stopRequest = new NetworkRequest(
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response);
                            executor.shutdown();
                            KeepAliveDialog.IS_OFF = true;

                        }
                    }, "OFF", Uuid.getUUID(MainActivity.this));
            for (int i = 0; i < 5; ++i)
                queue.add(stopRequest);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_time:
                displayFrequencyPrompt();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void signUpIfNeeded() {
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun)
            startActivity(new Intent(MainActivity.this, SignupActivity.class));

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();
    }

    void displayFrequencyPrompt() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        userInputDialogEditText.setText("" + PromptFrequencyManager.getTimeMinutes(this));
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        try {
                            Float userInputTime = Float.valueOf(userInputDialogEditText.getText().toString());
                            PromptFrequencyManager.setTimeMinutes(MainActivity.this, userInputTime);
                        } catch (NumberFormatException e) {
                            Toast.makeText(MainActivity.this, "Not a Valid Number, try again!", Toast.LENGTH_SHORT).show();
                        }

                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }
}

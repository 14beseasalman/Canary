package co.asad.canary;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends Activity implements View.OnClickListener {
    @BindView(R.id.your_name)
    EditText yourName;

    @BindView(R.id.em_contact_name)
    EditText emContactName;

    @BindView(R.id.em_contact_number)
    EditText emContactNumber;

    @BindView(R.id.select_contact_btn)
    Button selectContactBtn;

    @BindView(R.id.submit_btn)
    Button submitBtn;


    static final int PICK_CONTACT_REQUEST = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        selectContactBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.select_contact_btn) {
            Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
            pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
            startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
        } else if (v.getId() == R.id.submit_btn) {
            StringRequest signupRequest = new NetworkRequest(
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response);
                            Toast.makeText(SignupActivity.this, "Account Successfully Created!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }, null, null
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", Uuid.getUUID(SignupActivity.this));
                    params.put("action", "NEW_USER");
                    params.put("user", getUser().toJson());

                    return params;
                }
            };
            Volley.newRequestQueue(this).add(signupRequest);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri contactUri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                int number_column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int name_column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String number = cursor.getString(number_column);
                String name = cursor.getString(name_column);
                setEmergencyContact(name, number);
                cursor.close();
            }
        }
    }

    void setEmergencyContact(String name, String number) {
        emContactName.setText(name);
        emContactNumber.setText(number);

        emContactName.setVisibility(View.VISIBLE);
        findViewById(R.id.em_contact_name_txt).setVisibility(View.VISIBLE);
        emContactNumber.setVisibility(View.VISIBLE);
        findViewById(R.id.em_contact_number_txt).setVisibility(View.VISIBLE);
    }

    User getUser() {
        return new User(yourName.getText().toString(),
                        Uuid.getUUID(this),
                        emContactName.getText().toString(),
                        emContactNumber.getText().toString()
        );
    }

}

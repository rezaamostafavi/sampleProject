package com.sharifin.sharif.sampleproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        JSONObject object = new JSONObject();
        try {
            object.put("username", mEmailView.getText().toString());
            object.put("password", mPasswordView.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        PostRequest.ResponseReceiver response = new PostRequest.ResponseReceiver() {
            @Override
            public void getResponse(Object sender, String result) {
                if (result != null && !result.isEmpty()) {
                    try {
                        JSONObject jResult = new JSONObject(result);
                        if (jResult.has("token")) {
                            String token = jResult.optString("token");
                            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.share_preference),Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.token_key), token);
                            editor.commit();
                            startActivity(new Intent(LoginActivity.this, ContactsActivity.class));
                            finish();
                        } else
                            Toast.makeText(LoginActivity.this, R.string.error_in_network, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this, R.string.error_in_network, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(LoginActivity.this, R.string.error_in_network, Toast.LENGTH_SHORT).show();
            }
        };
        new PostRequest().postRequest(this, "http://private-fee148-cwcontacts.apiary-mock.com/user", object,"", response, true);
    }

}


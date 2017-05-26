package com.sharifin.sharif.sampleproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactsActivity extends AppCompatActivity {

    private String token;
    private RecyclerView contactRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contactRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.share_preference), Context.MODE_PRIVATE);
        token = sharedPref.getString(getString(R.string.token_key), "");

        getListContacts();

    }

    private void getListContacts() {
        PostRequest.ResponseReceiver response = new PostRequest.ResponseReceiver() {
            @Override
            public void getResponse(Object sender, String result) {
                if (result != null && !result.isEmpty()) {
                    try {
                        JSONArray data = new JSONArray(result);
                        ContactsListAdapter adapter = new ContactsListAdapter(ContactsActivity.this, data);
                        contactRecyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ContactsActivity.this, R.string.error_in_network, Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(ContactsActivity.this, R.string.error_in_network, Toast.LENGTH_SHORT).show();
            }
        };

        new PostRequest().postRequest(this, "http://private-fee148-cwcontacts.apiary-mock.com/contacts", null, token, response, true);
    }

}

package com.bd.agoraltd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DataListAdapter dataListAdapter;
    private List<DataModel> pushList;
    String URL_DATA = "https://agorasuperstores.com/rf-adminpanel/push_notification/getAllPushMessage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pushList = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Notification is Loadding");
        progressDialog.show();

        // Enable the back button in the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                try {

                    JSONArray array = new JSONArray(s);

                  //  Log.e("Push Message", "Data Length:" + array.length());

                    if (array.length() == 0) {
                        Toast.makeText(getApplicationContext(), "No special offer is available right now", Toast.LENGTH_LONG).show();
                    }


                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        //String data = array.getString(i);
                       // Log.e("push_title", "MSG:" + jsonObject.getString("push_message"));

                        // ListItem item = new ListItem(jsonObject.getString("name"),jsonObject.getString("bio"),jsonObject.getString("imageurl"));
                        DataModel pushData = new DataModel(jsonObject.getString("push_title"), jsonObject.getString("push_message"));
                        pushList.add(pushData);


                    }

                    if (pushList != null) {
                        Collections.reverse(pushList);
                        dataListAdapter = new DataListAdapter(pushList);
                        recyclerView.setAdapter(dataListAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Handle the back arrow button press
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


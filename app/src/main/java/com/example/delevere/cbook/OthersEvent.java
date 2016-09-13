package com.example.delevere.cbook;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OthersEvent extends AppCompatActivity {
    private DataBaseHelper db;
    private SimpleCursorAdapter dataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_event);
        db = new DataBaseHelper(this);




        displayBroadcastEvent();
    }

    private void getbroadcastevents() {
        SharedPreferences sharedPreferences = getSharedPreferences(Session.PREFS_NAME,0);
        String phone = sharedPreferences.getString(Session.KEY_phone,"9999999999");



        String url = Config.DATA_URL+phone;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //.makeText(getApplicationContext(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        displayBroadcastEvent();
    }

    private void displayBroadcastEvent() {
        //db = new DataBaseHelper(this);
        Cursor cursor = db.getAllBroadcastEvents();

        String[] columns = new String[] {
                DataBaseHelper.BEVENT_PHONENUMBER,
                DataBaseHelper.BEVENT_NAME
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.listbroadcasteventcontactname,
                R.id.listbroadcasteventcontactnumber,

        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.broadevent_list,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.othersbroadcasteventlist);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String countryCode =
                        cursor.getString(cursor.getColumnIndexOrThrow("phone_number"));
                Toast.makeText(getApplicationContext(),
                        countryCode, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void showJSON(String response){
        //DataBaseHelper db = new DataBaseHelper(this);
        String number="";
        String type="";
        String date = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            for(int i=0; i<result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                number = collegeData.getString(Config.KEY_NUMBER);
                type = collegeData.getString(Config.KEY_EVENT_TYPE);
                date = collegeData.getString(Config.KEY_EVENT_DATE);
                db.inserBroadcaseEvent("", number, type, date);
                //Toast.makeText(OthersEvent.this, number+"\n"+type+"\n"+date, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.contact, menu);

        return super.onCreateOptionsMenu(menu);


    }public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.refresh: {
                //db.truncatbevents();
                getbroadcastevents();
                displayBroadcastEvent();
                Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
                //displayBroadcastEvent();
                break;
            }

            default:
                return super.onOptionsItemSelected(item);

        }return super.onOptionsItemSelected(item);
    }



}

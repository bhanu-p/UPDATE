package com.example.delevere.cbook;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ChatsTab extends Fragment {

    ListView lv;
    Cursor cursor1;
    private DataBaseHelper db;
    private SimpleCursorAdapter dataAdapter;
    public static final String PREFS_NAME = "LoginPrefs";
    int RQS_1 = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_chats_tab, container, false);
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes

        lv = (ListView) rootView.findViewById(R.id.list);

        db = new DataBaseHelper(getContext());
        displayFriends();
        getbroadcastevents();



        new Thread(new Runnable() {
            public void run() {

            }
        }).start();



        return rootView;
    }



    private void getbroadcastevents() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Session.PREFS_NAME, 0);
        String phone = sharedPreferences.getString(Session.KEY_phone, "9999999999");


        String url = Config.FRIENDS_URL + phone;

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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    private void showJSON(String response) {
        //DataBaseHelper db = new DataBaseHelper(this);
        String number = "";

        try {
            db.truncatefriends();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                number = collegeData.getString(Config.KEY_NUMBER);
                String name = "";
                Cursor cur = db.getContactName(number);
                try {
                    name = cur.getString(cur.getColumnIndexOrThrow("name"));
                } catch (Exception e) {
                    name = "other";
                }

                if(name.equals("other")) {
                    db.inserFriends(number, number);
                }
                else {
                    db.inserFriends(name, number);
                }
                //db.inserBroadcaseEvent("", number, type, date);
                //Toast.makeText(OthersEvent.this, number+"\n"+type+"\n"+date, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        displayFriends();

    }
    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    private void displayFriends() {
        Cursor cursor = db.getAllFriends();
        //pick =(ImageView) findViewById(R.id.listeventcontactpick);

        String[] columns = new String[]{
                DataBaseHelper.FRIEND_ID,
                DataBaseHelper.FRIEND_NAME,
                DataBaseHelper.FRIEND_NUMBER,
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.hideid,
                R.id.listcontactname,
                R.id.listcontactnumber,

        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information


        dataAdapter = new SimpleCursorAdapter(
                this.getContext(), R.layout.contacts_list,
                cursor,
                columns,
                to,
                0);


        lv.setAdapter(dataAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) lv.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String name =
                        cursor.getString(cursor.getColumnIndexOrThrow("frnd_name"));
                String number =
                        cursor.getString(cursor.getColumnIndexOrThrow("frnd_number"));

                //Toast.makeText(getActivity(),      countryCode, Toast.LENGTH_SHORT).show();


                //Toast.makeText(getContext(), person.toString(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), ChatPage.class);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Session.PREFS_NAME, 0);
                String mynumber = sharedPreferences.getString(Session.KEY_phone, "9999999999");

                i.putExtra("name", name);

                i.putExtra("num", number);
                i.putExtra("myNo", mynumber);

                startActivity(i);

            }
        });

    }





    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){


        switch (item.getItemId()){
            case R.id.sms: {
                startActivity(new Intent(getActivity(),SmsActivity.class));
                return true;
            }
            case R.id.event: {
                getbroadcastevents();

                //startActivity(new Intent(getActivity(), CreateEventActivity.class));


                return true;
            }
            case R.id.contacts: {
                Toast.makeText(getActivity(), "Contacts", Toast.LENGTH_SHORT).show();
                try {
                    startActivity(new Intent(getActivity(), ContactsActivity.class));
                }catch (Exception e){
                    Toast.makeText(getActivity(), "some error", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            case R.id.profile: {
                Toast.makeText(getActivity(), "Profile page", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), Profile.class));
                return true;

            }




        }return false;
    }


}


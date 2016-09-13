package com.example.delevere.cbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsTab extends Fragment {
    public static final String PREFS_NAME = "LoginPrefs";
    ListView listView;
    ArrayAdapter<String> adapter;

    private DataBaseHelper db;
    private SimpleCursorAdapter dataAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_friends_tab, container, false);
        listView = (ListView) rootView.findViewById(R.id.friends_listView);


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
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                number = collegeData.getString(Config.KEY_NUMBER);
                String name = "mine";
                Cursor cur = db.getContactName(number);
                try {
                    name = cur.getString(cur.getColumnIndexOrThrow("name"));
                } catch (Exception e) {
                    continue;
                }


                db.inserFriends(name, number);
                //db.inserBroadcaseEvent("", number, type, date);
                //Toast.makeText(OthersEvent.this, number+"\n"+type+"\n"+date, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        displayFriends();

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
                R.id.friendlistcontactname,
                R.id.friendlistcontactnumber,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information

        dataAdapter = new SimpleCursorAdapter(
                this.getContext(), R.layout.friends_list,
                cursor,
                columns,
                to,
                0);


        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                String person_number = cursor.getString(cursor.getColumnIndexOrThrow("frnd_number"));
                String person_name = cursor.getString(cursor.getColumnIndexOrThrow("frnd_name"));

                //Toast.makeText(getContext(), person.toString(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(),FriendsEvents.class);
                i.putExtra("number",person_number);
                i.putExtra("name",person_name);

                startActivity(i);
            }
        });

    }
}

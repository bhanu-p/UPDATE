package com.example.delevere.cbook;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FriendsEvents extends AppCompatActivity {
    public static final String PREFS_NAME = "LoginPrefs";
    private static final int RESULT_PICK_CONTACT = 85500;
    DatePicker pickerDate;
    TimePicker pickerTime;
    ListView listView;
    Button pick;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    private int mYear, mMonth, mDay, mHour, mMinute;
    static final int DATE_DIALOG_ID = 0;
    int RQS_1 = 1;
    static String person = "";
    private DataBaseHelper db;
    private SimpleCursorAdapter dataAdapter;
    boolean doubleBackToExitPressedOnce = false;
    MatrixCursor mc;
    private static int key = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.friendseventstoolbar);
        setSupportActionBar(toolbar);
        String key_name = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(key_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //startActivity(new Intent(getApplicationContext(),Home2Activicy.class));
            }
        });
        listView = (ListView) findViewById(R.id.frinedseventslistView);
        mc = new MatrixCursor(new String[]{"_id","phone","image","eventdate","created"});

        db = new DataBaseHelper(this);

        displayEventNew();
        getbroadcastevents();
    }

    private void getbroadcastevents() {
        SharedPreferences sharedPreferences = getSharedPreferences(Session.PREFS_NAME, 0);
        String phone = sharedPreferences.getString(Session.KEY_phone, "9999999999");
        String key_id = getIntent().getStringExtra("number");
        //Toast.makeText(getApplicationContext(), key_id.toString(), Toast.LENGTH_SHORT).show();

        String url = Config.DATA_URL + key_id;

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

    }

    private void showJSON(String response) {
        //DataBaseHelper db = new DataBaseHelper(this);
        String number = "";
        String image = "";
        String date = "";
        String create = "";




        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);


            for (int i = 0; i < result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                number = collegeData.getString(Config.KEY_NUMBER);
                image = collegeData.getString(Config.KEY_EVENT_IMAGE);
                date = collegeData.getString(Config.KEY_EVENT_DATE);
                create = collegeData.getString(Config.KEY_EVENT_CREATED);



                //Toast.makeText(FriendsEvents.this, result.toString(), Toast.LENGTH_SHORT).show();
                mc.addRow(new Object[]{key,number,image,date,create});
                key++;
                //db.inserEvent(name,number ,type, date,image,creted);
                //db.inserBroadcaseEvent("", number, type, date);
                //Toast.makeText(OthersEvent.this, number+"\n"+type+"\n"+date, Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        displayEventNew();

    }

    private void displayEventNew() {


        String[] columns = new String[]{"_id","phone","image","eventdate","created"};

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.frndhide1,
                R.id.friendlisteventcontactname,
                R.id.friendlisteventtype,
                R.id.frndevnttime,
                R.id.friendlisteventcontactnumber,


        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.friends_events_list,
                mc,
                columns,
                to,0);

        dataAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            /** Binds the Cursor column defined by the specified index to the specified view */
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

                if(view.getId() == R.id.friendlisteventcontactname){
                    String personname ="";
                    String name = mc.getString(mc.getColumnIndexOrThrow("phone"));
                    Cursor cu = db.getName(name);
                    try {
                        personname = cu.getString(cu.getColumnIndexOrThrow("name"));
                    }catch (Exception e){
                    personname = name;

                }
                    ((TextView)view).setText(personname);
                    return true;
                }
                if (view.getId() == R.id.friendlisteventcontactnumber){
                    SharedPreferences sharedPreferences = getSharedPreferences(Session.PREFS_NAME,0);
                    String phonelocal = sharedPreferences.getString(Session.KEY_phone, "9999999999");
                    String created =mc.getString(mc.getColumnIndexOrThrow("created"));
                    String status = "By:";
                    String name = "";
                    //Toast.makeText(getActivity(), created.toString(), Toast.LENGTH_SHORT).show();
                    //Cursor cur = db.getContactName(created);

                    if(created.equals(phonelocal)){
                        status +="You";
                    }
                    /*else if(cur != null){
                        status += name;
                        //status +="Others";
                    }*/
                    else{

                        try {
                            Cursor cu = db.getName(created);
                            person = cu.getString(cu.getColumnIndexOrThrow("name"));
                        }catch (Exception e){
                            person = created;

                        }
                        status += person;
                    }
                    ((TextView)view).setText(status);
                    return true;


                }

                if (view.getId() == R.id.friendlisteventtype) {
                    String imageType = mc.getString(mc.getColumnIndexOrThrow("image"));

                    //int imageType = cursor.getInt(columnIndex);
                    int imageToBeShown = 0;
                    switch (imageType) {
                        case "R.drawable.bday":
                            imageToBeShown = R.drawable.bday;
                            break;
                        case "R.drawable.anniversary":
                            imageToBeShown = R.drawable.anniversary;
                            break;
                        case "R.drawable.events":
                            imageToBeShown = R.drawable.events;
                            break;

                    }
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;

                    BitmapFactory.decodeResource(getResources(), imageToBeShown, options);
                    String image = options.outMimeType;
                    //...
                    ((ImageView) view).setImageResource(imageToBeShown);
                    return true; //true because the data was bound to the view
                }

                if(view.getId() == R.id.frndevnttime){
                    String dt = mc.getString(mc.getColumnIndexOrThrow("eventdate"));
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date res = null;
                    try {
                        res = df.parse(dt);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    SimpleDateFormat df2 = new SimpleDateFormat("dd, MMM");
                    String dateText = df2.format(res);
                    ((TextView)view).setText(dateText);
                    return true;
                }


                return false;
            }
        });



        listView.setAdapter(dataAdapter);
    }
}



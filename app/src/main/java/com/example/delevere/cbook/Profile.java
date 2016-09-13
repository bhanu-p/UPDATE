package com.example.delevere.cbook;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Profile extends AppCompatActivity {
    public static final String PREFS_NAME = "LoginPrefs";
    private TextView name,phone,birthday,signout;
    private Button button,contact_us;
    private DataBaseHelper db;
    static final int DATE_PICKER_ID = 1111;
    int RQS_1 = 1;
    private String ph="";
    private String nm="";
    private String dob="";
    private int year;
    private int month;
    private int day;
    private ImageView refer;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = (TextView)findViewById(R.id.profilename);
        phone = (TextView)findViewById(R.id.profilephone);
        birthday = (TextView)findViewById(R.id.profilebirthday);
        button = (Button)findViewById(R.id.birthdaybutton);
        contact_us = (Button)findViewById(R.id.contactus);

        db = new DataBaseHelper(this);
        signout = (TextView)findViewById(R.id.profilesingout);
        refer = (ImageView)findViewById(R.id.referimage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.profiletoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID);
            }
        });


        SharedPreferences sharedPreferences = this.getSharedPreferences(Session.PREFS_NAME,0);
        ph = sharedPreferences.getString(Session.KEY_phone,"Phone Number Not Available");
        nm = sharedPreferences.getString(Session.KEY_NAME,"Name");
        dob = sharedPreferences.getString(Session.KEY_DOB,"Date Of Birth");


        name.setText(nm);
        phone.setText(ph);
        birthday.setText(dob);

        //Toast.makeText(Profile.this, ph.toString(), Toast.LENGTH_SHORT).show();

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ReportError.class));
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("logged");
                editor.clear();
                editor.commit();
                db.truncatstudent();
                db.truncatevents();
                Cursor c = db.getAllEventsSet();

                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);

                for (int cc=0 ;cc <= c.getCount();cc++) {
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), RQS_1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    RQS_1 +=1;

                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    alarmManager.cancel(pendingIntent);
                    pendingIntent.cancel();
                }
                Toast.makeText(Profile.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            }
        });

        refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Refer page", Toast.LENGTH_SHORT).show();

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Place here Your app Link");
                startActivity(Intent.createChooser(sharingIntent, "How do you want to share?"));


            }
        });

        String url = Config.PROFILE_URL+ph;

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
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:



                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            // Show selected date

            StringBuilder date = new StringBuilder();
            date.append(day).append("-").append(month + 1).append("-").append(year).append(" ");
            birthday.setText(new StringBuilder().append(day)
                    .append("-").append(month + 1).append("-").append(year)
                    .append(" "));

            String type = "profile";
            UserLoginTask userLoginTask = new UserLoginTask(getApplicationContext());
            userLoginTask.execute(type,ph.toString(),date.toString());

        }
    };
    private void showJSON(String response){
        //DataBaseHelper db = new DataBaseHelper(this);
        String profilenumber="";
        String profilename="";
        String profilebirthday="";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            for(int i=0; i<result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                profilenumber = collegeData.getString(Config.KEY_NUMBER);
                profilebirthday = collegeData.getString(Config.KEY_PROFILE_BIRTHDAY);
                profilename = collegeData.getString(Config.KEY_EVENT_TYPE);
                String name = "mine";
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = getSharedPreferences(Session.PREFS_NAME, 0);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Session.KEY_NAME,profilename);
        editor.putString(Session.KEY_DOB,profilebirthday);

        //Toast.makeText(LoginActivity.this, email, Toast.LENGTH_SHORT).show();
        //Saving values to editor
        editor.commit();

        name.setText(profilename);
        phone.setText(profilenumber);
        birthday.setText(profilebirthday);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profilemenu, menu);

        return super.onCreateOptionsMenu(menu);


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.contacts: {

                try {
                    startActivity(new Intent(this, ContactsActivity.class));
                }catch (Exception e){
                    Toast.makeText(this, "some error", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "Contacts", Toast.LENGTH_SHORT).show();
                return true;
            }



        }
        return false;
    }




}

package com.example.delevere.cbook;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EventEdit extends AppCompatActivity {
    Button calenderbutton,timerbutton;
    EditText contact_name,phone_number,event_name,date,time;
    private Spinner spinner1;
    public String key_id = "";
    private DataBaseHelper db;
    public String createdid = "";
    public String phone = "";
    public String datetime = "";
    int year_x,month_x,day_x,hour_x,minite_x;
    static final int DIALOG_ID = 0;
    static final int TIMER_ID = 1;

    static String event_type_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        String[] spinnerValue = {
                "Birthday",
                "Anniversary",
                "Others",

        };


        spinner1 = (Spinner) findViewById(R.id.spinnerlayoutedit);

        spinnerAdapter adapter = new spinnerAdapter(this, android.R.layout.simple_list_item_1);
        adapter.addAll(spinnerValue);
        adapter.add("Select Event type");
        spinner1.setAdapter(adapter);
        spinner1.setSelection(adapter.getCount());

        calenderbutton = (Button) findViewById(R.id.eventcalenderedit);
        timerbutton = (Button)findViewById(R.id.eventcalendertimeedit);
        date = (EditText)findViewById(R.id.eventdateedit);
        time = (EditText)findViewById(R.id.eventtimeedit);

        calenderbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });

        timerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIMER_ID);

            }
        });






        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EventDisplay.class));
            }
        });
        db = new DataBaseHelper(getApplicationContext());
        key_id = getIntent().getStringExtra("id");

        Cursor cur = db.getEvent(key_id);
        cur.moveToFirst();
        String name = cur.getString(cur.getColumnIndexOrThrow("contact_name"));
        phone = cur.getString(cur.getColumnIndexOrThrow("phone_number"));
        String eventname = cur.getString(cur.getColumnIndexOrThrow("event_name"));
        String eventtype = cur.getString(cur.getColumnIndexOrThrow("image"));
        datetime = cur.getString(cur.getColumnIndexOrThrow("date_time"));
        createdid =cur.getString(cur.getColumnIndexOrThrow("created_by"));

        cur.moveToNext();
        eventtype = eventtype.substring(11);
        int year =  Integer.valueOf(datetime.substring(0,4));
        int month =  Integer.valueOf(datetime.substring(5,7));
        int day =  Integer.valueOf(datetime.substring(8,10));
        int hours =  Integer.valueOf(datetime.substring(11,13));
        int minite =  Integer.valueOf(datetime.substring(14,16));
        int seconds =  Integer.valueOf(datetime.substring(17,19));
        contact_name = (EditText)findViewById(R.id.contactnameedit);
        phone_number = (EditText)findViewById(R.id.phonenumberedit);
        event_name = (EditText)findViewById(R.id.eventnameedit);
        Toast.makeText(EventEdit.this, year+","+month+","+day+","+hours, Toast.LENGTH_SHORT).show();
        contact_name.setText(name);
        phone_number.setText(phone);
        event_name.setText(eventname);
        if (eventtype.equals("bday")){
            spinner1.setSelection(0);
        }else if(eventtype.equals("anniversary")){
            spinner1.setSelection(1);
        }else {
            spinner1.setSelection(2);
        }
        year_x = year;
        month_x = month;
        day_x = day;
        hour_x = hours;
        minite_x = minite;


        date.setText(day_x+"/"+month_x+"/"+year_x);
        time.setText(hour_x+":"+minite_x);

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.event_update, menu);

        return super.onCreateOptionsMenu(menu);

    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.save: {


                Calendar cal = Calendar.getInstance();
                cal.set(year_x, month_x-1,day_x,hour_x,minite_x,00);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(cal.getTime());

                if(String.valueOf(spinner1.getSelectedItem()).equals("Birthday")){
                    event_type_string = "R.drawable.bday";
                }else if(String.valueOf(spinner1.getSelectedItem()).equals("Anniversary")){
                    event_type_string = "R.drawable.anniversary";
                }else {
                    event_type_string = "R.drawable.events";
                }

                db.editEvent(key_id,event_name.getText().toString(),formattedDate,event_type_string);


                Intent intent = new Intent(getApplicationContext(), EventDisplay.class);
                intent.putExtra("key id", key_id);
                startActivity(intent);
                String type = "eventedit";
                UserLoginTask userLoginTask = new UserLoginTask(this);
                userLoginTask.execute(type,event_name.getText().toString(),formattedDate,event_type_string,datetime,phone);

                Toast.makeText(EventEdit.this, "Updated", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.dontsave: {
                Intent intent = new Intent(getApplicationContext(), EventDisplay.class);


                intent.putExtra("key id", key_id);

                startActivity(intent);
                return true;
            }




        }return false;
    }
    protected Dialog onCreateDialog(int id){


        if(id == DIALOG_ID)
            return new DatePickerDialog(this,dpickerListner,year_x,month_x-1,day_x);

        else if(id == TIMER_ID)
            return new TimePickerDialog(this,tpikerListner,hour_x,minite_x,false);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear+1;
            day_x = dayOfMonth;
            date.setText(day_x+"/"+month_x+"/"+year_x);
        }
    };
    private TimePickerDialog.OnTimeSetListener tpikerListner = new TimePickerDialog.OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            hour_x=hourOfDay;
            minite_x=minute;
            time.setText(hour_x+":"+minite_x);
        }
    };


    public class spinnerAdapter extends ArrayAdapter<String> {

        public spinnerAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);

        }
        @Override
        public int getCount() {

            int count = super.getCount();

            return count>0 ? count-1 : count ;
        }
    }
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), EventDisplay.class);


        intent.putExtra("key id", key_id);

        startActivity(intent);

    }
}

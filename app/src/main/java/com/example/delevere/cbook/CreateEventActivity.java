package com.example.delevere.cbook;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {
    Button buttonSetAlarm,calenderbutton,timerbutton;
    EditText contact_name,phone_number,event_name,date,time;
    TextView hidden;
    static int RQS_1 = 1;
    private Spinner spinner1;
    private static final int RESULT_PICK_CONTACT = 85500;
    static String event_type_string;
    static String event_permission;
    static String key_id;

    int year_x,month_x,day_x,hour_x,minite_x;
    static final int DIALOG_ID = 0;
    static final int TIMER_ID = 1;
    private RadioButton radioSexButton;
    private RadioButton public_check,someone_check,onlyme_check;
    private RadioGroup radioSendGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        final DataBaseHelper db = new DataBaseHelper(this);

        String[] spinnerValue = {
                "Birthday",
                "Anniversary",
                "Others",

        };


        spinner1 = (Spinner) findViewById(R.id.spinnerlayout);

        spinnerAdapter adapter = new spinnerAdapter(this, android.R.layout.simple_list_item_1);
        adapter.addAll(spinnerValue);
        adapter.add("Select Event type");
        spinner1.setAdapter(adapter);
        spinner1.setSelection(adapter.getCount());


        //db.truncatevents();

        contact_name = (EditText)findViewById(R.id.contactname);
        phone_number = (EditText)findViewById(R.id.phonenumber);
        event_name = (EditText)findViewById(R.id.eventname);
        hidden = (TextView) findViewById(R.id.hidencontacts);
        public_check = (RadioButton) findViewById(R.id.publiccheck);
        public_check.setChecked(true);
        someone_check = (RadioButton) findViewById(R.id.someonecheck);
        onlyme_check = (RadioButton) findViewById(R.id.onlymecheck);
        radioSendGroup=(RadioGroup)findViewById(R.id.sendgroup);

        calenderbutton = (Button) findViewById(R.id.eventcalender);
        timerbutton = (Button)findViewById(R.id.eventcalendertime);
        date = (EditText)findViewById(R.id.eventdate);
        time = (EditText)findViewById(R.id.eventtime);
        Toolbar toolbar = (Toolbar) findViewById(R.id.eventcreatetoolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Home2Activicy.class));
            }
        });

        //Toast.makeText(CreateEventActivity.this, String.valueOf(spinner1.getSelectedItem()), Toast.LENGTH_SHORT).show();



        public_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "All", Toast.LENGTH_SHORT).show();
            }
        });
        onlyme_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "note";
                UserLoginTask userLoginTask = new UserLoginTask(CreateEventActivity.this);
                userLoginTask.execute(type, null,null);

                Toast.makeText(getApplicationContext(), "Only for Me", Toast.LENGTH_SHORT).show();
            }
        });
        someone_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(getApplicationContext(),PickMultipleContact.class);
                startActivityForResult(intent, 4);



            }
        });
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






        buttonSetAlarm = (Button)findViewById(R.id.setalarm);
        buttonSetAlarm.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View arg0) {
                boolean cancel = false;

                if (TextUtils.isEmpty(contact_name.getText()) || TextUtils.isEmpty(event_name.getText()) || TextUtils.isEmpty(date.getText()) || TextUtils.isEmpty(time.getText())) {

                    cancel = true;
                }

                if (cancel) {

                    Toast.makeText(CreateEventActivity.this, "Invaild Details.", Toast.LENGTH_SHORT).show();

                } else {


                    Calendar current = Calendar.getInstance();

                    Calendar cal = Calendar.getInstance();
                    cal.set(year_x, month_x, day_x, hour_x, minite_x, 00);


                    if (String.valueOf(spinner1.getSelectedItem()).equals("Birthday")) {
                        event_type_string = "R.drawable.bday";
                    } else if (String.valueOf(spinner1.getSelectedItem()).equals("Anniversary")) {
                        event_type_string = "R.drawable.anniversary";
                    } else {
                        event_type_string = "R.drawable.events";
                    }

                    if (public_check.isChecked()) {
                        event_permission = "1";

                    } else if (someone_check.isChecked()) {
                        event_permission = hidden.getText().toString();
                    } else if (onlyme_check.isChecked()) {
                        event_permission = "0";
                    }

                        //Toast.makeText(CreateEventActivity.this, cal.toString(), Toast.LENGTH_SHORT).show();

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(cal.getTime());

                        SharedPreferences sharedPreferences = getSharedPreferences(Session.PREFS_NAME, 0);
                        String phonelocal = sharedPreferences.getString(Session.KEY_phone, "8888888888");
                        String phone = phone_number.getText().toString();
                        String ph = phone.replaceAll("[^0-9]", "");
                        String p = ph.substring(ph.length() - 10, ph.length());



                        db.inserEvent(contact_name.getText().toString(), p, event_name.getText().toString(), formattedDate, event_type_string, phonelocal);
                        Cursor cursor = db.getLastId();
                       // key_id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                        db.close();
                        String sevent_phone = phone_number.getText().toString();
                        String sevent_name = event_name.getText().toString();
                        String sevent_date = formattedDate;
                    setAlarm(cal);

                        //Toast.makeText(CreateEventActivity.this, phonelocal, Toast.LENGTH_SHORT).show();
                        String type = "event";
                        UserLoginTask userLoginTask = new UserLoginTask(CreateEventActivity.this);
                        userLoginTask.execute(type, phonelocal, sevent_phone, sevent_name, sevent_date, event_type_string.toString(), event_permission.toString());

                        Intent i = new Intent(getApplicationContext(), Home2Activicy.class);
                    startActivity(i);

                }

            }});
    }

    protected Dialog onCreateDialog(int id){
        Calendar c = Calendar.getInstance();


        if(id == DIALOG_ID) {
            year_x = c.get(Calendar.YEAR);
            month_x = c.get(Calendar.MONTH);
            day_x = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this, R.style.DialogTheme,dpickerListner, year_x, month_x, day_x);
        }

        else if(id == TIMER_ID) {
            hour_x = c.get(Calendar.HOUR_OF_DAY);
            minite_x = c.get(Calendar.MINUTE);
            return new TimePickerDialog(this,R.style.DialogTheme, tpikerListner, hour_x, minite_x, false);
        }
            return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear;
            day_x = dayOfMonth;
            int month_y = month_x;
            month_y += 1;
            date.setText(day_x+"/"+month_y+"/"+year_x);
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


    private void setAlarm(Calendar targetCal){



        Calendar current = Calendar.getInstance();

        targetCal.set(Calendar.YEAR,current.get(Calendar.YEAR));


        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);


        intent.putExtra("EVENT",event_name.getText().toString());
        intent.putExtra("PERSON",contact_name.getText().toString());
        //intent.putExtra("key id",key_id.toString());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        RQS_1 +=1;

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), 1000 * 60 * 1, pendingIntent);
    }

    public void pickContact(View v){

        Intent intent=new Intent(this,PickContact.class);
        startActivityForResult(intent, 2);

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok

        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            String name=data.getStringExtra("cname");
            String number=data.getStringExtra("cnumber");

            contact_name.setText(name);
            phone_number.setText(number);
        }
        if(requestCode==4)
        {
            String name=data.getStringExtra("number_list");


            hidden.setText(name);

        }

    }

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

}

package com.example.delevere.cbook;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Home2Activicy extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DataBaseHelper db;
    int RQS_1 = 1;
    static int i = 0;
    int cc = 0;
    Cursor cur;
    Cursor cursor1;
    public static final String PREFS_NAME = "LoginPrefs";
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2_activicy);

        db = new DataBaseHelper(this);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("logged", "logged");
        editor.commit();


        cur = db.getAllContacts();
        if (cur.getCount() == 0) {
            if (weHavePermissionToReadContacts()) {

                new Thread(new Runnable() {
                    public void run() {
                        readTheContacts();
                    }
                }).start();

                cursor1 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                startManagingCursor(cursor1);
                SharedPreferences sharedPreferences = getSharedPreferences(Session.PREFS_NAME, 0);
                String mynumber = sharedPreferences.getString(Session.KEY_phone, "9999999999");
                db.inserData("You", mynumber);

                if (cursor1.moveToFirst()) {

                    do {
                        String name = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String phone = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String ph = phone.replaceAll("[^0-9]", "");
                        String p;
                        if (ph.length() > 10) {
                            p = ph.substring(ph.length() - 10, ph.length());
                        } else {
                            p = ph;

                        }
                        db.inserData(name, p);

                    } while (cursor1.moveToNext());


                }
                getContacts();
            } else {
                requestReadContactsPermissionFirst();
            }
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        View frnd = LayoutInflater.from(getApplicationContext()).inflate(R.layout.friends_title, null, false);
        View evnt = LayoutInflater.from(getApplicationContext()).inflate(R.layout.event_title, null, false);
        View chat = LayoutInflater.from(getApplicationContext()).inflate(R.layout.chat_title, null, false);
        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setCustomView(frnd));
        tabLayout.addTab(tabLayout.newTab().setCustomView(evnt));
        tabLayout.addTab(tabLayout.newTab().setCustomView(chat).setText("chat"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("EXTRA_PAGE");
            int position= Integer.parseInt(value );
            viewPager.setCurrentItem(position);
        }else {
            viewPager.setCurrentItem(1,false);
        }


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
       // AlarmSettings();
    }

    private boolean weHavePermissionToReadContacts() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    private void readTheContacts() {
        ContactsContract.Contacts.getLookupUri(getContentResolver(), ContactsContract.Contacts.CONTENT_URI);
    }

    private void requestReadContactsPermissionFirst() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
            Toast.makeText(this, "We need permission so you can text your friends.", Toast.LENGTH_LONG).show();
            requestForResultContactsPermission();
        } else {
            requestForResultContactsPermission();
        }
    }

    private void requestForResultContactsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 123);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            readTheContacts();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
    public void getContacts() {
        DataBaseHelper db = new DataBaseHelper(this);
        Cursor res = db.getContact();
        if (res.getCount() == 0) {

            //show
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append(res.getString(0));
            buffer.append(",");
        }
        sendContacts(buffer.toString());
        //Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();

        db.close();
    }

    public void sendContacts(String contact) {
        SharedPreferences sharedPreferences = getSharedPreferences(Session.PREFS_NAME, 0);
        String phone = sharedPreferences.getString(Session.KEY_phone, "9999999999");
        String type = "contacts";
        //Toast.makeText(getApplicationContext(), phone, Toast.LENGTH_SHORT).show();
        UserLoginTask userLoginTask = new UserLoginTask(this);
        userLoginTask.execute(type, phone, contact);

    }




    private void AlarmSettings() {
        Calendar current = Calendar.getInstance();

        Cursor c = db.getAllEventsSet();




        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);

        for (cc=0 ;cc <= c.getCount();cc++) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            RQS_1 +=1;

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }

        while (c.moveToNext()) {
            String title = c.getString(0);
            String subtitle = c.getString(1);
            String date = c.getString(2);


            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date res = null;
            try {
                res = (Date)df.parse(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();



            calendar.setTime(res);
            calendar.set(Calendar.YEAR, current.get(Calendar.YEAR));


            if(calendar.compareTo(current) <= 0){i = i+1;}
            else{
                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate1 = df1.format(current.getTime());

                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate2 = df2.format(calendar.getTime());
               // Toast.makeText(Home2Activicy.this, formattedDate1+"-"+formattedDate2, Toast.LENGTH_SHORT).show();
               // Toast.makeText(Home2Activicy.this, formattedDate1+"-"+formattedDate2, Toast.LENGTH_SHORT).show();

                //Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);

                intent.putExtra("EVENT", title);
                intent.putExtra("PERSON", subtitle);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                RQS_1 += 1;

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            }
            //Toast.makeText(Home2Activicy.this, i, Toast.LENGTH_SHORT).show();


        }c.close();
        db.close();

    }
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "Press Again to Exit", Toast.LENGTH_SHORT).show();
            doubleBackToExitPressedOnce = true;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }


    public void onTabUnselected(TabLayout.Tab tab) {
    }


    public void onTabReselected(TabLayout.Tab tab) {

    }
}

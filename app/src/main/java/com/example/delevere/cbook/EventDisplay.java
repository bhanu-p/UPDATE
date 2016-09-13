package com.example.delevere.cbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

public class EventDisplay extends AppCompatActivity {
    private TextView name,phone,time,created;
    public static final String PREFS_NAME = "LoginPrefs";
    public String key_id = "";
    public String key_image = "";
    public String creates = "";
    public String createdid = "";
    private DataBaseHelper db;

    Button greetingbutton;
    private EditText result;
    static private int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display);
        db = new DataBaseHelper(getApplicationContext());
        SharedPreferences settings = getSharedPreferences(Session.PREFS_NAME, 0);
       /* if (!settings.getString("logged", "").toString().equals("logged")) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.eventdisplaytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Home2Activicy.class));
            }
        });
        name = (TextView) findViewById(R.id.evnname);
        phone = (TextView) findViewById(R.id.evnphone);
        time = (TextView) findViewById(R.id.evntime);
        created = (TextView) findViewById(R.id.evncreated);
        ImageView img= (ImageView) findViewById(R.id.evnimage);

        //key_image = getIntent().getStringExtra("image");
        key_id = getIntent().getStringExtra("key id");

        Cursor cur = db.getEvent(key_id);
        cur.moveToFirst();
        final String personname = cur.getString(cur.getColumnIndexOrThrow("contact_name"));
        final String personphone = cur.getString(cur.getColumnIndexOrThrow("phone_number"));
        String eventname = cur.getString(cur.getColumnIndexOrThrow("event_name"));
        String db_image = cur.getString(cur.getColumnIndexOrThrow("image"));
        String datetime = cur.getString(cur.getColumnIndexOrThrow("date_time"));
        createdid =cur.getString(cur.getColumnIndexOrThrow("created_by"));
        cur.moveToNext();


        int imageToBeShown = 0;
        switch (db_image) {
            case "R.drawable.bday":
                imageToBeShown = R.drawable.birthdayno;
                break;
            case "R.drawable.anniversary":
                imageToBeShown = R.drawable.anniversarywall;
                break;
            case "R.drawable.events":
                imageToBeShown = R.drawable.logo_event;
                break;

        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(getResources(), imageToBeShown, options);

        img.setImageResource(imageToBeShown);





        greetingbutton = (Button)findViewById(R.id.greetingbutton);
       // Toast.makeText(EventDisplay.this, key_id.toString(), Toast.LENGTH_SHORT).show();

        //Toast.makeText(EventDisplay.this, personphone, Toast.LENGTH_SHORT).show();

        greetingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(status==0) {
                    Intent intent = new Intent(getApplicationContext(), Greetings.class);
                    intent.putExtra("name", personname);
                    intent.putExtra("number", personphone);
                    intent.putExtra("from", createdid);

                    startActivity(intent);
                    status =1;
                }else {
                    Toast.makeText(EventDisplay.this, "greeting already sended!", Toast.LENGTH_SHORT).show();
                    status =0;
                }

            }
        });


        SharedPreferences sharedPreferences = this.getSharedPreferences(Session.PREFS_NAME,0);
        String phonelocal = sharedPreferences.getString(Session.KEY_phone, "Phone Number Not available");

        if(createdid.equals(phonelocal)){
            creates ="Me";
        }else{

            try {
                Cursor cu = db.getName(createdid);
                creates = cu.getString(cu.getColumnIndexOrThrow("name"));
            }catch (Exception e){
                creates = "";

            }

        }

        db.close();
        name.setText(eventname);
        phone.setText(personname);
        time.setText(datetime);
        created.setText(creates);


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Home2Activicy.class));

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.event, menu);

        return super.onCreateOptionsMenu(menu);

    }




    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.eventdelete:{
                SharedPreferences sharedPreferences = this.getSharedPreferences(Session.PREFS_NAME,0);
                String phonelocal = sharedPreferences.getString(Session.KEY_phone, "Phone Number Not available");
                if(phonelocal.equals(createdid)) {

                    new AlertDialog.Builder(this)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle("Do you really want to delete?")
                            .setPositiveButton("No", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {



                                    return;
                                    //code for exit

                                }

                            })
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String key_id = getIntent().getStringExtra("key id");
                                    if(db.deleteEvent(key_id)) {

                                        Toast.makeText(EventDisplay.this, "Event Deleted", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),Home2Activicy.class));

                                    }else {
                                        Toast.makeText(EventDisplay.this, "Event Not deleted.", Toast.LENGTH_SHORT).show();
                                    }

                                    return;
                                    //code for exit

                                }

                            })
                            .show();


                }else {
                    Toast.makeText(EventDisplay.this, "You have No permission to delete", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            case R.id.eventedit: {
                SharedPreferences sharedPreferences = this.getSharedPreferences(Session.PREFS_NAME,0);
                String phonelocal = sharedPreferences.getString(Session.KEY_phone, "Phone Number Not available");
                if(phonelocal.equals(createdid)) {


                    String key_id = getIntent().getStringExtra("key id");

                    Intent intent = new Intent(getApplicationContext(), EventEdit.class);
                    intent.putExtra("id", key_id);


                    startActivity(intent);
                    /*if(db.editEvent(key_id,"SAMPLE","2017-08-16 10:42:00")) {
                        startActivity(new Intent(getApplicationContext(),Home2Activicy.class));

                        Toast.makeText(EventDisplay.this, "Editing ok", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(EventDisplay.this, "Editing not done", Toast.LENGTH_SHORT).show();
                    }*/
                }else {
                    Toast.makeText(EventDisplay.this, "You have no Permission to Edit", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            case R.id.contacts: {

                try {
                    startActivity(new Intent(this, ContactsActivity.class));
                }catch (Exception e){
                    Toast.makeText(this, "some error", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "Contacts", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.profile: {
                Toast.makeText(this, "Profile page", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,Profile.class));
                return true;

            }
            case R.id.refer_friend: {
                Toast.makeText(this, "Refer page", Toast.LENGTH_SHORT).show();

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Place here Your app Link");
                startActivity(Intent.createChooser(sharingIntent, "How do you want to share?"));
                //startActivity(new Intent(getApplicationContext(),ReferActivity.class));
                return true;
            }
            case R.id.logout: {
                Toast.makeText(this, "Try to Logout", Toast.LENGTH_SHORT).show();
                //SharedPreferences settings = getSharedPreferences(SessionCreator.PREFS_NAME, 0);
                //SharedPreferences.Editor editor = settings.edit();
                //editor.remove("logged");
                //editor.commit();
                //finish();
                SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("logged");
                editor.clear();
                editor.commit();
                db.truncatstudent();
                db.truncatevents();
                startActivity(new Intent(this, LoginActivity.class));
                return true;

            }



        }return false;
    }

}

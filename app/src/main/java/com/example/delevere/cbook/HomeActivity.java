package com.example.delevere.cbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
public class HomeActivity extends AppCompatActivity {
    Button events,others,sms,chat;
    public static final String PREFS_NAME = "LoginPrefs";
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("logged", "logged");
        editor.commit();
        events = (Button) findViewById(R.id.imageView1);
        others = (Button) findViewById(R.id.imageView2);
        sms = (Button) findViewById(R.id.imageView3);
        chat = (Button) findViewById(R.id.imageView4);



        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EventActivity.class));
            }
        });
        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),OthersEvent.class));
            }
        });
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ContactsActivity.class));
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SmsActivity.class));
            }
        });


    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.profile, menu);

        return super.onCreateOptionsMenu(menu);


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
        }
    }



    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.contacts: {
                Toast.makeText(getApplicationContext(), "Contacts", Toast.LENGTH_SHORT).show();
                try {
                    startActivity(new Intent(getApplicationContext(), ContactsActivity.class));
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "some error", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.profile: {
                Toast.makeText(getApplicationContext(), "Profile page", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                break;

            }
            case R.id.refer_friend: {
                Toast.makeText(getApplicationContext(), "Refer page", Toast.LENGTH_SHORT).show();

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Place here Your app Link");
                startActivity(Intent.createChooser(sharingIntent, "How do you want to share?"));
                //startActivity(new Intent(getApplicationContext(),ReferActivity.class));
                break;
            }
            case R.id.logout: {
                Toast.makeText(getApplicationContext(), "Try to Logout", Toast.LENGTH_SHORT).show();
                //SharedPreferences settings = getSharedPreferences(SessionCreator.PREFS_NAME, 0);
                //SharedPreferences.Editor editor = settings.edit();
                //editor.remove("logged");
                //editor.commit();
                //finish();
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("logged");
                editor.clear();
                editor.commit();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                break;

            }
            default:
                return super.onOptionsItemSelected(item);

        }return super.onOptionsItemSelected(item);
    }
}

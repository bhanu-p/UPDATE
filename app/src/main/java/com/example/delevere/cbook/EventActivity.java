package com.example.delevere.cbook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {
    private static final int RESULT_PICK_CONTACT = 85500;
    DatePicker pickerDate;
    TimePicker pickerTime;
    ListView listView;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    private int mYear, mMonth, mDay, mHour, mMinute;
    static final int DATE_DIALOG_ID = 0;
    final static int RQS_1 = 1;
    private DataBaseHelper db;
    private SimpleCursorAdapter dataAdapter;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_event);


        db = new DataBaseHelper(this);


        Button btn = (Button) findViewById(R.id.btnAdd);
        //listView = (ListView) findViewById(R.id.listView);
        //listItems = new ArrayList<String>();
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listItems);
        //listView.setAdapter(adapter);

        //displayListView();

        //getEvents();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateEventActivity.class));
                //Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                //       ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                //startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);


            }
        });




        //listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          //  @Override
         //   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         //       Toast.makeText(EventActivity.this, "developing edit option", Toast.LENGTH_SHORT).show();
                //DateTime();

         //   }
       // });
displayEventNew();


    }


    private void displayEventNew() {
        Cursor cursor = db.getAllEvents();

        String[] columns = new String[] {
                DataBaseHelper.EVENT_CONTACT,
                DataBaseHelper.EVENT_NAME
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.listeventcontactname,
                R.id.listeventcontactnumber,

        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.event_list,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.listView);
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
                        cursor.getString(cursor.getColumnIndexOrThrow("contact_name"));
                Toast.makeText(getApplicationContext(),
                        countryCode, Toast.LENGTH_SHORT).show();

            }
        });

    }


    public void getEvents(){
        DataBaseHelper db = new DataBaseHelper(this);
        Cursor res = db.getAllEvent();
        if(res.getCount() == 0){

            //show
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){
            buffer.append(res.getString(0));
            buffer.append("\n");
            listItems.add(res.getString(1)+res.getString(2)+"\n"+res.getString(3)+"-"+res.getString(4));


        }



        //sendContacts(buffer.toString());
        //Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();


    }
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.contact, menu);

        return super.onCreateOptionsMenu(menu);


    }public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.refresh: {
                db.truncatstudent();

                Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
                break;
            }

            default:
                return super.onOptionsItemSelected(item);

        }return super.onOptionsItemSelected(item);
    }

}


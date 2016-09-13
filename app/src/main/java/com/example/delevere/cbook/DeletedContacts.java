package com.example.delevere.cbook;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class DeletedContacts extends AppCompatActivity {
    Cursor cursor1;
    private DataBaseHelper db;
    private SimpleCursorAdapter dataAdapter;
    static Context mContext;
    Cursor cur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_contacts);
        db = new DataBaseHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.deletedcontacttoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mContext = getApplicationContext();



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



                Cursor cursor = db.getDeletedContacts();
                if (cursor == null) {
                    Toast.makeText(DeletedContacts.this, "No Deleted contacts", Toast.LENGTH_SHORT).show();
                }else {

                    String[] columns = new String[]{
                            DataBaseHelper.BACKUP_KEY_NAME,
                            DataBaseHelper.BACKUP_KEY_PHONENUMBER
                    };

                    // the XML defined views which the data will be bound to
                    int[] to = new int[]{
                            R.id.deletedlistcontactname,
                            R.id.deletedlistcontactnumber,

                    };

                    // create the adapter using the cursor pointing to the desired data
                    //as well as the layout information
                    dataAdapter = new SimpleCursorAdapter(
                            getApplicationContext(), R.layout.deleted_contact_list,
                            cursor,
                            columns,
                            to,
                            0);

                    final ListView listView = (ListView) findViewById(R.id.deletedlist);
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
                                    cursor.getString(cursor.getColumnIndexOrThrow("name_backup"));
                            Toast.makeText(getApplicationContext(),
                                    countryCode, Toast.LENGTH_SHORT).show();

                        }
                    });
//        Toast.makeText(DeletedContacts.this,cursor.getString(cursor.getColumnIndex("name_backup")), Toast.LENGTH_SHORT).show();

                    db.close();
                }

    }
}

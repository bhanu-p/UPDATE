package com.example.delevere.cbook;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.Toast;

public class PickContact extends AppCompatActivity {
    Cursor cursor1;
    private DataBaseHelper db;
    private SimpleCursorAdapter dataAdapter;
    static Context mContext;
    Cursor cur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contact);
        db = new DataBaseHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.contacttoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mContext = PickContact.this;



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.putExtra("cname",true);
                intent.putExtra("cnumber",true);
                setResult(2,intent);
                finish();
            }
        });


        cur = db.getAllContacts();




        if(cur.getCount() == 0){
            if (weHavePermissionToReadContacts()) {

                new Thread(new Runnable() {
                    public void run() {
                        readTheContacts();
                    }
                }).start();

                cursor1 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                startManagingCursor(cursor1);


                if (cursor1.moveToFirst()) {

                    do {
                        String name = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String phone = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String ph = phone.replaceAll("[^0-9]", "");
                        String p;
                        if(ph.length()>10) {
                            p = ph.substring(ph.length() - 10, ph.length());
                        }else {
                            p = ph;

                        }
                        db.inserData(name, p);

                    } while (cursor1.moveToNext());


                }

            } else {
                requestReadContactsPermissionFirst();
            }
            new Thread(new Runnable() {
                public void run() {
                    displayContactsNew();

                }
            }).start();
            //Toast.makeText(ContactsActivity.this, "Contacts empty", Toast.LENGTH_SHORT).show();
        }
        else{ new Thread(new Runnable() {
            public void run() {
                displayContactsNew();
            }
        }).start();

        }





        db.close();

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
    public void displayContactsNew() {

        Cursor cursor = db.getAllContacts();

        String[] columns = new String[]{
                DataBaseHelper.KEY_NAME,
                DataBaseHelper.KEY_PHONENUMBER
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.listcontactname,
                R.id.listcontactnumber,

        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.contacts_list,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.list);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String contactname =
                        cursor.getString(cursor.getColumnIndexOrThrow("name"));

                String contactnumber =
                        cursor.getString(cursor.getColumnIndexOrThrow("phone_number"));

                //Toast.makeText(getApplicationContext(),
                 //       contactname+"--"+contactnumber, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.putExtra("cname",contactname);
                intent.putExtra("cnumber",contactnumber);
                setResult(2,intent);
                finish();

            }
        });

        db.close();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.pickcontact, menu);

        SearchView mSearchView = (SearchView) menu.findItem(R.id.contactsearch).getActionView();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                dataAdapter.getFilter().filter(newText.toString());
                return false;
            }
        });


        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return db.fetchContactByName(constraint.toString());
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("cname",true);
        intent.putExtra("cnumber",true);
        setResult(2,intent);
        finish();

    }

}

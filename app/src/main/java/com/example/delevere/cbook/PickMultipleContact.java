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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PickMultipleContact extends AppCompatActivity {
    public final ArrayList<String> checked = new ArrayList<String>();
    Cursor cursor1;
    private DataBaseHelper db;
    private SimpleCursorAdapter dataAdapter;
    static Context mContext;
    Cursor cur;
    String contacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_multiple_contact);
        db = new DataBaseHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.multiplecontacttoolbar);
        setSupportActionBar(toolbar);

        mContext = PickMultipleContact.this;

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
                R.id.multiplelistcontactname,
                R.id.multiplelistcontactnumber,

        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        /*dataAdapter = new SimpleCursorAdapter(
                this, R.layout.multiple_contact_list,
                cursor,
                columns,
                to,
                0);*/

        dataAdapter = new MyListAdapter(getApplicationContext(), R.layout.multiple_contact_list,cursor,columns,to,0);

        ListView listView = (ListView) findViewById(R.id.multiplelist);

        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // Assign adapter to ListView

        listView.setAdapter(dataAdapter);

        db.close();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.event_update, menu);

        return super.onCreateOptionsMenu(menu);

    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.save: {
                Intent intent=new Intent();
                intent.putExtra("number_list",checked.toString());
                setResult(4,intent);
                //Toast.makeText(getApplicationContext(), s.toString(), Toast.LENGTH_SHORT).show();
                //onBackPressed();
                finish();
                return true;
            }
            case R.id.dontsave: {
                Toast.makeText(getApplicationContext(), "select contacts", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.putExtra("number_list",checked.toString());
                setResult(4,intent);
                //onBackPressed();
                finish();

                return true;
            }

        }return false;
    }
    private class MyListAdapter extends SimpleCursorAdapter {
        private int layout;
        Cursor items;
        boolean[] itemChecked;

        public MyListAdapter(Context context, int resource, Cursor c, String[] from, int[] to, int flags) {
            super(context, resource, c, from, to, flags);
            layout = resource;
            items = c;
            itemChecked = new boolean[c.getCount()];
            //array =new boolean[c.getCount()];
            //checkboxState = new ArrayList<Boolean>(Collections.nCopies(c.getCount(), true));
        }
        public View getView(final int position, View converView, ViewGroup parent){
            ViewHolder viewHolder = null;
            converView = null;

            if(converView == null){
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                converView = inflater.inflate(layout,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.mcheck = (CheckBox) converView.findViewById(R.id.multiplecheckbox);
                viewHolder.mname = (TextView) converView.findViewById(R.id.multiplelistcontactname);
                viewHolder.mnumber = (TextView) converView.findViewById(R.id.multiplelistcontactnumber);
                converView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) converView.getTag();
            }

            viewHolder.mname.setText(items.getString(items.getColumnIndex("name")));
            viewHolder.mnumber.setText(items.getString(items.getColumnIndex("phone_number")));
            viewHolder.mcheck.setChecked(false);
            if (itemChecked[position])
                viewHolder.mcheck.setChecked(true);
            else
                viewHolder.mcheck.setChecked(false);

            viewHolder.mcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Cursor c = getCursor();
                    c.moveToPosition(position);
                    String rowId = c.getString(c.getColumnIndex("phone_number"));
                    if (buttonView.isChecked())
                        itemChecked[position] = true;
                    else
                        itemChecked[position] = false;

                    if (buttonView.isChecked()) {
                        checked.add(rowId.toString());
                    }
                    else {
                        checked.remove(rowId.toString());
                    }

                    //Toast.makeText(getApplicationContext(), ""+checked.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            return converView;
        }
    }
    public class ViewHolder{
        CheckBox mcheck;
        TextView mname;
        TextView mnumber;
    }
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("number_list",checked.toString());
        setResult(4,intent);
        //onBackPressed();
        finish();

    }
}

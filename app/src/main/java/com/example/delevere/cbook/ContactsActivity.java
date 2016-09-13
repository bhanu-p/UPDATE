package com.example.delevere.cbook;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ContactsActivity extends AppCompatActivity {


    Cursor cursor1;
    private DataBaseHelper db;
    private SimpleCursorAdapter dataAdapter;
    static Context mContext;
    Cursor cur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        db = new DataBaseHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.contacttoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mContext = ContactsActivity.this;



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

                SharedPreferences sharedPreferences = getSharedPreferences(Session.PREFS_NAME, 0);
                String mynumber = sharedPreferences.getString(Session.KEY_phone, "9999999999");
                db.inserData("You","పవన్");

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
            Toast.makeText(ContactsActivity.this, "Contacts empty", Toast.LENGTH_SHORT).show();
        }
        else{ new Thread(new Runnable() {
            public void run() {
            displayContactsNew();
            }
        }).start();

        }





        db.close();


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.contact, menu);

        return super.onCreateOptionsMenu(menu);


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh: {

                if (weHavePermissionToReadContacts()) {
                    MyTask myTask = new MyTask(this);
                    myTask.execute("parameter");
                }else {
                    requestReadContactsPermissionFirst();

                }



                break;
            }
            case R.id.deletedcontacts: {
                startActivity(new Intent(getApplicationContext(),DeletedContacts.class));


                break;
            }
            case R.id.export:{


                //LogData();
                //getVCF();
                Activity mActivity = ContactsActivity.this;



                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        MyExport myExport = new MyExport(ContactsActivity.this);
                        myExport.execute("parameter");


                    }
                });






            }

            default:
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }

    public class MyTask extends AsyncTask<String, String, String> {
        private Context context;
        private ProgressDialog progressDialog;

        public MyTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {



            db.truncatstudent();


            if (weHavePermissionToReadContacts()) {

                readTheContacts();
                cursor1 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                startManagingCursor(cursor1);
                SharedPreferences sharedPreferences = getSharedPreferences(Session.PREFS_NAME, 0);
                String mynumber = sharedPreferences.getString(Session.KEY_phone, "9999999999");
                db.inserData("You","పవన్ ");


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

            db.close();
            return "finish";
        }


        @Override
        protected void onPostExecute(String result) {
            displayContactsNew();
            getContacts();
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
            //Start other Activity or do whatever you want
        }
    }

    public class MyExport extends AsyncTask<String, String, String> {
        private Context context;
        private ProgressDialog progressDialog;

        public MyExport(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Exporting to Phone Storage ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {





            if (weHavePermissionToReadContacts()) {

                readTheContacts();
                final String vfile = "Cbookbackup.vcf";
                Cursor phones = mContext.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        null, null, null);
                phones.moveToFirst();
                String path = Environment.getExternalStorageDirectory().toString() + File.separator + vfile;
                File myFile = new File(path);
                if(myFile.exists()) {
                    myFile.delete();
                }
                for (int i = 0; i < phones.getCount(); i++) {
                    String lookupKey = phones.getString(phones
                            .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                    Uri uri = Uri.withAppendedPath(
                            ContactsContract.Contacts.CONTENT_VCARD_URI,
                            lookupKey);

                    //Intent intent = new Intent(Intent.ACTION_VIEW);
                    //intent.setDataAndType(Uri.fromFile(new File(path + vfile)), "text/x-vcard"); //storage path is path of your vcf file and vFile is name of that file.
                    //startActivity(intent);
                    AssetFileDescriptor fd;
                    try {
                        fd = mContext.getContentResolver().openAssetFileDescriptor(uri, "r");
                        FileInputStream fis = fd.createInputStream();
                        byte[] buf = new byte[(int) fd.getDeclaredLength()];
                        fis.read(buf);
                        String VCard = new String(buf);
                        //String path = Environment.getExternalStorageDirectory()
                        //        .toString() + File.separator + vfile;
                        FileOutputStream mFileOutputStream = new FileOutputStream(path,
                                true);
                        mFileOutputStream.write(VCard.toString().getBytes());
                        phones.moveToNext();
                        Log.d("Vcard", VCard);
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }

            else {
                requestReadContactsPermissionFirst();
            }



            return "finish";
        }


        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            Toast.makeText(ContactsActivity.this, "Contacts Export  to \n Storage/Cbookbackup.vcf.", Toast.LENGTH_SHORT).show();
            //Start other Activity or do whatever you want
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

        final ListView listView = (ListView) findViewById(R.id.list);
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
                        cursor.getString(cursor.getColumnIndexOrThrow("name"));
                Toast.makeText(getApplicationContext(),
                        countryCode, Toast.LENGTH_SHORT).show();

            }
        });

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


}

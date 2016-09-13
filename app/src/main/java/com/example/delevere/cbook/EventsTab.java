package com.example.delevere.cbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLClientInfoException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EventsTab extends Fragment {
    public static final String PREFS_NAME = "LoginPrefs";
    private static final int RESULT_PICK_CONTACT = 85500;
    DatePicker pickerDate;
    TimePicker pickerTime;
    ListView listView;
    Button pick;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    private int mYear, mMonth, mDay, mHour, mMinute;
    static final int DATE_DIALOG_ID = 0;
    int RQS_1 = 1;
    static String person="";
    private DataBaseHelper db;
    private SimpleCursorAdapter dataAdapter;
    boolean doubleBackToExitPressedOnce = false;
    SwipeRefreshLayout swipeView;
    Cursor cursor;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_events_tab, container, false);
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes

        listView = (ListView) rootView.findViewById(R.id.listView);

        db = new DataBaseHelper(getContext());
        getbroadcastevents();
        displayEventNew();



        return rootView;
    }

    public void requery() {
        Cursor values = db.getAllEvents();
        dataAdapter.changeCursor(values);
    }

    public void displayEventNew() {
        DataBaseHelper dd = new DataBaseHelper(getActivity());
        cursor = db.getAllEvents();

        //pick =(ImageView) findViewById(R.id.listeventcontactpick);

        String[] columns = new String[] {
                DataBaseHelper.EVENT_ID,
                DataBaseHelper.EVENT_CONTACT,
                DataBaseHelper.EVENT_CREATED_BY,
                DataBaseHelper.EVENT_TYPE,
                DataBaseHelper.EVENT_DATE_TIME,


        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.hideid,
                R.id.listeventcontactname,
                R.id.listeventcontactnumber,
                R.id.listeventtype,
                R.id.evnttime,



        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information

        dataAdapter = new SimpleCursorAdapter(
                this.getContext(), R.layout.event_list,
                cursor,
                columns,
                to,
                0);





        dataAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            /** Binds the Cursor column defined by the specified index to the specified view */
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.listeventtype) {
                    String imageType = cursor.getString(cursor.getColumnIndexOrThrow("image"));

                    //int imageType = cursor.getInt(columnIndex);
                    int imageToBeShown = 0;
                    switch (imageType) {
                        case "R.drawable.bday":
                            imageToBeShown = R.drawable.bday;
                            break;
                        case "R.drawable.anniversary":
                            imageToBeShown = R.drawable.anniversary;
                            break;
                        case "R.drawable.events":
                            imageToBeShown = R.drawable.events;
                            break;

                    }
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;

                    BitmapFactory.decodeResource(getResources(), imageToBeShown, options);
                    String image = options.outMimeType;
                    //...
                    ((ImageView) view).setImageResource(imageToBeShown);
                    return true; //true because the data was bound to the view
                }
                if(view.getId() == R.id.evnttime){
                    String dt = cursor.getString(cursor.getColumnIndexOrThrow("date_time"));
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date res = null;
                    try {
                        res = df.parse(dt);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    SimpleDateFormat df2 = new SimpleDateFormat("dd, MMM");
                    String dateText = df2.format(res);
                    ((TextView)view).setText(dateText);
                    return true;
                }
                if (view.getId() == R.id.listeventcontactnumber){
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Session.PREFS_NAME,0);
                    String phonelocal = sharedPreferences.getString(Session.KEY_phone, "9999999999");
                    String created =cursor.getString(cursor.getColumnIndexOrThrow("created_by"));
                    String status = "By:";
                    String name = "";
                    //Toast.makeText(getActivity(), created.toString(), Toast.LENGTH_SHORT).show();
                    //Cursor cur = db.getContactName(created);

                    if(created.equals(phonelocal)){
                        status +=" Me";
                    }
                    /*else if(cur != null){
                        status += name;
                        //status +="Others";
                    }*/
                    else{

                        try {
                            Cursor cu = db.getName(created);
                            person = cu.getString(cu.getColumnIndexOrThrow("name"));
                        }catch (Exception e){
                            person = created;

                        }
                        status += person;
                    }
                        ((TextView)view).setText(status);
                        return true;


                }



                return false;
            }
        });

       // cursor.registerDataSetObserver(new DataSetObserver() {
           // @Override
           // public void onChanged() {
             //   super.onChanged();
         //   }
       // });

        dataAdapter.notifyDataSetChanged();

        listView.setAdapter(dataAdapter);
        cursor.requery();

       // listView.setAdapter(dataAdapter);

        //Collections.sort((List<Comparable>) listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String countryCode =
                        cursor.getString(cursor.getColumnIndexOrThrow("date_time"));

                String evname =
                        cursor.getString(cursor.getColumnIndexOrThrow("event_name"));

                String evphone =
                        cursor.getString(cursor.getColumnIndexOrThrow("contact_name"));

                String evtime =
                        cursor.getString(cursor.getColumnIndexOrThrow("date_time"));

                String image =
                        cursor.getString(cursor.getColumnIndexOrThrow("image"));

                String hiddenid =
                        cursor.getString(cursor.getColumnIndexOrThrow("_id"));


                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date res = null;
                try {
                    res = (Date) df.parse(countryCode);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Calendar c = Calendar.getInstance();
                c.setTime(res);

                String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
                String month = String.valueOf(c.get(Calendar.MONTH));
                String year = String.valueOf(c.get(Calendar.YEAR));
                String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
                String min = String.valueOf(c.get(Calendar.MINUTE));


                Intent intent = new Intent(getActivity(), EventDisplay.class);
                intent.putExtra("key id", hiddenid);
                intent.putExtra("image",image);

                startActivity(intent);
                Toast.makeText(getActivity(), "Event Time :" + hour + ":" + min, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(),
                //        day+"."+month+"."+year+"."+hour+"."+min, Toast.LENGTH_SHORT).show();

            }
        });


    }
    public void onResume() {
        super.onResume();
        dataAdapter.getCursor().requery();
    }

    private void getbroadcastevents() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Session.PREFS_NAME,0);
        String phone = sharedPreferences.getString(Session.KEY_phone,"9999999999");



        String url = Config.DATA_URL+phone;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //.makeText(getApplicationContext(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

        displayEventNew();

    }

    private void showJSON(String response){
        //DataBaseHelper db = new DataBaseHelper(this);
        String number="";
        String type="";
        String date = "";
        String creted = "";
        String image = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            for(int i=0; i<result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                number = collegeData.getString(Config.KEY_NUMBER);
                type = collegeData.getString(Config.KEY_EVENT_TYPE);
                date = collegeData.getString(Config.KEY_EVENT_DATE);
                creted = collegeData.getString(Config.KEY_EVENT_CREATED);
                image = collegeData.getString(Config.KEY_EVENT_IMAGE);
                String name = "mine";
                Cursor cur = db.getContactName(number);
                try {
                    name = cur.getString(cur.getColumnIndexOrThrow("name"));
                }catch (Exception e){
                    continue;
                }


                db.inserEvent(name,number ,type, date,image,creted);
                //db.inserBroadcaseEvent("", number, type, date);
                //Toast.makeText(OthersEvent.this, number+"\n"+type+"\n"+date, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




    //Overriden method onCreateView
    @Override


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.addevent, menu);

        TextView text= null;

        SearchView mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();

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
                return db.fetchCountriesByName(constraint.toString());
            }
        });

        /** Get the edit text from the action view */
        //EditText txtSearch = ( EditText ) v.findViewById(R.id.txt_search);
        //
        }


    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.crefresh: {
                getbroadcastevents();


                //db.truncatstudent();
                //startActivity(new Intent(getActivity(), CreateEventActivity.class));
                //Toast.makeText(getActivity(), "Refreshed", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.sms: {
                startActivity(new Intent(getActivity(),SmsActivity.class));
                return true;
            }
            case R.id.event: {
                //db.truncatstudent();
                startActivity(new Intent(getActivity(), CreateEventActivity.class));
                //Toast.makeText(getActivity(), "Refreshed", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.contacts: {

                try {
                    startActivity(new Intent(getActivity(), ContactsActivity.class));
                }catch (Exception e){
                    Toast.makeText(getActivity(), "some error", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getActivity(), "Contacts", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.profile: {
                Toast.makeText(getActivity(), "Profile page", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(),Profile.class));
                return true;

            }





        }return false;
    }


}



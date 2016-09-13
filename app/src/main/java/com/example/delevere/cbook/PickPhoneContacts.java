package com.example.delevere.cbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.content.Context;
import android.widget.ArrayAdapter;


public class PickPhoneContacts extends AppCompatActivity {
    ListView listview;
    String[] foody;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_phone_contacts);
        context = this;
        listview = (ListView)findViewById(R.id.phone_contact_list);
        //string array

        String[] foody = {"pizza", "burger", "chocolate", "ice-cream", "banana", "apple"};
        // set adapter for listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.phone_contact_list_view, foody);
        listview.setAdapter(adapter);
        listview.setItemsCanFocus(false);
        // we want multiple clicks
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }
}

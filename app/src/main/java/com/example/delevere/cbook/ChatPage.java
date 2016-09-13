package com.example.delevere.cbook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.delevere.cbook.Adapters.ChatListAdapter;
import com.example.delevere.cbook.Adapters.CustomAdapter;
import com.example.delevere.cbook.beans.Chat;
import com.example.delevere.cbook.db.ChatBean;
import com.example.delevere.cbook.db.ChatModel;
import com.example.delevere.cbook.helpers.ConnectionDetector;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ChatPage extends AppCompatActivity {
    ListView lv;
   // private DataBaseHelper dbase;
    ImageButton send;
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    public String key;
    private ChatListAdapter chatListAdapter;
    public String num, myNo, user;
    private ValueEventListener connectedListener;
    private CustomAdapter customAdapter;
    private ArrayList<ChatBean> chats = new ArrayList<>();
    private Firebase ref;

    private String url = "https://cbook-f44ed.firebaseio.com/";


    @SuppressLint({ "InflateParams", "SimpleDateFormat" })
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.chattoolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);
        //dbase = new DataBaseHelper(getApplicationContext());
        lv = (ListView) findViewById(R.id.chat_list);
        String key_name = getIntent().getStringExtra("name");
        myNo = getIntent().getExtras().getString("myNo");
        user = getIntent().getExtras().getString("name");
        num = getIntent().getExtras().getString("num");
        key = numberSorting(myNo,num);
        getSupportActionBar().setTitle(key_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        final ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        final Boolean isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            cd.showAlertDialog(ChatPage.this, "No Internet Connection",
                    "Press Ok to redirect to Network Connectionsß", false);
        }

        ChatModel cm = new ChatModel(getApplicationContext());
        chats = cm.getChats(key);

        customAdapter = new CustomAdapter(ChatPage.this,chats,myNo);
        lv.setAdapter(customAdapter);
        lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lv.setStackFromBottom(true);

//        chatListAdapter = new ChatListAdapter(list, this, R.layout.list_chat, myNo);
//        lv.setAdapter(chatListAdapter);
//        lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//        lv.setStackFromBottom(true);

        ChatModel.closeDBConnection(cm);

//        final ListView lv = getListView();

        ref = new Firebase(url).child("zChats");
        chatListAdapter = new ChatListAdapter(ref.child(key).limitToLast(100), this, R.layout.list_chat, myNo,num);
        lv.setAdapter(chatListAdapter);
        lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lv.setStackFromBottom(true);


        connectedListener = ref.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Chat post = postSnapshot.getValue(Chat.class);
                    System.out.println(post.getMessage() + " - " + post.getSender());
                    //Toast.makeText(ChatPage.this, post.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(ChatPage.this, "Failed. Try Again!", Toast.LENGTH_SHORT).show();

            }
        });

        ref.getRoot().child(".info/connected").removeEventListener(connectedListener);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatPage.this,Home2Activicy.class);
                intent.putExtra("EXTRA_PAGE", "2");
                startActivity(intent);
            }
        });
        //lv = (ListView) findViewById(R.id.chat_list);
        send = (ImageButton) findViewById(R.id.sendButton);

        //adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listItems);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                final Boolean isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    cd.showAlertDialog(ChatPage.this, "No Internet Connection",
                            "Press Ok to redirect to Network Connections", false);
                }
                else
                {
                    sendMessage();
                }



                /*
                EditText inputText = (EditText)findViewById(R.id.messageInput);
                String input = inputText.getText().toString();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat tim = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String tym = tim.format(c.getTime());

                if (!input.equals("")) {

                    listItems.add(inputText.getText().toString());
                    inputText.setText("");
                    adapter.notifyDataSetChanged();

                }*/

            }
        });
    }



    @SuppressLint("SimpleDateFormat")
    private void sendMessage() {
        EditText inputText = (EditText)findViewById(R.id.messageInput);
        String input = inputText.getText().toString();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat tim = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String tym = tim.format(c.getTime());

        if (!input.equals("")) {

            Chat chat = new Chat(input, myNo, tym);
            ref.child(key).push().setValue(chat);
            inputText.setText("");

            ChatModel cm = new ChatModel(getApplicationContext());
            if (cm.addNewRecord(myNo, tym, num, key, input)) {
                //dbase.timeStamp(num);

                ChatBean cb = new ChatBean();
                cb.setSentBy(myNo);
                cb.setSentAt(tym);
                cb.setSentTo(num);
                cb.setMsg(input);
                customAdapter.addItem(cb);
                customAdapter.notifyDataSetChanged();
//                CustomAdapter customAdapter = new CustomAdapter(UserActivity.this,chats,myNo);
//                customAdapter.addItem(cb);
//                customAdapter.notifyDataSetChanged();
                //lv.setAdapter(customAdapter);
                //lv.smoothScrollToPosition(customAdapter.getCount());
                //lv.setAdapter(customAdapter);

            }
            ChatModel.closeDBConnection(cm);


        }
    }

    public String numberSorting(String myNo , String num){
        String result = myNo + '_' + num;
        try{
            if(Long.parseLong(myNo) > Long.parseLong(num))
            {
                result = num + '_' + myNo;
            }
        }
        catch(Exception e){
            e.getStackTrace();
        }
        return result;
    }

}

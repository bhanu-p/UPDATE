package com.example.delevere.cbook.Adapters;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.delevere.cbook.DataBaseHelper;
import com.example.delevere.cbook.R;
import com.example.delevere.cbook.beans.Chat;
import com.example.delevere.cbook.db.ChatModel;
import com.firebase.client.Query;

public class ChatListAdapter extends FirebaseListAdapter<Chat> {

    private String number;
	private String sender;
	private Activity activitylocal;
	private DataBaseHelper dbase;
    public ChatListAdapter(Query ref, Activity activity, int layout, String number,String sender) {
        super(ref, Chat.class, layout, activity);
        this.number = number;
		this.sender = sender;
		this.activitylocal = activity;
		this.dbase = new DataBaseHelper(activity);
    }


    @Override
    protected void populateView(View view, Chat chat) {
        	String num = chat.getSender();
        	TextView ch = (TextView) view.findViewById(R.id.tv_chat);
        	TextView sent = (TextView) view.findViewById(R.id.tv_time);

        if (num.equals(number)) {
			dbase.timeStamp2(sender, chat.getSent_time());
        	ch.setText(chat.getMessage());

        	ch.setGravity(Gravity.RIGHT);
			ch.setBackgroundResource(R.drawable.bubble_a);
			sent.setGravity(Gravity.RIGHT);
			sent.setText(chat.getSent_time());
        	sent.setTextSize(11);

        }
         else {
			ChatModel cm = new ChatModel(activitylocal);
			//if (cm.addNewRecord(num, chat.getSent_time(), sender, , input)) {
			dbase.timeStamp2(sender,chat.getSent_time());
        	ch.setText(chat.getMessage());
        	ch.setGravity(Gravity.LEFT);
			ch.setBackgroundResource(R.drawable.bubble_b);
			sent.setGravity(Gravity.LEFT);
        	sent.setText(chat.getSent_time());
        	sent.setTextSize(11);
        }
    }
}

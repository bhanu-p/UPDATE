package com.example.delevere.cbook.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.delevere.cbook.R;
import com.example.delevere.cbook.beans.Chat;
import com.example.delevere.cbook.db.ChatBean;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Activity context;
    ViewHolder holder;
    private String yourNum;
    private ArrayList<ChatBean> names = new ArrayList<ChatBean>();


    public CustomAdapter(Activity context,ArrayList<ChatBean> chats, String yourNum) {
        super();
        this.context = context;
        this.names = chats;
        this.yourNum = yourNum;
    }

    public void addItem(ChatBean sD) {
        this.names.add(sD);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.names.size();
    }

    @Override
    public Object getItem(int i) {
        return this.names.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;

        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_chat, null);

            // configure view holder
            holder = new ViewHolder();
            holder.tv_chat = (TextView) rowView.findViewById(R.id.tv_chat);
            holder.tv_time = (TextView) rowView.findViewById(R.id.tv_time);
            rowView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) rowView.getTag();
        }
        // fill data
        try {

                String num = names.get(i).getSentBy();

                if (num.equals(yourNum)) {
                    holder.tv_chat.setText(names.get(i).getMsg());
                    holder.tv_chat.setBackgroundResource(R.drawable.bubble_a);
                    holder.tv_chat.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                    holder.tv_chat.setPadding(250, 5, 40, 15);
                    holder.tv_time.setText(names.get(i).getSentAt());
                    holder.tv_time.setTextSize(11);
                    holder.tv_time.setPadding(10, 10, 5, 15);
                }
                else {
                    holder.tv_chat.setText(names.get(i).getMsg());
                    holder.tv_chat.setBackgroundResource(R.drawable.bubble_b);
                    holder.tv_chat.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    holder.tv_chat.setPadding(40, 20, 250, 15);
                    holder.tv_time.setText(names.get(i).getSentAt());
                    holder.tv_time.setTextSize(11);
                    holder.tv_time.setPadding(500, 20, 5, 15);
                }


        }catch(Exception e)
        {
            e.printStackTrace();
        }

        return rowView;
    }

    public class ViewHolder {
        private TextView tv_chat;
        private TextView tv_time;
    }
}

package com.example.delevere.cbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Greetings extends AppCompatActivity {
    TextView name;
    Button send,cancel;
    EditText msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greetings);
        name = (TextView)findViewById(R.id.greetname);
        msg = (EditText)findViewById(R.id.greetmessage) ;
        send = (Button)findViewById(R.id.greetingbuttonsend);
        cancel = (Button)findViewById(R.id.greetingbuttoncancle);
        String str = getIntent().getStringExtra("name");
        final String num = getIntent().getStringExtra("number");
        final String from = getIntent().getStringExtra("from");


        name.setText(str);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Greetings.this, "hi", Toast.LENGTH_SHORT).show();
                String type = "greeting";
                UserLoginTask userLoginTask = new UserLoginTask(getApplicationContext());
                userLoginTask.execute(type,num.toString(),msg.getText().toString(),from.toString());
                onBackPressed();


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }
}

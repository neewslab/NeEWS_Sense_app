package com.neews.sense_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportGenerate extends AppCompatActivity{
    private static String firebaseDataSetName;
    //private static String str;
    //private static String ssr;
    private TextView screen;
    TextView receiver_msg;
    TextView receiver_msg2;
    TextView receiver_msg3;
    TextView receiver_msg4;
    TextView receiver_msg5;
    TextView receiver_msg6;
    String p_name;
    String pid;
    String dob;
    String t_id;
    String today;
    String result;
    String loc;
    public static final String FIREBASE_DATASET_NAME_FOR_UPLOAD = "com.neews.sense_app.FIREBASE_DATASET_NAME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // Log.i(MAIN_ACT_LOG_TAG, "ON CREATE");
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            p_name = bundle.getString("name");
            pid = bundle.getString("id");
            dob = bundle.getString("dob");
            t_id= bundle.getString("testid");
            result= bundle.getString("result");
            loc=bundle.getString("loc");



        }
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        String today = formatter.format(date);

       // String str = intent.getStringExtra(DisplayMessageActivity2.ssr);
        //String str1[]=str.split(" ");

        //String str2 = intent.getStringExtra(DisplayMessageActivity2.pat_id);
       // String str3 = intent.getStringExtra(DisplayMessageActivity2.dob);
        setContentView(R.layout.report);
        receiver_msg = findViewById(R.id.textView12);
        receiver_msg2 = (TextView)findViewById(R.id.textView13);
        receiver_msg3 = (TextView)findViewById(R.id.textView14);
        receiver_msg4=(TextView)findViewById(R.id.textView9);
        receiver_msg5=(TextView)findViewById(R.id.textView10);
        receiver_msg6=(TextView)findViewById(R.id.textView19);



        // receive the value by getStringExtra() method
        // and key must be same which is send by first activity
//        str = intent.getStringExtra(DisplayMessageActivity2.p_name);
//        String str2 = intent.getStringExtra("ssr2");
//        String str3 = intent.getStringExtra("ssr3");
       // String str="ABC";

        // display the string into textView
        receiver_msg.setText(p_name);
        receiver_msg2.setText(pid);
        receiver_msg3.setText(dob);
        receiver_msg4.setText(result);
        receiver_msg5.setText(today);
        receiver_msg6.setText(loc);
//        receiver_msg = (TextView)findViewById(R.id.received_value_id);
//
//        // create the get Intent object
//        Intent intent = getIntent();
//
//        // receive the value by getStringExtra() method
//        // and key must be same which is send by first activity
//        String str = intent.getStringExtra("FIREBASE_DATASET_NAME_FOR_UPLOAD");
//
//        // display the string into textView
//        receiver_msg.setText(str);
    }

    public void logout(View view) {
        // DO SOMETHING IN RESPONSE TO "STOP SENSE" BUTTON PRESS
        // Log.i(DISP_MSG_ACT_LOG_TAG, "SENSING STOPPED");
        // DisplayMessageActivity.this.finish();
        Intent intent = new Intent(this, Login.class);
        // EditText editText = (EditText) findViewById(R.id.firebaseDataSetName);
        // String firebase_proj_name = editText.getText().toString();
        //intent.putExtra("FIREBASE_DATASET_NAME_FOR_UPLOAD", firebaseDataSetName );
        //startActivity(intent);
        //Intent intent = new Intent(this,ReportGenerate.class);

        startActivity(intent);
        // FOLLOWED BY onDestroy()
    }
    public void retest(View view) {
        // DO SOMETHING IN RESPONSE TO "STOP SENSE" BUTTON PRESS
        // Log.i(DISP_MSG_ACT_LOG_TAG, "SENSING STOPPED");
        // DisplayMessageActivity.this.finish();
        Intent intent = new Intent(this,Diagonastics.class);

        // EditText editText = (EditText) findViewById(R.id.firebaseDataSetName);
        // String firebase_proj_name = editText.getText().toString();
        //intent.putExtra("FIREBASE_DATASET_NAME_FOR_UPLOAD", firebaseDataSetName );
        //startActivity(intent);
        //Intent intent = new Intent(this,ReportGenerate.class);

        startActivity(intent);
        // FOLLOWED BY onDestroy()
    }
}

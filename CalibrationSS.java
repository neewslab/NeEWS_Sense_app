package com.neews.sense_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CalibrationSS extends AppCompatActivity {
    public static final String FIREBASE_DATASET_NAME_FOR_UPLOAD = "com.neews.sense_app.FIREBASE_DATASET_NAME";
    static String p_name1;
    static String pat_id1;
    static String dob1;
    static String t_id1;
    static String loc;
    static String dev;

    private static final String MAIN_ACT_LOG_TAG = Diagonastics.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(MAIN_ACT_LOG_TAG, "ON CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calibrate_ss);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            p_name1 = bundle.getString("name");
            pat_id1 = bundle.getString("id");
            dob1 = bundle.getString("dob");
            t_id1= bundle.getString("testid");
            loc= bundle.getString("loc");
            dev= bundle.getString("dev");



        }
    }

    protected void onStart(Bundle savedInstanceState) {
        Log.i(MAIN_ACT_LOG_TAG, "ON START");
        setContentView(R.layout.calibrate);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            p_name1 = bundle.getString("name");
            pat_id1 = bundle.getString("id");
            dob1 = bundle.getString("dob");
            t_id1= bundle.getString("testid");
            loc= bundle.getString("loc");
            dev= bundle.getString("dev");



        }
    }

    @Override
    protected void onPause() {
        Log.i(MAIN_ACT_LOG_TAG, "ON PAUSE");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.i(MAIN_ACT_LOG_TAG, "ON RESTART");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i(MAIN_ACT_LOG_TAG, "ON RESUME");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.i(MAIN_ACT_LOG_TAG, "ON STOP");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(MAIN_ACT_LOG_TAG, "ON DESTROY");
        super.onDestroy();
    }

    /**
     * CALLED WHEN THE USER TAPS THE "SENSE" BUTTON
     */


    public void startSensing211(View view) {
        Log.i(MAIN_ACT_LOG_TAG, "'SENSE' BUTTON PRESSED");
        // SEND AN INTENT TO "DisplayMessageActivity" WITH FIREBASE DATASET NAME FOR UPLOAD

        EditText editText132 = (EditText) findViewById(R.id.editText132);
        EditText editText133 = (EditText) findViewById(R.id.editText133);
        EditText editText134 = (EditText) findViewById(R.id.editText134);
        EditText editText135 = (EditText) findViewById(R.id.editText135);
        String a = editText133.getText().toString();
        String b = editText134.getText().toString();
        String time = editText132.getText().toString();
        String df=editText135.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString("name",p_name1);
        bundle.putString("id",pat_id1);
        bundle.putString("dob",dob1);
        bundle.putString("testid",t_id1);
        bundle.putString("a",a);
        bundle.putString("b",b);
        bundle.putString("time",time);
        bundle.putString("df",df);
        bundle.putString("loc",loc);
        bundle.putString("dev",dev);

        Intent intent = new Intent(this, BeginTestss.class);
        // String firebase_proj_name="SMDS/"+pat_name+" "+pat_id+" "+dob+"/NanoVNA/"+testid;


        intent.putExtras(bundle);
//        intent.putExtra(pname,pat_name);
//        intent.putExtra(pid, pat_id);
//        intent.putExtra(db, dob);
        //intent.putExtra(tid,testid);
        startActivity(intent);
//        Intent i=getPackageManager().getLaunchIntentForPackage("net.lowreal.nanovnawebapp");
//        startActivity(i);


    }
    public void openapp(View view) {
        Log.i(MAIN_ACT_LOG_TAG, "'SENSE' BUTTON PRESSED");
        // SEND AN INTENT TO "DisplayMessageActivity" WITH FIREBASE DATASET NAME FOR UPLOAD


//        Intent i=getPackageManager().getLaunchIntentForPackage("net.lowreal.nanovnawebapp");
//        startActivity(i);


    }
}

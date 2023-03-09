package com.neews.sense_app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Diagonastics extends AppCompatActivity {

    public static final String FIREBASE_DATASET_NAME_FOR_UPLOAD = "com.neews.sense_app.FIREBASE_DATASET_NAME";
    public static final String pid=" ";
    public static final String db="";
    public static final String pname="";
    public static final String tid="";
    String dob="";

    private static final String MAIN_ACT_LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(MAIN_ACT_LOG_TAG, "ON CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagonastic);
        final EditText editText3 = findViewById(R.id.editText3);
       // dob = editText3.getText().toString();
       editText3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                       Diagonastics.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                editText3.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });
        dob = editText3.getText().toString();
    }

    protected void onStart(Bundle savedInstanceState) {
        Log.i(MAIN_ACT_LOG_TAG, "ON START");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagonastic);
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

    public void startSensing1(View view) {
        Log.i(MAIN_ACT_LOG_TAG, "'SENSE' BUTTON PRESSED");
        // SEND AN INTENT TO "DisplayMessageActivity" WITH FIREBASE DATASET NAME FOR UPLOAD
        Intent intent = new Intent(this, CalibrationSS.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        EditText editText2 = (EditText) findViewById(R.id.editText2);

        EditText editText4 = (EditText) findViewById(R.id.editText4);
        EditText editText404=(EditText)findViewById(R.id.editText404);
        EditText editText405=(EditText)findViewById(R.id.editText405);
        String pat_name = editText.getText().toString();
        String pat_id = editText2.getText().toString();

        String testid=editText4.getText().toString();
        String loc=editText404.getText().toString();
        String dev=editText405.getText().toString();
       // String firebase_proj_name="SMDS/"+pat_name+" "+pat_id+" "+dob+"/Sensit Smart/"+testid;
        Bundle bundle1 = new Bundle();
        bundle1.putString("name",pat_name);
        bundle1.putString("id",pat_id);
        bundle1.putString("dob",dob);
        bundle1.putString("testid",testid);
        bundle1.putString("loc",loc);
        bundle1.putString("dev",dev);
        intent.putExtras(bundle1);

        startActivity(intent);

        //Intent i=getPackageManager().getLaunchIntentForPackage("com.palmsens.pstouch");
        //startActivity(i);
    }

    public void startSensing2(View view) {
        Log.i(MAIN_ACT_LOG_TAG, "'SENSE' BUTTON PRESSED");
        // SEND AN INTENT TO "DisplayMessageActivity" WITH FIREBASE DATASET NAME FOR UPLOAD

        EditText editText = (EditText) findViewById(R.id.editText);
        EditText editText2 = (EditText) findViewById(R.id.editText2);
        EditText editText3 = (EditText) findViewById(R.id.editText3);
        EditText editText4 = (EditText) findViewById(R.id.editText4);
        EditText editText404=(EditText)findViewById(R.id.editText404);
        String pat_name = editText.getText().toString();
        String pat_id = editText2.getText().toString();
        String dob = editText3.getText().toString();
        String testid=editText4.getText().toString();
        String loc=editText404.getText().toString();

        Bundle bundle1 = new Bundle();
        bundle1.putString("name",pat_name);
        bundle1.putString("id",pat_id);
        bundle1.putString("dob",dob);
        bundle1.putString("testid",testid);
        bundle1.putString("loc",loc);
        Intent intent = new Intent(this, Calibration.class);
        //String firebase_proj_name="SMDS/"+pat_name+" "+pat_id+" "+dob+"/NanoVNA/"+testid;


        intent.putExtras(bundle1);
//        intent.putExtra(pname,pat_name);
//        intent.putExtra(pid, pat_id);
//        intent.putExtra(db, dob);
        //intent.putExtra(tid,testid);
        startActivity(intent);
//        Intent i=getPackageManager().getLaunchIntentForPackage("net.lowreal.nanovnawebapp");
//        startActivity(i);
    }
}

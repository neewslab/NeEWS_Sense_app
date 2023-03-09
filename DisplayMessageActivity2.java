package com.neews.sense_app;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
//import java.util.Calendar;
//import java.util.Random;


public class DisplayMessageActivity2 extends AppCompatActivity {
    private static final String DISP_MSG_ACT_LOG_TAG = DisplayMessageActivity.class.getSimpleName();
    private static FirebaseDatabase fb_database;
     static String firebaseDataSetName;
     String path="";
    private static String db;
    private static String pid;
    static String p_name;
    static String pat_id;
    static String dob;
    static String t_id;
    static String ssr;
    String a;
    String b;
    String f_low;
    String f_high;
   //static String ssr;
   String p10;
   String p20;
   String p30;
   String p40;
    double slope;
    double y_int;
    double min_ph;
    double res_fr;
    double res_fr1=0;
    double res_fr2=0;
    String loc;
    static String ssr2;
    static String ssr3;
    String st="";
    String result="";
    int len=0;
    int ctr=2;
    private ChildEventListener fb_child_event_listener;
    private DatabaseReference fb_data_ref;
    private TextView screen;
    private ScrollView screen_scroll;
    FirebaseUploadThread firebaseUploadThread;
    volatile public boolean threadRunning;
    volatile public long uploaded_data_count;
    volatile public boolean isListenerEnabled = false;


    public static final int SENSING_INTERVAL = 52000; // MILLISECONDS
    public static final int MAX_SENSED_VALUE = 4000; // MILLISECONDS

    // THREAD FOR UPLOADING SENSOR DATA TO FIREBASE DATABASE
    public  class FirebaseUploadThread implements Runnable {
        private String threadName;
        private String dataSetName;
        private long timer;
        Thread fbThread;
        List<Double> fr=new ArrayList<Double>();
        List<Double> phase=new ArrayList<Double>();

        public FirebaseUploadThread(String threadName, String dataSetName) {
            this.threadName = threadName;
            this.dataSetName = dataSetName;
            timer = System.currentTimeMillis() - SENSING_INTERVAL; // SET THIS TO 'SENSING_INTERVAL' MS LESS THAN CURRENT TIME, SUCH THAT UPLOADS START IMMEDIATELY
        }
        public File getFile()
        {
            path = Environment.getExternalStorageDirectory().toString()+"/Documents";
            len=path.length();

            File[] files=null;

            // Log.d("Files", "Path: " + path);
            File directory = new File(path);
            files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                int flag = 0;
                String stt=(files[i].toString());
                int l=len+1;
                String stt1=stt.substring(l,stt.length()-3);
                String stt2=stt1+"S1P";
                // String stt2="nanovna-baseline.S1P";
                if(stt1.contains("nano")) {
                    //if ((files[i].toString()).equals("nanovna-baseline.S1P"))
                    return files[i];
                }
            }
            return null;
//            if(files.length==0)
//                return null;
//            else
//                return path;
        }
        public String ReadData()
        {
        String upload="";
            int line_count=0;
            File sdcard = Environment.getExternalStorageDirectory();
            if(getFile()==null){
                st="No new data found!";
                return st;
            }
            else
            {
               String stt=(getFile().toString());
               int l=len+1;
               String stt1=stt.substring(l,stt.length()-3);
               String stt2=stt1+"S1P";
              // String stt2="nanovna-baseline.S1P";
               if(stt1.contains("nano")) {
                   File file = new File(path, stt2);
                   int ctr=1;
                   //st=file.toString();
                   try {
                       String lines="abc";
//                       BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
//                       while ((lines = bufferedReader.readLine()) != null ) {
//                            line_count++;
//                            }
//                       StringBuffer stringBuffer = new StringBuffer();
//                      // st = stt1;
//                       int count=1;
////                       while ((lines = bufferedReader.readLine()) != null ) {
//                            line_count++;
//                            }
                       //st=Long.toString(file.length()/1024);
                       BufferedReader bufferedReader1 = new BufferedReader(new FileReader(file));
                       while ((lines = bufferedReader1.readLine()) != null) {
                           //  stringBuffer.append(bufferedReader.readLine());
                           // String h = bufferedReader.readLine();
                           // st+="    "+count+"    "+h;

                      if(ctr>2)
                           calculate(lines);
                       ctr++;
                       }
                               //upload="ab";
                               //stringBuffer.append("    Data"+(count-1)+"   "+upload+"   ");
                      // List<Double> phase2 = phase.subList(10, phase.size());
                      // List<Double> fr2 = fr.subList(10, fr.size());

                              min_ph=Collections.min(phase);
                              res_fr=fr.get(phase.indexOf(min_ph));
                            // sensor_calibrate(p10,p20,p30,p40);
                      // slope=-1.8;
                      // y_int=560;
                              // double press=(res_fr-y_int)/slope;
                              // double roundpress = Math.round(press*100.0)/100.0;
                              // double roundresf = Math.round(res_fr*100.0)/100.0;
                       if(res_fr1==0) {
                           res_fr1 = res_fr;
                           st = "Initial data reading done";

                       }
                       else {
                           res_fr2=res_fr;
                           double del_x=Math.abs(res_fr2-res_fr1);
                           double rounddelx = Math.round(del_x*100.0)/100.0;
                           double press=Double.parseDouble(a)*del_x+Double.parseDouble(b);
                           st="Test Id:"+t_id+" Device:Nano-VNA Resonant frequency: "+res_fr+"MHz; Pressure:"+press+"ng/mL";

                       }





                               file.delete();
                       if(file.exists()) {
                           file.getCanonicalFile().delete();
                           if (file.exists()) {
                               getApplicationContext().deleteFile(file.getName());
                           }
                       }
                               return st;


                       }




                   catch (FileNotFoundException e) {
                    st="no data";
                    e.printStackTrace();
                    // st = "Now new data Found!";
                } catch (IOException e) {
                    st="wrong data";
                    e.printStackTrace();
                }

               }

               else
                   st="No data found";
                // st="Data";
            }
            screen.setText(st);
            return  st;
        }
        public void sensor_calibrate(String p10,String p20, String p30, String p40)
        {
            double nr=0,dr=0,intercept,reg;
            double xx[],xy[],yy[],y[];
            xx =new double[4];
            xy =new double[4];
            yy =new double[4];
            y=new double[4];

            y[0]=Double.parseDouble(p10);
            y[1]=Double.parseDouble(p20);;
            y[2]=Double.parseDouble(p30);;
            y[3]=Double.parseDouble(p40);;
            double x[]={10,20,30,40};
            double sum_y=0,sum_xy=0,sum_x=0,sum_xx=0,sum_x2=0;
            int i,n=4;
            for(i=0;i<n;i++)
            {
                xx[i]=x[i]*x[i];
                yy[i]=y[i]*y[i];
            }
            for(i=0;i<n;i++)
            {
                sum_x+=x[i];
                sum_y+=y[i];
                sum_xx+= xx[i];
                sum_xy+= x[i]*y[i];
            }
            nr=(n*sum_xy)-(sum_x*sum_y);
            sum_x2=sum_x*sum_x;
            dr=(n*sum_xx)-sum_x2;
            slope=nr/dr;
           // String s = String.format("%.2f",slope);
           // slope = Double.parseDouble(s);
            y_int=(sum_y- slope*sum_x)/n;
        }
            public void calculate(String lines1)
            {
                // JSONObject obj = new JSONObject();
                String upload="Data Reading complete";
                String vals[]=new String[3];

                if(lines1.length()!=0) {
                    vals = lines1.split("\\t", 3);

                    double r = Double.parseDouble(vals[1]);
                    double x = Double.parseDouble(vals[2]);
                    double f = Double.parseDouble(vals[0]) / Math.pow(10, 6);

                    double real = 50 * (1 - r * r - x * x) / (Math.pow((1 - r), 2) + x * x);
                    double img = 50 * (2 * x) / (Math.pow((1 - r), 2) + x * x);

                    double mag = Math.sqrt((Math.pow(real, 2)) + (Math.pow(img, 2)));

                    double ph = Math.atan2(img, real);
                    if(f>=Double.parseDouble(f_low) && f<=Double.parseDouble(f_high))
                    {
                        fr.add(f);
                        phase.add(ph);
                    }
                    upload = f+ ";" +ph;
                    // upload=lines1;
                }
             //   return upload;
            }
//        public File getFile()  {
//            String path = Environment.getExternalStorageDirectory().toString()+"/Documents";
//            File[] files=null;
//
//           // Log.d("Files", "Path: " + path);
//            File directory = new File(path);
//            files = directory.listFiles();
//                /*Log.d("Files", "Size: " + files.length);
//                for (int i = 0; i < files.length; i++) {
//                    Log.d("Files", "FileName:" + files[i].getName());
//                }*/
//
//
//
//            String nano="nanovna-baseline";
////            File sdcard = Environment.getExternalStorageDirectory();
////            String fname="/Documents/NVNA_Log.txt";
////            File file1 = new File(sdcard, fname);
//            // try {
//            // BufferedReader bufferedReader = new BufferedReader(new FileReader(file1));
//            //st = "Enter";
//
//            String lines;
//
//            //  File f=null;
//            //int flag=0;
//            //OUTER:
//            for (int i = 0; i < files.length; i++) {
//                int flag=0;
//                if ((files[i].toString()).equals("nanovna-baseline.S1P")) {
//                    // return files[i];
//                    /*try {
//                        //return files[i];
//                       // BufferedReader bufferedReader = new BufferedReader(new FileReader(file1));
//
//
//                       *//* while ((lines=bufferedReader.readLine())   != null) {
//                            if (files[i].toString().contains(lines) && i!=files.length-1  )
//                                flag = 1;
//                            //continue OUTER;
//                        }*//*
//
//                    }*/
//                    /*catch (FileNotFoundException e) {
//                        e.printStackTrace();
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }*/
//
//                    //finally {
//                    //if (flag == 0) {
//                    try{ /*BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file1,true));
//                                //FileWriter myWriter = new FileWriter(file1);
//                                bufferedWriter.write(files[i].toString()+"\n");
//
//                                //myWriter.close();
//                                bufferedWriter.close();*/
//                        return files[i];
//                    }
//                           /* catch (FileNotFoundException e) {
//                                e.printStackTrace();
//
//                            } catch (IOException e) {
//                                e.printStackTrace();
//
//                            }*/
//                    finally {
//return null;
//                    }
//
//                    //}
//                       /* else {
//
//                            continue;
//                        }*/
//                    //  }
//
//
//                }
//            }
//
//
//            return null;
//        }


//        public String ReadData()  {
//            String st = "xyz";
//            String rt="";
//            File sdcard = Environment.getExternalStorageDirectory();
//            int pos=0;
//            JSONObject obj = new JSONObject();
//            String s1="";
//            String stt = "";
//            if(getFile()==null){
//                st="No new data found!";
//                return st;
//            }
//            else {
//                st = (getFile().toString());
//
//                String stt1 = st.substring(19, 53);
//                stt = stt1 + ".S1P";
//                File file = new File(sdcard, stt);
//
//
//                try {
//
//                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
//
//                    StringBuffer stringBuffer = new StringBuffer();
//                    String lines;
//                    String ln="";
//                    String upload="";
//                    int count=1;
//                    rt="";
//                    int line_count=1;
//                    return "abc";
//                    //return "abc";
//                   /* while ((lines = bufferedReader.readLine()) != null ) {
//                        line_count++;
//                    }*/
////                    while ((lines = bufferedReader.readLine()) != null ) {
////                        if(count>1 && count>=ctr) {
////                            upload = calculate(lines, count);
////                            //stringBuffer.append("abc");
////                            //break;*/
////
////                            //stringBuffer.append(upload + ", ");
////                            rt = upload;
////                            count++;
////                            ctr++;
////                            return rt;
////                        }
////
////                        count++;
////
////                        /*if(count>line_count)
////                        {
////                            ctr=0;
////                            //file.delete();
////                        }*/
////                    }
//                    //screen.setText(stringBuffer.toString());
//                    //st = stringBuffer.toString();
//                    //file.delete();
//                } catch (FileNotFoundException e) {
//                    rt="no data";
//                    e.printStackTrace();
//                    // st = "Now new data Found!";
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            return rt;
//
//        }

//        public String calculate(String lines, int count)
//        {
//            // JSONObject obj = new JSONObject();
//            String upload="abc";
//            String vals[]=new String[3];
//
//            vals=lines.split(" ",3);
//
//            double r=Double.parseDouble(vals[1]);
//            double x=Double.parseDouble(vals[2]);
//            double f=Double.parseDouble(vals[0])/Math.pow(10,6);
//
//            double real=50*(1-r*r-x*x)/(Math.pow((1-r),2)+x*x);
//            double img=50*(2*x)/(Math.pow((1-r),2)+x*x);
//
//            double mag=Math.sqrt((Math.pow(real,2))+(Math.pow(img,2)));
//
//            double ph=Math.atan2(img,real);
//
//            upload= (count-1)+";"+f+";"+r+";"+x;
//
//            return upload;
//        }

        @Override
        public void run() {
            Log.i(DISP_MSG_ACT_LOG_TAG, "RUN FirebaseUploadThread");
            while (threadRunning) {
                if (System.currentTimeMillis()  > SENSING_INTERVAL) { // EVERY 'SENSING_INTERVAL'
                    // INITIALIZE DATA COUNT
                    fb_data_ref.child(dataSetName).orderByKey().addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                    uploaded_data_count = dataSnapshot.getChildrenCount();
                                    Log.i(DISP_MSG_ACT_LOG_TAG, "DATA COUNT IN CHOSEN DATA SET: " + uploaded_data_count);
                                    final long curr_data_count = uploaded_data_count;
                                    screen.post(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    screen.append("\n" + "Latest Data Count: " + curr_data_count + "\n");
                                                }
                                            });
                                    scrollToBottom();
                                    // ++ GENERATE OR COLLECT SENSOR DATA
                                    Random random_gen = new Random();
                                    final String random_sensor_data = ReadData();
                                    if (!random_sensor_data.equals("No new data found!") ) {
                                        result = st;
                                        uploaded_data_count++;
                                        /* UPLOAD DATA TO FIREBASE DATABASE
                                         * KEY: "dow mon dd hh:mm:ss zzz yyyy",
                                         * VALUE: "uploaded_data_count, random_sensor_data"
                                         * */
                                        final String curr_fb_data_key = Calendar.getInstance().getTime().toString();
                                        String curr_fb_data_val = uploaded_data_count + "," + random_sensor_data;
                                        final long curr_uploaded_data_count = uploaded_data_count;
                                        Log.i(DISP_MSG_ACT_LOG_TAG, "UPLOADING DATA: " + curr_uploaded_data_count + " TO FIREBASE");
                                        // PRINT UPLOAD ATTEMPT SUMMARY ON SCREEN
                                        screen.post(
                                                new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        screen.append("\nUploading Data: " + curr_uploaded_data_count + " ... Waiting for FB commit ..." + "\n");
                                                    }
                                                });
                                        scrollToBottom();
                                        fb_data_ref.child(dataSetName).child(curr_fb_data_key).setValue(curr_fb_data_val)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) { // WRITE WAS SUCCESSFUL
                                                        if (uploaded_data_count < curr_uploaded_data_count) { // DATA SYNC NEEDED - SOME DATA HAS BEEN REMOVED FROM FB
                                                            Log.i(DISP_MSG_ACT_LOG_TAG, "UPLOADED ID: " + curr_uploaded_data_count + " ACTUAL ID: " + uploaded_data_count);
                                                            Log.i(DISP_MSG_ACT_LOG_TAG, "DATA ID NEEDS TO BE UPDATED");
                                                            String updated_fb_data_val = uploaded_data_count + "," + random_sensor_data;
                                                            fb_data_ref.child(dataSetName).child(curr_fb_data_key).setValue(updated_fb_data_val).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) { // UPDATE SUCCESSFUL
                                                                    Log.i(DISP_MSG_ACT_LOG_TAG, "DATA: " + curr_uploaded_data_count + " UPLOAD TO FIREBASE SUCCESSFUL");
                                                                    // PRINT UPLOAD COMMIT ACKNOWLEDGEMENT ON SCREEN
                                                                    screen.post(
                                                                            new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    screen.append("\nData: " + curr_uploaded_data_count + " upload completed!" + "\n");
                                                                                    screen.append("\nLatest Data ID updated from: " + curr_uploaded_data_count + " to: " + uploaded_data_count + " to reflect current state of FB database" + "\n");
                                                                                }
                                                                            });
                                                                    scrollToBottom();
                                                                    // stopSensing();
                                                                }
                                                            });
                                                        } else { // DATA ALREADY SYNCED
                                                            Log.i(DISP_MSG_ACT_LOG_TAG, "DATA: " + curr_uploaded_data_count + " UPLOAD TO FIREBASE SUCCESSFUL");
                                                            // PRINT UPLOAD COMMIT ACKNOWLEDGEMENT ON SCREEN
                                                            screen.post(
                                                                    new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            screen.append("\nData: " + curr_uploaded_data_count + " upload completed!" + "\n");
                                                                        }
                                                                    });
                                                            scrollToBottom();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // WRITE FAILED
                                                Log.i(DISP_MSG_ACT_LOG_TAG, "DATA: " + uploaded_data_count + " UPLOAD TO FIREBASE FAILED");
                                                screen.post(
                                                        new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                screen.append("\nData: " + curr_uploaded_data_count + " upload failed!" + "\n");
                                                            }
                                                        });
                                                scrollToBottom();
                                            }
                                        });
                                        timer = System.currentTimeMillis();
                                        // -- GENERATE OR COLLECT SENSOR DATA
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }

                            }
                    );
                }
                try {
                    Thread.sleep(SENSING_INTERVAL / 20); // SLEEP FOR 'SENSING_INTERVAL' BEFORE NEXT UPLOAD
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void start() {
            Log.i(DISP_MSG_ACT_LOG_TAG, "START FirebaseUploadThread");
            Log.i(DISP_MSG_ACT_LOG_TAG, "Listing Latest Datasets in Firebase");
            fb_child_event_listener = fb_data_ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.i(DISP_MSG_ACT_LOG_TAG, "CHILD ADDED");
                    Log.i(DISP_MSG_ACT_LOG_TAG, "DATASET: " + dataSnapshot.getKey() + " | # OF ITEMS: " + dataSnapshot.getChildrenCount());
                    if (FirebaseUploadThread.this.dataSetName == dataSnapshot.getKey()) {
                        uploaded_data_count = dataSnapshot.getChildrenCount();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.i(DISP_MSG_ACT_LOG_TAG, "CHILD CHANGED");
                    Log.i(DISP_MSG_ACT_LOG_TAG, "DATASET: " + dataSnapshot.getKey() + " | # OF ITEMS: " + dataSnapshot.getChildrenCount());
                    if (FirebaseUploadThread.this.dataSetName == dataSnapshot.getKey()) {
                        uploaded_data_count = dataSnapshot.getChildrenCount();
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Log.i(DISP_MSG_ACT_LOG_TAG, "CHILD REMOVED");
                    Log.i(DISP_MSG_ACT_LOG_TAG, "DATASET: " + dataSnapshot.getKey() + " | # OF ITEMS: 0");
                    if (FirebaseUploadThread.this.dataSetName == dataSnapshot.getKey()) {
                        uploaded_data_count = 0;
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            threadRunning = true;
            new Thread(FirebaseUploadThread.this, threadName).start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(DISP_MSG_ACT_LOG_TAG, "ON CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message2);
        // GET THE INTENT THAT STARTED THIS ACTIVITY AND EXTRACT THE STRING PASSED
//        Intent intent = getIntent();
//        firebaseDataSetName = intent.getStringExtra(Diagonastics.FIREBASE_DATASET_NAME_FOR_UPLOAD);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            p_name = bundle.getString("name");
            pat_id = bundle.getString("id");
            dob = bundle.getString("dob");
            t_id= bundle.getString("testid");
            a=bundle.getString("a");
            b=bundle.getString("b");
            f_low=bundle.getString("f_low");
            f_high=bundle.getString("f_high");
            loc= bundle.getString("loc");




        }
        firebaseDataSetName="SMDS1/Test id-"+t_id+" Name-"+p_name+" Patient id-"+pat_id+" DOB-"+dob+" Location-"+loc+" Device-Nano VNA";
        if(!Utils.isPermissionGranted(this))
        {
            new AlertDialog.Builder(this).setTitle("All Files Permission")
                    .setMessage("Due to Android 11 restrictions, this app requires all files permission")
                    .setPositiveButton("Allow",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            takePermission();
                        }

                    })
                    .setNegativeButton("Deny",new DialogInterface.OnClickListener(){


                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();

        }
        else {
            Toast.makeText(this,"Permission already granted",Toast.LENGTH_LONG).show();
        }
        //p_name=intent.getStringExtra(Diagonastics.pname);
        //pat_id=intent.getStringExtra(Diagonastics.pid);
        //dob=intent.getStringExtra(Diagonastics.db);

        Log.i(DISP_MSG_ACT_LOG_TAG, "FIREBASE_DATASET_NAME: " + firebaseDataSetName);
        if (fb_database == null) {
            fb_database = FirebaseDatabase.getInstance();
            fb_database.setPersistenceEnabled(true); // FOR OFFLINE PERSISTENCE - RESTARTS
            fb_database.setPersistenceCacheSizeBytes(50000000); // 50 MB (HAS TO BE BETWEEN 10 AND 100 MB)
        }
        fb_data_ref = fb_database.getReference();
        fb_data_ref.keepSynced(true);
        Log.i(DISP_MSG_ACT_LOG_TAG, "LOCAL DATABASE SYNC ENABLED");

        // SET UP TEXTVIEW AND SCROLLVIEW
        screen = findViewById(R.id.textView);
        screen_scroll = (ScrollView) findViewById(R.id.SCROLLER_ID);
        screen.setText("Firebase Dataset for Sensor Data Upload: " + firebaseDataSetName + "\n\n");

        // EVERY SECOND, GENERATE NEW RANDOM DATA AND UPLOAD TO FIREBASE
        firebaseUploadThread = new FirebaseUploadThread("fb_upload_thread", firebaseDataSetName);
        firebaseUploadThread.start();
    }

    @Override
    public void onBackPressed() {
        Log.i(DISP_MSG_ACT_LOG_TAG, "BACK BUTTON PRESSED");
        super.onBackPressed();
        // FOLLOWED BY onDestroy()
    }

    protected void onStart(Bundle savedInstanceState) {
        Log.i(DISP_MSG_ACT_LOG_TAG, "ON START");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.i(DISP_MSG_ACT_LOG_TAG, "ON PAUSE");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.i(DISP_MSG_ACT_LOG_TAG, "ON RESTART");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i(DISP_MSG_ACT_LOG_TAG, "ON RESUME");
        super.onResume();
        if(!Utils.isPermissionGranted(this))
        {
            new AlertDialog.Builder(this).setTitle("All Files Permission")
                    .setMessage("Due to Android 11 restrictions, this app requires all files permission")
                    .setPositiveButton("Allow",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            takePermission();
                        }

                    })
                    .setNegativeButton("Deny",new DialogInterface.OnClickListener(){


                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();

        }
        else {
            Toast.makeText(this,"Permission already granted",Toast.LENGTH_LONG).show();
        }

    }


    private void takePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                //request for the permission
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);

            } catch (Exception e) {
                e.printStackTrace();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 101);
            }

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 101);


        }
    }


    public void onRequestPermissionResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(grantResults.length>0)
        {
            if(requestCode==101)
            {
                boolean readExt=grantResults[0]== PackageManager.PERMISSION_GRANTED;
                if(!readExt)
                {
                    takePermission();
                }
            }
        }
    }



    @Override
    public void onStop() {
        Log.i(DISP_MSG_ACT_LOG_TAG, "ON STOP");
        super.onStop();
    }

    @Override
    /* TRIGGERED ON BACK BUTTON PRESS OR ACTIVITY FINISH */
    public void onDestroy() {
        Log.i(DISP_MSG_ACT_LOG_TAG, "ON DESTROY");
        super.onDestroy();
        threadRunning = false;
        fb_data_ref.removeEventListener(fb_child_event_listener);
    }


    private void scrollToBottom() {
        screen_scroll.post(new Runnable() {
            public void run() {
                screen_scroll.smoothScrollTo(0, screen.getBottom());
            }
        });
    }

    /**
     * CALLED WHEN THE USER TAPS THE SENSE BUTTON
     */
    public void stopSensing(View view) {
        // DO SOMETHING IN RESPONSE TO "STOP SENSE" BUTTON PRESS
        Log.i(DISP_MSG_ACT_LOG_TAG, "SENSING STOPPED");
        // DisplayMessageActivity.this.finish();
        DisplayMessageActivity2.this.finish();
        Intent intent = getIntent();
        //firebaseDataSetName = intent.getStringExtra(firebaseDataSetName);
        //p_name=intent.getStringExtra(Diagonastics.pname);
        //pat_id=intent.getStringExtra(Diagonastics.pid);
        //dob=intent.getStringExtra(Diagonastics.db);
        //firebaseDataSetName = "ABC";
        //String ss="ABC";
        setContentView(R.layout.activity_display_message2);
        Intent inte = new Intent(getApplicationContext(), ReportGenerate.class);

        // EditText editText = (EditText) findViewById(R.id.firebaseDataSetName);
        // String firebase_proj_name = editText.getText().toString();
        //inte.putExtra(ssr,firebaseDataSetName);
        Bundle bundle2 = new Bundle();
        bundle2.putString("name",p_name);
        bundle2.putString("id",pat_id);
        bundle2.putString("dob",dob);
        bundle2.putString("testid",t_id);
        bundle2.putString("result",result);
        bundle2.putString("loc",loc);
        //bundle.putString("p10",p10);
        //bundle.putString("p20",p20);
        //bundle.putString("p30",p30);
        //bundle.putString("p40",p40);
       // inte.putExtra(ssr2,pat_id);
        //inte.putExtra(ssr3,dob);

        //startActivity(intent);
        //Intent intent = new Intent(this,ReportGenerate.class);
        inte.putExtras(bundle2);
        startActivity(inte);
        // FOLLOWED BY onDestroy()
    }
}
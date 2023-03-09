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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

public class DisplayMessageActivity extends AppCompatActivity {
    private static final String DISP_MSG_ACT_LOG_TAG = DisplayMessageActivity.class.getSimpleName();
    private static FirebaseDatabase fb_database;
    static String firebaseDataSetName;
    String path="";
    static String p_name;
    static String pat_id;
    static String dob;
    static String t_id;
    String st;
    static String time;
    static String df;
    static String a ;
    static String b;
    private static String db;
    private static String pid;
    static String loc;
    static String dev;
    String result="Concentraion values from Sensit Smart";
    //static String p_name;
   // static String pat_id;
   // static String dob;
    static String ssr;
    static String ssr2;
    static String ssr3;
    int len=0;
    int ctr=1;
    private ChildEventListener fb_child_event_listener;
    private DatabaseReference fb_data_ref;
    private TextView screen;
    private ScrollView screen_scroll;
    FirebaseUploadThread firebaseUploadThread;
    volatile public boolean threadRunning;
    volatile public long uploaded_data_count;
    volatile public boolean isListenerEnabled = false;

    public static final int SENSING_INTERVAL = 20000; // MILLISECONDS
    public static final int MAX_SENSED_VALUE = 4000; // MILLISECONDS

    // THREAD FOR UPLOADING SENSOR DATA TO FIREBASE DATABASE
    public class FirebaseUploadThread implements Runnable {
        private String threadName;
        private String dataSetName;
        private long timer;
        Thread fbThread;

        public FirebaseUploadThread(String threadName, String dataSetName) {
            this.threadName = threadName;
            this.dataSetName = dataSetName;
            timer = System.currentTimeMillis() - SENSING_INTERVAL; // SET THIS TO 'SENSING_INTERVAL' MS LESS THAN CURRENT TIME, SUCH THAT UPLOADS START IMMEDIATELY
        }
        public void getFile1() throws IOException {
//            String path = Environment.getExternalStorageDirectory().toString()+"/PStouch/file1.CSV";
//            // Use File
//            File file = new File(path);
//            // Use FileReader to red CSV file
//            FileReader fr = new FileReader(file);
//            // User BufferReader
//            BufferedReader br = new BufferedReader(fr);
//            String line = "";

            String[] tempArr;
            // User FileWriter to write content to text file
            FileWriter writer = new FileWriter("SS_temp.txt");
            // Use while loop to check when file contains data
//            while ((line = br.readLine()) != null) {
//                tempArr = line.split(",");
//                // User for loop to iterate String Array and write data to text file
//                for (String str : tempArr) {
//                    writer.write(str + " ");
//                }
//                // Write each line of CSV file to multiple lines
//                writer.write("\n");
//            }
           for(int k=1;k<=100;k++) {
               writer.write("ABC");
           }
            writer.close();

        }
        public File getFile()  {
            if(dev.contains("Pixel")) {
                path = Environment.getExternalStorageDirectory().toString() + "/Documents/PStouch";
                len = path.length();
                if (ctr == 1) {
                    String line = "";
                    String path1 = Environment.getExternalStorageDirectory().toString() + "/Documents/PStouch/file1.csv";
                    String csvfile = "file1.csv";
////            // Use File
                    File file = new File(path1);
////            // Use FileReader to red CSV file
                    try {
                        BufferedReader bufferedReader2 = new BufferedReader(new FileReader(file));
                        String[] tempArr;
                        File sdcard = Environment.getExternalStorageDirectory();
                        String fname = "/Documents/PStouch/SS_temp.txt";
                        File file1 = new File(sdcard, fname);
                        try {
                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file1));
                            while ((line = bufferedReader2.readLine()) != null) {
                                bufferedWriter.write(line + " ");
                                bufferedWriter.write("\n");
                            }
                            bufferedWriter.close();
                            file.delete();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (FileNotFoundException e) {
                        //st="no data";
                        e.printStackTrace();
                        // st = "Now new data Found!";
                    }

                }
            }
                else
                {
                path = Environment.getExternalStorageDirectory().toString() + "/PStouch";
                len = path.length();
                if (ctr == 1) {
                    String line = "";
                    String path1 = Environment.getExternalStorageDirectory().toString() + "/PStouch/file1.csv";
                    String csvfile = "file1.csv";
////            // Use File
                    File file = new File(path1);
////            // Use FileReader to red CSV file
                    try {
                        BufferedReader bufferedReader2 = new BufferedReader(new FileReader(file));
                        String[] tempArr;
                        File sdcard = Environment.getExternalStorageDirectory();
                        String fname = "/PStouch/SS_temp.txt";
                        File file1 = new File(sdcard, fname);
                        try {
                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file1));
                            while ((line = bufferedReader2.readLine()) != null) {
                                bufferedWriter.write(line + " ");
                                bufferedWriter.write("\n");
                            }
                            bufferedWriter.close();
                            file.delete();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (FileNotFoundException e) {
                        //st="no data";
                        e.printStackTrace();
                        // st = "Now new data Found!";
                    }
                }

            }

                File[] files = null;

                // Log.d("Files", "Path: " + path);
                File directory = new File(path);
                files = directory.listFiles();
                for (int i = 0; i < files.length; i++) {
                    int flag = 0;
                    String stt = (files[i].toString());
                    //if ((files[i].toString()).equals("nanovna-baseline.S1P"))
                    if (stt.contains("SS"))
                        return files[i];
                    else
                        continue;
                }

            return null;
//            if(files.length==0)
//                return null;
//            else
//                return path;
        }
        public String ReadData()  {


//            // User BufferReader
//            BufferedReader br = new BufferedReader(fr);
//            String line = "";


            st="not recorded";
            float time_fl=conv_toFloat(time);
            float a_fl=conv_toFloat(a);
            float b_fl=conv_toFloat(b);
            float df_fl=conv_toFloat(df);
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
                String stt1=stt.substring(l,stt.length());
                String stt2=stt1+"txt";
                //st=stt;
                // String stt2="nanovna-baseline.S1P";
//                if(stt1.contains("SS_01")) {
                   File file = new File(path, stt1);
                   //st=file.toString();
                    try {
                        String lines="abc";
                        //BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
//                        while ((lines = bufferedReader.readLine()) != null ) {
//                            line_count++;
//                        }
                        //StringBuffer stringBuffer = new StringBuffer();
                        // st = stt1;
                        int count=1;
//                       while ((lines = bufferedReader.readLine()) != null ) {
//                            line_count++;
//                            }
                        //st=Long.toString(file.length()/1024);
                        BufferedReader bufferedReader1 = new BufferedReader(new FileReader(file));
                        float current=0;
                        while ((lines = bufferedReader1.readLine()) != null) {
                            //  stringBuffer.append(bufferedReader.readLine());
                            // String h = bufferedReader.readLine();
                            // st+="    "+count+"    "+h;

                            String no_sp;
                          //  if(count>=ctr && count>12  ) {

                                if (lines.length() > 4 && count>12) {
//
                                    no_sp = lines.replaceAll("\\s", "");
                                    String hy[] = no_sp.split(",");
                                    float ti = conv_toFloat(hy[0]);

                                    if (ti == time_fl) {
                                        current = conv_toFloat(hy[1]);
                                        float y=(a_fl*current+b_fl)*df_fl;
                                        st ="Sample Concentration: "+ Float.toString(y)+"ng/mL";
                                        file.delete();
                                        return st;
                                    }





                                }
                            count++;

                        }
//
                       // st=stringBuffer.toString();

                    }
                    catch (FileNotFoundException e) {
                        st="no data";
                        e.printStackTrace();
                        // st = "Now new data Found!";
                    } catch (IOException e) {
                        st="wrong data";
                        e.printStackTrace();
                    }

                //}

//                else
//                    st="No data found";
//                // st="Data";
            }
            screen.setText(st);
            return  st;
        }
        public float conv_toFloat(String sd)
        {
            float fl=0;
            String sdd="";
            for (int i =0;i<sd.length();i++)
            {
                if(Character.isDigit(sd.charAt(i)) || sd.charAt(i)=='E'||sd.charAt(i)=='.')
                    sdd+=sd.charAt(i);

            }
            fl=Float.valueOf(sdd);
            return fl;
        }
        public String calculate(String lines1)
        {
            // JSONObject obj = new JSONObject();
            String upload="Data Reading complete";
            String vals[]=new String[2];

            if(lines1.length()!=0) {
                vals = lines1.split(",", 2);

                String r = vals[0];
                String x = vals[1];
//                double f = Double.parseDouble(vals[0]) / Math.pow(10, 6);
//
//                double real = 50 * (1 - r * r - x * x) / (Math.pow((1 - r), 2) + x * x);
//                double img = 50 * (2 * x) / (Math.pow((1 - r), 2) + x * x);
//
//                double mag = Math.sqrt((Math.pow(real, 2)) + (Math.pow(img, 2)));
//
//                double ph = Math.atan2(img, real);

                upload = r + ";" + x;
                // upload=lines1;
            }
            return upload;
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
                                    if (!random_sensor_data.equals("No new data found!")) {
                                        result = st;
                                        uploaded_data_count++;
                                        /* UPLOAD DATA TO FIREBASE DATABASE
                                         * KEY: "dow mon dd hh:mm:ss zzz yyyy",
                                         * VALUE: "uploaded_data_count, random_sensor_data"
                                         * */
                                        final String curr_fb_data_key = Calendar.getInstance().getTime().toString();
                                        String curr_fb_data_val = random_sensor_data;
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
                                                            String updated_fb_data_val = random_sensor_data;
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
        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            p_name = bundle.getString("name");
            pat_id = bundle.getString("id");
            dob = bundle.getString("dob");
            t_id= bundle.getString("testid");
            a=bundle.getString("a");
            b=bundle.getString("b");
            time=bundle.getString("time");
            df=bundle.getString("df");
            loc= bundle.getString("loc");
            dev= bundle.getString("dev");
        }
        firebaseDataSetName="SMDS1/Test id-"+t_id+" Name-"+p_name+" Patient id-"+pat_id+" DOB-"+dob+" Location-"+loc+" Device-Sensit Smart";
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
        DisplayMessageActivity.this.finish();
        Intent intent = getIntent();
        //firebaseDataSetName = intent.getStringExtra(firebaseDataSetName);
        //p_name=intent.getStringExtra(Diagonastics.pname);
        //pat_id=intent.getStringExtra(Diagonastics.pid);
        //dob=intent.getStringExtra(Diagonastics.db);
        //firebaseDataSetName = "ABC";
        //String ss="ABC";
        setContentView(R.layout.activity_display_message);
        Intent inte = new Intent(getApplicationContext(), ReportGenerate.class);

        // EditText editText = (EditText) findViewById(R.id.firebaseDataSetName);
        // String firebase_proj_name = editText.getText().toString();
        Bundle bundle2 = new Bundle();
        bundle2.putString("name",p_name);
        bundle2.putString("id",pat_id);
        bundle2.putString("dob",dob);
        bundle2.putString("testid",t_id);
        bundle2.putString("result",result);
        bundle2.putString("loc",loc);
        // inte.putExtra(ssr2,pat_id);
        //inte.putExtra(ssr3,dob);

        //startActivity(intent);
        //Intent intent = new Intent(this,ReportGenerate.class);
        inte.putExtras(bundle2);
        startActivity(inte);
        // FOLLOWED BY onDestroy()
    }
}
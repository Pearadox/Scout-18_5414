package com.pearadox.scout_5414;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.ToggleButton;
import android.widget.RadioGroup;
import static android.view.View.VISIBLE;
import java.util.List;
import android.widget.Spinner;
import android.widget.TextView;

// Debug & Messaging
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.widget.Toast;
import android.view.Gravity;


public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";        // This CLASS name
    String Pearadox_Version = " ";      // initialize
    private String deviceId;            // Android Device ID
    TextView txt_messageLine;
    Spinner spinner_Device;
    public String deviceSelected = " ";
    ArrayAdapter<String> adapter_dev;
    public String devSelected = " ";
    Spinner spinner_Student;
    public String studentSelected = " ";
    ToggleButton toggleLogon;
    RadioGroup radgrp_Scout;      RadioButton radioScoutTyp;
    Boolean logged_On = false;
    Boolean Scout_Match = false, Scout_Pit = false;
    private static final int REQUEST_ENABLE_BT = 1;
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfDatabaseReference;
    private ChildEventListener pfEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "******* Starting Pearadox-5414  *******");
        try {
            Pearadox_Version = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e("TestApp", e.getMessage());
        }
        Toast toast = Toast.makeText(getBaseContext(), "Pearadox ©2017  Ver." + Pearadox_Version, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//        Toast.makeText(this,"Device ID: " + deviceId, Toast.LENGTH_LONG).show();    // ** DEBUG
        Log.d(TAG, "Device ID: " + deviceId);                                       // ** DEBUG
        Pearadox.FRC514_Device = deviceId; 		// Save device ID

        setContentView(R.layout.activity_main);
        txt_messageLine = (TextView) findViewById(R.id.txt_messageLine);
        txt_messageLine.setText("Hello Pearadox!  Please Log yourself into Device. ");
        Spinner spinner_Device = (Spinner) findViewById(R.id.spinner_Device);
        String[] devices = getResources().getStringArray(R.array.device_array);
        adapter_dev = new ArrayAdapter<String>(this, R.layout.dev_list_layout, devices);
        adapter_dev.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Device.setAdapter(adapter_dev);
        spinner_Device.setSelection(0, false);
        spinner_Device.setOnItemSelectedListener(new device_OnItemSelectedListener());

        preReqs(); 				        // Check for pre-requisites
        isInternetAvailable();          // See if device has Internet
//        getFB_Data();                   // Retrieve data from Firebase D/B

        retrieveStudents();             // Get list of Students
        Spinner spinner_Student = (Spinner) findViewById(R.id.spinner_Student);
        ArrayAdapter adapter_Stud = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Pearadox.student_List);
        adapter_Stud.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Student.setAdapter(adapter_Stud);
        spinner_Student.setSelection(0, false);
        spinner_Student.setOnItemSelectedListener(new student_OnItemSelectedListener());

        toggleLogon = (ToggleButton) findViewById(R.id.toggleLogon);
        toggleLogon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radgrp_Scout = (RadioGroup) findViewById(R.id.radgrp_Scout);
                Spinner spinner_Device = (Spinner) findViewById(R.id.spinner_Device);
                Spinner spinner_Student = (Spinner) findViewById(R.id.spinner_Student);
                if (toggleLogon.isChecked()) {      // See what state we are in
//               if (devSelected.length() > 0) {
                switch (devSelected) {
                    case "Scout Master":         // Scout Master
                        Intent sm_intent = new Intent(MainActivity.this, ScoutMaster_Activity.class);
                        startActivity(sm_intent);        // Start the Scout Master activity
                        break;
                    case "Visualizer":          // Visualizer
//            	            TODO
                        break;
                    case ("Red-1"):             //#Red or Blue Scout
                    case ("Red-2"):             //#
                    case ("Red-3"):             //#
                    case ("Blue-1"):            //#
                    case ("Blue-2"):            //#
                    case ("Blue-3"):            //#####
//            	            TODO
                        Log.d(TAG, "### Red/Blue Scout ### " + devSelected);
                        if (Scout_Match) {
                            Intent smast_intent = new Intent(MainActivity.this, MatchScoutActivity.class);
                            Bundle SMbundle = new Bundle();
                            SMbundle.putString("dev", devSelected);             // Pass data
                            SMbundle.putString("stud", studentSelected);        //  to activity
                            smast_intent.putExtras(SMbundle);
                            startActivity(smast_intent);  	// Start Match Scout
                        } else {
                            if (Scout_Pit) {
                                Intent spit_intent = new Intent(MainActivity.this, PitScoutActivity.class);
                                Bundle SPbundle = new Bundle();
                                SPbundle.putString("dev", devSelected);             // Pass data
                                SPbundle.putString("stud", studentSelected);        //  to activity
                                spit_intent.putExtras(SPbundle);
                                startActivity(spit_intent);  	// Start Pit Scout
                            } else {
                                Log.e(TAG, "*** Error - Red/Blue Scout device selected but no TYPE indicator  ***");
                            }
                        }
                        break;
                    default:                //
                        Log.d(TAG, "DEV = NULL" );
//            	            TODO
                }

                } else {
                    Log.d(TAG, "!!!  Logged IN  !!!");
//                    logged_On = true;       // Logged ON
                    devSelected = "";       // Null
                    radgrp_Scout.setVisibility(View.GONE);    // Hide scout group
                    radgrp_Scout.setEnabled(false);
                    spinner_Device.setSelection(0);         //Reset to NO selection
                    spinner_Student.setSelection(0);        //*
                }
            }
       });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Logging Off " + devSelected , Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void getFB_Data() {
        Log.d(TAG, "getFB_Data");
        pfDatabase = FirebaseDatabase.getInstance();
        pfDatabaseReference = pfDatabase.getReference("teams");
        teamValueEventListener(pfDatabaseReference);


    }
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
private void preReqs() {
    boolean isSdPresent;
    isSdPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    Log.d(TAG, "SD card: " + isSdPresent);
    if (isSdPresent) { 		// Make sure FRC directory is there
        File extStore = Environment.getExternalStorageDirectory();
        File directFRC = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414");
        if(!directFRC.exists())  {
            if(directFRC.mkdir())
            { }        //directory is created;
        }
        File directImg = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/images");
        if(!directImg.exists())  {
            if(directImg.mkdir())
            { }        //directory is created;
        }
        File directMatch = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/match");
        if(!directMatch.exists())  {
            if(directMatch.mkdir())
            { }        //directory is created;
        }
        File directPit = new File(Environment.getExternalStorageDirectory() + "/download/FRC54414/pit");
        if(!directPit.exists())  {
            if(directPit.mkdir())
            { }        //directory is created;
        }
        Log.i(TAG, "FRC files created");
//        Toast toast = Toast.makeText(getBaseContext(), "FRC5414 ©2017  *** Files initialied ***" , Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//        toast.show();

    }  else {
        Toast.makeText(getBaseContext(), "There is no SD card available", Toast.LENGTH_LONG).show();
    }
}

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public boolean isInternetAvailable() {
        Log.i(TAG, "<<<< Checking Internet Status >>>>");
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); 	//test a site

            if (ipAddr.equals("")) {
                Log.d(TAG, "** No Internet available ** ");
                Toast.makeText(getBaseContext(), "There is no Internet available", Toast.LENGTH_LONG).show();
                return false;
            } else {
                Log.d(TAG, "** Internet is acccessible ** ");
                Toast.makeText(getBaseContext(), "Internet OK", Toast.LENGTH_LONG).show();
                return true;
            }

        } catch (Exception e) {
            return false;
        }
    }
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
private void retrieveStudents() {
    Log.d(TAG, "******* retrieveStudents  *******");
    int n = 0;
    Pearadox.student_List[0] = "";       // ** DEBUG **
    Pearadox.student_List[1] = "John Doe";
    Pearadox.student_List[2] = "Andrew Hartnett";
    Pearadox.student_List[3] = "Gale French";       // ** DEBUG **
    n = 4;
//  ToDo Replace with Firebase retrieve
    Pearadox.numStudents = n;
    Log.d(TAG, "###### Num Students = '" + Pearadox.numStudents + "'");

}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class device_OnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            devSelected = parent.getItemAtPosition(pos).toString();
            Log.d(TAG, ">>>>>  '" + devSelected + "'");
            BluetoothAdapter myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//            if (myBluetoothAdapter != null) {
//                Log.i(TAG, "Bluetooth is available");
//                if (!myBluetoothAdapter.isEnabled()) {
//                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//                }
//                Log.i(TAG, "Bluetooth is enabled");
                RadioGroup radgrp_Scout = (RadioGroup) findViewById(R.id.radgrp_Scout);
                switch (devSelected) {
                    case "Scout Master": 	            // Scout Master
//            	        TODO - Start Bluetooth as slave
                        radgrp_Scout.setVisibility(View.GONE);    // Hide scout group
                        radgrp_Scout.setEnabled(false);
                        break;
                    case "Visualizer": 		// Visualizer
                        radgrp_Scout.setVisibility(View.GONE);    // Hide scout group
                        radgrp_Scout.setEnabled(false);
                        break;
                    case ("Red-1"):             //#Red or Blue Scout
                    case ("Red-2"):             //#
                    case ("Red-3"):             //#
                    case ("Blue-1"):            //#
                    case ("Blue-2"):            //#
                    case ("Blue-3"):            //#####
                        radgrp_Scout.setVisibility(VISIBLE);    // Show scout group
                        radgrp_Scout.setEnabled(true);
                        break;
                    default:                // ?????
//            	        TODO  - Start Bluetooth as master
                }
            txt_messageLine.setText(" ");   // Clear Login message
//            } else {        // Device does not support Bluetooth
//                Log.d(TAG, "** Bluetooth is not availible ** ");
//                Toast.makeText(getBaseContext(), "** Bluetooth is not availible ** ", Toast.LENGTH_LONG).show();
//
//            }
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }

    private void start_Bluetooth(String devSelected) {

    }

    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    private class student_OnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            studentSelected = parent.getItemAtPosition(pos).toString();
            Log.d(TAG, ">>>>>  '" + studentSelected + "'");
            Pearadox.Student_ID = studentSelected;
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }
    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    public void RadioClick_Scout(View view) {
        Log.d(TAG, "@@ RadioClick_Scout @@");
        radgrp_Scout = (RadioGroup) findViewById(R.id.radgrp_Scout);
        int selectedId = radgrp_Scout.getCheckedRadioButtonId();
        radioScoutTyp = (RadioButton) findViewById(selectedId);
        String value = radioScoutTyp.getText().toString();
        Log.d(TAG, "RadioScout - Button '" + value + "'");
        if (value.equals("Match Scout")) { 	    // Match?
            Log.d(TAG, "Match Scout");
            Scout_Match = true;
            Scout_Pit = false;
         } else {                               // Pit
            Log.d(TAG, "Pit Scout");
            Scout_Match = false;
            Scout_Pit = true;
        }
    }
    private void teamValueEventListener(final DatabaseReference pfReference) {
        /*add ValueEventListener to update data in realtime*/
        pfReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*this is called when first passing the data and then whenever the data is updated*/
                String team_num="", team_name="", team_loc="";
                Pearadox_Firebase.teamsObj team = new Pearadox_Firebase.teamsObj(team_num, team_name, team_loc);
                Pearadox.numTeams = 0;
                int i = 0;
                   /*get the data children*/
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    /*get the values as a team object*/
//                    Pearadox_Firebase.teamsObj value = iterator.next().getValue(Pearadox_Firebase.class);
//                    teamList.add(value);
                    i++;
                }
                Pearadox.numTeams = i;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*listener failed or was removed for security reasons*/
            }
        });
    }

//###################################################################
//###################################################################
//###################################################################
@Override
public void onResume() {
    super.onResume();
    Log.v(TAG, "onResume");
    txt_messageLine = (TextView) findViewById(R.id.txt_messageLine);
//    txt_messageLine.setText("Prepare for next match ");
}
    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "OnDestroy");
    }

}

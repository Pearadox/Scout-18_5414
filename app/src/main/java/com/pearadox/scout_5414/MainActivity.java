package com.pearadox.scout_5414;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Iterator;


import static android.R.id.input;
import static android.view.View.VISIBLE;

// Debug & Messaging


public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";        // This CLASS name
    String Pearadox_Version = " ";      // initialize
    private String deviceId;            // Android Device ID
    TextView txt_messageLine;
    Spinner spinner_Device, spinner_Event;
    ImageView img_netStatus;            // Internet Status
    ArrayAdapter<String> adapter_dev, adapter_StudStr, adapter_Event;
    public String devSelected = " ";
    Spinner spinner_Student;
    public String studentSelected = " ";
    ToggleButton toggleLogon;
    Button btn_StoreData;
    private int num_PitObjs = 0, num_Photos = 0,num_MatchObjs = 0;
    RadioGroup radgrp_Scout;      RadioButton radioScoutTyp;
    Boolean logged_On = false;
    Boolean Scout_Match = false, Scout_Pit = false;
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfStudent_DBReference;
    private DatabaseReference pfDevice_DBReference;
    private DatabaseReference pfTeam_DBReference;
    private DatabaseReference pfPitData_DBReference;
    private DatabaseReference pfMatchData_DBReference;
    String team_num, team_name, team_loc;
    p_Firebase.teamsObj team_inst = new p_Firebase.teamsObj(team_num, team_name, team_loc);
    String key = null;

    // ===========================================================================
    pitData Pit_Data = new pitData();
    matchData Match_Data = new matchData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "******* Starting Pearadox-5414  *******");
        try {
            Pearadox_Version = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        Toast toast = Toast.makeText(getBaseContext(), "Pearadox Scouting App ©2017  Ver." + Pearadox_Version, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//        Toast.makeText(this,"Device ID: " + deviceId, Toast.LENGTH_LONG).show();    // ** DEBUG
        Log.w(TAG, "Device ID: " + deviceId);                                       // ** DEBUG
        Pearadox.FRC514_Device = deviceId;        // Save device ID
        setContentView(R.layout.activity_main);

        Pearadox.FRC_Event = "";
        txt_messageLine = (TextView) findViewById(R.id.txt_messageLine);
        txt_messageLine.setText("Hello Pearadox!  Please select Event and then Log yourself into Device.    ");
        final Spinner spinner_Event = (Spinner) findViewById(R.id.spinner_Event);
        String[] events = getResources().getStringArray(R.array.event_array);
        adapter_Event = new ArrayAdapter<String>(this, R.layout.dev_list_layout, events);
        adapter_Event.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Event.setAdapter(adapter_Event);
        spinner_Event.setSelection(0, false);
        spinner_Event.setOnItemSelectedListener(new event_OnItemSelectedListener());

        Spinner spinner_Device = (Spinner) findViewById(R.id.spinner_Device);
        String[] devices = getResources().getStringArray(R.array.device_array);
        adapter_dev = new ArrayAdapter<String>(this, R.layout.dev_list_layout, devices);
        adapter_dev.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Device.setAdapter(adapter_dev);
        spinner_Device.setSelection(0, false);
        spinner_Device.setOnItemSelectedListener(new device_OnItemSelectedListener());
        ImageView img_netStatus = (ImageView) findViewById(R.id.img_netStatus);

        preReqs();                        // Check for pre-requisites
        isInternetAvailable();          // See if device has Internet

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);     // Enable 'Offline' Database
        loadStudentString();            // Force student load from Strings
        pfDatabase = FirebaseDatabase.getInstance();
        if (Pearadox.is_Network) {      // is Internet available?
//            pfStudent_DBReference = pfDatabase.getReference("students");        // Get list of Students
//            addStud_VE_Listener(pfStudent_DBReference);
            pfDevice_DBReference = pfDatabase.getReference("devices");          // List of Devices
            pfPitData_DBReference = pfDatabase.getReference("pit-data/" + Pearadox.FRC_Event); // Pit Scout Data
            pfMatchData_DBReference = pfDatabase.getReference("match-data/" + Pearadox.FRC_Event);    // Match Data
        } else {        // Use smaller list in 'Values/strings'
            pfDevice_DBReference = pfDatabase.getReference("devices");          // List of Devices
//            loadStudentString();
        }

        Spinner spinner_Student = (Spinner) findViewById(R.id.spinner_Student);
        Button btn_StoreData = (Button) findViewById(R.id.btn_StoreData);   // Listner defined in Layout XML
//        button_View.setOnClickListener(buttonStore_Click);

//        if (Pearadox.is_Network) {      // is Internet available?
            btn_StoreData.setVisibility(View.VISIBLE);
//        } else {        // Don't show button
//            btn_StoreData.setVisibility(View.GONE);
//        }

            toggleLogon = (ToggleButton) findViewById(R.id.toggleLogon);
            toggleLogon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioGroup radgrp_Scout = (RadioGroup) findViewById(R.id.radgrp_Scout);
                    Spinner spinner_Device = (Spinner) findViewById(R.id.spinner_Device);
                    Spinner spinner_Student = (Spinner) findViewById(R.id.spinner_Student);
                    if (spinner_Event.getSelectedItemPosition() == 0 || spinner_Device.getSelectedItemPosition() == 0 || spinner_Student.getSelectedItemPosition() == 0) {
                        Toast toast = Toast.makeText(getBaseContext(), "Select _ALL_ items (Event,Device,Student) before logging ON", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                        toggleLogon.setChecked(false);  // Set Toggle to Logged Off
                    } else {
                        if (toggleLogon.isChecked()) {      // See what state we are in
                            Log.w(TAG, "!!!  Logged IN  !!!");
                            logged_On = true;       // Logged ON
                            switch (devSelected) {          // Who you gonna call?!?
                                case "Scout Master":         // Scout Master
                                    Intent sm_intent = new Intent(MainActivity.this, ScoutMaster_Activity.class);
                                    startActivity(sm_intent);        // Start the Scout Master activity
                                    break;
                                case "Visualizer":          // Visualizer
                                    Intent viz_intent = new Intent(MainActivity.this, Visualizer_Activity.class);
                                    Bundle VZbundle = new Bundle();
                                    VZbundle.putString("dev", devSelected);             // Pass data
                                    VZbundle.putString("stud", studentSelected);        //  to activity
                                    viz_intent.putExtras(VZbundle);
                                    startActivity(viz_intent);                        // Start Visualizer
                                    break;
                                case ("Red-1"):             //#Red or Blue Scout
                                case ("Red-2"):             //#
                                case ("Red-3"):             //#
                                case ("Blue-1"):            //#
                                case ("Blue-2"):            //#
                                case ("Blue-3"):            //#####
                                    Log.w(TAG, "### Red/Blue Scout ### " + devSelected +"  '" + studentSelected + "'" );
                                    if (Scout_Match) {
                                        updateDev(true);        // Update firebase with LOGON
                                        Intent smast_intent = new Intent(MainActivity.this, MatchScoutActivity.class);
                                        Bundle SMbundle = new Bundle();
                                        SMbundle.putString("dev", devSelected);             // Pass data
                                        SMbundle.putString("stud", studentSelected);        //  to activity
                                        smast_intent.putExtras(SMbundle);
                                        startActivity(smast_intent);                        // Start Match Scout
                                    } else {
                                        if (Scout_Pit) {
                                            Intent spit_intent = new Intent(MainActivity.this, PitScoutActivity.class);
                                            Bundle SPbundle = new Bundle();
                                            SPbundle.putString("dev", devSelected);             // Pass data
                                            SPbundle.putString("stud", studentSelected);        //  to activity
                                            spit_intent.putExtras(SPbundle);
                                            startActivity(spit_intent);                        // Start Pit Scout
                                        } else {
                                            Log.e(TAG, "*** Error - Red/Blue Scout device selected but no TYPE indicator  ***");
                                        }
                                    }
                                    break;
                                default:                //
                                    Log.w(TAG, "DEV = NULL");
                            }

                        } else {
                            Log.w(TAG, "---  Logged OFF  ---");
                            logged_On = false;       // Logged OFF
                            if (Scout_Match) {
                                updateDev(false);        // Update firebase with LOGOFF
                            }
//                    devSelected = "";       // Null
//                    radgrp_Scout.setVisibility(View.GONE);    // Hide scout group
//                    radgrp_Scout.setEnabled(false);
//                    spinner_Device.setSelection(0);         //Reset to NO selection
//                    spinner_Student.setSelection(0);        //*
                        }
                    }
                }
            });

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }


// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void buttonStore_Click(View view) {
        Log.i(TAG, " buttonStore_Click   '" + Pearadox.FRC_Event + "'  \n ");
        if (Pearadox.FRC_Event.length() == 4) {
            txt_messageLine = (TextView) findViewById(R.id.txt_messageLine);
            txt_messageLine.setText("*** Saving Pit Data to Firebase ***");
            pfPitData_DBReference = pfDatabase.getReference("pit-data/" + Pearadox.FRC_Event); // Pit Scout Data
            pfMatchData_DBReference = pfDatabase.getReference("match-data/" + Pearadox.FRC_Event);    // Match Data

            File direct_pit = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/pit/" + Pearadox.FRC_Event);
            Log.w(TAG, ">>>>> Path" + direct_pit);
            if (direct_pit != null) {
                String[] filenames = direct_pit.list();
                num_PitObjs = 0;
                for (String tmpf : filenames) {
//                Log.w(TAG, " file " + tmpf);
                    team_num = tmpf.replaceFirst("[.][^.]+$", "");    // fileNameWithOutExt
//                Log.w(TAG, "*******  Team:" + team_num);
                    try {
                        Log.w(TAG, "   Dir:" + direct_pit + "/" + tmpf);
                        InputStream file = new FileInputStream(direct_pit + "/" + tmpf);
                        InputStream buffer = new BufferedInputStream(file);
                        ObjectInput input = new ObjectInputStream(buffer);
                        pitData Pit_Data = (pitData) input.readObject();
                        Log.w(TAG, "#### Obect '" + Pit_Data.getPit_team() + "'  " + Pit_Data.getPit_scout());
                        //      ToDo - Check to see if already in FB or Delete file from SD card

                        String keyID = team_num;
                        pfPitData_DBReference.child(keyID).setValue(Pit_Data);      // Store it to Firebase
                        num_PitObjs++;
                        File goner = new File(direct_pit + "/" + tmpf);
//                    boolean deleted = goner.delete();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }  // End for
                Log.w(TAG, " ####### Pit Data Objects = " + num_PitObjs);
            }  // End If
// ---------------------------------------
//      ToDo - Read all data from SD card and write to Firebase (Photos)
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            File direct_img = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/images/" + Pearadox.FRC_Event);
            Log.w(TAG, ">>>>> Path" + direct_img);
            if (direct_img != null) {
                String[] filenames = direct_img.list();
                num_Photos = 0;
                for (String tmpf : filenames) {
                    Log.w(TAG, " filename " + tmpf);
                    num_Photos++;
                    Uri file = Uri.fromFile(new File(direct_img + "/" + tmpf));
                    Log.w(TAG, " URI " + file);
//                StorageReference imgRef = storageRef.child("images/" + Pearadox.FRC_Event);
                    StorageReference imgRef = storageRef.child("images/");
                    Log.e(TAG, "***  HTTP error in Firebase putFile;  _NOT_ written to Firebase  *** ");

//                imgRef.putFile(file)
//                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                                //and displaying a success toast
//                                Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception exception) {
//
//                                //and displaying error message
//                                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        });
////                uploadTask = imgRef.putFile(file);
//                // Register observers to listen for when the download is done or if it fails
////                uploadTask.addOnFailureListener(new OnFailureListener() {
////                    @Override
////                    public void onFailure(@NonNull Exception exception) {
////                        // Handle unsuccessful uploads
////                    }
////                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
////                    @Override
////                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
////                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
////                    }
////                });
                }  // End for
                Log.w(TAG, " ####### Photos = " + num_Photos);
            }  // End If

// ---------------------------------------
//      ToDo - Read all Match data from SD card and write to Firebase
            txt_messageLine.setText("*** Saving Match Data to Firebase ***");

            File direct_match = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/match/" + Pearadox.FRC_Event);
            Log.w(TAG, ">>>>> Path" + direct_match);
            if (direct_match != null) {
                String[] filenames = direct_match.list();
                num_MatchObjs = 0;
                for (String tmpf : filenames) {
                    String mID_team = tmpf.replaceFirst("[.][^.]+$", "");    // fileNameWithOutExt
                    Log.w(TAG, "*******  Match ID & Team:" + mID_team);
                    try {
                        Log.w(TAG, "   Dir:" + direct_match + "/" + tmpf);
                        InputStream file = new FileInputStream(direct_match + "/" + tmpf);
                        InputStream buffer = new BufferedInputStream(file);
                        ObjectInput input = new ObjectInputStream(buffer);
                        matchData Match_Data = (matchData) input.readObject();
                        Log.w(TAG, "#### Obect '" + matchData.getSerialVersionUID());
                        //      ToDo - Check to see if already in FB or Delete file from SD card

                        String keyID = mID_team;
                        pfMatchData_DBReference.child(keyID).setValue(Match_Data);      // Store it to Firebase
                        num_MatchObjs++;
                        File goner = new File(direct_match + "/" + tmpf);
//                    boolean deleted = goner.delete();     // Delete
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }  // End for
                Log.w(TAG, " ####### Match Objects = " + num_MatchObjs);
            }  // End If

            Toast.makeText(getBaseContext(), "•••• There were " + num_PitObjs + " Pit data objects, " + num_Photos + " photos and " + num_MatchObjs + " Match objects copied from SD card to Firebase Cloud storage  ••••", Toast.LENGTH_LONG).show();
            txt_messageLine.setText(" ");
        } else {
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            Toast.makeText(getBaseContext(), "•••• Select an Event first  ••••", Toast.LENGTH_LONG).show();
        }
    }


    private void updateDev(boolean x) {     // x=true LOGON  x=false LOGOFF
        Log.i(TAG, "#### updateDev #### " + x);
        switch (devSelected) {
            case "Scout Master":         // Scout Master
                key = "0";
                break;
            case ("Red-1"):             //#Red or Blue Scout
                key = "1";
                break;
            case ("Red-2"):             //#
                key = "2";
                break;
            case ("Red-3"):             //#
                key = "3";
                break;
            case ("Blue-1"):            //#
                key = "4";
                break;
            case ("Blue-2"):            //#
                key = "5";
                break;
            case ("Blue-3"):            //#####
                key = "6";
                break;
            case "Visualizer":          // Visualizer
                key = "7";
                break;
            default:                //
                Log.w(TAG, "DEV = NULL" );
        }
//        if (Pearadox.is_Network) {      // Got Internet?
            if (x) {
                Log.w(TAG, "updating KEY = " + key);
                pfDevice_DBReference.child(key).child("stud_id").setValue(studentSelected);
                pfDevice_DBReference.child(key).child("phase").setValue("Auto");
              } else {
                Log.w(TAG, "Nulling KEY = " + key);
                pfDevice_DBReference.child(key).child("stud_id").setValue(" ");
                pfDevice_DBReference.child(key).child("phase").setValue(" ");
            }
//        }
    }


    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        private void addTeam_VE_Listener(final DatabaseReference pfTeam_DBReference) {
        pfTeam_DBReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i(TAG, "<<<< getFB_Data >>>> Teams");
                    Pearadox.team_List.clear();
                    Pearadox.numTeams = 0;
                    p_Firebase.teamsObj tmobj = new p_Firebase.teamsObj();
                    Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();   /*get the data children*/
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                    while (iterator.hasNext()) {
                        tmobj = iterator.next().getValue(p_Firebase.teamsObj.class);
                        Pearadox.team_List.add(tmobj);
                        Pearadox.numTeams ++;
                    }
                    Log.i(TAG, "***** Teams Loaded. # = " + Pearadox.numTeams + "  " + Pearadox.team_List.size());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                /*listener failed or was removed for security reasons*/
                }
        });
        }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void addStud_VE_Listener(final DatabaseReference pfStudent_DBReference) {
        pfStudent_DBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w(TAG, "******* Firebase retrieveStudents  *******");
                Pearadox.stud_Lst.clear();
                p_Firebase.students student_Obj = new p_Firebase.students();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();   /*get the data children*/
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    student_Obj = iterator.next().getValue(p_Firebase.students.class);
                    Pearadox.stud_Lst.add(student_Obj);
                }
                Log.w(TAG, "*****  # of students = " + Pearadox.stud_Lst.size());
                Pearadox.numStudents = Pearadox.stud_Lst.size() +1;
                Log.w(TAG, "@@@ array size = " + Pearadox.numStudents);
                Pearadox.student_List = new String[Pearadox.numStudents];  // Re-size for spinner
                Arrays.fill(Pearadox.student_List, null );
                Pearadox.student_List[0] = " ";       // make it so 1st Drop-Down entry is blank
                for(int i=0 ; i < Pearadox.stud_Lst.size() ; i++)
                {
                    student_Obj = Pearadox.stud_Lst.get(i);
//                    Log.w(TAG, "***** student = " + student_Obj.getName() + " " + i);
                    Pearadox.student_List[i + 1] = student_Obj.getName();
                }
                Spinner spinner_Student = (Spinner) findViewById(R.id.spinner_Student);
                ArrayAdapter adapter_Stud = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, Pearadox.student_List);
                adapter_Stud.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_Student.setAdapter(adapter_Stud);
                spinner_Student.setSelection(0, false);
                spinner_Student.setOnItemSelectedListener(new student_OnItemSelectedListener());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*listener failed or was removed for security reasons*/
                throw databaseError.toException();
            }
        });
    }
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void loadStudentString() {
        Log.w(TAG, "++++++ loadStudentString ++++++ " + Pearadox.is_Network);
        Spinner spinner_Student = (Spinner) findViewById(R.id.spinner_Student);
        String[] studs = getResources().getStringArray(R.array.student_array);
        adapter_StudStr = new ArrayAdapter<String>(this, R.layout.dev_list_layout, studs);
        adapter_StudStr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Student.setAdapter(adapter_StudStr);
        spinner_Student.setSelection(0, false);
        spinner_Student.setOnItemSelectedListener(new student_OnItemSelectedListener());
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
private void preReqs() {
    boolean isSdPresent;
    isSdPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    Log.w(TAG, "SD card: " + isSdPresent);
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
        File direct_iLub = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/images/txlu");
        if(!direct_iLub.exists())  {
            if(direct_iLub.mkdir())
            {
            } else {
                Log.w(TAG, " ****>>> ERROR creating directory  <<<<**** " + direct_iLub);

            }        //directory is created;
        }
        File direct_iHou = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/images/txho");
        if(!direct_iHou.exists())  {
            if(direct_iHou.mkdir())
            { }        //directory is created;
        }
        File direct_iWaco = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/images/txwa");
        if(!direct_iWaco.exists())  {
            if(direct_iWaco.mkdir())
            { }        //directory is created;
        }
//=================================================================
        File directMatch = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/match");
        if(!directMatch.exists())  {
            if(directMatch.mkdir())
            { }        //directory is created;
        }
        File direct_mLub = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/match/txlu");
        if(!direct_mLub.exists())  {
            if(direct_mLub.mkdir())
            { }        //directory is created;
        }
        File direct_mHou = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/match/txho");
        if(!direct_mHou.exists())  {
            if(direct_mHou.mkdir())
            { }        //directory is created;
        }
        File direct_mWaco = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/match/txwa");
        if(!direct_mWaco.exists())  {
            if(direct_mWaco.mkdir())
            { }        //directory is created;
        }
//=================================================================
        File directPit = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/pit");
        if(!directPit.exists())  {
            if(directPit.mkdir())
            { }        //directory is created;
        }
        File direct_pLub = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/pit/txlu");
        if(!direct_pLub.exists())  {
            if(direct_pLub.mkdir())
            { }        //directory is created;
        }
        File direct_pHou = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/pit/txho");
        if(!direct_pHou.exists())  {
            if(direct_pHou.mkdir())
            { }        //directory is created;
        }
        File direct_pWaco = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/pit/txwa");
        if(!direct_pWaco.exists())  {
            if(direct_pWaco.mkdir())
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
    // ToDo - Add broadcast receiver to tell when internet status changes
    public boolean isInternetAvailable() {
        Log.i(TAG, "<<<< Checking Internet Status >>>>");
        boolean status = false;
        ImageView img_netStatus = (ImageView) findViewById(R.id.img_netStatus);

        try {
            final ConnectivityManager connMgr = (ConnectivityManager)
                    this.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            Log.w(TAG, ">>>>> wifi = " + wifi);
            NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            Log.w(TAG, ">>>>> mobile = " + mobile);
            NetworkInfo bt = connMgr.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
            Log.w(TAG, ">>>>> Bluetooth = " + bt);

            // ========= Test it =========
            if( wifi.isAvailable() && wifi.isConnected()){
                Log.w(TAG, "$$$ Wi-Fi $$$ " + wifi.getExtraInfo());
//                Toast.makeText(this, "Wifi" , Toast.LENGTH_LONG).show();
                img_netStatus.setImageDrawable(getResources().getDrawable(R.drawable.wifi_bad));
                Pearadox.is_Network = true;
                status = true;
            }
            else if( mobile.isAvailable() ){
                Log.w(TAG, "*** Mobile ***");
//                Toast.makeText(this, "Mobile 3/4G " , Toast.LENGTH_LONG).show();
                img_netStatus.setImageDrawable(getResources().getDrawable(R.drawable.net_4g));
                Pearadox.is_Network = true;
                status = true;
            }
            else if( bt.isAvailable() ){
                Log.w(TAG, "### Bluetooth ###");
//                Toast.makeText(this, " Bluetooth " , Toast.LENGTH_LONG).show();
                img_netStatus.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth));
                Pearadox.is_Network = true;
                status = true;
            }
            else
            {
                Log.w(TAG, "@@@ No Network @@@");
//                Toast.makeText(this, "No Network " , Toast.LENGTH_LONG).show();
                img_netStatus.setImageDrawable(getResources().getDrawable(R.drawable.no_connection));
                Pearadox.is_Network = false;
            }
        } catch (Exception e) {
            Log.e(TAG, "*****  Error in Communication Manager  *****" );
            e.printStackTrace();
            Pearadox.is_Network = false;
            return false;
        }

        Log.w(TAG, "@@@@@ Network Status = " + status);
        return status;
    }
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
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
        if (id == R.id.action_about) {
            Toast toast = Toast.makeText(getBaseContext(), "Pearadox Scouting App ©2017  Ver." + Pearadox_Version, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    private class event_OnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            String ev = parent.getItemAtPosition(pos).toString();
            Pearadox.FRC_EventName = ev;
            Log.w(TAG, ">>>>> Event '" + Pearadox.FRC_EventName + "'  \n ");
            Spinner spinner_Device = (Spinner) findViewById(R.id.spinner_Device);
            Spinner spinner_Student = (Spinner) findViewById(R.id.spinner_Student);
            spinner_Device.setClickable(true);
            spinner_Student.setClickable(true);
            switch (ev) {
                case "Brazos Valley Regional":          // txwa
                    Pearadox.FRC_Event = "txwa";
                    break;
                case ("Lone Star Central Regional"):    // txho
                    Pearadox.FRC_Event = "txho";
                    break;
                case ("Hub City Regional"):             // txlu
                    Pearadox.FRC_Event = "txlu";
                    break;
                default:                // ?????
                    Toast.makeText(getBaseContext(), "Event code not recognized", Toast.LENGTH_LONG).show();
                    Pearadox.FRC_Event = "zzzz";
            }
            Log.w(TAG, " Event code = '" + Pearadox.FRC_Event + "'  \n ");
            pfTeam_DBReference = pfDatabase.getReference("teams/" + Pearadox.FRC_Event);   // Team data from Firebase D/B
            addTeam_VE_Listener(pfTeam_DBReference);        // Load Teams since we now know event
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }

    private class device_OnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            devSelected = parent.getItemAtPosition(pos).toString();
            Log.w(TAG, ">>>>> Device '" + devSelected + "'");
            Pearadox.FRC514_Device = devSelected;
            RadioGroup radgrp_Scout = (RadioGroup) findViewById(R.id.radgrp_Scout);
            switch (devSelected) {
                case "Scout Master": 	            // Scout Master
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
            }
            txt_messageLine.setText(" ");   // Clear Login message
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }

    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    private class student_OnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            studentSelected = parent.getItemAtPosition(pos).toString();
            Log.w(TAG, ">>>>> Student  '" + studentSelected + "'");
            Pearadox.Student_ID = studentSelected;
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }
    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    public void RadioClick_Scout(View view) {
        Log.w(TAG, "@@ RadioClick_Scout @@");
        radgrp_Scout = (RadioGroup) findViewById(R.id.radgrp_Scout);
        int selectedId = radgrp_Scout.getCheckedRadioButtonId();
        radioScoutTyp = (RadioButton) findViewById(selectedId);
        String value = radioScoutTyp.getText().toString();
        Log.w(TAG, "RadioScout - Button '" + value + "'");
        if (value.equals("Match Scout")) { 	    // Match?
            Log.w(TAG, "Match Scout");
            Scout_Match = true;
            Scout_Pit = false;
         } else {                               // Pit
            Log.w(TAG, "Pit Scout");
            Scout_Match = false;
            Scout_Pit = true;
        }
    }

//###################################################################
//###################################################################
//###################################################################
@Override
public void onResume() {
    super.onResume();
    Log.v(TAG, "onResume");
    txt_messageLine = (TextView) findViewById(R.id.txt_messageLine);
    txt_messageLine.setText("Log ON and prepare/wait for next match ");

    if (toggleLogon.isChecked()) {              // See what state we are in
        logged_On = false;              // Logged OFF
        if (Scout_Match) {
            updateDev(false);           // Update firebase with LOGOFF
        }
        toggleLogon.setChecked(false);  // Set Toggle to Logged Off
    }
}

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "OnDestroy key-> " + key);
        if (logged_On) {
//            Toast toast = Toast.makeText(getBaseContext(), "Don't forget to _ALWAYS_ log OFF before exiting", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//            toast.show();
            if (key != null) {
                pfDevice_DBReference.child(key).child("stud_id").setValue(" ");
            }
        }
    }

}

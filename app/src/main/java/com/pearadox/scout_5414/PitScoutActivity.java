package com.pearadox.scout_5414;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import static android.app.PendingIntent.getActivity;
import static android.view.View.VISIBLE;

public class PitScoutActivity extends AppCompatActivity {

    String TAG = "PitScout_Activity";      // This CLASS name
    TextView txt_EventName, txt_dev, txt_stud, txt_TeamName, txt_NumWheels, lbl_FuelEst;
    EditText editTxt_Team, txtEd_FuelCap, editText_Comments;
    ImageView imgScoutLogo, img_Photo;
    Spinner spinner_Team, spinner_Traction, spinner_Omni, spinner_Mecanum;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter_Trac, adapter_Omni, adapter_Mac ;
    RadioGroup radgrp_Dim;      RadioButton radio_Dim;
    CheckBox chkBox_Gear, chkBox_Fuel, chkBox_Shooter, chkBox_Vision, chkBox_Pneumatics, chkBox_FuelManip, chkBox_Climb;
    Button btn_Save;
    int REQUEST_IMAGE_CAPTURE = 2;
    public static String[] teams = new String[Pearadox.numTeams+1];  // Team list (array of just Team Names)
    public static String[] wheels = new String[]
            {"0","1","2","3","4","5","6", "7", "8"};

    String team_num, team_name, team_loc;
    p_Firebase.teamsObj team_inst = new p_Firebase.teamsObj(team_num, team_name, team_loc);
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfPitData_DBReference;
    FirebaseStorage storage;
    StorageReference storageRef;

    boolean dataSaved = false;      // Make sure they save before exiting
    // ===================  Data Elements for Pit Scout object ===================
    public String teamSelected = " ";           // Team #
    public boolean dim_Tall = false;            // Dimension
    public int totalWheels = 0;                 // Total # of wheels
    public int numTraction = 0;                 // Num. of Traction wheels
    public int numOmnis = 0;                     // Num. of Omni wheels
    public int numMecanums = 0;                  // Num. of Mecanum wheels
    public boolean gear_Collecter = false;      // presence of gear collector
    public boolean fuel_Container = false;      // presence of Storage bin
    public int storSize = 0;                    // estimate of # of balls
    public boolean shooter = false;             // presence of Shooter
    public boolean vision = false;              // presence of Vision Camera
    public boolean pneumatics = false;          // presence of Pneumatics
    public boolean fuelManip = false;           // presence of a way to pick up fuel from floor
    public boolean climb = false;               // presence of a Climbing mechanism
    /* */
    public String comments = "";                // Comment(s)
    public String scout = "";                   // Student who collected the data
// ===========================================================================
pitData Pit_Data = new pitData(teamSelected,dim_Tall,totalWheels,numTraction,numOmnis,numMecanums,gear_Collecter,fuel_Container,storSize,shooter,vision,pneumatics,fuelManip,climb,comments,scout);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pit_scout);
        Log.i(TAG, "<< Pit Scout >>");
        Bundle bundle = this.getIntent().getExtras();
        String param1 = bundle.getString("dev");
        String param2 = bundle.getString("stud");
        Log.d(TAG, param1 + " " + param2);      // ** DEBUG **
        scout = param2; // Scout of record

        txt_EventName = (TextView) findViewById(R.id.txt_EventName);
        txt_EventName.setText(Pearadox.FRC_EventName);          // Event Name
        pfDatabase = FirebaseDatabase.getInstance();
        pfPitData_DBReference = pfDatabase.getReference("pit-data/" + Pearadox.FRC_Event); // Pit Scout Data
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        ImageView img_Photo = (ImageView) findViewById(R.id.img_Photo);
        txt_dev = (TextView) findViewById(R.id.txt_Dev);
        txt_stud = (TextView) findViewById(R.id.txt_stud);
        txt_TeamName = (TextView) findViewById(R.id.txt_TeamName);
        txt_NumWheels = (TextView) findViewById(R.id.txt_NumWheels);
        txt_dev.setText(param1);
        txt_stud.setText(param2);
        txt_TeamName.setText(" ");
        Spinner spinner_Team = (Spinner) findViewById(R.id.spinner_Team);
        editTxt_Team = (EditText) findViewById(R.id.editTxt_Team);
        editTxt_Team.setFocusable(true);
        editTxt_Team.setFocusableInTouchMode(true);
        if (Pearadox.is_Network) {      // is Internet available?
            loadTeams();
            spinner_Team.setVisibility(View.VISIBLE);
            editTxt_Team.setVisibility(View.GONE);
            adapter = new ArrayAdapter<String>(this, R.layout.team_list_layout, teams);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Team.setAdapter(adapter);
            spinner_Team.setSelection(0, false);
            spinner_Team.setOnItemSelectedListener(new team_OnItemSelectedListener());
        } else {        // Have the user type in Team #
            editTxt_Team.setText("");
            editTxt_Team.setVisibility(View.VISIBLE);
            editTxt_Team.setEnabled(true);
            editTxt_Team.requestFocus();        // Don't let EditText mess up layout!!
            spinner_Team.setVisibility(View.GONE);
            editTxt_Team.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Log.d(TAG, " editTxt_Team listener; Team = " + editTxt_Team.getText());
                    teamSelected = (String.valueOf(editTxt_Team.getText()));
                    chkForPhoto(teamSelected);      // see if photo already exists
                    return true;
                }
                return false;
                }
            });
        }

        Spinner spinner_Traction = (Spinner) findViewById(R.id.spinner_Traction);
        ArrayAdapter adapter_Trac = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wheels);
        adapter_Trac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Traction.setAdapter(adapter_Trac);
        spinner_Traction.setSelection(0, false);
        spinner_Traction.setOnItemSelectedListener(new PitScoutActivity.Traction_OnItemSelectedListener());
        Spinner spinner_Omni = (Spinner) findViewById(R.id.spinner_Omni);
        ArrayAdapter adapter_Omni = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wheels);
        adapter_Omni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Omni.setAdapter(adapter_Trac);
        spinner_Omni.setSelection(0, false);
        spinner_Omni.setOnItemSelectedListener(new PitScoutActivity.Omni_OnItemSelectedListener());
        Spinner spinner_Mecanum = (Spinner) findViewById(R.id.spinner_Mecanum);
        ArrayAdapter adapter_Mac = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wheels);
        adapter_Mac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Mecanum.setAdapter(adapter_Mac);
        spinner_Mecanum.setSelection(0, false);
        spinner_Mecanum.setOnItemSelectedListener(new PitScoutActivity.Mecanum_OnItemSelectedListener());
        chkBox_Gear = (CheckBox) findViewById(R.id.chkBox_Gear);
        chkBox_Shooter = (CheckBox) findViewById(R.id.chkBox_Shooter);
        chkBox_Vision = (CheckBox) findViewById(R.id.chkBox_Vision);
        chkBox_Pneumatics = (CheckBox) findViewById(R.id.chkBox_Pneumatics);
        chkBox_Fuel = (CheckBox) findViewById(R.id.chkBox_Fuel);
        lbl_FuelEst = (TextView) findViewById(R.id.lbl_FuelEst);
        txtEd_FuelCap = (EditText) findViewById(R.id.txtEd_FuelCap);
        chkBox_FuelManip = (CheckBox) findViewById(R.id.chkBox_FuelManip);
        chkBox_Climb = (CheckBox) findViewById(R.id.chkBox_Climb);
        editText_Comments = (EditText) findViewById(R.id.editText_Comments);
        editText_Comments.setClickable(true);

        chkBox_Gear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.i(TAG, "chkBox_Gear Listener");
                if (buttonView.isChecked()) {
                    Log.i(TAG,"Gear is checked.");
                    gear_Collecter = true;
                } else {
                    Log.i(TAG,"Gear is unchecked.");
                    gear_Collecter = false;
                }
            }
        });
        chkBox_Fuel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
               Log.i(TAG, "chkBox_Fuel Listener");
               if (buttonView.isChecked()) {
                   Log.i(TAG,"Fuel is checked.");
                   fuel_Container = true;
                   lbl_FuelEst.setVisibility(VISIBLE);
                   txtEd_FuelCap.setVisibility(VISIBLE);
                   txtEd_FuelCap.requestFocus();
               } else {
                   Log.i(TAG,"Fuel is unchecked.");
                   fuel_Container = false;
                   lbl_FuelEst.setVisibility(View.GONE);
                   txtEd_FuelCap.setVisibility(View.GONE);
               }
           }
        });
        chkBox_Shooter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
               Log.i(TAG, "chkBox_Fuel Listener");
               if (buttonView.isChecked()) {
                   Log.i(TAG,"shooter is checked.");
                   shooter = true;
               } else {
                   Log.i(TAG,"shooter is unchecked.");
                   shooter = false;
               }
           }
        });
        chkBox_Vision.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
               Log.i(TAG, "chkBox_Vision Listener");
               if (buttonView.isChecked()) {
                   Log.i(TAG,"Vision is checked.");
                   vision = true;
               } else {
                   Log.i(TAG,"Vision is unchecked.");
                   vision = false;
               }
           }
       });
        chkBox_Pneumatics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.i(TAG, "chkBox_Pneumatics Listener");
                if (buttonView.isChecked()) {
                    Log.i(TAG,"Pneumatics is checked.");
                    pneumatics = true;
                } else {
                    Log.i(TAG,"Pneumatics is unchecked.");
                    pneumatics = false;
                }
            }
        });
        chkBox_FuelManip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.i(TAG, "chkBox_FuelManip Listener");
                if (buttonView.isChecked()) {
                    Log.i(TAG,"FuelManip is checked.");
                    fuelManip = true;
                } else {
                    Log.i(TAG,"FuelManip is unchecked.");
                    fuelManip = false;
                }
            }
        });
        chkBox_Climb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.i(TAG, "chkBox_Climb Listener");
                if (buttonView.isChecked()) {
                    Log.i(TAG,"Climb is checked.");
                    climb = true;
                } else {
                    Log.i(TAG,"Climb is unchecked.");
                    climb = false;
                }
            }
        });

        editText_Comments.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.i(TAG, "******  onTextChanged TextWatcher  ******" + s);
                comments = String.valueOf(s);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Log.i(TAG, "******  beforeTextChanged TextWatcher  ******");
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "******  onTextChanged TextWatcher  ******" + s );
                comments = String.valueOf(s);
            }
        });

        txtEd_FuelCap.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(TAG, "******  txtEd_FuelCap listener  ******");

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Log.d(TAG, " txtEd_FuelCap listener"  + txtEd_FuelCap.getText());
                    storSize = Integer.valueOf(String.valueOf(txtEd_FuelCap.getText()));
                    return true;
                }
                return false;
            }
        });

/* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
        btn_Save = (Button) findViewById(R.id.btn_Save);
        btn_Save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "Save Button Listener");
                Spinner spinner_Team = (Spinner) findViewById(R.id.spinner_Team);
                storePitData();           // Put all the Pit data collected in Pit object
                dataSaved = true;
                if (Pearadox.is_Network) {      // is Internet available?
                    spinner_Team.setSelection(0);       //Reset to NO selection
                    txt_TeamName.setText(" ");
                }
                finish();       // Exit  <<<<<<<<
            }
        });
    }


    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_photo:
                onLaunchCamera();       // Start Camera
            default:
                break;
        }
        return false;
    }

    private void onLaunchCamera() {
        Log.i(TAG, "►►►►►  LaunchCamera  ◄◄◄◄◄");
        if (teamSelected.length() < 3) {        /// Make sure a Team is selected 1st
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            Toast toast = Toast.makeText(getBaseContext(), "*** Select a TEAM first before taking photo ***", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        } else {
            String picname = "robot_" + teamSelected.trim() + ".png";
            File dirPhotos = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/pit/" + Pearadox.FRC_Event + "/");
            Log.d(TAG, "SD card Path = " + dirPhotos);
            dirPhotos = new File(dirPhotos, picname);
            Log.d(TAG, "File = " + dirPhotos);
            Uri outputFileUri = Uri.fromFile(dirPhotos);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            Log.d(TAG, "Photo taken");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.i(TAG, "*****  onActivityResult " + requestCode);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == PitScoutActivity.RESULT_OK) {
//            Log.d(TAG, "requestCode = '" + requestCode + "'");
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            Log.d(TAG, "*** data '" + data + "'");
            ImageView img_Photo = (ImageView) findViewById(R.id.img_Photo);
            img_Photo.setImageBitmap(imageBitmap);
            if (Pearadox.is_Network) {      // is Internet available?
                encodeBitmapAndSaveToFirebase(imageBitmap);
            }
        }
    }
    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        Log.i(TAG, "$$$$$  encodeBitmapAndSaveToFirebase  $$$$$");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String picname = "robot_" + teamSelected.trim() + ".png";
        Log.d(TAG, "Photo = '" + picname + "'");
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);     // ByteArrayOutputStream
        byte[] data = baos.toByteArray();
        //                  gs://paradox-2017.appspot.com/images/txZZ/

//        StorageReference storageReference = storage.getReferenceFromUrl("gs://paradox-2017.appspot.com").child(picname);

//        UploadTask uploadTask = storageRef.putBytes(data);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//            }
//        });
    }

    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    public class team_OnItemSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            Log.i(TAG, "*****  team_OnItemSelectedListener " + pos);
            teamSelected = parent.getItemAtPosition(pos).toString();
            Log.d(TAG, ">>>>>  '" + teamSelected + "'");
            txt_TeamName = (TextView) findViewById(R.id.txt_TeamName);
            findTeam(teamSelected);
            txt_TeamName.setText(team_inst.getTeam_name());
            // ToDo - see if photo already exists
            chkForPhoto(teamSelected);
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }

// =============================================================================
    private void chkForPhoto(String team) {
        Log.i(TAG, "*****  chkForPhoto - team = " + team);

        // First check SD card
        String filename = "robot_" + team.trim() + ".png";
        File directPhotos = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/images/" + Pearadox.FRC_Event + "/" + filename);
        Log.d(TAG, "SD card Path = " + directPhotos);
        if(directPhotos.exists())  {
            Log.d(TAG, "@@@ PHOTO EXISTS LOCALLY @@@ ");
            Bitmap imageBitmap = BitmapFactory.decodeFile(directPhotos.getAbsolutePath());
            img_Photo.setImageBitmap(imageBitmap);

        } else {
            if (Pearadox.is_Network) {      // is Internet available?
                Log.d(TAG, "### Checking on Firebase Images ### ");
                //ToDo - check on Firebase (if internet is up)
            }
        }


    }

    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    public class Traction_OnItemSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            String num = " ";
            num = parent.getItemAtPosition(pos).toString();
            numTraction = Integer.parseInt(num);
            Log.d(TAG, ">>>>>  '" + numTraction + "'");
            updateNumWhls();
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }
    public class Omni_OnItemSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            String num = " ";
            num = parent.getItemAtPosition(pos).toString();
            numOmnis = Integer.parseInt(num);
            Log.d(TAG, ">>>>>  '" + numOmnis + "'");
            updateNumWhls();
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }
    public class Mecanum_OnItemSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            String num = " ";
            num = parent.getItemAtPosition(pos).toString();
            numMecanums = Integer.parseInt(num);
            Log.d(TAG, ">>>>>  '" + numMecanums + "'");
            updateNumWhls();
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }

    private void updateNumWhls() {
        Log.i(TAG, "######  updateNumWhls ###### T-O-M" + numTraction + numOmnis + numMecanums);
        int x = numTraction + numOmnis + numMecanums;
        txt_NumWheels.setText(String.valueOf(x));      // Total # of wheels
        totalWheels = x;
        if (x < 4){
            Toast.makeText(getBaseContext(), "Robot should have at least 4 wheels", Toast.LENGTH_LONG).show();
        }
    }

/* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    private void findTeam(String tnum) {
        Log.i(TAG, "$$$$$  findTeam " + tnum);
        boolean found = false;
        for (int i = 0; i < Pearadox.numTeams; i++) {        // check each team entry
            if (Pearadox.team_List.get(i).getTeam_num().equals(tnum)) {
                team_inst = Pearadox.team_List.get(i);
//                Log.d(TAG, "===  Team " + team_inst.getTeam_num() + " " + team_inst.getTeam_name() + " " + team_inst.getTeam_loc());
                found = true;
                break;  // found it!
            }
        }  // end For
        if (!found) {
            Log.e(TAG, "****** ERROR - Team _NOT_ found!! = " + tnum);
            txt_TeamName.setText(" ");
        }
    }

    private void loadTeams() {
        Log.i(TAG, "$$$$$  loadTeams $$$$$");
        int tNum = 0;
        teams[0] = " ";     // Make the 1st one BLANK for dropdown
        tNum ++;
        for (int i = 0; i < Pearadox.numTeams; i++) {        // get each team entry
            team_inst = Pearadox.team_List.get(i);
            teams[i+1] = team_inst.getTeam_num();
            tNum ++;
        }  // end For
        Log.d(TAG, "# of teams = " + tNum);

    }
    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    public void RadioClick_Dim(View view) {
        Log.i(TAG, "@@ RadioClick_Dim @@");
        radgrp_Dim = (RadioGroup) findViewById(R.id.radgrp_Dim);
        int selectedId = radgrp_Dim.getCheckedRadioButtonId();
        radio_Dim = (RadioButton) findViewById(selectedId);
        String value = radio_Dim.getText().toString();
        if (teamSelected.length() < 4) {        /// Make sure a Team is selected 1st
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            Toast toast = Toast.makeText(getBaseContext(), "*** Select a TEAM first before entering data ***", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            radio_Dim.setChecked(false);
        } else {
            if (value.equals("Short")) {           // Short?
                Log.d(TAG, "Short");
                dim_Tall = false;
            } else {                               // Tall
                Log.d(TAG, "Tall");
                dim_Tall = true;
            }
            Log.d(TAG, "RadioDim - Tall = '" + dim_Tall + "'");
        }
    }
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void storePitData() {
        Log.i(TAG, ">>>>  storePitData  <<<< " + teamSelected );

        Pit_Data.setPit_team(teamSelected);
        Pit_Data.setPit_tall(dim_Tall);
        Pit_Data.setPit_totWheels(totalWheels);
        Pit_Data.setPit_numTrac(numTraction);
        Pit_Data.setPit_numOmni(numOmnis);
        Pit_Data.setPit_numMecanum(numMecanums);
        Pit_Data.setPit_gear_Collect(gear_Collecter);
        Pit_Data.setPit_fuel_Container(fuel_Container);
        Pit_Data.setPit_storSize(storSize);
        Pit_Data.setPit_shooter(shooter);
        Pit_Data.setPit_vision(vision);
        Pit_Data.setPit_pneumatics(pneumatics);
        Pit_Data.setPit_fuelManip(fuelManip);
        Pit_Data.setPit_climb(climb);
         /* */
        Pit_Data.setPit_comment(comments);
        Pit_Data.setPit_scout(scout);
// -----------------------------------------------
        saveDatatoSDcard();             //Save locally
        if (Pearadox.is_Network) {      // is Internet available?
            String keyID = teamSelected;
            pfPitData_DBReference.child(keyID).setValue(Pit_Data);
        }
    }
    private void saveDatatoSDcard() {
        Log.i(TAG, "@@@@  saveDatatoSDcard  @@@@");
        String filename = Pit_Data.getPit_team().trim() + ".dat";
        ObjectOutput out = null;
        File directMatch = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/pit/" + Pearadox.FRC_Event + "/" + filename);
        Log.d(TAG, "SD card Path = " + directMatch);
        if(directMatch.exists())  {
            // Todo - Replace TOAST with Dialog Box  - "Do you really ..."
            Toast toast = Toast.makeText(getBaseContext(), "Data for " + filename + " already exists!!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }

        try {
            out = new ObjectOutputStream(new FileOutputStream(directMatch));
            out.writeObject(Pit_Data);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

// ################################################################
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 200);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);

            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit without saving?  All data will be lost!")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        dataSaved = false;
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

//###################################################################
//###################################################################
//###################################################################
    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume  \n");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
        if (!dataSaved) {
            Log.d(TAG, "** Data _NOT_ saved!!  **");
            // Handled with Dialog box
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "OnDestroy");
        // ToDo - ??????
    }

}

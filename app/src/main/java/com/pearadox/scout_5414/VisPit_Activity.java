package com.pearadox.scout_5414;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaActionSound;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

public class VisPit_Activity extends AppCompatActivity {

    String TAG = "VisPit_Activity";        // This CLASS name
    String tnum = "", tname = "", imgURL = "";
    TextView txt_team, txt_teamName;
    TextView txt_Ht, txt_TotWheels, txt_NumTrac, txt_NumOmni, txt_NumMecanum, txt_LiftCap, txt_Scout, txt_Comments;
    TextView txt_DriveMotor, txt_ProgLang, txt_Speed;
    ImageView imgView_Robot, imgView_LARGE;                // Robot image
    CheckBox chkBox_Vision, chkBox_Pneumatics, chkBox_Climb, chkBox_Lift, chkBox_Hook, chkBox_Ramp;
    CheckBox chkBox_Arms, chkBox_ArmIntake, chkBox_ArmPress, chkBox_OffFloor, chkBox_Belt, chkBox_Box, chkBox_Other;
    CheckBox chkBox_Switch, chkBox_SwitchMulti, chkBox_Scale, chkBox_ScaleMulti;
    RadioGroup radgrp_Deliver;      RadioButton radio_Launch, radio_Place;
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfPitData_DBReference;


    // ===================  Data Elements for Pit Scout object ===================
    public String teamSelected = " ";           // Team #
    public int tall = 0;                        // Height (inches)
    public int totalWheels = 0;                 // Total # of wheels
    public int numTraction = 0;                 // Num. of Traction wheels
    public int numOmnis = 0;                    // Num. of Omni wheels
    public int numMecanums = 0;                 // Num. of Mecanum wheels
    public boolean vision = false;              // presence of Vision Camera
    public boolean pneumatics = false;          // presence of Pneumatics
    public boolean cubeManip = false;           // presence of a way to pick up cube from floor
    public boolean climb = false;               // presence of a Climbing mechanism
    public boolean canLift = false;             // Ability to lift other robots
    public int numLifted = 0;                   // Num. of robots can lift (1-2)
    public boolean liftRamp = false;            // lift type Ramp
    public boolean liftHook = false;            // lift type Hook
    public int speed = 0;                       // Speed (Ft. per Sec)
    public String motor;                        // Type of Motor
    public String lang;                         // Programming  Language
    public boolean autoSwitch = false;          // Can do Switch in Autonomous
    public boolean switchMulti = false;         // Multiple Switch in Autonomous
    public boolean autoScale = false;           // Can do Scale in Autonomous
    public boolean scaleMulti = false;         // Multiple Scale in Autonomous
                                                //==== cube Mechanism
    public boolean cubeArm = false;             // presence of a Cube arm
    public boolean armIntake = false;           // ++ presence of a Cube intake device      \  Only if
    public boolean armSqueeze = false;          // ++ presence of a Cube Squeeze mechanism  /   Arm
    public boolean cubeBox = false;             // presence of a Cube box
    public boolean cubeBelt = false;            // presence of a Cube Conveyer Belt
    public boolean cubeOhtr = false;            // Other ?
    //==== cube Delivery
    public boolean delLaunch = false;           // Launch
    public boolean delPlace = false;            // Placement
    /* */
    /* */
    public String comments = "";                // Comment(s)
    public String scout = "";                   // Student who collected the data
    public String pit_photoURL;                     // URL of the robot photo in Firebase

    // ===========================================================================
    pitData Pit_Data = new pitData(teamSelected, tall, totalWheels, numTraction, numOmnis, numMecanums, vision, pneumatics, cubeManip, climb, canLift, numLifted, liftRamp, liftHook, speed, motor,  lang, autoSwitch, switchMulti, autoScale, scaleMulti, cubeArm, armIntake, armSqueeze, cubeBox, cubeBelt, cubeOhtr, delLaunch, delPlace, comments, scout, pit_photoURL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis_pit);
        Log.i(TAG, "@@@@@@@@  VisPit_Activity started  @@@@@@@@");
        Bundle bundle = this.getIntent().getExtras();
        tnum = bundle.getString("team");
        tname = bundle.getString("name");
        imgURL = bundle.getString("url");
        Log.w(TAG, "\n >>>>>>>> " + tnum + " " + tname + " " + imgURL);      // ** DEBUG **

        pfDatabase = FirebaseDatabase.getInstance();
        pfPitData_DBReference = pfDatabase.getReference("pit-data/" + Pearadox.FRC_Event); // Pit Scout Data
//        pfPitData_DBReference = pfDatabase.getReference("pit-data/");       // Pit Scout Data
        getTeam_Pit(tnum);
        txt_team = (TextView) findViewById(R.id.txt_team);
        txt_teamName = (TextView) findViewById(R.id.txt_teamName);
        ImageView imgView_Robot = (ImageView) findViewById(R.id.imgView_Robot);
        txt_team.setText(tnum);
        txt_teamName.setText(tname);
        if (imgURL.length() > 1) {
            Picasso.with(this).load(imgURL).into(imgView_Robot);
        } else {
            imgView_Robot.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        }
        txt_Ht = (TextView) findViewById(R.id.txt_Ht);
        txt_Scout = (TextView) findViewById(R.id.txt_Scout);
        txt_Comments = (TextView) findViewById(R.id.txt_Comments);
        txt_Ht.setText(" ");
        txt_Scout.setText(" ");
        txt_Comments.setText("***   No Pit data for this team   ***");

        // *****  If image selected, view full screen   *****
//        imgView_Robot.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            ImageView imgView_LARGE = (ImageView) findViewById(R.id.imgView_LARGE);
//            ImageView imgView_Robot = (ImageView) findViewById(R.id.imgView_Robot);
//            imgView_Robot.setVisibility(View.INVISIBLE);
//            imgView_LARGE.setVisibility(View.VISIBLE);
//        }
//    });
//        imgView_LARGE.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ImageView imgView_LARGE = (ImageView) findViewById(R.id.imgView_LARGE);
//                ImageView imgView_Robot = (ImageView) findViewById(R.id.imgView_Robot);
//                imgView_Robot.setVisibility(View.VISIBLE);
//                imgView_LARGE.setVisibility(View.INVISIBLE);
//            }
//        });
    }
// ===============================================================================
    private void getTeam_Pit(String team) {
        Log.i(TAG, "$$$$$  getTeam_Pit  $$$$$  " + team);

        String child = "pit_team";
        String key = team;      // Removed .trim()       GLF 3/31/2017
        Log.w(TAG, "   Q U E R Y  " + child + "  '" + key + "' \n ");
        Query query = pfPitData_DBReference.orderByChild(child).equalTo(key);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.w(TAG, "%%%%%%%%%%%%  ChildAdded");
                System.out.println(dataSnapshot.getValue());
                System.out.println("\n \n ");
                pitData Pit_Data = dataSnapshot.getValue(pitData.class);
                    System.out.println("Team: " + Pit_Data.getPit_team());
                    System.out.println("Comment: " + Pit_Data.getPit_comment());
                    System.out.println("\n \n ");
                txt_Ht = (TextView) findViewById(R.id.txt_Ht);
                txt_TotWheels = (TextView) findViewById(R.id.txt_TotWheels);
                txt_NumTrac = (TextView) findViewById(R.id.txt_NumTrac);
                txt_NumOmni = (TextView) findViewById(R.id.txt_NumOmni);
                txt_NumMecanum = (TextView) findViewById(R.id.txt_NumMecanum);
                chkBox_Climb = (CheckBox) findViewById(R.id.chkBox_Climb);
                chkBox_Vision = (CheckBox) findViewById(R.id.chkBox_Vision);
                chkBox_Pneumatics = (CheckBox) findViewById(R.id.chkBox_Pneumatics);
                chkBox_Lift = (CheckBox) findViewById(R.id.chkBox_Lift);
                txt_LiftCap = (TextView) findViewById(R.id.txt_LiftCap);
                chkBox_Ramp = (CheckBox) findViewById(R.id.chkBox_Ramp);
                chkBox_Hook = (CheckBox) findViewById(R.id.chkBox_Hook);
                chkBox_Arms = (CheckBox) findViewById(R.id.chkBox_Arms);
                chkBox_ArmIntake = (CheckBox) findViewById(R.id.chkBox_ArmIntake);
                chkBox_ArmPress = (CheckBox) findViewById(R.id.chkBox_ArmPress);
                chkBox_OffFloor = (CheckBox) findViewById(R.id.chkBox_OffFloor);
                chkBox_Belt = (CheckBox) findViewById(R.id.chkBox_Belt);
                chkBox_Box = (CheckBox) findViewById(R.id.chkBox_Box);
                chkBox_Other = (CheckBox) findViewById(R.id.chkBox_Other);
                radgrp_Deliver = (RadioGroup) findViewById(R.id.radgrp_Deliver);
                radio_Place = (RadioButton) findViewById(R.id.radio_Place);
                radio_Launch = (RadioButton) findViewById(R.id.radio_Launch);
                txt_DriveMotor = (TextView) findViewById(R.id.txt_DriveMotor);
                txt_ProgLang = (TextView) findViewById(R.id.txt_ProgLang);
                txt_Speed = (TextView) findViewById(R.id.txt_Speed);
                chkBox_Switch = (CheckBox) findViewById(R.id.chkBox_Switch);
                chkBox_SwitchMulti = (CheckBox) findViewById(R.id.chkBox_SwitchMulti);
                chkBox_Scale = (CheckBox) findViewById(R.id.chkBox_Scale);
                chkBox_ScaleMulti = (CheckBox) findViewById(R.id.chkBox_ScaleMulti);

                txt_Scout = (TextView) findViewById(R.id.txt_Scout);
                txt_Comments = (TextView) findViewById(R.id.txt_Comments);

                // ****  Start loading data  ****
                txt_Ht.setText(String.valueOf(Pit_Data.getPit_tall()));
                txt_TotWheels.setText(String.valueOf(Pit_Data.getPit_totWheels()));
                txt_NumTrac.setText(String.valueOf(Pit_Data.getPit_numTrac()));
                txt_NumOmni.setText(String.valueOf(Pit_Data.getPit_numOmni()));
                txt_NumMecanum.setText(String.valueOf(Pit_Data.getPit_numMecanum()));

                chkBox_Climb.setChecked(Pit_Data.isPit_climb());
                chkBox_Vision.setChecked(Pit_Data.isPit_vision());
                chkBox_Pneumatics.setChecked(Pit_Data.isPit_pneumatics());
                chkBox_Lift.setChecked(Pit_Data.isPit_canLift());
                if (Pit_Data.isPit_canLift()) {
                    txt_LiftCap.setVisibility(View.VISIBLE);
                    txt_LiftCap.setText(String.valueOf(Pit_Data.getPit_numLifted()));
                    chkBox_Hook.setVisibility(View.VISIBLE);
                    chkBox_Hook.setChecked(Pit_Data.isPit_liftHook());
                    chkBox_Ramp.setVisibility(View.VISIBLE);
                    chkBox_Ramp.setChecked(Pit_Data.isPit_liftRamp());
                } else {
                    txt_LiftCap.setVisibility(View.INVISIBLE);
                    chkBox_Ramp.setVisibility(View.INVISIBLE);
                    chkBox_Hook.setVisibility(View.INVISIBLE);
                }
                chkBox_Arms.setChecked(Pit_Data.isPit_cubeArm());
                if (Pit_Data.isPit_cubeArm()) {
                    chkBox_ArmIntake.setVisibility(View.VISIBLE);
                    chkBox_ArmIntake.setChecked(Pit_Data.isPit_armIntake());
                    chkBox_ArmPress.setVisibility(View.VISIBLE);
                    chkBox_ArmPress.setChecked(Pit_Data.isPit_armSqueeze());
                    chkBox_OffFloor.setVisibility(View.VISIBLE);
                    chkBox_OffFloor.setChecked(Pit_Data.isPit_cubeManip());
                } else {
                    chkBox_ArmIntake.setVisibility(View.INVISIBLE);
                    chkBox_ArmPress.setVisibility(View.INVISIBLE);
                    chkBox_OffFloor.setVisibility(View.INVISIBLE);
                }
                chkBox_Belt.setChecked(Pit_Data.isPit_cubeBelt());
                chkBox_Box.setChecked(Pit_Data.isPit_cubeBox());
                chkBox_Other.setChecked(Pit_Data.isPit_cubeOhtr());
                Log.w(TAG, "Radio - Launch= " + Pit_Data.isPit_delLaunch());
                if (Pit_Data.isPit_delLaunch()) {
                    radio_Launch.setChecked(true);
                    radio_Place.setChecked(false);      // ?
                } else {
                    radio_Place.setChecked(true);
                    radio_Launch.setChecked(false);     // ?
                }
                txt_Speed.setText(String.valueOf(Pit_Data.getPit_speed()));
                txt_DriveMotor.setText(Pit_Data.getPit_motor());
                txt_ProgLang.setText(Pit_Data.getPit_lang());
                chkBox_Switch.setChecked(Pit_Data.isPit_autoSwitch());
                chkBox_SwitchMulti.setChecked(Pit_Data.isPit_switchMulti());
                chkBox_Scale.setChecked(Pit_Data.isPit_autoScale());
                chkBox_ScaleMulti.setChecked(Pit_Data.isPit_scaleMulti());

                // Finally ...
                txt_Scout.setText(Pit_Data.getPit_scout());
                txt_Comments.setText(Pit_Data.getPit_comment());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.w(TAG, "%%%  ChildChanged");
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.w(TAG, "%%%  ChildRemoved");
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.w(TAG, "%%%  ChildMoved");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "%%%  DatabaseError");
            }
        });
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_viz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.e(TAG, "@@@  Options  @@@ " );
        Log.w(TAG, " \n  \n");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_screen) {
            String filNam = Pearadox.FRC_Event.toUpperCase() + "-VizPit"  + "_" + tnum.trim() + ".JPG";
            Log.w(TAG, "File='" + filNam + "'");
            try {
                File imageFile = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/" + filNam);
                View v1 = getWindow().getDecorView().getRootView();             // **\
                v1.setDrawingCacheEnabled(true);                                // ** \Capture screen
                Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());      // ** /  as bitmap
                v1.setDrawingCacheEnabled(false);                               // **/
                FileOutputStream fos = new FileOutputStream(imageFile);
                int quality = 100;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
                fos.flush();
                fos.close();
                bitmap.recycle();           //release memory
                MediaActionSound sound = new MediaActionSound();
                sound.play(MediaActionSound.SHUTTER_CLICK);
                Toast toast = Toast.makeText(getBaseContext(), "☢☢  Screen captured in Download/FRC5414  ☢☢", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            } catch (Throwable e) {
                // Several error may come out with file handling or DOM
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    //###################################################################
//###################################################################
//###################################################################
    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
   }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "OnDestroy ");
    }

}

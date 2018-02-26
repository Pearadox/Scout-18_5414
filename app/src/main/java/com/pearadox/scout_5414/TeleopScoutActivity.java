package com.pearadox.scout_5414;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

/**
 * Created by mlm.02000 on 2/5/2017.
 */

public class TeleopScoutActivity extends Activity {

    String TAG = "TeleopScoutActivity";      // This CLASS name
    TextView txt_dev, txt_stud, txt_match, txt_CubeSwitchNUM, txt_CubeSwitchAttNUM, txt_tnum, txt_CubeScaleNUM, txt_CubeScaleAttNUM;
    TextView  txt_OtherSwitchNUM, txt_OtherSwitchAttNUM, txt_CubeZoneNUM, txt_CubePlatformNUM, txt_OthrSwtchNUM, txt_PortalNUM, txt_ExchangeNUM, txt_RandomNUM;
    /* Final */       private Button button_GoToFinalActivity;
    /* Switch */      private Button btn_CubeSwitchM, btn_CubeSwitchP, btn_CubeSwitchAttP, btn_CubeSwitchAttM;
    /* Scale */       private Button btn_CubeScaleP,  btn_CubeScaleM,  btn_CubeScaleAttP,  btn_CubeScaleAttM;
    /* Opp Switch */  private Button btn_OtherSwitchM, btn_OtherSwitchP, btn_OtherSwitchAttP, btn_OtherSwitchAttM;
    /*Retrieved Cbs*/ private Button btn_CubeZoneM, btn_CubeZoneP, btn_CubePlatformP, btn_CubePlatformM, btn_OthrSwtchP, btn_OthrSwtchM, btn_PortalP, btn_PortalM, btn_ExchangeP, btn_ExchangeM, btn_RandomP, btn_RandomM;
    CheckBox   chk_climbsuccessful, chk_climbattempted, chkBox_PU_Cubes_floor, chkBox_Platform, chk_LiftedBy;
    EditText   editText_TeleComments;
    RadioGroup radgrp_Deliver, radgrp_Boss, radgrp_Lifted;      RadioButton radio_Deliver, radio_Climb, radio_Lift, radio_One, radio_Two, radio_Zero, radio_Rung, radio_Side;

    private FirebaseDatabase  pfDatabase;
    private DatabaseReference pfTeam_DBReference;
    private DatabaseReference pfMatch_DBReference;
    private DatabaseReference pfDevice_DBReference;
    private DatabaseReference pfCur_Match_DBReference;
    String key = null;
    String tn  = " ";

    // ===================  TeleOps Elements for Match Scout Data object ===================
    public int     cubeSwitch_placed  = 0;     // # Gears placed
    public int     cubeSwitch_attempt = 0;     // # Gears attempted
    public int     cube_scale         = 0;     // # cubes placed on Switch during Tele
    public int     scale_attempt      = 0;     // # cubes attempted on Switch during Tele
    public int     their_switch       = 0;     // # cubes placed on _THEIR_Switch during Tele
    public int     their_attempt      = 0;     // # cubes attempted on _THEIR_Switch during Tele
    public int     cube_exchange      = 0;     // # cubes placed in Exchange during Tele
    public int     cube_portal        = 0;     // # cubes retrieved from Portal during Tele
    public int     cube_pwrzone       = 0;     // # cubes retrieved from Power Zone during Tele
    public int     cube_floor         = 0;     // # cubes retrieved from our Floor or Platform Zone during Tele
    public int     their_floor        = 0;     // # cubes retrieved from their Floor or Platform Zone during Tele
    public int    random_floor        = 0;     // # cubes retrieved from random places during Tele
    public boolean cube_pickup        = false; // Did they pickup gears off the ground?
    public boolean on_platform        = false; // Finished on platform
    public boolean delPlace           = false; // Cube Delivery = Place    \ Radio
    public boolean delLaunch          = false; // Cube Delivery = Launch   /  Button
    public boolean climb_attempt      = false; // Did they ATTEMPT climb?
    public boolean climb_success      = false; // Was climb successful?
    public boolean grab_rung          = false; // == Grabbed rung to climb   \ Radio
    public boolean grab_side          = false; // == Grabbed side to climb   /  Button
    public boolean lift_zero          = false; // Lifted no other robot     \
    public boolean lift_one           = false; // Lifted one other robot       Radio
    public boolean lift_two           = false; // Lifted two other robots   /   Button
    public boolean got_lift           = false; // Got Lifted by another robot
    /* */
    public String  teleComment        = " ";   // Tele Comment
    // ===========================================================================
    matchData match_cycle = new matchData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "<< Teleop Scout >>");
        setContentView(R.layout.activity_teleop_scout);
        Bundle bundle = this.getIntent().getExtras();
        tn = bundle.getString("tnum");
        Log.w(TAG, tn);      // ** DEBUG **

        txt_tnum = (TextView) findViewById(R.id.txt_tnum);
        txt_tnum.setText(tn);



        btn_CubeZoneP             = (Button) findViewById(R.id.btn_CubeZoneP);
        btn_CubeZoneM             = (Button) findViewById(R.id.btn_CubeZoneM);
        txt_CubeZoneNUM           = (TextView) findViewById(R.id.txt_CubeZoneNUM);
        txt_CubeSwitchAttNUM      = (TextView) findViewById(R.id.txt_CubeSwitchAttNUM);
        btn_CubeSwitchAttP        = (Button)   findViewById(R.id.btn_CubeSwitchAttP);
        btn_CubeSwitchAttM        = (Button)   findViewById(R.id.btn_CubeSwitchAttM);
        btn_CubeScaleAttP         = (Button)   findViewById(R.id.btn_CubeScaleAttP);
        btn_CubeScaleAttM         = (Button)   findViewById(R.id.btn_CubeScaleAttM);
        btn_OtherSwitchM          = (Button)   findViewById(R.id.btn_OtherSwitchM);
        btn_OtherSwitchP          = (Button)   findViewById(R.id.btn_OtherSwitchP);
        btn_OtherSwitchAttM       = (Button)   findViewById(R.id.btn_OtherSwitchAttM);
        btn_OtherSwitchAttP       = (Button)   findViewById(R.id.btn_OtherSwitchAttP);
        txt_OtherSwitchNUM        = (TextView) findViewById(R.id.txt_OtherSwitchNUM);
        txt_OtherSwitchAttNUM     = (TextView) findViewById(R.id.txt_OtherSwitchAttNUM);
        btn_CubePlatformP         = (Button) findViewById(R.id.btn_CubePlatformP);
        btn_CubePlatformM         = (Button) findViewById(R.id.btn_CubePlatformM);
        txt_CubePlatformNUM       = (TextView) findViewById(R.id.txt_CubePlatformNUM);
        btn_OthrSwtchP            = (Button) findViewById(R.id.btn_OthrSwtchP);
        btn_OthrSwtchM            = (Button) findViewById(R.id.btn_OthrSwtchM);
        txt_OthrSwtchNUM          = (TextView) findViewById(R.id.txt_OthrSwtchNUM);
        chk_climbsuccessful       = (CheckBox) findViewById(R.id.chk_climbsuccess);
        chk_climbattempted        = (CheckBox) findViewById(R.id.chk_climbattempt);
        chk_LiftedBy              = (CheckBox) findViewById(R.id.chk_LiftedBy);
        chkBox_Platform           = (CheckBox) findViewById(R.id.chkBox_Platform);
        chkBox_PU_Cubes_floor     = (CheckBox) findViewById(R.id.chkBox_PU_Cubes_floor);
        editText_TeleComments     = (EditText) findViewById(R.id.editText_teleComments);
        button_GoToFinalActivity  = (Button)   findViewById(R.id.button_GoToFinalActivity);
        btn_CubeSwitchM           = (Button)   findViewById(R.id.btn_CubeSwitchM);
        btn_CubeSwitchP           = (Button)   findViewById(R.id.btn_CubeSwitchP);
        btn_CubeScaleP            = (Button)   findViewById(R.id.btn_CubeScaleP);
        btn_CubeScaleM            = (Button)   findViewById(R.id.btn_CubeScaleM);
        txt_CubeScaleAttNUM       = (TextView) findViewById(R.id.txt_CubeScaleAttNUM);
        txt_CubeScaleNUM          = (TextView) findViewById(R.id.txt_CubeScaleNUM);
        txt_CubeSwitchNUM         = (TextView) findViewById(R.id.txt_CubeSwitchNUM);
        btn_PortalP               = (Button) findViewById(R.id.btn_PortalP);
        btn_PortalM               = (Button) findViewById(R.id.btn_PortalM);
        txt_PortalNUM             = (TextView) findViewById(R.id.txt_PortalNUM);
        btn_ExchangeP             = (Button) findViewById(R.id.btn_ExchangeP);
        btn_ExchangeM             = (Button) findViewById(R.id.btn_ExchangeM);
        txt_ExchangeNUM           = (TextView) findViewById(R.id.txt_ExchangeNUM);
        btn_RandomP               = (Button) findViewById(R.id.btn_RandomP);
        btn_RandomM               = (Button) findViewById(R.id.btn_RandomM);
        txt_RandomNUM             = (TextView) findViewById(R.id.txt_RandomNUM);

        txt_CubeSwitchAttNUM.setText(Integer.toString(cubeSwitch_attempt));
        txt_CubeSwitchNUM   .setText(Integer.toString(cubeSwitch_placed));
        txt_CubeScaleAttNUM .setText(Integer.toString(scale_attempt));
        txt_CubeScaleNUM    .setText(Integer.toString(cube_scale));

        pfDatabase                = FirebaseDatabase.getInstance();
//        pfTeam_DBReference        = pfDatabase      .getReference("teams");         // Tteam data from Firebase D/B
//        pfStudent_DBReference     = pfDatabase      .getReference("students");      // List of Students
//        pfMatch_DBReference       = pfDatabase      .getReference("matches");       // List of matches
//        pfCur_Match_DBReference   = pfDatabase      .getReference("current-match"); // _THE_ current Match
        pfDevice_DBReference      = pfDatabase      .getReference("devices");    // List of Devices
        radio_One = (RadioButton) findViewById(R.id.radio_One);
        //radio_One.setEnabled(false);        // Don't let them choose if CLIMB not selected
        radio_Two = (RadioButton) findViewById(R.id.radio_Two);
        //radio_Two.setEnabled(false);        // Don't let them choose if CLIMB not selected
        radio_Zero = (RadioButton) findViewById(R.id.radio_Zero);
        //radio_Zero.setEnabled(false);        // Don't let them choose if CLIMB not selected
        radgrp_Boss = (RadioGroup) findViewById(R.id.radgrp_Boss);
        for (int i = 0; i < radgrp_Boss.getChildCount(); i++) {  // Same for Rung/Side
            radgrp_Boss.getChildAt(i).setEnabled(false);
        }


        btn_CubeSwitchAttP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cubeSwitch_attempt < 66) {
                    cubeSwitch_attempt++;
                }
                Log.w(TAG, "Cubes = " + cubeSwitch_attempt);      // ** DEBUG **
                txt_CubeSwitchAttNUM.setText(Integer.toString(cubeSwitch_attempt));    // Perform action on click
            }
        });
        btn_CubeSwitchAttM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cubeSwitch_attempt >= 1 && cubeSwitch_attempt > cubeSwitch_placed) {
                    cubeSwitch_attempt--;
                }
                Log.w(TAG, "Cubes = " + cubeSwitch_attempt);      // ** DEBUG **
                txt_CubeSwitchAttNUM.setText(Integer.toString(cubeSwitch_attempt));    // Perform action on click
            }
        });

        btn_CubeSwitchP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cubeSwitch_placed < 66) {
                    cubeSwitch_placed++;
                    cubeSwitch_attempt++;
                }
                Log.w(TAG, "Cubes = " + cubeSwitch_placed);      // ** DEBUG **
                Log.w(TAG, "Cubes Attempted = " + cubeSwitch_attempt);
                txt_CubeSwitchNUM.setText(Integer.toString(cubeSwitch_placed));    // Perform action on click
                txt_CubeSwitchAttNUM.setText(Integer.toString(cubeSwitch_attempt));
            }
        });
        btn_CubeSwitchM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cubeSwitch_placed >= 1 && cubeSwitch_attempt >= cubeSwitch_placed) {
                    cubeSwitch_placed--;
                    cubeSwitch_attempt--;
                }
                Log.w(TAG, "Cubes = " + cubeSwitch_placed);      // ** DEBUG **
                Log.w(TAG, "Cubes Attempted = " + cubeSwitch_attempt);
                txt_CubeSwitchNUM.setText(Integer.toString(cubeSwitch_placed));    // Perform action on click
                txt_CubeSwitchAttNUM.setText(Integer.toString(cubeSwitch_attempt));
            }
        });

        btn_CubeScaleAttP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (scale_attempt < 66) {
                    scale_attempt++;
                }
                Log.w(TAG, "Cubes = " + scale_attempt);      // ** DEBUG **
                txt_CubeScaleAttNUM.setText(Integer.toString(scale_attempt));    // Perform action on click
            }
        });
        btn_CubeScaleAttM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (scale_attempt >= 1 && scale_attempt > cube_scale) {
                    scale_attempt--;
                }
                Log.w(TAG, "Cubes = " + scale_attempt);      // ** DEBUG **
                txt_CubeScaleAttNUM.setText(Integer.toString(scale_attempt));    // Perform action on click
            }
        });

        btn_CubeScaleP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cube_scale < 66) {
                    cube_scale++;
                    scale_attempt++;
                }
                Log.w(TAG, "Cubes Scale = " + cube_scale);      // ** DEBUG **
                Log.w(TAG, "Cubes Scale Attempted = " + cube_scale);
                txt_CubeScaleNUM.setText(Integer.toString(cube_scale));    // Perform action on click
                txt_CubeScaleAttNUM.setText(Integer.toString(scale_attempt));
            }
        });
        btn_CubeScaleM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cube_scale >= 1 && scale_attempt >= cube_scale) {
                    cube_scale--;
                    scale_attempt--;
                }
                Log.w(TAG, "Cubes Scale = " + cube_scale);      // ** DEBUG **
                Log.w(TAG, "Cubes Scale Attempted = " + cube_scale);
                txt_CubeScaleNUM.setText(Integer.toString(cube_scale));    // Perform action on click
                txt_CubeScaleAttNUM.setText(Integer.toString(scale_attempt));
            }
        });


        btn_OtherSwitchAttP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (their_attempt < 66) {
                    their_attempt++;
                }
                Log.w(TAG, "Cubes = " + their_attempt);      // ** DEBUG **
                txt_OtherSwitchAttNUM.setText(Integer.toString(their_attempt));    // Perform action on click
            }
        });
        btn_OtherSwitchAttM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (their_attempt >= 1 && their_attempt > their_switch) {
                    their_attempt--;
                }
                Log.w(TAG, "Cubes = " + their_attempt);      // ** DEBUG **
                txt_OtherSwitchAttNUM.setText(Integer.toString(their_attempt));    // Perform action on click
            }
        });

        btn_OtherSwitchP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (their_switch < 6) {        // Max of 6 cubes
                    their_switch++;
                    their_attempt++;
                }
                Log.w(TAG, "+Other Cubes = " + their_switch);      // ** DEBUG **
                Log.w(TAG, "+Other Cubes Attempted = " + their_switch);
                txt_OtherSwitchNUM.setText(Integer.toString(their_switch));    // Perform action on click
                txt_OtherSwitchAttNUM.setText(Integer.toString(their_attempt));
            }
        });
        btn_OtherSwitchM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (their_switch >= 1 && their_attempt >= their_switch) {
                    their_switch--;
                    their_attempt--;
                }
                Log.w(TAG, "-Other Cubes = " + their_switch);      // ** DEBUG **
                Log.w(TAG, "-Other Cubes Attempted = " + their_switch);
                txt_OtherSwitchNUM.setText(Integer.toString(their_switch));    // Perform action on click
                txt_OtherSwitchAttNUM.setText(Integer.toString(their_attempt));
            }
        });


        btn_CubePlatformP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cube_floor < 6) {       // Max of 6 in Power Zone
                    cube_floor++;
                }
                Log.w(TAG, "+Platform Cubes = " + cube_floor);      // ** DEBUG **
                txt_CubePlatformNUM.setText(Integer.toString(cube_floor));    // Perform action on click
            }
        });
        btn_CubePlatformM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cube_floor >= 1) {
                    cube_floor--;
                }
                Log.w(TAG, "-Platform Cubes = " + cube_floor);      // ** DEBUG **
                txt_CubePlatformNUM.setText(Integer.toString(cube_floor));    // Perform action on click
            }
        });

        btn_OthrSwtchP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (their_floor < 6) {       // Max of 6 in Power Zone
                    their_floor++;
                }
                Log.w(TAG, "Their Cubes = " + their_floor);          // ** DEBUG **
                txt_OthrSwtchNUM.setText(Integer.toString(their_floor));    // Perform action on click
            }
        });
        btn_OthrSwtchM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (their_floor >= 1) {
                    their_floor--;
                }
                Log.w(TAG, "Their Cubes = " + their_floor);      // ** DEBUG **
                txt_OthrSwtchNUM.setText(Integer.toString(their_floor));    // Perform action on click
            }
        });

        btn_RandomP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (random_floor < 6) {       // Max of 6 in Power Zone
                    random_floor++;
                }
                Log.w(TAG, "Random Cubes = " + random_floor);          // ** DEBUG **
                txt_RandomNUM.setText(Integer.toString(random_floor));    // Perform action on click
            }
        });
        btn_RandomM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (random_floor >= 1) {
                    random_floor--;
                }
                Log.w(TAG, "Random Cubes = " + random_floor);      // ** DEBUG **
                txt_RandomNUM.setText(Integer.toString(random_floor));    // Perform action on click
            }
        });

        btn_CubeZoneP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cube_pwrzone < 10) {        // Max of 10 in Power Zone
                    cube_pwrzone++;
                }
                Log.w(TAG, "+Power Zone Cubes = " + cube_pwrzone);      // ** DEBUG **
                txt_CubeZoneNUM.setText(Integer.toString(cube_pwrzone));    // Perform action on click
            }
        });
        btn_CubeZoneM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cube_pwrzone >= 1) {
                    cube_pwrzone--;
                }
                Log.w(TAG, "-Power Zone Cubes = " + cube_pwrzone);      // ** DEBUG **
                txt_CubeZoneNUM.setText(Integer.toString(cube_pwrzone));    // Perform action on click
            }
        });

        btn_PortalP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cube_portal < 7) {        // Max of 7 from Portal
                    cube_portal++;
                }
                Log.w(TAG, "+Portal Cubes = " + cube_portal);      // ** DEBUG **
                txt_PortalNUM.setText(Integer.toString(cube_portal));    // Perform action on click
            }
        });
        btn_PortalM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cube_portal >= 1) {
                    cube_portal--;
                }
                Log.w(TAG, "-Portal Cubes = " + cube_portal);      // ** DEBUG **
                txt_PortalNUM.setText(Integer.toString(cube_portal));    // Perform action on click
            }
        });


        btn_ExchangeP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cube_exchange < 9) {        // Max of 9 into Exchange
                    cube_exchange++;
                }
                Log.w(TAG, "+Exchange Cubes = " + cube_exchange);      // ** DEBUG **
                txt_ExchangeNUM.setText(Integer.toString(cube_exchange));    // Perform action on click
            }
        });
        btn_ExchangeM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cube_exchange >= 1) {
                    cube_exchange--;
                }
                Log.w(TAG, "-Exchange Cubes = " + cube_exchange);      // ** DEBUG **
                txt_ExchangeNUM.setText(Integer.toString(cube_exchange));    // Perform action on click
            }
        });


        button_GoToFinalActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "Clicked Final");
                radio_Rung = (RadioButton) findViewById(R.id.radio_Rung);
                radio_Side = (RadioButton) findViewById(R.id.radio_Side);
                Log.i(TAG, "Climb=" + climb_success + "  Radio=" + radio_Rung.isChecked() + radio_Side.isChecked());
                if (!climb_success || (climb_success && (radio_Rung.isChecked() || radio_Side.isChecked()))) {     // Gotta pick one!
                    updateDev("Final");           // Update 'Phase' for stoplight indicator in ScoutM aster
                    storeTeleData();                    // Put all the TeleOps data collected in Match object

                    Intent smast_intent = new Intent(TeleopScoutActivity.this, FinalActivity.class);
                    Bundle SMbundle = new Bundle();
                    SMbundle.putString("tnum", tn);
                    smast_intent.putExtras(SMbundle);
                    startActivity(smast_intent);
                } else {
                    Log.e(TAG, "ERROR - did not select lift type");
                    Toast.makeText(getBaseContext(), "** Climb _MUST_ have 'Rung' or 'Side' selected **", Toast.LENGTH_LONG).show();
                    final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                    tg.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);
                }
            }
        });

        chkBox_PU_Cubes_floor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.i(TAG, "chkBox_PU_Cubes_floor Listener");
            if (buttonView.isChecked()) {
                Log.w(TAG,"PU_Cubes is checked.");
                cube_pickup = true;

            } else {  //not checked
                Log.i(TAG,"PU_Cubes is unchecked.");
                cube_pickup = false;
            }
            }
        });


        chk_climbattempted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
               Log.i(TAG, "chk_climbfailed Listener");
                if (buttonView.isChecked()) {
                    //checked
                    Log.i(TAG,"TextBox is checked.");
                    climb_attempt = true;
                    chkBox_Platform.setChecked(true);

                } else
                {
                    //not checked
                    Log.i(TAG,"TextBox is unchecked.");
                    climb_attempt = false;
                    chk_climbsuccessful.setChecked(false);

                }
            }
        });
        chk_climbsuccessful.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.i(TAG, "chk_climbsuccssful Listener");
                radio_One = (RadioButton) findViewById(R.id.radio_One);
                radio_Two = (RadioButton) findViewById(R.id.radio_Two);
                radgrp_Boss = (RadioGroup) findViewById(R.id.radgrp_Boss);
                if (buttonView.isChecked()) {
                    //checked
                    Log.i(TAG, "TextBox is checked.");
                    climb_success = true;
                    chk_climbattempted.setChecked(true);
                    chkBox_Platform.setChecked(true);       // Have to be on platform to climb!
                    //radio_One.setEnabled(true);     // Can't lift if you don't climb!!
                    //radio_Two.setEnabled(true);     // Can't lift if you don't climb!!
                    for (int i = 0; i < radgrp_Boss.getChildCount(); i++) {  // Same for Rung/Side
                        radgrp_Boss.getChildAt(i).setEnabled(true);
                    }
                    chk_LiftedBy.setChecked(false);         // Can't have both
                }else {
                        //not checked
                        Log.i(TAG, "TextBox is unchecked.");
                        climb_success = false;
                        //chk_climbattempted.setChecked(false);
                        //chkBox_Platform.setChecked(false);
                        //radio_One.setEnabled(false);        // Don't let them choose if CLIMB not selected
                        //radio_Two.setEnabled(false);        // Don't let them choose if CLIMB not selected
                        for (int i = 0; i < radgrp_Boss.getChildCount(); i++) {  // Same for Rung/Side
                            radgrp_Boss.getChildAt(i).setEnabled(false);
                            radgrp_Boss.getChildAt(i).setActivated(false);
                            radgrp_Boss.clearCheck();
                        }
                }

            }

        });

        chk_LiftedBy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.i(TAG, "chk_LiftedBy Listener");
                if (chk_LiftedBy.isChecked()) {
                    //checked
                    Log.i(TAG,"LiftedBy is checked.");
                    got_lift = true;
                    chk_climbsuccessful.setChecked(false);       // Don't count as climb!!
                    chkBox_Platform.setChecked(true);       // Have to be on platform to get lifted!
                }
                else {
                    //not checked
                    Log.i(TAG,"LiftedBy is unchecked.");
                    got_lift = false;
                    //chkBox_Platform.setChecked(false);       // Have to be on platform to get lifted!
                }
            }
        });

        chkBox_Platform.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.i(TAG, "chkBox_Platform Listener");
                if (chkBox_Platform.isChecked()) {
                    //checked
                    Log.i(TAG,"TextBox is checked.");
                    on_platform = true;
                }
                else {
                    //not checked
                    Log.i(TAG,"TextBox is unchecked.");
                    on_platform = false;
                }
            }
        });


            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(radio_One.isChecked())
                    {
                        lift_one = true;
                        chkBox_Platform.setChecked(true);
                        Log.w(TAG, "radio_One is " + lift_one);

                    }
                    else
                    {
                        lift_one=false;
                        Log.w(TAG, "radio_One is " + lift_one);

                    }

                }
            };

            radio_One.setOnClickListener(listener);

            listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (radio_Two.isChecked()) {
                        lift_two = true;
                        chkBox_Platform.setChecked(true);
                        Log.w(TAG, "radio_Two is " + lift_two);

                    } else {
                        lift_two = false;
                        Log.w(TAG, "radio_Two is " + lift_two);


                    }

                }
            };

            radio_Two.setOnClickListener(listener);

            listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (radio_Zero.isChecked()) {
                        lift_zero = true;
                        lift_one = false;
                        lift_two = false;
                        //chkBox_Platform.setChecked(true);
                        Log.w(TAG, "radio_Zero is " + lift_zero);

                    } else {
                        lift_zero = false;
                        Log.w(TAG, "radio_Zero is " + lift_zero);


                    }

                }
            };

            radio_Zero.setOnClickListener(listener);



        editText_TeleComments.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "******  onTextChanged TextWatcher  ******" + s);
                teleComment = String.valueOf(s);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, "******  beforeTextChanged TextWatcher  ******");
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "******  onTextChanged TextWatcher  ******" + s );
                teleComment = String.valueOf(s);
            }
        });
    }

    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    public void RadioClick_Del(View view) {
        Log.w(TAG, "@@ RadioClick_Del @@");
        radgrp_Deliver = (RadioGroup) findViewById(R.id.radgrp_Deliver);
        int selectedId = radgrp_Deliver.getCheckedRadioButtonId();
//        Log.w(TAG, "*** Selected=" + selectedId);
        radio_Deliver = (RadioButton) findViewById(selectedId);
        String value = radio_Deliver.getText().toString();
//        radio_Deliver.setChecked(false);
        if (value.equals("Place")) {           // Place?
            Log.w(TAG, "Place");
            delPlace = true;
            delLaunch = false;
        } else {                               // Launch
            Log.w(TAG, "Launch");
            delLaunch = true;
            delPlace = false;
        }
        Log.w(TAG, "RadioDel - Launch = '" + delLaunch + "'  Place = '" + delPlace + "'");
    }

    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    public void RadioClick_Boss(View view) {
        Log.w(TAG, "@@ RadioClick_Boss @@");
        radgrp_Boss = (RadioGroup) findViewById(R.id.radgrp_Boss);
        int selectedId = radgrp_Boss.getCheckedRadioButtonId();
//        Log.w(TAG, "*** Selected=" + selectedId);
        radio_Climb = (RadioButton) findViewById(selectedId);
        String value = radio_Climb.getText().toString();
//        radio_Climb.setChecked(false);
        if (value.equals("Rung")) {           // Rung?
            Log.w(TAG, "Rung");
            grab_rung = true;
            grab_side = false;
        } else {                               // Side
            Log.w(TAG, "Side");
            grab_side = true;
            grab_rung = false;
        }
    }

    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    public void RadioClick_Lifted(View view) {
        Log.w(TAG, "@@ RadioClick_Lifted @@");
        radgrp_Lifted = (RadioGroup) findViewById(R.id.radgrp_Lifted);
        int selectedId = radgrp_Lifted.getCheckedRadioButtonId();
//        Log.w(TAG, "*** Selected=" + selectedId);
        radio_Lift = (RadioButton) findViewById(selectedId);
        String value = radio_Lift.getText().toString();
//        radio_Lift.setChecked(false);
        if (value.equals("One")) {           // One?
            Log.w(TAG, "One");
            lift_one = true;
            lift_two = false;
        } else {                               // Two
            Log.w(TAG, "Two");
            lift_two = true;
            lift_one = false;
        }
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void storeTeleData() {
        Log.w(TAG, ">>>>  storeTeleData  <<<<");
        Pearadox.Match_Data.setTele_cube_switch(cubeSwitch_placed);
        Pearadox.Match_Data.setTele_switch_attempt(cubeSwitch_attempt);
        Pearadox.Match_Data.setTele_cube_scale(cube_scale);
        Pearadox.Match_Data.setTele_scale_attempt(scale_attempt);
        Pearadox.Match_Data.setTele_their_switch(their_switch);
        Pearadox.Match_Data.setTele_their_attempt(their_attempt);
        Pearadox.Match_Data.setTele_cube_exchange(cube_exchange);
        Pearadox.Match_Data.setTele_cube_portal(cube_portal);
        Pearadox.Match_Data.setTele_cube_pwrzone(cube_pwrzone);
        Pearadox.Match_Data.setTele_cube_floor(cube_floor);
        Pearadox.Match_Data.setTele_their_floor(their_floor);
        Pearadox.Match_Data.setTele_random_floor(random_floor);
        Pearadox.Match_Data.setTele_cube_pickup(cube_pickup);
        Pearadox.Match_Data.setTele_on_platform(on_platform);
        Pearadox.Match_Data.setTele_placed_cube(delPlace);
        Pearadox.Match_Data.setTele_launched_cube(delLaunch);
        Pearadox.Match_Data.setTele_climb_attempt(climb_attempt);
        Pearadox.Match_Data.setTele_climb_success(climb_success);
        Pearadox.Match_Data.setTele_grab_rung(grab_rung);
        Pearadox.Match_Data.setTele_grab_side(grab_side);
        Pearadox.Match_Data.setTele_lift_one(lift_one);
        Pearadox.Match_Data.setTele_lift_two(lift_two);
        Pearadox.Match_Data.setTele_got_lift(got_lift);
        // **
        Pearadox.Match_Data.setTele_comment(teleComment);
    }
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    private void updateDev(String phase) {     //
        Log.i(TAG, "#### updateDev #### " + phase);
        switch (Pearadox.FRC514_Device) {
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
        pfDevice_DBReference.child(key).child("phase").setValue(phase);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit without saving? All of your data will be lost!")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
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
        Log.v(TAG, "onResume");

        if (Pearadox.MatchData_Saved) {
            // ToDo - Clear all data back to original settings
            Log.w(TAG, "#### Data was saved in Final #### ");
            //Toast.makeText(getBaseContext(), "Data was saved in Final - probably should Exit", Toast.LENGTH_LONG).show();

            finish();       // Exit

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
        Log.v(TAG, "OnDestroy");
    }

}
/*  This is for committing! */
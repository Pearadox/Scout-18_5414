package com.pearadox.scout_5414;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by mlm.02000 on 2/5/2017.
 */

public class TeleopScoutActivity extends Activity {


    String TAG = "TeleopScoutActivity";      // This CLASS name
    TextView txt_dev, txt_stud, txt_match, txt_MyTeam, lbl_GearNUMT, lbl_GearsAttempted, txt_seekBarHGvalue, txt_seekBarLGvalue,txt_seekBarHGvalue1, txt_seekBarLGvalue1, txt_seekBarHGvalue2, txt_seekBarLGvalue2, txt_seekBarHGvalue3, txt_seekBarLGvalue3, lbl_shooting_cycles, txt_tnum;
    private Button button_GoToFinalActivity,button_GearPlacedT, button_GearPlacedTPlus, button_GearAttemptedP, button_GearAttemptedM, button_shooting_cyclesPlus, button_shooting_cyclesMinus;
    CheckBox chk_climbsuccessful, chk_climbattempted, chk_touchpad, chk_touchpadpts, chkBox_highGoal, chkBox_lowGoal, chkBox_PU_Gears_floor;
    CheckBox chkBox_highGoal1, chkBox_lowGoal1, chkBox_highGoal2, chkBox_lowGoal2, chkBox_highGoal3, chkBox_lowGoal3;
    SeekBar seekBar_HighGoal_Teleop, seekBar_LowGoal_Teleop, seekBar_HighGoal_Teleop1, seekBar_LowGoal_Teleop1, seekBar_HighGoal_Teleop2, seekBar_LowGoal_Teleop2, seekBar_HighGoal_Teleop3, seekBar_LowGoal_Teleop3;
    EditText editText_TeleComments;
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfTeam_DBReference;
    private DatabaseReference pfMatch_DBReference;
    private DatabaseReference pfDevice_DBReference;
    private DatabaseReference pfCur_Match_DBReference;
    String key = null;
    String tn = " ";

    // ===================  TeleOps Elements for Match Scout Data object ===================
    public int tele_gears_placed = 0;                   // # Gears placed
    public int tele_gears_attempt = 0;                  // # Gears attempted
    public boolean tele_hg = false;                             // Did they shoot at High Goal?
    public int tele_hg_percent = 0;                     // What percentage HG made?
    public boolean tele_lg = false;                             // Did they shoot at Low Goal?
    public int tele_lg_percent = 0;                     // What percentage LG made?
    public int tele_cycles = 0;                             // # cycles of shooting Upper Goal
    public boolean tele_touch_act = false;                      // Did they activate Touchpad?
//    public boolean tele_touch_pts = false;                      // Did they get Touchpad points?
    public boolean tele_climb_attempt = false;                  // Did they ATTEMPT climb?
    public boolean tele_climb_success = false;                  // Was climb successful?
    public boolean tele_gear_pickup = false;                    //Did they pickup gears off the ground?
    /* */
    public String teleComment = " ";    // Tele Comment
    // ===========================================================================

    public int tele_hg_percent0 = 0;
    public int tele_lg_percent0 = 0;
    public int tele_hg_percent1 = 0;
    public int tele_lg_percent1 = 0;
    public int tele_hg_percent2 = 0;
    public int tele_lg_percent2 = 0;
    public int tele_hg_percent3 = 0;
    public int tele_lg_percent3 = 0;

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

        txt_seekBarHGvalue = (TextView) findViewById(R.id.txt_seekBarHGvalue);
        txt_seekBarLGvalue = (TextView) findViewById(R.id.txt_seekBarLGvalue);
        txt_seekBarHGvalue1 = (TextView) findViewById(R.id.txt_seekBarHGvalue1);
        txt_seekBarLGvalue1 = (TextView) findViewById(R.id.txt_seekBarLGvalue1);
        txt_seekBarHGvalue2 = (TextView) findViewById(R.id.txt_seekBarHGvalue2);
        txt_seekBarLGvalue2 = (TextView) findViewById(R.id.txt_seekBarLGvalue2);
        txt_seekBarHGvalue3 = (TextView) findViewById(R.id.txt_seekBarHGvalue3);
        txt_seekBarLGvalue3 = (TextView) findViewById(R.id.txt_seekBarLGvalue3);

        seekBar_HighGoal_Teleop = (SeekBar) findViewById(R.id.seekBar_HighGoal_Teleop);
        seekBar_LowGoal_Teleop = (SeekBar) findViewById(R.id.seekBar_LowGoal_Teleop);
        seekBar_HighGoal_Teleop1 = (SeekBar) findViewById(R.id.seekBar_HighGoal_Teleop1);
        seekBar_LowGoal_Teleop1 = (SeekBar) findViewById(R.id.seekBar_LowGoal_Teleop1);
        seekBar_HighGoal_Teleop2 = (SeekBar) findViewById(R.id.seekBar_HighGoal_Teleop2);
        seekBar_LowGoal_Teleop2 = (SeekBar) findViewById(R.id.seekBar_LowGoal_Teleop2);
        seekBar_HighGoal_Teleop3 = (SeekBar) findViewById(R.id.seekBar_HighGoal_Teleop3);
        seekBar_LowGoal_Teleop3 = (SeekBar) findViewById(R.id.seekBar_LowGoal_Teleop3);

        chkBox_highGoal = (CheckBox) findViewById(R.id.chkBox_highGoal);
        chkBox_lowGoal = (CheckBox) findViewById(R.id.chkBox_lowGoal);
        chkBox_highGoal1 = (CheckBox) findViewById(R.id.chkBox_highGoal1);
        chkBox_lowGoal1 = (CheckBox) findViewById(R.id.chkBox_lowGoal1);
        chkBox_highGoal2 = (CheckBox) findViewById(R.id.chkBox_highGoal2);
        chkBox_lowGoal2 = (CheckBox) findViewById(R.id.chkBox_lowGoal2);
        chkBox_highGoal3 = (CheckBox) findViewById(R.id.chkBox_highGoal3);
        chkBox_lowGoal3 = (CheckBox) findViewById(R.id.chkBox_lowGoal3);


        chkBox_PU_Gears_floor = (CheckBox) findViewById(R.id.chkBox_PU_Gears_floor);
        editText_TeleComments = (EditText) findViewById(R.id.editText_teleComments);
        button_shooting_cyclesPlus = (Button) findViewById(R.id.button_shooting_cyclesPlus);
        button_shooting_cyclesMinus = (Button) findViewById(R.id.button_shooting_cyclesMinus);
        button_GoToFinalActivity = (Button) findViewById(R.id.button_GoToFinalActivity);
        button_GearPlacedT= (Button) findViewById(R.id.button_GearPlacedT);
        button_GearPlacedTPlus = (Button) findViewById(R.id.button_GearPlacedTPlus);
        lbl_GearNUMT = (TextView) findViewById(R.id.lbl_GearNUMT);
        lbl_GearNUMT.setText(Integer.toString(tele_gears_placed));
        lbl_shooting_cycles = (TextView) findViewById(R.id.lbl_shooting_cycles);
        lbl_shooting_cycles.setText(Integer.toString(tele_cycles));
        pfDatabase = FirebaseDatabase.getInstance();
//        pfTeam_DBReference = pfDatabase.getReference("teams");              // Tteam data from Firebase D/B
//        pfStudent_DBReference = pfDatabase.getReference("students");        // List of Students
//        pfDevice_DBReference = pfDatabase.getReference("devices");          // List of Students
//        pfMatch_DBReference = pfDatabase.getReference("matches");           // List of Students
//        pfCur_Match_DBReference = pfDatabase.getReference("current-match"); // _THE_ current Match
        pfDevice_DBReference = pfDatabase.getReference("devices");          // List of Students
        lbl_GearsAttempted = (TextView) findViewById(R.id.lbl_GearsAttempted);
        button_GearAttemptedP = (Button) findViewById(R.id.button_GearAttemptedP);
        button_GearAttemptedM = (Button) findViewById(R.id.button_GearAttemptedM);
        chk_climbsuccessful = (CheckBox) findViewById(R.id.chk_climbsuccess);
        chk_climbattempted = (CheckBox) findViewById(R.id.chk_climbattempt);
        chk_touchpad = (CheckBox) findViewById(R.id.chk_touchpad);
        chk_touchpadpts = (CheckBox) findViewById(R.id.chk_touchpadpts);
        txt_seekBarHGvalue.setVisibility(View.INVISIBLE);
        txt_seekBarLGvalue.setVisibility(View.INVISIBLE);
        txt_seekBarHGvalue1.setVisibility(View.INVISIBLE);
        txt_seekBarLGvalue1.setVisibility(View.INVISIBLE);
        txt_seekBarHGvalue2.setVisibility(View.INVISIBLE);
        txt_seekBarLGvalue2.setVisibility(View.INVISIBLE);
        txt_seekBarHGvalue3.setVisibility(View.INVISIBLE);
        txt_seekBarLGvalue3.setVisibility(View.INVISIBLE);
        seekBar_HighGoal_Teleop.setEnabled(false);
        seekBar_HighGoal_Teleop.setVisibility(View.GONE);
        seekBar_LowGoal_Teleop.setEnabled(false);
        seekBar_LowGoal_Teleop.setVisibility(View.GONE);
        lbl_shooting_cycles.setVisibility(View.VISIBLE);
        button_shooting_cyclesMinus.setVisibility(View.VISIBLE);
        button_shooting_cyclesMinus.setEnabled(true);
        button_shooting_cyclesPlus.setVisibility(View.VISIBLE);
        button_shooting_cyclesPlus.setEnabled(true);
        chkBox_highGoal.setVisibility(View.INVISIBLE);
        chkBox_highGoal.setEnabled(false);
        chkBox_lowGoal.setVisibility(View.INVISIBLE);
        chkBox_lowGoal.setEnabled(false);


        lbl_GearsAttempted.setText(Integer.toString(tele_gears_attempt));

        button_shooting_cyclesMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tele_cycles >= 1) {
                    tele_cycles--;
                }
                if (tele_cycles == 0) {
                    chkBox_highGoal.setVisibility(View.INVISIBLE);
                    chkBox_highGoal.setEnabled(false);
                    chkBox_lowGoal.setVisibility(View.INVISIBLE);
                    chkBox_lowGoal.setEnabled(false);
                    seekBar_HighGoal_Teleop.setEnabled(false);
                    seekBar_HighGoal_Teleop.setVisibility(View.INVISIBLE);
                    seekBar_LowGoal_Teleop.setEnabled(false);
                    seekBar_LowGoal_Teleop.setVisibility(View.INVISIBLE);
                    txt_seekBarHGvalue.setVisibility(View.INVISIBLE);
                    txt_seekBarLGvalue.setVisibility(View.INVISIBLE);
                    chkBox_highGoal.setChecked(false);
                    chkBox_lowGoal.setChecked(false);
                    seekBar_LowGoal_Teleop.setProgress(100);
                    seekBar_HighGoal_Teleop.setProgress(100);


                } //else {
//                    chkBox_highGoal.setVisibility(View.VISIBLE);
//                    chkBox_highGoal.setEnabled(true);
//                    chkBox_lowGoal.setVisibility(View.VISIBLE);
//                    chkBox_lowGoal.setEnabled(true);
//                    seekBar_HighGoal_Teleop.setEnabled(true);
//                    seekBar_HighGoal_Teleop.setVisibility(View.VISIBLE);
//                    seekBar_LowGoal_Teleop.setEnabled(true);
//                    seekBar_LowGoal_Teleop.setVisibility(View.VISIBLE);
//                    txt_seekBarHGvalue.setVisibility(View.VISIBLE);
//                    txt_seekBarLGvalue.setVisibility(View.VISIBLE);
//                }

                Log.w(TAG, "Number of Cycles = " + tele_cycles);
                lbl_shooting_cycles.setText(Integer.toString(tele_cycles));
            }
        });
        button_shooting_cyclesPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tele_cycles <= 3) {
                    tele_cycles++;
                }
                if (TeleopScoutActivity.this.tele_cycles == 1) {

                    chkBox_highGoal.setVisibility(View.VISIBLE);
                    chkBox_highGoal.setEnabled(true);
                    chkBox_lowGoal.setVisibility(View.VISIBLE);
                    chkBox_lowGoal.setEnabled(true);
                    //txt_seekBarHGvalue.setVisibility(View.VISIBLE);
                    //txt_seekBarLGvalue.setVisibility(View.VISIBLE);

                }
                else if (TeleopScoutActivity.this.tele_cycles == 2) {

                    chkBox_highGoal1.setVisibility(View.VISIBLE);
                    chkBox_highGoal1.setEnabled(true);
                    chkBox_lowGoal1.setVisibility(View.VISIBLE);
                    chkBox_lowGoal1.setEnabled(true);
                    //txt_seekBarHGvalue1.setVisibility(View.VISIBLE);
                    //txt_seekBarLGvalue1.setVisibility(View.VISIBLE);

                    chkBox_highGoal.setVisibility(View.GONE);
                    chkBox_highGoal.setEnabled(false);
                    chkBox_lowGoal.setVisibility(View.GONE);
                    chkBox_lowGoal.setEnabled(false);
                    txt_seekBarHGvalue.setVisibility(View.GONE);
                    txt_seekBarLGvalue.setVisibility(View.GONE);
                    seekBar_HighGoal_Teleop.setVisibility(View.GONE);
                    seekBar_LowGoal_Teleop.setVisibility(View.GONE);
                    seekBar_HighGoal_Teleop.setEnabled(false);
                    seekBar_LowGoal_Teleop.setEnabled(false);

                }
                else if (TeleopScoutActivity.this.tele_cycles == 3) {

                    chkBox_highGoal2.setVisibility(View.VISIBLE);
                    chkBox_highGoal2.setEnabled(true);
                    chkBox_lowGoal2.setVisibility(View.VISIBLE);
                    chkBox_lowGoal2.setEnabled(true);
                    //txt_seekBarHGvalue2.setVisibility(View.VISIBLE);
                    //txt_seekBarLGvalue2.setVisibility(View.VISIBLE);

                    chkBox_highGoal1.setVisibility(View.INVISIBLE);
                    chkBox_highGoal1.setEnabled(false);
                    chkBox_lowGoal1.setVisibility(View.INVISIBLE);
                    chkBox_lowGoal1.setEnabled(false);
                    txt_seekBarHGvalue1.setVisibility(View.INVISIBLE);
                    txt_seekBarLGvalue1.setVisibility(View.INVISIBLE);
                    seekBar_HighGoal_Teleop1.setVisibility(View.GONE);
                    seekBar_LowGoal_Teleop1.setVisibility(View.GONE);
                    seekBar_HighGoal_Teleop1.setEnabled(false);
                    seekBar_LowGoal_Teleop1.setEnabled(false);

                }
                else if (TeleopScoutActivity.this.tele_cycles == 4) {

                    chkBox_highGoal3.setVisibility(View.VISIBLE);
                    chkBox_highGoal3.setEnabled(true);
                    chkBox_lowGoal3.setVisibility(View.VISIBLE);
                    chkBox_lowGoal3.setEnabled(true);
                    //txt_seekBarHGvalue3.setVisibility(View.VISIBLE);
                    //txt_seekBarLGvalue3.setVisibility(View.VISIBLE);

                    chkBox_highGoal2.setVisibility(View.INVISIBLE);
                    chkBox_highGoal2.setEnabled(false);
                    chkBox_lowGoal2.setVisibility(View.INVISIBLE);
                    chkBox_lowGoal2.setEnabled(false);
                    txt_seekBarHGvalue2.setVisibility(View.INVISIBLE);
                    txt_seekBarLGvalue2.setVisibility(View.INVISIBLE);
                    seekBar_HighGoal_Teleop2.setVisibility(View.GONE);
                    seekBar_LowGoal_Teleop2.setVisibility(View.GONE);
                    seekBar_HighGoal_Teleop2.setEnabled(false);
                    seekBar_LowGoal_Teleop2.setEnabled(false);

                }
                else {

                    chkBox_highGoal.setVisibility(View.INVISIBLE);
                    chkBox_highGoal.setEnabled(false);
                    chkBox_lowGoal.setVisibility(View.INVISIBLE);
                    chkBox_lowGoal.setEnabled(false);
                    txt_seekBarHGvalue.setVisibility(View.INVISIBLE);
                    txt_seekBarLGvalue.setVisibility(View.INVISIBLE);

                    chkBox_highGoal1.setVisibility(View.INVISIBLE);
                    chkBox_highGoal1.setEnabled(false);
                    chkBox_lowGoal1.setVisibility(View.INVISIBLE);
                    chkBox_lowGoal1.setEnabled(false);
                    txt_seekBarHGvalue1.setVisibility(View.INVISIBLE);
                    txt_seekBarLGvalue1.setVisibility(View.INVISIBLE);

                    chkBox_highGoal2.setVisibility(View.INVISIBLE);
                    chkBox_highGoal2.setEnabled(false);
                    chkBox_lowGoal2.setVisibility(View.INVISIBLE);
                    chkBox_lowGoal2.setEnabled(false);
                    txt_seekBarHGvalue2.setVisibility(View.INVISIBLE);
                    txt_seekBarLGvalue2.setVisibility(View.INVISIBLE);

                    chkBox_highGoal3.setVisibility(View.INVISIBLE);
                    chkBox_highGoal3.setEnabled(false);
                    chkBox_lowGoal3.setVisibility(View.INVISIBLE);
                    chkBox_lowGoal3.setEnabled(false);
                    txt_seekBarHGvalue3.setVisibility(View.INVISIBLE);
                    txt_seekBarLGvalue3.setVisibility(View.INVISIBLE);

                }

                Log.w(TAG, "Number of Cycles = " + tele_cycles);
                lbl_shooting_cycles.setText(Integer.toString(tele_cycles));
            }
        });

        button_GearAttemptedP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tele_gears_attempt < 50) {
                    tele_gears_attempt++;
                }
                Log.w(TAG, "Gears = " + tele_gears_attempt);      // ** DEBUG **
                lbl_GearsAttempted.setText(Integer.toString(tele_gears_attempt));    // Perform action on click
            }
        });
        button_GearAttemptedM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tele_gears_attempt >= 1 && tele_gears_attempt > tele_gears_placed) {
                    tele_gears_attempt--;
                }
                Log.w(TAG, "Gears = " + tele_gears_attempt);      // ** DEBUG **
                lbl_GearsAttempted.setText(Integer.toString(tele_gears_attempt));    // Perform action on click
            }
        });

        button_GearPlacedTPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tele_gears_placed < 12) {
                    tele_gears_placed++;
                    tele_gears_attempt++;
                }
                Log.w(TAG, "Gears Placed = " + tele_gears_placed);      // ** DEBUG **
                Log.w(TAG, "Gears Attempted = " + tele_gears_attempt);
                lbl_GearNUMT.setText(Integer.toString(tele_gears_placed));    // Perform action on click
                lbl_GearsAttempted.setText(Integer.toString(tele_gears_attempt));
            }
        });
        button_GearPlacedT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tele_gears_placed >= 1 && tele_gears_attempt >= tele_gears_placed) {
                    tele_gears_placed--;
                    tele_gears_attempt--;
                }
                Log.w(TAG, "Gears Placed = " + tele_gears_placed);      // ** DEBUG **
                Log.w(TAG, "Gears Attempted = " + tele_gears_attempt);
                lbl_GearNUMT.setText(Integer.toString(tele_gears_placed));    // Perform action on click
                lbl_GearsAttempted.setText(Integer.toString(tele_gears_attempt));
            }
        });

        button_GoToFinalActivity.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.i(TAG, "Clicked Final");

                Log.w(TAG, "NUMBER OF CYCLES = " + tele_cycles);
                if (tele_cycles >= 1) {
                    tele_hg_percent = (tele_hg_percent0 + tele_hg_percent1 + tele_hg_percent2 + tele_hg_percent3)/tele_cycles;
                    tele_lg_percent = (tele_lg_percent0 + tele_lg_percent1 +tele_lg_percent2 + tele_lg_percent3)/tele_cycles;
                    Log.w(TAG, "TELE_HG_PERCENT = " + tele_hg_percent);
                    Log.w(TAG, "TELE_LG_PERCENT = " + tele_lg_percent);
                } else {
                    tele_cycles = 0;
                }

                updateDev("Final");         // Update 'Phase' for stoplight indicator in ScoutM aster
                storeTeleData();            // Put all the TeleOps data collected in Match object

                Intent smast_intent = new Intent(TeleopScoutActivity.this, FinalActivity.class);
                Bundle SMbundle = new Bundle();
                SMbundle.putString("tnum", tn);
                smast_intent.putExtras(SMbundle);
                startActivity(smast_intent);
            }
        });

        chkBox_PU_Gears_floor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.i(TAG, "chkBox_PU_Gears_floor Listener");
            if (buttonView.isChecked()) {
                  //checked
                 Log.i(TAG,"TextBox is checked.");
                tele_gear_pickup = true;

            }
            else
            {
                  //not checked
                Log.i(TAG,"TextBox is unchecked.");
                tele_gear_pickup = false;

            }
            }
        }
        );


        chk_climbattempted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
               Log.i(TAG, "chk_climbfailed Listener");
                if (buttonView.isChecked()) {
                    //checked
                    Log.i(TAG,"TextBox is checked.");
                    tele_climb_attempt = true;

                }
                else
                {
                    //not checked
                    Log.i(TAG,"TextBox is unchecked.");
                    tele_climb_attempt = false;

                }
            }
        }
        );
        chk_climbsuccessful.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.i(TAG, "chk_climbsuccssful Listener");
                if (buttonView.isChecked()) {
                    //checked
                    Log.i(TAG,"TextBox is checked.");
                    tele_climb_success = true;
                    chk_touchpad.setChecked(true);
                    chk_climbattempted.setChecked(true);


                }
                else
                {
                    //not checked
                    Log.i(TAG,"TextBox is unchecked.");
                    tele_climb_success = false;
                    chk_touchpad.setChecked(false);
                    chk_climbattempted.setChecked(false);


                }
            }

        }
        );
        chk_touchpad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.i(TAG, "chkBox_gears Listener");
            if (buttonView.isChecked()) {
                //checked
                Log.i(TAG,"TextBox is checked.");
                tele_touch_act =  true;
                chk_climbattempted.setChecked(true);


            }
            else
            {
                //not checked
                Log.i(TAG,"TextBox is unchecked.");
                tele_touch_act = false;
                chk_climbattempted.setChecked(false);


            }
            }
        }
        );
//        chk_touchpadpts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
//                Log.i(TAG, "chkBox_gears Listener");
//                if (buttonView.isChecked()) {
//                    //checked
//                    Log.i(TAG,"TextBox is checked.");
//                    tele_touch_pts = true;
//
//                }
//                else
//                {
//                    //not checked
//                    Log.i(TAG,"TextBox is unchecked.");
//                    tele_touch_pts = false;
//
//                }
//            }
//        }
//        );
        seekBar_HighGoal_Teleop.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar_HighGoal_Teleop) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar_HighGoal_Teleop) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar_HighGoal_Teleop, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub

                tele_hg_percent0=progress;	//we can use the progress value of pro as anywhere
                txt_seekBarHGvalue.setText(Integer.toString(tele_hg_percent0));
                Log.w(TAG, "HG SEEKBAR VALUE = " + tele_hg_percent0);
            }

        });

        chkBox_highGoal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.i(TAG, "chkBox_highGoal0 Listener");
            if (buttonView.isChecked()) {
                //checked
                Log.i(TAG, "TextBox is checked.");
                seekBar_HighGoal_Teleop.setEnabled(true);
                seekBar_HighGoal_Teleop.setVisibility(View.VISIBLE);
                txt_seekBarHGvalue.setVisibility(View.VISIBLE);
                tele_hg = true;

            }
            else
            {
                //not checked
                Log.i(TAG,"TextBox is unchecked.");
                seekBar_HighGoal_Teleop.setEnabled(false);
                seekBar_HighGoal_Teleop.setVisibility(View.GONE);
                txt_seekBarHGvalue.setVisibility(View.GONE);
                tele_hg = false;

            }

            }
        }
        );
        seekBar_LowGoal_Teleop.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar_LowGoal_Teleop) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar_LowGoal_Teleop) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar_LowGoal_Teleop, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub

                tele_lg_percent0=progress;	//we can use the progress value of pro as anywhere
                txt_seekBarLGvalue.setText(Integer.toString(tele_lg_percent0));
                Log.w(TAG, "LG SEEKBAR VALUE = " + tele_lg_percent0);
            }

        });
        chkBox_lowGoal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

           @Override
           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
               Log.i(TAG, "chkBox_lowGoal0 Listener");
               if (buttonView.isChecked()) {
                   //checked
                   Log.i(TAG,"TextBox is checked.");
                   seekBar_LowGoal_Teleop.setEnabled(true);
                   seekBar_LowGoal_Teleop.setVisibility(View.VISIBLE);
                   txt_seekBarLGvalue.setVisibility(View.VISIBLE);
                   tele_lg = true;

               }
               else
               {
                   //not checked
                   Log.i(TAG,"TextBox is unchecked.");
                   seekBar_LowGoal_Teleop.setEnabled(false);
                   seekBar_LowGoal_Teleop.setVisibility(View.GONE);
                   txt_seekBarLGvalue.setVisibility(View.GONE);
                   tele_lg = false;

               }
           }
       }
        );


        seekBar_HighGoal_Teleop1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar_HighGoal_Teleop1) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar_HighGoal_Teleop1) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onProgressChanged(SeekBar seekBar_HighGoal_Teleop1, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                tele_hg_percent1=progress;	//we can use the progress value of pro as anywhere
                txt_seekBarHGvalue1.setText(Integer.toString(tele_hg_percent1));
                Log.w(TAG, "HG SEEKBAR VALUE = " + tele_hg_percent1);
            }
        });
        chkBox_highGoal1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

           @Override
           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
           Log.i(TAG, "chkBox_highGoal1 Listener");
           if (buttonView.isChecked()) {
               //checked
               Log.i(TAG, "TextBox is checked.");
               seekBar_HighGoal_Teleop1.setEnabled(true);
               seekBar_HighGoal_Teleop1.setVisibility(View.VISIBLE);
               txt_seekBarHGvalue1.setVisibility(View.VISIBLE);
               tele_hg = true;
           }
           else
           {
               //not checked
               Log.i(TAG,"TextBox is unchecked.");
               seekBar_HighGoal_Teleop1.setEnabled(false);
               seekBar_HighGoal_Teleop1.setVisibility(View.GONE);
               txt_seekBarHGvalue1.setVisibility(View.GONE);
               tele_hg = false;
           }
           }
        }
        );
        seekBar_LowGoal_Teleop1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar_LowGoal_Teleop1) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar_LowGoal_Teleop1) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onProgressChanged(SeekBar seekBar_LowGoal_Teleop1, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                tele_lg_percent1=progress;	//we can use the progress value of pro as anywhere
                txt_seekBarLGvalue1.setText(Integer.toString(tele_lg_percent1));
                Log.w(TAG, "LG SEEKBAR VALUE = " + tele_lg_percent1);
            }
        });
        chkBox_lowGoal1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.i(TAG, "chkBox_lowGoal1 Listener");
                if (buttonView.isChecked()) {
                    //checked
                    Log.i(TAG,"TextBox is checked.");
                    seekBar_LowGoal_Teleop1.setEnabled(true);
                    seekBar_LowGoal_Teleop1.setVisibility(View.VISIBLE);
                    txt_seekBarLGvalue1.setVisibility(View.VISIBLE);
                    tele_lg = true;
                }
                else
                {
                    //not checked
                    Log.i(TAG,"TextBox is unchecked.");
                    seekBar_LowGoal_Teleop1.setEnabled(false);
                    seekBar_LowGoal_Teleop1.setVisibility(View.GONE);
                    txt_seekBarLGvalue1.setVisibility(View.GONE);
                    tele_lg = false;
                }
            }
        }
        );


        seekBar_HighGoal_Teleop2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar_HighGoal_Teleop2) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar_HighGoal_Teleop2) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onProgressChanged(SeekBar seekBar_HighGoal_Teleop2, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                tele_hg_percent2=progress;	//we can use the progress value of pro as anywhere
                txt_seekBarHGvalue2.setText(Integer.toString(tele_hg_percent2));
                Log.w(TAG, "HG SEEKBAR VALUE = " + tele_hg_percent2);
            }
        });
        chkBox_highGoal2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.i(TAG, "chkBox_highGoal2 Listener");
                if (buttonView.isChecked()) {
                    //checked
                    Log.i(TAG, "TextBox is checked.");
                    seekBar_HighGoal_Teleop2.setEnabled(true);
                    seekBar_HighGoal_Teleop2.setVisibility(View.VISIBLE);
                    txt_seekBarHGvalue2.setVisibility(View.VISIBLE);
                    tele_hg = true;
                }
                else
                {
                    //not checked
                    Log.i(TAG,"TextBox is unchecked.");
                    seekBar_HighGoal_Teleop2.setEnabled(false);
                    seekBar_HighGoal_Teleop2.setVisibility(View.GONE);
                    txt_seekBarHGvalue2.setVisibility(View.GONE);
                    tele_hg = false;
                }
            }
        }
        );
        seekBar_LowGoal_Teleop2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar_LowGoal_Teleop2) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar_LowGoal_Teleop2) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onProgressChanged(SeekBar seekBar_LowGoal_Teleop2, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                tele_lg_percent2=progress;	//we can use the progress value of pro as anywhere
                txt_seekBarLGvalue2.setText(Integer.toString(tele_lg_percent2));
                Log.w(TAG, "LG SEEKBAR VALUE = " + tele_lg_percent2);
            }
        });
        chkBox_lowGoal2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
               Log.i(TAG, "chkBox_lowGoal2 Listener");
               if (buttonView.isChecked()) {
                   //checked
                   Log.i(TAG,"TextBox is checked.");
                   seekBar_LowGoal_Teleop2.setEnabled(true);
                   seekBar_LowGoal_Teleop2.setVisibility(View.VISIBLE);
                   txt_seekBarLGvalue2.setVisibility(View.VISIBLE);
                   tele_lg = true;
               }
               else
               {
                   //not checked
                   Log.i(TAG,"TextBox is unchecked.");
                   seekBar_LowGoal_Teleop2.setEnabled(false);
                   seekBar_LowGoal_Teleop2.setVisibility(View.GONE);
                   txt_seekBarLGvalue2.setVisibility(View.GONE);
                   tele_lg = false;
               }
           }
        }
        );

        seekBar_HighGoal_Teleop3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar_HighGoal_Teleop3) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar_HighGoal_Teleop3) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onProgressChanged(SeekBar seekBar_HighGoal_Teleop3, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                tele_hg_percent3=progress;	//we can use the progress value of pro as anywhere
                txt_seekBarHGvalue3.setText(Integer.toString(tele_hg_percent3));
                Log.w(TAG, "HG SEEKBAR VALUE = " + tele_hg_percent3);
            }
        });
        chkBox_highGoal3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.i(TAG, "chkBox_highGoal3 Listener");
                if (buttonView.isChecked()) {
                    //checked
                    Log.i(TAG, "TextBox is checked.");
                    seekBar_HighGoal_Teleop3.setEnabled(true);
                    seekBar_HighGoal_Teleop3.setVisibility(View.VISIBLE);
                    txt_seekBarHGvalue3.setVisibility(View.VISIBLE);
                    tele_hg = true;
                }
                else
                {
                    //not checked
                    Log.i(TAG,"TextBox is unchecked.");
                    seekBar_HighGoal_Teleop3.setEnabled(false);
                    seekBar_HighGoal_Teleop3.setVisibility(View.GONE);
                    txt_seekBarHGvalue3.setVisibility(View.GONE);
                    tele_hg = false;
                }
            }
        }
        );
        seekBar_LowGoal_Teleop3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar_LowGoal_Teleop3) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar_LowGoal_Teleop3) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onProgressChanged(SeekBar seekBar_LowGoal_Teleop3, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                tele_lg_percent3=progress;	//we can use the progress value of pro as anywhere
                txt_seekBarLGvalue3.setText(Integer.toString(tele_lg_percent3));
                Log.w(TAG, "LG SEEKBAR VALUE = " + tele_lg_percent3);
            }
        });
        chkBox_lowGoal3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
               Log.i(TAG, "chkBox_lowGoal3 Listener");
               if (buttonView.isChecked()) {
                   //checked
                   Log.i(TAG,"TextBox is checked.");
                   seekBar_LowGoal_Teleop3.setEnabled(true);
                   seekBar_LowGoal_Teleop3.setVisibility(View.VISIBLE);
                   txt_seekBarLGvalue3.setVisibility(View.VISIBLE);
                   tele_lg = true;
               }
               else
               {
                   //not checked
                   Log.i(TAG,"TextBox is unchecked.");
                   seekBar_LowGoal_Teleop3.setEnabled(false);
                   seekBar_LowGoal_Teleop3.setVisibility(View.GONE);
                   txt_seekBarLGvalue3.setVisibility(View.GONE);
                   tele_lg = false;
               }
           }
        }
        );
//        Log.w(TAG, "NUMBER OF CYCLES = " + tele_cycles);
//        if (tele_cycles >= 1) {
//            tele_hg_percent = (tele_hg_percent0 + tele_hg_percent1 + tele_hg_percent2 + tele_hg_percent3)/tele_cycles;
//            tele_lg_percent = (tele_lg_percent0 + tele_lg_percent1 +tele_lg_percent2 + tele_lg_percent3)/tele_cycles;
//            Log.w(TAG, "TELE_HG_PERCENT = " + tele_hg_percent);
//            Log.w(TAG, "TELE_LG_PERCENT = " + tele_lg_percent);
//        } else {
//            tele_cycles = 0;
//        }


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
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void storeTeleData() {
        Log.i(TAG, ">>>>  storeTeleData  <<<<");
        Log.w(TAG, "TELE CYCLES" + tele_cycles);
        Log.w(TAG, "TELE HG" + tele_hg_percent);
        Log.w(TAG, "TELE LG" + tele_lg_percent);
//        Pearadox.Match_Data.setTele_gears_placed(tele_gears_placed);
//        Pearadox.Match_Data.setTele_gears_attempt(tele_gears_attempt);
//        Pearadox.Match_Data.setTele_hg(tele_hg);
//        Pearadox.Match_Data.setTele_hg_percent(tele_hg_percent);
//        Pearadox.Match_Data.setTele_lg(tele_lg);
//        Pearadox.Match_Data.setTele_lg_percent(tele_lg_percent);
//        Pearadox.Match_Data.setTele_cycles(tele_cycles);
//        Pearadox.Match_Data.setTele_touch_act(tele_touch_act);
//        Pearadox.Match_Data.setTele_gear_pickup(tele_gear_pickup);

        Pearadox.Match_Data.setTele_climb_attempt(tele_climb_attempt);
        Pearadox.Match_Data.setTele_climb_success(tele_climb_success);
        //ToDo - add remaining TeleOps elements

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
                        //close();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();


    }

//    public void onClick(View view) {
//        Log.i(TAG, "<< calling Other Activty >>");
//        switch (view.getId()){
//            case button_GoToOtherActivity:
//                Log.i(TAG, "<< Comment Button Pushed >>");
//                Intent smastintent = new Intent(this, OtherActivity.class);
//                startActivity(smastintent);
//                break;
//            default:
//                break;
//        }
//    }

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
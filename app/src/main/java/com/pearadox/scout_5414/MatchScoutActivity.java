package com.pearadox.scout_5414;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.BatteryManager;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;


public class MatchScoutActivity extends AppCompatActivity {

    String TAG = "MatchScout_Activity";      // This CLASS name
    boolean onStart = false;
    protected Vibrator vibrate;
    long[] once = { 0, 100 };
    long[] twice = { 0, 100, 400, 100 };
    long[] thrice = { 0, 100, 400, 100, 400, 100 };
    public static String device = " ";
    EditText editTxt_Team, editTxt_Match;
    TextView txt_EventName, txt_dev, txt_stud, txt_Match, txt_MyTeam, txt_TeamName, text_HGSeekBarValue, text_LGSeekBarValue, text_collected_balls;
    CheckBox chk_baseline, checkbox_automode, chk_cubeSwitch, chk_attemptSwitch, chk_XoverSwitch, chk_WrongSwitch, chk_cubeScale, chk_attemptScale, chk_XoverScale, chk_WrongScale, chk_highGoal, chk_gears, chk_lowGoal, chk_activate_hopper, chk_baselineINVIS;
    CheckBox chk_ExtraSwitch, chk_cube;
    EditText editText_autoComment, editText_Fuel;
    Spinner spinner_balls_collected;
    SeekBar seekBar_HighGoal, seekBar_LowGoal;
//    ImageView imgScoutLogo;
    private Button button_GoToTeleopActivity, button_GoToArenaLayoutActivity, button_GearsMinus, button_GearsPlus, button_GearsAttemptedMinus, button_GearsAttemptedPlus;
    String team_num, team_name, team_loc;
    p_Firebase.teamsObj team_inst = new p_Firebase.teamsObj(team_num, team_name, team_loc);
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfTeam_DBReference;
    private DatabaseReference pfMatch_DBReference;
    private DatabaseReference pfDevice_DBReference;
    private DatabaseReference pfCur_Match_DBReference;
    String key = null;      // key for Devices Firebase
    ArrayAdapter<String> adapter_autostartpos;
//    ArrayAdapter<String> adapter_autostoppos;
//    ArrayAdapter<String> adapter_auto_gear_placement;
//    ArrayAdapter<String> adapter_balls_collected;

    // ===================  Autonomous Elements for Match Scout Data object ===================
    public String matchID = "T00";          // Type + #
    public String tn = "";                  // Team #
    public boolean carry_cube;              // Do they carry a cube
    public String  startPos = " ";          // Start Position
    public boolean auto = false;            // Do they have Autonomous mode?
    public boolean baseline;                // Did they cross Baseline
    public boolean cube_switch;             // cube placed on Switch during Auto
    public boolean cube_switch_att;         // cube attemted on Switch during Auto
    public boolean switch_extra;            // extra cube placed on switch during Auto
    public boolean cube_scale;              // cube placed on switch during Auto
    public boolean cube_scale_att;          // cube attemted on switch during Auto
    public boolean xover_switch;            // crossed over field to Switch
    public boolean xover_scale;             // crossed over field to Scale
    public boolean wrong_switch;            // put cube in WRONG Switch
    public boolean wrong_scale;             // put cube in WRONG Scale
    public String autoComment = " ";        // Comment
    /* */
    public static String studID = " ";

// ===========================================================================



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "<< Match Scout >>");
        onStart = false;
        setContentView(R.layout.activity_match_scout);
        Bundle bundle = this.getIntent().getExtras();
        String device = bundle.getString("dev");
        studID = bundle.getString("stud");
        Log.w(TAG, device + " " + studID);      // ** DEBUG **

        tn = bundle.getString("tnum");

        Pearadox.MatchData_Saved = false;    // Set flag to show need to saved
        txt_EventName = (TextView) findViewById(R.id.txt_EventName);
        txt_EventName.setText(Pearadox.FRC_EventName);          // Event Name

        pfDatabase = FirebaseDatabase.getInstance();
        pfTeam_DBReference = pfDatabase.getReference("teams");              // Tteam data from Firebase D/B
//        pfStudent_DBReference = pfDatabase.getReference("students");        // List of Students
//        pfDevice_DBReference = pfDatabase.getReference("devices");          // List of Students
        pfMatch_DBReference = pfDatabase.getReference("matches");           // List of Students
        pfCur_Match_DBReference = pfDatabase.getReference("current-match"); // _THE_ current Match
        pfDevice_DBReference = pfDatabase.getReference("devices");          // List of Students
        updateDev("Auto");      // Update 'Phase' for stoplight indicator in ScoutMaster

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level / (float) scale;
        int pct = (int) (batteryPct * 100);
        String batpct = String.valueOf(pct);
        Log.w(TAG, "Battery=" + batteryPct + "  " + batpct);      // ** DEBUG **
        switch (Pearadox.FRC514_Device) {
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
            default:                //
                Log.d(TAG, "DEV = NULL");
        }
        Log.w(TAG, "batt_stat=" + key + "  " + batpct);      // ** DEBUG **
        pfDevice_DBReference.child(key).child("batt_stat").setValue(batpct);

        txt_dev = (TextView) findViewById(R.id.txt_Dev);
        txt_stud = (TextView) findViewById(R.id.txt_stud);
        txt_Match = (TextView) findViewById(R.id.txt_Match);
        txt_MyTeam = (TextView) findViewById(R.id.txt_MyTeam);
        txt_TeamName = (TextView) findViewById(R.id.txt_TeamName);
        editTxt_Match = (EditText) findViewById(R.id.editTxt_Match);
        editTxt_Team = (EditText) findViewById(R.id.editTxt_Team);
        ImageView imgScoutLogo = (ImageView) findViewById(R.id.imageView_MS);
        txt_dev.setText(device);
        txt_stud.setText(studID);
        txt_Match.setText("");
        if (Pearadox.is_Network && Pearadox.numTeams > 0) {      // is Internet available and Teams there?
            txt_MyTeam.setText("");
            editTxt_Match.setVisibility(View.INVISIBLE);
            editTxt_Match.setEnabled(false);
            editTxt_Team.setVisibility(View.INVISIBLE);
            editTxt_Team.setEnabled(false);
        } else {
            editTxt_Match.setVisibility(View.VISIBLE);
            editTxt_Match.setEnabled(true);
            editTxt_Match.requestFocus();        // Don't let EditText mess up layout!!
            editTxt_Match.setFocusable(true);
            editTxt_Match.setFocusableInTouchMode(true);
            editTxt_Match.requestFocus();        // Don't let EditText mess up layout!!
            txt_Match.setText("Q");         // Default to qualifying
            editTxt_Team.setVisibility(View.VISIBLE);
            editTxt_Team.setEnabled(true);
            editTxt_Team.setFocusable(true);
            editTxt_Team.setFocusableInTouchMode(true);
            txt_MyTeam.setVisibility(View.GONE);
            txt_TeamName.setVisibility(View.GONE);

            editTxt_Match.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        Log.d(TAG, " editTxt_Match listener; Match = " + editTxt_Match.getText());
                        if (editTxt_Match.getText().length() < 2) {
                            vibrate.vibrate(twice, -1);
                            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                            Toast.makeText(getBaseContext(), "*** Match number must be at least 2 characters  *** ", Toast.LENGTH_LONG).show();
                        } else {
                            matchID = "Q" + (String.valueOf(editTxt_Match.getText()));
                        }
                        Log.e(TAG, " Match ID = " + matchID);
                        return true;
                    }
                    return false;
                }
            });
            editTxt_Team.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        Log.d(TAG, " editTxt_Team listener; Team = " + editTxt_Team.getText());
                        if (editTxt_Team.getText().length() < 3 || editTxt_Team.getText().length() > 4) {
                            vibrate.vibrate(twice, -1);
                            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                            Toast.makeText(getBaseContext(), "*** Team number must be at least 3 characters and no more than 4  *** ", Toast.LENGTH_LONG).show();
                        } else {
                            tn = (String.valueOf(editTxt_Team.getText()));
                        }
                        Log.e(TAG, " Team # = " + tn);
                        return true;
                    }
                    return false;
                }
            });
        }
        txt_TeamName.setText("");
        String devcol = device.substring(0, 3);
        Log.d(TAG, "color=" + devcol);
        if (devcol.equals("Red")) {
            imgScoutLogo.setImageDrawable(getResources().getDrawable(R.drawable.red_scout));
        } else {
            imgScoutLogo.setImageDrawable(getResources().getDrawable(R.drawable.blue_scout));
        }

//        chk_baselineINVIS = (CheckBox) findViewById(R.id.chk_baselineINVIS);
//        chk_baselineINVIS.setVisibility(View.INVISIBLE);
//        txt_GearsPlaced = (TextView) findViewById(R.id.txt_GearsPlaced);
//        text_collected_balls = (TextView) findViewById(R.id.text_collected_balls);
//        spinner_balls_collected = (Spinner) findViewById(R.id.spinner_balls_collected);
//        txt_GearsAttempted = (TextView) findViewById(R.id.txt_GearsAttempted);
        chk_baseline = (CheckBox) findViewById(R.id.chk_baseline);
//        chk_highGoal = (CheckBox) findViewById(R.id.chk_highGoal);
//        chk_lowGoal = (CheckBox) findViewById(R.id.chk_LowGoal);
//        seekBar_HighGoal = (SeekBar) findViewById(R.id.seekBar_HighGoal);
//        seekBar_LowGoal = (SeekBar) findViewById(R.id.seekBar_LowGoal);
        checkbox_automode = (CheckBox) findViewById(R.id.checkbox_automode);
        editText_autoComment = (EditText) findViewById(R.id.editText_autoComment);
//        chk_gears = (CheckBox) findViewById(R.id.chk_cube);
        button_GoToTeleopActivity = (Button) findViewById(R.id.button_GoToTeleopActivity);
        button_GoToArenaLayoutActivity = (Button) findViewById(R.id.button_GoToArenaLayoutActivity);
        chk_ExtraSwitch = (CheckBox) findViewById(R.id.chk_ExtraSwitch);
        chk_cube = (CheckBox) findViewById(R.id.chk_cube);
//        txt_GearsPlaced.setText(Integer.toString(gearNum));
//        txt_GearsAttempted.setText(Integer.toString(gearAttemptNum));
//        seekBar_HighGoal.setEnabled(false);
//        seekBar_HighGoal.setVisibility(View.GONE);
//        chk_highGoal.setChecked(false);
//        seekBar_LowGoal.setEnabled(false);
//        seekBar_LowGoal.setVisibility(View.GONE);
//        chk_lowGoal.setChecked(false);
//        text_HGSeekBarValue = (TextView) findViewById(R.id.text_HGSeekBarValue);
//        text_LGSeekBarValue = (TextView) findViewById(R.id.text_LGSeekBarValue);
//        text_HGSeekBarValue.setVisibility(View.GONE);
//        text_LGSeekBarValue.setVisibility(View.GONE);
//        editText_Fuel.setVisibility(View.GONE);
//        editText_Fuel.setEnabled(false);
//        editText_Fuel.setText("");
//        text_collected_balls.setVisibility(View.INVISIBLE);
//        spinner_balls_collected.setVisibility(View.INVISIBLE);

/* ========================Switch Ids======================== */
        chk_cubeSwitch = (CheckBox) findViewById(R.id.chk_cubeSwitch);
        chk_attemptSwitch = (CheckBox) findViewById(R.id.chk_attemptSwitch);
        chk_XoverSwitch = (CheckBox) findViewById(R.id.chk_XoverSwitch);
        chk_WrongSwitch = (CheckBox) findViewById(R.id.chk_WrongSwitch);

/* ========================Scale Ids======================== */
        chk_cubeScale = (CheckBox) findViewById(R.id.chk_cubeScale);
        chk_attemptScale = (CheckBox) findViewById(R.id.chk_attemptScale);
        chk_XoverScale = (CheckBox) findViewById(R.id.chk_XoverScale);
        chk_WrongScale = (CheckBox) findViewById(R.id.chk_WrongScale);


        Spinner spinner_startPos = (Spinner) findViewById(R.id.spinner_startPos);
        String[] autostartPos = getResources().getStringArray(R.array.auto_start_array);
        adapter_autostartpos = new ArrayAdapter<String>(this, R.layout.dev_list_layout, autostartPos);
        adapter_autostartpos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_startPos.setAdapter(adapter_autostartpos);
        spinner_startPos.setSelection(0, false);
        spinner_startPos.setOnItemSelectedListener(new startPosOnClickListener());



// Start Listeners
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        checkbox_automode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             Log.w(TAG, "checkbox_automode Listener");
                 if (buttonView.isChecked()) {
                     //checked
                     Log.w(TAG, "TextBox is checked.");
                     auto = true;
                 } else {
                     //not checked
                     Log.w(TAG, "TextBox is unchecked.");
                     auto = false;
                 }
             }
             }
            );

        chk_baseline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.w(TAG, "chk_baseline Listener");
                if (buttonView.isChecked()) {
                    //checked
                    baseline = true;
                    Log.w(TAG, "Crossed the Baseline = " + baseline);

                } else {
                    //not checked
                    baseline = false;
                    Log.w(TAG, "Crossed the Baseline = " + baseline);
                }
            }
        }
        );


        chk_cubeSwitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "chk_cubeSwitch Listener");
                if (chk_cubeSwitch.isChecked()) {
                    //checked
                    cube_switch = true;
                    cube_switch_att = true;
                    chk_attemptSwitch.setChecked(true);
                    Log.w(TAG, "Cube in Switch = " + cube_switch);

                } else {
                    //not checked
                    cube_switch = false;
                    cube_switch_att = false;
                    chk_attemptSwitch.setChecked(false);
                    xover_switch = false;
                    chk_XoverSwitch.setChecked(false);
                    Log.w(TAG, "Cube in Switch = " + cube_switch);

                }

            }
        });
        chk_attemptSwitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "chk_attemptSwitch Listener");
                if (chk_attemptSwitch.isChecked()) {
                    //checked
                    cube_switch_att = true;
                    Log.w(TAG, "Attempted to place Cube in Switch = " + cube_switch_att);

                } else {
                    //not checked
                    cube_switch_att = false;
                    Log.w(TAG, "Attempted to place Cube in Switch = " + cube_switch_att);

                }

            }
        });

        chk_XoverSwitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "chk_XoverSwitch Listener");
                if (chk_XoverSwitch.isChecked()) {
                    //checked
                    xover_switch = true;
                    Log.w(TAG, "Cross over Switch = " + xover_switch);

                } else {
                    //not checked
                    xover_switch = false;
                    Log.w(TAG, "Cross over Switch = " + xover_switch);

                }

            }
        });

        chk_WrongSwitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "chk_WrongSwitch Listener");
                if (chk_WrongSwitch.isChecked()) {
                    //checked
                    wrong_switch = true;
                    Log.w(TAG, "Wrong switch = " + wrong_switch);

                } else {
                    //not checked
                    wrong_switch = false;
                    Log.w(TAG, "Wrong switch = " + wrong_switch);

                }

            }
        });


        chk_cubeScale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "chk_cubeScale Listener");
                if (chk_cubeScale.isChecked()) {
                    //checked
                    cube_scale = true;
                    cube_scale_att = true;
                    chk_attemptScale.setChecked(true);
                    Log.w(TAG, "Cube in Scale = " + cube_scale);

                } else {
                    //not checked
                    cube_scale = false;
                    cube_scale_att = false;
                    chk_attemptScale.setChecked(false);
                    xover_scale = false;
                    chk_XoverScale.setChecked(false);
                    Log.w(TAG, "Cube in Scale = " + cube_scale);

                }

            }
        });
        chk_attemptScale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "chk_attemptSwitch Listener");
                if (chk_attemptScale.isChecked()) {
                    //checked
                    cube_scale_att = true;
                    Log.w(TAG, "Attempted to place Cube in Scale = " + cube_scale_att);

                } else {
                    //not checked
                    cube_scale_att = false;
                    Log.w(TAG, "Attempted to place Cube in Scale = " + cube_scale_att);

                }

            }
        });

        chk_XoverScale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "chk_XoverScale Listener");
                if (chk_XoverScale.isChecked()) {
                    //checked
                    xover_scale = true;
                    Log.w(TAG, "Cross over Scale = " + xover_scale);

                } else {
                    //not checked
                    xover_scale = false;
                    Log.w(TAG, "Cross over Scale = " + xover_scale);

                }

            }
        });

        chk_WrongScale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "chk_WrongScale Listener");
                if (chk_WrongScale.isChecked()) {
                    //checked
                    wrong_scale = true;
                    Log.w(TAG, "Wrong Scale = " + wrong_scale);

                } else {
                    //not checked
                    wrong_scale = false;
                    Log.w(TAG, "Wrong Scale = " + wrong_scale);

                }

            }
        });


        chk_ExtraSwitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "chk_ExtraSwitch Listener");
                if (chk_ExtraSwitch.isChecked()) {
                    //checked
                    switch_extra = true;
                    Log.w(TAG, "Extra Switch = " + switch_extra);

                } else {
                    //not checked
                    switch_extra = false;
                    Log.w(TAG, "Extra Switch = " + switch_extra);

                }

            }
        });


        chk_cube.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "chk_cube Listener");
                if (chk_cube.isChecked()) {
                    //checked
                    carry_cube = true;
                    Log.w(TAG, "Carry Cube = " + carry_cube);

                } else {
                    //not checked
                    carry_cube = false;
                    Log.w(TAG, "Carry Cube = " + carry_cube);

                }

            }
        });


//        button_GearsAttemptedPlus.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (gearAttemptNum < 10) {
//                    gearAttemptNum++;
//                }
//                Log.d(TAG, "Gears Attempted = " + gearAttemptNum);      // ** DEBUG **
//                txt_GearsAttempted.setText(Integer.toString(gearAttemptNum));    // Perform action on click
//            }
//        });
//        button_GearsAttemptedMinus.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (gearAttemptNum >= 1 && gearAttemptNum > gearNum) {    // GLF - make sure note less than placed
//                    gearAttemptNum--;
//                }
//                Log.d(TAG, "Gears Attempted = " + gearAttemptNum);      // ** DEBUG **
//                txt_GearsAttempted.setText(Integer.toString(gearAttemptNum));    // Perform action on click
//            }
//        });


        button_GoToTeleopActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "Clicked 'NEXT/TeleOps' Button  match=" + matchID);
                if (matchID.length() < 2) {     // Between matches??
                    Toast.makeText(getBaseContext(), "*** Match has _NOT_ started; wait until you have a Team #  *** ", Toast.LENGTH_LONG).show();

                } else {        // It's OK - Match has started

                    if (0 > 0) {            //**COMPILE -  GLF 1/21 **
//                        if (gearAttemptNum > 0 && spinner_GearPlacement.getSelectedItemPosition() == 0) {  //Required field

                        Toast.makeText(getBaseContext(), "*** Select Gear Position  *** ", Toast.LENGTH_LONG).show();
//                        spinner_GearPlacement.performClick();

                    } else {
                        if (tn != null) {
                            // ToDo - check to see if ALL required fields entered (Start-pos, stop, gear, ....)
                            updateDev("Tele");      // Update 'Phase' for stoplight indicator in ScoutM aster
                            storeAutoData();        // Put all the Autonomous data collected in Match object

                            Intent smast_intent = new Intent(MatchScoutActivity.this, TeleopScoutActivity.class);
                            Bundle SMbundle = new Bundle();
                            SMbundle.putString("tnum", tn);
                            smast_intent.putExtras(SMbundle);
                            startActivity(smast_intent);
                        } else {
                            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                            Toast.makeText(getBaseContext(), "*** Team # not entered  *** ", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        button_GoToArenaLayoutActivity.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.i(TAG, "Clicked Sidebar");
                Intent smast_intent = new Intent(MatchScoutActivity.this, ArenaLayoutActivity.class);
                Bundle SMbundle = new Bundle();
                smast_intent.putExtras(SMbundle);
                startActivity(smast_intent);
            }
            });
        }


// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void storeAutoData() {
        Log.i(TAG, ">>>>  storeAutoData  <<<< " + studID);
        Pearadox.Match_Data.setMatch(matchID);
        Pearadox.Match_Data.setTeam_num(tn);
        Pearadox.Match_Data.setPre_cube(carry_cube);
        Pearadox.Match_Data.setPre_startPos(startPos);
        Pearadox.Match_Data.setAuto_baseline(baseline);
        Pearadox.Match_Data.setAuto_mode(auto);
        Pearadox.Match_Data.setAuto_cube_switch(cube_switch);
        Pearadox.Match_Data.setAuto_cube_switch_att(cube_switch_att);
        Pearadox.Match_Data.setAuto_switch_extra(switch_extra);
        Pearadox.Match_Data.setAuto_wrong_switch(wrong_switch);
        Pearadox.Match_Data.setAuto_cube_scale(cube_scale);
        Pearadox.Match_Data.setAuto_cube_scale_att(cube_scale_att);
        Pearadox.Match_Data.setAuto_xover_switch(xover_switch);
        Pearadox.Match_Data.setAuto_xover_scale(xover_scale);
        Pearadox.Match_Data.setAuto_wrong_scale(wrong_scale);
        // --------------
        Pearadox.Match_Data.setAuto_comment(autoComment);
        Pearadox.Match_Data.setFinal_studID(studID);
        Log.w(TAG, "*******  All done with AUTO setters!!");
    }

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    private void getMatch() {
        Log.i(TAG, "%%%%  getMatch  %%%%");
        pfCur_Match_DBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Current Match - onDataChange  %%%%");
                txt_Match = (TextView) findViewById(R.id.txt_Match);
                txt_MyTeam = (TextView) findViewById(R.id.txt_MyTeam);
                txt_TeamName = (TextView) findViewById(R.id.txt_TeamName);
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();   /*get the data children*/
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    p_Firebase.curMatch match_Obj = iterator.next().getValue(p_Firebase.curMatch.class);
                    matchID = match_Obj.getCur_match();
                    Log.d(TAG, "***>  Current Match = " + matchID + " " + match_Obj.getR1() + " " + match_Obj.getB3());
                    if (matchID.length() < 3) {
//                        Log.d(TAG, "MatchID NULL");
                        txt_Match.setText(" ");
                        txt_MyTeam.setText(" ");
                        txt_TeamName.setText(" ");
                        updateDev("Auto");      // Update 'Phase' for stoplight indicator in ScoutM aster
                    } else {        // OK!!  Match has started
//                        Log.d(TAG, "Match started " + matchID);
                        txt_Match.setText(matchID);
//                        Log.d(TAG, "Device = " + Pearadox.FRC514_Device + " ->" + onStart);
                        switch (Pearadox.FRC514_Device) {          // Who am I?!?
                            case ("Red-1"):             //#Red or Blue Scout
                                txt_MyTeam.setText(match_Obj.getR1());
                                break;
                            case ("Red-2"):             //#
                                txt_MyTeam.setText(match_Obj.getR2());
                                break;
                            case ("Red-3"):             //#
                                txt_MyTeam.setText(match_Obj.getR3());
                                break;
                            case ("Blue-1"):            //#
                                txt_MyTeam.setText(match_Obj.getB1());
                                break;
                            case ("Blue-2"):            //#
                                txt_MyTeam.setText(match_Obj.getB2());
                                break;
                            case ("Blue-3"):            //#####
                                txt_MyTeam.setText(match_Obj.getB3());
                                break;
                            default:                //
                                Log.d(TAG, "device is _NOT_ a Scout ->" + device );
                        }
                        tn = (String) txt_MyTeam.getText();
                        findTeam(tn);   // Find Team info
                        txt_TeamName.setText(team_inst.getTeam_name());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*listener failed or was removed for security reasons*/
            }
        });
    }

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
            txt_TeamName.setText("");
        }
    }
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
                Log.d(TAG, "DEV = NULL" );
        }
             pfDevice_DBReference.child(key).child("phase").setValue(phase);
    }

    private class startPosOnClickListener implements android.widget.AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            startPos = parent.getItemAtPosition(pos).toString();
            Log.d(TAG, ">>>>>  '" + startPos + "'");

        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
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
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }


//    private TextWatcher tw = new TextWatcher() {
//        public void afterTextChanged(Editable s){
//
//        }
//        public void  beforeTextChanged(CharSequence s, int start, int count, int after){
//            // you can check for enter key here
//        }
//        public void  onTextChanged (CharSequence s, int start, int before,int count) {
//        }
//    };
//
//    EditText et = (EditText) findViewById(R.id.editText_Fuel);
//    et.addTextChangedListener(tw)



//###################################################################
//###################################################################
//###################################################################
@Override
public void onStart() {
    super.onStart();
    Log.v(TAG, "onStart");

    onStart = true;
    getMatch();      // Get current match
    Log.d(TAG, "*** onStart  ->" + onStart);

    vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    if (vibrate == null) {
        Log.e(TAG, "No vibration service exists.");
    }
}

    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        onStart = false;
        if (Pearadox.MatchData_Saved) {
            // ToDo - Clear all data back to original settings
            Log.d(TAG, "#### Data was saved in Final #### ");
            //Toast.makeText(getBaseContext(), "Data was saved in Final - probably should clear data and wait for next match", Toast.LENGTH_LONG).show();
            finish();
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

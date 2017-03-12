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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by mlm.02000 on 2/5/2017.
 */

public class TeleopScoutActivity extends Activity {


    String TAG = "TeleopScoutActivity";      // This CLASS name
    TextView txt_dev, txt_stud, txt_match, txt_MyTeam, lbl_GearNUMT, lbl_GearsAttempted, txt_seekBarHGvalue, txt_seekBarLGvalue, lbl_shooting_cycles, txt_shooting_cycle;
    private Button button_GoToFinalActivity,button_GearPlacedT, button_GearPlacedTPlus, button_GearAttemptedP, button_GearAttemptedM, button_shooting_cyclesPlus, button_shooting_cyclesMinus;
    CheckBox chk_climbsuccessful, chk_climbattempted, chk_touchpad, chk_touchpadpts, chkBox_highGoal, chkBox_lowGoal, chkBox_PU_Gears_floor;
    SeekBar seekBar_HighGoal_Teleop, seekBar_LowGoal_Teleop;
    EditText editText_TeleComments;
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfTeam_DBReference;
    private DatabaseReference pfMatch_DBReference;
    private DatabaseReference pfDevice_DBReference;
    private DatabaseReference pfCur_Match_DBReference;
    String key = null;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "<< Teleop Scout >>");
        setContentView(R.layout.activity_teleop_scout);
        Bundle bundle = this.getIntent().getExtras();
        String param1 = bundle.getString("dev");
        String param2 = bundle.getString("stud");
        Log.w(TAG, param1 + " " + param2);      // ** DEBUG **

        chkBox_PU_Gears_floor = (CheckBox) findViewById(R.id.chkBox_PU_Gears_floor);
        txt_shooting_cycle = (TextView) findViewById(R.id.txt_shooting_cycle);
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
        chkBox_highGoal = (CheckBox) findViewById(R.id.chkBox_highGoal);
        chkBox_lowGoal = (CheckBox) findViewById(R.id.chkBox_lowGoal);
        seekBar_HighGoal_Teleop = (SeekBar) findViewById(R.id.seekBar_HighGoal_Teleop);
        txt_seekBarHGvalue = (TextView) findViewById(R.id.txt_seekBarHGvalue);
        txt_seekBarHGvalue.setVisibility(View.GONE);
        seekBar_LowGoal_Teleop = (SeekBar) findViewById(R.id.seekBar_LowGoal_Teleop);
        txt_seekBarLGvalue = (TextView) findViewById(R.id.txt_seekBarLGvalue);
        txt_seekBarLGvalue.setVisibility(View.GONE);
        seekBar_HighGoal_Teleop.setEnabled(false);
        seekBar_HighGoal_Teleop.setVisibility(View.GONE);
        seekBar_LowGoal_Teleop.setEnabled(false);
        seekBar_LowGoal_Teleop.setVisibility(View.GONE);
//        txt_shooting_cycle.setVisibility(View.GONE);
//        lbl_shooting_cycles.setVisibility(View.GONE);
//        button_shooting_cyclesMinus.setVisibility(View.GONE);
//        button_shooting_cyclesMinus.setEnabled(false);
//        button_shooting_cyclesPlus.setVisibility(View.GONE);
//        button_shooting_cyclesPlus.setEnabled(false);


        lbl_GearsAttempted.setText(Integer.toString(tele_gears_attempt));

        button_shooting_cyclesMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tele_cycles >= 1) {
                    tele_cycles--;
                }
                Log.w(TAG, "Number of Cycles = " + tele_cycles);
                lbl_shooting_cycles.setText(Integer.toString(tele_cycles));
            }
        });
        button_shooting_cyclesPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tele_cycles <= 15) {
                    tele_cycles++;
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
                if (tele_gears_attempt >= 1) {
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

                updateDev("Final");         // Update 'Phase' for stoplight indicator in ScoutM aster
                storeTeleData();            // Put all the TeleOps data collected in Match object

                Intent smast_intent = new Intent(TeleopScoutActivity.this, FinalActivity.class);
                Bundle SMbundle = new Bundle();
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

                tele_hg_percent=progress;	//we can use the progress value of pro as anywhere
                txt_seekBarHGvalue.setText(Integer.toString(tele_hg_percent));
            }

        });

        chkBox_highGoal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.i(TAG, "chkBox_highGoal Listener");
            if (buttonView.isChecked()) {
                //checked
                Log.i(TAG,"TextBox is checked.");
                seekBar_HighGoal_Teleop.setEnabled(true);
                seekBar_HighGoal_Teleop.setVisibility(View.VISIBLE);
                txt_seekBarHGvalue.setVisibility(View.VISIBLE);
                tele_hg = true;

                txt_shooting_cycle.setVisibility(View.VISIBLE);
                lbl_shooting_cycles.setVisibility(View.VISIBLE);
                button_shooting_cyclesMinus.setVisibility(View.VISIBLE);
                button_shooting_cyclesPlus.setVisibility(View.VISIBLE);

            }
            else
            {
                //not checked
                Log.i(TAG,"TextBox is unchecked.");
                seekBar_HighGoal_Teleop.setEnabled(false);
                seekBar_HighGoal_Teleop.setVisibility(View.GONE);
                txt_seekBarHGvalue.setVisibility(View.GONE);
                tele_hg = false;

                txt_shooting_cycle.setVisibility(View.GONE);
                lbl_shooting_cycles.setVisibility(View.GONE);
                button_shooting_cyclesMinus.setVisibility(View.GONE);
                button_shooting_cyclesPlus.setVisibility(View.GONE);

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

                tele_lg_percent=progress;	//we can use the progress value of pro as anywhere
                txt_seekBarLGvalue.setText(Integer.toString(tele_lg_percent));
            }

        });
        chkBox_lowGoal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

           @Override
           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
               Log.i(TAG, "chkBox_lowGoal Listener");
               if (buttonView.isChecked()) {
                   //checked
                   Log.i(TAG,"TextBox is checked.");
                   seekBar_LowGoal_Teleop.setEnabled(true);
                   seekBar_LowGoal_Teleop.setVisibility(View.VISIBLE);
                   txt_seekBarLGvalue.setVisibility(View.VISIBLE);
                   tele_lg = true;

//                   txt_shooting_cycle.setVisibility(View.VISIBLE);
//                   lbl_shooting_cycles.setVisibility(View.VISIBLE);
//                   button_shooting_cyclesMinus.setVisibility(View.VISIBLE);
//                   button_shooting_cyclesPlus.setVisibility(View.VISIBLE);

               }
               else
               {
                   //not checked
                   Log.i(TAG,"TextBox is unchecked.");
                   seekBar_LowGoal_Teleop.setEnabled(false);
                   seekBar_LowGoal_Teleop.setVisibility(View.GONE);
                   txt_seekBarLGvalue.setVisibility(View.GONE);
                   tele_lg = false;

//                   txt_shooting_cycle.setVisibility(View.GONE);
//                   lbl_shooting_cycles.setVisibility(View.GONE);
//                   button_shooting_cyclesMinus.setVisibility(View.GONE);
//                   button_shooting_cyclesPlus.setVisibility(View.GONE);

               }
           }
       }
        );

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
        Pearadox.Match_Data.setTele_gears_placed(tele_gears_placed);
        Pearadox.Match_Data.setTele_gears_attempt(tele_gears_attempt);
        Pearadox.Match_Data.setTele_hg(tele_hg);
        Pearadox.Match_Data.setTele_hg_percent(tele_hg_percent);
        Pearadox.Match_Data.setTele_lg(tele_lg);
        Pearadox.Match_Data.setTele_lg_percent(tele_lg_percent);
        Pearadox.Match_Data.setTele_cycles(tele_cycles);
        Pearadox.Match_Data.setTele_touch_act(tele_touch_act);
        Pearadox.Match_Data.setTele_climb_attempt(tele_climb_attempt);
        Pearadox.Match_Data.setTele_climb_success(tele_climb_success);
        Pearadox.Match_Data.setTele_gear_pickup(tele_gear_pickup);
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

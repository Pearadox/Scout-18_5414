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
    TextView txt_dev, txt_stud, txt_match, txt_MyTeam, lbl_GearNUMT, lbl_GearsAttempted, txt_seekBarHGvalue, txt_seekBarLGvalue;
    private Button button_GoToFinalActivity,button_GearPlacedT, button_GearPlacedTPlus, button_GearAttemptedP, button_GearAttemptedM;
    CheckBox chk_climbsuccessful, chk_climbfailed, chk_touchpad, chk_touchpadpts, chkBox_highGoal, chkBox_lowGoal;
    SeekBar seekBar_HighGoal_Teleop, seekBar_LowGoal_Teleop;
    EditText editText_TeleComments;
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfTeam_DBReference;
    private DatabaseReference pfMatch_DBReference;
    private DatabaseReference pfDevice_DBReference;
    private DatabaseReference pfCur_Match_DBReference;
    String key = null;

    // ===================  TeleOps Elements for Match Scout Data object ===================
    int gearNumT = 0;                   // # Gears placed
    int gearNumA = 0;                   // # Gears attempted
    int seekbarvalueHigh = 0;
    int seekbarvalueLow = 0;
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
        Log.d(TAG, param1 + " " + param2);      // ** DEBUG **

        editText_TeleComments = (EditText) findViewById(R.id.editText_teleComments);
        button_GoToFinalActivity = (Button) findViewById(R.id.button_GoToFinalActivity);
        button_GearPlacedT= (Button) findViewById(R.id.button_GearPlacedT);
        button_GearPlacedTPlus = (Button) findViewById(R.id.button_GearPlacedTPlus);
        lbl_GearNUMT = (TextView) findViewById(R.id.lbl_GearNUMT);
        lbl_GearNUMT.setText(Integer.toString(gearNumT));
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
        chk_climbfailed = (CheckBox) findViewById(R.id.chk_climbfailed);
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


        lbl_GearsAttempted.setText(Integer.toString(gearNumA));

        button_GearAttemptedP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (gearNumA < 50) {
                    gearNumA++;
                }
                Log.d(TAG, "Gears = " + gearNumA);      // ** DEBUG **
                lbl_GearsAttempted.setText(Integer.toString(gearNumA));    // Perform action on click
            }
        });
        button_GearAttemptedM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (gearNumA >= 1) {
                    gearNumA--;
                }
                Log.d(TAG, "Gears = " + gearNumA);      // ** DEBUG **
                lbl_GearsAttempted.setText(Integer.toString(gearNumA));    // Perform action on click
            }
        });

        button_GearPlacedTPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (gearNumT < 12) {
                    gearNumT++;
                }
                Log.d(TAG, "Gears = " + gearNumT);      // ** DEBUG **
                lbl_GearNUMT.setText(Integer.toString(gearNumT));    // Perform action on click
            }
        });
        button_GearPlacedT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (gearNumT >= 1) {
                    gearNumT--;
                }
                Log.d(TAG, "Gears = " + gearNumT);      // ** DEBUG **
                lbl_GearNUMT.setText(Integer.toString(gearNumT));    // Perform action on click
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

        chk_climbfailed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
               Log.i(TAG, "chk_climbfailed Listener");
                if (buttonView.isChecked()) {
                    //checked
                    Log.i(TAG,"TextBox is checked.");

                }
                else
                {
                    //not checked
                    Log.i(TAG,"TextBox is unchecked.");

                }
                if (buttonView.isChecked()) {
                    //checked
                    chk_climbsuccessful.setEnabled(false);
                }
                else
                {
                    //not checked
                    chk_climbsuccessful.setEnabled(true);

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

                }
                else
                {
                    //not checked
                    Log.i(TAG,"TextBox is unchecked.");

                }
                if (buttonView.isChecked()) {
                    //checked
                    chk_climbfailed.setEnabled(false);
                }
                else
                {
                    //not checked
                    chk_climbfailed.setEnabled(true);

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

            }
            else
            {
                //not checked
                Log.i(TAG,"TextBox is unchecked.");

            }
            }
        }
        );
        chk_touchpadpts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.i(TAG, "chkBox_gears Listener");
                if (buttonView.isChecked()) {
                    //checked
                    Log.i(TAG,"TextBox is checked.");

                }
                else
                {
                    //not checked
                    Log.i(TAG,"TextBox is unchecked.");

                }
            }
        }
        );
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

                seekbarvalueHigh=progress;	//we can use the progress value of pro as anywhere
                txt_seekBarHGvalue.setText(Integer.toString(seekbarvalueHigh));
            }

        });

        chkBox_highGoal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.i(TAG, "chkBox_highGoal Listener");
            if (buttonView.isChecked()) {
                //checked
                Log.i(TAG,"TextBox is checked.");

            }
            else
            {
                //not checked
                Log.i(TAG,"TextBox is unchecked.");

            }
            if (buttonView.isChecked()) {
                //checked
                seekBar_HighGoal_Teleop.setEnabled(true);
                seekBar_HighGoal_Teleop.setVisibility(View.VISIBLE);
                txt_seekBarHGvalue.setVisibility(View.VISIBLE);

            }
            else
            {
                //not checked
                seekBar_HighGoal_Teleop.setEnabled(false);
                seekBar_HighGoal_Teleop.setVisibility(View.GONE);
                txt_seekBarHGvalue.setVisibility(View.GONE);

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

                seekbarvalueLow=progress;	//we can use the progress value of pro as anywhere
                txt_seekBarLGvalue.setText(Integer.toString(seekbarvalueLow));
            }

        });
        chkBox_lowGoal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

           @Override
           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
               Log.i(TAG, "chkBox_lowGoal Listener");
               if (buttonView.isChecked()) {
                   //checked
                   Log.i(TAG,"TextBox is checked.");

               }
               else
               {
                   //not checked
                   Log.i(TAG,"TextBox is unchecked.");

               }
               if (buttonView.isChecked()) {
                   //checked
                   seekBar_LowGoal_Teleop.setEnabled(true);
                   seekBar_LowGoal_Teleop.setVisibility(View.VISIBLE);
                   txt_seekBarLGvalue.setVisibility(View.VISIBLE);

               }
               else
               {
                   //not checked
                   seekBar_LowGoal_Teleop.setEnabled(false);
                   seekBar_LowGoal_Teleop.setVisibility(View.GONE);
                   txt_seekBarLGvalue.setVisibility(View.GONE);

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
        Pearadox.Match_Data.setTele_gears_placed(gearNumT);
        Pearadox.Match_Data.setTele_gears_attempt(gearNumA);
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
                Log.d(TAG, "DEV = NULL" );
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
            // ToDo - Clear all data back to priginal settings
            Log.d(TAG, "#### Data was saved in Final #### ");
            Toast.makeText(getBaseContext(), "Data was saved in Final - probably should Exit", Toast.LENGTH_LONG).show();

            //                finish();       // Exit

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

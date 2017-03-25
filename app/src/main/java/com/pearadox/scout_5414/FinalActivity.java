package com.pearadox.scout_5414;

import android.app.Dialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
//import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.Date;

import static com.pearadox.scout_5414.R.id.radgrp_Scout;
import static com.pearadox.scout_5414.R.id.radioButton_def_bad;
import static com.pearadox.scout_5414.R.id.radioGroup_defense;

//import static com.pearadox.scout_5414.R.id.button_GoToFinalActivity;
//import static com.pearadox.scout_5414.R.id.rdBtn_def_good;
//import static com.pearadox.scout_5414.R.id.radioGroup_defense;

/**
 * Created by mlm.02000 on 2/5/2017.
 */

public class FinalActivity extends Activity {

    String TAG = "FinalActivity";      // This CLASS name
    TextView txt_dev, txt_stud, txt_match, txt_MyTeam, lbl_Number_Penalties, txt_robotnum;
    EditText editText_Comments;
    CheckBox chk_lostPart, chk_lostComm, chk_block, chk_starve, chk_dump, chkBox_final_def_gear;
    Button button_Saved, button_Number_PenaltiesPlus, button_Number_PenaltiesUndo;
    RadioGroup radioGroup_defense;
    RadioButton rdBtn_def_good, radioButton_def_bad;
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfTeam_DBReference;
    private DatabaseReference pfMatch_DBReference;
    private DatabaseReference pfDevice_DBReference;
    private DatabaseReference pfCur_Match_DBReference;
    private DatabaseReference pfMatchData_DBReference;
    String key = null;
    String tn = "";

// ===================  Final Elements for Match Scout Data object ===================
    // ToDo - add any remaining FINAL elements
    public boolean lost_Parts = false;                          // Did they lose parts?
    public boolean lost_Comms = false;                          // Did they lose communication?
    public boolean final_defense_good = false;                  // Was their overall Defense Good (bad = false)?
    public boolean final_def_Lane = false;                      // Did they use Lane Defense?
    public boolean final_def_Block = false;                     // Did they use Blocking Defense?
    public boolean final_def_Hopper = false;                    // Did they use Dump Defense (unload hoppers)?
    public boolean final_def_Gear = false;                      // Did they Block Access to Gear Placement?
    public int final_num_Penalties = 0;                         // How many penalties received?
    public String final_studID = "";


    /* */
    public String finalComment = " ";
    public static String currentDateTimeString = " ";


// ===========================================================================


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "<< Other Activity >>");
        setContentView(R.layout.activity_final);
        Bundle bundle = this.getIntent().getExtras();
        pfDatabase = FirebaseDatabase.getInstance();
//        pfTeam_DBReference = pfDatabase.getReference("teams");              // Tteam data from Firebase D/B
//        pfStudent_DBReference = pfDatabase.getReference("students");        // List of Students
//        pfDevice_DBReference = pfDatabase.getReference("devices");          // List of Students
//        pfMatch_DBReference = pfDatabase.getReference("matches");           // List of Students
//        pfCur_Match_DBReference = pfDatabase.getReference("current-match"); // _THE_ current Match
        pfDevice_DBReference = pfDatabase.getReference("devices");              // List of Students
        pfMatchData_DBReference = pfDatabase.getReference("match-data/" + Pearadox.FRC_Event);    // Match Data


        tn = bundle.getString("tnum");
        Log.w(TAG, tn);      // ** DEBUG **


        txt_robotnum = (TextView) findViewById(R.id.txt_robotnum);
        txt_robotnum.setText(tn);

        //TODO create field for firebase!!!!
        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        Log.w(TAG, currentDateTimeString);

        lbl_Number_Penalties = (TextView) findViewById(R.id.lbl_Number_Penalties);
        radioButton_def_bad = (RadioButton) findViewById(R.id.radioButton_def_bad);
        rdBtn_def_good = (RadioButton) findViewById(R.id.rdBtn_def_good);
        radioGroup_defense = (RadioGroup) findViewById(R.id.radioGroup_defense);
        chkBox_final_def_gear = (CheckBox) findViewById(R.id.chkBox_final_def_gear);
        chk_lostPart = (CheckBox) findViewById(R.id.chk_lostPart);
        chk_lostPart.requestFocus();        // Don't let EditText mess up layout!!
        chk_lostComm = (CheckBox) findViewById(R.id.chk_lostComm);
        chk_block = (CheckBox) findViewById(R.id.chk_block);
        chk_starve = (CheckBox) findViewById(R.id.chk_starve);
        chk_dump = (CheckBox) findViewById(R.id.chk_dump);
        editText_Comments = (EditText) findViewById(R.id.editText_Comments);
        editText_Comments.setClickable(true);
        button_Number_PenaltiesPlus = (Button) findViewById(R.id.button_Number_PenaltiesPlus);
        button_Number_PenaltiesUndo = (Button) findViewById(R.id.button_Number_PenaltiesUndo);
        button_Saved = (Button) findViewById(R.id.button_Saved);
        button_Saved.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                updateDev("Saved");         // Update "traffic light" status for Scout Master
                storeFinalData();           // Put all the Final data collected in Match object
                Pearadox.MatchData_Saved = true;    // Set flag to show saved
                // ToDo - Clear all data back to original settings

                finish();       // Exit

            }
        });


        button_Number_PenaltiesPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final_num_Penalties++;

                Log.w(TAG, "Gears = " + final_num_Penalties);      // ** DEBUG **
                lbl_Number_Penalties.setText(Integer.toString(final_num_Penalties));    // Perform action on click
            }
        });
        button_Number_PenaltiesUndo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (final_num_Penalties >= 1) {
                    final_num_Penalties--;
                }
                Log.w(TAG, "Gears = " + final_num_Penalties);      // ** DEBUG **
                lbl_Number_Penalties.setText(Integer.toString(final_num_Penalties));    // Perform action on click
            }
        });

        editText_Comments.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "******  onTextChanged TextWatcher  ******" + s);
                finalComment = String.valueOf(s);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, "******  beforeTextChanged TextWatcher  ******");
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "******  onTextChanged TextWatcher  ******" + s );
                finalComment = String.valueOf(s);
            }
        });
        chk_lostPart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.i(TAG, "chk_lostPart Listener");
                if (buttonView.isChecked()) {
                    //checked
                    Log.i(TAG,"TextBox is checked.");
                    lost_Parts = true;
                    Log.d(TAG,"Lost Parts = " + lost_Parts);
                }
                else
                {
                    //not checked
                    Log.i(TAG,"TextBox is unchecked.");
                    lost_Parts = false;
                    Log.d(TAG, "Lost Parts = " + lost_Parts);

                }
            }
        }
        );
        chk_lostComm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.i(TAG, "chk_lostComm Listener");
                if (buttonView.isChecked()) {
                    //checked
                    Log.i(TAG,"TextBox is checked.");
                    lost_Comms = true;

                }
                else
                {
                    //not checked
                    Log.i(TAG,"TextBox is unchecked.");
                    lost_Comms = false;

                }
            }
        }
        );
        chk_starve.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.i(TAG, "chk_starve Listener");
                if (buttonView.isChecked()) {
                    //checked
                    Log.i(TAG,"TextBox is checked.");
                    final_def_Lane = true;

                }
                else
                {
                    //not checked
                    Log.i(TAG,"TextBox is unchecked.");
                    final_def_Lane = false;

                }
            }
        }
        );
        chk_block.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.i(TAG, "chk_block Listener");
                if (buttonView.isChecked()) {
                    //checked
                    Log.i(TAG,"TextBox is checked.");
                    final_def_Block = true;

                }
                else
                {
                    //not checked
                    Log.i(TAG,"TextBox is unchecked.");
                    final_def_Block = false;

                }
            }
        }
        );
        chk_dump.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.i(TAG, "chk_dump Listener");
                if (buttonView.isChecked()) {
                    //checked
                    Log.i(TAG,"TextBox is checked.");
                    final_def_Hopper = true;

                }
                else
                {
                    //not checked
                    Log.i(TAG,"TextBox is unchecked.");
                    final_def_Hopper = false;

                }
            }
        }
        );

        chkBox_final_def_gear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.i(TAG, "chkBox_final_def_gear Listener");
            if (buttonView.isChecked()) {
                //checked
                Log.i(TAG,"TextBox is checked.");
                final_def_Gear = true;

            }
            else
            {
                //not checked
                Log.i(TAG,"TextBox is unchecked.");
                final_def_Gear = false;

            }
            }
        }
        );

    }
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void storeFinalData() {
        Log.i(TAG, ">>>>  storeFinalData  <<<<");
        Log.w(TAG, currentDateTimeString + " is the current date and time.");
        Pearadox.Match_Data.setFinal_lostParts(lost_Parts);
        Pearadox.Match_Data.setFinal_lostComms(lost_Comms);
        Pearadox.Match_Data.setFinal_defense_good(final_defense_good);
        Pearadox.Match_Data.setFinal_def_Lane(final_def_Lane);
        Pearadox.Match_Data.setFinal_def_Block(final_def_Block);
        Pearadox.Match_Data.setFinal_def_Hopper(final_def_Hopper);
        Pearadox.Match_Data.setFinal_def_Gear(final_def_Gear);
        Pearadox.Match_Data.setFinal_num_Penalties(final_num_Penalties);

        Pearadox.Match_Data.setFinal_dateTime(currentDateTimeString);

        //ToDo - add remaining Final elements
         /* */
//        Pearadox.Match_Data.setFinal_????? = lost_Parts;
         /* Lost Comms*/
         /* Defense checkboxes*/
         /* ???*/

         /* */
        Pearadox.Match_Data.setFinal_comment(finalComment);

        saveDatatoSDcard();     //Save locally

        String keyID = Pearadox.Match_Data.getMatch() + "-" + Pearadox.Match_Data.getTeam_num();
        pfMatchData_DBReference.child(keyID).setValue(Pearadox.Match_Data);
    }

    private void saveDatatoSDcard() {
        Log.i(TAG, "@@@@  saveDatatoSDcard  @@@@");
        String filename = Pearadox.Match_Data.getMatch() + "-" + Pearadox.Match_Data.getTeam_num() + ".dat";
        ObjectOutput out = null;
        File directMatch = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/match/" + Pearadox.FRC_Event + "/" + filename);
        Log.d(TAG, "SD card Path = " + directMatch);
        if(directMatch.exists())  {
            // Todo - Replace TOAST with Dialog Box  - "Do you really ..."
            Toast toast = Toast.makeText(getBaseContext(), "Data for " + filename + " already exists!!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }

        try {
            out = new ObjectOutputStream(new FileOutputStream(directMatch));
            out.writeObject(Pearadox.Match_Data);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void RadioClick_Defense(View view) {
        Log.w(TAG, "@@ RadioClick_Scout @@");
        radioGroup_defense = (RadioGroup) findViewById(R.id.radioGroup_defense);
        int selectedId = radioGroup_defense.getCheckedRadioButtonId();
        rdBtn_def_good = (RadioButton) findViewById(selectedId);
        String value = rdBtn_def_good.getText().toString();
        Log.w(TAG, "RadioDefnse - Button '" + value + "'");
        if (value.equals("Good")) { 	    // Match?
            Log.w(TAG, "Good Defense");
            final_defense_good = true;
        } else {                               // Pit
            Log.w(TAG, "Bad Defense");
            final_defense_good = false;
        }
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



//###################################################################//
//###################################################################//
//###################################################################//
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
        Log.v(TAG, "OnDestroy");

    }

}


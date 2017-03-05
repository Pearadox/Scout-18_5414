package com.pearadox.scout_5414;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
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

import static com.pearadox.scout_5414.R.id.button_GoToFinalActivity;

/**
 * Created by mlm.02000 on 2/5/2017.
 */

public class FinalActivity extends Activity {

    String TAG = "FinalActivity";      // This CLASS name
    TextView txt_dev, txt_stud, txt_match, txt_MyTeam;
    EditText editText_Comments;
    CheckBox chk_lostPart, chk_lostComm, chk_block, chk_starve, chk_dump;
    Button button_Saved;
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfTeam_DBReference;
    private DatabaseReference pfMatch_DBReference;
    private DatabaseReference pfDevice_DBReference;
    private DatabaseReference pfCur_Match_DBReference;
    private DatabaseReference pfMatchData_DBReference;
    String key = null;

// ===================  Final Elements for Match Scout Data object ===================
    // ToDo - add any remaining FINAL elements
    public boolean lost_Parts = false;
    public boolean lost_Comms = false;
    /* */
    public String finalComment = " ";

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

        String param1 = bundle.getString("dev");
        String param2 = bundle.getString("stud");
        Log.d(TAG, param1 + " " + param2);      // ** DEBUG **

        chk_lostPart = (CheckBox) findViewById(R.id.chk_lostPart);
        chk_lostPart.requestFocus();        // Don't let EditText mess up layout!!
        chk_lostComm = (CheckBox) findViewById(R.id.chk_lostComm);
        chk_block = (CheckBox) findViewById(R.id.chk_block);
        chk_starve = (CheckBox) findViewById(R.id.chk_starve);
        chk_dump = (CheckBox) findViewById(R.id.chk_dump);
        editText_Comments = (EditText) findViewById(R.id.editText_Comments);
        editText_Comments.setClickable(true);
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


    }
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void storeFinalData() {
        Log.i(TAG, ">>>>  storeFinalData  <<<<");
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
        String filename = Pearadox.Match_Data.getMatch() + "_" + Pearadox.Match_Data.getTeam_num() + ".dat";
        ObjectOutput out = null;
        File directMatch = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/match/" + filename);
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
        Log.v(TAG, "OnDestroy");

    }

}

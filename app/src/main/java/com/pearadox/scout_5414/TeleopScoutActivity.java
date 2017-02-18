package com.pearadox.scout_5414;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.pearadox.scout_5414.R.id.button_GoToFinalActivity;

/**
 * Created by mlm.02000 on 2/5/2017.
 */

public class TeleopScoutActivity extends Activity {


    String TAG = "TeleopScoutActivity";      // This CLASS name
    TextView txt_dev, txt_stud, txt_match, txt_MyTeam, lbl_GearNUMT;
    private Button button_GoToFinalActivity,button_GearPlacedT, button_GearPlacedTPlus;
    int gearNumT = 0;
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfTeam_DBReference;
    private DatabaseReference pfMatch_DBReference;
    private DatabaseReference pfDevice_DBReference;
    private DatabaseReference pfCur_Match_DBReference;
    String key = null;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "<< Teleop Scout >>");
        setContentView(R.layout.activity_teleop_scout);
        Bundle bundle = this.getIntent().getExtras();


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
                Log.i(TAG, "Clicked Comments");

                updateDev("Final");

                Intent smast_intent = new Intent(TeleopScoutActivity.this, FinalActivity.class);
                Bundle SMbundle = new Bundle();
                smast_intent.putExtras(SMbundle);
                startActivity(smast_intent);
            }
        });


        String param1 = bundle.getString("dev");
        String param2 = bundle.getString("stud");
        Log.d(TAG, param1 + " " + param2);      // ** DEBUG **
        Log.i(TAG, "<< before calling Other Activty >>");

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

package com.pearadox.scout_5414;

import android.icu.text.DecimalFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.actions.ItemListIntents;

import org.w3c.dom.Text;

public class VisMatch_Activity extends AppCompatActivity {
    String TAG = "VisMatch_Activity";        // This CLASS name
    String tnum = "";
    String tname = "";
    String underScore = new String(new char[60]).replace("\0", "_");  // string of 'x' underscores
    TextView txt_team, txt_teamName, txt_auto_HGpercent, txt_auto_LGpercent, txt_auto_gearRatio, txt_auto_baselineRatio, txt_tele_gearRatio, txt_climbAttempts, txt_successfulClimbs;
    /* Comment Boxes */     TextView txt_AutoComments, txt_TeleComments, txt_FinalComments;
    /* Labels */    TextView lbl_Autonomous, lbl_autoHGshootingPercent, lbl_autoLGshootingPercent, lbl_tele_gearRatio, lbl_climbAttempts, lbl_successfulClimbs;
    //----------------------------------
    int auto_HGtotalShooting = 0;
    int auto_LGtotalShooting = 0;
    int auto_totalgearsAttempted = 0;
    int auto_totalgearsPlaced = 0;
    String auto_collectedBalls = "";
    String auto_Comments = "";
    //----------------------------------
    int tele_HGtotalShooting = 0;
    int tele_LGtotalShooting = 0;
    int tele_totalGearsAttempted = 0;
    int tele_totalGearsPlaced = 0;
    String tele_Comments = "";
    //----------------------------------
    String final_Comments = "";
    //----------------------------------

    matchData match_inst = new matchData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis_match);
        Log.i(TAG, "@@@@  VisMatch_Activity started  @@@@");
        Bundle bundle = this.getIntent().getExtras();
        String param1 = bundle.getString("team");
        String param2 = bundle.getString("name");
        Log.w(TAG, param1);      // ** DEBUG **
        tnum = param1;      // Save Team #
        Log.w(TAG, param2);      // ** DEBUG **
        tname = param2;      // Save Team #


        txt_team = (TextView) findViewById(R.id.txt_team);
        txt_teamName = (TextView) findViewById(R.id.txt_teamName);
        txt_auto_HGpercent = (TextView) findViewById(R.id.txt_auto_HGpercent);
        txt_auto_LGpercent = (TextView) findViewById(R.id.txt_auto_LGpercent);
        txt_auto_gearRatio = (TextView) findViewById(R.id.txt_auto_gearRatio);
        txt_AutoComments = (TextView) findViewById(R.id.txt_AutoComments);
        txt_TeleComments = (TextView) findViewById(R.id.txt_TeleComments);
        txt_FinalComments = (TextView) findViewById(R.id.txt_FinalComments);
        txt_auto_baselineRatio = (TextView) findViewById(R.id.txt_auto_baselineRatio);
        txt_tele_gearRatio = (TextView) findViewById(R.id.txt_tele_gearRatio);
        txt_climbAttempts = (TextView) findViewById(R.id.txt_climbAttempts);
        txt_successfulClimbs = (TextView) findViewById(R.id.txt_successfulClimbs);
        txt_AutoComments.setMovementMethod(new ScrollingMovementMethod());
        txt_TeleComments.setMovementMethod(new ScrollingMovementMethod());
        txt_FinalComments.setMovementMethod(new ScrollingMovementMethod());
        txt_team.setText(tnum);
        txt_teamName.setText(tname);    // Get real

        int numObjects = Pearadox.Matches_Data.size();
        Log.w(TAG, "Objects = " + numObjects);
        int numAutoHG = 0;
        int numAutoLG = 0;
        int numAutoBaseline = 0;
        int numTeleClimbAttempt = 0;
        int numTeleClimbSuccess = 0;

        auto_HGtotalShooting = 0; auto_LGtotalShooting = 0; tele_HGtotalShooting = 0; tele_LGtotalShooting = 0; auto_totalgearsAttempted = 0; auto_totalgearsPlaced = 0; tele_totalGearsAttempted = 0; tele_totalGearsPlaced = 0; numAutoBaseline = 0;
        auto_Comments = ""; tele_Comments = ""; final_Comments="";

        for (int i = 0; i < numObjects; i++) {
            Log.w(TAG, "In for loop!   " + i);
            match_inst = Pearadox.Matches_Data.get(i);

            if (match_inst.isAuto_baseline()) {
                numAutoBaseline++;
                Log.w(TAG, "Auto Baseline = " + match_inst.isAuto_baseline());
                Log.w(TAG, "Auto Baseline Number= " + numAutoBaseline);
            }

            Log.w(TAG, "Auto HG Boolean = " + match_inst.isAuto_hg());
            if (match_inst.isAuto_hg()) {
                auto_HGtotalShooting = auto_HGtotalShooting + match_inst.getAuto_hg_percent();
                numAutoHG++;
                Log.w(TAG, "numAutoHG = " + numAutoHG);
            }
            Log.w(TAG, "Auto HG = " + match_inst.getAuto_hg_percent());

            if (match_inst.isAuto_lg()) {       /* Matthew - <<<<<<<<<<<<<<<<  match_inst _NOT_ Match_Data */
                auto_LGtotalShooting = auto_LGtotalShooting + match_inst.getAuto_lg_percent();
                numAutoLG++;
                Log.w(TAG, "numAutoLG = " + numAutoLG);

            }
            Log.w(TAG, "Auto LG = " + match_inst.getAuto_lg_percent());
//            Log.w(TAG, "Auto LG Boolean = " + match_inst.isAuto_lg());


            auto_totalgearsPlaced = auto_totalgearsPlaced + match_inst.getAuto_gears_placed();
            Log.w(TAG, "Auto Gears Placed = " + match_inst.getAuto_gears_placed());

            auto_totalgearsAttempted = auto_totalgearsAttempted + match_inst.getAuto_gears_attempt();
            Log.w(TAG, "Auto Gears Attempted = " + match_inst.getAuto_gears_attempt());

            Log.w(TAG, "Auto Comment = " + match_inst.getAuto_comment() + "  " + match_inst.getAuto_comment().length());
            if (match_inst.getAuto_comment().length() > 1) {
                auto_Comments = auto_Comments + match_inst.getMatch() + "-" + match_inst.getAuto_comment() + "\n" + underScore  + "\n" ;
            }

            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2
            tele_HGtotalShooting = tele_HGtotalShooting + match_inst.getTele_hg_percent();
            tele_LGtotalShooting = tele_LGtotalShooting + match_inst.getTele_lg_percent();

            tele_totalGearsPlaced = tele_totalGearsPlaced + match_inst.getTele_gears_placed();
            Log.w(TAG, "Tele Gears Placed = " + match_inst.getTele_gears_placed());
            tele_totalGearsAttempted = tele_totalGearsAttempted + match_inst.getTele_gears_attempt();
            Log.w(TAG, "Tele Gears Attempted = " + match_inst.getTele_gears_attempt());

            if (match_inst.isTele_climb_attempt()) {
                numTeleClimbAttempt++;
                Log.w(TAG, "Tele Climb Attempt = " + match_inst.isTele_climb_attempt());
                Log.w(TAG, "Tele Climb Attempt Number= " + numTeleClimbAttempt);
            }
            if (match_inst.isTele_climb_success()) {
                numTeleClimbSuccess++;
                Log.w(TAG, "Tele Climb Success = " + match_inst.isTele_climb_success());
                Log.w(TAG, "Tele Climb Success Number= " + numTeleClimbSuccess);
            }

            Log.w(TAG, "Tele Comment = " + match_inst.getTele_comment() + "  " + match_inst.getTele_comment().length());
            if (match_inst.getTele_comment().length() > 1) {
                tele_Comments = tele_Comments + match_inst.getMatch() + "-" + match_inst.getTele_comment() + "\n" + underScore  + "\n" ;
            }

            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2
            // ToDo - collect Final elements

            Log.w(TAG, "Final Comment = " + match_inst.getFinal_comment() + "  " + match_inst.getFinal_comment().length());
            if (match_inst.getFinal_comment().length() > 1) {
                final_Comments = final_Comments + match_inst.getMatch() + "-" + match_inst.getFinal_comment() + "\n" + underScore  + "\n" ;
            }

        } //end For
        Log.w(TAG, "Number of Attempted Climbs = " + numTeleClimbAttempt);
        Log.w(TAG, "Number of Successful Climbs = " + numTeleClimbSuccess);
// ======  Now start displaying all the data we collected  ========
        Log.w(TAG, "Auto HG Shooting = " + auto_HGtotalShooting + "   " + numAutoHG);
        if (numAutoHG > 0) {      // Don't divide by zero!!
            float auto_HGPer = ((float)auto_HGtotalShooting) / numAutoHG;
            Log.w(TAG, "Percentage of HG Shooting in Auto = " + ((float)auto_HGtotalShooting) / numAutoHG);
            txt_auto_HGpercent.setText(String.format("%3.2f", (auto_HGPer)) + "%");
        } else {
            txt_auto_HGpercent.setText(" ");
        }

        Log.w(TAG, "Auto LG Shooting = " + auto_LGtotalShooting + "   " + numAutoLG);
        if (numAutoLG > 0) {      // Don't divide by zero!!
            float auto_LGPer = ((float)auto_LGtotalShooting) / numAutoLG;
            Log.w(TAG, "Percentage of LG Shooting in Auto = " + ((float)auto_LGtotalShooting) / numAutoLG);
            txt_auto_LGpercent.setText(String.format("%3.2f",(auto_LGPer)) + "%");
        } else {
            txt_auto_LGpercent.setText("");
        }

        Log.w(TAG, "Ratio of Placed to Attempted Gears in Auto = " + auto_totalgearsPlaced + "/" + auto_totalgearsAttempted);
        txt_auto_gearRatio.setText(auto_totalgearsPlaced + "/" + auto_totalgearsAttempted);

        Log.w(TAG, "Ratio of Crossing Baseline to Number of Matches = " + numAutoBaseline + "/" + numObjects);
        txt_auto_baselineRatio.setText(numAutoBaseline +  "/" + numObjects);

//        Log.w(TAG, "Auto Gears Attempted = " + auto_gearsAttempted);
//
//        Log.w(TAG, "Auto Gears Placed = " + auto_gearsPlaced);


        txt_AutoComments.setText(auto_Comments);

        // ==============================================
        // ToDo - display Tele elements
        Log.w(TAG, "Ratio of Placed to Attempted Gears in Tele = " + tele_totalGearsPlaced + "/" + tele_totalGearsAttempted);
        txt_tele_gearRatio.setText(tele_totalGearsPlaced + "/" + tele_totalGearsAttempted);
        txt_climbAttempts.setText(numTeleClimbAttempt + "/" + numObjects);
        txt_successfulClimbs.setText(numTeleClimbSuccess + "/" + numObjects);

        txt_TeleComments.setText(tele_Comments);

        // ==============================================
        // ToDo - display Final elements

        txt_FinalComments.setText(final_Comments);
    }

    //###################################################################
//###################################################################
//###################################################################
    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }
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

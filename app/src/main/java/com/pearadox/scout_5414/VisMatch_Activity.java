package com.pearadox.scout_5414;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

public class VisMatch_Activity extends AppCompatActivity {
    String TAG = "VisMatch_Activity";        // This CLASS name
    String tnum = "";
    String tname = "";
    String underScore = new String(new char[60]).replace("\0", "_");  // string of 'x' underscores
    String matches = "";
    TextView txt_team, txt_teamName, txt_NumMatches, txt_Matches;
    TextView txt_auto_HGpercent, txt_auto_LGpercent, txt_auto_gearRatio, txt_auto_baselineRatio, txt_tele_gearRatio, txt_climbAttempts, txt_successfulClimbs;
    /* Comment Boxes */     TextView txt_AutoComments, txt_TeleComments, txt_FinalComments;
    TextView txt_spSi, txt_spSo, txt_spM;
    TextView txt_final_LostComm, txt_final_LostParts, txt_final_DefGood, txt_final_DefBlock,txt_final_DefDump, txt_final_DefStarve, txt_final_NumPen;
    /* Labels */    TextView lbl_Autonomous, lbl_autoHGshootingPercent, lbl_autoLGshootingPercent, lbl_tele_gearRatio, lbl_climbAttempts, lbl_successfulClimbs;
    //----------------------------------
    int auto_HGtotalShooting = 0;
    int auto_LGtotalShooting = 0;
    int auto_totalgearsAttempted = 0;
    int auto_totalgearsPlaced = 0;
    int auto_B1 = 0; int auto_B2 = 0; int auto_B3 = 0; int auto_B4 = 0; int auto_B5 = 0;
    int auto_gP1 = 0; int auto_gP2 = 0; int auto_gP3 = 0;
    String auto_collectedBalls = "";
    String auto_Comments = "";
    //----------------------------------
    int tele_HGtotalShooting = 0;
    int tele_LGtotalShooting = 0;
    int tele_totalGearsAttempted = 0;
    int tele_totalGearsPlaced = 0;
    String tele_Comments = "";
    //----------------------------------
    int final_LostComm = 0; int final_LostParts = 0; int final_DefGood = 0; int final_DefBlock = 0;  int final_DefDump = 0; int final_DefStarve = 0; int final_NumPen = 0;
    //        TextView txt_final_LostComm, txt_final_LostParts, txt_final_DefGood, txt_final_DefBlock,txt_final_DefDump, txt_final_DefStarve, txt_final_NumPen;
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


        txt_Matches = (TextView) findViewById(R.id.txt_Matches);
        txt_team = (TextView) findViewById(R.id.txt_team);
        txt_teamName = (TextView) findViewById(R.id.txt_teamName);
        txt_NumMatches = (TextView) findViewById(R.id.txt_NumMatches);
        txt_auto_gearRatio = (TextView) findViewById(R.id.txt_auto_gearRatio);
        txt_spSi = (TextView) findViewById(R.id.txt_spSi);
        txt_spSo = (TextView) findViewById(R.id.txt_spSo);
        txt_spM = (TextView) findViewById(R.id.txt_spM);
        txt_AutoComments = (TextView) findViewById(R.id.txt_AutoComments);
        txt_TeleComments = (TextView) findViewById(R.id.txt_TeleComments);
        txt_FinalComments = (TextView) findViewById(R.id.txt_FinalComments);
        txt_AutoComments.setMovementMethod(new ScrollingMovementMethod());
        txt_TeleComments.setMovementMethod(new ScrollingMovementMethod());
        txt_FinalComments.setMovementMethod(new ScrollingMovementMethod());
        txt_auto_baselineRatio = (TextView) findViewById(R.id.txt_auto_baselineRatio);
        txt_tele_gearRatio = (TextView) findViewById(R.id.txt_tele_gearRatio);
        txt_climbAttempts = (TextView) findViewById(R.id.txt_climbAttempts);
        txt_successfulClimbs = (TextView) findViewById(R.id.txt_successfulClimbs);

        txt_final_LostComm = (TextView) findViewById(R.id.txt_final_LostComm);
        txt_final_LostParts = (TextView) findViewById(R.id.txt_final_LostParts);
        txt_final_DefGood = (TextView) findViewById(R.id.txt_final_DefGood);
        txt_final_DefBlock = (TextView) findViewById(R.id.txt_final_DefBlock);
        txt_final_DefDump = (TextView) findViewById(R.id.txt_final_DefDump);
        txt_final_DefStarve = (TextView) findViewById(R.id.txt_final_DefStarve);
        txt_final_NumPen = (TextView) findViewById(R.id.txt_final_NumPen);

        txt_team.setText(tnum);
        txt_teamName.setText(tname);    // Get real

        int numObjects = Pearadox.Matches_Data.size();
        Log.w(TAG, "Objects = " + numObjects);
        txt_NumMatches.setText(String.valueOf(numObjects));
        int numAutoBaseline = 0;
        int numTeleClimbAttempt = 0;
        int numTeleClimbSuccess = 0;

        auto_totalgearsAttempted = 0; auto_totalgearsPlaced = 0; tele_totalGearsAttempted = 0; tele_totalGearsPlaced = 0; numAutoBaseline = 0;
        auto_B1 = 0; auto_B2 = 0; auto_B3 = 0;
        auto_Comments = ""; tele_Comments = ""; final_Comments=""; matches = "";
        final_LostComm = 0; final_LostParts = 0; final_DefGood = 0; final_DefBlock = 0;  final_DefDump = 0; final_DefStarve = 0; final_NumPen = 0;


// ================================================================
        for (int i = 0; i < numObjects; i++) {
            Log.w(TAG, "In for loop!   " + i);
            match_inst = Pearadox.Matches_Data.get(i);      // Get instance of Match Data
            matches = matches + match_inst.getMatch() + "  ";

            if (match_inst.isAuto_baseline()) {
                numAutoBaseline++;
//                Log.w(TAG, "Auto Baseline = " + match_inst.isAuto_baseline());
//                Log.w(TAG, "Auto Baseline Number= " + numAutoBaseline);
            }

//            Log.w(TAG, "Auto Comment = " + match_inst.getAuto_comment() + "  " + match_inst.getAuto_comment().length());
            if (match_inst.getAuto_comment().length() > 1) {
                auto_Comments = auto_Comments + match_inst.getMatch() + "-" + match_inst.getAuto_comment() + "\n" + underScore  + "\n" ;
            }
            String pos = match_inst.getPre_startPos().trim();
            Log.w(TAG, "Start Pos. " + pos);
            switch (pos) {
                case "Si":
                    auto_B1++;
                    break;
                case ("So"):
                    auto_B2++;
                    break;
                case "M":
                    auto_B3++;
                    break;
                default:                //
                    Log.e(TAG, "***  Invalid Start Position!!!  ***" );
            }

            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//            tele_HGtotalShooting = tele_HGtotalShooting + match_inst.getTele_hg_percent();
//            tele_LGtotalShooting = tele_LGtotalShooting + match_inst.getTele_lg_percent();

//            tele_totalGearsPlaced = tele_totalGearsPlaced + match_inst.getTele_gears_placed();
//            Log.w(TAG, "Tele Gears Placed = " + match_inst.getTele_gears_placed());
//            tele_totalGearsAttempted = tele_totalGearsAttempted + match_inst.getTele_gears_attempt();
//            Log.w(TAG, "Tele Gears Attempted = " + match_inst.getTele_gears_attempt());


//            Log.w(TAG, "Tele HG Boolean = " + match_inst.isTele_hg());
//            if (match_inst.isTele_hg()) {
//                tele_HGtotalShooting = tele_HGtotalShooting + match_inst.getTele_hg_percent();
//                numTeleHG++;
////                Log.w(TAG, "numTeleHG = " + numTeleHG);
//            }
//            Log.w(TAG, "Tele HG = " + match_inst.getTele_hg_percent());
//            if (match_inst.isTele_lg()) {       /* Matthew - <<<<<<<<<<<<<<<<  match_inst _NOT_ Match_Data */
//                tele_LGtotalShooting = tele_LGtotalShooting + match_inst.getTele_lg_percent();
//                numTeleLG++;
////                Log.w(TAG, "numTeleLG = " + numTeleLG);
//
//            }
//            Log.w(TAG, "Tele LG = " + match_inst.getAuto_lg_percent());


            if (match_inst.isTele_climb_attempt()) {
                numTeleClimbAttempt++;
//                Log.w(TAG, "Tele Climb Attempt = " + match_inst.isTele_climb_attempt());
//                Log.w(TAG, "Tele Climb Attempt Number= " + numTeleClimbAttempt);
            }
            if (match_inst.isTele_climb_success()) {
                numTeleClimbSuccess++;
//                Log.w(TAG, "Tele Climb Success = " + match_inst.isTele_climb_success());
//                Log.w(TAG, "Tele Climb Success Number= " + numTeleClimbSuccess);
            }

//            Log.w(TAG, "Tele Comment = " + match_inst.getTele_comment() + "  " + match_inst.getTele_comment().length());
            if (match_inst.getTele_comment().length() > 1) {
                tele_Comments = tele_Comments + match_inst.getMatch() + "-" + match_inst.getTele_comment() + "\n" + underScore  + "\n" ;
            }

            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2
            // ToDo - collect Final elements
            if (match_inst.isFinal_lostComms()) {
                final_LostComm++;
            }
            if (match_inst.isFinal_lostParts()) {
                final_LostParts++;
            }
            if (match_inst.isFinal_defense_good()) {
                final_DefGood++;
            }
            if (match_inst.isFinal_def_Lane()) {
                final_DefStarve++;
            }
            if (match_inst.isFinal_def_Block()) {
                final_DefBlock++;
            }
            if (match_inst.getFinal_num_Penalties() > 0) {
                final_NumPen = final_NumPen + match_inst.getFinal_num_Penalties();
            }

//            Log.w(TAG, "Final Comment = " + match_inst.getFinal_comment() + "  " + match_inst.getFinal_comment().length());
            if (match_inst.getFinal_comment().length() > 1) {
                final_Comments = final_Comments + match_inst.getMatch() + "-" + match_inst.getFinal_comment() + "\n" + underScore  + "\n" ;
            }
        } //end For


// ================================================================
// ======  Now start displaying all the data we collected  ========
// ================================================================

        txt_Matches.setText(matches);

//        Log.w(TAG, "Ratio of Placed to Attempted Gears in Auto = " + auto_totalgearsPlaced + "/" + auto_totalgearsAttempted);
        txt_auto_gearRatio.setText(auto_totalgearsPlaced + "/" + auto_totalgearsAttempted);

//        Log.w(TAG, "Ratio of Crossing Baseline to Number of Matches = " + numAutoBaseline + "/" + numObjects);
        txt_auto_baselineRatio.setText(numAutoBaseline +  "/" + numObjects);

//        Log.w(TAG, "Auto Gears Attempted = " + auto_gearsAttempted);
//        Log.w(TAG, "Auto Gears Placed = " + auto_gearsPlaced);
        txt_spSi.setText(String.valueOf(auto_B1));
        txt_spSo.setText(String.valueOf(auto_B2));
        txt_spM.setText(String.valueOf(auto_B3));


        txt_AutoComments.setText(auto_Comments);

        // ==============================================
        // Display Tele elements
        Log.w(TAG, "Ratio of Placed to Attempted Gears in Tele = " + tele_totalGearsPlaced + "/" + tele_totalGearsAttempted);
        txt_tele_gearRatio.setText(tele_totalGearsPlaced + "/" + tele_totalGearsAttempted);
        txt_climbAttempts.setText(numTeleClimbAttempt + "/" + numObjects);
        txt_successfulClimbs.setText(numTeleClimbSuccess + "/" + numObjects);

        txt_TeleComments.setText(tele_Comments);

        // ==============================================
        // ToDo - display Final elements
        txt_final_LostComm.setText(String.valueOf(final_LostComm));
        txt_final_LostParts.setText(String.valueOf(final_LostParts));
        txt_final_DefGood.setText(String.valueOf(final_DefGood));
        txt_final_DefDump.setText(String.valueOf(final_DefDump));
        txt_final_DefBlock.setText(String.valueOf(final_DefBlock));
        txt_final_DefStarve.setText(String.valueOf(final_DefStarve));
        txt_final_NumPen.setText(String.valueOf(final_NumPen));

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

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
    TextView txt_auto_baselineRatio, txt_noAuto, txt_auto_cubeSwRatio, txt_SwXover, txt_SwWrong, txt_SwExtra, txt_auto_cubeScRatio, txt_ScXover,txt_ScWrong;
    TextView txt_tele_cubeSwRatio, txt_climbs;
    /* Comment Boxes */     TextView txt_AutoComments, txt_TeleComments, txt_FinalComments;
    TextView txt_spSi, txt_spSo, txt_spM;
    TextView txt_final_LostComm, txt_final_LostParts, txt_final_DefGood, txt_final_DefBlock,txt_final_DefDump, txt_final_DefStarve, txt_final_NumPen;
    //----------------------------------
    int numAutoBaseline = 0; int noAuto = 0; int numExtra = 0;
    int auto_SwCubesAttempted = 0; int auto_SwCubesPlaced = 0; int auto_SwCrossOver = 0; int Auto_SwWrong = 0;
    int auto_ScCubesAttempted = 0; int auto_ScCubesPlaced = 0; int auto_ScCrossOver = 0; int Auto_ScWrong = 0;
    int auto_B1 = 0; int auto_B2 = 0; int auto_B3 = 0;
    String auto_Comments = "";
    //----------------------------------
    int tele_totalGearsAttempted = 0;
    int tele_totalGearsPlaced = 0;
    int numTeleClimbSuccess = 0;
    int numTeleClimbAttempt = 0;
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
        /*  Auto  */
        txt_auto_baselineRatio = (TextView) findViewById(R.id.txt_auto_baselineRatio);
        txt_noAuto = (TextView) findViewById(R.id.txt_noAuto);
        txt_auto_cubeSwRatio = (TextView) findViewById(R.id.txt_auto_cubeSwRatio);
        txt_SwXover = (TextView) findViewById(R.id.txt_SwXover);
        txt_SwWrong = (TextView) findViewById(R.id.txt_SwWrong);
        txt_SwExtra = (TextView) findViewById(R.id.txt_SwExtra);
        txt_auto_cubeScRatio = (TextView) findViewById(R.id.txt_auto_cubeScRatio);
        txt_ScXover = (TextView) findViewById(R.id.txt_ScXover);
        txt_ScWrong = (TextView) findViewById(R.id.txt_ScWrong);
        txt_spSi = (TextView) findViewById(R.id.txt_spSi);
        txt_spSo = (TextView) findViewById(R.id.txt_spSo);
        txt_spM = (TextView) findViewById(R.id.txt_spM);
        txt_AutoComments = (TextView) findViewById(R.id.txt_AutoComments);
        txt_AutoComments.setMovementMethod(new ScrollingMovementMethod());
        /*  Tele  */
        txt_tele_cubeSwRatio = (TextView) findViewById(R.id.txt_tele_cubeSwRatio);
        txt_TeleComments = (TextView) findViewById(R.id.txt_TeleComments);
        txt_TeleComments.setMovementMethod(new ScrollingMovementMethod());
        /*  Final  */
        txt_FinalComments = (TextView) findViewById(R.id.txt_FinalComments);
        txt_FinalComments.setMovementMethod(new ScrollingMovementMethod());
        txt_climbs = (TextView) findViewById(R.id.txt_climbs);

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

        numAutoBaseline = 0; auto_SwCubesAttempted = 0; auto_SwCubesPlaced = 0; tele_totalGearsAttempted = 0; numExtra = 0;
        tele_totalGearsPlaced = 0;
        auto_B1 = 0; auto_B2 = 0; auto_B3 = 0;
        auto_Comments = ""; tele_Comments = ""; final_Comments=""; matches = "";
        final_LostComm = 0; final_LostParts = 0; final_DefGood = 0; final_DefBlock = 0;  final_DefDump = 0; final_DefStarve = 0; final_NumPen = 0;


// ================================================================
        for (int i = 0; i < numObjects; i++) {
            Log.w(TAG, "In for loop!   " + i);
            match_inst = Pearadox.Matches_Data.get(i);      // Get instance of Match Data
            matches = matches + match_inst.getMatch() + "  ";

            if (match_inst.isAuto_mode()) {
                noAuto++;
            }
            if (match_inst.isAuto_baseline()) {
                numAutoBaseline++;
            }
            if (match_inst.isAuto_cube_switch()) {
                auto_SwCubesPlaced++;
            }
            if (match_inst.isAuto_cube_switch_att()) {
                auto_SwCubesAttempted++;
            }
            if (match_inst.isAuto_xover_switch()) {
                auto_SwCrossOver++;
            }
            if (match_inst.isAuto_wrong_switch()) {
                Auto_SwWrong++;
            }
            if (match_inst.isAuto_switch_extra()) {
                numExtra++;
            }

//            Log.w(TAG, "Auto Comment = " + match_inst.getAuto_comment() + "  " + match_inst.getAuto_comment().length());
            if (match_inst.getAuto_comment().length() > 1) {
                auto_Comments = auto_Comments + match_inst.getMatch() + "-" + match_inst.getAuto_comment() + "\n" + underScore  + "\n" ;
            }
            String pos = match_inst.getPre_startPos().trim();
            Log.w(TAG, "Start Pos. " + pos);
            switch (pos) {
                case "Side (in)":
                    auto_B1++;
                    break;
                case ("Side (out)"):
                    auto_B2++;
                    break;
                case "Middle":
                    auto_B3++;
                    break;
                default:                //
                    Log.e(TAG, "***  Invalid Start Position!!!  ***" );
            }


            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2
            // Tele elements
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
            // Final elements
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

        txt_auto_baselineRatio.setText(numAutoBaseline +  "/" + numObjects);
        txt_noAuto.setText(noAuto +  "/" + numObjects);
//        Log.w(TAG, "Ratio of Placed to Attempted Gears in Auto = " + auto_SwCubesPlaced + "/" + auto_SwCubesAttempted);
        txt_auto_cubeSwRatio.setText(auto_SwCubesPlaced + "/" + auto_SwCubesAttempted);
        txt_SwExtra.setText(String.valueOf(numExtra));
        txt_SwXover.setText(String.valueOf(auto_SwCrossOver));
        txt_SwWrong.setText(String.valueOf(Auto_SwWrong));
        txt_auto_cubeScRatio.setText(auto_ScCubesPlaced + "/" + auto_ScCubesAttempted);
        txt_ScXover.setText(String.valueOf(auto_ScCrossOver));
        txt_ScWrong.setText(String.valueOf(Auto_ScWrong));
//        Log.w(TAG, "Auto Gears Attempted = " + auto_gearsAttempted);
//        Log.w(TAG, "Auto Gears Placed = " + auto_gearsPlaced);
        txt_spSi.setText(String.valueOf(auto_B1));
        txt_spSo.setText(String.valueOf(auto_B2));
        txt_spM.setText(String.valueOf(auto_B3));


        txt_AutoComments.setText(auto_Comments);

        // ==============================================
        // Display Tele elements
        Log.w(TAG, "Ratio of Placed to Attempted Gears in Tele = " + tele_totalGearsPlaced + "/" + tele_totalGearsAttempted);
        txt_tele_cubeSwRatio.setText(tele_totalGearsPlaced + "/" + tele_totalGearsAttempted);
        txt_climbs.setText(numTeleClimbSuccess + "/" + numTeleClimbAttempt);

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

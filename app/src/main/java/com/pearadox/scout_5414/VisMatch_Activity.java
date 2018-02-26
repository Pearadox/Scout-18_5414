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
    TextView txt_auto_baselineRatio, txt_noAuto, txt_auto_cubeSwRatio, txt_SwXover, txt_SwWrong, txt_SwExtra, txt_ScExtra, txt_auto_cubeScRatio, txt_ScXover, txt_ScWrong, txt_CubeTheirsNUM;
    TextView txt_tele_cubeSwRatio, txt_TheirSwitch, txt_tele_cubeScRatio, txt_PortalNUM;
    TextView txt_CubeZoneNUM, txt_CubePlatformNUM, txt_CubeFloorNUM, txt_CubeRandomNUM;
    TextView txt_climbs, txt_Lift1NUM, txt_Lift2NUM, txt_WasLiftedNUM, txt_OnPlatNUM, txt_RungNUM, txt_SideNUM, txt_ExchangeNUM, txt_LaunchNUM, txt_PlaceNUM;
    /* Comment Boxes */     TextView txt_AutoComments, txt_TeleComments, txt_FinalComments;
    TextView txt_spSi, txt_spSo, txt_spM;
    //----------------------------------
    int numAutoBaseline = 0; int noAuto = 0; int numExtraSw = 0; int numExtraSc = 0;
    int auto_SwCubesAttempted = 0; int auto_SwCubesPlaced = 0; int auto_SwCrossOver = 0; int Auto_SwWrong = 0;
    int auto_ScCubesAttempted = 0; int auto_ScCubesPlaced = 0; int auto_ScCrossOver = 0; int Auto_ScWrong = 0;
    int auto_B1 = 0; int auto_B2 = 0; int auto_B3 = 0;
    String auto_Comments = "";
    //----------------------------------
    int tele_totalCubeSwAttempted = 0; int tele_totalCubeSwPlaced = 0; int tele_totalCubeScAttempted = 0; int tele_totalCubeScPlaced = 0; int tele_SwTheirs = 0; int tele_SwTheirAtt = 0; int tele_their_floor = 0;
    int cubznNUM = 0; int cubplatNUM = 0; int cubPlatOthrNUM = 0; int offFloorNUM = 0; int portalNUM = 0; int randomNUM = 0;
    int numTeleExch = 0; int numTeleLaunch = 0; int numTelePlace = 0;
    int numTeleClimbSuccess = 0; int numTeleClimbAttempt = 0; int Lift1num = 0; int Lift2num = 0;  int WasLifted = 0; int rungNum = 0; int sideNum = 0; int onPlatform = 0;
    String tele_Comments = "";
    //----------------------------------
    int final_LostComm = 0; int final_LostParts = 0; int final_DefGood = 0; int final_DefBlock = 0;  int final_DefSwitch = 0; int final_DefStarve = 0; int final_NumPen = 0;
    TextView txt_final_LostComm, txt_final_LostParts, txt_final_DefGood, txt_final_DefBlock, txt_final_BlkSwtch, txt_final_NumPen, txt_final_DefStarve;
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
        txt_auto_cubeScRatio = (TextView) findViewById(R.id.txt_auto_cubeScRatio);
        txt_SwXover = (TextView) findViewById(R.id.txt_SwXover);
        txt_SwWrong = (TextView) findViewById(R.id.txt_SwWrong);
        txt_SwExtra = (TextView) findViewById(R.id.txt_SwExtra);
        txt_ScExtra = (TextView) findViewById(R.id.txt_ScExtra);
        txt_ScXover = (TextView) findViewById(R.id.txt_ScXover);
        txt_ScWrong = (TextView) findViewById(R.id.txt_ScWrong);
        txt_spSi = (TextView) findViewById(R.id.txt_spSi);
        txt_spSo = (TextView) findViewById(R.id.txt_spSo);
        txt_spM = (TextView) findViewById(R.id.txt_spM);
        txt_AutoComments = (TextView) findViewById(R.id.txt_AutoComments);
        txt_AutoComments.setMovementMethod(new ScrollingMovementMethod());
        /*  Tele  */
        txt_tele_cubeSwRatio = (TextView) findViewById(R.id.txt_tele_cubeSwRatio);
        txt_TheirSwitch = (TextView) findViewById(R.id.txt_TheirSwitch);
        txt_tele_cubeScRatio = (TextView) findViewById(R.id.txt_tele_cubeScRatio);
        txt_PortalNUM = (TextView) findViewById(R.id.txt_PortalNUM);
        txt_CubeZoneNUM = (TextView) findViewById(R.id.txt_CubeZoneNUM);
        txt_CubePlatformNUM = (TextView) findViewById(R.id.txt_CubePlatformNUM);
        txt_CubeFloorNUM = (TextView) findViewById(R.id.txt_CubeFloorNUM);
        txt_CubeRandomNUM = (TextView) findViewById(R.id.txt_CubeRandomNUM);
        txt_Lift1NUM = (TextView) findViewById(R.id.txt_Lift1NUM);
        txt_Lift2NUM = (TextView) findViewById(R.id.txt_Lift2NUM);
        txt_WasLiftedNUM = (TextView) findViewById(R.id.txt_WasLiftedNUM);
        txt_OnPlatNUM = (TextView) findViewById(R.id.txt_OnPlatNUM);
        txt_RungNUM = (TextView) findViewById(R.id.txt_RungNUM);
        txt_SideNUM = (TextView) findViewById(R.id.txt_SideNUM);
        txt_CubeTheirsNUM = (TextView) findViewById(R.id.txt_CubeTheirsNUM);

        txt_ExchangeNUM = (TextView) findViewById(R.id.txt_ExchangeNUM);
        txt_LaunchNUM = (TextView) findViewById(R.id.txt_LaunchNUM);
        txt_PlaceNUM = (TextView) findViewById(R.id.txt_PlaceNUM);
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
        txt_final_BlkSwtch = (TextView) findViewById(R.id.txt_final_BlkSwtch);
        txt_final_NumPen = (TextView) findViewById(R.id.txt_final_NumPen);
        txt_final_DefStarve = (TextView) findViewById(R.id.txt_final_DefStarve);

        txt_team.setText(tnum);
        txt_teamName.setText(tname);    // Get real

        int numObjects = Pearadox.Matches_Data.size();
        Log.w(TAG, "Objects = " + numObjects);
        txt_NumMatches.setText(String.valueOf(numObjects));

        numAutoBaseline = 0; auto_SwCubesAttempted = 0; auto_SwCubesPlaced = 0; tele_totalCubeSwAttempted = 0; numExtraSw = 0; numExtraSc = 0;
        auto_B1 = 0; auto_B2 = 0; auto_B3 = 0;
        auto_Comments = ""; tele_Comments = ""; final_Comments=""; matches = "";
        final_LostComm = 0; final_LostParts = 0; final_DefGood = 0; final_DefBlock = 0;  final_DefSwitch = 0; final_DefStarve = 0; final_NumPen = 0;


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
                numExtraSw++;
            }
            if (match_inst.isAuto_cube_scale()) {
                auto_ScCubesPlaced++;
            }
            if (match_inst.isAuto_cube_scale_att()) {
                auto_ScCubesAttempted++;
            }
            if (match_inst.isAuto_scale_extra()) {
                numExtraSc++;
            }
            if (match_inst.isAuto_xover_scale()) {
                auto_ScCrossOver++;
            }
            if (match_inst.isAuto_wrong_scale()) {
                Auto_ScWrong++;
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
            tele_totalCubeSwPlaced = tele_totalCubeSwPlaced + match_inst.getTele_cube_switch();
            tele_totalCubeSwAttempted = tele_totalCubeSwAttempted + match_inst.getTele_switch_attempt();
            tele_totalCubeScPlaced = tele_totalCubeScPlaced + match_inst.getTele_cube_scale();
            tele_totalCubeScAttempted = tele_totalCubeScAttempted + match_inst.getTele_scale_attempt();
            portalNUM = portalNUM + match_inst.getTele_cube_portal();
            tele_SwTheirs = tele_SwTheirs + match_inst.getTele_their_switch();
            tele_SwTheirAtt = tele_SwTheirAtt + match_inst.getTele_their_attempt();
            tele_their_floor = tele_their_floor + match_inst.getTele_their_floor();
            randomNUM = randomNUM + match_inst.getTele_random_floor();

            cubznNUM = cubznNUM + match_inst.getTele_cube_pwrzone();
            cubplatNUM = cubplatNUM + match_inst.getTele_cube_floor();
            cubPlatOthrNUM = cubPlatOthrNUM + match_inst.getTele_their_floor();
            if (match_inst.isTele_cube_pickup()) {
                offFloorNUM++;
            }
            numTeleExch = numTeleExch + match_inst.getTele_cube_exchange();
            if (match_inst.isTele_lift_one()) {
                Lift1num++;
            }
            if (match_inst.isTele_lift_two()) {
                Lift2num++;
            }
            if (match_inst.isTele_got_lift()) {
                WasLifted++;
            }
            if (match_inst.isTele_on_platform()) {
                onPlatform++;
            }
            if (match_inst.isTele_grab_rung()) {
                rungNum++;
            }
            if (match_inst.isTele_grab_side()) {
                sideNum++;
            }
            if (match_inst.isTele_launched_cube()) {
                numTeleLaunch++;
            }
            if (match_inst.isTele_placed_cube()) {
                numTelePlace++;
            }
            if (match_inst.isTele_climb_attempt()) {
                numTeleClimbAttempt++;
            }
            if (match_inst.isTele_climb_success()) {
                numTeleClimbSuccess++;
            }
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
        txt_auto_cubeScRatio.setText(auto_ScCubesPlaced + "/" + auto_ScCubesAttempted);
        txt_SwExtra.setText(String.valueOf(numExtraSw));
        txt_SwXover.setText(String.valueOf(auto_SwCrossOver));
        txt_SwWrong.setText(String.valueOf(Auto_SwWrong));
        txt_auto_cubeScRatio.setText(auto_ScCubesPlaced + "/" + auto_ScCubesAttempted);
        txt_ScExtra.setText(String.valueOf(numExtraSc));
        txt_ScXover.setText(String.valueOf(auto_ScCrossOver));
        txt_ScWrong.setText(String.valueOf(Auto_ScWrong));
        txt_spSi.setText(String.valueOf(auto_B1));
        txt_spSo.setText(String.valueOf(auto_B2));
        txt_spM.setText(String.valueOf(auto_B3));


        txt_AutoComments.setText(auto_Comments);

        // ==============================================
        // Display Tele elements
        Log.w(TAG, "Ratio of Placed to Attempted Gears in Tele = " + tele_totalCubeSwPlaced + "/" + tele_totalCubeSwAttempted);
        txt_tele_cubeSwRatio.setText(tele_totalCubeSwPlaced + "/" + tele_totalCubeSwAttempted);
        txt_TheirSwitch.setText(tele_SwTheirs + "/" + tele_SwTheirAtt);
        txt_tele_cubeScRatio.setText(tele_totalCubeScPlaced + "/" + tele_totalCubeScAttempted);
        txt_PortalNUM.setText(String.valueOf(portalNUM));
        txt_CubeZoneNUM.setText(String.valueOf(cubznNUM));
        txt_CubePlatformNUM.setText(String.valueOf(cubplatNUM));
        txt_CubeFloorNUM.setText(String.valueOf(offFloorNUM));
        txt_Lift1NUM.setText(String.valueOf(Lift1num));
        txt_Lift2NUM.setText(String.valueOf(Lift2num));
        txt_WasLiftedNUM.setText(String.valueOf(WasLifted));
        txt_OnPlatNUM.setText(String.valueOf(onPlatform));
        txt_RungNUM.setText(String.valueOf(rungNum));
        txt_SideNUM.setText(String.valueOf(sideNum));
        txt_ExchangeNUM.setText(String.valueOf(numTeleExch));
        txt_LaunchNUM.setText(String.valueOf(numTeleLaunch));
        txt_PlaceNUM.setText(String.valueOf(numTelePlace));
        txt_climbs.setText(numTeleClimbSuccess + "/" + numTeleClimbAttempt);
        txt_CubeTheirsNUM.setText(String.valueOf(tele_their_floor));
        txt_CubeRandomNUM.setText(String.valueOf(randomNUM));

        txt_TeleComments.setText(tele_Comments);

        // ==============================================
        // Display Final elements
        txt_final_LostComm.setText(String.valueOf(final_LostComm));
        txt_final_LostParts.setText(String.valueOf(final_LostParts));
        txt_final_DefGood.setText(String.valueOf(final_DefGood));
        txt_final_BlkSwtch.setText(String.valueOf(final_DefSwitch));
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

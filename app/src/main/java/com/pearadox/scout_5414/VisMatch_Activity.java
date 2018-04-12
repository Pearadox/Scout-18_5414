package com.pearadox.scout_5414;

import android.graphics.Bitmap;
import android.media.MediaActionSound;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.io.File;
import java.io.FileOutputStream;

public class VisMatch_Activity extends AppCompatActivity {
    String TAG = "VisMatch_Activity";        // This CLASS name
    String tnum = "";
    String tname = "";
    int start = 0;          // Start Position for matches (0=ALL)
    int numObjects = 0; int numProcessed = 0;
    Spinner spinner_numMatches;
    String underScore = new String(new char[60]).replace("\0", "_");  // string of 'x' underscores
    String matches = "";
    TextView txt_team, txt_teamName, txt_NumMatches, txt_Matches;
    TextView txt_auto_baselineRatio, txt_noAuto, txt_auto_cubeSwRatio, txt_SwXover, txt_SwWrong, txt_SwExtra, txt_ScExtra, txt_auto_cubeScRatio, txt_ScXover, txt_ScWrong, txt_CubeTheirsNUM;
    TextView txt_tele_cubeSwRatio, txt_TheirSwitch, txt_tele_cubeScRatio, txt_PortalNUM;
    TextView txt_CubeZoneNUM, txt_CubePlatformNUM, txt_CubeFloorNUM, txt_CubeRandomNUM;
    TextView txt_climbs, txt_Lift1NUM, txt_Lift2NUM, txt_WasLiftedNUM, txt_OnPlatNUM, txt_RungNUM, txt_SideNUM, txt_ExchangeNUM, txt_LaunchNUM, txt_PlaceNUM;
    /* Comment Boxes */     TextView txt_AutoComments, txt_TeleComments, txt_FinalComments;
    TextView txt_spSi, txt_spSo, txt_spM;
    public static String[] numMatch = new String[]             // Num. of Matches to process
            {"ALL","Last","Last 2","Last 3"};
    BarChart mBarChart;
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
        Spinner spinner_numMatches = (Spinner) findViewById(R.id.spinner_numMatches);
        ArrayAdapter adapter_Matches = new ArrayAdapter<String>(this, R.layout.robonum_list_layout, numMatch);
        adapter_Matches.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_numMatches.setAdapter(adapter_Matches);
        spinner_numMatches.setSelection(0, false);
        spinner_numMatches.setOnItemSelectedListener(new VisMatch_Activity.numMatches_OnItemSelectedListener());
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
        mBarChart = (BarChart) findViewById(R.id.barchart);

        numObjects = Pearadox.Matches_Data.size();
        Log.w(TAG, "Objects = " + numObjects);
        txt_NumMatches.setText(String.valueOf(numObjects));

        init_Values();
        numProcessed = numObjects;
        start = 0;
        getMatch_Data();
    }
// ================================================================
    private void getMatch_Data() {
        for (int i = start; i < numObjects; i++) {
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
            mBarChart.addBar(new BarModel(match_inst.getTele_cube_switch(), 0xffff0000));       // Switch
            tele_totalCubeSwAttempted = tele_totalCubeSwAttempted + match_inst.getTele_switch_attempt();
            tele_totalCubeScPlaced = tele_totalCubeScPlaced + match_inst.getTele_cube_scale();
            mBarChart.addBar(new BarModel(match_inst.getTele_cube_scale(),  0xff08457e));       // Scale
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

        txt_auto_baselineRatio.setText(numAutoBaseline +  "/" + numProcessed);
        txt_noAuto.setText(noAuto +  "/" + numProcessed);
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


//
//        mBarChart.addBar(new BarModel(2.0f, 0xffff0000));       //1
//        mBarChart.addBar(new BarModel(4.0f,  0xff08457e));
//        mBarChart.addBar(new BarModel(2.0f, 0xffff0000));       //2
//        mBarChart.addBar(new BarModel(4.0f,  0xff08457e));
//        mBarChart.addBar(new BarModel(3.0f, 0xffff0000));       //3
//        mBarChart.addBar(new BarModel(2.0f, 0xff08457e));
//        mBarChart.addBar(new BarModel(5.0f, 0xffff0000));       //4
//        mBarChart.addBar(new BarModel(4.0f,  0xff08457e));
//        mBarChart.addBar(new BarModel(6.0f, 0xffff0000));       //5
//        mBarChart.addBar(new BarModel(5.0f,  0xff08457e));
//        mBarChart.addBar(new BarModel(0.0f, 0xffff0000));       //6
//        mBarChart.addBar(new BarModel(4.0f,  0xff08457e));
//        mBarChart.addBar(new BarModel(6.0f, 0xffff0000));       //7
//        mBarChart.addBar(new BarModel(2.0f,  0xff08457e));
//        mBarChart.addBar(new BarModel(8.0f, 0xffff0000));       //8
//        mBarChart.addBar(new BarModel(4.0f,  0xff08457e));
//        mBarChart.addBar(new BarModel(10.0f, 0xffff0000));      //9
//        mBarChart.addBar(new BarModel(5.0f,  0xff08457e));
//        mBarChart.addBar(new BarModel(7.0f, 0xffff0000));       //10
//        mBarChart.addBar(new BarModel(4.0f,  0xff08457e));
//        mBarChart.addBar(new BarModel(12.0f, 0xffff0000));      //11
//        mBarChart.addBar(new BarModel(5.0f,  0xff08457e));
//        mBarChart.addBar(new BarModel(7.0f, 0xffff0000));       //12
//        mBarChart.addBar(new BarModel(4.0f,  0xff08457e));
//        mBarChart.addBar(new BarModel(10.0f, 0xffff0000));      //13
//        mBarChart.addBar(new BarModel(5.0f,  0xff08457e));
//        mBarChart.addBar(new BarModel(8.0f, 0xffff0000));       //14
//        mBarChart.addBar(new BarModel(4.0f,  0xff08457e));
//        mBarChart.addBar(new BarModel(10.0f, 0xffff0000));      //15
//        mBarChart.addBar(new BarModel(5.0f,  0xff08457e));
//        mBarChart.addBar(new BarModel(8.0f, 0xffff0000));       //16
//        mBarChart.addBar(new BarModel(4.0f,  0xff08457e));

        mBarChart.startAnimation();

    }

//******************************
    private void init_Values() {
        noAuto = 0;
        numAutoBaseline = 0;
        auto_SwCubesPlaced = 0;
        auto_SwCubesAttempted = 0;
        auto_SwCrossOver = 0;
        Auto_SwWrong = 0;
        auto_ScCubesPlaced = 0;
        auto_ScCubesAttempted = 0;
        auto_ScCrossOver = 0;
        Auto_ScWrong = 0;
        tele_totalCubeSwPlaced = 0;
        tele_totalCubeSwAttempted = 0;
        tele_totalCubeScPlaced = 0;
        tele_totalCubeScAttempted = 0;
        tele_SwTheirs = 0;
        tele_SwTheirAtt = 0;
        numExtraSw = 0;
        numExtraSc = 0;
        portalNUM = 0;
        cubznNUM = 0;
        cubplatNUM = 0;
        offFloorNUM = 0;
        numTeleClimbSuccess = 0;
        numTeleClimbAttempt = 0;
        Lift1num = 0;
        Lift2num = 0;
        WasLifted = 0;
        onPlatform = 0;
        rungNum = 0;
        sideNum = 0;
        auto_B1 = 0;
        auto_B2 = 0;
        auto_B3 = 0;
        numTeleExch = 0;
        numTeleLaunch = 0; numTelePlace = 0;
        tele_their_floor = 0;
        randomNUM = 0;
        auto_Comments = "";
        tele_Comments = "";
        final_Comments = "";
        matches = "";
        final_LostComm = 0;
        final_LostParts = 0;
        final_DefGood = 0;
        final_DefBlock = 0;
        final_DefSwitch = 0;
        final_DefStarve = 0;
        final_NumPen = 0;

        mBarChart.clearChart();
    }


//===========================================================================================
    public class numMatches_OnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            String num = " ";
            num = parent.getItemAtPosition(pos).toString();
            Log.w(TAG, ">>>>> NumMatches '" + num + "'");
            switch (num) {
                case "Last":
                    start = numObjects - 1;     //
                    numProcessed = 1;
                    break;
                case "Last 2":
                    start = numObjects - 2;     //
                    numProcessed = 2;
                    break;
                case "Last 3":
                    start = numObjects - 3;     //
                    numProcessed = 3;
                    break;
                case "ALL":
                    start = 0;                  // Start at beginning
                    numProcessed = numObjects;
                    break;
                default:                //
                    Log.e(TAG, "Invalid Sort - " + start);
            }
            Log.w(TAG, "Start = " + num );
            init_Values();
            getMatch_Data();
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_viz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.e(TAG, "@@@  Options  @@@ " );
        Log.w(TAG, " \n  \n");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_screen) {
            String filNam = Pearadox.FRC_Event.toUpperCase() + "-VizMatch"  + "_" + tnum.trim() + ".JPG";
            Log.w(TAG, "File='" + filNam + "'");
            try {
                File imageFile = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/" + filNam);
                View v1 = getWindow().getDecorView().getRootView();             // **\
                v1.setDrawingCacheEnabled(true);                                // ** \Capture screen
                Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());      // ** /  as bitmap
                v1.setDrawingCacheEnabled(false);                               // **/
                FileOutputStream fos = new FileOutputStream(imageFile);
                int quality = 100;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
                fos.flush();
                fos.close();
                bitmap.recycle();           //release memory
                MediaActionSound sound = new MediaActionSound();
                sound.play(MediaActionSound.SHUTTER_CLICK);
                Toast toast = Toast.makeText(getBaseContext(), "☢☢  Screen captured in Download/FRC5414  ☢☢", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            } catch (Throwable e) {
                // Several error may come out with file handling or DOM
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
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

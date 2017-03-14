package com.pearadox.scout_5414;

import android.icu.text.DecimalFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;

public class VisMatch_Activity extends AppCompatActivity {
    String TAG = "VisMatch_Activity";        // This CLASS name
    String tnum = "";
    String tname = "";
    String auto_gearPos = "";
    TextView txt_team, txt_teamName, lbl_Autonomous, txt_auto_HGpercent, txt_auto_LGpercent, txt_auto_gearRatio;
    /* Labels */    TextView lbl_autoHGshootingPercent, lbl_autoLGshootingPercent, lbl_gearRatio;
    int auto_HGtotalShooting = 0;
    int auto_LGtotalShooting = 0;
    int auto_totalgearsAttempted = 0;
    int auto_totalgearsPlaced = 0;
    int tele_HGtotalShooting = 0;
    int tele_LGtotalShooting = 0;
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
        txt_team.setText(tnum);
        txt_teamName.setText(tname);    // Get real

        int numObjects = Pearadox.Matches_Data.size();
        Log.w(TAG, "Objects = " + numObjects);
        int numAutoHG = 0;
        int numAutoLG = 0;

        auto_HGtotalShooting = 0; auto_LGtotalShooting = 0; tele_HGtotalShooting = 0; tele_LGtotalShooting = 0; auto_totalgearsAttempted = 0; auto_totalgearsPlaced = 0;
        for (int i = 0; i < numObjects; i++) {
            Log.w(TAG, "In for loop!   " + i);
            match_inst = Pearadox.Matches_Data.get(i);

//            if (Pearadox.Match_Data.isAuto_hg()) {
                auto_HGtotalShooting = auto_HGtotalShooting + match_inst.getAuto_hg_percent();
  //              numAutoHG++;
//            }
            Log.w(TAG, "Auto HG = " + match_inst.getAuto_hg_percent());
            Log.w(TAG, "Auto HG Boolean = " + match_inst.isAuto_hg());

//            if (Pearadox.Match_Data.isAuto_lg()) {
                auto_LGtotalShooting = auto_LGtotalShooting + match_inst.getAuto_lg_percent();
  //              numAutoLG++;
//            }
            Log.w(TAG, "Auto LG = " + match_inst.getAuto_lg_percent());
            Log.w(TAG, "Auto LG Boolean = " + match_inst.isAuto_lg());


            auto_totalgearsPlaced = auto_totalgearsPlaced + match_inst.getAuto_gears_placed();
            Log.w(TAG, "Auto Gears Placed = " + match_inst.getAuto_gears_placed());

            auto_totalgearsAttempted = auto_totalgearsAttempted + match_inst.getAuto_gears_attempt();
            Log.w(TAG, "Auto Gears Attempted = " + match_inst.getAuto_gears_attempt());

            auto_gearPos = auto_gearPos + match_inst.getAuto_gear_pos();

            tele_HGtotalShooting = tele_HGtotalShooting + match_inst.getTele_hg_percent();
            tele_LGtotalShooting = tele_LGtotalShooting + match_inst.getTele_lg_percent();

        }

        Log.w(TAG, "Auto HG Shooting = " + auto_HGtotalShooting + "   " + numAutoHG);
        float auto_HGPer = auto_HGtotalShooting / numObjects;
        Log.w(TAG, "Percentage of HG Shooting in Auto = " + auto_HGtotalShooting / numObjects);

        Log.w(TAG, "Auto LG Shooting = " + auto_LGtotalShooting + "   " + numAutoLG);
        float auto_LGPer = auto_LGtotalShooting / numObjects;
        Log.w(TAG, "Percentage of LG Shooting in Auto = " + auto_LGtotalShooting / numObjects);

        Log.w(TAG, "Ratio of Placed to Attempted Gears = " + auto_totalgearsPlaced + "/" + auto_totalgearsAttempted);


//        Log.w(TAG, "Auto Gears Attempted = " + auto_gearsAttempted);
//
//        Log.w(TAG, "Auto Gears Placed = " + auto_gearsPlaced);


        String x = String.format("%3.2f",(auto_HGPer)) + "%";
        Log.w(TAG, "X = " + x);
        txt_auto_HGpercent.setText(String.format("%3.2f",(auto_HGPer)) + "%");
        txt_auto_LGpercent.setText(String.format("%3.2f",(auto_LGPer)) + "%");
        txt_auto_gearRatio.setText(auto_totalgearsPlaced + "/" + auto_totalgearsAttempted);

    }


}

package com.pearadox.scout_5414;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class VisPit_Activity extends AppCompatActivity {

    String TAG = "VisPit_Activity";        // This CLASS name
    String tnum = "";
    TextView txt_team, txt_teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis_pit);
        Log.i(TAG, "@@@@  VisPit_Activity started  @@@@");
        Bundle bundle = this.getIntent().getExtras();
        String param1 = bundle.getString("team");
        Log.w(TAG, param1);      // ** DEBUG **
        tnum = param1;      // Save Team #

        txt_team = (TextView) findViewById(R.id.txt_team);
        txt_teamName = (TextView) findViewById(R.id.txt_teamName);
        txt_team.setText(tnum);
        // ToDo - get "real" name  of team
        txt_teamName.setText("***");    // Get real

    }


}

package com.pearadox.scout_5414;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MatchScoutActivity extends AppCompatActivity {

    String TAG = "MatchScout_Activity";      // This CLASS name
    TextView txt_dev, txt_stud, txt_match, txt_MyTeam;
    ImageView imgScoutLogo;
    TextView txt_TeamName;
    TextView txt_GearsPlaced;
    private Button button_GearsMinus, button_GearsPlus;
    int gearNum = 0;

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "<< Match Scout >>");
        setContentView(R.layout.activity_match_scout);
        Bundle bundle = this.getIntent().getExtras();
        String param1 = bundle.getString("dev");
        String param2 = bundle.getString("stud");
        Log.d(TAG, param1 + " " + param2);      // ** DEBUG **

        txt_GearsPlaced = (TextView) findViewById(R.id.txt_GearsPlaced);
        button_GearsMinus = (Button) findViewById(R.id.button_GearsMinus);
        button_GearsPlus = (Button) findViewById(R.id.button_GearsPlus);
        txt_GearsPlaced.setText(Integer.toString(gearNum));

        button_GearsPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // ToDo check to ensure not over MAX # gears
                gearNum++;
                Log.d(TAG, "Gears = " + gearNum);      // ** DEBUG **
                txt_GearsPlaced.setText(Integer.toString(gearNum));    // Perform action on click
            }
        });
        button_GearsMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // ToDo make sure not already at zero
                gearNum--;
                Log.d(TAG, "Gears = " + gearNum);      // ** DEBUG **
                txt_GearsPlaced.setText(Integer.toString(gearNum));    // Perform action on click
            }
        });
        txt_dev = (TextView) findViewById(R.id.txt_Dev);
        txt_stud = (TextView) findViewById(R.id.txt_Student);
        txt_match = (TextView) findViewById(R.id.txt_Match);
        txt_MyTeam = (TextView) findViewById(R.id.txt_MyTeam);
        txt_TeamName = (TextView) findViewById(R.id.txt_TeamName);
        ImageView imgScoutLogo = (ImageView) findViewById(R.id.imageView_MS);
        txt_dev.setText(param1);
        txt_stud.setText(param2);
        txt_match.setText("");
        txt_MyTeam.setText("");
        txt_TeamName.setText("");
        String devcol = param1.substring(0,3);
        Log.d(TAG, "color=" + devcol);
        if (devcol.equals("Red")) {
            imgScoutLogo.setImageDrawable(getResources().getDrawable(R.drawable.red_scout));
        } else {
            imgScoutLogo.setImageDrawable(getResources().getDrawable(R.drawable.blue_scout));
        }

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
        // ToDo - ??????
    }



}

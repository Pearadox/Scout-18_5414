package com.pearadox.scout_5414;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;

import static com.pearadox.scout_5414.R.id.button_GoToFinalActivity;

/**
 * Created by mlm.02000 on 2/5/2017.
 */

public class ArenaLayoutActivity extends Activity {


    String TAG = "ArenaLayoutActivity";      // This CLASS name
    TextView txt_dev, txt_stud, txt_match, txt_MyTeam, lbl_GearNUMT;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "<< Arena Layout >>");
        setContentView(R.layout.activity_arena_layout);
        Bundle bundle = this.getIntent().getExtras();

        String param1 = bundle.getString("dev");
        String param2 = bundle.getString("stud");
        Log.d(TAG, param1 + " " + param2);      // ** DEBUG **

    }


}

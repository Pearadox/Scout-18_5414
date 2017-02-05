package com.pearadox.scout_5414;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class PitScoutActivity extends AppCompatActivity {

    String TAG = "PitScout_Activity";      // This CLASS name
    TextView txt_dev, txt_stud, txt_Team;
    ImageView imgScoutLogo;
    Spinner spinner_Team;
    ArrayAdapter<String> adapter;
    public String teamSelected = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pit_scout);
        Log.i(TAG, "<< Pit Scout >>");
        Bundle bundle = this.getIntent().getExtras();
        String param1 = bundle.getString("dev");
        String param2 = bundle.getString("stud");
        Log.d(TAG, param1 + " " + param2);      // ** DEBUG **

        txt_dev = (TextView) findViewById(R.id.txt_Dev);
        txt_stud = (TextView) findViewById(R.id.txt_Student);
        txt_dev.setText(param1);
        txt_stud.setText(param2);
        Spinner spinner_Team = (Spinner) findViewById(R.id.spinner_Team);
        String[] teams = getResources().getStringArray(R.array.team_array);
        adapter = new ArrayAdapter<String>(this, R.layout.team_list_layout, teams);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Team.setAdapter(adapter);
        spinner_Team.setSelection(0, false);
        spinner_Team.setOnItemSelectedListener(new team_OnItemSelectedListener());

    }
    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    public class team_OnItemSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            teamSelected = parent.getItemAtPosition(pos).toString();
            Log.d(TAG, ">>>>>  '" + teamSelected + "'");
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
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

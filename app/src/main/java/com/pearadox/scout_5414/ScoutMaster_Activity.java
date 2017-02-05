package com.pearadox.scout_5414;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// Debug & Messaging
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;
import android.widget.ToggleButton;

public class ScoutMaster_Activity extends AppCompatActivity {

    String TAG = "ScoutMaster_Activity";      // This CLASS name
    ArrayAdapter<String> adapter_typ;
    public String typSelected = " ";
    Spinner spinner_MatchType;
    Spinner spinner_MatchNum;
    ArrayAdapter<String> adapter_Num;
    public String NumSelected = " ";
    public String matchID = "T00";      // Type + #
    ToggleButton toggleStartStop;
    TextView txt_teamR1, txt_teamR2, txt_teamR3, txt_teamB1, txt_teamB2, txt_teamB3;
    TextView txt_scoutR1,txt_scoutR2, txt_scoutR3, txt_scoutB1, txt_scoutB2, txt_scoutB3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout_master);

        Log.i(TAG, "******* Scout Master  *******");
        matchID = "";
        Spinner spinner_MatchType = (Spinner) findViewById(R.id.spinner_MatchType);
        String[] devices = getResources().getStringArray(R.array.mtchtyp_array);
        adapter_typ = new ArrayAdapter<String>(this, R.layout.dev_list_layout, devices);
        adapter_typ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_MatchType.setAdapter(adapter_typ);
        spinner_MatchType.setSelection(0, false);
        spinner_MatchType.setOnItemSelectedListener(new type_OnItemSelectedListener());
        Spinner spinner_MatchNum = (Spinner) findViewById(R.id.spinner_MatchNum);
        ArrayAdapter adapter_Num = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Pearadox.matches);
        adapter_Num.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_MatchNum.setAdapter(adapter_Num);
        spinner_MatchNum.setSelection(0, false);
        spinner_MatchNum.setOnItemSelectedListener(new mNum_OnItemSelectedListener());

        toggleStartStop = (ToggleButton) findViewById(R.id.toggleStartStop);
        toggleStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner_MatchType = (Spinner) findViewById(R.id.spinner_MatchType);
                Spinner spinner_MatchNum = (Spinner) findViewById(R.id.spinner_MatchNum);
                txt_teamR1 = (TextView) findViewById(R.id.txt_teamR1);
                txt_teamR2 = (TextView) findViewById(R.id.txt_teamR2);
                txt_teamR3 = (TextView) findViewById(R.id.txt_teamR3);
                txt_teamB1 = (TextView) findViewById(R.id.txt_teamB1);
                txt_teamB2 = (TextView) findViewById(R.id.txt_teamB2);
                txt_teamB3 = (TextView) findViewById(R.id.txt_teamB3);
                if (toggleStartStop.isChecked()) {      // See what state we are in
//                  ToDo - Get list of team for this match
                    getTeams();
                    txt_teamR1.setText("1111");             // ** DEBUG
                    txt_teamR2.setText("2222");
                    txt_teamR3.setText("3333");
                    txt_teamB1.setText("1111");
                    txt_teamB2.setText("2222");
                    txt_teamB3.setText("3333");
                } else {        // Stop Session
//                  ToDo - Clear data
                    spinner_MatchType.setSelection(0);         //Reset to NO selection
                    spinner_MatchNum.setSelection(0);        //*
                    txt_teamR1.setText(" ");
                    txt_teamR2.setText(" ");
                    txt_teamR3.setText(" ");
                    txt_teamB1.setText(" ");
                    txt_teamB2.setText(" ");
                    txt_teamB3.setText(" ");
                }
            }
        });
//        int z = matchID.length();
//        if (z == 3) {
//
//        } else {
//            Toast.makeText(getBaseContext(), "** Select both Maatch TYPE & NUMBER ** ", Toast.LENGTH_LONG).show();
//        }
    }

    private void getTeams() {
        Log.d(TAG, "getTeams");

    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private class type_OnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            typSelected = parent.getItemAtPosition(pos).toString();
            Log.d(TAG, ">>>>>  '" + typSelected + "'");
            switch (typSelected) {
                case "Practice": 	    // Practice round
                    matchID = "X";
                    break;
                case "Qualifying": 		// Qualifying round
                    matchID = "Q";
                    break;
                case "Playoff": 		// Playoff round
                    matchID = "P";
                    break;
                default:                // ????
                    Log.e(TAG, "*** Error - bad TYPE indicator  ***");

            }
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private class mNum_OnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            NumSelected = parent.getItemAtPosition(pos).toString();
            Log.d(TAG, ">>>>>  '" + NumSelected + "'");
            matchID = matchID + NumSelected;

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
        // ToDo - stop BT service
    }


}

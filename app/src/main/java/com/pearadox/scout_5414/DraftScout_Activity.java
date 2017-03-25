package com.pearadox.scout_5414;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cpjd.main.Settings;
import com.cpjd.main.TBA;
import com.cpjd.models.Event;
import com.cpjd.models.Team;

import java.util.ArrayList;

public class DraftScout_Activity extends AppCompatActivity {

    String TAG = "DraftScout_Activity";        // This CLASS name
    TextView txt_EventName, txt_NumTeams;
    ListView lstView_Teams;
    ArrayAdapter<String> adaptTeams;
    ArrayList<String> draftList = new ArrayList<String>();
    public int teamSelected = 0;
    String tnum = "";
    Team[] teams;
    public static int BAnumTeams = 0;                                      // # of teams from Blue Alliance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_scout);

        Log.i(TAG, "@@@@@ DraftScout_Activity  @@@@@");
        txt_EventName = (TextView) findViewById(R.id.txt_EventName);
        txt_NumTeams = (TextView) findViewById(R.id.txt_NumTeams);
        lstView_Teams = (ListView) findViewById(R.id.lstView_Teams);
        txt_EventName.setText(Pearadox.FRC_EventName);              // Event Name
        txt_NumTeams.setText(String.valueOf(Pearadox.numTeams));    // # of Teams

        adaptTeams = new ArrayAdapter<String>(this, R.layout.draft_list_layout, draftList);
        lstView_Teams.setAdapter(adaptTeams);
        adaptTeams.notifyDataSetChanged();
        loadTeams();


// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        lstView_Teams.setOnItemClickListener(new AdapterView.OnItemClickListener()	{
            public void onItemClick(AdapterView<?> parent,
                                    View view, int pos, long id) {
                Log.w(TAG,"*** lstView_Teams ***   Item Selected: " + pos);
                teamSelected = pos;
                lstView_Teams.setSelector(android.R.color.holo_blue_light);
        		/* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
                tnum = draftList.get(teamSelected).substring(0,4);
            }
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            }
        });


    }

    private void loadTeams() {
        Log.i(TAG, "@@@@  loadTeams started  @@@@");
// ----------  Blue Alliance  -----------
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        TBA.setID("Pearadox", "Scout-5414", "V1");
        final TBA tba = new TBA();
        Settings.FIND_TEAM_RANKINGS = true;
        Settings.GET_EVENT_TEAMS = true;
        Settings.GET_EVENT_MATCHES = true;
        Settings.GET_EVENT_ALLIANCES = true;
        Settings.GET_EVENT_AWARDS = true;
        Settings.GET_EVENT_STATS = true;
        String tn = "";

        TBA t = new TBA();
        Event e = t.getEvent(Pearadox.FRC_Event, 2017);
        teams = e.teams.clone();
//        Team[] teams1 = e.teams;
        Log.e(TAG, Pearadox.FRC_Event + "Teams= " + teams.length);
        draftList.clear();
        BAnumTeams = e.teams.length;
        for(int i = 0; i < teams.length; i++) {
            if (String.valueOf(teams[i].team_number).length() < 4) {
                tn = " " + String.valueOf(teams[i].team_number);    // Add leading blank
            } else {
                tn = String.valueOf(teams[i].team_number);
            }
            draftList.add(tn +  "    Rank=" + teams[i].rank + "  " + teams[i].record +  "  OPR="  + String.format("%3.1f",(teams[i].opr)));
//            System.out.println("Team #: "+teams[i].team_number+ "  " +teams[i].rank+" OPR: "+teams[i].opr+ "  " +teams[i].touchpad);
//            System.out.println("        "+teams[i].record+" RankScore: "+teams[i].rankingScore+ "  " +teams[i].pressure + "\n ");
        }
        Log.w(TAG,"### Teams ###  : " + draftList.size());
        adaptTeams = new ArrayAdapter<String>(this, R.layout.draft_list_layout, draftList);
        lstView_Teams.setAdapter(adaptTeams);
        adaptTeams.notifyDataSetChanged();

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


package com.pearadox.scout_5414;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.cpjd.main.Settings;
import com.cpjd.main.TBA;
import com.cpjd.models.Event;
import com.cpjd.models.Team;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

public class DraftScout_Activity extends AppCompatActivity {

    String TAG = "DraftScout_Activity";        // This CLASS name
    Boolean is_Resumed = false;
    TextView txt_EventName, txt_NumTeams;
    ListView lstView_Teams;
    TextView TeamData, BA, Stats;
    Button btn_Match;
    RadioGroup radgrp_Sort;
    RadioButton radio_Climb, radio_Cubes, radio_Weight, radio_Team;
//    Button btn_Up, btn_Down, btn_Delete;
    public ArrayAdapter<String> adaptTeams;
//    ArrayList<String> draftList = new ArrayList<String>();
    static final ArrayList<HashMap<String,String>> draftList = new ArrayList<HashMap<String,String>>();
    public int teamSelected = 0; public String sortType = "Team#";
    String tnum = "";
    String tn = "";
    p_Firebase.teamsObj team_inst = new p_Firebase.teamsObj();
//    Team[] teams;
    public static int BAnumTeams = 0;                                      // # of teams from Blue Alliance
    String cubeChk=""; String climbChk=""; String climbRatio="";
    String autoSwRatio=""; String autoScRatio="-/-"; String autoSwXover=""; String autoScXover="";
    String teleSwRatio="-/-"; String teleScRatio="-/-";
    String mdNumMatches=""; String teleExch=""; String teleOthrRatio=""; String telePort="";
    String liftOne = ""; String liftTwo = ""; String gotLifted = "";  String onPlatform = "";
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfMatchData_DBReference;
    matchData match_inst = new matchData();
    // -----  Array of Match Data Objects for Draft Scout
    public static ArrayList<matchData> All_Matches = new ArrayList<matchData>();
    String load_team, load_name;
    //===========================
    public static class Scores {
        private String teamNum;
        private String teamName;
        private float cubeScore;
        private float climbScore;
        private float weightedScore;

        public Scores() {
        }

        public Scores(String teamNum, String teamName, float cubeScore, float climbScore, float weightedScore) {
            this.teamNum = teamNum;
            this.teamName = teamName;
            this.cubeScore = cubeScore;
            this.climbScore = climbScore;
            this.weightedScore = weightedScore;
        }

        public String getTeamNum() {
            return teamNum;
        }
        public void setTeamNum(String teamNum) {
            this.teamNum = teamNum;
        }
        public String getTeamName() {
            return teamName;
        }
        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }
        public float getCubeScore() {
            return cubeScore;
        }
        public void setCubeScore(float cubeScore) {
            this.cubeScore = cubeScore;
        }
        public float getClimbScore() {
            return climbScore;
        }
        public void setClimbScore(float climbScore) {
            this.climbScore = climbScore;
        }
        public float getWeightedScore() {
            return weightedScore;
        }
        public void setWeightedScore(float weightedScore) {
            this.weightedScore = weightedScore;
        }

        public static Comparator<Scores> teamComp = new Comparator<Scores>() {
            public int compare(Scores t1, Scores t2) {
                String TeamNum1 = t1.getTeamNum();
                String TeamNum2 = t2.getTeamNum();
                //ascending order
                return TeamNum1.compareTo(TeamNum2);
                //descending order
                //return TeamNum2.compareTo(TeamNum1);
            }};
        public static Comparator<Scores> climbComp = new Comparator<Scores>() {
            public int compare(Scores s1, Scores s2) {
                float climbNum1 = s1.getClimbScore();
                float climbNum2 = s2.getClimbScore();
	            /*For ascending order*/
                //return climbNum1-climbNum2;
	            /*For descending order*/
                return (int) (climbNum2-climbNum1);
            }};
    }
    //==========================
    public ArrayList<Scores> team_Scores = new ArrayList<Scores>();
    Scores score_inst = new Scores();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_scout);
        Log.i(TAG, "@@@@@ DraftScout_Activity  @@@@@");
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        String cubeExch = sharedPref.getString("prefCube_exchange", "1.0");

        String cubeColPort = sharedPref.getString("prefCubeCol_portal", "1.0");

        String climbLift1 = sharedPref.getString("prefClimb_lift1", "1.5");
        String climbLift2 = sharedPref.getString("prefClimb_lift2", "2.0");
        String climbLifted = sharedPref.getString("prefClimb_lifted", "0.3");

        String wtCubes = sharedPref.getString("prefWeight_cubes", "1.0");
        String wtClimb = sharedPref.getString("prefWeight_climb", "1.0");
// ToDo - add remaining preferences

        Log.e(TAG,"CUBE Preferences: Port=" + cubeColPort + " Exch=" + cubeExch + "   CLIMB Preferences: Lift1=" + climbLift1 + " Lift2=" + climbLift2 + " Lifted=" + climbLifted+ "   WEIGHT Preferences: Cubes=" + wtCubes + " Climb=" + wtClimb);

        txt_EventName = (TextView) findViewById(R.id.txt_EventName);
        txt_NumTeams = (TextView) findViewById(R.id.txt_NumTeams);
        lstView_Teams = (ListView) findViewById(R.id.lstView_Teams);
        txt_EventName.setText(Pearadox.FRC_EventName);              // Event Name
        txt_NumTeams.setText(String.valueOf(Pearadox.numTeams));    // # of Teams

        pfDatabase = FirebaseDatabase.getInstance();
        pfMatchData_DBReference = pfDatabase.getReference("match-data/" + Pearadox.FRC_Event);    // Match Data

//        SimpleAdapter adaptTeams = new SimpleAdapter(
//                this,
//                draftList,
//                R.layout.draft_list_layout,
//                new String[] {"team","BA","Stats"},
//                new int[] {R.id.TeamData,R.id.BA, R.id.Stats}
//        );
//
//        loadTeams();
//
//        lstView_Teams.setAdapter(adaptTeams);
//        adaptTeams.notifyDataSetChanged();


// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        lstView_Teams.setOnItemClickListener(new AdapterView.OnItemClickListener()	{
            public void onItemClick(AdapterView<?> parent,
                                    View view, int pos, long id) {
                Log.w(TAG,"*** lstView_Teams ***   Item Selected: " + pos);
                teamSelected = pos;
                lstView_Teams.setSelector(android.R.color.holo_blue_light);
        		/* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
//                tnum = draftList.get(teamSelected).substring(0,4);
            }
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            }
        });
    }

    public void RadioClick_Sort(View view) {
        Log.w(TAG, "@@ RadioClick_Sort @@");
        radgrp_Sort = (RadioGroup) findViewById(R.id.radgrp_Sort);
        int selectedId = radgrp_Sort.getCheckedRadioButtonId();
        radio_Team = (RadioButton) findViewById(selectedId);
        String value = radio_Team.getText().toString();
//        lstView_Teams = (ListView) findViewById(R.id.lstView_Teams);
        Log.w(TAG, "RadioSort -  '" + value + "'");
        switch (value) {
            case "Climb":
//                Log.w(TAG, "Climb sort");
                sortType = "Climb";
                Collections.sort(team_Scores, new Comparator<Scores>() {
                    @Override
                    public int compare(Scores c1, Scores c2) {
                        return Float.compare(c1.getClimbScore(), c2.getClimbScore());
                    }
                });
                Collections.reverse(team_Scores);
                loadTeams();
                break;
            case "Cubes":
                sortType = "Cubes";
//                Log.w(TAG, "Cube sort");
                Collections.sort(team_Scores, new Comparator<Scores>() {
                    @Override
                    public int compare(Scores c1, Scores c2) {
                        return Float.compare(c1.getCubeScore(), c2.getCubeScore());
                    }
                });
                Collections.reverse(team_Scores);   // D
                loadTeams();
                break;
            case "Weighted":
//                Log.w(TAG, "Weighted sort");
                sortType = "Weighted";
                Collections.sort(team_Scores, new Comparator<Scores>() {
                    @Override
                    public int compare(Scores c1, Scores c2) {
                        return Float.compare(c1.getWeightedScore(), c2.getWeightedScore());
                    }
                });
                Collections.reverse(team_Scores);   // D
                loadTeams();
                break;
            case "Team#":
//                Log.w(TAG, "Team# sort");
                sortType = "Team#";
                Collections.sort(team_Scores, Scores.teamComp);
                loadTeams();
                break;
            default:                //
                Log.e(TAG, "*** Invalid Sort " + value);
        }

    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void buttonDown_Click(View view) {
        Log.i(TAG, " buttonDown_Click   " + teamSelected);


    }


// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void buttonMatch_Click(View view) {
        Log.i(TAG, ">>>>> buttonMatch_Click  " + teamSelected);
        HashMap<String, String> temp = new HashMap<String, String>();
        String teamHash;

        draftList.get(teamSelected);
        temp = draftList.get(teamSelected);
        teamHash = temp.get("team");
//        Log.w(TAG, "teamHash: '" + teamHash + "' \n ");
        load_team = teamHash.substring(0,4);
        load_name = teamHash.substring(7,teamHash.indexOf("("));  // UP TO # MATCHES
//        Log.w(TAG, ">>>team & name: '" + load_team + "'  [" + load_name +"]");
        addMatchData_Team_Listener(pfMatchData_DBReference);        // Load Matches for _THIS_ selected team
    }


    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void addMatchData_Team_Listener(final DatabaseReference pfMatchData_DBReference) {
        pfMatchData_DBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "<<<< addMatchData_Team_Listener >>>> Match Data for team " + load_team);
                Pearadox.Matches_Data.clear();
                matchData mdobj = new matchData();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();   /*get the data children*/
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    mdobj = iterator.next().getValue(matchData.class);
                    if (mdobj.getTeam_num().matches(load_team)) {
                        Pearadox.Matches_Data.add(mdobj);
                    }
                }
                Log.i(TAG, "***** Matches Loaded. # = "  + Pearadox.Matches_Data.size());
                if (Pearadox.Matches_Data.size() > 0) {
                    Intent match_intent = new Intent(DraftScout_Activity.this, VisMatch_Activity.class);
                    Bundle VZbundle = new Bundle();
                    VZbundle.putString("team", load_team);          // Pass data to activity
                    VZbundle.putString("name", load_name);          // Pass data to activity
                    match_intent.putExtras(VZbundle);
                    startActivity(match_intent);                    // Start Visualizer for Match Data
                } else {
                    Toast toast = Toast.makeText(getBaseContext(), "★★★★  There is _NO_ Match Data for Team " + load_team + "  ★★★★", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*listener failed or was removed for security reasons*/
            }
        });
    }

    private void loadTeams() {
        Log.w(TAG, "@@@@  loadTeams started  @@@@  " + team_Scores.size());

        SimpleAdapter adaptTeams = new SimpleAdapter(
                this,
                draftList,
                R.layout.draft_list_layout,
                new String[] {"team","BA","Stats"},
                new int[] {R.id.TeamData,R.id.BA, R.id.Stats}
        );

        draftList.clear();
        String totalScore="";
        for (int i = 0; i < team_Scores.size(); i++) {    // load by sorted scores
            score_inst = team_Scores.get(i);
//            Log.w(TAG, i +" team=" + score_inst.getTeamNum());
            HashMap<String, String> temp = new HashMap<String, String>();
            tn = score_inst.getTeamNum();

            teamData(tn);   // Get Team's Match Data
            switch (sortType) {
                case "Climb":
                    totalScore = "[" + String.format("%3.2f", score_inst.getClimbScore()) + "]";
                    break;
                case "Cubes":
                    totalScore = "[" + String.format("%3.2f", score_inst.getCubeScore()) + "]";
                    break;
                case "Weighted":
                    totalScore = "[" + String.format("%3.2f", score_inst.getWeightedScore()) + "]";
                    break;
                case "Team#":
                    totalScore=" ";
                    break;
                default:                //
                    Log.e(TAG, "Invalid Sort - " + sortType);
            }

            temp.put("team", tn + " - " + score_inst.getTeamName() + "   (" + mdNumMatches + ")   " +  totalScore);
//            temp.put("BA", "Rank=" + teams[i].rank + "  " + teams[i].record + "   OPR=" + String.format("%3.1f", (teams[i].opr)) + "    ↑ " + String.format("%3.1f", (teams[i].touchpad)) + "   kPa=" + String.format("%3.1f", (teams[i].pressure)));
            temp.put("Stats", "Auto ⚻ " + autoSwRatio + " ➽ " + autoSwXover + "  ⚖ " + autoScRatio + " ➽ " + autoScXover+ "    Tele ⚻ " + teleSwRatio + "  Oth⚻ " + teleOthrRatio +  "   ⚖ " + teleScRatio + "   Exch " + teleExch + "  Port " + telePort);
            temp.put("BA",  "Climb " + "  " + climbRatio + "  on⚌" + onPlatform + "  ↕One " + liftOne + "  ↕Two " + liftTwo + "    Was↑ " + gotLifted + "    ▉P/U  " + cubeChk);
            draftList.add(temp);
        } // End For
        Log.w(TAG, "### Teams ###  : " + draftList.size());
        lstView_Teams.setAdapter(adaptTeams);
        adaptTeams.notifyDataSetChanged();

    }

//    private void loadTeams() {
//        Log.i(TAG, "@@@@  loadTeams started  @@@@");
//// ----------  Blue Alliance  -----------
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
//        StrictMode.setThreadPolicy(policy);
//        TBA.setID("Pearadox", "Scout-5414", "V1");
//        final TBA tba = new TBA();
//        Settings.FIND_TEAM_RANKINGS = true;
//        Settings.GET_EVENT_TEAMS = true;
//        Settings.GET_EVENT_MATCHES = true;
//        Settings.GET_EVENT_ALLIANCES = true;
//        Settings.GET_EVENT_AWARDS = true;
//        Settings.GET_EVENT_STATS = true;
//        String tn = "";
//
//        TBA t = new TBA();
//        Event e = t.getEvent(Pearadox.FRC_ChampDiv, 2017);
//        teams = e.teams.clone();
////        Team[] teams1 = e.teams;
//        Log.e(TAG, Pearadox.FRC_ChampDiv + "Teams= " + teams.length);
//        draftList.clear();
//        BAnumTeams = e.teams.length;
//        if (BAnumTeams > 0) {
//            for (int i = 0; i < teams.length; i++) {
//                HashMap<String, String> temp = new HashMap<String, String>();
//                if (String.valueOf(teams[i].team_number).length() < 4) {
//                    tn = " " + String.valueOf(teams[i].team_number);    // Add leading blank
//                } else {
//                    tn = String.valueOf(teams[i].team_number);
//                }
//
//                teamData(tn);   // Get Team's Match Data
//
//                temp.put("team", tn + " - " + teams[i].nickname);
//                temp.put("BA", "Rank=" + teams[i].rank + "  " + teams[i].record + "   OPR=" + String.format("%3.1f", (teams[i].opr)) + "    ↑ " + String.format("%3.1f", (teams[i].touchpad)) + "   kPa=" + String.format("%3.1f", (teams[i].pressure)));
//                temp.put("Stats", "Auto Gears=" + autoSwRatio + "  Tele Gears=" + teleSwRatio + "   Pick up Gears " + cubeChk + "   Climb " + climbChk + "  " + climbRatio);
//                draftList.add(temp);
//                                                      } // End For
//                Log.w(TAG, "### Teams ###  : " + draftList.size());
//
//        } else {
//            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
//            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
//            Toast toast = Toast.makeText(getBaseContext(), "***  Data from the Blue Alliance is _NOT_ available this session  ***", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//            toast.show();
//        }
//
//    }
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_draft, menu);
    return true;
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, DraftSettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_help) {
            Intent help_intent = new Intent(this, HelpActivity.class);
            startActivity(help_intent);  	// Show Help
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


/* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    private void teamData(String team) {
//        Log.i(TAG, "$$$$  teamData  $$$$ " + team);
        int autoCubeSw = 0; int autoCubeSwAtt = 0; int autoCubeSc = 0; int autoCubeScAtt = 0; int autoSwXnum = 0;  int autoScXnum = 0;
        int teleCubeSw = 0; int teleCubeSwAtt = 0; int teleCubeSc = 0; int teleCubeScAtt = 0;
        int teleCubeExch = 0; int teleOthrNUM = 0;  int teleOthrATT = 0; int telePortalNUM = 0; int teleZoneNUM = 0; int teleFloorNUM = 0; int teleTheirNUM = 0;
        int climbs = 0; int climbAttemps = 0; int platNum = 0; int lift1Num = 0; int lift2Num = 0; int liftedNum = 0;
        int numMatches = 0;
        boolean cube_pu =false;

        for (int i = 0; i < All_Matches.size(); i++) {
            match_inst = All_Matches.get(i);      // Get instance of Match Data
            if (match_inst.getTeam_num().matches(team)) {
//                Log.e(TAG, i + "  " + match_inst.getMatch() + "  Team=" + team);
                numMatches++;
                if (match_inst.isAuto_cube_switch()) {
                    autoCubeSw++;
                }
                if (match_inst.isAuto_cube_switch_att()) {
                    autoCubeSwAtt++;
                }
                if (match_inst.isAuto_xover_switch()) {
                    autoSwXnum++;
                }
                if (match_inst.isAuto_cube_scale()) {
                    autoCubeSc++;
                }
                if (match_inst.isAuto_cube_scale_att()) {
                    autoCubeScAtt++;
                }
                if (match_inst.isAuto_xover_scale()) {
                    autoScXnum++;
                }
                teleCubeSw = teleCubeSw + match_inst.getTele_cube_switch();
                teleCubeSwAtt = teleCubeSwAtt + match_inst.getTele_switch_attempt();
                teleCubeSc = teleCubeSc + match_inst.getTele_cube_scale();
                teleCubeScAtt = teleCubeScAtt + match_inst.getTele_scale_attempt();
                teleCubeExch = teleCubeExch + match_inst.getTele_cube_exchange();
                telePortalNUM = telePortalNUM + match_inst.getTele_cube_portal();
                teleZoneNUM = teleZoneNUM + match_inst.getTele_cube_pwrzone();
                teleFloorNUM = teleFloorNUM + match_inst.getTele_cube_floor();
                teleTheirNUM = teleTheirNUM + match_inst.getTele_their_floor();
                teleOthrNUM = teleOthrNUM + match_inst.getTele_their_switch();
                teleOthrATT = teleOthrATT + match_inst.getTele_their_attempt();
                if (match_inst.isTele_cube_pickup()) {
                    cube_pu = true;
                }
                if (match_inst.isTele_climb_success()) {
                    climbs++;
                }
                if (match_inst.isTele_climb_attempt()) {
                    climbAttemps++;
                }
                if (match_inst.isTele_lift_one()) {
                    lift1Num++;
                }
                if (match_inst.isTele_lift_two()) {
                    lift2Num++;
                }
                if (match_inst.isTele_on_platform()) {
                    platNum++;
                }
                if (match_inst.isTele_got_lift()) {
                    liftedNum++;
                }
//                Log.w(TAG, "matches= " + numMatches);
            } //End if teams equal
        } // End For _ALL_ matches
        mdNumMatches = String.valueOf(numMatches);
        if (numMatches > 0) {
            if (cube_pu) {
                cubeChk = "❎";
            } else {
                cubeChk = "⎕";
            }
            autoSwRatio = autoCubeSw + "/" + autoCubeSwAtt;
            autoScRatio = autoCubeSc + "/" + autoCubeScAtt;
            autoSwXover = String.valueOf(autoSwXnum);
            autoScXover = String.valueOf(autoScXnum);
            teleSwRatio = teleCubeSw + "/" + teleCubeSwAtt;
            teleScRatio = teleCubeSc + "/" + teleCubeScAtt;
            teleOthrRatio = teleOthrNUM + "/" + teleOthrATT;
            teleExch = String.valueOf(teleCubeExch);
            telePort = String.valueOf(telePortalNUM);
            climbRatio = climbs + "/" + climbAttemps;
            liftOne = String.valueOf(lift1Num);
            liftTwo = String.valueOf(lift2Num);
            onPlatform = String.valueOf(platNum);
            gotLifted = String.valueOf(liftedNum);
        } else {
            cubeChk = "⎕";
            autoSwRatio = "0/0";
            autoScRatio = "0/0";
            autoSwXover = "0";
            autoScXover = "0";
            teleSwRatio = "0/0";
            teleScRatio = "0/0";
            teleOthrRatio = "0/0";
            teleExch = "0";
            telePort = "0";
            climbRatio = "0/0";
            liftOne = "0";
            liftTwo = "0";
            onPlatform = "0";
            gotLifted = "0";
        }
        //============================
// ToDo - get multipliers from preferences
        float climbScore = 0; float cubeScored = 0; float cubeCollect = 0; float cubeScore = 0;  float weightedScore = 0;
//        Log.e(TAG, team + " "+ climbs + " "+ lift1Num + " "+ lift2Num + " " + platNum +  " " + liftedNum + " / " + numMatches);
        if (numMatches > 0) {
            climbScore = (float) (((climbs + (lift1Num * 1.5) + (lift2Num * 2.0)) + (platNum * 0.15) + (liftedNum * 0.3)) / numMatches);
            cubeScored = (float) ((autoCubeSw + autoCubeSc + teleCubeSw + teleCubeSc + teleOthrNUM + teleCubeExch) / numMatches);
            cubeCollect = (float) ((telePortalNUM + teleZoneNUM + teleFloorNUM + teleTheirNUM) / numMatches);
            cubeScore = (float) ((cubeScored * 2) + cubeCollect);
            weightedScore = ((climbScore + cubeScore) / numMatches);
//            Log.w(TAG, team + " **Scores**  Climb = " + String.format("%3.2f", climbScore) + "  Cubes Scored = " + String.format("%3.2f", cubeScored) + "  Cubes Collected = " + String.format("%3.2f", cubeCollect) + "  Cubes Score = " + String.format("%3.2f", cubeScore) + "  Weighted Score = " + String.format("%3.2f", weightedScore));
        } else {
            climbScore = 0;
            cubeScore = 0;
            weightedScore = 0;
        }
        String tNumber="";
        for (int i = 0; i < team_Scores.size(); i++) {    // load by sorted scores
            Scores score_data = new Scores();
            score_data = team_Scores.get(i);
            tNumber = score_data.getTeamNum();
            if (score_data.getTeamNum().matches(team)) {
//                Log.w(TAG, "score team=" + score_data.getTeamNum());
                score_data.setClimbScore(climbScore);           // Save
                score_data.setCubeScore(cubeScore);             //  all
                score_data.setWeightedScore(weightedScore);     //   scores
            }
        }
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@ @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void addMD_VE_Listener(final DatabaseReference pfMatchData_DBReference) {
        pfMatchData_DBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "<<<< getFB_Data >>>> _ALL_ Match Data ");
                All_Matches.clear();
                matchData mdobj = new matchData();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();   /*get the data children*/
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    mdobj = iterator.next().getValue(matchData.class);
                    All_Matches.add(mdobj);
                }
                Log.w(TAG, "addMD_VE *****  Matches Loaded. # = "  + All_Matches.size());
                Button btn_Match = (Button) findViewById(R.id.btn_Match);   // Listner defined in Layout XML
                btn_Match.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*listener failed or was removed for security reasons*/
            }
        });
        loadTeams();
    }

    private void initScores() {
        Log.w(TAG, " ## initScores ##  " + is_Resumed);
        if (!is_Resumed) {
            team_Scores.clear();
            for (int i = 0; i < Pearadox.numTeams; i++) {
                Scores curScrTeam = new Scores();       // instance of Scores object
                team_inst = Pearadox.team_List.get(i);
                curScrTeam.setTeamNum(team_inst.getTeam_num());
                curScrTeam.setTeamName(team_inst.getTeam_name());
//            Log.w(TAG, curScrTeam.getTeamNum() + "  " + curScrTeam.getTeamName());
                curScrTeam.setClimbScore((float) 0);
                curScrTeam.setCubeScore((float) 0);
                curScrTeam.setWeightedScore((float) 0);
                team_Scores.add(i, curScrTeam);
            }
            Log.w(TAG, "#Scores = " + team_Scores.size());
        } else {
            // ToDONE - Load teams according to Radio Button (VisMatch return messes it up)
//            Log.w(TAG, "Leave scores alone '"  + sortType + "'");
            radgrp_Sort = (RadioGroup) findViewById(R.id.radgrp_Sort);
            radgrp_Sort.setActivated(true);
            radgrp_Sort.setSelected(true);
            radgrp_Sort.performClick();         // "force" radio button click
        }
    }

//###################################################################
//###################################################################
//###################################################################
@Override
public void onStart() {
    super.onStart();
    Log.v(TAG, "onStart");
    initScores();
    addMD_VE_Listener(pfMatchData_DBReference);        // Load _ALL_ Matches
    }
@Override
public void onResume() {
    super.onResume();
    Log.v(TAG, "****> onResume <**** " + sortType);
    is_Resumed = true;
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


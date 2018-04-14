package com.pearadox.scout_5414;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import static android.util.Log.i;

public class DraftScout_Activity extends AppCompatActivity {

    String TAG = "DraftScout_Activity";        // This CLASS name
    Boolean is_Resumed = false;
    int numPicks = 24;              // # of picks to show for Alliance Picks
    /*Shared Prefs-Scored Cubes*/ public String cubeAutoSw  = ""; public String cubeAutoSc  = ""; public String cubeTeleSw  = ""; public String cubeTeleSc  = ""; public String teleOthr  = ""; public String cubeExch = "";
    /*Shared Prefs-Retrieved Cbs*/ public String cubeColPort = ""; public String cubeColZone  = ""; public String cubeColFloor  = ""; public String cubeColStolen  = "";  public String cubeColRandom  = "";
    /*Shared Prefs-Climbing*/ public String climbClimbs = ""; public String climbLift1 = ""; public String climbLift2 = ""; public String climbPlat = ""; public String climbLifted = "";
    /*Weight factors*/ public String wtClimb = ""; public String wtCubeScore = ""; public String wtCubeCollct = "";
    ImageView imgStat_Load;
    TextView txt_EventName, txt_NumTeams, txt_Formula, lbl_Formula, txt_LoadStatus, txt_SelNum;
    ListView lstView_Teams;
    TextView TeamData, BA, Stats;
    Button btn_Match, btn_Default;
    RadioGroup radgrp_Sort;
    RadioButton radio_Climb, radio_Cubes, radio_Weight, radio_Team, radio_Switch, radio_Scale, radio_Exchange;
    //    Button btn_Up, btn_Down, btn_Delete;
    public ArrayAdapter<String> adaptTeams;
    //    ArrayList<String> draftList = new ArrayList<String>();
    static final ArrayList<HashMap<String, String>> draftList = new ArrayList<HashMap<String, String>>();
    public int teamSelected = -1;
    public static String sortType = "";
    String tNumb = "";
    String tn = "";
    String Viz_URL = "";
    String teamNum=""; String teamName = "";
    p_Firebase.teamsObj team_inst = new p_Firebase.teamsObj();
    //    Team[] teams;
    public static int BAnumTeams = 0;                                      // # of teams from Blue Alliance
    String cubeChk = "";
    String climbChk = "";
    String climbRatio = "";
    String autoSwRatio = "";
    String autoScRatio = "-/-";
    String autoSwXover = "";
    String autoScXover = "";
    String teleSwRatio = "-/-";
    String teleScRatio = "-/-";
    String mdNumMatches = "";
    String teleExch = "";
    String teleOthrRatio = "";
    String telePort = "";
    String teleZone = "";
    String cubeRandom = "";
    String liftOne = "";
    String liftTwo = "";
    String gotLifted = "";
    String onPlatform = "";
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfMatchData_DBReference;
    FirebaseStorage storage;
    StorageReference storageRef;
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
        private float switchScore;
        private float scaleScore;
        private float exchangeScore;

        public Scores() {
        }

        public Scores(String teamNum, String teamName, float cubeScore, float climbScore, float weightedScore, float switchScore, float scaleScore, float exchangeScore) {
            this.teamNum = teamNum;
            this.teamName = teamName;
            this.cubeScore = cubeScore;
            this.climbScore = climbScore;
            this.weightedScore = weightedScore;
            this.switchScore = switchScore;
            this.scaleScore = scaleScore;
            this.exchangeScore = exchangeScore;
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

        public float getSwitchScore() {
            return switchScore;
        }

        public void setSwitchScore(float switchScore) {
            this.switchScore = switchScore;
        }

        public float getScaleScore() {
            return scaleScore;
        }

        public void setScaleScore(float scaleScore) {
            this.scaleScore = scaleScore;
        }

        public float getExchangeScore() {
            return exchangeScore;
        }

        public void setExchangeScore(float exchangeScore) {
            this.exchangeScore = exchangeScore;
        }

        public static Comparator<Scores> teamComp = new Comparator<Scores>() {
            public int compare(Scores t1, Scores t2) {
                String TeamNum1 = t1.getTeamNum();
                String TeamNum2 = t2.getTeamNum();
                //ascending order
                return TeamNum1.compareTo(TeamNum2);
                //descending order
                //return TeamNum2.compareTo(TeamNum1);
            }
        };
        public static Comparator<Scores> climbComp = new Comparator<Scores>() {
            public int compare(Scores s1, Scores s2) {
                float climbNum1 = s1.getClimbScore();
                float climbNum2 = s2.getClimbScore();
	            /*For ascending order*/
                //return climbNum1-climbNum2;
	            /*For descending order*/
                return (int) (climbNum2 - climbNum1);
            }
        };
    }

    //==========================
    public ArrayList<Scores> team_Scores = new ArrayList<Scores>();
    Scores score_inst = new Scores();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_scout);
        Log.i(TAG, "@@@@@ DraftScout_Activity  @@@@@");
        Log.e(TAG, "B4 - " + sortType);
        if (savedInstanceState != null) {
            Log.w(TAG, "Are we ever getting called? " + is_Resumed);
            SharedPreferences prefs = getPreferences(MODE_PRIVATE);
            String sortType = prefs.getString("Sort", "");
        } else {
//            sortType = "Team#";
        }
        Log.e(TAG, "After - " + sortType);
        getprefs();         // Get multiplier values from Preferences

        txt_EventName = (TextView) findViewById(R.id.txt_EventName);
        txt_NumTeams = (TextView) findViewById(R.id.txt_NumTeams);
        txt_Formula = (TextView) findViewById(R.id.txt_Formula);
        lbl_Formula = (TextView) findViewById(R.id.lbl_Formula);
        txt_LoadStatus = (TextView) findViewById(R.id.txt_LoadStatus);
        txt_SelNum = (TextView) findViewById(R.id.txt_SelNum);
        txt_SelNum.setText("");
        lstView_Teams = (ListView) findViewById(R.id.lstView_Teams);
        txt_EventName.setText(Pearadox.FRC_EventName);              // Event Name
        txt_NumTeams.setText(String.valueOf(Pearadox.numTeams));    // # of Teams
        txt_Formula.setText(" ");

        pfDatabase = FirebaseDatabase.getInstance();
        pfMatchData_DBReference = pfDatabase.getReference("match-data/" + Pearadox.FRC_Event);    // Match Data

        RadioGroup radgrp_Sort = (RadioGroup) findViewById(R.id.radgrp_Sort);
        radgrp_Sort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i(TAG, "@@ RadioClick_Sort @@");
                txt_SelNum = (TextView) findViewById(R.id.txt_SelNum);
                txt_SelNum.setText("");
                teamSelected = -1;
                radio_Team = (RadioButton) findViewById(checkedId);
                String value = radio_Team.getText().toString();
                Log.w(TAG, "RadioSort -  '" + value + "'");
                switch (value) {
                    case "Climb":
//                        Log.w(TAG, "Climb sort");
                        sortType = "Climb";
                        Collections.sort(team_Scores, new Comparator<Scores>() {
                            @Override
                            public int compare(Scores c1, Scores c2) {
                                return Float.compare(c1.getClimbScore(), c2.getClimbScore());
                            }
                        });
                        Collections.reverse(team_Scores);
                        showFormula(sortType);              // update the formula
                        loadTeams();
                        break;
                    case "Cubes":
                        sortType = "Cubes";
//                      Log.w(TAG, "Cube sort");
                        Collections.sort(team_Scores, new Comparator<Scores>() {
                            @Override
                            public int compare(Scores c1, Scores c2) {
                                return Float.compare(c1.getCubeScore(), c2.getCubeScore());
                            }
                        });
                        Collections.reverse(team_Scores);   // Descending
                        showFormula("Cubes");              // update the formula
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
                        Collections.reverse(team_Scores);   // Descending
                        showFormula(sortType);              // update the formula
                        loadTeams();
                        break;
                    case "Switch":
                        sortType = "Switch";
//                        Log.w(TAG, "Switch sort");
                        Collections.sort(team_Scores, new Comparator<Scores>() {
                            @Override
                            public int compare(Scores c1, Scores c2) {
                                return Float.compare(c1.getSwitchScore(), c2.getSwitchScore());
                            }
                        });
                        Collections.reverse(team_Scores);   // Descending
                        showFormula(sortType);              // update the formula
                        loadTeams();
                        break;
                    case "Scale":
                        sortType = "Scale";
//                        Log.w(TAG, "Scale sort");
                        Collections.sort(team_Scores, new Comparator<Scores>() {
                            @Override
                            public int compare(Scores c1, Scores c2) {
                                return Float.compare(c1.getScaleScore(), c2.getScaleScore());
                            }
                        });
                        Collections.reverse(team_Scores);   // Descending
                        showFormula(sortType);              // update the formula
                        loadTeams();
                        break;
                    case "Exchange":
                        sortType = "Exchange";
//                        Log.w(TAG, "Exchange sort");
                        Collections.sort(team_Scores, new Comparator<Scores>() {
                            @Override
                            public int compare(Scores c1, Scores c2) {
                                return Float.compare(c1.getExchangeScore(), c2.getExchangeScore());
                            }
                        });
                        Collections.reverse(team_Scores);   // Descending
                        showFormula(sortType);              // update the formula
                        loadTeams();
                        break;
                    case "Team#":
//                Log.w(TAG, "Team# sort");
                        sortType = "Team#";
                        Collections.sort(team_Scores, Scores.teamComp);
                        lbl_Formula.setTextColor(Color.parseColor("#ffffff"));
                        txt_Formula.setText(" ");       // set formulat to blank
                        loadTeams();
                        break;
                    default:                //
                        Log.e(TAG, "*** Invalid Sort " + value);
                }

            }
        });


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
        lstView_Teams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View view, int pos, long id) {
                Log.w(TAG, "*** lstView_Teams ***   Item Selected: " + pos);
                teamSelected = pos;
                lstView_Teams.setSelector(android.R.color.holo_blue_light);
        		/* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
//                tnum = draftList.get(teamSelected).substring(0,4);
                txt_SelNum = (TextView) findViewById(R.id.txt_SelNum);
                txt_SelNum.setText(String.valueOf(pos+1));      // Sort Position
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            }
        });
    }

    private void getprefs() {
        Log.i(TAG, "** getprefs **");

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);

        cubeAutoSw = sharedPref.getString("prefCube_autoSw", "1.0");
        cubeAutoSc = sharedPref.getString("prefCube_autoSc", "1.0");
        cubeTeleSw = sharedPref.getString("prefCube_teleSw", "1.0");
        cubeTeleSc = sharedPref.getString("prefCube_teleSc", "1.0");
        teleOthr =   sharedPref.getString("prefCube_othr", "1.0");
        cubeExch =   sharedPref.getString("prefCube_exchange", "1.0");

        cubeColPort = sharedPref.getString("prefCubeCol_portal", "1.0");
        cubeColZone = sharedPref.getString("prefCubeCol_zone", "1.0");
        cubeColFloor = sharedPref.getString("prefCubeCol_floor", "1.0");
        cubeColStolen = sharedPref.getString("prefCubeCol_stolen", "1.0");
        cubeColRandom = sharedPref.getString("prefCubeCol_random", "1.0");

        climbClimbs = sharedPref.getString("prefClimb_NumClimbs", "1.0");
        climbLift1 = sharedPref.getString("prefClimb_lift1", "1.5");
        climbLift2 = sharedPref.getString("prefClimb_lift2", "2.0");
        climbPlat = sharedPref.getString("prefClimb_onPlat", "0.3");
        climbLifted = sharedPref.getString("prefClimb_lifted", "0.3");

        wtCubeScore = sharedPref.getString("prefWeight_cubesScored", "1.0");
        wtCubeCollct = sharedPref.getString("prefWeight_cubesCollected", "1.0");
        wtClimb = sharedPref.getString("prefWeight_climb", "1.0");

        numPicks = Integer.parseInt(sharedPref.getString("prefAlliance_num", "24"));


    }

    private String showFormula(String typ) {
        Log.i(TAG, "** showFormula **  " + typ);
        String form = "";
        getprefs();         // make sure Prefs are up to date
        switch (typ) {
            case "Climb":
//                Log.e(TAG, "*** CLIMB   cl=" + climbClimbs + " 1=" + climbLift1 + " 2=" + climbLift2 + " pl=" + climbPlat + " was=" + climbLifted);
                form = "(" + climbClimbs + "*(climbs) + " +"(Lift1*" + climbLift1 + ") + " +"(Lift2*" + climbLift2 + ") + (Plat*" + climbPlat + ") + (WasLifted*" + climbLifted + ")) / # matches";
                lbl_Formula.setTextColor(Color.parseColor("#4169e1"));      // blue
                txt_Formula.setText(form);
                break;
            case "Cubes":
                form = "SCR:  (" + cubeAutoSw +"*(aCSw)+" + cubeAutoSc + "*(aCSc)+" + cubeTeleSw + "*(tCSw)+" + cubeTeleSc + "*(tCSc)+" + teleOthr + "*(oth)+" + cubeExch + "*(Exc)) / # matches  ✚ \n";
                form = form + "COL:  (" + cubeColPort +"*(Port) + " + cubeColZone + "*(zone) + " + cubeColFloor + "*(flr) + " + cubeColStolen + "*(Their) " + cubeColRandom + "*(Random)) / # matches";
                lbl_Formula.setTextColor(Color.parseColor("#ee00ee"));      // magenta
                txt_Formula.setText(form);
                break;
            case "Weighted":
                form = "((" + wtClimb + "*(climbScore) + " + wtCubeScore + "*(cubesScored) + " + wtCubeCollct + "*(cubesCollected)) / #matches";
                lbl_Formula.setTextColor(Color.parseColor("#ff0000"));      // red
                txt_Formula.setText(form);
                break;
            case "Switch":
                form = "(" + cubeAutoSw +"*(aCSw) + " + cubeTeleSw + "*(tCSw) + " + teleOthr + "*(oth)) / #matches";
                lbl_Formula.setTextColor(Color.parseColor("#00eeee"));          // cyan
                txt_Formula.setText(form);
                break;
            case "Scale":
                form = "(" + cubeAutoSc + "*(aCSc) + " + cubeTeleSc + "*(tCSc) / #matches";
                lbl_Formula.setTextColor(Color.parseColor("#32cd32"));      // lime green
                txt_Formula.setText(form);
                break;
            case "Exchange":
                form = "(Exc) / #matches";
                lbl_Formula.setTextColor(Color.parseColor("#a8a8a8"));      /// grey
                txt_Formula.setText(form);
                break;
            default:                //
                Log.e(TAG, "*** Invalid Type " + typ);
        }
        return typ;
    }


    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void buttonDefault_Click(View view) {
        // Reload _ALL_ the Preference defaults
        Log.i(TAG, ">>>>> buttonDefault_Click" );
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();

        showFormula(sortType);              // update the formula
        loadTeams();                        // reload based on default
    }

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void buttonMatch_Click(View view) {
        Log.i(TAG, ">>>>> buttonMatch_Click  " + teamSelected);
        HashMap<String, String> temp = new HashMap<String, String>();
        String teamHash;

        if (teamSelected >= 0) {
            draftList.get(teamSelected);
            temp = draftList.get(teamSelected);
            teamHash = temp.get("team");
//        Log.w(TAG, "teamHash: '" + teamHash + "' \n ");
            load_team = teamHash.substring(0, 4);
            load_name = teamHash.substring(7, teamHash.indexOf("("));  // UP TO # MATCHES
//        Log.w(TAG, ">>>team & name: '" + load_team + "'  [" + load_name +"]");
            addMatchData_Team_Listener(pfMatchData_DBReference);        // Load Matches for _THIS_ selected team
        } else {
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            Toast toast = Toast.makeText(getBaseContext(), "★★★★  There is _NO_ Team selected for Match Data ★★★★", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }


    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void buttonPit_Click(View view) {
        Log.i(TAG, ">>>>> buttonPit_Click  " + teamSelected);
        HashMap<String, String> temp = new HashMap<String, String>();
        String teamHash="";
        final String[] URL = {""};

        if (teamSelected >= 0) {
            draftList.get(teamSelected);
            temp = draftList.get(teamSelected);
            teamHash = temp.get("team");
            teamNum = teamHash.substring(0, 4);
            teamName = teamHash.substring(7, teamHash.indexOf("("));  // UP TO # MATCHES
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl("gs://paradox-2017.appspot.com/images/" + Pearadox.FRC_Event).child("robot_" + teamNum.trim() + ".png");
//            Log.e(TAG, "gs://paradox-2017.appspot.com/images/" + Pearadox.FRC_Event + ".child(robot_" + teamNum.trim() + ".png)");
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
//                    Log.w(TAG, "URI=" + uri);
                    URL[0] = uri.toString();
//                    Log.w(TAG, "URL=" + URL[0]);
                    Viz_URL = URL[0];
//                    Log.w(TAG, "Team '" + teamNum + "'  '" + teamName + "'  URL=" + Viz_URL);
                    launchVizPit(teamNum, teamName, Viz_URL);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e(TAG, "ERR= '" + exception + "'");
                }
            });
        } else {
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            Toast toast = Toast.makeText(getBaseContext(), "★★★★  There is _NO_ Team selected for Pit Data ★★★★", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }


    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void launchVizPit(String team, String name, String imgURL) {
        i(TAG,">>>>>>>>  launchVizPit " + team + " " + name + " " + imgURL);      // ** DEBUG **
        Intent pit_intent = new Intent(DraftScout_Activity.this, VisPit_Activity.class);
        Bundle VZbundle = new Bundle();
        VZbundle.putString("team", team);        // Pass data to activity
        VZbundle.putString("name", name);        // Pass data to activity
        VZbundle.putString("url", imgURL);       // Pass data to activity
        pit_intent.putExtras(VZbundle);
        startActivity(pit_intent);               // Start Visualizer for Pit Data

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
        Log.w(TAG, "@@@@  loadTeams started  @@@@  " + team_Scores.size() + " Type=" + sortType);

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
                case "Switch":
                    totalScore = "[" + String.format("%3.2f", score_inst.getSwitchScore()) + "]";
                    break;
                case "Scale":
                    totalScore = "[" + String.format("%3.2f", score_inst.getScaleScore()) + "]";
                    break;
                case "Exchange":
                    totalScore = "[" + String.format("%3.2f", score_inst.getExchangeScore()) + "]";
                    break;
                case "Team#":
                    totalScore=" ";
                    break;
                default:                //
                    Log.e(TAG, "Invalid Sort - " + sortType);
            }

            temp.put("team", tn + " - " + score_inst.getTeamName() + "   (" + mdNumMatches + ")   " +  totalScore);
//            temp.put("BA", "Rank=" + teams[i].rank + "  " + teams[i].record + "   OPR=" + String.format("%3.1f", (teams[i].opr)) + "    ↑ " + String.format("%3.1f", (teams[i].touchpad)) + "   kPa=" + String.format("%3.1f", (teams[i].pressure)));
            temp.put("Stats", "Auto ⚻ " + autoSwRatio + " ➽ " + autoSwXover + "  ⚖ " + autoScRatio + " ➽ " + autoScXover+ "  Tele ⚻ " + teleSwRatio + " Oth⚻ " + teleOthrRatio +  "  ⚖ " + teleScRatio + "  Exch " + teleExch + " Port " + telePort + " Zone " + teleZone);
            temp.put("BA",  "Climb " + "  " + climbRatio + "  ↕One " + liftOne + "  ↕Two " + liftTwo + "   onPlat⚌" + onPlatform+ "    Was↑ " + gotLifted + "    ▉P/U  " + cubeChk);
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
        Log.e(TAG, "@@@  Options  @@@ " + sortType);
        Log.w(TAG, " \n  \n");
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
        if (id == R.id.action_picks) {
            Intent help_intent = new Intent(this, HelpActivity.class);
            Log.e(TAG, "Picks");
            alliance_Picks();
            return true;
        }
        if (id == R.id.action_screen) {
            String filNam = Pearadox.FRC_Event.toUpperCase() + "-Draft"  + "_" + sortType + ".JPG";
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

    private void alliance_Picks() {
        String tName = ""; String totalScore=""; String DS = "";
        String underScore = new String(new char[30]).replace("\0", "_");    // string of 'x' underscores
        String blanks = new String(new char[50]).replace("\0", " ");        // string of 'x' blanks
        sortType = "Weighted";          // Attempt to "force" correct sort 1st time
        Collections.sort(team_Scores, new Comparator<Scores>() {
            @Override
            public int compare(Scores c1, Scores c2) {
                return Float.compare(c1.getWeightedScore(), c2.getWeightedScore());
            }
        });
        Collections.reverse(team_Scores);   // Descending
        loadTeams();

        if (numPicks > team_Scores.size()) {
//            Log.w(TAG, "******>> numPick changed to: " + team_Scores.size());
            numPicks = team_Scores.size();      // Use max (prevent Error when # teams < 'numPicks')
        }
        if (numPicks > 24) {
            DS = "";
        }else {
            DS = "\n";
        }
        try {
            String destFile = Pearadox.FRC_ChampDiv + "_Alliance-Picks" + ".txt";
            File prt = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/" + destFile);
//            Log.e(TAG, " path = " + prt);
//            BufferedWriter bW;
//            bW = new BufferedWriter(new FileWriter(prt, false));    // true = Append to existing file
            BufferedWriter bW = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(prt), "UTF-8"
            ));
            bW.write(Pearadox.FRC_ChampDiv + " - " + Pearadox.FRC_EventName +  "\n");
            bW.write(underScore + "  SWITCH  " + underScore +  "\n \n");
            //  Switch sort
            sortType = "Switch";
            Collections.sort(team_Scores, new Comparator<Scores>() {
                @Override
                public int compare(Scores c1, Scores c2) {
                    return Float.compare(c1.getSwitchScore(), c2.getSwitchScore());
                }
            });
            Collections.reverse(team_Scores);   // Descending
            loadTeams();
            for (int i = 0; i < numPicks; i++) {    // load by sorted scores
                score_inst = team_Scores.get(i);
//                Log.w(TAG, " Scores = " + score_inst.teamNum + " " + score_inst.switchScore + " " + score_inst.scaleScore + " " + score_inst.exchangeScore );
                tNumb = score_inst.getTeamNum();
                tName = score_inst.getTeamName();
                tName = tName + blanks.substring(0, (36 - tName.length()));
                totalScore = "[" + String.format("%3.2f", score_inst.getSwitchScore()) + "]";
                teamData(tNumb);   // Get Team's Match Data
                bW.write(String.format("%2d", i+1) + ") " + tNumb + "-" + tName + " \t (" + String.format("%2d",(Integer.parseInt(mdNumMatches))) + ") " +  totalScore + "\t");
                bW.write("A⚻ " + autoSwRatio + " ➽ " + autoSwXover + "  T⚻ " + teleSwRatio + " Oth⚻ " + teleOthrRatio+ "\n" + DS);
            } // end For # teams
            bW.write(" \n" + "\n" + (char)12);        // NL & FF
            //=====================================================================

            bW.write(Pearadox.FRC_ChampDiv + " - " + Pearadox.FRC_EventName +  "\n");
            bW.write(underScore + "  SCALE  " + underScore +  "\n \n");
            sortType = "Scale";
            Collections.sort(team_Scores, new Comparator<Scores>() {
                @Override
                public int compare(Scores c1, Scores c2) {
                    return Float.compare(c1.getScaleScore(), c2.getScaleScore());
                }
            });
            Collections.reverse(team_Scores);   // Descending
            loadTeams();
            for (int i = 0; i < numPicks; i++) {    // load by sorted scores
                score_inst = team_Scores.get(i);
                tNumb = score_inst.getTeamNum();
                tName = score_inst.getTeamName();
                tName = tName + blanks.substring(0, (36 - tName.length()));
                totalScore = "[" + String.format("%3.2f", score_inst.getScaleScore()) + "]";
                teamData(tNumb);   // Get Team's Match Data
                bW.write(String.format("%2d", i+1) +") " + tNumb + " - " + tName + " \t  (" + String.format("%2d",(Integer.parseInt(mdNumMatches))) + ") " +  totalScore + " \t");
                bW.write(" A⚖ " + autoScRatio + " ➽ " + autoScXover + "  T⚖ " + teleScRatio+ "\n" + DS);
            } // end For # teams
            bW.write(" \n" + "\n" + (char)12);        // NL & FF
            //=====================================================================

            bW.write(Pearadox.FRC_ChampDiv + " - " + Pearadox.FRC_EventName +  "\n");
            bW.write(underScore + "  EXCHANGE  " + underScore +  "\n " + DS);
            sortType = "Exchange";
            Collections.sort(team_Scores, new Comparator<Scores>() {
                @Override
                public int compare(Scores c1, Scores c2) {
                    return Float.compare(c1.getExchangeScore(), c2.getExchangeScore());
                }
            });
            Collections.reverse(team_Scores);   // Descending
            loadTeams();
            for (int i = 0; i < numPicks; i++) {    // load by sorted scores
                score_inst = team_Scores.get(i);
                tNumb = score_inst.getTeamNum();
                tName = score_inst.getTeamName();
                tName = tName + blanks.substring(0, (36 - tName.length()));
                totalScore = "[" + String.format("%3.2f", score_inst.getExchangeScore()) + "]";
                teamData(tNumb);   // Get Team's Match Data
                bW.write(String.format("%2d", i+1) +") " + tNumb + " - " + tName + " \t  (" + String.format("%2d",(Integer.parseInt(mdNumMatches))) + ")   " +  totalScore + "  \t");
                bW.write( "Exch " + teleExch + "\n" + DS);
            } // end For # teams
            bW.write(" \n" + "\n");        // NL
            //=====================================================================


            bW.flush();
            bW.close();
            Toast toast = Toast.makeText(getBaseContext(), "*** '" + Pearadox.FRC_Event + "' Alliance Picks file (" + numPicks + " teams) written to SD card [Download/FRC5414] ***", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage() + " not found in the specified directory.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    private void teamData(String team) {
//        Log.i(TAG, "$$$$  teamData  $$$$ " + team);
        int autoCubeSw = 0; int autoCubeSwAtt = 0; int autoCubeSc = 0; int autoCubeScAtt = 0; int autoSwXnum = 0;  int autoScXnum = 0;
        int teleCubeSw = 0; int teleCubeSwAtt = 0; int teleCubeSc = 0; int teleCubeScAtt = 0;
        int teleCubeExch = 0; int teleOthrNUM = 0;  int teleOthrATT = 0; int telePortalNUM = 0; int teleZoneNUM = 0; int teleFloorNUM = 0; int teleTheirNUM = 0; int teleRandomNUM = 0;
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
                teleRandomNUM = teleRandomNUM + match_inst.getTele_random_floor();
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
//                Log.w(TAG, "Accum. matches = " + numMatches);
            } //End if teams equal
        } // End For _ALL_ matches
//        Log.w(TAG, "####### Total Matches/Team = " + numMatches);
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
            teleZone = String.valueOf(teleZoneNUM);
            cubeRandom = String.valueOf(teleRandomNUM);
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
            teleZone  = "0";
            cubeRandom = "0";
            climbRatio = "0/0";
            liftOne = "0";
            liftTwo = "0";
            onPlatform = "0";
            gotLifted = "0";
        }
        //============================
        float climbScore = 0; float cubeScored = 0; float cubeCollect = 0; float cubeScore = 0; float weightedScore = 0; float switchScore = 0; float scaleScore = 0; float exchangeScore = 0;
//        Log.e(TAG, team + " "+ climbs + " "+ lift1Num + " "+ lift2Num + " " + platNum +  " " + liftedNum + " / " + numMatches);
        if (numMatches > 0) {
            climbScore = (float) (((climbs * Float.parseFloat(climbClimbs)) + (lift1Num * Float.parseFloat(climbLift1)) + (lift2Num * Float.parseFloat(climbLift2))) + (platNum * Float.parseFloat(climbPlat)) + (liftedNum * Float.parseFloat(climbLifted))) / numMatches;
            cubeScored = (float) ((autoCubeSw * Float.parseFloat(cubeAutoSw) + autoCubeSc * Float.parseFloat(cubeAutoSc) + teleCubeSw * Float.parseFloat(cubeTeleSw) + teleCubeSc * Float.parseFloat(cubeTeleSc) + teleOthrNUM * Float.parseFloat(teleOthr) + teleCubeExch * Float.parseFloat(cubeExch)) / numMatches);
            cubeCollect = (float) ((telePortalNUM * Float.parseFloat(cubeColPort) + teleZoneNUM * Float.parseFloat(cubeColZone) + teleFloorNUM * Float.parseFloat(cubeColFloor) + teleTheirNUM * Float.parseFloat(cubeColStolen) + teleRandomNUM * Float.parseFloat(cubeColRandom)) / numMatches);
            cubeScore = (float) ((cubeScored) + cubeCollect);
            weightedScore = ((climbScore* Float.parseFloat(wtClimb) + cubeScored * Float.parseFloat(wtCubeScore) + cubeCollect * Float.parseFloat(wtCubeCollct)) / numMatches);
            switchScore = (float) ((autoCubeSw * Float.parseFloat(cubeAutoSw) + (teleCubeSw * Float.parseFloat(cubeTeleSw)) + (teleOthrNUM * Float.parseFloat(teleOthr)))) / numMatches;
            scaleScore = (float) ((autoCubeSc * Float.parseFloat(cubeAutoSc) + teleCubeSc * Float.parseFloat(cubeTeleSc))) / numMatches;
            exchangeScore = (float) ((teleCubeExch * Float.parseFloat(cubeExch)) / numMatches);
//            Log.e(TAG, "Exch= " + teleCubeExch + "  #Matches=" + numMatches + "  Score=" + String.format("%3.2f", exchangeScore));
//            Log.w(TAG, team + " **Scores**  Climb = " + String.format("%3. ", climbScore) + "  Cubes Scored = " + String.format("%3.2f", cubeScored) + "  Cubes Collected = " + String.format("%3.2f", cubeCollect) + "  Cubes Score = " + String.format("%3.2f", cubeScore) + "  Weighted Score = " + String.format("%3.2f", weightedScore));
        } else {
            climbScore = 0;
            cubeScore = 0;
            weightedScore = 0;
            switchScore = 0;
            scaleScore = 0;
            exchangeScore = 0;
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
                score_data.setSwitchScore(switchScore);         //
                score_data.setScaleScore(scaleScore);           //
                score_data.setExchangeScore(exchangeScore);     //
            }
        }
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@ @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void addMD_VE_Listener(final DatabaseReference pfMatchData_DBReference) {
        pfMatchData_DBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "<<<< getFB_Data >>>> _ALL_ Match Data ");
                ImageView imgStat_Load = (ImageView) findViewById(R.id.imgStat_Load);
                txt_LoadStatus = (TextView) findViewById(R.id.txt_LoadStatus);
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
                imgStat_Load.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_green));
                txt_LoadStatus.setText("");
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
        Log.w(TAG, "Start to Load teams '"  + sortType + "'");
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
            curScrTeam.setSwitchScore((float) 0);
            curScrTeam.setScaleScore((float) 0);
            curScrTeam.setExchangeScore((float) 0);
            team_Scores.add(i, curScrTeam);
        } // end For
        Log.w(TAG, "#Scores = " + team_Scores.size());
        if (sortType.matches("") || sortType.matches("Team#")) {       // if 1st time
            sortType = "Team#";
        } else {
//            SharedPreferences prefs = getPreferences(MODE_PRIVATE);
//            String sortType = prefs.getString("Sort", "");
//
            // ToDONE - Load teams according to Radio Button (VisMatch return messes it up)
            Log.e(TAG, "Leave scores alone '"  + sortType + "'");
            radgrp_Sort = (RadioGroup) findViewById(R.id.radgrp_Sort);
            radgrp_Sort.setActivated(true);
            radgrp_Sort.setSelected(true);
            switch (sortType) {
                case "Climb":
                    radio_Climb = (RadioButton) findViewById(R.id.radio_Climb);
                    radio_Climb.performClick();         // "force" radio button click
                    break;
                case "Cubes":
                    radio_Cubes = (RadioButton) findViewById(R.id.radio_Cubes);
                    radio_Cubes.performClick();         // "force" radio button click
                    break;
                case "Weighted":
                    radio_Weight = (RadioButton) findViewById(R.id.radio_Weight);
                    radio_Weight.performClick();         // "force" radio button click
                    break;
                case "Switch":
                    radio_Switch = (RadioButton) findViewById(R.id.radio_Switch);
                    radio_Switch.performClick();         // "force" radio button click
                    break;
                case "Scale":
                    radio_Scale = (RadioButton) findViewById(R.id.radio_Scale);
                    radio_Scale.performClick();         // "force" radio button click
                    break;
                case "Exchange":
                    radio_Exchange = (RadioButton) findViewById(R.id.radio_Exchange);
                    radio_Exchange.performClick();         // "force" radio button click
                    break;
                default:                //
                    Log.e(TAG, "*** Invalid Type " + sortType);
            }
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
    SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    String sortType = prefs.getString("Sort", "");
//    initScores();           // make sure it sorts by _LAST_ radio button
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause  "  + sortType);
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("Sort", sortType);
        editor.commit();        // keep sort type
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

//
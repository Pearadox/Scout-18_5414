package com.pearadox.scout_5414;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;

import android.widget.Toast;
import android.util.Log;
import android.view.Gravity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

// ===== TBA - API for Blue Alliance
import com.cpjd.main.TBA;
import com.cpjd.main.Settings;
import com.cpjd.models.Event;
import com.cpjd.models.Team;
import android.os.StrictMode;

import static android.os.AsyncTask.execute;
import static android.util.Log.*;
import static java.lang.Integer.*;


public class Visualizer_Activity extends AppCompatActivity {

    String TAG = "Visualizer_Activity";        // This CLASS name
    TextView txt_dev, txt_stud;
    public boolean launchViz = false;
     // -----------------------
    ArrayAdapter<String> adapter_typ;
    public String typSelected = " ";
    Spinner spinner_MatchType;
    Spinner spinner_MatchNum;
    ArrayAdapter<String> adapter_Num;
    public String NumSelected = " ";
    ListView listView_Matches;
    ArrayList<String> matchList = new ArrayList<String>();
    ArrayAdapter<String> adaptMatch;
    public int matchSelected = 0;
    public String matchID = "T00";          // Type + #
    public String next_Match = " ";         // List of next Matches for 5414
    TextView txt_EventName, txt_MatchID, txt_NextMatch;
    TextView txt_teamR1, txt_teamR2, txt_teamR3, txt_teamB1, txt_teamB2, txt_teamB3;
    TextView txt_teamR1_Name, txt_teamR2_Name, txt_teamR3_Name, txt_teamB1_Name, txt_teamB2_Name, txt_teamB3_Name;
    TextView tbl_teamR1, tbl_teamR2, tbl_teamR3, tbl_teamB1, tbl_teamB2, tbl_teamB3;
    TextView tbl_event1R1, tbl_event1R2, tbl_event1R3, tbl_event1B1, tbl_event1B2, tbl_event1B3;
    TextView tbl_rate1R1,tbl_rate1R2,tbl_rate1R3, tbl_rate1B1, tbl_rate1B2, tbl_rate1B3;
    TextView tbl_event2R1, tbl_event2R2, tbl_event2R3, tbl_event2B1, tbl_event2B2, tbl_event2B3;
    TextView tbl_rate2R1,tbl_rate2R2,tbl_rate2R3, tbl_rate2B1, tbl_rate2B2, tbl_rate2B3;
    TextView txt_MatchesR1, txt_MatchesR2, txt_MatchesR3, txt_MatchesB1, txt_MatchesB2, txt_MatchesB3;

    Button button_View;
    String team_num, team_name, team_loc;
    String load_team, load_name;
    p_Firebase.teamsObj team_inst = new p_Firebase.teamsObj(team_num, team_name,  team_loc);
    private FirebaseDatabase pfDatabase;
//    private DatabaseReference pfStudent_DBReference;
//    private DatabaseReference pfDevice_DBReference;
//    private DatabaseReference pfTeam_DBReference;
    private DatabaseReference pfMatch_DBReference;
    private DatabaseReference pfMatchData_DBReference;
//    private DatabaseReference pfCur_Match_DBReference;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ArrayList<p_Firebase.teamsObj> Scout_teams = new ArrayList<p_Firebase.teamsObj>();
    ImageView tbl_robotR1, tbl_robotR2, tbl_robotR3, tbl_robotB1, tbl_robotB2, tbl_robotB3;
    String tnum = "";
    Bitmap img;
    String URL = "";
    String FB_teams[] = new String[]{"","","","","",""};
    String FB_url[] = new String[]{"","","","","",""};
    int FB_num;
    FirebaseStorage storage;
    StorageReference storageRef;
    ArrayList<matchData> Vis_MD = new ArrayList<>();
    matchData match_inst = new matchData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizer);
        i(TAG, "@@@@  Visualizer_Activity started  @@@@");
        Bundle bundle = this.getIntent().getExtras();
        String param1 = bundle.getString("dev");
        String param2 = bundle.getString("stud");
        w(TAG, param1 + " " + param2);      // ** DEBUG **

//      -----------------------------------------

        txt_dev = (TextView) findViewById(R.id.txt_Dev);
        txt_stud = (TextView) findViewById(R.id.txt_TeamName);
        txt_NextMatch = (TextView) findViewById(R.id.txt_NextMatch);
        txt_dev.setText(param1);
        txt_stud.setText(param2);
        txt_NextMatch.setText("");
        matchID = "";
        txt_MatchID = (TextView) findViewById(R.id.txt_MatchID);
        txt_MatchID.setText(" ");
        listView_Matches = (ListView) findViewById(R.id.listView_Matches);
        adaptMatch = new ArrayAdapter<String>(this, R.layout.match_list_layout, matchList);
        listView_Matches.setAdapter(adaptMatch);
        adaptMatch.notifyDataSetChanged();

        pfDatabase = FirebaseDatabase.getInstance();
        pfMatch_DBReference = pfDatabase.getReference("matches/" + Pearadox.FRC_Event); // List of Matches
        pfMatchData_DBReference = pfDatabase.getReference("match-data/" + Pearadox.FRC_Event);    // Match Data

//        Spinner spinner_MatchType = (Spinner) findViewById(R.id.spinner_MatchType);
//        String[] devices = getResources().getStringArray(R.array.mtchtyp_array);
//        adapter_typ = new ArrayAdapter<String>(this, R.layout.dev_list_layout, devices);
//        adapter_typ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_MatchType.setAdapter(adapter_typ);
//        spinner_MatchType.setSelection(0, false);
//        spinner_MatchType.setOnItemSelectedListener(new Visualizer_Activity.type_OnItemSelectedListener());
//        Spinner spinner_MatchNum = (Spinner) findViewById(R.id.spinner_MatchNum);
//        ArrayAdapter adapter_Num = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Pearadox.matches);
//        adapter_Num.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_MatchNum.setAdapter(adapter_Num);
//        spinner_MatchNum.setSelection(0, false);
//        spinner_MatchNum.setOnItemSelectedListener(new Visualizer_Activity.mNum_OnItemSelectedListener());
        clearTeams();
        Button button_View = (Button) findViewById(R.id.button_View);   // Listner defined in Layout XML
//        button_View.setOnClickListener(buttonView_Click);


// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        listView_Matches.setOnItemClickListener(new AdapterView.OnItemClickListener()	{
            public void onItemClick(AdapterView<?> parent,
                                    View view, int pos, long id) {
                w(TAG,"*** listView_Matches ***   Item Selected: " + pos);
                matchSelected = pos;
                listView_Matches.setSelector(android.R.color.holo_blue_light);
        		/* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
                int blnk = matchList.get(matchSelected).indexOf(" ");          // 1st blank after MatchID
                matchID = matchList.get(matchSelected).substring(0,blnk);      // GLF 4/20  (112 matches!!)  5/2 2 or 3 digit
                w(TAG,"   MatchID: " + matchID);
                txt_MatchID = (TextView) findViewById(R.id.txt_MatchID);
                txt_MatchID.setText(matchID);
            }
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            }
        });
    }


    private void clearTeams() {
        i(TAG, "Clearing Team data");
        txt_teamR1 = (TextView) findViewById(R.id.txt_teamR1);
        txt_teamR2 = (TextView) findViewById(R.id.txt_teamR2);
        txt_teamR3 = (TextView) findViewById(R.id.txt_teamR3);
        txt_teamB1 = (TextView) findViewById(R.id.txt_teamB1);
        txt_teamB2 = (TextView) findViewById(R.id.txt_teamB2);
        txt_teamB3 = (TextView) findViewById(R.id.txt_teamB3);
        txt_teamR1_Name = (TextView) findViewById(R.id.txt_teamR1_Name);
        txt_teamR2_Name = (TextView) findViewById(R.id.txt_teamR2_Name);
        txt_teamR3_Name = (TextView) findViewById(R.id.txt_teamR3_Name);
        txt_teamB1_Name = (TextView) findViewById(R.id.txt_teamB1_Name);
        txt_teamB2_Name = (TextView) findViewById(R.id.txt_teamB2_Name);
        txt_teamB3_Name = (TextView) findViewById(R.id.txt_teamB3_Name);
        tbl_teamR1 = (TextView) findViewById(R.id.tbl_teamR1);
        tbl_teamR2 = (TextView) findViewById(R.id.tbl_teamR2);
        tbl_teamR3 = (TextView) findViewById(R.id.tbl_teamR3);
        tbl_teamB1 = (TextView) findViewById(R.id.tbl_teamB1);
        tbl_teamB2 = (TextView) findViewById(R.id.tbl_teamB2);
        tbl_teamB3 = (TextView) findViewById(R.id.tbl_teamB3);
        tbl_event1R1 = (TextView) findViewById(R.id.tbl_event1R1);
        tbl_event1R2 = (TextView) findViewById(R.id.tbl_event1R2);
        tbl_event1R3 = (TextView) findViewById(R.id.tbl_event1R3);
        tbl_event1B1 = (TextView) findViewById(R.id.tbl_event1B1);
        tbl_event1B2 = (TextView) findViewById(R.id.tbl_event1B2);
        tbl_event1B3 = (TextView) findViewById(R.id.tbl_event1B3);
        tbl_rate1R1 = (TextView) findViewById(R.id.tbl_rate1R1);
        tbl_rate1R2 = (TextView) findViewById(R.id.tbl_rate1R2);
        tbl_rate1R3 = (TextView) findViewById(R.id.tbl_rate1R3);
        tbl_rate1B1 = (TextView) findViewById(R.id.tbl_rate1B1);
        tbl_rate1B2 = (TextView) findViewById(R.id.tbl_rate1B2);
        tbl_rate1B3 = (TextView) findViewById(R.id.tbl_rate1B3);
        tbl_event2R1 = (TextView) findViewById(R.id.tbl_event2R1);
        tbl_event2R2 = (TextView) findViewById(R.id.tbl_event2R2);
        tbl_event2R3 = (TextView) findViewById(R.id.tbl_event2R3);
        tbl_event2B1 = (TextView) findViewById(R.id.tbl_event2B1);
        tbl_event2B2 = (TextView) findViewById(R.id.tbl_event2B2);
        tbl_event2B3 = (TextView) findViewById(R.id.tbl_event2B3);
        tbl_rate2R1 = (TextView) findViewById(R.id.tbl_rate2R1);
        tbl_rate2R2 = (TextView) findViewById(R.id.tbl_rate2R2);
        tbl_rate2R3 = (TextView) findViewById(R.id.tbl_rate2R3);
        tbl_rate2B1 = (TextView) findViewById(R.id.tbl_rate2B1);
        tbl_rate2B2 = (TextView) findViewById(R.id.tbl_rate2B2);
        tbl_rate2B3 = (TextView) findViewById(R.id.tbl_rate2B3);
        txt_MatchesR1 = (TextView) findViewById(R.id.txt_MatchesR1);
        txt_MatchesR2 = (TextView) findViewById(R.id.txt_MatchesR2);
        txt_MatchesR3 = (TextView) findViewById(R.id.txt_MatchesR3);
        txt_MatchesB1 = (TextView) findViewById(R.id.txt_MatchesB1);
        txt_MatchesB2 = (TextView) findViewById(R.id.txt_MatchesB2);
        txt_MatchesB3 = (TextView) findViewById(R.id.txt_MatchesB3);


        txt_teamR1.setText("");
        txt_teamR2.setText("");
        txt_teamR3.setText("");
        txt_teamB1.setText("");
        txt_teamB2.setText("");
        txt_teamB3.setText("");

        txt_teamR1_Name.setText("");
        txt_teamR2_Name.setText("");
        txt_teamR3_Name.setText("");
        txt_teamB1_Name.setText("");
        txt_teamB2_Name.setText("");
        txt_teamB3_Name.setText("");

        tbl_teamR1.setText("");
        tbl_teamR2.setText("");
        tbl_teamR3.setText("");
        tbl_teamB1.setText("");
        tbl_teamB2.setText("");
        tbl_teamB3.setText("");

        tbl_event1R1.setText("");
        tbl_event1R2.setText("");
        tbl_event1R3.setText("");
        tbl_event1B1.setText("");
        tbl_event1B2.setText("");
        tbl_event1B3.setText("");
        tbl_rate1R1.setText("");
        tbl_rate1R2.setText("");
        tbl_rate1R3.setText("");
        tbl_rate1B1.setText("");
        tbl_rate1B2.setText("");
        tbl_rate1B3.setText("");

        tbl_event2R1.setText("");
        tbl_event2R2.setText("");
        tbl_event2R3.setText("");
        tbl_event2B1.setText("");
        tbl_event2B2.setText("");
        tbl_event2B3.setText("");
        tbl_rate2R1.setText("");
        tbl_rate2R2.setText("");
        tbl_rate2R3.setText("");
        tbl_rate2B1.setText("");
        tbl_rate2B2.setText("");
        tbl_rate2B3.setText("");
        txt_MatchesR1.setText("");
        txt_MatchesR2.setText("");
        txt_MatchesR3.setText("");
        txt_MatchesB1.setText("");
        txt_MatchesB2.setText("");
        txt_MatchesB3.setText("");

                ImageView tbl_robotR1 = (ImageView) findViewById(R.id.tbl_robotR1);
        ImageView tbl_robotR2 = (ImageView) findViewById(R.id.tbl_robotR2);
        ImageView tbl_robotR3 = (ImageView) findViewById(R.id.tbl_robotR3);
        ImageView tbl_robotB1 = (ImageView) findViewById(R.id.tbl_robotB1);
        ImageView tbl_robotB2 = (ImageView) findViewById(R.id.tbl_robotB2);
        ImageView tbl_robotB3 = (ImageView) findViewById(R.id.tbl_robotB3);
        tbl_robotR1.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        tbl_robotR2.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        tbl_robotR3.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        tbl_robotB1.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        tbl_robotB2.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        tbl_robotB3.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));

    }

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void btn_PitR1_Click(View view) {
        i(TAG, " btn_PitR1_Click   ");
        txt_teamR1 = (TextView) findViewById(R.id.txt_teamR1);
        txt_teamR1_Name = (TextView) findViewById(R.id.txt_teamR1_Name);
        tnum = (String) txt_teamR1.getText();
        team_name = (String) txt_teamR1_Name.getText();
        w(TAG, "*** Team " + tnum + " " + team_name + "  URL ='" + FB_url[0] + "' ");
        launchVizPit(tnum, team_name, FB_url[0]);
    }
    public void btn_PitR2_Click(View view) {
        i(TAG, " btn_PitR2_Click   ");
        txt_teamR2 = (TextView) findViewById(R.id.txt_teamR2);
        txt_teamR2_Name = (TextView) findViewById(R.id.txt_teamR2_Name);
        tnum = (String) txt_teamR2.getText();
        team_name = (String) txt_teamR2_Name.getText();
        launchVizPit(tnum, team_name, FB_url[1]);
    }
    public void btn_PitR3_Click(View view) {
        i(TAG, " btn_PitR3_Click   ");
        txt_teamR3 = (TextView) findViewById(R.id.txt_teamR3);
        txt_teamR3_Name = (TextView) findViewById(R.id.txt_teamR3_Name);
        tnum = (String) txt_teamR3.getText();
        team_name = (String) txt_teamR3_Name.getText();
        launchVizPit(tnum, team_name, FB_url[2]);
    }
    public void btn_PitB1_Click(View view) {
        i(TAG, " btn_PitB1_Click   ");
        txt_teamB1 = (TextView) findViewById(R.id.txt_teamB1);
        txt_teamB1_Name = (TextView) findViewById(R.id.txt_teamB1_Name);
        tnum = (String) txt_teamB1.getText();
        team_name = (String) txt_teamB1_Name.getText();
        launchVizPit(tnum, team_name, FB_url[3]);
    }
    public void btn_PitB2_Click(View view) {
        i(TAG, " btn_PitB2_Click   ");
        txt_teamB2 = (TextView) findViewById(R.id.txt_teamB2);
        txt_teamB2_Name = (TextView) findViewById(R.id.txt_teamB2_Name);
        tnum = (String) txt_teamB2.getText();
        team_name = (String) txt_teamB2_Name.getText();
        launchVizPit(tnum, team_name, FB_url[4]);
    }
    public void btn_PitB3_Click(View view) {
        i(TAG, " btn_PitB3_Click   ");
        txt_teamB3 = (TextView) findViewById(R.id.txt_teamB3);
        txt_teamB3_Name = (TextView) findViewById(R.id.txt_teamB3_Name);
        tnum = (String) txt_teamB3.getText();
        team_name = (String) txt_teamB3_Name.getText();
        launchVizPit(tnum, team_name, FB_url[5]);
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void btn_MatchR1_Click(View view) {
        i(TAG, " btn_MatchR1_Click   ");
        txt_teamR1 = (TextView) findViewById(R.id.txt_teamR1);
        txt_teamR1_Name = (TextView) findViewById(R.id.txt_teamR1_Name);
        tnum = (String) txt_teamR1.getText();
        team_name = (String) txt_teamR1_Name.getText();
        w(TAG, "*** Team " + tnum + " " + team_name);
        launchVizMatch(tnum, team_name);
    }
    public void btn_MatchR2_Click(View view) {
        i(TAG, " btn_MatchR2_Click   ");
        txt_teamR2 = (TextView) findViewById(R.id.txt_teamR2);
        txt_teamR2_Name = (TextView) findViewById(R.id.txt_teamR2_Name);
        tnum = (String) txt_teamR2.getText();
        team_name = (String) txt_teamR2_Name.getText();
        launchVizMatch(tnum, team_name);
    }
    public void btn_MatchR3_Click(View view) {
        i(TAG, " btn_MatchR3_Click   ");
        txt_teamR3 = (TextView) findViewById(R.id.txt_teamR3);
        txt_teamR3_Name = (TextView) findViewById(R.id.txt_teamR3_Name);
        tnum = (String) txt_teamR3.getText();
        team_name = (String) txt_teamR3_Name.getText();
        launchVizMatch(tnum, team_name);
    }
    public void btn_MatchB1_Click(View view) {
        i(TAG, " btn_MatchB1_Click   ");
        txt_teamB1 = (TextView) findViewById(R.id.txt_teamB1);
        txt_teamB1_Name = (TextView) findViewById(R.id.txt_teamB1_Name);
        tnum = (String) txt_teamB1.getText();
        team_name = (String) txt_teamB1_Name.getText();
        launchVizMatch(tnum, team_name);
    }
    public void btn_MatchB2_Click(View view) {
        i(TAG, " btn_MatchB2_Click   ");
        txt_teamB2 = (TextView) findViewById(R.id.txt_teamB2);
        txt_teamB2_Name = (TextView) findViewById(R.id.txt_teamB2_Name);
        tnum = (String) txt_teamB2.getText();
        team_name = (String) txt_teamB2_Name.getText();
        launchVizMatch(tnum, team_name);
    }
    public void btn_MatchB3_Click(View view) {
        i(TAG, " btn_MatchB3_Click   ");
        txt_teamB3 = (TextView) findViewById(R.id.txt_teamB3);
        txt_teamB3_Name = (TextView) findViewById(R.id.txt_teamB3_Name);
        tnum = (String) txt_teamB3.getText();
        team_name = (String) txt_teamB3_Name.getText();
        launchVizMatch(tnum, team_name);
    }

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void launchVizMatch(String team, String name) {
        i(TAG, ">>>>> launchVizMatch   <<<<<"  + team + " " + name);

        launchViz = true;
        load_team = team;
        load_name = name;
        addMD_VE_Listener(pfMatchData_DBReference.orderByChild("match"));        // Load Matches
//        childTeamMD_Listner();

    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//    private void childTeamMD_Listner() {
    private void addMD_VE_Listener(final Query pfMatchData_DBReference) {
        i(TAG, "<<<< getFB_Data >>>> Match Data for team " + load_team);

            pfMatchData_DBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.w(TAG, "@@@@ onChildAdded @@@@ Match Data for team " + load_team);
                Pearadox.Matches_Data.clear();
                Vis_MD.clear();
                matchData mdobj = new matchData();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();   /*get the data children*/
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
//                    w(TAG, " WHILE: "  + Pearadox.Matches_Data.size());
//                    System.out.println(dataSnapshot.getValue());
//                    System.out.println("  \n  \n");
                    mdobj = iterator.next().getValue(matchData.class);
                    Vis_MD.add(mdobj);      // add _ALL_ matches to this array
                    if (mdobj.getTeam_num().matches(load_team)) {
                        Log.w(TAG, " Match: " + mdobj.getMatch() + " # "  + Pearadox.Matches_Data.size());
//                        System.out.println("  \n");
                        Pearadox.Matches_Data.add(mdobj);   // load data for team specified
                    }
                }
                Log.w(TAG, "***** Matches Loaded. # = " + Vis_MD.size() + "  Team# = " + Pearadox.Matches_Data.size());
                getMatchForTeams();     // Get Match Data for each team

                if(launchViz) {
                    if (Pearadox.Matches_Data.size() > 0) {
                        Intent pit_intent = new Intent(Visualizer_Activity.this, VisMatch_Activity.class);
                        Bundle VZbundle = new Bundle();
                        VZbundle.putString("team", load_team);        // Pass data to activity
                        VZbundle.putString("name", load_name);        // Pass data to activity
                        pit_intent.putExtras(VZbundle);
                        startActivity(pit_intent);               // Start Visualizer for Match Data
                    } else {
                        Toast toast = Toast.makeText(getBaseContext(), "★★★★  There is _NO_ Match Data for Team " + load_team + "  ★★★★", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();

                    }
                }
            }
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Log.w(TAG, "%%%  ChildChanged");
//            }
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Log.w(TAG, "%%%  ChildRemoved");
//            }
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                Log.w(TAG, "%%%  ChildMoved");
//            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*listener failed or was removed for security reasons*/
            }
        });
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void launchVizPit(String team, String name, String imgURL) {
        i(TAG,">>>>>>>>  launchVizPit " + team + " " + name + " " + imgURL);      // ** DEBUG **
        Intent pit_intent = new Intent(Visualizer_Activity.this, VisPit_Activity.class);
        Bundle VZbundle = new Bundle();
        VZbundle.putString("team", team);        // Pass data to activity
        VZbundle.putString("name", name);        // Pass data to activity
        VZbundle.putString("url", imgURL);       // Pass data to activity
        pit_intent.putExtras(VZbundle);
        startActivity(pit_intent);               // Start Visualizer for Pit Data

    }

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private class type_OnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            typSelected = parent.getItemAtPosition(pos).toString();
            w(TAG, ">>>>>  '" + typSelected + "'");
            switch (typSelected) {
                case "Practice":        // Practice round
                    matchID = "X";
                    break;
                case "Qualifying":        // Qualifying round
                    matchID = "Q";
                    break;
                case "Playoff":        // Playoff round
                    matchID = "P";
                    break;
                default:                // ????
                    e(TAG, "*** Error - bad TYPE indicator  ***");
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
            w(TAG, ">>>>>  '" + NumSelected + "'");
            matchID = matchID + NumSelected;
            w(TAG, ">>>>>  Match = '" + matchID + "'");
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void buttonView_Click(View view) {
        Log.w(TAG, " View Click  " + matchID);

        clearTeams();
        getTeams();             // Get the teams for match selected
    }

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void loadTblData () {
        i(TAG, "#### loadTblData  ####");
        // Start getting data for Table
        ImageView tbl_robotR1 = (ImageView) findViewById(R.id.tbl_robotR1);
        ImageView tbl_robotR2 = (ImageView) findViewById(R.id.tbl_robotR2);
        ImageView tbl_robotR3 = (ImageView) findViewById(R.id.tbl_robotR3);
        ImageView tbl_robotB1 = (ImageView) findViewById(R.id.tbl_robotB1);
        ImageView tbl_robotB2 = (ImageView) findViewById(R.id.tbl_robotB2);
        ImageView tbl_robotB3 = (ImageView) findViewById(R.id.tbl_robotB3);

        tnum = (String) txt_teamR1.getText();
//        FB_teams[0] = tnum;
        tbl_robotR1.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        getURL0(tnum);       // Get URLs for Robot photos (if they exist) and load them
        tnum = (String) txt_teamR2.getText();
        tbl_robotR2.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        getURL1(tnum);       // Get URLs for Robot photos (if they exist) and load them
        tnum = (String) txt_teamR3.getText();
        tbl_robotR3.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        getURL2(tnum);       // Get URLs for Robot photos (if they exist) and load them
        tnum = (String) txt_teamB1.getText();
        tbl_robotB1.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        getURL3(tnum);       // Get URLs for Robot photos (if they exist) and load them
        tnum = (String) txt_teamB2.getText();
        tbl_robotB2.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        getURL4(tnum);       // Get URLs for Robot photos (if they exist) and load them
        tnum = (String) txt_teamB3.getText();
        tbl_robotB3.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        getURL5(tnum);       // Get URLs for Robot photos (if they exist) and load them

    }

    private void getURL0(String team) {
        i(TAG, ">>>>>  getURL0: " + team);

//        ImageView tbl_robotR1 = (ImageView) findViewById(R.id.tbl_robotR1);
//        ImageView tbl_robotR2 = (ImageView) findViewById(R.id.tbl_robotR2);
//        ImageView tbl_robotR3 = (ImageView) findViewById(R.id.tbl_robotR3);
//        ImageView tbl_robotB1 = (ImageView) findViewById(R.id.tbl_robotB1);
//        ImageView tbl_robotB2 = (ImageView) findViewById(R.id.tbl_robotB2);
//        ImageView tbl_robotB3 = (ImageView) findViewById(R.id.tbl_robotB3);
        URL = "";

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://paradox-2017.appspot.com/images/" + Pearadox.FRC_Event).child("robot_" + team.trim() + ".png");
        e(TAG, "images/" + Pearadox.FRC_Event + "/robot_" + team.trim() + ".png" + "\n \n");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                e(TAG,  FB_num + "  uri: " + uri.toString());
                ImageView tbl_robotR1 = (ImageView) findViewById(R.id.tbl_robotR1);
                URL = uri.toString();
                FB_url[0] = URL;
                if (URL.length() > 0) {
                    Picasso.with(Visualizer_Activity.this).load(URL).into(tbl_robotR1);
                }
            }
        });

        //        Toast.makeText(getBaseContext(), "Robot images loaded", Toast.LENGTH_LONG).show();  //** DEBUG
    }
    private void getURL1(String team) {
        i(TAG, ">>>>>  getURL1: " + team);
        URL = "";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://paradox-2017.appspot.com/images/" + Pearadox.FRC_Event).child("robot_" + team.trim() + ".png");
        e(TAG, "images/" + Pearadox.FRC_Event + "/robot_" + team.trim() + ".png" + "\n \n");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                e(TAG,  FB_num + "  uri: " + uri.toString());
                ImageView tbl_robotR2 = (ImageView) findViewById(R.id.tbl_robotR2);
                URL = uri.toString();
                FB_url[1] = URL;
                if (URL.length() > 0) {
                    Picasso.with(Visualizer_Activity.this).load(URL).into(tbl_robotR2);
                }
            }
        });
    }
    private void getURL2(String team) {
        i(TAG, ">>>>>  getURL2: " + team);
        URL = "";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://paradox-2017.appspot.com/images/" + Pearadox.FRC_Event).child("robot_" + team.trim() + ".png");
        e(TAG, "images/" + Pearadox.FRC_Event + "/robot_" + team.trim() + ".png" + "\n \n");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                e(TAG,  FB_num + "  uri: " + uri.toString());
                ImageView tbl_robotR3 = (ImageView) findViewById(R.id.tbl_robotR3);
                URL = uri.toString();
                FB_url[2] = URL;
                if (URL.length() > 0) {
                    Picasso.with(Visualizer_Activity.this).load(URL).into(tbl_robotR3);
                }
            }
        });
    }
    private void getURL3(String team) {
        i(TAG, ">>>>>  getURL3: " + team);
        URL = "";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://paradox-2017.appspot.com/images/" + Pearadox.FRC_Event).child("robot_" + team.trim() + ".png");
        e(TAG, "images/" + Pearadox.FRC_Event + "/robot_" + team.trim() + ".png" + "\n \n");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                e(TAG,  FB_num + "  uri: " + uri.toString());
                ImageView tbl_robotB1 = (ImageView) findViewById(R.id.tbl_robotB1);
                URL = uri.toString();
                FB_url[3] = URL;
                if (URL.length() > 0) {
                    Picasso.with(Visualizer_Activity.this).load(URL).into(tbl_robotB1);
                }
            }
        });
    }
    private void getURL4(String team) {
        i(TAG, ">>>>>  getURL4: " + team);
        URL = "";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://paradox-2017.appspot.com/images/" + Pearadox.FRC_Event).child("robot_" + team.trim() + ".png");
        e(TAG, "images/" + Pearadox.FRC_Event + "/robot_" + team.trim() + ".png" + "\n \n");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                e(TAG,  FB_num + "  uri: " + uri.toString());
                ImageView tbl_robotB2 = (ImageView) findViewById(R.id.tbl_robotB2);
                URL = uri.toString();
                FB_url[4] = URL;
                if (URL.length() > 0) {
                    Picasso.with(Visualizer_Activity.this).load(URL).into(tbl_robotB2);
                }
            }
        });
    }
    private void getURL5(String team) {
        i(TAG, ">>>>>  getURL5: " + team);
        URL = "";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://paradox-2017.appspot.com/images/" + Pearadox.FRC_Event).child("robot_" + team.trim() + ".png");
        e(TAG, "images/" + Pearadox.FRC_Event + "/robot_" + team.trim() + ".png" + "\n \n");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                e(TAG,  FB_num + "  uri: " + uri.toString());
                ImageView tbl_robotB3 = (ImageView) findViewById(R.id.tbl_robotB3);
                URL = uri.toString();
                FB_url[5] = URL;
                if (URL.length() > 0) {
                    Picasso.with(Visualizer_Activity.this).load(URL).into(tbl_robotB3);
                }
            }
        });
    }


    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void getTeams() {
        i(TAG, "$$$$$  getTeams");
        w(TAG, ">>>>>  Match = '" + matchID + "'");
        final int[] rank = {0};
        txt_teamR1 = (TextView) findViewById(R.id.txt_teamR1);
        txt_teamR2 = (TextView) findViewById(R.id.txt_teamR2);
        txt_teamR3 = (TextView) findViewById(R.id.txt_teamR3);
        txt_teamB1 = (TextView) findViewById(R.id.txt_teamB1);
        txt_teamB2 = (TextView) findViewById(R.id.txt_teamB2);
        txt_teamB3 = (TextView) findViewById(R.id.txt_teamB3);
        txt_teamR1_Name = (TextView) findViewById(R.id.txt_teamR1_Name);
        txt_teamR2_Name = (TextView) findViewById(R.id.txt_teamR2_Name);
        txt_teamR3_Name = (TextView) findViewById(R.id.txt_teamR3_Name);
        txt_teamB1_Name = (TextView) findViewById(R.id.txt_teamB1_Name);
        txt_teamB2_Name = (TextView) findViewById(R.id.txt_teamB2_Name);
        txt_teamB3_Name = (TextView) findViewById(R.id.txt_teamB3_Name);

//        int z = matchID.length();
        if (matchID.length() >= 3) {        // Worlds = 103 matches!!  GLF 4/17
            i(TAG, "\n   Q U E R Y  ");
            String child = "match";
            String key = matchID;
            Query query = pfMatch_DBReference.orderByChild(child).equalTo(key);
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    w(TAG, "%%%  ChildAdded");
//                    System.out.println(dataSnapshot.getValue());
                    p_Firebase.matchObj mobj = dataSnapshot.getValue(p_Firebase.matchObj.class);
//                    System.out.println("Match: " + mobj.getMatch());
//                    System.out.println("Type: " + mobj.getMtype());
//                    System.out.println("Date: " + mobj.getDate());
//                    System.out.println("R1: " + mobj.getR1());
//                    System.out.println("B3: " + mobj.getB3());
                    Scout_teams.clear();          // empty the list
                    String tn = mobj.getR1();
                    findTeam(tn);
                    load_team = tn;
                    launchViz =false;   // Don't launch VisMatch
                    addMD_VE_Listener(pfMatchData_DBReference.orderByChild("match"));        // Load ALL Match data into Vis_MD
                    tn = mobj.getR2();
                    findTeam(tn);
                    tn = mobj.getR3();
                    findTeam(tn);
                    tn = mobj.getB1();
                    findTeam(tn);
                    tn = mobj.getB2();
                    findTeam(tn);
                    tn = mobj.getB3();
                    findTeam(tn);
                    load_team = tn;
                    Log.w(TAG, ">>>> # team instances = " + Scout_teams.size());  //** DEBUG

                    txt_teamR1 = (TextView) findViewById(R.id.txt_teamR1);
                    txt_teamR2 = (TextView) findViewById(R.id.txt_teamR2);
                    txt_teamR3 = (TextView) findViewById(R.id.txt_teamR3);
                    txt_teamB1 = (TextView) findViewById(R.id.txt_teamB1);
                    txt_teamB2 = (TextView) findViewById(R.id.txt_teamB2);
                    txt_teamB3 = (TextView) findViewById(R.id.txt_teamB3);
                    txt_teamR1_Name = (TextView) findViewById(R.id.txt_teamR1_Name);
                    txt_teamR2_Name = (TextView) findViewById(R.id.txt_teamR2_Name);
                    txt_teamR3_Name = (TextView) findViewById(R.id.txt_teamR3_Name);
                    txt_teamB1_Name = (TextView) findViewById(R.id.txt_teamB1_Name);
                    txt_teamB2_Name = (TextView) findViewById(R.id.txt_teamB2_Name);
                    txt_teamB3_Name = (TextView) findViewById(R.id.txt_teamB3_Name);
                    tbl_teamR1 = (TextView) findViewById(R.id.tbl_teamR1);
                    tbl_teamR2 = (TextView) findViewById(R.id.tbl_teamR2);
                    tbl_teamR3 = (TextView) findViewById(R.id.tbl_teamR3);
                    tbl_teamB1 = (TextView) findViewById(R.id.tbl_teamB1);
                    tbl_teamB2 = (TextView) findViewById(R.id.tbl_teamB2);
                    tbl_teamB3 = (TextView) findViewById(R.id.tbl_teamB3);


                    w(TAG, "@@@@@@@@@@@@@@@@@@@ GET TEAM DATA  @@@@@@@@@@@@@@@@@@@");  //** DEBUG

                    team_inst = Scout_teams.get(0);       // Team#R1
                    txt_teamR1.setText(team_inst.getTeam_num());
                    txt_teamR1_Name.setText(team_inst.getTeam_name());
                    tbl_teamR1.setText(team_inst.getTeam_num());
                    team_inst = Scout_teams.get(1);       // Team#R2
                    txt_teamR2.setText(team_inst.getTeam_num());
                    txt_teamR2_Name.setText(team_inst.getTeam_name());
                    tbl_teamR2.setText(team_inst.getTeam_num());
                    team_inst = Scout_teams.get(2);       // Team#R3
                    txt_teamR3.setText(team_inst.getTeam_num());
                    txt_teamR3_Name.setText(team_inst.getTeam_name());
                    tbl_teamR3.setText(team_inst.getTeam_num());
                    team_inst = Scout_teams.get(3);       // Team#B1
                    txt_teamB1.setText(team_inst.getTeam_num());
                    txt_teamB1_Name.setText(team_inst.getTeam_name());
                    tbl_teamB1.setText(team_inst.getTeam_num());
                    team_inst = Scout_teams.get(4);       // Team#B2
                    txt_teamB2.setText(team_inst.getTeam_num());
                    txt_teamB2_Name.setText(team_inst.getTeam_name());
                    tbl_teamB2.setText(team_inst.getTeam_num());
                    team_inst = Scout_teams.get(5);       // Team#B3
                    txt_teamB3.setText(team_inst.getTeam_num());
                    txt_teamB3_Name.setText(team_inst.getTeam_name());
                    tbl_teamB3.setText(team_inst.getTeam_num());
                    Log.w(TAG, "+++++++++; size = " + Scout_teams.size());

//                    w(TAG, "***  Calling Async class  ***");  //** DEBUG
//                    new Load_MatchData_Task().execute();     // Load match data Asyncronously
                    loadTblData();      // Load the images (if any)

                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    w(TAG, "%%%  ChildChanged");
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    w(TAG, "%%%  ChildRemoved");
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    w(TAG, "%%%  ChildMoved");
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else {
            Toast.makeText(getBaseContext(), "** Select both Match TYPE & NUMBER ** ", Toast.LENGTH_LONG).show();
            // ToDo - turn toggle back to logon
        }
        Log.e(TAG, "<<<<<< End of getteams; size = " + Scout_teams.size());
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void getMatchForTeams() {
        Log.w(TAG, "$$$$$  getMatchForTeams " + Pearadox.Matches_Data.size());
        String team="";
        int md = Vis_MD.size();
        if (md > 0) {
            int ndx=0;
            for(int x = 0; x < 6; x++) {
                team_inst = Scout_teams.get(x);
                team = team_inst.getTeam_num();        // Team#
                Log.w(TAG, "TEAM  " + team);
                ndx = x;
                Log.w(TAG, "NDX  " + ndx);
                int numMDs = 0; int swNum = 0; int swAtt = 0; int scNum = 0; int scAtt = 0; int base = 0; int extra=0;
                int tswNum = 0; int tswAtt = 0; int tscNum = 0; int tscAtt = 0; int othr=0; int o_att = 0;
                int climb=0; int c_att=0; int lift1=0; int lift2=0; int was=0; int exch=0; int portal=0; int zone=0; int floor=0; int tfloor=0;
                for (int i = 0; i < md; i++) {
                match_inst = Vis_MD.get(i);      // Get instance of Match Data
                String mdt = match_inst.getTeam_num();
                if (mdt.matches(team)) {        // is this match data for the team we are working on?
                Log.w(TAG, "GMFT TEAM  " + mdt);
                    numMDs++;       // increment # of MDs
                    if (match_inst.isAuto_baseline()) {
                        base++;
                    }
                    if (match_inst.isAuto_cube_switch()) {
                        swNum++;
                    }
                    if (match_inst.isAuto_cube_switch_att()) {
                        swAtt++;
                    }
                    if (match_inst.isAuto_cube_scale()) {
                        scNum++;
                    }
                    if (match_inst.isAuto_cube_scale_att()) {
                        scAtt++;
                    }
                    if (match_inst.isAuto_switch_extra()) {
                        extra++;
                    }
                    // =================== TeleOps ============
                    tswNum = tswNum + match_inst.getTele_cube_switch();
                    tswAtt = tswAtt + match_inst.getTele_switch_attempt();
                    tscNum = tscNum + match_inst.getTele_cube_scale();
                    tscAtt = tscAtt + match_inst.getTele_scale_attempt();
                    exch = exch + match_inst.getTele_cube_exchange();
                    othr = othr + match_inst.getTele_their_switch();
                    o_att = o_att + match_inst.getTele_their_attempt();
                    portal = portal + match_inst.getTele_cube_portal();
                    zone = zone + match_inst.getTele_cube_pwrzone();
                    floor = floor + match_inst.getTele_cube_floor();
                    tfloor = tfloor + match_inst.getTele_their_floor();
                    if (match_inst.isTele_climb_success()) {
                        climb++;
                    }
                    if (match_inst.isTele_climb_attempt()) {
                        c_att++;
                    }
                    if (match_inst.isTele_lift_one()) {
                        lift1++;
                    }
                    if (match_inst.isTele_lift_two()) {
                        lift2++;
                    }
                    if (match_inst.isTele_got_lift()) {
                        was++;
                    }
                } // EndIf teams match
            } // End for #teams
            Log.e(TAG, team + " ==== Match Data " +  base + "  " +  swNum + "/" +  swAtt + "  " +  scNum + "/" +  scAtt + " ");
            tbl_event1R1 = (TextView) findViewById(R.id.tbl_event1R1);
            tbl_event1R2 = (TextView) findViewById(R.id.tbl_event1R2);
            tbl_event1R3 = (TextView) findViewById(R.id.tbl_event1R3);
            tbl_event1B1 = (TextView) findViewById(R.id.tbl_event1B1);
            tbl_event1B2 = (TextView) findViewById(R.id.tbl_event1B2);
            tbl_event1B3 = (TextView) findViewById(R.id.tbl_event1B3);
            tbl_rate1R1 = (TextView) findViewById(R.id.tbl_rate1R1);
            tbl_rate1R2 = (TextView) findViewById(R.id.tbl_rate1R2);
            tbl_rate1R3 = (TextView) findViewById(R.id.tbl_rate1R3);
            tbl_rate1B1 = (TextView) findViewById(R.id.tbl_rate1B1);
            tbl_rate1B2 = (TextView) findViewById(R.id.tbl_rate1B2);
            tbl_rate1B3 = (TextView) findViewById(R.id.tbl_rate1B3);
            tbl_event2R1 = (TextView) findViewById(R.id.tbl_event2R1);
            tbl_event2R2 = (TextView) findViewById(R.id.tbl_event2R2);
            tbl_event2R3 = (TextView) findViewById(R.id.tbl_event2R3);
            tbl_event2B1 = (TextView) findViewById(R.id.tbl_event2B1);
            tbl_event2B2 = (TextView) findViewById(R.id.tbl_event2B2);
            tbl_event2B3 = (TextView) findViewById(R.id.tbl_event2B3);
            tbl_rate2R1 = (TextView) findViewById(R.id.tbl_rate2R1);
            tbl_rate2R2 = (TextView) findViewById(R.id.tbl_rate2R2);
            tbl_rate2R3 = (TextView) findViewById(R.id.tbl_rate2R3);
            tbl_rate2B1 = (TextView) findViewById(R.id.tbl_rate2B1);
            tbl_rate2B2 = (TextView) findViewById(R.id.tbl_rate2B2);
            tbl_rate2B3 = (TextView) findViewById(R.id.tbl_rate2B3);
            txt_MatchesR1 = (TextView) findViewById(R.id.txt_MatchesR1);
            txt_MatchesR2 = (TextView) findViewById(R.id.txt_MatchesR2);
            txt_MatchesR3 = (TextView) findViewById(R.id.txt_MatchesR3);
            txt_MatchesB1 = (TextView) findViewById(R.id.txt_MatchesB1);
            txt_MatchesB2 = (TextView) findViewById(R.id.txt_MatchesB2);
            txt_MatchesB3 = (TextView) findViewById(R.id.txt_MatchesB3);

            switch (ndx) {
            case 0:
                txt_MatchesR1.setText(String.valueOf(numMDs));
                tbl_event1R1.setText("Auto" + " \n" + "Tele");
                tbl_rate1R1.setText( "⊕" + base + "  ⚻  " + swNum + "/" + swAtt + "  ⚖ " + scNum + "/" + scAtt + "   +" + extra + " \n" + "⚻  " + tswNum + "/" + tswAtt + "   ⚖ " + tscNum + "/" + tscAtt + "   Oth⚻ " + othr + "/" + o_att);
                tbl_event2R1.setText("Climb" + " \n" + "▉");
                tbl_rate2R1.setText(climb + "/" + c_att + "  ↕One" + lift1 + "  ↕Two" + lift2 + " ↑ " + was+ " \n Ex " + exch + "   Prt " + portal + "  Zn " + zone + "  FL  " + floor + " ➤ " +tfloor);
                break;
            case 1:
                txt_MatchesR2.setText(String.valueOf(numMDs));
                tbl_event1R2.setText("Auto" + " \n" + "Tele");
                tbl_rate1R2.setText( "⊕" + base + "  ⚻  " + swNum + "/" + swAtt + "  ⚖ " + scNum + "/" + scAtt + "   +" + extra + " \n" + "⚻  " + tswNum + "/" + tswAtt + "   ⚖ " + tscNum + "/" + tscAtt + "   Oth⚻ " + othr + "/" + o_att);
                tbl_event2R2.setText("Climb" + " \n" + "▉");
                tbl_rate2R2.setText(climb + "/" + c_att + "  ↕One" + lift1 + "  ↕Two" + lift2 + " ↑ " + was+ " \n Ex " + exch + "   Prt " + portal + "  Zn " + zone + "  FL  " + floor + " ➤ " +tfloor);
                break;
            case 2:
                txt_MatchesR3.setText(String.valueOf(numMDs));
                tbl_event1R3.setText("Auto" + " \n" + "Tele");
                tbl_rate1R3.setText( "⊕" + base + "  ⚻  " + swNum + "/" + swAtt + "  ⚖ " + scNum + "/" + scAtt + "   +" + extra + " \n" + "⚻  " + tswNum + "/" + tswAtt + "   ⚖ " + tscNum + "/" + tscAtt + "   Oth⚻ " + othr + "/" + o_att);
                tbl_event2R3.setText("Climb" + " \n" + "▉");
                tbl_rate2R3.setText(climb + "/" + c_att + "  ↕One" + lift1 + "  ↕Two" + lift2 + " ↑ " + was+ " \n Ex " + exch + "   Prt " + portal + "  Zn " + zone + "  FL  " + floor + " ➤ " +tfloor);
                break;
            case 3:
                txt_MatchesB1.setText(String.valueOf(numMDs));
                tbl_event1B1.setText("Auto" + " \n" + "Tele");
                tbl_rate1B1.setText( "⊕" + base + "  ⚻  " + swNum + "/" + swAtt + "  ⚖ " + scNum + "/" + scAtt + "   +" + extra + " \n" + "⚻  " + tswNum + "/" + tswAtt + "   ⚖ " + tscNum + "/" + tscAtt + "   Oth⚻ " + othr + "/" + o_att);
                tbl_event2B1.setText("Climb" + " \n" + "▉");
                tbl_rate2B1.setText(climb + "/" + c_att + "  ↕One" + lift1 + "  ↕Two" + lift2 + " ↑ " + was+ " \n Ex " + exch + "   Prt " + portal + "  Zn " + zone + "  FL  " + floor + " ➤ " +tfloor);
                break;
            case 4:
                txt_MatchesB2.setText(String.valueOf(numMDs));
                tbl_event1B2.setText("Auto" + " \n" + "Tele");
                tbl_rate1B2.setText( "⊕" + base + "  ⚻  " + swNum + "/" + swAtt + "  ⚖ " + scNum + "/" + scAtt + "   +" + extra + " \n" + "⚻  " + tswNum + "/" + tswAtt + "   ⚖ " + tscNum + "/" + tscAtt + "   Oth⚻ " + othr + "/" + o_att);
                tbl_event2B2.setText("Climb" + " \n" + "▉");
                tbl_rate2B2.setText(climb + "/" + c_att + "  ↕One" + lift1 + "  ↕Two" + lift2 + " ↑ " + was+ " \n Ex " + exch + "   Prt " + portal + "  Zn " + zone + "  FL  " + floor + " ➤ " +tfloor);
                break;
            case 5:
                txt_MatchesB3.setText(String.valueOf(numMDs));
                tbl_event1B3.setText("Auto" + " \n" + "Tele");
                tbl_rate1B3.setText( "⊕" + base + "  ⚻  " + swNum + "/" + swAtt + "  ⚖ " + scNum + "/" + scAtt + "   +" + extra + " \n" + "⚻  " + tswNum + "/" + tswAtt + "   ⚖ " + tscNum + "/" + tscAtt + "   Oth⚻ " + othr + "/" + o_att);
                tbl_event2B3.setText("Climb" + " \n" + "▉");
                tbl_rate2B3.setText(climb + "/" + c_att + "  ↕One" + lift1 + "  ↕Two" + lift2 + " ↑ " + was+ " \n Ex " + exch + "   Prt " + portal + "  Zn " + zone + "  FL  " + floor + " ➤ " +tfloor);
                break;
            default:                // ????
                Log.e(TAG, "*** Error - bad NDX  ***");
            } // end Switch

        } //end If md>0
        } // End for #teams
     }


    private void findTeam(String tnum) {
        Log.w(TAG, "$$$$$  findTeam " + tnum);
        boolean found = false;
        for (int i = 0; i < Pearadox.numTeams; i++) {        // check each team entry
            if (Pearadox.team_List.get(i).getTeam_num().equals(tnum)) {
                team_inst = Pearadox.team_List.get(i);
                Scout_teams.add(team_inst);
//                Log.w(TAG, "===  Team " + team_inst.getTeam_num() + " " + team_inst.getTeam_name() + " " + team_inst.getTeam_loc());
                found = true;
                break;  // found it!
            }
        }  // end For
        Log.w("TAG", ">>>>>>>  findTeam: " + Scout_teams.size());
        if (!found) {
            Toast.makeText(getBaseContext(),"** Team '" + tnum + "' from Matches table _NOT_ found in Team list  ** ", Toast.LENGTH_LONG).show();
            p_Firebase.teamsObj team_dummy = new p_Firebase.teamsObj("****", "team _NOT_ found in Team list - Check for TYPOs in Match Sched."," ");
            Scout_teams.add(team_dummy);
        }
    }


    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void loadMatches() {
        i(TAG, "###  loadMatches  ###");

        addMatchSched_VE_Listener(pfMatch_DBReference.orderByChild("match"));

    }

        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void addMatchSched_VE_Listener(final Query pfMatch_DBReference) {
        pfMatch_DBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                i(TAG, "******* Firebase retrieve Match Schedule  *******");
                txt_NextMatch = (TextView) findViewById(R.id.txt_NextMatch);
                matchList.clear();
                next_Match = "";
                p_Firebase.matchObj match_inst = new p_Firebase.matchObj();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();   /*get the data children*/
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    match_inst = iterator.next().getValue(p_Firebase.matchObj.class);
//                    Log.w(TAG,"      " + match_inst.getMatch());
//                    matchList.add(match_inst.getMatch() + "  Time: " + match_inst.getTime() + "  " + match_inst.getMtype());
                    matchList.add(match_inst.getMatch() +  "  " + match_inst.getMtype());
                    // Create the list of _OUR_ matches across the top
                    if (match_inst.getR1().matches("5414")) {
                        next_Match =  next_Match + match_inst.getMatch() + " ";
                    }
                    if (match_inst.getR2().matches("5414")) {
                        next_Match =  next_Match + match_inst.getMatch() + " ";
                    }
                    if (match_inst.getR3().matches("5414")) {
                        next_Match =  next_Match + match_inst.getMatch() + " ";
                    }
                    if (match_inst.getB1().matches("5414")) {
                        next_Match =  next_Match + match_inst.getMatch() + " ";
                    }
                    if (match_inst.getB2().matches("5414")) {
                        next_Match =  next_Match + match_inst.getMatch() + " ";
                    }
                    if (match_inst.getB3().matches("5414")) {
                        next_Match =  next_Match + match_inst.getMatch() + " ";
                    }
                }
                w(TAG,"### Matches ###  : " + matchList.size());
                txt_NextMatch.setText(next_Match);
                listView_Matches = (ListView) findViewById(R.id.listView_Matches);
                adaptMatch = new ArrayAdapter<String>(Visualizer_Activity.this, R.layout.match_list_layout, matchList);
                listView_Matches.setAdapter(adaptMatch);
                adaptMatch.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*listener failed or was removed for security reasons*/
                throw databaseError.toException();
            }
        });
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }

//###################################################################
//###################################################################
//###################################################################
@Override
public void onStart() {
    super.onStart();
    v(TAG, "onStart");

    FirebaseUser user = mAuth.getCurrentUser();
    if (user != null) {
        // do your stuff
    } else {
        signInAnonymously();
    }
    loadMatches();  // Find all matches for this event
}
    @Override
    public void onResume() {
        super.onResume();
        v(TAG, "onResume");
    }
    @Override
    public void onStop() {
        super.onStop();
        v(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        v(TAG, "OnDestroy");
    }

}

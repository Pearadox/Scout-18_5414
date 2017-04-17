package com.pearadox.scout_5414;

import android.app.ProgressDialog;
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


public class Visualizer_Activity extends AppCompatActivity {

    String TAG = "Visualizer_Activity";        // This CLASS name
    TextView txt_dev, txt_stud;
    // @@@  Blue Alliance  @@@
    public static int BA1numTeams = 0;                                      // # of teams from Blue Alliance
    public static int BA2numTeams = 0;
    public static ArrayList<String> BAteams_List = new ArrayList<String>();     // Teams (in RANK order)
    public boolean BA_avail = false;
    Team[] teams1; Team[] teams2; Event[] evt1;
    String rank1 = ""; String rank2 = "";  String OPR1 = "";  String OPR2 = ""; String NTP1 = ""; String NTP2 = "";
    String WLT1 = ""; String WLT2 = ""; String kPa1 = ""; String kPa2 = ""; String EVT1 = ""; String EVT2 = "";
    // ----------------------- String NTP1 = "";
    ProgressBar progressBar1;
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
    public String OPR = " ";                // Offensive Power Rating
//    public static String[] OPR_Teams = new String[]       // Top 15 OPRs from TXLU
//            {"2481", "1477", " 118", "3366", "5572", " 192", " 624", "2468", "2341", "2164", "4696", "1817", "2613", "2352", "3847"};
//    public static String[] OPR_rating = new String[]       // Top 15 OPRs from TXLU
//            {"128.5","118.2","109.4"," 99.3"," 96.3"," 87.7"," 87.5"," 84.9"," 84.2"," 79.5"," 79.4"," 78.7"," 74.8"," 72.4"," 68.7"};
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
    ArrayList<p_Firebase.teamsObj> teams = new ArrayList<p_Firebase.teamsObj>();
    ImageView tbl_robotR1, tbl_robotR2, tbl_robotR3, tbl_robotB1, tbl_robotB2, tbl_robotB3;
    String tnum = "";
    Bitmap img;
    String URL = "";
    String FB_teams[] = new String[]{"","","","","",""};
    String FB_url[] = new String[]{"","","","","",""};
    int FB_num;
    FirebaseStorage storage;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizer);
        Log.i(TAG, "@@@@  Visualizer_Activity started  @@@@");
        Bundle bundle = this.getIntent().getExtras();
        String param1 = bundle.getString("dev");
        String param2 = bundle.getString("stud");
        Log.w(TAG, param1 + " " + param2);      // ** DEBUG **

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

        TBA t = new TBA();
        Event e = t.getEvent("txlu", 2017);
        teams1 = e.teams.clone();
        Log.e(TAG, "BLUE1 " + teams1.length);
        BA1numTeams = e.teams.length;
//        for(int i = 0; i < teams1.length; i++) {
//            System.out.println("Team #: "+teams1[i].team_number+ "  " +teams1[i].rank+" OPR: "+teams1[i].opr+ "  " +teams1[i].touchpad);
//            System.out.println("        "+teams1[i].record+" RankScore: "+teams1[i].rankingScore+ "  " +teams1[i].pressure + "\n ");
//        }

//        Event x = t.getEvent("txho", 2017);
//        teams2 = x.teams.clone();
////        Team[] teams2 = x.teams;
//        Log.e(TAG, "BLUE2 " + teams2.length);
//        BA2numTeams = x.teams.length;
//        for(int i = 0; i < teams2.length; i++) {
//            System.out.println("Team #: "+teams2[i].team_number+ "  " +teams2[i].rank+" OPR: "+teams2[i].opr+ "  " +teams2[i].touchpad);
//            System.out.println("        "+teams2[i].record+" RankScore: "+teams2[i].rankingScore+ "  " +teams2[i].pressure + "\n ");
//        }

//        if (!e.name.isEmpty() && !x.name.isEmpty()) {
//            BA_avail = true;
//        } else {
//            BA_avail = false;
//            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
//            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
//            Toast toast = Toast.makeText(getBaseContext(), "***  Data from the Blue Alliance is _NOT_ available this session  ***", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//            toast.show();
//        }


//      -----------------------------------------

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar1.setVisibility(View.INVISIBLE);
        txt_dev = (TextView) findViewById(R.id.txt_Dev);
        txt_stud = (TextView) findViewById(R.id.txt_TeamName);
        txt_NextMatch = (TextView) findViewById(R.id.txt_NextMatch);
        txt_dev.setText(param1);
        txt_stud.setText(param2);
        txt_NextMatch.setText("");
        matchID = "";
        listView_Matches = (ListView) findViewById(R.id.listView_Matches);
        adaptMatch = new ArrayAdapter<String>(this, R.layout.match_list_layout, matchList);
        listView_Matches.setAdapter(adaptMatch);
        adaptMatch.notifyDataSetChanged();

        pfDatabase = FirebaseDatabase.getInstance();
//        pfTeam_DBReference = pfDatabase.getReference("teams/" + Pearadox.FRC_Event);  // Tteam data from Firebase D/B
//        pfStudent_DBReference = pfDatabase.getReference("students");        // List of Students
//        pfDevice_DBReference = pfDatabase.getReference("devices");          // List of Devicess
        pfMatch_DBReference = pfDatabase.getReference("matches/" + Pearadox.FRC_Event); // List of Matches
        pfMatchData_DBReference = pfDatabase.getReference("match-data/" + Pearadox.FRC_Event);    // Match Data
//        pfCur_Match_DBReference = pfDatabase.getReference("current-match"); // _THE_ current Match
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();

        Spinner spinner_MatchType = (Spinner) findViewById(R.id.spinner_MatchType);
        String[] devices = getResources().getStringArray(R.array.mtchtyp_array);
        adapter_typ = new ArrayAdapter<String>(this, R.layout.dev_list_layout, devices);
        adapter_typ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_MatchType.setAdapter(adapter_typ);
        spinner_MatchType.setSelection(0, false);
        spinner_MatchType.setOnItemSelectedListener(new Visualizer_Activity.type_OnItemSelectedListener());
        Spinner spinner_MatchNum = (Spinner) findViewById(R.id.spinner_MatchNum);
        ArrayAdapter adapter_Num = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Pearadox.matches);
        adapter_Num.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_MatchNum.setAdapter(adapter_Num);
        spinner_MatchNum.setSelection(0, false);
        spinner_MatchNum.setOnItemSelectedListener(new Visualizer_Activity.mNum_OnItemSelectedListener());
        clearTeams();
        Button button_View = (Button) findViewById(R.id.button_View);   // Listner defined in Layout XML
//        button_View.setOnClickListener(buttonView_Click);


// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        listView_Matches.setOnItemClickListener(new AdapterView.OnItemClickListener()	{
            public void onItemClick(AdapterView<?> parent,
                                    View view, int pos, long id) {
                Log.w(TAG,"*** listView_Matches ***   Item Selected: " + pos);
                matchSelected = pos;
                listView_Matches.setSelector(android.R.color.holo_blue_light);
        		/* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
                matchID = matchList.get(matchSelected).substring(0,3);
                Log.w(TAG,"   MatchID: " + matchID);
                txt_MatchID = (TextView) findViewById(R.id.txt_MatchID);
                txt_MatchID.setText(matchID);
            }
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            }
        });
    }


    private void clearTeams() {
        Log.i(TAG, "Clearing Team data");
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
        Log.i(TAG, " btn_PitR1_Click   ");
        txt_teamR1 = (TextView) findViewById(R.id.txt_teamR1);
        txt_teamR1_Name = (TextView) findViewById(R.id.txt_teamR1_Name);
        tnum = (String) txt_teamR1.getText();
        team_name = (String) txt_teamR1_Name.getText();
        Log.w(TAG, "*** Team " + tnum + " " + team_name + "  URL ='" + FB_url[0] + "' ");
        launchVizPit(tnum, team_name, FB_url[0]);
    }
    public void btn_PitR2_Click(View view) {
        Log.i(TAG, " btn_PitR2_Click   ");
        txt_teamR2 = (TextView) findViewById(R.id.txt_teamR2);
        txt_teamR2_Name = (TextView) findViewById(R.id.txt_teamR2_Name);
        tnum = (String) txt_teamR2.getText();
        team_name = (String) txt_teamR2_Name.getText();
        launchVizPit(tnum, team_name, FB_url[1]);
    }
    public void btn_PitR3_Click(View view) {
        Log.i(TAG, " btn_PitR3_Click   ");
        txt_teamR3 = (TextView) findViewById(R.id.txt_teamR3);
        txt_teamR3_Name = (TextView) findViewById(R.id.txt_teamR3_Name);
        tnum = (String) txt_teamR3.getText();
        team_name = (String) txt_teamR3_Name.getText();
        launchVizPit(tnum, team_name, FB_url[2]);
    }
    public void btn_PitB1_Click(View view) {
        Log.i(TAG, " btn_PitB1_Click   ");
        txt_teamB1 = (TextView) findViewById(R.id.txt_teamB1);
        txt_teamB1_Name = (TextView) findViewById(R.id.txt_teamB1_Name);
        tnum = (String) txt_teamB1.getText();
        team_name = (String) txt_teamB1_Name.getText();
        launchVizPit(tnum, team_name, FB_url[3]);
    }
    public void btn_PitB2_Click(View view) {
        Log.i(TAG, " btn_PitB2_Click   ");
        txt_teamB2 = (TextView) findViewById(R.id.txt_teamB2);
        txt_teamB2_Name = (TextView) findViewById(R.id.txt_teamB2_Name);
        tnum = (String) txt_teamB2.getText();
        team_name = (String) txt_teamB2_Name.getText();
        launchVizPit(tnum, team_name, FB_url[4]);
    }
    public void btn_PitB3_Click(View view) {
        Log.i(TAG, " btn_PitB3_Click   ");
        txt_teamB3 = (TextView) findViewById(R.id.txt_teamB3);
        txt_teamB3_Name = (TextView) findViewById(R.id.txt_teamB3_Name);
        tnum = (String) txt_teamB3.getText();
        team_name = (String) txt_teamB3_Name.getText();
        launchVizPit(tnum, team_name, FB_url[5]);
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void btn_MatchR1_Click(View view) {
        Log.i(TAG, " btn_MatchR1_Click   ");
        txt_teamR1 = (TextView) findViewById(R.id.txt_teamR1);
        txt_teamR1_Name = (TextView) findViewById(R.id.txt_teamR1_Name);
        tnum = (String) txt_teamR1.getText();
        team_name = (String) txt_teamR1_Name.getText();
        Log.w(TAG, "*** Team " + tnum + " " + team_name);
        launchVizMatch(tnum, team_name);
    }
    public void btn_MatchR2_Click(View view) {
        Log.i(TAG, " btn_MatchR2_Click   ");
        txt_teamR2 = (TextView) findViewById(R.id.txt_teamR2);
        txt_teamR2_Name = (TextView) findViewById(R.id.txt_teamR2_Name);
        tnum = (String) txt_teamR2.getText();
        team_name = (String) txt_teamR2_Name.getText();
        launchVizMatch(tnum, team_name);
    }
    public void btn_MatchR3_Click(View view) {
        Log.i(TAG, " btn_MatchR3_Click   ");
        txt_teamR3 = (TextView) findViewById(R.id.txt_teamR3);
        txt_teamR3_Name = (TextView) findViewById(R.id.txt_teamR3_Name);
        tnum = (String) txt_teamR3.getText();
        team_name = (String) txt_teamR3_Name.getText();
        launchVizMatch(tnum, team_name);
    }
    public void btn_MatchB1_Click(View view) {
        Log.i(TAG, " btn_MatchB1_Click   ");
        txt_teamB1 = (TextView) findViewById(R.id.txt_teamB1);
        txt_teamB1_Name = (TextView) findViewById(R.id.txt_teamB1_Name);
        tnum = (String) txt_teamB1.getText();
        team_name = (String) txt_teamB1_Name.getText();
        launchVizMatch(tnum, team_name);
    }
    public void btn_MatchB2_Click(View view) {
        Log.i(TAG, " btn_MatchB2_Click   ");
        txt_teamB2 = (TextView) findViewById(R.id.txt_teamB2);
        txt_teamB2_Name = (TextView) findViewById(R.id.txt_teamB2_Name);
        tnum = (String) txt_teamB2.getText();
        team_name = (String) txt_teamB2_Name.getText();
        launchVizMatch(tnum, team_name);
    }
    public void btn_MatchB3_Click(View view) {
        Log.i(TAG, " btn_MatchB3_Click   ");
        txt_teamB3 = (TextView) findViewById(R.id.txt_teamB3);
        txt_teamB3_Name = (TextView) findViewById(R.id.txt_teamB3_Name);
        tnum = (String) txt_teamB3.getText();
        team_name = (String) txt_teamB3_Name.getText();
        launchVizMatch(tnum, team_name);
    }

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void launchVizMatch(String team, String name) {
        Log.i(TAG, ">>>>> launchVizMatch   <<<<<"  + team + " " + name);

        load_team = team;
        load_name = name;
        addMD_VE_Listener(pfMatchData_DBReference.orderByChild("match"));        // Load Matches
//        childTeamMD_Listner();

    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//    private void childTeamMD_Listner() {
    private void addMD_VE_Listener(final Query pfMatchData_DBReference) {
        Log.i(TAG, "<<<< getFB_Data >>>> Match Data for team " + load_team);
//        String child = "team_num";
//        String key = load_team;
//        Query query = pfMatchData_DBReference.orderByChild(child).equalTo(key);
//        query.addChildEventListener(new ChildEventListener() {

            pfMatchData_DBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.w(TAG, "@@@@ onChildAdded @@@@ Match Data for team " + load_team);
                Pearadox.Matches_Data.clear();
                matchData mdobj = new matchData();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();   /*get the data children*/
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    Log.w(TAG, " WHILE: "  + Pearadox.Matches_Data.size());
//                    System.out.println(dataSnapshot.getValue());
//                    System.out.println("  \n  \n");
                    mdobj = iterator.next().getValue(matchData.class);
                    if (mdobj.getTeam_num().matches(load_team)) {
                        Log.w(TAG, " Match: " + mdobj.getMatch() + " # "  + Pearadox.Matches_Data.size());
//                        System.out.println("  \n");
                        Pearadox.Matches_Data.add(mdobj);
                    }
               }
                Log.w(TAG, "***** Matches Loaded. # = "  + Pearadox.Matches_Data.size());
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
        Log.i(TAG,">>>>>>>>  launchVizPit " + team + " " + name + " " + imgURL);      // ** DEBUG **
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
            Log.w(TAG, ">>>>>  '" + typSelected + "'");
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
            Log.w(TAG, ">>>>>  '" + NumSelected + "'");
            matchID = matchID + NumSelected;
            Log.w(TAG, ">>>>>  Match = '" + matchID + "'");
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void buttonView_Click(View view) {
        Log.w(TAG, " Start Button Click  " + matchID);

        clearTeams();
        getTeams();         // Get the teams for match selected
    }

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void loadTblData () {
        Log.i(TAG, "#### loadTblData  ####");
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
        Log.i(TAG, ">>>>>  getURL0: " + team);

//        ImageView tbl_robotR1 = (ImageView) findViewById(R.id.tbl_robotR1);
//        ImageView tbl_robotR2 = (ImageView) findViewById(R.id.tbl_robotR2);
//        ImageView tbl_robotR3 = (ImageView) findViewById(R.id.tbl_robotR3);
//        ImageView tbl_robotB1 = (ImageView) findViewById(R.id.tbl_robotB1);
//        ImageView tbl_robotB2 = (ImageView) findViewById(R.id.tbl_robotB2);
//        ImageView tbl_robotB3 = (ImageView) findViewById(R.id.tbl_robotB3);
        URL = "";

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://paradox-2017.appspot.com/images/" + Pearadox.FRC_Event).child("robot_" + team.trim() + ".png");
        Log.e(TAG, "images/" + Pearadox.FRC_Event + "/robot_" + team.trim() + ".png" + "\n \n");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e(TAG,  FB_num + "  uri: " + uri.toString());
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
        Log.i(TAG, ">>>>>  getURL1: " + team);
        URL = "";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://paradox-2017.appspot.com/images/" + Pearadox.FRC_Event).child("robot_" + team.trim() + ".png");
        Log.e(TAG, "images/" + Pearadox.FRC_Event + "/robot_" + team.trim() + ".png" + "\n \n");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e(TAG,  FB_num + "  uri: " + uri.toString());
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
        Log.i(TAG, ">>>>>  getURL2: " + team);
        URL = "";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://paradox-2017.appspot.com/images/" + Pearadox.FRC_Event).child("robot_" + team.trim() + ".png");
        Log.e(TAG, "images/" + Pearadox.FRC_Event + "/robot_" + team.trim() + ".png" + "\n \n");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e(TAG,  FB_num + "  uri: " + uri.toString());
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
        Log.i(TAG, ">>>>>  getURL3: " + team);
        URL = "";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://paradox-2017.appspot.com/images/" + Pearadox.FRC_Event).child("robot_" + team.trim() + ".png");
        Log.e(TAG, "images/" + Pearadox.FRC_Event + "/robot_" + team.trim() + ".png" + "\n \n");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e(TAG,  FB_num + "  uri: " + uri.toString());
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
        Log.i(TAG, ">>>>>  getURL4: " + team);
        URL = "";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://paradox-2017.appspot.com/images/" + Pearadox.FRC_Event).child("robot_" + team.trim() + ".png");
        Log.e(TAG, "images/" + Pearadox.FRC_Event + "/robot_" + team.trim() + ".png" + "\n \n");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e(TAG,  FB_num + "  uri: " + uri.toString());
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
        Log.i(TAG, ">>>>>  getURL5: " + team);
        URL = "";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://paradox-2017.appspot.com/images/" + Pearadox.FRC_Event).child("robot_" + team.trim() + ".png");
        Log.e(TAG, "images/" + Pearadox.FRC_Event + "/robot_" + team.trim() + ".png" + "\n \n");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e(TAG,  FB_num + "  uri: " + uri.toString());
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
        Log.i(TAG, "$$$$$  getTeams");
        Log.w(TAG, ">>>>>  Match = '" + matchID + "'");
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
        if (matchID.length() == 3) {
            Log.i(TAG, "\n   Q U E R Y  ");
            String child = "match";
            String key = matchID;
            Query query = pfMatch_DBReference.orderByChild(child).equalTo(key);
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.w(TAG, "%%%  ChildAdded");
                    System.out.println(dataSnapshot.getValue());
                    p_Firebase.matchObj mobj = dataSnapshot.getValue(p_Firebase.matchObj.class);
                    System.out.println("Match: " + mobj.getMatch());
                    System.out.println("Type: " + mobj.getMtype());
                    System.out.println("Date: " + mobj.getDate());
                    System.out.println("R1: " + mobj.getR1());
                    System.out.println("B3: " + mobj.getB3());
                    teams.clear();          // empty the list
                    String tn = mobj.getR1();
                    findTeam(tn);
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
                    Log.w(TAG, ">>>> # team instances = " + teams.size());  //** DEBUG

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


                    Log.w(TAG, "@@@@@@@@@@@@@@@@@@@ GET TEAM DATA  @@@@@@@@@@@@@@@@@@@");  //** DEBUG

                    ProgressDialog pd = new ProgressDialog(Visualizer_Activity.this);
                    pd.setTitle("Loading Blue Alliance data for ALL teams");
                    pd.setMessage("Please wait for Blue Alliance data");
                    pd.setCancelable(false);
                    pd.show();

                    team_inst = teams.get(0);       // Team#R1
                    txt_teamR1.setText(team_inst.getTeam_num());
                    txt_teamR1_Name.setText(team_inst.getTeam_name());
                    tbl_teamR1.setText(team_inst.getTeam_num());
                    get_BA1data(team_inst.getTeam_num().trim());
//                    if (rank[0] > 0) {
                        tbl_event1R1.setText(EVT1 + " \n" + WLT1);
                        tbl_rate1R1.setText("OPR " + OPR1 + "   ↑ " + NTP1 + "  \n" + "Rank=" + rank1 + "  kPa=" + kPa1);
//                    } else {
//                        tbl_event1R1.setText("");
//                        tbl_rate1R1.setText("");
//                    }
//                    rank[0] = get_BA2data(team_inst.getTeam_num().trim());
                    if (!EVT2.matches("    ")) {
                        tbl_event2R1.setText(EVT2 + " \n" + WLT2);
                        tbl_rate2R1.setText("OPR " + OPR2 + "   ↑ " + NTP2 + "  \n" + "Rank=" + rank2 + "  kPa=" + kPa2);
                    } else {
                        tbl_event2R1.setText("");
                        tbl_rate2R1.setText("");
                    }

                    team_inst = teams.get(1);       // Team#R2
                    txt_teamR2.setText(team_inst.getTeam_num());
                    txt_teamR2_Name.setText(team_inst.getTeam_name());
                    tbl_teamR2.setText(team_inst.getTeam_num());
                    get_BA1data(team_inst.getTeam_num().trim());
//                    if (rank[0] > 0) {
                        tbl_event1R2.setText(EVT1 + " \n" + WLT1);
                        tbl_rate1R2.setText("OPR " + OPR1 + "   ↑ " + NTP1 + "  \n" + "Rank=" + rank1 + "  kPa=" + kPa1);
//                    } else {
//                        tbl_event1R2.setText("");
//                        tbl_rate1R2.setText("");
//                    }
//                    rank[0] = get_BA2data(team_inst.getTeam_num().trim());
                    if (!EVT2.matches("    ")) {
                        tbl_event2R2.setText(EVT2 + " \n" + WLT2);
                        tbl_rate2R2.setText("OPR " + OPR2 + "   ↑ " + NTP2 + "  \n" + "Rank=" + rank2 + "  kPa=" + kPa2);
                    } else {
                        tbl_event2R2.setText("");
                        tbl_rate2R2.setText("");
                    }
                    team_inst = teams.get(2);       // Team#R3
                    txt_teamR3.setText(team_inst.getTeam_num());
                    txt_teamR3_Name.setText(team_inst.getTeam_name());
                    tbl_teamR3.setText(team_inst.getTeam_num());
                    get_BA1data(team_inst.getTeam_num().trim());
//                    if (rank[0] > 0) {
                        tbl_event1R3.setText(EVT1 + " \n" + WLT1);
//                        OPR = chkOPR(team_inst.getTeam_num());
                        tbl_rate1R3.setText("OPR " + OPR1 + "   ↑ " + NTP1 + "  \n" + "Rank=" + rank1 + "  kPa=" + kPa1);
//                    } else {
//                        tbl_event1R3.setText("");
//                        tbl_rate1R3.setText("");
//                    }
//                    rank[0] = get_BA2data(team_inst.getTeam_num().trim());
                    if (!EVT2.matches("    ")) {
                        tbl_event2R3.setText(EVT2 + " \n" + WLT2);
                        tbl_rate2R3.setText("OPR " + OPR2 + "   ↑ " + NTP2 + "  \n" + "Rank=" + rank2 + "  kPa=" + kPa2);
                    } else {
                        tbl_event2R3.setText("");
                        tbl_rate2R3.setText("");
                    }
                    team_inst = teams.get(3);       // Team#B1
                    txt_teamB1.setText(team_inst.getTeam_num());
                    txt_teamB1_Name.setText(team_inst.getTeam_name());
                    tbl_teamB1.setText(team_inst.getTeam_num());
                    get_BA1data(team_inst.getTeam_num().trim());
//                    if (rank[0] > 0) {
                        tbl_event1B1.setText(EVT1 + " \n" + WLT1);
//                        OPR = chkOPR(team_inst.getTeam_num());
                        tbl_rate1B1.setText("OPR " + OPR1 + "   ↑ " + NTP1 + "  \n" + "Rank=" + rank1 + "  kPa=" + kPa1);
//                    } else {
//                        tbl_event1B1.setText("");
//                        tbl_rate1B1.setText("");
//                    }
//                    rank[0] = get_BA2data(team_inst.getTeam_num().trim());
                    if (!EVT2.matches("    ")) {
                        tbl_event2B1.setText(EVT2 + " \n" + WLT2);
                        tbl_rate2B1.setText("OPR " + OPR2 + "   ↑ " + NTP2 + "  \n" + "Rank=" + rank2 + "  kPa=" + kPa2);
                    } else {
                        tbl_event2B1.setText("");
                        tbl_rate2B1.setText("");
                    }
                    team_inst = teams.get(4);       // Team#B2
                    txt_teamB2.setText(team_inst.getTeam_num());
                    txt_teamB2_Name.setText(team_inst.getTeam_name());
                    tbl_teamB2.setText(team_inst.getTeam_num());
                    get_BA1data(team_inst.getTeam_num().trim());
//                    if (rank[0] > 0) {
                        tbl_event1B2.setText(EVT1 + " \n" + WLT1);
//                        OPR = chkOPR(team_inst.getTeam_num());
                        tbl_rate1B2.setText("OPR " + OPR1 + "   ↑ " + NTP1 + "  \n" + "Rank=" + rank1 + "  kPa=" + kPa1);
//                    } else {
//                        tbl_event1B2.setText("");
//                        tbl_rate1B2.setText("");
//                    }
//                    rank[0] = get_BA2data(team_inst.getTeam_num().trim());
                    if (!EVT2.matches("    ")) {
                        tbl_event2B2.setText(EVT2 + " \n" + WLT2);
                        tbl_rate2B2.setText("OPR " + OPR2 + "   ↑ " + NTP2 + "  \n" + "Rank=" + rank2 + "  kPa=" + kPa2);
                    } else {
                        tbl_event2B2.setText("");
                        tbl_rate2B2.setText("");
                    }
                    team_inst = teams.get(5);       // Team#B3
                    txt_teamB3.setText(team_inst.getTeam_num());
                    txt_teamB3_Name.setText(team_inst.getTeam_name());
                    tbl_teamB3.setText(team_inst.getTeam_num());
                    get_BA1data(team_inst.getTeam_num().trim());
//                    if (rank[0] > 0) {
                        tbl_event1B3.setText(EVT1 + " \n" + WLT1);
                        tbl_rate1B3.setText("OPR " + OPR1 + "   ↑ " + NTP1 + "  \n" + "Rank=" + rank1 + "  kPa=" + kPa1);
//                    } else {
//                        tbl_event1B3.setText("");
//                        tbl_rate1B3.setText("");
//                    }
//                    rank[0] = get_BA2data(team_inst.getTeam_num().trim());
                    if (!EVT2.matches("    ")) {
                        tbl_event2B3.setText(EVT2 + " \n" + WLT2);
                        tbl_rate2B3.setText("OPR " + OPR2 + "   ↑ " + NTP2 + "  \n" + "Rank=" + rank2 + "  kPa=" + kPa2);
                    } else {
                        tbl_event2B3.setText("");
                        tbl_rate2B3.setText("");
                    }

                    pd.dismiss();
                    loadTblData();      // Load the images (if any)

                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.w(TAG, "%%%  ChildChanged");
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.w(TAG, "%%%  ChildRemoved");
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    Log.w(TAG, "%%%  ChildMoved");
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else {
            Toast.makeText(getBaseContext(), "** Select both Match TYPE & NUMBER ** ", Toast.LENGTH_LONG).show();
            // ToDo - turn toggle back to logon
        }
    }

    private String chkOPR(String team) {
        Log.i(TAG, "###  chkOPR ### " + team);
        // ToDo - Get OPR data from Blue Alliance
        String opr = "000.0";
//        for(int i = 0; i < OPR_Teams.length; i++) {   // Search Teams to find Rank
//            Log.w(TAG, "opr Team " + OPR_Teams[i]);
//            if (OPR_Teams[i].matches(team)) {
//                opr = OPR_rating[i];
//            }
//        }
        return opr;
    }

    private int get_BA1data(String team) {
        Log.i(TAG, "%%%  get_BA1data %%% " + team);
        int rank = 0; int numEvts = 0; int bad1 = 99;
        TBA BA1 = new TBA();
        Event[] events = new TBA().getTeamEvents(Integer.parseInt(team), 2017);
        evt1 = events.clone();
        System.out.println("events: "+ events.length );
        for(int i = 0; i < events.length; i++) {   // Search Teams to find Rank
            Log.w(TAG, "%%% Event %%% " + evt1[i].name + "  " + evt1[i].event_code + " " + evt1[i].name.substring(0,7));
            if (evt1[i].name.substring(0,7).matches("Galileo") || evt1[i].name.substring(0,6).matches("Hopper") || evt1[i].name.substring(0,6).matches("Turing") ||
                    evt1[i].name.substring(0,6).matches("Carver") || evt1[i].name.substring(0,6).matches("Newton") || evt1[i].name.substring(0,8).matches("Roebling")
                    || evt1[i].name.substring(0,5).matches("FIRST")) {
                bad1 = i;
            } else {
                numEvts++;
            }
        }
        Log.w(TAG, "#=" + events.length + " numEvts=" + numEvts + "  " + bad1 + " \n ");
        switch (events.length) {
            case 1:
                EVT1 = evt1[0].event_code.toUpperCase();
                EVT2 = "    ";
                break;
            case 2:
                if (bad1 == 99) {
                    EVT1 = evt1[0].event_code.toUpperCase();
                    EVT2 = evt1[1].event_code.toUpperCase();
                } else {
                    if (bad1 == 0) {
                        EVT1 = evt1[1].event_code.toUpperCase();
                        EVT2 = "    ";
                    } else {
                        EVT1 = evt1[0].event_code.toUpperCase();
                        EVT2 = "    ";
                    }
                }
                break;
            case 3:
                Log.w(TAG, " CASE #3  numEvts=" + numEvts + "  " + bad1);
                if (bad1 == 99) {       // Use last 2
                    EVT1 = evt1[1].event_code.toUpperCase();
                    EVT2 = evt1[2].event_code.toUpperCase();
                } else {
                    if (bad1 == 0) {
                        Log.w(TAG, " CASE #3 " + evt1[1].event_code + "  " + evt1[2].event_code);
                        EVT1 = evt1[1].event_code.toUpperCase();
                        EVT2 = evt1[2].event_code.toUpperCase();
                    } else {
                        if (bad1 == 1) {
                            EVT1 = evt1[0].event_code.toUpperCase();
                            EVT2 = evt1[2].event_code.toUpperCase();
                        } else {
                            EVT1 = evt1[0].event_code.toUpperCase();
                            EVT2 = evt1[1].event_code.toUpperCase();
                        }
                    }
                }
                break;
            case 4:
                Log.w(TAG, " WOW!! #4  numEvts=" + numEvts + "  " + bad1);
                if (bad1 == 99) {
                    EVT1 = evt1[2].event_code.toUpperCase();
                    EVT2 = evt1[3].event_code.toUpperCase();
                } else {
                    if (bad1 == 0) {
                        EVT1 = evt1[2].event_code.toUpperCase();
                        EVT2 = evt1[3].event_code.toUpperCase();
                    } else {
                        if (bad1 == 1) {
                            EVT1 = evt1[2].event_code.toUpperCase();
                            EVT2 = evt1[3].event_code.toUpperCase();
                        } else {
                            if (bad1 == 2) {
                                EVT1 = evt1[1].event_code.toUpperCase();
                                EVT2 = evt1[3].event_code.toUpperCase();
                            } else {
                            }
                        }
                    }
                }
                break;
            default:                // ????
                Log.e(TAG, "*** Error - numEvts??  ***  " + numEvts);
        }
//        new Load_BAdata_Task();

        Log.w(TAG, "Evt1=" + EVT1 + "  Evt2=" + EVT2);
        TBA t = new TBA();
        Event e = t.getEvent(EVT1.toLowerCase(), 2017);
        teams1 = e.teams.clone();
        Log.e(TAG, "BLUE1 " + teams1.length);
        BA1numTeams = e.teams.length;

//        Team t = BA1.getTeam(Integer.parseInt(team));
//        System.out.println("Team #: "+t.team_number + "  " +t.rank +" OPR: "+t.opr+ "  " +t.touchpad );
//        System.out.println("  Record: " +t.record + "  " +t.pressure +" pts "+t.matchPoints+ "  score" +t.rankingScore + " \n \n");

        String team_num = "";  rank1=""; WLT1=""; OPR1="";NTP1="";kPa1="";
        for(int i = 0; i < BA1numTeams; i++) {   // Search Teams to find Rank
            team_num = String.valueOf(teams1[i].team_number);
//            Log.w(TAG, "LOOP " + i + " " + team_num);
            if (team_num.matches(team)) {
//                Log.w(TAG, ">>>>>>>>>>>>>>>>  Found Team # " + team);
                rank1 = String.valueOf(i + 1);                                       // Rank
                OPR1 = String.format("%3.1f",(teams1[i].opr));       // OPR
                NTP1 = String.format("%3.1f",(teams1[i].touchpad));  // Touchpad
                kPa1 = String.format("%3.1f",(teams1[i].pressure));  // kPa - Pressure
                WLT1 = teams1[i].record;                             // W-L-T Record
            }
        }

        if (!EVT2.matches("    ")) {
            Event x = t.getEvent(EVT2.toLowerCase(), 2017);
            teams2 = x.teams.clone();
//        Team[] teams2 = x.teams;
            Log.e(TAG, "BLUE2 " + teams2.length);
            BA2numTeams = x.teams.length;

            team_num = "";
            rank2 = "";
            WLT2 = "";
            OPR2 = "";
            NTP2 = "";
            kPa2 = "";
            for (int i = 0; i < BA2numTeams; i++) {      // Search Teams to find Rank
                team_num = String.valueOf(teams2[i].team_number);
//            Log.w(TAG, "LOOP " + i + " " + team_num);
                if (team_num.matches(team)) {
//                    Log.w(TAG, ">>>>>>>>>>>>>>>>  Found Team # " + team);
                    rank2 = String.valueOf(i + 1);                        // Rank
                    OPR2 = String.format("%3.1f", (teams2[i].opr));       // OPR
                    NTP2 = String.format("%3.1f", (teams2[i].touchpad));  // Touchpad
                    kPa2 = String.format("%3.1f", (teams2[i].pressure));  // kPa - Pressure
                    WLT2 = teams2[i].record;                              // W-L-T Record
                }
            }
        }
        return rank;
    }

    public class Load_BAdata_Task extends AsyncTask<Void, Integer, String> {
        int count = 0;

        @Override
        protected void onPreExecute() {
            progressBar1.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            while (count < 5) {
                SystemClock.sleep(1000);
                count++;
                publishProgress(count * 20);
            }
            return "Complete";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar1.setProgress(values[0]);
        }
    }
//    private int get_BA2data(String team) {
//        Log.i(TAG, "@@@  get_BA2data @@@ " + team);
//        int rank = 0;
//        String team_num = "";  WLT1 =""; WLT2 ="";
//        for(int i = 0; i < BA2numTeams; i++) {      // Search Teams to find Rank
//            team_num = String.valueOf(teams2[i].team_number);
////            Log.w(TAG, "LOOP " + i + " " + team_num);
//            if (team_num.matches(team)) {
//                Log.w(TAG, ">>>>>>>>>>>>>>>>  Found Team # " + team);
//                rank = i + 1;                                       // Rank
//                EVT = "TXHO";
//                OPR = String.format("%3.1f",(teams2[i].opr));       // OPR
//                NTP = String.format("%3.1f",(teams2[i].touchpad));  // Touchpad
//                kPa = String.format("%3.1f",(teams2[i].pressure));  // kPa - Pressure
//                WLT = teams2[i].record;                             // W-L-T Record
//            }
//        }
//        return rank;
//    }

    private void findTeam(String tnum) {
        Log.i(TAG, "$$$$$  findTeam " + tnum);
        boolean found = false;
        for (int i = 0; i < Pearadox.numTeams; i++) {        // check each team entry
            if (Pearadox.team_List.get(i).getTeam_num().equals(tnum)) {
                team_inst = Pearadox.team_List.get(i);
                teams.add(team_inst);
//                Log.w(TAG, "===  Team " + team_inst.getTeam_num() + " " + team_inst.getTeam_name() + " " + team_inst.getTeam_loc());
                found = true;
                break;  // found it!
            }
        }  // end For
        if (!found) {
            Toast.makeText(getBaseContext(),"** Team '" + tnum + "' from Matches table _NOT_ found in Team list  ** ", Toast.LENGTH_LONG).show();
            p_Firebase.teamsObj team_dummy = new p_Firebase.teamsObj("****", "team _NOT_ found in Team list - Check for TYPOs in Match Sched."," ");
            teams.add(team_dummy);
        }
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void loadMatches() {
        Log.i(TAG, "###  loadMatches  ###");

        addMatchSched_VE_Listener(pfMatch_DBReference.orderByChild("match"));

    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void addMatchSched_VE_Listener(final Query pfMatch_DBReference) {
        pfMatch_DBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "******* Firebase retrieve Match Schedule  *******");
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
                        next_Match =  next_Match + match_inst.getMatch() + "  ";
                    }
                    if (match_inst.getR2().matches("5414")) {
                        next_Match =  next_Match + match_inst.getMatch() + "  ";
                    }
                    if (match_inst.getR3().matches("5414")) {
                        next_Match =  next_Match + match_inst.getMatch() + "  ";
                    }
                    if (match_inst.getB1().matches("5414")) {
                        next_Match =  next_Match + match_inst.getMatch() + "  ";
                    }
                    if (match_inst.getB2().matches("5414")) {
                        next_Match =  next_Match + match_inst.getMatch() + "  ";
                    }
                    if (match_inst.getB3().matches("5414")) {
                        next_Match =  next_Match + match_inst.getMatch() + "  ";
                    }
                }
                Log.w(TAG,"### Matches ###  : " + matchList.size());
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
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }

//###################################################################
//###################################################################
//###################################################################
@Override
public void onStart() {
    super.onStart();
    Log.v(TAG, "onStart");

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

package com.pearadox.scout_5414;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Visualizer_Activity extends AppCompatActivity {

    String TAG = "Visualizer_Activity";        // This CLASS name
    TextView txt_dev, txt_stud;
    ArrayAdapter<String> adapter_typ;
    public String typSelected = " ";
    Spinner spinner_MatchType;
    Spinner spinner_MatchNum;
    ArrayAdapter<String> adapter_Num;
    public String NumSelected = " ";
    public String matchID = "T00";      // Type + #
    TextView txt_teamR1, txt_teamR2, txt_teamR3, txt_teamB1, txt_teamB2, txt_teamB3;
    TextView txt_teamR1_Name, txt_teamR2_Name, txt_teamR3_Name, txt_teamB1_Name, txt_teamB2_Name, txt_teamB3_Name;
    TextView tbl_teamR1, tbl_teamR2, tbl_teamR3, tbl_teamB1, tbl_teamB2, tbl_teamB3;
    Button button_View;
    String team_num, team_name, team_loc;
    p_Firebase.teamsObj team_inst = new p_Firebase.teamsObj(team_num, team_name,  team_loc);
    private FirebaseDatabase pfDatabase;
//    private DatabaseReference pfStudent_DBReference;
//    private DatabaseReference pfDevice_DBReference;
//    private DatabaseReference pfTeam_DBReference;
    private DatabaseReference pfMatch_DBReference;
//    private DatabaseReference pfCur_Match_DBReference;
    ArrayList<p_Firebase.teamsObj> teams = new ArrayList<p_Firebase.teamsObj>();
    ImageView tbl_robotR1, tbl_robotR2, tbl_robotR3, tbl_robotB1, tbl_robotB2, tbl_robotB3;
    String tnum = "";
    Bitmap img;
    String FB_url ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizer);
        Log.i(TAG, "@@@@  Visualizer_Activity started  @@@@");
        Bundle bundle = this.getIntent().getExtras();
        String param1 = bundle.getString("dev");
        String param2 = bundle.getString("stud");
        Log.d(TAG, param1 + " " + param2);      // ** DEBUG **

        txt_dev = (TextView) findViewById(R.id.txt_Dev);
        txt_stud = (TextView) findViewById(R.id.txt_TeamName);
        txt_dev.setText(param1);
        txt_stud.setText(param2);
        matchID = "";

        pfDatabase = FirebaseDatabase.getInstance();
//        pfTeam_DBReference = pfDatabase.getReference("teams");              // Tteam data from Firebase D/B
//        pfStudent_DBReference = pfDatabase.getReference("students");        // List of Students
//        pfDevice_DBReference = pfDatabase.getReference("devices");          // List of Students
        pfMatch_DBReference = pfDatabase.getReference("matches");           // List of Students
//        pfCur_Match_DBReference = pfDatabase.getReference("current-match"); // _THE_ current Match

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

    }


    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private class type_OnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            typSelected = parent.getItemAtPosition(pos).toString();
            Log.d(TAG, ">>>>>  '" + typSelected + "'");
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
            Log.d(TAG, ">>>>>  '" + NumSelected + "'");
            matchID = matchID + NumSelected;
            Log.d(TAG, ">>>>>  Match = '" + matchID + "'");
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }

    public void buttonView_Click(View view) {
        Log.d(TAG, " VIEW Button Click  ");

        getTeams();         // Get the teams for match selected
    }
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void loadTblData () {
        // Start getting data for Table
        Log.d(TAG, " Loading Table Data ");          //** DEBUG
        ImageView tbl_robotR1 = (ImageView) findViewById(R.id.tbl_robotR1);
        ImageView tbl_robotR2 = (ImageView) findViewById(R.id.tbl_robotR2);
        ImageView tbl_robotR3 = (ImageView) findViewById(R.id.tbl_robotR3);
        ImageView tbl_robotB1 = (ImageView) findViewById(R.id.tbl_robotB1);
        ImageView tbl_robotB2 = (ImageView) findViewById(R.id.tbl_robotB2);
        ImageView tbl_robotB3 = (ImageView) findViewById(R.id.tbl_robotB3);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://paradox-2017.appspot.com");
        StorageReference imagesRef = storageRef.child("images");
        tnum = (String) txt_teamR1.getText();
        FB_url = "gs://paradox-2017.appspot.com/images/robot_" + tnum + ".jpg";
        Log.d(TAG, "FireBase storage " + FB_url);
        Picasso.with(this).load(FB_url).into(tbl_robotR1);
        tnum = (String) txt_teamR2.getText();
        FB_url = "gs://paradox-2017.appspot.com/images/robot_" + tnum + ".jpg";
        Picasso.with(this).load(FB_url).into(tbl_robotR2);
        tnum = (String) txt_teamR3.getText();
        FB_url = "gs://paradox-2017.appspot.com/images/robot_" + tnum + ".jpg";
        Picasso.with(this).load(FB_url).into(tbl_robotR3);
        tnum = (String) txt_teamB1.getText();
        FB_url = "gs://paradox-2017.appspot.com/images/robot_" + tnum + ".jpg";
        Picasso.with(this).load(FB_url).into(tbl_robotB1);
        tnum = (String) txt_teamB2.getText();
        FB_url = "gs://paradox-2017.appspot.com/images/robot_" + tnum + ".jpg";
        Picasso.with(this).load(FB_url).into(tbl_robotB2);
        tnum = (String) txt_teamB3.getText();
        FB_url = "gs://paradox-2017.appspot.com/images/robot_" + tnum + ".jpg";
        Picasso.with(this).load(FB_url).into(tbl_robotB3);
        Toast.makeText(getBaseContext(), "Robot images loaded", Toast.LENGTH_LONG).show();  //** DEBUG
    }

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void getTeams() {
        Log.i(TAG, "$$$$$  getTeams");
        Log.d(TAG, ">>>>>  Match = '" + matchID + "'");
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
            Log.i(TAG, "   Q U E R Y  ");
            String child = "match";
            String key = matchID;
            Query query = pfMatch_DBReference.orderByChild(child).equalTo(key);
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d(TAG, "%%%  ChildAdded");
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
                    Log.d(TAG, ">>>> # team instances = " + teams.size());  //** DEBUG
                    Log.d(TAG, ">>>> # team instances = " + teams.size());  //** DEBUG

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

                    team_inst = teams.get(0);
                    txt_teamR1.setText(team_inst.getTeam_num());
                    txt_teamR1_Name.setText(team_inst.getTeam_name());
                    tbl_teamR1.setText(team_inst.getTeam_num());
                    team_inst = teams.get(1);
                    txt_teamR2.setText(team_inst.getTeam_num());
                    txt_teamR2_Name.setText(team_inst.getTeam_name());
                    tbl_teamR2.setText(team_inst.getTeam_num());
                    team_inst = teams.get(2);
                    txt_teamR3.setText(team_inst.getTeam_num());
                    txt_teamR3_Name.setText(team_inst.getTeam_name());
                    tbl_teamR3.setText(team_inst.getTeam_num());
                    team_inst = teams.get(3);
                    txt_teamB1.setText(team_inst.getTeam_num());
                    txt_teamB1_Name.setText(team_inst.getTeam_name());
                    tbl_teamB1.setText(team_inst.getTeam_num());
                    team_inst = teams.get(4);
                    txt_teamB2.setText(team_inst.getTeam_num());
                    txt_teamB2_Name.setText(team_inst.getTeam_name());
                    tbl_teamB2.setText(team_inst.getTeam_num());
                    team_inst = teams.get(5);
                    txt_teamB3.setText(team_inst.getTeam_num());
                    txt_teamB3_Name.setText(team_inst.getTeam_name());
                    tbl_teamB3.setText(team_inst.getTeam_num());
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.d(TAG, "%%%  ChildChanged");
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "%%%  ChildRemoved");
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    Log.d(TAG, "%%%  ChildMoved");
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

    private void findTeam(String tnum) {
        Log.i(TAG, "$$$$$  findTeam " + tnum);
        boolean found = false;
        for (int i = 0; i < Pearadox.numTeams; i++) {        // check each team entry
            if (Pearadox.team_List.get(i).getTeam_num().equals(tnum)) {
                team_inst = Pearadox.team_List.get(i);
                teams.add(team_inst);
//                Log.d(TAG, "===  Team " + team_inst.getTeam_num() + " " + team_inst.getTeam_name() + " " + team_inst.getTeam_loc());
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
    }

}

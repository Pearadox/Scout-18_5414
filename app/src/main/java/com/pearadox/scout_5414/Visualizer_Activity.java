package com.pearadox.scout_5414;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;
import java.util.Iterator;

public class Visualizer_Activity extends AppCompatActivity {

    String TAG = "Visualizer_Activity";        // This CLASS name
    TextView txt_dev, txt_stud;
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
    public String matchID = "T00";      // Type + #
    TextView txt_EventName, txt_MatchID;
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

        txt_dev = (TextView) findViewById(R.id.txt_Dev);
        txt_stud = (TextView) findViewById(R.id.txt_TeamName);
        txt_dev.setText(param1);
        txt_stud.setText(param2);
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
//        pfCur_Match_DBReference = pfDatabase.getReference("current-match"); // _THE_ current Match
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

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
    public void btn_PitR1_Click(View view) {
        Log.i(TAG, " btn_PitR1_Click   ");
        txt_teamR1 = (TextView) findViewById(R.id.txt_teamR1);
        txt_teamR1_Name = (TextView) findViewById(R.id.txt_teamR1_Name);
        tnum = (String) txt_teamR1.getText();
        team_name = (String) txt_teamR1_Name.getText();
        Log.w(TAG, "*** Team " + tnum + " " + team_name);
//        FB_url = "https://firebasestorage.googleapis.com/v0/b/paradox-2017.appspot.com/o/images%2Ftxlu%2Frobot_1296.png?alt=media&token=e6c8fbc1-f0e1-4ddc-9e01-f30a2a32a291";
        launchVizPit(tnum, team_name, FB_url);
    }
    public void btn_MatchR1_Click(View view) {
        Log.i(TAG, " btn_MatchR1_Click   ");
        txt_teamR1 = (TextView) findViewById(R.id.txt_teamR1);
        txt_teamR1_Name = (TextView) findViewById(R.id.txt_teamR1_Name);
        tnum = (String) txt_teamR1.getText();
        team_name = (String) txt_teamR1_Name.getText();
        Log.w(TAG, "*** Team " + tnum + " " + team_name);
        launchVizMatch(tnum, team_name);
    }

    private void launchVizMatch(String team, String name) {
        Intent pit_intent = new Intent(Visualizer_Activity.this, VisMatch_Activity.class);
        Bundle VZbundle = new Bundle();
        VZbundle.putString("team", team);        // Pass data to activity
        VZbundle.putString("name", name);        // Pass data to activity
        pit_intent.putExtras(VZbundle);
        startActivity(pit_intent);               // Start Visualizer for Match Data
    }

    private void launchVizPit(String team, String name, String url) {
        Intent pit_intent = new Intent(Visualizer_Activity.this, VisPit_Activity.class);
        Bundle VZbundle = new Bundle();
        VZbundle.putString("team", team);        // Pass data to activity
        VZbundle.putString("name", name);        // Pass data to activity
        VZbundle.putString("url", url);          // Pass data to activity
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
        getURL(tnum);   // Get the Firebase URL if photo exists
//        FB_url = "gs://paradox-2017.appspot.com/images/" + Pearadox.FRC_Event + "/robot_" + tnum + ".png";
        Log.w(TAG, "FireBase storage " + FB_url);
        if (FB_url.length() > 1) {
            Picasso.with(this).load(FB_url).into(tbl_robotR1);
        } else {
            tbl_robotR1.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        }
        tnum = (String) txt_teamR2.getText();
        getURL(tnum);   // Get the Firebase URL if photo exists
        if (FB_url.length() > 1) {
            Picasso.with(this).load(FB_url).into(tbl_robotR2);
        } else {
            tbl_robotR2.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        }
        tnum = (String) txt_teamR3.getText();
        getURL(tnum);   // Get the Firebase URL if photo exists
        if (FB_url.length() > 1) {
            Picasso.with(this).load(FB_url).into(tbl_robotR3);
        } else {
            tbl_robotR3.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        }
        tnum = (String) txt_teamB1.getText();
        getURL(tnum);   // Get the Firebase URL if photo exists
        if (FB_url.length() > 1) {
            Picasso.with(this).load(FB_url).into(tbl_robotB1);
        } else {
            tbl_robotB1.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        }
        tnum = (String) txt_teamB2.getText();
        getURL(tnum);   // Get the Firebase URL if photo exists
        if (FB_url.length() > 1) {
            Picasso.with(this).load(FB_url).into(tbl_robotB2);
        } else {
            tbl_robotB2.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        }
        tnum = (String) txt_teamB3.getText();
        getURL(tnum);   // Get the Firebase URL if photo exists
        if (FB_url.length() > 1) {
            Picasso.with(this).load(FB_url).into(tbl_robotB3);
        } else {
            tbl_robotB3.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        }
//        Toast.makeText(getBaseContext(), "Robot images loaded", Toast.LENGTH_LONG).show();  //** DEBUG
    }

    private void getURL(String team) {
        Log.i(TAG, ">>>>>  getURL: " + team);

        FB_url = "";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://paradox-2017.appspot.com");
//        StorageReference imagesRef = storageRef.child("images/" + Pearadox.FRC_Event);
        storageRef.child("images/" + Pearadox.FRC_Event + "/" + team.trim() + ".png" ).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.w(TAG, "\n  uri: " + uri);
//                Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
//                FB_url = downloadUri.toString(); /// The string(file link) that you need
                FB_url = String.valueOf(uri);
                Log.w(TAG, "\n  URL: " + FB_url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void getTeams() {
        Log.i(TAG, "$$$$$  getTeams");
        Log.w(TAG, ">>>>>  Match = '" + matchID + "'");
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
//                    Log.w(TAG, ">>>> # team instances = " + teams.size());  //** DEBUG

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
//        matchList.add("Q01"  + "  Time: 2:00PM" );
//        matchList.add("Q02"  + "  Time: 2:15PM" );
//        matchList.add("Q03"  + "  Time: 2:30PM" );
//        matchList.add("Q04"  + "  Time: 2:45PM" );
//        matchList.add("Q05"  + "  Time: 3:00PM" );
//        matchList.add("Q06"  + "  Time: 3:15PM" );
//        matchList.add("Q07"  + "  Time: 3:30PM" );
//        matchList.add("Q08"  + "  Time: 3:45PM" );
//        matchList.add("Q09"  + "  Time: 4:00PM" );
//        matchList.add("Q10"  + "  Time: 4:15PM" );
//        matchList.add("Q11"  + "  Time: 4:30PM" );
//        matchList.add("Q12"  + "  Time: 4:45PM" );

    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void addMatchSched_VE_Listener(final Query pfMatch_DBReference) {
        pfMatch_DBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "******* Firebase retrieve Match Schedule  *******");
                matchList.clear();
                p_Firebase.matchObj match_inst = new p_Firebase.matchObj();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();   /*get the data children*/
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    match_inst = iterator.next().getValue(p_Firebase.matchObj.class);
//                    Log.w(TAG,"      " + match_inst.getMatch());
//                    matchList.add(match_inst.getMatch() + "  Time: " + match_inst.getTime() + "  " + match_inst.getMtype());
                    matchList.add(match_inst.getMatch() +  "  " + match_inst.getMtype());
                }
                Log.w(TAG,"### Matches ###  : " + matchList.size());
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


    //###################################################################
//###################################################################
//###################################################################
@Override
public void onStart() {
    super.onStart();
    Log.v(TAG, "onStart");

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

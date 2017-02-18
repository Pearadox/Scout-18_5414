package com.pearadox.scout_5414;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;


public class MatchScoutActivity extends AppCompatActivity {

    String TAG = "MatchScout_Activity";      // This CLASS name
    boolean onStart = false;
    public static String device = " ";
    public static String studID = " ";
    TextView txt_dev, txt_stud, txt_Match, txt_MyTeam;
    ImageView imgScoutLogo;
    public String matchID = "T00";      // Type + #
    String team_num, team_name, team_loc;
    p_Firebase.teamsObj team_inst = new p_Firebase.teamsObj(team_num, team_name, team_loc);
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfTeam_DBReference;
    private DatabaseReference pfMatch_DBReference;
    private DatabaseReference pfCur_Match_DBReference;
    TextView txt_TeamName;
    TextView txt_GearsPlaced;
    private Button button_GearsMinus, button_GearsPlus;
    int gearNum = 0;

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "<< Match Scout >>");
        onStart = false;
        setContentView(R.layout.activity_match_scout);
        Bundle bundle = this.getIntent().getExtras();
        String device = bundle.getString("dev");
        String studID = bundle.getString("stud");
        Log.d(TAG, device + " " + studID);      // ** DEBUG **
        pfDatabase = FirebaseDatabase.getInstance();
        pfTeam_DBReference = pfDatabase.getReference("teams");              // Tteam data from Firebase D/B
//        pfStudent_DBReference = pfDatabase.getReference("students");        // List of Students
//        pfDevice_DBReference = pfDatabase.getReference("devices");          // List of Students
        pfMatch_DBReference = pfDatabase.getReference("matches");           // List of Students
        pfCur_Match_DBReference = pfDatabase.getReference("current-match"); // _THE_ current Match

        txt_GearsPlaced = (TextView) findViewById(R.id.txt_GearsPlaced);
        button_GearsMinus = (Button) findViewById(R.id.button_GearsMinus);
        button_GearsPlus = (Button) findViewById(R.id.button_GearsPlus);
        txt_GearsPlaced.setText(Integer.toString(gearNum));

        button_GearsPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // ToDo check to ensure not over MAX # gears
                gearNum++;
                Log.d(TAG, "Gears = " + gearNum);      // ** DEBUG **
                txt_GearsPlaced.setText(Integer.toString(gearNum));    // Perform action on click
            }
        });
        button_GearsMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // ToDo make sure not already at zero
                gearNum--;
                Log.d(TAG, "Gears = " + gearNum);      // ** DEBUG **
                txt_GearsPlaced.setText(Integer.toString(gearNum));    // Perform action on click
            }
        });
        txt_dev = (TextView) findViewById(R.id.txt_Dev);
        txt_stud = (TextView) findViewById(R.id.txt_TeamName);
        txt_Match = (TextView) findViewById(R.id.txt_Match);
        txt_MyTeam = (TextView) findViewById(R.id.txt_MyTeam);
        txt_TeamName = (TextView) findViewById(R.id.txt_TeamName);
        ImageView imgScoutLogo = (ImageView) findViewById(R.id.imageView_MS);
        txt_dev.setText(device);
        txt_stud.setText(studID);
        txt_Match.setText("");
        txt_MyTeam.setText("");
        txt_TeamName.setText("");
        String devcol = device.substring(0,3);
        Log.d(TAG, "color=" + devcol);
        if (devcol.equals("Red")) {
            imgScoutLogo.setImageDrawable(getResources().getDrawable(R.drawable.red_scout));
        } else {
            imgScoutLogo.setImageDrawable(getResources().getDrawable(R.drawable.blue_scout));
        }

    }
    private void getMatch() {
        Log.d(TAG, "%%%%  getMatch  %%%%");
        pfCur_Match_DBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Current Match - onDataChange  %%%%");
                txt_Match = (TextView) findViewById(R.id.txt_Match);
                txt_MyTeam = (TextView) findViewById(R.id.txt_MyTeam);
                txt_TeamName = (TextView) findViewById(R.id.txt_TeamName);
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();   /*get the data children*/
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    p_Firebase.curMatch match_Obj = iterator.next().getValue(p_Firebase.curMatch.class);
                    matchID = match_Obj.getCur_match();
//                    Log.d(TAG, "***>  Current Match = " + matchID + " " + match_Obj.getR1() + " " + match_Obj.getB3());
                    if (matchID.equals(null)) {
                        txt_Match.setText(" ");
                        txt_MyTeam.setText(" ");
                        txt_TeamName.setText(" ");
                    } else {        // OK!!  Match has started
                        txt_Match.setText(matchID);
//                        Log.d(TAG, "Device = " + Pearadox.FRC514_Device + " ->" + onStart);
                        switch (Pearadox.FRC514_Device) {          // Who am I?!?
                            case ("Red-1"):             //#Red or Blue Scout
                                txt_MyTeam.setText(match_Obj.getR1());
                                break;
                            case ("Red-2"):             //#
                                txt_MyTeam.setText(match_Obj.getR2());
                                break;
                            case ("Red-3"):             //#
                                txt_MyTeam.setText(match_Obj.getR3());
                                break;
                            case ("Blue-1"):            //#
                                txt_MyTeam.setText(match_Obj.getB1());
                                break;
                            case ("Blue-2"):            //#
                                txt_MyTeam.setText(match_Obj.getB2());
                                break;
                            case ("Blue-3"):            //#####
                                txt_MyTeam.setText(match_Obj.getB3());
                                break;
                            default:                //
                                Log.d(TAG, "device is _NOT_ a Scout ->" + device );
                        }
                        String tn = (String) txt_MyTeam.getText();
                        findTeam(tn);   // Find Team info
                        txt_TeamName.setText(team_inst.getTeam_name());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*listener failed or was removed for security reasons*/
            }
        });
    }
    private void findTeam(String tnum) {
        Log.i(TAG, "$$$$$  findTeam " + tnum);
        boolean found = false;
        for (int i = 0; i < Pearadox.numTeams; i++) {        // check each team entry
            if (Pearadox.team_List.get(i).getTeam_num().equals(tnum)) {
                team_inst = Pearadox.team_List.get(i);
//                Log.d(TAG, "===  Team " + team_inst.getTeam_num() + " " + team_inst.getTeam_name() + " " + team_inst.getTeam_loc());
                found = true;
                break;  // found it!
            }
        }  // end For
        if (!found) {
            Log.e(TAG, "****** ERROR - Team _NOT_ found!! = " + tnum);
        }
    }

//###################################################################
//###################################################################
//###################################################################
@Override
public void onStart() {
    super.onStart();
    Log.v(TAG, "onStart");

    onStart = true;
    getMatch();      // Get current match
    Log.d(TAG, "onStart Device = " + device + " ->" + onStart);
}

    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Device", device);
        editor.putString("Student", studID);
        editor.commit(); 		// keep same data
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        onStart = false;
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String device = prefs.getString("Device", "");
        String studID = prefs.getString("Student", "");
        Log.d(TAG, "Dev=" + device + "  " + "Student=" + studID);
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

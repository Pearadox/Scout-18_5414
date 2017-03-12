package com.pearadox.scout_5414;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class VisPit_Activity extends AppCompatActivity {

    String TAG = "VisPit_Activity";        // This CLASS name
    String tnum = "", tname = "", imgURL = "";
    TextView txt_team, txt_teamName;
    TextView txt_Dim, txt_TotWheels, txt_NumTrac, txt_NumOmni, txt_NumMecanum, txt_FuelCap,txt_Scout, txt_Comments;
    ImageView imgView_Robot;                // Robot image
    CheckBox chkBox_Gear, chkBox_Fuel, chkBox_Shooter, chkBox_Vision, chkBox_Pneumatics, chkBox_FuelManip, chkBox_Climb;
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfPitData_DBReference;

    // ===================  Data Elements for Pit Scout object ===================
    public String teamSelected = " ";           // Team #
    public boolean dim_Tall = false;            // Dimension
    public int totalWheels = 0;                 // Total # of wheels
    public int numTraction = 0;                 // Num. of Traction wheels
    public int numOmnis = 0;                    // Num. of Omni wheels
    public int numMecanums = 0;                 // Num. of Mecanum wheels
    public boolean gear_Collecter = false;      // presence of gear collector
    public boolean fuel_Container = false;      // presence of Storage bin
    public int storSize = 0;                    // estimate of # of balls
    public boolean shooter = false;             // presence of Shooter
    public boolean vision = false;              // presence of Vision Camera
    public boolean pneumatics = false;          // presence of Pneumatics
    public boolean fuelManip = false;           // presence of a way to pick up fuel from floor
    public boolean climb = false;               // presence of a Climbing mechanism
    /* */
    public String comments = "";                // Comment(s)
    public String scout = "";                   // Student who collected the data
    // ===========================================================================
    pitData Pit_Data = new pitData(teamSelected,dim_Tall,totalWheels,numTraction,numOmnis,numMecanums,gear_Collecter,fuel_Container,storSize,shooter,vision,pneumatics,fuelManip,climb,comments,scout);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis_pit);
        Log.i(TAG, "@@@@@@@@  VisPit_Activity started  @@@@@@@@");
        Bundle bundle = this.getIntent().getExtras();
        tnum = bundle.getString("team");
        tname = bundle.getString("name");
        imgURL = bundle.getString("url");
        Log.w(TAG,"\n >>>>>>>> " + tnum + " " + tname + " " + imgURL);      // ** DEBUG **

        pfDatabase = FirebaseDatabase.getInstance();
        pfPitData_DBReference = pfDatabase.getReference("pit-data/" + Pearadox.FRC_Event); // Pit Scout Data
//        pfPitData_DBReference = pfDatabase.getReference("pit-data/");       // Pit Scout Data
        getTeam_Pit(tnum);
        txt_team = (TextView) findViewById(R.id.txt_team);
        txt_teamName = (TextView) findViewById(R.id.txt_teamName);
        ImageView imgView_Robot = (ImageView) findViewById(R.id.imgView_Robot);
        txt_team.setText(tnum);
        txt_teamName.setText(tname);
        if (imgURL.length() > 1) {
            Picasso.with(this).load(imgURL).into(imgView_Robot);
        } else {
            imgView_Robot.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        }
    }
    private void getTeam_Pit(String team) {
        Log.i(TAG, "$$$$$  getTeam_Pit  $$$$$  " + team);

        String child = "pit_team";
        String key = team.trim();
        Log.w(TAG, "   Q U E R Y  " + child + "  " + team);
        Query query = pfPitData_DBReference.orderByChild(child).equalTo(key);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.w(TAG, "%%%  ChildAdded");
                System.out.println(dataSnapshot.getValue());
                System.out.println("\n \n ");
                pitData Pit_Data = dataSnapshot.getValue(pitData.class);
                    System.out.println("Team: " + Pit_Data.getPit_team());
                    System.out.println("Comment: " + Pit_Data.getPit_comment());
                    System.out.println("\n \n ");

                txt_Dim = (TextView) findViewById(R.id.txt_Dim);
                txt_TotWheels = (TextView) findViewById(R.id.txt_TotWheels);
                txt_NumTrac = (TextView) findViewById(R.id.txt_NumTrac);
                txt_NumOmni = (TextView) findViewById(R.id.txt_NumOmni);
                txt_NumMecanum = (TextView) findViewById(R.id.txt_NumMecanum);
                chkBox_Gear = (CheckBox) findViewById(R.id.chkBox_Gear);
                chkBox_Shooter = (CheckBox) findViewById(R.id.chkBox_Shooter);
                chkBox_Vision = (CheckBox) findViewById(R.id.chkBox_Vision);
                chkBox_Pneumatics = (CheckBox) findViewById(R.id.chkBox_Pneumatics);
                chkBox_Fuel = (CheckBox) findViewById(R.id.chkBox_Fuel);
                txt_FuelCap = (TextView) findViewById(R.id.txt_FuelCap);
                chkBox_FuelManip = (CheckBox) findViewById(R.id.chkBox_FuelManip);
                chkBox_Climb = (CheckBox) findViewById(R.id.chkBox_Climb);
                txt_Scout = (TextView) findViewById(R.id.txt_Scout);
                txt_Comments = (TextView) findViewById(R.id.txt_Comments);

                if (Pit_Data.ispit_tall()) {
                    txt_Dim.setText("Tall    [30 in. X 32 in. X 36 in. tall]");
                } else {
                    txt_Dim.setText("Short    [36 in. X 40 in. X 24 in. tall]");
                }
                txt_TotWheels.setText(String.valueOf(Pit_Data.getPit_totWheels()));
                txt_NumTrac.setText(String.valueOf(Pit_Data.getPit_numTrac()));
                txt_NumOmni.setText(String.valueOf(Pit_Data.getPit_numOmni()));
                txt_NumMecanum.setText(String.valueOf(Pit_Data.getPit_numMecanum()));

                chkBox_Fuel.setChecked(Pit_Data.isPit_fuel_Container());
                Log.w(TAG, ">>>>> Fuel Container = '" + Pit_Data.isPit_fuel_Container() + "'");
                if (Pit_Data.isPit_fuel_Container()) {
                    txt_FuelCap.setText(String.valueOf(Pit_Data.getPit_storSize()));
                } else {
                    txt_FuelCap.setText(" ");
                }
                chkBox_Gear.setChecked(Pit_Data.isPit_gear_Collect());
                chkBox_Shooter.setChecked(Pit_Data.isPit_shooter());
                chkBox_Vision.setChecked(Pit_Data.isPit_vision());
                chkBox_Pneumatics.setChecked(Pit_Data.isPit_pneumatics());
                chkBox_FuelManip.setChecked(Pit_Data.isPit_fuelManip());
                chkBox_Climb.setChecked(Pit_Data.isPit_climb());

                txt_Scout.setText(Pit_Data.getPit_scout());
                txt_Comments.setText(Pit_Data.getPit_comment());
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
                Log.e(TAG, "%%%  DatabaseError");
            }
        });
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
        Log.v(TAG, "OnDestroy ");
    }

}

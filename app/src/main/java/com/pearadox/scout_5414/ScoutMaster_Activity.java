package com.pearadox.scout_5414;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

// Debug & Messaging
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.app.PendingIntent.getActivity;
import static com.pearadox.scout_5414.Pearadox.numTeams;

public class ScoutMaster_Activity extends AppCompatActivity {

    static String TAG = "ScoutMaster_Activity";      // This CLASS name
    ArrayAdapter<String> adapter_typ;
    public String typSelected = " ";
    Spinner spinner_MatchType;
    Spinner spinner_MatchNum;
    ArrayAdapter<String> adapter_Num;
    public String NumSelected = " ";
    public String matchID = "T00";      // Type + #
    ToggleButton toggleStartStop;
    TextView txt_teamR1, txt_teamR2, txt_teamR3, txt_teamB1, txt_teamB2, txt_teamB3;
    TextView txt_teamR1_Name, txt_teamR2_Name, txt_teamR3_Name, txt_teamB1_Name, txt_teamB2_Name, txt_teamB3_Name;
    TextView txt_scoutR1, txt_scoutR2, txt_scoutR3, txt_scoutB1, txt_scoutB2, txt_scoutB3;
    ImageView imgStat_R1, imgStat_R2, imgStat_R3, imgStat_B1, imgStat_B2, imgStat_B3;
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfStudent_DBReference;
    private DatabaseReference pfDevice_DBReference;
    private DatabaseReference pfTeam_DBReference;
    private DatabaseReference pfMatch_DBReference;
    private DatabaseReference pfCur_Match_DBReference;
    public static String[] signedStudents = new String[]
            {" ", " ", " ", " ", " ", " "};
    String team_num, team_name, team_loc;
    p_Firebase.teamsObj team_inst = new p_Firebase.teamsObj(team_num, team_name, team_loc);
    ArrayList<p_Firebase.teamsObj> teams = new ArrayList<p_Firebase.teamsObj>();

    //  Bluetooth
//    BluetoothAdapter myBluetoothAdapter;
//    BroadcastReceiver myBTReceiver;
//    IntentFilter filter;
//    ArrayList<String> devList = new ArrayList<String>();
//    int numBT_connects = 0;     //# of Bluetooth connections
//    public static String EXTRA_DEVICE_ADDRESS = "device_address";
//    String BTdevName = null;
//    String MACaddr = null;
//    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
//    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
//    private static final int REQUEST_ENABLE_BT = 3;
//    private BluetoothChatService mChatService = null;   // Member object for the chat services
//    private String mConnectedDeviceName = null;         // Name of the connected device
//    private StringBuffer mOutStringBuffer;              // String buffer for outgoing messages


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
        pfDatabase = FirebaseDatabase.getInstance();
        pfTeam_DBReference = pfDatabase.getReference("teams");              // Tteam data from Firebase D/B
        pfStudent_DBReference = pfDatabase.getReference("students");        // List of Students
        pfDevice_DBReference = pfDatabase.getReference("devices");          // List of Students
        pfMatch_DBReference = pfDatabase.getReference("matches");           // List of Students
        pfCur_Match_DBReference = pfDatabase.getReference("current-match"); // _THE_ current Match
        clearTeamData();
        clearDevData();

//        startBluetooth();       // Start BT as Master & listen for connections
        toggleStartStop = (ToggleButton) findViewById(R.id.toggleStartStop);
        toggleStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner_MatchType = (Spinner) findViewById(R.id.spinner_MatchType);
                Spinner spinner_MatchNum = (Spinner) findViewById(R.id.spinner_MatchNum);
                String key = "0";
                if (toggleStartStop.isChecked()) {      // See what state we are in
                    pfCur_Match_DBReference.child(key).child("cur_match").setValue(matchID);
                    getTeams();

                } else {        // Stop Session - Clear data
                    matchID = "";
                    spinner_MatchType.setSelection(0);       //Reset to NO selection
                    spinner_MatchNum.setSelection(0);        //*
                    pfCur_Match_DBReference.child(key).child("cur_match").setValue("");  // set to null
                    pfCur_Match_DBReference.child(key).child("r1").setValue("");
                    pfCur_Match_DBReference.child(key).child("r2").setValue("");
                    pfCur_Match_DBReference.child(key).child("r3").setValue("");
                    pfCur_Match_DBReference.child(key).child("b1").setValue("");
                    pfCur_Match_DBReference.child(key).child("b2").setValue("");
                    pfCur_Match_DBReference.child(key).child("b3").setValue("");
                    clearTeamData();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.scoutmaster_menu, menu);
        return true;
    }


//    private void startBluetooth() {
//        Log.d(TAG, "<<<<<  Bluetooth  >>>>>>");
//        BluetoothAdapter myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        devList.clear();
//        if (myBluetoothAdapter != null) {
//            Log.i(TAG, "Bluetooth is available");
//            if (!myBluetoothAdapter.isEnabled()) {
//                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//            }
//            Log.i(TAG, "Bluetooth is enabled");
//            Set<BluetoothDevice> pairedDevices = myBluetoothAdapter.getBondedDevices();
//            // If there are paired devices
//            if (pairedDevices.size() > 0) {
//                // Loop through paired devices
//                for (BluetoothDevice device : pairedDevices) {
//                    // Add the name and address to an array adapter to show in a ListView
//                    Log.d(TAG, "*** Already Paired " + device.getName() + " - " + device.getAddress());
//                    devList.add(device.getName());
//                }
//                Log.d(TAG, "# Bluetooth devices paired = " + devList.size());
//            } else {
//                Log.i(TAG, "No paired devices");
//            }
//
//            final BroadcastReceiver myBTReceiver = new BroadcastReceiver() {  // Create a BroadcastReceiver for ACTION_FOUND
//                public void onReceive(Context context, Intent intent) {
//                    String action = intent.getAction();
//                    // When discovery finds a device
//                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                        // Get the BluetoothDevice object from the Intent
//                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                        // Add the name and address to an array adapter to show in a ListView
////                        devList.add(device.getName() + "\n" + device.getAddress());
////                        Log.d(TAG, "*** Added device " + device.getName() + " - " + device.getAddress());
//                    }
//                }
//            };
//
//            for (int i = 0; i < devList.size(); i++) {
//                String paired_dev = devList.get(i);
//                Log.i(TAG, "Paired device " + i + " = '" + paired_dev + "'");
//                switch (paired_dev) {
//                    case ("5414_Red-1"):
//                        imgStat_R1.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth_on));
//                        break;
//                    case ("5414_Red-2"):
//                        imgStat_R2.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth_on));
//                        break;
//                    case ("5414_Red-3"):
//                        imgStat_R3.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth_on));
//                        break;
//                    case ("5414_Blue-1"):
//                        imgStat_B1.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth_on));
//                        break;
//                    case ("5414_Blue-2"):
//                        imgStat_B2.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth_on));
//                        break;
//                    case ("5414_Blue-3"):
//                        imgStat_B3.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth_on));
//                        break;
//                    default:                // ????
//                        Log.e(TAG, "*** Error - Invalid Bluetooth Device Name (e.g., '5414_<Color>-#'  *** " + paired_dev);
//                }
//            }
//
//            // Register the BroadcastReceiver
//            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//            registerReceiver(myBTReceiver, filter);   // Don't forget to unregister during onDestroy
//
//            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//            startActivity(discoverableIntent);
//
//        } else {        // Device does not support Bluetooth
//            Log.d(TAG, "** Bluetooth is not availible ** ");
//            Toast.makeText(getBaseContext(), "** Bluetooth is not availible ** ",
//                    Toast.LENGTH_LONG).show();
//
//        }
//    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.i(TAG, "$$$$$$$$  onActivityResult  $$$$$$$$ " + requestCode);
//        switch (requestCode) {
//            case REQUEST_CONNECT_DEVICE_SECURE:
//                Log.d(TAG, "<CONNECT>");
//                // When DeviceListActivity returns with a device to connect
//                if (resultCode == Activity.RESULT_OK) {
//                    Log.d(TAG, "####### RESULT_OK " + Activity.RESULT_OK);
//                    connectDevice(data, true);
//                } else {        // No BT devices found
//                    Log.d(TAG, "******* No BT deviices ******" + Activity.RESULT_OK);
//                }
//                break;
//            case REQUEST_ENABLE_BT:
//                Log.d(TAG, "[ENABLE]");
//                // When the request to enable Bluetooth returns
//                if (resultCode == Activity.RESULT_OK) {
//                    Log.d(TAG, "setUpChat");
////                    setupChat();  // Bluetooth is now enabled, so set up a chat session
//                } else {
//                    // User did not enable Bluetooth or an error occurred
//                    Log.d(TAG, "BT not enabled");
//                    Toast.makeText(getBaseContext(), "** Bluetooth is not availible ** ",
//                            Toast.LENGTH_LONG).show();
////                    finish();         //EXIT
//                }
//            default:                // ????
//                Log.e(TAG, "*** Error - bad Request Code  *** " + requestCode);
//        }
//    }

//    private void connectDevice(Intent data, boolean secure) {
//        Log.i(TAG, "connectDevice");
//        String address = data.getExtras()
//                .getString(ScoutMaster_Activity.EXTRA_DEVICE_ADDRESS);  // Get the information
//        Log.d(TAG, "Returned address = " + address);
//        BTdevName = address.substring(0, address.length() - 18);    // Get The Device name
//        MACaddr = address.substring(address.length() - 17);         // Get the device MAC address
//        Log.d(TAG, "DEV=" + BTdevName + " MAC addr = " + MACaddr);
//        BluetoothDevice device = myBluetoothAdapter.getRemoteDevice(address);
//        mChatService.connect(device, secure);   // Attempt to connect to the device
//    }

    private void clearTeamData() {
        Log.i(TAG, "$$$$$  Clear Team Data");
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
        txt_teamR1.setText(" ");
        txt_teamR2.setText(" ");
        txt_teamR3.setText(" ");
        txt_teamB1.setText(" ");
        txt_teamB2.setText(" ");
        txt_teamB3.setText(" ");
        txt_teamR1_Name.setText(" ");
        txt_teamR2_Name.setText(" ");
        txt_teamR3_Name.setText(" ");
        txt_teamB1_Name.setText(" ");
        txt_teamB2_Name.setText(" ");
        txt_teamB3_Name.setText(" ");
    }

    private void clearDevData() {
        Log.i(TAG, "$$$$$  Clear Device Data");
        txt_scoutR1 = (TextView) findViewById(R.id.txt_scoutR1);
        txt_scoutR2 = (TextView) findViewById(R.id.txt_scoutR2);
        txt_scoutR3 = (TextView) findViewById(R.id.txt_scoutR3);
        txt_scoutB1 = (TextView) findViewById(R.id.txt_scoutB1);
        txt_scoutB2 = (TextView) findViewById(R.id.txt_scoutB2);
        txt_scoutB3 = (TextView) findViewById(R.id.txt_scoutB3);
        txt_scoutR1.setText(" ");
        txt_scoutR2.setText(" ");
        txt_scoutR3.setText(" ");
        txt_scoutB1.setText(" ");
        txt_scoutB2.setText(" ");
        txt_scoutB3.setText(" ");
    }

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
        int z = matchID.length();
        if (z == 3) {
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
//                    System.out.println("Match: " + mobj.getMatch());
//                    System.out.println("Type: " + mobj.getMtype());
//                    System.out.println("Date: " + mobj.getDate());
//                    System.out.println("R1: " + mobj.getR1());
//                    System.out.println("B3: " + mobj.getB3());
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
                    // Put the teams for this match on screen
                    String key = "0";   // Since only 1, key is zero
                    team_inst = teams.get(0);
                    txt_teamR1.setText(team_inst.getTeam_num());
                    txt_teamR1_Name.setText(team_inst.getTeam_name());
                    pfCur_Match_DBReference.child(key).child("r1").setValue(team_inst.getTeam_num());
                    team_inst = teams.get(1);
                    txt_teamR2.setText(team_inst.getTeam_num());
                    txt_teamR2_Name.setText(team_inst.getTeam_name());
                    pfCur_Match_DBReference.child(key).child("r2").setValue(team_inst.getTeam_num());
                    team_inst = teams.get(2);
                    txt_teamR3.setText(team_inst.getTeam_num());
                    txt_teamR3_Name.setText(team_inst.getTeam_name());
                    pfCur_Match_DBReference.child(key).child("r3").setValue(team_inst.getTeam_num());
                    team_inst = teams.get(3);
                    txt_teamB1.setText(team_inst.getTeam_num());
                    txt_teamB1_Name.setText(team_inst.getTeam_name());
                    pfCur_Match_DBReference.child(key).child("b1").setValue(team_inst.getTeam_num());
                    team_inst = teams.get(4);
                    txt_teamB2.setText(team_inst.getTeam_num());
                    txt_teamB2_Name.setText(team_inst.getTeam_name());
                    pfCur_Match_DBReference.child(key).child("b2").setValue(team_inst.getTeam_num());
                    team_inst = teams.get(5);
                    txt_teamB3.setText(team_inst.getTeam_num());
                    txt_teamB3_Name.setText(team_inst.getTeam_name());
                    pfCur_Match_DBReference.child(key).child("b3").setValue(team_inst.getTeam_num());
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
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }

//    private void ensureDiscoverable() {
//        Log.d(TAG, "******* Discoverable");
//        BluetoothAdapter myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (myBluetoothAdapter.getScanMode() !=
//                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
//            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//            startActivity(discoverableIntent);
//        } else {
//            Toast.makeText(getBaseContext(), "Your device is already 'Discoverable' ", Toast.LENGTH_LONG).show();
//        }
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.i(TAG, ">>>>> onOptionsItemSelected  ");
//        switch (item.getItemId()) {
//            case R.id.BTconnect_scan: {
//                // Launch the DeviceListActivity to see devices and do scan
//                Intent BTdev_Intent = new Intent(ScoutMaster_Activity.this, BTdevices_Activity.class);
//                startActivityForResult(BTdev_Intent, REQUEST_CONNECT_DEVICE_SECURE);   // Find BT devices
//                return true;
//            }
//            case R.id.BTdiscoverable: {
//                // Ensure this device is discoverable by others
//                ensureDiscoverable();
//                return true;
//            }
//            default:                // ????
//                Log.e(TAG, "*** Error - bad MENU indicator  ***");
//        }
//        return false;
//    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
//    private final Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            String activity = "BTC_Handler";
//            switch (msg.what) {
//                case Constants.MESSAGE_STATE_CHANGE:
//                    switch (msg.arg1) {
//                        case BluetoothChatService.STATE_CONNECTED:
////                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
////                            mConversationArrayAdapter.clear();
//                            break;
//                        case BluetoothChatService.STATE_CONNECTING:
////                            setStatus(R.string.title_connecting);
//                            break;
//                        case BluetoothChatService.STATE_LISTEN:
//                        case BluetoothChatService.STATE_NONE:
////                            setStatus(R.string.title_not_connected);
//                            break;
//                    }
//                    break;
//                case Constants.MESSAGE_WRITE:
//                    byte[] writeBuf = (byte[]) msg.obj;
//                    // construct a string from the buffer
//                    String writeMessage = new String(writeBuf);
////                    mConversationArrayAdapter.add("Me:  " + writeMessage);
//                    break;
//                case Constants.MESSAGE_READ:
//                    byte[] readBuf = (byte[]) msg.obj;
//                    // construct a string from the valid bytes in the buffer
//                    String readMessage = new String(readBuf, 0, msg.arg1);
////                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
//                    break;
//                case Constants.MESSAGE_DEVICE_NAME:
//                    // save the connected device's name
//                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
//                    Toast.makeText(getBaseContext(), "Connected to "
//                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
//                    break;
//                case Constants.MESSAGE_TOAST:
//                    Toast.makeText(getBaseContext(), msg.getData().getString(Constants.TOAST),
//                            Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    };


//    private void setupChat() {
//        Log.d(TAG, "%%%%  setupChat  %%%%");
//        // Initialize the BluetoothChatService to perform bluetooth connections
//        mChatService = new BluetoothChatService(getBaseContext(), mHandler);
//
//        // Initialize the buffer for outgoing messages
//        mOutStringBuffer = new StringBuffer("");
//
//    }

    public void FindDevItem() {
        Log.d(TAG, "%%%%  FindDevItem  %%%%");
        pfDevice_DBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Device onDataChange  %%%%");
                txt_scoutR1 = (TextView) findViewById(R.id.txt_scoutR1);
                txt_scoutR2 = (TextView) findViewById(R.id.txt_scoutR2);
                txt_scoutR3 = (TextView) findViewById(R.id.txt_scoutR3);
                txt_scoutB1 = (TextView) findViewById(R.id.txt_scoutB1);
                txt_scoutB2 = (TextView) findViewById(R.id.txt_scoutB2);
                txt_scoutB3 = (TextView) findViewById(R.id.txt_scoutB3);
                ImageView imgStat_R1 = (ImageView) findViewById(R.id.imgStat_R1);
                ImageView imgStat_R2 = (ImageView) findViewById(R.id.imgStat_R2);
                ImageView imgStat_R3 = (ImageView) findViewById(R.id.imgStat_R3);
                ImageView imgStat_B1 = (ImageView) findViewById(R.id.imgStat_B1);
                ImageView imgStat_B2 = (ImageView) findViewById(R.id.imgStat_B2);
                ImageView imgStat_B3 = (ImageView) findViewById(R.id.imgStat_B3);
                int numDevs = 0;
                String device = "";
                String  studname = "";
                String  status = "";
                p_Firebase.devicesObj dev_Obj = new p_Firebase.devicesObj();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();   /*get the data children*/
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    dev_Obj = iterator.next().getValue(p_Firebase.devicesObj.class);
                    device = dev_Obj.getDev_name();
                    studname = dev_Obj.getStud_id();
                    status = dev_Obj.getPhase();
                    Log.d(TAG, "%%%%  " + studname + " is logged onto " + device + " at Phase '" + status + "' ");
                    numDevs++;
//                    if (studname.length() > 2) {
                        switch (device) {
                            case "Scout Master":         // Scout Master
                                // only interested in Scouts
                                break;
                            case ("Red-1"):             //#Red or Blue Scout
                                signedStudents[0] = studname;
                                txt_scoutR1.setText(signedStudents[0]);
                                switch(status) {
                                    case ("Auto"):
                                        imgStat_R1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_red));
                                        break;
                                    case ("Tele"):
                                        imgStat_R1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_yellow));
                                        break;
                                    case ("Final"):
                                        imgStat_R1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_green));
                                        break;
                                    case ("Saved"):
                                        imgStat_R1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_all));
                                        break;
                                    default:                //
                                        imgStat_R1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_off));
                                        Log.d(TAG, "***** Unknown Device Status ***** -> " + status);
                                }
                                break;
                            case ("Red-2"):             //#
                                signedStudents[1] = studname;
                                txt_scoutR2.setText(signedStudents[1]);
                                switch(status) {
                                    case ("Auto"):
                                        imgStat_R2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_red));
                                        break;
                                    case ("Tele"):
                                        imgStat_R2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_yellow));
                                        break;
                                    case ("Final"):
                                        imgStat_R2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_green));
                                        break;
                                    case ("Saved"):
                                        imgStat_R2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_all));
                                        break;
                                    default:                //
                                        imgStat_R2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_off));
                                        Log.d(TAG, "***** Unknown Device Status ***** -> " + status);
                                }
                                break;
                            case ("Red-3"):             //#
                                signedStudents[2] = studname;
                                txt_scoutR3.setText(signedStudents[2]);
                                switch(status) {
                                    case ("Auto"):
                                        imgStat_R3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_red));
                                        break;
                                    case ("Tele"):
                                        imgStat_R3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_yellow));
                                        break;
                                    case ("Final"):
                                        imgStat_R3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_green));
                                        break;
                                    case ("Saved"):
                                        imgStat_R3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_all));
                                        break;
                                    default:                //
                                        imgStat_R3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_off));
                                        Log.d(TAG, "***** Unknown Device Status ***** -> " + status);
                                }
                                break;
                            case ("Blue-1"):            //#
                                signedStudents[3] = studname;
                                txt_scoutB1.setText(signedStudents[3]);
                                switch(status) {
                                    case ("Auto"):
                                        imgStat_B1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_red));
                                        break;
                                    case ("Tele"):
                                        imgStat_B1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_yellow));
                                        break;
                                    case ("Final"):
                                        imgStat_B1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_green));
                                        break;
                                    case ("Saved"):
                                        imgStat_B1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_all));
                                        break;
                                    default:                //
                                        imgStat_B1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_off));
                                        Log.d(TAG, "***** Unknown Device Status ***** -> " + status);
                                }
                                break;
                            case ("Blue-2"):            //#
                                signedStudents[4] = studname;
                                txt_scoutB2.setText(signedStudents[4]);
                                switch(status) {
                                    case ("Auto"):
                                        imgStat_B2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_red));
                                        break;
                                    case ("Tele"):
                                        imgStat_B2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_yellow));
                                        break;
                                    case ("Final"):
                                        imgStat_B2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_green));
                                        break;
                                    case ("Saved"):
                                        imgStat_B2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_all));
                                        break;
                                    default:                //
                                        imgStat_B2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_off));
                                        Log.d(TAG, "***** Unknown Device Status ***** -> " + status);
                                }
                                break;
                            case ("Blue-3"):            //#####
                                signedStudents[5] = studname;
                                txt_scoutB3.setText(signedStudents[5]);
                                switch(status) {
                                    case ("Auto"):
                                        imgStat_B3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_red));
                                        break;
                                    case ("Tele"):
                                        imgStat_B3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_yellow));
                                        break;
                                    case ("Final"):
                                        imgStat_B3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_green));
                                        break;
                                    case ("Saved"):
                                        imgStat_B3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_all));
                                        break;
                                    default:                //
                                        imgStat_B3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_off));
                                        Log.d(TAG, "***** Unknown Device Status ***** -> " + status);
                                }
                                break;
                            case "Visualizer":          // Visualizer
                                // only interested in Scouts
                                break;
                            default:                //
                                Log.d(TAG, "*** Error DEV = " + device);
                        } // End Switch - Device
//                    }
                } // End While
                Log.d(TAG, "*****  # of devices = " + numDevs);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*listener failed or was removed for security reasons*/
            }
        });
    }

//###################################################################
//###################################################################
//###################################################################
//###################################################################
    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
        FindDevItem();  // Get devices that are logged on

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

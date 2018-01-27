package com.pearadox.scout_5414;

//******************************************************
// GLOBAL Variables

import java.util.ArrayList;

public class Pearadox {

    public static boolean  is_Network; 								// Internet available?
    public static String FRC_Event;                                 // FIRST Event Code (e.g., txwa)
    public static String FRC_EventName;                             // FIRST Event Code (e.g., 'Hub City')
    public static String FRC_ChampDiv;                              // FIRST Championshio Division (e.g., 'Einstein')
    public static int maxTeams = 300; 								// Maximum # of Teams per event (increase for Worlds)  GLF 4/9
    public static int maxStudents = 80; 						    // Maximum # of Students
    public static ArrayList<p_Firebase.eventObj> eventList = new ArrayList<p_Firebase.eventObj>();      // Event objects
    public static String[] comp_List = new String[8];               // Events list (array of just Names)
    public static int num_Events = 0; 						        // Actual # of Events/Competitions
    public static ArrayList<p_Firebase.teamsObj> team_List = new ArrayList<p_Firebase.teamsObj>();
    public static int numTeams = 0; 						        // Actual # of Teams
    public static ArrayList<p_Firebase.students> stud_Lst = new ArrayList<p_Firebase.students>();
    public static String[] student_List = new String[maxStudents];  // Student list (array of just Names)
    public static int numStudents = 0; 						        // # of Students
    public static String FRC514_Device;                             // Device ID
    public static String Student_ID;                                // Student Name
    public static String our_Matches = "";                          // List of all matches for 5414
    public static String[] matches = new String[]
            {"","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20"};

//*********************************************************
// Inter-Activity for saving data
    public static boolean  MatchData_Saved; 			// Data from Match saved to disk and Firebase



    //*********************************************************
// Firebase Obects used by Scouts for saving data
    private static String match;                   // Match ID (e.g., 'T00' where T = X(Practice), Q(ualifying) and '00' - match #
    private static String team_num;                // Team Number (e.gg., '5414'
    private static boolean auto_mode;              // Do they have Autonamous mode?
    private static boolean auto_rope;              // Do they have their own rope?
    private static boolean auto_carry_fuel;        // Do they carry fuel
    private static int auto_fuel_amount;           // Amount of fuel they carry
    private static boolean auto_gear;              // Do they carry a gear
    private static int auto_gears_placed;          // # gears placed during Auto
    private static int auto_gears_attempt;         // # gears attempted during Auto
    private static boolean auto_baseline;          // Did they cross Baseline
    private static boolean auto_hg;                // Did they shoot at High Goal
    private static int auto_hg_percent;            // What percentage HG made?
    private static boolean auto_lg;                // Did they shoor at Low Goal
    private static int auto_lg_percent;            // What percentage LG made?
    private static String auto_start;              // Where they started Auto
    private static String auto_stop;               // Where they stopped at end of Auto
    private static String auto_gear_pos;           // Where they placed their gear(s)
    private static boolean auto_act_hopper;        // Did they activate hopper
    private static String auto_fuel_collected;     // Fuel Collected Range
    private static String auto_comment;            // Auto comment
    // ============== TELE =================
    private static int     tele_gears_placed;      // # gears placed during Tele
    private static int     tele_gears_attempt;     // # gears attempted during ATele
    private static boolean tele_gear_pickup;       // Did they pick up Gear(s)
    private static boolean tele_hg;                // Did they shoot at High Goal
    private static int     tele_hg_percent;        // What percentage HG made?
    private static boolean tele_lg;                // Did they shoot at Low Goal
    private static int     tele_lg_percent;        // What percentage LG made?
    private static int     tele_cycles;            // # cycles of shooting Upper Goal
    private static boolean tele_touch_act;         // Did they activate Touchpad
    private static boolean tele_touch_pts;         // Did they get Touchpad points
    private static boolean tele_climb_attempt;     // Did they ATTEMPT climb?
    private static boolean tele_climb_success;     // Was climb successful?

    private static String tele_comment;            // Tele comment
    // ============= Final  ================
    private static boolean final_lostParts;         // Did they lose parts
    private static boolean final_lostComms;         // Did they lose communication
    private static boolean final_defense_good;      // Was their overall Defense Good (bad=false)
    private static boolean final_def_Lane;          // Did they use Lane Defense
    private static boolean final_def_Block;         // Did they use Blocking Defense
    private static boolean final_def_BlockSwitch;   // Did they block the Switch
    private static int     final_num_Penalties;     // How many penalties received?

    private static String  final_comment;           // Final comment
    private static String  final_studID;            // Student doing the scouting
    private static String  final_dateTime;          // Date & Time data was saved

// Java Object _SHARED_ by Auto, Tele & Final
    public static matchData Match_Data = new matchData();

// -----  Array of Match Data Objects for Match Data Visualizer
public static ArrayList<matchData> Matches_Data = new ArrayList<matchData>();


}

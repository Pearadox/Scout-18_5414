package com.pearadox.scout_5414;

//******************************************************
// GLOBAL Variables

import java.util.ArrayList;

public class Pearadox {

    public static boolean  is_Network; 								// Internet available?
    public static String FRC_Event;                                 // FIRST Event Code (e.g., txwa)
    public static String FRC_EventName;                             // FIRST Event Code (e.g., 'Hub City')
    public static int maxTeams = 60; 								// Maximum # of Teams per event
    public static int maxStudents = 80; 						    // Maximum # of Students
    public static ArrayList<p_Firebase.teamsObj> team_List = new ArrayList<p_Firebase.teamsObj>();
    public static int numTeams = 0; 						        // Actual # of Teams
    public static ArrayList<p_Firebase.students> stud_Lst = new ArrayList<p_Firebase.students>();
    public static String[] student_List = new String[maxStudents];  // Student list (array of just Names)
    public static int numStudents = 0; 						        // Actual # of Students
    public static String FRC514_Device;                             // Device ID
    public static String Student_ID;                                // Student Name
    public static String[] matches = new String[]
            {"","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20"};

//*********************************************************
// Inter-Activity for saving data
    public static boolean  MatchData_Saved; 			// Data from Match saved to disk and Firebase



    //*********************************************************
// Firebase Obects used by Scouts for saving data
    private String match;                   // Match ID (e.g., 'T00' where T = X(Practice), Q(ualifying) and '00' - match #
    private String team_num;                // Team Number (e.gg., '5414'
    private boolean auto_mode;              // Do they have Autonamous mode?
    private boolean auto_rope;              // Do they have their own rope?
    private boolean auto_carry_fuel;        // Do they carry fuel
    private int auto_fuel_amount;           // Amount of fuel they carry
    private boolean auto_gear;              // Do they carry a gear
    private int auto_gears_placed;          // # gears placed during Auto
    private int auto_gears_attempt;         // # gears attempted during Auto
    private boolean auto_baseline;          // Did they cross Baseline
    private boolean auto_hg;                // Did they shoot at High Goal
    private int auto_hg_percent;            // What percentage HG made?
    private boolean auto_lg;                // Did they shoor at Low Goal
    private int auto_lg_percent;            // What percentage LG made?
    private String auto_start;              // Where they started Auto
    private String auto_stop;               // Where they stopped at end of Auto
    private String auto_gear_pos;           // Where they placed their gear(s)
    private boolean auto_act_hopper;        // Did they activate hopper
    private String auto_fuel_collected;     // Fuel Collected Range
    private String auto_comment;            // Auto comment
    // ============== TELE =================
    private  static int tele_gears_placed;          // # gears placed during Tele
    private  static int tele_gears_attempt;         // # gears attempted during ATele

    private  static String tele_comment;            // Tele comment
    // ============= Final  ================

    private  static String final_comment;                    // Final comment
    private  static String final_studID;                    // Student doing the scouting


    public matchData Match_Data = new matchData(match, team_num, auto_mode, auto_rope, auto_carry_fuel, auto_fuel_amount, auto_gear, auto_gears_placed, auto_gears_attempt, auto_baseline, auto_hg, auto_hg_percent, auto_lg, auto_lg_percent, auto_start, auto_stop, auto_gear_pos, auto_act_hopper, auto_fuel_collected, auto_comment, tele_gears_placed, tele_gears_attempt, tele_comment, final_comment, final_studID);

}

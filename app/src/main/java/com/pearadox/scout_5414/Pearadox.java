package com.pearadox.scout_5414;

//******************************************************
// GLOBAL Variables

import java.util.ArrayList;

public class Pearadox {

    public static boolean  is_Network; 								// Internet available?
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
// Firebase Obects used by Scouts for saving data
    private  static String match;                   // Matcj ID (e.g., 'T00' where T = X(Practice), Q(ualifying) or P(Playoff) and '00' - match #
    private  static String team_num;                // Team Number (e.gg., '5414'
    private  static boolean auto_mode;              // Do they have Autonamous mode?
    private  static boolean auto_carry_fuel;        // Do they carry fuel
    private  static boolean auto_gear;              // Do they carry a gear
    private  static int auto_gears_placed;          // # gears placed during Auto
    private  static int auto_gears_attempt;         // # gears attempted during Auto
    private  static boolean auto_baseline;          // Did they cross Baseline
    private  static boolean auto_hg;                // Did they shoor at High Goal
    private  static int auto_hg_percent;            // What percentage HG made?
    private  static boolean auto_lg;                // Did they shoor at Low Goal
    private  static int auto_lg_percent;            // What percentage LG made?
    private  static  String auto_start;             // Where they started Auto
    private  static String auto_stop;               // Where they stopped at end of Auto
    private  static String auto_gear_pos;           // Where they placed their gear(s)
    private  static boolean auto_pu_fuel;           // Did they pick up a gear
    private  static boolean auto_pu_gear;           // Did they pick up a gear
    private  static String auto_comment;            // Auto comment
    // ============== TELE =================
    private  static int tele_gears_placed;          // # gears placed during Tele
    private  static int tele_gears_attempt;         // # gears attempted during ATele

    private  static String tele_comment;            // Tele comment
    // ============= Final  ================

    private  static String final_comment;                    // Final comment
    private  static String final_studID;                    // Student doing the scouting

    public static matchData Match_Data = new matchData(match, team_num, auto_mode, auto_carry_fuel, auto_gear, auto_gears_placed, auto_gears_attempt, auto_baseline, auto_hg, auto_hg_percent, auto_lg, auto_lg_percent, auto_start, auto_stop, auto_gear_pos, auto_pu_fuel, auto_pu_gear, auto_comment, tele_gears_placed, tele_gears_attempt, tele_comment, final_comment, final_studID);


}

package com.pearadox.scout_5414;

//******************************************************
// GLOBAL Variables

import java.util.ArrayList;

public class Pearadox {

    public static boolean  is_Network; 								// Internet available?
    public static int maxTeams = 40; 								// Maximum # of Teams per event
    public static int maxStudents = 80; 						    // Maximum # of Students
    public static ArrayList<p_Firebase.teamsObj> team_List = new ArrayList<p_Firebase.teamsObj>();
    public static int numTeams = 0; 						        // Actual # of Teams
    public static ArrayList<p_Firebase.students> stud_Lst = new ArrayList<p_Firebase.students>();
    public static String[] student_List = new String[maxStudents];  // Student list (array of just Names)
    public static int numStudents = 0; 						        // Actual # of Students
    public static String FRC514_Device;                             // Devide ID
    public static String Student_ID;                                // Student Name
    public static String[] matches = new String[]
            {"","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20"};

    //*********************************************************

}

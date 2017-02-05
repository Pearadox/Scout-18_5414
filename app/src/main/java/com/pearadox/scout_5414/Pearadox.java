package com.pearadox.scout_5414;

import com.pearadox.*;
//******************************************************
// GLOBAL Variables

public class Pearadox {

    public static boolean  is_Network; 								// Internet available?
    public static int maxTeams = 40; 								// Maximum # of Teams per event
    public static int maxStudents = 80; 						    // Maximum # of Students
    public static Pearadox_Firebase.teamsObj[] team_List = new Pearadox_Firebase.teamsObj[maxTeams]; 	    // Team list
//    public static Pearadox_Firebase.students[] student_List = new Pearadox_Firebase.students[maxStudents]; 	// Student list
    public static String[] student_List = new String[maxStudents];
    public static int numTeams = 0; 						        // Actual # of Teams
    public static int numStudents = 0; 						        // Actual # of Students
    public static String FRC514_Device;                             // Devide ID
    public static String Student_ID;                                // Student Name
    public static String[] matches = new String[]
            {"","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20"};

}

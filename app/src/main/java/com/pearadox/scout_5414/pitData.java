package com.pearadox.scout_5414;

import java.io.Serializable;

public class pitData implements Serializable {
    private static final long serialVersionUID = -54145414541400L;
    // ============= Pit Scout Data ================
    public String pit_team = " ";                   // Team #
    public int pit_tall = 0;                        // Height (inches)
    public int pit_totWheels = 0;                   // Total # of wheels
    public int pit_numTrac = 0;                     // Num. of Traction wheels
    public int pit_numOmni = 0;                     // Num. of Omni wheels
    public int pit_numMecanum = 0;                  // Num. of Mecanum wheels
    public boolean pit_vision = false;              // presence of Vision Camera
    public boolean pit_pneumatics = false;          // presence of Pneumatics
    public boolean pit_cubeManip = false;           // presence of a way to pick up cube from floor
    public boolean pit_climb = false;               // presence of a Climbing mechanism
    public boolean pit_canLift = false;             // Ability to lift other robots
    public int pit_numLifted = 0;                   // Num. of robots can lift (1-2)
    public boolean pit_liftRamp = false;            // lift type Ramp
    public boolean pit_liftHook = false;            // lift type Hook
                                                    //==== cube Mechanism
    public boolean pit_cubeArm = false;             // presence of a Cube arm
    public boolean pit_armIntake = false;           // ++ presence of a Cube intake device      \  Only if
    public boolean pit_armSqueeze = false;          // ++ presence of a Cube Squeeze mechanism  /   Arm
    public boolean pit_cubeBox = false;             // presence of a Cube box
    public boolean pit_cubeBelt = false;            // presence of a Cube Conveyer Belt
    public boolean pit_cubeOhtr = false;            // Other ?
                                                    //==== cube Delivery
    public boolean pit_delLaunch = false;           // Launch
    public boolean pit_delPlace = false;            // Placement

    /* */
    public String pit_comment;                      // Comment(s)
    public String pit_scout = " ";                  // Student who collected the data
    public String pit_photoURL;                     // URL of the robot photo in Firebase


    // ===========================================================================
        public pitData(String pit_team, int pit_tall, int pit_totWheels, int pit_numTrac, int pit_numOmni, int pit_numMecanum, boolean pit_vision, boolean pit_pneumatics, boolean pit_cubeManip, boolean pit_climb, boolean pit_canLift, int pit_numLifted, boolean pit_liftRamp, boolean pit_liftHook, boolean pit_cubeArm, boolean pit_armIntake, boolean pit_armSqueeze, boolean pit_cubeBox, boolean pit_cubeBelt, boolean pit_cubeOhtr, boolean pit_delLaunch, boolean pit_delPlace, String pit_comment, String pit_scout, String pit_photoURL) {
            this.pit_team = pit_team;
            this.pit_tall = pit_tall;
            this.pit_totWheels = pit_totWheels;
            this.pit_numTrac = pit_numTrac;
            this.pit_numOmni = pit_numOmni;
            this.pit_numMecanum = pit_numMecanum;
            this.pit_vision = pit_vision;
            this.pit_pneumatics = pit_pneumatics;
            this.pit_cubeManip = pit_cubeManip;
            this.pit_climb = pit_climb;
            this.pit_canLift = pit_canLift;
            this.pit_numLifted = pit_numLifted;
            this.pit_liftRamp = pit_liftRamp;
            this.pit_liftHook = pit_liftHook;
            this.pit_cubeArm = pit_cubeArm;
            this.pit_armIntake = pit_armIntake;
            this.pit_armSqueeze = pit_armSqueeze;
            this.pit_cubeBox = pit_cubeBox;
            this.pit_cubeBelt = pit_cubeBelt;
            this.pit_cubeOhtr = pit_cubeOhtr;
            this.pit_delLaunch = pit_delLaunch;
            this.pit_delPlace = pit_delPlace;
            this.pit_comment = pit_comment;
            this.pit_scout = pit_scout;
            this.pit_photoURL = pit_photoURL;
        }


// ===========================================================================
// Default constructor required for calls to
// DataSnapshot.getValue(teams.class)
public pitData() {
    }

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// Getters & Setters

    public String getPit_team() {
        return pit_team;
    }

    public void setPit_team(String pit_team) {
        this.pit_team = pit_team;
    }

    public int getPit_tall() {
        return pit_tall;
    }

    public void setPit_tall(int pit_tall) {
        this.pit_tall = pit_tall;
    }

    public int getPit_totWheels() {
        return pit_totWheels;
    }

    public void setPit_totWheels(int pit_totWheels) {
        this.pit_totWheels = pit_totWheels;
    }

    public int getPit_numTrac() {
        return pit_numTrac;
    }

    public void setPit_numTrac(int pit_numTrac) {
        this.pit_numTrac = pit_numTrac;
    }

    public int getPit_numOmni() {
        return pit_numOmni;
    }

    public void setPit_numOmni(int pit_numOmni) {
        this.pit_numOmni = pit_numOmni;
    }

    public int getPit_numMecanum() {
        return pit_numMecanum;
    }

    public void setPit_numMecanum(int pit_numMecanum) {
        this.pit_numMecanum = pit_numMecanum;
    }

    public boolean isPit_vision() {
        return pit_vision;
    }

    public void setPit_vision(boolean pit_vision) {
        this.pit_vision = pit_vision;
    }

    public boolean isPit_pneumatics() {
        return pit_pneumatics;
    }

    public void setPit_pneumatics(boolean pit_pneumatics) {
        this.pit_pneumatics = pit_pneumatics;
    }

    public boolean isPit_cubeManip() {
        return pit_cubeManip;
    }

    public void setPit_cubeManip(boolean pit_cubeManip) {
        this.pit_cubeManip = pit_cubeManip;
    }

    public boolean isPit_climb() {
        return pit_climb;
    }

    public void setPit_climb(boolean pit_climb) {
        this.pit_climb = pit_climb;
    }

    public boolean isPit_canLift() {
        return pit_canLift;
    }

    public void setPit_canLift(boolean pit_canLift) {
        this.pit_canLift = pit_canLift;
    }

    public int getPit_numLifted() {
        return pit_numLifted;
    }

    public void setPit_numLifted(int pit_numLifted) {
        this.pit_numLifted = pit_numLifted;
    }

    public boolean isPit_liftRamp() {
        return pit_liftRamp;
    }

    public void setPit_liftRamp(boolean pit_liftRamp) {
        this.pit_liftRamp = pit_liftRamp;
    }

    public boolean isPit_liftHook() {
        return pit_liftHook;
    }

    public void setPit_liftHook(boolean pit_liftHook) {
        this.pit_liftHook = pit_liftHook;
    }

    public boolean isPit_cubeArm() {
        return pit_cubeArm;
    }

    public void setPit_cubeArm(boolean pit_cubeArm) {
        this.pit_cubeArm = pit_cubeArm;
    }

    public boolean isPit_armIntake() {
        return pit_armIntake;
    }

    public void setPit_armIntake(boolean pit_armIntake) {
        this.pit_armIntake = pit_armIntake;
    }

    public boolean isPit_armSqueeze() {
        return pit_armSqueeze;
    }

    public void setPit_armSqueeze(boolean pit_armSqueeze) {
        this.pit_armSqueeze = pit_armSqueeze;
    }

    public boolean isPit_cubeBox() {
        return pit_cubeBox;
    }

    public void setPit_cubeBox(boolean pit_cubeBox) {
        this.pit_cubeBox = pit_cubeBox;
    }

    public boolean isPit_cubeBelt() {
        return pit_cubeBelt;
    }

    public void setPit_cubeBelt(boolean pit_cubeBelt) {
        this.pit_cubeBelt = pit_cubeBelt;
    }

    public boolean isPit_cubeOhtr() {
        return pit_cubeOhtr;
    }

    public void setPit_cubeOhtr(boolean pit_cubeOhtr) {
        this.pit_cubeOhtr = pit_cubeOhtr;
    }

    public boolean isPit_delLaunch() {
        return pit_delLaunch;
    }

    public void setPit_delLaunch(boolean pit_delLaunch) {
        this.pit_delLaunch = pit_delLaunch;
    }

    public boolean isPit_delPlace() {
        return pit_delPlace;
    }

    public void setPit_delPlace(boolean pit_delPlace) {
        this.pit_delPlace = pit_delPlace;
    }

    public String getPit_comment() {
        return pit_comment;
    }

    public void setPit_comment(String pit_comment) {
        this.pit_comment = pit_comment;
    }

    public String getPit_scout() {
        return pit_scout;
    }

    public void setPit_scout(String pit_scout) {
        this.pit_scout = pit_scout;
    }

    public String getPit_photoURL() {
        return pit_photoURL;
    }

    public void setPit_photoURL(String pit_photoURL) {
        this.pit_photoURL = pit_photoURL;
    }
}

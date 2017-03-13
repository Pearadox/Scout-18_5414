package com.pearadox.scout_5414;

import java.io.Serializable;

public class pitData implements Serializable {
    private static final long serialVersionUID = -54145414541400L;
    // ============= Pit Scout Data ================
    public String pit_team = " ";                   // Team #
    public boolean pit_tall = false;                // Dimension (Short or Tall)
    public int pit_totWheels = 0;                   // Total # of wheels
    public int pit_numTrac = 0;                     // Num. of Traction wheels
    public int pit_numOmni = 0;                     // Num. of Omni wheels
    public int pit_numMecanum = 0;                  // Num. of Mecanum wheels
    public boolean pit_gear_Collect = false;        // presence of gear collector
    public boolean pit_fuel_Container = false;      // presence of Storage bin
    public int pit_storSize = 0;                    // estimate of # of balls in bin
    public boolean pit_shooter = false;             // presence of Shooter
    public boolean pit_vision = false;              // presence of Vision Camera
    public boolean pit_pneumatics = false;          // presence of Pneumatics
    public boolean pit_fuelManip = false;           // presence of a way to pick up fuel from floor
    public boolean pit_climb = false;               // presence of a Climbing mechanism
    /* */
    public String pit_comment;                      // Comment(s)
    public String pit_scout = " ";                  // Student who collected the data

// ===========================================================================

    public pitData(String pit_team, boolean pit_tall, int pit_totWheels, int pit_numTrac, int pit_numOmni, int pit_numMecanum, boolean pit_gear_Collect, boolean pit_fuel_Container, int pit_storSize, boolean pit_shooter, boolean pit_vision, boolean pit_pneumatics, boolean pit_fuelManip, boolean pit_climb, String pit_comment, String pit_scout) {
        this.pit_team = pit_team;
        this.pit_tall = pit_tall;
        this.pit_totWheels = pit_totWheels;
        this.pit_numTrac = pit_numTrac;
        this.pit_numOmni = pit_numOmni;
        this.pit_numMecanum = pit_numMecanum;
        this.pit_gear_Collect = pit_gear_Collect;
        this.pit_fuel_Container = pit_fuel_Container;
        this.pit_storSize = pit_storSize;
        this.pit_shooter = pit_shooter;
        this.pit_vision = pit_vision;
        this.pit_pneumatics = pit_pneumatics;
        this.pit_fuelManip = pit_fuelManip;
        this.pit_climb = pit_climb;
        this.pit_comment = pit_comment;
        this.pit_scout = pit_scout;
    }

// Default constructor required for calls to
// DataSnapshot.getValue(teams.class)
public pitData() {
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPit_team() {
        return pit_team;
    }

    public void setPit_team(String pit_team) {
        this.pit_team = pit_team;
    }

    public boolean ispit_tall() {
        return pit_tall;
    }

    public void setPit_tall(boolean pit_tall) {
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

    public boolean isPit_gear_Collect() {
        return pit_gear_Collect;
    }

    public void setPit_gear_Collect(boolean pit_gear_Collect) {
        this.pit_gear_Collect = pit_gear_Collect;
    }

    public boolean isPit_fuel_Container() {
        return pit_fuel_Container;
    }

    public void setPit_fuel_Container(boolean pit_fuel_Container) {
        this.pit_fuel_Container = pit_fuel_Container;
    }

    public int getPit_storSize() {
        return pit_storSize;
    }

    public void setPit_storSize(int pit_storSize) {
        this.pit_storSize = pit_storSize;
    }

    public boolean isPit_shooter() {
        return pit_shooter;
    }

    public void setPit_shooter(boolean pit_shooter) {
        this.pit_shooter = pit_shooter;
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

    public boolean isPit_fuelManip() {
        return pit_fuelManip;
    }

    public void setPit_fuelManip(boolean pit_fuelManip) {
        this.pit_fuelManip = pit_fuelManip;
    }

    public boolean isPit_climb() {
        return pit_climb;
    }

    public void setPit_climb(boolean pit_climb) {
        this.pit_climb = pit_climb;
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




}

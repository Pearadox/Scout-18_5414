package com.pearadox.scout_5414;

import java.io.Serializable;

public class matchData implements Serializable {
    private static final long serialVersionUID = -54145414541400L;
    // ============= AUTO ================
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
    private int tele_gears_placed;
    private int tele_gears_attempt;

    private String tele_comment;            // Tele comment
    // ============= Final  ================

    private String final_comment;           // Final comment
    private String final_studID;            // Student doing the scouting

    public matchData(String match, String team_num, boolean auto_mode, boolean auto_rope, boolean auto_carry_fuel, int auto_fuel_amount, boolean auto_gear, int auto_gears_placed, int auto_gears_attempt, boolean auto_baseline, boolean auto_hg, int auto_hg_percent, boolean auto_lg, int auto_lg_percent, String auto_start, String auto_stop, String auto_gear_pos, boolean auto_act_hopper, String auto_fuel_collected, String auto_comment, int tele_gears_placed, int tele_gears_attempt, String tele_comment, String final_comment, String final_studID) {
        this.match = match;
        this.team_num = team_num;
        this.auto_mode = auto_mode;
        this.auto_rope = auto_rope;
        this.auto_carry_fuel = auto_carry_fuel;
        this.auto_fuel_amount = auto_fuel_amount;
        this.auto_gear = auto_gear;
        this.auto_gears_placed = auto_gears_placed;
        this.auto_gears_attempt = auto_gears_attempt;
        this.auto_baseline = auto_baseline;
        this.auto_hg = auto_hg;
        this.auto_hg_percent = auto_hg_percent;
        this.auto_lg = auto_lg;
        this.auto_lg_percent = auto_lg_percent;
        this.auto_start = auto_start;
        this.auto_stop = auto_stop;
        this.auto_gear_pos = auto_gear_pos;
        this.auto_act_hopper = auto_act_hopper;
        this.auto_fuel_collected = auto_fuel_collected;
        this.auto_comment = auto_comment;
        this.tele_gears_placed = tele_gears_placed;
        this.tele_gears_attempt = tele_gears_attempt;
        this.tele_comment = tele_comment;
        this.final_comment = final_comment;
        this.final_studID = final_studID;
    }

    // =====================================


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getTeam_num() {
        return team_num;
    }

    public void setTeam_num(String team_num) {
        this.team_num = team_num;
    }

    public boolean isAuto_mode() {
        return auto_mode;
    }

    public void setAuto_mode(boolean auto_mode) {
        this.auto_mode = auto_mode;
    }

    public boolean isAuto_rope() {
        return auto_rope;
    }

    public void setAuto_rope(boolean auto_rope) {
        this.auto_rope = auto_rope;
    }

    public boolean isAuto_carry_fuel() {
        return auto_carry_fuel;
    }

    public void setAuto_carry_fuel(boolean auto_carry_fuel) {
        this.auto_carry_fuel = auto_carry_fuel;
    }

    public int getAuto_fuel_amount() {
        return auto_fuel_amount;
    }

    public void setAuto_fuel_amount(int auto_fuel_amount) {
        this.auto_fuel_amount = auto_fuel_amount;
    }

    public boolean isAuto_gear() {
        return auto_gear;
    }

    public void setAuto_gear(boolean auto_gear) {
        this.auto_gear = auto_gear;
    }

    public int getAuto_gears_placed() {
        return auto_gears_placed;
    }

    public void setAuto_gears_placed(int auto_gears_placed) {
        this.auto_gears_placed = auto_gears_placed;
    }

    public int getAuto_gears_attempt() {
        return auto_gears_attempt;
    }

    public void setAuto_gears_attempt(int auto_gears_attempt) {
        this.auto_gears_attempt = auto_gears_attempt;
    }

    public boolean isAuto_baseline() {
        return auto_baseline;
    }

    public void setAuto_baseline(boolean auto_baseline) {
        this.auto_baseline = auto_baseline;
    }

    public boolean isAuto_hg() {
        return auto_hg;
    }

    public void setAuto_hg(boolean auto_hg) {
        this.auto_hg = auto_hg;
    }

    public int getAuto_hg_percent() {
        return auto_hg_percent;
    }

    public void setAuto_hg_percent(int auto_hg_percent) {
        this.auto_hg_percent = auto_hg_percent;
    }

    public boolean isAuto_lg() {
        return auto_lg;
    }

    public void setAuto_lg(boolean auto_lg) {
        this.auto_lg = auto_lg;
    }

    public int getAuto_lg_percent() {
        return auto_lg_percent;
    }

    public void setAuto_lg_percent(int auto_lg_percent) {
        this.auto_lg_percent = auto_lg_percent;
    }

    public String getAuto_start() {
        return auto_start;
    }

    public void setAuto_start(String auto_start) {
        this.auto_start = auto_start;
    }

    public String getAuto_stop() {
        return auto_stop;
    }

    public void setAuto_stop(String auto_stop) {
        this.auto_stop = auto_stop;
    }

    public String getAuto_gear_pos() {
        return auto_gear_pos;
    }

    public void setAuto_gear_pos(String auto_gear_pos) {
        this.auto_gear_pos = auto_gear_pos;
    }

    public boolean isAuto_act_hopper() {
        return auto_act_hopper;
    }

    public void setAuto_act_hopper(boolean auto_act_hopper) {
        this.auto_act_hopper = auto_act_hopper;
    }

    public String getAuto_fuel_collected() {
        return auto_fuel_collected;
    }

    public void setAuto_fuel_collected(String auto_fuel_collected) {
        this.auto_fuel_collected = auto_fuel_collected;
    }

    public String getAuto_comment() {
        return auto_comment;
    }

    public void setAuto_comment(String auto_comment) {
        this.auto_comment = auto_comment;
    }

    public int getTele_gears_placed() {
        return tele_gears_placed;
    }

    public void setTele_gears_placed(int tele_gears_placed) {
        this.tele_gears_placed = tele_gears_placed;
    }

    public int getTele_gears_attempt() {
        return tele_gears_attempt;
    }

    public void setTele_gears_attempt(int tele_gears_attempt) {
        this.tele_gears_attempt = tele_gears_attempt;
    }

    public String getTele_comment() {
        return tele_comment;
    }

    public void setTele_comment(String tele_comment) {
        this.tele_comment = tele_comment;
    }

    public String getFinal_comment() {
        return final_comment;
    }

    public void setFinal_comment(String final_comment) {
        this.final_comment = final_comment;
    }

    public String getFinal_studID() {
        return final_studID;
    }

    public void setFinal_studID(String final_studID) {
        this.final_studID = final_studID;
    }

// End of Getters/Setters

}
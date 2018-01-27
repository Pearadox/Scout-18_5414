package com.pearadox.scout_5414;

import java.io.Serializable;

public class matchData implements Serializable {
    private static final long serialVersionUID = -54145414541400L;
    // ============= AUTO ================
    private String match;                   // Match ID (e.g., 'T00' where T = X(Practice), Q(ualifying) and '00' - match #
    private String team_num;                // Team Number (e.g., '5414')
                                            // *** Pre-Game **
    private boolean pre_cube;               // Do they carry a cube
    private String pre_startPos;            // Start Position

    private boolean auto_mode;              // Do they have Autonomous mode?
    private boolean auto_baseline;          // Did they cross Baseline
    private boolean auto_cube_switch;       // cube placed on Switch during Auto
    private boolean auto_cube_switch_att;   // cube attempt on Switch during Auto
    private boolean auto_switch_extra;      // extra cube placed on switch during Auto
    private boolean auto_cube_scale;        // cube placed on switch during Auto
    private boolean auto_cube_scale_att;    // cube attempt on switch during Auto
    private boolean auto_xover_switch;      // crossed over field to Switch
    private boolean auto_xover_scale;       // crossed over field to Scale
    private boolean auto_wrong_switch;      // put cube in WRONG Switch
    private boolean auto_wrong_scale;       // put cube in WRONG Scale
    private String auto_comment;            // Auto comment
    // ============== TELE =================
    private int     tele_cube_switch;       // # cubes placed on Switch during Tele
    private int     tele_switch_attempt;    // # cubes attempted on Switch during Tele
    private int     tele_cube_scale;        // # cubes placed on Switch during Tele
    private int     tele_scale_attempt;     // # cubes attempted on Switch during Tele
    private int     tele_their_switch;      // # cubes placed on _THEIR_Switch during Tele
    private int     tele_their_attempt;     // # cubes attempted on _THEIR_Switch during Tele
    private int     tele_cube_exchange;     // # cubes placed in Exchange during Tele
    private int     tele_cube_portal;       // # cubes retrieved from Portal during Tele
    private int     tele_cube_pwrzone;      // # cubes retrieved from Power Zone during Tele
    private int     tele_cube_floor;        // # cubes retrieved from our Floor or Platform Zone during Tele
    private int     tele_their_floor;       // # cubes retrieved from their Floor or Platform Zone during Tele
    private boolean tele_cube_pickup;       // Did they pick up cube(s)
    private boolean tele_placed_cube;       // Did they place cube(s)
    private boolean tele_launched_cube;     // Did they launch cube(s)
    private boolean tele_on_platform;       // Finished on platform
    private boolean tele_climb_attempt;     // Did they ATTEMPT climb?
    private boolean tele_climb_success;     // Was climb successful?
    private boolean tele_grab_rung;         // == Grabbed rung to climb
    private boolean tele_grab_side;         // == Grabbed side to climb
    private boolean tele_lift_one;          // Lifted one other robot
    private boolean tele_lift_two;          // Lifted one other robot
    private boolean tele_got_lift;          // Got Lifted by another robot

    private String tele_comment;            // Tele comment
    // ============= Final  ================
    private boolean final_lostParts;         // Did they lose parts
    private boolean final_lostComms;         // Did they lose communication
    private boolean final_defense_good;      // Was their overall Defense Good (bad=false)
    private boolean final_def_Lane;          // Did they use Lane Defense
    private boolean final_def_Block;         // Did they use Blocking Defense
    private boolean final_def_BlockSwitch;   // Did they block the Switch
    private int     final_num_Penalties;     // How many penalties received?

    private String  final_comment;           // Final comment
    private String  final_studID;            // Student doing the scouting
    private String  final_dateTime;          // Date & Time data was saved

// =================================================================================


    public matchData(String match, String team_num, boolean pre_cube, String pre_startPos, boolean auto_mode, boolean auto_baseline, boolean auto_cube_switch, boolean auto_cube_switch_att, boolean auto_switch_extra, boolean auto_cube_scale, boolean auto_cube_scale_att, boolean auto_xover_switch, boolean auto_xover_scale, boolean auto_wrong_switch, boolean auto_wrong_scale, String auto_comment, int tele_cube_switch, int tele_switch_attempt, int tele_cube_scale, int tele_scale_attempt, int tele_their_switch, int tele_their_attempt, int tele_cube_exchange, int tele_cube_portal, int tele_cube_pwrzone, int tele_cube_floor, int tele_their_floor, boolean tele_cube_pickup, boolean tele_placed_cube, boolean tele_launched_cube, boolean tele_on_platform, boolean tele_climb_attempt, boolean tele_climb_success, boolean tele_grab_rung, boolean tele_grab_side, boolean tele_lift_one, boolean tele_lift_two, boolean tele_got_lift, String tele_comment, boolean final_lostParts, boolean final_lostComms, boolean final_defense_good, boolean final_def_Lane, boolean final_def_Block, boolean final_def_BlockSwitch, int final_num_Penalties, String final_comment, String final_studID, String final_dateTime) {
        this.match = match;
        this.team_num = team_num;
        this.pre_cube = pre_cube;
        this.pre_startPos = pre_startPos;
        this.auto_mode = auto_mode;
        this.auto_baseline = auto_baseline;
        this.auto_cube_switch = auto_cube_switch;
        this.auto_cube_switch_att = auto_cube_switch_att;
        this.auto_switch_extra = auto_switch_extra;
        this.auto_cube_scale = auto_cube_scale;
        this.auto_cube_scale_att = auto_cube_scale_att;
        this.auto_xover_switch = auto_xover_switch;
        this.auto_xover_scale = auto_xover_scale;
        this.auto_wrong_switch = auto_wrong_switch;
        this.auto_wrong_scale = auto_wrong_scale;
        this.auto_comment = auto_comment;
        this.tele_cube_switch = tele_cube_switch;
        this.tele_switch_attempt = tele_switch_attempt;
        this.tele_cube_scale = tele_cube_scale;
        this.tele_scale_attempt = tele_scale_attempt;
        this.tele_their_switch = tele_their_switch;
        this.tele_their_attempt = tele_their_attempt;
        this.tele_cube_exchange = tele_cube_exchange;
        this.tele_cube_portal = tele_cube_portal;
        this.tele_cube_pwrzone = tele_cube_pwrzone;
        this.tele_cube_floor = tele_cube_floor;
        this.tele_their_floor = tele_their_floor;
        this.tele_cube_pickup = tele_cube_pickup;
        this.tele_placed_cube = tele_placed_cube;
        this.tele_launched_cube = tele_launched_cube;
        this.tele_on_platform = tele_on_platform;
        this.tele_climb_attempt = tele_climb_attempt;
        this.tele_climb_success = tele_climb_success;
        this.tele_grab_rung = tele_grab_rung;
        this.tele_grab_side = tele_grab_side;
        this.tele_lift_one = tele_lift_one;
        this.tele_lift_two = tele_lift_two;
        this.tele_got_lift = tele_got_lift;
        this.tele_comment = tele_comment;
        this.final_lostParts = final_lostParts;
        this.final_lostComms = final_lostComms;
        this.final_defense_good = final_defense_good;
        this.final_def_Lane = final_def_Lane;
        this.final_def_Block = final_def_Block;
        this.final_def_BlockSwitch = final_def_BlockSwitch;
        this.final_num_Penalties = final_num_Penalties;
        this.final_comment = final_comment;
        this.final_studID = final_studID;
        this.final_dateTime = final_dateTime;
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// Default constructor required for calls to
// DataSnapshot.getValue(matchData.class)
public matchData() {

}

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// Getters & Setters

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

    public boolean isPre_cube() {
        return pre_cube;
    }

    public void setPre_cube(boolean pre_cube) {
        this.pre_cube = pre_cube;
    }

    public String getPre_startPos() {
        return pre_startPos;
    }

    public void setPre_startPos(String pre_startPos) {
        this.pre_startPos = pre_startPos;
    }

    public boolean isAuto_mode() {
        return auto_mode;
    }

    public void setAuto_mode(boolean auto_mode) {
        this.auto_mode = auto_mode;
    }

    public boolean isAuto_baseline() {
        return auto_baseline;
    }

    public void setAuto_baseline(boolean auto_baseline) {
        this.auto_baseline = auto_baseline;
    }

    public boolean isAuto_cube_switch() {
        return auto_cube_switch;
    }

    public void setAuto_cube_switch(boolean auto_cube_switch) {
        this.auto_cube_switch = auto_cube_switch;
    }

    public boolean isAuto_cube_switch_att() {
        return auto_cube_switch_att;
    }

    public void setAuto_cube_switch_att(boolean auto_cube_switch_att) {
        this.auto_cube_switch_att = auto_cube_switch_att;
    }

    public boolean isAuto_switch_extra() {
        return auto_switch_extra;
    }

    public void setAuto_switch_extra(boolean auto_switch_extra) {
        this.auto_switch_extra = auto_switch_extra;
    }

    public boolean isAuto_cube_scale() {
        return auto_cube_scale;
    }

    public void setAuto_cube_scale(boolean auto_cube_scale) {
        this.auto_cube_scale = auto_cube_scale;
    }

    public boolean isAuto_cube_scale_att() {
        return auto_cube_scale_att;
    }

    public void setAuto_cube_scale_att(boolean auto_cube_scale_att) {
        this.auto_cube_scale_att = auto_cube_scale_att;
    }

    public boolean isAuto_xover_switch() {
        return auto_xover_switch;
    }

    public void setAuto_xover_switch(boolean auto_xover_switch) {
        this.auto_xover_switch = auto_xover_switch;
    }

    public boolean isAuto_xover_scale() {
        return auto_xover_scale;
    }

    public void setAuto_xover_scale(boolean auto_xover_scale) {
        this.auto_xover_scale = auto_xover_scale;
    }

    public boolean isAuto_wrong_switch() {
        return auto_wrong_switch;
    }

    public void setAuto_wrong_switch(boolean auto_wrong_switch) {
        this.auto_wrong_switch = auto_wrong_switch;
    }

    public boolean isAuto_wrong_scale() {
        return auto_wrong_scale;
    }

    public void setAuto_wrong_scale(boolean auto_wrong_scale) {
        this.auto_wrong_scale = auto_wrong_scale;
    }

    public String getAuto_comment() {
        return auto_comment;
    }

    public void setAuto_comment(String auto_comment) {
        this.auto_comment = auto_comment;
    }

    public int getTele_cube_switch() {
        return tele_cube_switch;
    }

    public void setTele_cube_switch(int tele_cube_switch) {
        this.tele_cube_switch = tele_cube_switch;
    }

    public int getTele_switch_attempt() {
        return tele_switch_attempt;
    }

    public void setTele_switch_attempt(int tele_switch_attempt) {
        this.tele_switch_attempt = tele_switch_attempt;
    }

    public int getTele_cube_scale() {
        return tele_cube_scale;
    }

    public void setTele_cube_scale(int tele_cube_scale) {
        this.tele_cube_scale = tele_cube_scale;
    }

    public int getTele_scale_attempt() {
        return tele_scale_attempt;
    }

    public void setTele_scale_attempt(int tele_scale_attempt) {
        this.tele_scale_attempt = tele_scale_attempt;
    }

    public int getTele_their_switch() {
        return tele_their_switch;
    }

    public void setTele_their_switch(int tele_their_switch) {
        this.tele_their_switch = tele_their_switch;
    }

    public int getTele_their_attempt() {
        return tele_their_attempt;
    }

    public void setTele_their_attempt(int tele_their_attempt) {
        this.tele_their_attempt = tele_their_attempt;
    }

    public int getTele_cube_exchange() {
        return tele_cube_exchange;
    }

    public void setTele_cube_exchange(int tele_cube_exchange) {
        this.tele_cube_exchange = tele_cube_exchange;
    }

    public int getTele_cube_portal() {
        return tele_cube_portal;
    }

    public void setTele_cube_portal(int tele_cube_portal) {
        this.tele_cube_portal = tele_cube_portal;
    }

    public int getTele_cube_pwrzone() {
        return tele_cube_pwrzone;
    }

    public void setTele_cube_pwrzone(int tele_cube_pwrzone) {
        this.tele_cube_pwrzone = tele_cube_pwrzone;
    }

    public int getTele_cube_floor() {
        return tele_cube_floor;
    }

    public void setTele_cube_floor(int tele_cube_floor) {
        this.tele_cube_floor = tele_cube_floor;
    }

    public int getTele_their_floor() {
        return tele_their_floor;
    }

    public void setTele_their_floor(int tele_their_floor) {
        this.tele_their_floor = tele_their_floor;
    }

    public boolean isTele_cube_pickup() {
        return tele_cube_pickup;
    }

    public void setTele_cube_pickup(boolean tele_cube_pickup) {
        this.tele_cube_pickup = tele_cube_pickup;
    }

    public boolean isTele_placed_cube() {
        return tele_placed_cube;
    }

    public void setTele_placed_cube(boolean tele_placed_cube) {
        this.tele_placed_cube = tele_placed_cube;
    }

    public boolean isTele_launched_cube() {
        return tele_launched_cube;
    }

    public void setTele_launched_cube(boolean tele_launched_cube) {
        this.tele_launched_cube = tele_launched_cube;
    }

    public boolean isTele_on_platform() {
        return tele_on_platform;
    }

    public void setTele_on_platform(boolean tele_on_platform) {
        this.tele_on_platform = tele_on_platform;
    }

    public boolean isTele_climb_attempt() {
        return tele_climb_attempt;
    }

    public void setTele_climb_attempt(boolean tele_climb_attempt) {
        this.tele_climb_attempt = tele_climb_attempt;
    }

    public boolean isTele_climb_success() {
        return tele_climb_success;
    }

    public void setTele_climb_success(boolean tele_climb_success) {
        this.tele_climb_success = tele_climb_success;
    }

    public boolean isTele_grab_rung() {
        return tele_grab_rung;
    }

    public void setTele_grab_rung(boolean tele_grab_rung) {
        this.tele_grab_rung = tele_grab_rung;
    }

    public boolean isTele_grab_side() {
        return tele_grab_side;
    }

    public void setTele_grab_side(boolean tele_grab_side) {
        this.tele_grab_side = tele_grab_side;
    }

    public boolean isTele_lift_one() {
        return tele_lift_one;
    }

    public void setTele_lift_one(boolean tele_lift_one) {
        this.tele_lift_one = tele_lift_one;
    }

    public boolean isTele_lift_two() {
        return tele_lift_two;
    }

    public void setTele_lift_two(boolean tele_lift_two) {
        this.tele_lift_two = tele_lift_two;
    }

    public boolean isTele_got_lift() {
        return tele_got_lift;
    }

    public void setTele_got_lift(boolean tele_got_lift) {
        this.tele_got_lift = tele_got_lift;
    }

    public String getTele_comment() {
        return tele_comment;
    }

    public void setTele_comment(String tele_comment) {
        this.tele_comment = tele_comment;
    }

    public boolean isFinal_lostParts() {
        return final_lostParts;
    }

    public void setFinal_lostParts(boolean final_lostParts) {
        this.final_lostParts = final_lostParts;
    }

    public boolean isFinal_lostComms() {
        return final_lostComms;
    }

    public void setFinal_lostComms(boolean final_lostComms) {
        this.final_lostComms = final_lostComms;
    }

    public boolean isFinal_defense_good() {
        return final_defense_good;
    }

    public void setFinal_defense_good(boolean final_defense_good) {
        this.final_defense_good = final_defense_good;
    }

    public boolean isFinal_def_Lane() {
        return final_def_Lane;
    }

    public void setFinal_def_Lane(boolean final_def_Lane) {
        this.final_def_Lane = final_def_Lane;
    }

    public boolean isFinal_def_Block() {
        return final_def_Block;
    }

    public void setFinal_def_Block(boolean final_def_Block) {
        this.final_def_Block = final_def_Block;
    }

    public boolean isFinal_def_BlockSwitch() {
        return final_def_BlockSwitch;
    }

    public void setFinal_def_BlockSwitch(boolean final_def_BlockSwitch) {
        this.final_def_BlockSwitch = final_def_BlockSwitch;
    }

    public int getFinal_num_Penalties() {
        return final_num_Penalties;
    }

    public void setFinal_num_Penalties(int final_num_Penalties) {
        this.final_num_Penalties = final_num_Penalties;
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

    public String getFinal_dateTime() {
        return final_dateTime;
    }

    public void setFinal_dateTime(String final_dateTime) {
        this.final_dateTime = final_dateTime;
    }


// End of Getters/Setters

}
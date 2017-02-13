package com.pearadox.scout_5414;

import com.google.firebase.database.IgnoreExtraProperties;

public class p_Firebase {

    @IgnoreExtraProperties
    public static class teamsObj {
        private String team_num;
        private String team_name;
        private String team_loc;
        // Default constructor required for calls to
        // DataSnapshot.getValue(teams.class)
        public teamsObj() {
        }

        public teamsObj(String team_num, String team_name, String team_loc) {
            this.team_num = team_num;
            this.team_name = team_name;
            this.team_loc = team_loc;
        }

        public String getTeamNum() {
            return team_num;
        }
        public String getTeamName() {
            return team_name;
        }
        public String getTeamLoc() {
            return team_loc;
        }
        public void setTeamName(String teamName) {
            this.team_name = teamName;
        }
        public void setTeamNumber(String teamNumber) {
            this.team_num = teamNumber;
        }
        public void setTeamLoc(String teamLoc) {
            this.team_loc = teamLoc;
        }
    }

// ==========================================================
// ==========================================================
public static class devicesObj {
    private String dev_name;
    private String dev_desc;
    private String dev_id;
    private String stud_id;
    private String btUUID;
    private devicesObj() {
    }
    public devicesObj(String dev_name, String dev_desc, String dev_id, String stud_id, String btUUID) {
        this.dev_name = dev_name;
        this.dev_desc = dev_desc;
        this.dev_id = dev_id;
        this.stud_id = stud_id;
        this.btUUID = btUUID;
    }
}

// ==========================================================
// ==========================================================
    public static class students {
        private String name;
        private String status;
        // Default constructor required for calls to
        // DataSnapshot.getValue(students.class)
        public students() {
        }
        public students(String name, String status) {
            this.name = name;
            this.status = status;
        }
        public String getName() {
            return name;
        }
        public String getStatus() {
            return status;
        }
        public void setName(String sName) {
            this.name = sName;
        }
        public void setStatus(String Stat) {
            this.status = Stat;
        }
    }

// ==========================================================
// ==========================================================

}

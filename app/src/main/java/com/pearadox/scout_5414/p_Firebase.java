package com.pearadox.scout_5414;

import com.google.firebase.database.IgnoreExtraProperties;

public class p_Firebase {

    @IgnoreExtraProperties
    public static class teamsObj {
        private String team_num;
        private String team_name;
        private String team_loc;
        public teamsObj() {
        }

        public teamsObj(String team_num, String team_name, String team_loc) {
            this.team_num = team_num;
            this.team_name = team_name;
            this.team_loc = team_loc;
        }
        public String getTeam_num() {
            return team_num;
        }

        public void setTeam_num(String team_num) {
            this.team_num = team_num;
        }

        public String getTeam_name() {
            return team_name;
        }

        public void setTeam_name(String team_name) {
            this.team_name = team_name;
        }

        public String getTeam_loc() {
            return team_loc;
        }

        public void setTeam_loc(String team_loc) {
            this.team_loc = team_loc;
        }


//        public String getTeamNum() {
//            return team_num;
//        }
//        public String getTeamName() {
//            return team_name;
//        }
//        public String getTeamLoc() {
//            return team_loc;
//        }
//        public void setTeamName(String teamName) {
//            this.team_name = teamName;
//        }
//        public void setTeamNumber(String teamNumber) {
//            this.team_num = teamNumber;
//        }
//        public void setTeamLoc(String teamLoc) {
//            this.team_loc = teamLoc;
//        }
    }

// ==========================================================
// ==========================================================
    public static class devicesObj {
        private String dev_name;
        private String dev_desc;
        private String dev_id;
        private String stud_id;
        private String btUUID;
        public devicesObj() {
        }

    public String getDev_desc() {
        return dev_desc;
    }

    public void setDev_desc(String dev_desc) {
        this.dev_desc = dev_desc;
    }

    public String getDev_id() {
        return dev_id;
    }

    public void setDev_id(String dev_id) {
        this.dev_id = dev_id;
    }

    public String getStud_id() {
        return stud_id;
    }

    public void setStud_id(String stud_id) {
        this.stud_id = stud_id;
    }

    public String getBtUUID() {
        return btUUID;
    }

    public void setBtUUID(String btUUID) {
        this.btUUID = btUUID;
    }

    public devicesObj(String dev_name, String dev_desc, String dev_id, String stud_id, String btUUID) {
            this.dev_name = dev_name;
            this.dev_desc = dev_desc;
            this.dev_id = dev_id;
            this.stud_id = stud_id;
            this.btUUID = btUUID;
        }

    public String getDev_name() {
        return dev_name;
    }

    public void setDev_name(String dev_name) {
        this.dev_name = dev_name;
    }
}

// ==========================================================
// ==========================================================
    public static class students {
        private String name;
        private String status;
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

    public static class matchObj {
        private String date;
        private String time;
        private String mtype;
        private String match;
        private String r1;
        private String r2;
        private String r3;
        private String b1;
        private String b2;
        private String b3;

        // Default constructor required for calls to
        // DataSnapshot.getValue(teams.class)
        public matchObj() {
        }

        public matchObj(String date, String time, String mtype, String match, String r1, String r2, String r3, String b1, String b2, String b3) {
            this.date = date;
            this.time = time;
            this.mtype = mtype;
            this.match = match;
            this.r1 = r1;
            this.r2 = r2;
            this.r3 = r3;
            this.b1 = r1;
            this.b2 = b2;
            this.b3 = b3;
        }
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMtype() {
            return mtype;
        }

        public void setMtype(String mtype) {
            this.mtype = mtype;
        }

        public String getMatch() {
            return match;
        }

        public void setMatch(String match) {
            this.match = match;
        }

        public String getR1() {
            return r1;
        }

        public void setR1(String r1) {
            this.r1 = r1;
        }

        public String getR2() {
            return r2;
        }

        public void setR2(String r2) {
            this.r2 = r2;
        }

        public String getR3() {
            return r3;
        }

        public void setR3(String r3) {
            this.r3 = r3;
        }

        public String getB1() {
            return b1;
        }

        public void setB1(String b1) {
            this.b1 = b1;
        }

        public String getB2() {
            return b2;
        }

        public void setB2(String b2) {
            this.b2 = b2;
        }

        public String getB3() {
            return b3;
        }

        public void setB3(String b3) {
            this.b3 = b3;
        }
    }
// ==========================================================
// ==========================================================

}

package com.pearadox.scout_5414;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class DraftSettingsActivity extends AppCompatActivity {
    String TAG = "DraftSettingsActivity";        // This CLASS name
    public static final String  CUBE_PREF_AUTOSW =   "prefCube_autoSw";
    public static final String  CUBE_PREF_AUTOSC =   "prefCube_autoSc";
    public static final String  CUBE_PREF_TELESW =   "prefCube_teleSw";
    public static final String  CUBE_PREF_TELESC =   "prefCube_teleSc";
    public static final String  CUBE_PREF_OTHER =    "prefCube_othr";
    public static final String  CUBE_PREF_EXCHANGE = "prefCube_exchange";

    public static final String  CUBECOL_PREF_PORTAL = "prefCubeCol_portal";
    public static final String  CUBECOL_PREF_ZONE =   "prefCubeCol_zone";
    public static final String  CUBECOL_PREF_FLOOR =  "prefCubeCol_floor";
    public static final String  CUBECOL_PREF_STOLEN = "prefCubeCol_stolen";
    public static final String  CUBECOL_PREF_RANDOM = "prefCubeCol_random";

    public static final String  CLIMB_PREF_CLIMBS =  "prefClimb_NumClimbs";
    public static final String  CLIMB_PREF_LIFT1 =  "prefClimb_lift1";
    public static final String  CLIMB_PREF_LIFT2 =  "prefClimb_lift2";
    public static final String  CLIMB_PREF_PLAT =   "prefClimb_onPlat";
    public static final String  CLIMB_PREF_LIFTED = "prefClimb_lifted";

    public static final String  WEIGHT_PREF_CLIMB =       "prefWeight_climb";
    public static final String  WEIGHT_PREF_CUBESSWITCH = "prefWeight_cubesSwitch";
    public static final String  WEIGHT_PREF_CUBESSCALE =  "prefWeight_cubesScale";

    public static final String  ALLIANCE_PICKS_NUM =   "prefAlliance_num";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new CubeSettingsFrag())
                .commit();
        Log.e(TAG, "**** Draft Scout Settings  **** ");
        Log.w(TAG, " \n  \n");
    }
}


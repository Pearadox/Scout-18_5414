package com.pearadox.scout_5414;

import android.os.Bundle;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */

public class CubeSettingsFrag extends PreferenceFragmentCompat {
    String TAG = "CubeSettingsFrag";        // This CLASS name
    EditTextPreference txt_portal;
    EditTextPreference txt_exchange;
    EditTextPreference txt_Lift1;
    EditTextPreference txt_Lift2;
    EditTextPreference txt_Cubes;
    EditTextPreference txt_Climb;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState,
                                    String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        Log.i(TAG, "**** Cube Settings  ****");

    }

}

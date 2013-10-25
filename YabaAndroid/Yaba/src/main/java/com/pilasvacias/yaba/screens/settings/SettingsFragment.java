package com.pilasvacias.yaba.screens.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.pilasvacias.yaba.R;

/**
 * Created by IzanRodrigo on 25/10/13.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}

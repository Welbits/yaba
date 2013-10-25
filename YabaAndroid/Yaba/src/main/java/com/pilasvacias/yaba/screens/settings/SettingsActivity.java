package com.pilasvacias.yaba.screens.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by IzanRodrigo on 25/10/13.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = new SettingsFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }
}

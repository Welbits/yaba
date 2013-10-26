package com.pilasvacias.yaba.screens.settings;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.util.VersionUtils;
import com.pilasvacias.yaba.util.WToast;

/**
 * Created by IzanRodrigo on 25/10/13.
 */
public class SettingsFragment extends PreferenceFragment {

    // Fields
    /**
     * Array used to handle user click in VersionPreference
     */
    private long[] hits = new long[3];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        configureOpenSourceLicensesPreference();
        configureYabaVersionPreference();
    }

    private void configureOpenSourceLicensesPreference() {
        Preference openSourceLicensesPreference = findPreference(SettingsConstants.OPEN_SOURCE_LICENSES);
        openSourceLicensesPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                loadOpenSourceLicenses();
                return true;
            }
        });
    }

    private void configureYabaVersionPreference() {
        PackageManager packageManager = getActivity().getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
            String yabaVersion = packageInfo.versionName;
            Preference yabaVersionPreference = findPreference(SettingsConstants.YABA_VERSION);
            yabaVersionPreference.setSummary(yabaVersion);
            yabaVersionPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    System.arraycopy(hits, 1, hits, 0, hits.length - 1);
                    hits[hits.length - 1] = SystemClock.uptimeMillis();
                    if (hits[0] >= (SystemClock.uptimeMillis() - 500)) {
                        WToast.showLong(getActivity(), "Anda majete, cúrrate un Easter Egg");
                    }
                    return true;
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadOpenSourceLicenses() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        DialogFragment dialogFragment = new OpenSourceLicensesFragment();
        dialogFragment.show(fragmentTransaction, null);
    }
}

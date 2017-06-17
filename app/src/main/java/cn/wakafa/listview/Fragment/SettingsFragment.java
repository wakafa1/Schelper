package cn.wakafa.listview.Fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import cn.wakafa.listview.R;

/**
 * Created by wakaf on 2017/5/23.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }

}
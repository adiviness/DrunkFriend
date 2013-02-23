package com.austindiviness.drunkfriend;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {
		String[] temp = {"hello", "world"};
		String[] tempValues = {"0", "1"};
		
		private String[] buttonPrefNames = {
			"dial1",
			"dial2",
			"dial3",
			"dial4",
			"dial5",
			"dial6"
		};
		private ListPreference listPref;
		
		@Override
		protected void onCreate(Bundle bundle) {
			super.onCreate(bundle);
			addPreferencesFromResource(R.layout.settings);
			
			for (String buttonPrefName: buttonPrefNames) {
				listPref = (ListPreference) findPreference(buttonPrefName);
				listPref.setEntries(temp);
				listPref.setEntryValues(tempValues);
			}
		}
}

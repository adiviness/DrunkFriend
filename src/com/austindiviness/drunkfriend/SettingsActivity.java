package com.austindiviness.drunkfriend;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
		ArrayList<ContactData> data;
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> numbers = new ArrayList<String>();
		
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
			Bundle extraData = getIntent().getExtras();
			data = (ArrayList<ContactData>) extraData.get("contactsData");
			names.add("None");
			numbers.add("null");
			for (ContactData item: data) {
				names.add(item.getName());
				numbers.add(item.getNumber());
			}
			
			for (String buttonPrefName: buttonPrefNames) {
				listPref = (ListPreference) findPreference(buttonPrefName);
				listPref.setEntries(names.toArray(new String[names.size()]));
				listPref.setEntryValues(names.toArray(new String[numbers.size()]));
			}
		}
		
		@Override
		protected void onResume() {
			super.onResume();
			getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(SettingsActivity.this);
			// set summary for speed dial prefs to selection
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
			for (String prefName: buttonPrefNames) {
				Preference pref = findPreference(prefName);
				String value = prefs.getString(prefName, "None");
				if (value.equalsIgnoreCase("None")) {
					pref.setSummary("No Contact Selected");
				}
				else {
					pref.setSummary(value);
				}
			}
		}
		
		@Override
		protected void onPause() {
			super.onPause();
			getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(SettingsActivity.this);
		}
	
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		Preference pref = findPreference(key);
		String value = prefs.getString(key, "None");
		//Toast.makeText(getBaseContext(), value, Toast.LENGTH_SHORT).show();
		boolean nameFlag = value.equalsIgnoreCase("None") ? false : true;
		ArrayList<String> buttonPrefs = new ArrayList<String>(Arrays.asList(buttonPrefNames));
		if (buttonPrefs.contains(key)) {
			if (!nameFlag) {
				pref.setSummary("No Contact Selected");
			}
			else {
				pref.setSummary(value);
			//Toast.makeText(getBaseContext(), value, Toast.LENGTH_SHORT).show();
			}
		}
	}

}
















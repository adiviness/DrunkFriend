package com.austindiviness.drunkfriend;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FavoritesActivity extends Activity {
	private int[] buttonIds = {
			R.id.speed_dial_1,
			R.id.speed_dial_2,
			R.id.speed_dial_3,
			R.id.speed_dial_4,
			R.id.speed_dial_5,
			R.id.speed_dial_6
	};
	
	private String[] buttonPrefNames = {
		"dial1",
		"dial2",
		"dial3",
		"dial4",
		"dial5",
		"dial6"
	};
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.favorites_layout);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(FavoritesActivity.this);
		for (int i = 0; i < buttonIds.length; ++i) {
			int buttonId = buttonIds[i];
			String buttonPref = buttonPrefNames[i];
			Button tempButton = (Button) findViewById(buttonId);
			tempButton.setText(prefs.getString(buttonPref, "null"));
		}
		
	}
	
	public void favoriteButtonClick(View v) {
		Button button = (Button) v;
		CharSequence which =  button.getText();
		Toast.makeText(getBaseContext(), which, Toast.LENGTH_SHORT).show();
	}

}

package com.austindiviness.drunkfriend;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FavoritesActivity extends Activity {
	public int mainMenuId = 1;
	public int settingsId = Menu.FIRST;
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
	}
	
	@Override
	protected void onResume() {
		super.onResume();
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(mainMenuId, settingsId, 0, "Settings");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case Menu.FIRST:
				Intent settingsIntent = new Intent(FavoritesActivity.this, SettingsActivity.class);
				FavoritesActivity.this.startActivity(settingsIntent);
		}
		return true;
	}

}

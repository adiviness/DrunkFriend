package com.austindiviness.drunkfriend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Home extends Activity {
	// The main launcher for the two different portions of the app
	public int mainMenuId = 1;
	public int settingsId = Menu.FIRST;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.home_layout);
		
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
				Intent settingsIntent = new Intent(Home.this, SettingsActivity.class);
				Home.this.startActivity(settingsIntent);
		}
		return true;
	}

	public void startAllContactsActivity(View v) {
		//Toast.makeText(getBaseContext(), "all contacts", Toast.LENGTH_SHORT).show();
		Intent allContactsIntent = new Intent(Home.this, MainActivity.class);
		Home.this.startActivity(allContactsIntent);
	}
	
	public void startFavoritesActivity(View v) {
		//Toast.makeText(getBaseContext(), "favorites", Toast.LENGTH_SHORT).show();
		Intent favoritesIntent = new Intent(Home.this, FavoritesActivity.class);
		Home.this.startActivity(favoritesIntent);
	}
}

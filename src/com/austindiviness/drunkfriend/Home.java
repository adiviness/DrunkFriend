package com.austindiviness.drunkfriend;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
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
		
		// check if GPS is enabled
		if (!gpsEnabled()) {
			AlertDialog.Builder noGPSEnabled = new AlertDialog.Builder(Home.this);
			noGPSEnabled.setTitle("GPS Disabled");
			noGPSEnabled.setMessage("Please enable GPS in settings menu");
			noGPSEnabled.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			noGPSEnabled.setPositiveButton("GPS Settings", new DialogInterface.OnClickListener() {		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// create intent to send user to GPS settings screen
					Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(gpsIntent);
					dialog.dismiss();
				}
			});
			noGPSEnabled.show();
		}
		
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
	
	public boolean gpsEnabled() {
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
} // end class




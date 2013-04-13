package com.austindiviness.drunkfriend;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Home extends Activity {
	// The main launcher for the two different portions of the app
	public int mainMenuId = 1;
	public int settingsId = Menu.FIRST;
	public ArrayList<ContactData> data;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.home_layout);
		data = getContacts();
		
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
				settingsIntent.putExtra("contactsData", data);
				Home.this.startActivity(settingsIntent);
		}
		return true;
	}

	public void startAllContactsActivity(View v) {
		//Toast.makeText(getBaseContext(), "all contacts", Toast.LENGTH_SHORT).show();
		Intent allContactsIntent = new Intent(Home.this, MainActivity.class);
		allContactsIntent.putExtra("contactsData", data);
		Home.this.startActivity(allContactsIntent);
	}
	
	public void startFavoritesActivity(View v) {
		//Toast.makeText(getBaseContext(), "favorites", Toast.LENGTH_SHORT).show();
		Intent favoritesIntent = new Intent(Home.this, FavoritesActivity.class);
		favoritesIntent.putExtra("contactsData", data);
		Home.this.startActivity(favoritesIntent);
	}
	
	public boolean gpsEnabled() {
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
 	public ArrayList<ContactData> getContacts() {
		ArrayList<ContactData> data = new ArrayList<ContactData>(); // array to hold contact data to return to main method
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // dunno
		String sql = "data "
                + "JOIN raw_contacts ON (data.raw_contact_id = raw_contacts._id) "
                + "JOIN contacts ON (raw_contacts.contact_id = contacts._id)";
		String[] projection = new String[] {
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, 
				ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER, 
				ContactsContract.CommonDataKinds.Phone.TYPE,
				ContactsContract.CommonDataKinds.Phone.NUMBER}; 
		String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1";
		String[] selectionArgs = {selection};
		Cursor names;
		PackageManager manager = this.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(getPackageName(), 0);
		}
		catch (Exception e) {
			Toast.makeText(getBaseContext(), "Android version cannot be determined", Toast.LENGTH_LONG).show();
			finish();
		}
		if (info.versionCode < Build.VERSION_CODES.HONEYCOMB) {
			names = getContentResolver().query(uri, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
		}
		else {
			names = getContentResolver().query(uri, projection, selection, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
		}
		//Cursor names = new DBHelper(getBaseContext()).getReadableDatabase().rawQuery(sql, selectionArgs);
		names.moveToFirst();
		do {
			int phoneType = names.getInt(names.getColumnIndex(Phone.TYPE));
			if (phoneType == Phone.TYPE_MOBILE) {
				String name = names.getString(names.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				String number = names.getString(names.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				data.add(new ContactData(name, number));
			}
		}
		while (names.moveToNext());
		return data;
	}
	

} // end class




package com.austindiviness.drunkfriend;

import com.austindiviness.drunkfriend.R;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Collections;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public ArrayList<ContactData> data;
	public LocationListener locationListener;
	public LocationManager locationManager;
	public String numberToText;
	public String message1 = "Hi, this is an automated message being sent to you because I'm drunk and would like to be picked up near the following location:";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);
		data = getContacts(); // creates arraylist for contact data
		ArrayList<String> names = new ArrayList<String>();
		for (ContactData item: data) {
			names.add(item.getName());
		}
		Collections.sort(names);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.textview, names);
		ListView listView = (ListView) findViewById(R.id.listview1);
		listView.setAdapter(adapter);
		// check if GPS is enabled
		if (!gpsEnabled()) {
			Toast.makeText(getBaseContext(), "Please enable GPS", Toast.LENGTH_LONG).show();
			finish();
		}
		// set click listener
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				numberToText = "Invalid";
				TextView textView = (TextView) view;
				String name = textView.getText().toString();
				Toast.makeText(getBaseContext(), name + " has been selected", Toast.LENGTH_SHORT).show();
				// get number of selected contact
				for(ContactData contact: data) {
					if (contact.getName().equalsIgnoreCase(name)) {
						numberToText = contact.getNumber();
						break;
					}
				}
				// check to make sure number matches correct contact, remove after done debugging
				//Toast.makeText(getBaseContext(), numberToText, Toast.LENGTH_SHORT).show();
				// get location
				locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				// I should check if the GPS is enabled here, however I'm leaving that for later
				locationListener = new MyLocationListener();
				String locationProvider = LocationManager.GPS_PROVIDER;
				locationManager.requestLocationUpdates(locationProvider, 2000, 50, locationListener);
				Toast.makeText(getBaseContext(), "Gathering GPS information, this will take a little while...", Toast.LENGTH_LONG).show();
				// pause for 15 seconds, before running rest of code inside handler
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					public void run() {
						Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location == null) {
                            Toast.makeText(getBaseContext(), "GPS either turned off or cannot contact satellites", Toast.LENGTH_LONG).show();
                            finish();
                        }
						String lat = String.valueOf(location.getLatitude());
						String lon = String.valueOf(location.getLongitude());
						// correct order to display values is apparently lat, lon, even though it seems pretty ambiguous
						//String message2 = "https://maps.google.com/maps?z=12&t=m&q=" + lon + "+N,+" + lat + "+W"; // Not the correct way to do is
						String message2 = "https://maps.google.com/maps?z=12&t=m&q=" + lat + "," + lon; 
						SmsManager sms = SmsManager.getDefault();
						sms.sendTextMessage(numberToText, null, message1, null, null);
						sms.sendTextMessage(numberToText, null, message2, null, null);
						Toast.makeText(getBaseContext(), "Message sent!", Toast.LENGTH_SHORT).show();
						finish();
					}
				}, 15000);
				// end of handler code
			}
		});
	}



	public ArrayList<ContactData> getContacts() {
		ArrayList<ContactData> data = new ArrayList<ContactData>(); // array to hold contact data to return to main method
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // dunno
		ContentResolver cr = getContentResolver(); // dunno
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null); // never actually used, dunno
		String[] projection = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER}; // creates projection to gather data from name and number columnns in database
		Cursor names = getContentResolver().query(uri, projection, null, null, null); // dunno
		int indexName = names.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME); // gets column index of name
		int indexNumber = names.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER); // gets column index of number
		names.moveToFirst();
		do {
			String name = names.getString(indexName);
			String number = names.getString(indexNumber);
			data.add(new ContactData(name, number));
		}
		while (names.moveToNext());
		return data;
	}
	
	public boolean gpsEnabled() {
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
}



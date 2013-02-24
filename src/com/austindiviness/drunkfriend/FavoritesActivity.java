package com.austindiviness.drunkfriend;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.SmsManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FavoritesActivity extends Activity {
	public int mainMenuId = 1;
	public int settingsId = Menu.FIRST;
	
	public final int contextMenuId = 2;
	public final int editId = Menu.FIRST;
	
	public String numberToText;
	public String contactName;
	public LocationListener locationListener;
	public LocationManager locationManager;
	public ProgressDialog loading;
	public String message1;
	public ArrayList<ContactData> data;
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
		data = getContacts();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(FavoritesActivity.this);
		for (int i = 0; i < buttonIds.length; ++i) {
			int buttonId = buttonIds[i];
			String buttonPref = buttonPrefNames[i];
			Button tempButton = (Button) findViewById(buttonId);
			String prefName = prefs.getString(buttonPref, "No Contact Set");
			if (prefName.equalsIgnoreCase("None")) {
				tempButton.setText("No Contact Set");
			}
			else {
				tempButton.setText(prefName);
				registerForContextMenu(tempButton);
			}
		}
	}
	
	public void favoriteButtonClick(View v) {
		Button button = (Button) v;
		contactName = (String)  button.getText();
		//Toast.makeText(getBaseContext(), contactName, Toast.LENGTH_SHORT).show();
		// check to make sure the button actually has a contact
		if (contactName.equalsIgnoreCase("No Contact Set")) {
			AlertDialog.Builder noContactSet = new AlertDialog.Builder(FavoritesActivity.this);
			noContactSet.setTitle("No Contact Set");
			noContactSet.setMessage("There is no contact set to this speed dial!");
			noContactSet.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			noContactSet.show();
			return;
		}
		numberToText = "Invalid";
		// insert alert dialog here to make sure the user wants to text the user they picked
		AlertDialog.Builder checkCorrectContact = new AlertDialog.Builder(FavoritesActivity.this);
        checkCorrectContact.setTitle("Confirm Contact");
        checkCorrectContact.setMessage("Do you want to send pick up message to " + contactName + "?");
        checkCorrectContact.setNegativeButton("No", new DialogInterface.OnClickListener() {
        	@Override
            public void onClick(DialogInterface dialog, int which) {
        		// no, close the dialog
                dialog.dismiss();
            }
        });
        checkCorrectContact.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        	@Override
            public void onClick(DialogInterface dialog, int which) {
                // yes, send the message
        		// get number of selected contact
				for(ContactData contact: data) {
					if (contact.getName().equalsIgnoreCase(contactName)) {
						numberToText = contact.getNumber();
						break;
					}
				}
                loading = new ProgressDialog(getBaseContext()).show(FavoritesActivity.this, "Gathering GPS Data", "This may take a little while...", true);
				sendMessage(numberToText);
            }
        });
        checkCorrectContact.show();
	}
	
	public void sendMessage(String contactNumber) {
		// get location
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		String locationProvider = LocationManager.GPS_PROVIDER;
		locationManager.requestLocationUpdates(locationProvider, 2000, 50, locationListener);
		//Toast.makeText(getBaseContext(), "Gathering GPS information, this will take a little while...", Toast.LENGTH_LONG).show();
		// pause for 15 seconds, before running rest of code inside handler
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    //Toast.makeText(getBaseContext(), "GPS either turned off or cannot contact satellites", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder badSignal = new AlertDialog.Builder(FavoritesActivity.this);
                    loading.dismiss();
                    badSignal.setTitle("No Satellite Coverage");
                    badSignal.setMessage("Insufficient satellite information is available. Perhaps try again outside?");
                    badSignal.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    	@Override
                    	public void onClick(DialogInterface dialog, int which) {
                    		// bad GPS signal, turn off location manager and exit
                    		locationManager.removeUpdates(locationListener);
                    		locationManager = null;
                    		finish();
                    	}
                    });
                    badSignal.show();
                }
                else {
					String lat = String.valueOf(location.getLatitude());
					String lon = String.valueOf(location.getLongitude());
					// correct order to display values is apparently lat, lon, even though it seems pretty ambiguous
					String message2 = "https://maps.google.com/maps?z=12&t=m&q=" + lat + "," + lon; 
					SmsManager sms = SmsManager.getDefault();
					// set message1 to the string stored in shared preferences, somewhere
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(FavoritesActivity.this);
					message1 = prefs.getString("message_to_text", "ERROR reading custom message. GPS location as follows:");
					// send messages
					if (message1.length() > 160) {
						int numberOfMessages = message1.length() / 160;
						if ((160 * numberOfMessages) < message1.length()) {
							++numberOfMessages;
						}
						for (int i = 0; i < numberOfMessages - 1; ++i) {
							sms.sendTextMessage(numberToText, null, message1.substring(i * 160, (i + 1) * 160), null, null);
						}
						sms.sendTextMessage(numberToText, null, message1.substring((numberOfMessages - 1) * 160, message1.length()), null, null);
					}
					else {
						sms.sendTextMessage(numberToText, null, message1, null, null);
					}
					sms.sendTextMessage(numberToText, null, message2, null, null);
					AlertDialog.Builder messageSent = new AlertDialog.Builder(FavoritesActivity.this);
					messageSent.setTitle("Success!");
					messageSent.setMessage("Successfully sent SMS message to " + contactName + " regarding your situation and present location.");
					messageSent.setNeutralButton("OK", new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface dialog, int which) {
			                // message sent, turn off location manager and exit
			                locationManager.removeUpdates(locationListener);
			                locationManager = null;
							finish();	
						}
					});
					loading.dismiss();
					messageSent.show();
                }
			}
		}, 15000);
		// end of handler code
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
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		//Toast.makeText(getBaseContext(), "context menu", Toast.LENGTH_SHORT).show();
		menu.add(contextMenuId, editId, 0, "Edit");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case editId:
				Toast.makeText(getBaseContext(), "clicked edit", Toast.LENGTH_SHORT).show();
				
		}
		return true;
	}

	public ArrayList<ContactData> getContacts() {
		ArrayList<ContactData> data = new ArrayList<ContactData>(); // array to hold contact data to return to main method
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // dunno
		String[] projection = new String[] {
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, 
				ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER, 
				ContactsContract.CommonDataKinds.Phone.TYPE,
				ContactsContract.CommonDataKinds.Phone.NUMBER}; 
		String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1";
		Cursor names = getContentResolver().query(uri, projection, selection, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
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
	
	
} // end of class

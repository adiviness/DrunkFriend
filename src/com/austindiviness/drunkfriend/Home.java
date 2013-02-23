package com.austindiviness.drunkfriend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Home extends Activity {
	// The main launcher for the two different portions of the app
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.home_layout);
		
	}

	public void startAllContactsActivity(View v) {
		Toast.makeText(getBaseContext(), "all contacts", Toast.LENGTH_SHORT).show();
		Intent allContactsIntent = new Intent(Home.this, MainActivity.class);
		Home.this.startActivity(allContactsIntent);
	}
	
	public void startFavoritesActivity(View v) {
		Toast.makeText(getBaseContext(), "favorites", Toast.LENGTH_SHORT).show();
	}
}

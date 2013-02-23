package com.austindiviness.drunkfriend;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FavoritesActivity extends Activity {
	boolean[] buttonSet = {false, false, false, false, false, false};
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.favorites_layout);
	}
	
	public void favoriteButtonClick(View v) {
		Button button = (Button) v;
		CharSequence which =  button.getText();
		Toast.makeText(getBaseContext(), which, Toast.LENGTH_SHORT).show();
	}

}

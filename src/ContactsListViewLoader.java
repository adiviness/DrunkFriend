import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;


public class ContactsListViewLoader extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	SimpleCursorAdapter mAdapter;
	static final String[] PROJECTION = new String[] {ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME};
	static final String SELECTION = "((" + ContactsContract.Data.DISPLAY_NAME + " NOTNULL) AND (" + ContactsContract.Data.DISPLAY_NAME + " != '' ))";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// create progress bar to display while list loads
		ProgressBar progressBar = new ProgressBar(this);
		progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));	
		progressBar.setIndeterminate(true);
		getListView().setEmptyView(progressBar);
		// add progress bar to root of layout
		ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
		root.addView(progressBar);
		// specify which columns go into which views, for the cursor adapter
		String[] fromColumns = {ContactsContract.Data.DISPLAY_NAME};
		int[] toViews = {android.R.id.text1}; // the textview in simple_list_item_1
		// create an empty adapter to use to display the loaded data
		mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, fromColumns, toViews, 0);
		setListAdapter(mAdapter);
		// prepare the loader
		getLoaderManager().initLoader(0, null, this);
	}
	
	//called when a new loader needs to be created
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// creates and returns a CursorLoader that will take care of creating a Cursor for the data being displayed
		return new CursorLoader(this, ContactsContract.Data.CONTENT_URI, PROJECTION, SELECTION, null, null);	
	}	
	
	// called when a previously created loader has finished loading
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		//swap the new cursor in
		mAdapter.swapCursor(data);
	}
	
	// called when a previously created loader is reset, making the data unavailable
	public void onLoaderReset(Loader<Cursor> loader) {
		// this is called when the last Cursor provided to onLoadFinished() above is about to be closed.
		// this ensures that we are no longer using it
		mAdapter.swapCursor(null);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// do something when a list item is clicked
	}
}













package com.tolstoff.orthodoxyicons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements
		ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";	
	final List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	Map<String, Object> map;
	YandexApiHelper yandexApiHelper;
	
	ArrayList<String> titile = new ArrayList<String>();
	ArrayList<String> preview = new ArrayList<String>();
	
	
	DBHelper dbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		actionBarInit();
         
		yandexApiHelper =YandexApiHelper.getInstance();
		yandexApiHelper.setContex(this);
		

		LogT.log("Соединение: " + isNetWorkAvailable());

//		yandexFotkiApi.execute();

	

	}


	

	// public void ApiProcessinDone() {
	//
	// titile = yandexFotkiApi.getPreviewImageURL();
	// preview = yandexFotkiApi.getPreviewImageURL();
	// LogT.log("Размер:" + titile.size());
	//
	// for (String i : titile) {
	//
	// LogT.log(i);
	//
	// }
	//
	//
	// }

	private void actionBarInit() {
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

//		 map = new HashMap<String, Object>();
//		 map.put("title",
//		 getResources().getString(R.string.title_gallery_view));
//		 map.put("fragment",
//		 Fragment.instantiate(this, FragmentGallery.class.getName()));
//		 data.add(map);

//		map = new HashMap<String, Object>();
//		map.put("title", getResources().getString(R.string.title_list_view));
//		map.put("fragment",
//				Fragment.instantiate(this, FragmentList.class.getName()));
//		data.add(map);
//
//		map = new HashMap<String, Object>();
//		map.put("title", getResources().getString(R.string.title_detail_view));
//		map.put("fragment",
//				Fragment.instantiate(this, FragmentDetailView.class.getName()));
//		data.add(map);

		SimpleAdapter adapter = new SimpleAdapter(this, data,
				android.R.layout.simple_spinner_dropdown_item,
				new String[] { "title" }, new int[] { android.R.id.text1 });

		actionBar.setListNavigationCallbacks(adapter, this);

	}

	
	
	
	
		
	/**
	 * Backward-compatible version of {@link ActionBar#getThemedContext()} that
	 * simply returns the {@link android.app.Activity} if
	 * <code>getThemedContext</code> is unavailable.
	 */

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private Context getActionBarThemedContextCompat() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return getActionBar().getThemedContext();
		} else {
			return this;
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		map = new HashMap<String, Object>();

		map = data.get(position);
		Object o = map.get("fragment");
		if (o instanceof Fragment) {
			FragmentTransaction tx = getFragmentManager().beginTransaction();
			tx.replace(android.R.id.content, (Fragment) o);
			tx.commit();
		}
		LogT.log("postion: " + position + "; id: " + id);
		return true;

	}

	public boolean isNetWorkAvailable() {
		boolean available = false;
		ConnectivityManager myConnMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = myConnMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			available = true;
		}

		return available;

	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

}

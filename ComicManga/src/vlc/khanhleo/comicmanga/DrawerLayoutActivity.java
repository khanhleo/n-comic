/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vlc.khanhleo.comicmanga;

import java.util.ArrayList;
import java.util.List;

import vlc.khanhle.comicmanga.R;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public abstract class DrawerLayoutActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawer;
	// private TextView mContent;

	public ActionBarHelper mActionBar;

	private ActionBarDrawerToggle mDrawerToggle;

	private MenuApdapter adapter;

	public static List<String> listMenu;
	public static int SEC_NUM;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupView();
		// setContentView(R.layout.drawer_layout);

		// style for listview

		// final ListView listView = (ListView) findViewById(R.id.lvSummary);

		// listMenu = new ArrayList<String>();
		// listMenu.add("Setting");
		// listMenu.add("About");
		// listMenu.add("Exit");

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// mContent = (TextView) findViewById(R.id.content_text);
		mDrawerLayout.setDrawerListener(new DemoDrawerListener());
//		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
//				GravityCompat.START);

		// The drawer title must be set in order to announce state changes when
		// accessibility is turned on. This is typically a simple description,
		// e.g. "Navigation".
		mDrawerLayout.setDrawerTitle(GravityCompat.START,
				getString(R.string.drawer_title)); 

		mDrawer = (ListView) findViewById(R.id.start_drawer);
		adapter = new MenuApdapter(this);
		mDrawer.setAdapter(adapter);
		adapter.setListContact(listMenu);
		adapter.notifyDataSetChanged();
		// Utils.setListViewHeightBasedOnChildren(mDrawer);

		// mDrawer.setAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1,
		// Shakespeare.TITLES));
		mDrawer.setOnItemClickListener(new DrawerItemClickListener());

		mActionBar = createActionBarHelper();
		mActionBar.init();

		// ActionBarDrawerToggle provides convenient helpers for tying together
		// the
		// prescribed interactions between a top-level sliding drawer and the
		// action bar.
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close);
	}

	@SuppressLint("ClickableViewAccessibility")
	private class MenuApdapter extends BaseAdapter {
		private Context mContext;
		private List<String> listMenu = new ArrayList<String>();

		public MenuApdapter(Context c) {
			mContext = c;
		}

		@Override
		public String getItem(int position) {
			return listMenu.get(position);
		}

		@Override
		public int getCount() {
			return listMenu.size();
		}

		public void setListContact(List<String> summary) {
			List<String> result = new ArrayList<String>();
			for (String item : summary) {
				result.add(item);
			}
			listMenu = result;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final String summaryItem = getItem(position);
			View v = convertView;
			if (v == null) {
				v = LayoutInflater.from(mContext).inflate(
						R.layout.listview_summary_item, null);
			}

			final TextView itemText = (TextView) v.findViewById(R.id.itemText);
			final ImageView itemIcon = (ImageView) v
					.findViewById(R.id.itemImage);

			itemText.setText(summaryItem);
			if (itemText.getText().toString().equals(getString(R.string.about))) {
				itemIcon.setImageResource(R.drawable.ico_about);
			} else if (itemText.getText().toString()
					.equals(getString(R.string.exit))
					|| itemText.getText().toString()
							.equals(getString(R.string.back))) {
				itemIcon.setImageResource(R.drawable.ico_exit);
			}else if (itemText.getText().toString()
					.equals(getString(R.string.rate_app))) {
				itemIcon.setImageResource(R.drawable.ico_rate);
			} else if (itemText.getText().toString()
					.equals(getString(R.string.share_app))) {
				itemIcon.setImageResource(R.drawable.ico_share);
			} else {
//				if (position == 1)
//					itemIcon.setImageResource(R.drawable.ico_sec);
//				else {
//					itemIcon.setVisibility(View.GONE);
//				}
			}
			// itemText.setOnTouchListener(new View.OnTouchListener() {
			// @Override
			// public boolean onTouch(View v, MotionEvent event) {
			// Toast.makeText(getApplicationContext(),
			// itemText.getText().toString(), Toast.LENGTH_SHORT).show();
			// return false;
			// }
			// });
			itemText.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (itemText.getText().toString()
							.equals(getString(R.string.about))) {
						
						mDrawerLayout.closeDrawer(mDrawer);
					} else if (itemText.getText().toString()
							.equals(getString(R.string.search))) {
						searchContents();
						mDrawerLayout.closeDrawer(mDrawer);
					} else if (itemText.getText().toString()
							.equals(getString(R.string.exit))
							|| itemText.getText().toString()
									.equals(getString(R.string.back))) {
						finish();
					} else if (itemText.getText().toString()
							.equals(getString(R.string.rate_app))) {
						// rate app
						/* This code assumes you are inside an activity */
						final Uri uri = Uri.parse("market://details?id="
								+ getApplicationContext().getPackageName());
						final Intent rateAppIntent = new Intent(
								Intent.ACTION_VIEW, uri);

						if (getPackageManager().queryIntentActivities(
								rateAppIntent, 0).size() > 0) {
							startActivity(rateAppIntent);
						} else {
							/*
							 * handle your error case: the device has no way to
							 * handle market urls
							 */
							Toast.makeText(getApplicationContext(),
									"App is not on google play",
									Toast.LENGTH_SHORT).show();
						}
						mDrawerLayout.closeDrawer(mDrawer);
					} else if (itemText.getText().toString()
							.equals(getString(R.string.share_app))) {
						
						Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//						Uri screenshotUri = Uri.parse(path);
						final Uri screenshotUri = Uri.parse("market://details?id="
								+ getApplicationContext().getPackageName());

						sharingIntent.setType("plain/text");
						sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
						startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_title)));
						
						mDrawerLayout.closeDrawer(mDrawer);
					} 
				}
			});

			return v;
		}

		@Override
		public long getItemId(int paramInt) {
			return 0;
		}
	}

	abstract void searchContents();

	protected void setupView() {
		listMenu = new ArrayList<String>();
		listMenu.add(getString(R.string.refer));
		listMenu.add(getString(R.string.about));
		listMenu.add(getString(R.string.rate_app));
		listMenu.add(getString(R.string.share_app));
		listMenu.add(getString(R.string.exit));
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * The action bar home/up action should open or close the drawer.
		 * mDrawerToggle will take care of this.
		 */
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * This list item click listener implements very simple view switching by
	 * changing the primary content text. The drawer is closed when a selection
	 * is made.
	 */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// mContent.setText(Shakespeare.DIALOGUE[position]);
			mActionBar.setTitle(listMenu.get(position));
			mDrawerLayout.closeDrawer(mDrawer);
			Toast.makeText(getApplicationContext(), "abc", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * A drawer listener can be used to respond to drawer events such as
	 * becoming fully opened or closed. You should always prefer to perform
	 * expensive operations such as drastic relayout when no animation is
	 * currently in progress, either before or after the drawer animates.
	 * 
	 * When using ActionBarDrawerToggle, all DrawerLayout listener methods
	 * should be forwarded if the ActionBarDrawerToggle is not used as the
	 * DrawerLayout listener directly.
	 */
	private class DemoDrawerListener implements DrawerLayout.DrawerListener {
		@Override
		public void onDrawerOpened(View drawerView) {
			mDrawerToggle.onDrawerOpened(drawerView);
			mActionBar.onDrawerOpened();
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			mDrawerToggle.onDrawerClosed(drawerView);
			mActionBar.onDrawerClosed();
		}

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
		}

		@Override
		public void onDrawerStateChanged(int newState) {
			mDrawerToggle.onDrawerStateChanged(newState);
		}
	}

	/**
	 * Create a compatible helper that will manipulate the action bar if
	 * available.
	 */
	private ActionBarHelper createActionBarHelper() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return new ActionBarHelperICS();
		} else {
			return new ActionBarHelper();
		}
	}

	public static String mStrTitle;

	public void setTitle(String title) {
		mActionBar.setTitle(title);
	}

	/**
	 * Stub action bar helper; this does nothing.c
	 */
	private class ActionBarHelper {
		public void init() {
		}

		public void onDrawerClosed() {
		}

		public void onDrawerOpened() {
		}

		public void setTitle(CharSequence title) {
		}
	}

	/**
	 * Action bar helper for use on ICS and newer devices.
	 */
	private class ActionBarHelperICS extends ActionBarHelper {
		private final ActionBar mActionBar;
		private CharSequence mDrawerTitle;
		private CharSequence mTitle;

		@SuppressLint("NewApi")
		ActionBarHelperICS() {
			mActionBar = getActionBar();
		}

		@Override
		public void init() {
			mActionBar.setDisplayHomeAsUpEnabled(true);
			mActionBar.setHomeButtonEnabled(true);
			mTitle = mDrawerTitle = mStrTitle;
			mActionBar.setTitle(mTitle);
//			mActionBar.setBackgroundDrawable(getResources().getDrawable(
//					R.drawable.orange));
		}

		/**
		 * When the drawer is closed we restore the action bar state reflecting
		 * the specific contents in view.
		 */
		@Override
		public void onDrawerClosed() {
			super.onDrawerClosed();
			mActionBar.setTitle(mTitle);
		}

		/**
		 * When the drawer is open we set the action bar to a generic title. The
		 * action bar should only contain data relevant at the top level of the
		 * nav hierarchy represented by the drawer, as the rest of your content
		 * will be dimmed down and non-interactive.
		 */
		@Override
		public void onDrawerOpened() {
			super.onDrawerOpened();
			mActionBar.setTitle(mDrawerTitle);
		}

		@Override
		public void setTitle(CharSequence title) {
			mTitle = title;
		}
	}
}

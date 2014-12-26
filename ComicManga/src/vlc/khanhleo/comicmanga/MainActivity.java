package vlc.khanhleo.comicmanga;

import java.util.ArrayList;

import vlc.khanhle.comicmanga.R;
import vlc.khanhleo.comicmanga.adapter.TestAdapter;
import vlc.khanhleo.comicmanga.adapter.VolAdapter;
import vlc.khanhleo.comicmanga.data.VolListDao;
import vlc.khanhleo.comicmanga.object.VolItem;
import vlc.khanhleo.comicmanga.utils.Consts;
import vlc.khanhleo.comicmanga.utils.GetVolApi;
import android.view.View;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends DrawerLayoutActivity {

	private GridView mGvVol;
	private ArrayList<VolItem> listItem;
	private int[] mDrawableItem = new int[] { R.drawable.vol1, R.drawable.vol2,
			R.drawable.vol3, R.drawable.vol4, R.drawable.vol5, R.drawable.vol6,
			R.drawable.vol7, R.drawable.vol8, R.drawable.vol9,
			R.drawable.vol10, R.drawable.vol11, R.drawable.vol12,
			R.drawable.vol13, R.drawable.vol14, R.drawable.vol15,
			R.drawable.vol16, R.drawable.vol17, R.drawable.vol18,
			R.drawable.vol19, R.drawable.vol20, R.drawable.vol21,
			R.drawable.vol22, R.drawable.vol23, R.drawable.vol24,
			R.drawable.vol25, R.drawable.vol26, R.drawable.vol27,
			R.drawable.vol28, R.drawable.vol29, R.drawable.vol30,
			R.drawable.vol31, R.drawable.vol32, R.drawable.vol33,
			R.drawable.vol34 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		init();
//		mGvVol = (GridView) findViewById(R.id.gvVol);
//		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//			mGvVol.setNumColumns(5);
//		} else {
//			mGvVol.setNumColumns(3);
//		}
//		VolAdapter va = new VolAdapter(getApplicationContext(), listItem);
//		mGvVol.setAdapter(va);
//		// mGvVol.setAdapter(new VolAdapter(this));
//		mGvVol.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> parent, View v,
//					int position, long id) {
//				String mVol = "";
//				if (position < 10) {
//					mVol = "vol0" + String.valueOf(position + 1);
//				} else {
//					mVol = "vol" + String.valueOf(position + 1);
//				}
//				Bundle bundle = new Bundle();
//				bundle.putString(Consts.VOL, mVol);
//				// After all data has been entered and calculated, go to new
//				// page for results
//				Intent myIntent = new Intent();
//				myIntent.putExtras(bundle);
//				myIntent.setClass(getBaseContext(), ChapActivity.class);
//				startActivity(myIntent);
//
//			}
//		});

		// GridView gridview = (GridView) findViewById(R.id.gvVol);
		// gridview.setAdapter(new TestAdapter(this));
	}

	@Override
	protected void onResume() {
		init();
		super.onResume();
	}
	private void init() {
		listItem = new ArrayList<VolItem>();
		VolListDao mVolListDao = new VolListDao(getApplicationContext());
		if (Consts.getIsFirstUse(getApplication())) {
			int count = 1;
			String strCount = "01";
			for (int item : mDrawableItem) {
				if(count<10)
					strCount="0"+String.valueOf(count);
				else
					strCount=String.valueOf(count);
				VolItem volItem = new VolItem();
				volItem.setmDrawbaleitem(item);
				volItem.setmId(strCount);
				volItem.setmIsDownload("false");
				volItem.setmIsNew("false");
				count++;
				mVolListDao.insertRow(volItem);
				// listItem.add(volItem);
			}
			Consts.setIsFirstUse(getApplicationContext(), false);
		}
		listItem = (ArrayList<VolItem>) mVolListDao.selectAll();
		mVolListDao.close();
		
		mGvVol = (GridView) findViewById(R.id.gvVol);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			mGvVol.setNumColumns(5);
		} else {
			mGvVol.setNumColumns(3);
		}
		VolAdapter va = new VolAdapter(getApplicationContext(), listItem);
		mGvVol.setAdapter(va);
		// mGvVol.setAdapter(new VolAdapter(this));
		mGvVol.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				String mVol = "";
				if (position < 10) {
					mVol = "vol0" + String.valueOf(position + 1);
				} else {
					mVol = "vol" + String.valueOf(position + 1);
				}
				Bundle bundle = new Bundle();
				bundle.putString(Consts.VOL, mVol);
				// After all data has been entered and calculated, go to new
				// page for results
				Intent myIntent = new Intent();
				myIntent.putExtras(bundle);
				myIntent.setClass(getBaseContext(), ChapActivity.class);
				startActivity(myIntent);

			}
		});

	}

	@Override
	protected void setupView() {
		setContentView(R.layout.activity_main);
		mStrTitle = (String) getTitle();
		super.setupView();
	}

	@Override
	void getVolCount() {
		if (Consts.isNetworkOnline(getApplication()))
			new GetVolApi(this).execute(Consts.URL_GET_VOL_NUMBER_API);
		else
			Toast.makeText(getApplicationContext(),
					getString(R.string.network_unconnect), Toast.LENGTH_LONG)
					.show();
	}

	@Override
	void appInfor() {
		Consts.showAppInforDialog(this);
	}
}

package vlc.khanhleo.comicmanga;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import vlc.khanhle.comicmanga.R;
import vlc.khanhleo.comicmanga.object.DownloadItem;
import vlc.khanhleo.comicmanga.utils.Consts;
import vlc.khanhleo.comicmanga.utils.DownloadFile;
import vlc.khanhleo.comicmanga.utils.HandleApi;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ChapActivity extends DrawerLayoutActivity {

	private ArrayList<Integer> mListChaps = new ArrayList<Integer>();
	private String[] mArrayChaps = new String[] { "1", "2", "3" };
	private boolean[] isDownloading = new boolean[] { false, false, false };
	private boolean[] isHasDownloaded = new boolean[] { false, false, false };
	private ChapArrayAdapter adapter;
	private String mVol, mChap;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mVol = extras.getString(Consts.VOL);
		}
		listView = (ListView) findViewById(R.id.lv_chap);

		// adapter = new SummaryAdapter(this);
		// listView.setAdapter(adapter);
		// adapter.setListContact(mListChaps);
		for (int i = 1; i < 4; i++) {
			mListChaps.add(i);
		}
		adapter = new ChapArrayAdapter(this, mArrayChaps);
		listView.setAdapter(adapter);
		// adapter.setListItem(mListChaps);
		// adapter.notifyDataSetChanged();
		listView.setDivider(null);
		listView.setDividerHeight(0);

	}

	@Override
	void searchContents() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setupView() {
		setContentView(R.layout.activity_chap);
		mStrTitle = (String) getTitle();
		super.setupView();
	}

	// handle to download'
	private void downloadFile(int positon, ProgressBar mPr, TextView mTv,
			DownloadItem di,LinearLayout mDownload) {
		isDownloading[positon] = true;
		DownloadFile df = new DownloadFile(mPr, mTv, this,positon,mDownload);
		df.execute(di);
	}

	// check api to download
	private void checkAPI(String url, ProgressBar mPr,TextView mTv, int mPosition, LinearLayout mDownload) {
		new HandleApi(this,mPr,mTv,mPosition,mDownload).execute(url);
	}
	// call back
	public void resultData(DownloadItem result,int positon, ProgressBar mPr, TextView mTv, LinearLayout mDownload){
		if (result != null) {
			if (!result.getmUrl().equals(Consts.URL+"null")) {
				// will start download file
				result.setmFolderName(mVol+"/"+mChap);
				mDownload.setVisibility(View.VISIBLE);
				downloadFile(positon, mPr, mTv, result,mDownload);
				Toast.makeText(
						this,
						result.getmUrl(),
						Toast.LENGTH_LONG).show();
				Log.d("url", result.getmUrl());
				Log.d("folder_name", result.getmFolderName());
				Log.d("file_name", result.getmFileName());
			} else {
				new AlertDialog.Builder(this)
						.setMessage(R.string.vol_not_exit)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is
										// clicked, close
										dialog.cancel();
									}
								}).show();
			}
		}else{
			Toast.makeText(
					this,
					getString(R.string.network_error),
					Toast.LENGTH_SHORT).show();
		}
	}
	
	// call back when download finish
	public void downloadFinish(int mPosition, LinearLayout mDownload){
		isDownloading[mPosition]=false;
		mDownload.setVisibility(View.GONE);
		
	}
	
	@SuppressLint("ViewHolder")
	public class ChapArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final String[] values;

		public ChapArrayAdapter(Context context, String[] values) {
			super(context, R.layout.listview_chap_item, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View v = inflater.inflate(R.layout.listview_chap_item, parent,
					false);
			// TextView textView = (TextView) rowView.findViewById(R.id.label);
			// ImageView imageView = (ImageView)
			// rowView.findViewById(R.id.logo);
			// textView.setText(values[position]);
			final int ps = position;

			ImageView icon = (ImageView) v.findViewById(R.id.list_item_icon);
			TextView title = (TextView) v
					.findViewById(R.id.list_item_chap_title);
			switch (position) {
			case 0:
				icon.setImageResource(R.drawable.ico_one_star);
				title.setText("Chap 1");
				break;
			case 1:
				icon.setImageResource(R.drawable.ico_two_star);
				title.setText("Chap 2");
				break;
			case 2:
				icon.setImageResource(R.drawable.ico_three_star);
				title.setText("Chap 3");
				break;
			}

			final LinearLayout llDownload = (LinearLayout) v
					.findViewById(R.id.list_ll_download);
			final ProgressBar pr = (ProgressBar) v
					.findViewById(R.id.list_progress_item);
			final TextView tv = (TextView) v
					.findViewById(R.id.list_percent_item);
			final LinearLayout click = (LinearLayout) v
					.findViewById(R.id.list_item_ll_click);
			click.setOnTouchListener(new View.OnTouchListener() {
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						click.setBackgroundResource(R.color.light_gray);
						
						// get list item of folder
						//Consts.getSdCardPath() +item[0].getmFolderName() mVol+"/"+mChap
						mChap = "chap" + String.valueOf(ps + 1);
						int mNumberItem =0;
						File dir = new File(Consts.getSdCardPath()
				                + mVol+"/"+mChap);
				        if (dir.exists() != false) {
				        	if(dir.listFiles().length>2)
				        		mNumberItem=dir.listFiles().length;
				        		isHasDownloaded[ps]=true;
						}
						if (isHasDownloaded[ps]) {
							// handle to read
							ArrayList<String> listExtras = new ArrayList<String>();
							listExtras.add(mVol);
							listExtras.add(mChap);
							listExtras.add(String.valueOf(mNumberItem));
							Bundle bundle = new Bundle();
//							bundle.putString(Consts.VOL,
//									mVol);
//							bundle.putInt(Consts.NUMBER_ITEM, mNumberItem);
//							bundle.putString(Consts.CHAP, mChap);
							bundle.putStringArrayList(Consts.NUMBER_ITEM, listExtras);
							// After all data has been entered and calculated, go to new
							// page for results
							Intent myIntent = new Intent();
							myIntent.putExtras(bundle);
							myIntent.setClass(getBaseContext(), FragmentPagerActivity.class);
							startActivity(myIntent);
							return true;
						} else {
							if (isDownloading[ps]) {
								// still download
							} else {
								// check API is it has exit or not
								String url_check_api = Consts.URL_CHECK_API;
								
								String name = mVol + "_" + mChap;
								url_check_api += name;
								checkAPI(url_check_api,pr,tv,ps,llDownload);
							}
						}
					}else if (event.getAction() == MotionEvent.ACTION_CANCEL||event.getAction() == MotionEvent.ACTION_UP) {
						click.setBackgroundResource(R.color.white);
					}
					return true;
				}
			});

			return v;
		}
	}

}

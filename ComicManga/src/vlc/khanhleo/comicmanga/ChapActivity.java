package vlc.khanhleo.comicmanga;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import vlc.khanhle.comicsongoku.R;
import vlc.khanhleo.comicmanga.data.VolListDao;
import vlc.khanhleo.comicmanga.object.DownloadItem;
import vlc.khanhleo.comicmanga.utils.Consts;
import vlc.khanhleo.comicmanga.utils.DownloadFile;
import vlc.khanhleo.comicmanga.utils.GetVolApi;
import vlc.khanhleo.comicmanga.utils.HandleApi;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
	private String[] mArrayChaps = new String[] { "1", "2" };
	private boolean[] isDownloading = new boolean[] { false, false, false };
	private boolean[] isHasDownloaded = new boolean[] { false, false, false };
	private HandleApi[] atChekDownload = new HandleApi[3];
	private DownloadFile[] atDownloadFile = new DownloadFile[3];
	private ChapArrayAdapter adapter;
	private String mVol, mChap;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
	protected void setupView() {
		setContentView(R.layout.activity_chap);
		String strVol = "";
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mVol = extras.getString(Consts.VOL);
			strVol = mVol.substring(3);
		}

		mStrTitle = getString(R.string.vol_number, strVol);
		super.setupView();
	}

	// handle to download'
	private void downloadFile(int positon, ProgressBar mPr, TextView mTv,
			DownloadItem di, LinearLayout mDownload) {
		isDownloading[positon] = true;
		DownloadFile df = new DownloadFile(mPr, mTv, this, positon, mDownload);
		df.execute(di);
		atDownloadFile[positon] = df;
	}

	// check api to download
	private void checkAPI(String url, ProgressBar mPr, TextView mTv,
			int mPosition, LinearLayout mDownload, ProgressBar mPrCheckApi) {
		HandleApi abc = (HandleApi) new HandleApi(this, mPr, mTv, mPosition,
				mDownload, mPrCheckApi).execute(url);
		atChekDownload[mPosition] = abc;
	}

	// call back
	public void resultData(DownloadItem result, int positon, ProgressBar mPr,
			TextView mTv, LinearLayout mDownload) {
		if (result != null && result.getmUrl() != null) {
			if (!result.getmUrl().equals(Consts.URL + "null")) {
				// will start download file
				result.setmFolderName(mVol + "/" + mChap);
				mDownload.setVisibility(View.VISIBLE);
				downloadFile(positon, mPr, mTv, result, mDownload);
				// Toast.makeText(this, result.getmUrl(), Toast.LENGTH_LONG)
				// .show();
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
		} else {
			Toast.makeText(this, getString(R.string.network_error),
					Toast.LENGTH_SHORT).show();
		}
	}

	// call back when download finish
	public void downloadFinish(int mPosition, LinearLayout mDownload,
			boolean isGood) {
		isDownloading[mPosition] = false;
		mDownload.setVisibility(View.GONE);

		// save to data base that have download
		if (isGood) {
			String strVol = mVol.substring(3);
			VolListDao mVolListDao = new VolListDao(getApplicationContext());
			mVolListDao.updateRowIsDownload("true", strVol);
			mVolListDao.close();
		}
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

			final ProgressBar prCheckApi = (ProgressBar) v
					.findViewById(R.id.list_progress_check_api_item);
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
						// Consts.getSdCardPath() +item[0].getmFolderName()
						// mVol+"/"+mChap
						mChap = "chap" + String.valueOf(ps + 1);
						int mNumberItem = 0;
						File dir = new File(Consts.getSdCardPath() + mVol + "/"
								+ mChap);
						if (dir.exists() != false) {
							if (dir.listFiles().length > 2) {
								mNumberItem = dir.listFiles().length;
								isHasDownloaded[ps] = true;
							}
						}
						if (isHasDownloaded[ps]) {
							// handle to read
							ArrayList<String> listExtras = new ArrayList<String>();
							listExtras.add(mVol);
							listExtras.add(mChap);
							listExtras.add(String.valueOf(mNumberItem));

							// save to data base that have read
							String strVol = mVol.substring(3);
							VolListDao mVolListDao = new VolListDao(
									getApplicationContext());
							mVolListDao.updateRowIsNew("true", strVol);
							mVolListDao.close();

							Bundle bundle = new Bundle();
							// bundle.putString(Consts.VOL,
							// mVol);
							// bundle.putInt(Consts.NUMBER_ITEM, mNumberItem);
							// bundle.putString(Consts.CHAP, mChap);
							bundle.putStringArrayList(Consts.NUMBER_ITEM,
									listExtras);
							// After all data has been entered and calculated,
							// go to new
							// page for results
							Intent myIntent = new Intent();
							myIntent.putExtras(bundle);
							myIntent.setClass(getBaseContext(),
									FragmentPagerActivity.class);
							startActivity(myIntent);

							return true;
						} else {
							if (isDownloading[ps]) {
								// still download
							} else {
								// check network has connected or not
								if (Consts
										.isNetworkOnline(getApplicationContext())) {
									// check API is it has exit or not
									String url_check_api = Consts.URL_CHECK_API;

									String name = mVol + "_" + mChap;
									url_check_api += name;
									checkAPI(url_check_api, pr, tv, ps,
											llDownload, prCheckApi);
								} else {
									Toast.makeText(
											getApplicationContext(),
											getString(R.string.network_unconnect),
											Toast.LENGTH_LONG).show();
								}
							}
						}
					} else if (event.getAction() == MotionEvent.ACTION_CANCEL
							|| event.getAction() == MotionEvent.ACTION_UP) {
						click.setBackgroundResource(R.color.white);
					}
					return true;
				}
			});

			final ImageView cancel = (ImageView) v
					.findViewById(R.id.list_cancel_item);
			cancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (atDownloadFile[ps] != null
							&& atDownloadFile[ps].getStatus().equals(
									AsyncTask.Status.RUNNING)) {
						atDownloadFile[ps].cancel(true);
						isDownloading[ps] = false;
						llDownload.setVisibility(View.GONE);
					}
				}
			});

			return v;
		}
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

	@Override
	public void onBackPressed() {
		boolean flag = false;
		for (int i = 0; i < 3; i++) {
			if (isDownloading[i]) {
				flag = true;
				break;
			}
		}
		if (flag) {
			new AlertDialog.Builder(this)
					.setMessage(R.string.cancel_all_thread)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									for (int i = 0; i < 3; i++) {
										if (atChekDownload[i] != null
												&& atChekDownload[i]
														.getStatus()
														.equals(AsyncTask.Status.RUNNING)) {
											atChekDownload[i].cancel(true);
										}
										if (atDownloadFile[i] != null
												&& atDownloadFile[i]
														.getStatus()
														.equals(AsyncTask.Status.RUNNING)) {
											atDownloadFile[i].cancel(true);
										}
									}

									dialog.cancel();
									finish();
								}
							})
					.setNegativeButton(getString(R.string.cancel),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).show();
		} else {
			finish();
		}

	}

}

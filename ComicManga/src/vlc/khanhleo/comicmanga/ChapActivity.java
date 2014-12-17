package vlc.khanhleo.comicmanga;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import vlc.khanhle.comicmanga.R;
import vlc.khanhleo.comicmanga.object.DownloadItem;
import vlc.khanhleo.comicmanga.utils.Consts;
import vlc.khanhleo.comicmanga.utils.DownloadFile;
import vlc.khanhleo.comicmanga.utils.HandleApi;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ChapActivity extends DrawerLayoutActivity {

	private ArrayList<Integer> mListChaps = new ArrayList<Integer>();
	private String[] mArrayChaps = new String[]{"1","2","3"};
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
		
//		adapter = new SummaryAdapter(this);
//		listView.setAdapter(adapter);
//		adapter.setListContact(mListChaps);
		for(int i=1;i<4;i++){
			mListChaps.add(i);
		}
		adapter = new ChapArrayAdapter(this,mArrayChaps);
		listView.setAdapter(adapter);
//		adapter.setListItem(mListChaps);
//		adapter.notifyDataSetChanged();
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
			DownloadItem di) {
		isDownloading[positon] = true;
		DownloadFile df = new DownloadFile(mPr, mTv, this);
		df.execute(di);
	}

	// check api to download
	private DownloadItem checkAPI(String url) {
		DownloadItem di = new DownloadItem();
		try {
			di = new HandleApi().execute(url).get();
			return di;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return null;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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
	 
			View v = inflater.inflate(R.layout.listview_chap_item, parent, false);
//			TextView textView = (TextView) rowView.findViewById(R.id.label);
//			ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
//			textView.setText(values[position]);
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
			LinearLayout click = (LinearLayout) v
					.findViewById(R.id.list_item_ll_click);
			click.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (isHasDownloaded[ps]) {
						// handle to read
					} else {
						if (isDownloading[ps]) {
							// still download
						} else {
							// check API is it has exit or not
							DownloadItem di = new DownloadItem();
							String url_check_api = Consts.URL_CHECK_API;
							mChap="chap"+String.valueOf(ps+1);
							String name = mVol + "_" + mChap;
							url_check_api += name;
							di = checkAPI(url_check_api);
							if (di!=null&&!di.getmFileName().equals("null")) {
								Toast.makeText(
										getApplication(),
										di.getmFileName() + ";"
												+ di.getmFolderName() + ";"
												+ di.getmUrl(),
										Toast.LENGTH_SHORT).show();
								Log.d("result",di.getmFileName() + ";"
										+ di.getmFolderName() + ";"
										+ di.getmUrl() );
//								llDownload.setVisibility(View.VISIBLE);
//								mChap = String.valueOf(position + 1);
//
//								downloadFile(position, pr, tv, di);
							} else {
								new AlertDialog.Builder(ChapActivity.this)
										.setMessage(R.string.vol_not_exit)
										.setPositiveButton(
												"OK",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id) {
														// if this button is
														// clicked, close
														dialog.cancel();
													}
												}).show();

							}
						}
					}
				}
			});
	 
			return v;
		}
	}
	public class ChapAdapter extends BaseAdapter {
		private Context context;
		private ArrayList<Integer> itemChap = new ArrayList<Integer>() ;
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemChap.size();
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return itemChap.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return itemChap.get(position);
		}

		

		public ChapAdapter(Context context, ArrayList<Integer> itemChap) {
			this.context = context;
//			this.itemChap = new ArrayList<Integer>();
//			this.itemChap = itemChap;

		}
		public void setListItem(ArrayList<Integer> listItem) {
			this.itemChap = new ArrayList<Integer>();
			for (Integer item : listItem) {
				itemChap.add(item);
			} 
		}
		public ChapAdapter(Context context) {
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView,
				ViewGroup parent) {
			final int ps = position;
			View v = convertView;
			if (v == null) {
				v = LayoutInflater.from(context).inflate(
						R.layout.listview_chap_item, null);
			}

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
			LinearLayout click = (LinearLayout) v
					.findViewById(R.id.list_item_ll_click);
			click.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (isHasDownloaded[ps]) {
						// handle to read
					} else {
						if (isDownloading[ps]) {
							// still download
						} else {
							// check API is it has exit or not
							DownloadItem di = new DownloadItem();
							String url_check_api = Consts.URL_CHECK_API;
							String name = mVol + "_" + mChap;
							url_check_api += name;
							di = checkAPI(url_check_api);
							if (!di.getmFileName().equals("null")) {
								Toast.makeText(
										getApplication(),
										di.getmFileName() + ";"
												+ di.getmFolderName() + ";"
												+ di.getmUrl(),
										Toast.LENGTH_SHORT).show();
//								llDownload.setVisibility(View.VISIBLE);
//								mChap = String.valueOf(position + 1);
//
//								downloadFile(position, pr, tv, di);
							} else {
								new AlertDialog.Builder(getApplication())
										.setMessage(R.string.vol_not_exit)
										.setPositiveButton(
												"OK",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id) {
														// if this button is
														// clicked, close
														dialog.cancel();
													}
												}).show();

							}
						}
					}
				}
			});

			return v;
		}

	}
}

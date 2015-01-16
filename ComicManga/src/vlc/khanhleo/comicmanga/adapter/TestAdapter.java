package vlc.khanhleo.comicmanga.adapter;

import vlc.khanhle.comicsongoku.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class TestAdapter extends BaseAdapter {

	private Context mContext;

	// Constructor
	public TestAdapter(Context c) {
		mContext = c;
	}

	public int getCount() {
		return mThumbIds.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(130, 200));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (ImageView) convertView;
		}

		imageView.setImageResource(mThumbIds[position]);
		return imageView;
	}

	// Keep all Images in array
	public Integer[] mThumbIds = { R.drawable.vol1, R.drawable.vol2,
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

}

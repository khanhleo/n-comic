package vlc.khanhleo.comicmanga.adapter;

import java.util.ArrayList;

import vlc.khanhle.comicmanga.R;
import vlc.khanhleo.comicmanga.object.VolItem;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class VolAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private int[] mDrawableItem = new int[] { R.drawable.vol1, R.drawable.vol2,
			R.drawable.vol3, R.drawable.vol4, R.drawable.vol5, R.drawable.vol6,
			R.drawable.vol7, R.drawable.vol8, R.drawable.vol9,
			R.drawable.vol10, R.drawable.vol11, R.drawable.vol12,
			R.drawable.vol13, R.drawable.vol14, R.drawable.vol15,
			R.drawable.vol16, R.drawable.vol17, R.drawable.vol18,
			R.drawable.vol19, R.drawable.vol20, R.drawable.vol21,
			R.drawable.vol22, R.drawable.vol23, R.drawable.vol24,
			R.drawable.vol25, R.drawable.vol26, R.drawable.vol27,
			R.drawable.vol28, R.drawable.vol29, R.drawable.vol30, R.drawable.vol31,
			R.drawable.vol32, R.drawable.vol33, R.drawable.vol34 };
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemImage.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return itemImage.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return itemImage.get(position).getmDrawbaleitem();
	}

	private Context context;
	private final ArrayList<VolItem> itemImage;

	public VolAdapter(Context context, ArrayList<VolItem> itemImage) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.itemImage = itemImage;

	}
	
	public VolAdapter(Context context){
		inflater = LayoutInflater.from(context);
		itemImage = new  ArrayList<VolItem>();
		for (int item : mDrawableItem) {
			VolItem volItem = new VolItem();
			volItem.setmDrawbaleitem(item);
			itemImage.add(volItem);
		}
	}
	
	
	
	@SuppressLint("ViewTag")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// LayoutInflater inflater = (LayoutInflater) context
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView = convertView;

		if (gridView == null) {

			// gridView = new View(context);

			gridView = inflater.inflate(R.layout.gridview_vol_item, parent,
					false);
			gridView.setTag(R.id.grid_item_image, gridView.findViewById(R.id.grid_item_image));
		}
			ImageView volItem = (ImageView) gridView.getTag(R.id.grid_item_image);
			VolItem item = (VolItem) getItem(position);
			volItem.setImageResource(item.getmDrawbaleitem());

		// else {
		// gridView = (View) convertView;
		// }

		return gridView;
	}

}

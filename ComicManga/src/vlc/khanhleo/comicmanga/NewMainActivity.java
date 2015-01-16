package vlc.khanhleo.comicmanga;

import vlc.khanhle.comicsongoku.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NewMainActivity extends Activity{
	public class ChapArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final String[] values;

		public ChapArrayAdapter(Context context, String[] values) {
			super(context, R.layout.list_view_new_main_item, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View v = inflater.inflate(R.layout.list_view_new_main_item, parent,
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
		

			final ImageView cancel = (ImageView) v
					.findViewById(R.id.list_cancel_item);

			return v;
		}
	}

}

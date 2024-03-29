package vlc.khanhleo.comicmanga.utils;

import vlc.khanhle.comicsongoku.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Consts {
	public static String VOL;
	public static String CHAP;
	public static String VOL_CHAP;
	public static String NUMBER_ITEM;
	public static final String URL = "http://khanhleo.com/";
	public static final String URL_CHECK_API = "http://khanhleo.com/api/check_vol/?name=";
	public static final String URL_GET_VOL_NUMBER_API = "http://khanhleo.com/api/get_vol_number/";
	
	public static final String APPLICATION_PREFERENCES = "comicmanga_preferences";
	public static final int PREFERENCES_MODE = Context.MODE_PRIVATE;
	public static final String IS_FIRST_USE = "false";
	
	public static final String AD_UNIT_ID="ca-app-pub-5056196661864424/5308176792";
	public static final String AD_INTERSTITIAL_ID = "ca-app-pub-5056196661864424/6468682396"; 
	
	public static final String COUNT_TO_SHOW_AD = "count";

	public static String getSdCardPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/ComicSongoku/";
	}

	public static void showAppInforDialog(Activity ac) {

		final Dialog dialog = new Dialog(ac);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_menu);
		TextView message = (TextView) dialog.findViewById(R.id.txt_message);
		message.setMovementMethod(LinkMovementMethod.getInstance());
		Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setCancelable(true);
		dialog.show();
	}

	public static boolean isNetworkOnline(Context context) {
		boolean status = false;
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getNetworkInfo(0);
			if (netInfo != null
					&& netInfo.getState() == NetworkInfo.State.CONNECTED) {
				status = true;
			} else {
				netInfo = cm.getNetworkInfo(1);
				if (netInfo != null
						&& netInfo.getState() == NetworkInfo.State.CONNECTED)
					status = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return status;

	}

	// get, set for first use app
	public static boolean getIsFirstUse(final Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				APPLICATION_PREFERENCES,
				PREFERENCES_MODE);

		boolean value = prefs.getBoolean(
				IS_FIRST_USE, true);

		return value;
	}

	public static void setIsFirstUse(final Context context, final Boolean value) {
		SharedPreferences prefs = context.getSharedPreferences(
				APPLICATION_PREFERENCES,
				PREFERENCES_MODE);
		Editor editor = prefs.edit();

		editor.putBoolean(IS_FIRST_USE, value);

		editor.commit();
	}
	
	// get, set for count to show admob
		public static int getCountAd(final Context context) {
			SharedPreferences prefs = context.getSharedPreferences(
					APPLICATION_PREFERENCES,
					PREFERENCES_MODE);

			int value = prefs.getInt(
					COUNT_TO_SHOW_AD, 0);

			return value;
		}

		public static void setCountAd(final Context context, final int value) {
			SharedPreferences prefs = context.getSharedPreferences(
					APPLICATION_PREFERENCES,
					PREFERENCES_MODE);
			Editor editor = prefs.edit();

			editor.putInt(COUNT_TO_SHOW_AD, value);

			editor.commit();
		}
}

package vlc.khanhleo.comicmanga.utils;

import vlc.khanhle.comicmanga.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
	    boolean status=false;
	    try{
	        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo netInfo = cm.getNetworkInfo(0);
	        if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
	            status= true;
	        }else {
	            netInfo = cm.getNetworkInfo(1);
	            if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
	                status= true;
	        }
	    }catch(Exception e){
	        e.printStackTrace();  
	        return false;
	    }
	    return status;

	    }
}

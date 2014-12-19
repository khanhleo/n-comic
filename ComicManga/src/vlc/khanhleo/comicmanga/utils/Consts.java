package vlc.khanhleo.comicmanga.utils;

import android.os.Environment;

public class Consts {
	public static String VOL;
	public static String CHAP;
	public static String VOL_CHAP;
	public static String NUMBER_ITEM;
	public static final String URL = "http://khanhleo.com/";
	public static final String URL_CHECK_API = "http://khanhleo.com/api/check_vol/?name=";
	
	public static String getSdCardPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + "/ComicSongoku/";
	}
}

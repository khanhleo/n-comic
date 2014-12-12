package vlc.khanhleo.comicmanga.object;

import android.R.bool;

public class VolItem {
	private int mDrawbaleitem;
	private bool mIsNew;
	private bool mIsDownload;
	
	public VolItem(int mDrawbaleitem, bool mIsNew, bool mIsDownload) {
		super();
		this.mDrawbaleitem = mDrawbaleitem;
		this.mIsNew = mIsNew;
		this.mIsDownload = mIsDownload;
	}
	public VolItem(){};
	public int getmDrawbaleitem() {
		return mDrawbaleitem;
	}
	public void setmDrawbaleitem(int mDrawbaleitem) {
		this.mDrawbaleitem = mDrawbaleitem;
	}
	public bool getmIsNew() {
		return mIsNew;
	}
	public void setmIsNew(bool mIsNew) {
		this.mIsNew = mIsNew;
	}
	public bool getmIsDownload() {
		return mIsDownload;
	}
	public void setmIsDownload(bool mIsDownload) {
		this.mIsDownload = mIsDownload;
	}
	
}

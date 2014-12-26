package vlc.khanhleo.comicmanga.object;


public class VolItem {
	private String mId;
	private int mDrawbaleitem;
	private String mIsNew;
	private String mIsDownload;
	
	public VolItem(){};
	

	public VolItem(String mId, int mDrawbaleitem, String mIsNew,
			String mIsDownload) {
		super();
		this.mId = mId;
		this.mDrawbaleitem = mDrawbaleitem;
		this.mIsNew = mIsNew;
		this.mIsDownload = mIsDownload;
	}


	public int getmDrawbaleitem() {
		return mDrawbaleitem;
	}
	public void setmDrawbaleitem(int mDrawbaleitem) {
		this.mDrawbaleitem = mDrawbaleitem;
	}
	public String getmIsNew() {
		return mIsNew;
	}
	public void setmIsNew(String mIsNew) {
		this.mIsNew = mIsNew;
	}
	public String getmIsDownload() {
		return mIsDownload;
	}
	public void setmIsDownload(String mIsDownload) {
		this.mIsDownload = mIsDownload;
	}


	public String getmId() {
		return mId;
	}


	public void setmId(String mId) {
		this.mId = mId;
	}
	
}

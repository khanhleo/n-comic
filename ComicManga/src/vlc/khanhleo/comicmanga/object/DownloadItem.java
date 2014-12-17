package vlc.khanhleo.comicmanga.object;

public class DownloadItem {
	private String mFileName;
	private String mUrl;
	private String mFolderName;
	
	
	public DownloadItem() {
		super();
	}
	public DownloadItem(String mFileName, String mUrl, String mFolderName) {
		super();
		this.mFileName = mFileName;
		this.mUrl = mUrl;
		this.mFolderName = mFolderName;
	}
	public String getmFileName() {
		return mFileName;
	}
	public void setmFileName(String mFileName) {
		this.mFileName = mFileName;
	}
	public String getmUrl() {
		return mUrl;
	}
	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}
	public String getmFolderName() {
		return mFolderName;
	}
	public void setmFolderName(String mFolderName) {
		this.mFolderName = mFolderName;
	}
	
	
	
}

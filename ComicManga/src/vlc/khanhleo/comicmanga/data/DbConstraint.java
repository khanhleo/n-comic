package vlc.khanhleo.comicmanga.data;

public class DbConstraint {
	public static final String DATABASE_NAME = "sendsms.db"; 
    public static final int DATABASE_VERSION = 2;
        	
	// Fields of EXCEPTIONS table
	public static final String EXCEPTIONS = "vol_list";
	public static final String IS_DOWNLOAD = "is_download";
	public static final String IS_NEW = "is_new";
	public static final String IMAGE = "image";
	public static final String ID = "id";
	public static final String CREATE_TABLE_EXCEPTION_VERSION = "CREATE VIRTUAL TABLE "
			+ EXCEPTIONS + " USING FTS3(" +
			ID + " TEXT, " +
			IS_DOWNLOAD + " TEXT, " +
			IS_NEW + " TEXT, " +
			IMAGE + " TEXT)";
}

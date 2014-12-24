package vlc.khanhleo.comicmanga.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DaoBase {
	 private Context context;
	 protected SQLiteDatabase db;
	   
	 public DaoBase(Context context) {
		 this.context = context;
		 OpenHelper openHelper = new OpenHelper(this.context);
		 this.db = openHelper.getWritableDatabase();
	 }
   
	 public SQLiteDatabase getDb() {
		 return this.db;
	 }
	 
	 public void close()
	 {
		 if(this.db != null)
			 this.db.close();
	 }
	
	 private static class OpenHelper extends SQLiteOpenHelper {

		 public OpenHelper(Context context) {
			super(context, DbConstraint.DATABASE_NAME, null, DbConstraint.DATABASE_VERSION);
		 }
	      
	     @Override
	     public void onCreate(SQLiteDatabase db) {
	    	
	  		db.execSQL(DbConstraint.CREATE_TABLE_EXCEPTION_VERSION);
//	  		db.execSQL(DbConstraint.CREATE_TABLE_SUMMARY_VERSION);
//	  		db.execSQL(DbConstraint.CREATE_TABLE_USER_VERSION);
	  	}
  	
	  	@Override
	  	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	  		db.execSQL("DROP TABLE IF EXISTS " + DbConstraint.EXCEPTIONS); 
//	  		db.execSQL("DROP TABLE IF EXISTS " + DbConstraint.USER); 

	        onCreate(db);
	  	}
	}
}

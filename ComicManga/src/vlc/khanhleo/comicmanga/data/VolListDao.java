package vlc.khanhleo.comicmanga.data;


import java.util.ArrayList;
import java.util.List;

import vlc.khanhleo.comicmanga.object.VolItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


public class VolListDao extends DaoBase {

	private final static String tableName = DbConstraint.EXCEPTIONS;
	private String[] mColumns = new String[] { DbConstraint.ID,
			DbConstraint.IS_DOWNLOAD, DbConstraint.IS_NEW, DbConstraint.IMAGE };

	public VolListDao(Context context) {
		super(context);
	}

	public void deleteAll() {
		this.db.delete(tableName, null, null);
	}

	public long insertRow(VolItem rowData) {
		ContentValues values = new ContentValues();
		values.put(DbConstraint.ID, rowData.getmId());
		values.put(DbConstraint.IS_DOWNLOAD, rowData.getmIsDownload());
		values.put(DbConstraint.IS_NEW, rowData.getmIsNew());

		return db.insertOrThrow(tableName, null, values);
	}

	public long updateRow(VolItem rowData, String id) {
		ContentValues values = new ContentValues();
		values.put(DbConstraint.IS_DOWNLOAD, rowData.getmIsDownload());
		values.put(DbConstraint.IS_NEW, rowData.getmIsNew());

		// return db.update(tableName, values, null, null);
		return db.update(tableName, values, DbConstraint.ID + " = ?",
				new String[] { id });
	}

//	public long deleteRow(ContactItem rowData) {
//		return db.delete(tableName, DbConstraint.STT + " = ?",
//				new String[] { rowData.getStt() });
//	}

	public List<VolItem> selectAll() {
		List<VolItem> result = new ArrayList<VolItem>();
		VolItem rowData = null;
		Cursor cursor = null;
		try {

			cursor = this.db.query(tableName, mColumns, null, null, null, null,
					null);

			if (cursor.moveToFirst()) {
				do {
					rowData = new VolItem();
					rowData.setmId(cursor.getInt(cursor
							.getColumnIndex(DbConstraint.ID)));
					rowData.setmIsDownload(cursor.getString(cursor
							.getColumnIndex(DbConstraint.IS_DOWNLOAD)));
					rowData.setmIsNew(cursor.getString(cursor
							.getColumnIndex(DbConstraint.IS_NEW)));
					rowData.setmDrawbaleitem(cursor.getInt(cursor
							.getColumnIndex(DbConstraint.IMAGE)));
					result.add(rowData);
				} while (cursor.moveToNext());
			}

		} finally {
			if (cursor != null)
				cursor.close();
		}

		return result;
	}

//	public List<VolItem> selectForClassDetails(String mClassDetails) {
//		List<VolItem> result = new ArrayList<VolItem>();
//		VolItem rowData = null;
//		Cursor cursor = null;
//		try {
//
//			cursor = this.db.query(tableName, mColumns,
//					DbConstraint.CLASS_DETAILS + " = ?",
//					new String[] { mClassDetails }, null, null, null);
//
//			if (cursor.moveToFirst()) {
//				do {
//					rowData = new ContactItem();
//					rowData.setStt(cursor.getString(cursor
//							.getColumnIndex(DbConstraint.STT)));
//					rowData.setContactName(cursor.getString(cursor
//							.getColumnIndex(DbConstraint.NAME)));
//					rowData.setPhoneNumber(cursor.getString(cursor
//							.getColumnIndex(DbConstraint.PHONES)));
//					rowData.setmToan(cursor.getString(cursor
//							.getColumnIndex(DbConstraint.TOAN)));
//					rowData.setmTiengViet(cursor.getString(cursor
//							.getColumnIndex(DbConstraint.TIENG_VIET)));
//					result.add(rowData);
//				} while (cursor.moveToNext());
//			}
//
//		} finally {
//			if (cursor != null)
//				cursor.close();
//		}
//
//		return result;
//	}

}

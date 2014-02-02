package com.tolstoff.orthodoxyicons;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBcache {
	private static final String DATABASE_NAME = "OrthodoxIconsDB";
	private static final String DATABASE_TABLE = "OrthidoxIconsMainTable";
	private static final String COLUMN_TITLE = "titile";
	private static final String COLUMN_PREVIEW_LIST = "preview";
	private static final String COLUMN_PREVIEW_GALLERY = "gallery";
	private static final String COLUMN_DETAIL = "detail";
	private DBHelper dbHelper;
	private Context context;
	private SQLiteDatabase mDB;

	public static final String DATABASE_CREATE_SCRIPT = "create table "
			+ DATABASE_TABLE + "(" + "id integer primary key autoincrement,"
			+ COLUMN_TITLE + " text," + COLUMN_PREVIEW_LIST + " text,"
			+ COLUMN_PREVIEW_GALLERY + " text," + COLUMN_DETAIL + " text"
			+ ");";

	public DBcache(Context context) {

		this.context = context;

	}

	public void open() {
		dbHelper = new DBHelper(context);
		mDB = dbHelper.getWritableDatabase();
	}

	public void close() {
		if (dbHelper != null)
			dbHelper.close();

	}
	
	public void clear(){
	mDB.execSQL("delete from " + DATABASE_TABLE);	
		
		
	}

	public void WriteResult(String titleImage, String listImageUrls,
			String galleryImageUrls, String detailImageUrls) {
		// dbHelper = new DBHelper(context);
	//	LogT.log(titleImage +  " " + listImageUrls +  " " + galleryImageUrls +  " " + detailImageUrls);
		ContentValues cv = new ContentValues();

//		try {
//			// db = dbHelper.getWritableDatabase();
//			mDB.execSQL("delete from " + DATABASE_TABLE);
//		}
//
//		catch (Exception e) {
//			LogT.log("DBCache: " + e);
//
//		}

		cv.put(COLUMN_TITLE, titleImage);
		cv.put(COLUMN_PREVIEW_LIST, listImageUrls);
		cv.put(COLUMN_PREVIEW_GALLERY, galleryImageUrls);
		cv.put(COLUMN_DETAIL, detailImageUrls);

		mDB.insert(DATABASE_TABLE, null, cv);

		// //�������� ������
		//
		//
		// LogT.log("--- Rows in mytable: ---");
		// // ������ ������ ���� ������ �� ������� mytable, �������� Cursor
		// Cursor c = db.query(DBHelper.DATABASE_NAME, null, null, null, null,
		// null, null);
		//
		// // ������ ������� ������� �� ������ ������ �������
		// // ���� � ������� ��� �����, �������� false
		// if (c.moveToFirst()) {
		//
		// // ���������� ������ �������� �� ����� � �������
		// // int idColIndex = c.getColumnIndex(DBHelper.COLUMN_TITL);
		// int titileColIndex = c.getColumnIndex(DBHelper.COLUMN_TITLE);
		// int previewColIndex = c.getColumnIndex(DBHelper.COLUMN_PREVIEW_LIST);
		//
		// do {
		// // �������� �������� �� ������� �������� � ����� ��� � ���
		// LogT.log(
		// // "id" = " + c.getInt() +
		// "Title = " + c.getString(titileColIndex) +
		// ", preview = " + c.getString(previewColIndex));
		// // ������� �� ��������� ������
		// // � ���� ��������� ��� (������� - ���������), �� false - ������� ��
		// �����
		// } while (c.moveToNext());
		// } else
		//
		// LogT.log("0 rows");
		// c.close();
		//

		// LogT.backupDB(context, DATABASE_NAME);

	}

	// �������� ��� ������ �� ������� DB_TABLE
	public Cursor getAllData() {
		return mDB.query(DATABASE_TABLE, null, null, null, null, null, null);
	}

	class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {

			super(context, DATABASE_NAME, null, 1);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_SCRIPT);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	

		}

	}
}

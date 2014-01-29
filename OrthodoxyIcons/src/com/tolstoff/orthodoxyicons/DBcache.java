package com.tolstoff.orthodoxyicons;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBcache {
	private static final String DATABASE_NAME = "OrthodoxIconsDB";
	private static final String COLUMN_TITLE = "titile";
	private static final String COLUMN_PREVIEW_LIST = "preview";
	private static final String COLUMN_PREVIEW_GALLERY = "gallery";
	private static final String COLUMN_DETAIL = "detail";
	private DBHelper dbHelper;
	private Context context;

	public static final String DATABASE_CREATE_SCRIPT = "create table "
			+ DATABASE_NAME + "(" + "id integer primary key autoincrement,"
			+ COLUMN_TITLE + " text," + COLUMN_PREVIEW_LIST + " text,"
			+ COLUMN_PREVIEW_GALLERY + " text," + COLUMN_DETAIL + " text"
			+ ");";
	
	private void WriteResult(ArrayLst<titleImage>, AttayList) {
		dbHelper = new DBHelper(context);

		ContentValues cv = new ContentValues();
		SQLiteDatabase db;

		try {
			db = dbHelper.getWritableDatabase();
			db.execSQL("delete from " + DATABASE_NAME);
		}

		catch (Exception e) {
			LogT.log(e);

		}

		db = dbHelper.getWritableDatabase();
		for (int i = 0; i < titleImage.size(); i++) {

			cv.put(DBHelper.COLUMN_TITLE, titleImage.get(i));
			cv.put(DBHelper.COLUMN_PREVIEW_LIST, listImageUrls.get(i));
			cv.put(DBHelper.COLUMN_PREVIEW_GALLERY, galleryImageUrls.get(i));
			cv.put(DBHelper.COLUMN_DETAIL, detailImageUrls.get(i));

			db.insert(DBHelper.DATABASE_NAME, null, cv);

		}

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

		dbHelper.close();

		LogT.backupDB(context, DBHelper.DATABASE_NAME);

	}
	
	
	
	
	
	class DBHelper extends SQLiteOpenHelper {
		

		
		
		public DBHelper (Context context)
		{
			
		super (context, DATABASE_NAME, null, 1);	
			
		
		
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
		   db.execSQL(DATABASE_CREATE_SCRIPT);
			
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}

	}
}

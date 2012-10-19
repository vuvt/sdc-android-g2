package com.nikmesoft.android.nearfood.daos;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDAO extends SQLiteOpenHelper {

	private static String DB_PATH = "";
	public static final String DB_NAME = "";
	private SQLiteDatabase dataBase;
	private final Context context;
	private static BaseDAO connection;

	public BaseDAO(Context context) {
		super(context, DB_NAME, null, 1);
		this.context = context;
		DB_PATH = "/data/data/"
				+ context.getApplicationContext().getPackageName()
				+ "/databases/";
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	@Override
	public synchronized void close() {
		if (dataBase != null)
			dataBase.close();
		super.close();
	}

	public static synchronized BaseDAO getDBAdapterInstance(Context context) {
		if (connection == null) {
			connection = new BaseDAO(context);
		}
		return connection;
	}

	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		if (!dbExist) {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	private void copyDataBase() throws IOException {
		InputStream myInput = context.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		dataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	public Cursor selectRecordsFromDB(String tableName, String[] tableColumns,
			String whereClase, String whereArgs[], String groupBy,
			String having, String orderBy) {
		return dataBase.query(tableName, tableColumns, whereClase, whereArgs,
				groupBy, having, orderBy);
	}

	public ArrayList<ArrayList<String>> selectRecordsFromDBList(
			String tableName, String[] tableColumns, String whereClase,
			String whereArgs[], String groupBy, String having, String orderBy) {

		ArrayList<ArrayList<String>> retList = new ArrayList<ArrayList<String>>();
		ArrayList<String> list = new ArrayList<String>();
		Cursor cursor = dataBase.query(tableName, tableColumns, whereClase,
				whereArgs, groupBy, having, orderBy);
		if (cursor.moveToFirst()) {
			do {
				list = new ArrayList<String>();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					list.add(cursor.getString(i));
				}
				retList.add(list);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return retList;

	}

	public long insertRecordsInDB(String tableName, String nullColumnHack,
			ContentValues initialValues) {
		return dataBase.insert(tableName, nullColumnHack, initialValues);
	}

	public boolean updateRecordInDB(String tableName,
			ContentValues initialValues, String whereClause, String whereArgs[]) {
		return dataBase
				.update(tableName, initialValues, whereClause, whereArgs) > 0;
	}

	public int updateRecordsInDB(String tableName, ContentValues initialValues,
			String whereClause, String whereArgs[]) {
		return dataBase
				.update(tableName, initialValues, whereClause, whereArgs);
	}

	public int deleteRecordInDB(String tableName, String whereClause,
			String[] whereArgs) {
		return dataBase.delete(tableName, whereClause, whereArgs);
	}

	public Cursor selectRecordsFromDB(String query, String[] selectionArgs) {
		return dataBase.rawQuery(query, selectionArgs);
	}

	public ArrayList<ArrayList<String>> selectRecordsFromDBList(String query,
			String[] selectionArgs) {
		ArrayList<ArrayList<String>> retList = new ArrayList<ArrayList<String>>();
		ArrayList<String> list = new ArrayList<String>();
		Cursor cursor = dataBase.rawQuery(query, selectionArgs);
		if (cursor.moveToFirst()) {
			do {
				list = new ArrayList<String>();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					list.add(cursor.getString(i));
				}
				retList.add(list);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return retList;
	}
}

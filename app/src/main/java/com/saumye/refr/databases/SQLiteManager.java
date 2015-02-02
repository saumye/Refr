package com.saumye.refr.databases;

/**
 * Helper class for SQLite Db operations.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class SQLiteManager {

	private SQLiteDatabase sqliteDB = null;
	private SQLiteHelper sqliteHelper = null;	
	private static SQLiteManager sqliteManager = null;
	private ContentValues contentValues = null;
	private Cursor cursor = null;

	private SQLiteManager(){}

	/**
	 * constructor
	 * @param context
	 */
	private SQLiteManager(Context context) {
		sqliteHelper = new SQLiteHelper(context);
	}

	/**
	 * 
	 * @param context
	 * @return SQLiteManager
	 */
	public static SQLiteManager getInstance(Context context) {
		if(sqliteManager == null) {
			sqliteManager = new SQLiteManager(context);
		}
		return sqliteManager;
	}

	/**
	 * get a writable database
	 * @throws android.database.SQLException
	 */
	public void open() throws SQLException {
		sqliteDB = sqliteHelper.getWritableDatabase();
	}

	public void insertDetails(String firstname,String lastname,String email, String picUrl, String location, String skills, String profileId, String profileurl) throws SQLException {
		contentValues = new ContentValues();
		contentValues.put(SQLiteConstants.COLUMN_FIRST_NAME, firstname);
		contentValues.put(SQLiteConstants.COLUMN_LAST_NAME, lastname);
		contentValues.put(SQLiteConstants.COLUMN_EMAIL, email);
		contentValues.put(SQLiteConstants.COLUMN_PICTURE_URL, picUrl);
		contentValues.put(SQLiteConstants.COLUMN_LOCATION, location);
		contentValues.put(SQLiteConstants.COLUMN_SKILLS, skills);
		contentValues.put(SQLiteConstants.COLUMN_PROFILE_ID, profileId);
        contentValues.put(SQLiteConstants.COLUMN_PROFILE_URL, profileurl);
		sqliteDB.insert(SQLiteConstants.TABLE_USER_INFO, null, contentValues);
	}			

	public void updateUserDetails(String firstname,String lastname,String email, String picUrl, String location, String skills) {
		contentValues = new ContentValues();
		contentValues.put(SQLiteConstants.COLUMN_FIRST_NAME, firstname);
		contentValues.put(SQLiteConstants.COLUMN_LAST_NAME, lastname);
		contentValues.put(SQLiteConstants.COLUMN_EMAIL, email);
		contentValues.put(SQLiteConstants.COLUMN_PICTURE_URL, picUrl);
		contentValues.put(SQLiteConstants.COLUMN_LOCATION, location);
		contentValues.put(SQLiteConstants.COLUMN_SKILLS, skills);
		sqliteDB.update(SQLiteConstants.TABLE_USER_INFO, contentValues, SQLiteConstants.COLUMN_EMAIL + " =" + email , null);
	}

	public Cursor getUserDetails(String userName) {
		cursor = sqliteDB.query(SQLiteConstants.TABLE_USER_INFO, null, SQLiteConstants.COLUMN_FIRST_NAME + " ='" + userName + "'", null, null, null, null);
		return cursor;
	}
	
	public Cursor getUserSuggestion(CharSequence userName) {
		String select =  SQLiteConstants.COLUMN_FIRST_NAME + " LIKE ? ";
		String[]  selectArgs = { userName + "%"};
		cursor = sqliteDB.query(SQLiteConstants.TABLE_USER_INFO, null, select, selectArgs, null, null, null);
		return cursor;
	}
	
	public Cursor getUserDetails() {
		cursor = sqliteDB.query(SQLiteConstants.TABLE_USER_INFO, null, null, null, null, null, null);
		return cursor;
	}
	
	
	/**
	 * close the database
	 */
	public void close() {		
		sqliteHelper.close();
	}

}

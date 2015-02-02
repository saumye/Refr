package com.saumye.refr.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper{
	
	public SQLiteHelper(Context context) {
		super(context, SQLiteConstants.DATABASE_NAME, null , SQLiteConstants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {		
		db.execSQL(SQLiteConstants.CREATE_TABLE_USER_INFO);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {		
		db.execSQL(" DROP TABLE IF EXISTS " + SQLiteConstants.TABLE_USER_INFO );
	}
}

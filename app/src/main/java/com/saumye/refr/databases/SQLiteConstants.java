package com.saumye.refr.databases;

/**
 * Class to define the SQlite Constants
 * @author NAGARAJ
 *
 */
public interface SQLiteConstants {

	public String DATABASE_NAME = "user.db";
	public int DATABASE_VERSION = 1;

	public String TABLE_USER_INFO = "user_info";
    public String COLUMN_PROFILE_URL = "profile_url";
	public String COLUMN_ID = "_id";
	public String COLUMN_FIRST_NAME = "first_name";
	public String COLUMN_LAST_NAME = "last_name";
	public String COLUMN_EMAIL = "email";
	public String COLUMN_PICTURE_URL = "picture_url";
	public String COLUMN_LOCATION = "location";
	public String COLUMN_SKILLS = "skills";
	public String COLUMN_PROFILE_ID = "profile_id";

	public String CREATE_TABLE_USER_INFO = "create table " + TABLE_USER_INFO + "(" +
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COLUMN_FIRST_NAME + " text, "
			+ COLUMN_LAST_NAME + " text, "
			+ COLUMN_EMAIL + " text, "
			+ COLUMN_PICTURE_URL + " text, "
			+ COLUMN_LOCATION + " text, "
			+ COLUMN_PROFILE_ID + " text, "
            + COLUMN_PROFILE_URL + " text, "
			+ COLUMN_SKILLS + " text);";




}

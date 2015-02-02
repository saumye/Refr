package com.saumye.refr;

/**
 * Constants used in tha app.
 * @author NAGARAJ
 *
 */
public class ManiPlantConstants {

	public static final String QUESTION_MARK = "?";
	public static final String AMPERSAND = "&";
	public static final String EQUALS = "=";


	public static final String OAUTH2_ACCESS_TOKEN 	     = "oauth2_access_token";
	public static final String ACCESS_TOKEN 			 = "access_token";

	public static final String BASE_URL					 = "https://api.linkedin.com/v1";	
	public static final String URL_PEOPLE				 = "/people/~";
	//public static final String URL_CONNECTIONS 		     = "/people/~/connections:(firstName,lastName,pictureUrl,location,id)";
	public static final String URL_CONNECTIONS 		     = "/people/~/connections:(firstName,lastName,pictureUrl,location,id,public-profile-url)";
	//https://api.linkedin.com/v1/people/id=abcdefg
	public static final String URL_PROFILE_INFO		     = "/people/id=";

    public static final String PROFILE_URL              =      "publicProfileUrl";
	
	public static final String VALUES 			          = "values";
	public static final String ID 			              = "id";
	public static final String FIRST_NAME 			      = "firstName";
	public static final String LAST_NAME 			      = "lastName";
	public static final String LOCATION 			      = "location";
	public static final String PICTURE_URL                = "pictureUrl";
	public static final String EMAIL     	              = "email";
	public static final String SKILLS 			          = "skills";						
	public static final String NAME     	              = "name";
	public static final String USERID     	              = "userid";
}

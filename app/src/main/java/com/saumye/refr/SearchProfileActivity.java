package com.saumye.refr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.TextView;

import com.saumye.refr.databases.SQLiteConstants;
import com.saumye.refr.databases.SQLiteManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * Activity used for searching your LinkedIn contacts.
 * @author NAGARAJ
 *
 */
public class SearchProfileActivity extends  Activity implements OnItemClickListener{

	private TextView textview;
	private String accesstoken;	
	private ProgressDialog pd;

	private AutoCompleteTextView searchAutoCompleteTextView;
    private String user_id;
	private String profileId = "";
	private String firstname = "";
	private String lastname = "";
	private String email = "";
	private String picUrl = "";
	private String location = "";
	private String skills = "";
    private String profileurl = "";
    private String REGISTER_URL = "http://192.168.0.116:8080/users/";
    private String access_token = "";
	private SQLiteManager sqliteManager = null;				


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		textview = (TextView) findViewById(R.id.name);

		SharedPreferences preferences = getSharedPreferences("user_info", 0);
		accesstoken = preferences.getString(ManiPlantConstants.ACCESS_TOKEN, "");
		sqliteManager = SQLiteManager.getInstance(this);
		try {
			sqliteManager.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 String url_profile = ManiPlantConstants.BASE_URL + ManiPlantConstants.URL_PEOPLE + ManiPlantConstants.QUESTION_MARK + ManiPlantConstants.OAUTH2_ACCESS_TOKEN
				+ ManiPlantConstants.EQUALS + accesstoken+ ManiPlantConstants.AMPERSAND + "format=json";

		 String url_connections = ManiPlantConstants.BASE_URL + ManiPlantConstants.URL_CONNECTIONS + ManiPlantConstants.QUESTION_MARK + ManiPlantConstants.OAUTH2_ACCESS_TOKEN
				+ ManiPlantConstants.EQUALS + accesstoken+ ManiPlantConstants.AMPERSAND + "format=json";

        SharedPreferences preferences1 = getSharedPreferences("user_info_id", 0);
        user_id = preferences1.getString("user_id", "");

        Log.i("user_id:",user_id);
        new GetRequestAsyncTask().execute(REGISTER_URL.concat(user_id));
		Cursor cursor = sqliteManager.getUserDetails();
		if(cursor != null && cursor.getCount() > 0 ) {
			initializeSearchView();
		} else {

			new PostRequestAsyncTask().execute(url_connections);
		}
		cursor.close();
	}

	/**
	 * Asynctask  for fetching the LinkedIn contacts.
	 * @author NAGARAJ
	 *
	 */
    private class GetRequestAsyncTask extends AsyncTask<String, Void, Boolean>{
        private String USER_REGISTER_URL = "http://192.168.0.101:8080/users/";
        @Override
        protected void onPreExecute(){
            }

        @Override
        protected Boolean doInBackground(String... urls) {

            if(urls.length>0){
                String url = urls[0];
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try{
                    HttpResponse response = httpClient.execute(httpGet);
                    Log.i("tag-----",response.toString());
                    if(response!=null){
                        //If status is OK 200
                        if(response.getStatusLine().getStatusCode()==200){
                            String result = EntityUtils.toString(response.getEntity());
                            //Convert the string result to a JSON Object
                            Log.i("tag",result);
                            if(result.equals("[]"))
                            {
                                JSONArray fr = new JSONArray();
                                JSONObject user_obj = new JSONObject();
                                user_obj.accumulate("refId",user_id);
                                user_obj.accumulate("refName", "Saumye");
                                user_obj.accumulate("refToken", access_token);
                                user_obj.accumulate("friends", fr);
                                String json = user_obj.toString();
                                Log.i("test",json);
                                StringEntity se = new StringEntity(json);
                                HttpPost httppost = new HttpPost(REGISTER_URL);
                                httppost.setEntity(se);
                                httppost.setHeader("Content-type", "application/json");
                                HttpResponse httpResponse = httpClient.execute(httppost);

                            }
                            else
                            {

                            }
                            Log.e("Authorize","Http response "+ result);

                            return true;
                        }
                    }

                }catch(IOException e){
                    Log.e("Authorize","Error Http response "+e.getLocalizedMessage());
                }
                catch (ParseException e) {
                    Log.e("Authorize","Error Parsing Http response "+e.getLocalizedMessage());
                } catch (JSONException e) {
                    Log.e("Authorize","Error Parsing Http response "+e.getLocalizedMessage());
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean status){
            }

    };




    private class PostRequestAsyncTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected void onPreExecute(){
			pd = ProgressDialog.show(SearchProfileActivity.this, "", SearchProfileActivity.this.getString(R.string.loadingsearch),true);
		}

		@Override
		protected Boolean doInBackground(String... urls) {

			if(urls.length>0){
				String url = urls[0];
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				//HttpPost httpost = new HttpPost(url);
				try{
					HttpResponse response = httpClient.execute(httpGet);
					if(response!=null){
						//If status is OK 200
						if(response.getStatusLine().getStatusCode()==200){
							String result = EntityUtils.toString(response.getEntity());
							//Convert the string result to a JSON Object
							JSONObject resultJson = new JSONObject(result);

							JSONArray valuesJsonArray = resultJson.getJSONArray(ManiPlantConstants.VALUES);

							for(int i=0; i<valuesJsonArray.length(); i++) {
								JSONObject valuesJson = valuesJsonArray.getJSONObject(i);
								if(valuesJson.has(ManiPlantConstants.FIRST_NAME)) {
									firstname = valuesJson.getString(ManiPlantConstants.FIRST_NAME);
								}
								if(valuesJson.has(ManiPlantConstants.LAST_NAME)) {
									lastname = valuesJson.getString(ManiPlantConstants.LAST_NAME);
								}
								if(valuesJson.has(ManiPlantConstants.EMAIL)) {
									email = valuesJson.getString(ManiPlantConstants.EMAIL);
								}
								if(valuesJson.has(ManiPlantConstants.LOCATION)) {
									JSONObject locationObject = valuesJson.getJSONObject(ManiPlantConstants.LOCATION);
									location = locationObject.getString(ManiPlantConstants.NAME);
								}
								if(valuesJson.has(ManiPlantConstants.PICTURE_URL)) {
									picUrl = valuesJson.getString(ManiPlantConstants.PICTURE_URL);
								}
								if(valuesJson.has(ManiPlantConstants.SKILLS)) {
									skills = valuesJson.getString(ManiPlantConstants.SKILLS);
								}
								if(valuesJson.has(ManiPlantConstants.ID)) {
									profileId = valuesJson.getString(ManiPlantConstants.ID);
								}
                                if(valuesJson.has(ManiPlantConstants.PROFILE_URL)) {
                                    profileurl = valuesJson.getString(ManiPlantConstants.PROFILE_URL);
                                }
								Log.e("Authorize","User DEtails "+ firstname + " " + lastname + " " + email + " "
										+ location + " " + picUrl  + " " + skills + " " + profileId + " " + profileurl);

								sqliteManager.insertDetails(firstname, lastname, "email", picUrl, location, "skill", profileId, profileurl);

							}
							Log.e("Authorize","Http response "+ result);

							return true;
						}
					}												

				}catch(IOException e){
					Log.e("Authorize","Error Http response "+e.getLocalizedMessage());  
				}
				catch (ParseException e) {
					Log.e("Authorize","Error Parsing Http response "+e.getLocalizedMessage());
				} catch (JSONException e) {
					Log.e("Authorize","Error Parsing Http response "+e.getLocalizedMessage());
				}		
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean status){
			if(pd!=null && pd.isShowing()){
				pd.dismiss();
			}

			Cursor cursor = sqliteManager.getUserDetails(firstname);
			if(cursor.moveToFirst()) {
				firstname = cursor.getString(cursor.getColumnIndex(SQLiteConstants.COLUMN_FIRST_NAME));
				lastname = cursor.getString(cursor.getColumnIndex(SQLiteConstants.COLUMN_LAST_NAME));
				email = cursor.getString(cursor.getColumnIndex(SQLiteConstants.COLUMN_EMAIL));
				location = cursor.getString(cursor.getColumnIndex(SQLiteConstants.COLUMN_LOCATION));
				picUrl = cursor.getString(cursor.getColumnIndex(SQLiteConstants.COLUMN_PICTURE_URL));
				skills = cursor.getString(cursor.getColumnIndex(SQLiteConstants.COLUMN_SKILLS));
				profileId = cursor.getString(cursor.getColumnIndex(SQLiteConstants.COLUMN_PROFILE_ID));
				Log.e("Authorize","User Details "+ firstname + " " + lastname + " " + email + " "
						+ location + " " + picUrl  + " " + skills + " id : " + profileId);
			}
			cursor.close();			
			initializeSearchView();
		}

	};


	/**
	 * Initialize and setup the AutoCompleteTextView.
	 */
	private void initializeSearchView() {

		searchAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.search);
		searchAutoCompleteTextView.setOnItemClickListener(this);
		final int[] to = new int[]{android.R.id.text1, android.R.id.text2};
		final String[] from = new String[]{SQLiteConstants.COLUMN_FIRST_NAME, SQLiteConstants.COLUMN_LAST_NAME};
		android.widget.SimpleCursorAdapter adapter = new android.widget.SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2,
				null,
				from,
				to,0);

		// This will provide the labels for the choices to be displayed in the AutoCompleteTextView
		adapter.setCursorToStringConverter(new android.widget.SimpleCursorAdapter.CursorToStringConverter() {
			public CharSequence convertToString(Cursor cursor) {
				final int colIndex = cursor.getColumnIndexOrThrow(SQLiteConstants.COLUMN_FIRST_NAME);
				return cursor.getString(colIndex);
			}
		});

		adapter.setFilterQueryProvider(new FilterQueryProvider() {
			@Override
			public Cursor runQuery(CharSequence description) {
				Log.i("tag", " " + description);
				Cursor managedCursor = sqliteManager.getUserSuggestion(description);
				Log.d("profileactivity", "Query has " + managedCursor.getCount() + " rows of description for " + description);
				return managedCursor;
			}
		});

		searchAutoCompleteTextView.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position,long id) {
		
		Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
		firstname = cursor.getString(cursor.getColumnIndex(SQLiteConstants.COLUMN_FIRST_NAME));
		lastname = cursor.getString(cursor.getColumnIndex(SQLiteConstants.COLUMN_LAST_NAME));
		email = cursor.getString(cursor.getColumnIndex(SQLiteConstants.COLUMN_EMAIL));
		location = cursor.getString(cursor.getColumnIndex(SQLiteConstants.COLUMN_LOCATION));
		picUrl = cursor.getString(cursor.getColumnIndex(SQLiteConstants.COLUMN_PICTURE_URL));
		skills = cursor.getString(cursor.getColumnIndex(SQLiteConstants.COLUMN_SKILLS));
		profileId = cursor.getString(cursor.getColumnIndex(SQLiteConstants.COLUMN_PROFILE_ID));
        profileurl = cursor.getString(cursor.getColumnIndex(SQLiteConstants.COLUMN_PROFILE_URL));

		Log.e("Authorize","User DEtails "+ firstname + " " + lastname + " " + email + " "
				+ location + " " + picUrl  + " " + skills + " id : " + profileId);

        Intent myIntent = new Intent(SearchProfileActivity.this, RateActivity.class);
        myIntent.putExtra("first_name",firstname);
        myIntent.putExtra("last_name",lastname);
        myIntent.putExtra("location",location);
        myIntent.putExtra("profileid",profileId);
        myIntent.putExtra("profileurl",profileurl);
        myIntent.putExtra("pictureurl",picUrl);
        SearchProfileActivity.this.startActivity(myIntent);
	}

	/**
	 * Get the basic profile info of a particular user.
	 */
	private void getProfileInfo() {
		HttpClient httpClient1 = new DefaultHttpClient();
		HttpGet httpGet1 = new HttpGet(ManiPlantConstants.BASE_URL + ManiPlantConstants.URL_PROFILE_INFO + profileId + 
				ManiPlantConstants.QUESTION_MARK + ManiPlantConstants.OAUTH2_ACCESS_TOKEN
				+ ManiPlantConstants.EQUALS + accesstoken+ ManiPlantConstants.AMPERSAND + "format=json");
		Log.e("Authorize","url "+ ManiPlantConstants.BASE_URL + ManiPlantConstants.URL_PROFILE_INFO + profileId);
		//HttpPost httpost = new HttpPost(url);
		try{
			HttpResponse response1 = httpClient1.execute(httpGet1);
			if(response1!=null){
				//If status is OK 200
				if(response1.getStatusLine().getStatusCode()==200){
					String result1 = EntityUtils.toString(response1.getEntity());
					//Convert the string result to a JSON Object
					JSONObject resultJson1 = new JSONObject(result1);

					Log.e("Authorize","Http response "+ result1);

					//return true;
				}
			}												

		}catch(IOException e){
			Log.e("Authorize","Error Http response "+e.getLocalizedMessage());  
		}
		catch (ParseException e) {
			Log.e("Authorize","Error Parsing Http response "+e.getLocalizedMessage());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("Authorize","Error Parsing Http response "+e.getLocalizedMessage());
		}				
	}
	
}

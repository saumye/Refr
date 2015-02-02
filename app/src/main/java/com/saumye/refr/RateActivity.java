package com.saumye.refr;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.ArrayList;
//import com.pkmmte.view.CircularImageView;

public class RateActivity extends ActionBarActivity {
    private ProgressDialog pd;
    String PICTURE_URL = "";
    String NAME = "";
    String LOCATION = "";
    String LINKEDIN_URL = "";
    String PROFILE_ID;
    String access_token;
    String user_id;
    String SKILLS_API_URL = "http://192.168.0.116:8080/skills?link=";
    String POST_URL = "http://192.168.0.116:8080/users/";
    String POST_API_URL;

    Spinner skill1;
    Spinner skill2;
    ArrayList<String> a;
    Button submit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rate);
        Intent myIntent = getIntent();
  //     CircularImageView circularImageView = (CircularImageView)findViewById(R.id.connection_picture);
    //    circularImageView.setBorderWidth(10);
     //   circularImageView.setSelectorStrokeWidth(10);
        skill1 = (Spinner)findViewById(R.id.skill1);
        skill2 = (Spinner)findViewById(R.id.skill2);

        submit_button = (Button)findViewById(R.id.submit_button);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getSharedPreferences("user_info_id", 0);
                user_id = sharedPref.getString("user_id", getString(R.string.user_id));
                SharedPreferences preferences1 = getSharedPreferences("user_info", 0);
                access_token = preferences1.getString(ManiPlantConstants.ACCESS_TOKEN, "");
                Log.i("tag",POST_URL.concat(user_id));
                new PostRequestAsyncTask().execute(POST_URL.concat(user_id));
            }
        });

      //  circularImageView.addShadow();
        PICTURE_URL = myIntent.getStringExtra("pictureurl");
       // Picasso.with(this).load(PICTURE_URL).into(circularImageView);

        TextView name = (TextView)findViewById(R.id.name);
        TextView location = (TextView)findViewById(R.id.description);

        String finalname = myIntent.getStringExtra("first_name").concat(" ").concat(myIntent.getStringExtra("last_name"));
        name.setText(finalname);
        location.setText(myIntent.getStringExtra("location"));

        PROFILE_ID = myIntent.getStringExtra("profileid");
        LINKEDIN_URL = myIntent.getStringExtra("profileurl");

        //private static AsyncHttpClient client = new AsyncHttpClient();
        //RequestParams params = new RequestParams();
        //params.put("notes", "Test api support");


        //client.post(SKILLS_API_URL, params, responseHandler);

        //JSONObject jsonParams = new JSONObject();
        //jsonParams.put("notes", "Test api support");
        //StringEntity entity = new StringEntity(jsonParams.toString());
        //client.post(this, SKILLS_API_URL, entity, "application/json",
          //      responseHandler);


        //Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Dense-Regular");
        //name.setTypeface(tf);
        Log.i("TAG",SKILLS_API_URL.concat(LINKEDIN_URL));
        new GetRequestAsyncTask().execute(SKILLS_API_URL.concat(LINKEDIN_URL));
        //name.setTypeface(tf);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private int sendPost()
    {
        return 1;

    }



    private class PostRequestAsyncTask extends AsyncTask<String, Void, Boolean>{
        private String USER_REGISTER_URL = "http://192.168.0.101:8080/users/";
        @Override
        protected void onPreExecute(){
            pd = ProgressDialog.show(RateActivity.this, "", RateActivity.this.getString(R.string.loading),true);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            if(urls.length>0){
                String url = urls[0];
                try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);
                JSONObject fr = new JSONObject("");
                fr.accumulate("empName",NAME);
                fr.accumulate("empId",PROFILE_ID);
                fr.accumulate("location",LOCATION);
                fr.accumulate("skill1",skill1.getSelectedItem().toString());
                fr.accumulate("skill2",skill2.getSelectedItem().toString());
                JSONObject user_obj = new JSONObject();
                user_obj.accumulate("refId",user_id);
                user_obj.accumulate("refName", "Saumye");
                user_obj.accumulate("refToken", access_token);
                user_obj.accumulate("friends", fr);
                String json = user_obj.toString();
                Log.i("test",json);
                StringEntity se = new StringEntity(json);
                httppost.setEntity(se);
                httppost.setHeader("Accept", "application/json");
                httppost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpClient.execute(httppost);
                //Log.i("response!!",httpResponse.toString());
                return true;
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
        }

    };





    private class GetRequestAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute(){
            pd = ProgressDialog.show(RateActivity.this, "", RateActivity.this.getString(R.string.loading), true);
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

                            JSONArray valuesJsonArray = resultJson.getJSONArray(ManiPlantConstants.SKILLS);

                            a =  new ArrayList<String>();

                            for(int i=0; i<valuesJsonArray.length(); i++) {
                                {
                                    String valuesJson = valuesJsonArray.get(i).toString();
                                    a.add(i,valuesJson);
                                    Log.i("skill",valuesJson);
                                }
                            }
                            Log.e("Authorize","Http response "+ result);
                            if(a == null)
                                return false;

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

            if(pd!=null && pd.isShowing()) {
                pd.dismiss();
            }
            if(status) {
                ArrayAdapter<String> arr = new ArrayAdapter<String>(RateActivity.this, android.R.layout.simple_spinner_item, a);

                skill1.setAdapter(arr);
                skill2.setAdapter(arr);
            }
            }
        }



};


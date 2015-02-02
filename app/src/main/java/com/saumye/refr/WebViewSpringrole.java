package com.saumye.refr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewSpringrole extends ActionBarActivity {
    private WebView webView;
    private String REDIRECT_URL = "http://www.saumye.com";
    private String USER_ID = "user_id";
    private String user_id;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        pd = ProgressDialog.show(WebViewSpringrole.this, "", WebViewSpringrole.this.getString(R.string.loading),true);
        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webView.loadUrl("https://api.springrole.com/authorize?scope=basic&redirect_uri=http://www.saumye.com&response_type=token&client_id=18ff1ad0");
        if(pd!=null && pd.isShowing()){
            pd.dismiss();
        }
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(REDIRECT_URL))
                {

                    Uri parsed_url = Uri.parse(url);
                    user_id = parsed_url.getQueryParameter(USER_ID);
                    Log.i("user_id", user_id);
                    /*SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("user_id", user_id);
                    editor.commit();*/

                    SharedPreferences preferences = getSharedPreferences("user_info_id", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user_id", user_id);
                    editor.commit();


                    Intent myIntent = new Intent(WebViewSpringrole.this, SearchProfileActivity.class);
                    myIntent.putExtra("user_id", user_id);
                    return super.shouldOverrideUrlLoading(view, url);
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
    });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_view, menu);
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
}

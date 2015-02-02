package com.saumye.refr;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;

/**
 * Splash screen of the app.
 * @author NAGARAJ
 *
 */
public class SplashActivity extends Activity {

	private final int SPLASH_TIME = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_splash);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				SharedPreferences preferences = getSharedPreferences("user_info", 0);				
				String token = preferences.getString(ManiPlantConstants.ACCESS_TOKEN, "");

                SharedPreferences preferences2 = getSharedPreferences("user_id", 0);
                String sptoken = preferences.getString(ManiPlantConstants.ACCESS_TOKEN, "");


				if(TextUtils.isEmpty(token) && TextUtils.isEmpty(sptoken)) {
					final Intent intent = new Intent(SplashActivity.this, WebViewLinkedin.class);
					startActivity(intent);
				} else {
					final Intent intent = new Intent(SplashActivity.this, SearchProfileActivity.class);
					startActivity(intent);
				}
				finish();

			}
		}, SPLASH_TIME);
	}

}

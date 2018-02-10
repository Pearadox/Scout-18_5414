package com.pearadox.scout_5414;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class HelpActivity extends Activity {

		WebView webView_help;
		
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.w("HelpActivity","onCreate");
		setContentView(R.layout.help_layout);
//	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        webView_help = (WebView) findViewById(R.id.webView_help);
			webView_help.loadUrl("file:///android_asset/draft_help.html");

	}
}

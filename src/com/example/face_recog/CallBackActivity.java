package com.example.face_recog;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CallBackActivity extends Activity {

	String url1;
	String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_back);
		
		WebView webview = (WebView)findViewById(R.id.CallBack);
		webview.getSettings().setJavaScriptEnabled(true);
		
		url1 = getIntent().getExtras().getString("URL");
		id = getIntent().getExtras().getString("ID");
		
		webview.setWebViewClient(new WebViewClient() { 

			

		    @Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				 if(url.startsWith("temboo://")) {
			          //  handled = true;
			           // We got forwarded here from the 3rd party OAuth approval page; proceed
			           // to next step
			           Log.i("Temboo", "Got callback!");
			           Intent i = new Intent(getBaseContext(), FaceBookActivity.class);
			           i.putExtra("callbackID", id);
			           startActivity(i); 
			        }
				
			}

			
		});

		webview.loadUrl(url1);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.call_back, menu);
		return true;
	}

}

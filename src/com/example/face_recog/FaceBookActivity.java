package com.example.face_recog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.face_recog.LinkedinDialog.OnVerifyListener;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.enumeration.SearchParameter;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Address;
import com.google.code.linkedinapi.schema.ApiStandardProfileRequest;
import com.google.code.linkedinapi.schema.Education;
import com.google.code.linkedinapi.schema.Educations;
import com.google.code.linkedinapi.schema.Headers;
import com.google.code.linkedinapi.schema.Location;
import com.google.code.linkedinapi.schema.Person;
import com.google.code.linkedinapi.schema.Position;
import com.google.code.linkedinapi.schema.SiteStandardProfileRequest;
import com.google.code.linkedinapi.schema.ThreeCurrentPositions;
import com.google.code.linkedinapi.schema.xpp.ApiStandardProfileRequestImpl;
import com.temboo.Library.Facebook.OAuth.FinalizeOAuth;
import com.temboo.Library.Facebook.OAuth.FinalizeOAuth.FinalizeOAuthInputSet;
import com.temboo.Library.Facebook.OAuth.FinalizeOAuth.FinalizeOAuthResultSet;
import com.temboo.Library.Facebook.OAuth.InitializeOAuth;
import com.temboo.Library.Facebook.OAuth.InitializeOAuth.InitializeOAuthInputSet;
import com.temboo.Library.Facebook.OAuth.InitializeOAuth.InitializeOAuthResultSet;
import com.temboo.Library.Facebook.Reading.Permissions;
import com.temboo.Library.Facebook.Reading.Permissions.PermissionsInputSet;
import com.temboo.Library.Facebook.Reading.Permissions.PermissionsResultSet;
import com.temboo.Library.Facebook.Searching.FQL;
import com.temboo.Library.Facebook.Searching.FQL.FQLInputSet;
import com.temboo.Library.Facebook.Searching.FQL.FQLResultSet;
import com.temboo.Library.Google.Plus.People.Search;
import com.temboo.Library.Google.Plus.People.Search.SearchInputSet;
import com.temboo.Library.Google.Plus.People.Search.SearchResultSet;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

public class FaceBookActivity extends Activity {

	public static ArrayList<Param> resultList = new ArrayList<Param>();
	Handler handler;
	public static ArrayList<Param> res = new ArrayList<Param>();
	ArrayList<String> tags = new ArrayList<String>();
	ArrayList<String> conf = new ArrayList<String>();
	ArrayList<String> urls = new ArrayList<String>();
    Handler hand;
    ImageButton button1;
    ImageButton button2;
    ImageButton button3;
    Button cancel;
    SharedPreferences settings;
    String PREF_FILE_NAME = "filename";
    private static String authURL;
    TextView text1,text2,text3;
    WebView webview;
	Param parm = null;
	private LinkedInOAuthService oAuthService;
	private LinkedInApiClientFactory factory;
	private LinkedInRequestToken liToken;
	private LinkedInApiClient client;
	ImageButton googleplus;
	ImageButton facebk;
	ImageButton twitter;
	  ProgressDialog progressDialog;
	  ProgressDialog progressDialog1;
	OAuthService service;
	Token requestToken;
	 Person p;
	String name;
	IntentFilter ifilter;
	Intent batteryStatus;
	float batteryPct;


	  RekoSDK.APICallback callback = new RekoSDK.APICallback(){
	 		public void gotResponse(String sResponse){
	 			
	 			
	 			JSONObject object;
				try {
					object = new JSONObject(sResponse);
					Log.d("face", sResponse);
					JSONArray arr = object.getJSONArray("visualization");
					for(int i =0;i<arr.length();i++)
					{
					  urls.add(arr.getJSONObject(i).optString("url"));
					}
					Log.d("length", String.valueOf(urls.size()));
					Message msg = new Message();
					Bundle b= new Bundle();
					b.putStringArrayList("TEXT", urls);
					msg.setData(b);
					hand.sendMessage(msg);
				

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 			}
	 		};
	 		
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int layout_name = getIntent().getExtras().getInt("LAYOUT");
		
		if(layout_name==0)
		{
		setContentView(R.layout.rekognition);
		}
		else
		{
			setContentView(R.layout.activity_face_book);	
		}
		
		button1 = (ImageButton)findViewById(R.id.button1);
		button2 = (ImageButton)findViewById(R.id.button2);
		button3 = (ImageButton)findViewById(R.id.button3);

		TextView heading = (TextView)findViewById(R.id.text1);
		heading.setTypeface(null,Typeface.BOLD_ITALIC);
		text1 = (TextView) findViewById(R.id.text2);
		text2 = (TextView) findViewById(R.id.text3);
		text3 = (TextView) findViewById(R.id.text4);
		
		text1.setTypeface(null,Typeface.BOLD_ITALIC);
		text2.setTypeface(null,Typeface.BOLD_ITALIC);
		text3.setTypeface(null,Typeface.BOLD_ITALIC);
		ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		batteryStatus = this.registerReceiver(null, ifilter);

		

		//settings = getSharedPreferences(PREF_FILE_NAME, 0);
		//Set<String> set = settings.getStringSet("CONF",new HashSet<String>());
		//Set<String> set1 = settings.getStringSet("TAG",new HashSet<String>());
		int decider = getIntent().getExtras().getInt("DECIDER");
		
		
		tags = getIntent().getStringArrayListExtra("TAG");
        conf = getIntent().getStringArrayListExtra("CONF");
		//tags = new ArrayList(set1);
        //conf = new ArrayList(set);
        
       
        String [] name_split = tags.get(0).split("_");
        text1.setText(name_split[0]+" "+name_split[1]+"\n "+"CONFIDENCE= "+conf.get(0));
        
        name_split = tags.get(1).split("_");
        text2.setText(name_split[0]+" " +name_split[1]+"\n "+"CONFIDENCE= "+conf.get(1));
        
        name_split = tags.get(2).split("_");
        text3.setText(name_split[0]+" "+name_split[1]+"\n "+"CONFIDENCE= "+conf.get(2));
        
       
        
        
       String [] tag = {tags.get(0),tags.get(1),tags.get(2)};
        
      //  Log.d("tag", tags.get(0)+ tags.get(1) + tags.get(2));
        
       if(decider==0)
		{
        RekoSDK.face_visualize(tag, callback);
		}
       else
       {
    	   ArrayList<String> server_urls = new ArrayList<String>();
    	   server_urls = getIntent().getStringArrayListExtra("URL");
    	new Download_Images(button1).execute(server_urls.get(0));
   		new Download_Images(button2).execute(server_urls.get(1));
   		new Download_Images(button3).execute(server_urls.get(2)); 
    	   
       }
        if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                }
        
	
        
hand = new Handler(new Handler.Callback() {

	@Override
	public boolean handleMessage(Message msg) {
		
		
		ArrayList<String> url = msg.getData().getStringArrayList("TEXT");
		Log.d("url", url.get(1));
    	new Download_Images(button1).execute(url.get(0));
		new Download_Images(button2).execute(url.get(1));
		new Download_Images(button3).execute(url.get(2));
		return false;
		// TODO Auto-generated method stub

	}
});




		
		Log.d("package", getPackageName());
		name = getIntent().getExtras().getString("NAME");
		
		
		
	
		 button1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				

				init_popup(tags.get(0));
			}
		});
		 
		 button2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					init_popup(tags.get(1));
				}
			});
		 
		 button3.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					init_popup(tags.get(2));
				}
			});
  
		
		  
		  

		  }


		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.face_book, menu);
		return true;
	}

	class GooglePlus extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
	    	TembooSession session = null;
			try {
				session = new TembooSession("shashank007", "myFirstApp", "12a5b8d6394e4ecaac69ae52b503ed08");
			} catch (TembooException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	Search searchChoreo = new Search(session);

	    	// Get an InputSet object for the choreo
	    	SearchInputSet searchInputs = searchChoreo.newInputSet();

	    	// Set inputs
	    	searchInputs.set_ClientSecret("mlB1hWgFDcni7HTOtSjwaf4l");
	    	searchInputs.set_Query(params[0]);
	    	searchInputs.set_RefreshToken("1/4XmhEKpByZnHtOX7I-wDqDEQJRRzdWEaJSXklLLYKuE");
	    	searchInputs.set_ClientID("954609926996-q2gqp57vu92ngjf8jqbf8kmgebccidl6.apps.googleusercontent.com");

	    	// Execute Choreo
	    	try {
				SearchResultSet searchResults = searchChoreo.execute(searchInputs);
				String responseData = searchResults.get_Response();
				return responseData;
			} catch (TembooException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}      
			return null;
		}
		
		

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog1 =  new ProgressDialog(FaceBookActivity.this);
			progressDialog1.setMessage("Please Wait...");
			progressDialog1.setTitle("Retreiving Data From Google+");
			progressDialog1.setCancelable(true);
			progressDialog1.show();
		}



		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog1.dismiss();
			Log.d("rest", result);
			JSONObject nytJSON;
			res= new ArrayList<Param>();
			try {
				nytJSON = new JSONObject(result);
				JSONArray firstResult = nytJSON.getJSONArray("items");
				for(int i=0; i<firstResult.length(); i++)
				{
				JSONObject object = firstResult.getJSONObject(i);
				String url = object.optString("url");
				String name = object.optString("displayName");
				String pic = object.getJSONObject("image").optString("url");
				String type = object.optString("objectType");
				parm = new Param(url, name, pic, type);
				res.add(parm);
				}
				Intent intent = new Intent(FaceBookActivity.this, NamesActivity.class);
    			intent.putParcelableArrayListExtra("NAMES", res);
    			intent.putExtra("CHOOSE_DROP", "GOOGLE+");
    			intent.putExtra("DECIDE", "Google+");
    			startActivity(intent);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}
		
		
	}
	void init_popup(String person){
		 
		LayoutInflater inflater = (LayoutInflater) FaceBookActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View layout = inflater.inflate(R.layout.pop_up,
				(ViewGroup) findViewById(R.id.popup_element));
				

       final PopupWindow pwindo = new PopupWindow(layout, 600, 850, true);
       pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
       
       final String [] persons = person.split("_");
       facebk = (ImageButton) layout.findViewById(R.id.imageButton1);
       googleplus = (ImageButton) layout.findViewById(R.id.imageBt2);
       twitter = (ImageButton) layout.findViewById(R.id.imageButton3);
        cancel = (Button) layout.findViewById(R.id.button12);
        ImageButton linkedin = (ImageButton) layout.findViewById(R.id.linkedin);

       Log.d("test", "test: " );
       
       googleplus.setOnClickListener(new View.OnClickListener() {
   		
   		@Override
   		public void onClick(View arg0) {
   			// TODO Auto-generated method stub
   			
                  new GooglePlus().execute(persons[0]+" "+persons[1]);
   			
   			  
   			}
   	});
       
       linkedin.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			linkedInLogin(persons[0], persons[1]);
		}
	});

          twitter.setOnClickListener(new View.OnClickListener() {
      		
      		@Override
      		public void onClick(View arg0) {
      			// TODO Auto-generated method stub
      			
      			new Twitter().execute(persons[0]+" "+persons[1]);
      		}
      	});
          

          facebk.setOnClickListener(new View.OnClickListener() {
      		
      		@Override
      		public void onClick(View arg0) {
      			// TODO Auto-generated method stub
      			
      			new Facebook().execute(persons[0]+" "+persons[1]);
      		}
      	});
          
          cancel.setOnClickListener(new View.OnClickListener() {
  			
  			@Override
  			public void onClick(View v) {
  				// TODO Auto-generated method stub
  				
  				pwindo.dismiss();
  			}
  		});
          
		
	 }
	
	class Twitter extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			TembooSession session;
			try {
				session = new TembooSession("shashank007", "myFirstApp", "12a5b8d6394e4ecaac69ae52b503ed08");
				com.temboo.Library.Twitter.Users.Search searchChoreo = new com.temboo.Library.Twitter.Users.Search(session);
				// Get an InputSet object for the choreo
				com.temboo.Library.Twitter.Users.Search.SearchInputSet searchInputs= searchChoreo.newInputSet();
				// Set inputs
				searchInputs.set_AccessToken("51787768-jYPlKDpmsStbuNyRwQFHHB3K6P1g4vc59jKz74JP9");
				searchInputs.set_AccessTokenSecret("1Ri4NyIjykn7jGaPDv853CZCoqLySd3MpYuwfEkD9ibUx");
				searchInputs.set_ConsumerSecret("t6hwPaYKgpkcLS24gsBJGOO1YBA7KRIRD6iuPW0qE");
				searchInputs.set_ConsumerKey("2kFtJiimdPH7qH4y2GxUA");
				searchInputs.set_SearchString(arg0[0]);
				searchInputs.set_IncludeEntities(true);
			

				// Execute Choreo
				com.temboo.Library.Twitter.Users.Search.SearchResultSet searchResults = searchChoreo.execute(searchInputs);
				String responseData = searchResults.get_Response();
				return responseData;
			} catch (TembooException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
			} 

			return null;

			
		}

		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog1 =  new ProgressDialog(FaceBookActivity.this);
			progressDialog1.setMessage("Please Wait...");
			progressDialog1.setTitle("Retreiving Data From Twitter");
			progressDialog1.setCancelable(true);
			progressDialog1.show();
		}


		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			Log.d("twit", result);
			progressDialog1.dismiss();
			res = new ArrayList<Param>() ;
			JSONArray nytJSON;
			try {
				nytJSON = new JSONArray(result);
				for(int i=0; i<nytJSON.length(); i++)
				{
				String url = nytJSON.getJSONObject(i).optString("profile_banner_url");
				String name = nytJSON.getJSONObject(i).optString("name");
				String pic = nytJSON.getJSONObject(i).optString("profile_image_url");
				String type = nytJSON.getJSONObject(i).optString("screen_name");
				String location = nytJSON.getJSONObject(i).optString("location");
				String description = nytJSON.getJSONObject(i).optString("description");
				Log.d("url", url);
				parm = new Param("https://twitter.com/"+type, name, pic, location,description);
				res.add(parm);
				}
				int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

				float batteryPct1 = level / (float)scale;
				Log.d("batt",String.valueOf(batteryPct));
				Log.d("batt",String.valueOf(batteryPct1));

				
				Intent intent = new Intent(FaceBookActivity.this, NamesActivity.class);
    			intent.putParcelableArrayListExtra("NAMES", res);
    			intent.putExtra("CHOOSE_DROP", "TWITTER");
    			intent.putExtra("DECIDE", "Twitter");
    			startActivity(intent);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		} 
		
		
	}
	
	
	class Facebook extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			TembooSession session;
				// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
				try {
					session = new TembooSession("shashank007", "myFirstApp", "12a5b8d6394e4ecaac69ae52b503ed08");
					FQL fQLChoreo = new FQL(session);
					
					// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
					// TembooSession session = new TembooSession("shashank007", "APP_NAME", "APP_KEY");
					/*InitializeOAuth initializeOAuthChoreo = new InitializeOAuth(session);

					// Get an InputSet object for the choreo
					InitializeOAuthInputSet initializeOAuthInputs = initializeOAuthChoreo.newInputSet();

					// Set inputs
					initializeOAuthInputs.set_AppID("607182542708086");
					initializeOAuthInputs.set_Scope("user_checkins,friends_about_me,user_education_history");

					// Execute Choreo
					InitializeOAuthResultSet initializeOAuthResults = initializeOAuthChoreo.execute(initializeOAuthInputs);
					// Get an InputSet object for the choreo
					
					FinalizeOAuth finalizeOAuthChoreo = new FinalizeOAuth(session);
					
					String callback_id = initializeOAuthResults.get_CallbackID();
					String callback_url = initializeOAuthResults.get_AuthorizationURL();
					
					Intent callback = new Intent(FaceBookActivity.this,CallBackActivity.class);
					callback.putExtra("ID", callback_id);
					callback.putExtra("URL", callback_url);
					startActivity(callback);

					
					Log.d("access",callback_id);
					
 
				//	String callback_id = initializeOAuthResults.
					// Get an InputSet object for the choreo
					FinalizeOAuthInputSet finalizeOAuthInputs = finalizeOAuthChoreo.newInputSet();

					
					// Set inputs
					finalizeOAuthInputs.set_CallbackID(callback_id);
					finalizeOAuthInputs.set_AppSecret("b0c28bfbb8b2470344555afc12a77728");
					finalizeOAuthInputs.set_AppID("607182542708086");

					// Execute Choreo
					FinalizeOAuthResultSet finalizeOAuthResults = finalizeOAuthChoreo.execute(finalizeOAuthInputs);
					
					String access_token = finalizeOAuthResults.get_AccessToken();
					
					*/
					
					
				//	Log.d("access",access_token);
					
					FQLInputSet fQLInputs = fQLChoreo.newInputSet();

					// Set inputs
					//fQLInputs.set_Conditions("name='"+arg0[0]+ "'");
					fQLInputs.set_Conditions("uid in(SELECT id FROM profile WHERE name='"+arg0[0]+ "')");
					fQLInputs.set_Fields("locale,profile_url,username,pic_with_logo,first_name,last_name");
					//fQLInputs.set_AccessToken("CAAIoOrPFnXYBANhQ5nc7tTeSRd1mZBgfKkkumlps3N19B6bkfuxDN8e7EKqkX3aX81QiFbx3IBqnevfAOy1gEvSZBMqQxvw0THuLyloaEbhNu19fNYpzycCMqWaNFVCZCe9gd71ojZCQY8YtIy8ylXZBWQ3DXMeOqZCte1KOICDK981ZCBaw4z693xWZBxgYZAekZD");
					fQLInputs.set_AccessToken("CAAIoOrPFnXYBAKOYWeNtuUJU50sybj8ovGxMB1lBasZBi63VZBNLtyZAsIEXVHzqUQjZAI4BYyao5qHbTHkO0Xyrbr3c3uhhZBhDfRj7X3KX6NrEBZCTG3iGbhLTipKeStaDSUdMgAQj3ZAWIU8Cej8ZCC747n4ERlZASx81lGL8qnQjXxtxcB5RxdtoNy2kwNqQZD");
					//fQLInputs.set_AccessToken(access_token);
					fQLInputs.set_Table("user");
				
					

					// Execute Choreo
					FQLResultSet fQLResults = fQLChoreo.execute(fQLInputs);
					return fQLResults.get_Response();
				} catch (TembooException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			return null;

			
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			Log.d("twit", result);
			progressDialog1.dismiss();
		    res = new ArrayList<Param>() ;

			JSONObject nytJSON;
			try {
				nytJSON = new JSONObject(result);
				JSONArray arr = nytJSON.getJSONArray("data");
				for(int i=0; i<arr.length(); i++)
				{
				String url = arr.getJSONObject(i).optString("profile_url");
				Log.d("url1",url);
				String first_name = arr.getJSONObject(i).optString("first_name");
				String last_name = arr.getJSONObject(i).optString("last_name");
				String name = first_name + " " + last_name;
				String pic = arr.getJSONObject(i).optString("pic_with_logo");
				String locale = arr.getJSONObject(i).optString("locale");
				String username = arr.getJSONObject(i).optString("username");
				String type = "locale= " + locale + " ,username= " + username;
				Log.d("type", type);
				parm = new Param(url, name, pic, locale,username);
				res.add(parm);
				}
				Intent intent = new Intent(FaceBookActivity.this, NamesActivity.class);
    			intent.putParcelableArrayListExtra("NAMES", res);
    			intent.putExtra("CHOOSE_DROP", "FACEBOOK");
    			intent.putExtra("DECIDE", "Facebook");
    			startActivity(intent);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog1 =  new ProgressDialog(FaceBookActivity.this);
			progressDialog1.setMessage("Please Wait...");
			progressDialog1.setTitle("Retreiving Data From Facebook");
			progressDialog1.setCancelable(true);
			progressDialog1.show();
		} 
		
		
	}
		
	
	
	private void linkedInLogin(final String firstname,final String lastname)
	
	
	{
		Intent stateUpdate = new Intent("com.quicinc.Trepn.UpdateAppState");
		stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value", 4);
		stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value.Desc", "LinkedIn_Search()");
		sendBroadcast(stateUpdate); 

		ProgressDialog progressDialog = new ProgressDialog(FaceBookActivity.this);//.show(LinkedInSampleActivity.this, null, "Loadong...");

		LinkedinDialog d = new LinkedinDialog(FaceBookActivity.this, progressDialog);
		d.show();

		//set call back listener to get oauth_verifier value
		d.setVerifierListener(new OnVerifyListener()
		{
			public void onVerify(String verifier)
			{
				try
				{
					Log.d("LinkedinSample", "verifier: " + verifier);

					LinkedInAccessToken accessToken = LinkedinDialog.oAuthService.getOAuthAccessToken(LinkedinDialog.liToken, verifier); 
					LinkedInApiClient client = LinkedinDialog.factory.createLinkedInApiClient(accessToken);
					
					 Map<SearchParameter, String> searchParameters = new EnumMap<SearchParameter, String>(SearchParameter.class);
					 searchParameters.put(SearchParameter.FIRST_NAME, firstname);
					 searchParameters.put(SearchParameter.LAST_NAME, lastname);
					 com.google.code.linkedinapi.schema.People people = client.searchPeople(searchParameters, EnumSet.of(ProfileField.IM_ACCOUNTS,ProfileField.FIRST_NAME, ProfileField.LAST_NAME, ProfileField.ID, ProfileField.HEADLINE,ProfileField.PICTURE_URL,ProfileField.PUBLIC_PROFILE_URL,ProfileField.ASSOCIATIONS,ProfileField.EDUCATIONS,ProfileField.API_STANDARD_PROFILE_REQUEST,ProfileField.EDUCATIONS_SCHOOL_NAME,ProfileField.API_STANDARD_PROFILE_REQUEST_HEADERS,ProfileField.LOCATION_NAME,ProfileField.THREE_CURRENT_POSITIONS,ProfileField.LOCATION,ProfileField.MAIN_ADDRESS,ProfileField.SUMMARY,ProfileField.SKILLS,ProfileField.INDUSTRY,ProfileField.INTERESTS,ProfileField.HONORS));
					 res = new ArrayList<Param>() ;
					 Log.d("TESTING" ,"check");
					 
					 for (Person person : people.getPersonList()) {
					
							
						 Log.d("TESTING" ,"check");
						Address addres = person.getLocation().getAddress();
						
						 String addr = null;
						if(addres ==null)
						{
							addr = "NOT AVAILABLE";
						}
						else
						{
						 addr = addres.getStreet1()+"," + addres.getStreet2() + "," + addres.getCity()+ ","+ addres.getState()+"," + addres.getPostalCode()+ "," + addres.getCountryCode(); 
						}
						 // String addr = "blah";
						 Educations education = person.getEducations();
						 String educa;
						 Log.d("TESTING" ,"check");
						 if(education ==null)
						 {
							 educa ="NOT AVAILABLE"; 
						 }
						
						
						 else
						 {
							 List<Education> edulist = education.getEducationList();
						 educa = edulist.get(0).getSchoolName() + ", " + edulist.get(0).getStartDate() + " - " +  edulist.get(0).getStartDate() + " , " + edulist.get(0).getDegree();
						 }	 
						 ThreeCurrentPositions pos = person.getThreeCurrentPositions();
						 Log.d("TESTING" ,"check");
						 
						 String company=null;
						 if(pos ==null)
						 {
							 company ="NOT AVAILABLE";
						 }
						 else
						 {
							 List<Position> pos_list  = pos.getPositionList();
							 Log.d("company_list", pos_list.toString());
							 if(pos_list.isEmpty())
							 {
								 company ="NOT AVAILABLE"; 
							 }
							 else
							 {
						 company =pos_list.get(0).getCompany().getName() + "  ,  " + pos_list.get(0).getDescription() + "  , " + pos_list.get(0).getSkillsAndExperience();
							  Log.d("company",company);
							 }
							 }
				
						 parm = new Param(person.getPublicProfileUrl(), person.getFirstName()+" "+person.getLastName(), person.getPictureUrl(), person.getHeadline(),person.getSummary(),person.getLocation().getName(),person.getIndustry(),person.getSpecialties());
							res.add(parm);
						 //Log.d("sahana" ,person.getId() + ":" + person.getFirstName() + " " + person.getLastName() + ":" + person.getHeadline());
			        }
					 
					    Intent intent = new Intent(FaceBookActivity.this, NamesActivity.class);
		    			intent.putParcelableArrayListExtra("NAMES", res);
		    			intent.putExtra("CHOOSE_DROP", "LINKEDIN");
		    			intent.putExtra("DECIDE", "Linkedin");
		    			startActivity(intent);

					Log.d("LinkedinSample", "ln_access_token: " + accessToken.getToken());
					Log.d("LinkedinSample", "ln_access_token: " + accessToken.getTokenSecret());
				}
				catch (Exception e) 
				{
					Log.d("LinkedinSample", "error to get verifier");
					e.printStackTrace();
					//Log.d("error",e.getMessage());
				}
			}
		});

		//set progress dialog 
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(true);
		progressDialog.show();
	}
	
	
	
}

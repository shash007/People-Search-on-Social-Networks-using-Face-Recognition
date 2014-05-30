package com.example.face_recog;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.relocation.entity.mime.MultipartEntity;
import com.mashape.relocation.entity.mime.content.ByteArrayBody;
import com.mashape.relocation.entity.mime.content.FileBody;
import com.mashape.relocation.entity.mime.content.StringBody;
import com.mashape.relocation.impl.nio.client.DefaultHttpAsyncClient;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.MultipartBody;

import android.R.string;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Textbox extends Activity {
	
	ProgressDialog dialog;
	byte [] ba;
	EditText edit;
	String text1;
	String path;

	 RekoSDK.APICallback callback = new RekoSDK.APICallback(){
	 		public void gotResponse(String sResponse){	 			
	 			Log.d("resp", sResponse);
	 			}
	 		};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_textbox);
		
		final EditText edit = (EditText) findViewById(R.id.editText1);
		Button submit = (Button)findViewById(R.id.button1);
	    ba = getIntent().getExtras().getByteArray("IMAGE");
	    path = getIntent().getExtras().getString("PATH");
		
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Editable text = edit.getText();
				text1 = text.toString();
				
				if(text1.isEmpty())
				{
					//Toast.makeText(Textbox.this, "Please enter a name", Toast.LENGTH_LONG);
				}
				else
				{
					new Recognize_task().execute();
				}
					
			}
		});
	}

	class Recognize_task extends AsyncTask<Void, Void, String> {
		@SuppressWarnings("unused")
		@Override
		protected String doInBackground(Void... unsued) {
			
			ByteArrayBody body;
			HttpResponse response = null;
				
			String [] out = text1.split(" ");
			RekoSDK.face_add(out[0]+"_"+out[1],ba , callback);
		//	RekoSDK.face

		/*	Log.d("entry", text1);
			Log.d("entry", path);
			


				try {
					HttpResponse<JsonNode> request = Unirest.post("https://face.p.mashape.com/faces/detect?api_key=6a87b136103b4ed488ce02867d7fe17d&api_secret=3c28e43d75424d2889957f790dfd2d04")
							  .header("X-Mashape-Authorization", "38up88MoFOas7YVy18lAyocHMO27V6Yv")
							  .field("detector", "Aggressive")
							  .field("attributes", "all")
							  .field("files", new File(path))
							  .asJson();
					InputStream is = request.getRawBody();
					
					BufferedReader br = null;
					StringBuilder sb = new StringBuilder();
			 
					String line;
			 
						br = new BufferedReader(new InputStreamReader(is));
						while ((line = br.readLine()) != null) {
							sb.append(line);
						}
			 
					JSONObject nytJSON;
					nytJSON = new JSONObject(sb.toString());
					Log.d("entry", sb.toString());
					String subject_id = nytJSON.getJSONArray("images").getJSONObject(0).optString("image_id");
					String topleftx = nytJSON.getJSONArray("images").getJSONObject(0).getJSONArray("faces").getJSONObject(0).getString("topLeftX");
					String toplefty = nytJSON.getJSONArray("images").getJSONObject(0).getJSONArray("faces").getJSONObject(0).getString("topLeftY");
					String width = nytJSON.getJSONArray("images").getJSONObject(0).getJSONArray("faces").getJSONObject(0).getString("width");
					String height = nytJSON.getJSONArray("images").getJSONObject(0).getJSONArray("faces").getJSONObject(0).getString("height");

					Log.d("entry", subject_id);
					Log.d("entry", topleftx);
					Log.d("entry", toplefty);
					Log.d("entry", width + height);


		
					
					JSONArray firstResult = nytJSON.getJSONArray("items");
				

				} catch (UnirestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	*/			
		
			return null;
			// (null);
		}
					
		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
		
					//dialog.dismiss();
	 		Toast.makeText(Textbox.this, "Successfully Added", Toast.LENGTH_LONG).show();

		}

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.textbox, menu);
		return true;
	}

}

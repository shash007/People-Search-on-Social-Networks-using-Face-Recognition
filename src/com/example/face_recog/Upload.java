package com.example.face_recog;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Upload extends Activity {
	private static final int PICK_IMAGE = 1;
	private static final int PICK_Camera_IMAGE = 2;
	private ImageView imgView;
	private Button upload,cancel;
	private Bitmap bitmap;
	private ProgressDialog dialog;
	Uri imageUri;
	MediaPlayer mp=new MediaPlayer();
	    static InputStream inputStream;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_upload);

	//imgView = (ImageView) findViewById(R.id.ImageView);
	upload = (Button) findViewById(R.id.btnUpload);
	cancel = (Button) findViewById(R.id.btnCancel);
	upload.setOnClickListener(new View.OnClickListener() {

	public void onClick(View v) {
		try {
			Intent gintent = new Intent();
			gintent.setType("video/*");
			gintent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
			Intent.createChooser(gintent, "Select Picture"),
			PICK_IMAGE);
			} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
			e.getMessage(),
			Toast.LENGTH_LONG).show();
			Log.e(e.getClass().getName(), e.getMessage(), e);
			}
	dialog = ProgressDialog.show(Upload.this, "Uploading",
	"Please wait...", true);
	new ImageGalleryTask().execute();
	}
	
	});

	cancel.setOnClickListener(new OnClickListener() {
	public void onClick(View v) {
	// TODO Auto-generated method stub
	Upload.this.finish();
	}
	});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	                 MenuInflater inflater = getMenuInflater();
	                 inflater.inflate(R.menu.upload, menu);
	                 return true;
	        }
	    
	//@Override
	/*public boolean onOptionsItemSelected(MenuItem item) {
	       
	       /*case R.id.camera:
	       	//define the file-name to save photo taken by Camera activity
	       	String fileName = "new-photo-name.jpg";
	       	//create parameters for Intent with filename
	       	ContentValues values = new ContentValues();
	       	values.put(MediaStore.Images.Media.TITLE, fileName);
	       	values.put(MediaStore.Images.Media.DESCRIPTION,"Image captured by camera");
	       	//imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
	       	imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	       	//create new Intent
	       	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	       	intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
	       	intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
	       	startActivityForResult(intent, PICK_Camera_IMAGE);
	           return true;
	       
	      
	    }*/

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	Uri selectedImageUri = null;
	String filePath = null;
	switch (requestCode) {
	case PICK_IMAGE:
	if (resultCode == Activity.RESULT_OK) {
	selectedImageUri = data.getData();
	}
	break;
	case PICK_Camera_IMAGE:
	if (resultCode == RESULT_OK) {
	       //use imageUri here to access the image
	   	selectedImageUri = imageUri;
	   	/*Bitmap mPic = (Bitmap) data.getExtras().get("data");
	selectedImageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), mPic, getResources().getString(R.string.app_name), Long.toString(System.currentTimeMillis())));*/
	   } else if (resultCode == RESULT_CANCELED) {
	       Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
	   } else {
	   	Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
	   }
	break;
	}
	if(selectedImageUri != null){
	try {
	// OI FILE Manager
	String filemanagerstring = selectedImageUri.getPath();
	// MEDIA GALLERY
	String selectedImagePath = getPath(selectedImageUri);
	if (selectedImagePath != null) {
	filePath = selectedImagePath;
	} else if (filemanagerstring != null) {
	filePath = filemanagerstring;
	} else {
	Toast.makeText(getApplicationContext(), "Unknown path",
	Toast.LENGTH_LONG).show();
	Log.e("Bitmap", "Unknown path");
	}
	if (filePath != null) {
	decodeFile(filePath);
	} else {
	bitmap = null;
	}
	} catch (Exception e) {
	Toast.makeText(getApplicationContext(), "Internal error",
	Toast.LENGTH_LONG).show();
	Log.e(e.getClass().getName(), e.getMessage(), e);
	}
	}
	}

	class ImageGalleryTask extends AsyncTask<Void, Void, String> {
	@SuppressWarnings("unused")
	@Override
	protected String doInBackground(Void... unsued) {
	InputStream is;
	   BitmapFactory.Options bfo;
	   Bitmap bitmapOrg;
	   ByteArrayOutputStream bao ;
	  
	   bfo = new BitmapFactory.Options();
	   bfo.inSampleSize = 2;
	   //bitmapOrg = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + customImage, bfo);
	     
	   bao = new ByteArrayOutputStream();
	   //bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
	byte [] ba = bao.toByteArray();
	//changed from encodeBytes(ba)
	String ba1 = Base64.encodeToString(ba, 0);
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	nameValuePairs.add(new BasicNameValuePair("image",ba1));
	nameValuePairs.add(new BasicNameValuePair("cmd","image_android"));
	Log.v("log_tag", System.currentTimeMillis()+".mp4");	      
	try{
	       HttpClient httpclient = new DefaultHttpClient();
	       HttpPost httppost = new 
	                      //  Here you need to put your server file address
	       HttpPost("http://192.168.56.1/uploadimage/upload1.php");
	       httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	       HttpResponse response = httpclient.execute(httppost);
	       HttpEntity entity = response.getEntity();
	       is = entity.getContent();
	       Log.v("log_tag", "In the try Loop" );
	       String response_string = convertResponseToString(response);
	       return response_string;  
	}catch(Exception e){
	       Log.v("log_tag", "Error in http connection "+e.toString());
	  }
	return null;
	// (null);
	}

	@Override
	protected void onProgressUpdate(Void... unsued) {

	}

	@Override
	protected void onPostExecute(String sResponse) {
	try {
	if (dialog.isShowing())
	dialog.dismiss();
	Toast.makeText(Upload.this, "Response " + sResponse, Toast.LENGTH_LONG).show();

	} catch (Exception e) {
	Toast.makeText(getApplicationContext(),
	e.getMessage(),
	Toast.LENGTH_LONG).show();
	Log.e(e.getClass().getName(), e.getMessage(), e);
	}
	}

	}

	public String getPath(Uri uri) {
	String[] projection = { MediaStore.Images.Media.DATA };
	Cursor cursor = managedQuery(uri, projection, null, null, null);
	if (cursor != null) {
	// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
	// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
	int column_index = cursor
	.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	cursor.moveToFirst();
	return cursor.getString(column_index);
	} else
	return null;
	}

	public void decodeFile(String filePath) {
	// Decode image size
	BitmapFactory.Options o = new BitmapFactory.Options();
	o.inJustDecodeBounds = true;
	BitmapFactory.decodeFile(filePath, o);

	// The new size we want to scale to
	final int REQUIRED_SIZE = 1024;

	// Find the correct scale value. It should be the power of 2.
	int width_tmp = o.outWidth, height_tmp = o.outHeight;
	int scale = 1;
	while (true) {
	if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
	break;
	width_tmp /= 2;
	height_tmp /= 2;
	scale *= 2;
	}

	// Decode with inSampleSize
	BitmapFactory.Options o2 = new BitmapFactory.Options();
	o2.inSampleSize = scale;
	bitmap = BitmapFactory.decodeFile(filePath, o2);

	imgView.setImageBitmap(bitmap);

	}
	public static String convertResponseToString(HttpResponse response) throws IllegalStateException, IOException{
	   	 
	       String res = "";
	       StringBuffer buffer = new StringBuffer();
	       inputStream = response.getEntity().getContent();
	       int contentLength = (int) response.getEntity().getContentLength(); //getting content length…..
	     //  Toast.makeText(MainActivity.this, "contentLength : " + contentLength, Toast.LENGTH_LONG).show();
	       if (contentLength < 0){
	       }
	       else{
	              byte[] data = new byte[512];
	              int len = 0;
	              try
	              {
	                  while (-1 != (len = inputStream.read(data)) )
	                  {
	                      buffer.append(new String(data, 0, len)); //converting to string and appending  to stringbuffer…..
	                  }
	              }
	              catch (IOException e)
	              {
	                  e.printStackTrace();
	              }
	              try
	              {
	                  inputStream.close(); // closing the stream…..
	              }
	              catch (IOException e)
	              {
	                  e.printStackTrace();
	              }
	              res = buffer.toString();     // converting stringbuffer to string…..

	          //    Toast.makeText(MainActivity.this, "Result : " + res, Toast.LENGTH_LONG).show();
	              Log.d("Response" ,"Response => " +  EntityUtils.toString(response.getEntity()));
	       }
	       return res;
	  }
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Upload.this,MainActivity.class);
		  
		   startActivity(intent);
	}

	}
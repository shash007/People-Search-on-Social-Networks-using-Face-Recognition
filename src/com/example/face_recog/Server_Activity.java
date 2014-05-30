package com.example.face_recog;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class Server_Activity extends Fragment{

	private static final int PICK_IMAGE = 1;
	private static final int PICK_Camera_IMAGE = 2;
	private ImageView imgView;
	private Button upload,cancel;
	private Bitmap bitmap;
	SharedPreferences settings;
	String PREF_FILE_NAME = "filename";
	View rootView;
	private ProgressDialog dialog;
	Uri imageUri;
	DataInputStream inStream;
	MediaPlayer mp=new MediaPlayer();
    static InputStream inputStream;
    ArrayList<String> serv_outp ;
    ArrayList<String> people_names;
    ArrayList<String> conf;

 
		
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	rootView = inflater.inflate(R.layout.activity_server_, container, false);
    
    return rootView;
	}

	
public void onActivityCreated(Bundle savedInstanceState) {
// TODO Auto-generated method stub
super.onActivityCreated(savedInstanceState);
		

		imgView = (ImageView) rootView.findViewById(R.id.Face);
		upload = (Button) rootView.findViewById(R.id.upload_btn);
		cancel = (Button) rootView.findViewById(R.id.train_btn);
		
		Activity activity = getActivity();
		settings = activity.getSharedPreferences(PREF_FILE_NAME, 0);
        String arr = settings.getString("IMG_PATH","");
        Log.d("array",arr);
        byte[] bytearr = Base64.decode(arr, Base64.DEFAULT);
        Log.d("array",bytearr.toString());
		bitmap = BitmapFactory.decodeByteArray(bytearr,0, bytearr.length);
		imgView.setImageBitmap(bitmap);
		
		upload.setBackgroundColor(Color.BLUE);
		cancel.setBackgroundColor(Color.RED);
		upload.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (bitmap == null) {
					Toast.makeText(rootView.getContext(),
							"Please select image", Toast.LENGTH_SHORT).show();
				} else {
					dialog = ProgressDialog.show(rootView.getContext(), "Uploading",
							"Please wait...", true);
					new FaceRecognize().execute();
				}
			}
		});
		
		

		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				InputStream is;
			    BitmapFactory.Options bfo;
			    Bitmap bitmapOrg;
			    ByteArrayOutputStream bao ;
			   
			    bfo = new BitmapFactory.Options();
			    bfo.inSampleSize = 2;
			    //bitmapOrg = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + customImage, bfo);
			      
			    bao = new ByteArrayOutputStream();
			    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
				final byte [] ba = bao.toByteArray();
				LayoutInflater inflater = (LayoutInflater) rootView.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View lay = inflater.inflate(R.layout.activity_textbox,
						(ViewGroup) rootView.findViewById(R.id.add));

		       final PopupWindow pwindo = new PopupWindow(lay, 600, 850, true);
		       pwindo.showAtLocation(lay, Gravity.CENTER, 0, 0);
		
		       Button submit = (Button) lay.findViewById(R.id.button1);
		       Button canc = (Button) lay.findViewById(R.id.canc);
		       
		       canc.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					pwindo.dismiss();
					
				}
			});
		       final EditText add_name = (EditText) lay.findViewById(R.id.editText1);
		       submit.setOnClickListener(new View.OnClickListener() {
				
				@SuppressLint("NewApi")
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					Editable edit = add_name.getText();
					String text1 = edit.toString();
					
					if(text1.equals(""))
					{
						Toast.makeText(rootView.getContext(), "Please enter a name", Toast.LENGTH_LONG).show();
					}
					else
					{
						String text2= text1.toLowerCase();
						new FaceTrain().execute(text2);
						pwindo.dismiss();
					}
					
				}
			});
				//Intent intent = new Intent(rootView.getContext(),Textbox.class);
				//intent.putExtra("PATH", filePath);
				//intent.putExtra("IMAGE", ba);
				//startActivity(intent);
				
				
			}
		});
	
		
	}

public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    //   MenuInflater inflater = inf;
            super.onCreateOptionsMenu(menu, inflater);
}
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.camera:
	        	//define the file-name to save photo taken by Camera activity
	        	String fileName = "new-photo-name.jpg";
	        	//create parameters for Intent with filename
	        	ContentValues values = new ContentValues();
	        	values.put(MediaStore.Images.Media.TITLE, fileName);
	        	values.put(MediaStore.Images.Media.DESCRIPTION,"Image captured by camera");
	        	//imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
	        	imageUri = rootView.getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	        	//create new Intent
	        	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	        	intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
	        	intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
	        	startActivityForResult(intent, PICK_Camera_IMAGE);
	            return true;
	        
	        case R.id.gallery:
	        	try {
				Intent gintent = new Intent();
				gintent.setType("image/*");
				gintent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
				Intent.createChooser(gintent, "Select Picture"),
				PICK_IMAGE);
			} catch (Exception e) {
				Toast.makeText(rootView.getContext(),
				e.getMessage(),
				Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
	        	return true;
        }
		return false;
    }

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Uri selectedImageUri = null;
		String filePath = null;
		switch (requestCode) {
				case PICK_IMAGE:
					if (resultCode == Activity.RESULT_OK) {
						selectedImageUri = data.getData();
					}
					break;
				case PICK_Camera_IMAGE:
					 if (resultCode == Activity.RESULT_OK) {
		 		        //use imageUri here to access the image
		 		    	selectedImageUri = imageUri;
		 		    	/*Bitmap mPic = (Bitmap) data.getExtras().get("data");
						selectedImageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), mPic, getResources().getString(R.string.app_name), Long.toString(System.currentTimeMillis())));*/
				    } else if (resultCode == Activity.RESULT_CANCELED) {
		 		        Toast.makeText(rootView.getContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
		 		    } else {
		 		    	Toast.makeText(rootView.getContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
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
							Toast.makeText(rootView.getContext(), "Unknown path",
									Toast.LENGTH_LONG).show();
							Log.e("Bitmap", "Unknown path");
						}
			
						if (filePath != null) {
							decodeFile(filePath);
						} else {
							bitmap = null;
						}
					} catch (Exception e) {
						Toast.makeText(rootView.getContext(), "Internal error",
								Toast.LENGTH_LONG).show();
						Log.e(e.getClass().getName(), e.getMessage(), e);
					}
			}
	
	}

	class FaceRecognize extends AsyncTask<Void, Void, String> {
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
			    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
				byte [] ba = bao.toByteArray();
				String ba1 = com.example.face_recog.Base64.encodeBytes(ba);
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("image",ba1));
				nameValuePairs.add(new BasicNameValuePair("option","1"));
				nameValuePairs.add(new BasicNameValuePair("filename","shashank_hebbale"));
				Log.v("log_tag", System.currentTimeMillis()+".jpg");	       
				try{
				        HttpClient httpclient = new DefaultHttpClient();
				        HttpPost httppost = new 
                      //  Here you need to put your server file address
				        HttpPost("http://10.16.31.209/Face_Reg_Server.php");
				        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				        HttpResponse response = httpclient.execute(httppost);
				        HttpEntity entity = response.getEntity();
				        is = entity.getContent();
				        inStream = new DataInputStream (is);
			              String str;
			              String finalstr ="";
			              serv_outp = new ArrayList<String>();
			              people_names = new ArrayList<String>();
			              conf = new ArrayList<String>();
			              
			              Log.d("inStream",inStream.toString());
			              
			              //str = inStream.readLine();
			              //Log.d("str", str);
			              
			               
			             while (( str = inStream.readLine()) != null)
			              {
			                   Log.d("Debug","Server Response "+str);
			                   String[] splt = str.split(" ");
			                   Log.d("split", splt[1]);
			                   
			                   conf.add(splt[0]);
			                   String[] splt2 = splt[1].split("/");
			                   if(splt2==null)
			                   {
			                	   return "No Face Detected";
			                   }
			                   Log.d("split", splt2[4]);
			                   serv_outp.add("http://10.16.31.209/"+splt2[4]+"/"+splt2[5]);
			                   Log.d("split","http://10.16.31.209/"+splt2[4]+"/"+splt2[5]);
			                   String[] splt3 = splt2[5].split("\\.");
			                   if(splt3==null)
			                   {
			                	   return "No Face Detected";
			                   }
			                   if(splt3[0].matches(".*[0-9].*"))
			                   {
			                	   Log.d("contains","contains");
			                	   String final_names = splt3[0].replaceAll("[0-9]","");
				                   people_names.add(final_names);
			                   }
			                   else
			                   {
			                	   people_names.add(splt3[0]);
			                   }
			                   
			                   
			                   
			                   
			                   //people_names.add(splt2[5]);
			                  // Log.d("split", splt3[1]);
			                   finalstr = finalstr + str;
			              }
			             
			          //  Log.d("first", serv_outp.get(0));
				      //  Log.d("log_tag", finalstr );
				        //String response_string = convertResponseToString(response);
				        //return response_string; 
			             if(people_names.isEmpty())
			             {
			            	 
			            	 Log.d("null", "null");
			            	 return null;
			             }
			             else
			             {
			            	 Log.d("null", "success");
			            	 return "success";
			             }
		                   

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
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Intent stateUpdate = new Intent("com.quicinc.Trepn.UpdateAppState");
			stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value", 8);
			stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value.Desc", "Recognition_Server");
			Server_Activity.this.getActivity().getBaseContext().sendBroadcast(stateUpdate);
		}

		@Override
		protected void onPostExecute(String sResponse) {
			
			Intent stateUpdate = new Intent("com.quicinc.Trepn.UpdateAppState");
			stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value", 9);
			stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value.Desc", "Random()");
			Server_Activity.this.getActivity().getBaseContext().sendBroadcast(stateUpdate);
			
			if (dialog.isShowing())
				dialog.dismiss();
			
			if(sResponse==null)
			{
				Toast.makeText(rootView.getContext(), "No Face Detected ", Toast.LENGTH_LONG).show();

			}
			else
			{
			if(sResponse.equals("success"))
			{
				try {
					
					//Toast.makeText(rootView.getContext(), "Response " + sResponse, Toast.LENGTH_LONG).show();

					Intent intent = new Intent(rootView.getContext(), FaceBookActivity.class);
	    			intent.putStringArrayListExtra("URL", serv_outp);
	    			intent.putStringArrayListExtra("CONF", conf);
	    			intent.putStringArrayListExtra("TAG", people_names);
	    			intent.putExtra("DECIDER", 1);
	    			intent.putExtra("LAYOUT", 1);
	    			startActivity(intent);
	    			
					
				} catch (Exception e) {
					Toast.makeText(rootView.getContext(),
							e.getMessage(),
							Toast.LENGTH_LONG).show();
					Log.e(e.getClass().getName(), e.getMessage(), e);
				}
			}
			else
			{
				Toast.makeText(rootView.getContext(), "No Face Detected ", Toast.LENGTH_LONG).show();

			}
			}
		}

	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = ((Activity) rootView.getContext()).managedQuery(uri, projection, null, null, null);
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

	 class FaceTrain extends AsyncTask<String, Void, String> {
			@SuppressWarnings("unused")
			@Override
			protected String doInBackground(String... arg0) {
					InputStream is;
				    BitmapFactory.Options bfo;
				    Bitmap bitmapOrg;
				    ByteArrayOutputStream bao ;
				   
				    bfo = new BitmapFactory.Options();
				    bfo.inSampleSize = 2;
				    //bitmapOrg = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + customImage, bfo);
				      
				    bao = new ByteArrayOutputStream();
				    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
					byte [] ba = bao.toByteArray();
					String ba1 = com.example.face_recog.Base64.encodeBytes(ba);
					ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("image",ba1));
					nameValuePairs.add(new BasicNameValuePair("option","0"));
					String[] add = arg0[0].split(" ");
					nameValuePairs.add(new BasicNameValuePair("filename",add[0]+"_"+add[1]));
					Log.v("log_tag", System.currentTimeMillis()+".jpg");	       
					try{
					        HttpClient httpclient = new DefaultHttpClient();
					        HttpPost httppost = new 
	                      //  Here you need to put your server file address
					        HttpPost("http://10.16.31.209/Face_Reg_Server.php");
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
					Toast.makeText(rootView.getContext(), "Successfully Added", Toast.LENGTH_LONG).show();

				} catch (Exception e) {
					Toast.makeText(rootView.getContext(),
							e.getMessage(),
							Toast.LENGTH_LONG).show();
					Log.e(e.getClass().getName(), e.getMessage(), e);
				}
			}

		}
}


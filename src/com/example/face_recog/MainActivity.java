package com.example.face_recog;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.brickred.socialauth.provider.MySpaceImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.face_recog.Textbox.Recognize_task;



import android.R.bool;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.util.Log;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Fragment {

	private static final int PICK_IMAGE = 1;
	Uri selectedImageUri;
	private static final int PICK_Camera_IMAGE = 2;
	private ImageView imgView;
	private Button upload,cancel;
	private Bitmap bitmap;
	private ProgressDialog dialog;
	Uri imageUri;
	String output,output1,output2;
	Handler hand,success;
	String selectedImagePath;
	String filePath = null;
	ArrayList<String> outp;
	ArrayList<String> conf;
	SharedPreferences settings;
	JSONArray arr1;
	View rootView;
    String PREF_FILE_NAME = "filename";

	
    static InputStream inputStream;
    
    RekoSDK.APICallback callback = new RekoSDK.APICallback(){
 		public void gotResponse(String sResponse){
 				//Log.d("resp", sResponse);
 			}
 		};
 		
 		  
 	    RekoSDK.APICallback callback3 = new RekoSDK.APICallback(){
 	 		public void gotResponse(String sResponse){
 	 				Log.d("visu", sResponse);
 	 			}
 	 		};
 	 		
 	 		RekoSDK.APICallback Added = new RekoSDK.APICallback(){
 		 		public void gotResponse(String sResponse){	 			
 		 		
 		 			Message msg = new Message();
					Bundle b= new Bundle();
					b.putString("TEXT1", sResponse);
					msg.setData(b);
					success.sendMessage(msg);

 		 			}
 		 		};

    
 		  RekoSDK.APICallback callback2 = new RekoSDK.APICallback(){
 		 		public void gotResponse(String sResponse){
						Log.d("out1", sResponse);
						Message msg = new Message();
						Bundle b= new Bundle();
						b.putString("TEXT", sResponse);
						msg.setData(b);
						hand.sendMessage(msg);
						
				

 		 		}
 		 		};
 
  
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
 		 	            Bundle savedInstanceState) {
	
	
	rootView = inflater.inflate(R.layout.activity_main, container, false);
	
	return rootView;
}	
 		 		
  @Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			
			imgView = (ImageView) rootView.findViewById(R.id.ImageView);
			upload = (Button) rootView.findViewById(R.id.imguploadbtn);
			cancel = (Button) rootView.findViewById(R.id.imgcancelbtn);
			
			Activity activity = getActivity();
			settings = activity.getSharedPreferences(PREF_FILE_NAME, 0);
            String arr = settings.getString("IMG_PATH","");
            byte[] bytearr = Base64.decode(arr, Base64.DEFAULT);
            Log.d("array",bytearr.toString());
			bitmap = BitmapFactory.decodeByteArray(bytearr,0, bytearr.length);
			imgView.setImageBitmap(bitmap);
			

			upload.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					if (bitmap == null) {
						Toast.makeText(rootView.getContext(),
								"Please select image", Toast.LENGTH_SHORT).show();
					} else {
						dialog = ProgressDialog.show(rootView.getContext(), "Uploading",
								"Please wait...", true);
						new Recognize_task().execute();
					}
				}
			});

			success = new Handler(new Handler.Callback() {
				
				@Override
				public boolean handleMessage(Message arg0) {
					// TODO Auto-generated method stub
 		 			Toast.makeText(rootView.getContext(), "Successfully Added", Toast.LENGTH_LONG).show();
					
					return false;
				}
			});

		hand = new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				// TODO Auto-generated method stub
				String res = msg.getData().getString("TEXT");
			    		
				try {
					Log.d("res", res);

						JSONObject object = new JSONObject(res);
						arr1 = object.getJSONArray("face_detection");
						Log.d("array1", arr1.toString());
						
						

						//String [] out1 = new String[1];
						//out1[0] = output;
						//RekoSDK.face_visualize(out1, callback3);
					//	output=		arr.getJSONObject(0).getJSONArray("matches").getJSONObject(0).optString("tag");
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				if(arr1.isNull(0))
				{
					no_images_found();
	 
				}
				else
				{
					JSONArray temp;
					try {
						temp = arr1.getJSONObject(0).getJSONArray("matches");
						if(temp==null)
						{
							no_images_found();
						}
						 outp = new ArrayList<String>();
							conf = new ArrayList<String>();

							int j=0;
							for(int i =0;i<temp.length();i++)
							{
								outp.add(temp.getJSONObject(i).optString("tag"));
								conf.add(temp.getJSONObject(i).optString("score"));
								
							}
							if(outp ==null)
							{
								no_images_found();
							}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			       
				//	Toast.makeText(MainActivity.this, "The person is " + sResponse, Toast.LENGTH_LONG).show(); 
					Toast.makeText(rootView.getContext(), "The Person is " + outp.get(0), Toast.LENGTH_LONG).show();
				   
					Intent intent = new Intent(rootView.getContext(),FaceBookActivity.class);
					intent.putStringArrayListExtra("CONF", conf);
					intent.putStringArrayListExtra("TAG", outp);
					intent.putExtra("LAYOUT", 0);
					intent.putExtra("DECIDER", 0);
					startActivity(intent);
				}
				/*	Editor editor = settings.edit();
					           
			            
			            Set<String> set = new HashSet<String>(conf);
			            Set<String> set1 = new HashSet<String>(outp);

                     editor.putStringSet("CONF", set);
                      editor.putStringSet("TAG", set1);
                      editor.commit();

					Fragment newFragment = new Fragment();
					FragmentTransaction transaction = getFragmentManager().beginTransaction();

					// Replace whatever is in the fragment_container view with this fragment
					transaction.replace(R.layout.activity_face_book, newFragment);

					// Commit the transaction
					transaction.commit();
				 //  new LinkedIn().execute(); 
				*/	
				return false;
			}
		});


			cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
					add_images();
					
					
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
	selectedImageUri = null;
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
					selectedImagePath = getPath(selectedImageUri);
		
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


class Recognize_task extends AsyncTask<Void, Void, String> {
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
			String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
			//String image = getPath(selectedImageUri);
			//Log.d("image", image);
			
			
			RekoSDK.face_train(callback);
			RekoSDK.face_recognize(ba, callback2);
		  
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
		stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value", 2);
		stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value.Desc", "Recognition_Web()");
		MainActivity.this.getActivity().getBaseContext().sendBroadcast(stateUpdate); 




	}

	@Override
	protected void onPostExecute(String sResponse) {
		Intent stateUpdate = new Intent("com.quicinc.Trepn.UpdateAppState");
		stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value", 1);
		stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value.Desc", "Random()");
		MainActivity.this.getActivity().getBaseContext().sendBroadcast(stateUpdate); 

		
		try {
			if (dialog.isShowing())
				dialog.dismiss();
	

		} catch (Exception e) {
			Toast.makeText(rootView.getContext(),
					e.getMessage(),
					Toast.LENGTH_LONG).show();
			Log.e(e.getClass().getName(), e.getMessage(), e);
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

public String getRealPathFromURI(Uri contentUri)
{
    try
    {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = ((Activity) rootView.getContext()).managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    catch (Exception e)
    {
        return contentUri.getPath();
    }
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


public void add_images()
{
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
   final EditText add_name = (EditText) lay.findViewById(R.id.editText1);
   
   canc.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		pwindo.dismiss();
	}
});
   
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
			Log.d("text1", text1);
			String [] name = text1.split(" ");
			Log.d("out",String.valueOf(name[0]));
			RekoSDK.face_add(name[0]+"_"+name[1],ba , Added);
			pwindo.dismiss();
		}
		
	}
});
	//Intent intent = new Intent(rootView.getContext(),Textbox.class);
	//intent.putExtra("PATH", filePath);
	//intent.putExtra("IMAGE", ba);
	//startActivity(intent);
	
}

public void no_images_found()
{
	LayoutInflater inflater = (LayoutInflater) rootView.getContext()
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View lay = inflater.inflate(R.layout.pop_to_add,
			(ViewGroup) rootView.findViewById(R.id.errorlog));
			
			lay.setBackgroundColor(Color.BLUE);

   final PopupWindow pwind = new PopupWindow(lay, 600, 850, true);
   pwind.showAtLocation(lay, Gravity.CENTER, 0, 0);

   Button error = (Button) lay.findViewById(R.id.got_it);
   TextView show_err = (TextView) lay.findViewById(R.id.error_tag);
   show_err.setText("No results found for this Image, Please Click on the Add Button to add this image to the database");
   show_err.setTextColor(Color.RED);
   error.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		pwind.dismiss();
	}
});
}



}


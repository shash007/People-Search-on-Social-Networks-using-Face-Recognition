package com.example.face_recog;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;



import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.FaceDetector;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.media.FaceDetector.Face;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Video;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.style.UpdateAppearance;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Video_Activity extends Activity {
	RadioGroup rgVideoOptions;
	RadioButton rdUploadVideo,rdUploadImage,rdCapture,rgClickImage;
	private static final int UPLOAD_VIDEO = 2;
	private static final int CAPTURE_IMAGE =3;
	private static final int UPLOAD_IMAGE = 4;
	 RelativeLayout rl;
	 int alpha_val=98;
	ImageView iv11;
	Uri bmpUploadImage=null;
    String selectedPath = "";
    Handler hand;
    int cameraData = 0;
    Bitmap bmp=null;
    private static final int PICK_IMAGE = 1;
	private static final int PICK_Camera_IMAGE = 2;
	private ImageView imgView;
	private Button cancel;
	private Bitmap bitmap;
	private ProgressDialog dialog;
	ProgressDialog progressDialog;
	
    
	Drawable bcg; 
    int upload=0;
    public static ArrayList<String> string_path = new ArrayList<String>();
    public static ArrayList<Bitmap> final_result = new ArrayList<Bitmap>();

/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
 
    
    
    setContentView(R.layout.activity_video);
    rl=(RelativeLayout)findViewById(R.id.rlMain);
    rl.setBackgroundResource(R.drawable.bcg);
   
    rgVideoOptions=(RadioGroup)findViewById(R.id.rgVideoOptions);
    rgClickImage=(RadioButton)findViewById(R.id.rdClickImage);
    rdUploadVideo=(RadioButton)findViewById(R.id.rdUploadVideo);
    rdUploadImage=(RadioButton)findViewById(R.id.rdUploadImage);
    rdCapture=(RadioButton)findViewById(R.id.rdCapture);
    rgVideoOptions.setOnCheckedChangeListener(rgListener);
   
    
    hand = new Handler(new Handler.Callback() {

    	@Override
    	public boolean handleMessage(Message msg) {
    		
    		Bitmap b = BitmapFactory.decodeByteArray(msg.getData().getByteArray("ARRAY"),0,msg.getData().getByteArray("ARRAY").length);
    		final_result.add(b);
    		
    		return false;
    		// TODO Auto-generated method stub

    	}
    });
}
   
    //Get Camera for preview
    
    
@Override
public void onBackPressed() {
	System.gc();
	Intent intent = new Intent(Intent.ACTION_MAIN);
	   intent.addCategory(Intent.CATEGORY_HOME);
	   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	   startActivity(intent);
}

RadioGroup.OnCheckedChangeListener rgListener=new OnCheckedChangeListener() {
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.rdCapture:
			bcg = getResources().getDrawable(R.drawable.bcg);

			// setting the opacity (alpha)
			bcg.setAlpha(alpha_val);

			// setting the images on the ImageViews
			rl.setBackgroundDrawable(bcg);
			rdCapture.setTextColor(Color.BLACK);
			Intent intent=new Intent(Video_Activity.this, Capture.class);
			rgVideoOptions.clearCheck();
			startActivity(intent);
			break;
		case R.id.rdUploadVideo:
			
			bcg = getResources().getDrawable(R.drawable.bcg);
			// setting the opacity (alpha)
			bcg.setAlpha(alpha_val);

			// setting the images on the ImageViews
			rl.setBackgroundDrawable(bcg);
			rdUploadVideo.setTextColor(Color.BLACK);
			//Intent intentNew=new Intent(Video_Activity.this, Upload.class);
			//startActivity(intentNew);;
			upload=0;
			
			openGalleryAudio();
			break;
		case R.id.rdClickImage:
			bcg = getResources().getDrawable(R.drawable.bcg);

			// setting the opacity (alpha)
			bcg.setAlpha(alpha_val);

			// setting the images on the ImageViews
			rl.setBackgroundDrawable(bcg);
			rgClickImage.setTextColor(Color.BLACK);
			upload=1;
			
			Intent intentImage = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intentImage,CAPTURE_IMAGE);
			break;
		case R.id.rdUploadImage:
			bcg = getResources().getDrawable(R.drawable.bcg);

			// setting the opacity (alpha)
			bcg.setAlpha(alpha_val);

			// setting the images on the ImageViews
			rl.setBackgroundDrawable(bcg);
			rdUploadImage.setTextColor(Color.BLACK);
			try {
				Intent gintent = new Intent();
				gintent.setType("image/*");
				gintent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
				Intent.createChooser(gintent, "Select an image"),
				UPLOAD_IMAGE);
				} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
				e.getMessage(),
				Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
				}
		
		break;	
		}
		
		// TODO Auto-generated method stub
		
	}
};


public void openGalleryAudio(){
	 
    Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video "), UPLOAD_VIDEO);
   }
 
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
 
       
    	if (resultCode == RESULT_OK) {
 
            if (requestCode == UPLOAD_VIDEO)
            {
                System.out.println("SELECT_AUDIO");
                Uri selectedImageUri = data.getData();
                selectedPath = getPath(selectedImageUri);
                System.out.println("SELECT_AUDIO Path : " + selectedPath);
                System.gc();
                new doFileUpload().execute();
                //HERE??
               
            }
            if((requestCode == CAPTURE_IMAGE)&&(null != data))
        	{
        	Bundle extras = data.getExtras();
        	//Image saved
        	bmp = (Bitmap) extras.get("data");
        	//Toast.makeText(this, String.valueOf(bmp.getByteCount()), Toast.LENGTH_LONG).show();

        		System.gc();

        	    String PREF_FILE_NAME = "filename";
            	ByteArrayOutputStream stream = new ByteArrayOutputStream();
        		Intent i = new Intent(Video_Activity.this, TabActivity.class);
                bmp.compress(Bitmap.CompressFormat.PNG,100 ,stream);
                byte[] byteArray = stream.toByteArray();
                
                SharedPreferences settings = Video_Activity.this.getSharedPreferences(PREF_FILE_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                String saveThis = Base64.encodeToString(byteArray, Base64.DEFAULT);
             //   i.putExtra("IMAGE",byteArray );
                Log.d("array",saveThis);
                editor.putString("IMG_PATH",saveThis );
                editor.commit();
                startActivity(i);

        	}
            if((requestCode==UPLOAD_IMAGE)&&(null != data)){
            	bmpUploadImage = data.getData();
            	String filemanagerstring = getPath(bmpUploadImage);
            	BitmapFactory.Options o = new BitmapFactory.Options();
            	o.inJustDecodeBounds = true;
            	BitmapFactory.decodeFile(filemanagerstring, o);

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
            	bitmap = BitmapFactory.decodeFile(filemanagerstring, o2);
            		String PREF_FILE_NAME = "filename";
                	ByteArrayOutputStream stream = new ByteArrayOutputStream();
            		Intent i = new Intent(Video_Activity.this, TabActivity.class);
            		bitmap.compress(Bitmap.CompressFormat.PNG,100 ,stream);
                    byte[] byteArray = stream.toByteArray();                    
                    SharedPreferences settings = Video_Activity.this.getSharedPreferences(PREF_FILE_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    String saveThis = Base64.encodeToString(byteArray, Base64.DEFAULT);
                 //   i.putExtra("IMAGE",byteArray );
                    Log.d("array",saveThis);
                    editor.putString("IMG_PATH",saveThis );
                    editor.commit();
                    startActivity(i);
            		
            }
            }
        }
    
       
        	
        
    
   public Bitmap getStringPath(Uri uri) {
    	 
    	String[] projection = { MediaStore.Images.Media.DATA };
    	Cursor cursor = managedQuery(uri, projection, null, null, null);
    	int column_index = cursor
    	.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    	cursor.moveToFirst();
    	String filePath = cursor.getString(column_index);
    	cursor.close();
    	// Convert file path into bitmap image using below line.
    	Bitmap bitmap = BitmapFactory.decodeFile(filePath);
    	return bitmap;
    	}
    
    
 
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
 
     class doFileUpload extends AsyncTask<Void , Void, ArrayList<String>>{
    
    	
    	
    	
		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			// TODO Auto-generated method stub
		    HttpURLConnection conn = null;
	        DataOutputStream dos = null;
	        DataInputStream inStream = null;
	        String lineEnd = "\r\n";
	        String twoHyphens = "--";
	        String boundary =  "*****";
	        int bytesRead, bytesAvailable, bufferSize;
	        byte[] buffer;
	        //changed here
	        int maxBufferSize = 10*1024*1024;
	       
	        String urlString = "http://10.16.31.209/Video_to_frames.php";
	        try
	        {
	         //------------------ CLIENT REQUEST
	        FileInputStream fileInputStream = new FileInputStream(new File(selectedPath) );
	         // open a URL connection to the Servlet
	         URL url = new URL(urlString);
	         // Open a HTTP connection to the URL
	         conn = (HttpURLConnection) url.openConnection();
	         // Allow Inputs
	         conn.setDoInput(true);
	         // Allow Outputs
	         conn.setDoOutput(true);
	         // Don't use a cached copy.
	         conn.setUseCaches(false);
	         // Use a post method.
	         conn.setRequestMethod("POST");
	         conn.setRequestProperty("Connection", "Keep-Alive");
	         conn.setRequestProperty("ENCTYPE", "multipart/form-data");
	         conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
	     //    conn.setRequestProperty("uploadedfile", selectedPath);
	     //    Log.d("selected_path",selectedPath);
	         dos = new DataOutputStream( conn.getOutputStream() );
	         dos.writeBytes(twoHyphens + boundary + lineEnd);
	         dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + selectedPath +"\"" + lineEnd);	   
	         dos.writeBytes(lineEnd);
	         // create a buffer of maximum size
	         bytesAvailable = fileInputStream.available();
	         bufferSize = Math.min(bytesAvailable, maxBufferSize);
	         buffer = new byte[bufferSize];
	         // read file and write it into form...
	         bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	         while (bytesRead > 0)
	         {
	          dos.write(buffer, 0, bufferSize);
	          bytesAvailable = fileInputStream.available();
	          bufferSize = Math.min(bytesAvailable, maxBufferSize);
	          bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	         }
	         // send multipart form data necesssary after file data...
	         dos.writeBytes(lineEnd);
	         dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	         // close streams
	         Log.d("Debug","File is written");
	         fileInputStream.close();
	         dos.flush();
	        }
	        catch (MalformedURLException ex)
	        {
	             Log.d("Debug", "error: " + ex.getMessage(), ex);
	        }
	        catch (IOException ioe)
	        {
	             Log.d("Debug", "error: " + ioe.getMessage(), ioe);
	        }
	        //------------------ read the SERVER RESPONSE
	        try {
	              inStream = new DataInputStream ( conn.getInputStream() );
	              String str;
	              String finalstr ="";
	 
	              while (( str = inStream.readLine()) != null)
	              {
	                   Log.d("Debug","Server Response "+str);
	                   finalstr = finalstr + str;
	              }
	              String[] paths = finalstr.split("<br/>");
                  
                  for(String s:paths)
                  {
                  	string_path.add(s);	
                  }
                  Log.d("paths",string_path.toString());
	              inStream.close();
	              
	              return string_path;
	 
	        }
	        catch (IOException ioex){
	             Log.d("Debug", "error: " + ioex.getMessage(), ioex);
	        }
	         try {
				dos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return string_path;
		}

		
		@Override
		protected void onPostExecute(ArrayList<String> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			//for(int i=0;i<result.size();i++)
		//	{
			
			
			
		    progressDialog.dismiss();
				//new Download_Images_from_Server(result,mcontext).execute();
			Intent intent = new Intent(Video_Activity.this,GridActivity.class);
			intent.putExtra("BITMAP", result);
			intent.putExtra("CHOOSE", 1);
			Log.d("TEST", final_result.toString());
			startActivity(intent);
				 
			//}
			//}
			
			
		}


		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			
			
			progressDialog =  new ProgressDialog(Video_Activity.this);
			progressDialog.setMessage("Uploading To Server...");
			progressDialog.setTitle("Please Wait");
			progressDialog.setCancelable(true);
			progressDialog.show();
		}
		
		
		
		
		
      }
    
    
   
    

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

    	 final float densityMultiplier = context.getResources().getDisplayMetrics().density;        

    	 int h= (int) (newHeight*densityMultiplier);
    	 int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

    	 photo=Bitmap.createScaledBitmap(photo, w, h, true);

    	 return photo;
    	 }


    
   
}

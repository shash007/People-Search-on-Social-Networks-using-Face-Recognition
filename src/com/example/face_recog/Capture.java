package com.example.face_recog;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;

import com.example.face_recog.Video_Activity.doFileUpload;



import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.FaceDetector;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.media.FaceDetector.Face;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

public class Capture extends Activity {
 Camera myCamera;
 MyCameraSurfaceView myCameraSurfaceView;
 MediaRecorder mediaRecorder;
int phoneHeight,phoneWidth;
VideoView vw;
String anew="";

ProgressDialog dialog=null;

Button myButton;
ArrayList<Bitmap> alBitMap;
public static ArrayList<String> filePath;// = new ArrayList<String>();
ArrayList<Bitmap> alFinalPrint=new ArrayList<Bitmap>();
SurfaceHolder surfaceHolder;
ImageView iv;
boolean recording;
FrameLayout myCameraPreview; 
RelativeLayout rl;
String root = Environment.getExternalStorageDirectory().toString();
//String file_dest_faces = root + "/saved_images";   
String file_path = root;
      
String fileNameNew;

File fileTest;
/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  
    recording = false;
    
    setContentView(R.layout.activity_capture);
  
    //Get Camera for preview
    myCamera = getCameraInstance();
    if(myCamera == null){
     Toast.makeText(Capture.this,
       "Fail to get Camera",
       Toast.LENGTH_LONG).show();
    }
    rl=(RelativeLayout)findViewById(R.id.rlMain);
    

    myCameraSurfaceView = new MyCameraSurfaceView(Capture.this, myCamera);
    //vw=(VideoView)findViewById(R.id.vW1);
    myCameraPreview = (FrameLayout)findViewById(R.id.videoview);
    
    myCameraPreview.addView(myCameraSurfaceView);
   
    rl=(RelativeLayout)findViewById(R.id.rlMain);
    phoneWidth=rl.getWidth();
    phoneHeight=rl.getHeight();
    
   
    myButton = (Button)findViewById(R.id.btnRecord);
   
    myButton.setOnClickListener(myButtonOnClickListener);
}

@Override
public void onBackPressed() {
	Intent intent = new Intent(Capture.this,Video_Activity.class);
	  
	   startActivity(intent);
}


Button.OnClickListener myButtonOnClickListener
= new Button.OnClickListener(){

@Override
public void onClick(View v) {
// TODO Auto-generated method stub
	//myCameraPreview.setEnabled(true);
	
	if(recording){
            // stop recording and release camera
              // stop the recording
          //  mediaRecorder.setVideoSize(myCameraSurfaceView.getWidth(),
          //  myCameraSurfaceView.getHeight()); 
          //  ax_video_size));
            try{
          	  mediaRecorder.stop();
          	  
          	  mediaRecorder.release();
          	  mediaRecorder = null;
          	AlertDialog.Builder builder = new AlertDialog.Builder(Capture.this);
            builder.setTitle("I want to run recognition on");
            builder.setItems(new CharSequence[]
                    {"Phone", "Server"},
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                            switch (which) {
                                case 0:
                                	 myCameraPreview.setEnabled(false);
                           		  myButton.setClickable(false);
                           		  myCameraPreview.removeAllViews();
                           		  System.gc();
                           		  new detectFaces().execute();
                                	 
                                   
                                    break;
                                case 1:
                                	 new doFileUpload().execute();
                                //	val="server";
                                    break;
                               
                                
                            }
                        }
                    });
            builder.create().show();
           
            
               
            }catch(RuntimeException stopException){
                //handle cleanup here
            }
           recording=false;
            myCamera.lock();
            myButton.setText("RECORD");
           // mediaRecorder.setVideoFrameRate(getResources().getInteger());  // release the MediaRecorder object
          
            //Exit after saved
          //  finish();
}else{

//Release Camera before MediaRecorder start
releaseCamera();

      if(!prepareMediaRecorder()){
       Toast.makeText(Capture.this,
         "Fail in prepareMediaRecorder()!\n - Ended -",
         Toast.LENGTH_LONG).show();
       finish();
      }

mediaRecorder.start();
recording = true;
myButton.setText("STOP");

}
}};

private Camera getCameraInstance(){
//TODO Auto-generated method stub
    Camera c = null;
    try {
        c = Camera.open(); // attempt to get a Camera instance
    }
    catch (Exception e){
        // Camera is not available (in use or does not exist)
    }
    return c; // returns null if camera is unavailable
}

private boolean prepareMediaRecorder(){
 myCamera = getCameraInstance();
 myCamera.setDisplayOrientation(90);
 mediaRecorder = new MediaRecorder();
// mediaRecorder.setVideoSize(myCameraSurfaceView.getWidth(),
//         myCameraSurfaceView.getHeight()); 

 myCamera.unlock();
 mediaRecorder.setCamera(myCamera);
// mCamera.setDisplayOrientation(degrees);
FileInputStream fi=null;
 FileDescriptor fd=null;

 mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
 mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

 mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QVGA));

    mediaRecorder.setMaxDuration(15000); // Set max duration 10 sec.
    mediaRecorder.setMaxFileSize(4000000); // Set max file size 4M
    
   	 mediaRecorder.setOutputFile(this.initFile().getAbsolutePath());
	 Log.d("mediarecorder",String.valueOf(this.initFile().getAbsoluteFile()));
	 fileNameNew=this.initFile().getAbsolutePath();
	 
 mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());

 try {
     mediaRecorder.prepare();
 } catch (IllegalStateException e) {
     releaseMediaRecorder();
     return false;
 } catch (IOException e) {
     releaseMediaRecorder();
     return false;
 }
 return true;

}

@Override
protected void onPause() {
    super.onPause();
    releaseMediaRecorder(); 
   // if you are using MediaRecorder, release it first
    releaseCamera();              // release the camera immediately on pause event
}


private void releaseMediaRecorder(){
    if (mediaRecorder != null) {
        mediaRecorder.reset();   // clear recorder configuration
        mediaRecorder.release(); // release the recorder object
        mediaRecorder = null;
        myCamera.lock();  
        // lock camera for later use
    }
}

private void releaseCamera(){
    if (myCamera != null){
        myCamera.release();        // release the camera for other applications
        myCamera = null;
    }
}

public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

private SurfaceHolder mHolder;
 private Camera mCamera;

public MyCameraSurfaceView(Context context, Camera camera) {
     super(context);
     mCamera = camera;

     // Install a SurfaceHolder.Callback so we get notified when the
     // underlying surface is created and destroyed.
     mHolder = getHolder();
     mHolder.addCallback(this);
     // deprecated setting, but required on Android versions prior to 3.0
     mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
 }

@Override
public void surfaceChanged(SurfaceHolder holder, int format, int weight,
int height) {
     // If your preview can change or rotate, take care of those events here.
     // Make sure to stop the preview before resizing or reformatting it.

     if (mHolder.getSurface() == null){
       // preview surface does not exist
       return;
     }

     // stop preview before making changes
     try {
         mCamera.stopPreview();
     } catch (Exception e){
       // ignore: tried to stop a non-existent preview
     }

     // make any resize, rotate or reformatting changes here

     // start preview with new settings
     try {
         mCamera.setPreviewDisplay(mHolder);
         mCamera.startPreview();

     } catch (Exception e){
     }
}

@Override
public void surfaceCreated(SurfaceHolder holder) {
// TODO Auto-generated method stub
// The Surface has been created, now tell the camera where to draw the preview.
     try {
         mCamera.setPreviewDisplay(holder);
         mCamera.startPreview();
     } catch (IOException e) {
     }
}

@Override
public void surfaceDestroyed(SurfaceHolder holder) {
// TODO Auto-generated method stub

}
}

@SuppressLint("NewApi")
ArrayList<Bitmap> rotate(float x, ArrayList<Bitmap> alBit)
{
	ArrayList<Bitmap> alFlip=new ArrayList<Bitmap>();
	
	Bitmap bitmapOrg;
	
	
		
		 Bitmap resizedBitmap=null;
		 float scaleWidth,scaleHeight;
		 Matrix matrix;
		 int width,height,newWidth=600,newHeight=400;
		
		for(int i=0;i<alBit.size();i++){
				bitmapOrg = Bitmap.createBitmap(alBit.get(i));
				 

    width = bitmapOrg.getWidth();

     height = bitmapOrg.getHeight();


   

    // calculate the scale - in this case = 0.4f

      scaleWidth = ((float) newWidth) / width;

     scaleHeight = ((float) newHeight) / height;

    matrix = new Matrix();

     matrix.postScale(scaleWidth, scaleHeight);
     matrix.postRotate(x);

     resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,width,height, matrix, false);
     alFlip.add(resizedBitmap);
		}
     
     return alFlip;
}



       
 class myView {
	 
	   int imageWidth, imageHeight;
	  int numberOfFace = 5;
	  float dist;
	  float lowVal, highVal;
	   FaceDetector myFaceDetect;
	   FaceDetector.Face[] myFace;
	  float myEyesDistance=(float)0.0000;
	  int numberOfFaceDetected;
	  ArrayList<Bitmap> alFaces=new ArrayList<Bitmap>();
	  ArrayList<Integer> alGetIndex=new ArrayList<Integer>();
	  Bitmap myBitmap;//=Bitmap.createBitmap(uri));
	 int tab=0;
	 int total=0;
	  ArrayList<Bitmap> alFinalFaces=new ArrayList<Bitmap>();
	  
	  ArrayList<Integer> retFaces1(ArrayList<Bitmap> alCrazy){
		 
		  
	   BitmapFactory.Options BitmapFactoryOptionsbfo = new BitmapFactory.Options();
	   BitmapFactoryOptionsbfo.inPreferredConfig = Bitmap.Config.RGB_565;
	  
	   BitmapFactoryOptionsbfo.inPreferQualityOverSpeed=false;
	   BitmapFactoryOptionsbfo.outHeight=500;
	   BitmapFactoryOptionsbfo.outWidth=500;
	   String finalPath="";
	   for(int i=0;i<alCrazy.size();i++){
		   myBitmap=alCrazy.get(i);
		   //Width();
	  // imageHeight = alTest01.get(i).getHeight();
	   
		  // myBitmap=BitmapFactory.decodeFile(finalPath, BitmapFactoryOptionsbfo);
		   // myBitmap = BitmapFactory.decodeResource(getResources(),
		   //   R.drawable.shash, BitmapFactoryOptionsbfo);
		   // myBitmap.setHeight(200);
		   if(myBitmap==null){}
		   else{
		   imageWidth = myBitmap.getWidth();
		    imageHeight = myBitmap.getHeight();
		    myFace = new FaceDetector.Face[numberOfFace];
		    
		    myFaceDetect = new FaceDetector(imageWidth, imageHeight,
		      numberOfFace);
		    numberOfFaceDetected = myFaceDetect.findFaces(myBitmap, myFace);
		    for(int p=0;p<numberOfFaceDetected;p++){
		    PointF myMidPoint = new PointF();
		    myFace[p].getMidPoint(myMidPoint);
		    lowVal=(myEyesDistance-(float)0.0100);
		    highVal=(myEyesDistance+(float)0.0100);
		    myEyesDistance = myFace[p].eyesDistance();
		    tab++;
		    Log.d("CEYE",String.valueOf(tab)+" "+String.valueOf(myEyesDistance)+" "+String.valueOf(i));
		    }
		    
		    System.gc();
		    
		    Log.d("FACENO",String.valueOf(numberOfFaceDetected)+" "+String.valueOf(i));
		    if((numberOfFaceDetected>0)&&((!(lowVal<=myEyesDistance))||(!(highVal>=myEyesDistance)))){
		    	alGetIndex.add(i);
		    	
		    	++total;
		    	 
		 		 /*   for(int k=0;k<myFace.length;k++){
		 		    	dist=myFace[i].eyesDistance();
		 		    	Log.d("EYE",String.valueOf(dist));
		 		    	
		 		    	
		 		    }*/
		    }
		    else{}
		    
		  //  myBitmap.recycle();
	      //  myBitmap = null;
	        System.gc();
		    
		   }
		 	   
	
	  
	  
	  
	   
	   }
	   Log.d("GETTOTAL",String.valueOf(total));
	   return alGetIndex;
	  // finish();
	   
	 
	   
	  }
	 
	 
	 
	 
	 }
 class detectFaces extends AsyncTask<Void,Void,ArrayList<Bitmap>>{

	@Override
	protected ArrayList<Bitmap> doInBackground(Void... params) {
		//String resp="";
		// TODO Auto-generated method stub
		MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
		 // Get the directory for the user's public pictures directory.
		alBitMap=new ArrayList<Bitmap>();
	   
	   FileInputStream fi=null;
	   FileDescriptor fd=null;
	try {
		fi=new FileInputStream(fileNameNew);
		//PASSED FILE NAME
		Log.d("DFfileName",fileNameNew+"");
		 fd=fi.getFD();
		 mediaMetadataRetriever.setDataSource(fd);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 System.gc();
     // mediaMetadataRetriever.setDataSource(uri);
    //  Log.d("urilocation",String.valueOf(uri)+"");
    //  int duration= mediaMetadataRetriever.METADATA_KEY_DURATION;
    // Bitmap b=mediaMetadataRetriever.getFrameAtTime(2000);
  // int bytecnt=  b.getAllocationByteCount();
  // Log.d("bytecount",String.valueOf(bytecnt)+"");
      String time = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
      long timeInmillisec = Long.parseLong( time );
      long durationTime = timeInmillisec / 1000;
      long hours = durationTime / 3600;
      long minutes = (durationTime - hours * 3600) / 60;
      long seconds = durationTime - (hours * 3600 + minutes * 60);
      Log.d("duration",String.valueOf(seconds)+"");
    //  Log.d("duration",String.valueOf(duration)+"");
     for(int i=0;i<seconds*1000000;i=i+100000){
      alBitMap.add(mediaMetadataRetriever.getFrameAtTime(i));
      }
     System.gc();
     // Toast.makeText(Capture.this, alBitMap.size()+" the size is"+duration, Toast.LENGTH_LONG).show();
     // iv.setImageBitmap(alBitMap.get(50));
      //new btnClickTask().execute(uri);
     Log.d("NF",String.valueOf(alBitMap.size()));
      ArrayList<Bitmap> alCheck=new ArrayList<Bitmap>();
      System.gc();
     
      ArrayList<Integer> alRetIndex=new ArrayList<Integer>();
  	alCheck=rotate((float) 90.0, alBitMap);
  	alBitMap=null;
  	//saveBitmapAL(alCheck,0);
   myView objNew=new myView();
  //	alRetIndex=objNew.retFaces(file_path,alCheck.size());
   alRetIndex=objNew.retFaces1(alCheck);
  	for(int i=0;i<alRetIndex.size();i++){
  		alFinalPrint.add(alCheck.get(alRetIndex.get(i)));
  	}
  	Log.d("alFinal",String.valueOf(alFinalPrint.size()));
 //  saveBitmapAL(alFinalPrint,1);
 // resp=String.valueOf(alFinalPrint.size());
		return alFinalPrint;
	}
	@Override
	protected void onProgressUpdate(Void... unsued) {

	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		Intent stateUpdate = new Intent("com.quicinc.Trepn.UpdateAppState");
		stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value", 7);
		stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value.Desc", "Face_Detection_Mobile()");
		sendBroadcast(stateUpdate); 
		
		
		dialog = ProgressDialog.show(Capture.this, "Detecting",
				"Please wait...", true);
		
	}
	protected void onPostExecute(ArrayList<Bitmap> sResponse) {
		try {
			Intent stateUpdate = new Intent("com.quicinc.Trepn.UpdateAppState");
			stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value", 8);
			stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value.Desc", "Random()");
			sendBroadcast(stateUpdate); 
			
			
		if (dialog.isShowing())
		dialog.dismiss();
		
		//Toast.makeText(Capture.this, "Response " + sResponse.size(), Toast.LENGTH_LONG).show();
		System.gc();
		
		
		 
		 Log.d("realFaces",String.valueOf(alFinalPrint.size()));
		 ArrayList<String> Save = new ArrayList<String>();
		 for(int i=0;i< sResponse.size();i++)
		 {
			 ByteArrayOutputStream stream = new ByteArrayOutputStream();
				
			 sResponse.get(i).compress(Bitmap.CompressFormat.JPEG,100 ,stream);
	         byte[] byteArray = stream.toByteArray();
	         
	         Save.add(Base64.encodeToString(byteArray, Base64.DEFAULT));
		 }
			
		 ArrayList<Bitmap> new_bit = new ArrayList<Bitmap>();
		 for(int i=0;i< sResponse.size();i++)
		 {
			 new_bit.add(scaleDownBitmap(sResponse.get(i), 180, Capture.this));
		 }
		 
		 
         
		   //gridIntent.putParcelableArrayListExtra("BITMAP",alFinalPrint );
		 Log.d("shashank",Save.get(0));
		 Intent gridIntent = new Intent(Capture.this,GridActivity.class);
		// gridIntent.putExtra("BITMAP", Save);
		 gridIntent.putParcelableArrayListExtra("BITMAP",new_bit );
		// gridIntent.putStringArrayListExtra("BITMAP", Save);
		  // gridIntent.putExtra("BITMAP",saveThis );
		   gridIntent.putExtra("CHOOSE", 2);
			Log.d("grid", "grid");
			startActivity(gridIntent);
		
		} catch (Exception e) {
		//Toast.makeText(getApplicationContext(),
		//e.getMessage(),
			e.printStackTrace();
		//Toast.LENGTH_LONG).show();
		//Log.e(e.getClass().getName(), e.getMessage(), e);
		}
		}
	 
 }
 
 private File initFile() {
	    // File dir = new
	    // File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
	    // this
	    File dir = new File(Environment.getExternalStorageDirectory(), this
	            .getClass().getPackage().getName());


	    if (!dir.exists() && !dir.mkdirs()) {
	        Log.wtf("FileNotStore",
	                "Failed to create storage directory: "
	                        + dir.getAbsolutePath());
	        //Toast.makeText(Capture.this, "not record", Toast.LENGTH_SHORT).show();;
	        fileTest = null;
	    } else {
	    	
	        fileTest = new File(dir.getAbsolutePath(),"VideoTest01.mp4");
	    }
	    return fileTest;
	}

public void saveBitmapAL(ArrayList<Bitmap> ampFace)

{
	
	String dest="";
	//int status=iFlag;
	
	Log.d("AMP",String.valueOf(ampFace.size()+""));
   
  //  Log.d("INSAVE1","INSAVE");
    File dir;
    File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"FACES/");
   Log.d("EXPATH",String.valueOf(directory.getAbsolutePath()));
    directory.mkdirs();
   // dir = new File(dest);
    deleteFile(directory);
    File file;
    String sname="";
    Bitmap bnew;
   
   
    for(int i=0;i<ampFace.size();i++){
    	 
         sname=String.valueOf(i)+".jpg";
         
        
        file= new File(directory, sname);
        Log.d("FILE",sname);
        FileOutputStream fOut;
		try {
			fOut = new FileOutputStream(file);
			//amp.get(i);
			//bnew= amp.get(i).createBitmap(400, 400, Config.RGB_565);
			ampFace.get(i).compress(CompressFormat.JPEG,100, fOut);
			//amp.get(i)
	          fOut.flush();
	          fOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
    }
   
   
    
}
void deleteFile(File file) {
	
	    	if(file.length()>0){
	        for (File child : file.listFiles())
	        {
	            child.delete();
	            deleteFile(child);
	        }}
	    	else {}

	    //fileOrDirectory.delete();
	}

 
 class doFileUpload extends AsyncTask<Void , Void, ArrayList<String>>{

		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			filePath = new ArrayList<String>();
			HttpURLConnection conn = null;
	        DataOutputStream dos = null;
	        DataInputStream inStream = null;
	        String lineEnd = "\r\n";
	        String twoHyphens = "--";
	        String boundary =  "*****";
	        int bytesRead, bytesAvailable, bufferSize;
	        byte[] buffer;
	        int maxBufferSize = 1*1024*1024;
	       
	        String urlString = "http://10.16.31.209/Video_to_frames.php";
	        try
	        {
	         //------------------ CLIENT REQUEST
	        FileInputStream fileInputStream = new FileInputStream(new File(fileNameNew) );
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
	         dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fileNameNew +"\"" + lineEnd);	   
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
            	   filePath.add(s);	
               }
               Log.d("paths",filePath.toString());
	              inStream.close();
	              
	              return filePath;
	 
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

			return filePath;
		}

		
		@Override
		protected void onPostExecute(ArrayList<String> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			//for(int i=0;i<result.size();i++)
		//	{
			Intent stateUpdate = new Intent("com.quicinc.Trepn.UpdateAppState");
			stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value", 6);
			stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value.Desc", "Random()");
			sendBroadcast(stateUpdate); 
			
			
				//new Download_Images_from_Server(result,mcontext).execute();
			Intent intent = new Intent(Capture.this,GridActivity.class);
			intent.putExtra("BITMAP", result);
			intent.putExtra("CHOOSE", 1);
			Log.d("TEST", filePath.toString());
			startActivity(intent);
				 
			//}
			//}
			
			
		}


		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Intent stateUpdate = new Intent("com.quicinc.Trepn.UpdateAppState");
			stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value", 5);
			stateUpdate.putExtra("com.quicinc.Trepn.UpdateAppState.Value.Desc", "Upload_Video_to_Server()");
			sendBroadcast(stateUpdate); 
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

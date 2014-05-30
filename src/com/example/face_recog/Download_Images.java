package com.example.face_recog;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;

import com.example.face_recog.MovieAdapter.ViewHolder;

public class Download_Images extends AsyncTask<String, Void, Bitmap> {

    ImageButton btn;

	public Download_Images(ImageButton btn) {
		// TODO Auto-generated constructor stub
		this.btn = btn;
	}

	protected Bitmap doInBackground(String... params) {

		URL aURL;
		Bitmap bm = null;
		URLConnection conn;
		try {
			aURL = new URL(params[0]);
			conn = aURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
			// bm;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return null;
		return bm;
	}

	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		Bitmap bit = getResizedBitmap(result, 380, 1450);
		BitmapDrawable draw = new BitmapDrawable(bit);
		btn.setScaleType(ScaleType.FIT_XY);
		btn.setBackgroundDrawable(draw);
		// progressDialog.dismiss();
		// resultList= new ArrayList<Movie>();
		/*
		 * Message msg=new Message(); msg.obj=result;
		 * MovieAdapter.mHandler.sendMessage(msg);
		 */

	}

	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		// progressDialog.show();
	}
	
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		 
		int width = bm.getWidth();
		 
		int height = bm.getHeight();
		Log.d("BMPHW",String.valueOf(width)+" "+String.valueOf(height)+" ");
		 
		float scaleWidth = ((float) newWidth) / width;
		 
		float scaleHeight = ((float) newHeight) / height;
		 
		// CREATE A MATRIX FOR THE MANIPULATION
		 
		Matrix matrix = new Matrix();
		 
		// RESIZE THE <SPAN CLASS="OD8Y7" ID="OD8Y7_7">BIT MAP</SPAN>
		 
		matrix.postScale(scaleWidth, scaleHeight);
		// matrix.postScale(newWidth, newHeight);
		// RECREATE THE NEW BITMAP
		 
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		 
		return resizedBitmap;
		 
		}
}


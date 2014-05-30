

package com.example.face_recog;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import com.example.face_recog.MovieAdapter.ViewHolder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

	private int mPosition;
	private ViewHolder mHolder;

	public DownloadImage(int position, ViewHolder holder) {
		// TODO Auto-generated constructor stub
		mPosition = position;
		mHolder = holder;
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
		if (mHolder.position == mPosition) {
			mHolder.imageview1.setImageBitmap(result);
		}
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
}

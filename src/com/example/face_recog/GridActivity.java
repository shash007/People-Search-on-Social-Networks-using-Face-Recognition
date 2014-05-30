package com.example.face_recog;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Arrays;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class GridActivity extends Activity {
 
    private Utils utils;
    private ArrayList<String> imagePaths = new ArrayList<String>();
    private GridViewImageAdapter adapter;
    private GridView gridView;
    private int columnWidth;
    ArrayList<String> face_urls= new ArrayList<String>();
    ArrayList<Bitmap> face_list = new ArrayList<Bitmap>();
    String PREF_FILE_NAME = "filename";
    ArrayList<String> bit_str = new ArrayList<String>();
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        Log.d("GRV","here");
        gridView = (GridView) findViewById(R.id.gridView1);
 
        utils = new Utils(this);
 
        // Initilizing Grid View
        InitilizeGridLayout();
        
        int choose = getIntent().getExtras().getInt("CHOOSE");
        
        Log.d("choose", String.valueOf(choose));
        if(choose==1)
        {
        	face_urls = getIntent().getExtras().getStringArrayList("BITMAP");
        	Log.d("face_urls", face_urls.toString());
        	new Download_Images_from_Server(face_urls, this).execute();
        }
        else
        {
        	
        	//bit_str = getIntent().getExtras().getStringArrayList("BITMAP");
        	face_list = getIntent().getParcelableArrayListExtra("BITMAP");
            imagePaths = utils.getFilePaths();
            
            // Gridview adapter
            adapter = new GridViewImageAdapter(GridActivity.this, face_list,
                    columnWidth);
     
            // setting grid view adapter
            gridView.setAdapter(adapter);
			
        	
        	//new get_bitmap().execute();
        	

        }
        
        gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
	            Intent i = new Intent(GridActivity.this, TabActivity.class);
	            i.putExtra("position", arg2);
	            Bitmap bit = face_list.get(arg2);
	            bit.compress(Bitmap.CompressFormat.JPEG,100 ,stream);
	            byte[] byteArray = stream.toByteArray();
	            
	            SharedPreferences settings = getSharedPreferences(PREF_FILE_NAME, 0);
	            SharedPreferences.Editor editor = settings.edit();
	            String saveThis = Base64.encodeToString(byteArray, Base64.DEFAULT);
	            //i.putExtra("IMG_PATH",saveThis );
	            Log.d("array",saveThis);
	            editor.putString("IMG_PATH",saveThis );
	            editor.commit();
	            startActivity(i);
				
			}
		});
        
       
 
       Log.d("RCV",String.valueOf(face_list.size()));
        // loading all image paths from SD card
       
    }
 
    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConstant.GRID_PADDING, r.getDisplayMetrics());
 
        columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);
 
        gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }
 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.grid, menu);
		return true;
	}
	
	public static class AppConstant {
		 
	    // Number of columns of Grid View
	    public static final int NUM_OF_COLUMNS = 3;
	 
	    // Gridview image padding
	    public static final int GRID_PADDING = 8; // in dp
	 
	    // SD card image directory
	    public static final String PHOTO_ALBUM = "webkit";
	 
	    // supported file formats
	    public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg",
	            "png");
	}
	
	
	 public class Download_Images_from_Server extends AsyncTask<Void, Void, Bitmap> {

	    	
	    	Context mcontext;
	    	ArrayList<String> mPosition;

	    	public Download_Images_from_Server(ArrayList<String> position, Context context) {
	    		// TODO Auto-generated constructor stub
	    		mPosition = position;
	    		mcontext = context;
	    		
	    		
	    	}

	    	protected Bitmap doInBackground(Void... params) {

	    		URL aURL;
	    		Bitmap bm = null;
	    		URLConnection conn;
	    		try {
	    			for(int i=0;i<mPosition.size();i++)
	    			{
	    			aURL = new URL("http://10.16.31.209/"+mPosition.get(i));
	    			Log.d("URLS",String.valueOf(aURL));
	    			conn = aURL.openConnection();
	    			conn.connect();
	    			InputStream is = conn.getInputStream();
	    			BufferedInputStream bis = new BufferedInputStream(is);
	    			bm = BitmapFactory.decodeStream(bis);
	    			face_list.add(bm);
	    			bis.close();
	    			is.close();
	    			}
	    			// bm;
	    		} catch (IOException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		 return null;
	    	}

	    	protected void onPostExecute(Bitmap result) {
	    		// TODO Auto-generated method stub
	    		super.onPostExecute(result);
	    		/*if(mPosition == (Video_Activity.string_path.size() -1))
	    		{
	    			Video_Activity.final_result.add(result);
	    			Intent intent = new Intent(mcontext,GridActivity.class);
	    			intent.putParcelableArrayListExtra("BITMAP", Video_Activity.final_result);
	    			mcontext.startActivity(intent);
	    			
	    		}
	    		else
	    		{*/
	        	Log.d("face_urls", face_list.toString());
	    		adapter = new GridViewImageAdapter(GridActivity.this, face_list,
	                    columnWidth);
	     
	            // setting grid view adapter
	            gridView.setAdapter(adapter);
	    		
	    		
	    			
	    		
	    		// progressDialog.dismiss();
	    		// resultList= new ArrayList<Movie>();
	    		/*
	    		 * 
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


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(GridActivity.this,Video_Activity.class);
		  startActivity(intent);
	}
	 
	 
	class get_bitmap extends AsyncTask<Void, Void, ArrayList<Bitmap>>
	{

		@Override
		protected ArrayList<Bitmap> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			for(int i=0;i<bit_str.size();i++)
        	{
        		byte[] bytearr = Base64.decode(bit_str.get(i), Base64.DEFAULT);
        		Bitmap bitmap = BitmapFactory.decodeByteArray(bytearr,0, bytearr.length);
        		face_list.add(bitmap);
        		
        	}
			
			return face_list;
		}

		@Override
		protected void onPostExecute(ArrayList<Bitmap> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
            
        //	face_list = getIntent().getParcelableArrayListExtra("BITMAP");
            imagePaths = utils.getFilePaths();
            
            // Gridview adapter
            adapter = new GridViewImageAdapter(GridActivity.this, result,
                    columnWidth);
     
            // setting grid view adapter
            gridView.setAdapter(adapter);
			
			
		}
		
		
		
	}
	 

}

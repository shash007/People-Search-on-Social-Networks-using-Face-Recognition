package com.example.face_recog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridViewImageAdapter extends BaseAdapter {
 
    private Activity _activity;
    private ArrayList<Bitmap> _filePaths = new ArrayList<Bitmap>();
    private int imageWidth;
    Bitmap image;
    String PREF_FILE_NAME = "filename";
    
 
    public GridViewImageAdapter(Activity activity, ArrayList<Bitmap> filePaths,
            int imageWidth) {
        this._activity = activity;
        this._filePaths = filePaths;
        this.imageWidth = imageWidth;
    }
 
    @Override
    public int getCount() {
        return this._filePaths.size();
    }
 
    @Override
    public Object getItem(int position) {
        return this._filePaths.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
    
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(_activity);
        } else {
            imageView = (ImageView) convertView;
        }
 
        // get screen dimensions
       // image = decodeFile(_filePaths.get(position), imageWidth,
         //       imageWidth);
 
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(imageWidth,
                imageWidth));
        imageView.setImageBitmap(_filePaths.get(position));
 
        // image view click listener
        //imageView.setOnClickListener(new OnImageClickListener(,_filePaths.get(position)));
 
        return imageView;
    }
 
    
    class OnImageClickListener implements OnClickListener {
 
        int _postion;
        Bitmap bit;
 
        // constructor
        public OnImageClickListener(int position, Bitmap bit) {
            this._postion = position;
            this.bit = bit;
        }
 
        public void onClick(View v) {
            // on selecting grid view image
            // launch full screen activity
        	ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Intent i = new Intent(_activity, TabActivity.class);
            i.putExtra("position", _postion);
            Bitmap bit = _filePaths.get(_postion);
            bit.compress(Bitmap.CompressFormat.JPEG,100 ,stream);
            byte[] byteArray = stream.toByteArray();
            
            SharedPreferences settings = _activity.getSharedPreferences(PREF_FILE_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            String saveThis = Base64.encodeToString(byteArray, Base64.DEFAULT);
            //i.putExtra("IMG_PATH",saveThis );
            Log.d("array",saveThis);
            editor.putString("IMG_PATH",saveThis );
            editor.commit();
            _activity.startActivity(i);
        	
        }
 
    }
 
    /*
     * Resizing image size
     */
    public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
        try {
 
            File f = new File(filePath);
 
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
 
            final int REQUIRED_WIDTH = WIDTH;
            final int REQUIRED_HIGHT = HIGHT;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
                    && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
                scale *= 2;
 
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

	
}

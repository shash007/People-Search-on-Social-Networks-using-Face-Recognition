// Midterm
//MovieAdapter.java
//Shashank G Hebbale (800773977)


package com.example.face_recog;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieAdapter extends ArrayAdapter<Param>{

	Context context;
	String strName,strHeadline,strAssoc,strEdu,strCompany,strAddr;
	int resource;
	ArrayList<Param> objects;
	Param movie =null;
	TextView textview1;
	TextView textview2;
	TextView textview3;
	ImageView imageview1;
	ImageView imageview2;
	ImageView imageview3;
	static Handler mHandler;
	ViewHolder holder;
	String decider;

	public MovieAdapter(Context context, int resource, ArrayList<Param> objects, String decider) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.context =context;
		this.resource = resource;
		this.objects = objects;
		this.decider =decider;
	}

	public static class ViewHolder{

		TextView name;
		TextView headline;
		TextView address;
		TextView associations;
		TextView education;
		TextView company;
		ImageView imageview1;
		int position;


	}

	@SuppressWarnings("null")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub


		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if(convertView == null)
		{

			convertView = mInflater.inflate(R.layout.activity_movielist, null);
			holder = new ViewHolder();
			holder.imageview1 = (ImageView) convertView.findViewById(R.id.imageView1);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.headline = (TextView) convertView.findViewById(R.id.headline);
			holder.address = (TextView) convertView.findViewById(R.id.address);
			holder.associations = (TextView) convertView.findViewById(R.id.associations);
			holder.education = (TextView) convertView.findViewById(R.id.education);
			holder.company = (TextView) convertView.findViewById(R.id.company);
            holder.headline.setVisibility(View.VISIBLE);
		    
		    holder.name.setVisibility(View.VISIBLE);
			holder.address.setVisibility(View.VISIBLE);
		   
		   
		  
		   
		    holder.associations.setVisibility(View.VISIBLE);
		    holder.education.setVisibility(View.VISIBLE);
		    holder.company.setVisibility(View.VISIBLE);
			convertView.setTag(holder);
		}


		holder = (ViewHolder) convertView.getTag();
		movie = (Param) objects.get(position);
		strName=movie.getName();
		if((movie.getAddress()==null)||(movie.getAddress()==""))
			strAddr="NOT AVAILABLE";
		else strAddr=movie.getAddress();
		if((movie.getEducation()==null)||(movie.getEducation()==""))
			strEdu="NOT AVAILABLE";
		else
			strEdu=movie.getEducation();
		if((movie.getCompany()==null)||(movie.getCompany()==""))
			strCompany="NOT AVAILABLE";
		else strCompany=movie.getCompany();
		if((movie.getAssociations()==null)||(movie.getAssociations()==""))
			strAssoc="NOT AVAILABLE";
		else strAssoc=movie.getAssociations();
		if((movie.getType()==null)||(movie.getType()==""))
			strHeadline="NOT AVAILABLE";
		else strHeadline=movie.getType();
		
			
		holder.position =position;
		String thumbimage = movie.getPic();
		//Log.d("thumb",thumbimage);
		new DownloadImage(position,holder).execute(thumbimage);

		if(decider.equals("Google+"))
		{
		    holder.headline.setText("OBJECT TYPE:  "+ strHeadline);
		    
		    holder.name.setText(strName);
			holder.address.setVisibility(View.GONE);
		   
		   
		  
		   
		    holder.associations.setVisibility(View.GONE);
		    holder.education.setVisibility(View.GONE);
		    holder.company.setVisibility(View.GONE);
		   

		}
		
		
		else if(decider.equals("Twitter"))
		{
		    holder.headline.setText("LOCATION:  "+ strHeadline);
		    
		    holder.name.setText(strName);
			holder.address.setVisibility(View.GONE);
		   
		   
		  
		   
		    holder.associations.setVisibility(View.GONE);
		    holder.education.setText("DESCRIPTION:  "+ strEdu);
		    holder.company.setVisibility(View.GONE);
		   

		}
	    
		else if(decider.equals("Facebook"))
		{
		    holder.headline.setText("LOCALE:  "+ strHeadline);
		    
		    holder.name.setText(strName);
			holder.address.setVisibility(View.GONE);
		   
		    holder.associations.setVisibility(View.GONE);
		    holder.education.setText("USER NAME:  "+ strEdu);
		    holder.company.setVisibility(View.GONE);
		   

		}
		
		else
		{
		holder.name.setText(strName);
		holder.address.setText("SUMMARY : " + strAddr);
	   
	    holder.headline.setText("HEADLINE: " + strHeadline);
	   
	  
	   
	    holder.associations.setText("LOCATION: "+ strAssoc);
	    
	    holder.education.setText("INDUSTRY: " + strEdu);
	   
	    holder.company.setText("SPECALITIES: " + strCompany);
	   
		}

		return convertView;

	}



}

package com.example.face_recog;

import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;

public class NamesActivity extends Activity {

	ArrayList<Param> movieinfo = new ArrayList<Param>();
	SearchView searchView;
	int check_search =1;
	MovieAdapter adapter;
	ListView listview;
	Spinner spinner;
	ArrayList<Param> new_names = new ArrayList<Param>();
	List<String> list;
	String selector;
	String choose_dropdown;
	String decider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		movieinfo = getIntent().getParcelableArrayListExtra("NAMES");

		setContentView(R.layout.activity_names);
		
		listview = (ListView) findViewById(R.id.listView1);
		//movieinfo = getIntent().getParcelableArrayListExtra("NAMES");
		
		spinner = (Spinner) findViewById(R.id.spinner1);
		list = new ArrayList<String>();
		choose_dropdown = getIntent().getExtras().getString("CHOOSE_DROP");
		decider =getIntent().getExtras().getString("DECIDE");
	
		
		if(choose_dropdown.equals("LINKEDIN"))
		{
		list.add("Headline");
		list.add("Summary");
		list.add("Location");
		list.add("Industry");
		selector = "Headline";
		}
		

		if(choose_dropdown.equals("TWITTER"))
		{
		list.add("Twitter_Location");
		list.add("Description");
		selector = "Twitter_Location";
		}
		

		if(choose_dropdown.equals("FACEBOOK"))
		{
		list.add("Locale");
		list.add("Username");
		selector = "Locale";
		}
		
		

		if(choose_dropdown.equals("GOOGLE+"))
		{
		list.add("Type");
		selector = "Type";
		}
		
		
		
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(dataAdapter);
			
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					
					selector =arg0.getItemAtPosition(arg2).toString();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		
		adapter = new MovieAdapter(this,
				R.layout.activity_movielist, movieinfo,decider);
		listview.setAdapter(adapter);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    searchView = (SearchView) findViewById(R.id.searchView1);
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	   // searchView.setIconifiedByDefault(false);
		
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				if(!newText.isEmpty())
				{
					
					check_search = 0;
					new_names = new ArrayList<Param>();
					for(int i=0;i<movieinfo.size();i++)
		       	    {
						if(selector.equalsIgnoreCase("headline"))
						{
							if(movieinfo.get(i).getType()!=null)
							{
								Log.d("head", "head");
		       	    	if(movieinfo.get(i).getType().toUpperCase().contains(newText.toUpperCase()))

		       	    	{
		       	    		Log.d("string", movieinfo.get(i).getType().toUpperCase());
		       	    		Log.d("string", newText.toUpperCase());
		       	    		Log.d("head", "head");
		       	    		Param new_param = new Param(movieinfo.get(i).getUrl(),movieinfo.get(i).getName(),movieinfo.get(i).getPic(),movieinfo.get(i).getType(),movieinfo.get(i).getAddress(),movieinfo.get(i).getAssociations(),movieinfo.get(i).getEducation(),movieinfo.get(i).getCompany());
		       	    	    new_names.add(new_param);
		       	    	    adapter.notifyDataSetChanged();
		       	    	    

		       	    	}
							}
						}
						if(selector.equalsIgnoreCase("Summary"))  
						{
							if(movieinfo.get(i).getAddress()!=null)
							{
		       	    	if(movieinfo.get(i).getAddress().toLowerCase().contains(newText.toLowerCase()))

		       	    	{
		       	    		Param new_param = new Param(movieinfo.get(i).getUrl(),movieinfo.get(i).getName(),movieinfo.get(i).getPic(),movieinfo.get(i).getType(),movieinfo.get(i).getAddress(),movieinfo.get(i).getAssociations(),movieinfo.get(i).getEducation(),movieinfo.get(i).getCompany());
		       	    	    new_names.add(new_param);
		       	    	    adapter.notifyDataSetChanged();
		       	    	    

		       	    	}
							}
						}
						if(selector.equalsIgnoreCase("location"))
						{
							if(movieinfo.get(i).getAssociations()!=null)
							{
		       	    	if(movieinfo.get(i).getAssociations().toLowerCase().contains(newText.toLowerCase()))

		       	    	{
		       	    		Param new_param = new Param(movieinfo.get(i).getUrl(),movieinfo.get(i).getName(),movieinfo.get(i).getPic(),movieinfo.get(i).getType(),movieinfo.get(i).getAddress(),movieinfo.get(i).getAssociations(),movieinfo.get(i).getEducation(),movieinfo.get(i).getCompany());
		       	    	    new_names.add(new_param);
		       	    	    adapter.notifyDataSetChanged();
		       	    	    

		       	    	}
						}
						}
						if(selector.equalsIgnoreCase("industry"))
						{
							if(movieinfo.get(i).getEducation()!=null)
							{
		       	    	if(movieinfo.get(i).getEducation().toLowerCase().contains(".*"+newText.toLowerCase()+".*"))

		       	    	{
		       	    		Param new_param = new Param(movieinfo.get(i).getUrl(),movieinfo.get(i).getName(),movieinfo.get(i).getPic(),movieinfo.get(i).getType(),movieinfo.get(i).getAddress(),movieinfo.get(i).getAssociations(),movieinfo.get(i).getEducation(),movieinfo.get(i).getCompany());
		       	    	    new_names.add(new_param);
		       	    	    adapter.notifyDataSetChanged();
		       	    	    

		       	    	}
							}
						}
						
						if(selector.equalsIgnoreCase("Twitter_Location"))
						{
							if(movieinfo.get(i).getType()!=null)
							{
								
		       	    	if(movieinfo.get(i).getType().toUpperCase().contains(newText.toUpperCase()))

		       	    	{
		       	    		Log.d("string", movieinfo.get(i).getType().toUpperCase());
		       	    		Log.d("string", newText.toUpperCase());
		       	    		Log.d("head", "head");
		       	    		Param new_param = new Param(movieinfo.get(i).getUrl(),movieinfo.get(i).getName(),movieinfo.get(i).getPic(),movieinfo.get(i).getType(),movieinfo.get(i).getAddress(),movieinfo.get(i).getAssociations(),movieinfo.get(i).getEducation(),movieinfo.get(i).getCompany());
		       	    	    new_names.add(new_param);
		       	    	    adapter.notifyDataSetChanged();
		       	    	    

		       	    	}
							}
						}
						
						
						if(selector.equalsIgnoreCase("Description"))
						{
							if(movieinfo.get(i).getType()!=null)
							{
								
		       	    	if(movieinfo.get(i).getEducation().toUpperCase().contains(newText.toUpperCase()))

		       	    	{
		       	    		Log.d("string", movieinfo.get(i).getType().toUpperCase());
		       	    		Log.d("string", newText.toUpperCase());
		       	    		Log.d("head", "head");
		       	    		Param new_param = new Param(movieinfo.get(i).getUrl(),movieinfo.get(i).getName(),movieinfo.get(i).getPic(),movieinfo.get(i).getType(),movieinfo.get(i).getAddress(),movieinfo.get(i).getAssociations(),movieinfo.get(i).getEducation(),movieinfo.get(i).getCompany());
		       	    	    new_names.add(new_param);
		       	    	    adapter.notifyDataSetChanged();
		       	    	    

		       	    	}
							}
						}
						
						if(selector.equalsIgnoreCase("Type"))
						{
							if(movieinfo.get(i).getType()!=null)
							{
								
		       	    	if(movieinfo.get(i).getType().toUpperCase().contains(newText.toUpperCase()))

		       	    	{
		       	    		Log.d("string", movieinfo.get(i).getType().toUpperCase());
		       	    		Log.d("string", newText.toUpperCase());
		       	    		Log.d("head", "head");
		       	    		Param new_param = new Param(movieinfo.get(i).getUrl(),movieinfo.get(i).getName(),movieinfo.get(i).getPic(),movieinfo.get(i).getType(),movieinfo.get(i).getAddress(),movieinfo.get(i).getAssociations(),movieinfo.get(i).getEducation(),movieinfo.get(i).getCompany());
		       	    	    new_names.add(new_param);
		       	    	    adapter.notifyDataSetChanged();
		       	    	    

		       	    	}
							}
						}
						
						
						if(selector.equalsIgnoreCase("Locale"))
						{
							if(movieinfo.get(i).getType()!=null)
							{
								
		       	    	if(movieinfo.get(i).getType().toUpperCase().contains(newText.toUpperCase()))

		       	    	{
		       	    		Log.d("string", movieinfo.get(i).getType().toUpperCase());
		       	    		Log.d("string", newText.toUpperCase());
		       	    		Log.d("head", "head");
		       	    		Param new_param = new Param(movieinfo.get(i).getUrl(),movieinfo.get(i).getName(),movieinfo.get(i).getPic(),movieinfo.get(i).getType(),movieinfo.get(i).getAddress(),movieinfo.get(i).getAssociations(),movieinfo.get(i).getEducation(),movieinfo.get(i).getCompany());
		       	    	    new_names.add(new_param);
		       	    	    adapter.notifyDataSetChanged();
		       	    	    

		       	    	}
							}
						}
						
						
						if(selector.equalsIgnoreCase("Username"))
						{
							if(movieinfo.get(i).getType()!=null)
							{
								
		       	    	if(movieinfo.get(i).getEducation().toUpperCase().contains(newText.toUpperCase()))

		       	    	{
		       	    		Log.d("string", movieinfo.get(i).getType().toUpperCase());
		       	    		Log.d("string", newText.toUpperCase());
		       	    		Log.d("head", "head");
		       	    		Param new_param = new Param(movieinfo.get(i).getUrl(),movieinfo.get(i).getName(),movieinfo.get(i).getPic(),movieinfo.get(i).getType(),movieinfo.get(i).getAddress(),movieinfo.get(i).getAssociations(),movieinfo.get(i).getEducation(),movieinfo.get(i).getCompany());
		       	    	    new_names.add(new_param);
		       	    	    adapter.notifyDataSetChanged();
		       	    	    

		       	    	}
							}
						}
						
		       	    }
					
					adapter = new MovieAdapter(NamesActivity.this,
							R.layout.activity_movielist, new_names,decider);
					listview.setAdapter(adapter);
					        adapter.notifyDataSetChanged();
				}
				else
				{
					check_search=1;
					adapter = new MovieAdapter(NamesActivity.this,
							R.layout.activity_movielist, movieinfo,decider);
				       listview.setAdapter(adapter);
				        adapter.notifyDataSetChanged();
				}
					
				return false;
			}
		});
		
	        searchView.setOnCloseListener(new OnCloseListener() {
				
				@Override
				public boolean onClose() {
					// TODO Auto-generated method stub
					listview.setAdapter(adapter);
					return false;
				}
			});
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(NamesActivity.this,WebActivity.class);
				if(check_search==1)
				{
				
				intent.putExtra("WEB", movieinfo.get(arg2).getUrl());
				}
				else
				{
					intent.putExtra("WEB", new_names.get(arg2).getUrl());
				}
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.names, menu);
		
		return true;
	}
	

}

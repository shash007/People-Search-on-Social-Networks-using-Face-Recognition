package com.example.face_recog;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;


@SuppressLint("NewApi")
public class TabActivity extends FragmentActivity {
	  ViewPager Tab;
	   TabsPagerAdapter TabAdapter;
	  ActionBar actionBar;
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_tab);
	        setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
	        TabAdapter = new TabsPagerAdapter(getSupportFragmentManager());
	        Tab = (ViewPager)findViewById(R.id.pager);
	        Tab.setAdapter(TabAdapter);
	        Tab.setOnPageChangeListener(
	                new ViewPager.SimpleOnPageChangeListener() {
	                    @Override
	                    public void onPageSelected(int position) {
	                      actionBar = getActionBar();
	                      actionBar.setSelectedNavigationItem(position);                    }
	                });
	        actionBar = getActionBar();
	        //Enable Tabs on Action Bar
	        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	        ActionBar.TabListener tabListener = new ActionBar.TabListener(){
	      @Override
	      public void onTabReselected(android.app.ActionBar.Tab tab,
	          FragmentTransaction ft) {
	        // TODO Auto-generated method stub
	      }
	      @Override
	       public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	              Tab.setCurrentItem(tab.getPosition());
	          }
	      @Override
	      public void onTabUnselected(android.app.ActionBar.Tab tab,
	          FragmentTransaction ft) {
	        // TODO Auto-generated method stub
	      }};
	      //Add New Tab
	      actionBar.addTab(actionBar.newTab().setText("Web Recognition").setTabListener(tabListener));
	      actionBar.addTab(actionBar.newTab().setText("Server Recognition").setTabListener(tabListener));
	      
	    }
	}

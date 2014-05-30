package com.example.face_recog;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class TabsPagerAdapter extends FragmentStatePagerAdapter {
    public TabsPagerAdapter(FragmentManager fm) {
    super(fm);
    // TODO Auto-generated constructor stub
  }
  @Override
  public Fragment getItem(int i) {
    switch (i) {
        case 0:
            //Fragement for Android Tab
            return new MainActivity();
        case 1:
           //Fragment for Ios Tab
            return new Server_Activity();
       
        }
    return null;
  }
  @Override
  public int getCount() {
    // TODO Auto-generated method stub
    return 2; //No of Tabs
  }
    }
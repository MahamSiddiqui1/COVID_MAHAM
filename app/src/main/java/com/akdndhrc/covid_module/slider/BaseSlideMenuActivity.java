package com.akdndhrc.covid_module.slider;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.akdndhrc.covid_module.R;


public class BaseSlideMenuActivity extends AppCompatActivity {
	public SlideMenu mSlideMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.slide_menu_layout);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		mSlideMenu = (SlideMenu) findViewById(R.id.slideMenu);
	}

	public void setSlideRole(int res) {
		if (null == mSlideMenu) {
			return;
		}

		getLayoutInflater().inflate(res, mSlideMenu, true);
	}

	public SlideMenu getSlideMenu() {
		return mSlideMenu;
	}


}

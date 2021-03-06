package com.example.nextbigthing;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends Activity
{
	private static final int ACTION_START_APP = 1;
	private static final int ACTION_TAKE_PICTURE = 2;
	private EditText userName;
	private EditText passWord;
	private ParseUser currentUser;
	private int userNameSet = 0;
	private int passWordSet = 0;
	
	public static boolean isIntentAvailable(Context context, String action){
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = 
				packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
	
	private void setBtnListenerOrDisable(
			Button btn,
			Button.OnClickListener onClickListener
	) {
		btn.setOnClickListener(onClickListener);
	}
	
	Button.OnClickListener mStartApp = 
			new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				dispatchStartAppIntent(ACTION_START_APP);
			}
		};
		
	Button.OnClickListener mLoadApp = 
			new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				dispatchLoadAppIntent(ACTION_START_APP);
			}
		};	
		
	private void dispatchStartAppIntent(int actionCode){
		
		if (actionCode == ACTION_START_APP){
			//startActivityForResult(startApp, actionCode);
			if ((userName.getText().toString().equals("User Name")) || (userName.getText().toString().equals(""))){
				Log.d("Main Activity", "No User Name entered");
				Toast.makeText(getApplicationContext(), "Please enter a User Name", Toast.LENGTH_LONG).show();
				userNameSet = 0;
				passWordSet = 0;
			}
			else{
				userNameSet = 1;
			}
			
			if ((passWord.getText().toString().equals("Password")) || (passWord.getText().toString().equals(""))){
				Log.d("Main Activity", "No Password entered");
				Toast.makeText(getApplicationContext(), "Please enter a Password", Toast.LENGTH_LONG).show();
				userNameSet = 0;
				passWordSet = 0;
			}
			else{
				passWordSet = 1;
			}

			if (userNameSet == 1 && passWordSet == 1){
				
				ParseUser.logInInBackground(userName.getText().toString(), passWord.getText().toString(), new LogInCallback() {
					public void done(ParseUser user, ParseException d){
						if (user != null){
							Log.d("MainActivity", "Old user logged in");
							currentUser = ParseUser.getCurrentUser();
							if (currentUser != null){
								Log.d("Main Activity", "Starting load picture");
								Intent startApp = new Intent(MainActivity.this, LoadPicture.class);
								startActivityForResult(startApp, ACTION_START_APP);
							}
						}
						else{
							ParseUser newUser = new ParseUser();
							newUser.setUsername(userName.getText().toString());
							newUser.setPassword(passWord.getText().toString());
							newUser.signUpInBackground(new SignUpCallback() {
								public void done(ParseException e) {
									if (e == null){
										Log.d("Main Activity", "New user created");
										Toast.makeText(getApplicationContext(), "Successful Sign up", Toast.LENGTH_LONG).show();
										currentUser = ParseUser.getCurrentUser();
										if (currentUser != null){
											Log.d("Main Activity", "Starting load picture");
											Intent startApp = new Intent(MainActivity.this, LoadPicture.class);
											startActivityForResult(startApp, ACTION_START_APP);
										}
									}
									else{
										Log.d("Main Activity", "New user NOT created");
										Toast.makeText(getApplicationContext(), "Unsuccessful Sign up, please try again", Toast.LENGTH_LONG).show();
									}
								}
							});
						}
					}	
				});
			}
			
			
		}	
	}
	
	private void dispatchLoadAppIntent(int actionCode){
		Intent startApp = new Intent(this, LoadPicture.class);
		
		if (actionCode == ACTION_START_APP){
			startActivityForResult(startApp, actionCode);
		}	
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		
		//Button startBtn = (Button) findViewById(R.id.btnStart);
		//Button loadBtn = (Button) findViewById(R.id.btnLoad);
		/*
		setBtnListenerOrDisable(
				startBtn,
				mStartApp
				);
		*/
		/*
		setBtnListenerOrDisable(
				loadBtn,
				mLoadApp
				);
		*/
		//Parse stuff
		Parse.initialize(this, "vQm6jpJhvdC7DJavE1aOFcb7ytTBUV1wPden4jmy", "Oj1hVCxed731RsvGMExhbS5TjVWoAL2nR71FpqLZ");
		ParseAnalytics.trackAppOpened(getIntent());
		
		currentUser = ParseUser.getCurrentUser();

		if (currentUser != null){	
			/*
			Intent take_picture = new Intent(this, PictureCapture.class);
			startActivityForResult(take_picture, ACTION_TAKE_PICTURE);
			*/
			
			Intent load_picture = new Intent(this, LoadPicture.class);
			startActivityForResult(load_picture, ACTION_START_APP);
			 
		}
		else{
			setContentView(R.layout.activity_main);
			
			Button startBtn = (Button) findViewById(R.id.btnStart);
			userName = (EditText) findViewById(R.id.userName);
			passWord = (EditText) findViewById(R.id.passWord);
			userNameSet = 0;
			passWordSet = 0;
			
			setBtnListenerOrDisable(
					startBtn,
					mStartApp
					);
			
		}
		
	}
	
	
	
	
/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTION_TAKE_PHOTO_B) {
			if (resultCode == RESULT_OK) {
				handleBigCameraPhoto();
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap1);
		outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap1 != null));
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mImageBitmap1 = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
		mImageView1.setImageBitmap(mImageBitmap1);
		mImageView1.setVisibility(
			savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
					ImageView.VISIBLE : ImageView.INVISIBLE
		);
		
	}
	*/
}
	

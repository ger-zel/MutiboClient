package ui;

import org.apache.commons.io.output.ThresholdingOutputStream;

import client.ExchangeService;
import client.SrvClient;

import com.mutiboclient.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

public class StartActivity extends Activity{
	
	private Boolean mLoggedIn = false;
	
	private EditText mLoginText;
	
	private EditText mPassText;
	
	private Button mSignInButton;
	
	private ImageButton mQuitButton;
	
	private ImageButton mPlayButton;
	
	private ImageButton mLeadersButton;
	
	private IntentFilter mFilterCheck;
	
	private ProgressBar mSpinner;
	
	public class UserPointsReceiver extends BroadcastReceiver {
		public static final String ACTION_RESP = ExchangeService.GET_USER_POINTS_CALL;
		    
		@Override	
		public void onReceive(Context context, Intent intent) {
			
			mSpinner.setVisibility(View.GONE);
			
			if (intent.hasExtra(ExchangeService.EXTRA_STATUS_RESPONSE_ERROR)) {
				Toast.makeText(getApplicationContext(), "Sign in failed", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "Sign in successfull", Toast.LENGTH_LONG).show();
				mSignInButton.setVisibility(View.INVISIBLE);
				mPassText.setVisibility(View.INVISIBLE);			
				mLoggedIn = true;
			}
			
			unregisterReceiver(mUserPointsReceiver);
		}
	}
	
	private UserPointsReceiver mUserPointsReceiver;
			
	@Override
	protected void onCreate(Bundle savedInstanseState){
		
		super.onCreate(savedInstanseState);
		setContentView(R.layout.start_activity);
		
		mLoginText = (EditText) findViewById(R.id.loginText);
		
		mPassText = (EditText) findViewById(R.id.passText);
		
		mSpinner = (ProgressBar) findViewById(R.id.loginSpinner);
		
		mSpinner.setVisibility(View.GONE);
		
		mSignInButton = (Button) findViewById(R.id.signInButton);
				
		mFilterCheck = new IntentFilter(ExchangeService.GET_USER_POINTS_CALL);
		
		mFilterCheck.addCategory(Intent.CATEGORY_DEFAULT);
		
		mSignInButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("StartActivity", "login");
				
				String user = mLoginText.getText().toString();
				String pass = mPassText.getText().toString();
				
				mSpinner.setVisibility(View.VISIBLE);
				
				SrvClient.create(user, pass);
				
				Intent exchangeIntent = new Intent(StartActivity.this, ExchangeService.class);
				
				exchangeIntent.putExtra(ExchangeService.PARAM_IN_MSG, ExchangeService.GET_USER_POINTS_CALL);

				mUserPointsReceiver = new UserPointsReceiver();
				
				registerReceiver(mUserPointsReceiver, mFilterCheck);
				
				startService(exchangeIntent);
			}
		});
		
		mQuitButton = (ImageButton) findViewById(R.id.quitButton);
		
		mQuitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}			
		});
		
		mPlayButton = (ImageButton) findViewById(R.id.playButton);
		
		mPlayButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (mLoggedIn) {
					Intent startPlay = new Intent(StartActivity.this, RoundActivity.class);
					
					startActivity(startPlay);
				} else {
					Toast.makeText(getApplicationContext(), "Sign in first!", Toast.LENGTH_LONG).show();
				}			
			}			
		});
		
		mLeadersButton = (ImageButton) findViewById(R.id.leadersButton);
		
		mLeadersButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (mLoggedIn) {
					Intent startLeaders= new Intent(StartActivity.this, LeadersActivity.class);
					
					startActivity(startLeaders);
				} else {
					Toast.makeText(getApplicationContext(), "Sign in first!", Toast.LENGTH_LONG).show();
				}	
			}
		});
		
		Log.d("StartActivity", "onCreate");
	}
	
}

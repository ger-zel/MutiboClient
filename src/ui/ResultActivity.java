package ui;

import client.ExchangeService;

import com.mutiboclient.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity{
	
	public final static String EXTRA_POINS_LONG = "extraPointsLong";
	
	private ImageButton mReturn;
	private TextView mPoints;
	private ProgressBar mSpinner;
	
	private Long mUserPoints;
	
	private UserPointsReceiver mUserPointsReceiver;	
	private IntentFilter mFilterCheck;
	
	public class UserPointsReceiver extends BroadcastReceiver {
		public static final String ACTION_RESP = ExchangeService.SET_USER_POINTS_CALL;
		    
		@Override	
		public void onReceive(Context context, Intent intent) {
			
			mSpinner.setVisibility(View.GONE);
			
			if (intent.hasExtra(ExchangeService.EXTRA_STATUS_RESPONSE_ERROR)) {
				Toast.makeText(getApplicationContext(), "Update user points failed", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "Update user points successfull", Toast.LENGTH_LONG).show();
			}
			
			unregisterReceiver(mUserPointsReceiver);
			
			finish();
		}
	}
	
	protected void onCreate(Bundle savedInstanseState){
					
		super.onCreate(savedInstanseState);
		setContentView(R.layout.result_activity);
		
		mPoints = (TextView) findViewById(R.id.textView3);
		
		mReturn = (ImageButton) findViewById(R.id.playButton);
		
		mSpinner = (ProgressBar) findViewById(R.id.progressBar1);

		mSpinner.setVisibility(View.GONE);
		
		Intent intent = getIntent();
		
		mUserPoints = intent.getLongExtra(EXTRA_POINS_LONG, 0L);
		
		mPoints.setText(mUserPoints.toString());	
		
		mReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mSpinner.setVisibility(View.VISIBLE);
				
				mFilterCheck = new IntentFilter(ExchangeService.SET_USER_POINTS_CALL);
				
				mFilterCheck.addCategory(Intent.CATEGORY_DEFAULT);
				
				Intent exchangeIntent = new Intent(ResultActivity.this, ExchangeService.class);
				
				exchangeIntent.putExtra(ExchangeService.PARAM_IN_MSG, ExchangeService.SET_USER_POINTS_CALL);
				
				exchangeIntent.putExtra(ExchangeService.EXTRA_LONG, mUserPoints);

				mUserPointsReceiver = new UserPointsReceiver();
				
				registerReceiver(mUserPointsReceiver, mFilterCheck);
				
				startService(exchangeIntent);
			}
		});		
	}

}

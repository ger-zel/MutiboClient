package ui;

import java.util.Map;

import ui.StartActivity.UserPointsReceiver;
import client.ExchangeService;
import client.LeadersListAdapter;

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
import android.widget.ListView;
import android.widget.ProgressBar;

public class LeadersActivity extends Activity{
	
	ImageButton mReturnButton;
	ProgressBar mSpinner;
	
	LeadersReceiver mLeadersReceiver;
	private IntentFilter mFilterCheck;
	
	ListView mLeadersList;
	LeadersListAdapter mAdapter;
	
	Map<String, Long> mLeaders;
	public class LeadersReceiver extends BroadcastReceiver {
		public static final String ACTION_RESP = ExchangeService.GET_LEADERS_CALL;
		    
		@Override	
		public void onReceive(Context context, Intent intent) {
			
			mSpinner.setVisibility(View.GONE);
			
			mLeaders = (Map<String, Long>) intent.getExtras().getSerializable(ExchangeService.EXTRA_LEADERS_MAP);
			
			mAdapter = new LeadersListAdapter(mLeaders);
			
			mLeadersList.setAdapter(mAdapter);
			
			unregisterReceiver(mLeadersReceiver);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leaders_activity);
		
		mReturnButton = (ImageButton) findViewById(R.id.playButton);
		
		mSpinner = (ProgressBar) findViewById(R.id.progressBar1);
		
		mSpinner.setVisibility(View.VISIBLE);
		
		mLeadersList = (ListView) findViewById(android.R.id.list);
		
		mFilterCheck =  new IntentFilter(ExchangeService.GET_LEADERS_CALL);
		
		mFilterCheck.addCategory(Intent.CATEGORY_DEFAULT);
		
		Intent exchangeIntent = new Intent(LeadersActivity.this, ExchangeService.class);
		
		exchangeIntent.putExtra(ExchangeService.PARAM_IN_MSG, ExchangeService.GET_LEADERS_CALL);

		mLeadersReceiver = new LeadersReceiver();
		
		registerReceiver(mLeadersReceiver, mFilterCheck);
		
		startService(exchangeIntent);
		
		mReturnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();			
			}
		});		
	}
}

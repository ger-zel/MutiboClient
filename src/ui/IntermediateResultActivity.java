package ui;

import ui.RoundActivity.GameSetReceiver;
import client.ExchangeService;
import client.GameSet;

import com.mutiboclient.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class IntermediateResultActivity extends Activity{
	
	public final static String PARAM_CORRECTNESS = "paramCorrectness";
	public final static String PARAM_CORRECT_FILM_NAME = "paramCorrectName";
	public final static String PARAM_USER_FILM = "paramUserFilmName";
	public final static String PARAM_EXPLANATION = "paramExplanation";
	public final static String PARAM_CURR_FAILS = "paramCurrFails";
	public final static String PARAM_SCORE = "paramScore";
	public final static String PARAM_PASS = "paramPass";
	public final static String PARAM_FINISH = "paramFinish";
	public final static String PARAM_ID = "paramId";
	public final static String VALUE_TRUE = "valueTrue";
	public final static String VALUE_FALSE = "valueFalse";
	
	private TextView mResult;
	private TextView mUserChoiseText;
	private TextView mUserChoiseFilmNAme;
	private TextView mCorrectFilmName;
	private TextView mExplanation;
	private TextView mFailsNum;
	private TextView mScore;
	
	private ImageButton mLikeButton;
	private ImageButton mUnlikeButton;
	private ProgressBar mSpinner;
	
	private String mFinishStatus;
	private Boolean mFinished = false;
	
	private Long mScoreLong;
	private Long mFailsLong;
	private Long mGameSetId;
	
	private RateReceiver mRateReceiver;
	private IntentFilter mFilterGetGameSet;
	
	public class RateReceiver extends BroadcastReceiver {
		public static final String ACTION_RESP = ExchangeService.GET_USER_POINTS_CALL;
		    
		@Override	
		public void onReceive(Context context, Intent intent) {
			
			mSpinner.setVisibility(View.GONE);
			
			if (intent.hasExtra(ExchangeService.EXTRA_STATUS_RESPONSE_ERROR)) {
				Toast.makeText(getApplicationContext(), "Probably you allready rated this set", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "Rate game set success", Toast.LENGTH_LONG).show();
			}
		
			unregisterReceiver(mRateReceiver);
			
			if (!mFinishStatus.equals(VALUE_TRUE)) {
				finish();
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanseState){
		
		super.onCreate(savedInstanseState);
		setContentView(R.layout.intermediate_result_activity);
		
		mResult = (TextView) findViewById(R.id.textView5);
		mUserChoiseText = (TextView) findViewById(R.id.textView1);
		mUserChoiseFilmNAme = (TextView) findViewById(R.id.TextView01);
		mCorrectFilmName = (TextView) findViewById(R.id.TextView03);
		mExplanation = (TextView) findViewById(R.id.FilmName1);
		mFailsNum = (TextView) findViewById(R.id.textView4);
		mScore = (TextView) findViewById(R.id.TextView05);
		
		mLikeButton = (ImageButton) findViewById(R.id.settingsButton);
		mUnlikeButton = (ImageButton) findViewById(R.id.playButton);
		mSpinner = (ProgressBar) findViewById(R.id.progressBar1);
		
		mSpinner.setVisibility(View.INVISIBLE);
		
		Intent intent = getIntent();
		
		String status = intent.getStringExtra(PARAM_CORRECTNESS);
		
		if (status.equals(VALUE_TRUE)) {			
			mResult.setText("Correct.");
			mResult.setTextColor(Color.GREEN);	
			mUserChoiseFilmNAme.setVisibility(View.INVISIBLE);
			mUserChoiseText.setVisibility(View.INVISIBLE);
			MediaPlayer mp = MediaPlayer.create(this, R.raw.applause4);
			mp.start();
		} else {		
			mResult.setText("Incorrect.");
			mResult.setTextColor(Color.RED);
		}
		
		mScoreLong = intent.getLongExtra(PARAM_SCORE, 0L);
		mFailsLong = intent.getLongExtra(PARAM_CURR_FAILS, 0L);
		
		mGameSetId = intent.getLongExtra(PARAM_ID, 0L);
		
		mCorrectFilmName.setText(intent.getStringExtra(PARAM_CORRECT_FILM_NAME));
		mUserChoiseFilmNAme.setText(intent.getStringExtra(PARAM_USER_FILM));
		mExplanation.setText(intent.getStringExtra(PARAM_EXPLANATION));
		mFailsNum.setText(mFailsLong.toString());
		mScore.setText(mScoreLong.toString());		
		
		mFinishStatus = intent.getStringExtra(PARAM_FINISH);
						
		mLikeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				mSpinner.setVisibility(View.VISIBLE);
				
				Intent exchangeIntent = new Intent(IntermediateResultActivity.this, ExchangeService.class);
				
				exchangeIntent.putExtra(ExchangeService.PARAM_IN_MSG, ExchangeService.LIKE_GAME_SET_CALL);
				exchangeIntent.putExtra(ExchangeService.PARAM_ID, mGameSetId);
				
				mFilterGetGameSet = new IntentFilter(ExchangeService.LIKE_GAME_SET_CALL);
				
				mFilterGetGameSet.addCategory(Intent.CATEGORY_DEFAULT);

				mRateReceiver = new RateReceiver();
				
				registerReceiver(mRateReceiver, mFilterGetGameSet);
				
				startService(exchangeIntent);
				
				if (mFinishStatus.equals(VALUE_TRUE)) {
//					Log.d("IntermediateResultActivity", "Finish");
					
					mFinished = true;
					
					Intent result = new Intent(IntermediateResultActivity.this, ResultActivity.class);
					
					result.putExtra(ResultActivity.EXTRA_POINS_LONG, mScoreLong);
					
					startActivity(result);
					
				} 
//				else {
//					Log.d("IntermediateResultActivity", "Next");
//					
//					finish();
//				}
			}
		});
		
		mUnlikeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mSpinner.setVisibility(View.VISIBLE);
				
				Intent exchangeIntent = new Intent(IntermediateResultActivity.this, ExchangeService.class);
				
				exchangeIntent.putExtra(ExchangeService.PARAM_IN_MSG, ExchangeService.UNLIKE_GAME_SET_CALL);
				exchangeIntent.putExtra(ExchangeService.PARAM_ID, mGameSetId);
				
				mFilterGetGameSet = new IntentFilter(ExchangeService.UNLIKE_GAME_SET_CALL);
				
				mFilterGetGameSet.addCategory(Intent.CATEGORY_DEFAULT);

				mRateReceiver = new RateReceiver();
				
				registerReceiver(mRateReceiver, mFilterGetGameSet);
				
				startService(exchangeIntent);
				
				if (mFinishStatus.equals(VALUE_TRUE)) {
//					Log.d("IntermediateResultActivity", "Finish");
					
					mFinished = true;
					
					Intent result = new Intent(IntermediateResultActivity.this, ResultActivity.class);
					
					result.putExtra(ResultActivity.EXTRA_POINS_LONG, mScoreLong);
					
					startActivity(result);
					
				} 
//				else {
//					Log.d("IntermediateResultActivity", "Next");
//					
//					finish();
//				}
			}
		});
	}
	
	@Override
	protected void onResume(){
		
		super.onResume();
		
		if (mFinished) {
			finish();
		}
	}

}

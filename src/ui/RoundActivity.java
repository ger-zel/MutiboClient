package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ui.StartActivity.UserPointsReceiver;
import client.ExchangeService;
import client.GameSet;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class RoundActivity extends Activity{
	
	public final static int MAX_ROUND_COUNT = 6;
	public final static int MAX_FAILS_COUNT = 3;
		
	private CheckBox[] mCheckBoxArray = new CheckBox[4];
	
	private TextView[] mFileNameArray = new TextView[4];
	
	private ImageButton mFiftyFiftyButton;
	private ImageButton mPassButton;
	private ImageButton mGoCheckButton;
	private ProgressBar mSpinner;
	
	private Long mFailsCount = 0L;
	private Long mRoundCount = 0L;
	
	private Long mCurrentScore= 0L;
	
	private Boolean mIsChecked = false;
	private Integer mUserChoiseId = -1;
	private Boolean mIsFiftyFiftyUsed = false;
	private Integer setListId = 0;
	private Boolean mGameSetIsLoaded = false;
	private Boolean mLoadInReceiver = true;
	
	private GameSetReceiver mGameSetReceiver;
	private IntentFilter mFilterGetGameSet;
	
	private Random mRandom = new Random();
	
	private GameSet gameSet = null;
	private ArrayList<GameSet> gameSetList;
	
	public class GameSetReceiver extends BroadcastReceiver {
		public static final String ACTION_RESP = ExchangeService.GET_USER_POINTS_CALL;
		    
		@Override	
		public void onReceive(Context context, Intent intent) {
			
			mSpinner.setVisibility(View.GONE);
			
//			gameSet = (GameSet) intent.getSerializableExtra(ExchangeService.EXTRA_GAME_SET);
			gameSetList = (ArrayList<GameSet>) intent.getSerializableExtra(ExchangeService.EXTRA_GAME_SET);
			
			if (gameSetList != null) {
				
				if (mLoadInReceiver) {
					
					mFileNameArray[0].setText(gameSetList.get(setListId).getFirstFilmName());
					mFileNameArray[1].setText(gameSetList.get(setListId).getSecondFilmName()); 
					mFileNameArray[2].setText(gameSetList.get(setListId).getThirdFilmName()); 
					mFileNameArray[3].setText(gameSetList.get(setListId).getFourthFilmName());
					
					mLoadInReceiver = false;
				}
					
				mGameSetIsLoaded = true;
			} else {
				Toast.makeText(getApplicationContext(), "Can't load game set", Toast.LENGTH_LONG).show();
			}
			
			unregisterReceiver(mGameSetReceiver);
		}
	}
		
	@Override
	protected void onCreate(Bundle savedInstanseState){
		
		Log.d("RoundActivity", "onCreate()");
		
		super.onCreate(savedInstanseState);
		setContentView(R.layout.round_activity);
				
		mCheckBoxArray[0] = (CheckBox) findViewById(R.id.CheckBoxFilm1);
		mCheckBoxArray[1] = (CheckBox) findViewById(R.id.CheckBoxFilm2);
		mCheckBoxArray[2] = (CheckBox) findViewById(R.id.CheckBoxFilm3);
		mCheckBoxArray[3] = (CheckBox) findViewById(R.id.CheckBoxFilm4);
		
		mCheckBoxArray[0].setChecked(false);
		mCheckBoxArray[1].setChecked(false);
    	mCheckBoxArray[2].setChecked(false);
    	mCheckBoxArray[3].setChecked(false);
    	
    	mCheckBoxArray[0].setVisibility(View.VISIBLE);
    	mCheckBoxArray[1].setVisibility(View.VISIBLE);
    	mCheckBoxArray[2].setVisibility(View.VISIBLE);
    	mCheckBoxArray[3].setVisibility(View.VISIBLE);
		
		mFileNameArray[0] = (TextView) findViewById(R.id.FilmName1);
		mFileNameArray[1] = (TextView) findViewById(R.id.FilmName2);
		mFileNameArray[2] = (TextView) findViewById(R.id.FilmName3);
		mFileNameArray[3] = (TextView) findViewById(R.id.FilmName4);
		
		mFiftyFiftyButton = (ImageButton)findViewById(R.id.quitButton); // change name if possible
		mPassButton= (ImageButton)findViewById(R.id.playButton); // change name if possible
		mGoCheckButton = (ImageButton)findViewById(R.id.settingsButton); // change name if possible
		
		mSpinner = (ProgressBar) findViewById(R.id.progressBar1);
		mSpinner.setVisibility(View.GONE);
		
		if (mLoadInReceiver) {
			
			mSpinner.setVisibility(View.VISIBLE);
			
			loadGameSet();
		}
				
		mFiftyFiftyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (!mIsFiftyFiftyUsed) {
					
					int id_1, id_2;
					
					long unixTime = System.currentTimeMillis() / 1000L;
					mRandom.setSeed(unixTime);
					
					do {
						id_1 = mRandom.nextInt(4);
						id_2 = mRandom.nextInt(4);
					} while (gameSetList.get(setListId).getOddId() == id_1+1 || gameSetList.get(setListId).getOddId() == id_2+1 || id_1 == id_2);				
					
					mCheckBoxArray[id_1].setVisibility(View.INVISIBLE);
					mCheckBoxArray[id_2].setVisibility(View.INVISIBLE);
					
					mCheckBoxArray[0].setChecked(false);
					mCheckBoxArray[1].setChecked(false);
					mCheckBoxArray[2].setChecked(false);
					mCheckBoxArray[3].setChecked(false);
					
					mIsChecked = true;
					
					mIsFiftyFiftyUsed = true;
				}				
			}
		});
		
		mGoCheckButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (mIsChecked) {	
					
					mRoundCount++;
					
					Intent intent = new Intent(RoundActivity.this, IntermediateResultActivity.class);
					
					if (mUserChoiseId == gameSetList.get(setListId).getOddId()) {
						intent.putExtra(IntermediateResultActivity.PARAM_CORRECTNESS, IntermediateResultActivity.VALUE_TRUE);
						mCurrentScore++;
					} else {
						intent.putExtra(IntermediateResultActivity.PARAM_CORRECTNESS, IntermediateResultActivity.VALUE_FALSE);
						mFailsCount++;
					}					
					
					if (mRoundCount == MAX_ROUND_COUNT || mFailsCount == MAX_FAILS_COUNT) {
						intent.putExtra(IntermediateResultActivity.PARAM_FINISH, IntermediateResultActivity.VALUE_TRUE);
					} else {
						intent.putExtra(IntermediateResultActivity.PARAM_FINISH, IntermediateResultActivity.VALUE_FALSE);
					}
					
					intent.putExtra(IntermediateResultActivity.PARAM_USER_FILM, mFileNameArray[mUserChoiseId - 1].getText().toString());
					intent.putExtra(IntermediateResultActivity.PARAM_CORRECT_FILM_NAME, mFileNameArray[(int) gameSetList.get(setListId).getOddId() - 1].getText().toString());
					intent.putExtra(IntermediateResultActivity.PARAM_EXPLANATION, gameSetList.get(setListId).getExplanation());
					intent.putExtra(IntermediateResultActivity.PARAM_CURR_FAILS, mFailsCount);
					intent.putExtra(IntermediateResultActivity.PARAM_SCORE, mCurrentScore);
					intent.putExtra(IntermediateResultActivity.PARAM_ID, gameSetList.get(setListId).getId());
					
					setListId++;
					
					startActivity(intent);
					
				} else {
					Toast.makeText(getApplicationContext(), "Make your choise.", Toast.LENGTH_LONG).show();
				}
			}
			
		});
		
		mPassButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mRoundCount++;

//				mFailsCount++;				
				
				Intent intent = new Intent(RoundActivity.this, IntermediateResultActivity.class);
					
				intent.putExtra(IntermediateResultActivity.PARAM_CORRECTNESS, IntermediateResultActivity.VALUE_FALSE);
				
				if (mRoundCount == MAX_ROUND_COUNT || mFailsCount == MAX_FAILS_COUNT) {
					intent.putExtra(IntermediateResultActivity.PARAM_FINISH, IntermediateResultActivity.VALUE_TRUE);
				} else {
					intent.putExtra(IntermediateResultActivity.PARAM_FINISH, IntermediateResultActivity.VALUE_FALSE);
				}
				
//				intent.putExtra(IntermediateResultActivity.PARAM_USER_FILM, mFileNameArray[mUserChoiseId].getText().toString());
				intent.putExtra(IntermediateResultActivity.PARAM_CORRECT_FILM_NAME, mFileNameArray[(int) gameSetList.get(setListId).getOddId() - 1].getText().toString());
				intent.putExtra(IntermediateResultActivity.PARAM_EXPLANATION, gameSetList.get(setListId).getExplanation());
				intent.putExtra(IntermediateResultActivity.PARAM_CURR_FAILS, mFailsCount);
				intent.putExtra(IntermediateResultActivity.PARAM_SCORE, mCurrentScore);
				intent.putExtra(IntermediateResultActivity.PARAM_ID, gameSetList.get(setListId).getId());
				
				setListId++;
				
				startActivity(intent);
			}
			
		});

		mCheckBoxArray[0].setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	mCheckBoxArray[1].setChecked(false);
		        	mCheckBoxArray[2].setChecked(false);
		        	mCheckBoxArray[3].setChecked(false);	
		        	
		        	mIsChecked = true;
		        	
		        	mUserChoiseId = 1;
		        }
		    }
		});
		
		mCheckBoxArray[1].setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	mCheckBoxArray[0].setChecked(false);
		        	mCheckBoxArray[2].setChecked(false);
		        	mCheckBoxArray[3].setChecked(false);		 
		        	
		        	mIsChecked = true;
		        	
		        	mUserChoiseId = 2;
		        }
		    }
		});
		
		mCheckBoxArray[2].setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	mCheckBoxArray[0].setChecked(false);
		        	mCheckBoxArray[1].setChecked(false);
		        	mCheckBoxArray[3].setChecked(false);
		        	
		        	mIsChecked = true;
		        	
		        	mUserChoiseId = 3;
		        }
		    }
		});
		
		mCheckBoxArray[3].setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	mCheckBoxArray[0].setChecked(false);
		        	mCheckBoxArray[1].setChecked(false);
		        	mCheckBoxArray[2].setChecked(false);	
		        	
		        	mIsChecked = true;
		        	
		        	mUserChoiseId = 4;
		        }
		    }
		});
	}
	
	@Override
	protected void onResume(){
		
		super.onResume();
		
		Log.d("RoundActivity", "onResume()");
		
		if (mRoundCount == MAX_ROUND_COUNT || mFailsCount == MAX_FAILS_COUNT) {
			setListId = 1;
			finish();
		} else {
			
			mIsFiftyFiftyUsed = false;
			
			mCheckBoxArray[0].setChecked(false);
			mCheckBoxArray[1].setChecked(false);
			mCheckBoxArray[2].setChecked(false);
			mCheckBoxArray[3].setChecked(false);
	    	
	    	mCheckBoxArray[0].setVisibility(View.VISIBLE);
	    	mCheckBoxArray[1].setVisibility(View.VISIBLE);
	    	mCheckBoxArray[2].setVisibility(View.VISIBLE);
	    	mCheckBoxArray[3].setVisibility(View.VISIBLE);
			
			if (!mGameSetIsLoaded) {
				mSpinner.setVisibility(View.VISIBLE);
				mLoadInReceiver = true;
			} else if (gameSetList != null){
//				mFileNameArray[0].setText(gameSet.getFirstFilmName());
//				mFileNameArray[1].setText(gameSet.getSecondFilmName()); 
//				mFileNameArray[2].setText(gameSet.getThirdFilmName()); 
//				mFileNameArray[3].setText(gameSet.getFourthFilmName());
				
				mFileNameArray[0].setText(gameSetList.get(setListId).getFirstFilmName());
				mFileNameArray[1].setText(gameSetList.get(setListId).getSecondFilmName()); 
				mFileNameArray[2].setText(gameSetList.get(setListId).getThirdFilmName()); 
				mFileNameArray[3].setText(gameSetList.get(setListId).getFourthFilmName());
			}
		}
	}
	
//	@Override
//	protected void onPause(){
//		
//		super.onPause();
//		
//		//Log.d("RoundActivity", "onPause()");
//		
//		//loadGameSet(setListId++);
//	}
	
	private void loadGameSet() {
		
		Intent exchangeIntent = new Intent(RoundActivity.this, ExchangeService.class);
		
		exchangeIntent.putExtra(ExchangeService.PARAM_IN_MSG, ExchangeService.GET_GAME_SET_CALL);
//		exchangeIntent.putExtra(ExchangeService.PARAM_ID, id);

		mFilterGetGameSet = new IntentFilter(ExchangeService.GET_GAME_SET_CALL);
		
		mFilterGetGameSet.addCategory(Intent.CATEGORY_DEFAULT);
		
		mGameSetReceiver = new GameSetReceiver();
		
		registerReceiver(mGameSetReceiver, mFilterGetGameSet);
		
		startService(exchangeIntent);
		
		mGameSetIsLoaded = false;
	}

}

package ui;

import java.util.Random;

import client.GameSet;

import com.mutiboclient.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class RoundActivity extends Activity{
	
	public final static int MAX_ROUND_COUNT = 5;
	public final static int MAX_FAILS_COUNT = 3;
		
	private CheckBox[] mCheckBoxArray = new CheckBox[4];
	
	private TextView[] mFileNameArray = new TextView[4];
	
	private ImageButton mFiftyFiftyButton;
	private ImageButton mPassButton;
	private ImageButton mGoCheckButton;
	
	private Integer mFailsCount = 0;
	private Integer mRoundCount = 0;
	
	private Integer mCurrentScore= 0;
	
	private Boolean mIsChecked = false;
	private Integer mUserChoiseId = -1;
	private Boolean mIsFiftyFiftyUsed = false;
	
	private Random mRandom = new Random();
	
	private GameSet gameSet;
		
	@Override
	protected void onCreate(Bundle savedInstanseState){
		
		super.onCreate(savedInstanseState);
		setContentView(R.layout.round_activity);
				
		mCheckBoxArray[0] = (CheckBox) findViewById(R.id.CheckBoxFilm1);
		mCheckBoxArray[1] = (CheckBox) findViewById(R.id.CheckBoxFilm2);
		mCheckBoxArray[2] = (CheckBox) findViewById(R.id.CheckBoxFilm3);
		mCheckBoxArray[3] = (CheckBox) findViewById(R.id.CheckBoxFilm4);
		
		mFileNameArray[0] = (TextView) findViewById(R.id.FilmName1);
		mFileNameArray[1] = (TextView) findViewById(R.id.FilmName2);
		mFileNameArray[2] = (TextView) findViewById(R.id.FilmName3);
		mFileNameArray[3] = (TextView) findViewById(R.id.FilmName4);
		
		mFiftyFiftyButton = (ImageButton)findViewById(R.id.quitButton); // change name if possible
		mPassButton= (ImageButton)findViewById(R.id.playButton); // change name if possible
		mGoCheckButton = (ImageButton)findViewById(R.id.settingsButton); // change name if possible
		
		gameSet = new GameSet("Titanic",
				  			  "Gladiator", 
				  			  "American Beauty",
				  			  "King's Speech",
				  			  (long) 1,
				  			  "By Oscar Win: All the rest, won best movie and best Actor ",
				  			  (long)0);
		
		mFileNameArray[0].setText(gameSet.getFirstFilmName());
		mFileNameArray[1].setText(gameSet.getSecondFilmName()); 
		mFileNameArray[2].setText(gameSet.getThirdFilmName()); 
		mFileNameArray[3].setText(gameSet.getFourthFilmName()); 
		
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
					} while (gameSet.getOddId() == id_1+1 || gameSet.getOddId() == id_2+1 || id_1 == id_2);				
					
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
					
					if (mUserChoiseId == gameSet.getOddId()) {
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
					intent.putExtra(IntermediateResultActivity.PARAM_CORRECT_FILM_NAME, mFileNameArray[(int) gameSet.getOddId() - 1].getText().toString());
					intent.putExtra(IntermediateResultActivity.PARAM_EXPLANATION, gameSet.getExplanation());
					intent.putExtra(IntermediateResultActivity.PARAM_CURR_FAILS, mFailsCount.toString());
					intent.putExtra(IntermediateResultActivity.PARAM_SCORE, mCurrentScore.toString());
					
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

				mFailsCount++;
				
				Intent intent = new Intent(RoundActivity.this, IntermediateResultActivity.class);
					
				intent.putExtra(IntermediateResultActivity.PARAM_CORRECTNESS, IntermediateResultActivity.VALUE_FALSE);
				
//				intent.putExtra(IntermediateResultActivity.PARAM_USER_FILM, mFileNameArray[mUserChoiseId].getText().toString());
				intent.putExtra(IntermediateResultActivity.PARAM_CORRECT_FILM_NAME, mFileNameArray[(int) gameSet.getOddId() - 1].getText().toString());
				intent.putExtra(IntermediateResultActivity.PARAM_EXPLANATION, gameSet.getExplanation());
				intent.putExtra(IntermediateResultActivity.PARAM_CURR_FAILS, mFailsCount.toString());
				intent.putExtra(IntermediateResultActivity.PARAM_SCORE, mCurrentScore.toString());
				
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

}

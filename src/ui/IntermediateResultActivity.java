package ui;

import com.mutiboclient.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class IntermediateResultActivity extends Activity{
	
	public final static String PARAM_CORRECTNESS = "paramCorrectness";
	public final static String PARAM_CORRECT_FILM_NAME = "paramCorrectName";
	public final static String PARAM_USER_FILM = "paramUserFilmName";
	public final static String PARAM_EXPLANATION = "paramExplanation";
	public final static String PARAM_CURR_FAILS = "paramCurrFails";
	public final static String PARAM_SCORE = "paramScore";
	public final static String PARAM_PASS = "paramPass";
	public final static String PARAM_FINISH = "paramFinish";
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
	
	private String mFinishStatus;
	private Boolean mFinished = false;
	
	private Long mScoreLong;
	private Long mFailsLong;
	
	
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
		
		Intent intent = getIntent();
		
		String status = intent.getStringExtra(PARAM_CORRECTNESS);
		
		if (status.equals(VALUE_TRUE)) {			
			mResult.setText("Correct.");
			mResult.setTextColor(Color.GREEN);	
			mUserChoiseFilmNAme.setVisibility(View.INVISIBLE);
			mUserChoiseText.setVisibility(View.INVISIBLE);
		} else {		
			mResult.setText("Incorrect.");
			mResult.setTextColor(Color.RED);
		}
		
		mScoreLong = intent.getLongExtra(PARAM_SCORE, 0L);
		mFailsLong = intent.getLongExtra(PARAM_CURR_FAILS, 0L);
		
		mCorrectFilmName.setText(intent.getStringExtra(PARAM_CORRECT_FILM_NAME));
		mUserChoiseFilmNAme.setText(intent.getStringExtra(PARAM_USER_FILM));
		mExplanation.setText(intent.getStringExtra(PARAM_EXPLANATION));
		mFailsNum.setText(mFailsLong.toString());
		mScore.setText(mScoreLong.toString());		
		
		mFinishStatus = intent.getStringExtra(PARAM_FINISH);
						
		mLikeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (mFinishStatus.equals(VALUE_TRUE)) {
					Log.d("IntermediateResultActivity", "Finish");
					
					mFinished = true;
					
					Intent result = new Intent(IntermediateResultActivity.this, ResultActivity.class);
					
					result.putExtra(ResultActivity.EXTRA_POINS_LONG, mScoreLong);
					
					startActivity(result);
					
				} else {
					Log.d("IntermediateResultActivity", "Next");
					
					finish();
				}
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

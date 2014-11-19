package client;

import java.util.Collection;

import retrofit.RetrofitError;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

public class ExchangeService extends IntentService {
	
	public ExchangeService() {
		super("ExchangeService");
		setIntentRedelivery(false);
		Log.d("ExchangeService", "create empty");
	}
	
	public ExchangeService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		setIntentRedelivery(false);
		Log.d("ExchangeService", "create");
	}
	
	public final static String PARAM_IN_MSG = "imsg";
	
	public final static String PARAM_OUT_MSG = "omsg";
	
	public final static String PARAM_ID = "paramId";
	
	public final static String EXTRA_IN_REST = "restApi";
	
	public final static String EXTRA_LONG = "extraLong";
	
	public final static String EXTRA_GAME_SET = "extraGameSet";
	
	public final static String EXTRA_GAME_SET_COLLECTION = "extraGameSetCollection";
	
	public final static String EXTRA_BOOLEAN = "extraBoolean";
	
	public final static String EXTRA_STATUS_RESPONSE_ERROR = "extraStatusResponse";
	
	public final static String GET_GAME_SET_LIST_CALL = "getGameSetList";	
	
	public final static String GET_REPO_CAPACITY_CALL = "getRepoCapacity";
	
	public final static String GET_GAME_SET_CALL = "getGameSet";
	
	public final static String LIKE_GAME_SET_CALL = "likeGameSet";
	
	public final static String UNLIKE_GAME_SET_CALL = "unlikeGameSet";
	
	public final static String GET_USER_POINTS_CALL = "getUserPoints";
	
	public final static String ADD_GAME_SET_CALL = "addGameSet";
	
	@Override
	protected void onHandleIntent(Intent intent) {
		
		Log.d("ExchangeService", "onHandleIntent");
		
		Bundle extras = intent.getExtras();
		
		String msg = extras.getString(PARAM_IN_MSG);
		
		SrvAPI restUser = SrvClient.get();
		
		Intent broadcastIntent = new Intent();
		
		broadcastIntent.setAction(msg);
		
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		
		try {
		
		if (msg.equals(GET_GAME_SET_LIST_CALL)) {		
			
			System.out.println(GET_GAME_SET_LIST_CALL);
			Collection<GameSet> repoCollection = restUser.getGameSetList();
			broadcastIntent.putExtra(EXTRA_GAME_SET_COLLECTION, (Parcelable)repoCollection);		
			
		} else if (msg.equals(GET_REPO_CAPACITY_CALL)) {
			
			System.out.println(GET_REPO_CAPACITY_CALL);
			Long repoCapacity = restUser.getRepoCapacity();
			broadcastIntent.putExtra(EXTRA_LONG, repoCapacity);
			
		} else if (msg.equals(GET_GAME_SET_CALL)) {
			
			System.out.println(GET_GAME_SET_CALL);
			Long id = extras.getLong(PARAM_ID);			
			GameSet set = restUser.getGameSet(id);
			broadcastIntent.putExtra(EXTRA_GAME_SET, (Parcelable)set);
			
		} else if (msg.equals(LIKE_GAME_SET_CALL)) {
			
			System.out.println(LIKE_GAME_SET_CALL);
			Long id = extras.getLong(PARAM_ID);		
			GameSet set = restUser.likeGameSet(id);
			broadcastIntent.putExtra(EXTRA_GAME_SET, (Parcelable)set);			
			
		} else if (msg.equals(UNLIKE_GAME_SET_CALL)) {
			
			System.out.println(UNLIKE_GAME_SET_CALL);
			Long id = extras.getLong(PARAM_ID);		
			GameSet set = restUser.unlikeGameSet(id);
			broadcastIntent.putExtra(EXTRA_GAME_SET, (Parcelable)set);
			
		} else if (msg.equals(GET_USER_POINTS_CALL)) {
			
			Log.d("ExchangeService", GET_USER_POINTS_CALL);
			Long points = restUser.getUserPoints();
			broadcastIntent.putExtra(EXTRA_LONG, points);
			
		} else if (msg.equals(ADD_GAME_SET_CALL)) {
			System.out.println(ADD_GAME_SET_CALL);
			System.out.println("Unimplemented for now");
		}
		
		} catch (RetrofitError e) {
			
			String responseStatus = "error";
			
			broadcastIntent.putExtra(EXTRA_STATUS_RESPONSE_ERROR, responseStatus);
		}		
		
		sendBroadcast(broadcastIntent);
	}

}

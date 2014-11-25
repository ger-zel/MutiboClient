package client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit.RetrofitError;
import ui.RoundActivity;
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
	
	public final static String EXTRA_LEADERS_MAP = "extraLeadersMap";
	
	public final static String GET_GAME_SET_LIST_CALL = "getGameSetList";	
	
	public final static String GET_REPO_CAPACITY_CALL = "getRepoCapacity";
	
	public final static String GET_GAME_SET_CALL = "getGameSet";
	
	public final static String LIKE_GAME_SET_CALL = "likeGameSet";
	
	public final static String UNLIKE_GAME_SET_CALL = "unlikeGameSet";
	
	public final static String GET_USER_POINTS_CALL = "getUserPoints";
	
	public final static String SET_USER_POINTS_CALL = "setUserPoints";
	
	public final static String ADD_GAME_SET_CALL = "addGameSet";
	
	public final static String GET_LEADERS_CALL = "getLeaders";
	
	private static Long mId = 0L;
	
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
				
				Log.d("ExchangeService", GET_GAME_SET_CALL);
				
				List<GameSet> gameSetList = loadGameSetList(1L, RoundActivity.MAX_ROUND_COUNT, restUser);
				
				broadcastIntent.putExtra(EXTRA_GAME_SET, (Serializable)gameSetList);
				
//				GameSet set = null;
//
//				Long repoCapacity = restUser.getRepoCapacity();
//				
//				Log.d("ExchangeService", GET_GAME_SET_CALL);
//				if (mId == 0L) {
//					if (repoCapacity != 0) {
//						mId = 1L;
//					}
//				}
//				Log.d("ExchangeService", "id = " + mId);
//				
//				Boolean weGetIt = false;
//				
//				while(!weGetIt) {				
//					try {
//						Log.d("ExchangeService", "try id = " + mId);
//						set = restUser.getGameSet(mId);	
//						weGetIt = true;
//						if (mId < repoCapacity)
//							mId++;
//						else 
//							mId = 1L;
//						break;
//						
//					} catch (RetrofitError e) {
//						if (404 == e.getResponse().getStatus()) {
//							weGetIt = false;
//							if (mId < repoCapacity) {
//								mId++;
//								continue;
//							} else {
//								mId = 1L;
//								break;
//							}
//						}
//					}
//				}
//				
//				broadcastIntent.putExtra(EXTRA_GAME_SET, set);
				
			} else if (msg.equals(LIKE_GAME_SET_CALL)) {
				
				System.out.println(LIKE_GAME_SET_CALL);
				Long id = extras.getLong(PARAM_ID);		
				GameSet set = restUser.likeGameSet(id);
				broadcastIntent.putExtra(EXTRA_GAME_SET, set);			
				
			} else if (msg.equals(UNLIKE_GAME_SET_CALL)) {
				
				System.out.println(UNLIKE_GAME_SET_CALL);
				Long id = extras.getLong(PARAM_ID);		
				GameSet set = restUser.unlikeGameSet(id);
				broadcastIntent.putExtra(EXTRA_GAME_SET, set);
				
			} else if (msg.equals(GET_USER_POINTS_CALL)) {
				
				Log.d("ExchangeService", GET_USER_POINTS_CALL);
				Long points = restUser.getUserPoints();
				broadcastIntent.putExtra(EXTRA_LONG, points);
				
			} else if (msg.equals(SET_USER_POINTS_CALL)) {
				Log.d("ExchangeService", SET_USER_POINTS_CALL);
				Long points = extras.getLong(EXTRA_LONG);
				points = restUser.updateUserPoints(points);
				broadcastIntent.putExtra(EXTRA_LONG, points);
			} else if (msg.equals(GET_LEADERS_CALL)) {
				Log.d("ExchangeService", GET_LEADERS_CALL);
				Map users = restUser.getLeadersList();
				broadcastIntent.putExtra(EXTRA_LEADERS_MAP, (Serializable)users);
			}		
		
		} catch (RetrofitError e) {
			
			String responseStatus = "error";
			
			broadcastIntent.putExtra(EXTRA_STATUS_RESPONSE_ERROR, responseStatus);
		}		
		
		sendBroadcast(broadcastIntent);
	}
	
	private List<GameSet> loadGameSetList(Long startId, Integer num, SrvAPI rest) {
	
		class GameSetCallable implements Callable<GameSet> {
			
			private Long id;
			private SrvAPI rest;
			
			GameSetCallable(Long id, SrvAPI rest) {
				this.id = id;
				this.rest = rest;
			}

			@Override
			public GameSet call() throws Exception {
				
				GameSet set = null;
				
				try {
					set = rest.getGameSet(id);	
					
				} catch (RetrofitError e) {
					if (404 == e.getResponse().getStatus()) {
						set = null;
					}
				}
				
				return set;
			}					
		}
		
		List<GameSet> setList = new ArrayList<GameSet>();
		List<Future<GameSet>> futuresList = new ArrayList<Future<GameSet>> ();
		
		ExecutorService executor = Executors.newFixedThreadPool(num);
		
		for (Long i = 0L; i <= num.longValue(); i++) {
			
			Future<GameSet> future = executor.submit(new GameSetCallable(i + startId, rest));
			futuresList.add(future);
		}
		
		for (Future<GameSet> fut : futuresList) {
			
			GameSet set = null;
			try {
				set = fut.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (set != null) {
				setList.add(set);
			}
		}
		
		return setList;
	}

}

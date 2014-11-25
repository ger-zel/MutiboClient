package client;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import auth.SecuredRestBuilder;

public class SrvClient {

	public final static String CLIENT_ID = "mobile";
	
//	public final static String SERVER = "https://localhost:8443";
	public final static String SERVER = "https://10.0.2.2:8443";
	
	private static SrvAPI mSrvClient;
		
	public static synchronized SrvAPI get() {
		
		return mSrvClient;
	}
	
	public static synchronized SrvAPI create(String username, String password) {
	
//		Log.d("SrvClient", "user: " + username + " pass: " + password);
						
		mSrvClient = new SecuredRestBuilder()
		.setLoginEndpoint(SERVER + SrvAPI.TOKEN_PATH)
		.setUsername(username)
		.setPassword(password)
		.setClientId(CLIENT_ID)
		.setClient(new ApacheClient(UnsafeHttpsClient.getNewHttpClient()))
		.setEndpoint(SERVER).setLogLevel(LogLevel.BASIC).build()
		.create(SrvAPI.class);	
		
		return mSrvClient;
	}
}

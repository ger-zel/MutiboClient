package client;

import java.util.Collection;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface SrvAPI {

	public static final String TOKEN_PATH = "/oauth/token";

	public static final String GAME_SET_SVC_PATH = "/gameSet";

	public static final String GAME_REPOSITORY_CAPACITY = "/repoCapacity";

	public static final String LIKE_GAME_SET = "/likeSet/{id}";

	public static final String UNLIKE_GAME_SET = "/unlikeSet/{id}";

	public static final String USER_POINTS = "/userPoints";
	
	public static final String USER_CHECK = "/user";

	public static final String GET_GAME_SET_BY_ID = GAME_SET_SVC_PATH + "/{id}";

	public static final String ID_PARAMETER = "id";
	

	@GET(GAME_SET_SVC_PATH)
	public Collection<GameSet> getGameSetList();
	
	@GET(GAME_REPOSITORY_CAPACITY)
	public Long getRepoCapacity();

	@GET(GET_GAME_SET_BY_ID)
	public GameSet getGameSet(@Path(ID_PARAMETER) long id);

	@POST(LIKE_GAME_SET)
	public GameSet likeGameSet(@Path(ID_PARAMETER) long id);

	@POST(UNLIKE_GAME_SET)
	public GameSet unlikeGameSet(@Path(ID_PARAMETER) long id);
	
	@POST(USER_POINTS)
	public Long updateUserPoints(@Body Long pointsDiff);

	@GET(USER_POINTS)
	public Long getUserPoints();

	@POST(GAME_SET_SVC_PATH)
	public GameSet addGameSet(@Body GameSet gameSet);
}
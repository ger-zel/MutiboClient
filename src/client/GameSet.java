package client;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GameSet{
	
	private long id;
		
	private String firstFilmName;
	private String secondFilmName;
	private String thirdFilmName;
	private String fourthFilmName;
	
	private long oddId;
	private String explanation;	

	private long rating;
	
	public final Long MIN_RATING = (long) 10;

	private Set<String> usersWhoRatedGameSet;

	GameSet(){}
	
	public GameSet(String firstFilmName, 
				   String secondFilmName,
				   String thirdFilmName,
				   String fourthFilmName,
				   long oddId,
				   String explanation,
				   long rating) {
		super();
		this.firstFilmName = firstFilmName;
		this.secondFilmName = secondFilmName;
		this.thirdFilmName = thirdFilmName;
		this.fourthFilmName = fourthFilmName;
		this.oddId = oddId;
		this.explanation = explanation;
		this.rating = rating;
	}
	public void setFirstFilmName(String firstFilmName) {
		this.firstFilmName = firstFilmName;
	}
	public String getFirstFilmName() {
		return this.firstFilmName;
	}	
	public void setRating(long rating) {
		this.rating = rating;
	}
	public long getRating() {
		return rating;
	}

	public void addUserWhoRatedGameSet(String name){
		if (usersWhoRatedGameSet == null) {
			usersWhoRatedGameSet = new HashSet<String>();
		}
		usersWhoRatedGameSet.add(name);
	}	
	public void removeUserWhoRatedGameSet(String name){
		if (usersWhoRatedGameSet != null)
			usersWhoRatedGameSet.remove(name);
	}	
	public Boolean userRatedGameSetAllready(String name){
		if (usersWhoRatedGameSet != null)
			return usersWhoRatedGameSet.contains(name);
		else 
			return false;
	}
	public void setUsersWhoRatedGameSet(Set<String> usersWhoRatedGameSet) {
		this.usersWhoRatedGameSet = usersWhoRatedGameSet;
	}
	public Collection<String> getUsersWhoRatedGameSet(){
		return usersWhoRatedGameSet;
	}

	public String getSecondFilmName() {
		return secondFilmName;
	}

	public void setSecondFilmName(String secondFilmName) {
		this.secondFilmName = secondFilmName;
	}
	public String getThirdFilmName() {
		return thirdFilmName;
	}

	public void setThirdFilmName(String thirdFilmName) {
		this.thirdFilmName = thirdFilmName;
	}

	public String getFourthFilmName() {
		return fourthFilmName;
	}

	public void setFourthFilmName(String fourthFilmName) {
		this.fourthFilmName = fourthFilmName;
	}

	public long getOddId() {
		return oddId;
	}

	public void setOddId(long oddId) {
		this.oddId = oddId;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
}

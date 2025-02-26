package classes;

import java.util.ArrayList;

public class User {
	
	String username;
	String code;
	ArrayList<Quizz> answeredQuizzes;
	
	public User(String u, String c){
		username=u;
		code=c;
		answeredQuizzes=new ArrayList<Quizz>();
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCode() {
		return code;
	}
	public void addAnsweredQuizz(Quizz q){
		answeredQuizzes.add(q);
	}
	public void setCode(String code) {
		this.code = code;
	}
	public ArrayList<Quizz> getAnsweredQuizzes(){
		return answeredQuizzes;
	}
	public ArrayList<String> getAnsweredLocations(){
		ArrayList<String> locations = new ArrayList<String>();
		for(Quizz q: answeredQuizzes)
			locations.add(q.getLocation());
		return locations;
	}
}

package classes;

public class User {
	
	String username;
	String code;
	
	public User(String u, String c){
		username=u;
		code=c;
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
	public void setCode(String code) {
		this.code = code;
	}
}

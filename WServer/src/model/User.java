package model;

public class User {
	private String nickName;
	
	public User(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String toString() {
		return this.nickName;
	}
}

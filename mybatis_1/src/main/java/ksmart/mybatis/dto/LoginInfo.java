package ksmart.mybatis.dto;

public class LoginInfo {
	
	private String loginId;
	private String loginName;
	private int loginLevel;
	
	public LoginInfo() {}
	
	public LoginInfo(String loginId, String loginName, int loginLevel) {
		this.loginId = loginId;
		this.loginName = loginName;
		this.loginLevel = loginLevel;
	}
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public int getLoginLevel() {
		return loginLevel;
	}
	public void setLoginLevel(int loginLevel) {
		this.loginLevel = loginLevel;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("loginInfo [loginId=");
		builder.append(loginId);
		builder.append(", loginName=");
		builder.append(loginName);
		builder.append(", loginLevel=");
		builder.append(loginLevel);
		builder.append("]");
		return builder.toString();
	}
	
}

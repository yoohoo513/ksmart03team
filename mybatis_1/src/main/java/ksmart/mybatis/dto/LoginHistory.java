package ksmart.mybatis.dto;

public class LoginHistory {
	private int loginNum;
	private String loginId;
	private String loginDate;
	private String logoutDate;
	
	private Member member;

	public int getLoginNum() {
		return loginNum;
	}

	public void setLoginNum(int loginNum) {
		this.loginNum = loginNum;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}

	public String getLogoutDate() {
		return logoutDate;
	}

	public void setLogoutDate(String logoutDate) {
		this.logoutDate = logoutDate;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoginHistory [loginNum=");
		builder.append(loginNum);
		builder.append(", loginId=");
		builder.append(loginId);
		builder.append(", loginDate=");
		builder.append(loginDate);
		builder.append(", logoutDate=");
		builder.append(logoutDate);
		builder.append(", member=");
		builder.append(member);
		builder.append("]");
		return builder.toString();
	}
	
}

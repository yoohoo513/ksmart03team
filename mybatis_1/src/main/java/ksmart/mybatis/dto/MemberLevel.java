package ksmart.mybatis.dto;

public class MemberLevel {
	
	private int LevelNum;
	private String LevelName;
	
	public int getLevelNum() {
		return LevelNum;
	}
	public void setLevelNum(int levelNum) {
		LevelNum = levelNum;
	}
	public String getLevelName() {
		return LevelName;
	}
	public void setLevelName(String levelName) {
		LevelName = levelName;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MemberLevel [LevelNum=");
		builder.append(LevelNum);
		builder.append(", LevelName=");
		builder.append(LevelName);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}

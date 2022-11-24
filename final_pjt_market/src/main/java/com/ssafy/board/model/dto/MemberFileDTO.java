package com.ssafy.board.model.dto;

public class MemberFileDTO {
	private int fno;
	private String username;
	private String originalName;
	private String savedPath;
	private String absolPath;
	public MemberFileDTO() {}
	public MemberFileDTO(String userid, String originalName, String savedPath,String absolPath) {
		super();
		this.username = userid;
		this.originalName = originalName;
		this.savedPath = savedPath;
		this.absolPath = absolPath;
	}
	public int getFno() {
		return fno;
	}
	public void setFno(int fno) {
		this.fno = fno;
	}
	public String getUserid() {
		return username;
	}
	public void setUserid(String userid) {
		this.username = userid;
	}
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	public String getSavedPath() {
		return savedPath;
	}
	public void setSavedPath(String savedPath) {
		this.savedPath = savedPath;
	}
	
	public String getAbsolPath() {
		return absolPath;
	}
	public void setAbsolPath(String absolPath) {
		this.absolPath = absolPath;
	}
	@Override
	public String toString() {
		return "MemberFileDTO [fno=" + fno + ", userid=" + username + ", originalName=" + originalName + ", savedPath="
				+ savedPath + "]";
	}
	
}

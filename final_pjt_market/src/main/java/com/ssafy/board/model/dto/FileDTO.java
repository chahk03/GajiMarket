package com.ssafy.board.model.dto;

public class FileDTO {
	private int fno;
	private int bno;
	private String originalName;
	private String savedPath;

	public FileDTO(int bno, String originalName, String savedPath) {
		this.bno = bno;
		this.originalName = originalName;
		this.savedPath = savedPath;
	}

	public FileDTO() {
	}

	public int getFno() {
		return fno;
	}

	public void setFno(int fno) {
		this.fno = fno;
	}

	public int getBno() {
		return bno;
	}

	public void setBno(int bno) {
		this.bno = bno;
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

	@Override
	public String toString() {
		return "FileDTO [fno=" + fno + ", bno=" + bno + ", originalName=" + originalName + ", savedPath=" + savedPath + "]";
	}
}

package com.ssafy.board.model.dao;

import java.util.List;

import com.ssafy.board.model.dto.FileDTO;
import com.ssafy.board.model.dto.MemberFileDTO;

public interface FileMapper {
	public int insert(FileDTO file);
	public int update(FileDTO file);
	public List<FileDTO> selectFiles(int bno);
	public FileDTO selectOne(int fno);
	public MemberFileDTO selectUserImg(String username);
	public int mfileinsert(MemberFileDTO file);
	public int mfileUpdate(MemberFileDTO file);
}

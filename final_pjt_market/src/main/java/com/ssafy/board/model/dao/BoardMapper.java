package com.ssafy.board.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.board.model.dto.BoardDTO;
import com.ssafy.board.model.dto.BoardParameterDto;

@Repository
public interface BoardMapper {
	public int insert(BoardDTO board);
	public List<BoardDTO> selectList(@Param("start")int startRow, @Param("cnt")int count);
	public List<BoardDTO> selectConditionList(BoardParameterDto boardParameterDto);
	public int selectTotalCount();
	public int selectConditionCount(BoardParameterDto boardParameterDto);
    public BoardDTO selectOne(int bno);
    public int updateReadCount(int bno);
    public int selectRecommendCount(String userid, int bno);
    public int selectTotalRecommendCount(int bno);
    public int updateRecommendPlus(int bno);
    public int updateRecommendMinus(int bno);
    public int insertRecommend(String userid, int bno);
    public int deleteRecommend(String userid, int bno);
    public int update(BoardDTO board);
    public int delete(int bno);
    public List<BoardDTO> getAdminList();
}

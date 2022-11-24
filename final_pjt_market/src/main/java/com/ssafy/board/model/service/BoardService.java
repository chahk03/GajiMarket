package com.ssafy.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.board.model.dao.BoardMapper;
import com.ssafy.board.model.dao.CommentMapper;
import com.ssafy.board.model.dao.FileMapper;
import com.ssafy.board.model.dto.BoardDTO;
import com.ssafy.board.model.dto.BoardParameterDto;
import com.ssafy.board.model.dto.CommentDTO;
import com.ssafy.board.model.dto.FileDTO;
import com.ssafy.board.model.dto.MemberDTO;
import com.ssafy.board.model.dto.MemberFileDTO;

@Service
public class BoardService {
	private static final int COUNT_PER_PAGE = 10;
	
	@Autowired
	private BoardMapper dao;
	
	@Autowired
	private CommentMapper cdao;
	
	@Autowired
	private FileMapper fdao;
	
	public Map<String, Object> makePage(int page) {
		int totalCount = dao.selectTotalCount(); // 총 게시글의 개수
		
		int totalPage = totalCount / COUNT_PER_PAGE; // 53/10 = 5
		if(totalCount % COUNT_PER_PAGE > 0) totalPage++; // 53%10 = 3이면 페이지 하나 더 필요함

		int startPage = (page - 1) / 10 * 10 + 1; // (/10*10) 하면 1의 자리가 떨어져 나가서 11,12,13,... -> 10
		int endPage = startPage + 9; // 시작이 11이면 화면 하단 끝 페이지는 20으로 맞춰놓음

		if(endPage > totalPage) endPage = totalPage;
		
		int startRow = (page - 1) * COUNT_PER_PAGE; // (1-1)*10=0, (2-1)*1=10, (3-1)*10=20
		List<BoardDTO> boardList = dao.selectList(startRow, COUNT_PER_PAGE);
		/////////// 페이지에 보여줄 모든 데이터 확보 완료 ////////////
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("startPage", startPage);
		result.put("endPage", endPage);
		result.put("currPage", page);
		result.put("totalPage", totalPage);
		result.put("boardList", boardList);
		
		return result;
	}
	
	public Map<String, Object> makePage(BoardParameterDto boardParameterDto) {
		System.out.println("makePage : " + boardParameterDto);
		int totalCount = dao.selectConditionCount(boardParameterDto); // 총 게시글의 개수
		System.out.println("totalCount " + totalCount);
		
		int totalPage = totalCount / COUNT_PER_PAGE; // 53/10 = 5
		if(totalCount % COUNT_PER_PAGE > 0) totalPage++; // 53%10 = 3이면 페이지 하나 더 필요함

		int startPage = (boardParameterDto.getPg() - 1) / 10 * 10 + 1; // (/10*10) 하면 1의 자리가 떨어져 나가서 11,12,13,... -> 10
		int endPage = startPage + 9; // 시작이 11이면 화면 하단 끝 페이지는 20으로 맞춰놓음

		if(endPage > totalPage) endPage = totalPage;
		
		int startRow = (boardParameterDto.getPg() - 1) * COUNT_PER_PAGE; // (1-1)*10=0, (2-1)*1=10, (3-1)*10=20
		boardParameterDto.setStart(startRow);
		System.out.println("SORT : " + boardParameterDto.getSort()+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		List<BoardDTO> boardList = dao.selectConditionList(boardParameterDto);
		/////////// 페이지에 보여줄 모든 데이터 확보 완료 ////////////
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("startPage", startPage);
		result.put("endPage", endPage);
		result.put("currPage", boardParameterDto.getPg());
		result.put("totalPage", totalPage);
		result.put("boardList", boardList);
		System.out.println(result);
		
		return result;
	} 
	
	public boolean write(BoardDTO board) {
		if(board == null || board.getTitle() == null || board.getTitle().length() == 0 || 
				board.getWriter() == null || board.getWriter().length() == 0)
			return false;
			
		if(dao.insert(board) == 1)
			return true;
		return false;
	}

//	public BoardDTO read(int bno, MemberDTO loginInfo) {
//		BoardDTO board = dao.selectOne(bno);
//		
//		// 현재 로그인 정보와 글 작성자가 다를 때에만 조회수 증가시키기
//		if(!board.getWriter().equals(loginInfo.getId()))
//				dao.updateReadCount(bno); // 로그인 하면 내가 쓴 글 읽을 때는 조회수 증가 안함
//		
//		return dao.selectOne(bno);
//	}
	
	public BoardDTO read(int bno) {
//		BoardDTO board = dao.selectOne(bno);
		
		// 현재 로그인 정보와 글 작성자가 다를 때에만 조회수 증가시키기
//		if(!board.getWriter().equals(loginInfo.getId()))
				dao.updateReadCount(bno); // 로그인 하면 내가 쓴 글 읽을 때는 조회수 증가 안함
		
		return dao.selectOne(bno);
	}
	
	public BoardDTO update(int bno) {
		BoardDTO board = dao.selectOne(bno);
		
		return board;
	}
	
	public Map<String, Object> update(BoardDTO board) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(board == null || board.getTitle() == null || board.getTitle().length() == 0 || 
				board.getWriter() == null || board.getWriter().length() == 0) {
			result.put("msg", "fail");
		}
		
		if(dao.update(board) == 1) {
			result.put("msg", "success");
			result.put("board", dao.selectOne(board.getBno()));
		}
		
		return result;
	}
	
	public boolean delete(int bno) {
		cdao.delete(bno);
		if(dao.delete(bno) == 1)
			return true;
		
		return false;
	}
	
	// 추천수
	public boolean selectRecommendCount(String userid, int bno) {
		if(dao.selectRecommendCount(userid, bno) == 1) return true;
		else return false;
	}
	
	public int updateRecommend(String userid, int bno) {
		if(dao.selectRecommendCount(userid, bno) == 0) {
			if(dao.insertRecommend(userid, bno) == 1 && dao.updateRecommendPlus(bno) == 1) {
				return dao.selectTotalRecommendCount(bno);
			}
		} else {
			if(dao.deleteRecommend(userid, bno) == 1 && dao.updateRecommendMinus(bno) == 1) {
				return dao.selectTotalRecommendCount(bno);
			}
		}
		
		return dao.selectTotalRecommendCount(bno);
	}
	
	///////////////////////////////////////////
//	public int writeComment(CommentDTO comment) {
//		return cdao.insert(comment);
//	}
	
	public boolean writeComment(CommentDTO comment) {
		if(comment.getCwriter().length() == 0 || comment.getCcontent().length() == 0)
			return false;
		
		if(cdao.insert(comment) == 1)
			return true;
		return false;
	}
	
	public Map<String, Object> getComments(int bno) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("cList", cdao.selectList(bno));
		List<MemberFileDTO> flist = new ArrayList<MemberFileDTO>();
		for(CommentDTO comment : cdao.selectList(bno)) {
			flist.add(fdao.selectUserImg(comment.getCwriter()));
		}
		result.put("cListImg", flist);
		result.put("msg", "success");
		return result;
	}
	
	public boolean modifyComment(CommentDTO comment) {
		if(comment.getCwriter().length() == 0 || comment.getCcontent().length() == 0)
			return false;
		
		if(cdao.update(comment) == 1)
			return true;
		return false;
	}
	
	public boolean deleteCemment(int cno) {
		if(cdao.deleteComment(cno) == 1)
			return true;
		return false;
	}
	
	// 파일 업로드 관련, 파일 복잡해지면 별도의 FileService로 분리시킬 수 있음
	public int addFile(FileDTO file) {
		return fdao.insert(file);
	}
	
	public List<FileDTO> getFiles(int bno) { // 글 읽기 화면에서 첨부된 파일 목록 보여줄 때
		return fdao.selectFiles(bno);
	}
	
	public FileDTO getFile(int fno) {
		return fdao.selectOne(fno);
	}
	
	public int modifyFile(FileDTO file) {
		return fdao.update(file);
	}
	
	public Map<String, Object> makeRead(int bno, String userid, String username) {
		System.out.println("글 가져오기 : " + bno + " " + userid + " " + username);
		Map<String, Object> result = new HashMap<String, Object>();
		if(!dao.selectOne(bno).getWriter().equals(username)) dao.updateReadCount(bno);
		result.put("board", dao.selectOne(bno));
		result.put("writerImg", fdao.selectUserImg(dao.selectOne(bno).getWriter()));
		if(dao.selectRecommendCount(userid, bno) == 1)
			result.put("isrecommend", true);
		else result.put("isrecommend", false);		
		result.put("cList", cdao.selectList(bno));
		List<MemberFileDTO> flist = new ArrayList<MemberFileDTO>();
		for(CommentDTO comment : cdao.selectList(bno)) {
			flist.add(fdao.selectUserImg(comment.getCwriter()));
		}
		result.put("cListImg", flist);
		result.put("file", fdao.selectFiles(bno));
		System.out.println(result);
		return result;
	}
	
	public Map<String, Object> getadminlist() {
        Map<String, Object> result = new HashMap<String, Object>();
        List<BoardDTO> adminList = dao.getAdminList();
        System.out.println(adminList);
        result.put("adminList", adminList);
        return result;
    }
}

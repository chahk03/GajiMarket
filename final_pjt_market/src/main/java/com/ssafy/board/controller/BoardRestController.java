package com.ssafy.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.board.model.dto.BoardDTO;
import com.ssafy.board.model.dto.BoardParameterDto;
import com.ssafy.board.model.dto.CommentDTO;
import com.ssafy.board.model.dto.FileDTO;
import com.ssafy.board.model.service.BoardService;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/board")
public class BoardRestController {
	
	@Autowired
	private BoardService bservice;
		
	@GetMapping
	public ResponseEntity<Map<String, Object>> list(@RequestParam(value = "page", defaultValue = "1") int page) {
		return new ResponseEntity<Map<String, Object>>(bservice.makePage(page), HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/list")
	public ResponseEntity<Map<String, Object>> list(@RequestBody BoardParameterDto boardParameterDto) {
		return new ResponseEntity<Map<String, Object>>(bservice.makePage(boardParameterDto), HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/adminlist")
    public ResponseEntity<Map<String, Object>> adminlist() {
        return new ResponseEntity<Map<String, Object>>(bservice.getadminlist(), HttpStatus.ACCEPTED);
    }
	
	@PostMapping
	public ResponseEntity<String> write(BoardDTO board, MultipartFile[] uploadFile) throws IllegalStateException, IOException {
//	public ResponseEntity<String> write(@RequestBody BoardDTO board) throws IllegalStateException, IOException {
		System.out.println("글 쓰기에 왔어요");
		// 일단 글이 DB에 저장되야 글번호를 파일 업로드에 넣을 수 있음
		System.out.println("before write: " + board);
		boolean writeResult = bservice.write(board);
		System.out.println("after write: " + board);
		
		// 글쓰기에 파일 첨부 기능 추가
		if(uploadFile != null && uploadFile.length > 0) {
			String uploadPath = "C:/SSAFY/workspace/board_final_pjt/board_final_pjt/src/assets/boardImg";
			File uploadDir = new File(uploadPath);
			
			if(!uploadDir.exists()) { // 업로드 파일 저장 폴더 없으면 생성
				uploadDir.mkdir();
			}
			
			for(MultipartFile file : uploadFile) { // 파일 개수만큼 반복
				String savedName = new Random().nextInt(1000000000) + "." + file.getOriginalFilename().split("\\.")[1];
				System.out.println(savedName);
				File savedFile = new File(uploadPath + "/" + savedName);
				file.transferTo(savedFile); // profile.png -> c:/SSAFY/upload/2145346434
				
				FileDTO dto = new FileDTO(board.getBno(), file.getOriginalFilename(), savedName);
				bservice.addFile(dto);
			}
		}
		
		if(writeResult) {
			return new ResponseEntity<String>("success", HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<String>("error", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/image")
	public ResponseEntity<Map<String, Object>> modify(int bno, MultipartFile[] uploadFile) throws IllegalStateException, IOException {
//	public ResponseEntity<String> write(@RequestBody BoardDTO board) throws IllegalStateException, IOException {
		System.out.println("글 이미지 수정에 왔어요");
		System.out.println("글 이미지 수정 글 번호 : " + bno);
		System.out.println("글 이미지 수정 이미지 : " + uploadFile);
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 글쓰기에 파일 첨부 기능 추가
		if(uploadFile != null && uploadFile.length > 0) {
			String uploadPath = "C:/SSAFY/workspace/board_final_pjt/board_final_pjt/src/assets/boardImg";
			File uploadDir = new File(uploadPath);
			
			if(!uploadDir.exists()) { // 업로드 파일 저장 폴더 없으면 생성
				uploadDir.mkdir();
			}
			
			for(MultipartFile file : uploadFile) { // 파일 개수만큼 반복
				String savedName = new Random().nextInt(1000000000) + "." + file.getOriginalFilename().split("\\.")[1];
				System.out.println(savedName);
				File savedFile = new File(uploadPath + "/" + savedName);
				file.transferTo(savedFile); // profile.png -> c:/SSAFY/upload/2145346434
				
				FileDTO dto = new FileDTO(bno, file.getOriginalFilename(), savedName);
				bservice.modifyFile(dto);
			}
		}
		
		result.put("file", bservice.getFiles(bno));
		System.out.println(result);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/read")
	public ResponseEntity<Map<String, Object>> read(int bno, String userid, String username) {
		System.out.println("게시물 읽기 : " + bno + " " + username);
		return new ResponseEntity<Map<String, Object>>(bservice.makeRead(bno, userid, username), HttpStatus.OK);
	}
	
	@GetMapping("/read/recommend")
	public ResponseEntity<Boolean> readRecommend(String userid, int bno) {
		return new ResponseEntity<Boolean>(bservice.selectRecommendCount(userid, bno), HttpStatus.OK);
	}
	
	@PutMapping("/modify")
	public ResponseEntity<Map<String, Object>> modify(@RequestBody BoardDTO board) {
		System.out.println(board);
		return new ResponseEntity<Map<String, Object>>(bservice.update(board), HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/{bno}")
	public ResponseEntity<String> delete(@PathVariable("bno") int bno) {
		System.out.println("오긴해?");
		if(bservice.delete(bno)) {
			return new ResponseEntity<String>("success", HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<String>("error", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/recommend")
	public ResponseEntity<Integer> recommend(String userid, int bno) {
		System.out.println("추천수 업데이트 : " + userid + " " + bno);
		return new ResponseEntity<Integer>(bservice.updateRecommend(userid, bno), HttpStatus.OK);
	}
	
	@PostMapping("/comment")
	public ResponseEntity<Map<String, Object>> comment(@RequestBody CommentDTO comment) {
		Map<String, Object> result = new HashMap<String, Object>();
		System.out.println(comment);
		if(bservice.writeComment(comment)) {
			return new ResponseEntity<Map<String, Object>>(bservice.getComments(comment.getBno()), HttpStatus.ACCEPTED);
		} else {
			result.put("msg", "error");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/comment/update")
	public ResponseEntity<Map<String, Object>> commentUpdate(@RequestBody CommentDTO comment) {
		Map<String, Object> result = new HashMap<String, Object>();
		System.out.println("업데이트 댓글 : " + comment);
		if(bservice.modifyComment(comment)) {
			System.out.println("리스트 다시 불러오기");
			return new ResponseEntity<Map<String, Object>>(bservice.getComments(comment.getBno()), HttpStatus.ACCEPTED);
		} else {
			result.put("msg", "error");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/comment/delete")
	public ResponseEntity<Map<String, Object>> deleteComment(int bno, int cno) {
		Map<String, Object> result = new HashMap<String, Object>();
		System.out.println("게시물 : " + bno + " 댓글 삭제 : " + cno);
		if(bservice.deleteCemment(cno)) {
			System.out.println(cno + " 댓글 삭제했음!");
			return new ResponseEntity<Map<String, Object>>(bservice.getComments(bno), HttpStatus.ACCEPTED);
		} else {
			result.put("msg", "error");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.BAD_REQUEST);
		}
	}
	
}

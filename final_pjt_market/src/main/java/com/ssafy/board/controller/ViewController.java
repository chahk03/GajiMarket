package com.ssafy.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view") // 비동기 요청 말고 화면 요청 받는 컨트롤러
public class ViewController {
	
	@GetMapping("/boardlist")
	public String boardlist() {
		return "List"; // WEB-INF/views/List.jsp
	}
	
	@GetMapping("/boardwrite")
	public String boardwrite() {
		return "Write"; // WEB-INF/views/Write.jsp
	}
	
	@GetMapping("/boardread")
	public String boardread() {
		return "Read"; // WEB-INF/views/Read.jsp
	}
	
}

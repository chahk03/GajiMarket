package com.ssafy.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ssafy.board.model.dao.ChatMapper;
import com.ssafy.board.model.dto.ChatMessageDTO;
import com.ssafy.board.model.dto.ChatRoomDTO;

@Repository
public class ChatRoomService {

	@Autowired
	private ChatMapper dao;

	public List<ChatRoomDTO> findAllRooms() {
		// 채팅방 생성 순서 최근 순으로 반환
		return dao.selectRoomList();
	}

	public ChatRoomDTO findRoomById(String id) {
		return dao.selectRoomOne(id);
	}

	public String createChatRoomDTO(ChatRoomDTO rdto) {
		if (dao.insertRoom(rdto) == 1) {
			return "success";
		} else {
			return "fail";
		}
	}

	public String insertMessage(ChatMessageDTO message) {
		if (dao.insertMessage(message) == 1) {
			return "success";
		} else {
			return "fail";
		}
	}

	public List<ChatMessageDTO> getChatList(String roomId) {
		return dao.selectChatList(roomId);
	}
}
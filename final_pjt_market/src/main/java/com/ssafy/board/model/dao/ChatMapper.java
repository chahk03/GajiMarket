package com.ssafy.board.model.dao;

import java.util.List;

import com.ssafy.board.model.dto.ChatMessageDTO;
import com.ssafy.board.model.dto.ChatRoomDTO;

public interface ChatMapper {

    List<ChatRoomDTO> selectRoomList();
    ChatRoomDTO selectRoomOne(String id);
    int insertRoom(ChatRoomDTO room);
    int insertMessage(ChatMessageDTO message);
    List<ChatMessageDTO> selectChatList(String roomId);

}
package com.ssafy.board.model.service;

import com.ssafy.board.model.dto.FileDTO;
import com.ssafy.board.model.dto.MemberDTO;
import com.ssafy.board.model.dto.MemberFileDTO;

public interface MemberService {

    public MemberDTO login(MemberDTO memberDto) throws Exception;
    public MemberDTO userInfo(String userid) throws Exception;
    public void saveRefreshToken(String userid, String refreshToken) throws Exception;
    public Object getRefreshToken(String userid) throws Exception;
    public void deleRefreshToken(String userid) throws Exception;
    public MemberDTO userSignup(MemberDTO memberDto) throws Exception;
    public MemberDTO userModify(MemberDTO memberDto) throws Exception;
    public int addFile(MemberFileDTO file) throws Exception;
    public MemberFileDTO getFile(String username) throws Exception;
    public int modifyFile(MemberFileDTO file) throws Exception;
    public int checkId(String userid);
    public int checkName(String name);
    public int deleteMember(String username, String userid);
    
}
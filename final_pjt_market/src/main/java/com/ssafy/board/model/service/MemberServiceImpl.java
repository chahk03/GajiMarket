package com.ssafy.board.model.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.board.model.dao.FileMapper;
import com.ssafy.board.model.dao.MemberMapper;
import com.ssafy.board.model.dto.FileDTO;
import com.ssafy.board.model.dto.MemberDTO;
import com.ssafy.board.model.dto.MemberFileDTO;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private SqlSession sqlSession;
    
    @Autowired
    private FileMapper fdao;
    
    @Override
    public MemberDTO login(MemberDTO MemberDTO) throws Exception {
        if (MemberDTO.getUserid() == null || MemberDTO.getUserpwd() == null)
            return null;
        return sqlSession.getMapper(MemberMapper.class).login(MemberDTO);
    }

    @Override
    public MemberDTO userInfo(String userid) throws Exception {
        return sqlSession.getMapper(MemberMapper.class).userInfo(userid);
    }

    @Override
    public void saveRefreshToken(String userid, String refreshToken) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("token", refreshToken);
        sqlSession.getMapper(MemberMapper.class).saveRefreshToken(map);
    }

    @Override
    public Object getRefreshToken(String userid) throws Exception {
        return sqlSession.getMapper(MemberMapper.class).getRefreshToken(userid);
    }

    @Override
    public void deleRefreshToken(String userid) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("token", null);
        sqlSession.getMapper(MemberMapper.class).deleteRefreshToken(map);
    }

    @Override
    public MemberDTO userSignup(MemberDTO memberDto) throws Exception {
        sqlSession.getMapper(MemberMapper.class).userSignup(memberDto);
        return memberDto;
    }
    
    @Override
    public int addFile(MemberFileDTO file) {
        return fdao.mfileinsert(file);
    }

    @Override
    public MemberFileDTO getFile(String username) {
        return fdao.selectUserImg(username);
    }

    @Override
    public MemberDTO userModify(MemberDTO memberDto) throws Exception {
        sqlSession.getMapper(MemberMapper.class).userModify(memberDto);
        return sqlSession.getMapper(MemberMapper.class).userInfo(memberDto.getUserid());
    }

    @Override
    public int modifyFile(MemberFileDTO file) throws Exception {
        return fdao.mfileUpdate(file);
    }

    @Override
    public int checkId(String userid) {
        return sqlSession.getMapper(MemberMapper.class).checkId(userid);
    }

    @Override
    public int checkName(String username) {
        return sqlSession.getMapper(MemberMapper.class).checkName(username);
    }

    @Override
    public int deleteMember(String username, String userid) {
        sqlSession.getMapper(MemberMapper.class).deleteBoardByWriter(username);
        sqlSession.getMapper(MemberMapper.class).deleteChatroomByRoomMaker(username);
        sqlSession.getMapper(MemberMapper.class).deleteMfileByUser(username);
        sqlSession.getMapper(MemberMapper.class).deleteRecommendByUser(userid);
        sqlSession.getMapper(MemberMapper.class).deleteMember(userid);

        return 1;
    }

}
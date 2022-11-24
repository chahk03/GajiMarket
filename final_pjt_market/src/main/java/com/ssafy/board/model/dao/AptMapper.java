package com.ssafy.board.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ssafy.board.model.dto.HousedealinfoDTO;
import com.ssafy.board.model.dto.InterestareaDTO;

public interface AptMapper {
    public String selectDongcode(@Param("dongName")String dongName, @Param("sidoName")String sidoName);
    public String selectLatlng(String dongName);
    public List<HousedealinfoDTO> selectList(@Param("dongName")String dongName,
            @Param("dongCode")String dongCode,@Param("dealYear")int dealYear,@Param("dealMonth")int dealMonth);
    public List<String> selectSidoName();// 검색기능
    public List<String> selectGugunName(@Param("sidoName")String sidoName); // 검색기능
    public List<String> selectDongName(@Param("sidoName")String sidoName,@Param("gugunName")String gugunName); // 검색기능
    public String selectDongNameByDongCode(String dongCode);
    public HousedealinfoDTO selectDealinfo(int dno);
    public int registinterest(InterestareaDTO idto);
    public List<InterestareaDTO> listinterest(String userid);
    public int deleteinterest(@Param("userid")String userid, @Param("aptname")String aptname);
    public int selectInterestCount(@Param("userid")String userid, @Param("aptname")String aptname);
    public List<HousedealinfoDTO> selectRecent();
}
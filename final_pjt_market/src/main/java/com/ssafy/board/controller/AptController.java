package com.ssafy.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.board.model.dto.HousedealinfoDTO;
import com.ssafy.board.model.dto.InterestareaDTO;
import com.ssafy.board.model.service.AptService;

@RestController
@RequestMapping("/apt")
public class AptController {
    @Autowired
    private AptService aService;
    
    @GetMapping("/sido")
    public ResponseEntity<List<String>> sidoList(){
        return new ResponseEntity<List<String>>(aService.getSidoNames(),HttpStatus.ACCEPTED);
    }
    @GetMapping("/gugun")
    public ResponseEntity<List<String>> gugunList(String sidoName){
        return new ResponseEntity<List<String>>(aService.getgugunNames(sidoName),HttpStatus.ACCEPTED);

    }
    @GetMapping("/dong")
    public ResponseEntity<List<String>> dongList(String sidoName, String gugunName){
        return new ResponseEntity<List<String>>(aService.getDongNames(sidoName, gugunName),HttpStatus.ACCEPTED);
    }
    @GetMapping("/search")
    public ResponseEntity<List<HousedealinfoDTO>> search(String sido, String dong,int dealYear,int dealMonth){
        System.out.println(sido+" "+dong+" "+dealYear+" "+dealMonth);
        String dongCode = aService.getDongCode(dong, sido);
        System.out.println(dongCode);
        List<HousedealinfoDTO> a = aService.getsearchList(dong, dongCode, dealYear, dealMonth);
        System.out.println(a);
        return new ResponseEntity<List<HousedealinfoDTO>>(aService.getsearchList(dong, dongCode, dealYear, dealMonth),HttpStatus.ACCEPTED);
    }
    @GetMapping("/detail")
    public ResponseEntity<HousedealinfoDTO> search(int dno){
        System.out.println(aService.getdetail(dno));
        return new ResponseEntity<HousedealinfoDTO>(aService.getdetail(dno),HttpStatus.ACCEPTED);
    }
    
    @PostMapping("/registinterest")
    public ResponseEntity<Map<String, Object>> registinterest(@RequestBody InterestareaDTO idto){
        Map<String, Object> result = new HashMap<String, Object>();
        if(aService.registinterest(idto)) {
            result.put("msg", "관심지역 등록 성공");
        }
        else {
            result.put("msg", "이미 관심지역으로 등록되어 있습니다");
        }
        return new ResponseEntity<Map<String, Object>>(result,HttpStatus.ACCEPTED);
    }
    @GetMapping("/listinterest")
    public ResponseEntity<Map<String, Object>> listinterest(String userid){
        System.out.println(userid);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("interestList", aService.listinterest(userid));

        return new ResponseEntity<Map<String, Object>>(result,HttpStatus.ACCEPTED);
    }
    
    @GetMapping("/isinterest")
    public ResponseEntity<?> interestCount(String userid, String aptname) {
    	if(aService.getInterestCount(userid, aptname) != 0) {
    		System.out.println("관심 지역 등록되어있음 OOOOOOOOO " + true);
    		return new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);
    	} else {
    		System.out.println("관심 지역 등록 안되어있음 XXXXXXXXX " + false);
    		return new ResponseEntity<Boolean>(false, HttpStatus.ACCEPTED);
    	}
    }
    
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteinterest(String userid, String aptname){
        System.out.println(userid + " "+ aptname);
        Map<String, Object> result = new HashMap<String, Object>();

        if(aService.deleteinterest(userid, aptname)) {
            result.put("msg", "관심지역 해제 성공");
            result.put("interestList", aService.listinterest(userid));
        }
        else {
            result.put("msg", "관심지역 해제 실패");
        }
        return new ResponseEntity<Map<String, Object>>(result,HttpStatus.ACCEPTED);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<List<HousedealinfoDTO>> recentList(){
        return new ResponseEntity<List<HousedealinfoDTO>>(aService.getRecentList(),HttpStatus.ACCEPTED);
    }
}
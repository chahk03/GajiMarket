package com.ssafy.board.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.board.model.dto.FileDTO;
import com.ssafy.board.model.dto.MemberDTO;
import com.ssafy.board.model.dto.MemberFileDTO;
import com.ssafy.board.model.service.JwtServiceImpl;
import com.ssafy.board.model.service.MemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/user")
@Api("사용자 컨트롤러  API V1")
public class MemberController {

    public static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    @Autowired
    private JwtServiceImpl jwtService;

    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "로그인", notes = "Access-token과 로그인 결과 메세지를 반환한다.", response = Map.class)
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody @ApiParam(value = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) MemberDTO memberDto) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        try {
            MemberDTO loginUser = memberService.login(memberDto);
            if (loginUser != null) {
                String accessToken = jwtService.createAccessToken("userid", loginUser.getUserid());// key, data
                String refreshToken = jwtService.createRefreshToken("userid", loginUser.getUserid());// key, data
                memberService.saveRefreshToken(memberDto.getUserid(), refreshToken);
                logger.debug("로그인 accessToken 정보 : {}", accessToken);
                logger.debug("로그인 refreshToken 정보 : {}", refreshToken);
                resultMap.put("access-token", accessToken);
                resultMap.put("refresh-token", refreshToken);
                resultMap.put("message", SUCCESS);
                status = HttpStatus.ACCEPTED;
            } else {
                resultMap.put("message", FAIL);
                status = HttpStatus.ACCEPTED;
            }
        } catch (Exception e) {
            logger.error("로그인 실패 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @ApiOperation(value = "회원인증", notes = "회원 정보를 담은 Token을 반환한다.", response = Map.class)
    @GetMapping("/info/{userid}")
    public ResponseEntity<Map<String, Object>> getInfo(
            @PathVariable("userid") @ApiParam(value = "인증할 회원의 아이디.", required = true) String userid,
            HttpServletRequest request) {
//        logger.debug("userid : {} ", userid);
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        if (jwtService.checkToken(request.getHeader("access-token"))) {
            logger.info("사용 가능한 토큰!!!");
            try {
//                로그인 사용자 정보.
                MemberDTO memberDto = memberService.userInfo(userid);
                resultMap.put("userInfo", memberDto);
                resultMap.put("message", SUCCESS);
                status = HttpStatus.ACCEPTED;
            } catch (Exception e) {
                logger.error("정보조회 실패 : {}", e);
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
            logger.error("사용 불가능 토큰!!!");
            resultMap.put("message", FAIL);
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @ApiOperation(value = "로그아웃", notes = "회원 정보를 담은 Token을 제거한다.", response = Map.class)
    @GetMapping("/logout/{userid}")
    public ResponseEntity<?> removeToken(@PathVariable("userid") String userid) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
            memberService.deleRefreshToken(userid);
            resultMap.put("message", SUCCESS);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            logger.error("로그아웃 실패 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);

    }

    @ApiOperation(value = "Access Token 재발급", notes = "만료된 access token을 재발급받는다.", response = Map.class)
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody MemberDTO memberDto, HttpServletRequest request)
            throws Exception {
        System.out.println("토큰 재발급 하러 왔다 " + memberDto);
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        String token = request.getHeader("refresh-token");
        logger.debug("token : {}, memberDto : {}", token, memberDto);
        if (jwtService.checkToken(token)) {
            if (token.equals(memberService.getRefreshToken(memberDto.getUserid()))) {
                String accessToken = jwtService.createAccessToken("userid", memberDto.getUserid());
                logger.debug("token : {}", accessToken);
                logger.debug("정상적으로 액세스토큰 재발급!!!");
                resultMap.put("access-token", accessToken);
                resultMap.put("message", SUCCESS);
                status = HttpStatus.ACCEPTED;
            }
        } else {
            logger.debug("리프레쉬토큰도 사용불!!!!!!!");
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(MemberDTO memberDto, MultipartFile[] uploadFile) throws Exception {
        System.out.println("회원가입에 왔어요");
        System.out.println("input member: " + memberDto);
        // 일단 글이 DB에 저장되야 글번호를 파일 업로드에 넣을 수 있음
        MemberDTO signupResult = memberService.userSignup(memberDto);

        // 글쓰기에 파일 첨부 기능 추가
        if (uploadFile != null && uploadFile.length > 0) {
            String uploadPath = "C:/SSAFY/workspace/board_final_pjt/board_final_pjt/src/assets/memberImg";
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) { // 업로드 파일 저장 폴더 없으면 생성
                uploadDir.mkdir();
            }

            for (MultipartFile file : uploadFile) { // 파일 개수만큼 반복
                String savedName = new Random().nextInt(1000000000) + "." + file.getOriginalFilename().split("\\.")[1];
                System.out.println(savedName);
                uploadPath += "/";
                uploadPath += savedName;
                System.out.println(uploadPath);
                File savedFile = new File(uploadPath);
                file.transferTo(savedFile); // profile.png -> c:/SSAFY/upload/2145346434

                MemberFileDTO dto = new MemberFileDTO(memberDto.getUsername(), file.getOriginalFilename(), savedName,
                        uploadPath);
                memberService.addFile(dto);
            }
        }

        if (signupResult != null) {
            return new ResponseEntity<String>("회원 가입되었습니다.", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<String>("회원 가입에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/mypage")
    public ResponseEntity<?> mypage(String userid, String username) throws Exception {
        System.out.println("마이 페이지 : " + userid + " " + username);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("myInfo", memberService.userInfo(userid));
        result.put("myImg", memberService.getFile(username));
        System.out.println(result);
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.ACCEPTED);
    }

    @PostMapping("/modify")
    public ResponseEntity<?> modify(@RequestBody MemberDTO memberDto) throws Exception {
        System.out.println("회원정보 수정에 왔어요");
        System.out.println("input member: " + memberDto);
        // 일단 글이 DB에 저장되야 글번호를 파일 업로드에 넣을 수 있음
        MemberDTO modifyResult = memberService.userModify(memberDto);
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("myInfo", modifyResult);

        if (modifyResult != null) {
            result.put("msg", "회원 정보를 수정하였습니다.");
            return new ResponseEntity<Map<String, Object>>(result, HttpStatus.ACCEPTED);
        } else {
            result.put("msg", "fail");
            return new ResponseEntity<String>("error", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/image")
    public ResponseEntity<?> modify(String username, MultipartFile[] uploadFile) throws Exception {
        System.out.println("회원정보 사진 수정에 왔어요");
        Map<String, Object> result = new HashMap<String, Object>();

        // 회원정보 수정에 사진 수정
        if (uploadFile != null && uploadFile.length > 0) {
            System.out.println("사진 있어???");
            String uploadPath = "C:/SSAFY/workspace/board_final_pjt/board_final_pjt/src/assets/memberImg";
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) { // 업로드 파일 저장 폴더 없으면 생성
                uploadDir.mkdir();
            }

            for (MultipartFile file : uploadFile) { // 파일 개수만큼 반복
                String savedName = new Random().nextInt(1000000000) + "." + file.getOriginalFilename().split("\\.")[1];
                System.out.println(savedName);
                uploadPath += "/";
                uploadPath += savedName;
                System.out.println(uploadPath);
                File savedFile = new File(uploadPath);
                file.transferTo(savedFile); // profile.png -> c:/SSAFY/upload/2145346434

                MemberFileDTO dto = new MemberFileDTO(username, file.getOriginalFilename(), savedName, uploadPath);
                System.out.println(dto);
                memberService.modifyFile(dto);
            }
        }

        result.put("myImg", memberService.getFile(username));
        System.out.println(result);
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.ACCEPTED);
    }
    
    @GetMapping("/idcheck")
    public String idcheck(String userid) throws Exception {
        System.out.println("아이디체크에  왔어요");
        if(memberService.checkId(userid)==1) {
            return "이미 존재하는 아이디입니다.";
        }
        else {
            return "사용가능한 아이디입니다.";
        }
    }
    @GetMapping("/namecheck")
    public String namecheck(String username) throws Exception {
        System.out.println("아이디체크에  왔어요");
        if(memberService.checkName(username)==1) {
            return "이미 존재하는 별명입니다.";
        }
        else {
            return "사용가능한 별명입니다.";
        }
    }
    
    @DeleteMapping
    public String memberdelete(String username, String userid) {
        if(memberService.deleteMember(username,userid)==1) {
            return "회원정보 삭제 성공";
        }
        else {
            return "회원정보 삭제 실패";
        }
    }
}
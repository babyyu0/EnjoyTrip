package com.ssafy.trip.member.controller;

import com.ssafy.trip.global.aop.TimeTrace;
import com.ssafy.trip.member.model.dto.command.LoginCommandDto;
import com.ssafy.trip.member.model.dto.command.LogoutCommandDto;
import com.ssafy.trip.member.model.dto.command.RegisterCommandDto;
import com.ssafy.trip.member.model.dto.command.ValidIdCommandDto;
import com.ssafy.trip.member.model.dto.request.LoginRequestDto;
import com.ssafy.trip.member.model.dto.request.LogoutRequestDto;
import com.ssafy.trip.member.model.dto.request.RegisterRequestDto;
import com.ssafy.trip.member.model.service.MemberService;
import com.ssafy.trip.global.util.exception.MyException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("exist/{memberId}")
    public ResponseEntity<?> isValidId(@PathVariable("memberId") String memberId) throws MyException {
        return ResponseEntity.ok(
                memberService.isValidId(ValidIdCommandDto.builder().memberId(memberId).build())
        );
    }

    @TimeTrace
    @PostMapping(value = "register", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> register(@RequestPart(value = "member") @Valid RegisterRequestDto registerRequestDto, @RequestPart(value = "profile", required = false) MultipartFile profile) throws MyException {
        return ResponseEntity.ok(
                memberService.register(RegisterCommandDto.from(registerRequestDto, profile))
        );
    }

    @TimeTrace
    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) throws MyException {
        return ResponseEntity.ok(memberService.login(LoginCommandDto.from(loginRequestDto)));
    }

    @PostMapping(value = "logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequestDto loginRequestDto) throws MyException {
        return ResponseEntity.ok(memberService.logout(LogoutCommandDto.from(loginRequestDto)));
    }

}
/*ㅒ

	@PostMapping("logout")
	public String logout(HttpServletRequest request) throws MyException {
		HttpSession session = request.getSession(false);
		session.invalidate();

		return null;
	}

	@PostMapping("get-logged-member")
	public ResponseEntity<?> getLoggedMemberVO(HttpServletRequest request) throws MyException {
		HttpSession session = request.getSession(false);
		
		if(session != null) {
			MemberVO member = memberService.selectOne(new MemberVO((String) session.getAttribute("id"), null, null, null, 0, 0));
			member.setSido(tripService.getOneSido(member.getSidoCode()));
			member.setGugun(tripService.getOneGugunBySidoCode(member.getGugunCode(), member.getSidoCode()));
			
			return new ResponseEntity<>(member, HttpStatus.OK);
		}

		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@PostMapping("upload-profile")
	public ResponseEntity<?> uploadProfile(HttpServletRequest request, @RequestBody MultipartFile profileImg) {

		HttpSession session = request.getSession(false);
		
		if(session != null) {
			try {
				String contentType = profileImg.getContentType();
				String originalFileExtension;
				if(contentType == null || contentType.trim().equals("")) {
					throw new RuntimeException("파일 업로드 실패");
		        }
		        else {  // 확장자가 jpeg, png인 파일들만 받아서 처리
		            if(contentType.contains("image/jpeg"))
		                originalFileExtension = ".jpg";
		            else if(contentType.contains("image/png"))
		                originalFileExtension = ".png";
		            else  // 다른 확장자일 경우 처리 x
		            	throw new RuntimeException("파일 업로드 실패");
		        }
				// 파일 이름
				String new_file_name = System.nanoTime() + originalFileExtension;
				File profileFile = new File("D:/SSAFY/01_workspace/04_PJT/spring-workspace/Pair01_ParkSeungSoo_YouYoung/src/main/resources/static/img/userProfile" + File.separator + new_file_name);
				profileImg.transferTo(profileFile);
	
				profileFile.setExecutable(false);
				
				memberService.setProfileImg((String) session.getAttribute("id"), profileFile);
				
				return new ResponseEntity<>("http://localhost:9999/static/img/userProfile/" + profileFile.getName()	, HttpStatus.OK);
			} catch (IllegalStateException e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			} catch (IOException e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			} catch(Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		}
		return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
		
	}
*/
package com.ssafy.trip.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.model.service.MemberService;
import com.ssafy.trip.model.vo.MemberVO;
import com.ssafy.trip.util.MyException;

@RestController
@RequestMapping("/member")
@CrossOrigin
public class MemberProcessController {

	@Autowired
	private MemberService memberService;

	@PostMapping("login")
	public String login(HttpSession session, MemberVO member) throws MyException {
		String name = memberService.login(member);
		session.setAttribute("id", member.getId());

		return name;
	}

	@PostMapping("logout")
	public String logout(HttpSession session) throws MyException {
		session.invalidate();

		return null;
	}

	@PostMapping("duplicate-check")
	public String duplicateCheck(MemberVO member) throws MyException {
		MemberVO selMember = memberService.selectOne(member);

		if (selMember == null)
			return "ok";
		return null;
	}

	@PostMapping("regist")
	public String regist(MemberVO member) throws MyException {
		MemberVO selMember = memberService.selectOne(member);
		if (selMember != null)
			return null;

		memberService.regist(member);
		return "ok";
	}

	@PostMapping("get-logged-member")
	public MemberVO getLoggedMember(HttpSession session) throws MyException {
		System.out.println("Session id : " + session.getAttribute("id"));
		MemberVO selMember = memberService.selectOne(new MemberVO((String) session.getAttribute("id"), null, null, null, null, null));

		return selMember;
	}
}

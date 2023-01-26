package com.hanul.smart;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import common.CommonUtility;
import member.MemberServiceImpl;
import member.MemberVO;


@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired private CommonUtility common;
	@Autowired private MemberServiceImpl member;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model, HttpSession session, HttpServletRequest request) {
		session.removeAttribute("category");
		
		/*
		// 암호화하지 않은 비번을 암호하시켜서 저장해두는 처리 -----
		// 전체 회원의 목록을 조회해와 각 회원의 비밀번호에 대한 암호화처리
		List<MemberVO> list = member.member_list();
		for( MemberVO vo : list ) {
			if( vo.getSalt() == null && vo.getPw() != null ) {
				String salt = common.generateSalt(); //암호화용 salt 생성
				String pw = common.getEncrypt(vo.getPw(), salt); //암호화된 비번
				vo.setSalt(salt);
				vo.setPw(pw);
				member.member_salt_pw(vo);
			}
		}		
		//-----------------------------------------------------------
		List<MemberVO> list = member.member_admin();
		for(MemberVO vo : list ) {
			if( vo.getSalt() == null && vo.getPw() != null ) {
				String salt = common.generateSalt();
				String pw = common.getEncrypt(vo.getPw(), salt); 
				vo.setSalt(salt);
				vo.setPw(pw);
				member.member_salt_pw(vo);
			}
		}
		 */
		
		return "home";
	}
	
}

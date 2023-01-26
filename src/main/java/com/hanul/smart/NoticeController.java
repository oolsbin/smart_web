package com.hanul.smart;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import common.CommonUtility;
import member.MemberServiceImpl;
import member.MemberVO;
import notice.NoticePageVO;
import notice.NoticeServiceImpl;
import notice.NoticeVO;

@Controller
public class NoticeController {
	@Autowired private NoticeServiceImpl service;
	
	//
	
	//공지글 첨부파일 다운로드 요청
	@ResponseBody @RequestMapping(value="/download.no"
									, produces="text/html; charset=utf-8")
	public String download(int id, String url, HttpServletRequest request
						, HttpServletResponse response) throws Exception{
		//해당 공지글에 대한 첨부파일 정보를 DB에서 조회해와
		//클라이언트 에 다운로드하는 처리
		NoticeVO vo = service.notice_info(id);
		boolean download 
		=common.fileDownload(vo.getFilename(), vo.getFilepath(), request, response);
		
		if( !download ) {
			//다운로드가 안되어지는 경우 처리
			StringBuffer msg = new StringBuffer("<script>");
			msg.append("alert('다운로드할 파일이 없습니다!'); location='")
				.append(url).append("'; ");
			msg.append("</script>");
			return msg.toString();
		}else
			return null;
	}
	
	
	//공지글삭제처리 요청
	@RequestMapping("/delete.no")
	public String delete(int id, NoticePageVO page
						, HttpServletRequest request) throws Exception{
		//해당 공지글을 DB에서 삭제한 후
		NoticeVO vo = service.notice_info(id);
		if( service.notice_delete(id)==1 ) {
			//첨부파일이 있었던 경우 해당 물리적파일 삭제
			common.attachedFile_delete(vo.getFilepath(), request);
		}
		//응답화면연결
		return "redirect:list.no?curPage=" + page.getCurPage()
		+ "&search=" + page.getSearch()
		+ "&keyword=" + URLEncoder.encode(page.getKeyword(),"utf-8");
	}
	
	//공지글에 대한 답글저장처리 요청
	@RequestMapping("/reply_insert.no")
	public String reply(NoticeVO vo, NoticePageVO page) throws Exception {
		//화면에서 입력한 정보를 DB에 신규저장한 후
		service.notice_reply_insert(vo);
		//응답화면연결
		return "redirect:list.no?curPage=" + page.getCurPage()
				+ "&search=" + page.getSearch()
				+ "&keyword=" + URLEncoder.encode(page.getKeyword(),"utf-8");
	}
	
	
	
	//공지글에 대한 답글쓰기화면 요청
	@RequestMapping("/reply.no")
	public String reply(int id, NoticePageVO page, Model model) {
		model.addAttribute("page", page);
		//DB에서 답글쓰기위한 원글의 정보를 조회해와
		//답글화면에 사용할 수 있도록 Model에 attribute로 담는다
		model.addAttribute("vo", service.notice_info(id));
		//응답화면연결: 원글에 대한 답글쓰기 화면
		return "notice/reply";
	}
	
	//공지글수정저장처리 요청
	@RequestMapping("/update.no")
	public String update(NoticeVO vo, NoticePageVO page
						, String filename, MultipartFile file
						, HttpServletRequest request) throws Exception{
		
		//DB에 저장되어 있던 첨부파일정보를 조회해 온다
		NoticeVO notice = service.notice_info(vo.getId());
		
		if( file.isEmpty() ) {
			//첨부파일이 없는 경우
			if( filename.isEmpty() ) {
				//파일명이 없는 경우는 삭제한 경우
				common.attachedFile_delete(notice.getFilepath(), request);
				
			}else {
				//파일명이 있는 경우
				//원래 첨부파일이 있었고, 그 파일을 그대로 사용하는 경우
				vo.setFilename( notice.getFilename() );
				vo.setFilepath( notice.getFilepath() );
			}
			
		}else {
			//첨부파일이 있는 경우
			vo.setFilename( file.getOriginalFilename() );
			vo.setFilepath( common.fileUpload("notice", file, request) );	
			
			//원래 첨부파일이 있었다면 물리적파일을 삭제
			common.attachedFile_delete( notice.getFilepath(), request );
		}
		
		
		//화면에서 수정입력한 정보를 DB에 변경저장한 후 
		service.notice_update(vo);
		//응답화면연결
		return "redirect:info.no?id=" + vo.getId() 
						+ "&curPage=" + page.getCurPage()
						+ "&search=" + page.getSearch()
						+ "&keyword=" + URLEncoder.encode(page.getKeyword(),"utf-8")
						;
	}
	

	
	//공지글수정화면 요청
	@RequestMapping("/modify.no")
	public String modify(int id, Model model, NoticePageVO page) {
		//해당 공지글을 DB에서 조회해와
		NoticeVO vo = service.notice_info(id);
		//화면에 출력할 수 있도록 Model에 attribute로 담는다
		model.addAttribute("vo", vo);
		model.addAttribute("page", page);
		//응답화면연결
		return "notice/modify";
	}
	
	
	
	//공지글상세화면 요청
	@RequestMapping("/info.no")
	public String info(Model model, int id, NoticePageVO page) {
		//조회수변경
		service.notice_read(id);
		//해당 공지글 정보를 DB에서 조회해와
		NoticeVO vo = service.notice_info(id);
		//화면에 출력할 수 있도록 Model 에 attribute 로 담는다
		model.addAttribute("vo", vo);
		model.addAttribute("crlf", "\r\n");
		model.addAttribute("page", page);
		//응답화면연결
		return "notice/info";
	}
	
	
	//공지글저장처리 요청
	@RequestMapping("/insert.no")
	public String insert(NoticeVO vo, MultipartFile file, HttpServletRequest request) {
		//첨부파일이 있는 경우
		if( ! file.isEmpty() ) {
			vo.setFilename( file.getOriginalFilename() );
			vo.setFilepath( common.fileUpload("notice", file, request) );
		}
		
		//화면에서 입력한 정보를 DB에 신규저장한 후
		service.notice_insert(vo);
		//응답화면연결
		return "redirect:list.no";
	}
	
	
	//공지글쓰기 화면 요청
	@RequestMapping("/new.no")
	public String notice() {
		return "notice/new";
	}
	
	@Autowired private MemberServiceImpl member;
	@Autowired private CommonUtility common;
	
	//공지글목록 화면 요청
	@RequestMapping("/list.no")
	public String list(Model model, NoticePageVO page, HttpSession session) {
		//임시로 로그인처리를 한다 -----
		String id = "admin97";
		String pw = "manager";		
		String salt = member.member_salt(id);
		pw = common.getEncrypt(pw, salt);		
		MemberVO vo = member.member_login(id, pw);
		session.setAttribute("loginInfo", vo);
		//------------------------------
		
		session.setAttribute("category", "no");
		
		//공지글 정보 목록을 DB에서 조회해와
		//List<NoticeVO> list = service.notice_list();
		//화면에 출력할 수 있도록 Model에 attribute로 담는다
		//model.addAttribute("list", list);
		page = service.notice_list(page);
		model.addAttribute("page", page);
		
		//응답화면연결
		return "notice/list";
	}
	
}

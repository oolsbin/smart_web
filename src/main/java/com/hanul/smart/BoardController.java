package com.hanul.smart;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import board.BoardCommentVO;
import board.BoardFileVO;
import board.BoardPageVO;
import board.BoardServiceImpl;
import board.BoardVO;
import common.CommonUtility;
import member.MemberServiceImpl;
import member.MemberVO;

@Controller
public class BoardController {
	@Autowired private BoardServiceImpl service;
	
	//방명록 댓글삭제처리 요청
	@ResponseBody @RequestMapping("/board/comment/delete/{id}")
	public void comment_delete(@PathVariable int id) {
		//해당 댓글을 DB에서 삭제
		service.board_comment_delete(id);
	}
	
	
	//방명록 댓글변경저장처리 요청
	@ResponseBody @RequestMapping(value="/board/comment/update"
							, produces="text/plain; charset=utf-8")
	public String comment_update(@RequestBody BoardCommentVO vo) {
		//변경입력한 댓글을 DB에 변경저장한다
		return service.board_comment_update(vo)==1 ? "성공^^" : "실패ㅠㅠ";
	}
	
	//방명록 댓글목록조회 요청
	@RequestMapping("/board/comment/list/{board_id}")
	public String comment_list(@PathVariable int board_id, Model model) {
		//해당 방명록글에 대한 댓글목록을 DB에서 조회
		//댓글목록화면에 데이터를 출력할 수 있도록 Model에 attribute로 담는다
		model.addAttribute("list", service.board_comment_list(board_id) );
		model.addAttribute("crlf", "\r\n");
		model.addAttribute("lf", "\n");
		//응답화면연결
		return "board/comment/comment_list";
	}
	
	//방명록 댓글저장처리 요청
	@ResponseBody @RequestMapping("/board/comment/insert")
	public boolean comment_insert(BoardCommentVO vo) {
		//화면에서 입력한 정보를 DB에 신규저장
		return service.board_comment_insert(vo)==1 ? true : false;
	}
	
	
	//신규 방명록 글저장처리 요청
	@RequestMapping("/insert.bo")
	public String insert(BoardVO vo, MultipartFile file[], HttpServletRequest request) {
		//첨부파일 저장
		//file 태그가 한 개이면 첨부파일이 없는 경우
		if( file.length > 1 ) {
			attached_file(vo, file, request);
		}
		
		//화면에서 입력한 정보를 DB에 신규저장한 후
		service.board_insert(vo);
		
		//응답화면연결
		return "redirect:list.bo";
	}
	
	private void attached_file(BoardVO vo, MultipartFile file[], HttpServletRequest request) {
		//첨부파일 정보를 DB에 신규저장
		List<BoardFileVO> list = null;
		for(MultipartFile attached : file) {
			if( attached.isEmpty() ) continue;
			if( list==null ) list = new ArrayList<BoardFileVO>();
			BoardFileVO filevo = new BoardFileVO();
			filevo.setFilename( attached.getOriginalFilename() );
			filevo.setFilepath( common.fileUpload("board", attached, request) );
			list.add(filevo);
		}
		vo.setFileList(list);
	}
	
	//신규 방명록 글쓰기화면 요청
	@RequestMapping("/new.bo")
	public String board() {
		//응답화면연결
		return "board/new";
	}
	
	//첨부파일 다운로드요청
	@RequestMapping("/download.bo")
	public String download(int file, int id, Model model
							, HttpServletRequest request
							, HttpServletResponse response )throws Exception {
		//해당 파일의 정보를 DB에서 조회해와 다운로드한다
		BoardFileVO vo = service.board_file_info(file);
		boolean download
		= common.fileDownload(vo.getFilename(), vo.getFilepath(), request, response);
		if( !download ) {
			//리다이렉트할 화면에 출력할 정보를 Model에 attribute로 담는다
			model.addAttribute("id", id);
			model.addAttribute("url", "info.bo");
			model.addAttribute("download", true);
			return "board/redirect";
		}else
			return null;
	}
	
	
	//방명록 정보 삭제처리 요청
	@RequestMapping("/remove.bo")
	public String delete(int id, BoardPageVO page, Model model
						, HttpServletRequest request) {
		//물리적파일삭제를 위해 첨부파일정보를 조회해 둔다
		List<BoardFileVO> list = service.board_file_list(id);
		
		//해당 방명록정보를 DB에서 삭제한후
		if( service.board_delete(id)==1 ) { //첨부파일정보도 함께 삭제됨(DB에 지정)
			for(BoardFileVO vo : list) {
				common.attachedFile_delete(vo.getFilepath(), request);
			}
		}
		
		//리다이렉트할 페이지에서 submit하기위해 사용할 데이터를 
		//Model에 attribute로 담는다
		model.addAttribute("page", page);
		model.addAttribute("url", "list.bo");
		
		//응답화면연결
		return "board/redirect";
	}
	
	
	//방명록 정보 수정처리 요청
	@RequestMapping ("/update.bo")
	public String update(Model model, MultipartFile file[]
						, HttpServletRequest request
						, String removed, int id, BoardVO vo) {
		//추가/변경첨부한 첨부파일 처리
		attached_file(vo, file, request);
		
		//화면에서 입력한 정보를 DB에 변경저장한 후
		if( service.board_update(vo)==1 ) {
		
		//	변경후 삭제된 물리적첨부파일 처리: DB삭제 + 물리적파일삭제
			if( ! removed.isEmpty() ) {
				//DB삭제하기 전에 물리적파일의 위치(filepath)를 조회해둔다
				List<BoardFileVO> list = service.board_file_list(removed);
				
				//DB삭제
				if( service.board_file_delete(removed) > 0 ) {
					//물리적파일삭제
					for(BoardFileVO filevo : list) {
						common.attachedFile_delete(filevo.getFilepath(), request);
					}
				}
			}			
		}
		
		//파라미터 노출되지 않도록 redirect 페이지를 통해 form을 submit
		model.addAttribute("url", "info.bo");
		model.addAttribute("id", id);
		//응답화면연결		
		return "board/redirect"; 
	}
	
	
	
	//방명록 정보 수정화면 요청
	@RequestMapping("/modify.bo")
	public String modify(Model model, int id, BoardPageVO page) {
		//해당 방명록 글의 정보를 DB에서 조회해와 
		//화면에 출력할 수 있도록 Model에 attribute로 담는다
		model.addAttribute("vo", service.board_info(id));
		model.addAttribute("page", page);
		return "board/modify";
	}
	
	
	@Autowired private MemberServiceImpl member;
	@Autowired private CommonUtility common;
	
	//방명록 정보화면 요청
	@RequestMapping("/info.bo")
	public String info(int id, BoardPageVO page, Model model) {
		//조회수증가처리
		service.board_read(id);
		
		//선택한 방명록 글정보를 DB에서 조회해와
		//화면에 출력할 수 있도록 Model에 attribute 로 담는다
		model.addAttribute("vo", service.board_info(id) );
		model.addAttribute("page", page);
		model.addAttribute("crlf", "\r\n");
		
		//응답화면연결
		return "board/info";
	}
	
	
	//방명록 목록화면 요청
	@RequestMapping("/list.bo")
	public String list(HttpSession session, BoardPageVO page, Model model) {
		//임시로 로그인처리를 한다 -----
		String id = "sim2022";
		String pw = "Sim2022";		
		String salt = member.member_salt(id);
		pw = common.getEncrypt(pw, salt);		
		MemberVO vo = member.member_login(id, pw);
		session.setAttribute("loginInfo", vo);
		//------------------------------
		
		session.setAttribute("category", "bo");
		//DB에서 방명록 글 목록을 조회해와 
		//화면에 출력할 수 있도록 Model에 attribute 로 담는다
		model.addAttribute("page",  service.board_list(page) );
		
		//응답화면연결
		return "board/list";
	}
}

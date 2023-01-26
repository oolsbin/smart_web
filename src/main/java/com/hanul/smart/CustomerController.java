package com.hanul.smart;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import customer.CustomerServiceImpl;
import customer.CustomerVO;

@Controller
public class CustomerController {
	@Autowired private CustomerServiceImpl service;
	
	//신규고객정보 저장처리 요청
	@RequestMapping("/insert.cu")
	public String insert( CustomerVO vo ) {
		//화면에서 입력한 정보를 DB에 신규저장한 후
		service.customer_insert(vo);
		//응답화면연결
		return "redirect:list.cu";
	}
	
	
	//신규고객등록화면 요청
	@RequestMapping("/new.cu")
	public String customer() {
		//응답화면연결
		return "customer/new";
	}
	
	
	//고객정보삭제 처리 요청
	@RequestMapping("/delete.cu")
	public String delete(String id) {
		//해당 고객정보를 DB에서 삭제한 후
		service.customer_delete(id);
		//응답화면 연결
		return "redirect:list.cu";
	}
	
	//고객정보수정 저장처리 요청
	@RequestMapping("/update.cu")
	public String update( CustomerVO vo ) {
		//화면에서 수정 입력한 정보를 DB에 변경저장처리한 후
		service.customer_update(vo);
		//응답화면연결
		return "redirect:detail.cu?id="+ vo.getId();
	}
	
	
	//고객정보수정화면 요청
	@RequestMapping("/modify.cu")
	public String modify( String id, Model model ) {
		//해당 특정 고객정보를 DB에서 조회해와
		CustomerVO vo = service.customer_info( id );
		//화면에 출력할 수 있도록 Model 에 attribute로 데이터를 담는다
		model.addAttribute("vo", vo);
		
		//응답화면연결
		return "customer/modify";
	}
	
	
	//특정고객정보화면 요청
	@RequestMapping("/detail.cu")
//	public String detail(@RequestParam String id) {
	public String detail(String id, Model model) {
		//해당 특정 고객정보를 DB에서 조회해와
		CustomerVO vo = service.customer_info( id );
		//화면에 출력할 수 있도록 데이터를 Model 에 attribute 로 담는다
		model.addAttribute("vo", vo);
		
		//응답화면연결
		return "customer/detail";
	}
	
	
	//고객목록화면 요청
	@RequestMapping("/list.cu")
	public String list(Model model, HttpSession session) {
		//고객관리 가 선택되어졌음에 해당하는 데이터를 세션에 담는다.
		session.setAttribute("category", "cu");
		
		//DB에서 고객목록정보를 조회해와
		List<CustomerVO> list = service.customer_list();
		//화면에서 출력할 수 있도록
		//Model 에 attribute 로 데이터를 담는다
		model.addAttribute("list", list);
		
		//응답화면연결
		return "customer/list";
	}
}

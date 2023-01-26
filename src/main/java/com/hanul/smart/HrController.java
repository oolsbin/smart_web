package com.hanul.smart;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hr.DepartmentVO;
import hr.EmployeeVO;
import hr.HrServiceImpl;
import hr.JobVO;

@Controller
public class HrController {
	@Autowired private HrServiceImpl service;
	
	//신규사원등록처리 요청
	@RequestMapping("/insert.hr")
	public String insert(EmployeeVO vo) {
		//화면에서 입력한 정보를 DB에 신규저장처리한 후
		service.employee_insert(vo);
		//응답화면 연결
		return "redirect:list.hr";
	}
	
	
	//신규사원등록화면 요청
	@RequestMapping("/new.hr")
	public String hr(Model model) {
		//회사의 부서/업무/매니저 목록을 DB에서 조회해와
		List<DepartmentVO> departments = service.hr_department_list();
		List<JobVO> jobs = service.hr_job_list();
		List<EmployeeVO> managers = service.hr_manager_list();
		//신규등록화면에서 출력할 수 있도록 Model 에 attribute 로 담는다
		model.addAttribute("jobs", jobs);
		model.addAttribute("departments", departments);
		model.addAttribute("managers", managers);
		//응답화면연결
		return "hr/new";
	}
	
	
	//사원정보 삭제처리 요청
	@RequestMapping("/delete.hr")
	public String delete(int id) {
		//해당 사원정보를 DB에서 삭제한 후
		service.employee_delete(id);
		//응답화면 연결
		return "redirect:list.hr";
	}
	
	//사원정보 수정저장처리 요청
	@RequestMapping("/update.hr")
	public String update(EmployeeVO vo) {
		//화면에서 변경입력한 사원정보를 DB에 변경저장처리한 후
		service.employee_update(vo);
		//응답화면연결
		return "redirect:info.hr?id=" + vo.getEmployee_id();
	}
	
	
	//사원정보 수정화면 요청
	@RequestMapping("/modify.hr")
	public String modify( int id, Model model ) {
		//회사의 부서/업무/매니저 목록을 DB에서 조회..
		List<DepartmentVO> departments = service.hr_department_list();
		List<JobVO> jobs = service.hr_job_list();
		List<EmployeeVO> managers = service.hr_manager_list();

		
		//사원정보를 DB에서 조회해와
		EmployeeVO vo = service.employee_info(id);
		//수정화면에 출력할 수 있도록 Model에 attribute 로 데이터를 담는다
		model.addAttribute("vo", vo);
		model.addAttribute("departments", departments);
		model.addAttribute("jobs", jobs);
		model.addAttribute("managers", managers);
		//응답화면연결
		return "hr/modify";
	}
	
	
	//특정사원의 정보화면 요청
	@RequestMapping("/info.hr")
	public String info( int id, Model model ) {
//		int id = Integer.parseInt( request.getParameter("id") ); //서블릿에서는 변화처리가 필요함
		//특정 사원의정보를 DB에서 조회해와
		EmployeeVO vo = service.employee_info( id );
		//화면에 출력할 수 있도록 Model에 attribute로 데이터를 담는다.
		model.addAttribute("vo", vo);
		//응답화면연결
		return "hr/info";
	}
	
	//사원목록화면 요청
	@RequestMapping("/list.hr")  
	public String list(Model model, HttpSession session
						, @RequestParam(defaultValue = "-1") int department_id) {
		session.setAttribute("category", "hr");
		
		//사원의 부서목록 조회...
		List<DepartmentVO> departments = service.employee_department_list();
		
		//DB에서 사원목록을 조회해와
		List<EmployeeVO> list;
		if( department_id == -1 )
			list = service.employee_list();
		else 
			list = service.employee_list(department_id);
		
		//화면에서 출력할 수 있도록 Model에 attribute 로 데이터를 담는다
		model.addAttribute("list", list);
		model.addAttribute("departments", departments);
		model.addAttribute("department_id", department_id);
		//응답화면연결
		return "hr/list";
	}
}

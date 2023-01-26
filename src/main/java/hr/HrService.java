package hr;

import java.util.List;

public interface HrService {
	//CRUD(Create, Read, Update, Delete)
	void employee_insert(EmployeeVO vo); 		//신규 사원정보 등록 
	List<EmployeeVO> employee_list(); 			//사원목록 조회
	List<EmployeeVO> employee_list(int deparmtment_id);	//특정 부서의 사원목록 조회
	EmployeeVO employee_info(int employee_id); 	//특정 사원정보 조회
	void employee_update(EmployeeVO vo); 		//사원정보 변경
	void employee_delete(int employee_id); 		//사원정보 삭제
	
	List<DepartmentVO> employee_department_list();//사원이 있는 부서목록
	List<DepartmentVO> hr_department_list(); 	//회사의 전체 부서목록
	List<JobVO> hr_job_list(); 					//회사의 전체 업무목록
	List<EmployeeVO> hr_manager_list(); 		//회사의 전체 매니저사원목록
}

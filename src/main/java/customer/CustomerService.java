package customer;

import java.util.List;

public interface CustomerService {
	//CRUD(Create, Read, Update, Delete)
	int customer_insert(CustomerVO vo);			//신규고객정보 삽입저장
	List<CustomerVO> customer_list();			//고객정보목록 조회
	CustomerVO customer_info(String id);		//특정고객정보 조회
	void customer_update(CustomerVO vo);		//고객정보 변경저장
	void customer_delete(String id);			//고객정보 삭제
}

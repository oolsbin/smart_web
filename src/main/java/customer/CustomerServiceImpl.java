package customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService{
	@Autowired private CustomerDAO dao;
	

	@Override
	public int customer_insert(CustomerVO vo) {
		return dao.customer_insert(vo);
	}

	@Override
	public List<CustomerVO> customer_list() {
		return dao.customer_list();
	}

	@Override
	public CustomerVO customer_info(String id) {
		return dao.customer_info(id);
	}

	@Override
	public void customer_update(CustomerVO vo) {
		dao.customer_update(vo);
	}

	@Override
	public void customer_delete(String id) {
		dao.customer_delete(id);		
	}

}

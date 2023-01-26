package customer;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDAO implements CustomerService{
	@Autowired @Qualifier("hanul") private SqlSession sql ;
	
	@Override
	public int customer_insert(CustomerVO vo) {		
		return sql.insert("customer.insert", vo);
	}

	@Override
	public List<CustomerVO> customer_list() {
		return sql.selectList("customer.list");
	}

	@Override
	public CustomerVO customer_info(String id) {		
		return sql.selectOne("customer.info", id);
	}

	@Override
	public void customer_update(CustomerVO vo) {
		sql.update("customer.update", vo);
	}

	@Override
	public void customer_delete(String id) {
		sql.delete("customer.delete", id);
	}

}

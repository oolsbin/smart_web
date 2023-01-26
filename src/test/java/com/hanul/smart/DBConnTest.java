package com.hanul.smart;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class DBConnTest {
	@Autowired @Qualifier("dbcp-hanul")
	private DataSource dataSource;
	
	@Test
	public void connect() throws SQLException {	
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			System.out.println("DB Connection Success" + conn);
		} catch (Exception e) {
			System.out.println("DB Connection fail");
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}
	
	@Test
	public void query_test() {
		String today = sql.selectOne("customer.today");
		System.out.println("today : "+today);
	}
	@Qualifier("hanul") @Autowired private SqlSession sql;
	
	/*	
	@Autowired   private PasswordEncoder pwEncoder;

	
	
	@Test
	public void member_passwordencode() {
		HashMap<String, String> map = new HashMap<String, String>();
		List<MemberVO> list = sql.selectList("member.list");
		for(MemberVO vo :  list) {
			map.put("id", vo.getId());
			String pw = vo.getId().substring(0,1).toUpperCase() 
						+  vo.getId().substring(1);
			map.put("pw", pwEncoder.encode(pw) );
			sql.update("member.security_pw_update", map);
		}
				
	}
	
	@Test
	public void member_login() {
		HashMap<String, String> map = new HashMap<String, String>();
		String id = "admin";
		String pw = id.substring(0,1).toUpperCase() 
						+  id.substring(1);
		map.put("id", id);
		map.put("pw", pwEncoder.encode(pw));
		List<String> auths = sql.selectList("member.authorities", map);
		MemberVO member = sql.selectOne("member.security_login", map);		
		Optional<MemberVO> vo = Optional.ofNullable(member);
		if( vo.isPresent() ) {
			member.setAuthList(auths);
			System.out.println("이름:" +vo.get().getName());
	        Assert.assertEquals("admin", member.getId());
		}
		
	}
	*/
}

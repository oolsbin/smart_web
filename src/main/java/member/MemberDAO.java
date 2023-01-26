package member;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDAO implements MemberService {
	@Qualifier("hanul") @Autowired private SqlSession sql;

	@Override
	public int member_join(MemberVO vo) {
		return sql.insert("member.join", vo);
	}

	@Override
	public MemberVO member_myinfo(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberVO member_login(String id, String pw) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("pw", pw);
		return sql.selectOne("member.login", map);
	}

	@Override
	public int member_id_check(String id) {
		return sql.selectOne("member.id_check", id);
	}

	@Override
	public int member_update(MemberVO vo) {
		return sql.update("member.update", vo);
	}

	@Override
	public int member_delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<MemberVO> member_list() {
		return sql.selectList("member.list");
	}

	@Override
	public int member_salt_pw(MemberVO vo) {
		return sql.update("member.salt_pw", vo);
	}

	@Override
	public String member_salt(String id) {
		return sql.selectOne("member.salt", id);
	}

	@Override
	public List<MemberVO> member_admin() {
		return sql.selectList("member.admin");
	}

}

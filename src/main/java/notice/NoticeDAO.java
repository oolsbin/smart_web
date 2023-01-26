package notice;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class NoticeDAO implements NoticeService {
	@Autowired @Qualifier("hanul") private SqlSession sql;
	
	@Override
	public int notice_insert(NoticeVO vo) {
		return sql.insert("notice.insert", vo);
	}

	@Override
	public List<NoticeVO> notice_list() {
		return sql.selectList("notice.list");
	}

	@Override
	public NoticeVO notice_info(int id) {
		return sql.selectOne("notice.info", id);
	}

	@Override
	public int notice_update(NoticeVO vo) {
		return sql.update("notice.update", vo);
	}

	@Override
	public int notice_delete(int id) {
		return sql.delete("notice.delete", id);
	}

	@Override
	public int notice_read(int id) {
		return sql.update("notice.read", id);
	}

	@Override
	public int notice_reply_insert(NoticeVO vo) {
		return sql.insert("notice.reply_insert", vo);
	}

	@Override
	public NoticePageVO notice_list(NoticePageVO page) {
		//총 공지글 건수를 조회해온다
		page.setTotalList( sql.selectOne("notice.totalList", page) );
		//시작~끝 글의 범위에 해당하는 목록 10건 조회해온다
		page.setList( sql.selectList("notice.list", page) );
		return page;
	}

}

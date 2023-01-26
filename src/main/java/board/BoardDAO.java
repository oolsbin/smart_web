package board;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDAO implements BoardService {
	@Autowired @Qualifier("hanul") private SqlSession sql;
	
	@Override
	public int board_insert(BoardVO vo) {
		//방명록 글 저장 후
		int insert = sql.insert("board.insert", vo);
		
		//방명록 글에 첨부된 파일을 저장
		if( insert==1 && vo.getFileList()!=null ) {
			sql.insert("board.file_insert", vo);
		}
		return insert;
	}

	@Override
	public BoardPageVO board_list(BoardPageVO page) {
		//글의 총건수를 조회
		page.setTotalList( sql.selectOne("board.total", page) );
		//공지글 10건 조회
		page.setList( sql.selectList("board.list", page) );
		return page;
	}

	@Override
	public BoardVO board_info(int id) {
		BoardVO vo = sql.selectOne("board.info", id);
		vo.setFileList( sql.selectList("board.file_list", id) );
		return vo;
	}

	@Override
	public int board_update(BoardVO vo) {
		int dml = sql.update("board.update", vo);
		if( dml==1 && vo.getFileList()!=null )
			sql.insert("board.file_insert", vo);
		return dml;
	}

	@Override
	public int board_read(int id) {
		return sql.update("board.read", id);
	}

	@Override
	public int board_delete(int id) {
		return sql.delete("board.delete", id);
	}

	@Override
	public BoardFileVO board_file_info(int id) {
		return sql.selectOne("board.file_info", id);
	}

	@Override
	public List<BoardFileVO> board_file_list(String removed) {
		return sql.selectList("board.removed_file_list", removed);
	}

	@Override
	public int board_file_delete(String removed) {
		return sql.delete("board.file_delete", removed);
	}

	@Override
	public List<BoardFileVO> board_file_list(int board_id) {
		return sql.selectList("board.file_list", board_id);
	}

	@Override
	public int board_comment_insert(BoardCommentVO vo) {
		return sql.insert("board.comment_insert", vo);
	}

	@Override
	public List<BoardCommentVO> board_comment_list(int board_id) {
		return sql.selectList("board.comment_list", board_id);
	}

	@Override
	public int board_comment_update(BoardCommentVO vo) {
		return sql.update("board.comment_update", vo);
	}

	@Override
	public int board_comment_delete(int id) {
		return sql.delete("board.comment_delete", id);
	}

}

package board;

import java.util.List;

public interface BoardService {
	//CRUD
	int board_insert(BoardVO vo);	//방명록 글 신규저장
	BoardPageVO board_list(BoardPageVO page); //방명록 글 목록조회(페이징처리)
	BoardVO board_info(int id); 	//방명록 정보 조회
	int board_update(BoardVO vo); 	//방명록 글변경저장
	int board_read(int id); 		//방명록 글조회수 증가처리
	int board_delete(int id); 		//방명록 글삭제
	
	BoardFileVO board_file_info(int id);//선택한 첨부파일정보 조회
	List<BoardFileVO> board_file_list( String removed ); //삭제대상인 첨부파일정보목록 조회
	List<BoardFileVO> board_file_list( int board_id ); //방명록에 첨부되 첨부파일정보목록 조회
	int board_file_delete( String removed ); //삭제대상인 첨부파일정보 삭제
	
	
	int board_comment_insert(BoardCommentVO vo); //댓글신규저장
	List<BoardCommentVO> board_comment_list(int board_id);//댓글목록조회
	int board_comment_update(BoardCommentVO vo);//댓글수정저장
	int board_comment_delete(int id);//댓글삭제
	
	
	
}

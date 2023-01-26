package member;

import java.util.List;

public interface MemberService {
	//CRUD(Create, Read,  Update, Delete)
	int member_join(MemberVO vo); 				//회원가입시 회원정보 신규저장
	MemberVO member_myinfo(String id); 			//내정보보기(내 프로필/마이인포)
	MemberVO member_login(String id, String pw);//로그인처리시 아이디/비번 일치하는 회원정보조회
	int member_id_check(String id);				//아이디 중복확인
	int member_update(MemberVO vo); 			//회원정보수정
	int member_delete(String id); 				//회원탈퇴시 회원정보삭제
	String member_salt(String id); 				//암호화에 사용한 salt 조회
	
	
	
	List<MemberVO> member_admin(); 				//관리자 회원 조회
	List<MemberVO> member_list(); 				//관리자모드: 전체 회원목록 조회
	int member_salt_pw(MemberVO vo); 	//암호화하지 않은 비밀번호를 암호화해서 저장
}

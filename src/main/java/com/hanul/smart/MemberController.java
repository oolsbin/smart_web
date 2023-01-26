package com.hanul.smart;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import common.CommonUtility;
import member.MemberServiceImpl;
import member.MemberVO;

@Controller
public class MemberController {
 	@Autowired private MemberServiceImpl service;
 	@Autowired private CommonUtility common;
	private String NAVER_ID = "cmg3bBYHwudZL3cfrAWJ";
	private String NAVER_SECRET = "CL4i_Uy02M";
	private String KAKAO_ID = "e59e9d6b56e6d28fc73eab24e266b174";
	
	//회원가입처리 요청
	@ResponseBody @RequestMapping(value="/join", produces="text/html; charset=utf-8")
	public String join(MemberVO vo, MultipartFile file, HttpServletRequest request) {
		//첨부파일이 있는 경우
		if( ! file.isEmpty() ) {
			//서버에 첨부파일을 저장한다: 파일업로드
			vo.setProfile( common.fileUpload("profile", file, request) );
		}
		
		//화면에서 입력한 정보를 DB에 신규저장한 후
		//입력한 비밀번호를 암호화해서 저장
		//1.암호화에 사용할 salt 생성
		String salt = common.generateSalt();
		//2.salt 를 사용해 입력한 비번을 암호화
		String pw = common.getEncrypt(vo.getPw(), salt);
		vo.setPw( pw ); //암호화된 비번을 담는다
		vo.setSalt(salt);
		
		StringBuffer msg = new StringBuffer("<script>");
		if( service.member_join(vo) == 1) {
			//가입축한 안내문서를 첨부해 이메일로 전송하기
			String filename = request.getSession().getServletContext()
					.getRealPath("resources/files/회원가입시안내.pdf");
			common.sendWelcome(vo, filename);
			
			//가입정상처리되면 로그인되게
			request.getSession().setAttribute("loginInfo", vo);
			
			msg.append("alert('회원가입을 축하합니다^^'); location='")
					 .append( request.getContextPath() ).append("'");			
		}else {
			msg.append("alert('회원가입에 실패했습니다ㅠㅠ'); history.go(-1);");
		}
		msg.append("</script>");
		
		//응답화면연결
		return msg.toString();
	}
	
	
	//아이디 중복확인처리 요청
	@ResponseBody @RequestMapping("/id_check")
	public boolean id_check(String id) {
		//아이디가 DB에 존재하는지 확인한 후: 1:사용불가(false), 0:사용가능(true)
		//true/false 로 반환
		return service.member_id_check(id)==1 ? false : true;
	}
	
	
	//회원가입화면 요청
	@RequestMapping("/member")
	public String member(HttpSession session) {
		session.setAttribute("category", "join");
		//응답화면연결
		return "member/join";
	}
	
	
	
	//카카오로그인처리 요청
	@RequestMapping("/kakao_login")
	public String kakao_login(HttpServletRequest request) {
		//https://kauth.kakao.com/oauth/authorize?response_type=code
		//&client_id=${REST_API_KEY}
		//&redirect_uri=${REDIRECT_URI}
		StringBuffer url =
			new StringBuffer("https://kauth.kakao.com/oauth/authorize?response_type=code");
		url.append("&client_id=").append(KAKAO_ID);
		url.append("&redirect_uri=").append( appName(request) ).append("/kakaocallback");
		
		return "redirect:"+url.toString();
	}
	
	@RequestMapping("/kakaocallback")
	public String kakao_callback(String code, HttpSession session) {
		if( code==null ) return "redirect:/";
		
		//토큰 받기까지 마쳐야 카카오 로그인을 정상적으로 완료
//		curl -v -X POST "https://kauth.kakao.com/oauth/token" \
//		 -d "grant_type=authorization_code" \
//		 -d "client_id=${REST_API_KEY}" \
//		 -d "code=${AUTHORIZE_CODE}"
		StringBuffer url = 
			new StringBuffer("https://kauth.kakao.com/oauth/token?grant_type=authorization_code");
		url.append("&client_id=").append( KAKAO_ID );
		url.append("&code=").append(code);
		String response = common.requestAPI(url);
		JSONObject json = new JSONObject( response );
		String type = json.getString("token_type");
		String token = json.getString("access_token");
		
		url = new StringBuffer("https://kapi.kakao.com/v2/user/me");
		response = common.requestAPI(url, type+" "+token);
		json = new JSONObject( response );
		if( ! json.isEmpty() ) {
			//DB에 저장할 카카오 프로필 데이터를 수집
			MemberVO vo = new MemberVO();
			vo.setSocial("K");
			//long -> String
			vo.setId( String.valueOf( json.getLong("id") ) );
			
			json = json.getJSONObject("kakao_account");
			vo.setName( hasKey(json, "name", "무명씨") );	
			vo.setEmail( hasKey(json, "email") );	
			vo.setGender( hasKey(json, "gender", "male") );	//female/male
			vo.setGender( vo.getGender().equals("male") ? "남" : "여");
			vo.setPhone( hasKey(json, "phone_number") );
			
			json = json.getJSONObject("profile");
			vo.setProfile( hasKey(json, "profile_image_url") );
			
			//카카오 로그인이 정상처리되었다면 세션에 로그인정보를 담는다
			session.setAttribute("loginInfo", vo);
			
			if( service.member_id_check( vo.getId() ) == 0 ) {
				service.member_join(vo);
			}else {
				service.member_update(vo);
			}
		}
		
		return "redirect:/";
	}
	
	private String hasKey(JSONObject json, String key) {
		return json.has(key) ? json.getString(key) : "";
	}
	
	private String hasKey(JSONObject json, String key, String val) {
		key = json.has(key) ? json.getString(key) : "";
		if( key.isEmpty() ) key = val;
		return key;
	}
	
	private String hasKey(JSONObject json, String key, boolean empty, String val) {
		key = json.has(key) ? json.getString(key) : "";
		if( key.isEmpty() && empty ) {
			key = val;
		}
		return key;
	}
 	
 	//네이버로그인처리 요청
 	@RequestMapping("/naver_login")
 	public String naver_login(HttpSession session, HttpServletRequest request) {
		
 		//https://nid.naver.com/oauth2.0/authorize?response_type=code
 		//&client_id=CLIENT_ID
 		//&state=STATE_STRING
 		//&redirect_uri=CALLBACK_URL
 		
 		String state = UUID.randomUUID().toString(); //상태토큰으로 사용할 데이터
 		session.setAttribute("state", state);
 		
 		StringBuffer url =
 		new StringBuffer("https://nid.naver.com/oauth2.0/authorize?response_type=code");
 		url.append("&client_id=").append( NAVER_ID );
 		url.append("&state=").append(state);
 		url.append("&redirect_uri=").append( appName(request) ).append("/navercallback");
 		
 		return "redirect:" + url.toString();
 	}
 	
 	//네이버콜백처리
 	@RequestMapping("/navercallback")
	public String navercallback(String code, String state, HttpSession session) {
		String session_state = (String)session.getAttribute("state");
		if( code==null || !state.equals( session_state ) ) return "redirect:/";
		
		//정상처리되어 얻게되는  code 를 사용해서 
		//네이버 사용자정보를 요청하는데  사용할 토큰을 요청할 수 있다
		
		//접근 토큰 발급 요청
		//https://nid.naver.com/oauth2.0/token?grant_type=authorization_code
		//&client_id=jyvqXeaVOVmV
		//&client_secret=527300A0_COq1_XV33cf
		//&code=EIc5bFrl4RibFls1
		//&state=9kgsGTfH4j7IyAkg  
		StringBuffer url =
		new StringBuffer("https://nid.naver.com/oauth2.0/token?grant_type=authorization_code");
		url.append("&client_id=").append( NAVER_ID );
		url.append("&client_secret=").append( NAVER_SECRET );
		url.append("&code=").append(code);
		url.append("&state=").append(state);
		
		String response = common.requestAPI(url);
		JSONObject json = new JSONObject( response );
		String token = json.getString("access_token");
		String type = json.getString("token_type");
		
		//네이버 사용자정보를 요청:프로필 API 호출
		//https://openapi.naver.com/v1/nid/me
		url = new StringBuffer("https://openapi.naver.com/v1/nid/me");
		response = common.requestAPI(url, type + " "+ token);
		json = new JSONObject( response );
		
		if( json.getString("resultcode").equals("00") ) {
			
			json = json.getJSONObject("response");
			
			//네이버 프로필정보를 사이트의 DB에 관리하도록 한다
			MemberVO vo = new MemberVO();
			vo.setSocial("N");
			vo.setId( json.getString("id") );
			vo.setName( json.has("nickname") ? json.getString("nickname") : "");
			if( vo.getName().isEmpty() ) {
				vo.setName( json.has("name") ? json.getString("name") : "무명씨");
			}
			vo.setEmail( json.has("email") ? json.getString("email") : "");
			vo.setGender( json.has("gender") ? json.getString("gender") : "");
			if( vo.getGender().equals("M") ) vo.setGender("남");
			else vo.setGender("여");
			vo.setProfile( json.has("profile_image") ? json.getString("profile_image") : "");
			vo.setPhone( json.has("mobile") ? json.getString("mobile") : "" );
			
			//네이버로그인이 처음인 경우
			int member = 0;
			if( service.member_id_check( vo.getId() )==0 ) {
				member = service.member_join(vo);
			}else {
				member = service.member_update(vo);
			}
			//네이버로그인이 정상이면 세션에 로그인정보로 담는다
			session.setAttribute("loginInfo", vo);
		}
		return "redirect:/";
	}
 	
 	
 	private String appName(HttpServletRequest request) {
 		return request.getRequestURL().toString().replace(request.getServletPath(), "") ;
 	}
 	
 	
	//로그아웃처리 요청
 	@RequestMapping("/logout")
 	public String logout(HttpSession session, HttpServletRequest request) {
 		//카카오계정과 함께 로그아웃
 		MemberVO login = (MemberVO)session.getAttribute("loginInfo");
 		if( login == null ) return "redirect:/";
 		String social = login.getSocial();
 		
 		//세션에 있는 로그인정보(회원정보)를 삭제한다
 		session.removeAttribute("loginInfo");
 		
 		if( social != null && social.equals("K") ) {
 			//"https://kauth.kakao.com/oauth/logout
 			//?client_id=${YOUR_REST_API_KEY}
 			//&logout_redirect_uri=${YOUR_LOGOUT_REDIRECT_URI}"
 			StringBuffer url = 
 				new StringBuffer("https://kauth.kakao.com/oauth/logout");
 			url.append("?client_id=").append(KAKAO_ID);
 			url.append("&logout_redirect_uri=").append( appName(request) );
 			return "redirect:" + url.toString();
 		}else
 			return "redirect:/";
 	}
 	
 	//로그인처리 요청
	@ResponseBody @RequestMapping("/smart_login")
	public boolean login(String id, String pw, HttpSession session) {
		//화면에서 입력한 아이디에 대한 salt 를 사용해서 
		String salt = service.member_salt(id);
		//입력한 비밀번호를 암호화하고, 
		pw = common.getEncrypt(pw, salt);
		//화면에서 입력한 아이디/비번가 일치하는 회원정보를 DB에서 조회해와
		MemberVO vo = service.member_login(id, pw);
		//로그인되게 처리: 세션에 회원정보를 담는다
		session.setAttribute("loginInfo", vo);
		return vo == null ? false : true;
	}
	
	//비밀번호 변경화면 요청
	@RequestMapping("/password")
	public String password(HttpSession session) {
		session.setAttribute("category", "password");
		//로그인 된 경우 비밀번호 변경화면으로 연결
		//로그인 안 된 경우 로그인 화면으로 연결
		MemberVO vo = (MemberVO)session.getAttribute("loginInfo");
		//응답화면연결
		if( vo==null )
			return "redirect:login";
		else
			return "member/password";
	}
	
	
	//비밀번호 변경처리 요청
	@RequestMapping("/change")
	public String change(HttpSession session, String pw) {
		//로그인 사용자의 salt 를 사용해서 새로 입력한 비밀번호를 암호화한다
		MemberVO vo = (MemberVO)session.getAttribute("loginInfo");
		if( vo==null ) return "redirect:login";
		
		pw = common.getEncrypt( pw, vo.getSalt() ); //암호화된 새 비번
		vo.setPw(pw);
		
		//화면에서 입력한 비밀번호를 DB에 변경저장한 후
		service.member_salt_pw(vo);
		
		//세션의 로그인정보를 변경된 정보로 바꿔 담는다
		session.setAttribute("loginInfo", vo);
		
		//응답화면 연결
		return "redirect:/";
	}
	
	
	//비밀번호 재발급(임시비번발급)처리 요청
	@ResponseBody @RequestMapping(value="/reset"
						, produces="text/html; charset=utf-8")
	public String reset(MemberVO vo) {
		//해당 아이디에 대해 임시비밀번호를 만들어 이메일로 전송한다
		String pw = UUID.randomUUID().toString(); //adflh4e9afa-lhalfa-afadfshgsha
		pw = pw.substring( pw.lastIndexOf("-") + 1 ); //afadfshgsha
		
		//암호화에 사용할 salt생성
		String salt = common.generateSalt();
		vo.setPw( common.getEncrypt(pw, salt) ); //암호화된 비번 afjklhaflhlaurheogrbafdlaf
		vo.setSalt(salt);
		
		//이메일로 비번전송했음을 화면에 띄운다
		StringBuffer email = new StringBuffer("<script>");
		//임시 비번을 DB에 저장한 후
		if( service.member_salt_pw(vo)==1 ) {
			//이메일로 전송한다.
			common.sendPassword(vo, pw);
			email.append("alert('임시 비밀번호가 이메일로 발송되었습니다\\n이메일을 확인하세요'); ");
			email.append("location='login'; ");
		}else {
			email.append("alert('임시 비밀번호 발급 실패ㅠㅠ');");
		}
		email.append("</script>");
		return email.toString();
	}
	
	//비밀번호찾기화면 요청
	@RequestMapping("/find")
	public String find(HttpSession session) {
		session.setAttribute("category", "find");
		//응답화면 연결
		return "default/member/find";
	}
	
	
	//로그인화면 요청
	@RequestMapping("/login")
	public String login(HttpSession session) {
		session.setAttribute("category", "login");
		
		//응답화면연결
		return "default/member/login";
	}
	
}

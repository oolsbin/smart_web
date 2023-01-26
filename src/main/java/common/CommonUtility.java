package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import member.MemberVO;

@Service
public class CommonUtility {
	
	//첨부되어진 물리적파일 삭제 
	public void attachedFile_delete(String filepath, HttpServletRequest request) {
		if( filepath != null ) {
			//DB: http://localhost/smart/upload/notice/2022/10/21/80984d3_kakao_login.zip
			//실제: D:\\app\\smart\\upload\\notice\\2022\\10\\21\\abc.txt
			filepath = filepath.replace( appURL(request)
										, "d://app/" + request.getContextPath() );
			File file = new File( filepath );
			if( file.exists() ) file.delete();
		}
	}
	
	//파일다운로드
	public boolean fileDownload(String filename, String filepath
							, HttpServletRequest request
							, HttpServletResponse response) throws Exception{
		//http://localhost/smart/upload/notice/2022/10/21/80984d3_kakao_login.zip
		//D:\\app\\smart\\upload\\notice\\2022\\10\\21\\abc.txt
		filepath = filepath.replace(
						appURL(request), "d://app/" + request.getContextPath() );
		
		//다운로드할 파일객체 생성		
		File file = new File( filepath );
		if( ! file.exists() ) return false;
		
		//text/html, image/png,  xlxs, .....
		String mime 
			= request.getSession().getServletContext().getMimeType(filepath);
		response.setContentType(mime);		
		
		filename = URLEncoder.encode( filename, "utf-8" );
		filename = filename.replaceAll("\\+", "%20");//.replaceAll("?", "%36");
		
		response.setHeader("content-disposition"
					, "attachment; filename="+ filename); //첨부되어진 파일을 내려받도록 처리
		
		ServletOutputStream out = response.getOutputStream();
		FileCopyUtils.copy( new FileInputStream(file), out );
		out.flush();
		return true;
	}
	
	
	//파일업로드
	public String fileUpload(String category, MultipartFile file
							, HttpServletRequest request) {
		//upload/profile/2022/10/20/abc.txt
		//upload/notice/2022/10/20/abc.png
		//업로드 위치: D:\Study_Spring\Workspace\.meta....pps\smart\resources
		String path 
//		= request.getSession().getServletContext().getRealPath("resources");
			= "d://app" + request.getContextPath();
		
		// /upload/profile/2022/10/20
		String upload = "/upload/" + category 
				+ new SimpleDateFormat("/yyyy/MM/dd").format(new Date());
		
		//D:\Study_Spring\Wor...pps\smart\resources/upload/profile/2022/10/20
		path += upload; 
		
		//폴더가 있는지 확인해서 없으면 폴더를 생성한다
		File folder = new File( path );
		if( ! folder.exists() ) folder.mkdirs();
		
		//업로드하는 파일명을 고유한 아이디를 붙여 저장한다: ajlh2348-ahflhq_abc.txt
		String filename 
			= UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		
		try {
			file.transferTo( new File(path, filename) );
		} catch (Exception e) {	}
		
		// /upload/profile/2022/10/20/ajlh2348-ahflhq_abc.txt
		// -> http://localhost/smart/upload/profile/2022/10/20/ajlh2348-ahflhq_abc.txt
		return appURL(request) + upload + "/" + filename;
	}
	
	public String appURL(HttpServletRequest request) {
		//http://localhost/smart/join : URL
		// /join : servletPath
		// -> http://localhost/smart
		return request.getRequestURL().toString().replace( request.getServletPath(), "");
	}
	
	
	//회원가입축하 이메일전송처리
	public void sendWelcome(MemberVO vo, String attach) {
		HtmlEmail mail = new HtmlEmail();
		mail.setCharset("utf-8");
		mail.setDebug(true);
		mail.setHostName("smtp.naver.com");
		
		mail.setAuthentication("아이디", "비번");
		mail.setSSLOnConnect(true);
		
		try {
			mail.setFrom("아이디@naver.com", "스마트 웹&앱 관리자");
			mail.addTo(vo.getEmail(), vo.getName());
			
			mail.setSubject("한울 스마트 웹&앱 과정 가입 축하");
			
			StringBuffer content = new StringBuffer();
			content.append("<html>");
			content.append("<body>");
			content.append("<h3><a target='_blank' href='http://hanuledu.co.kr/'>한울 과정</a></h3>");
			content.append("<div>한울 스마트 웹&앱 과정 가입을 축하합니다.</div>");
			content.append("<div>프로젝트까지 마무리하시고 취업에 성공하시길 바랍니다.</div>");
			content.append("<div>첨부된 파일을 확인하신후 등교하세요</div>");
			content.append("</body>");
			content.append("</html>");
			mail.setHtmlMsg( content.toString() );
			
			EmailAttachment file = new EmailAttachment();
			file.setPath( attach );
			mail.attach(file);
			
			mail.send();
		}catch(Exception e) {
		}
	}
	
	
	//임시비밀번호 이메일전송처리
	public void sendPassword(MemberVO vo, String pw) {
		HtmlEmail email = new HtmlEmail();
		email.setHostName("smtp.naver.com"); //이메일 전송: 관리자
		email.setDebug(true);
		email.setCharset("utf-8");
		
		email.setAuthentication("아이디", "이메일비번"); //관리자 이메일 로그인
		email.setSSLOnConnect(true); //로그인버튼 클릭
		
		try{
			email.setFrom("아이디@naver.com", "스마트 웹&앱 관리자");
			email.addTo(vo.getEmail(), vo.getName());  //받는사람
			email.setSubject("임시 비밀번호 발급");
			
			StringBuffer content = new StringBuffer();
			content.append("<html>");
			content.append("<body>");
			content.append("<h3>").append( vo.getName() )
										.append("님 임시 비밀번호 발급</h3>");
			content.append("<div>아이디: <strong>").append( vo.getId() )
										.append("</strong></div>");			
			content.append("<div>임시 비밀번호: <strong>").append( pw )
										.append("</strong></div>");			
			content.append("</body>");
			content.append("</html>");
			
			email.setHtmlMsg(content.toString());
			email.send(); //보내기버튼클릭
		
		}catch(Exception e) {
		}
	}
	
	
	
	
	
	//비밀번호를 salt를 사용해 암호화처리(암호화:encrypt, 복호화:decrypt)
	public String getEncrypt(String pw, String salt) {
		pw += salt; //abc afahlkjhlaf -> abcafahlkjhlaf
		//암호화 처리 메소드를 갖는 클래스: MessageDigest

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");//암호화방식지정
			md.update( pw.getBytes() );
			byte[] bytes = md.digest();
			
			//16진수로 변환하여 하나의 문자열로 만든다
			StringBuffer encrypt = new StringBuffer();
			for( byte b : bytes ) {
				encrypt.append( String.format("%02x", b)  );
			}
			pw = encrypt.toString();
			
		} catch (NoSuchAlgorithmException e) {
		} 
		return pw;
	}
	
	
	//암호화에 사용할 salt 
	public String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[ ] bytes = new byte[24];
		random.nextBytes(bytes);
		
		//16진수로 변환 한 24개의 바이트 정보를 하나의 문자열로 만든다
		StringBuffer salt = new StringBuffer();
		for(byte b : bytes) {
			salt.append( String.format("%02x", b) ); //16진수: %x, %o -> 02141551			
		}
		return salt.toString();
	}
	
	
	public Map<String, Object> requestAPItoMap( StringBuffer apiURL ) {
		int count = 0; 
		JSONObject json = new JSONObject( requestAPI( apiURL ) );
		json = json.getJSONObject( "response" ); //header, body 
		int code = Integer.parseInt(
				 json.getJSONObject("header").getString("resultCode"));
		if( code==0 ) {
			json = json.getJSONObject( "body" ); // items,numOfRows,pageNo,totalCount
			if( json.has("totalCount") ) count = json.getInt( "totalCount" );
			json = json.getJSONObject( "items" ); //item 이 10개
		}else {
			json.put("item", new JSONObject());
		}
		json.put("count", count);
		return json.toMap();
	}
	
	
	
	public String requestAPI( StringBuffer apiURL ) {
		String response = "";
		try {
		      URL url = new URL(apiURL.toString());
		      HttpURLConnection con = (HttpURLConnection)url.openConnection();
		      con.setRequestMethod("GET");
		      int responseCode = con.getResponseCode();
		      BufferedReader br;
		      System.out.print("responseCode="+responseCode);
		      if(responseCode==200) { // 정상 호출
		        br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
		      } else {  // 에러 발생
		        br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
		      }
		      String inputLine;
		      StringBuffer res = new StringBuffer();
		      while ((inputLine = br.readLine()) != null) {
		        res.append(inputLine);
		      }
		      br.close();
		      response = res.toString();
		    } catch (Exception e) {
		      System.out.println(e);
		    }
			return response;
	}
	
	public String requestAPI( StringBuffer apiURL, String header ) {
		String response = "";
		try {
			URL url = new URL(apiURL.toString());
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", header);
			
			int responseCode = con.getResponseCode();
			BufferedReader br;
			System.out.print("responseCode="+responseCode);
			if(responseCode==200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
			} else {  // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
			}
			String inputLine;
			StringBuffer res = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				res.append(inputLine);
			}
			br.close();
			response = res.toString();
		} catch (Exception e) {
			System.out.println(e);
		}
		return response;
	}
}

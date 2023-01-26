<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix='c'%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel='stylesheet' type='text/css' href='css/member.css?<%=new java.util.Date()%>'>
<style>
#naver { background: url('img/naverlogin.png') no-repeat center; background-size: cover; }
#kakao { background: url('img/kakaologin.png') no-repeat center; background-size: cover }
</style>
</head>
<body>
<div class='center'>
	<a href='<c:url value="/" />'><img src='img/hanul.logo.png'></a>
	<div class='box'>
	<ul>
		<li><input type='text' id='id' class='chk' placeholder="아이디"></li>
		<li><input type='password' id='pw' class='chk' placeholder="비밀번호"></li>
		<li><input type='button' value='로그인' id='login'></li>
		<li><hr></li>
		<li><input type='button' id='naver' onclick='location="naver_login"'></li>
		<li><input type='button' id='kakao' onclick='location="kakao_login"'></li>
	</ul>
	</div>
	<div><a href='find'>비밀번호 찾기</a></div>
</div>
<script>
$('#pw').keyup(function(e){
	if( e.keyCode==13 ) login();
});

function login(){
	if( emptyCheck() )	{
		
		$.ajax({
			url: 'smart_login',
			data: { id:$('#id').val(), pw:$('#pw').val() },
			success: function( response ){
				if( response )
					location = '<c:url value="/"/>';
				else{
					alert('아이디나 비밀번호가 일치하지 않습니다!');
					$('#id').focus();
				}
				
			},error: function(req, text){
				alert(text+ ':' + req.status);
			}
		});
	}
}

$('#login').click(function(){
	login();
});
</script>
</body>
</html>









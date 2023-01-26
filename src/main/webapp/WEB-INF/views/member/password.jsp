<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel='stylesheet' type='text/css' href='css/member.css'>
</head>
<body>
<div class='box'>
<form method='post' action='change'>
<ul>
	<li><input type='password' name='pw' placeholder="새 비밀번호">
		<div class='valid text-left'>비밀번호 입력(영문대/문자,숫자 모두 포함)</div>
	</li>
	<li><input type='password' name='pw_ck' placeholder="새 비밀번호 확인">
		<div class='valid text-left'>비밀번호를 다시 입력하세요</div>
	</li>
	<li><input type='reset' value='다시입력'></li>
	<li><input type='button' value='비밀번호 변경'></li>
</ul>
</form>
</div>
<script src='js/member.js?<%=new java.util.Date()%>'></script>
<script>
$('[type=button]').on('click', function(){
	if( tagIsValid() )	$('form').submit();	
});

function tagIsValid(){
	var ok = true;
	$('[type=password]').each(function(){
		var status = member.tag_status( $(this) );
		if( status.code=='invalid' ){
			alert( '비밀번호 변경 불가!\n' + status.desc );
			$(this).focus();
			ok = false;
			return ok;
		}
		
	});
	
	return ok;
}

$('[type=password]').keyup(function(){
	var status = member.tag_status( $(this) );
	$(this).siblings('div').removeClass('valid invalid')
							.addClass( status.code ).text( status.desc );
});
</script>

</body>
</html>
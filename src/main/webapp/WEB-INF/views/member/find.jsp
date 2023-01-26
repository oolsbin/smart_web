<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix='c'%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel='stylesheet' type='text/css' href='css/member.css'>
</head>
<body>

<div class='center'>
	<a href='<c:url value="/"/>'><img src='img/hanul.logo.png'></a>
	<form method='post' action='reset'>
	<div class='box'>
	<ul>
		<li><input type='text' name='id' class='chk' placeholder="아이디"></li>
		<li><input type='text' name='name' class='chk' placeholder="성명"></li>
		<li><input type='text' name='email' class='chk' placeholder="이메일"></li>
		<li><input type='reset' value='다시입력'></li>
		<li><input type='button' value='확인' id='find'></li>
	</ul>
	</div>
	</form>
</div>

<script>
// $('#find').click(function(){
// });
$('#find').on('click', function(){
	if(  emptyCheck()  ) {
		$('form').submit();
	}
});
</script>

</body>
</html>









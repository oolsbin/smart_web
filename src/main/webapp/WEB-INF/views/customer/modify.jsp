<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix='c'%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<h3>고객정보수정</h3>
<form method='post' action='update.cu'>
<input type='hidden' name='id' value='${vo.id}'>
<table class='w-px600'>
<tr><th class='w-px160'>고객명</th>
	<td><input type='text' name='name' value='${vo.name}'></td>
</tr>
<tr><th>성별</th>
	<td>
		<label><input type='radio' name='gender' value='남' ${vo.gender eq '남' ? 'checked' : ''}>남</label>
		<label><input type='radio' name='gender' value='여' <c:if test='${vo.gender eq "여"}'>checked</c:if> >여</label>
	</td>
</tr>
<tr><th>전화번호</th>
	<td><input type='text' name='phone' value='${vo.phone}'></td>
</tr>
<tr><th>주소</th>
	<td><input type='text' name='addr' value='${vo.addr}'></td>
</tr>
</table>
</form>
<div class='btnSet'>
	<a class='btn-fill' href="javascript:$('form').submit()">저장</a>
<!-- 	<a class='btn-fill' onclick="$('form').submit()">저장</a> -->
	<a class='btn-empty' href='detail.cu?id=${vo.id}'>취소</a>
</div>


</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt'%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3>사원정보</h3>
<table class='w-px600'>
<tr><th class='w-px160'>사번</th>
	<td>${vo.employee_id }</td>
</tr>
<tr><th>사원명</th>
	<td>${vo.name }</td>
</tr>
<tr><th>이메일</th>
	<td>${vo.email }</td>
</tr>
<tr><th>전화번호</th>
	<td>${vo.phone_number }</td>
</tr>
<tr><th>부서명</th>
	<td>${vo.department_name }</td>
</tr>
<tr><th>업무제목</th>
	<td>${vo.job_title }</td>
</tr>
<tr><th>매니저명</th>
	<td>${vo.manager_name }</td>
</tr>
<tr><th>입사일자</th>
	<td>${vo.hire_date }</td>
</tr>
<tr><th>급여</th>
	<td><fmt:formatNumber value='${vo.salary }'/> </td>
</tr>
</table>
<div class='btnSet'>
	<a class='btn-fill' href='modify.hr?id=${vo.employee_id}'>정보수정</a>
	<a class='btn-fill' 
	onclick="if( confirm('사번[${vo.employee_id}] 삭제?') ) href='delete.hr?id=${vo.employee_id}' " >정보삭제</a>
	<a class='btn-fill' href='list.hr'>사원목록</a>
</div>
</body>
</html>








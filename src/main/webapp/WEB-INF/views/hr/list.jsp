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
<h3>사원목록</h3>

<form method='post' action='list.hr'>
<div id='list-top' class='w-px1200'>
<ul><li>부서명</li>
	<li><select name='department_id' class='w-px200' onchange='$("form").submit()'>
		<option value='-1'>전체 부서</option>
		<c:forEach items='${departments}' var='d'>
			<option ${department_id eq d.department_id ? 'selected' : ''} 
			value='${d.department_id}'>${d.department_name}</option>
		</c:forEach>
		</select>
	</li>
</ul>
<ul><li><a class='btn-fill' href='new.hr'>신규사원등록</a></li>
</ul>
</div>
</form>

<div class='tb-wrap w-px1200'>
<table class='tb-list'>
<colgroup>
	<col width='80px'>
	<col width='250px'>
	<col width='300px'>
	<col>
	<col width='120px'>
</colgroup>
<tr><th>사번</th><th>사원명</th><th>부서명</th><th>업무제목</th><th>입사일자</th></tr>
<c:forEach items='${list}' var='vo'>
<tr><td>${vo.employee_id}</td>
	<td><a href='info.hr?id=${vo.employee_id}'>${vo.last_name} ${vo.first_name}</a></td>
	<td>${vo.department_name}</td>
	<td>${vo.job_title}</td>
	<td>${vo.hire_date}</td>
</tr>
</c:forEach>
</table>
</div>

</body>
</html>












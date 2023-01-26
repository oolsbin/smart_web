<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix='c'%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
input[name=last_name], input[name=first_name] { width: 140px }
table td { text-align: left }
</style>
</head>
<body>
<h3>사원정보수정</h3>
<form method='post' action='update.hr'>
<input type='hidden' name='employee_id' value='${vo.employee_id }'>
<table class='w-px600'>
<tr><th class='w-px160'>사번</th>
	<td>${vo.employee_id }</td>
</tr>
<tr><th>사원명</th>
	<td><input type='text' name='last_name' value='${vo.last_name }'>
		<input type='text' name='first_name' value='${vo.first_name }'>
	</td>
</tr>
<tr><th>이메일</th>
	<td><input type='text' name='email' value='${vo.email }'></td>
</tr>
<tr><th>전화번호</th>
	<td><input type='text' name='phone_number' value='${vo.phone_number }'></td>
</tr>
<tr><th>부서명</th>
	<td>
		<select name='department_id' style='width:217px'>
			<option value='-1'>부서 선택</option>
			<c:forEach items='${departments}' var='d'>
			<option  ${vo.department_name eq d.department_name ? 'selected' : ''} 
					value='${d.department_id}'>${d.department_name}</option>
			</c:forEach>			
		</select>
	</td>
</tr>
<tr><th>업무제목</th>
	<td>
		<select name='job_id'>
			<c:forEach items='${jobs}' var='j'>
			<option ${j.job_id eq vo.job_id ? 'selected' : ''} 
				value='${j.job_id }'>${j.job_title}</option>
			</c:forEach>
		</select>
	</td>
</tr>
<tr><th>매니저</th>
	<td>
		<select name='manager_id'>
			<option value='-1'>매니저선택</option>
			<c:forEach items='${managers}' var='m'>
			<option ${m.employee_id eq vo.manager_id ? 'selected' : ''} 
				value='${m.employee_id }'>${m.name}</option>
			</c:forEach>
		</select>
	</td>
</tr>
<tr><th>입사일자</th>
	<td><input type='text' class='date' name='hire_date' value='${vo.hire_date }'></td>
</tr>
<tr><th>급여</th>
	<td><input type='text' name='salary' value='${vo.salary }'></td>
</tr>
</table>
</form>

<div class='btnSet'>
	<a class='btn-fill' onclick='$("form").submit()'>저장</a>
	<a class='btn-empty' href='info.hr?id=${vo.employee_id}'>취소</a>
</div>
<script>
$(function(){
	$('.date').datepicker();
	
});
</script>

</body>
</html>
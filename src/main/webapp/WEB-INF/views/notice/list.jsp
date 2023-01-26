<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3>공지사항</h3>
<form method='post' action='list.no'>
<div id='list-top' class='w-px1200'>
	<ul>
		<li><select class='w-px100' name='search'>
			<option value='all' ${page.search eq 'all' ? 'selected': ''}>전체</option>
			<option value='title' <c:if test='${page.search eq "title"}'>selected</c:if>  >제목</option>
			<option value='content' ${page.search eq 'content' ? 'selected': ''}>내용</option>
			<option value='writer' ${page.search eq 'writer' ? 'selected': ''}>작성자</option>
			</select>
		</li>
		<li><input type='text' value='${page.keyword}' name='keyword' class='w-px300'></li>
		<li><a class='btn-fill' onclick='$("form").submit()'>검색</a></li>
	</ul>
	<ul><!-- 관리자인 경우만 글쓰기 가능 -->
		<c:if test='${loginInfo.admin eq "Y"}'>
		<li><a class='btn-fill' href='new.no'>글쓰기</a></li>
		</c:if>
	</ul>
</div>
<input type='hidden' name='curPage' value='1'>
</form>

<table class='w-px1200 tb-list'>
<colgroup>
	<col width='100px'>
	<col>
	<col width='140px'>
	<col width='140px'>
	<col width='100px'>
</colgroup>
<tr><th>번호</th>
	<th>제목</th>
	<th>작성자</th>
	<th>작성일자</th>
	<th>첨부</th>
</tr>
<c:forEach items='${page.list}' var='vo'>
<tr><td>${vo.no}</td>
	<td class='text-left'><span style='margin-right:${15*vo.indent}px'></span>
		<c:forEach var='i' begin='1' end='${vo.indent}'>
		${i eq vo.indent ? '<i class="fa-regular fa-comment-dots"></i>' : ''}
		</c:forEach>
		<a href='info.no?id=${vo.id}&curPage=${page.curPage}&search=${page.search}&keyword=${page.keyword}'>${vo.title}</a></td>
	<td>${vo.name}</td>
	<td>${vo.writedate}</td>
	<td>${empty vo.filename ? '' : '<i class="font-c fa-solid fa-paperclip"></i>'}</td>
</tr>
</c:forEach>
</table>
<div class='btnSet'>
	<jsp:include page="/WEB-INF/views/include/page.jsp"/>
</div>

<script>
function page( no ){
	$('[name=curPage]').val( no );
	$('form').submit();
}
</script>
</body>
</html>
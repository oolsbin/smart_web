<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3>방명록 목록</h3>

<form method='post' action='list.bo'>
<div id='list-top' class='w-px1200'>
<ul>
	<li><select name='search' class='w-px100'>
		<option value='s1' ${page.search eq 's1' ? 'selected' : ''}>전체</option>
		<option value='s2' ${page.search eq 's2' ? 'selected' : ''}>제목</option>
		<option value='s3' ${page.search eq 's3' ? 'selected' : ''}>내용</option>
		<option value='s4' ${page.search eq 's4' ? 'selected' : ''}>작성자</option>
		<option value='s5' ${page.search eq 's5' ? 'selected' : ''}>제목+내용</option>
		</select>
	</li>
	<li><input type='text' name='keyword' value='${page.keyword}' class='w-px300'>	</li>
	<li><a class='btn-fill' onclick='$("form").submit()'>검색</a></li>
</ul>
<ul>
	<li><select class='w-px100' name='pageList'>
		<c:forEach var='i' begin='1' end='6'> 
		<option value='${5*i}'>${5*i}개씩</option>
		</c:forEach>
		</select>
	</li>	
	<li><select class='w-px100' name='viewType'>
		<option value='list'>리스트</option>
		<option value='grid'>그리드</option>
		</select>
	</li>
	<!-- 로그인한 경우 글쓰기 가능 -->
	<c:if test='${not empty loginInfo}'>
	<li><a class='btn-fill' href='new.bo'>글쓰기</a></li>
	</c:if>
</ul>
</div>
<input type='hidden' name='curPage' value='1'>
<input type='hidden' name='id'>
</form>

<c:if test='${page.viewType eq "grid"}'>
<ul class='grid w-px1200'>
	<c:forEach items='${page.list}' var='vo'>
	<li><div><a onclick='info(${vo.id})'>${vo.title}</a></div>
		<div>${empty vo.name ? '무명씨' : vo.name}</div>
		<div>${vo.writedate}
			<span style='float:right;'>${vo.filecnt gt 0 ? '<i class="font-b fa-solid fa-paperclip"></i>' : ''}</span>
		</div>
	</li>
	</c:forEach>
</ul>
</c:if>

<c:if test='${page.viewType eq "list"}'>
<table class='tb-list w-px1200'>
<colgroup>
	<col width='100px'>
	<col>
	<col width='140px'>
	<col width='140px'>
</colgroup>
<tr><th>번호</th>
	<th>제목</th>
	<th>작성자</th>
	<th>작성일자</th>
</tr>
<c:forEach items='${page.list}' var='vo'>
<tr><td>${vo.no}</td>
	<td class='text-left'><a onclick='info(${vo.id})'>${vo.title}</a>
		<c:if test='${vo.filecnt gt 0}'>
			<i class="font-b fa-solid fa-paperclip"></i>
		</c:if>
	</td>
	<td>${vo.name}</td>
	<td>${vo.writedate}</td>
</tr>
</c:forEach>
</table>
</c:if>


<div class='btnSet'>
	<jsp:include page="/WEB-INF/views/include/page.jsp"/>
</div>

<script>
function info(id){
	$('[name=id]').val( id );
	$('[name=curPage]').val( ${page.curPage} );
	$('form').attr('action', 'info.bo');
	$('form').submit();
}

$('[name=pageList]').val( ${page.pageList} ).prop('selected', true);
$('[name=viewType]').val( '${page.viewType}' ).prop('selected', true);

$('[name=pageList], [name=viewType]').change(function(){
	if( $(this).attr('name')=='viewType' )  
		$('[name=curPage]').val( ${page.curPage} );
	
	$('form').submit();
});

function page( no ){
	$('[name=curPage]').val( no );
	$('form').submit();
}
</script>
</body>
</html>










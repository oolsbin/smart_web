<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/functions' prefix='fn'%>
<c:set var='hrefParam' value='curPage=${page.curPage}&search=${page.search}&keyword=${page.keyword}' />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
table td { text-align: left }
</style>
</head>
<body>
<h3>공지글 안내</h3>
<table class='w-px1200'>
<colgroup>
	<col width='140px'>
	<col>
	<col width='140px'>
	<col width='140px'>
	<col width='100px'>
	<col width='100px'>
</colgroup>
<tr><th>제목</th>
	<td colspan='5'>${vo.title}</td>
</tr>
<tr><th>작성자</th>
	<td>${vo.name}</td>
	<th>작성일자</th>
	<td>${vo.writedate}</td>
	<th>조회수</th>
	<td>${vo.readcnt}</td>
</tr>
<tr><th>내용</th>
	<td colspan='5'>${fn: replace(vo.content, crlf, '<br>') }</td>
</tr>
<tr><th>첨부파일</th>
	<td colspan='5'>
	<div class='align'>
		<span id='file-name'>${vo.filename}</span>
		<c:if test='${not empty vo.filename }'>
		<a id='download' ><i class="font-b fa-solid fa-file-arrow-down"></i></a>
		</c:if>	
	</div>
	</td>
</tr>
</table>
<div class='btnSet'>
	<a href='list.no?${hrefParam}' class='btn-fill'>목록으로</a>
	<!-- 관리자로 로그인된 경우만 수정/삭제 가능 -->
	<c:if test='${loginInfo.admin eq "Y"}'>
	<a class='btn-fill' 
		href='modify.no?id=${vo.id}&${hrefParam}'>정보수정</a>
	<a class='btn-fill' id='remove'>정보삭제</a>
	</c:if>
	<!-- 로그인한 경우 답글쓰기 가능 -->
	<c:if test='${not empty loginInfo}'>
	<a class='btn-fill' href='reply.no?id=${vo.id}&${hrefParam}'>답글쓰기</a>
	</c:if>
</div>

<!-- 
QnA : Q: 로그인한 사용자
 	  A: 관리자
 -->

<script>
$('#download').click(function(){
	$(this).attr('href'
			, 'download.no?id=${vo.id}&url=' + $(location).attr('href'));
});
if( isImage( "${vo.filename}" ) ){
	$('#file-name').after( '<span id="preview"><img src="${vo.filepath}"></span>' );
}
$('#remove').click(function(){
	if( confirm('김강윤 할까요?') ) {
		location = 'delete.no?id=${vo.id}&${hrefParam}';
	}
});
</script>
</body>
</html>











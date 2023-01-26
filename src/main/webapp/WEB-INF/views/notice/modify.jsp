<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/functions' prefix='fn'%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3>공지글수정</h3>
<form method='post' action='update.no' enctype='multipart/form-data'>
<table class='w-px1000'>
<tr><th class='w-px140'>제목</th>
	<td><input type='text' value='${fn: escapeXml(vo.title)}' name='title' class='full chk' title='제목'></td>
</tr>
<tr><th>내용</th>
	<td><textarea name='content' class='full chk' title='내용'>${vo.content}</textarea></td>
</tr>
<tr><th>첨부파일</th>
	<td class='text-left'>
		<div class='align'>
		<label>
			<input type='file' name='file' id='attach-file'>
			<a><i class="font-b fa-solid fa-file-circle-plus"></i></a>
		</label>
		<span id='file-name'>${vo.filename}</span>
		<span id='preview'></span>
		<!-- 첨부파일이 있는 경우는 삭제 이미지가 보이게 처리 -->
		<a id='delete-file' style='display:${empty vo.filename ? "none" : "inline"}'><i class="font-r fa-solid fa-trash-can"></i></a>
		</div>
	</td>
</tr>
</table>
<input type='hidden' name='id' value='${vo.id}'>
<input type='hidden' name='filename' >
<input type='hidden' name='curPage' value='${page.curPage}'>
<input type='hidden' name='search' value='${page.search}'>
<input type='hidden' name='keyword' value='${page.keyword}'>
</form>
<div class='btnSet'>
	<a class='btn-fill' id='save'>저장</a>
	<a class='btn-empty' href='list.no?curPage=${page.curPage}&search=${page.search}&keyword=${page.keyword}'>취소</a>
</div>
<script>

$('#save').click(function(){
	$('[name=filename]').val( $('#file-name').text() );
	if( emptyCheck() ) $('form').submit();
});
</script>
</body>
</html>








<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3>공지글 답글쓰기</h3>
<form method='post' action='reply_insert.no' enctype='multipart/form-data'>
<table class='w-px1000'>
<tr><th class='w-px140'>제목</th>
	<td><input type='text' name='title' class='full chk' title='제목'></td>
</tr>
<tr><th>내용</th>
	<td><textarea name='content' class='full chk' title='내용'></textarea></td>
</tr>
<tr><th>첨부파일</th>
	<td class='text-left'>
		<div class='align'>
		<label>
			<input type='file' name='file' id='attach-file'>
			<a><i class="font-b fa-solid fa-file-circle-plus"></i></a>
		</label>
		<span id='file-name'></span>
		<span id='preview'></span>
		<a id='delete-file'><i class="font-r fa-solid fa-trash-can"></i></a>
		</div>
	</td>
</tr>
</table>
<input type='hidden' name='writer' value='${loginInfo.id}'>
<!-- 원글의 정보 -->
<input type='hidden' name='root' value='${vo.root}'>
<input type='hidden' name='step' value='${vo.step}'>
<input type='hidden' name='indent' value='${vo.indent}'>

<!-- 페이지 정보 -->
<input type='hidden' name='curPage' value='${page.curPage}'>
<input type='hidden' name='search' value='${page.search}'>
<input type='hidden' name='keyword' value='${page.keyword}'>

</form>
<div class='btnSet'>
	<a class='btn-fill' id='save'>저장</a>
	<a class='btn-empty' href='info.no?id=${vo.id}&curPage=${page.curPage}&search=${page.search}&keyword=${page.keyword}'>취소</a>
</div>
<script>
$('#save').click(function(){
	if( emptyCheck() ) $('form').submit();
});
</script>
</body>
</html>








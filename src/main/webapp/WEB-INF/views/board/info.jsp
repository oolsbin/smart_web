<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/functions' prefix='fn'%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
table td { text-align: left }
#comment-regist, #comment-list { width:600px; margin: 0 auto; text-align: left }
#comment-regist div { display: flex; justify-content: space-between;}
#comment { height: 60px; margin-top: 5px; }
#comment-list span { float: right;}
.modify { display: none; width:calc(100% - 2px); height:50px; margin-top:3px; padding:0 }
.view { margin-top:3px; }
</style>
</head>
<body>
<h3>방명록 안내</h3>
<table class='w-px1200'>
<colgroup>
	<col width='140px'>
	<col>
	<col width='140px'>
	<col width='140px'>
	<col width='120px'>
	<col width='120px'>
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
	<td colspan='5'>${fn: replace(vo.content, crlf, '<br>')}</td>
</tr>
<tr><th>첨부파일</th>
	<td colspan='5'>
	<c:forEach items='${vo.fileList}' var='file'>
	<div class='align'>
		<span>${file.filename}</span>
		<span class='preview'></span>
		<a class='download' data-file='${file.id}'><i class="font-b fa-solid fa-file-arrow-down"></i></a>		
	</div>
	</c:forEach>
	</td>
</tr>

</table>
<div class='btnSet'>
	<a class='btn-fill' id='list'>목록으로</a>	
	<!-- 로그인한 사용자가 작성한 글인 경우만 수정/삭제 가능 -->
	<c:if test='${loginInfo.id eq vo.writer}'>
	<a class='btn-fill' id='modify'>정보수정</a>
	<a class='btn-fill' id='remove'>정보삭제</a>
	</c:if>
</div>

<div id='comment-regist'>
	<div><span>댓글작성</span>
		<a class='btn-fill-s' id='regist'>댓글등록</a>		 
	</div>
	<textarea id='comment' class='full'></textarea>
</div>
<div id='comment-list'></div>

<div id='popup-background'></div>
<div id='popup' class='center'></div>

<form method='post'>
<input type='hidden' name='file'>
<input type='hidden' name='id' value='${vo.id}'>
<input type='hidden' name='curPage' value='${page.curPage}'>
<input type='hidden' name='search' value='${page.search}'>
<input type='hidden' name='keyword' value='${page.keyword}'>
<input type='hidden' name='pageList' value='${page.pageList}'>
<input type='hidden' name='viewType' value='${page.viewType}'>
</form>

<script>
//댓글등록처리
$('#regist').click(function(){
	if( ${empty loginInfo} ){
		alert('댓글을 등록하려면 로그인하세요!');
	}else if( $('#comment').val()=='' ){
		alert('댓글을 입력하세요!');
		$('#comment').focus();
	}else{
		$.ajax({
			url: 'board/comment/insert',
			data: { board_id:${vo.id}, content:$('#comment').val()
					, writer:'${loginInfo.id}' },
			success: function( response ){
				if( response ){
					alert('댓글이 등록되었습니다');
					$('#comment').val('');
					comment_list();
				}else{
					alert('댓글이 등록 실패ㅠㅠ')
				}
			},error: function(req, text){
				alert(text+':'+req.status);
			}
		});
	}
});

comment_list();

//댓글목록조회
function comment_list(){
	$.ajax({
		url: 'board/comment/list/${vo.id}',
		//data: { board_id:${vo.id} },
		success: function( response ){
			$('#comment-list').html( response );
		},error: function(req, text){
			alert(text+':'+req.status);
		}
	});
}

//목록으로, 수정, 삭제 버튼 클릭시 form 서브밋
$('#list, #modify, #remove').click(function(){
	$('form').attr('action', $(this).attr('id')+".bo" );
	if( $(this).attr('id')=='remove' ){
		if( confirm('정말 삭제?') ){
			$('form').submit();			
		}
	}else
		$('form').submit();
});

//동적으로 만든 이미지태그 클릭시 이미지를 크게 볼수 있도록 처리
$(document).on('click', '.preview img', function(){
	$('#popup, #popup-background').css('display', 'block');
	$('#popup').append( $(this).clone() );
});

//background 클릭시 팝업사라지게
$('#popup-background').click(function(){
	$('#popup, #popup-background').css('display', 'none');
	$('#popup').empty();
});

//다운로드 클릭시
$('.download').click(function(){
	$('[name=file]').val( $(this).data('file') );
	$('form').attr('action', 'download.bo').submit();
});

//첨부된 파일이 이미지파일인 경우 미리보기 이미지 태그만들기
<c:forEach items="${vo.fileList}" var='file' varStatus='state'>
	if( isImage( '${file.filepath}' ) ){
		$('.preview').eq(${state.index}).append( '<img src="${file.filepath}">' );
	}
</c:forEach>
</script>


</body>
</html>







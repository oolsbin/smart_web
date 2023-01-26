<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/functions' prefix='fn'%>

<c:forEach items='${list}' var='vo' varStatus='state'>
<%-- ${state.index eq 0 ? '<hr>' : ''} --%>
${state.first ? '<hr>' : ''}
<div data-id='${vo.id}'>${vo.name} [ ${vo.writedate} ]
	<c:if test='${vo.writer eq loginInfo.id}'>
	<span>
		<a class='btn-fill-s btn-modify-save'>수정</a>
		<a class='btn-fill-s btn-delete-cancel'>삭제</a>
	</span>
	</c:if>
	<div class='view'>${fn: replace(  fn: replace(vo.content, lf, '<br>') , crlf, '<br>')}</div>
	<textarea class='modify'></textarea>
</div>
<hr>
</c:forEach>

<script>
$('.btn-delete-cancel').click(function(){
	$div = $(this).closest('div');
	if( $(this).text()=='취소' ){
		modifyStatus(false, $div);
	}else{
		//DB에서 댓글삭제
		if( confirm('정말 삭제?') ){
			$.ajax({
				url: 'board/comment/delete/' + $div.data('id'),
				success: function(){
					comment_list();					
				}
			});
		}
	}
});

$('.btn-modify-save').click(function(){
	$div = $(this).closest('div');
	if( $(this).text()=='수정' ){
		//댓글영역과 같은 높이로 textarea의 높이를 지정
		$div.children('.modify').css( 'height', $div.children('.view').height()-2 );
		
		$div.children('.modify').val( $div.children('.view').html().replace(/<br>/g, '\n') );
		
		modifyStatus(true, $div);
	}else{
		//변경입력정보를 DB에 저장
		var comment = 
		JSON.stringify( { id: $div.data('id')
						 , content: $div.children('.modify').val() } );
		$.ajax({
			type: 'post',
			contentType: 'application/json',
			url: 'board/comment/update',
			data: comment,
// 			data: { id: $div.data('id'), content: $div.children('.modify').val() },
			success: function( response ){
				alert('댓글변경 ' + response);
				comment_list();
				
			},error: function(req,text){
				alert(text+':'+reqs.status);
			}
		});	
		
	}
});

function modifyStatus(is, $div){
	//수정상태: view 안보이게, modify 보이게,  저장/취소 버튼             
	//보기상태: view 보이게, modify 안 보이게, 수정/삭제 버튼             
	$div.children('.modify').css('display', is ? 'block' : 'none');
	$div.children('.view').css('display', is ? 'none' : 'block');
	$div.find('.btn-modify-save').text( is ? '저장' : '수정' );
	$div.find('.btn-delete-cancel').text( is ? '취소' : '삭제' );
}
</script>















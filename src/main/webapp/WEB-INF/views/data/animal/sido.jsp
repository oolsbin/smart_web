<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<li>
	<select class='w-px160' id='sido'>
		<option value=''>시도 선택</option>
		<c:forEach items='${sido.item}' var='vo'>
		<option value='${vo.orgCd}'>${vo.orgdownNm}</option>		
		</c:forEach>
	</select>
</li>
<li>
	<select class='w-px120' id='upkind'>
	<option value=''>축종 선택</option>
	<option value='417000'>개</option>
	<option value='422400'>고양이</option>
	<option value='429900'>기타</option>
	</select>
</li>
<!-- 	축종 - 개 : 417000, - 고양이 : 422400, - 기타 : 429900 -->
<script>
$('#sido').change(function(){
	animal_sigungu(); //시도에 따른 시군구
	animal_list(1);
});
function animal_sigungu(){
	$('#sigungu').closest('li').remove();
	if( $('#sido').val()=='' ) return;   //선택인 경우
	
	$.ajax({
		url: 'data/animal/sigungu',
		data: { sido: $('#sido').val() },
		success: function( response ){
			$('#sido').closest('li').after( response );
		}
		
	});
}

$('#upkind').change(function(){ //축종에 따른 품종
	animal_kind();
	animal_list( 1 );
})
function animal_kind(){
	$('#kind').closest('li').remove();
	if( $('#upkind').val()=='' ) return;
	
	$.ajax({
		url: 'data/animal/kind',
		data: { upkind:$('#upkind').val() },
		success: function(response){
			$('#upkind').closest('li').after( response );
		}
	})	;
}
</script>





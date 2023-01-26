<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<li>
	<select class='w-px160' id='sigungu'>
	<option value=''>시군구 선택</option>
	<c:forEach items='${sigungu.item}' var='vo'>
	<option value='${vo.orgCd}'>${vo.orgdownNm}</option>
	</c:forEach>
	</select>
</li>
<script>
$('#sigungu').change(function(){
	aniaml_shelter(); //시도,시군구에 따른 보호소
	animal_list( 1 );
});

function aniaml_shelter(){
	$('#shelter').closest('li').remove();
	if( $('#sigungu').val()=='' ) return;
	
	$.ajax({
		url: 'data/animal/shelter',
		data: { sido:$('#sido').val(), sigungu:$('#sigungu').val() },
		success: function( response ){
			$('#sigungu').closest('li').after( response );
		}
	});
	
}
</script>

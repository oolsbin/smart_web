/**
 * 유기동물 관련처리 함수
 */
 
 
//동물사진 확대보기
$(document).on('click', '.animal img', function(){
	$('#popup, #popup-background').css('display', 'block');
	$('#popup').html( $(this).clone() );
});
 
 
//유기동물정보 목록보기
function animal_list_view( item ){ //li 10개

	var tag = '';
	if( item.length==0 ){
		tag = "<table  class='animal'><thead><tr><td>해당 유기동물이 없습니다</td></tr></thead></table>";
		$( '#data-list' ).html( tag );
	}else{
		item.each(function(){
			tag += "<table class='animal tb-list'>"			
			+ "<colgroup>"
			+ "	<col width='120px'>"
			+ "	<col width='100px'><col width='80px'>"
			+ "	<col width='60px'><col width='120px'>"
			+ "	<col width='60px'><col width='100px'>"
			+ "	<col width='60px'><col>"
			+ "	<col width='100px'><col width='120px'>"
			+ "</colgroup>"
			+ "<tr><td rowspan='3'><img src='"+ $(this).data('popfile')+"'></td>"
			+ "	<th>성별</th><td>"+ $(this).data('sexcd')+"</td>"
			+ "	<th>나이</th><td>"+ $(this).data('age')+"</td>"
			+ "	<th>체중</th><td>"+ $(this).data('weight')+"</td>"
			+ "	<th>색상</th><td>"+ $(this).data('colorcd')+"</td>"
			+ "	<th>접수일자</th><td>"+ $(this).data('happendt')+"</td>"
			+ "</tr>"
			+ "<tr><th>특징</th>"
			+ "	<td colspan='9' class='text-left'>"+ $(this).data('specialmark')+"</td>"
			+ "</tr>"
			+ "<tr><th>발견장소</th>"
			+ "	<td colspan='8' class='text-left'>"+ $(this).data('happenplace')+"</td>"
			+ "	<td>"+ $(this).data('processstate')+"</td>"
			+ "</tr>"
			+ "<tr><td colspan='2'>"+ $(this).data('carenm')+"</td>"
			+ "	<td colspan='7' class='text-left'>"+ $(this).data('careaddr')+"</td>"
			+ "	<td colspan='2'>"+ $(this).data('caretel')+"</td>"
			+ "</tr>"
			+ "</table>"						
		});
		$('#data-list').html( tag );
	}
}

//유기동물정보 그리드보기:목록->그리드
function animal_grid_view( item ){  //tbody 10개
	var tag = '';
	if( item.length==0 ){
		tag = "<table  class='animal'><thead><tr><td>해당 유기동물이 없습니다</td></tr></thead></table>";
		$( '#data-list' ).html( tag );
	}else{
		
		$('#data-list').html( "<ul class='grid animal'></ul>" );
		
		item.each(function(){
			var animal = new Object(); //유기동물 각각의 정보
			//tbody내에 tr이 4개
			var $tr = $(this).children('tr');			
			$tr.each(function(idx){
				if( idx==0 ){
					animal.popfile = $(this).find('img').attr('src');
					animal.sexcd = $(this).children('td').eq(1).text();
					animal.age = $(this).children('td').eq(2).text();
					animal.weight = $(this).children('td').eq(3).text();
					animal.colorcd = $(this).children('td').eq(4).text();
					animal.happendt = $(this).children('td').eq(5).text();
					
				}else if( idx==1 ){
					animal.specialmark = $(this).children('td').eq(0).text();
					
				}else if( idx==2 ){
					animal.happenplace = $(this).children('td').eq(0).text();
					animal.processstate = $(this).children('td').eq(1).text();
					
				}else if( idx==3 ){
					animal.carenm = $(this).children('td').eq(0).text();
					animal.careaddr = $(this).children('td').eq(1).text();
					animal.caretel = $(this).children('td').eq(2).text();
					
				}
			});
		
		tag += "<li data-popfile='"+ animal.popfile +"' "
			+ "		data-sexcd='"+ animal.sexcd +"' "
			+ "		data-age='"+ animal.age +"' "
			+ "		data-weight='"+ animal.weight +"' "
			+ "		data-colorcd='"+ animal.colorcd +"' "
			+ "		data-happendt='"+ animal.happendt +"' "
			+ "		data-specialmark='"+ animal.specialmark +"' "
			+ "		data-happenplace='"+ animal.happenplace +"' "
			+ "		data-processstate='"+ animal.processstate +"' "
			+ "		data-carenm='"+ animal.carenm +"' "
			+ "		data-careaddr='"+ animal.careaddr +"' "
			+ "		data-caretel='"+ animal.caretel +"' "
			+ ">"
			+  "	<div class='info'>"
			+  "	<img src='"+ animal.popfile +"'>"
			+  "	<div>"
			+  "		<span>"+ animal.age +"</span>"
			+  "		<span class='sw'>"
			+  "			<span>"+ animal.sexcd +"</span>"
			+  "			<span>"+ animal.weight +"</span>"
			+  "		</span>"
			+  "		<span>"+ animal.colorcd +"</span>"
			+  "		<span>"+ animal.processstate +"</span>"
			+  "	</div>"
			+  "</div>"
			+  "<div class='care'>"
			+  "	<span>"+ animal.carenm +"</span>"
			+  "	<span>"+ animal.happendt +"</span>"
			+  "</div>"
			+  "</li>"					
		});
		$('#data-list ul.animal').append( tag );
	}
	
}

//유기동물 시도 요청
function animal_sido(){
	$.ajax({
		url: 'data/animal/sido',
		success: function( response ){
			$('.animal-top').prepend( response );
		}
		
	});
	
}


//유기동물정보 요청
function animal_list( page ){
	if( $('#sido').length==0 ) animal_sido();
	
	loading(true);
	$('#data-list').html('');
	$('.page-list').empty();
	
	var animal = new Object();
	animal.pageNo = page;
	animal.rows = pageList;
	animal.viewType = viewType;
	animal.sido = $('#sido').length>0 ? $('#sido').val() : '';
	animal.sigungu = $('#sigungu').length>0 ? $('#sigungu').val() : '';
	animal.shelter = $('#shelter').length>0 ? $('#shelter').val() : '';
	animal.upkind = $('#upkind').length>0 ? $('#upkind').val() : '';
	animal.kind = $('#kind').length>0 ? $('#kind').val() : '';
	
	$.ajax({
		url: 'data/animal/list',
		data: JSON.stringify( animal ),
		type: 'post',
		contentType: 'application/json',		
		success: function( response ){
			$('#data-list').html( response );
			loading(false);
			
		},error: function(req,text){
			loading(false);
			alert(text+':'+req.status);
		}
	});
}
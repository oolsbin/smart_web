<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
.pharmacy, #list-top, .animal { width: 1200px; }
table.pharmacy td:nth-child(3) { text-align: left; }
#popup { width: 800px; height: 600px; }
.marker { font-weight: bold;  font-size: 16px; color: #ff0000; }
.list-view, .grid-view { border: 1px solid #b0b0b0; padding: 4px 6px }
.common a.on { color:#3367d6; }
.common a.off { color:#b0b0b0; }

ul.pharmacy li div:first-child {  height: 40px }
ul.pharmacy li div:last-child {  font-size: 14px }

table.animal img { width: 100%;   height:100px; }

ul.animal li { display: flex; flex-direction: column; }
ul.animal img { width: 80px; height: 100px; }
ul.animal .care { font-size: 14px;  height: 35px; 
	display: flex; justify-content: space-between;
}
ul.animal .info { display: flex  !important; justify-content: space-between;
	height: calc(100% - 35px) !important;
}
ul.animal .info div { display: flex; flex-direction: column; 
	padding: 0 !important;  width: 120px;
}
ul.animal .info span { white-space: nowrap; 
	overflow: hidden; text-overflow: ellipsis;}
ul.animal .info .sw {  display: flex; justify-content: space-between;}


</style>
</head>
<body>
<h3>공공데이터</h3>
<div class='btnSet api'>
	<a>약국조회</a>
	<a>유기동물조회</a>
</div>
<div id='list-top'>
	<ul class='animal-top'></ul>
	<ul class='common'>
		<li>
			<select class='w-px100' id='pageList'>
			<c:forEach var='i' begin='1' end='5'>
			<option value='${i*10}'>${i*10}개씩</option>
			</c:forEach>
			</select>
		</li>
		<li class='list-view'><a class='on'><i class="font fa-regular fa-rectangle-list"></i></a></li>
		<li class='grid-view'><a class='off'><i class="font fa-solid fa-table-cells"></i></a></li>
	</ul>
</div>
<div id='data-list'></div>
<div class='btnSet'>
	<div class='page-list'></div>
</div>
<div id='popup-background'></div>
<div id='popup' class='center'></div>
<div class='loading center'><img src='img/loading.gif'></div>

<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=98e6eb0cfc6721630538f993ae458df9"></script>

<script 
src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCsrerDHJrp9Wu09Ij7MUELxCTPiYfxfBI"></script>

<script src='js/animal.js?<%=new java.util.Date()%>'></script>
<script>
$('#pageList').change(function(){
	pageList = $(this).val();
	pharmacy_list( 1 );	
});

$(function(){
	$('.api a').eq(1).trigger( 'click' ); //클릭이벤트 강제발생
});

$('.api a').click(function(){
	$('.api a').removeClass();
	$(this).addClass('btn-fill');
	$('.api a').not( $(this) ).addClass( 'btn-empty' );
	
	if( $(this).index()==0 )   	pharmacy_list( 1 );
	else 						animal_list( 1 );
});

//약국정보 요청
function pharmacy_list( page ){
	$('.animal-top').empty();
	$('.page-list').empty();
	$('#data-list').empty();
	
	loading(true);
	$.ajax({
		url: 'data/pharmacy',
		data: { pageNo: page, rows: pageList },
		success: function( response ){
// 			console.log( response );
			
			if( viewType=='list' )  pharmacy_list_view( $(response.item), true );
			else 					pharmacy_grid_view( $(response.item), true ); 
					
			makePage( response.count, page );
			loading(false);
		},error: function(req, text){
			loading(false);
			alert(text+':'+req.status);
		}
	});
	
}

var pageList = 10, blockPage = 10; //한페이지당 목록갯수, 한 블럭당 페이지갯수
//페이지만들기
function makePage( totalList, curPage ){
	var totalPage = Math.ceil( totalList / pageList ); // 총페이지갯수
	var totalBlock = Math.ceil( totalPage / blockPage ); //총블럭갯수
	var curBlock = Math.ceil( curPage / blockPage ); //현재블럭번호
	var endPage = curBlock * blockPage; //현재블럭에서의 끝페이지번호
	var beginPage = endPage - (blockPage-1);//현재블럭에서의 시작페이지번호
	if( totalPage < endPage )  endPage = totalPage;
	
	var tag = '';
	if( curBlock > 1 ){
		tag += '<a data-page=1><i class="fa-solid fa-angles-left"></i></a>'
			+  '<a data-page='+ (beginPage-blockPage) +'><i class="fa-solid fa-angle-left"></i></a>'
	}
	
	for(var no=beginPage; no<=endPage; no++){
		if( no == curPage ) tag += '<span>' + no + '</span>';
		else				tag += '<a data-page='+ no +'>'+ no +'</a>';
	}
	
	if( curBlock < totalBlock ){
		tag += '<a data-page='+ (endPage+1) +'><i class="fa-solid fa-angle-right"></i></a>'
			+  '<a data-page='+ totalPage +'><i class="fa-solid fa-angles-right"></i></a>'
	}
	
	$('.page-list').html( tag );
}

//특정 페이지 요청
$(document).on('click', '.page-list a', function(){
	if( $('.pharmacy').length > 0 )
		pharmacy_list( $(this).data('page') );
	else
		animal_list( $(this).data('page') );
	
}).on('click', '.map', function(){
	if( $(this).data('x')=='undefined' ){
		alert('위경도가 없어 위치를 표시할 수 없습니다');
	}else
		//showGoogleMap( $(this) );
		showKakaoMap( $(this) );
	
});

//카카오맵
function showKakaoMap( point ){
	$('#popup, #popup-background').css('display', 'block');
	
	var xy = new kakao.maps.LatLng(Number(point.data('y')), Number(point.data('x')));
	var container = document.getElementById('popup'); 
	var options = {
		center: xy,
		level: 3 
	};

	var map = new kakao.maps.Map(container, options);
	
	var marker = new kakao.maps.Marker({
	    position: xy
	});
	marker.setMap(map);
	
	var infowindow = new kakao.maps.InfoWindow({
	    position : xy, 
	    content : '<div style="padding:10px" class="marker">' + point.text() + '</div>' 
	});
	infowindow.open(map, marker); 
}

//구글맵
function showGoogleMap( point ){
	$('#popup, #popup-background').css('display', 'block');
	
	const xy = { lat: Number(point.data('y')), lng: Number(point.data('x')) };
	
	const map = new google.maps.Map(document.getElementById("popup"), {
	  zoom: 15,
	  center: xy,
	});
	
	const marker = new google.maps.Marker({
	  position: xy,
	  map: map,
	  title: point.text(),
	});
	
	const infowindow = new google.maps.InfoWindow({
	    content: "<div class='marker'>"+ point.text() +"</div>",
  	});
	
	 infowindow.open({
	      	anchor: marker,
	     	map,
	 });
}

//지도 안보이게
$('#popup-background').click(function(){
	$('#popup-background, #popup').css('display', 'none');
	$('#popup').empty();  //지도태그삭제
});


var viewType = 'list';
$('.list-view, .grid-view').click(function(){
	//현재 보기상태와 다른 보기를 클릭한 경우만 처리
	if( ! $(this).hasClass( viewType + '-view' ) ){
		
		viewType = viewType == 'list' ? 'grid' : 'list';
		$(this).children('a').removeClass().addClass('on');
		$(this).siblings('li').children('a').removeClass().addClass('off');
		
		if( $('.pharmacy').length > 0 ){
		
			if( viewType == 'grid')
				pharmacy_grid_view( $('.pharmacy tbody tr'), false );
			else	
				pharmacy_list_view( $('.pharmacy li'), false );
			
		}else{
			
			if( viewType == 'grid')
				animal_grid_view( $('.animal tbody') );
			else	
				animal_list_view( $('.animal li') );			
		}
	}
});		

//약국정보 그리드로 보기: 화면의 그리드 -> 목록 
function pharmacy_list_view( item, api ){
	var tag 
	= '<table class="tb-list pharmacy">'
	+ '<thead>'
	+ '	<tr><th class="w-px300">약국명</th>'
	+ '		<th class="w-px160">전화번호</th>'
	+ '		<th>주소</th>'
	+ '	</tr>'
	+ '</thead>'
	+ '<tbody></tbody>'
	+ '</table>';
	$('#data-list').html( tag );
	
	tag = '';
	if( api ){
		item.each(function(){
			tag
			+= '<tr><td><a class="map" data-x='+ this.XPos 
					   + ' data-y='+ this.YPos +'>'+ this.yadmNm +'</a></td>'
			+  '	<td>'+ (this.telno ? this.telno : '-') +'</td>'
			+  '	<td>'+ this.addr +'</td>'
			+  '</tr>';	
		});

	}else{
	
		item.each(function(){
			var $div = $(this).children('div'), $a = $(this).find('.map');
			tag
			+= '<tr><td><a class="map" data-x='+ $a.data('x') 
					   + ' data-y='+ $a.data('y') +'>'+ $div.eq(0).text() +'</a></td>'
			+  '	<td>'+ $div.eq(1).text() +'</td>'
			+  '	<td>'+ $div.eq(2).text() +'</td>'
			+  '</tr>';		
		});
	}
	
	$('#data-list table tbody').append( tag );
}

//약국정보 그리드로 보기: 화면의 목록 -> 그리드
function pharmacy_grid_view( item, api ){
	var tag = '<ul class="pharmacy grid"></ul>';
	$('#data-list').html(tag);
	
	tag = '';
	if( api ){
		item.each(function(){
			tag += '<li>'
				+  '	<div><a class="map" data-x='+ this.XPos
							+' data-y='+ this.YPos +'>'+ this.yadmNm +'</a></div>'
				+  '	<div>'+ (this.telno ? this.telno : '-') +'</div>'
				+  '	<div>'+ this.addr +'</div>'
				+  '</li>';
		});

	}else{
	
		item.each(function(){
			var $td = $(this).children('td'), $a = $(this).find('.map');
			tag += '<li>'
				+  '	<div><a class="map" data-x='+ $a.data('x') 
							+' data-y='+ $a.data('y') +'>'+ $td.eq(0).text() +'</a></div>'
				+  '	<div>'+ $td.eq(1).text() +'</div>'
				+  '	<div>'+ $td.eq(2).text() +'</div>'
				+  '</li>';
		});
	}
	$('#data-list ul').append( tag );
}

$(window).resize(function(){
	center( $('#popup'), $('#popup-background') );
});
$(window).scroll(function(){
	center( $('#popup'), $('#popup-background') );
});
</script>
</body>
</html>
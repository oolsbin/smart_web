/**
 * 공통으로 사용할 함수들
 */


$(function(){
	var today = new Date();
	var range = '1950:' + today.getFullYear();
	
	$.datepicker.setDefaults({
		dateFormat: 'yy-mm-dd',
		changeYear: true,
		changeMonth: true,
		showMonthAfterYear: true,
		monthNamesShort: [ '1월', '2월', '3월', '4월', '5월', '6월'
							, '7월', '8월', '9월', '10월', '11월', '12월' ],
		dayNamesMin: [ '일', '월', '화', '수', '목', '금', '토' ],
		maxDate: new Date(), 
		yearRange: range		
	});
	
	
	//선택한 파일을 미리보기되도록 이미지로 보이게 처리
	$('#attach-file').change(function(){
		console.log( this.files[0] );
		var attached = this.files[0];
		if( attached ){ //선택한 파일이 있는 경우
			$('#file-name').text( attached.name ); //선택한 파일명 보이게
			$('#delete-file').css('display', 'inline'); //삭제버튼 보이게
			
			//미리보기 태그가 있으면 
			if( $('#preview').length > 0 ){
				//해당 파일이 이미지파일인지 확인
				if( isImage( attached.name ) ){
					$('#preview').html( '<img>' );
					var reader = new FileReader();
					reader.onload = function( e ){
						$('#preview img').attr('src', e.target.result );						
					}
					reader.readAsDataURL( attached );
				}else
					$('#preview').html('');
			}	
		}else{
			$('#file-name').text('');		//선택한 파일명 안보이게
			$('#delete-file').css('display', 'none');	//파일삭제 이미지도 안보이게
		}
	});
	
	$('#delete-file').click(function(){
		$('#file-name').text('');		//선택한 파일명 안보이게
		$('#attach-file').val('');		//선택한 file태그를 초기화
		$(this).css('display', 'none');	//파일삭제 이미지도 안보이게
		$('#preview').html('');//첨부된 이미지 미리보기 없애기
	});
	

});


//여러개의 파일첨부처리(동적으로 생성한 태그에 대한 이벤트처리는 문서에)
$(document).on( 'change', '.attach-file', function(){
	var attached = this.files[0];
	var $div = $(this).closest('div.align');
	
	if( attached ){
		//원래있던 첨부파일은 삭제대상
		removedFile( $div );
		
		//파일첨부 관련태그를 복제(선택파일명이 없을때)
		if( $div.children('.file-name').text()=='' )	copyFileTag(); 
		
		$div.children('.file-name').text( attached.name ); //파일명보이게
		$div.children('.delete-file').css('display', 'inline');
		
		//선택한 파일이 이미지인경우 미리보이게
		if( $div.children('.preview').length > 0 ){
			if( isImage(attached.name) ){
				$div.children('.preview').html('<img>');
				var reader = new FileReader();
				reader.onload = function( e ){
					$div.find('.preview img').attr('src', e.target.result);
				}
				reader.readAsDataURL( attached );
			}else
				//$div.children('.preview').html('');
				$div.children('.preview').empty();
		}
		
	}else{
		$div.remove();
	}		
	
}).on('click', '.delete-file', function(){
	var $div = $(this).closest('div.align');
	
	removedFile( $div ); //삭제클릭한 첨부파일의 id를 관리
	
	$div.remove();
	
}) 
;

//삭제클릭한 첨부파일의 id를 관리할 함수
function removedFile( $div ){
	var removed = $('[name=removed]').val(); //18, 20
	if( removed=='' ) 
		removed = [];
	else 
		removed = removed.indexOf(',')==-1 ? [removed] : removed.split(',');
		
	if( $div.data('file') ) removed.push( String($div.data('file')) ); //배열에 문자로 추가
	removed = Array.from( new Set( removed ) );//집합데이터를 배열데이터로 만든다
	//console.log('file>', removed );
	
	$('[name=removed]').val( removed ); 
	//console.log('최종>', $('[name=removed]').val() );
}
	
	

//첨부파일관련태그 복제함수
function copyFileTag(){
	var $div = $('div.align').last();
	$div.after( $div.clone() );
	
	//복제된 태그 초기화
	$div = $('div.align').last();
	$div.find('.attach-file').val('');
}


//이미지파일인지 확인하는 함수
function isImage( filename ){
	//KOAS_SeatInfo.txt, abc.png, abc.jpg, abc.gif, ...
	var ext = filename.substring( filename.lastIndexOf('.')+1 );
	var imgs = [ 'png', 'jpg', 'jpeg', 'gif', 'bmp', 'webp' ];
	if( imgs.indexOf( ext ) == -1 ) return false;
	else 							return true;
}



//input 태그에 입력값이 있는지 확인
function emptyCheck(){
	var ok = true;
	$('.chk').each(function(){
		if( $(this).val()=='' ){
			var item = $(this).attr('placeholder')
						? $(this).attr('placeholder') : $(this).attr('title');
			alert(item + ' 입력하세요!');
			$(this).focus();
			ok = false;
			return ok;			
		}
	});
	return ok;
}

//팝업효과 태그 항상 가운데 위치할 수 있게
function center( tag, back ){
	var width 
	= Math.max( $(window).width(), $('body').prop('scrollWidth') );
	var height 
	= Math.max( $(window).height(), $('body').prop('scrollHeight') );
	back.css( {'width': width, 'height': height } );
	
	var left 
	= ($(window).width() - tag.width())/2 + $(window).scrollLeft();
	var top 
	= ($(window).height() - tag.height())/2 + $(window).scrollTop();
	tag.removeClass('center').css( 
				{'left': left, 'top': top, 'position':'absolute'} );
	
}

function loading( is ){
	$('.loading').css('display', is ? 'block' : 'none');
}






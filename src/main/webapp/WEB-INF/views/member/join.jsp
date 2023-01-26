<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<h3>회원가입</h3>

<div class='w-px600 text-right'  style='margin: 10px auto; color: #ff0000'>* 는 필수입력항목입니다</div>
<!--  
파일 업로드시 지켜야 할 사항
1. form태그의 method는 post 로 지정
2. form태그의 enctype는 multipart/form-data 로 지정
-->
<form method='post' action='join' enctype='multipart/form-data'>
<table class='w-px600'>
<tr><th class='w-px140'>* 성명</th>
	<td><input type='text' name='name' autofocus></td>
</tr>
<tr><th>* 아이디</th>
	<td><input type='text' name='id' class='chk'>
		<a class='btn-fill' id='btn-id'>아이디 중복확인</a>
		<div class='valid'>아이디를 입력하세요(영문소문자,숫자만)</div>
	</td>
</tr>
<tr><th>* 비밀번호</th>
	<td><input type='password' name='pw' class='chk'>
		<div class='valid'>비밀번호를 입력하세요(영문대/소문자,숫자 모두 포함)</div>
	</td>
</tr>
<tr><th>* 비밀번호 확인</th>
	<td><input type='password' name='pw_ck' class='chk'>
		<div class='valid'>비밀번호를 다시 입력하세요</div>
	</td>
</tr>
<tr><th>* 이메일</th>
	<td><input type='text' name='email' class='chk'>
		<div class='valid'>이메일을 입력하세요</div>
	</td>
</tr>
<tr><th>* 성별</th>
	<td><label><input type='radio' name='gender' value='남'>남</label>
		<label><input type='radio' name='gender' value='여' checked>여</label>
	</td>
</tr>
<tr><th>프로필이미지</th>
	<td>
		<div class='align'>
		<label>
			<input type='file' name='file' id='attach-file' accept='image/*'>
			<a><i class="font-b fa-regular fa-address-card"></i></a>
		</label>
		<span id='preview'></span>
		<a id='delete-file'><i class="font-r fa-solid fa-trash-can"></i></a>
		</div>
	</td>
</tr>
<tr><th>생년월일</th>
	<td><input type='text' name='birth' class='date' readonly>
		<a id='delete'><i class="font-r fa-regular fa-calendar-xmark"></i></a>
	</td>
</tr>
<tr><th>전화번호</th>
	<td><input type='text' name='phone'></td>
</tr>
<tr><th>주소</th>
	<td><a class='btn-fill' id='post'>우편번호찾기</a>
		<input type='text' name='post' class='w-px60' readonly>
		<input type='text' name='address' class='full' readonly>
		<input type='text' name='address' class='full' >
	</td>
</tr>
</table>
</form>
<div class='btnSet'>
	<a class='btn-fill' id='join'>회원가입</a>
	<a class='btn-empty' onclick='history.go(-1)'>취소</a>
</div>

<script src='js/member.js?<%=new java.util.Date()%>'></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
$('#join').click(function(){
	if( $('[name=name]').val()=='' ){
		alert( '성명을 입력하세요!' );
		$('[name=name]').focus();
		return;
	}
	
	var id = $('[name=id]');
	if( id.hasClass('chked') ){
		//중복확인한 경우
		if( id.siblings('div').hasClass('invalid') ){
			alert('회원가입 불가!\n' + member.id.unUsable.desc);
			id.focus();
			return;
		}
		
	}else{
		//중복확인하지 않은 경우
		//태그의 입력값이 유효한지 확인
		if(  tagIsInValid( id ) )	return;	
		else{
			//입력값은 유효하지만 중복확인하지 않은 경우
			alert('회원가입 불가!\n' + member.id.valid.desc);
			id.focus();
			return;
		}	
	}
	
	if( tagIsInValid( $('[name=pw]') ) )	return;
	if( tagIsInValid( $('[name=pw_ck]') ) )	return;
	if( tagIsInValid( $('[name=email]') ) )	return;
	
	
	$('form').submit();
});

function tagIsInValid( tag ){
	var status = member.tag_status( tag );
	if( status.code=='invalid' ){
		alert('회원가입 불가!\n' +  status.desc);
		tag.focus();
		return true;
	}else
		return false;
}

$('#btn-id').click(function(){
	id_check();	
});

//아이디 중복확인
function id_check(){
	var id = $('[name=id]');
	//이미 중복확인 했다면 다시 확인할 필요없음
	if( id.hasClass('chked') ) return;
		
	var status = member.tag_status( id );
	if( status.code == 'invalid' ){
		alert( '아이디 중복확인 불필요!\n' + status.desc );
		id.focus();
	}else{
		$.ajax({
			url: 'id_check',
			data: { id:id.val() },
			success: function( response ){
				console.log( response )
				//1:사용불가(false), 0:사용가능(true)
				response = response ? member.id.usable : member.id.unUsable;
				id.siblings('div').text( response.desc )
							.removeClass('valid invalid').addClass( response.code );
				id.addClass('chked');
				
			},error: function(req, text){
				alert(text+':'+req.status);
			}
		});		
	}
}



$('.chk').keyup(function( e ){
	//아이디 태그에서 enter인 경우는 중복확인처리
	if( $(this).attr('name')=='id' && e.keyCode==13 ){
		id_check();
	}else{
		$(this).removeClass('chked');
		
		var status = member.tag_status( $(this) );
		$(this).siblings('div').text( status.desc )
					.removeClass('valid invalid').addClass( status.code );
	}
	
});

var today = new Date();
var endDay = new Date( today.getFullYear()-13, today.getMonth(), today.getDate()-1 );

$('#post').click(function(){
	new daum.Postcode({
        oncomplete: function(data) {
			console.log(data);
        	$('[name=post]').val( data.zonecode );
        	var address = data.userSelectedType=='R' 
        					? data.roadAddress : data.jibunAddress;
			if( data.buildingName != '' ) address += ' (' + data.buildingName + ')';        	
        	$('[name=address]').eq(0).val( address );
        }
    }).open();
});

$(function(){
	$('[name=birth]').datepicker({
		maxDate: endDay,
	});
});

$('.date').change(function(){
	$(this).next().css('display', 'inline');
});
$('#delete').click(function(){
	$(this).siblings('.date').val('');
	$(this).css('display', 'none');
});
</script>

</body>
</html>
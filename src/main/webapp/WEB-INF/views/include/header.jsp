<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix='c'%>    
   
<header>
	<div class='category'>
		<ul>
			<li><a href="<c:url value='/'/>"><img src='img/hanul.logo.png'></a></li>
			<li><a href='list.cu' class="${category eq 'cu' ? 'active' : ''}">고객관리</a></li>
			<li><a href='list.hr' class="${category eq 'hr' ? 'active' : ''}">사원관리</a></li>
			<li><a href='list.no' class="${category eq 'no' ? 'active' : ''}">공지사항</a></li>
			<li><a href='list.bo' class="${category eq 'bo' ? 'active' : ''}">방명록</a></li>
			<li><a href='list.da' class="${category eq 'da' ? 'active' : ''}">공공데이터</a></li>
			<li><a href='list.vi' class="${category eq 'vi' ? 'active' : ''}">데이터시각화</a></li>
		</ul>
	</div>
	<div>
		<ul>
			<c:if test='${empty loginInfo}'>
			<li><a class='btn-fill' href='login'>로그인</a></li>
			<li><a class='btn-fill' href='member'>회원가입</a></li>
			</c:if>
			<c:if test='${not empty loginInfo}'>

			<c:choose>
			<c:when test="${empty loginInfo.profile}">
				<li><i class="font-profile fa-regular fa-circle-user"></i></li>
			</c:when>
			<c:otherwise>
				<li><img class='profile' src='${loginInfo.profile}' ></li>
			</c:otherwise>
			</c:choose>

			<li><strong>${loginInfo.name}</strong> 님</li>
			<li><a class='btn-empty' href='password'>비밀번호변경</a></li>
			<li><a class='btn-fill' href='logout'>로그아웃</a></li>
			</c:if>
		</ul>
	</div>
</header>
<style>
.profile { width:50px; height:50px; border-radius: 50%; border:1px solid #aaa;}

header {
	align-items: center;
	padding: 0 100px;
	width: calc(100% - 200px);
	display: flex;
	justify-content: space-between;
	border-bottom: 1px solid #aaa;  
}

header div.category ul {
	display: flex;
	font-size: 18px;
	font-weight: bold;
}

header div.category ul li:not(:first-child) { margin-left: 50px; }

header div.category ul li:hover { color: #0040ff; }

header div.category a.active { color: #0040ff; } 

</style>
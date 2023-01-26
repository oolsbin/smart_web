<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix='tiles'%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix='c'%>    
    

<c:choose>
	<c:when test="${category eq 'join'}"><c:set var='title' value='회원가입'/></c:when>
	<c:when test="${category eq 'password'}"><c:set var='title' value='비밀번호변경'/></c:when>
	<c:when test="${category eq 'cu'}"><c:set var='title' value='고객관리'/></c:when>
	<c:when test="${category eq 'hr'}"><c:set var='title' value='사원관리'/></c:when>
	<c:when test="${category eq 'no'}"><c:set var='title' value='공지사항'/></c:when>
	<c:when test="${category eq 'bo'}"><c:set var='title' value='방명록'/></c:when>
	<c:when test="${category eq 'da'}"><c:set var='title' value='공공데이터'/></c:when>
	<c:when test="${category eq 'vi'}"><c:set var='title' value='데이터시각화'/></c:when>
</c:choose>    


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>스마트 웹&amp;앱 ${title}</title>
<link rel='icon' type='image/x-icon' href='img/hanul.ico'>
<link href='css/common.css?<%=new java.util.Date()%>' type='text/css' rel='stylesheet'> 
<link rel="stylesheet" href="//code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
  
<script src='https://code.jquery.com/jquery-3.6.1.min.js'></script>   
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<script src='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/js/all.min.js'></script>
<script src='js/common.js?<%=new java.util.Date()%>'></script>

</head>
<body>
<tiles:insertAttribute name='header'/>
<tiles:insertAttribute name='container'/>
<tiles:insertAttribute name='footer'/>
</body>
</html>
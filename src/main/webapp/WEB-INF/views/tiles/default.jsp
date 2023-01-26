<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix='tiles'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix='c'%>
<c:choose>
	<c:when test="${category eq 'login'}"><c:set var='title' value='로그인'/></c:when>
	<c:when test="${category eq 'find'}"><c:set var='title' value='비밀번호찾기'/></c:when>
	<c:when test="${category eq 'password'}"><c:set var='title' value='비밀번호변경'/></c:when>
</c:choose>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>스마트 웹&amp;앱 ${title}</title>
<link rel='icon' type='image/x-icon' href='img/hanul.ico'>
<link rel='stylesheet' type='text/css' href='css/common.css?<%=new java.util.Date()%>'>
<script src='https://code.jquery.com/jquery-3.6.1.min.js'></script>
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>   
<script src='js/common.js?<%=new java.util.Date()%>'></script>
</head>
<body>
<tiles:insertAttribute name='container'/>
</body>
</html>
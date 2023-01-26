<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<c:if test='${empty list.item}'>
<table  class='animal'><thead><tr><td>해당 유기동물이 없습니다</td></tr></thead></table>
</c:if>

<c:if test='${not empty list.item}'>
<ul class='grid animal'>
<c:forEach items='${list.item}' var='vo'>
	<li data-popfile='${vo.popfile}'
		data-sexcd='${vo.sexCd}'
		data-age='${vo.age}'
		data-weight='${vo.weight}'
		data-colorcd='${vo.colorCd}'
		data-happendt='${vo.happenDt}'
		data-specialmark='${vo.specialMark}'
		data-happenplace='${vo.happenPlace}'
		data-processstate='${vo.processState}'
		data-carenm='${vo.careNm}'
		data-careaddr='${vo.careAddr}'
		data-caretel='${vo.careTel}'
	>
		<div class='info'>
			<img src='${vo.popfile}'>
			<div>
				<span>${vo.age}</span>
				<span class='sw'>
					<span>${vo.sexCd}</span>
					<span>${vo.weight}</span>
				</span>
				<span>${vo.colorCd}</span>
				<span>${vo.processState}</span>
			</div>
		</div>
		<div class='care'>
			<span>${vo.careNm}</span>
			<span>${vo.happenDt}</span>
		</div>
	</li>
</c:forEach>
</ul>
</c:if>

<script>
makePage(${list.count}, ${pageNo} );
</script>

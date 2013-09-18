<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="fn" uri="fn"%>
<div class="" id="selectItemClose">
	<ul>
		<div style="float: right;">
			<a class="css_closeSelectItem" href="javascript:void(0)">[关闭]</a>
		</div>
		<li>
			<a href="javaScript:void(0);" onclick="hideItem()">不限位置（清空）</a>
		</li>
	</ul>
</div>
<div id="selectItemCount">
	<div class="sq_xiaxian" id="titleCls">
		商业区
	</div>
	<div class="shangquan_list" id="selectSub">
		<ul>
			<c:forEach items="${businessList}" var="biz">
				<li>
					<c:if test="${fn:length(biz[1])>8}">
						<a href="javascript:void(0);" title="${biz[1]}"
							onclick="selectItemSeftVal('${biz[1]}','${biz[3]}','1')">
							${fn:substring(biz[1],0,8)}... </a>
					</c:if>
					<c:if test="${fn:length(biz[1])<=8}">
						<a href="javascript:void(0);"
							onclick="selectItemSeftVal('${biz[1]}','${biz[3]}','1')">
							${biz[1]} </a>
					</c:if>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>
<div id="selectItemCount">
	<div class="sq_xiaxian" id="titleCls">
		行政区
	</div>
	<div class="shangquan_list" id="selectSub">
		<ul>
			<c:forEach items="${districtList}" var="biz">
				<li>
					<c:if test="${fn:length(biz[1])>8}">
						<a href="javascript:void(0);" title="${biz[1]}"
							onclick="selectItemSeftVal('${biz[1]}','${biz[3]}','2')"> <c:out
								value="${fn:substring(biz[1],0,8)}" />... </a>
					</c:if>
					<c:if test="${fn:length(biz[1])<=8}">
						<a href="javascript:void(0);"
							onclick="selectItemSeftVal('${biz[1]}','${biz[3]}','2')"> <c:out
								value="${biz[1]}" /> </a>
					</c:if>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>
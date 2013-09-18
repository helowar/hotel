<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="fn" uri="fn"%>
	<div class="hotelPanel">
	<c:if test="${!empty resultMap.subway||!empty resultMap.airdrome||!empty resultMap.train}"> 
		<div class="hotPanel">
			<table cellpadding="0" cellspacing="0">
				<tr>
					<th>
						交通
					</th>
					<td>
						<p>
							<c:if test="${!empty resultMap.subway}"> 
								地铁：
								<c:forEach items="${resultMap.subway}" var="entry" varStatus="status">
									<c:if test="${status.index>0}">
								       <span>|</span>
								    </c:if>
							        <a href="list-${fn:toLowerCase(entry.cityCode)}-<c:out value="${entry.ID}"/>.html" title="<c:out value="${entry.name}"/>" ><c:out value="${entry.name}" /></a>
							     </c:forEach>
						     </c:if>
						</p>
						<p>
							<c:if test="${!empty resultMap.airdrome}"> 
								机场：
								<c:forEach items="${resultMap.airdrome}" var="entry" varStatus="status">
								<c:if test="${status.index>0}">
								       <span>|</span>
								    </c:if>
							        <a href="list-${fn:toLowerCase(entry.cityCode)}-<c:out value="${entry.ID}"/>.html" title="<c:out value="${entry.name}"/>"><c:out value="${entry.name}" /></a>
							     </c:forEach>
						      </c:if>
						  	<c:if test="${!empty resultMap.train}">   
								火车站：
								<c:forEach items="${resultMap.train}" var="entry" varStatus="status">
								<c:if test="${status.index>0}">
								       <span>|</span>
								    </c:if>
							      <a href="list-${fn:toLowerCase(entry.cityCode)}-<c:out value="${entry.ID}"/>.html" title="<c:out value="${entry.name}"/>"><c:out value="${entry.name}" /></a>
							  </c:forEach>
						   </c:if>
						</p>
					</td>
				</tr>
			</table>
		</div>
		</c:if>
		<c:if test="${!empty resultMap.business}"> 
		<div class="hotPanel">
			<table cellpadding="0" cellspacing="0">
				<tr>
					<th>
						商圈
					</th>
					<td>
						<p>
							<c:forEach items="${resultMap.business}" var="entry" varStatus="status">
							<c:if test="${status.index>0}">
						       <span>|</span>
						    </c:if>
						      <a href="list-${fn:toLowerCase(cityCode)}-${fn:toLowerCase(entry[3])}.html" title="<c:out value="${entry[1]}" />"><c:out value="${entry[1]}" /></a>
						  </c:forEach>
						</p>
					</td>
				</tr>
			</table>
		</div>
		</c:if>
		<c:if test="${!empty resultMap.brand}"> 
		<div class="hotPanel">
			<table cellpadding="0" cellspacing="0">
				<tr>
					<th>
						品牌
					</th>
					<td>
						<p>
						
						<c:forEach items="${resultMap.brand}" var="entry" varStatus="status">
						<c:if test="${status.index>0}">
						       <span>|</span>
						    </c:if>
						<a href="list-group-${fn:toLowerCase(cityCode)}-${entry[0]}.html" title="<c:out value="${entry[1]}" />"><c:out value="${entry[1]}" /></a>
						  </c:forEach>
						</p>
					</td>
				</tr>
			</table>
		</div>
		</c:if>
		<c:if test="${!empty resultMap.university}"> 
		<div class="hotPanel">
			<table cellpadding="0" cellspacing="0">
				<tr>
					<th>
						大学
					</th>
					<td>
						<p>
							<c:forEach items="${resultMap.university}" var="entry" varStatus="status">
								<c:if test="${status.index>0}">
							       <span>|</span>
							    </c:if>
						        <a href="list-${fn:toLowerCase(entry.cityCode)}-<c:out value="${entry.ID}"/>.html" title="<c:out value="${entry.name}" />"><c:out value="${entry.name}" /></a>
						        <c:if test="${status.count==20}">
									<a href="more-${fn:toLowerCase(cityCode)}-26.html" class="green">更多&gt;&gt;</a>
								</c:if>
						     </c:forEach>
						     
						</p>
					</td>
				</tr>
			</table>
		</div>
		</c:if>
		<c:if test="${!empty resultMap.district}"> 
		<div class="hotPanel">
			<table cellpadding="0" cellspacing="0">
				<tr>
					<th>
						行政区
					</th>
					<td>
						<p>
							<c:forEach items="${resultMap.district}" var="entry" varStatus="status">
							<c:if test="${status.index>0}">
						       <span>|</span>
						    </c:if>
						      <a href="list-xingzheng-${fn:toLowerCase(cityCode)}-${fn:toLowerCase(entry[3])}.html" title="<c:out value="${entry[1]}" />"><c:out value="${entry[1]}" /></a>
						      
						  </c:forEach>
						</p>
					</td>
				</tr>
			</table>
		</div>
		</c:if>
		<c:if test="${!empty resultMap.scenic}"> 
		<div class="hotPanel">
			<table cellpadding="0" cellspacing="0">
				<tr>
					<th>
						景区
					</th>
					<td>
						<p>
							<c:forEach items="${resultMap.scenic}" var="entry" varStatus="status">
							<c:if test="${status.index>0}">
						       <span>|</span>
						    </c:if>
					        <a href="list-${fn:toLowerCase(entry.cityCode)}-<c:out value="${entry.ID}"/>.html" title="<c:out value="${entry.name}" />"><c:out value="${entry.name}" /></a>
					        <c:if test="${status.count==20}">
								<a href="more-${fn:toLowerCase(cityCode)}-23.html" class="green">更多&gt;&gt;</a>
							</c:if>
					     </c:forEach>
						</p>
					</td>
				</tr>
			</table>
		</div>
		</c:if>
		<c:if test="${!empty resultMap.hospital}"> 
		<div class="hotPanel nobor">
			<table cellpadding="0" cellspacing="0">
				<tr>
					<th>
						医院
					</th>
					<td>
						<p>
							<c:forEach items="${resultMap.hospital}" var="entry" varStatus="status">
							<c:if test="${status.index>0}">
						       <span>|</span>
						    </c:if>
					        <a href="list-${fn:toLowerCase(entry.cityCode)}-<c:out value="${entry.ID}"/>.html" title="<c:out value="${entry.name}" />"><c:out value="${entry.name}" /></a>
					        <c:if test="${status.count==27}">
								<a href="more-${fn:toLowerCase(cityCode)}-27.html" class="green">更多&gt;&gt;</a>
							</c:if>
					     </c:forEach>
						</p>
					</td>
				</tr>
			</table>
		</div>
		</c:if>
	</div>
	<script type="text/javascript">
	changeDivColor();
	function changeDivColor(){
	    jQuery(".hotPanel").mouseover(
	    function(){
	        jQuery(this).addClass("hotFocus");
	    });  
	    jQuery(".hotPanel").mouseout(
	    function(){
	        jQuery(this).removeClass("hotFocus");
	    });
	}
	    
	</script>

<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="java.security.Principal"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Craiglist Home</title>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

</head>
<body>
<div>
	<button id="viewList">Get Updated Craigslist</button>
	<button id="addItem">Add Item</button>
</div>

<div id="craigslistTable">
					<c:choose>
						<c:when test="${!empty allItems}">
							<div class="search-result">
								<div class="result-header">
									<h3 id="resultCount">Craiglist</h3>
								</div>
								<div class="table-responsive">
									<table class="table table-bordered table-advance" id="tblCraigslist">
										<thead>
											<tr>
												<th>Item Id</th>
												<th>Item Name</th>
												<th>Item Description</th>
												<th>Encoded byteArray Image</th>
												<th>Image from AWS S3</th>
												<th>Price</th>
												<th>Posted Date</th>
												<th>Posted By</th>
												<th>Action</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${allItems}" var="item">
												<tr>
													<td><c:out value="${item.itemId}" /></td>
													<td><c:out value="${item.itemName}" /></td>
													<td><c:out value="${item.description}" /></td>
													<td>
													<c:choose>
														<c:when test="${!empty item.encodedImage}">
															<img height="30" width="30" alt="itemImage" src="data:image/png;base64,<c:out value="${item.encodedImage}" />">
														</c:when>
														<c:otherwise>
															Encoded byte array image not available as the image is too large
														</c:otherwise>
													</c:choose>
													</td>	
													<td>
													<c:choose>
														<c:when test="${!empty item.s3url}">
															<img height="30" width="30" alt="itemImagefromS3" src="<c:out value="${item.s3url}" />">	
														</c:when>
														<c:otherwise>
															Image URL from AWS S3 not available
														</c:otherwise>
													</c:choose>
													</td>
													<td><c:out value="${item.price}" /></td>													
													<td><c:out value="${item.postedDate}" /></td>
													<td><c:out value="${item.postedBy}" /></td>
													<td>
														<c:if test="${item.postedBy==sessionScope.userName}">
															<button id="deleteItem" name="delete">Delete</button>
														</c:if>	
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>

								</div>

							</div>
						</c:when>
						<c:otherwise>
							<div>
								<c:out value="${SearchResult}" />
							</div>
						</c:otherwise>
					</c:choose>
	
</div>

<script type="text/javascript">

$("#addItem").click(function() {
	window.location.href = "addItem";
});
$("#viewList").click(function() {
	//$("#craigslistTable").show();
	window.location.href = "retrieveCraigslist";
});

$("button[name='delete']").click(function(event) {  //on click 
	var trow = $(this).parent('td').parent('tr');
	rowIndex = trow.index();
	var itemId= $('#tblCraigslist tbody tr:eq('+rowIndex+') td:eq(0)').text();
	
	
 	$.ajax({
		type : "POST",
		url : "deleteItem",
		data : {
			itemId : itemId								 						 
		}
	})
	.done(function(msg) {
	
		trow.remove();
	}); 
	
});
</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Item</title>
<!-- Latest compiled and minified CSS -->

	<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">

</head>
<body>

<div>

<fieldset>
<span id="successMsg" class="alert-success"></span>
<span id="errMsg" class="alert-danger"></span>
	
	<form id="idUploadImage" enctype="multipart/form-data">
		<div class="form-group row">
			<label class="col-md-2" for="image">Upload Image<em
				style="color: red;">*</em></label>
			<div class="col-md-6">
				<input id="imageUpload" type="file" name="itemImage"
					class="form-control" />
			</div>

			<div class="col-md-2">
				<input type="submit" id="btnUploadImage" value="Upload"
					class="form-control" />
			</div>
		</div>
	</form>
	<form:form method="POST" action="addNewItemImpl" modelAttribute="Craiglist" class="form-horizontal"
								id="addItemForm">
			<div class="form-group row">
					<label class=" col-md-2" for="itemName">Item Name</label>
					<div class="col-md-2">
						<input id="txtItemName" name="itemName"
							class="form-control" type="text" />
					</div>
					
					<label class=" col-md-2" for="description">Description<em style="color:red;">*</em></label>
					<div class="col-md-5">
						<input id="txtDescription" name="description"	class="form-control" type="text" />
					</div>
			</div>
			
			<div class="form-group row">
					<label class=" col-md-2" for="price">Item Price</label>
					<div class="col-md-2">
						<input id="txtItemPrice" name="price"
							class="form-control" type="text" />
					</div>
			</div>
			
			<input id="txtHiddenImageName" type="hidden" name="imageName" type="text" />
			
			<div id="submitItem" class="col-md-offset-8 col-md-1">
					<button class="btn btn-primary" name="addNewItem"  id="btnSubmit">Add Item</button>
			</div>				
	</form:form>
	
	
	</fieldset>
</div>

<script type="text/javascript">

	$("#errMsg").text(""); 		
	$("#errMsg").hide();  

	$("#btnUploadImage").click(function() {  
		var form = new FormData(document.getElementById('idUploadImage'));

		uploadedFileName = $("#imageUpload").val().replace(/C:\\fakepath\\/i,'');
		

		if (!(uploadedFileName.substring(uploadedFileName.lastIndexOf(".") + 1) == "jpg"
				|| uploadedFileName
						.substring(uploadedFileName.lastIndexOf(".") + 1) == "jpeg" || uploadedFileName
				.substring(uploadedFileName.lastIndexOf(".") + 1) == "png")) {
			$("#errMsg")
					.append(
							"<span>Please select an image with .jpg or .png or .jpeg extension</span><br>");
			$("#errMsg").show();
			return false;
		}
		
		
		 $.ajax({
			  type: "POST",
			  url: "uploadImage",
			  data:form,			 				 
			  processData: false,
			  contentType: false,
			  
			  success: function (data) {
			  	$('#txtHiddenImageName').val(data);		  
	          	$( "#successMsg" ).append("<span>Image: '"+$('#txtHiddenImageName').val()+"' uploaded successfully!</span><br>");					         
		     },

			  error: function (jqXHR) {
					 $("#errMsg").append("Error uploading the document, please try again");
					 $("#errMsg").show();
			  }
		       
		}); 	 	
		 event.preventDefault();
	});

 	$("#txtItemPrice").keypress(function (e) {
	     //if the letter is not digit then display error and don't type anything
	     if (e.which != 8 && e.which != 0 && e.which != 46 && (e.which < 48 || e.which > 57)) {
	        //display error message
	        $("#errmsg").html("Digits Only").show().fadeOut("slow");
	        return false;
	    }
	  });
 	
 	
	$("#btnSubmit").click(function(){
		$("#errMsg").text("");
		$("#errMsg").show();
		$("#successMsg").text("");
		
		formcontrol = $('.form-control');
		if (formcontrol.length == 0
				|| $(formcontrol).val() == "") {
			$("#errMsg")
					.append(
							"<span>Please select required values</span><br>");
			return false;
		}
		
		itemName = $('#txtItemName');
		if ($(itemName).val() == "") {
			$("#errMsg")
					.append(
							"<span>Please enter the Item Name</span><br>");
			return false;
		}
		
		itemDescription = $('#txtDescription');
		if ($(itemDescription).val() == "") {
			$("#errMsg")
					.append(
							"<span>Please enter the Item Description</span><br>");
			return false;
		}
		
		itemPrice = $('#txtItemPrice');
		if ($(itemPrice).val() == "") {
			$("#errMsg")
					.append(
		
							"<span>Please enter the Item Price</span><br>");
			return false;
		}
		
		if ($(txtHiddenImageName).val() == "")
		{
			$( "#errMsg" ).append( "<span>Please upload the image of the Item</span><br>" );
		    return false;
		}

	});
	
	</script>
</body>
</html>
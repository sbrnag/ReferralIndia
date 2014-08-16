<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Homepage</title>

<style>
#postjob {
	position: absolute;
	border-style:none;
	top: 60px;
}

#jobstatus {
	position: absolute;
	top: 80px;
	right: 70px;
}
</style>


<link
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css"
	rel="stylesheet" type="text/css" />

<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.js"
	type="text/javascript"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"
	type="text/javascript"></script>
<script
	src="http://ajax.microsoft.com/ajax/jquery.validate/1.7/jquery.validate.js"
	type="text/javascript"></script>


<script type="text/javascript">
	$(document).ready(
			function() {

				var myCounter = 1;
				$(".myDate").datepicker();

				$("#moreDates").click(
						function() {

							$('.myTemplate').clone().removeClass("myTemplate")
									.addClass("additionalDate").show()
									.appendTo('#importantDates');

							myCounter++;
							$('.additionalDate input[name=inputDate]').each(
									function(index) {
										$(this).addClass("myDate");
										$(this).attr(
												"name",
												$(this).attr("name")
														+ myCounter);
									});

							$(".myDate").on('focus', function() {
								var $this = $(this);
								if (!$this.data('datepicker')) {
									$this.removeClass("hasDatepicker");
									$this.datepicker();
									$this.datepicker("show");
								}
							});

						});

			});
</script>

<script>
	function postjobAjax() {
		$.ajax({
			type : "POST",
			url : "postajob",
			data : $("#rfjob").serialize(),
			success : function(result) {

				$("#jobstatus").text(result.status)
			},
			error : function(e) {
				$("#jobstatus").text("job not posted")
			}
		});

	}
</script>




<script>
	$(document)
			.ready(
					function() {

						$
								.ajax({
									type : "GET",
									url : "displayreferraljobs",
									success : function(data) {

										var tr;

										for ( var i = 0; i < data.length; i++) {
											tr = $('<tr/>');
											tr
													.append("<input type=\"Checkbox\" name=\"select\" value=\"selected\"/>");
											tr.append("<td>"
													+ data[i].posted_date
													+ "</td>");
											tr.append("<td>"
													+ data[i].expire_date
													+ "</td>");
											tr.append("<td>"
													+ data[i].open_position
													+ "</td>");
											tr.append("<td>" + data[i].skill
													+ "</td>");
											tr.append("<td>" + data[i].company
													+ "</td>");
											tr.append("<td>"
													+ data[i].posted_by
													+ "</td>");

											$('#rfjobs').append(tr);

										}
									},
									error : function(e) {
									}
								});
					});
</script>





</head>
<body>

	<div id="postjob">
		<b>post referral job</b>
		<sf:form method="POST" commandName="rfjob" id="rfjob">
			<table>




				<tr>


					<td>Experiece:<sf:select path="exp_from" placeholder="from">
							<sf:option value="0" label="0" />
							<sf:option value="1" label="1" />
							<sf:option value="2" label="2" />
							<sf:option value="3" label="3" />
							<sf:option value="4" label="4" />
							<sf:option value="5" label="5" />
							<sf:option value="6" label="6" />
							<sf:option value="7" label="7" />
							<sf:option value="8" label="8" />
							<sf:option value="9" label="9" />
							<sf:option value="10" label="10" />

						</sf:select> <sf:select path="exp_to">
							<sf:option value="0" label="0" />
							<sf:option value="1" label="1" />
							<sf:option value="2" label="2" />
							<sf:option value="3" label="3" />
							<sf:option value="4" label="4" />
							<sf:option value="5" label="5" />
							<sf:option value="6" label="6" />
							<sf:option value="7" label="7" />
							<sf:option value="8" label="8" />
							<sf:option value="9" label="9" />
							<sf:option value="10" label="10" />

						</sf:select></td>

					<td><sf:input path="open_position" placeholder="Position" /></td>
					<td><sf:errors path="open_position" /></td>







					<td><sf:input path="skill" placeholder="Skill/Description" /></td>
					<td><sf:errors path="skill" /></td>






					<td><sf:input path="company" placeholder="Company" /></td>
					<td><sf:errors path="company" /></td>


					<td>
						<div id="myContent">

							<sf:input path="expire_date" placeholder="expiredate"
								class="myDate" type="text" name="inputDate1" size="10" value="" />
						</div>

					</td>


					<td><sf:select path="Location">
							<sf:option value="0" label="location" />
							<sf:option value="1" label="Banglore" />
							<sf:option value="2" label="Hyderabad" />
							<sf:option value="3" label="Chennai" />
							<sf:option value="4" label="Mumbai" />

						</sf:select></td>
					<td><sf:errors path="location" /></td>
					<sf:hidden path="posted_by" value="${user.username}" />




					<td><input type="Button" value="Post" onclick="postjobAjax()"></td>
				</tr>



			</table>

		</sf:form>
	</div>
	<div id="jobstatus"></div>
	<h3>Latest jobs posted</h3>
	<div id="rfjobs"></div>

</body>
</html>
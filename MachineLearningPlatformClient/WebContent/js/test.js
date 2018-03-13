$(function() {
	$("#button").on('click', function() {
		var request={
			"username" : "username",
			"password" : [
				{
					"name":"name",
					"value":"value"
				},
				{
					"name":"name1",
					"value":"value1"
				}
			]
		}
		request=JSON.stringify(request);
		var url="task/submitTask";
		$.ajax({
			type : "post",
			dataType : "json",
			contentType : "application/json;charset=utf-8",
			async : false,
			url : url,
			data : request,
			success : function(data) {
				
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				console.log(XMLHttpRequest);
				//alert("请求发生错误");
			}
		});
	});
});
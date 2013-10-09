/*function that appends select options onto a select menu with name user
Willis Kennedy created 12/1*/
function fillUsers(user){
	$.get("readUser.php", function(data){
		$i = 0;
		$.each(data, function(){
			$("select[name='user']").append("<option value=" + data[$i] + ">" + data[$i] + "</option>");
			$i++;
		});

		if(user!=="")
        		$("select[name='user']>option[value='"+user+"']").prop('selected', true);

	}, "json");
}

$(document).ready(function() {
	$.fn.dataTable.moment('HH:mm MMM D, YY');
	$('#table_current_sensors').DataTable({
		"ajax" : "/rest/sensors/current",
		"columns" : [ {
			"data" : "name"
		}, {
			"data" : "data[0].value"
		}, {
			"data" : "data[0].date",
			"render" : function(data, type, row) {
				return moment(data, "x").format('HH:mm:ss A MMM D, YYYY');
			}
		} ],
		"ordering" : false,
		"info" : false,
		"paging" : false,
		"searching" : false,
		"columnDefs": [
           { className: "dt-center", "targets": [ 0,1,2 ] }
         ]
	});
});
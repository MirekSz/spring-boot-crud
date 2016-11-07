$(document).ready(function() {
	// $("table tbody tr").click(function(){
	// $.get( "/getRandomLinks", function( data ) {
	// $( "#details" ).hide()
	// $( "#details" ).html( data );
	// $( "#details" ).animate({
	// height: "toggle"
	// }, 500, function() {
	// // Animation complete.
	// });
	// });
	// })
	$("#refresh").click(function() {
		refreshState()
	})
	
	$("#addTag").click(function(e) {
		addTag();
		e.preventDefault();
	})
	
$("#referer").select2({
	  ajax: {
		    url: "/altkom/asJSON",
		    dataType: 'json',
		    delay: 250,
		    data: function (params) {
		      return {
		        q: params.term, // search term
		        page: params.page
		      };
		    },
		    processResults: function (data, params) {
		      // parse the results into the format expected by Select2
		      // since we are using custom formatting functions we do not need to
		      // alter the remote JSON data, except to indicate that infinite
		      // scrolling can be used
		      params.page = params.page || 1;

		      return {
		        results: data.items,
		        pagination: {
		          more: (params.page * 10) < data.total_count
		        }
		      };
		    },
		    cache: true
		  },
		  escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
		  minimumInputLength: 1,
		  templateResult: function(data){
			  return '<div class="clearfix"><div class="col-xs-4"><h3>'+data.name+'</h3></div><div class="col-xs-8"><img class="pull-right" style="width: 120px" src="/altkom/phones/'+data.phone+'" /></div></div>'
			  
		  },
		  templateSelection: function template(data, container) {
			  return data.name;
		  } ,
		  allowClear: true
		});
})

function addTag(){
	var length = $("#tags input").length
	$("#tags").append('<input name="tags['+length+'].name" class="form-control col-xs-2"/>')
}
function refreshState(){
	$.get('/altkom/greeting', function(data) {
		$("ul").html(data)
	})
}
var sockjs_url = '/altkom/websocket';
var sockjs
var new_conn = function() {
	sockjs = new SockJS(sockjs_url);

	stompClient = Stomp.over(sockjs);
	stompClient.connect({login:'mirek'}, function(frame) {
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/products-change', function(greeting) {
			refreshState()
		});
		stompClient.subscribe("/queue/mirek/priv", function(greeting) {
			debugger;
			alert(greeting)
		});
	}, function() {
		setTimeout(function() {
			new_conn();
			refreshState();
		}, 5000);
	});
};
new_conn();

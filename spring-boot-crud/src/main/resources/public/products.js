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
	stompClient.connect({}, function(frame) {
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/products-change', function(greeting) {
			refreshState()
		});
	}, function() {
		setTimeout(function() {
			new_conn();
			refreshState();
		}, 5000);
	});
};
new_conn();

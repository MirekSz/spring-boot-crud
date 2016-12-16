var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

var _ctx = $("meta[name='ctx']").attr("content");

$(document).ajaxSend(function(e, xhr, options) {
	xhr.setRequestHeader(header, token);
});

$(document).ready(function() {
	$("table tbody tr").click(function() {
		$.post(_ctx + "/getRandomLinks", function(data) {
			$("#details").hide()
			$("#details").html(data);
			$("#details").animate({
				height : "toggle"
			}, 500, function() {
				// Animation complete.
			});
		});
	})
	if (window.location.href.endsWith('/auctions')) {
		var source = new EventSource(_ctx + '/auctions/stream');
		source.onmessage = function(event) {
			console.log(event)
		}
	}
})
// thymelaft javascript

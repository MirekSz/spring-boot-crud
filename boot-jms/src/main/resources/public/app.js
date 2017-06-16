$(document).ready(function(){
	var sockjs_url = '/jms/websocket';
	var sockjs
	var new_conn = function() {
		var options = {protocols_whitelist: ["websocket", "xhr-streaming", "xdr-streaming", "xhr-polling", "xdr-polling", "iframe-htmlfile", "iframe-eventsource", "iframe-xhr-polling"], debug: true};             
		sockjs = new SockJS(sockjs_url);

		stompClient = Stomp.over(sockjs);
//		stompClient.debug = null
		stompClient.connect({
//				login : 'user',
//				passcode:'user'
		}, function(frame) {
			console.log('Connected: ' + frame);
			stompClient.subscribe('/topic/products-change', function(t) {
				console.log('topic',t)
					
				var left = Math.floor((Math.random() * 1000) + 1);
				var top=Math.floor((Math.random() * 700) + 1);
				$(document.body).append('<img alt="" src="avatar.png" style="position:absolute;width:32px;height:32px;top:'+top+'px;left:'+left+'px">')
			});
			
			stompClient.subscribe("/user/queue/priv", function(q) {
				console.log('queue user ',q)
				var o = JSON.parse( q.body);
				$("h1").text(o.login+' '+o.users)
			
			});
		}, function() {
			setTimeout(function() {
				new_conn();
			}, 3000);
		});
	};
	new_conn();
})

setTimeout(function() {
	stompClient.send("/jms/topic/hello", {}, JSON.stringify({
		'name' : 'mirek'
	}));
	stompClient.subscribe("/topic/chat/{institutionId}", function(t) {
		console.log('chat',t);
	});
	
	stompClient.send("/jms/topic/chat/1", {}, JSON.stringify({
		'name' : 'mirek'
	}));
}, 5000)

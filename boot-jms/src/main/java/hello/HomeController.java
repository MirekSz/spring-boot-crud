package hello;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import hello.ws.Data;

@Controller
public class HomeController {
	@Autowired
	private SimpMessagingTemplate webSocket;
	@Autowired
	SessionRegistry sr;

	@MessageMapping("/topic/hello")
	public void greeting(Data data, Principal p) throws Exception {
		List<Object> allPrincipals = sr.getAllPrincipals();
		data.setUsers(allPrincipals.size());
		data.setLogin(p.getName());
		data.setRespo(data.getName() + p.getName() + " " + allPrincipals.size());
		webSocket.convertAndSendToUser(p.getName(), "/queue/priv", data);
	}

	@MessageMapping("/topic/chat/{institutionId}")
	public void greeting(@DestinationVariable String institutionId, final Data message) throws Exception {
		message.setRespo("/topic/chat/" + institutionId);
		webSocket.convertAndSend("/topic/chat/" + institutionId, message);
	}

	@RequestMapping(value = "/")
	public String home() {
		return "home";
	}

	@RequestMapping(value = "/ws")
	public String ws() {
		return "ws";
	}

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@RequestMapping(value = "/user")
	public String user() {
		return "ws";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@RequestMapping(value = "/admin")
	public String admin() {
		return "ws";
	}

	@PreAuthorize("hasAnyRole('ROLE_PARTNER')")
	@RequestMapping(value = "/partner")
	public String partner() {
		return "ws";
	}

}

package site.metacoding.chat.controller;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@ServerEndpoint("/websocket")//엔드포인트를 지정
public class MessageController extends Socket {
	private static final List<Session> session = new ArrayList<Session>();

	//view할곳 지정
	@GetMapping("/")
	public String index() {
		return "/index";
	}

	//웹소켓에 연결되면 호출되는 이벤트
	@OnOpen
	public void open(Session newUser) {
		System.out.println("connected");
		session.add(newUser);
		System.out.println(newUser.getId());
	}

	//웹소켓으로 부터 메시지가 오면 호출되는 이벤트
	@OnMessage
	public void getMsg(Session recieveSession, String msg) {
		for (int i = 0; i < session.size(); i++) {
			if (!recieveSession.getId().equals(session.get(i).getId())) {
				try {
					session.get(i).getBasicRemote().sendText("상대 : " + msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					session.get(i).getBasicRemote().sendText("나 : " + msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultJsonMapper;
import com.restfb.FacebookClient;
import com.restfb.JsonMapper;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.send.IdMessageRecipient;
import com.restfb.types.send.Message;
import com.restfb.types.send.SendResponse;
import com.restfb.types.webhook.WebhookEntry;
import com.restfb.types.webhook.WebhookObject;
import com.restfb.types.webhook.messaging.MessagingItem;

/**
 * Servlet implementation class FBChat
 */
@WebServlet("/isitcom.php")
public class FBChat extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	private String accsessToken = "EAAauqfJUiNIBAIGE62bk4r1WKsksMkLKuczgmXEpU9dA20J3ZCewa7i5ZAkLjUM3A7nL3iUl6yoNAZCa1ReeZAB3CCmqTHnP4vaXHEhgIy5tEekONRCe28KmgZCbNnJiaVQ7VXMk3t0TAQOAcWBgIBkK1DUaxiT41X0KTkZAZBnEwZDZD";
	private String verifyToken = "haroun123";

	public FBChat() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String hubToken = request.getParameter("hub.verify_token");
		String hubChallenge = request.getParameter("hub.challenge");

		if (verifyToken.equals(hubToken)) {
			response.getWriter().write(hubChallenge);
			response.getWriter().flush();
			response.getWriter().close();
		} else {
			response.getWriter().write("incorrect token");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = request.getReader();
		String line = "";
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		JsonMapper mapper = new DefaultJsonMapper();
		WebhookObject webhookObject = mapper.toJavaObject(sb.toString(), WebhookObject.class);

		for (WebhookEntry entry : webhookObject.getEntryList()) {
			if (entry.getMessaging() != null) {
				for (MessagingItem mItem : entry.getMessaging()) {
					String senderId = mItem.getSender().getId();
					IdMessageRecipient recipient = new IdMessageRecipient(senderId);
					if((mItem.getMessage() != null)&&(mItem.getMessage().getText()!=null)){
						sendMessage(recipient, new Message("Hi"));
					}
				}
			}
		}
	}

	void sendMessage(IdMessageRecipient recipient, Message message) {
		// create a version 2.6 client
		FacebookClient pageClient = new DefaultFacebookClient(accsessToken, Version.VERSION_2_6);

		SendResponse resp = pageClient.publish("me/messages", SendResponse.class,
				Parameter.with("recipient", recipient), // the id or phone
														// recipient
				Parameter.with("message", message)); // one of the messages from
														// above
	}
}

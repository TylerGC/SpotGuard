package SpotGuard.api.Spotify.http;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import SpotGuard.api.Discord.ui.Registration;
import SpotGuard.api.Spotify.SpotifyAPI;
import SpotGuard.manage.Manager;
import SpotGuard.manage.User;

public class ResponseHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String uri = exchange.getRequestURI().toString();
		String token = uri.split("\\?code=")[1].split("&state=")[0];
		String state = exchange.getRequestURI().toString().split("&state=")[1];
		handleResponse(exchange);
		String discordID = RequestHandler.getUser(state).getId();
	    Manager.addUser(new User(discordID, token), discordID);
		SpotifyAPI.authorizationCode(token, discordID);
		Registration.sendRegistrationReply(RequestHandler.getUser(state));
		RequestHandler.removeUser(state);
	}
	
	private void handleResponse(HttpExchange exchange) {
		//TODO Reference a better-looking HTMl page.
		OutputStream outputStream = exchange.getResponseBody();
		StringBuilder htmlBuilder = new StringBuilder();
		
		htmlBuilder.append("<html>")
		.append("<body>")
		.append("<h1>")
		.append("Thank you for registering with SpotGuard. You may close this window and go back to Discord. Use /manage to manage your playlists.")
		.append("</h1>")
		.append("</body>")
		.append("</html>");
		
		try {
			exchange.sendResponseHeaders(200, htmlBuilder.toString().length());
			outputStream.write(htmlBuilder.toString().getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

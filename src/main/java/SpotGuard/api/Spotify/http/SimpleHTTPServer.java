package SpotGuard.api.Spotify.http;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class SimpleHTTPServer {

	public static void listen() {
		InetSocketAddress serverAddress = null;
			//serverAddress = new InetSocketAddress(InetAddress.getByName("www.spotguard.app"), 8888);
			serverAddress = new InetSocketAddress(80);//72.185.58.246
		try {
			HttpServer server = HttpServer.create(serverAddress, 0);
			server.createContext("/register", new ResponseHandler());
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

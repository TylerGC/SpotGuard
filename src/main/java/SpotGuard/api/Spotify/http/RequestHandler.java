package SpotGuard.api.Spotify.http;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import net.dv8tion.jda.api.entities.User;

public class RequestHandler {

	private static HashMap<String, User> requests = new HashMap<String, User>();
	
	public static void addRequest(User event, String check) {
		requests.put(check, event);
	}
	
	public static User getUser(String check) {
		return requests.get(check);
	}
	
	public static boolean removeUser(String check) {
		if (!(requests.remove(check) == null))
			return true;
		return false;
	}
	
	public static String createMD5Hash(String input) throws NoSuchAlgorithmException {

		      String hashtext = null;
		      MessageDigest md = MessageDigest.getInstance("MD5");

		      // Compute message digest of the input
		      byte[] messageDigest = md.digest(input.getBytes());

		      hashtext = convertToHex(messageDigest);

		      return hashtext;
	}
	
	   private static String convertToHex(final byte[] messageDigest) {
		      BigInteger bigint = new BigInteger(1, messageDigest);
		      String hexText = bigint.toString(16);
		      while (hexText.length() < 32) {
		         hexText = "0".concat(hexText);
		      }
		      return hexText;
		   }
	
}

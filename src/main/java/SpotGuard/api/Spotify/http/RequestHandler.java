package SpotGuard.api.Spotify.http;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import SpotGuard.manage.User;

public class RequestHandler {

	private static HashMap<String, String> registrationRequests = new HashMap<String, String>();
	private static HashMap<String, User> reauthorizationRequests = new HashMap<String, User>();
	
	public static void addRegistrationRequest(String discordID, String check) {
		registrationRequests.put(check, discordID);
	}
	
	public static void addReauthorizationRequest(User event, String check) {
		reauthorizationRequests.put(check, event);
	}
	
	public static String getRegistrationUser(String check) {
		return registrationRequests.get(check);
	}
	
	public static User getReauthorizationUser(String check) {
		return reauthorizationRequests.get(check);
	}
	
	public static boolean removeRegistrationUser(String check) {
		if (!(registrationRequests.remove(check) == null))
			return true;
		return false;
	}
	
	public static boolean removeReauthorizationUser(String check) {
		if (!(reauthorizationRequests.remove(check) == null))
			return true;
		return false;
	}
	
	public static String createMD5Hash(String input) {

		      String hashtext = null;
		      MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				// This should never happen.
				e.printStackTrace();
			}

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

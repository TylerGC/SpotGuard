package SpotGuard.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A PlayList object that represents a playlist under SpotGuard's protection, including the rules to follow and enforce.
 * 
 * @author TylerGC
 *
 */
public class PlayList {
	
	String playlistID;
	String ownerID;
	String discordID;
	HashMap<String, Boolean> whitelist = new HashMap<String, Boolean>();
	List<String> backup = new ArrayList<String>();
	boolean isProtected = false;

	public PlayList(String plid, String oid, String did) {
		playlistID = plid;
		ownerID = oid;
		discordID = did;
		whitelist.put(ownerID, true);
	}
	
	public boolean isWhitelisted(String userID) {
		if (!whitelist.containsKey(userID))
			return false;
		else
			return whitelist.get(userID);
	}
	
	public void setWhitelist(String userID, boolean value) {
		whitelist.replace(userID, value);
	}
	
	public HashMap<String, Boolean> getWhitelist() {
		return whitelist;
	}
	
	public List<String> getWhitelistMembers() {
		ArrayList<String> members = new ArrayList<String>();
		for (String member : whitelist.keySet()) {
			members.add(member);
		}
		return members;
	}
	
	public String getOwner() {
		return ownerID;
	}
	
	public String getOwnerDiscordID() {
		return discordID;
	}
	
	public String getPlaylistID() {
		return playlistID;
	}
	
	public void setIsProtected(boolean bool) {
		isProtected = bool;
	}
	
	public boolean getIsProtected() {
		return isProtected;
	}
	
}

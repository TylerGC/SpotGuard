package SpotGuard.manage;

import java.util.ArrayList;
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
	List<String> whitelist = new ArrayList<String>();
	boolean isProtected = false;

	public PlayList(String plid, String oid, String did) {
		playlistID = plid;
		ownerID = oid;
		discordID = did;
		whitelist.add(ownerID);
	}
	
	public List<String> getWhitelist() {
		return whitelist;
	}
	
	public void addToWhitelist(String userID) {
		if(!whitelist.contains(userID))
			whitelist.add(userID);
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

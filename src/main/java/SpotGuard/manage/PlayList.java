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
	List<String> whitelist = new ArrayList<String>();

	public PlayList(String plid, String oid) {
		playlistID = plid;
		ownerID = oid;
	}
	
	public List<String> getWhitelist() {
		return whitelist;
	}
	
}

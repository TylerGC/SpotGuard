package SpotGuard.manage;

import java.util.HashMap;

public class Manager {

	public static HashMap<String, PlayList> playlistMap = new HashMap<String, PlayList>();
	
	public static void addPlayList(PlayList pl) {
		playlistMap.put(pl.playlistID, pl);
	}
	
}

package core.game.sound;

import core.game.sound.region.Music;

/**
 * A class which handles the loading of music data.
 * 
 * @author Michael P
 *
 */
public class MusicManager {

	/**
	 * An array of loaded music.
	 */
	public static Music[] music;

	/**
	 * Gets the music array.
	 * 
	 * @return The array of loaded music.
	 */
	public static Music[] getMusic() {
		return music;
	}

	/**
	 * Gets the music for the region id.
	 * 
	 * @param region
	 *            The region id.
	 * @return The music track for the region.
	 */
	public static Music forRegion(int region) {
		for (Music m : music) {
			if (m == null)
				continue;
			if (m.getRegion() == region)
				return m;
		}
		return null;
	}
	
	public static String songNameForRegion(int region) {
		for (Music m : music) {
			if (m == null)
				continue;
			if (m.getRegion() == region)
				return m.getName();
		}
		return "No song for this region";
	}

	public static int songIdForRegion(int region) {
		for (Music m : music) {
			if (m == null)
				continue;
			if (m.getRegion() == region) {
				return m.getSong();
			}
		}
		return -1;
	}

	/**
	 * Gets the music for the frame.
	 * 
	 * @param button
	 *            The button of the track.
	 * @return The music track for the frame.
	 */
	public static Music forFrame(int button) {
		for (Music m : music) {
			if (m == null)
				continue;
			if (m.getButton() == button)
				return m;
		}
		return null;
	}

	public static Music forSong(int song) {
		for (Music m : music) {
			if (m == null)
				continue;
			if (m.getSong() == song)
				return m;
		}
		return null;
	}
}
package core.game.sound.region;

/**
 * Represents a single music track.
 * 
 * @author Michael P
 *
 */
public class Music {
	
	public Music(int region, String name, int song, int frame, int button) {
		this.region = region;
		this.name = name;
		this.song = song;
		this.frame = frame;
		this.button = button;
	}
	
	private final int region;
	
	private final String name;
	
	private final int song;
	
	private final int frame;
	
	private final int button;
	
	public int getRegion() {
		return region;
	}
	
	public String getName() {
		return name;
	}
	
	public int getSong() {
		return song;
	}
	
	public int getFrame() {
		return frame;
	}

	public int getButton() {
		return button;
	}	
}

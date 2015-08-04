package core.game.util.log;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A custom logger that logs the output stream.
 * @author 7Winds
 */
public class CustomLogger extends PrintStream {

	private DateFormat dateFormat = new SimpleDateFormat();
	private Date cachedDate = new Date();

	public CustomLogger(PrintStream out) {
		super(out);
	}

	@Override
	public void print(String str) {
		if (str.startsWith("debug:"))
			super.print("[" + getPrefix() + "] DEBUG: " + str.substring(6));
		else
			super.print("[" + getPrefix() + "]: " + str);
	}

	private String getPrefix() {
		return dateFormat.format(cachedDate);
	}
}

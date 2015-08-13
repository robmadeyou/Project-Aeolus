package core.net;

import java.math.BigInteger;

/**
 * A class which holds network-related constants.
 * @author 7Winds
 */
public final class NetworkConstants {
	
	/**
	 * The HTTP port.
	 */
	public static final int HTTP_PORT = 80;
	
	/**
	 * The JAGGRAB port.
	 */
	public static final int JAGGRAB_PORT = 43595;
	
	/**
	 * The service port (which is also used for the 'on-demand' protocol).
	 */
	public static final int SERVICE_PORT = 43594;

	
	public static final BigInteger RSA_MODULUS = new BigInteger(
			"94904992129904410061849432720048295856082621425118273522925386720620318960919649616773860564226013741030211135158797393273808089000770687087538386210551037271884505217469135237269866084874090369313013016228010726263597258760029391951907049483204438424117908438852851618778702170822555894057960542749301583313");

	public static final BigInteger RSA_EXPONENT = new BigInteger(
			"72640252303588278644467876834506654511692882736878142674473705672822320822095174696379303197013981434572187481298130748148385818094460521624198552406940508805602215708418094058951352076283100448576575511642453669107583920561043364042814766866691981132717812444681081534760715694225059124574441435942822149161");
	
	/**
	 * The number of seconds a channel can be idle before being closed
	 * automatically.
	 */
	public static final int IDLE_TIME = 15;
	
	/**
	 * Default private constructor to prevent instantiaton.
	 */
	private NetworkConstants() {
		
	}

}

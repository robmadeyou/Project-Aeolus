package core.game.model.entity.player;

public enum Rights {

	/**
	 * Protocol Value, Server value
	 */
	PLAYER(0, 0), MODERATOR(1, 1), ADMINISTRATOR(2, 2), DEVELOPER(2, 3);
	
	/**
	 * value client-sided
	 */
	private final int protocolValue;
	
	/**
	 * rights server-sided
	 */
	private final int values;

	/**
	 * Create a new {@link Rights}.
	 *
	 * @param protocolValue
	 *            the value of this rank as seen by the protocol.
	 * @param value
	 *            the value of this rank as seen by the server.
	 */
	private Rights(int protocalValue, int values) {
		this.protocolValue = protocalValue;
		this.values = values;
	}

	/**
	 * Determines if this right is greater than the argued right. Please note
	 * that this method <b>does not</b> compare the Objects themselves, but
	 * instead compares the value behind them as specified by {@code value} in
	 * the enumerated type.
	 *
	 * @param other
	 *            the argued right to compare.
	 * @return {@code true} if this right is greater, {@code false} otherwise.
	 */
	public final boolean greater(Rights other) {
		return ordinal() > other.ordinal();
	}

	public final boolean greaterOrEqual(Rights other) {
		return ordinal() >= other.ordinal();
	}

	/**
	 * Determines if this right is lesser than the argued right. Please note
	 * that this method <b>does not</b> compare the Objects themselves, but
	 * instead compares the value behind them as specified by {@code value} in
	 * the enumerated type.
	 *
	 * @param other
	 *            the argued right to compare.
	 * @return {@code true} if this right is lesser, {@code false} otherwise.
	 */
	public final boolean less(Rights other) {
		return ordinal() < other.ordinal();
	}

	public final boolean lessOrEqual(Rights other) {
		return ordinal() <= other.ordinal();
	}

	/**
	 * Determines if this right is equal in power to the argued right. Please
	 * note that this method <b>does not</b> compare the Objects themselves, but
	 * instead compares the value behind them as specified by {@code value} in
	 * the enumerated type.
	 *
	 * @param other
	 *            the argued right to compare.
	 * @return {@code true} if this right is equal, {@code false} otherwise.
	 */
	public final boolean equal(Rights other) {
		return values == other.values;
	}

	/**
	 * Gets the value of this rank as seen by the protocol.
	 *
	 * @return the protocol value of this rank.
	 */
	public final int getProtocolValue() {
		return protocolValue;
	}

	/**
	 * Gets the value of this rank as seen by the server.
	 *
	 * @return the server value of this rank.
	 */
	public final int getValues() {
		return values;
	}

	public static Rights forValue(int value) {
		for (Rights r : Rights.values()) {
			if (r.values == value) {
				return r;
			}
		}
		return Rights.PLAYER;
	}
}
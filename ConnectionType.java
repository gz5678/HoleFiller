
/**
 * A enum class representing a connection type, 4 or 8
 * @author gzivor
 *
 */
public enum ConnectionType {
	FOUR(4),
	EIGHT(8);
	
	// The connectin type as an int
	private final int connectionType;
	
	/**
	 * Private constructory
	 * @param connectionType - The type of connection, 4 or 8
	 */
	private ConnectionType(int connectionType) { this.connectionType = connectionType; }
	
	/**
	 * Getter for the int value of the connection type.
	 * @return The int value of the connection type.
	 */
	public int getValue() { return connectionType; }
}

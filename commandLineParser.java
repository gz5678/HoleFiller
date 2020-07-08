import java.util.HashMap;
import java.io.File;

/**
 * A class which parses command line arguments for the hole filler program
 * @author gzivor
 *
 */
public class commandLineParser {
	protected HashMap<String, String> arguments;
	
	// The usage string
	public static final String USAGE = "Usage: -i imagePath -m maskPath "
			+ "-z normExponent -e epsilon -c connectionType -a mean|approx|fast";
	
	public static final String IMAGE_PATH_EXCPETION = "Image path doesn't exist";
	
	public static final String NORM_EXCEPTION = "Norm exponent given is not a double";
	
	public static final String EPSILON_NOT_DOUBLE = "Epsilon given is not a double";
	
	public static final String EPSILON_NEGATIVE = "Epsilon cannot be negative";
	
	public static final String CONNECTION_NOT_INT = "Connection type given is not an int";
	
	public static final String CONNECTION_VALUE = "Connection type must be 4 or 8";
	
	public static final String MODE_VALUE = "Mode must be mean|approx|fast";
	
	public static final String MEAN_MODE = "mean";

	public static final String APPROX_MODE = "approx";
	
	public static final String FAST_MODE = "fast";
	
	public static final String IMAGE_PATH_PREFIX = "-i";
	
	public static final String MASK_PATH_PREFIX = "-m";
	
	public static final String Z_PREFIX = "-z";
	
	public static final String EPS_PREFIX = "-e";
	
	public static final String CONNECTION_PREFIX = "-c";
	
	public static final String MODE_PREFIX = "-a";
	
	// The number of args the program expects to receive.
	public static final int NUM_OF_ARGS = 12;
	
	
	/**
	 * Constructor
	 * @param args - The arguments the program received.
	 * @throws IllegalArgumentException
	 */
	public commandLineParser(String[] args) throws IllegalArgumentException {
		if(args.length != NUM_OF_ARGS) {
			throw new IllegalArgumentException(USAGE);
		}

		arguments = new HashMap<String, String>();

		for(int i = 0; i < args.length; i+=2) {
			if(!args[i].startsWith("-")) {
				throw new IllegalArgumentException(USAGE);
			}
			arguments.put(args[i], args[i+1]);
		}
		checkArgs();
	}

	/**
	 * Checks that the arguments are valid.
	 */
	private void checkArgs() {
		// TODO Auto-generated method stub
		checkImagePaths(IMAGE_PATH_PREFIX);
		checkImagePaths(MASK_PATH_PREFIX);
		checkNorm();
		checkEpsilon();
		checkConnectionType();
		checkMode();
	}

	/**
	 * Check that the path to the image is valid.
	 * @param indentifier - Which image to check (mask or src)
	 */
	private void checkImagePaths(String indentifier) {
		// TODO Auto-generated method stub
		String imagePath = arguments.get(indentifier);
		if(imagePath == null) {
			throw new IllegalArgumentException(USAGE);
		}
		File imageFile = new File(imagePath);
		if(!imageFile.exists()) {
			throw new IllegalArgumentException(IMAGE_PATH_EXCPETION);
		}
	}
	
	/**
	 * Check that the norm exponent argument is valid.
	 */
	private void checkNorm() {
		String normString = arguments.get(Z_PREFIX);
		if(normString == null) {
			throw new IllegalArgumentException(USAGE);
		}
		try {
			double normExp = Double.parseDouble(normString);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(NORM_EXCEPTION, e);
		}
	}
	
	/**
	 * Check that the epsilon argument is valid.
	 */
	private void checkEpsilon() {
		String epsilonString = arguments.get(EPS_PREFIX);
		if(epsilonString == null) {
			throw new IllegalArgumentException(USAGE);
		}
		double epsilon;
		try {
			epsilon = Double.parseDouble(epsilonString);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(EPSILON_NOT_DOUBLE, e);
		}
		if(epsilon < 0) {
			throw new IllegalArgumentException(EPSILON_NEGATIVE);
		}
	}
	
	/**
	 * Check that the connection type argument is valid.
	 */
	private void checkConnectionType() {
		String connectionString = arguments.get(CONNECTION_PREFIX);
		if(connectionString == null) {
			throw new IllegalArgumentException(USAGE);
		}
		int connection;
		try {
			connection = Integer.parseInt(connectionString);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(CONNECTION_NOT_INT, e);
		}
		if(connection != 4 && connection != 8) {
			throw new IllegalArgumentException(CONNECTION_VALUE);
		}
	}
	
	/**
	 * Check that the mode argument is valid.
	 */
	private void checkMode() {
		String modeString = arguments.get(MODE_PREFIX);
		if(modeString == null) {
			throw new IllegalArgumentException(USAGE);
		}
		if(!(modeString.equals(MEAN_MODE) || modeString.equals(APPROX_MODE) || modeString.equals(FAST_MODE))) {
			System.out.println(modeString);
			throw new IllegalArgumentException(MODE_VALUE);
		}
	}
	
	/**
	 * Getter for the image path argument
	 * @return The image path argument
	 */
	public String getImagePath() { return arguments.get(IMAGE_PATH_PREFIX); }
	
	/**
	 * Getter for the mask path argument
	 * @return The mask path argument
	 */
	public String getMaskPath() { return arguments.get(MASK_PATH_PREFIX); }
	
	/**
	 * Getter for the norm exponent argument
	 * @return The norm exponent argument
	 */
	public double getZNorm() { return Double.parseDouble(arguments.get(Z_PREFIX)); }
	
	/**
	 * Getter for the epsilon argument
	 * @return The epsilon argument
	 */
	public double getEpsilon() { return Double.parseDouble(arguments.get(EPS_PREFIX)); }
	
	/**
	 * Getter for the connection type argument
	 * @return The connection type argument
	 */
	public ConnectionType getConnectionType() { 
		int connection = Integer.parseInt(arguments.get(CONNECTION_PREFIX));
		if(connection == 4) { return ConnectionType.FOUR; }
		return ConnectionType.EIGHT;
	}
	
	/**
	 * Getter for the mode argument
	 * @return The mode argument
	 */
	public String getMode() { return arguments.get(MODE_PREFIX); }
}

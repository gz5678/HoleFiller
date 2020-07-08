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
		checkImagePaths("-i");
		checkImagePaths("-m");
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
			throw new IllegalArgumentException("Image path doesn't exist");
		}
	}
	
	/**
	 * Check that the norm exponent argument is valid.
	 */
	private void checkNorm() {
		String normString = arguments.get("-z");
		if(normString == null) {
			throw new IllegalArgumentException(USAGE);
		}
		try {
			double normExp = Double.parseDouble(normString);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Norm exponent given is not a double", e);
		}
	}
	
	/**
	 * Check that the epsilon argument is valid.
	 */
	private void checkEpsilon() {
		String epsilonString = arguments.get("-e");
		if(epsilonString == null) {
			throw new IllegalArgumentException(USAGE);
		}
		double epsilon;
		try {
			epsilon = Double.parseDouble(epsilonString);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Epsilon given is not a double", e);
		}
		if(epsilon < 0) {
			throw new IllegalArgumentException("Epsilon cannot be negative");
		}
	}
	
	/**
	 * Check that the connection type argument is valid.
	 */
	private void checkConnectionType() {
		String connectionString = arguments.get("-c");
		if(connectionString == null) {
			throw new IllegalArgumentException(USAGE);
		}
		int connection;
		try {
			connection = Integer.parseInt(connectionString);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Connection type given is not an int", e);
		}
		if(connection != 4 && connection != 8) {
			throw new IllegalArgumentException("Connection type must be 4 or 8");
		}
	}
	
	/**
	 * Check that the mode argument is valid.
	 */
	private void checkMode() {
		String modeString = arguments.get("-a");
		if(modeString == null) {
			throw new IllegalArgumentException(USAGE);
		}
		if(!(modeString.equals("mean") || modeString.equals("approx") || modeString.equals("fast"))) {
			System.out.println(modeString);
			throw new IllegalArgumentException("Mode must be mean|approx|fast");
		}
	}
	
	/**
	 * Getter for the image path argument
	 * @return The image path argument
	 */
	public String getImagePath() { return arguments.get("-i"); }
	
	/**
	 * Getter for the mask path argument
	 * @return The mask path argument
	 */
	public String getMaskPath() { return arguments.get("-m"); }
	
	/**
	 * Getter for the norm exponent argument
	 * @return The norm exponent argument
	 */
	public double getZNorm() { return Double.parseDouble(arguments.get("-z")); }
	
	/**
	 * Getter for the epsilon argument
	 * @return The epsilon argument
	 */
	public double getEpsilon() { return Double.parseDouble(arguments.get("-e")); }
	
	/**
	 * Getter for the connection type argument
	 * @return The connection type argument
	 */
	public int getConnectionType() { return Integer.parseInt(arguments.get("-c")); }
	
	/**
	 * Getter for the mode argument
	 * @return The mode argument
	 */
	public String getMode() { return arguments.get("-a"); }
}

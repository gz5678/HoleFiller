/**
 * A class which represents a non-negative double
 * @author gzivor
 *
 */
public class PositiveDouble {
	private double val;
	
	/**
	 * Constructor
	 * @param num - The value of the the double.
	 */
	public PositiveDouble(double num) throws IllegalArgumentException{
		// TODO Auto-generated constructor stub
		if(num < 0) {
			throw new IllegalArgumentException("Value given needs to be non-negative");
		}
		this.val = num;
	}
	
	/**
	 * Returns the value as a double.
	 * @return The value
	 */
	public double getValue() { return this.val; }
}


public class PositiveDouble {
	private double val;
	
	public PositiveDouble(double num) {
		// TODO Auto-generated constructor stub
		if(num < 0) {
			throw new IllegalArgumentException("Value given needs to be non-negative");
		}
		this.val = num;
	}
	
	public double getValue() { return this.val; }
}

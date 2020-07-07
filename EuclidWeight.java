
public class EuclidWeight implements ImageWeightFunction {
	private double zExponent;
	private double epsilon;
	
	public EuclidWeight(double z, double epsilon) {
		this.zExponent = z;
		this.epsilon = epsilon;
	}
	
	public PositiveDouble calcWeight(PixelCoordinate u, PixelCoordinate v) {
		// This design allows without more work also 3d coordinates.
		int[] uArrCords = u.getArrayCords();
		int[] vArrCords = v.getArrayCords();
		if(uArrCords.length != vArrCords.length) {
			throw new IllegalArgumentException("Coordinates are not of the same size");
		}
		int sumSquaredDiff = 0;
		for (int i = 0; i < uArrCords.length; i++) {
			sumSquaredDiff += Math.pow(uArrCords[i] - vArrCords[i], 2); // This hides the conversion to int
		}
		double zNorm = Math.pow(Math.sqrt(sumSquaredDiff), this.zExponent);
		return new PositiveDouble(1 / (zNorm + this.epsilon));
	}
}

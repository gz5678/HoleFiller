/**
 * A interface for image weight functions.
 * @author gzivor
 *
 */
public interface ImageWeightFunction {
	/**
	 * The method weight functions for images need to implement: Given two
	 * pixel coordinates, calculate the weight.
	 * @param u - The first pixel
	 * @param v - The second pixel
	 * @return - The weight between the 2 given pixels.
	 */
	public PositiveDouble calcWeight(PixelCoordinate u, PixelCoordinate v);
}

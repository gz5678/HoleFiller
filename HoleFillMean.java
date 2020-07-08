import org.opencv.core.Mat;

/**
 * A class which represents a hole filler. Given an image, a connection type and
 * and weight function, this will fill the hole in the image accordingly. The class
 * assumes that there is only one hole in the image.
 * @author gzivor
 *
 */
public class HoleFillMean extends HoleFiller{
	
	/**
	 * Constructor
	 * @param image - A matrix representing an image.
	 * @param connectionType - The type of connection for boundary pixels. Can be 4 or 8.
	 * @param weightFunction - The weight function for calculating the hole pixels color.
	 */
	public HoleFillMean(final Mat image, ConnectionType connectionType, ImageWeightFunction weightFunction) {
		super(image, connectionType, weightFunction);
	}
	
	public void fillHole() {
		while(!hole.isFilled()) {
			PixelCoordinate holePixel = hole.getHolePixel();
			double sumNumerator = 0;
			double sumDenominator = 0;
			for (PixelCoordinate boundaryPixel: hole.getBoundaryPixels()) {
				double weightVal = weightFunction.calcWeight(holePixel, boundaryPixel).getValue();
				sumNumerator += weightVal * filledImage.get(boundaryPixel.getRow(), boundaryPixel.getCol())[0];
				sumDenominator += weightVal;
			}
			filledImage.put(holePixel.getRow(), holePixel.getCol(), (sumNumerator / sumDenominator));
		}
		
	}
}

import org.opencv.core.Mat;
import java.util.HashSet;

/**
 * A class which fills a hole by approximating the mean border function in
 * HoleFillMean.
 * @author gzivor
 *
 */
public class HoleFillApprox extends HoleFiller {
	
	/**
	 * Constructor
	 * @param image - The given image with a hole
	 * @param connectionType - The connection between neighbours (4 or 8)
	 * @param weightFunction - The weight function between 2 pixel coordinates.
	 */
	public HoleFillApprox(final Mat image, ConnectionType connectionType, ImageWeightFunction weightFunction) {
		super(image, connectionType, weightFunction);
	}
	
	@Override
	public void fillHole() {
		HashSet<PixelCoordinate> outmostMissingPixels = getOutmostMissingPixels(hole.getBoundaryPixels());
		double sumNumerator = 0;
		double sumDenominator = 0;
		double weight;

		// We fill the outmost missing pixels (closest to boundary) until there are no more left.
		while(!outmostMissingPixels.isEmpty()) {
			for(PixelCoordinate edgePixel: outmostMissingPixels) {
				sumNumerator = 0;
				sumDenominator = 0;
				for (int i = 0; i < this.connectionType.getValue(); i++) {
					PixelCoordinate neighbour = edgePixel.add(hole.getNeighboursMap().get(i));
					double neighbourVal = filledImage.get(neighbour.getRow(), neighbour.getCol())[0];

					// If the neighbour is in the hole, give it weight 0.
					weight = (neighbourVal == Constants.HOLE_PIXEL_VAL) ? 0 : weightFunction.calcWeight(edgePixel, neighbour).getValue();
					sumNumerator += weight * neighbourVal;
					sumDenominator += weight;
				}
				filledImage.put(edgePixel.getRow(), edgePixel.getCol(), (sumNumerator / sumDenominator));
			}
			// Get the next batch of outmost missing pixels.
			outmostMissingPixels = getOutmostMissingPixels(outmostMissingPixels);
		}
	}
	
	/**
	 * Gets a set of the outmost missing pixels (closest to boundary) in relation to
	 * the current filled image.
	 * @param boundaryPixels - The current boundary of the hole.
	 * @return A set of the outmost missing pixels
	 */
	private HashSet<PixelCoordinate> getOutmostMissingPixels(HashSet<PixelCoordinate> boundaryPixels) {
		HashSet<PixelCoordinate> outmostMissingPixels = new HashSet<PixelCoordinate>();
		for(PixelCoordinate boundaryPixel: boundaryPixels) {
			for (int i = 0; i < this.connectionType.getValue(); i++) {
				PixelCoordinate neighbour = boundaryPixel.add(hole.getNeighboursMap().get(i));
				
				// We want all the pixels that are in the whole since they are one pixel away from the boundary.
				if(filledImage.get(neighbour.getRow(), neighbour.getCol())[0] == Constants.HOLE_PIXEL_VAL) {
					outmostMissingPixels.add(neighbour);
				}
			}
		}
		return outmostMissingPixels;
	}
}

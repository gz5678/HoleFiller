import org.opencv.core.Mat;
import java.util.HashSet;

public class HoleFillApprox extends HoleFiller {
	
	public HoleFillApprox(final Mat image, int connectionType, ImageWeightFunction weightFunction) {
		super(image, connectionType, weightFunction);
	}
	
	public void fillHole() {
		HashSet<PixelCoordinate> outmostMissingPixels = getOutmostMissingPixels(hole.getBoundaryPixels());
		double sumNumerator = 0;
		double sumDenominator = 0;
		double weight;
		while(!outmostMissingPixels.isEmpty()) {
			for(PixelCoordinate edgePixel: outmostMissingPixels) {
				sumNumerator = 0;
				sumDenominator = 0;
				for (int i = 0; i < this.connectionType; i++) {
					PixelCoordinate neighbour = edgePixel.add(hole.getNeighboursMap().get(i));
					double neighbourVal = filledImage.get(neighbour.getRow(), neighbour.getCol())[0];
					weight = (neighbourVal == Constants.HOLE_PIXEL_VAL) ? 0 : weightFunction.calcWeight(edgePixel, neighbour).getValue();
					sumNumerator += weight * neighbourVal;
					sumDenominator += weight;
				}
				filledImage.put(edgePixel.getRow(), edgePixel.getCol(), (sumNumerator / sumDenominator));
			}
			outmostMissingPixels = getOutmostMissingPixels(outmostMissingPixels);
		}
	}
	
	private HashSet<PixelCoordinate> getOutmostMissingPixels(HashSet<PixelCoordinate> boundaryPixels) {
		HashSet<PixelCoordinate> outmostMissingPixels = new HashSet<PixelCoordinate>();
		for(PixelCoordinate boundaryPixel: boundaryPixels) {
			for (int i = 0; i < this.connectionType; i++) {
				PixelCoordinate neighbour = boundaryPixel.add(hole.getNeighboursMap().get(i));
				if(filledImage.get(neighbour.getRow(), neighbour.getCol())[0] == Constants.HOLE_PIXEL_VAL) {
					outmostMissingPixels.add(neighbour);
				}
			}
		}
		return outmostMissingPixels;
	}
}

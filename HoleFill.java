import org.opencv.core.Mat;

public class HoleFill {
	private final Mat image;
	private int connectionType;
	private ImageWeightFunction weightFunction;
	private Hole hole;
	private Mat filledImage;
	
	
	public HoleFill(final Mat image, int connectionType, ImageWeightFunction weightFunction) {
		this.image = image;
		this.connectionType = connectionType;
		this.weightFunction = weightFunction;
		this.filledImage = image.clone();
		this.hole = new Hole(image, connectionType);
	}
	
	public void fillHole() {
		//1st Pass - Get hole and boundary
		findHole();

		//2nd Pass - Fill hole
		while(!hole.isFilled()) {
			PixelCoordinate holePixel = hole.getHolePixel();
			double sumNumerator = 0;
			double sumDenominator = 0;
			for (PixelCoordinate boundaryPixel: hole.getBoundaryPixels()) {
				double weightVal = weightFunction.calcWeight(holePixel, boundaryPixel).getValue();
				sumNumerator += weightVal * image.get(boundaryPixel.getRow(), boundaryPixel.getCol())[0];
				sumDenominator += weightVal;
			}
			filledImage.put(holePixel.getRow(), holePixel.getCol(), (sumNumerator / sumDenominator));
		}
		
	}
	private void findHole() {
		for(int r = 0; r < image.rows(); r++) {
			for(int c = 0; c < image.cols(); c++) {
				if(image.get(r, c)[0] == -1) {
					hole.add(new PixelCoordinate(r, c));
				}
			}
		}
	}
	
	public Mat getFilledImage() { return this.filledImage; }
	public Mat getOriginalImage() { return this.image; }
	public Hole getHole() { return this.hole; }
	public int getConnectionType() { return this.connectionType; }
}

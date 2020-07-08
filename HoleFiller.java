import org.opencv.core.Mat;

/**
 * An abstract class which represents a hole filler
 * @author gzivor
 *
 */
public abstract class HoleFiller {
	protected Mat image;
	protected Hole hole;
	protected Mat filledImage;
	protected int connectionType;
	protected ImageWeightFunction weightFunction;
	
	/**
	 * Constructor
	 * @param image - The given image to fill a hole in.
	 */
	public HoleFiller(final Mat image, int connectionType, ImageWeightFunction weightFunction) {
		this.image = image;
		this.filledImage = image.clone();
		this.connectionType = connectionType;
		this.weightFunction = weightFunction;
		this.hole = new Hole(image, connectionType);
		findHole();
	}
	
	/**
	 * Fills the hole in the given image
	 */
	public abstract void fillHole();
	
	/**
	 * Find the hole in the given image.
	 */
	private void findHole() {
		for(int r = 0; r < image.rows(); r++) {
			for(int c = 0; c < image.cols(); c++) {
				if(image.get(r, c)[0] == Constants.HOLE_PIXEL_VAL) {
					hole.add(new PixelCoordinate(r, c));
				}
			}
		}
	}
	
	/**
	 * Getter for the hole filled image
	 * @return - A matrix representing the hole filled image.
	 */
	public Mat getFilledImage() { return this.filledImage; }
	
	/**
	 * Getter for the original image
	 * @return - A matrix representing the original image.
	 */
	public Mat getOriginalImage() { return this.image; }
	
	/**
	 * Getter for the connection type
	 * @return The given connection type
	 */
	public int getConnectionType() { return this.connectionType; }
}

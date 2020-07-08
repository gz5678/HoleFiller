import java.util.HashSet;
import org.opencv.core.Mat;
import java.util.HashMap;
import java.util.ArrayDeque;

/**
 * A class which represents a hole in the given image.
 * @author gzivor
 *
 */
public class Hole {
	private ArrayDeque<PixelCoordinate> holeCoordinates;
	private HashSet<PixelCoordinate> boundaryCoordinates;
	private ConnectionType connectionType;
	private Mat image;
	private HashMap<Integer, PixelCoordinate> neighboursIndexes;
	
	/**
	 * Constructor
	 * @param image - The image the hole is in.
	 * @param connectionType - The type of connection between pixels.
	 */
	public Hole(final Mat image, ConnectionType connectionType) {
		this.holeCoordinates = new ArrayDeque<PixelCoordinate>();
		this.boundaryCoordinates = new HashSet<PixelCoordinate>();
		this.connectionType = connectionType;
		this.neighboursIndexes = neighboursMap();
		this.image = image;
	}
	
	/**
	 * Add a pixel to the hole and adds it's neighbours (which are
	 * not in the hole) to the boundary.
	 * @param pix - A given pixel in the whole.
	 */
	public void add(PixelCoordinate pix) {
		holeCoordinates.push(pix);
		for (int i = 0; i < this.connectionType.getValue(); i++) {
			PixelCoordinate neighbour = pix.add(neighboursIndexes.get(i));

			// Check if the neighbor is a hole pixel.
			if(image.get(neighbour.getRow(), neighbour.getCol())[0] == Constants.HOLE_PIXEL_VAL) { continue; }
			boundaryCoordinates.add(neighbour);
		}
	}
	
	/**
	 * Checks if the hole is filled.
	 * @return true if the hole is filled and false otherwise.
	 */
	public boolean isFilled() { return holeCoordinates.isEmpty(); }
	
	/**
	 * Removes a pixel from the hole and returns it.
	 * @return A pixel from hole
	 */
	public PixelCoordinate getHolePixel() { return holeCoordinates.pop();}
	
	/**
	 * Getter for the boundary coordinates.
	 * @return The boundary coordinates of the hole.
	 */
	public HashSet<PixelCoordinate> getBoundaryPixels() { return boundaryCoordinates; }
	
	/**
	 * Getter for the set of pixels in the hole.
	 * @return
	 */
	public ArrayDeque<PixelCoordinate> getHolePixels() { return holeCoordinates; }
	
	/**
	 * Return the neighbours map for a pixel.
	 * @return The neighbours map for a pixel.
	 */
	protected HashMap<Integer, PixelCoordinate> getNeighboursMap() { return this.neighboursIndexes; }
	
	/**
	 * Creates a map of neighbours of a pixel
	 * @return A map of neighbours of a pixel
	 */
	private HashMap<Integer, PixelCoordinate> neighboursMap() {
		HashMap<Integer, PixelCoordinate> neightbours = new HashMap<Integer, PixelCoordinate>();
		neightbours.put(0, new PixelCoordinate(0, 1));
		neightbours.put(1, new PixelCoordinate(1, 0));
		neightbours.put(2, new PixelCoordinate(0, -1));
		neightbours.put(3, new PixelCoordinate(-1, 0));
		neightbours.put(4, new PixelCoordinate(-1, 1));
		neightbours.put(5, new PixelCoordinate(1, -1));
		neightbours.put(6, new PixelCoordinate(-1, -1));
		neightbours.put(7, new PixelCoordinate(-1, 1));
		return neightbours;
	}
}

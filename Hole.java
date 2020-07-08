import java.util.HashSet;
import org.opencv.core.Mat;
import java.util.HashMap;
import java.util.ArrayDeque;


public class Hole {
	private ArrayDeque<PixelCoordinate> holeCoordinates;
	private HashSet<PixelCoordinate> boundaryCoordinates;
	private int connectionType;
	private Mat image;
	private HashMap<Integer, PixelCoordinate> neighboursIndexes;
	
	public Hole(final Mat image, int connectionType) {
		this.holeCoordinates = new ArrayDeque<PixelCoordinate>();
		this.boundaryCoordinates = new HashSet<PixelCoordinate>();
		this.connectionType = connectionType;
		this.neighboursIndexes = neighboursMap();
		this.image = image;
	}
	
	public void add(PixelCoordinate pix) {
		holeCoordinates.push(pix);
		for (int i = 0; i < this.connectionType; i++) {
			PixelCoordinate neighbour = pix.add(neighboursIndexes.get(i));

			// Check if the neighbor is a hole pixel.
			if(image.get(neighbour.getRow(), neighbour.getCol())[0] == Constants.HOLE_PIXEL_VAL) { continue; }
			boundaryCoordinates.add(neighbour);
		}
	}
	
	public boolean isFilled() { return holeCoordinates.isEmpty(); }
	public PixelCoordinate getHolePixel() { return holeCoordinates.pop();}
	
	public HashSet<PixelCoordinate> getBoundaryPixels() { return boundaryCoordinates; }
	public ArrayDeque<PixelCoordinate> getHolePixels() { return holeCoordinates; }
	protected HashMap<Integer, PixelCoordinate> getNeighboursMap() { return this.neighboursIndexes; }
	
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

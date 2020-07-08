import java.util.Objects;

/**
 * A class which represents a pixel coordinate.
 * @author gzivor
 *
 */
public class PixelCoordinate {
	private int row;
	private int col;
	
	/**
	 * Constructor
	 * @param row - The row coordinate of the pixel
	 * @param col - The col coordinate of the pixel
	 */
	public PixelCoordinate(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;

		if(obj == null || obj.getClass()!= this.getClass()) 
            return false;

		PixelCoordinate other = (PixelCoordinate) obj;
		
		return (other.row == this.row && other.col == this.col);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(row, col);
	}
	
	/**
	 * Adds the given pixel to this one element-wise and return a new pixel with the result.
	 * @param right - The pixel to add to this pixel.
	 * @return A new pixel with sum.
	 */
	public PixelCoordinate add(PixelCoordinate right) {
		return new PixelCoordinate(this.row + right.getRow(), this.col + right.getCol());
	}
	
	/**
	 * Getter for the row coordinate of the pixel
	 * @return The row coordinate of the pixel
	 */
	public int getRow() { return this.row; }
	
	/**
	 * Getter for the col coordinate of the pixel
	 * @return The col coordinate of the pixel
	 */
	public int getCol() { return this.col; }
	
	/**
	 * Returns an array representation of the pixel
	 * @return An array representation of the pixel
	 */
	public int[] getArrayCords() { return new int[]{this.row, this.col}; }
}

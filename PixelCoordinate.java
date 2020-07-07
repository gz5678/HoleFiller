import java.util.Objects;

public class PixelCoordinate {
	private int row;
	private int col;
	
	public PixelCoordinate(int row, int col) {
		// TODO Auto-generated constructor stub
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
	
	public PixelCoordinate add(PixelCoordinate right) {
		return new PixelCoordinate(this.row + right.getRow(), this.col + right.getCol());
	}
	
	public int getRow() { return this.row; }
	public int getCol() { return this.col; }
	public int[] getArrayCords() { return new int[]{this.row, this.col}; }
}

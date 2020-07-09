import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.CvType;

/**
 * A class for filling a hole using Fourier Transform and convolution.
 * @author gzivor
 *
 */
public class HoleFillFast extends HoleFiller {
	
	/**
	 * Constructor
	 * @param image - The given image with a hole
	 * @param connectionType - The connection between neighbours (4 or 8)
	 * @param weightFunction - The weight function between 2 pixel coordinates.
	 */
	public HoleFillFast(final Mat image, ConnectionType connectionType, ImageWeightFunction weightFunction) {
		super(image, connectionType, weightFunction);
	}
	
	public void fillHole() {
		Mat weightKernel = getWeightKernel();
		Mat valNumeratorMatrix = createValNumeratorMatrix();
		Mat valDenominatorMatrix = createValDenominatorMatrix();
				
		
		// Get optimal size of padded matrix and DFT all matrices
		// The result is complex type matrices
		int optRowsSize = Core.getOptimalDFTSize(weightKernel.rows());
		int optColsSize = Core.getOptimalDFTSize(weightKernel.cols());
		Mat dftWeights = applyDFT(weightKernel, optRowsSize, optColsSize);
		Mat dftNumeratorValues = applyDFT(valNumeratorMatrix, optRowsSize, optColsSize);
		Mat dftDenominatorValues = applyDFT(valDenominatorMatrix, optRowsSize, optColsSize);
		
		// Element-wise multiplication for convolution
		Mat numeratorMat = dftWeights.mul(dftNumeratorValues);
		Mat denominatorMat = dftWeights.mul(dftDenominatorValues);
		
		// IDFT the convolution result
		numeratorMat = applyIDFT(numeratorMat);
		denominatorMat = applyIDFT(denominatorMat);
		
		// Update the hole pixels
		updateHolePixels(numeratorMat, denominatorMat);
	}
	
	/**
	 * Applies DFT on a given matrix with padding to reach shape (rows,cols)
	 * @param matrix - The given matrix to apply DFT on
	 * @param rows - The row size wanted.
	 * @param cols - The col size wanted.
	 * @return - The given matrix after applying DFT on it.
	 */
	private Mat applyDFT(Mat matrix, int rows, int cols) {
		
		// Initialize padded matrix
		Mat padded = new Mat();
		Core.copyMakeBorder(matrix, padded, 0, rows - matrix.rows(), 0, cols - matrix.cols(), Core.BORDER_CONSTANT, Scalar.all(0));
		
		// Convert to float to make room for complex representation
		padded.convertTo(padded, CvType.CV_32F);
		
		// Make room for real and imaginary parts of the complex representation.
		List<Mat> planes = new ArrayList<>();
		planes.add(padded);
		planes.add(Mat.zeros(padded.size(), CvType.CV_32F));
		
		Mat complexImage = new Mat();
		Core.merge(planes, complexImage);
		
		// Do DFT
		Core.dft(complexImage, complexImage);
		return complexImage;
	}
	
	/**
	 * Applies IDFT on a given matrix.
	 * @param matrix - The matrix to apply IDFT on
	 * @return - The given matrix after applying IDFT on it.
	 */
	private Mat applyIDFT(Mat matrix) {
		Core.idft(matrix, matrix);
		List<Mat> planes = new ArrayList<>();
		Core.split(matrix, planes);
		Mat res = planes.get(0);
		Core.normalize(res,res,0.0,1.0,Core.NORM_MINMAX,CvType.CV_32FC1);
		return res;
	}
	
	/**
	 * Creates the values matrix needed for the convolution in the numerator of the algorithm.
	 * Will assign 0 to all values that are not in the boundary and will leave the boundary
	 * pixels with the same value.
	 * @return - The above described matrix.
	 */
	private Mat createValNumeratorMatrix() {
		Mat valMatrix = this.image.clone();
		for(int r = 0; r < valMatrix.rows(); r++) {
			for(int c = 0; c < valMatrix.cols(); c++) {
				PixelCoordinate curPixel = new PixelCoordinate(r, c);
				if(!this.hole.getBoundaryPixels().contains(curPixel)) {
					valMatrix.put(r, c, 0);
				}
			}
		}
		return valMatrix;
	}
	
	/**
	 * Creates the values matrix needed for the convolution in the denominator of the algorithm.
	 * Will assign 0 to all values that are not in the boundary and will assign the boundary
	 * pixels 1. 
	 * @return - The above described matrix.
	 */
	private Mat createValDenominatorMatrix() {
		Mat valMatrix = this.image.clone();
		for(int r = 0; r < valMatrix.rows(); r++) {
			for(int c = 0; c < valMatrix.cols(); c++) {
				PixelCoordinate curPixel = new PixelCoordinate(r, c);
				if(this.hole.getBoundaryPixels().contains(curPixel)) {
					valMatrix.put(r, c, 1);
				}
				else {
					valMatrix.put(r, c, 0);
				}
			}
		}
		return valMatrix;
	}
	
	/**
	 * Creates the weights kernel for the algorithm. The matrix is the weight,
	 * given by the weight function, between every pixel and the center of the
	 * image.
	 * @return
	 */
	private Mat getWeightKernel() {
		// We need the kernel to be twice as large as the image to cover all pixels
		// when the corners are in the center of the kernel.
		Mat weightKernel = new Mat(image.rows()*2, image.cols()*2, CvType.CV_32F);
		
		// We want the weights to be relational to the middle pixel
		int middleRows = (int)(image.rows() / 2);
		int middleCols = (int)(image.cols() / 2);
		for(int r = 0; r < weightKernel.rows(); r++) {
			for(int c = 0; c < weightKernel.cols(); c++) {
				double weight = weightFunction.calcWeight(new PixelCoordinate(r, c), new PixelCoordinate(middleRows, middleCols)).getValue();
				weightKernel.put(r, c, weight);
			}
		}
		return weightKernel;
	}
	
	/**
	 * Updated the pixels in the whole to the values we got from the convolution.
	 * @param numeratorMat - The numerator matrix in the algorithm after convolution.
	 * @param denominatorMat - The denominator matrix in the algorithm after convolution.
	 */
	private void updateHolePixels(Mat numeratorMat, Mat denominatorMat) {
		int rowsFactor = numeratorMat.rows() - image.rows();
		int colsFactor = numeratorMat.cols() - image.cols();
		Mat divided = new Mat();
		Core.divide(numeratorMat, denominatorMat, divided);
		while(!hole.isFilled()) {
			PixelCoordinate holePix = hole.getHolePixel();
			double pixVal = divided.get((int)(rowsFactor / 2) + holePix.getRow(), (int)(colsFactor / 2) + holePix.getCol())[0];
			filledImage.put(holePix.getRow(), holePix.getCol(), pixVal);
		}
	}
}

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.CvType;

public class HoleFillFast extends HoleFiller {
	
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
	}
	
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
	
	private Mat applyIDFT(Mat matrix) {
		Core.idft(matrix, matrix);
		List<Mat> planes = new ArrayList<>();
		Core.split(matrix, planes);
		return planes.get(0);
	}
	
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
	
	private Mat getWeightKernel() {
		// We need the kernel to be twice as large as the image to cover all pixels
		// when the corners are in the center of the kernel.
		Mat weightKernel = new Mat(image.rows()*2, image.cols()*2, CvType.CV_32F);
		
		// We want the weights to be relational to the middle pixel
		int middleRows = image.rows() / 2;
		int middleCols = image.cols() / 2;
		for(int r = 0; r < weightKernel.rows(); r++) {
			for(int c = 0; c < weightKernel.cols(); c++) {
				double weight = weightFunction.calcWeight(new PixelCoordinate(r, c), new PixelCoordinate(middleRows, middleCols)).getValue();
				weightKernel.put(r, c, weight);
			}
		}
		return weightKernel;
	}
}

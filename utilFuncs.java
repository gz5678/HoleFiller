import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs; 
import org.opencv.imgproc.Imgproc;

/**
 * A class with some utility functions for working with images.
 * @author gzivor
 *
 */
public class utilFuncs {
	
	/**
	 * Reads an image, converts to grayscale, normalizes to 0-1 and applies the mask
	 * @param imgPath - The path to the image.
	 * @param maskPath - The path to the mask.
	 * @return A image ready for hole filling after the above manipulations.
	 */
	public static Mat readImage(String imgPath, String maskPath) throws IllegalArgumentException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// Read the image as grayscale
		Mat srcImage = Imgcodecs.imread(imgPath);
		
		// Allocate a new mat for the grayscale conversion.
		Mat grayImage = new Mat();
		
		// Convert to grayscale and normalize between 0 and 1
		Imgproc.cvtColor(srcImage, grayImage, Imgproc.COLOR_RGB2GRAY);
		Core.normalize(grayImage,grayImage,0.0,1.0,Core.NORM_MINMAX,CvType.CV_32FC1);
		
		// Read the mask to mat. 0 is where the hole is and 1 is not in the hole
		Mat srcMask = Imgcodecs.imread(maskPath);
		
		// Get a Mat with the mask applied on the src image
		applyMask(grayImage, srcMask);
		
		return grayImage;
	}
	
	/**
	 * Apply the mask on the image. The function will put -1 where there is 0 in
	 * the mask and will leave the value the same where there is 0.
	 * @param srcImage - The image to apply the mask on.
	 * @param srcMask - The mask to apply on the image.
	 * @throws IllegalArgumentException
	 */
	private static void applyMask(Mat srcImage, Mat srcMask) throws IllegalArgumentException{
		if((srcImage.rows() != srcMask.rows()) || (srcImage.cols() != srcMask.cols())) {
			throw new IllegalArgumentException("Image and mask are not the same size");
		}
		for(int r = 0; r < srcMask.rows(); r++) {
			for(int c = 0; c < srcMask.cols(); c++) {
				if(srcMask.get(r, c)[0] == 0) {
					srcImage.put(r, c, Constants.HOLE_PIXEL_VAL);
				}
			}
		}
	}
	
	/**
	 * Writes an image, given as a matrix, to a file
	 * @param image - A matrix representing an image.
	 */
	public static void writeImage(Mat image) {
		Mat filledScaledImage = new Mat();
		Core.normalize(image,filledScaledImage,0.0,255.0,Core.NORM_MINMAX,CvType.CV_32FC1);
		Imgcodecs.imwrite("filledImage.jpg", filledScaledImage);
	}
}

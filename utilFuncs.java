import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs; 
import org.opencv.imgproc.Imgproc;

public class utilFuncs {
	public static Mat readImage(String imgPath, String maskPath) {
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
	
	private static void applyMask(Mat srcImage, Mat srcMask) {
		//TODO: Check if the size of the image and the mask are the same.
		for(int r = 0; r < srcMask.rows(); r++) {
			for(int c = 0; c < srcMask.cols(); c++) {
				if(srcMask.get(r, c)[0] == 0) {
					srcImage.put(r, c, Constants.HOLE_PIXEL_VAL);
				}
			}
		}
	}
	
	public static void writeImage(Mat image) {
		Mat filledScaledImage = new Mat();
		Core.normalize(image,filledScaledImage,0.0,255.0,Core.NORM_MINMAX,CvType.CV_32FC1);
		Imgcodecs.imwrite("mergedImage2.jpg", filledScaledImage);
	}
	
	//TODO: DELETE THIS
	public static void printPixels(Mat image) {
		for(int r = 0; r < 50; r++) {
			for(int c = 0; c < 50; c++) {
				System.out.println(image.get(r, c)[0]);
			}
		}
	}
}

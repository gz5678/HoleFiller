import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class HoleFillRunner {

	public static void main(String[] args) {
		try {
			commandLineParser argsParser = new commandLineParser(args);
			String imagePath = argsParser.getImagePath();
			String maskPath = argsParser.getMaskPath();
			double z = argsParser.getZNorm();
			double epsilon = argsParser.getEpsilon();
			int connectionType = argsParser.getConnectionType();
			Mat mergedImage = utilFuncs.readImage(imagePath, maskPath);
			HoleFiller holeFiller;
			switch(argsParser.getMode()) {
				case "approx":
					holeFiller = new HoleFillApprox(mergedImage, connectionType, new EuclidWeight(z, epsilon));
					break;
				default:
					holeFiller = new HoleFillMean(mergedImage, connectionType, new EuclidWeight(z, epsilon));	
			}
			holeFiller.fillHole();
			utilFuncs.writeImage(holeFiller.getFilledImage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

}

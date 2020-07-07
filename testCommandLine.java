import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class testCommandLine {
	
	public static final String IMAGE_PATH = "C:\\Users\\gzivor\\eclipse-workspace\\LightricksTest\\src\\lena.jpg";
	public static final String MASK_PATH = "C:\\Users\\gzivor\\eclipse-workspace\\LightricksTest\\src\\lena_mask.jpg";
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testTooManyArgs() {
		String[] args = new String[]{"-i"};
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(commandLineParser.USAGE);
		commandLineParser parser = new commandLineParser(args);
	}
	
	@Test
	public void testBadPrefix() {
		String[] args = new String[]{"-i", "fdsfdsfds", "-m", "rrerf", "fdsfds", "fdsfdsf", "-e", "fdsf", "-c", "fedfds"};
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(commandLineParser.USAGE);
		commandLineParser parser = new commandLineParser(args);
	}
	
	@Test
	public void testNoImageArgPrefix() {
		String[] args = new String[]{"-k", "fdsfdsfds", "-m", "rrerf", "-z", "fdsfdsf", "-e", "fdsf", "-c", "fedfds"};
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(commandLineParser.USAGE);
		commandLineParser parser = new commandLineParser(args);
	}
	
	@Test
	public void testImageFileDoesntExist() {
		String[] args = new String[]{"-i", "C:\\Users\\gzivor\\eclipse-workspace\\LightricksTest\\src\\le.jpg", "-m", "rrerf", "-z", "fdsfdsf", "-e", "fdsf", "-c", "fedfds"};
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Image path doesn't exist");
		commandLineParser parser = new commandLineParser(args);
	}
	
	@Test
	public void testNoMaskArgPrefix() {
		String[] args = new String[]{"-i", IMAGE_PATH, "-k", "rrerf", "-z", "fdsfdsf", "-e", "fdsf", "-c", "fedfds"};
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(commandLineParser.USAGE);
		commandLineParser parser = new commandLineParser(args);
	}
	
	@Test
	public void testMaskFileDoesntExist() {
		String[] args = new String[]{"-i", IMAGE_PATH, "-m", "C:\\Users\\gzivor\\eclipse-workspace\\LightricksTest\\src\\lena_mask_3.jpg", "-z", "fdsfdsf", "-e", "fdsf", "-c", "fedfds"};
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Image path doesn't exist");
		commandLineParser parser = new commandLineParser(args);
	}
	
	@Test
	public void testNoZArgPrefix() {
		String[] args = new String[]{"-i", IMAGE_PATH, "-m", MASK_PATH, "-k", "fdsfdsf", "-e", "fdsf", "-c", "fedfds"};
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(commandLineParser.USAGE);
		commandLineParser parser = new commandLineParser(args);
	}
	
	@Test
	public void testZNotDouble() {
		String[] args = new String[]{"-i", IMAGE_PATH, "-m", MASK_PATH, "-z", "fdsfdsf", "-e", "fdsf", "-c", "fedfds"};
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Norm exponent given is not a double");
		commandLineParser parser = new commandLineParser(args);
	}
	
	@Test
	public void testNoEpsilonArgPrefix() {
		String[] args = new String[]{"-i", IMAGE_PATH, "-m", MASK_PATH, "-z", "3", "-k", "fdsf", "-c", "fedfds"};
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(commandLineParser.USAGE);
		commandLineParser parser = new commandLineParser(args);
	}
	
	@Test
	public void testEpsilonNotDouble() {
		String[] args = new String[]{"-i", IMAGE_PATH, "-m", MASK_PATH, "-z", "2", "-e", "fdsf", "-c", "fedfds"};
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Epsilon given is not a double");
		commandLineParser parser = new commandLineParser(args);
	}
	
	@Test
	public void testEpsilonRange() {
		String[] args = new String[]{"-i", IMAGE_PATH, "-m", MASK_PATH, "-z", "2", "-e", "-2", "-c", "fedfds"};
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Epsilon cannot be negative");
		commandLineParser parser = new commandLineParser(args);
	}
	
	@Test
	public void testNoConnectArgPrefix() {
		String[] args = new String[]{"-i", IMAGE_PATH, "-m", MASK_PATH, "-z", "2", "-e", "0.5", "-k", "fedfds"};
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(commandLineParser.USAGE);
		commandLineParser parser = new commandLineParser(args);
	}
	
	@Test
	public void testConnectNotInt() {
		String[] args = new String[]{"-i", IMAGE_PATH, "-m", MASK_PATH, "-z", "2", "-e", "0.5", "-c", "0.5"};
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Connection type given is not an int");
		commandLineParser parser = new commandLineParser(args);
	}
	
	@Test
	public void testConnectRange() {
		String[] args = new String[]{"-i", IMAGE_PATH, "-m", MASK_PATH, "-z", "2", "-e", "0.5", "-c", "6"};
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Connection type must be 4 or 8");
		commandLineParser parser = new commandLineParser(args);
	}
}

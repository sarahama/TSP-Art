import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class GrayScale {
	/**
	 * @brief creates an image given a file name
	 * @param fileName
	 * @return Image object of given fileName
	 */
	public static BufferedImage convertImage(String fileName) {

		BufferedImage read = null;
		try {
			read = ImageIO.read(new File(fileName));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return read;

	}

	/**
	 * @brief writes an image to the screen
	 * @param fileName
	 *            , img and extension
	 * @return void
	 */
	public static void writeImage(BufferedImage img, String fileName,
			String extension) {
		BufferedImage x = img;
		File OutputFile = new File(fileName);
		try {
			ImageIO.write(x, extension, OutputFile);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static BufferedImage grayOut(BufferedImage img) {
		ColorConvertOp colorConvert = new ColorConvertOp(
				ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		colorConvert.filter(img, img);

		return img;
	}

	public static BufferedImage halfToning(BufferedImage img) {

		BufferedImage bi = img;
		int height = bi.getHeight();
		int width = bi.getWidth();
		int minX = bi.getMinX();
		int minY = bi.getMinY();
		return img;
	}

	public static void main(String arg[]) {

		BufferedImage img;
		Scanner input = new Scanner(System.in);
		// System.out.println("Enter the filename");
		String inputPath = "C:/Users/Shruti/workspace/TheoryOfComputation/src/plants.jpg";

		String outputPath = "C:/Users/Shruti/workspace/TheoryOfComputation/src/plants-grayedout.jpg";
		;

		img = convertImage(inputPath);
		img = grayOut(img);
		writeImage(img, outputPath, "jpg");
		

	}

}

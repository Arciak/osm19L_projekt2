package image_processing;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.awt.Color;
import java.awt.image.*;

/****************************************
 * w tej klasie zosatnie zaimplementowana 
 * filtracja obrazu
 * @author arciak
 *
 */

public class BufferedImageOp {
	private BufferedImage image_ = null; 
	private int filterSize_ = 0;
	
	public BufferedImageOp(BufferedImage image, int filterSize){
		this.image_ = image;
		if(filterSize%2==0 || filterSize<3){
			if(filterSize<3){
				this.filterSize_ = 3;
				System.out.println("Podany rozmiar filtru jest za mały i ustawiono filtr na 3");
			}
			else if (filterSize%2 == 0){
				this.filterSize_ = filterSize+1;
				System.out.println("Podano rozmiar filtru "+ filterSize + " nie moze byc parzysty. Dlatego ustawiono rozmiar: " + filterSize+1);
			}
		}
		else this.filterSize_ = filterSize;
	}
	
	public BufferedImage median_RGB() {  
		   
		BufferedImage image = this.image_;
        int maskSize = 11;  
        int width = image.getWidth();  
        int height = image.getHeight();  
        int outputPixels[] = new int[width * height];  
          
        int red[], green[], blue[];  
        int xMin, xMax, yMin, yMax;  
   
        int argb, reD, greenN, bluE;  
          
        /** Median Filter operation */ 
        for (int y = 0; y < height; y++) {  
            for (int x = 0; x < width; x++) {  
                int a = image.getRGB(x, y);  
                red = new int[maskSize * maskSize];  
                green = new int[maskSize * maskSize];  
                blue = new int[maskSize * maskSize];  
                int count = 0;  
                xMin = x - (maskSize / 2);  
                xMax = x + (maskSize / 2);  
                yMin = y - (maskSize / 2);  
                yMax = y + (maskSize / 2);  
                for (int r = yMin; r <= yMax; r++) {  
                    for (int c = xMin; c <= xMax; c++) {  
                        if (r < 0 || r >= height || c < 0 || c >= width) {  
                            /** Some portion of the mask is outside the image. */ 
                            continue;  
                        } else {  
                            argb = image.getRGB(c, r);  
                            reD = (argb >> 16) & 0xff;  
                            red[count] = reD;  
                            greenN = (argb >> 8) & 0xff;  
                            green[count] = greenN;  
                            bluE = (argb) & 0xFF;  
                            blue[count] = bluE;  
                            count++;  
                        }  
                    }  
                }  
   
                /** sort red, green, blue array */ 
                java.util.Arrays.sort(red);  
                java.util.Arrays.sort(green);  
                java.util.Arrays.sort(blue);  
   
                /** save median value in outputPixels array */ 
                int index = (count % 2 == 0) ? count / 2 - 1 : count / 2;  
                //logger.info("valore mediano " + index);  
                int p = (a << 24) | (red[index] << 16) | (green[index] << 8) | blue[index];  
                outputPixels[x + y * width] = p;  
            }  
        }  
        /** Write the output pixels to the image pixels */ 
        for (int y = 0; y < height; y++) {  
            for (int x = 0; x < width; x++) {  
            	image.setRGB(x, y, outputPixels[x + y * width]);  
            }  
        }  
        return image;
    }  
}

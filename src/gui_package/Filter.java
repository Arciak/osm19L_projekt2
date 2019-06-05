package gui_package;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;

import javax.swing.JLabel;

public class Filter implements BufferedImageOp {
	
	private int filterSize_ = 3;
	
	public Filter(int filterSize, JLabel setErrorInfoFromSizeTextField){
		if(filterSize%2==0 || filterSize<3){
			if(filterSize<3){
				this.filterSize_ = 3;
				System.out.println("Zbyt male rzomiar. ustawiono 3");
				setErrorInfoFromSizeTextField.setText("Zbyt male okno. ustawiono 3");
			}
			else if (filterSize%2 == 0){
				this.filterSize_ = filterSize+1;
				System.out.println("Podano parzysty, ustawiono: " + this.filterSize_);
				setErrorInfoFromSizeTextField.setText("Podano parzysty, ustawiono: " + this.filterSize_);
			}
		}
		else {
			this.filterSize_ = filterSize;
			setErrorInfoFromSizeTextField.setText("Rozmiar filtru: "+ filterSize);
		}
	}

	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
		
        int maskSize = this.filterSize_;  
        int width = src.getWidth();  
        int height = src.getHeight();  
        int outputPixels[] = new int[width * height];  
        if (dest == null)
        	dest = createCompatibleDestImage(src, src.getColorModel());
          
        int red[], green[], blue[];  
        int xMin, xMax, yMin, yMax;  
   
        int argb, reD, greenN, bluE;  
          
        /** Median Filter operation */ 
        for (int y = 0; y < height; y++) {  
            for (int x = 0; x < width; x++) {  
                int a = src.getRGB(x, y);  
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
                            argb = src.getRGB(c, r);  
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
            	dest.setRGB(x, y, outputPixels[x + y * width]);  
            }  
        }  
		return dest;
	}

	@Override
	public Rectangle2D getBounds2D(BufferedImage src) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
		if (destCM == null)
			destCM = src.getColorModel();
		return(new BufferedImage(destCM,destCM.createCompatibleWritableRaster(src.getWidth(),src.getHeight()), destCM.isAlphaPremultiplied(),null));
	}

	@Override
	public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RenderingHints getRenderingHints() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

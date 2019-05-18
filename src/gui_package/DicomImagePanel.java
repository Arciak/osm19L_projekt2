package gui_package;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class DicomImagePanel extends JPanel{
	private BufferedImage image_ = null;
	private String noImageState_ = "Brak wczytanego obrazu";
	
	public DicomImagePanel()
    {
    	this.setPreferredSize(new Dimension(300,300));
    }

    public void setImage(BufferedImage image)
    {
        if (image!=null)
        {
        	this.image_=image;
            this.setPreferredSize(new Dimension(this.image_.getWidth(),this.image_.getHeight()));
            this.repaint();    
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D  g2d=null;

        g2d=(Graphics2D)g;
        
        this.setBackground(Color.WHITE);
        super.paintComponent(g2d);
        
        /****** usatwienie napisu w przypadku braku wpisanego obrazu ***********/
        g2d.setFont(new Font(Font.MONOSPACED,Font.BOLD, 18));
        g2d.setColor(Color.BLACK); 
        g2d.drawString(this.noImageState_,50,100);
                
        if (this.image_!=null)
            g2d.drawImage(this.image_,null,0,0);
    }
	
}

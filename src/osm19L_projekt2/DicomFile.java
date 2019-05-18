package osm19L_projekt2;

import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.text.html.HTMLDocument.Iterator;

import org.dcm4che2.data.*;
import org.dcm4che2.io.*;
import org.dcm4che2.util.CloseUtils;

public class DicomFile {
	
	private String filePath_;
	private DicomInputStream dis_ = null;
	private DicomObject dcm_ = null;
	private BufferedImage image_=null;
	
	public DicomFile(String filePath_) {
		super();
		this.filePath_ = filePath_;
		File file=new File(filePath_);
		
		/*************** read DICOM file **********************/
		try{
			this.dis_ = new DicomInputStream(file);
			this.dcm_ = dis_.readDicomObject();
		}
		catch (IOException e){
			System.out.println("Error while reading DICOM file");
		}
		finally{
			if (dis_ != null){
				CloseUtils.safeClose(dis_);
			}
		}
		/************* read image from DICOM ******************/
		try
		{
			this.setImage_(ImageIO.read(file));
		}
		catch (IOException ioe)
		{
			System.out.println("Error while reading DICOM image data");
			System.exit(-1);
		}		
	}
	
	void printDataPatient(){
		 DicomObject dcm = dcm_;
		if (dcm!=null)
			   {
			System.out.println("Modalnosc: " + dcm.getString(Tag.Modality)); 
			System.out.println("ID pacjenta: " + dcm.getString(Tag.PatientID)); 
			System.out.println("Nazwisko pacjenta: " + dcm.getString(Tag.PatientName)); 
			System.out.println("Wiek pacjenta: " + dcm.getString(Tag.PatientAge)); 
			System.out.println("UID badania: " + dcm.getString(Tag.StudyInstanceUID)); 
			System.out.println("Data badania: " + dcm.getString(Tag.StudyDate)); 
			System.out.println("UID serii: " + dcm.getString(Tag.SeriesInstanceUID)); 
			System.out.println("Opis serii: " + dcm.getString(Tag.SeriesDescription)); 
			System.out.println("Data serii: " + dcm.getString(Tag.SeriesDate)); 
			System.out.println("Numer w serii: " + dcm.getString(Tag.InstanceNumber));
			   }
	}

	public DicomObject getDcm_() {
		return dcm_;
	}

	public void setDcm_(DicomObject dcm_) {
		this.dcm_ = dcm_;
	}

	public BufferedImage getImage_() {
		return image_;
	}

	public void setImage_(BufferedImage image_) {
		this.image_ = image_;
	}
}
package gui_package;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.*;
import java.io.File;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import osm19L_projekt2.DicomFile;


public class MainWindowGUI extends JFrame {
	private JFrame mainFrame_ = null; // glowne okno
	private JPanel imagesPanel_ = null; // panel na zdejeci/obrazy	
	private JPanel buttonsPanel_ = null; //panel na przyciski do wybrania obrazu, ustawiania parametrow filtru itd
//	private JPanel medianFilterPanel_ = null; // panel dla okien dialogowychh zwiazanych z filtracja medianowa
//	private JPanel filterPanel_ = null; // w tym panelu zostały zdefiniowane wszytskie okna dialogowe do ustawiania filtrów 
	
//	private JPanel contrastPanel_ = null; //tutaj umieścić przyciski JScrolle do zmiany kontrastu i jasności 
//	private JPanel contrastBrightnessTreshPanel_ = null;
	
	private JButton setDicomPathButton_ = null;
	private JButton startMedianFilter_ = null;
	private JTextField filterSizeTextFiled_ = null;
	private JLabel setErrorInfoFromSizeTextField_ = null;
	private JLabel brightnessSliderLabel_ = null;
	private JSlider sliderForBrightness_ = null;
	private JLabel contrastSliderLabel_ = null;
	private JSlider sliderForContrast_ = null;
	private JLabel treshSliderLabel_ = null;
	private JSlider sliderForTresh_ = null;
	private JLabel  medianFilterText = null;
	
	private int filterSize_ = 3;
	private int tresholding_ = 70;
	private float brightness_ = (float) 1.0;
	private float contrast_ = (float) 1.0;
	private int wasTresholding = 0;
	
	DicomFile readDicom_ = null;
	DicomFile transformImage_ = null;
	DicomImagePanel mImagePanel_=null; // panel na umiszczenie BufferedImage
	DicomImagePanel mConvertedImagePanel_=null; // panel na umiszczenie BufferedImage
	Filter setFilter = null;
	RescaleOp  opBF = null;
	LookupOp opT = null;
	
	private BufferedImage orginalImg_ = null;
	private BufferedImage convertedImg_ = null;
	
	public MainWindowGUI(){
		
/********************** inicjacja glowne okno programu **********************************/
		this.mainFrame_ = new JFrame("Zadanie 5 OSM");
		this.mainFrame_.setLayout(new BorderLayout());
		
/********************** inicjacja panelu w glownym oknie gdzie beda zdjecia ***********************/
		this.imagesPanel_ = new JPanel();
		this.imagesPanel_.setLayout(new GridLayout(1,2));
		
/********************** inicjacja 2 paneli na 2 zdjecia i dodanie ich do glownego panelu programu **********************************/		
		this.mImagePanel_ = new DicomImagePanel();
		this.imagesPanel_.add(this.mImagePanel_);
		this.mConvertedImagePanel_ = new DicomImagePanel();
		this.imagesPanel_.add(this.mConvertedImagePanel_);

/****************************** inicjacja przycisku do wybrania pliku DICOM pobranie zdjec  ************/	
		this.setDicomPathButton_ = new JButton("Wybierz plik");
		this.setDicomPathButton_.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setDialogTitle("Wybierz plik DICOM");
				jfc.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("DCM files", "dcm");
				jfc.addChoosableFileFilter(filter);
				
				int returnValue = jfc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					System.out.println(selectedFile.getAbsolutePath());
					readDicom_ = new DicomFile(selectedFile.getAbsolutePath());
					orginalImg_ = readDicom_.getImage_();
					convertedImg_ = readDicom_.getImage_();
					mImagePanel_.setImage(orginalImg_);
					mConvertedImagePanel_.setImage(convertedImg_);
					wasTresholding = 0;
					filterSize_ = 3;
					tresholding_ = 70;
					brightness_ = (float) 1.0;
					contrast_ = (float) 1.0;
					startMedianFilter_.setEnabled(true);
					filterSizeTextFiled_.setEditable(true);
					sliderForBrightness_.setEnabled(true);
					sliderForContrast_.setEnabled(true);
					sliderForTresh_.setEnabled(true);
					treshSliderLabel_.setEnabled(true);
					contrastSliderLabel_.setEnabled(true);
					brightnessSliderLabel_.setEnabled(true);
					medianFilterText.setEnabled(true);
				}
			}
		});
		
		/************** Filtr Medianowy ***************/
		startMedianFilter_ = new JButton("Filtr Medianowy");
		startMedianFilter_.setEnabled(false);
		startMedianFilter_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setFilter = new Filter(filterSize_, setErrorInfoFromSizeTextField_);
				convertedImg_ = setFilter.filter(orginalImg_, null); // nie robi kolejnego filtru medianowego z poprzedniego obrazu tylko ciagle pracuje na oryginalnym
				BufferedImage tempImg_ = tresholdingFunction(tresholding_,convertedImg_, wasTresholding);
				mConvertedImagePanel_.setImage(tempImg_);
				opBF = new RescaleOp(contrast_, brightness_, null);
				tempImg_ = opBF.filter(tempImg_, null);
				mConvertedImagePanel_.setImage(tempImg_);
			}
		});
		
		/************************* ustawianie rozmiaru filtru **************************/
		this.filterSizeTextFiled_ = new JTextField();
		this.filterSizeTextFiled_.setEditable(false);
		this.setErrorInfoFromSizeTextField_= new JLabel();
		
		this.filterSizeTextFiled_.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				try{
					filterSize_ = Integer.parseInt(filterSizeTextFiled_.getText());
					setErrorInfoFromSizeTextField_.setText("");
					startMedianFilter_.setEnabled(true);
				}
				catch(NumberFormatException notNumber){
					setErrorInfoFromSizeTextField_.setText("Wymagana całkowite nieprzytse");
					startMedianFilter_.setEnabled(false);
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				try{
					filterSize_ = Integer.parseInt(filterSizeTextFiled_.getText());
					setErrorInfoFromSizeTextField_.setText("");
					startMedianFilter_.setEnabled(true);
				}
				catch(NumberFormatException notNumber){
					setErrorInfoFromSizeTextField_.setText("Wymagana całkowite nieprzytse");
					startMedianFilter_.setEnabled(false);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				try{
					filterSize_ = Integer.parseInt(filterSizeTextFiled_.getText());
					setErrorInfoFromSizeTextField_.setText("");
					startMedianFilter_.setEnabled(true);
				}
				catch(NumberFormatException notNumber){
					setErrorInfoFromSizeTextField_.setText("Wymagana całkowite nieprzytse");
					startMedianFilter_.setEnabled(false);
				}
			}
		});
		
		
/******************** ustwienie sildera w GUI do zmiany kontrastu i jasności i progowanie ***********************/
		
		/*************** JASNOSC ********************/
		brightnessSliderLabel_ = new JLabel("Zmien jasnosc obrazu");
		brightnessSliderLabel_.setEnabled(false);
		sliderForBrightness_ = new JSlider(0,800,400);
		sliderForBrightness_.setEnabled(false);
		sliderForBrightness_.addChangeListener(new ChangeListener() {	
			@Override
			public void stateChanged(ChangeEvent e) { //trzeba liczyć różnice o jaką powiększamy i o ile pomnijszamy
				 brightness_  = (float) ((JSlider)e.getSource()).getValue() - (float) 400;
				 opBF = new RescaleOp(contrast_, brightness_, null ); 
				 BufferedImage tempImg_ = tresholdingFunction(tresholding_,convertedImg_, wasTresholding);
				 tempImg_ = opBF.filter(tempImg_, null );
				 mConvertedImagePanel_.setImage(tempImg_);
			}
		});
		
		/*************** KONTRAST ********************/
		contrastSliderLabel_ = new JLabel("Zmien kontrast obrazu");
		contrastSliderLabel_.setEnabled(false);
		sliderForContrast_ = new JSlider(0,100,50);
		sliderForContrast_.setEnabled(false);
		sliderForContrast_.addChangeListener(new ChangeListener() {	
			@Override
			public void stateChanged(ChangeEvent e) {
				 contrast_  = (float) ((JSlider)e.getSource()).getValue() - (float)50;
				 System.out.println(contrast_);
				 opBF = new RescaleOp(contrast_, brightness_, null );
				 BufferedImage tempImg_ = tresholdingFunction(tresholding_,convertedImg_, wasTresholding);
				 tempImg_ = opBF.filter(tempImg_, null );
				 mConvertedImagePanel_.setImage(tempImg_);
				
			}
		});
		
		/*************** PROGOWANIE ********************/
		treshSliderLabel_ = new JLabel("Progowanie");
		treshSliderLabel_.setEnabled(false);
		sliderForTresh_ = new JSlider(1,10,1);
		sliderForTresh_.setEnabled(false);
		// Add change listener to the slider
        this.sliderForTresh_.addChangeListener(new ChangeListener() {
        	@Override
            public void stateChanged(ChangeEvent e) {
        		tresholding_ = (int) 256/(((JSlider)e.getSource()).getValue());
        		wasTresholding = 1;
        		if( tresholding_ == 256){
        			opBF = new RescaleOp(contrast_, brightness_, null );
        			BufferedImage tempImg_ = opBF.filter(convertedImg_, null );
        			mConvertedImagePanel_.setImage(tempImg_);
        		}
        		else {
        			BufferedImage tempImg_ = tresholdingFunction(tresholding_,convertedImg_, wasTresholding);
        			opBF = new RescaleOp(contrast_, brightness_, null);
        			tempImg_ = opBF.filter(tempImg_, null);
        			mConvertedImagePanel_.setImage(tempImg_);
        		}
            }
        });

        
        medianFilterText = new JLabel("Wprowadz rozmiar filtru");
        medianFilterText.setFont(new Font("Serif", Font.BOLD, 14));
        medianFilterText.setEnabled(false);
        
		buttonsPanel_ = new JPanel(new GridLayout(13, 1));
		buttonsPanel_.add(setDicomPathButton_);
		buttonsPanel_.add(new JLabel()); //pusta komórka 
		buttonsPanel_.add(medianFilterText);
		buttonsPanel_.add(filterSizeTextFiled_);
		buttonsPanel_.add(setErrorInfoFromSizeTextField_);
		buttonsPanel_.add(startMedianFilter_);
		buttonsPanel_.add(new JLabel()); //pusta komórka 
		buttonsPanel_.add(brightnessSliderLabel_);
		buttonsPanel_.add(sliderForBrightness_);
		buttonsPanel_.add(contrastSliderLabel_);
		buttonsPanel_.add(sliderForContrast_);
		buttonsPanel_.add(treshSliderLabel_);
		buttonsPanel_.add(sliderForTresh_);
	
		
		this.mainFrame_.add(this.buttonsPanel_,BorderLayout.WEST); // dodanie panelu zprzycikami
		this.mainFrame_.add(this.imagesPanel_, BorderLayout.EAST); //dodanie panelu ze zdjeciami  do głównego okna
		
		
		this.mainFrame_.setSize(2500, 1000);
		this.mainFrame_.pack();
		this.mainFrame_.setVisible(true);
		this.mainFrame_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}	
	
	public BufferedImage tresholdingFunction(int size,BufferedImage image, int wasTresholding){
		BufferedImage tmpImg = null;
		if(wasTresholding==1){
			byte[] data = new byte[256];
			for ( int i = size; i < data.length; i++ )
				data[i] = (byte) 255;
			opT = new LookupOp(new ByteLookupTable(0,data),null);
			tmpImg = opT.filter( image, null );
			return tmpImg;
		}
		else return image;
	}
	
}


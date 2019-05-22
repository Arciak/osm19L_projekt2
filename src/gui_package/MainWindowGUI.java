package gui_package;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.io.File;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import javafx.event.ActionEvent;
import osm19L_projekt2.DicomFile;
import image_processing.*;
import image_processing.BufferedImageOp;

public class MainWindowGUI extends JFrame {
	private JFrame mainFrame_ = null; // glowne okno
	private JPanel imagesPanel_ = null; // panel na zdejeci/obrazy	
	private JPanel buttonsPanel_ = null; //panel na przyciski do wybrania obrazu, ustawiania parametrow filtru itd
	
	private JButton setDicomPathButton_ = null;
	private JButton startMedianFilter_ = null;
	
	DicomFile readDicom_ = null;
	DicomFile transformImage_ = null;
	DicomImagePanel mImagePanel_=null; // panel na umiszczenie BufferedImage
	DicomImagePanel mConvertedImagePanel_=null; // panel na umiszczenie BufferedImage
	
	public MainWindowGUI(){
		
/********************** inicjacja glowne okno programu **********************************/
		this.mainFrame_ = new JFrame("Zadanie 5 OSM");
		this.mainFrame_.setLayout(new BorderLayout());
		/****** inicjacja panelu w glownym oknie gdzie bedazdjecia ***********************/
		this.imagesPanel_ = new JPanel(new BorderLayout());
		
//		BoxLayout boxLayout = new BoxLayout(this.mainFrame_.getContentPane(), BoxLayout.X_AXIS);
//		this.mainFrame_.setLayout(boxLayout);
		
/********************** inicjacja 2 paneli na 2 zdjecia i dodanie ich do glownego panelu programu **********************************/		
		this.mImagePanel_ = new DicomImagePanel();
		this.imagesPanel_.add(this.mImagePanel_,BorderLayout.WEST);
		this.mConvertedImagePanel_ = new DicomImagePanel();
		this.imagesPanel_.add(this.mConvertedImagePanel_,BorderLayout.EAST);

/****************************** pobranie zdjec jak narazie tymczasowe aby saprwdzic czys ie wysztko wyswietla ************/
		//this.mImagePanel_.setImage(tmpDicomImage); //dodanie zdjecia do Panelu 
		//this.mConvertedImagePanel_.setImage(tmpDicomImage);// dodanie zdjecia do Panelu
		
		/********* dodanie i inicjacja przyciku **************/
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
					transformImage_ = new DicomFile(selectedFile.getAbsolutePath());;
					mImagePanel_.setImage(readDicom_.getImage_()); 
				}
			}
		});
		
		this.startMedianFilter_ = new JButton("Filtr Medianowy");
		this.startMedianFilter_.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				// TODO Auto-generated method stub
				BufferedImage tempImg = transformImage_.getImage_();
				BufferedImageOp convertImg = new BufferedImageOp(tempImg,11);
				mConvertedImagePanel_.setImage(convertImg.median_RGB());
			}
		});
		
		this.buttonsPanel_ = new JPanel(new BorderLayout());
		this.buttonsPanel_.add(this.setDicomPathButton_, BorderLayout.NORTH);
		this.buttonsPanel_.add(this.startMedianFilter_, BorderLayout.SOUTH);
	
		this.mainFrame_.add(this.imagesPanel_, BorderLayout.CENTER); //dodanie panelu ze zdjeciami  do głównego okna
		this.mainFrame_.add(this.buttonsPanel_, BorderLayout.WEST); // dodanie panelu zprzycikami
		
		this.mainFrame_.setSize(1150, 531);
		this.mainFrame_.pack();
		this.mainFrame_.setVisible(true);
		this.mainFrame_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	
}

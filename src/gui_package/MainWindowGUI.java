package gui_package;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	private JPanel medianFilterPanel_ = null;
	private JPanel contrastBrightPanel_ = null; //tutaj umieścić przyciski JScrolle do zmiany kontrastu i jasności 
	
	private JButton setDicomPathButton_ = null;
	private JButton startMedianFilter_ = null;
	private JTextField filterSizeTextFiled_ = null;
	private JLabel setErrorInfoFromSizeTextField_ = null;
	private int filterSize_ = 3;
	
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
				BufferedImageOp convertImg = new BufferedImageOp(tempImg,filterSize_);
				mConvertedImagePanel_.setImage(convertImg.median_RGB());
			}
		});
		
		/************************* ustawianie rozmiaru filtru **************************/
		this.filterSizeTextFiled_ = new JTextField();
		this.setErrorInfoFromSizeTextField_= new JLabel();
		this.filterSizeTextFiled_.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				try{
					filterSize_ = Integer.parseInt(filterSizeTextFiled_.getText());
					setErrorInfoFromSizeTextField_.setText("");
				}
				catch(NumberFormatException notNumber){
					setErrorInfoFromSizeTextField_.setText("Potrzeba intow");
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		this.medianFilterPanel_ = new JPanel(new BorderLayout());
		this.medianFilterPanel_.add(this.filterSizeTextFiled_,BorderLayout.NORTH);
		this.medianFilterPanel_.add(this.setErrorInfoFromSizeTextField_,BorderLayout.CENTER);
		this.medianFilterPanel_.add(this.startMedianFilter_, BorderLayout.SOUTH);
		
		
		this.buttonsPanel_ = new JPanel(new BorderLayout());
		this.buttonsPanel_.add(this.setDicomPathButton_, BorderLayout.NORTH);
		this.buttonsPanel_.add(this.medianFilterPanel_,BorderLayout.CENTER);

	
		this.mainFrame_.add(this.imagesPanel_, BorderLayout.CENTER); //dodanie panelu ze zdjeciami  do głównego okna
		this.mainFrame_.add(this.buttonsPanel_, BorderLayout.WEST); // dodanie panelu zprzycikami
		
		this.mainFrame_.setSize(1150, 531);
		this.mainFrame_.pack();
		this.mainFrame_.setVisible(true);
		this.mainFrame_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}	
	
}

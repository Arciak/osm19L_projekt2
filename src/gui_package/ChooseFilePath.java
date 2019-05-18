package gui_package;
/***********************************
 * To zrobic jak wystarczy czasu teraz jest niedokonczone
 */

import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import osm19L_projekt2.DicomFile;

public class ChooseFilePath {
	private JButton setDicomPathButton_ = null;
	
	
	public ChooseFilePath(DicomFile readDicom, DicomImagePanel imagePanel){
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
					
				}

			}
		});
	}

	public JButton getSetDicomPathButton_() {
		return setDicomPathButton_;
	}

	public void setSetDicomPathButton_(JButton setDicomPathButton_) {
		this.setDicomPathButton_ = setDicomPathButton_;
	}
}

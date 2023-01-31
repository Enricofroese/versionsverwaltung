package de.fie_fro.versionsverwaltung;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
//import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;

/**
 * Hello world!
 *
 */
public class Service 
{
	int id; //fürs logging nachher
	FileHandler fileHandler;
	public Service(String pRepo) {
		DateFormat dateFormat = new SimpleDateFormat("ddHHmmss");
        String date = dateFormat.format(new Date());
 
        id = Integer.valueOf(date);
        fileHandler = new FileHandler(pRepo);
	}
	
	public void compare(String pFilename1, String pFilename2) {
		fileHandler.setCurrentFile(pFilename1);
		File file1 = fileHandler.getFile();
		fileHandler.setCurrentFile(pFilename2);
		File file2 = fileHandler.getFile();
		System.out.println("Vergleiche Dateien "+file1.getName()+" und "+file2.getName()+":");
		//TODO Vergleichen
	}
	
	public void editFile(String pFilename) {
		System.out.println("Bearbeite Datei "+pFilename);
		fileHandler.setCurrentFile(pFilename);
		fileHandler.lock();
		JFileChooser fileChooser = new JFileChooser();
	    int returnValue = fileChooser.showSaveDialog(null);
	    if (returnValue == JFileChooser.APPROVE_OPTION) {
	        File selectedFile = fileChooser.getSelectedFile();
	        try {
	            Files.copy(fileHandler.getFile().toPath(), selectedFile.toPath());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    //TODO klappt noch nicht
	}
	
	public String uploadNewFile(String pPathToFile) {
		File newFile = new File(pPathToFile);
		fileHandler.uploadNewFile(newFile);
		return "Hochladen erfolgreich";
	}
	
	public void setFileBackToVersion(String pFilename, String pVersion) {
		//TODO
	}
	
	public Integer[] getFileVersionHistory(String pFilename) {
		fileHandler.setCurrentFile(pFilename);
		return fileHandler.getVersions();
	}
	
	public void viewFile(String pFilename) {
		fileHandler.setCurrentFile(pFilename);
		fileHandler.getFile();
		// TODO Datei anzeigen
	}
	
	public String uploadExistingFileWithNewVersion(String pPathToFile) {
		File existingFile = new File(pPathToFile);
		String filename = existingFile.getName();
		String filenameWithouteExtension = filename.substring(0, filename.lastIndexOf('.'));
		try {
			fileHandler.setCurrentFile(filenameWithouteExtension);
			// log System.out.println(filenameWithouteExtension);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		fileHandler.uploadNewVersion(existingFile);
		fileHandler.unlock();
		return "Hochladen erfolgreich";
	}
	
	public String[] getRepoContent() {
		return fileHandler.getFilesInRepo();
	}
}
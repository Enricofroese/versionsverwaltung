package de.fie_fro.versionsverwaltung;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        fileHandler = new FileHandler(pRepo+"\\");
	}
	
	public void compare(String pFilename1, String pFilename2) {
		fileHandler.setCurrentFile(pFilename1);
		//Datei file1 = fileHandler.getFile();
		fileHandler.setCurrentFile(pFilename2);
		//Datei file2 = fileHandler.getFile();
		System.out.println("Vergleiche Dateien "+pFilename1+" und "+pFilename2+":");
		//TODO Vergleichen
	}
	
	public void editFile(String pFilename) {
		System.out.println("Bearbeite Datei "+pFilename);
		fileHandler.setCurrentFile(pFilename);
		fileHandler.setLockOnFile(true); //Annahme true = locked?
		//TODO Methode im FileHandler zum Editieren
	}
	
	public String uploadNewFile(String pPathToFile) {
		Datei newFile = new Datei(pPathToFile);
		fileHandler.uploadNewFile(newFile);
		return "Hochladen erfolgreich";
	}
	
	public String setFileVersionToHead(String pFilename, String pVersion) {
		int version = Integer.valueOf(pVersion);
		fileHandler.setCurrentFile(pFilename);
		// TODO Auto-generated method stub
		return "";
	}
	
	public Integer[] getFileVersionHistory(String pFilename) {
		// TODO Auto-generated method stub
		fileHandler.setCurrentFile(pFilename);
		return fileHandler.getVersions();
	}
	
	public void viewFile(String pFilename) {
		fileHandler.setCurrentFile(pFilename);
		fileHandler.getFile();
		// TODO Datei anzeigen
	}
	
	public String uploadExistingFileWithNewVersion(String pPathToFile) {
		Datei existingFile = new Datei(pPathToFile);
		fileHandler.uploadNewVersion(existingFile);
		return "Hochgeladen erfolgreich";
	}
}
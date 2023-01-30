package de.fie_fro.versionsverwaltung;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Hello world!
 *
 */
public class Service 
{
	int id;
	public Service() {
		DateFormat dateFormat = new SimpleDateFormat("ddHHmmss");
        String date = dateFormat.format(new Date());
 
        id = Integer.valueOf(date);
	}
	
	public void compare(String pFilename1, String pFilename2) {
		System.out.println("Vergleiche Dateien "+pFilename1+" und "+pFilename2+":");
	}
	
	public void editFile(String pFilename) {
		System.out.println("Bearbeite Datei "+pFilename);
	}
	
	public String uploadNewFile(String pPathToFile) {
		// TODO Auto-generated method stub
		String dateiname = "";
		int version = 0;
		return "Neue Datei \""+dateiname+"\" wurde erfolgreich hochgeladen (Version "+version+").";
	}
	
	public String setFileVersionToHead(String pFilename, String pVersion) {
		int version = Integer.valueOf(pVersion);
		System.out.println("Version: "+version);
		// TODO Auto-generated method stub
		return "";
	}
	
	public void getFileVersionHistory(String pFilename) {
		// TODO Auto-generated method stub
		
	}
	
	public void viewFile(String pFilename) {
		// TODO Auto-generated method stub
		
	}
	
	public String uploadExistingFileWithNewVersion(String pPathToFile) {
		// TODO Auto-generated method stub
		String dateiname = "";
		int version = 0;
		return "Datei \""+dateiname+"\" wurde erfolgreich hochgeladen (Version "+version+").";
	}
}
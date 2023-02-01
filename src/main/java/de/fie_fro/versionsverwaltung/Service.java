package de.fie_fro.versionsverwaltung;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
//import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

/**
 * Hello world!
 *
 */
public class Service 
{
	int id; //fürs logging nachher
	private static FileHandler fileHandler;
	private final static Logger logger = Logger.getLogger(consoleScanner.class.getName());
	
	public Service(String pRepo) {
		initializeLogger();
		DateFormat dateFormat = new SimpleDateFormat("ddHHmmss");
        String date = dateFormat.format(new Date());
 
        id = Integer.valueOf(date);
        logger.fine("Die ID des Service ist: "+id+".");
        fileHandler = new FileHandler(pRepo);
	}
	
	private static void initializeLogger() {
		final LogManager logManager = LogManager.getLogManager();
		try {
			logManager.readConfiguration(new FileInputStream("./src/main/LoggerVersionsverwaltung.properties"));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Logger \""+logger.getName()+"\" wurde initialisiert.");
	}
	
	public void compare(String pFilename1, String pFilename2) {
		fileHandler.setCurrentFile(pFilename1);
		File file1 = fileHandler.getFile();
		fileHandler.setCurrentFile(pFilename2);
		File file2 = fileHandler.getFile();
		logger.info("Vergleiche Dateien "+file1.getName()+" und "+file2.getName()+":");
		//TODO Vergleichen
	}
	
	public void editFile(String pFilename) {
		logger.info("Bearbeite Datei "+pFilename);
		fileHandler.setCurrentFile(pFilename);
		fileHandler.lock();
		logger.info("Die Datei "+fileHandler.getFile()+" wurde für die Bearbeitung gesperrt.");
		JFileChooser fileChooser = new JFileChooser();
	    int returnValue = fileChooser.showSaveDialog(null);
	    if (returnValue == JFileChooser.APPROVE_OPTION) {
	        File selectedFile = fileChooser.getSelectedFile();
	        try {
	            Files.copy(fileHandler.getFile().toPath(), selectedFile.toPath());
	        } catch (IOException e) {
	        	logger.severe("Folgende Exception ist bei der Ausführung der Methode editFile aufgetreten:"+e);
	            e.printStackTrace();
	        }
	    }
	    //TODO klappt noch nicht
	}
	
	public String uploadNewFile(String pPathToFile) {
		File newFile = new File(pPathToFile);
		fileHandler.uploadNewFile(newFile);
		logger.info("Datei "+newFile.getName()+" wurde hochgeladen.");
		return "Hochladen erfolgreich";
	}
	
	public void setFileBackToVersion(String pFilename, String pVersion) {
		fileHandler.setCurrentFile(pFilename);

		//TODO Methode im FileHandler zum Zurücksetzten
		//Alternative: die alte Version nochmals hochladen als neue Version
		logger.info("Datei "+pFilename+" wurde auf die Version "+pVersion+" zurückgesetzt.");
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
	
	public void viewFileOfVersion(String pFilename, String pVersion) {
		int version = Integer.valueOf(pVersion);
		fileHandler.setCurrentFile(pFilename);
		fileHandler.getOldFile(version);
		//TODO Datei anzeigen
	}
	
	public String uploadExistingFileWithNewVersion(String pPathToFile) {
		File existingFile = new File(pPathToFile);
		String filename = existingFile.getName();
		String filenameWithouteExtension = filename.substring(0, filename.lastIndexOf('.'));
		try {
			fileHandler.setCurrentFile(filenameWithouteExtension);
			logger.finer("Der Dateiname ohne Erweiterung ist "+filenameWithouteExtension);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		fileHandler.uploadNewVersion(existingFile);
		fileHandler.unlock();
		logger.info("Die Datei "+existingFile.getName()+" wurde als neue Version hochgeladen.");
		return "Hochladen erfolgreich";
	}
	
	public String[] getRepoContent() {
		return fileHandler.getFilesInRepo();
	}
}
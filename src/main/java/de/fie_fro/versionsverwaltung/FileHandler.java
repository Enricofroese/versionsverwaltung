package de.fie_fro.versionsverwaltung;

import java.io.File;
import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.LogManager;
//import java.util.Scanner;
import java.util.logging.Logger;

public class FileHandler {
	
	private final static Logger logger = Logger.getLogger(consoleScanner.class.getName());
	public static String reporoot = "C:\\repos\\";

	private Datei currentFile;
	private String repository;
	
	public FileHandler(String repo) {
		initializeLogger();
		this.repository = reporoot+repo+"\\";
		logger.info("Aktuelles Repository: "+this.repository);
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
	
	public boolean setCurrentFile(String filename) {
		
		try {
			currentFile = new Datei(repository + filename);
			logger.finer("Die aktuelle Datei ist "+currentFile);
			return true;
		}
		catch(Exception e) {
        	logger.severe("Folgende Exception ist bei der Ausführung der Methode setCurrentFile aufgetreten:"+e);
			e.printStackTrace();
		}
		return false;
	}
	
	public void lock() {
		this.currentFile.setEditable(false);
		logger.fine("Die Datei "+currentFile+" wurde gesperrt.");
	}
	
	public boolean isLocked() {
		return this.currentFile.isEditable();
	}
	
	public void unlock() {
		this.currentFile.setEditable(true);
		logger.fine("Die Datei "+currentFile+" wurde entsperrt.");
	}
	
	public File getFile() {
		logger.finer("Folgender Wert wurde zurückgegeben: "+this.currentFile.getAbsolutePath() + "\\" 
				+ this.currentFile.getName() + "v" + this.getVersion() + ".txt");
		return new File(this.currentFile.getAbsolutePath() + "\\" + this.currentFile.getName() + "v" + this.getVersion() + ".txt");
	}
	
	public int getVersion() {
		logger.finer("Die aktuelle Datei "+currentFile+" hat folgende Version: "
				+currentFile.getCurrentversion());
		return currentFile.getCurrentversion();
	}
	
	public void uploadNewFile(File file) {
		uploadNewFile(file, file.getName().replaceAll("[.][^.]+$", ""));
		logger.finer("Die Datei "+file+" wurde hochgeladen.");
	}
	
	//Creates new directory with the name. The File itself becomes namev1
	public void uploadNewFile(File file, String pName) 
	{
		if (doesFileExist(pName)) {
			logger.info("Die Datei "+pName+" existiert bereits.");
			//Log: File already exists
		}
		else {
		try {
			String filename = pName;
			String dateipfad = repository + filename + "\\";
			Files.createDirectory(Path.of(dateipfad));
			logger.fine("Das verzeichnis für die Datei "+filename+" wurde angelegt: "+dateipfad);
			Files.copy(file.toPath(),Path.of(dateipfad + filename + "v1.txt"));
			logger.finer("Die Datei "+filename+" wurde in das Verzeichnis "+dateipfad+" hochgeladen.");
			Files.createFile(Path.of(dateipfad + filename + ".properties"));
			logger.finest("Die .properties-Datei wurde in das Verzeichnis "+dateipfad+" hochgeladen.");
			
			Properties props = new Properties();;
			props.setProperty("editable", String.valueOf(true));
			props.setProperty("currentversion", String.valueOf(1));
			props.store(new FileOutputStream(dateipfad + filename + ".properties"),null);
			logger.finest("Die Standardproperties wurden in der .properties-Datei hinterlegt.");
		}
		catch(Exception e) {
        	logger.severe("Folgende Exception ist bei der Ausführung der Methode uploadNewFile aufgetreten:"+e);
			e.printStackTrace();
		}
		}
	}
	
	public void uploadNewVersion(File file) {
		if(currentFile == null) {
			logger.info("Es wurde noch keine Datei ausgewählt.");
			return;
		}
		try {
			Files.copy(file.toPath(),Path.of(currentFile.getAbsolutePath() +"\\"+ currentFile.getName() + "v" + (currentFile.getCurrentversion()+1) + ".txt"));
			logger.finer("Die neue Version der Datei heißt "+currentFile.getName());
			currentFile.setCurrentversion(currentFile.getCurrentversion()+1);
			logger.fine("Die neue Version der Datei ist "+currentFile.getCurrentversion());
		}
		catch(Exception e) {
        	logger.severe("Folgende Exception ist bei der Ausführung der Methode uploadNewVersion aufgetreten:"+e);
			e.printStackTrace();
		}
		
	}
	
	public boolean doesFileExist(String filename) {
		
		File temp = new File(repository + filename);
		logger.info("Zu prüfende Datei: "+repository + filename);
		return temp.exists();
	}
	
	private boolean doesVersionExist(int version) {
		Integer[] arr = getVersions();
		logger.fine("Zu prüfende Version: "+currentFile.getName()+" mit der Version "+version);
		for(int i = 0; i<arr.length;i++) {
			if(arr[i].intValue() == version) {
				logger.finer("Version "+version+" gefunden.");
				return true;
			}
		}
		logger.finer("Version "+version+" nicht gefunden.");
		return false;
	}
	
	public Integer[] getVersions() {
		return currentFile.getVersions();
	}
	
	public File getOldFile(int version) {
		if(doesVersionExist(version)) {
			logger.severe("Die Version "+version+" der Datei "+currentFile.getName()+"existiert.");
			return new File(this.currentFile.getAbsolutePath() + "\\" + this.currentFile.getName() + "v" + version+ ".txt");
		}
		logger.severe("Die Version "+version+" der Datei "+currentFile.getName()+"existiert nicht.");
		return null;
		
	}
	
	public boolean toOldVersion(int version) {
		if(!doesVersionExist(version)) {
			logger.severe("Die Version "+version+" der Datei "+currentFile.getName()+"existiert nicht.");
			return false;
		}
		
		this.currentFile.setCurrentversion(version);
			
		logger.severe("Die Version "+version+" der Datei "+currentFile.getName()+"existiert.");		
		return true;
		
	}
	
	
	public String[] getFilesInRepo() {
		return getFolderContent(new File(repository));
	}
	
	public static void setReporoot(String pReporoot) {
		reporoot = pReporoot;
		logger.info("RepoRoot wurde auf "+reporoot+" gesetzt.");
	}
	
	public static String[] getRepositories() {
		return getFolderContent(new File(FileHandler.reporoot));
	}
	
	private static String[] getFolderContent(File pFile) {
		return pFile.list(new FilenameFilter() {
    		public boolean accept(File dir, String name) {
            return new File(dir, name).isDirectory();
        }
    });
	}

	public static void main(String[] args) {
		FileHandler f = new FileHandler("testrepo\\");
		f.setCurrentFile("testdatei");
		f.getFile();
	}
}

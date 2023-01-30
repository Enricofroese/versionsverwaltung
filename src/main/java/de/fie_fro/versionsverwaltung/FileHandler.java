package de.fie_fro.versionsverwaltung;

import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
//import java.util.Scanner;

public class FileHandler {
	
	public static String reporoot = "C:\\repos\\";

	private Datei currentFile;
	private String repository;
	
	public FileHandler(String repo) {
		this.repository = reporoot+repo+"\\";
	}
	
	public boolean setCurrentFile(String filename) {
		
		try {
			currentFile = new Datei(repository + filename);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public void lock() {
		this.currentFile.setEditable(false);
	}
	
	public void unlock() {
		this.currentFile.setEditable(true);
	}
	
	public File getFile() {
		return new File(this.currentFile.getAbsolutePath() + "\\" + this.currentFile.getName() + "v" + this.getVersion() + ".txt");
	}
	
	public int getVersion() {
		return currentFile.getCurrentversion();
	}
	
	public void uploadNewFile(File file) {
		uploadNewFile(file, file.getName().replaceAll("[.][^.]+$", ""));
	}
	
	//Creates new directory with the name. The File itself becomes namev1
	public void uploadNewFile(File file, String pName) 
	{
		if (doesFileExist(pName)) {
			//Log: File already exists
		}
		else {
		try {
			String filename = pName;
			String dateipfad = repository + filename + "\\";
			Files.createDirectory(Path.of(dateipfad));
			Files.copy(file.toPath(),Path.of(dateipfad + filename + "v1.txt"));
			Files.createFile(Path.of(dateipfad + filename + ".properties"));
			
			Properties props = new Properties();;
			props.setProperty("editable", String.valueOf(true));
			props.setProperty("currentversion", String.valueOf(1));
			props.store(new FileOutputStream(dateipfad + filename + ".properties"),null);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		}
	}
	
	public void uploadNewVersion(File file) {
		if(currentFile == null) {
			//Log: No File chosen
			return;
		}
		try {
			Files.copy(file.toPath(),Path.of(currentFile.getAbsolutePath() +"\\"+ currentFile.getName() + "v" + (currentFile.getCurrentversion()+1) + ".txt"));
			currentFile.setCurrentversion(currentFile.getCurrentversion()+1);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean doesFileExist(String filename) {
		
		File temp = new File(repository + filename);
		System.out.println(repository + filename);
		return temp.exists();
	}
	
	private boolean doesVersionExist(int version) {
		Integer[] arr = getVersions();
		for(int i = 0; i<arr.length;i++) {
			if(arr[i].intValue() == version)return true;
		}
		return false;
	}
	
	public Integer[] getVersions() {
		return currentFile.getVersions();
	}
	
	public File getOldFile(int version) {
		if(doesVersionExist(version)) {
			return new File(this.currentFile.getAbsolutePath() + "\\" + this.currentFile.getName() + "v" + version+ ".txt");
		}
		//Error Version existiert nicht
		return null;
		
	}
	
	public String[] getFilesInRepo() {
		return getFolderContent(new File(repository));
	}
	
	public static void setReporoot(String pReporoot) {
		reporoot = pReporoot;
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

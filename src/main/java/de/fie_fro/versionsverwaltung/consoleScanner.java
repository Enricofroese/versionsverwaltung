package de.fie_fro.versionsverwaltung;

import java.util.Arrays;
import java.util.Scanner;

public class consoleScanner {
	
	private static Service service;
	private static Scanner scanner = new Scanner(System.in);
	//private static String reporoot = "C:\\repos\\"; gehört in FileHandler
	private String repo;
	
	
	public static void main(String[] args) {
		new consoleScanner();
	}
	
	public consoleScanner() {
		
		String[] directories = FileHandler.getRepositories();
		System.out.println("Folgende Repositories sind vorhanden: "
				+Arrays.toString(directories)+
				"\nBitte Repository eingeben:");
		
		while(true) {
			repo = scanner.nextLine();
			if(!repo.equals("")) {
				break;
			}
		}
		boolean vorhanden = false;
		for(int i=0; i < directories.length; i++){
		    if(directories[i].equals(repo)){
		         vorhanden = true;
		    }
		}
		if (!vorhanden) {
			System.out.println("Das Repository ist nicht vorhanden, die Console wird geschlossen.");
			scanner.close();
		}
		else {
			//--> log System.out.println("Gewähltes Repository: "+repo);
			service = new Service(repo);
			
			//ab hier beginnt die eigentliche Interaktion mit der Konsole
			readConsole();
		}
	}
	
	private static void readConsole() {
		//Scanner scanner = new Scanner(System.in); //refactored
		String input = "";
		while(true) 
		{
			input = scanner.nextLine();
			evaluateConsoleInput(input);
			if(input.equals("exit")) 
			{
				writeConsole("Die Console wird geschlossen.");
				break;
			}
		}
		scanner.close();
	}
	
	private static void writeConsole(String pInfo) //eigentlich obsolet
	{
		System.out.println(pInfo);
	}
	
	private static void evaluateConsoleInput(String pInput)
	{
		String input[] = pInput.split(" ");
		//writeConsole(input[0]);
		switch(input[0])
		{
		case "help":
			writeConsole("comp\t2 Dateien vergleichen(Parameter: Dateiname Dateiname)\n"
					+ "edit\tDatei bearbeiten (Parameter: Dateiname)\n"
					+ "newf\tNeue Datei erstellen (Parameter: Pfad zur Datei)\n"
					+ "setv\tVersion als aktuell setzen (Parameter: Dateiname Version)\n"
					+ "vhis\tVersionshistorie anzeigen (Parameter: Dateiname)\n"
					+ "view\tDatei anzeigen (Parameter: Dateiname)\n"
					+ "\tDatei der Version n anzeigen (Parameter: Dateiname Version)\n"
					+ "upld\tDatei hochladen (Parameter: Pfad zur Datei)\n\n"
					+ "Beispiel: edit MyApp.java");
			break;
		case "comp":
			try {
				service.compare(input[1], input[2]);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			break;
		case "edit":
			try {
				service.editFile(input[1]);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			break;
		case "newf":
			try {
				writeConsole(service.uploadNewFile(input[1]));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			break;
		case "setv":
			try {
				writeConsole(service.setFileVersionToHead(input[1], input[2]));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			break;
		case "vhis":
			try {
				service.getFileVersionHistory(input[1]);
				//TODO Versionshistorie ausgeben
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			break;
		case "view":
			try {
				service.viewFile(input[1]);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			break;
		case "upld":
			try {
				writeConsole(service.uploadExistingFileWithNewVersion(input[1]));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			break;
        default:
        	break;
		}
	}
}

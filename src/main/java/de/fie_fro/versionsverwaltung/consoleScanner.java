package de.fie_fro.versionsverwaltung;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
//import java.util.ArrayList;
import java.util.Arrays;
//import java.util.List;
import java.util.Scanner;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class consoleScanner {
	private static Service service;
	private static Scanner scanner = new Scanner(System.in);
	private String repo;
	private final static Logger logger = Logger.getLogger(consoleScanner.class.getName());
	//private static String reporoot = "C:\\repos\\"; gehört in FileHandler --> refactored

	
	public static void main(String[] args) {
		initializeLogger();
		new consoleScanner();
	}
	
	public static void initializeLogger() {
		final LogManager logManager = LogManager.getLogManager();
		try {
			logManager.readConfiguration(new FileInputStream("./src/main/LoggerVersionsverwaltung.properties"));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Logger \"" + logger.getName() + "\" wurde initialisiert.");
	}
	
	public consoleScanner() {
		
		String[] directories = FileHandler.getRepositories();
		writeConsole("Folgende Repositories sind vorhanden: "
				+Arrays.toString(directories)+
				"\nBitte Repository eingeben:");
		
		while(true) {
			repo = scanner.nextLine();
			if(!repo.equals("")) {
				logger.info("Gewähltes Repository ist: " + repo + ".");
				break;
			}
		}
		boolean vorhanden = false;
		for(int i=0; i < directories.length; i++){
		    if(directories[i].equals(repo)){
		         vorhanden = true;
		         logger.info("Repository " + repo + " gefunden.");
		    }
		}
		if (!vorhanden) {
			writeConsole("Das Repository ist nicht vorhanden, die Console wird geschlossen.");
			logger.info("Das Repository " + repo + " ist nicht vorhanden.");
			scanner.close();
		}
		else {
			service = new Service(repo);
			logger.info("Ein Service-Object wurde erstellt.");
			writeConsole("OK");
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
			logger.info("Eingegebener Befehl: " + input);
			if(input.equals("exit")) 
			{
				writeConsole("Die Console wird geschlossen.");
				logger.info("Die Console wird geschlossen.");
				scanner.close();
				System.exit(0);
				break;
			}
			evaluateConsoleInput(input);
		}
	}
	
	private static void writeConsole(String pInfo) //eigentlich obsolet
	{
		System.out.println(pInfo);
		logger.finest("Folgende Information wird in die Console geschrieben: " + pInfo);
	}
	
	private static void evaluateConsoleInput(String pInput)
	{
		//Kein Space in Dateinamen erlaubt
		String input[] = pInput.split(" ");
		for(int i=0;i<input.length;i++) {
			logger.finer("Teil " + i + " der Eingabe: " + input[i]);
		}
		
		switch(input[0])
		{
		case "help":
			writeConsole("comp\t2 Dateien vergleichen(Parameter: Dateiname Version1 Version2)\n"
					+ "edit\tDatei bearbeiten (Parameter: Dateiname)\n"
					+ "ls\tListet alle Dateien im aktuellen Repository auf\n"
					+ "newf\tNeue Datei erstellen (Parameter: PfadZurDatei)\n"
					+ "setv\tVersion als aktuell setzen (Parameter: Dateiname Version)\n"
					+ "vhis\tVersionshistorie anzeigen (Parameter: Dateiname)\n"
					+ "view-v\tDatei anzeigen (Parameter: Dateiname)\n"
					+ "\tDatei der Version n anzeigen (Parameter: Dateiname Version)\n"
					+ "view\tDatei herunterladen zum Anzeigen (Parameter: Dateiname)\n"
					+ "upld\tDatei hochladen (Parameter: Dateiname PfadZurDatei)\n\n"
					+ "Beispiel:\tedit MyApp\n"
					+ "Beispiel2:\tview App2\n"
					+ "Beispiel3:\tcomp text 3 5\n\n"
					+ "Hinweis: Dateinamen immer ohne Endung angeben, Pfade mit Dateinamen+Endung");
			break;
		case "comp":
			try {
				service.compare(input[1], input[2], input[3]);
				logger.info("Die Funktion " + input[0] + " war erfolgreich.");
			}
			catch(Exception e) {
				logger.severe("Folgende Exception ist bei dem Aufruf von der Funktion " + input[0] + " aufgetreten:"
						+e);
				e.printStackTrace();
			}
			break;
		case "edit":
			try {
				service.editFile(input[1]);
				logger.info("Die Funktion " + input[0] + " war erfolgreich.");
			}
			catch(Exception e) {
				logger.severe("Folgende Exception ist bei dem Aufruf von der Funktion " + input[0] + " aufgetreten:"
						+e);
				e.printStackTrace();
			}
			break;
		case "newf":
			try {
				writeConsole(service.uploadNewFile(input[1]));
				logger.info("Die Funktion " + input[0] + " war erfolgreich.");
			}
			catch(Exception e) {
				logger.severe("Folgende Exception ist bei dem Aufruf von der Funktion " + input[0] + " aufgetreten:"
						+e);
				e.printStackTrace();
			}
			break;
		case "setv":
			try {
				service.setFileBackToVersion(input[1], input[2]);
				logger.info("Die Funktion " + input[0] + " war erfolgreich.");
			}
			catch(Exception e) {
				logger.severe("Folgende Exception ist bei dem Aufruf von der Funktion " + input[0] + " aufgetreten:"
						+e);
				e.printStackTrace();
			}
			break;
		case "vhis":
			try {
				Integer[] history = service.getFileVersionHistory(input[1]);
				for(int i = 0; i<history.length;i++) {
					if(i==history.length-1) {
						writeConsole("davon aktuelle Version: " + history[i].toString());

					}
					else {
						writeConsole("Version: " + history[i].toString());
					}
				}
				logger.info("Die Funktion " + input[0] + " war erfolgreich.");
			}
			catch(Exception e) {
				logger.severe("Folgende Exception ist bei dem Aufruf von der Funktion " + input[0] + " aufgetreten:"
						+e);
				e.printStackTrace();
			}
			break;
		case "view-v":
			if(input.length<3) {
				try {
					logger.info("Die Funktion " + input[0] + " war erfolgreich.");
					service.viewFile(input[1]);
				}
				catch(Exception e) {
					logger.severe("Folgende Exception ist bei dem Aufruf von der Funktion " + input[0] + " aufgetreten:"
							+e);
					e.printStackTrace();
				}
			}
			else {
				try {
					logger.info("Die Funktion " + input[0] + " war erfolgreich.");
					service.viewFileOfVersion(input[1], input[2]);
				}
				catch(Exception e) {
					logger.severe("Folgende Exception ist bei dem Aufruf von der Funktion " + input[0] + " aufgetreten:"
							+e);
					e.printStackTrace();
				}
			}
			break;
		case "view":
			try {
				service.viewFileDownload(input[1]);
				logger.info("Die Funktion " + input[0] + " war erfolgreich.");
			}
			catch(Exception e) {
				logger.severe("Folgende Exception ist bei dem Aufruf von der Funktion " + input[0] + " aufgetreten:"
						+e);
				e.printStackTrace();
			}
			break;
		case "upld":
			try {
				writeConsole(service.uploadNewVersion(input[1], input[2]));
				logger.info("Die Funktion " + input[0] + " war erfolgreich.");
			}
			catch(Exception e) {
				logger.severe("Folgende Exception ist bei dem Aufruf von der Funktion " + input[0] + " aufgetreten:"
						+e);
				e.printStackTrace();
			}
			break;
		case "ls":
			try {
				for (String s : service.getRepoContent()){ 
					writeConsole(s);
					logger.fine("Die Datei " + s + " wurde im aktuellen Repository gefunden.");
				}
				logger.info("Die Funktion " + input[0] + " war erfolgreich.");
			}
			catch(Exception e) {
				logger.severe("Folgende Exception ist bei dem Aufruf von der Funktion " + input[0] + " aufgetreten:"
						+e);
				e.printStackTrace();
			}
			break;
        default:
        	logger.warning("Für die eingegebene Funktion " + input[0] + " ist keine Aktion definiert.");
        	break;
		}
	}
	public static void fileToConsole(File pDatei) throws Exception{
		writeConsole(pDatei.getName() + ":\n"
				+ "------------------------------------------------------------\n");
		BufferedReader in = new BufferedReader(new FileReader(pDatei));
        String line = "";
        while((line = in.readLine()) != null) {
	        final StringBuffer output = new StringBuffer();
	        output.append(line);
	        logger.fine("Folgende Zeile wird ausgegeben: " + output.toString());
	        writeConsole(output.toString());
        }
        in.close();
        writeConsole("\n------------------------------------------------------------");
	}
}

package de.fie_fro.versionsverwaltung;

import java.util.Scanner;

public class consoleScanner {
	
	private static Service service;
	
	public consoleScanner() {
		service = new Service();
	}
	
	public static void main(String[] args) {
		new consoleScanner();
		readConsole();
	}
	
	private static void readConsole() {
		Scanner scanner = new Scanner(System.in);
		String input = "";
		while(true) 
		{
			input = scanner.nextLine();
			evaluateConsoleInput(input);
			if(input.equals("exit")) 
			{
				writeConsole("Console wird geschlossen");
				break;
			}
		}
		scanner.close();
	}
	
	private static void writeConsole(String pInfo)
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
					+ "newf\tNeue Datei erstellen (Parameter: Dateiname)\n"
					+ "setv\tVersion als aktuell setzen (Parameter: Dateiname Version)\n"
					+ "vhis\tVersionshistorie anzeigen (Parameter: Dateiname)\n"
					+ "view\tDatei anzeigen (Parameter: Dateiname)\n"
					+ "\tDatei der Version n anzeigen (Parameter: Dateiname Version)\n"
					+ "upld\tDatei hochladen (Parameter: Dateiname)\n\n"
					+ "Beispiel: edit MyApp.java");
            break;
		case "comp":
			service.compare(input[1], input[2]);
			//vorher abfangen, wie viele Parameter gesetzt sind
			break;
		case "edit":
			break;
		case "newf":
			break;
		case "setv":
			break;
		case "vhis":
			break;
		case "view":
			break;
		case "upld":
			break;
        default:
        	break;
		}
	}
}

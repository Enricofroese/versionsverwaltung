package de.fie_fro.versionsverwaltung;

import java.util.Scanner;

public class consoleScanner {
	
	public static void main(String[] args) {
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
		switch(pInput)
		{
		case "help":
			writeConsole("get lost");
            break;
        //ToDo: Befehl zerlegen und weitergeben an den Service
        default:
        	break;
		}
	}
}

package de.fie_fro.versionsverwaltung;

import java.util.Scanner;

public class consoleScanner {
	
	public void main(String[] args) {
		System.out.println(readConsole());
	}
	private String readConsole() {
		Scanner scanner = new Scanner(System.in);
		String input = "";
		while(true) {
			input = scanner.nextLine();
			if(input == "Abbruch") {
				break;
			}
		}
		scanner.close();
		return input;
	}
}

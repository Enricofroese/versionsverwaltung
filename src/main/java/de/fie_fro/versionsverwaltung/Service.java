
package de.fie_fro.versionsverwaltung;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.nio.file.StandardCopyOption;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * . Service-Klasse, die Befehle von der consoleScanner-Klasse annimmt und
 * durchfuehrt
 * 
 * @author enrico, jonas
 *
 */
public class Service {
	int id; // fürs logging nachher
	public static FileHandler fileHandler;
	private static final Logger logger = Logger.getLogger(consoleScanner.class.getName());

	/**
	 * . Constructor der Klasse Service
	 * 
	 * @param paRepo Repository
	 */
	public Service(String paRepo) {
		initializeLogger();
		DateFormat dateFormat = new SimpleDateFormat("ddHHmmss");
		String date = dateFormat.format(new Date());

		id = Integer.valueOf(date);
		logger.fine("Die ID des Service ist: " + id + ".");
		fileHandler = new FileHandler(paRepo);
	}

	private static void initializeLogger() {
		final LogManager logManager = LogManager.getLogManager();
		try {
			logManager.readConfiguration(new FileInputStream("./src/main/LoggerVersionsverwaltung.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Logger \"" + logger.getName() + "\" wurde initialisiert.");
	}

	/**
	 * . Vergleichen von zwei Dateien
	 * 
	 * @param paFilename1 Dateiname 1
	 * @param paFilename2 Dateiname 2
	 */
	public void compare(String paFilename1, String paFilename2) {
		fileHandler.setCurrentFile(paFilename1);
		File file1 = fileHandler.getFile();
		fileHandler.setCurrentFile(paFilename2);
		File file2 = fileHandler.getFile();
		logger.info("Vergleiche Dateien " + file1.getName() + " und " + file2.getName() + ":");
		// TODO Vergleichen
	}

	/**
	 * . Editieren einer Datei
	 * 
	 * @param paFilename Dateiname
	 */
	public void editFile(String paFilename) {
		logger.info("Bearbeite Datei " + paFilename);
		try {
			fileHandler.setCurrentFile(paFilename);

			if (fileHandler.isLocked()) {
				logger.severe("Datei " + paFilename + " ist zur Bearbeitung gesperrt.");
				return;
			}
			fileHandler.lock();
			logger.info("Die Datei " + fileHandler.getFile().toString() + " wurde für die Bearbeitung gesperrt.");
		} catch (Exception e) {
			logger.severe("Datei konnte nicht gefunden werden");
		}
		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				Files.copy(fileHandler.getFile().toPath(), selectedFile.toPath());
			} catch (IOException e) {
				logger.severe(
						"Folgende Exception ist bei der Ausführung der Methode editFile aufgetreten:" + e.getMessage());
				e.printStackTrace();

			}
			return;
		}
		fileHandler.unlock();
		logger.severe("Datei wurde wieder entsperrt, da es einen Fehler gab");
	}

	/**
	 * . Neue Datei hochladen
	 * 
	 * @param paPathToFile Pfad zur Datei
	 * @return Erfolgsmeldung
	 */
	public String uploadNewFile(String paPathToFile) {
		File newFile = new File(paPathToFile);
		fileHandler.uploadNewFile(newFile);
		logger.info("Datei " + newFile.getName() + " wurde hochgeladen.");
		return "Hochladen erfolgreich";
	}

	/**
	 * . Version zurücksetzen
	 * 
	 * @param paFilename Dateiname
	 * @param paVersion  Version
	 */
	public void setFileBackToVersion(String paFilename, String paVersion) {
		fileHandler.setCurrentFile(paFilename);

		boolean success = fileHandler.toOldVersion(Integer.parseInt(paVersion));
		if (success)
			logger.info("Datei " + paFilename + " wurde auf die Version " + paVersion + " zurückgesetzt.");
		if (!success)
			logger.info("Datei " + paFilename + " wurde NICHT auf die Version " + paVersion + " zurückgesetzt.");
	}

	/**
	 * . Versionshistorie zurückgeben
	 * 
	 * @param paFilename Dateiname
	 * @return Versionshistorie
	 */
	public Integer[] getFileVersionHistory(String paFilename) {
		fileHandler.setCurrentFile(paFilename);
		return fileHandler.getVersions();
	}

	/**
	 * . Datei ansehen
	 * 
	 * @param paFilename Dateiname
	 */
	public void viewFile(String paFilename) {
		fileHandler.setCurrentFile(paFilename);
		viewFile(fileHandler.getFile());
	}

	/**
	 * . alte Version einer Datei ansehen
	 * 
	 * @param paFilename Dateiname
	 * @param paVersion  Version
	 */
	public void viewFileOfVersion(String paFilename, String paVersion) {
		int version = Integer.valueOf(paVersion);
		fileHandler.setCurrentFile(paFilename);
		viewFile(fileHandler.getOldFile(version));
	}

	private void viewFile(File f) {
		Viewer v = new Viewer(f);
		v.setVisible(true);
	}

	/**
	 * . Neue Version einer Datei hochladen
	 * 
	 * @param paPathToFile Pfad zur Datei
	 * @return Erfolgsmeldung
	 */
	public String uploadNewVersion(String pName, String paPathToFile) {
		File existingFile = new File(paPathToFile);
		String filename = existingFile.getName();
		String filenameWithouteExtension = filename.substring(0, filename.lastIndexOf('.'));
		try {
			fileHandler.setCurrentFile(pName);
			logger.finer("Der Dateiname ohne Erweiterung ist " + filenameWithouteExtension);
		} catch (Exception e) {
			e.printStackTrace();
		}
		fileHandler.uploadNewVersion(existingFile);
		fileHandler.unlock();
		logger.info("Die Datei " + existingFile.getName() + " wurde als neue Version hochgeladen.");
		return "Hochladen erfolgreich";
	}

	/**
	 * . Inhalt des Repositorys ausgeben
	 * 
	 * @return Inhalt
	 */
	public String[] getRepoContent() {
		return fileHandler.getFilesInRepo();
	}

	class Viewer extends JFrame {
		private JPanel contentPane;
		private JTextArea textArea;

		public Viewer(File pFile) {
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 450, 300);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(new BorderLayout(0, 0));
			setContentPane(contentPane);

			JScrollPane scrollPane = new JScrollPane();
			contentPane.add(scrollPane, BorderLayout.CENTER);

			textArea = new JTextArea();
			scrollPane.setViewportView(textArea);

			try (BufferedReader reader = new BufferedReader(new FileReader(pFile))) {
				String line;
				while ((line = reader.readLine()) != null) {
					textArea.append(line + System.lineSeparator());
				}
			} catch (IOException e) {
				logger.severe("Fehler in der GUI");
				e.printStackTrace();
			}
		}
	}

}
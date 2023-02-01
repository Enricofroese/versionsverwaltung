package de.fie_fro.versionsverwaltung;



//de.fie_fro.versionsverwaltung
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
//import java.util.List;
import java.util.Properties;
import java.util.logging.LogManager;
//import java.util.Scanner;
//import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.logging.Logger;

public class Datei extends File{
	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(consoleScanner.class.getName());
	private Properties props;
	private boolean editable;
	private int currentversion;
	
	public static void initializeLogger() {
		final LogManager logManager = LogManager.getLogManager();
		try {
			logManager.readConfiguration(new FileInputStream("./src/main/LoggerVersionsverwaltung.properties"));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Logger \""+logger.getName()+"\" wurde initialisiert.");
	}
	
	public Datei(String pPath) {
		super(pPath);
		initializeLogger();
		readProperties();
	}
	
	public int getCurrentversion() {
		logger.finest("Aktuelle Verion: "+currentversion);
		return currentversion;
	}

	public void setCurrentversion(int currentversion) {
		this.currentversion = currentversion;
		this.writeProperties();
		logger.finest("Currentversion für die Datei "+this.getName()+" wurde auf "+this.currentversion+" gesetzt.");
	}

	public boolean isEditable() {
		logger.finest("Editable: "+editable);
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		this.writeProperties();
		logger.finest("Editable für die Datei "+this.getName()+" wurde auf "+this.editable+" gesetzt.");
	}
	
	public void readProperties() {
		try
		{
			this.props = new Properties();
			this.props.load(new FileInputStream(this.getAbsolutePath() + "\\" + this.getName() + ".properties"));
			this.editable = Boolean.parseBoolean((String)this.props.get("editable"));
			this.currentversion = Integer.parseInt((String)this.props.get("currentversion"));
			
		} catch (FileNotFoundException e) {
			logger.warning("Datei "+this.getName()+" nicht gefunden.");
			e.printStackTrace();
		} catch (IOException e) {
			logger.warning("Folgende exception ist aufgetreten: "+e);
			e.printStackTrace();
		}
	}
	
	private void writeProperties() {
		try {
			this.props.setProperty("editable", String.valueOf(editable));
			this.props.setProperty("currentversion", String.valueOf(currentversion));
			this.props.store(new FileOutputStream(this.getAbsolutePath() + "\\" + this.getName() + ".properties"),null);
			logger.fine("Die neuen Properties wurden geschrieben.");
		} catch (FileNotFoundException e) {
			logger.warning("Datei "+this.getName()+" nicht gefunden.");
			e.printStackTrace();
		} catch (IOException e) {
			logger.warning("Folgende exception ist aufgetreten: "+e);
			e.printStackTrace();
		}
	}
	
	public Integer[] getVersions() {
		
		File[] file = this.listFiles();
		LinkedList<Integer> namen = new LinkedList<Integer>();
		
		for(int i = 0; i<file.length;i++) {
			
			if(file[i].getName().contains(".properties"))continue;
			
			String version = file[i].getName();
			//TODO funktioniert nicht bei Dateiendungen mit v 			
			version = version.substring(version.lastIndexOf("v")+1,version.lastIndexOf("."));
			
			namen.add(Integer.parseInt(version));
			logger.finer("Gefundene Version: "+version);
		}
		return namen.toArray(new Integer[namen.size()]);
	}
}

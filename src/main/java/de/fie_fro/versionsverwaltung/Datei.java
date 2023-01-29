package de.fie_fro.versionsverwaltung;



//de.fie_fro.versionsverwaltung
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.filechooser.FileNameExtensionFilter;

public class Datei extends File{
	private Properties props;
	private boolean editable;
	private int currentversion;
	
	public Datei(String pPath) {
		super(pPath);
		readProperties();
	}
	
	public int getCurrentversion() {
		return currentversion;
	}

	public void setCurrentversion(int currentversion) {
		this.currentversion = currentversion;
		this.writeProperties();

	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		this.writeProperties();
	}
	
	public void readProperties() {
		try
		{
			this.props = new Properties();
			this.props.load(new FileInputStream(this.getAbsolutePath() + "\\" + this.getName() + ".properties"));
			this.editable = Boolean.parseBoolean((String)this.props.get("editable"));
			this.currentversion = Integer.parseInt((String)this.props.get("currentversion"));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void writeProperties() {
		try {
			this.props.setProperty("editable", String.valueOf(editable));
			this.props.setProperty("currentversion", String.valueOf(currentversion));
			this.props.store(new FileOutputStream(this.getAbsolutePath() + "\\" + this.getName() + ".properties"),null);
		} catch (FileNotFoundException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Integer[] getVersions() {
		
		File[] file = this.listFiles();
		LinkedList<Integer> namen = new LinkedList<Integer>();
		
		for(int i = 0; i<file.length;i++) {
			
			if(file[i].getName().contains(".properties"))continue;
			
			String version = file[i].getName();
			//funktioniert nicht bei Dateiendungen mit v 			
			version = version.substring(version.lastIndexOf("v"),version.lastIndexOf("."));
			
			namen.add(Integer.parseInt(version));
		}
		return namen.toArray(new Integer[namen.size()]);
	}
}

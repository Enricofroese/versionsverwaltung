package de.fie_fro.versionsverwaltung;

//import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

/**
 * Tests for "Versionsverwaltung"
 */
@TestMethodOrder(OrderAnnotation.class)
public class AppTest 
{
	Service serv = new Service("JUnitTestRepo");
	@BeforeAll
	public static void aufraeumen() {
    	File file = new File( "C:\\repos\\JUnitTestRepo");

    	if(file.isDirectory()){
    		File[] listFiles = file.listFiles();
    		for(int i = 0; i < listFiles.length; i++){
    			File tempfile=(listFiles[i]);
    			if(tempfile.isDirectory()){         
    	     	     File[] listFiles2 = tempfile.listFiles();
    	     	     for(int j = 0; j < listFiles2.length; j++){
    	     	    	 listFiles2[j].delete();
    	     	     }
    	     	}
    			tempfile.delete();
    		}
    	}
    }
    
	@Test
	@Order(1)
    @DisplayName("Upload-Neue-Datei")
    public void uploadNewFileTest()
    {
        boolean vorhanden = false;
    	serv.uploadNewFile(".\\src\\test\\v1\\testupload.txt");
    	for (String s : serv.getRepoContent()){ 
			if(s.equals("testupload")) {
				vorhanden = true;
			}
		}
    	assertTrue(vorhanden);
    }
	
	@Test
	@Order(3)
	@DisplayName("Upload-Zweite-Dateiversion")
	public void uploadNewVersionTest() {
		boolean vorhanden = false;
    	serv.uploadNewVersion("testupload", ".\\src\\test\\v2\\testupload.txt");
    	Integer[] hist = serv.getFileVersionHistory("testupload");
    	for(int i = 0; i<hist.length;i++) {
			if(hist[i] == 2){
				vorhanden = true;
			}
		}
    	assertTrue(vorhanden);
    }
	
	@Test
	@Order(2)
	@DisplayName("Set-Lock-bei-Edit")
	public void editFileTest() {
		serv.editFile("testupload");
		//System.out.println(Service.fileHandler.isLocked());
		assertTrue(Service.fileHandler.isLocked());
	}
	
	@Test
	@Order(4)
	@DisplayName("Lock-entfernt")
	public void isUnlockedAgainTest() {
		Service.fileHandler.setCurrentFile("testupload");
		assertFalse(Service.fileHandler.isLocked());
	}
	
	@Test
	@Order(5)
	@DisplayName("Version-zuruecksetzen")
	public void setVersionBackTest() {
		serv.setFileBackToVersion("testupload", "1");
		assertEquals(1,Service.fileHandler.getVersion());
	}
	
	@Test
	@Order(6)
	@DisplayName("Compare-von-Versionen")
	public void compareVersionsTest() {
		serv.compare("testupload", "1", "2");
		//Keine Fehlermeldung ist hier Erfolg, man kann leider keine Consolen-Eingaben "asserten"
		//In der Console kann man aber das Ergebnis vom Vergleich sehen
	}
}

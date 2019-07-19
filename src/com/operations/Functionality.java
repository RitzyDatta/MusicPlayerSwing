package com.operations;

import java.io.File; 
import org.apache.log4j.Logger;
import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner; 
  
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line.Info;
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import com.UI.AudioPlayerUI; 

public class Functionality {

	Long currentFrame;
    public Clip clip;
   // String status;
    AudioInputStream audioInputStream; 
    public String filePath;
    public List<String> toBePlayed;
    File file;
    public TrackInformation info;
    HashMap<String,ArrayList<String>> playList;
    static Logger log = Logger.getLogger(Functionality.class.getName());
   // Audioformat format;
    //= "C:\\Users\\Ritzy\\Music\\Veer-Zaara.wav";
    
    public Functionality()  {
    	currentFrame = null;
    	filePath=null;
    	toBePlayed = new ArrayList<String>();
    	playList=new HashMap<String,ArrayList<String>>();
    	
    //	status=new String("none");
    	}
    
    public void playMusic(String status) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
    	
    	System.out.println("play music called");
    	System.out.println(status);
    	
    	if(status=="none") { // no music is previously selected
    		// create AudioInputStream object
    		if(filePath != null) {
    			status="play";
    			
				audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile()); 
				
				// create clip reference 
				clip = AudioSystem.getClip();
				  
				// open audioInputStream to the clip 
				clip.open(audioInputStream);
				clip.loop(0);
				
 
				//clip.loop(Clip.LOOP_CONTINUOUSLY);
    			
	    			
    		}
    		else
    			System.out.println("blank path");
    	}
    	else if(status=="paused") { // music is paused
    		clip.close();
    		resetAudioStream();
    		clip.setMicrosecondPosition(currentFrame);
    		clip.start();
    		
    	}
    	
    }
    
    public void pauseMusic(String status) {
    	
    	if(status.equals("play")) {
    		currentFrame = clip.getMicrosecondPosition();
    		clip.stop();
    		
    		
    	}
    }
    /**
     * this is to open a music file from the local drive
     * @param window
     * @throws LineUnavailableException 
     * @throws IOException 
     * @throws UnsupportedAudioFileException 
     */
    public void openFile(JFrame window) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    	
    	JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    	 fc.setAcceptAllFileFilterUsed(false);
    	fc.addChoosableFileFilter(new FileNameExtensionFilter( "songs", "wav", "mp3"));
    	int i=fc.showOpenDialog(window);
    	 if(i==JFileChooser.APPROVE_OPTION){
    		 File f=fc.getSelectedFile();
    	     filePath=f.getPath();
    	     
    	     //store information of the selected file
    	     try {
				info= new TrackInformation(filePath);
			} catch (SAXException | TikaException e) {
				//e.printStackTrace();
				log.error( "failed!", e);
			}
    	     
    	     
    	  //   System.out.println(filePath);
    	     
    	 }
    }
    
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException { 
		audioInputStream = AudioSystem.getAudioInputStream( 
		new File(filePath).getAbsoluteFile()); 
		clip.open(audioInputStream); 
		clip.loop(Clip.LOOP_CONTINUOUSLY); 
		}
    
    public void stop() throws UnsupportedAudioFileException, IOException, LineUnavailableException  { 
        currentFrame = 0L; 
        clip.stop(); 
        clip.close();
    }
    
    public ArrayList<String> createPlayList(JFrame window) {
    	JFrame temp;  
	    temp=new JFrame();
	    ArrayList <String> tempList = new ArrayList<String>();
	    
	    String name=JOptionPane.showInputDialog(temp,"Enter Name");
	    
	    //open multiple file chooser
	    JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	    fc.setMultiSelectionEnabled(true);
	    fc.addChoosableFileFilter(new FileNameExtensionFilter( "songs", "wav", "mp3"));	
	    int i = fc.showOpenDialog(window);
	    
	    if(i==JFileChooser.APPROVE_OPTION){
   		 File[] filesForPlayList= fc.getSelectedFiles();
   		 
   		 for(File q : filesForPlayList) {
   			tempList.add(q.getPath());
   		 }
   		 
   		playList.put(name, tempList);
   	     
	    }
	    
	    return tempList;
    }
    
    
    
    
}

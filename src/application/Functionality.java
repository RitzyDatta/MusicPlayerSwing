//USING JAVA FX


package application;
import java.awt.Container;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Functionality {
	
	@FXML Container border;
	@FXML MenuBar file;
	@FXML MenuItem open;
	@FXML MenuItem Create_PlayListBtn;
	@FXML Button stop;
	@FXML Button next;
	@FXML Button previous;
	@FXML ToggleButton playPause;
	@FXML Label labelCurrentSong;
	@FXML Slider timeslider;
	@FXML ListView<String> list;
	@FXML ListView<String> playListName;
	
	MediaPlayer mediaPlayer;
	FileChooser fileChooser;
	File fileForSong;
	TrackInformation info;
	Stage primaryStage;
	Media media;
	Double currentTime,totalTime,audioPosition;
	TextInputDialog dialog;
	 List <File> listOfFiles; //this is to keep all the selected files for a playlist
	 int index;
	Boolean isPlayList;
	
	//Status status;
	
	
	public Functionality() {
		primaryStage = new Stage();
		currentTime = null;
		audioPosition= 0.0;
		timeslider= new Slider();
		list = new ListView<String>();
		dialog= new TextInputDialog();
		isPlayList = false;
		index=0;
	}
	
	
	@FXML
	public void PlayMusic() {
        
		if(isPlayList && playPause.isSelected()) {
			playPlayList();
		}
		else if(isPlayList && !playPause.isSelected()) {
			pause();
		}
		else if(playPause.isSelected())
        	play();
        else
        	pause();
        
	}
	
	public void play() {
		
		if(mediaPlayer != null) {
			Status status=mediaPlayer.getStatus();
			//System.out.println("status" +status);
			
			if(status==Status.READY || status==Status.UNKNOWN) {
				
				playPause.setText("Pause");
					
			        //by setting this property to true, the audio will be played   
			    //    mediaPlayer.setAutoPlay(true);
			        mediaPlayer.play();
			        
			        mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
			            public void invalidated(Observable ov) 
			            { 
			            	Status status = mediaPlayer.getStatus();
			            //	System.out.println(status);
			            	
			            	sliderControl(); 
			            } 
			        }); 
			        
				
				
			}
			else if(status==Status.PAUSED) {
				playPause.setText("Play");
				mediaPlayer.play();
			}
		}
		
	}
	
	public void pause() {
		if(mediaPlayer != null) {
			playPause.setText("Play");
			mediaPlayer.pause();
		}
		
	}
	
	public void nextSong() {
		
		if(isPlayList) { // only if more than one song is selected then we can select next or previous
			if(listOfFiles.size() >1) { // if there is only one song we don't have any next or previous songs
				stop();
				int tempIndex=index;
				
				
				System.out.println("in next song");
				
				if(tempIndex<listOfFiles.size()) {
					System.out.println("TEMP INDEX" + tempIndex);
					try {
						
						String temp = listOfFiles.get(tempIndex).getAbsolutePath();
						labelCurrentSong.setText(temp); // it shows the current song name in the player
						media = new Media(listOfFiles.get(tempIndex).toURI().toString());  
				        //System.out.println("filepath:  "+listOfFiles.get(index));
				        //Instantiating MediaPlayer class   
				        mediaPlayer = new MediaPlayer(media);
						info= new TrackInformation(listOfFiles.get(tempIndex).getPath()); //NEED CORRECTION
					} catch (IOException | SAXException | TikaException e) {
						
						e.printStackTrace();
					}
					index++;
					play();
					
				}
				else {
					playPause.setSelected(false);
					mediaPlayer.stop();
					index=0;
				}
				
			}
		}
	}
	
	
	public void previousSong() {
		if(isPlayList && listOfFiles.size()>1) {
			int tempIndex=index -2;
			stop();
			System.out.println("in previous song");
			
			if(tempIndex>=0) {
				System.out.println("TEMP INDEX" + tempIndex);
				try {
					
					String temp = listOfFiles.get(tempIndex).getAbsolutePath();
					labelCurrentSong.setText(temp); // it shows the current song name in the player
					media = new Media(listOfFiles.get(tempIndex).toURI().toString());  
			        //System.out.println("filepath:  "+listOfFiles.get(index));
			        //Instantiating MediaPlayer class   
			        mediaPlayer = new MediaPlayer(media);
					info= new TrackInformation(listOfFiles.get(tempIndex).getPath()); //NEED CORRECTION
				} catch (IOException | SAXException | TikaException e) {
					
					e.printStackTrace();
				}
				index--;
				play();
				
			} // end of if(tempIndex>0)
			else {				
				playPause.setSelected(false);
				mediaPlayer.stop();
				index=0;
			}
		} //end of if(isPlayList && listOfFiles.size()>1)
	}
	
	public void playPlayList() {
		if(listOfFiles != null) {
			if(index<listOfFiles.size()) {
				System.out.println("index" + index);
				try {					
					String temp = listOfFiles.get(index).getAbsolutePath();
					labelCurrentSong.setText(temp); // it shows the current song name in the player
					media = new Media(listOfFiles.get(index).toURI().toString());  
			         System.out.println("filepath:  "+listOfFiles.get(index));
			        //Instantiating MediaPlayer class   
			        mediaPlayer = new MediaPlayer(media);
					info= new TrackInformation(listOfFiles.get(0).getPath()); //NEED CORRECTION
				} catch (IOException | SAXException | TikaException e) {
					
					e.printStackTrace();
				}
				index++;
				play();				
			}
			else {
				index=0;
				mediaPlayer.stop();
				playPause.setSelected(false);								
			}			
		}
	}
	
	
	 public void openFile() {
		 
		 isPlayList=false; // play list song will not be played
		 fileChooser = new FileChooser();
		 fileChooser.setTitle("Open File");
		 //fileChooser.setSelectedExtensionFilter("wav", "mp3");
		 
		 fileForSong = fileChooser.showOpenDialog(primaryStage);
		 System.out.println(fileForSong);
		 String temp = fileForSong.getAbsolutePath();
		 System.out.println("temp" + temp);
		 labelCurrentSong.setText(temp);
		 
		 if(fileForSong != null) {
			//store information of the selected file
			try {
				media = new Media(fileForSong.toURI().toString());  
		          
		        //Instantiating MediaPlayer class   
		        mediaPlayer = new MediaPlayer(media);  
		          
				info= new TrackInformation(fileForSong.getPath());
			} catch (IOException | SAXException | TikaException e) {
				
				e.printStackTrace();
			}
			
			
		 }
	    }
	 
	 public void stop() {
	        currentTime = null; 
	        mediaPlayer.stop();
	        playPause.setSelected(false);
			playPause.setText("Play");
	        
	    }
	
	 public void sliderControl() {
		 currentTime = mediaPlayer.getCurrentTime().toSeconds();
		 totalTime = mediaPlayer.getTotalDuration().toSeconds();
		 
	//	 System.out.println("currentTime" + currentTime);
	//	 System.out.println("totalTime" + totalTime);
		 
		 audioPosition= (currentTime/(totalTime))*100; //as slide bar min length is 0 and max length is 100
		 
	//	 System.out.println("audioPosition" + audioPosition);
		 timeslider.setValue(audioPosition);
			
		 
		mediaPlayer.setOnEndOfMedia(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				 // Restart the music
				timeslider.setValue(0);
				
				
				//this is for playlist
				if(isPlayList) {
					
					System.out.println("this function was called");
					playPlayList();
				}
				else {
					mediaPlayer.seek(mediaPlayer.getStartTime());
					playPause.setSelected(false);
					playPause.setText("Play");
					mediaPlayer.stop();

				}
			}
			
		});
		 
	/*	if(currentTime.intValue() == totalTime.intValue()) {
			 // Restart the music
			timeslider.setValue(0);
			
			
			//this is for playlist
			if(isPlayList) {
				
				System.out.println("this function was called");
				playPlayList();
			}
			else {
				mediaPlayer.seek(mediaPlayer.getStartTime());
				playPause.setSelected(false);
				playPause.setText("Play");
				mediaPlayer.stop();

			}
			
		} */
		
		 
	 }
	 
	 public void createPlayList() {
		 ArrayList <String> tempList = new ArrayList<String>();
		 
		 
		 dialog.setTitle("Create Play List");
		 dialog.setHeaderText("Enter a Name:");
		 Optional<String> result = dialog.showAndWait();
		 if(result.isPresent()) { //this means, user has entered the name of the playlist
			 fileChooser = new FileChooser();
			 fileChooser.setTitle("selct files");
			 listOfFiles=  fileChooser.showOpenMultipleDialog(primaryStage);
			 
			 if(listOfFiles != null) {
				 ObservableList<String> names = FXCollections.observableArrayList();
				//add playList name->
				 ObservableList<String> temp  = FXCollections.observableArrayList(result.get());
				 
				 names.addAll(temp);
				 
				 for(File f : listOfFiles) {
					 names.add(f.getPath());
				 }
				 
				 
				 
				 list.setItems(temp);
				 list.setItems(names);
				 isPlayList = true;
				 
				 
				 				 
			 }
		 }
	 }
	 
	 public void dragSlider() {
		 
		 Duration duration = mediaPlayer.getTotalDuration();
	//	 mediaPlayer.stop();
	//	 timeslider.setValue(timeslider.getValue());
		 mediaPlayer.seek(duration.multiply(timeslider.getValue() / 100.0));
	//	 mediaPlayer.play();
	 }

}

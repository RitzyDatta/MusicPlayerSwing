//USING JAVA FX


package com.music.application;
import java.awt.Container;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
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
	@FXML ToggleButton playPause,repeat;
	@FXML Label labelCurrentSong;
	@FXML Slider timeslider,volumeBar;
	@FXML ListView<String> list;
	@FXML ListView<String> playListName;
	
	MediaPlayer mediaPlayer;
	FileChooser fileChooser;
	File fileForSong;
	TrackInformation info;
	Stage primaryStage;
	Media media;
	Double currentTime,totalTime,audioPosition,onclicked;
	TextInputDialog dialog;
	Optional<String> result; // store create play list name
	//List <File> listOfFiles; //this is to keep all the selected files for a playlist
	ArrayList <String> listOfFiles; //this is to keep all the selected files for a playlist
	int index;
	Boolean isPlayList;
	Boolean isPaused;
	
	//Status status;
	
	
	public Functionality() {
		primaryStage = new Stage();
		currentTime = null;
		audioPosition= 0.0;
		timeslider= new Slider();
		volumeBar = new Slider();
		list = new ListView<String>();
		dialog= new TextInputDialog();
		isPlayList = false;
		index=0;
		listOfFiles= new ArrayList<String>();
		isPaused = false;
		onclicked =0.0;
	}
	
	
	public void reset() {
		 media=null;
		 mediaPlayer=null;
		 timeslider.setValue(0);
		 list = new ListView<String>();
		 isPlayList = false;
		 index=0;
		listOfFiles= new ArrayList<String>();
		onclicked =0.0;
		if(mediaPlayer !=null)
			mediaPlayer.dispose();

	 }
	
	public void repeat() {
		 if(repeat.isSelected()) {
			 stop();
			 Timeline timeline = new Timeline(
		        	    new KeyFrame(Duration.seconds(0.8), e -> play()));
		      timeline.play();
		 }
			
			
	 }
	
	public String getName(String name) {
		name=name.replace("\\", "\\\\");
		System.out.println(name);
		 String[] arrOfStr = name.split("\\\\");
		 int len=arrOfStr.length;
		 
		//return null;
		return arrOfStr[len-1]; 
		 
	    } 
	
	@FXML
	public void PlayMusic() {
        
		if(isPlayList && playPause.isSelected()) {
			//playPlayList();
			play();
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
			System.out.println("status" +status);
			
			if(status==Status.READY || status == Status.STOPPED) {
				
				playPause.setText("Pause");
				playPause.setSelected(true);
					
			        //by setting this property to true, the audio will be played   
			    //    
			        mediaPlayer.play();
			        
			        volumeBar.setValue( mediaPlayer.getVolume()*100);
			        System.out.println("Volume is: " +mediaPlayer.getVolume());
			        mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
			            public void invalidated(Observable ov) 
			            { 
			            	Status status = mediaPlayer.getStatus();
			            //	System.out.println(status);

			            	timeslider.getOnDragDetected();
			            	sliderControl(); 
			            } 
			        }); 
			        
				
				
			}
			else if(status==Status.PAUSED) {
				System.out.println("status paused called");
				playPause.setText("Pause");
				mediaPlayer.play();
			}
			else if( status == Status.UNKNOWN) {   // this is to show if the file status is unknown
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setContentText("Ooops, there was an error!");

				alert.showAndWait();
			}
		}
		
	}
	
	public void pause() {
		if(mediaPlayer != null) {
			System.out.println("Pause function called");
			playPause.setText("Play");
			mediaPlayer.pause();
			isPaused = true;
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
						
						String temp = listOfFiles.get(tempIndex);
						
						labelCurrentSong.setText(getName(temp));// it shows the current song name in the player
						media = new Media(new File(listOfFiles.get(tempIndex)).toURI().toString());
						list.scrollTo(tempIndex);
						list.getSelectionModel().select(tempIndex+1); //select the song which is currently playing
				        //System.out.println("filepath:  "+listOfFiles.get(index));
				        //Instantiating MediaPlayer class   
				        mediaPlayer = new MediaPlayer(media);
						info= new TrackInformation(listOfFiles.get(tempIndex)); //NEED CORRECTION
					} catch (IOException | SAXException | TikaException e) {
						
						e.printStackTrace();
					}
					index++;
					 //this is to delay the function
			        Timeline timeline = new Timeline(
			        	    new KeyFrame(Duration.seconds(0.8), e -> play()));
			       timeline.play();
					
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
					
					String temp = listOfFiles.get(tempIndex);
					labelCurrentSong.setText(getName(temp));// it shows the current song name in the player
					media = new Media(new File(listOfFiles.get(tempIndex)).toURI().toURL().toExternalForm());
					list.scrollTo(tempIndex);
					list.getSelectionModel().select(tempIndex+1); //select the song which is currently playing
			        //System.out.println("filepath:  "+listOfFiles.get(index));
			        //Instantiating MediaPlayer class   
			        mediaPlayer = new MediaPlayer(media);
					info= new TrackInformation(listOfFiles.get(tempIndex)); //NEED CORRECTION
				} catch (IOException | SAXException | TikaException e) {
					
					e.printStackTrace();
				}
				index--;
				
				 //this is to delay the function
		        Timeline timeline = new Timeline(
		        	    new KeyFrame(Duration.seconds(0.8), e -> play()));
		       timeline.play();
				
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
					String temp = listOfFiles.get(index);
					labelCurrentSong.setText(getName(temp)); // it shows the current song name in the player
				//	media = new Media(new File(listOfFiles.get(index)).toURI().toString());
					String path = new File(temp).toURI().toString();
					media = new Media(path);
					// media = new Media(new File(temp).toURI().toString());
			         System.out.println("filepath:"+listOfFiles.get(index));
			         System.out.println(media.getError());
			        //Instantiating MediaPlayer class   
			        mediaPlayer = new MediaPlayer(media);
			        System.out.println("Status" +mediaPlayer.getStatus());
			     //   play();
			        list.scrollTo(index);
			        list.getSelectionModel().select(index+1); //select the song which is currently playing
			        
					info = new TrackInformation(listOfFiles.get(index)); //NEED CORRECTION
				} catch (IOException | SAXException | TikaException e) {
					
					e.printStackTrace();
				}
					
					index++;		
			}
			else {
				index=0;
				mediaPlayer.stop();
				playPause.setSelected(false);								
			}			
		}
	}
	
	
	 public void openFile() {
		 
		 
		 fileChooser = new FileChooser();
		 fileChooser.setTitle("Open File");
		 //fileChooser.setSelectedExtensionFilter("wav", "mp3");
		 
		 fileForSong = fileChooser.showOpenDialog(primaryStage);
		 
		 if(fileForSong != null) {
			 
			 System.out.println(fileForSong);
			 String temp = fileForSong.getAbsolutePath();
			 System.out.println("temp" + temp);
			 
			 labelCurrentSong.setText(getName(temp));
			 
			//store information of the selected file
			try {
				stop();
				media = new Media(fileForSong.toURI().toString());  
		          
		        //Instantiating MediaPlayer class   
		        mediaPlayer = new MediaPlayer(media);
		        
		        //this is to delay the function
		        
		        Timeline timeline = new Timeline(
		        	    new KeyFrame(Duration.seconds(0.8), e -> play()));
		        timeline.play();
		       
		     /*  Thread t1 = new Thread(new Runnable() {
		    	    @Override
		    	    public void run() {
		    	        play();
		    	    }
		    	});
		    	t1.start(); */
		       
		       
				info= new TrackInformation(fileForSong.getPath());
			} catch (IOException | SAXException | TikaException e) {
				
				e.printStackTrace();
			}
			
			
		 }
	    }
	 
	 public void stop() {
		 if(mediaPlayer!=null) {
			 System.out.println("Stop");
			currentTime = null; 
	        mediaPlayer.stop();
	        playPause.setSelected(false);
			playPause.setText("Play");
		 }
	        
			
	        
	    }
	
	 public void sliderControl() {
		 currentTime = mediaPlayer.getCurrentTime().toSeconds();
		 totalTime = mediaPlayer.getTotalDuration().toSeconds();
		 
	//	 System.out.println("currentTime" + currentTime);
	//	 System.out.println("totalTime" + totalTime
		 audioPosition= (currentTime/(totalTime))*100; //as slide bar min length is 0 and max length is 100
		 
	//	 System.out.println("audioPosition" + audioPosition);
		 if(onclicked ==0.0)
			 timeslider.setValue(audioPosition);
		 else {
			 timeslider.setValue(onclicked);
			 onclicked=0.0;
		 }
			 
			 
			
		 
		mediaPlayer.setOnEndOfMedia(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				 // Restart the music
				timeslider.setValue(0);
				
				repeat();
				//this is for playlist
				if(isPlayList) {
					playPlayList();
					index++;
				}
				else {
					mediaPlayer.seek(mediaPlayer.getStartTime());
					playPause.setSelected(false);
					playPause.setText("Play");
					mediaPlayer.stop();

				}
			}
			
		});
		 
	 }
	 
	 
	public void createPlayList() {
		
		 List <File> allFiles = new ArrayList<File>();
		 
		 dialog.setTitle("Create Play List");
		 dialog.setHeaderText("Enter a Name:");
		 result = dialog.showAndWait();
		 
		 if(result.isPresent()) { //this means, user has entered the name of the playlist
			 fileChooser = new FileChooser();
			 fileChooser.setTitle("select files");
			// allFiles=  fileChooser.showOpenMultipleDialog(primaryStage);
			 allFiles = fileChooser.showOpenMultipleDialog(primaryStage);
			 
			 for(File f : allFiles)
				 listOfFiles.add(f.getAbsolutePath());
			 
			 
			 
			 if(listOfFiles != null) {
				 ObservableList<String> names = FXCollections.observableArrayList();
				//add playList name->
				 ObservableList<String> temp  = FXCollections.observableArrayList(result.get());
				 
				 names.addAll(temp);
				 
				 for(String s : listOfFiles) {
					 names.add(getName(s));
				 }
				 
				 
				 list.setItems(names);
				 isPlayList = true;
				 stop();
				 playPlayList();
				 
				 				 
			 }
		 }
	 }
	 
	 public void dragSlider() {
		 
		 System.out.println("dragslider");
		 
		 Duration duration = mediaPlayer.getTotalDuration();
	//	 mediaPlayer.stop();
		 mediaPlayer.seek(duration.multiply(timeslider.getValue() / 100.0));
		 onclicked = timeslider.getValue();
		 System.out.println("onclicked: " +onclicked);
	//	 
	 }
	 
	 public void shuffelPlayList() {
		 if(isPlayList) {
			// Collections.shuffle(listOfFiles);
			
			Random rand = new Random();
			int index;
			String temp, temp2;
			int len= listOfFiles.size();
			
			int j=0;
			for(int i=len-1; i>=0;i--) {
				index=rand.nextInt(len-j);
				System.out.println("i is -> " +i);
				temp = listOfFiles.get(i);
				temp2=listOfFiles.get(index);
				//swaping 
				listOfFiles.set(i, temp2);
				listOfFiles.set(index, temp);
				j++;
			} 
			
			 if(listOfFiles != null) {
				 ObservableList<String> names = FXCollections.observableArrayList();
				//add playList name->
				 ObservableList<String> temp1  = FXCollections.observableArrayList(result.get());
				 
				 names.addAll(temp1);
				 
				 for(String f : listOfFiles) {
					 names.add(getName(f));
					 System.out.println("fileName" +getName(f));
				 } 
				 list.setItems(names);
			}
			
		 }
		 
			
	 }
	 
	 public void playSelectedSong() {
		 
		 list.setOnMouseClicked(new EventHandler<MouseEvent>() {

			    @Override
			    public void handle(MouseEvent click) {

			        if (click.getClickCount() == 2) {
			        	stop();
			           //Use ListView's getSelected Item
			           index = list.getSelectionModel().getSelectedIndex()-1;
			           playPause.setSelected(true);
			           //use this to do whatever you want to. Open Link etc.
			           playPlayList();
			        }
			    }

				
			});
		 
		// index = list.getSelectionModel().getSelectedIndex() -1;
		 
	 }
	 
	 public void muteMedia() {
		 if(mediaPlayer != null) {
			 if(mediaPlayer.isMute() == false)
				 mediaPlayer.setMute(true);
			 else
				 mediaPlayer.setMute(false);
		 }
	 }
	 
	 public void setVolume() {
		 if(mediaPlayer != null) {
			 double vol = volumeBar.getValue()/100;
			 mediaPlayer.setVolume(vol);
			 
		 }
	 }
	 
	 
	 
	 

}

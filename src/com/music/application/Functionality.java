/**
 * Here we are using javafx library for this music player. This class contains all the functionality of the music player app.
 * In this class, in some function "Timeline" has been used, to introduce some delay before calling a function. It is used because, some problem were faced while executing the function
 * without any delay.
 */


package com.music.application;
import java.awt.Container;


import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

import javax.swing.text.TabableView;

import org.apache.commons.io.FileUtils;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Functionality {
	//VARIABLE DECLARATION
	/**
	 * @FXML is used to refer the component in the UI i.e. it is used to connect variable from backend to UI elements.
	 */
	
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
	@FXML Label startdur;
	@FXML Label enddur;
	//@FXML ListView<String> playListName;
	
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
	String playListName;
	ArrayList <String> libNames, libSongs;
	DirectoryChooser dicPath;
	
	FileInputStream inputButton;
	ImageView stopImage, playImage, nextImage, prevImage,shuffleImage,repeatImage,muteImage;
	
	
	//Status status;
	
	/**
	 * Here the constructor is used to initialize the variables.
	 * @throws FileNotFoundException 
	 */
	
	public Functionality() throws FileNotFoundException {
		primaryStage = new Stage();
		currentTime = null;
		audioPosition= 0.0;
		timeslider= new Slider();
		volumeBar = new Slider();
		list = new ListView<String>();
		isPlayList = false;
		index=0;
		listOfFiles= new ArrayList<String>();
		isPaused = false;
		onclicked =0.0;
		libNames = new ArrayList<String>();
		libNames.add("Add");
		libSongs = new ArrayList<String>();
		
	/*	playPause = new ToggleButton();
		playPause.setGraphic(new ImageView(new Image("stop.png"))); */
		
		
	}
	
	/**
	 * This function is used to repeat a particular song.
	 */
	
	public void repeat() {
		 if(repeat.isSelected()) {
			 timeslider.setVisible(true);
			 if(isPlayList) {
				 index--;
			 }
			 else {
				 stop();
				 Timeline timeline = new Timeline(
			        	    new KeyFrame(Duration.seconds(0.8), e -> play()));
			      timeline.play();
			 }
			 
			 
		 }
		
			
	 }
	/**
	 * This function is used to get the name of the song from the filepath. this will show the name by which song is saved in the local machine.
	 * Here the function is taking the path of the song as parameter. Path will be split by "\"(back slash) and the last string will be the song name. 
	 * @param name
	 * @return
	 */
	
	public String getName(String path) {
		path=path.replace("\\", "\\\\");
		System.out.println(path);
		 String[] arrOfStr = path.split("\\\\");
		 int len=arrOfStr.length;
		return arrOfStr[len-1]; 
		 
	    }
	
		
	@FXML
	
	/**
	 * When the play button is pressed in the UI it calls this function. If the button is selected then it calls the play function, otherwise it calls the pause function
	 */
	public void PlayMusic() {
        
		if(playPause.isSelected()) {

			Timeline timeline = new Timeline(
			new KeyFrame(Duration.seconds(0.8), e -> {
				try {
					play();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}));
	        timeline.play();
			
		}
        else
        	pause();
        
	}
	
	/**
	 * This function plays song when mediaplayer is already loaded with the song previously.
	 * This function first decides the state of the media player then it takes action based on that. 
	 */
	
	public void play() {
		
		if(mediaPlayer != null) {
			Status status=mediaPlayer.getStatus();
			System.out.println("status" +status);
			
			if(status==Status.READY || status == Status.STOPPED) {
				
				//playPause.setText("Pause");
				
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
			//	playPause.setText("Pause");
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
	
	/**
	 * This function is used to pause the currently playing song.
	 */
	
	public void pause() {
		if(mediaPlayer != null) {
			System.out.println("Pause function called");
	//		playPause.setText("Play");
			mediaPlayer.pause();
			isPaused = true;
		}
		
	}
	
	
	/**
	 * This function is used to play the next song in the playlist. This function won't work if there is no playlist currently loaded or there is only one song in the playlist.
	 */
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
						info= new TrackInformation(listOfFiles.get(tempIndex),getName(temp)); //NEED CORRECTION
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
	
	/**
	 * This function is used to play the previous song in the playlist. This function won't work if there is no playlist currently loaded or there is only one song in the playlist.
	 */
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
					info= new TrackInformation(listOfFiles.get(tempIndex), getName(temp)); //NEED CORRECTION
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
	
	/**
	 * 
	 */
	
	public void playPlayList() {
		if(listOfFiles != null) {
			if(index<listOfFiles.size()) {
				System.out.println("index" + index);
				try {					
					String temp = listOfFiles.get(index);
					labelCurrentSong.setText(getName(temp)); // it shows the current song name in the player
					String path = new File(temp).toURI().toString();
					media = new Media(path);
			         System.out.println("filepath:"+listOfFiles.get(index));
			         System.out.println(media.getError());
			        //Instantiating MediaPlayer class   
			        mediaPlayer = new MediaPlayer(media);
			        System.out.println("Status" +mediaPlayer.getStatus());
			        

					Timeline timeline = new Timeline(
			        	    new KeyFrame(Duration.seconds(0.8), e -> play()));
			        timeline.play();
					
			        
			        list.scrollTo(index);
			        list.getSelectionModel().select(index+1); //select the song which is currently playing
			        
					info = new TrackInformation(listOfFiles.get(index),temp); //NEED CORRECTION
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
	
	
	 public void openFile() throws IOException {
		 
		 fileChooser = new FileChooser();
		 fileChooser.setTitle("Open File");
		 //fileChooser.setSelectedExtensionFilter("wav", "mp3");
		 
		 fileChooser.getExtensionFilters().addAll(
				    new FileChooser.ExtensionFilter("all", "*.mp3","*.wav","*.aif","*.aiff")
				);
		 fileForSong = fileChooser.showOpenDialog(primaryStage);
		 
		 if(fileForSong != null) {
			 
			 System.out.println(fileForSong);
			 String temp = fileForSong.getAbsolutePath();
			 System.out.println("temp" + temp);
			 
			 labelCurrentSong.setText(getName(temp));
			 
			stop();
			media = new Media(fileForSong.toURI().toString());  
			  
			//Instantiating MediaPlayer class   
			mediaPlayer = new MediaPlayer(media);
			
			//this is to delay the function
			try {
				info= new TrackInformation(fileForSong.getPath(),getName(temp));
			} catch (SAXException | TikaException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Timeline timeline = new Timeline(
				    new KeyFrame(Duration.seconds(0.8), e -> play()));
			timeline.play();
			
			
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
		 
		 Double tempTotal= mediaPlayer.getTotalDuration().toMinutes();
		 Double tempCur = mediaPlayer.getCurrentTime().toMinutes();
		 enddur.setText(tempTotal.toString());
		 
	//	 System.out.println("currentTime" + currentTime);
	//	 System.out.println("totalTime" + totalTime
		 audioPosition= (currentTime/(totalTime))*100; //as slide bar min length is 0 and max length is 100
		 
	//	 System.out.println("audioPosition" + audioPosition);
		 if(onclicked ==0.0) {
			 timeslider.setValue(audioPosition);
			 startdur.setText(tempCur.toString());
		 }
			
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
					System.out.println("inside isplaylist");
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
		 
	 }
	 
	 
	public void createPlayList() {
		
		
		 List <File> allFiles = new ArrayList<File>();
		 dialog = new TextInputDialog();
		 
		 dialog.setTitle("Create Play List");
		 dialog.setHeaderText("Enter a Name:");
		 result = dialog.showAndWait();
		 
		 if(result.isPresent()) { //this means, user has entered the name of the playlist
			 fileChooser = new FileChooser();
			 fileChooser.setTitle("select files");
			 fileChooser.getExtensionFilters().addAll(
					    new FileChooser.ExtensionFilter("all", "*.mp3","*.wav","*.aif","*.aiff")
					);
			 allFiles = fileChooser.showOpenMultipleDialog(primaryStage);
			 
			 if(mediaPlayer != null) {
				 stop();
				 mediaPlayer.dispose();
			 }
			 
			 if(allFiles != null) {
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
					 playListName=result.get();
					 				 
				 }
				 else {
					 dialog = new TextInputDialog();
				 }
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
	 
	 /**
		 * This function is used to save play list in the local machine.
		 * @param path
		 */
		
		public void saveList() {
			if(list!=null) {
				 FileChooser fileChooser = new FileChooser();
			   	  
		         //Set extension filter
		         FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ritz", "*.ritz");
		         fileChooser.getExtensionFilters().add(extFilter);
		         fileChooser.setInitialFileName(playListName);
		         //Show save file dialog
		         File file = fileChooser.showSaveDialog(primaryStage);
		         
		         if(file != null){		    			 
	    			 try {
	    				  FileOutputStream fout=new FileOutputStream(file.getPath());  
	    				  ObjectOutputStream out=new ObjectOutputStream(fout); 
	    				  out.writeObject(listOfFiles);
	    				  out.writeObject(playListName);
	    				  out.flush();  
	    				  System.out.println("success");  
	    				  
	    				  out.close();
	    				  fout.close();
	    				}
	    				catch (IOException e) {  
	    		            e.printStackTrace();  
	    		        }
		         }
			}
			
			
			 
		 }
	 
	 
	 /**
		 * 
		 * @param path
		 */
		
		public void loadPlayList() {
			 isPlayList = true;
			 fileChooser = new FileChooser();
			 fileChooser.setTitle("Open an existing play list");
			 
			 fileChooser.getExtensionFilters().addAll(
					    new FileChooser.ExtensionFilter("ritz", "*.ritz")
					);
			 File file = fileChooser.showOpenDialog(primaryStage);
			
			try {
				if(file.exists() && !file.isDirectory()) {
					ObjectInputStream deserial=new ObjectInputStream(new FileInputStream(file));
					try {
						//list1 = (ListView<String>) deserial.readObject();
						listOfFiles= (ArrayList<String>) deserial.readObject();
						playListName=(String) deserial.readObject();
						System.out.println(playListName);
						
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
					
					if(listOfFiles != null && playListName != null) {
						 ObservableList<String> names = FXCollections.observableArrayList();
						//add playList name->
						 ObservableList<String> temp1  = FXCollections.observableArrayList(playListName);
						 
						 names.addAll(temp1);
						 
						 for(String s : listOfFiles) {
							 names.add(getName(s));
						 }
						 
						 
						 list.setItems(names);
					}
					deserial.close();
					
				}
				
			}
			catch (IOException e) {  
	            e.printStackTrace();  
	        }
			
		}
		
	 

}

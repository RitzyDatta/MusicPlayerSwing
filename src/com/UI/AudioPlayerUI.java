package com.UI;

import java.awt.BorderLayout;
import org.apache.log4j.Logger;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import com.operations.Functionality;

public class AudioPlayerUI implements ActionListener,Runnable,MouseListener {

	JFrame window;
	ImageIcon play, pause,stop, next, previous;
	JButton forward, backward, stopButton;
	JToggleButton playPause;
	JSlider slider;
	Timer timer;
//	ActionListener ac;
	JMenuBar menubar;
	JMenu file,help; 
	JMenuItem open, createPlayList;
	JPanel playList;
	JScrollPane scroll;
	JLabel playListLabel, display, heading;
	ActionEvent action;
	Functionality operations;
	String status;
	Long currentFrame;
	int audioPosition,len;
	JList <String> listOfItem;
	static Logger log = Logger.getLogger(AudioPlayerUI.class.getName());
	
	
	/**
	 * Initialization take place in constructor
	 * @throws LineUnavailableException 
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 */
	public AudioPlayerUI() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		
		window = new JFrame("Music Player");

        play = new ImageIcon("./src/resources/play.png");
        pause = new ImageIcon("./src/resources/pause.png");
        stop = new ImageIcon("./src/resources/stop.png");
        next = new ImageIcon("./src/resources/next.png");
        previous = new ImageIcon("./src/resources/back.png");
        
        playPause= new JToggleButton("play");
        stopButton= new JButton("stop");        
        forward = new JButton("next");
        backward= new JButton("previous");
        slider = new JSlider(JSlider.HORIZONTAL,0,100,0); 
        
        menubar = new JMenuBar();
        file=new JMenu("File");
        help=new JMenu("Help");
        open= new JMenuItem("Open");
        createPlayList = new JMenuItem("Create PlayList");
        
        playList= new JPanel(new FlowLayout());
        
        playListLabel = new JLabel("Your Play List:");
        display= new JLabel(); // Name of the song
        heading = new JLabel(); //Now playing
        
        //for functionality class
        operations= new Functionality();
        status="none";
        
	}
	
	@Override
	public void run() {
		
		//creating the menu bar-
		
		file.add(open); file.add(createPlayList);
		menubar.add(file); menubar.add(help);
		
		//display
		heading.setBounds(100, 20, 450, 100);
		display.setBounds(100, 80, 450, 100);
		heading.setText("Now Playing");
		
		//slider.setBorder(new CompoundBorder(new EmptyBorder(6, 10, 10, 10), border));
		//slider.addChangeListener(window);
		
		//display.setText("testing something with something");
		
		//create play list area
		
		playListLabel.setBounds(500, 10, 200, 50);
	//	playList.setBounds(500, 30, 250, 400);
		//scroll.setBounds(500, 30, 250, 400);
		playList.add(playListLabel);
		playList.setBackground(Color.blue);
		
		scroll=new JScrollPane(playList);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(550, 50, 300, 350);
		//creating play area
		
        stopButton.setBounds(50, 280, 70, 70);
        backward.setBounds(200, 300, 80, 50);
        playPause.setBounds(300, 300, 80, 50);
        forward.setBounds(400, 300, 80, 50);
        slider.setBounds(50, 240, 450, 20);
        
     
        timer= new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
			
			if(audioPosition >= 100 ) {
				
				try {
					operations.stop();
					slider.setValue(0);
					timer.stop();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
					log.debug(e);
				}
				
			}
			else {
				audioPosition = (int) ((operations.clip.getMicrosecondPosition()/ 1000000)*(100/(int)operations.info.durationInSeconds));
			    slider.setValue(audioPosition);
	            System.out.println("in timer loop" + audioPosition);
	            
			}
			
			}		
			
        });
       
        
       
	   playPause.addActionListener(this);
	   backward.addActionListener(this);
	   forward.addActionListener(this);
	   stopButton.addActionListener(this);
	   heading.addMouseListener(this);
	   open.addActionListener(this);
	   createPlayList.addActionListener(this);
	   
	   
	  //  window.add(playPause);
	    window.add(menubar);
	    window.setJMenuBar(menubar);
	    window.add(heading);
	    window.add(display);
	    window.add(stopButton);
	    window.add(backward);
	    window.add(playPause);
	    window.add(forward);
	    window.add(slider);
	    window.add(playListLabel);
	    window.add(scroll);
	   // window.add(new JScrollPane(playList));
	   // window.add(playListLabel);
	   
	    window.setLocationRelativeTo(null);
	    
	    window.setLayout(null);
	    window.setSize(900, 500);
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //window.setBackground(Color.GREEN);
	    window.setVisible(true);
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==playPause) {
			if(playPause.isSelected()) {
				
 				try {
 					operations.playMusic(status);
 					status="play";
 					playPause.setText("Pause");
 					
 					System.out.println("duration in seconds: " +operations.info.durationInSeconds);
 					timer.start();
 				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
 					//e1.printStackTrace();
 					log.error( "failed!", e1 );
 				}
 	 	       }
 			
 			else {
 					operations.pauseMusic(status); 
 					status = "paused";
 					playPause.setText("Play");
 					timer.stop();
 			}
		} //playPause
		
		/* opening file from system */
		if(e.getSource()==open) {
			if(status.equals("none")) {
				try {
					operations.openFile(window);
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					//e1.printStackTrace();
					log.error( "failed!", e1 );
				}
				display.setText(operations.filePath);
				
			}
			else if(status.equals("play")) {
				
				try {
					 status = "none";
					 operations.toBePlayed= new ArrayList<String>();
					 operations.openFile(window);
					 operations.stop();
					 display.setText(operations.filePath);
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					//e1.printStackTrace();
					log.error( "failed!", e1 );
				}
				playPause.setSelected(false);
				playPause.setText("Play");
			}
			
		}
		else if(e.getSource()== stopButton) {
			try {
				operations.stop();
				timer.stop();
				playPause.setSelected(false);
				playPause.setText("Play");
				status="none";
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
				//e1.printStackTrace();
				log.error( "failed!", e1 );
			}
		}
		else if(e.getSource()== createPlayList) {
			ArrayList<String> tempList= operations.createPlayList(window);
			DefaultListModel<String> l1 = new DefaultListModel<>();  
			for(String item:tempList)
				 l1.addElement(item);
			listOfItem= new JList<>(l1);
			listOfItem.setBounds(510, 200, 100, 200);
			playList.add(listOfItem);
			//window.add(listOfItem);
			//playList.repaint();
			//window.repaint();
			
			//add this to window
			
		}
		
		else if(e.getSource()==heading) {
			System.out.println("clicked");
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		 heading.setText("Mouse Clicked: (" +e.getX()+", "+e.getY() +")");
	     System.out.println("clicked");
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	

}

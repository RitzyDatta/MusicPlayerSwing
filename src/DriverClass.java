import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.UI.AudioPlayerUI;

public class DriverClass {

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		AudioPlayerUI obj = new AudioPlayerUI();
		Thread t1= new Thread(obj);
		t1.start();

	}

}
                                                      
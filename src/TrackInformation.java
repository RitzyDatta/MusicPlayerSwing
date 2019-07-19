

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class TrackInformation {
	File file;
	Parser parser;
	BodyContentHandler handler;
	Metadata metadata;
	FileInputStream inputstream;
	ParseContext context;
	AudioInputStream audioInputStream;
	long audioFileLength;
	public int frameSize;
	AudioFormat format;
	public float durationInSeconds;
	public float frameRate;
	public long frameCount;
	public long qqwe;
	
	public TrackInformation(String filePath) throws IOException, SAXException, TikaException {
		file= new File(filePath);
		
		parser = new AutoDetectParser();
		handler = new BodyContentHandler();
		metadata = new Metadata();
		inputstream = new FileInputStream(file);
		context = new ParseContext();
		try {
			audioInputStream = AudioSystem.getAudioInputStream(file.getAbsoluteFile());
			format = audioInputStream.getFormat();
			audioFileLength = file.length();
			frameSize = format.getFrameSize();
			frameRate = format.getFrameRate();
			frameCount = audioInputStream.getFrameLength();
			durationInSeconds = (audioFileLength / (frameSize * frameRate));
			
			qqwe= (long) (frameSize*frameRate);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		parser.parse(inputstream, handler, metadata, context);
		
		//getting the list of all meta data elements 
		String[] metadataNames = metadata.names();
		for(String name : metadataNames) {
			System.out.println(name + ": " + metadata.get(name));
			
		}
	}
	
}

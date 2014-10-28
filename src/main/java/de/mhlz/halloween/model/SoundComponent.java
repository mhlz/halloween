package de.mhlz.halloween.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.*;

public abstract class SoundComponent {

	private static final Logger logger = LoggerFactory.getLogger(SoundComponent.class);

	private class BackgroundMusicThread extends Thread {

		private File audioFile;

		private boolean playing;

		private boolean currentlyPlaying;

		public BackgroundMusicThread(File file) {
			this.audioFile = file;
			playing = true;
			currentlyPlaying = false;
		}

		public void stopPlaying() {
			playing = false;
		}

		public boolean isPlaying() {
			return currentlyPlaying;
		}

		public void run() {
			try {
				logger.info("Loading audio file {}...", audioFile);
				AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);
				AudioInputStream din = null;
				AudioFormat baseFormat = ais.getFormat();
				AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
				din = AudioSystem.getAudioInputStream(decodedFormat, ais);

				byte buffer[] = new byte[4096];
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
				SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
				line.open(decodedFormat);

				logger.info("Playing sound");
				currentlyPlaying = true;
				line.start();
				int bytesRead = 0;
				int bytesWritten = 0;
				while(bytesRead != -1 && playing) {
					bytesRead = din.read(buffer, 0, buffer.length);
					if(bytesRead != -1) {
						bytesWritten = line.write(buffer, 0, buffer.length);
					}
				}

				line.drain();
				line.stop();
				line.close();
				din.close();
				currentlyPlaying = false;
				logger.info("Sound stopped");
			} catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
				logger.error("{}", e);
			}
		}
	}

	private BackgroundMusicThread thread = null;

	public BackgroundMusicThread start(File audioFile) {
		thread = new BackgroundMusicThread(audioFile);

		thread.start();

		return thread;
	}

	public void stop() {
		if(thread != null) {
			thread.stopPlaying();
		}
	}

	public boolean isPlaying() {
		return thread != null && thread.isPlaying();
	}
}

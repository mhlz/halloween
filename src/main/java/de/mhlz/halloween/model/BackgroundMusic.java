package de.mhlz.halloween.model;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class BackgroundMusic extends SoundComponent {

	public void start() {
		super.start(new File("./bgmusic.mp3"));
	}

}

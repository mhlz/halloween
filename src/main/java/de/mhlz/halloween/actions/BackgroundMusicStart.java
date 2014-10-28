package de.mhlz.halloween.actions;

import de.mhlz.halloween.annotation.Action;
import de.mhlz.halloween.model.BackgroundMusic;
import de.mhlz.halloween.model.SoundComponent;
import de.mhlz.halloween.model.ModelAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Action(name = "bg_music_start", description = "start bg music")
public class BackgroundMusicStart extends ModelAction {

	@Autowired
	private BackgroundMusic music;

	@Override
	public String run() {
		music.start();

		return "music started";
	}

	@Override
	public String getStatus() {
		return music.isPlaying() ? "currenty running" : "not running";
	}
}

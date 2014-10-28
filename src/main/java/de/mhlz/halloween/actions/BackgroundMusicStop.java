package de.mhlz.halloween.actions;

import de.mhlz.halloween.annotation.Action;
import de.mhlz.halloween.model.BackgroundMusic;
import de.mhlz.halloween.model.SoundComponent;
import de.mhlz.halloween.model.ModelAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Action(name = "bg_music_stop", description = "stop bg music")
public class BackgroundMusicStop extends ModelAction {

	@Autowired
	private BackgroundMusic music;

	@Override
	public String run() {
		music.stop();

		return "music stopped";
	}

	@Override
	public String getStatus() {
		return music.isPlaying() ? "currenty running" : "not running";
	}
}

package de.mhlz.halloween.actions;

import de.mhlz.halloween.annotation.Action;
import de.mhlz.halloween.model.ModelAction;
import de.mhlz.halloween.model.Thunder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Action(name = "thunder_and_music", description = "thunder and continue with music")
public class ThunderAndMusicAction extends ModelAction {

	@Autowired
	private Thunder thunder;

	@Override
	public String run() {
		thunder.playThunder(true);

		return "thunder started";
	}

	@Override
	public String getStatus() {
		return "last played: " + thunder.getLastPlayedDate();
	}
}

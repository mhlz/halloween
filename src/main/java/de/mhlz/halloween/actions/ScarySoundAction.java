package de.mhlz.halloween.actions;

import de.mhlz.halloween.annotation.Action;
import de.mhlz.halloween.model.ModelAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Action(name = "scary_sound", description = "scary sound")
public class ScarySoundAction extends ModelAction {

	@Autowired
	private ScarySound sound;

	@Override
	public String run() {
        sound.start();

		return "sound started";
	}

	@Override
	public String getStatus() {
		return "lazy";
	}
}

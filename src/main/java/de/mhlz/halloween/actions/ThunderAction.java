package de.mhlz.halloween.actions;

import com.pi4j.io.gpio.*;
import de.mhlz.halloween.annotation.Action;
import de.mhlz.halloween.model.GpioAction;
import de.mhlz.halloween.model.ModelAction;
import de.mhlz.halloween.model.Thunder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Action(name = "thunder", description = "just thunder")
public class ThunderAction extends ModelAction {

	@Autowired
	private Thunder thunder;

	@Override
	public String run() {
		thunder.playThunder(false);

		return "thunder started";
	}

	@Override
	public String getStatus() {
		return "last played: " + thunder.getLastPlayedDate();
	}
}

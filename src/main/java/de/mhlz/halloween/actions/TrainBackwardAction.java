package de.mhlz.halloween.actions;

import de.mhlz.halloween.annotation.Action;
import de.mhlz.halloween.model.ModelAction;
import de.mhlz.halloween.model.Train;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Action(name = "train_backwd", description = "Train backwards")
public class TrainBackwardAction extends ModelAction {

	private final static Logger logger = LoggerFactory.getLogger(TrainBackwardAction.class);

	@Autowired
	private Train train;

	private boolean moving = false;

	@Override
	public String run() {
		Thread movement = new Thread(() -> {
			moving = true;

			logger.info("starting to move...");

			train.backwards();
			train.moveFor(5500);

			logger.info("done");
			moving = false;
		});

		movement.start();

		return "train started";
	}

	@Override
	public String getStatus() {
		return moving ? "moving backwards" : "not moving backwads";
	}
}

package de.mhlz.halloween.actions;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import de.mhlz.halloween.annotation.Action;
import de.mhlz.halloween.model.GpioAction;
import de.mhlz.halloween.model.Thunder;
import de.mhlz.halloween.model.Train;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Action(name = "zz_klingel", description = "zz jemand klingelt")
public class DoorbellTrigger extends GpioAction {

	private Logger logger = LoggerFactory.getLogger(DoorbellTrigger.class);

	protected long lastTrigger = 0;

	@Autowired
	private Thunder thunder;

	@Autowired
	private Train train;

	@Override
	public Pin getPin() {
		return RaspiPin.GPIO_07;
	}

	@Override
	public PinPullResistance getResistance() {
		return PinPullResistance.PULL_DOWN;
	}

	@Override
	public String run() {
		if(System.currentTimeMillis() - lastTrigger < 30000) {
			return "";
		}

		logger.info("TRICK OR TREAT :D");

		lastTrigger = System.currentTimeMillis();

		thunder.playThunder(true);

		logger.info("Waiting for thunder to finish (and hopefully door to open....)");

		while(thunder.isPlaying()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				logger.error("{}", e);
			}
		}

		logger.info("Thunder done. Moving train in 3 seconds...");

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			logger.error("{}", e);
		}

		train.forward();
		train.moveFor(5500);

		logger.info("Train done.");

		return "";
	}

	@Override
	public String getStatus() {
		return null;
	}
}

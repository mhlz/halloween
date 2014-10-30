package de.mhlz.halloween.model;

import com.pi4j.io.gpio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Train {
	private final static Logger logger = LoggerFactory.getLogger(Train.class);

	private final GpioController gpio;

	private final GpioPinDigitalOutput direction;
	private final GpioPinDigitalOutput move;

	public Train() {
		gpio = GpioFactory.getInstance();

		direction = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "train dir", PinState.LOW);
		move = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "train move", PinState.LOW);

		move.low();
	}

	public void forward() {
		direction.high();
	}

	public void backwards() {
		direction.low();
	}

	public void moveFor(long millis) {
		move.high();
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			logger.error("lol {}", e);
		}
		move.low();
	}
}

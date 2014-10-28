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
	private final GpioPinDigitalOutput direction2;
	private final GpioPinDigitalOutput direction3;
	private final GpioPinDigitalOutput move;
	private final GpioPinDigitalOutput move2;
	private final GpioPinDigitalOutput move3;

	public Train() {
		gpio = GpioFactory.getInstance();

		direction = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11, "train dir", PinState.LOW);
		direction2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10, "train dir", PinState.LOW);
		direction3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "train dir", PinState.LOW);
		move = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, "train move", PinState.LOW);
		move2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, "train move", PinState.LOW);
		move3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, "train move", PinState.LOW);

		move.low();
		move2.low();
		move3.low();
	}

	public void forward() {
		direction.high();
		direction2.high();
		direction3.high();
	}

	public void backwards() {
		direction.low();
		direction2.low();
		direction3.low();
	}

	public void moveFor(long millis) {
		move.high();
		move2.high();
		move3.high();
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			logger.error("lol {}", e);
		}
		move.low();
		move2.low();
		move3.low();
	}
}

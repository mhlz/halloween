package de.mhlz.halloween.model;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;

/**
 * Created by mischa on 11/10/14.
 */
public abstract class GpioAction extends ModelAction {

	protected GpioPinDigitalInput gpioInput;

	public GpioPinDigitalInput getGpioInput() {
		return gpioInput;
	}

	public void setGpioInput(GpioPinDigitalInput gpioInput) {
		this.gpioInput = gpioInput;
	}

	public abstract Pin getPin();
	public abstract PinPullResistance getResistance();
}

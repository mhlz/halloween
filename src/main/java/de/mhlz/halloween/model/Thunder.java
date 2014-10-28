package de.mhlz.halloween.model;

import com.pi4j.io.gpio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Thunder {

	private final static Logger logger = LoggerFactory.getLogger(Thunder.class);

	private final GpioController gpio;

	private final GpioPinDigitalOutput pin;
	private final GpioPinDigitalOutput pin2;
	private final GpioPinDigitalOutput pin3;

	private long lastPlayed = -1;

	@Autowired
	private ThunderSound sound;

	@Autowired
	private BackgroundMusic music;

	public Thunder() {
		gpio = GpioFactory.getInstance();

		pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "light1", PinState.LOW);
		pin2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "light2", PinState.LOW);
		pin3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "light3", PinState.LOW);
	}

	public void playThunder(boolean startMusicAgain) {
		Thread thunder = new Thread(() -> {
			logger.info("Starting thunder");

			music.stop();

			sound.start();
			lastPlayed = System.currentTimeMillis();

			int times = (int) (Math.random() * 7 + 27);
			try {
				Thread.sleep(1000);
				for(int i = 0; i < times; i++) {
					pin.high();
					pin2.high();
					pin3.high();

					Thread.sleep((int) (Math.random() * 150 + 50));

					pin.low();
					pin2.low();
					pin3.low();

					if (Math.random() < 0.3) {
						Thread.sleep((int) (Math.random() * 100 + 50));
					} else {
						Thread.sleep((int) (Math.random() * 600 + 200));
					}

				}

				while(sound.isPlaying()) {
					Thread.sleep(50);
				}

				logger.info("done");

				if(startMusicAgain) {
					music.start();
				}

			} catch(InterruptedException e) {
				logger.error("{}", e);
			}
		});

		thunder.start();
	}

	public String getLastPlayedDate() {
		if(lastPlayed == -1) {
			return "never";
		}

		Date time = new Date(lastPlayed);
		DateFormat formatter = new SimpleDateFormat();
		return formatter.format(time);
	}
}

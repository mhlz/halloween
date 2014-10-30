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

	private long lastPlayed = -1;

	@Autowired
	private ThunderSound sound;

	@Autowired
	private BackgroundMusic music;

	private boolean playing;

	public Thunder() {
		gpio = GpioFactory.getInstance();

		playing = false;

		pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "light1", PinState.LOW);
	}

	public void playThunder(boolean startMusicAgain) {
		Thread thunder = new Thread(() -> {

			playing = true;

			logger.info("Starting thunder");

			music.stop();

			sound.start();
			lastPlayed = System.currentTimeMillis();

			int times = (int) (Math.random() * 7 + 27);
			try {
				Thread.sleep(1000);
				for(int i = 0; i < times; i++) {
					pin.high();

					Thread.sleep((int) (Math.random() * 150 + 50));

					pin.low();

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

			playing = false;
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

	public boolean isPlaying() {
		return playing;
	}
}

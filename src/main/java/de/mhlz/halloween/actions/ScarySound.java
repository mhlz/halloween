package de.mhlz.halloween.actions;

import de.mhlz.halloween.model.SoundComponent;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author Mischa Holz
 */
@Component
public class ScarySound extends SoundComponent {

    public void start() {
        super.start(new File("scary_sound.mp3"));
    }

}

package de.mhlz.halloween.annotation;

import com.pi4j.io.gpio.RaspiPin;
import de.mhlz.halloween.model.ModelAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mischa on 11/10/14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Action {

	String name();
	String description() default "";

}

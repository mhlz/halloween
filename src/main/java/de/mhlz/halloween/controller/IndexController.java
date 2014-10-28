package de.mhlz.halloween.controller;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import de.mhlz.halloween.annotation.Action;
import de.mhlz.halloween.model.GpioAction;
import de.mhlz.halloween.model.ModelAction;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by mischa on 11/10/14.
 */
@Controller
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	public IndexController() {
		gpio = GpioFactory.getInstance();
	}

	protected Map<String, ModelAction> actions = null;

	protected GpioController gpio;

	@Autowired
	private ApplicationContext appContext;

	@PostConstruct
	protected void scanForActions() {
		actions = new HashMap<>();

		Reflections reflections = new Reflections("de.mhlz.halloween.actions");
		Set<Class<?>> actionClasses = reflections.getTypesAnnotatedWith(Action.class);
		for(Class<?> clazz : actionClasses) {
			if(ModelAction.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(Action.class)) {
				Action a = clazz.getAnnotation(Action.class);
				String[] possibleBeans = appContext.getBeanNamesForType(clazz);
				if(possibleBeans.length != 1) {
					logger.error("More than one possible bean for {}", clazz);
					continue;
				}

				ModelAction ma = (ModelAction) appContext.getBean(possibleBeans[0]);
				ma.setName(a.name());
				ma.setDescription(a.description());

				if(ma instanceof GpioAction) {
					final GpioAction ga = (GpioAction) ma;
					GpioPinDigitalInput button = gpio.provisionDigitalInputPin(ga.getPin(), ga.getResistance());

					button.addListener((GpioPinListenerDigital) gpioPinDigitalStateChangeEvent -> {
						logger.info("Running gpio action {}...", ga.getClass());
						ga.run();
						logger.info("Done");
					});
				}

				actions.put(a.name(), ma);
			}
		}
	}

	@RequestMapping(value = "/rescan")
	public String rescan() {
		scanForActions();
		return "redirect:/";
	}

	@RequestMapping(value = {"/index", ""})
	public String index(Model model, @RequestParam(value = "action", required = false, defaultValue = "") String actionName, @RequestParam(value = "message", required = false, defaultValue = "") String message) {
		if(actions == null) {
			scanForActions();
		}

		Collection collection = this.actions.values();
		ArrayList<ModelAction> actions = new ArrayList<>();
		actions.addAll(this.actions.values());
		Collections.sort(actions, (o1, o2) -> o1.getName().compareTo(o2.getName()));

		model.addAttribute("actions", actions);

		model.addAttribute("messageAction", actionName);
		model.addAttribute("message", message);

		return "index";
	}

	protected ModelAction getActionObject(String name) {
		return actions.get(name);
	}

	@RequestMapping("/run/{name}")
	public String execute(@PathVariable String name) {
		ModelAction ma = this.getActionObject(name);
		if(ma == null) {
			return "redirect:/";
		}

		logger.info("Running html action {}...", ma.getClass());
		String answer = ma.run();
		logger.info("Done");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return "redirect:/index?action=" + ma.getName() + "&message=" + answer;
	}
}

package de.mhlz.halloween.model;

/**
 * Created by mischa on 11/10/14.
 */
public abstract class ModelAction {

	protected String name;

	protected String description;

	public ModelAction() {
		this.name = "";
		this.description = "";
	}

	public ModelAction(String name) {
		this.name = name;
		this.description = "";
	}

	public ModelAction(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public abstract String run();

	public abstract String getStatus();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

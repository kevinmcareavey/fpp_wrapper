package fpp_wrapper.main;

import java.util.List;

public class Plan {
	
	private List<Action> actions;
	private double weight;
	
	public Plan(List<Action> actions, double weight) {
		this.setActions(actions);
		this.setWeight(weight);
	}
	
	public Plan(List<Action> actions) {
		this(actions, -1);
	}
	
	public List<Action> getActions() {
		return this.actions;
	}
	
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	
	public double getWeight() {
		return this.weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
}

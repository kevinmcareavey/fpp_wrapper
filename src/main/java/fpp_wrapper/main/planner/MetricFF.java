package fpp_wrapper.main.planner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fpp_wrapper.exception.PlannerException;
import fpp_wrapper.exception.UnsolvableException;
import fpp_wrapper.main.Action;
import fpp_wrapper.main.Location;
import fpp_wrapper.main.Plan;
import fpp_wrapper.main.Planner;
import ppddl.exception.NameException;
import ppddl.main.name.ActionSymbol;
import ppddl.main.term.Constant;

public class MetricFF extends Planner {

	public MetricFF(Location location, String path) {
		super(location, path);
	}

	@Override
	public Plan run(String domain, String problem) throws IOException, InterruptedException, PlannerException, UnsolvableException, NameException {
		Process process;
		if(this.getLocation() == Location.LOCAL) {
			String[] cmd = {this.getPath(), "-o", domain, "-f", problem, "-s", "0"};
			process = Runtime.getRuntime().exec(cmd);
		} else {
			String[] cmd = {this.getPath(), "-s", "metric-ff", "-d", domain, "-p", problem};
			process = Runtime.getRuntime().exec(cmd);
		}
		process.waitFor();
		int exitStatus = process.exitValue();
		if(exitStatus != 0) {
			throw new PlannerException("metric-ff exited with status " + exitStatus);
		}
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    
	    String line;
	    boolean skip = true;
	    List<Action> actions = new ArrayList<Action>();
	    double cost = -1;
	    while((line = bufferedReader.readLine()) != null) {
	    	if(line.contains("problem proven unsolvable") || line.contains("No plan will solve it")) {
	    		throw new UnsolvableException();
	    	}
	    	if(line.contains("time spent:")) {
	    		skip = true;
	    	}
	    	if(line.contains("plan cost:")) {
	    		cost = this.parseCost(line);
	    		skip = true;
	    	}
	    	if(!skip) {
	    		if(!line.trim().isEmpty()) {
	    			if(line.contains(":")) { // Action lines always contain a colon.
	    				actions.add(this.parseAction(line));
	    			}
	    		}
	    	}
	    	if(line.contains("found legal plan as follows")) {
	    		skip = false;
	    	}
	    }
	    return new Plan(actions, cost); // If plan is empty then goal is trivially solvable.
	}
	
	private Action parseAction(String input) throws NameException {
		String[] tokens = input.split(":");
		tokens = tokens[1].substring(1, tokens[1].length()).split("\\s+");
		ActionSymbol actionSymbol = new ActionSymbol(tokens[0].toLowerCase());
		List<Constant> arguments = new ArrayList<Constant>();
		for(int i = 1; i < tokens.length; i++) {
			arguments.add(new Constant(tokens[i].toLowerCase()));
		}
		return new Action(actionSymbol, arguments);
	}
	
	private double parseCost(String input) {
		String[] tokens = input.split(":");
		return Double.parseDouble(tokens[1].trim());
	}

}

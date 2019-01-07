package fpp_wrapper.main.planner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fpp_wrapper.exception.PlannerException;
import fpp_wrapper.main.Action;
import fpp_wrapper.main.Location;
import fpp_wrapper.main.Plan;
import fpp_wrapper.main.Planner;
import ppddl.exception.NameException;
import ppddl.main.name.ActionSymbol;
import ppddl.main.term.Constant;

public class FF extends Planner {
	
	public FF(String path) {
		super(Location.LOCAL, path);
	}
	
	@Override
	public Plan run(String domain, String problem) throws IOException, InterruptedException, PlannerException, NameException {
		String[] cmd = {this.getPath(), "-o", domain, "-f", problem};
		Process process = Runtime.getRuntime().exec(cmd);
		process.waitFor();
		int exitStatus = process.exitValue();
		if(exitStatus != 0) {
			throw new PlannerException("ff exited with status " + exitStatus);
		}
		
		String uri = problem + ".ff";
		FileReader reader = new FileReader(uri);
		BufferedReader bufferedReader = new BufferedReader(reader);
	    
	    String line;
	    boolean skip = true;
	    List<Action> actions = new ArrayList<Action>();
	    while((line = bufferedReader.readLine()) != null) {
	    	if(skip) {
	    		skip = false;
	    	} else {
	    		actions.add(this.parseAction(line));
	    	}
	    }
	    reader.close();
	    return new Plan(actions);
	}
	
	private Action parseAction(String input) throws NameException {
		String[] tokens = input.replace("(", "").replace(")", "").split("\\s+");
		ActionSymbol actionSymbol = new ActionSymbol(tokens[0].toLowerCase());
		List<Constant> arguments = new ArrayList<Constant>();
		for(int i = 1; i < tokens.length; i++) {
			arguments.add(new Constant(tokens[i].toLowerCase()));
		}
		return new Action(actionSymbol, arguments);
	}

}

package fpp_wrapper.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import fpp_wrapper.exception.PlannerException;
import fpp_wrapper.exception.UnsolvableException;
import ppddl.exception.NameException;
import ppddl.main.Domain;
import ppddl.main.Problem;

public abstract class Planner {
	
	private Location location;
	private String path;
	
	public Planner(Location location, String path) {
		this.setLocation(location);
		this.setPath(path);
	}
	
	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public List<Action> run(Domain domain, Problem problem) throws IOException, InterruptedException, PlannerException, UnsolvableException, NameException {
		BufferedWriter domainWriter = new BufferedWriter(new FileWriter("domain.pddl"));
		domainWriter.write(domain.toString());
		domainWriter.close();
		
		BufferedWriter problemWriter = new BufferedWriter(new FileWriter("problem.pddl"));
		problemWriter.write(problem.toString());
		problemWriter.close();
		
		return this.run("domain.pddl", "problem.pddl");
	}
	
	public abstract List<Action> run(String domain, String problem) throws IOException, InterruptedException, PlannerException, UnsolvableException, NameException;

	@Override
	public String toString() {
		return "(" + this.getLocation() + ", " + this.getPath() + ")";
	}

}

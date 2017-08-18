package fpp_wrapper.debug;

import fpp_wrapper.main.Planner;
import fpp_wrapper.main.planner.FF;

public class Main {
	
	public static void main(String[] args) {
		try {
			Planner planner = new FF("/Users/kevin/Downloads/Planners/FF-v2.3-big-parse-suda/ff");
			System.out.println(planner.run(ppddl.debug.Main.adlDomain(), ppddl.debug.Main.adlProblem()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

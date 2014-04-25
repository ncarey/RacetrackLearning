package edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command;

import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Command;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Shell;

/**
 * This command provides help information regarding variables in the environment.
 * 
 * @author Zachary Palmer
 */
public class VarHelpCommand extends Command
{

	@Override
	public void execute(Shell shell, String[] args) throws CommandFailureException
	{
		checkArgCount(args, 0, 0);

		final int WRAP = 25;
		shell.print("alpha                  - Controls learning rate in RL agents.", WRAP);
		shell.print("epsilon                - Controls convergence tolerance in RL agents.", WRAP);
		shell.print("gamma                  - Controls discount factor in RL agents.", WRAP);
		shell.print("minExplorations        - Encourages RL agents to explore by setting a minimum on the number of " +
				"times they will visit a given state before optimizing for expected reward.", WRAP);
		shell.print("hardCrash              - If true, crashes reset the agent to the starting line.  If false, " +
				"crashes simply set agent to position of collision with zero velocity.", WRAP);
		shell.print("verboseSimulation      - If true, each simulation time step results in a printout of the world " +
				"state.  If false, simulation is quiet.", WRAP);
		shell.print("verboseSimulationDelay - The duration between displays of the simulation state in milliseconds. " +
				"This value has no impact unless verboseSimulation is set.", WRAP);
		shell.print("progressReportDelay    - The minimum duration between progress reports provided by some shell " +
				"commands (in milliseconds).");
	}

	@Override
	public String getLongHelp(String name)
	{
		return "Usage: " + name + "\n\n" + "Displays help information for variables in the environment.";
	}

	@Override
	public String getShortHelp()
	{
		return "displays information about environment variables";
	}

}

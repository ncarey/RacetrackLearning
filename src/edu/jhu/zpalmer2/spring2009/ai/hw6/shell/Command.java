package edu.jhu.zpalmer2.spring2009.ai.hw6.shell;

import java.util.Formatter;

import edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm.StepCostRewardFunction;
import edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm.TerrainBasedTransitionFunction;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command.CommandFailureException;
import edu.jhu.zpalmer2.spring2009.ai.hw6.simulator.LoggingSimulatorListener;
import edu.jhu.zpalmer2.spring2009.ai.hw6.simulator.Simulator;

/**
 * This interface is implemented by all shell commands.
 * 
 * @author Zachary Palmer
 */
public abstract class Command
{
	/**
	 * The last time at which a progress report was issued by this command.
	 */
	private long lastProgressReport;
	
	/**
	 * General constructor.
	 */
	public Command()
	{
		this.lastProgressReport = 0;
	}
	
	/**
	 * Executes this command.
	 * 
	 * @param shell The shell in which to execute this command.
	 * @param args The string arguments which were provided.
	 * @throws CommandFailureException If the command does not execute successfully.
	 */
	public abstract void execute(Shell shell, String[] args) throws CommandFailureException;

	/**
	 * Retrieves a short help string describing this command.
	 * 
	 * @return A short help string.
	 */
	public abstract String getShortHelp();

	/**
	 * Retrieves a long help string describing this command.
	 * 
	 * @param name The name which is being used to identify this command.
	 * @return A long help string.
	 */
	public abstract String getLongHelp(String name);

	/**
	 * Ensures that the number of arguments is within the provided range.
	 * 
	 * @param args The number of arguments.
	 * @param min The minimum number of arguments.
	 * @param max The maximum number of arguments.
	 * @throws CommandFailureException If the number of arguments is outside of the prescribed range.
	 */
	protected void checkArgCount(String[] args, int min, int max)
		throws CommandFailureException
	{
		if ((args.length < min) || (args.length > max))
		{
			throw new CommandFailureException((min == max) ? ("This command expects exactly " + min + " argument" +
					(min==1?"":"s") + ".")
					: ("This command expects between " + min + " and " + max + " arguments."));
		}
	}
	
	/**
	 * Ensures that the provided shell has loaded an agent.
	 * @param shell The shell to check.
	 * @throws CommandFailureException If no agent has been loaded in the provided shell.
	 */
	protected void checkAgent(Shell shell)
		throws CommandFailureException
	{
		if (shell.getAgent()==null) throw new CommandFailureException("No agent loaded.");
	}
	
	/**
	 * Ensures that the provided shell has loaded a world map.
	 * @param shell The shell to check.
	 * @throws CommandFailureException If no map has been loaded in the provided shell.
	 */
	protected void checkWorldMap(Shell shell)
		throws CommandFailureException
	{
		if (shell.getWorld()==null) throw new CommandFailureException("No world map loaded.");
	}
	
	/**
	 * Parses the provided string as a positive integer.
	 * @param string The string to parse.
	 * @param description The description of this value to use in error messages.
	 * @return The resulting value.
	 * @throws CommandFailureException If the provided value is not a string representation of a positive integer.
	 */
	protected int parsePositiveInteger(String string, String description)
		throws CommandFailureException
	{
		int i;
		try
		{
			i = Integer.parseInt(string);
		} catch (NumberFormatException nfe)
		{
			throw new CommandFailureException("Invalid " + description + ": " + nfe.getMessage());
		}
		if (i<1)
		{
			throw new CommandFailureException("Invalid " + description + ": must be positive");
		}
		return i;
	}
	
	/**
	 * Builds a simulator using the rules of the environment.
	 * @param shell The shell in which to build the simulator.
	 */
	protected Simulator buildSimulator(Shell shell)
	{
		Simulator simulator = new Simulator(
				shell.getWorld(),
				new TerrainBasedTransitionFunction(shell.getWorld(), shell.getEnvironment().getHardCrashing()),
				new StepCostRewardFunction(shell.getWorld()));
		if (shell.getEnvironment().getVerboseSimulation())
		{
			simulator.addSimulatorListener(new LoggingSimulatorListener(
					shell.getWorld(), shell.getOutput(), shell.getEnvironment().getVerboseSimulationDelay()));
		}
		return simulator;
	}
	
	/**
	 * Generates a progress report if appropriate.  The progress report is only written if timing is correct (or if
	 * forcing is performed) and if progress reports are active (non-zero delay).
	 * @param shell The shell to which to write the report.
	 * @param force <code>true</code> to force this message to be written regardless of timing; <code>false</code>
	 * 		        otherwise.
	 * @param str The string to format for the report.
	 * @param obj The objects to format into the report string.
	 */
	protected void progressReport(Shell shell, boolean force, String str, Object... obj)
	{
		if (shell.getEnvironment().getProgressReportDelay()==0) return;
		
		if ((System.currentTimeMillis() < this.lastProgressReport + shell.getEnvironment().getProgressReportDelay()) &&
				(!force))
		{
			return;
		}
		
		Formatter formatter = new Formatter();
		formatter.format(str, obj);
		shell.print(formatter.toString());
		
		this.lastProgressReport = System.currentTimeMillis();
	}
}

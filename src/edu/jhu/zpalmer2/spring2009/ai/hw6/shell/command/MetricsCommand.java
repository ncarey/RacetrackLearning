package edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command;

import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Command;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Shell;

/**
 * This command displays human-readable metrics data to the user.
 * @author Zachary Palmer
 */
public class MetricsCommand extends Command
{
	@Override
	public void execute(Shell shell, String[] args)
		throws CommandFailureException
	{
		checkArgCount(args, 0, 0);
		checkAgent(shell);
		
		shell.print("Iterations: " + shell.getAgent().getIterationCount());
		shell.print("Time:       " + shell.getAgent().getTime() + " ms");
		shell.print("Converged?  " + (shell.getAgent().isConvergenceDetected() ? "yes" : "no"));
	}

	@Override
	public String getLongHelp(String name)
	{
		return
			"Usage: " + name + "\n\n" +
			"Displays metrics on the current agent.  Note that CPU time accuracy is dependent upon the accuracy of " +
			"the underlying OS's system timer.  For Linux and Mac, this is typically 1ms.  For Windows, this is " +
			"usually between 10ms and 15ms.\n\n" +
			"Note that the \"time\" metric refers to wall time, not CPU time.  Thus, this value becomes " +
			"uninteresting if the agent is iterated when verboseSimulation is set or any other factor separates wall " +
			"and CPU time significantly.";
			
	}

	@Override
	public String getShortHelp()
	{
		return "displays metrics on the current agent";
	}
}

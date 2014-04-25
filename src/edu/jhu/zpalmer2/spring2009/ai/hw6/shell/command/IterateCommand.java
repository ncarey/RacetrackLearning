package edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command;

import java.util.Set;

import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Command;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Shell;
import edu.jhu.zpalmer2.spring2009.ai.hw6.simulator.Simulator;
import edu.jhu.zpalmer2.spring2009.ai.hw6.simulator.SimulatorListener;

/**
 * Iterates through one step of learning for the currently-loaded agent.
 * @author Zachary Palmer
 */
public class IterateCommand extends Command
{
	@Override
	public void execute(Shell shell, String[] args)
		throws CommandFailureException
	{
		checkArgCount(args, 0, 1);
		checkWorldMap(shell);
		checkAgent(shell);

		int iterations = 1;
		if (args.length>0)
		{
			if (args[0].equals("oo"))
			{
				iterations = Integer.MAX_VALUE;
			} else
			{
				iterations = parsePositiveInteger(args[0], "iteration count");
			}
		}
		
		Simulator simulator = buildSimulator(shell);
		Set<? extends SimulatorListener> listeners = shell.getAgent().getSimulatorListeners();
		for (SimulatorListener listener : listeners)
		{
			simulator.addSimulatorListener(listener);
		}
		shell.getAgent().setSimulator(simulator);
		
		// Iterate until we run out of iterations or we converge
		for (int i=0; i<iterations; i++)
		{
			boolean convergence = shell.getAgent().iterate();
			if (convergence)
			{
				shell.print("Detected convergence at agent iteration " + shell.getAgent().getIterationCount() + ".");
				break;
			}
			
			if (iterations==Integer.MAX_VALUE)
			{
				progressReport(shell, false, "Iteration: %d complete", i+1);
			} else
			{
				progressReport(shell, i+1==iterations, "Iteration: %d/%d (%.2f%%) complete", i+1, iterations,
						(double)(i+1)/iterations*100);
			}
		}
	}

	@Override
	public String getLongHelp(String name)
	{
		return
			"Usage: " + name + " [cycles]\n\n"+
			"Iterates the loaded agent's learning algorithm.  If a number of cycles is provided, it must be a " +
			"positive integer; otherwise, one is assumed.  If the algorithm converges, the remaining iterations " +
			"are not performed.\n\n" +
			"If the number of cycles provided is \"oo\" (which looks vaguely like an infinity symbol if one squints " +
			"just so), the algorithm is run until it converges.";
	}

	@Override
	public String getShortHelp()
	{
		return "iterates the agent's learning cycle";
	}
}

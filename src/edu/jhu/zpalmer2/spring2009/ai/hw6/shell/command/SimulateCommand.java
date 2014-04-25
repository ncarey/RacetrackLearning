package edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command;

import java.util.Formatter;
import java.util.List;
import java.util.Set;

import edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm.MetricTrackingAgent;
import edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm.Policy;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Command;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Shell;
import edu.jhu.zpalmer2.spring2009.ai.hw6.simulator.SimulationStep;
import edu.jhu.zpalmer2.spring2009.ai.hw6.simulator.Simulator;
import edu.jhu.zpalmer2.spring2009.ai.hw6.simulator.SimulatorListener;

/**
 * Runs a simulation using the policy of the current agent.
 * @author Zachary Palmer
 */
public class SimulateCommand extends Command
{
	@Override
	public void execute(Shell shell, String[] args)
		throws CommandFailureException
	{
		checkArgCount(args, 0, 1);
		checkWorldMap(shell);
		checkAgent(shell);
		
		int simulationCount = 1;
		if (args.length>0)
		{
			simulationCount = parsePositiveInteger(args[0], "simulation count");
		}
		
		Simulator simulator = buildSimulator(shell);		
		double totalScore = 0.0;
		
		Formatter formatter = new Formatter();
		for (int i=0;i<simulationCount;i++)
		{
			MetricTrackingAgent agent = shell.getAgent().duplicate();
			Set<? extends SimulatorListener> listeners = agent.getSimulatorListeners();
			for (SimulatorListener listener : listeners) simulator.addSimulatorListener(listener);
			agent.setSimulator(simulator);
			Policy policy = agent.getPolicy();			
			
			List<SimulationStep> simulation = simulator.simulate(policy);
			double score = simulation.get(simulation.size()-1).getAfterScore();
			totalScore += score;
			formatter.format(" %.2f", score);
			shell.setSimulation(simulation);
			
			for (SimulatorListener listener : listeners) simulator.removeSimulatorListener(listener);
			
			if (simulationCount>1)
			{
				progressReport(shell, i+1==simulationCount, "Simulation: %d/%d (%.2f%%) complete", i+1, simulationCount,
						(double)(i+1)/simulationCount*100);
			}
		}
		
		if (simulationCount>1)
		{
			shell.print("Simulation scores:" + formatter.toString());
			shell.print("Average simulation score: " + (totalScore / simulationCount));
		} else
		{
			shell.print("Simulation score:" + formatter.toString());
		}
	}

	@Override
	public String getLongHelp(String name)
	{
		return
			"Usage: " + name + " [count]\n\n" +
			"Runs a simulation of the policy that the current agent is using.  If a count is specified, more than " +
			"one simulation is executed.";
	}

	@Override
	public String getShortHelp()
	{
		return "runs a simulation";
	}
}

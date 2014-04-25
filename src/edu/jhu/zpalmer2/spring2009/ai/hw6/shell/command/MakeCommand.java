package edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command;

import edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm.MetricTrackingAgent;
import edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm.QLearningAgent;
import edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm.ReinforcementLearningAgent;
import edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm.StepCostRewardFunction;
import edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm.TerrainBasedTransitionFunction;
import edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm.ValueIteratingAgent;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Command;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Shell;

/**
 * This command allows the creation of reinforcement learning agents from within the shell.
 * @author Zachary Palmer
 */
public class MakeCommand extends Command
{
	@Override
	public void execute(Shell shell, String[] args)
		throws CommandFailureException
	{
		checkArgCount(args, 1, 1);
		checkWorldMap(shell);
		
		String agentType = args[0];
		ReinforcementLearningAgent learningAgent;
		if (agentType.equals("vi"))
		{
			ValueIteratingAgent agent = new ValueIteratingAgent();
			agent.setWorld(shell.getWorld());
			agent.setConvergenceTolerance(shell.getEnvironment().getEpsilon());
			agent.setDiscountFactor(shell.getEnvironment().getGamma());
			agent.setRewardFunction(new StepCostRewardFunction(shell.getWorld()));
			agent.setTransitionFunction(new TerrainBasedTransitionFunction(
					shell.getWorld(), shell.getEnvironment().getHardCrashing()));
			learningAgent = agent;
		} else if (agentType.equals("q"))
		{
			QLearningAgent agent = new QLearningAgent();
			agent.setLearningFactor(shell.getEnvironment().getAlpha());
			agent.setConvergenceTolerance(shell.getEnvironment().getEpsilon());
			agent.setDiscountFactor(shell.getEnvironment().getGamma());
			agent.setMinimumExplorationCount(shell.getEnvironment().getMinExplorations());
			learningAgent = agent;
		} else
		{
			throw new CommandFailureException("Unrecognized agent type: " + agentType);
		}

		shell.setAgent(new MetricTrackingAgent(learningAgent));
}

	@Override
	public String getLongHelp(String name)
	{
		return
			"Usage: " + name + " <type>\n\n" +
			"Creates a reinforcement learning agent using the defined environment variables.  Type may be one of " +
			"\"vi\" for value-iterating or \"q\" for Q-learning.\n\n"+
			"NOTE: the agent will be configured with values from the environment WHEN IT IS CREATED.  Further " +
			"changes to environment values after the agent has been created will have no effect unless the " + name +
			" command is invoked again.";
	}

	@Override
	public String getShortHelp()
	{
		return "creates a reinforcement learning agent";
	}

}

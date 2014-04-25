package edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm;

import edu.jhu.zpalmer2.spring2009.ai.hw6.data.State;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.WorldMap;

/**
 * Represents a reward function for step cost.  Each state which is not part of the goal is worth -1; the goal is worth
 * 0.
 * @author Zachary Palmer
 */
public class StepCostRewardFunction implements RewardFunction
{
	private static final long serialVersionUID = 1L;
	
	/** The world in which to implement this reward function. */
	private WorldMap world;
	
	/**
	 * General constructor.
	 * @param world the world in which to implement this reward function.
	 */
	public StepCostRewardFunction(WorldMap world)
	{
		super();
		this.world = world;
	}

	/**
	 * Generates a reward based on whether or not the provided state is a finish position.
	 * @param state The state to examine.
	 * @return 0 if the provided state is a finish position; -1 if it is not.
	 */
	public double reward(State state)
	{
		return this.world.getFinishPositions().contains(state.getPosition()) ? 0 : -1;
	}
}

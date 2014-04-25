package edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm;

import java.io.Serializable;

import edu.jhu.zpalmer2.spring2009.ai.hw6.data.State;

/**
 * Implementers of this interface provide a means by which rewards can be assigned for certain states.
 * @author Zachary Palmer
 */
public interface RewardFunction extends Serializable
{
	/**
	 * Provides the reward for a given state.
	 * @param state The state to reward.
	 */
	public double reward(State state);
}

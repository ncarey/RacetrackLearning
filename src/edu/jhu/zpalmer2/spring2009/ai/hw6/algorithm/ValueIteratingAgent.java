package edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import edu.jhu.zpalmer2.spring2009.ai.hw6.data.Action;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.State;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.WorldMap;
import edu.jhu.zpalmer2.spring2009.ai.hw6.util.DefaultValueHashMap;
import edu.jhu.zpalmer2.spring2009.ai.hw6.util.Pair;

public class ValueIteratingAgent implements ReinforcementLearningAgent
{
	private static final long serialVersionUID = 1L;
	
	/** A mapping between states in that world and their expected values. */
	private Map<State, Double> expectedValues;
	
	/** The world in which this agent is operating. */
	private WorldMap world;	
	/** The discount factor for this agent. */
	private double discountFactor;
	/** The transition function that this agent uses. */
	private TransitionFunction transitionFunction;
	/** The reward function that this agent uses. */
	private RewardFunction rewardFunction;
	/** The convergence tolerance (epsilon). */
	private double convergenceTolerance;
	
	/**
	 * Creates a new value iterating agent.
	 * @param world The world in which the agent will learn.
	 */
	public ValueIteratingAgent()
	{
		this.expectedValues = new DefaultValueHashMap<State, Double>(0.0);
		
		this.world = null;
		this.discountFactor = 0.5;
		this.transitionFunction = null;
		this.rewardFunction = null;
		this.convergenceTolerance = 0.000000001;
	}

	@Override
	public Policy getPolicy()
	{
		return new ValuePolicy();
	}


	/**
	 * Iterate performs a single update of the estimated utilities of each
	 * state.  Return value specifies whether a termination criterion has been
	 * met.
	 */
	@Override
	public boolean iterate()
	{
		// TODO: implement value iteration; this is basically the inside of the
		// while(!done) loop.
	}

	public ValueIteratingAgent duplicate()
	{
		ValueIteratingAgent ret = new ValueIteratingAgent();
		ret.setConvergenceTolerance(this.convergenceTolerance);
		ret.setDiscountFactor(this.discountFactor);
		ret.setRewardFunction(this.rewardFunction);
		ret.setTransitionFunction(this.transitionFunction);
		ret.setWorld(this.world);
		ret.expectedValues.putAll(this.expectedValues);
		return ret;
	}
	
	public double getLearningFactor()
	{
		return discountFactor;
	}

	public void setDiscountFactor(double discountFactor)
	{
		this.discountFactor = discountFactor;
	}

	public TransitionFunction getTransitionFunction()
	{
		return transitionFunction;
	}

	public void setTransitionFunction(TransitionFunction transitionFunction)
	{
		this.transitionFunction = transitionFunction;
	}

	public RewardFunction getRewardFunction()
	{
		return rewardFunction;
	}

	public void setRewardFunction(RewardFunction rewardFunction)
	{
		this.rewardFunction = rewardFunction;
	}
	
	public WorldMap getWorld()
	{
		return world;
	}

	public void setWorld(WorldMap world)
	{
		this.world = world;
	}
	
	public double getConvergenceTolerance()
	{
		return convergenceTolerance;
	}

	public void setConvergenceTolerance(double convergenceTolerance)
	{
		this.convergenceTolerance = convergenceTolerance;
	}

	/**
	 * Represents a policy that this agent would produce.
	 */
	public class ValuePolicy implements Policy
	{
		private static final long serialVersionUID = 1L;
		
		private Random random = new Random();

		/**
		 * The action an agent decides to take from a given state 
		 */
		public Action decide(State state)
		{
			// TODO: this function should return an appropriate action based on
			// an exploration policy and the current estimate of expected
			// future reward. 
		}
	}
}

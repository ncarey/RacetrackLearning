package edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.lang.Math;

import edu.jhu.zpalmer2.spring2009.ai.hw6.data.Terrain;
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
	private Map<State, Double> expectedValuesPrime;
	
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
	/** housekeeping variable for initialization */
	private boolean isInit;

	/**
	 * Creates a new value iterating agent.
	 * @param world The world in which the agent will learn.
	 */
	public ValueIteratingAgent()
	{
		this.expectedValues = new DefaultValueHashMap<State, Double>(0.0);
		this.expectedValuesPrime = new DefaultValueHashMap<State, Double>(0.0);
		
		this.world = null;
		this.discountFactor = 0.5;
		this.transitionFunction = null;
		this.rewardFunction = null;
		this.convergenceTolerance = 0.000000001;
		this.isInit = false;
	}

	@Override
	public Policy getPolicy()
	{
		return new ValuePolicy();
	}

	private boolean initExpectedValues(){
		int x = world.getSize().getFirst().intValue();
		int y = world.getSize().getSecond().intValue();
		for(int i = 0; i < x; i++){
			for(int j = 0; j < y; j++){

				Pair<Integer, Integer> location = 
					new Pair<Integer, Integer>(new Integer(i), new Integer(j));
				Terrain curTer = world.getTerrain(location);

				//now that we have a map location, list all possible velocities to get all states
				for(int dx = -5; dx <=5; dx++){
					for(int dy = -5; dy <= 5; dy++){

						Pair<Integer, Integer> velocity = 
							new Pair<Integer, Integer>(new Integer(dx), new Integer(dy));
						State curState = new State(location, velocity);
						expectedValuesPrime.put(curState, new Double(0.0));
					}
				}
			}
		}
		return true;
	}

	/**
	 * Iterate performs a single update of the estimated utilities of each
	 * state.  Return value specifies whether a termination criterion has been
	 * met.
	 */
	@Override
	public boolean iterate()
	{
		//initialize expectedValuesPrime with all possible states
		if(isInit == false){
			this.isInit = this.initExpectedValues();
		}

		double maxChange = 0.0;
		expectedValues = expectedValuesPrime;

		//iterate through all states
		for(Map.Entry<State, Double> entry : expectedValuesPrime.entrySet()) {
			State curState = entry.getKey();
			Double reward = rewardFunction.reward(curState);
			//iterate through all possible actions, find max
			Double maxAction = Double.NEGATIVE_INFINITY;
			for(Action action : Action.LEGAL_ACTIONS) {
				//iterate through all possible states resulting from action
				Double sum = new Double(0.0);
				for(Pair<State, Double> sPrime : transitionFunction.transition(curState, action)) {
					sum += (sPrime.getSecond() * expectedValues.get(sPrime.getFirst()));
				}
				if(sum >= maxAction){
					maxAction = new Double(sum.doubleValue());
				}
			}
			//maxAction found, update expectedValuesPrime
			expectedValuesPrime.put(curState, new Double(reward + (discountFactor * maxAction)));
			
			//update maxChange
			double change = Math.abs(expectedValuesPrime.get(curState) - expectedValues.get(curState));
			if(change > maxChange){
				maxChange = change;
			}

		}


		//check convergence criterion
		if(maxChange < (convergenceTolerance * ((1 - discountFactor) / discountFactor))){
			return true;
		}else{
			return false;
		}		
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
			// this function should return an appropriate action based on
			// an exploration policy and the current estimate of expected
			// future reward. 

			//iterate through all possible actions, find max
			Double maxAction = Double.NEGATIVE_INFINITY;
			Action maxA = null;
			for(Action action : Action.LEGAL_ACTIONS) {
				//iterate through all possible states resulting from action
				Double sum = new Double(0.0);
				for(Pair<State, Double> sPrime : transitionFunction.transition(state, action)) {
					sum += (sPrime.getSecond() * expectedValues.get(sPrime.getFirst()));
				}
				if(sum >= maxAction){
					maxAction = new Double(sum.doubleValue());
					maxA = action;
				}
			}
			if(maxA == null){
				System.out.println("Action is null???");
			}
			return maxA;
		}
	}
}

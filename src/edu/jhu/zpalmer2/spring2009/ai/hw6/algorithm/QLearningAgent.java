package edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import edu.jhu.zpalmer2.spring2009.ai.hw6.data.Action;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.State;
import edu.jhu.zpalmer2.spring2009.ai.hw6.simulator.Simulator;
import edu.jhu.zpalmer2.spring2009.ai.hw6.simulator.SimulatorEvent;
import edu.jhu.zpalmer2.spring2009.ai.hw6.simulator.SimulatorListener;
import edu.jhu.zpalmer2.spring2009.ai.hw6.util.DefaultValueHashMap;
import edu.jhu.zpalmer2.spring2009.ai.hw6.util.Pair;

/**
 * A reinforcement agent which uses the Q-learning technique.
 * 
 * @author Zachary Palmer
 */
public class QLearningAgent implements SimulationBasedReinforcementLearningAgent
{
	private static final long serialVersionUID = 1L;

	/** The number of times the agent will explore a given state-action pair before giving up on it. */
	private int minimumExplorationCount;
	/** The discount factor used by this agent to allow control over how important short-term gains are considered. */
	private double discountFactor;
	/** The learning factor for this agent. */
	private double learningFactor;
	/** The convergence tolerance (epsilon). */
	private double convergenceTolerance;

	/** Tracks the maximum change in our perception of the environment during an iteration. */
	double maximumChange = 0;

	/** The record of how frequently each action has been explored from each state. */
	private Map<Pair<State, Action>, Integer> visitEvents;
	/** The expected reward for the provided state-action pair. */
	private Map<Pair<State, Action>, Double> expectedReward;

	/** The simulator which is simulating the environment in which this agent is learning. */
	private transient Simulator simulator;

	/**
	 * General constructor.
	 */
	public QLearningAgent()
	{
		this.minimumExplorationCount = 1;
		this.discountFactor = 0.99;
		this.learningFactor = 0.5;
		this.convergenceTolerance = 0.000000001;

		this.visitEvents = new DefaultValueHashMap<Pair<State, Action>, Integer>(0);
		this.expectedReward = new DefaultValueHashMap<Pair<State, Action>, Double>(0.0);
		
		this.simulator = null;
	}

	@Override
	public Policy getPolicy()
	{
		return new QPolicy();
	}
	
	/**
	 * Iterates a single learn-as-I-go simulation for this Q learning agent. A
	 * single iteration of this algorithm will walk the agent to a goal state;
	 * thus, lower order iterations are likely to take much longer.  Return
	 * value specifies whether a termination criterion has been met.
	 */
	public boolean iterate()
	{
		// TODO: this function should call the simulator to perform a sample run
	}
	
	@Override
	public Set<? extends SimulatorListener> getSimulatorListeners()
	{
		return Collections.singleton(new QLearningListener());
	}

	@Override
	public QLearningAgent duplicate()
	{
		QLearningAgent ret = new QLearningAgent();
		ret.setConvergenceTolerance(this.convergenceTolerance);
		ret.setDiscountFactor(this.discountFactor);
		ret.setLearningFactor(this.learningFactor);
		ret.setMinimumExplorationCount(this.minimumExplorationCount);
		ret.expectedReward.putAll(this.expectedReward);
		ret.visitEvents.putAll(this.visitEvents);
		return ret;
	}

	public int getMinimumExplorationCount()
	{
		return minimumExplorationCount;
	}

	public void setMinimumExplorationCount(int minimumExplorationCount)
	{
		this.minimumExplorationCount = minimumExplorationCount;
	}

	public double getDiscountFactor()
	{
		return discountFactor;
	}

	public void setDiscountFactor(double discountFactor)
	{
		this.discountFactor = discountFactor;
	}

	public double getLearningFactor()
	{
		return learningFactor;
	}

	public void setLearningFactor(double learningFactor)
	{
		this.learningFactor = learningFactor;
	}

	public double getConvergenceTolerance()
	{
		return convergenceTolerance;
	}

	public void setConvergenceTolerance(double convergenceTolerance)
	{
		this.convergenceTolerance = convergenceTolerance;
	}

	public Simulator getSimulator()
	{
		return simulator;
	}

	public void setSimulator(Simulator simulator)
	{
		this.simulator = simulator;
	}

	/**
	 * The policy used by this agent.
	 */
	class QPolicy implements Policy
	{
		private static final long serialVersionUID = 1L;
		
		/** A randomizer used to break ties. */
		private Random random = new Random();
		
		public QPolicy()
		{
			super();
		}

		@Override
		/**
		 * Returns the action the agent chooses to take for the given state.
		 */
		public Action decide(State state)
		{
			// TODO: this function should return an appropriate action based on
			// an exploration policy and the current estimate of expected
			// future reward. 
			
		}
	}

	/**
	 * The listener which learns on behalf of this agent.
	 */
	class QLearningListener implements SimulatorListener
	{
		/**
		 * Called once for every timestep of a simulation; every 
		 * time an agent takes an action, an "event" occurs.  
		 * Q-learning needs to do an update after every step, and this
		 * function is where it takes place.
		 */
		@Override
		public void simulationEventOccurred(SimulatorEvent event)
		{
			// TODO: this function will be called each time an action is taken;
			// this is where updates to e.g. the Q-function should occur
		}
	}
}

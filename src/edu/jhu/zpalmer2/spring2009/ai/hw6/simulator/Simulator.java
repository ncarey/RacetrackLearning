package edu.jhu.zpalmer2.spring2009.ai.hw6.simulator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm.Policy;
import edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm.RewardFunction;
import edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm.TransitionFunction;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.Action;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.State;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.WorldMap;
import edu.jhu.zpalmer2.spring2009.ai.hw6.util.Pair;
import edu.jhu.zpalmer2.spring2009.ai.hw6.util.ReinforcementLearningUtilities;

/**
 * This class represents a mechanism that can be used to simulate the behavior of an agent given a world and a policy.
 * @author Zachary Palmer
 */
public class Simulator
{
	/**
	 * The transition function used in the simulation.
	 */
	private TransitionFunction transitionFunction;
	/**
	 * The reward function used in the simulation.
	 */
	private RewardFunction rewardFunction;
	/**
	 * The world in which to simulate.
	 */
	private WorldMap world;
	
	/**
	 * The simulator listeners listening to events that occur in this simulator.
	 */
	private Set<SimulatorListener> listeners;
	
	/**
	 * A random number generator.
	 */
	private Random random;
	
	/**
	 * General constructor.
	 * @param world The world in which to simulate.
	 * @param transitionFunction The transition function for the world.
	 * @param rewardFunction The reward function for the world.
	 * @param output The PrintStream to which to log output.  If <code>null</code>, no output is logged.
	 */
	public Simulator(WorldMap world, TransitionFunction transitionFunction, RewardFunction rewardFunction)
	{
		super();
		this.world = world;
		this.transitionFunction = transitionFunction;
		this.rewardFunction = rewardFunction;
		this.listeners = new HashSet<SimulatorListener>();
		
		this.random = new Random();
	}
	
	/**
	 * Runs a simulation.
	 * @param policy The policy to use.
	 * @return The simulation steps that occurred.
	 */
	public List<SimulationStep> simulate(Policy policy)
	{
		State state;
		double score = 0;
		
		// Select a random starting state.
		state = ReinforcementLearningUtilities.getRandomStartingState(this.world);
		
		// Set up history
		List<SimulationStep> history = new ArrayList<SimulationStep>();
		
		// Simulate until we reach a finish state
		while (!(world.getFinishPositions().contains(state.getPosition())))
		{
			Action action = policy.decide(state);
			Pair<State,Double> outcome = executeTransition(state, action);
			State newState = outcome.getFirst();
			double newScore = score + outcome.getSecond();
			
			SimulationStep step = new SimulationStep(state, action, newState, score, newScore);
			history.add(step);
			fireSimulatorEvent(new SimulatorEvent(this, step));
			
			score = newScore;
			state = newState;
		}
		
		SimulationStep step = new SimulationStep(state, null, null, score, score);
		history.add(step);
		fireSimulatorEvent(new SimulatorEvent(this, step));
		
		return history;
	}
	
	/**
	 * Performs a transition using the transition function in this simulator.
	 * @param state The current state.
	 * @param action The action being taken in that state.
	 * @return A pairing between the resulting state and the reward that was incurred for entering that state.
	 */
	private Pair<State,Double> executeTransition(State state, Action action)
	{
		Set<Pair<State,Double>> outcomes = this.transitionFunction.transition(state, action);
		double prob = random.nextDouble();
		State resultingState = null;
		for (Pair<State,Double> outcome : outcomes)
		{
			if (prob < outcome.getSecond())
			{
				resultingState = outcome.getFirst();
				break;
			} else
			{
				prob -= outcome.getSecond();
			}
		}
		if (resultingState==null)
		{
			throw new IllegalStateException("Malformed probability model provided by transition function!");
		}
		return new Pair<State,Double>(resultingState, this.rewardFunction.reward(resultingState));
	}
	
	/**
	 * Adds a listener.
	 * @param listener The listener to add.
	 */
	public void addSimulatorListener(SimulatorListener listener)
	{
		this.listeners.add(listener);
	}
	
	/**
	 * Removes a listener.
	 * @param listener The listener to remove.
	 */
	public void removeSimulatorListener(SimulatorListener listener)
	{
		this.listeners.remove(listener);
	}
	
	/**
	 * Reports an event to all listeners.
	 * @param event The event to report.
	 */
	protected void fireSimulatorEvent(SimulatorEvent event)
	{
		for (SimulatorListener listener : this.listeners)
		{
			listener.simulationEventOccurred(event);
		}
	}	
}

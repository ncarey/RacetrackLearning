package edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.jhu.zpalmer2.spring2009.ai.hw6.data.Action;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.State;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.Terrain;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.WorldMap;
import edu.jhu.zpalmer2.spring2009.ai.hw6.util.Pair;
import edu.jhu.zpalmer2.spring2009.ai.hw6.util.ReinforcementLearningUtilities;

/**
 * This transition function moves the agent in accordance with the rules of the worlds set forth in the assignment
 * specification.
 * @author Zachary Palmer
 */
public class TerrainBasedTransitionFunction implements TransitionFunction
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * The world in which to perform transition.
	 */
	private WorldMap world;
	/**
	 * Whether or not to implement hard crashing.  A hard crash resets the agent to the starting line; a soft crash
	 * resets the agent to the location where it hit the wall with no velocity.
	 */
	private boolean hardCrash;
	
	/**
	 * A memoized field for hard crash outcomes.
	 */
	private Set<Pair<State,Double>> outcomes;

	/**
	 * General constructor.
	 * @param world The world in which to perform transition.
	 * @param hardCrash <code>true</code> if hard crashing is implemented; <code>false</code> if it is not.
	 */
	public TerrainBasedTransitionFunction(WorldMap world, boolean hardCrash)
	{
		super();
		this.world = world;
		this.hardCrash = hardCrash;
		this.outcomes = null;
	}

	@Override
	public Set<Pair<State, Double>> transition(State state, Action action)
	{
		double slipProb = 0;
		switch (this.world.getTerrain(state.getPosition()))
		{
			case WALL:
				return hardCrashOutcomes();
			case GROUND:
				slipProb = 0.1;
				break;
			case ROUGH:
				slipProb = 0.6;
				break;
		}
		
		// Calculate for successful acceleration
		Pair<Integer,Integer> newVelocity = ReinforcementLearningUtilities.pairPlusMaxAbs(
				state.getVelocity(), action.getAcceleration(), 5);
		Pair<Integer,Integer> newPosition = ReinforcementLearningUtilities.pairPlus(
				state.getPosition(), newVelocity);
		State noSlipState = new State(newPosition, newVelocity);
		State slipState = new State(
				ReinforcementLearningUtilities.pairPlus(state.getPosition(), state.getVelocity()),
				state.getVelocity());
		
		// Reprocess for crash
		Set<Pair<State,Double>> ret = new HashSet<Pair<State,Double>>();
		for (Pair<State,Double> result : crashFilter(noSlipState,state))
		{
			ret.add(new Pair<State,Double>(result.getFirst(), result.getSecond() * (1-slipProb)));
		}
		for (Pair<State,Double> result : crashFilter(slipState,state))
		{
			ret.add(new Pair<State,Double>(result.getFirst(), result.getSecond() * slipProb));
		}
		
		return ret;
	}
	
	/**
	 * Filters states for crashing.
	 * @param newState The state to consider.
	 * @param oldState The state from which we came.
	 * @return The resulting states and their respective probabilities.
	 */
	private Collection<Pair<State,Double>> crashFilter(State newState, State oldState)
	{
		// First, see if a crash occurred.
		int increments = Math.max(
				Math.abs(newState.getVelocity().getFirst()),
				Math.abs(newState.getVelocity().getSecond()));
		
		if (increments==0)
		{
			// Special case - we're not moving.  (i/increments==NaN)  Only crash hard if we're already in a wall.
			if (this.world.getTerrain(newState.getPosition())==Terrain.WALL)
			{
				return hardCrashOutcomes();
			} else
			{
				return Collections.singleton(new Pair<State,Double>(newState, 1.0));
			}
		}
		
		Pair<Integer,Integer> crashPosition = null; // the position the agent would be in right before it hit the wall
		for (int i=0;i<=increments;i++)
		{
			double factor = (double)i/increments;
			Pair<Integer,Integer> proposedPosition = new Pair<Integer,Integer>(
					(int)(Math.round(
							oldState.getPosition().getFirst() + factor * newState.getVelocity().getFirst())),
					(int)(Math.round(
							oldState.getPosition().getSecond() + factor * newState.getVelocity().getSecond())));
			if (this.world.getTerrain(proposedPosition)==Terrain.WALL)
			{
				break;
			} else
			{
				crashPosition = proposedPosition;
			}
		}
		
		// If we never set crashPosition, the agent must have been in the wall to start with.
		if (crashPosition==null)
		{
			return hardCrashOutcomes();
		}
		
		// If the crash position is the position of the new state, no crash occurred.
		if (crashPosition.equals(newState.getPosition()))
		{
			return Collections.singleton(new Pair<State,Double>(newState,1.0));
		}
		
		// At this point, we have a crash.
		if (this.hardCrash)
		{
			// Hard crash - reset agent
			return hardCrashOutcomes();
		} else
		{
			// Soft crash - stop agent at the crash position
			return Collections.singleton(new Pair<State,Double>(
					new State(crashPosition, new Pair<Integer,Integer>(0,0)),
					1.0));
		}
	}
	
	/**
	 * Obtains the outcomes for a hard crash.
	 */
	private Set<Pair<State,Double>> hardCrashOutcomes()
	{
		if (this.outcomes==null)
		{
			this.outcomes = new HashSet<Pair<State,Double>>();
			for (Pair<Integer,Integer> position : this.world.getStartPositions())
			{
				this.outcomes.add(new Pair<State,Double>(
						new State(position, new Pair<Integer,Integer>(0,0)),
						1.0/this.world.getStartPositions().size()));
			}
		}
		return this.outcomes;
	}
}

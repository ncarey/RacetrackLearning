package edu.jhu.zpalmer2.spring2009.ai.hw6.simulator;

import java.io.Serializable;

import edu.jhu.zpalmer2.spring2009.ai.hw6.data.Action;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.State;

/**
 * Contains the state of the simulator at a given time step.
 * @author Zachary Palmer
 */
public class SimulationStep implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * The state the agent was in.
	 */
	private State state;
	/**
	 * The decision the agent made at this step.  If this is the last simulation step, this action will be
	 * <code>null</code>.
	 */
	private Action action;
	/**
	 * The state the agent reached as a result of the action.  If action is <code>null</code>, this state will also be
	 * <code>null</code>.
	 */
	private State resultState;
	/**
	 * The total score for the agent before taking the described action.
	 */
	private double beforeScore;
	/**
	 * The total score for the agent after taking the described action.
	 */
	private double afterScore;
	
	public SimulationStep(State state, Action action, State resultState, double beforeScore, double afterScore)
	{
		super();
		this.state = state;
		this.action = action;
		this.resultState = resultState;
		this.beforeScore = beforeScore;
		this.afterScore = afterScore;
	}

	public State getState()
	{
		return state;
	}

	public Action getAction()
	{
		return action;
	}

	public State getResultState()
	{
		return resultState;
	}

	public double getBeforeScore()
	{
		return beforeScore;
	}

	public double getAfterScore()
	{
		return afterScore;
	}
}

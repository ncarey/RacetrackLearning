package edu.jhu.zpalmer2.spring2009.ai.hw6.data;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.jhu.zpalmer2.spring2009.ai.hw6.util.Pair;

/**
 * This class represents actions which can be taken by the reinforcement learning agent.
 * @author Zachary Palmer
 */
public class Action implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * The set of actions which are legal for the reinforcement learning agent to take.
	 */
	public static final Set<Action> LEGAL_ACTIONS;
	
	static
	{
		Set<Action> actions = new HashSet<Action>();
		for (int ddx=-1;ddx<=1;ddx++)
		{
			for (int ddy=-1;ddy<=1;ddy++)
			{
				actions.add(new Action(new Pair<Integer,Integer>(ddx,ddy)));
			}
		}
		LEGAL_ACTIONS = Collections.unmodifiableSet(actions);
	}
	
	/**
	 * The acceleration to apply during the upcoming round.
	 */
	private Pair<Integer,Integer> acceleration;

	public Action(Pair<Integer, Integer> acceleration)
	{
		super();
		this.acceleration = acceleration;
	}

	public Pair<Integer, Integer> getAcceleration()
	{
		return acceleration;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acceleration == null) ? 0 : acceleration.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Action other = (Action) obj;
		if (acceleration == null)
		{
			if (other.acceleration != null)
				return false;
		} else if (!acceleration.equals(other.acceleration))
			return false;
		return true;
	}

	public String toString()
	{
		return "[" + acceleration.toString() + "]";
	}
}

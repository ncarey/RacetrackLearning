package edu.jhu.zpalmer2.spring2009.ai.hw6.data;

import java.io.Serializable;

import edu.jhu.zpalmer2.spring2009.ai.hw6.util.Pair;

/**
 * A class representing the states in which an agent can exist in the world.
 */
public class State implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** The position of the agent in the world. */
	private Pair<Integer,Integer> position;
	/** The velocity of the agent in the world. */
	private Pair<Integer,Integer> velocity;
	
	/**
	 * General constructor.
	 * @param position The position for this state.
	 * @param velocity The velocity for this state.
	 */
	public State(Pair<Integer, Integer> position, Pair<Integer, Integer> velocity)
	{
		super();
		this.position = position;
		this.velocity = velocity;
	}

	public Pair<Integer, Integer> getPosition()
	{
		return position;
	}

	public Pair<Integer, Integer> getVelocity()
	{
		return velocity;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((velocity == null) ? 0 : velocity.hashCode());
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
		State other = (State) obj;
		if (position == null)
		{
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (velocity == null)
		{
			if (other.velocity != null)
				return false;
		} else if (!velocity.equals(other.velocity))
			return false;
		return true;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[p=");
		sb.append(this.position);
		sb.append(";v=");
		sb.append(this.velocity);
		sb.append("]");
		return sb.toString();
	}
}

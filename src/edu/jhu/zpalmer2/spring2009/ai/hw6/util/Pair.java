package edu.jhu.zpalmer2.spring2009.ai.hw6.util;

import java.io.Serializable;

/**
 * Represents a pairing between two objects.
 * @author Zachary Palmer
 *
 * @param <T> The type of the first element in the pair.
 * @param <U> The type of the second element in the pair.
 */
public class Pair<T,U> implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** The first value in the pair. */
	private T first;
	/** The second value in the pair. */
	private U second;
	
	public Pair(T first, U second)
	{
		super();
		this.first = first;
		this.second = second;
	}

	public T getFirst()
	{
		return first;
	}

	public U getSecond()
	{
		return second;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (first == null)
		{
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null)
		{
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		sb.append(this.first);
		sb.append(",");
		sb.append(this.second);
		sb.append(")");
		return sb.toString();
	}
}

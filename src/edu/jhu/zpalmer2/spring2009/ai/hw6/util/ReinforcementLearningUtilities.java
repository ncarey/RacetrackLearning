package edu.jhu.zpalmer2.spring2009.ai.hw6.util;

import java.util.Iterator;
import java.util.Random;

import edu.jhu.zpalmer2.spring2009.ai.hw6.data.State;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.WorldMap;
import edu.jhu.zpalmer2.spring2009.ai.hw6.simulator.SimulationStep;

/**
 * This class serves as a catch-all for static functionality.
 * @author Zachary Palmer
 */
public class ReinforcementLearningUtilities
{
	/**
	 * A random number generator.
	 */
	private static Random random = new Random();
	
	/**
	 * Generates a random starting state for the provided world.  This function is not included in the WorldMap class
	 * to prevent it from being coupled with the concept of a state.
	 * @param world The world for which a random starting state is desired.
	 * @return A random starting state for that world.
	 */
	public static State getRandomStartingState(WorldMap world)
	{
		Iterator<Pair<Integer,Integer>> iterator = world.getStartPositions().iterator();
		for (int i=0;i<random.nextInt(world.getStartPositions().size());i++) iterator.next();
		return new State(iterator.next(), new Pair<Integer,Integer>(0,0));
	}
	
	/**
	 * Retrieves a string which represents a full simulation display.  It includes a display of the world map, state
	 * information, and an indicator on the map of the agent's location.
	 * @param worldMap The map to use.
	 * @param step The simulation step containing the information to render.
	 * @return The resulting string.
	 */
	public static String renderSimulationStep(WorldMap worldMap, SimulationStep step)
	{
		final int MIN_ROWS = 5;
		String[] buffer = worldMap.toString().split("\n");
		if (worldMap.getSize().getSecond()<MIN_ROWS)
		{
			StringBuffer sb = new StringBuffer();
			for (int i=0;i<worldMap.getSize().getFirst();i++)
			{
				sb.append(" ");
			}
			String spaces = sb.toString();
			for (int i=worldMap.getSize().getSecond(); i<MIN_ROWS; i++)
			{
				buffer[i] = spaces;
			}
		}
		buffer[1] += "        Position: " + step.getState().getPosition();
		buffer[2] += "        Velocity: " + step.getState().getVelocity();
		if (step.getAction()!=null) buffer[3] += "        Accel:    " + step.getAction().getAcceleration();
		buffer[4] += "        Score:    " + step.getBeforeScore();
		
		int x = step.getState().getPosition().getFirst();
		int y = step.getState().getPosition().getSecond();
		buffer[y] = buffer[y].substring(0,x) + "@" + buffer[y].substring(x+1);
		
		StringBuffer sb = new StringBuffer();
		for (String s : buffer)
		{
			sb.append(s);
			sb.append('\n');
		}
		
		return sb.toString();
	}
	
	/**
	 * Adds the values of two integer pairs.
	 * @param a The first pair.
	 * @param b The second pair.
	 * @return The resulting pair.
	 */
	public static Pair<Integer,Integer> pairPlus(Pair<Integer,Integer> a,Pair<Integer,Integer> b)
	{
		return new Pair<Integer,Integer>(a.getFirst()+b.getFirst(), a.getSecond()+b.getSecond());
	}
	
	/**
	 * Creates a pair for which each component is the integer sum of the other two pairs bounded by an absolute value
	 * maximum.
	 * @param a The first pair.
	 * @param b The second pair.
	 * @param m The maximum.
	 * @return The resulting pair.
	 */
	public static Pair<Integer,Integer> pairPlusMaxAbs(Pair<Integer,Integer> a,Pair<Integer,Integer> b, int m)
	{
		return new Pair<Integer,Integer>(
				Math.min(
						Math.max(
								a.getFirst()+b.getFirst(),
								-m),
						m),
				Math.min(
						Math.max(
								a.getSecond()+b.getSecond(),
								-m),
						m)
			);
	}
}

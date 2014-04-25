package edu.jhu.zpalmer2.spring2009.ai.hw6.data;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.jhu.zpalmer2.spring2009.ai.hw6.util.Pair;

/**
 * A class for representing the contents of the world's map.
 * @author Zachary Palmer
 */
public class WorldMap implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * The size of the world map.
	 */
	private Pair<Integer,Integer> size;
	/**
	 * The data in the world map.
	 */
	private Terrain[] data;
	
	/**
	 * The positions in the world which represent start locations.
	 */
	private Set<Pair<Integer,Integer>> startPositions;
	/**
	 * The positions in the world which represent finish locations.
	 */
	private Set<Pair<Integer,Integer>> finishPositions;
	
	/**
	 * A memoized copy of the output of toString.
	 */
	private String toStringOutput;
	
	/**
	 * General constructor.
	 * @param size The size of the world map.
	 * @param data The data the world map contains.
	 */
	public WorldMap(Pair<Integer, Integer> size, Terrain[] data, Set<Pair<Integer, Integer>> startPositions,
			Set<Pair<Integer, Integer>> finishPositions)
	{
		super();
		this.size = size;
		this.data = Arrays.copyOf(data,data.length);
		this.startPositions = new HashSet<Pair<Integer,Integer>>(startPositions);
		this.finishPositions = new HashSet<Pair<Integer,Integer>>(finishPositions);
		this.toStringOutput = null;
	}	
	
	/**
	 * Retrieves the size of this world map.
	 * @return The size of this world map.
	 */
	public Pair<Integer, Integer> getSize()
	{
		return size;
	}
	
	/**
	 * Retrieves the terrain in the provided position.  Any terrain outside of the physical boundaries of the map is
	 * assumed to be wall.
	 * @param position The position to check.
	 * @return The terrain at that logical position.
	 */
	public Terrain getTerrain(Pair<Integer,Integer> position)
	{
		if (position.getFirst()>=this.size.getFirst() || position.getFirst() < 0 ||
				position.getSecond()>=this.size.getSecond() || position.getSecond() < 0)
		{
			return Terrain.WALL;
		}
		return data[position.getFirst() + position.getSecond() * this.size.getFirst()];
	}

	public Set<Pair<Integer, Integer>> getStartPositions()
	{
		return Collections.unmodifiableSet(startPositions);
	}

	public Set<Pair<Integer, Integer>> getFinishPositions()
	{
		return Collections.unmodifiableSet(finishPositions);
	}
	
	public String toString()
	{
		if (toStringOutput==null)
		{
			StringBuffer sb = new StringBuffer();
			for (int y=0;y<this.size.getSecond();y++)
			{
				for (int x=0;x<this.size.getFirst();x++)
				{
					char c;
					Pair<Integer,Integer> p = new Pair<Integer,Integer>(x,y);
					if (this.startPositions.contains(p))
					{
						c = 'S';
					} else if (this.finishPositions.contains(p))
					{
						c = 'F';
					} else
					{
						Terrain t = getTerrain(p);
						switch (t)
						{
							case WALL:
								c='#';
								break;
							case ROUGH:
								c=',';
								break;
							case GROUND:
								c='.';
								break;
							default:
								throw new IllegalStateException("Unrecognized terrain type " + t);
						}
					}
					sb.append(c);
				}
				sb.append('\n');
			}
			this.toStringOutput = sb.toString();
		}
		return this.toStringOutput;
	}
	
	/**
	 * Generates a world map based on a provided input file.
	 * @param input The input stream from which to read the map.
	 * @return The world map that was generated.
	 * @throws IOException If an I/O error occurs.
	 * @throws EOFException If an unexpected end of file occurred.
	 * @throws IllegalArgumentException If the stream's data is incorrectly formatted.
	 */
	public static WorldMap readFromStream(InputStream input)
		throws IOException, IllegalArgumentException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(input));
		String format = br.readLine();
		if (format==null)
		{
			throw new EOFException("No format line.");
		}
		
		String[] components = format.trim().split(",");
		if (components.length!=2) throw new IllegalArgumentException("Badly formatted format line.");
		
		Pair<Integer,Integer> size;
		try
		{
			// File has rows first, then columns
			size = new Pair<Integer,Integer>(Integer.parseInt(components[1]),Integer.parseInt(components[0]));
		} catch (NumberFormatException nfe)
		{
			throw new IllegalArgumentException("Invalid number in format line.");
		}
		if ((size.getFirst()<0) || (size.getSecond()<0))
		{
			throw new IllegalArgumentException("Negative number in format line.");
		}
		
		Set<Pair<Integer,Integer>> starts = new HashSet<Pair<Integer,Integer>>();
		Set<Pair<Integer,Integer>> finishes = new HashSet<Pair<Integer,Integer>>();
		Terrain[] data = new Terrain[size.getFirst()*size.getSecond()];
		for (int i=0;i<size.getSecond();i++)
		{
			String dataStr = br.readLine();
			if (dataStr==null) throw new EOFException("Missing data.");
			dataStr = dataStr.trim();
			if (dataStr.length()!=size.getFirst()) throw new IllegalArgumentException("Incorrect length on line "+i);
			
			for (int j=0;j<dataStr.length();j++)
			{
				Terrain terrain;
				switch (dataStr.charAt(j))
				{
					case '#':
						terrain = Terrain.WALL;
						break;
					case '.':
						terrain = Terrain.GROUND;
						break;
					case ',':
						terrain = Terrain.ROUGH;
						break;
					case 'S':
						terrain = Terrain.GROUND;
						starts.add(new Pair<Integer,Integer>(j,i));
						break;
					case 'F':
						terrain = Terrain.GROUND;
						finishes.add(new Pair<Integer,Integer>(j,i));
						break;
					default:
						throw new IllegalArgumentException("Unrecognized terrain character " + (dataStr.charAt(j)));
				}
				data[j+i*size.getFirst()] = terrain;
			}
		}
		
		if (starts.size()==0)
		{
			throw new IllegalArgumentException("At least one start position is required.");
		}
		if (finishes.size()==0)
		{
			throw new IllegalArgumentException("At least one finish position is required.");
		}
		
		return new WorldMap(size, data, starts, finishes);
	}
}

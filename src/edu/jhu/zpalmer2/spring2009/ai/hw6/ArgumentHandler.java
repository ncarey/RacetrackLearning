package edu.jhu.zpalmer2.spring2009.ai.hw6;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Handles parsing command-line arguments for the reinforcement learning software.
 * 
 * @author Zachary Palmer
 */
public class ArgumentHandler
{
	private Map<String, Integer> intParamMap;
	private Map<String, Double> doubleParamMap;
	private Map<String, String> stringParamMap;
	private Map<String, Boolean> booleanParamMap;

	private Set<String> intParams;
	private Set<String> doubleParams;
	private Set<String> stringParams;
	private Set<String> booleanParams;

	/**
	 * Creates an argument handler.
	 * @param intParams The integer parameters to parse.
	 * @param doubleParams The double parameters to parse.
	 * @param stringParams The string parameters to parse.
	 * @param booleanParams The boolean parameters to parse.
	 */
	public ArgumentHandler(Collection<String> intParams, Collection<String> doubleParams,
			Collection<String> stringParams, Collection<String> booleanParams)
	{
		this.intParamMap = new HashMap<String, Integer>();
		this.doubleParamMap = new HashMap<String, Double>();
		this.stringParamMap = new HashMap<String, String>();
		this.booleanParamMap = new HashMap<String, Boolean>();

		this.intParams = new HashSet<String>(intParams);
		this.doubleParams = new HashSet<String>(doubleParams);
		this.stringParams = new HashSet<String>(stringParams);
		this.booleanParams = new HashSet<String>(booleanParams);
	}

	/**
	 * Parses the provided arguments.
	 * 
	 * @param args The arguments to use.
	 * @throws IllegalArgumentException When the arguments are poorly formatted. If this is the case, the exception's
	 *             message is human-readable and describes the problem.
	 */
	public void parse(String[] args) throws IllegalArgumentException
	{
		int idx = 0;
		try
		{
			while ((idx < args.length) && (args[idx].startsWith("-")))
			{
				String arg = args[idx++];
				String key = arg.substring(1);
				if (("--help".equals(arg)) || ("-h".equals(arg)))
				{
					throw new IllegalArgumentException((String) null);
				} else if (intParams.contains(key))
				{
					int val;
					try
					{
						val = Integer.parseInt(args[idx++]);
					} catch (NumberFormatException nfe)
					{
						throw new IllegalArgumentException("Invalid integer for parameter " + arg);
					}
					putIntParam(key, val, "You cannot specify " + arg + " twice.");
				} else if (doubleParams.contains(key))
				{
					double val;
					try
					{
						val = Double.parseDouble(args[idx++]);
					} catch (NumberFormatException nfe)
					{
						throw new IllegalArgumentException("Invalid double for parameter " + arg);
					}
					putDoubleParam(key, val, "You cannot specify " + arg + " twice.");
				} else if (stringParams.contains(key))
				{
					String val = args[idx++];
					putStringParam(key, val, "You cannot specify " + arg + " twice.");
				} else if (booleanParams.contains(key))
				{
					putBooleanParam(key, true, "You cannot specify " + arg + " twice.");
				} else
				{
					throw new IllegalArgumentException("Unrecognized option: " + arg);
				}
			}
		} catch (IndexOutOfBoundsException oobe)
		{
			throw new IllegalArgumentException("Missing argument.");
		}
		if (idx != args.length)
		{
			throw new IllegalArgumentException("Too many arguments starting with " + args[idx]);
		}
	}

	private <T> T insistParam(String key, String message, Map<String, T> map)
	{
		T t = map.get(key);
		if (t == null)
		{
			throw new IllegalArgumentException(message);
		}
		return t;
	}

	private <T> T defaultParam(String key, T def, Map<String, T> map)
	{
		T t = map.get(key);
		return t == null ? def : t;
	}

	private <T> void putParam(String key, T t, String message, Map<String, T> map)
	{
		if (map.put(key, t) != null)
		{
			throw new IllegalArgumentException(message);
		}
	}

	public int insistIntParam(String key, String message)
	{
		return insistParam(key, message, intParamMap);
	}

	public void insistIntRange(String key, int min, int max)
	{
		int v = defaultIntParam(key, min);
		if ((v < min) || (v > max))
		{
			throw new IllegalArgumentException("Range of parameter -" + key + " must be within [" + min + "," + max
					+ "]");
		}
	}

	public int defaultIntParam(String key, Integer def)
	{
		return defaultParam(key, def, intParamMap);
	}

	private void putIntParam(String key, Integer i, String message)
	{
		putParam(key, i, message, intParamMap);
	}

	public double insistDoubleParam(String key, String message)
	{
		return insistParam(key, message, doubleParamMap);
	}

	public void insistDoubleRange(String key, double min, double max)
	{
		double v = defaultDoubleParam(key, min);
		if ((v < min) || (v > max))
		{
			throw new IllegalArgumentException("Range of parameter -" + key + " must be within [" + min + "," + max
					+ "]");
		}
	}

	public double defaultDoubleParam(String key, Double def)
	{
		return defaultParam(key, def, doubleParamMap);
	}

	private void putDoubleParam(String key, Double d, String message)
	{
		putParam(key, d, message, doubleParamMap);
	}

	public String insistStringParam(String key, String message)
	{
		return insistParam(key, message, stringParamMap);
	}

	public String defaultStringParam(String key, String def)
	{
		return defaultParam(key, def, stringParamMap);
	}

	private void putStringParam(String key, String s, String message)
	{
		putParam(key, s, message, stringParamMap);
	}

	public boolean insistBooleanParam(String key, String message)
	{
		return insistParam(key, message, booleanParamMap);
	}

	public boolean defaultBooleanParam(String key)
	{
		return defaultParam(key, false, booleanParamMap);
	}

	private void putBooleanParam(String key, Boolean b, String message)
	{
		putParam(key, b, message, booleanParamMap);
	}

	@SuppressWarnings("unchecked")
	public boolean hasArgs(String... keys)
	{
		for (String key : keys)
		{
			for (Map<String, ? extends Object> map : Arrays.asList(intParamMap, doubleParamMap, stringParamMap,
					booleanParamMap))
			{
				if (map.containsKey(key))
					return true;
			}
		}
		return false;
	}
}

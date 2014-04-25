package edu.jhu.zpalmer2.spring2009.ai.hw6.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm.MetricTrackingAgent;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.WorldMap;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command.CommandFailureException;
import edu.jhu.zpalmer2.spring2009.ai.hw6.simulator.SimulationStep;
import edu.jhu.zpalmer2.spring2009.ai.hw6.util.Pair;

/**
 * This class represents the Reinforcement Learning Agent Shell (RLASH) which allows a user to issue commands to the
 * system.
 * @author Zachary Palmer
 */
public class Shell
{
	/**
	 * The prompt string.
	 */
	private static final String PROMPT = "RLASH:> ";
	
	/**
	 * The source for shell input.
	 */
	private BufferedReader input;
	/**
	 * The target for shell output.
	 */
	private PrintStream output;
	/**
	 * The width of the console.
	 */
	private int outputWidth;

	/**
	 * The commands which are registered in this shell.
	 */
	private Map<String,Command> commandMap;
	/**
	 * A set of the registrations this shell has seen.
	 */
	private Set<Pair<Command,List<String>>> registrationSet;
	/**
	 * Whether or not the agent should terminate.
	 */
	private boolean terminate;
	
	/**
	 * The shell's variable environment.
	 */
	private Environment environment;
	/**
	 * The world map loaded into the shell.
	 */
	private WorldMap world;
	/**
	 * The agent currently loaded into the shell.
	 */
	private MetricTrackingAgent agent;
	/**
	 * The log of the most recently executed simulation.
	 */
	private List<SimulationStep> simulation;
	
	/**
	 * Creates a shell.
	 * @param input The source of the shell's input.
	 * @param output The target for the shell's output.
	 * @param outputWidth The maximum width of output to the shell.
	 */
	public Shell(BufferedReader input, PrintStream output, int outputWidth)
	{
		super();
		this.input = input;
		this.output = output;
		this.outputWidth = outputWidth;
		
		this.commandMap = new HashMap<String,Command>();
		this.registrationSet = new HashSet<Pair<Command,List<String>>>();
		this.terminate = false;
		
		this.environment = new Environment();
		this.world = null;
		this.agent = null;
		this.simulation = null;
	}
	
	/**
	 * Prints the provided message to the output of this shell, wrapping at spaces as appropriate.
	 * @param s The string to print.
	 */
	public void print(String s)
	{
		print(s,0);
	}
	
	/**
	 * Prints the provided message to the output of this shell, wrapping at spaces as appropriate.
	 * @param s The string to print.
	 * @param wrap The index at which to wrap all lines except the first.
	 */
	public void print(String s, int wrap)
	{
		if (s.equals(""))
		{
			this.output.println();
			return;
		}
		
		StringBuffer sb = new StringBuffer("");
		while (sb.length()<wrap) sb.append(" ");
		s = s.replaceAll("\t","    ");
		
		while (s.length()>this.outputWidth)
		{			
			String toPrint;
			
			int newlineIndex = s.indexOf('\n');
			if ((newlineIndex>=0) && (newlineIndex<=this.outputWidth))
			{
				toPrint = s.substring(0,newlineIndex);
				s = s.substring(newlineIndex+1);
			} else
			{
	
				int spaceIndex = s.lastIndexOf(' ', this.outputWidth);
				if (spaceIndex==-1)
				{
					spaceIndex = s.indexOf(' ');
				}
				if (spaceIndex == -1)
				{
					toPrint = s;
					s = "";
				} else
				{
					toPrint = s.substring(0, spaceIndex);
					s = s.substring(spaceIndex + 1);
				}
			}
			
			this.output.println(toPrint);
			while (s.charAt(0)==' ') s = s.substring(1);
			if (s.length()>0)
			{
				s = sb.toString() + s;
			}
		}
		if (s.length()>0) this.output.println(s);
	}
	
	/**
	 * Registers a command with this shell.
	 * @param command The command to register.
	 * @param names The names to give the command.
	 */
	public void register(Command command, String... names)
	{
		if (names.length==0) return;
		for (String s : names)
		{
			this.commandMap.put(s, command);
		}
		Arrays.sort(names);
		this.registrationSet.add(new Pair<Command,List<String>>(command,Collections.unmodifiableList(
				Arrays.asList(names))));
	}
	
	/**
	 * Executes this shell.  This method will execute commands read from the provided input until termination is
	 * requested or the end of input is reached.
	 * 
	 * Before executing commands read from standard input, it will execute the commands provided in the initalCommands
	 * parameter.  These commands will be executed without displaying a prompt.
	 * 
	 * @param initialCommands The commands to execute before displaying a prompt.
	 * 
	 * @throws IOException If an I/O error occurs while reading input.
	 */
	public void execute(String[] initialCommands)
		throws IOException
	{
		for (String commandStr : initialCommands)
		{
			interpret(commandStr);
			if (getTerminate()) return;
		}
		while (!getTerminate())
		{
			this.output.print(PROMPT);
			String commandStr = this.input.readLine();
			if (commandStr==null) return;
			interpret(commandStr);
		}
	}
	
	/**
	 * Interprets the provided string as a shell command.
	 * @param commandStr The string to interpret as a shell command.
	 */
	public void interpret(String commandStr)
	{
		commandStr = commandStr.trim();
		if (commandStr.length()==0) return;
		
		String[] parts = commandStr.split("\\s+");
		String[] args = Arrays.copyOfRange(parts, 1, parts.length);
		Command command = commandMap.get(parts[0]);
		
		if (command==null)
		{
			// Shortcut - if the command string contains an equal sign and no spaces, treat it like a call to set
			if ((commandStr.indexOf('=')!=-1) && (commandStr.indexOf(' ')==-1))
			{
				interpret("set " + commandStr);
				return;
			}
			this.output.println("Unrecognized command: " + parts[0]);
			return;
		}
		
		try
		{
			command.execute(this, args);
		} catch (CommandFailureException e)
		{
			this.print(e.getMessage());
		}
	}
	
	public Set<Pair<Command, List<String>>> getRegistrationSet()
	{
		return Collections.unmodifiableSet(registrationSet);
	}
	
	public Map<String, Command> getCommandMap()
	{
		return Collections.unmodifiableMap(commandMap);
	}
	
	/* *********** GETTERS AND SETTERS *********** */

	public BufferedReader getInput()
	{
		return input;
	}

	public void setInput(BufferedReader input)
	{
		this.input = input;
	}

	public PrintStream getOutput()
	{
		return output;
	}

	public void setOutput(PrintStream output)
	{
		this.output = output;
	}

	public MetricTrackingAgent getAgent()
	{
		return agent;
	}

	public void setAgent(MetricTrackingAgent agent)
	{
		this.agent = agent;
	}

	public boolean getTerminate()
	{
		return terminate;
	}

	public void setTerminate(boolean terminate)
	{
		this.terminate = terminate;
	}

	public Environment getEnvironment()
	{
		return environment;
	}

	public void setEnvironment(Environment environment)
	{
		this.environment = environment;
	}

	public WorldMap getWorld()
	{
		return world;
	}

	public void setWorld(WorldMap world)
	{
		this.world = world;
	}

	public List<SimulationStep> getSimulation()
	{
		return simulation;
	}

	public void setSimulation(List<SimulationStep> simulation)
	{
		this.simulation = simulation;
	}
}

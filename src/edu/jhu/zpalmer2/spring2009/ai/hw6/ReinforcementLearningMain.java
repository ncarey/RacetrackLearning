package edu.jhu.zpalmer2.spring2009.ai.hw6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Shell;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command.EnvCommand;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command.HelpCommand;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command.IterateCommand;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command.LoadCommand;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command.LoadWorldCommand;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command.MakeCommand;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command.MetricsCommand;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command.QuitCommand;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command.SaveCommand;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command.SetCommand;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command.SimulateCommand;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command.VarHelpCommand;

/**
 * The main class for the reinforcement learning software.
 * @author Zachary Palmer
 */
public class ReinforcementLearningMain
{
	public static void main(String arg[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Shell shell = new Shell(br, System.out, 79);
		
		shell.register(new EnvCommand(), "e", "env");
		shell.register(new HelpCommand(), "h", "help", "?");
		shell.register(new IterateCommand(), "i", "iterate");
		shell.register(new LoadCommand(), "load");
		shell.register(new LoadWorldCommand(), "map");
		shell.register(new MakeCommand(), "make");
		shell.register(new MetricsCommand(), "metrics");
		shell.register(new QuitCommand(), "quit");
		shell.register(new SaveCommand(), "save");
		shell.register(new SetCommand(), "s", "set");
		shell.register(new SimulateCommand(), "sim", "simulate");
		shell.register(new VarHelpCommand(), "vhelp");
		
		try
		{
			shell.execute(arg);
		} catch (IOException ioe)
		{
			System.err.println("Error reading from console input.");
		}
	}
}

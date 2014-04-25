package edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.zip.GZIPInputStream;

import edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm.MetricTrackingAgent;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Command;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Shell;

/**
 * Allows a persisted agent to be loaded.
 * @author Zachary Palmer
 */
public class LoadCommand extends Command
{
	@Override
	public void execute(Shell shell, String[] args) throws CommandFailureException
	{
		checkArgCount(args, 1, 1);
		
		FileInputStream fis = null;
		
		try
		{
			fis = new FileInputStream(args[0]);
		} catch (FileNotFoundException e)
		{
			throw new CommandFailureException(e.getMessage());
		}
		
		GZIPInputStream gzis = null;
		ObjectInputStream ois = null;
		try
		{
			gzis = new GZIPInputStream(fis);
			ois = new ObjectInputStream(gzis);
		} catch (IOException e)
		{
			throw new CommandFailureException(e.getMessage());
		}
		
		Object o;
		try
		{
			o = ois.readObject();
		} catch (IOException e)
		{
			throw new CommandFailureException(e.getMessage());
		} catch (ClassNotFoundException e)
		{
			throw new CommandFailureException("The provided file is not a persisted agent (bad class).");
		}
		
		try
		{
			ois.close();
		} catch (IOException e)
		{
			throw new CommandFailureException("Could not close input stream!");
		}
		
		if (!(o instanceof MetricTrackingAgent))
		{
			throw new CommandFailureException("The provided file is not a persisted agent (wrong class: " +
					o.getClass().getName() + ").");
		}
		
		shell.setAgent((MetricTrackingAgent)o);
	}

	@Override
	public String getLongHelp(String name)
	{
		return
			"Usage: " + name + " <pathname>\n\n"+
			"Loads a persisted agent from disk.  Note that the agent's world must be loaded separately.";
	}

	@Override
	public String getShortHelp()
	{
		return "loads a saved agent";
	}
}

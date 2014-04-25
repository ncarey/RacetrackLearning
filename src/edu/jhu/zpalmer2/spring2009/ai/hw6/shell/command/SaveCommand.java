package edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPOutputStream;

import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Command;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Shell;

/**
 * This command can be used to persist an agent to disk.
 * 
 * @author Zachary Palmer
 */
public class SaveCommand extends Command
{

	@Override
	public void execute(Shell shell, String[] args) throws CommandFailureException
	{
		checkArgCount(args, 1, 1);
		checkAgent(shell);

		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(args[0]);
		} catch (IOException e)
		{
			throw new CommandFailureException(e.getMessage());
		}
		
		GZIPOutputStream gzos = null;
		ObjectOutputStream oos = null;
		try
		{
			gzos = new GZIPOutputStream(fos);
			oos = new ObjectOutputStream(gzos);
			oos.writeObject(shell.getAgent());
			oos.close();
		} catch (IOException e)
		{
			try
			{
				fos.close();
			} catch (IOException ioe)
			{
				// Not much can be done about that.
			}
			throw new CommandFailureException("Failed to write to file: " + e.getMessage());
		}
	}

	@Override
	public String getLongHelp(String name)
	{
		return "Usage: "
				+ name
				+ " <pathname>\n\n"
				+ "Saves the agent in memory.  This operation persists the agent's knowledge base, allowing an agent which "
				+ "took significant amounts of time to generate to be preserved and recalled quickly.";
	}

	@Override
	public String getShortHelp()
	{
		return "saves the active agent";
	}
}

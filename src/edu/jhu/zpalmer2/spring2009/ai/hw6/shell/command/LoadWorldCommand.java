package edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import edu.jhu.zpalmer2.spring2009.ai.hw6.data.WorldMap;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Command;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Shell;

/**
 * Loads a world into memory from a specified file.
 * @author Zachary Palmer
 */
public class LoadWorldCommand extends Command
{
	@Override
	public void execute(Shell shell, String[] args)
		throws CommandFailureException
	{
		checkArgCount(args, 1, 1);
		
		String path = args[0];
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(path);
		} catch (FileNotFoundException e)
		{
			throw new CommandFailureException(path + ": file not found");
		}
		
		WorldMap map;
		try
		{
			map = WorldMap.readFromStream(fis);
		} catch (IOException ioe)
		{
			throw new CommandFailureException(path + ": error reading file: " + ioe.getMessage());
		} catch (IllegalArgumentException iae)
		{
			throw new CommandFailureException(path + ": format error: " + iae.getMessage());
		}
		
		try
		{
			fis.close();
		} catch (IOException e)
		{
			shell.print("Warning: could not close open file " + path + ": " + e.getMessage());
		}
		
		shell.setWorld(map);
	}

	@Override
	public String getLongHelp(String name)
	{
		return
			"Usage: " + name + " <map filename>\n\n" + 
			"Loads a map.  The specified path indicates the location of the map file.";
	}

	@Override
	public String getShortHelp()
	{
		return "loads a map";
	}
}

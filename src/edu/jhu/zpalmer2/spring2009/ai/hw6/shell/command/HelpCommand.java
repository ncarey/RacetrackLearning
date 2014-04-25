package edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Command;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Shell;
import edu.jhu.zpalmer2.spring2009.ai.hw6.util.Pair;

public class HelpCommand extends Command
{
	@Override
	public void execute(Shell shell, String[] args)
		throws CommandFailureException
	{
		checkArgCount(args, 0, 1);

		if (args.length==0)
		{
			List<Pair<Command,List<String>>> registrations = new ArrayList<Pair<Command,List<String>>>(
					shell.getRegistrationSet());
			Collections.sort(registrations,
					new Comparator<Pair<Command,List<String>>>()
					{
						public int compare(Pair<Command, List<String>> a, Pair<Command, List<String>> b)
						{
							return a.getSecond().get(0).compareTo(b.getSecond().get(0));
						}
					});
			
			/* Figure out how wide the longest prefix will be */
			int prefixLength = 0;
			for (Pair<Command, List<String>> registration : registrations)
			{
				int length = 0;
				for (String name : registration.getSecond()) length+=name.length();
				length+=(registration.getSecond().size()-1)*2;
				length+=3;
				prefixLength = Math.max(length, prefixLength);
			}
			
			/* Print the commands */
			for (Pair<Command, List<String>> registration : registrations)
			{
				StringBuffer sb = new StringBuffer();
				for (int i=0;i<registration.getSecond().size();i++)
				{
					if (i>0) sb.append(", ");
					sb.append(registration.getSecond().get(i));
				}
				while (sb.length()<prefixLength-3) sb.append(" ");
				sb.append(" - ");
				sb.append(registration.getFirst().getShortHelp());
				shell.print(sb.toString(), prefixLength);
			}
		} else
		{
			Command command = shell.getCommandMap().get(args[0]);
			if (command==null)
			{
				throw new CommandFailureException("Unrecognized command: " + args[0]);
			}
			shell.print("");
			shell.print(command.getLongHelp(args[0]));
			shell.print("");
		}
	}

	@Override
	public String getLongHelp(String name)
	{
		return
			"Usage: " + name + " [command]\n\n" +
			"If a command is provided, detailed help regarding that command is displayed.  Otherwise, a list of " +
			"commands and help summaries is displayed.";
	}

	@Override
	public String getShortHelp()
	{
		return "displays help";
	}
	
}

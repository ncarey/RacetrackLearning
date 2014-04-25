package edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Command;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Environment;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Shell;

/**
 * This command displays the contents of the environment.
 * @author Zachary Palmer
 */
public class EnvCommand extends Command
{
	@Override
	public void execute(Shell shell, String[] args)
		throws CommandFailureException
	{
		checkArgCount(args, 0, 0);
		
		int maxLength = 0; // -3 for "get", +3 for " = "
		List<Method> getters = new ArrayList<Method>();
		for (Method method : Environment.class.getMethods())
		{
			if ((method.getName().startsWith("get")) && (!method.getName().equals("getClass")))
			{
				getters.add(method);
				maxLength = Math.max(maxLength, method.getName().length());
			}
		}
		
		Collections.sort(getters, new Comparator<Method>()
				{
					public int compare(Method a, Method b)
					{
						return a.getName().compareTo(b.getName());
					}
				});
		
		for (Method method : getters)
		{
			Object o;
			try
			{
				o = method.invoke(shell.getEnvironment());
			} catch (IllegalArgumentException e)
			{
				throw new IllegalStateException(e);
			} catch (IllegalAccessException e)
			{
				throw new IllegalStateException(e);
			} catch (InvocationTargetException e)
			{
				throw new IllegalStateException(e);
			}
			StringBuffer sb = new StringBuffer();
			String name = method.getName().substring(3);
			name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
			sb.append(name);
			while (sb.length()<maxLength-3) sb.append(" ");
			sb.append(" = ");
			sb.append(o);
			shell.print(sb.toString());
		}
	}

	@Override
	public String getLongHelp(String name)
	{
		return
			"Usage: " + name + "\n\n" +
			"Displays each environment variable and its current value.";
	}

	@Override
	public String getShortHelp()
	{
		return "displays contents of environment variables";
	}
	
}

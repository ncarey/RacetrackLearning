package edu.jhu.zpalmer2.spring2009.ai.hw6.shell.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Command;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Environment;
import edu.jhu.zpalmer2.spring2009.ai.hw6.shell.Shell;

/**
 * This command allows the changing of variables in the shell's environment.
 * @author Zachary Palmer
 */
public class SetCommand extends Command
{
	@Override
	public void execute(Shell shell, String[] args)
		throws CommandFailureException
	{
		checkArgCount(args, 1, 1);
		
		String setCommand = args[0];
		if (!setCommand.contains("="))
		{
			throw new CommandFailureException("The set expression must contain an equal sign.");
		}
		
		String name = setCommand.substring(0,setCommand.indexOf('='));
		String value = setCommand.substring(setCommand.indexOf('=')+1);
		
		String setterName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
		Method[] methods = Environment.class.getMethods();
		Method setter = null;
		for (Method method : methods)
		{
			if (method.getName().equals(setterName))
			{
				setter = method;
				// Note: assuming only one setter for each variable
				break;
			}
		}
		
		if (setter==null)
		{
			throw new CommandFailureException("Unrecognized variable: " + name);
		}
		
		if (setter.getParameterTypes().length!=1)
		{
			throw new IllegalStateException("Setter has a number of variables other than 1: " + setter.getName());
		}
		
		// Extract the type of the parameter
		Class<?> paramClass = setter.getParameterTypes()[0];
		Object parsedValue;
		if (String.class.isAssignableFrom(paramClass))
		{
			parsedValue = value;
		} else if ((Integer.class.isAssignableFrom(paramClass)) || (int.class.isAssignableFrom(paramClass)))
		{
			try
			{
				parsedValue = Integer.parseInt(value);
			} catch (NumberFormatException nfe)
			{
				throw new CommandFailureException("Invalid value: " + nfe.getMessage());
			}
		} else if ((Double.class.isAssignableFrom(paramClass)) || (double.class.isAssignableFrom(paramClass)))
		{
			try
			{
				parsedValue = Double.parseDouble(value);
			} catch (NumberFormatException nfe)
			{
				throw new CommandFailureException("Invalid value: " + nfe.getMessage());
			}
		} else if ((Boolean.class.isAssignableFrom(paramClass)) || (boolean.class.isAssignableFrom(paramClass)))
		{
			if ("t".equals(value.toLowerCase()))
			{
				parsedValue = Boolean.TRUE;
			} else if ("f".equals(value.toLowerCase()))
			{
				parsedValue = Boolean.FALSE;
			} else
			{
				throw new CommandFailureException("Invalid boolean value (not t or f): " + value);
			}
		} else
		{
			throw new IllegalStateException("Not prepared to parse value for parameter of type " + paramClass);
		}
		
		// Call the setter
		try
		{
			setter.invoke(shell.getEnvironment(), parsedValue);
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
	}

	@Override
	public String getLongHelp(String name)
	{
		return
			"Usage: " + name + " <name>=<value>\n\n" +
			"Assigns the provided value to the specified variable.  The name provided must be that of an existing " +
			"variable; for a list of appropriate variables, see the env command.";
	}

	@Override
	public String getShortHelp()
	{
		return "sets environment variable values";
	}
}

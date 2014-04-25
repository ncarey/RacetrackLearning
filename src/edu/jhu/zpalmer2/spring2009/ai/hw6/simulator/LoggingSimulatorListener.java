package edu.jhu.zpalmer2.spring2009.ai.hw6.simulator;

import java.io.PrintStream;

import edu.jhu.zpalmer2.spring2009.ai.hw6.data.WorldMap;
import edu.jhu.zpalmer2.spring2009.ai.hw6.util.ReinforcementLearningUtilities;

/**
 * This listener logs each simulation event to a print stream.
 * @author Zachary Palmer
 */
public class LoggingSimulatorListener implements SimulatorListener
{
	/**
	 * The world map over which to render the logging messages.
	 */
	private WorldMap worldMap;
	/**
	 * The print stream to which logging messages should be written.
	 */
	private PrintStream output;
	/**
	 * The delay (in milliseconds) between message displays.
	 */
	private int outputDelay;
	
	/**
	 * The last time at which a message was displayed.
	 */
	private long lastDisplay;
	
	/**
	 * Creates a new logging listener.
	 * @param worldMap The world map over which to render the logging messages.
	 * @param output The target for output.
	 * @param outputDelay The minimum time between outputs.
	 */
	public LoggingSimulatorListener(WorldMap worldMap, PrintStream output, int outputDelay)
	{
		super();
		this.worldMap = worldMap;
		this.output = output;
		this.outputDelay = outputDelay;
		
		this.lastDisplay = 0;
	}

	@Override
	public void simulationEventOccurred(SimulatorEvent event)
	{
		String message = ReinforcementLearningUtilities.renderSimulationStep(this.worldMap, event.getStep());
		long waitTime = this.lastDisplay + this.outputDelay - System.currentTimeMillis();
		if (waitTime > 0)
		{
			try
			{
				Thread.sleep(waitTime);
			} catch (InterruptedException e)
			{
				// This is me not caring.
			}
		}
		this.output.println(message);
		this.lastDisplay = System.currentTimeMillis();
	}
}

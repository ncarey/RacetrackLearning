package edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm;

import java.util.Collections;
import java.util.Set;

import edu.jhu.zpalmer2.spring2009.ai.hw6.simulator.Simulator;
import edu.jhu.zpalmer2.spring2009.ai.hw6.simulator.SimulatorListener;

/**
 * This wrapper object tracks the call metrics to another agent which are made through it.
 * @author Zachary Palmer
 */
public class MetricTrackingAgent implements SimulationBasedReinforcementLearningAgent
{
	private static final long serialVersionUID = 1L;

	/**
	 * The agent that this tracking agent is wrapping.
	 */
	private ReinforcementLearningAgent backingAgent;
	
	/** The number of learning iterations performed so far. */
	private int learningIterations;
	/** The time, expressed in ms, that this agent has spent in learning iterations. */
	private long time;
	/** The return value of the last iteration. */
	private boolean convergenceDetected;
	
	/**
	 * General constructor.	
	 * @param backingAgent The agent for which metrics will be tracked.
	 */
	public MetricTrackingAgent(ReinforcementLearningAgent backingAgent)
	{
		super();
		this.backingAgent = backingAgent;
		this.learningIterations = 0;
		this.time = 0;
		this.convergenceDetected = false;
	}

	/**
	 * Indicates the number of learning iterations this agent has performed.
	 * @return The number of learning iterations this agent has performed.
	 */
	public int getIterationCount()
	{
		return this.learningIterations;
	}
	
	/**
	 * Determines the CPU time this agent has spent in learning iterations.
	 */
	public long getTime()
	{
		return this.time;
	}
	
	public boolean isConvergenceDetected()
	{
		return convergenceDetected;
	}
	
	@Override
	public void setSimulator(Simulator simulator)
	{
		if (this.backingAgent instanceof SimulationBasedReinforcementLearningAgent)
		{
			((SimulationBasedReinforcementLearningAgent)this.backingAgent).setSimulator(simulator);
		}
	}
	
	@Override
	public Set<? extends SimulatorListener> getSimulatorListeners()
	{
		if (this.backingAgent instanceof SimulationBasedReinforcementLearningAgent)
		{
			return ((SimulationBasedReinforcementLearningAgent)this.backingAgent).getSimulatorListeners();
		} else
		{
			return Collections.emptySet();
		}
	}
	
	@Override
	public MetricTrackingAgent duplicate()
	{
		MetricTrackingAgent ret = new MetricTrackingAgent(this.backingAgent.duplicate());
		ret.convergenceDetected = this.convergenceDetected;
		ret.time = this.time;
		ret.learningIterations = this.learningIterations;
		return ret;
	}

	@Override
	public Policy getPolicy()
	{
		return this.backingAgent.getPolicy();
	}

	@Override
	public boolean iterate()
	{
		long start = System.currentTimeMillis();
		this.learningIterations++;
		
		this.convergenceDetected = this.backingAgent.iterate();

		long stop = System.currentTimeMillis();
		this.time += (stop - start);
		
		return this.convergenceDetected;
	}
}

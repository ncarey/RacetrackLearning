package edu.jhu.zpalmer2.spring2009.ai.hw6.shell;

/**
 * This class contains the environment variables used by the shell's commands.
 * @author Zachary Palmer
 */
public class Environment
{
	/**
	 * The epsilon value used for convergence tolerance.
	 */
	private double epsilon;
	/**
	 * The alpha value used for controlling learning rate.
	 */
	private double alpha;
	/**
	 * The gamma value used to control discount factor.
	 */
	private double gamma;
	/**
	 * The minimum number of times a state will be explored.
	 */
	private int minExplorations;
	/**
	 * Whether or not hard crashing is used.
	 */
	private boolean hardCrashing;
	/**
	 * Whether or not simulation causes updates to be printed.
	 */
	private boolean verboseSimulation;
	/**
	 * The delay time (in milliseconds) between simulation steps.  This allows for a video-like presentation of the
	 * verbose simulation.  This value has no impact if verbose simulation is not set.
	 */
	private int verboseSimulationDelay;
	/**
	 * An approximate interval at which progress reports are made.  Progress reports will not be made more often than
	 * this value (in milliseconds).  If zero, no progress reports are made.  Not every operation supports progress
	 * reporting.
	 */
	private int progressReportDelay;
	
	/**
	 * General constructor.  Builds a default environment.
	 */
	public Environment()
	{
		this.epsilon = 0;
		this.alpha = 0.5;
		this.gamma = 1.0;
		this.minExplorations = 1;
		this.hardCrashing = false;
		this.verboseSimulation = false;
		this.verboseSimulationDelay = 0;
		this.progressReportDelay = 0;
	}

	public double getEpsilon()
	{
		return epsilon;
	}

	public void setEpsilon(double epsilon)
	{
		this.epsilon = epsilon;
	}

	public double getAlpha()
	{
		return alpha;
	}

	public void setAlpha(double alpha)
	{
		this.alpha = alpha;
	}
	
	public double getGamma()
	{
		return gamma;
	}

	public void setGamma(double gamma)
	{
		this.gamma = gamma;
	}
	
	public int getMinExplorations()
	{
		return minExplorations;
	}

	public void setMinExplorations(int minExplorations)
	{
		this.minExplorations = minExplorations;
	}

	public boolean getHardCrashing()
	{
		return hardCrashing;
	}

	public void setHardCrashing(boolean hardCrashing)
	{
		this.hardCrashing = hardCrashing;
	}

	public boolean getVerboseSimulation()
	{
		return verboseSimulation;
	}

	public void setVerboseSimulation(boolean verboseSimulation)
	{
		this.verboseSimulation = verboseSimulation;
	}

	public int getVerboseSimulationDelay()
	{
		return verboseSimulationDelay;
	}

	public void setVerboseSimulationDelay(int verboseSimulationDelay)
	{
		this.verboseSimulationDelay = verboseSimulationDelay;
	}

	public int getProgressReportDelay()
	{
		return progressReportDelay;
	}

	public void setProgressReportDelay(int progressReportDelay)
	{
		this.progressReportDelay = progressReportDelay;
	}
}

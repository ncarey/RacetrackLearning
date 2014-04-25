package edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm;

import java.io.Serializable;

/**
 * This interface is implemented by those classes which represent reinforcement learning agent. A reinforcement
 * learning agent will, after an amount of learning, produce a policy which can be used to make decisions in the agent's
 * world.
 * 
 * A reinforcement learning agent is stateful; it maintains intermediate data representing the knowledge it has
 * obtained.  Each call to the learning method makes some amount of progress toward a final policy.  If it does not
 * reach the final policy, it indicates as much and expects to be called again.  In either case, the object is prepared
 * to produce a policy; it may just be suboptimal.
 * 
 * As a result of the state being stored in this object, persisting a reinforcement learning agent is sufficient to
 * ensure that knowledge is preserved between JVMs.
 * 
 * @author Zachary Palmer
 */
public interface ReinforcementLearningAgent extends Serializable
{
	/**
	 * Performs some work in approaching a final policy.  Note that multiple final policies may exist as a result of the
	 * particulars of the algorithm in question (such as a flexible definition of convergence); this algorithm will
	 * simply do work in an attempt to reach one of them.
	 * @return <code>true</code> if a final policy was found; <code>false</code> otherwise.
	 */
	public boolean iterate();
	
	/**
	 * Retrieves the policy that this algorithm has thus far established.
	 * @return A policy produced by this algorithm.
	 */
	public Policy getPolicy();
	
	/**
	 * Creates a duplicate of this reinforcement learning agent.
	 */
	public ReinforcementLearningAgent duplicate();
}

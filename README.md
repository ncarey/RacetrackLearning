===============================================
= Simple Reinforcement Learner Implementation =
===============================================
Copyright 2010, Zachary Palmer
All rights reserved


--------------------------------------------------------------------------------
1. Introduction
--------------------------------------------------------------------------------

The provided files provide a partial implementation of the reinforcement
learning homework assignment assigned in the Spring 2014 session of Artificial
Intelligence (EN.600.335/435) at Johns Hopkins University.  It is intended to
be used by students as a basis for their reinforcement learner
implementations, and provides a framework that will allow students to
concentrate on the details of the reinforcement learning algorithms, rather
than the racetrack problem simulator.


Much of the following README describes the functioning of a completed program
using this codebase; obviously, none of this will work until you actually fill
in the reinforcement learning algorithms.

The source files which require completion by students are:

	./src/edu/jhu/zpalmer2/spring2009/ai/hw6/algorithm/ValueIteratingAgent.java
	./src/edu/jhu/zpalmer2/spring2009/ai/hw6/algorithm/QLearningAgent.java

The functions which need to be completed are noted with a "TODO" comment.


Terms of use are addressed below.


--------------------------------------------------------------------------------
2. Usage
--------------------------------------------------------------------------------

In order to use this software, you must have a Java runtime environment of at least version 1.5 installed; it was tested using OpenJDK 1.6.0_0 under Debian GNU/Linux Lenny stable, although it should work for any compatible, standards-compliant Java 1.5 implementation.

Extract the tarball to a directory and open a terminal in that directory.
Running the following commands is sufficient to launch the program:

	./build-jar.sh
	java -jar ./ReinforcementLearning.jar

This will start the RLASH prompt (Reinforcement Learning Agent SHell) which will accept a series of commands.  To quit the application, end your input (Ctrl-D under Linux or Mac, Ctrl-Z + Enter under Windows) or issue the command "quit".


--------------------------------------------------------------------------------
3. RLASH Commands
--------------------------------------------------------------------------------

An inline help details most of the commands of the application.  When help is issued by itself, a summary of each command is displayed.  When the name of a command follows the help command, detailed help is displayed on that command.

Maps are loaded using the "map" command, such as "map data/L-track.txt".  A map must be loaded before you can create an agent.  If you load a map for which an agent was not trained, results are undefined.

Agents are created using variables from a configured environment (the contents of which can be displayed using the command "env").  For example, the environment variable epsilon controls the convergence tolerance for the algorithms (as shown in the pseudocode in the book).  To change an environment variable, use the "set" command.  For example:

	set epsilon=0.99

Information about the purpose of environment variables can be obtained with the "vhelp" command.

After configuring the variables to your liking, create the agent with the "make" command.  The make command requires that you specify the algorithm ("vi" or "q") you are using.  Note that changes to the environment variables will not affect an agent after it has been created (although they may affect other behaviors of the application).

In order to train the agent, use the "iterate" command.  If called alone, the iterate command iterates through a single pass of the algorithm's learning routine.  For value iteration, this is one iteration of value learning.  For Q-learning, this is one pass through the race track.  If "iterate" is called with a parameter, that parameter must be either a positive integer (which specifies a number of iterations) or "oo" (which specifies to run until convergence).

Once the agent is trained, it can be tested using the "sim" command.  Again, a number of simulations can be specified.  Each simulation is executed using a COPY of the agent; thus, no side effects will occur from running a simulation.

Q-learning iterations and simulations can be observed by setting the environment variable "verboseSimulation" to true (via "set verboseSimulation=t").  When this value is set, an ASCII representation of the race track is written to the console at each simulation time step.  This value can be modified before or after agent creation.  In order to obtain an animation-like effect, the environment variable "verboseSimulationDelay" can be set to a number of milliseconds between frames.

The metrics of the agent can be obtained using the "metrics" command.  This indicates whether or not the agent has converged, how many learning iterations it has performed, and the amount of wall time it has spent learning.  NOTE: If you train a Q-learning agent with verboseSimulation=t, the wall time you get from metrics will be meaningless!

Agents can be loaded from and saved to disk using the "load" and "save" commands.


--------------------------------------------------------------------------------
4. Command-line Interface
--------------------------------------------------------------------------------

If arguments are provided to the application on the command line, they are interpreted as commands to be issued to the system before a prompt is provided.  For example, running the program as

	java -jar ReinforcementLearning.jar "set epsilon=0.001" "set alpha=0.5" "set gamma=0.7"

will initialize environment variables in the above manner by the time an RLASH prompt is presented.  On the other hand, this command:

	java -jar ReinforcementLearning.jar "set epsilon=0.5" "set gamma=0.5" "map data/L-track.txt" "make vi" "i oo" "sim 100" "quit"

could be used to perform a headless test of an agent, although these parameters are not very interesting.


--------------------------------------------------------------------------------
5. Disclaimer
--------------------------------------------------------------------------------

This software was originally written only for the purpose of analyzing the software engineering problems involved in producing an application of its type.  It is not guaranteed in any sense to be suitable for any purpose and no warranty, express or implied, is provided.  Use at your own risk.


--------------------------------------------------------------------------------
6. Terms of Use
--------------------------------------------------------------------------------

This software is only intended to be used by the members of the JHU Artificial Intelligence class taught by Ben Mitchell.  Ben Mitchell has the right to distribute and utilize the application for purposes related to teaching this class.  Individuals to whom he distributes this software are permitted to use it only for the purpose of generating data for the scientific paper assignments provided in this class.  Any other use is expressly prohibited without written consent of the author, Zachary Palmer

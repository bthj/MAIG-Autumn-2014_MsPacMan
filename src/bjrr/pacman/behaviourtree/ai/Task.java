package bjrr.pacman.behaviourtree.ai;

/**
 * Base abstract class for all tasks in the behaviour tree
 * 
 * Based on {@link http://magicscrollsofcode.blogspot.dk/2010/12/behavior-trees-by-example-ai-in-android.html}
 * 
 * @author bthj
 *
 */

public abstract class Task {

	protected Blackboard bb;
	
	/*
	 * node name
	 */
	protected String name;
	
	/**
	 * Creates a new instance of the Task class
	 * @param name Name of the node (for debugging)
	 */
	public Task( Blackboard blackboard, String name ) {
		
		this.name = name;
		this.bb = blackboard;
	}
	
	/**
	 * Override to do a pre-conditions check to see if the task can be updated.
	 * @return True if it can, false if it can't
	 */
	public abstract boolean checkConditions();
	
	/**
	 * Override to add startup logic to the task
	 */
	public abstract void start();
	
	/**
	 * Override to add ending logic to the task
	 */
	public abstract void end();
	
	/**
	 * Override to specify the logic for this task on each update
	 */
	public abstract void run();
	
	/**
	 * Override to specify the controller for this task
	 * @return The task controller for this task.
	 */
	public abstract TaskController getControl();
}

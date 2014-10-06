package bjrr.pacman.behaviourtree.ai.composite;

import java.util.Vector;

import bjrr.pacman.behaviourtree.ai.Task;
import bjrr.pacman.behaviourtree.ai.TaskController;

/**
 * This class extends the TaskController class to add support for 
 * child tasks and their logic. Used together with ParentTask.
 * 
 * Based on {@link http://magicscrollsofcode.blogspot.dk/2010/12/behavior-trees-by-example-ai-in-android.html}
 * 
 * @author bthj
 *
 */

public class ParentTaskController extends TaskController {

	/**
	 * Vector of child Tasks
	 */
	public Vector<Task> subtasks;
	
	/**
	 * Current updating task
	 */
	public Task currentTask;
	
	/**
	 * Creates a new instance of the ParentTaskController class
	 * @param task
	 */
	public ParentTaskController( Task task ) {
		
		super(task);
		
		this.subtasks = new Vector<Task>();
		this.currentTask = null;
	}
	
	/**
	 * Adds a new subtask to the end of the subtask list.
	 * @param task
	 */
	public void add( Task task ) {
		
		subtasks.add( task );
	}
	
	/**
	 * Resets the task as if it had just started.
	 */
	public void reset() {
		
		super.reset();
		this.currentTask = subtasks.firstElement();
	}

}

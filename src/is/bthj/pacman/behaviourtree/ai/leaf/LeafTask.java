package is.bthj.pacman.behaviourtree.ai.leaf;

/**
 * Leaf task (or node) in the behavior tree.
 *  
 * Specifies a TaskControler, by composition, 
 * to take care of all the control logic, 
 * without burdening the Task class with 
 * complications.
 * 
 * Based on {@link http://magicscrollsofcode.blogspot.dk/2010/12/behavior-trees-by-example-ai-in-android.html}
 * 
 */

import is.bthj.pacman.behaviourtree.ai.Blackboard;
import is.bthj.pacman.behaviourtree.ai.Task;
import is.bthj.pacman.behaviourtree.ai.TaskController;

public abstract class LeafTask extends Task {
	
	/**
	 * Task controler to keep track of the Task state.
	 */
	protected TaskController control;
	

	/**
	 * Creates a new instance of the LeafTask class
	 * @param blackboard Reference to the AI Blackboard data
	 * @param name Name of this node
	 */
	public LeafTask(Blackboard blackboard, String name) {
		
		super(blackboard, name);
		createController();
	}
	
	private void createController() {
		
		this.control = new TaskController( this );
	}


	@Override
	public TaskController getControl() {

		return this.control;
	}

}

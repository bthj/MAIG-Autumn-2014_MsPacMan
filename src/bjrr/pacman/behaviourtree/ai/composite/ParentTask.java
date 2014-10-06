package bjrr.pacman.behaviourtree.ai.composite;

import bjrr.pacman.behaviourtree.ai.Blackboard;
import bjrr.pacman.behaviourtree.ai.Task;
import bjrr.pacman.behaviourtree.ai.TaskController;
import sun.font.CreatedFontTracker;

/**
 * A composite node.
 * 
 * Inner node of the behavior tree, a flow director node, 
 * that selects a child to be executed next.
 * 
 * Sets a specific kind of TaskController for these kinds of tasks.
 * 
 * Based on {@link http://magicscrollsofcode.blogspot.dk/2010/12/behavior-trees-by-example-ai-in-android.html}
 * 
 * @author bthj
 *
 */
public abstract class ParentTask extends Task {

	/**
	 * TaskController for the parent task
	 */
	ParentTaskController control;
	
	
	/**
	 * Creates a new instance of the ParentTask class
	 * @param name Name for the node
	 */
	public ParentTask( Blackboard blackboard, String name) {
		
		super( blackboard, name );
		createController();
	}
	
	/**
	 * Creates the TaskController.
	 */
	private void createController() {
		
		this.control = new ParentTaskController( this );
	}
	
	/**
	 * Gets the control reference
	 */
	@Override
	public TaskController getControl() {
		
		return control;
	}
	
	/**
	 * Checks for the appropriate pre-state of the data
	 */
	@Override
	public boolean checkConditions() {

		return control.subtasks.size() > 0;
	}
	
	/**
	 * Abstract to be overridden in child classes. Called when a child finishes with success.
	 */
	public abstract void childSucceeded();
	
	/**
	 * Abstract to be overridden in child classes. Called when a child finishes with failure.
	 */
	public abstract void childFailed();
	
	
	/**
	 * Checks whether the child has started, finished or needs updating, and takes the needed
	 * measures in each case
	 */
	@Override
	public void run() {
		
		if( control.finished() ) {
//			System.out.println( "FINISHED" );
			return;
		}
		if( control.currentTask == null ) {
			
			// If there there is a null task, we've done something wrong
			// TODO: log...
			return;
		}
		
		// If we do have a current task...
		if( !control.currentTask.getControl().started() ) {
			
			// ...and it's not yet started, let's start it
			control.currentTask.getControl().safeStart();
			
		} else if( control.currentTask.getControl().finished() ) {
			
			// ...and it's finished, let's end it properly.
			control.currentTask.getControl().safeEnd();
			if( control.currentTask.getControl().succeeded() ) {
				
				this.childSucceeded();
			}
			
			if( control.currentTask.getControl().failed() ) {
				
				this.childFailed();
			}
		} else {
			
			// ...and it's ready, update it
			control.currentTask.run();
		}
	}
	
	
	@Override
	public void end() {
		
//		System.out.println( "ENDED" );
//		
//		control.currentTask.getControl().reset();
//		control.currentTask.getControl().safeStart();
	}
	
	
	@Override
	public void start() {
		
		control.currentTask = control.subtasks.firstElement();
		
		if( control.currentTask == null ) {
			
			// TODO: log...
		}
	}
	
}

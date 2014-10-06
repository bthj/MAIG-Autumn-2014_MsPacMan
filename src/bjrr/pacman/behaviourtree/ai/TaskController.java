package bjrr.pacman.behaviourtree.ai;

/**
 * Controller for a task, either a leaf task or a parent task.
 * 
 * Based on {@link http://magicscrollsofcode.blogspot.dk/2010/12/behavior-trees-by-example-ai-in-android.html}
 * 
 * @author bthj
 *
 */
public class TaskController {

	/**
	 * Indicates whether the task is finished or not.
	 */
	private boolean done;
	
	/**
	 * If finished, indicates if it has finished with success or not.
	 */
	private boolean success;
	
	/**
	 * Indicates if the task has started or not.
	 */
	private boolean started;
	
	/**
	 * Reference to the task we monitor.
	 */
	private Task task;
	
	
	
	/**
	 * Creates a new instance of the TaskController class
	 * @param task
	 */
	public TaskController( Task task ) {
		setTask( task );
		initialize();
	}
	
	
	
	/**
	 * Initializes the class data.
	 */
	private void initialize() {
		this.done = false;
		this.success = true;
		this.started = false;
	}
	
	
	
	/**
	 * Sets the task reference.
	 * @param task
	 */
	public void setTask( Task task ) {
		
		this.task = task;
	}
	
	/**
	 * Starts the monitored class.
	 */
	public void safeStart() {
		
		this.started = true;
		task.start();
	}
	
	/**
	 * Ends the monitored task.
	 */
	public void safeEnd() {
		
		this.done = false;
		this.started = false;
		task.end();
	}
	
	
	
	/**
	 * Ends the monitored class with success
	 */
	public void finishWithSuccess() {
		
		this.success = true;
		this.done = true;
	}
	
	/**
	 * Ends the monitored class with failure
	 */
	public void finishWithFailure() {
		
		this.success = false;
		this.done = true;
	}
	
	
	
	/**
	 * Indicates whether the taks finished successfully
	 * @return True if it did, false if it didn't
	 */
	public boolean succeeded() {
		
		return this.success;
	}
	
	/**
	 * Indicates wheter the task finished with failure
	 * @return True if it did, false if it didn't
	 */
	public boolean failed() {
		
		return ! this.success;
	}
	
	/**
	 * Indicates whether the task finished 
	 * @return True if it did, false if it didn't
	 */
	public boolean finished() {
		
		return this.done;
	}
	
	/**
	 * Indicates whether the class has started or not
	 * @return True if it has, false if it hasn't
	 */
	public boolean started() {
		
		return this.started;
	}
	
	/**
	 * Marks the class as just started.
	 */
	public void reset() {
		
		this.done = false;
	}
}

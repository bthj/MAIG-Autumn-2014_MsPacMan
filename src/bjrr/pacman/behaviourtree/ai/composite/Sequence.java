package bjrr.pacman.behaviourtree.ai.composite;

import bjrr.pacman.behaviourtree.ai.Blackboard;

public class Sequence extends ParentTask {

	public Sequence( Blackboard blackboard, String name) {
		
		super( blackboard, name );
	}

	@Override
	public void childSucceeded() {
		
		int currentPosition = control.subtasks.indexOf( control.currentTask );
		if( currentPosition == (control.subtasks.size() - 1) ) {
			
			control.finishWithSuccess();
			
		} else {
			
			control.currentTask = control.subtasks.elementAt( currentPosition + 1 );
			
			if( ! control.currentTask.checkConditions() ) {

				control.finishWithFailure();
			}
		}
	}

	@Override
	public void childFailed() {

		control.finishWithFailure();
	}

}

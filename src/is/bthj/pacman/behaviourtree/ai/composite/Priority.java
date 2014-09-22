package is.bthj.pacman.behaviourtree.ai.composite;

import is.bthj.pacman.behaviourtree.ai.Blackboard;

public class Priority extends ParentTask {

	public Priority( Blackboard blackboard, String name) {
		
		super( blackboard, name );
	}

	@Override
	public void childSucceeded() {

//		System.out.println( "SUCCEEDED" );
		
		control.finishWithSuccess();
		
//		control.reset();
	}

	@Override
	public void childFailed() {

		int currentPosition = control.subtasks.indexOf( control.currentTask );
		if( currentPosition == (control.subtasks.size() - 1) ) {
			
			control.finishWithFailure();
			
		} else {
			
			control.currentTask = control.subtasks.elementAt( currentPosition + 1 );
			
			if( ! control.currentTask.checkConditions() ) {
				
				control.finishWithFailure();
			}
		}
	}

}

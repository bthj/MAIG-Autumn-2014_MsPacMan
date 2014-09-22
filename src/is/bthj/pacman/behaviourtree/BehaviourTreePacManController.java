package is.bthj.pacman.behaviourtree;

import is.bthj.pacman.behaviourtree.ai.Blackboard;
import is.bthj.pacman.behaviourtree.ai.composite.ParentTaskController;
import is.bthj.pacman.behaviourtree.ai.composite.Priority;
import is.bthj.pacman.behaviourtree.ai.composite.Sequence;
import is.bthj.pacman.behaviourtree.ai.leaf.FindAvailablePills;
import is.bthj.pacman.behaviourtree.ai.leaf.FindClosestEdibleGhost;
import is.bthj.pacman.behaviourtree.ai.leaf.GoAfterClosesAvailablePill;
import is.bthj.pacman.behaviourtree.ai.leaf.GoAfterEdibleGhost;
import is.bthj.pacman.behaviourtree.ai.leaf.IsGhostTooClose;
import is.bthj.pacman.behaviourtree.ai.leaf.RunAwayFromDanger;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class BehaviourTreePacManController extends Controller<MOVE> {

	PacManBlackboard pacManBB;
	
	
	private Priority rootPrioritySelector;

	
	public BehaviourTreePacManController() {
		
		super();
		
		pacManBB = new PacManBlackboard();
		
		Sequence avoidanceSequence = new Sequence(pacManBB, "Sequence: Avoidance");
		((ParentTaskController)avoidanceSequence.getControl()).add( 
				new IsGhostTooClose(pacManBB, "Task: Is ghost too close") );
		((ParentTaskController)avoidanceSequence.getControl()).add( 
				new RunAwayFromDanger(pacManBB, "Task: Run away from danger") );
		
		Sequence ghostEatingSequence = new Sequence(pacManBB, "Sequence: Eat ghosts");
		((ParentTaskController)ghostEatingSequence.getControl()).add( 
				new FindClosestEdibleGhost(pacManBB, "Task: Find closest edible ghost") );
		((ParentTaskController)ghostEatingSequence.getControl()).add( 
				new GoAfterEdibleGhost(pacManBB, "Task: Go after closest edible ghost") );
		
		Sequence pillEatingSequence = new Sequence(pacManBB, "Sequence: Eat pills");
		((ParentTaskController)pillEatingSequence.getControl()).add( 
				new FindAvailablePills(pacManBB, "Task: Find available pills") );
		((ParentTaskController)pillEatingSequence.getControl()).add( 
				new GoAfterClosesAvailablePill(pacManBB, "Task: Go after closest available pill") );
		
		rootPrioritySelector = new Priority(pacManBB, "rootPriorityComposite");
		((ParentTaskController)rootPrioritySelector.getControl()).add( avoidanceSequence );
		((ParentTaskController)rootPrioritySelector.getControl()).add( ghostEatingSequence );
		((ParentTaskController)rootPrioritySelector.getControl()).add( pillEatingSequence );
	
		rootPrioritySelector.getControl().safeStart();
	}

	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		
//		if( null == pacManBB.game ) {
			pacManBB.game = game;
//		}

//		rootPrioritySelector.getControl().reset();
//		rootPrioritySelector.run();
		
		do {
			rootPrioritySelector.run();
		} while( !rootPrioritySelector.getControl().finished() );
		rootPrioritySelector.getControl().reset();
		
//		System.out.println( "MOVE: " + pacManBB.move );
		
		return pacManBB.move;
	}

}

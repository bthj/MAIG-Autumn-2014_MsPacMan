package bjrr.pacman.behaviourtree.ai.leaf;

import bjrr.pacman.behaviourtree.PacManBlackboard;
import bjrr.pacman.behaviourtree.ai.Blackboard;
import pacman.game.Constants.DM;
import pacman.game.Game;

public class GoAfterEdibleGhost extends LeafTask {
	
	private PacManBlackboard pacManBB;

	public GoAfterEdibleGhost(Blackboard blackboard, String name) {
		super(blackboard, name);

		pacManBB = (PacManBlackboard)blackboard;
	}

	@Override
	public boolean checkConditions() {
		
		return pacManBB.game != null;
	}

	@Override
	public void start() {

	}

	@Override
	public void end() {

	}

	@Override
	public void run() {

		Game game = pacManBB.game;
		
		pacManBB.move = game.getNextMoveTowardsTarget(
				game.getPacmanCurrentNodeIndex(), 
				game.getGhostCurrentNodeIndex(pacManBB.edibleGhost), 
				DM.PATH );
		
//		System.out.println( "WILL GO AFTER EDIBLE GHOST BY MOVE: " + pacManBB.move );
		
		control.finishWithSuccess();
	}

}

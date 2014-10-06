package bjrr.pacman.behaviourtree.ai.leaf;

import bjrr.pacman.behaviourtree.PacManBlackboard;
import bjrr.pacman.behaviourtree.ai.Blackboard;
import pacman.game.Game;
import pacman.game.Constants.DM;

public class RunAwayFromDanger extends LeafTask {
	
	private PacManBlackboard pacManBB;
	

	public RunAwayFromDanger(Blackboard blackboard, String name) {
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
		
		pacManBB.move = game.getNextMoveAwayFromTarget(
				game.getPacmanCurrentNodeIndex(), 
				game.getGhostCurrentNodeIndex(pacManBB.imminentDanger), 
				DM.PATH );
		
//		System.out.println( "avoidance MOVE: " + pacManBB.move );
		
		control.finishWithSuccess();
	}

}

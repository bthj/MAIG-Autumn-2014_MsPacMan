package is.bthj.pacman.behaviourtree.ai.leaf;

import is.bthj.pacman.behaviourtree.PacManBlackboard;
import is.bthj.pacman.behaviourtree.ai.Blackboard;
import pacman.game.Game;
import pacman.game.Constants.DM;

public class GoAfterClosesAvailablePill extends LeafTask {
	
	private PacManBlackboard pacManBB;
	

	public GoAfterClosesAvailablePill(Blackboard blackboard, String name) {
		super(blackboard, name);

		pacManBB = (PacManBlackboard)blackboard;
	}

	@Override
	public boolean checkConditions() {

		return pacManBB.game != null && pacManBB.availablePillIndices.length > 0;
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
		int currentNode = game.getPacmanCurrentNodeIndex();
		
		System.out.println( "current node index: " + currentNode );
		
		pacManBB.move = game.getNextMoveTowardsTarget(
				currentNode, 
				game.getClosestNodeIndexFromNodeIndex(
						currentNode, pacManBB.availablePillIndices, 
						DM.PATH), 
				DM.PATH);
		
		System.out.println( "WILL GO AFTER PILL BY MOVE: " + pacManBB.move );
		
		control.finishWithSuccess();
	}

}

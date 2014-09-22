package is.bthj.pacman.behaviourtree.ai.leaf;

import is.bthj.pacman.behaviourtree.PacManBlackboard;
import is.bthj.pacman.behaviourtree.ai.Blackboard;
import pacman.game.Game;
import pacman.game.Constants.GHOST;

public class FindClosestEdibleGhost extends LeafTask {
	
	private PacManBlackboard pacManBB;
	

	public FindClosestEdibleGhost(Blackboard blackboard, String name) {
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
		int currentNode = game.getPacmanCurrentNodeIndex();
		
		int minDistance = Integer.MAX_VALUE;
		GHOST minGhost = null;
		
		for( GHOST ghost : GHOST.values() ) {
			
			if( game.getGhostEdibleTime(ghost) > 0 ) {
				
				int distance = game.getShortestPathDistance(currentNode, game.getGhostCurrentNodeIndex(ghost));
				if( distance < minDistance ) {
					
					minDistance = distance;
					minGhost = ghost;
				}
			}
		}
		if( null != minGhost ) {
			
			System.out.println( "FOUND EDIBLE GHOST TO GO AFTER" );
			
			pacManBB.edibleGhost = minGhost;
			control.finishWithSuccess();
		} else {
			
			System.out.println( "FOUND no edible ghost TO GO AFTER" );
			
			control.finishWithFailure();
		}
	}

}

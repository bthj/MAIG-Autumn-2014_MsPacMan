package is.bthj.pacman.behaviourtree.ai.leaf;

import is.bthj.pacman.behaviourtree.PacManBlackboard;
import is.bthj.pacman.behaviourtree.ai.Blackboard;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.Constants.GHOST;

public class IsGhostTooClose extends LeafTask {

	private PacManBlackboard pacManBB;

	private static final int MIN_DISTANCE=20;	//if a ghost is this close, run away
	
	
	public IsGhostTooClose(Blackboard blackboard, String name) {
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
		
		pacManBB.imminentDanger = null;
		for( GHOST ghost : GHOST.values() ) {
			
			if( game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0 ) {
				if( game.getShortestPathDistance(currentNode, game.getGhostCurrentNodeIndex(ghost)) < MIN_DISTANCE ) {
					
					pacManBB.imminentDanger = ghost;
					System.out.println( "GHOST TOO CLOSE !!!1!!1!1!" );
					break;
				}
			}
		}
		if( null == pacManBB.imminentDanger ) {
			
			pacManBB.move = MOVE.NEUTRAL;
			control.finishWithFailure();
		} else {
			
			control.finishWithSuccess();
		}
	}

}

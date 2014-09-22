package is.bthj.pacman.behaviourtree.ai.leaf;

import is.bthj.pacman.behaviourtree.PacManBlackboard;
import is.bthj.pacman.behaviourtree.ai.Blackboard;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GoAfterClosesAvailablePill extends LeafTask {
	
	private PacManBlackboard pacManBB;
	

	public GoAfterClosesAvailablePill(Blackboard blackboard, String name) {
		super(blackboard, name);

		pacManBB = (PacManBlackboard)blackboard;
	}

	@Override
	public boolean checkConditions() {

		return pacManBB.game != null && pacManBB.allAvailablePillIndices.length > 0;
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
		
//		System.out.println( "current node index: " + currentNode );
		
		int closestPillIndex = game.getClosestNodeIndexFromNodeIndex(
				currentNode, pacManBB.allAvailablePillIndices, DM.PATH);
		int closestPowerPillIndex = game.getClosestNodeIndexFromNodeIndex(
				currentNode, pacManBB.availablePowerPillIndices, DM.PATH);
		
		if( game.getShortestPathDistance(
				currentNode, closestPowerPillIndex) < pacManBB.MAX_POWER_PILL_DISTANCE ) {
			
			pacManBB.move = game.getNextMoveAwayFromTarget(
					currentNode, 
					closestPowerPillIndex, 
					DM.PATH);
			
		} else if( game.getShortestPathDistance(currentNode, closestPowerPillIndex) < 
					pacManBB.MAX_POWER_PILL_DISTANCE + pacManBB.POWER_PILL_WALK_AWAY_DISTANCE ) {
			
			pacManBB.move = MOVE.NEUTRAL;
			
//			System.out.println( "WILL GO AFTER PILL BY MOVE: " + pacManBB.move );
		} else {
			
			pacManBB.move = game.getNextMoveTowardsTarget(
					currentNode, 
					closestPillIndex, 
					DM.PATH);
		}
		
		control.finishWithSuccess();
		
/*
		if( game.getPowerPillIndex(closestPillIndex) > -1 ) {
			
			// we have a power pill, let's wait until a ghost is close enough
			// or until a our waiting time has passed
			
			// TODO: The following code is almost identical to the 
			// IsGhostTooClose.run() leaf node code, but we are also 
			// factoring in waiting time here, so not so obvious how to reuse that
			// leaf node, but maybe something to consider...
			pacManBB.imminentDanger = null;
			for( GHOST ghost : GHOST.values() ) {
				
				if( game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0 ) {
					if( game.getShortestPathDistance(
							currentNode, 
							game.getGhostCurrentNodeIndex(ghost)) < pacManBB.MIN_GHOST_DISTANCE ) {
						
						pacManBB.imminentDanger = ghost;
						System.out.println( "GHOST TOO CLOSE !!!1!!1!1!" );
						break;
					}
				}
			}
			if( null == pacManBB.imminentDanger ) {
				// no ghost close by, so let's wait at the power pill
				pacManBB.move = MOVE.NEUTRAL;
				control.finishWithFailure();
			} else {
				// a ghost is too close, so let's eat that power pill
				pacManBB.move = game.getNextMoveTowardsTarget(
						currentNode, 
						closestPillIndex, 
						DM.PATH);	
				control.finishWithSuccess();
			}
			
		} else {
			pacManBB.move = game.getNextMoveTowardsTarget(
					currentNode, 
					closestPillIndex, 
					DM.PATH);
			
			System.out.println( "WILL GO AFTER PILL BY MOVE: " + pacManBB.move );
			
			control.finishWithSuccess();
		}
*/		
		
	}


}

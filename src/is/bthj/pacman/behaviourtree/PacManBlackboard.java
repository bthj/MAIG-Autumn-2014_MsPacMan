package is.bthj.pacman.behaviourtree;

import is.bthj.pacman.behaviourtree.ai.Blackboard;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class PacManBlackboard extends Blackboard {

	public Game game;
	
	public PacManBlackboard() {
		MIN_GHOST_DISTANCE = 10;
		MAX_POWER_PILL_DISTANCE = 18;
		POWER_PILL_WALK_AWAY_DISTANCE = 12;
	}
	
	public PacManBlackboard( 
			int minGhostDistance, 
			int maxPowerPillDistance, 
			int powerPillWalkAwayDistance ) {
		
		MIN_GHOST_DISTANCE = minGhostDistance;
		MAX_POWER_PILL_DISTANCE = maxPowerPillDistance;
		POWER_PILL_WALK_AWAY_DISTANCE = powerPillWalkAwayDistance;
	}
	
	/*
	 * Thresholds affecting pacman behaviour.
	 * Those will form the gene in a Genetic Algorithm
	 */
	public int MIN_GHOST_DISTANCE = 10;
	public int MAX_POWER_PILL_DISTANCE = 18;
	public int POWER_PILL_WALK_AWAY_DISTANCE = 12;
	
	
	/*
	 * Placeholder variables for communication between nodes 
	 * in the Behaviour Tree.
	 */
	
	public GHOST imminentDanger;
	public GHOST edibleGhost;
	
	public MOVE move;
	
	public int[] availablePowerPillIndices;
	public int[] allAvailablePillIndices;
}

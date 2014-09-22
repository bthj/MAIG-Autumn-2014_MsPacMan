package is.bthj.pacman.behaviourtree;

import is.bthj.pacman.behaviourtree.ai.Blackboard;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class PacManBlackboard extends Blackboard {

	public Game game;
	
	public GHOST imminentDanger;
	public GHOST edibleGhost;
	
	public MOVE move;
	
	public int[] availablePillIndices;
}

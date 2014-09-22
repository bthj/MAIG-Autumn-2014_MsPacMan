package is.bthj.pacman.ga;

import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GeneticAlgorithmPacManController extends Controller<MOVE> {

	@Override
	public MOVE getMove(Game game, long timeDue) {
		// TODO Auto-generated method stub
		
		/**
		 * TODO:  new behaviour move:  wait at power pill until ghost are near
		 * 
		 * TODO: genes represent values on the behaviour tree blackboard:
		 * 			waiting time at power pill, distances, etc...
		 * 
		 * 			should genes define structure of behaviour tree? 
		 */
		
		return null;
	}

}

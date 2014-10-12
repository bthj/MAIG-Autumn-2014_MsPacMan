package bjrr.pacman.mcts;

import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MCTSPacmanController extends Controller<MOVE> {
	
	private UCT monteCarloTreeSearch;
	
	public MCTSPacmanController( 
			int playoutIterationLimit, boolean mctsDefaultPolicy ) {
		super();
		
		monteCarloTreeSearch = 
				new UCT( playoutIterationLimit, mctsDefaultPolicy );
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		
//		System.out.println( "about to run UCT" );

		return monteCarloTreeSearch.runUCT( game, timeDue );
	}

}

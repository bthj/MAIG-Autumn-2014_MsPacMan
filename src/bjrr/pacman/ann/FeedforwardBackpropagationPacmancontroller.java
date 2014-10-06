package bjrr.pacman.ann;

import java.io.IOException;

import bjrr.pacman.ann.dataRecording.DataTuple;
import bjrr.pacman.ann.feedforwardbackpropagation.Network;
import bjrr.pacman.ann.feedforwardbackpropagation.SerializeObject;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class FeedforwardBackpropagationPacmancontroller extends Controller<MOVE> {

	Network network;
	
	MOVE lastMove;
	
	public FeedforwardBackpropagationPacmancontroller() {
		
		lastMove = MOVE.NEUTRAL;
		
		String pacmanNeuralNeetworkFilename = 
				"pacmanANN__hiddenNeuronCount_11.save";
		
		try {
			
			network = (Network) SerializeObject.load(
					pacmanNeuralNeetworkFilename);
			
		} catch (ClassNotFoundException | IOException e) {

			e.printStackTrace();
		}
	}
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		
		DataTuple currentStateData = new DataTuple(game, MOVE.NEUTRAL, lastMove);
		
		double[] networkInput = FeedforwardBackpropagationPacmanTraining
				.getTrainingInputFromData( currentStateData );
		
		double networkOutput[] = network.computeOutputs( networkInput );
		
		MOVE move = getMoveFromNetworkOutput( networkOutput );
		
		lastMove = move;
		
		return move;
	}
	
	
	private MOVE getMoveFromNetworkOutput( double[] output ) {
		
		int highestIndex = 0;
		for( int i=0; i < output.length; i++ ) {
			
			if( output[i] > highestIndex ) {
				highestIndex = i;
			}
		}
		
		/**
		 * Here we rely on the same move order as in
		 * FeedforwardBackpropagationPacmanTraining.main(...)
		 */
		MOVE move;
		switch( highestIndex ) {
		case 0:
			move = MOVE.UP;
			break;
		case 1:
			move = MOVE.RIGHT;
			break;
		case 2:
			move = MOVE.DOWN;
			break;
		case 3:
			move = MOVE.LEFT;
			break;
		default:
			move = MOVE.NEUTRAL;
			break;
		}
		
		return move;
	}

}

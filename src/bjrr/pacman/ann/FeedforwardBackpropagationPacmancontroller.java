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
//				"pacmanANN__hiddenNeuronCount_20__learningRateFixedAt0dot1__hiddenNeuronsLessThanTwiceInputSize.save";
//				"pacmanANN__hiddenNeuronCount_12__learningRateDownEvery100thEpoch.save";
//				"pacmanANN__hiddenNeuronCount_12__learningRateFixedAt0dot1.save";
//				"pacmanANN__hiddenNeuronCount_12__learningRateFixedAt0dot1__alternativeInputSet.save";
//				"pacmanANN__hiddenNeuronCount_20__hiddenNeuronsLessThanTwiceInputSize__alternativeInputSet.save";
				"pacmanANN__hiddenNeuronCount_20__learningRateFixedAt0dot1__hiddenNeuronsLessThanTwiceInputSize.save";
		
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
		
//		System.out.println( "Chosen move: " + move );
		
		lastMove = move;
		
		return move;
	}
	
	
	private MOVE getMoveFromNetworkOutput( double[] output ) {
		
		int highestIndex = 0;
		double highestOutputValue = - Double.MAX_VALUE;
		for( int i=0; i < output.length; i++ ) {
//			System.out.println( "one output: " + output[i] );
			if( output[i] > highestOutputValue ) {
				highestOutputValue = output[i];
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

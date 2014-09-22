package is.bthj.pacman.behaviourtree.ai.leaf;

import is.bthj.pacman.behaviourtree.PacManBlackboard;
import is.bthj.pacman.behaviourtree.ai.Blackboard;

import java.util.ArrayList;

import pacman.game.Game;

public class FindAvailablePills extends LeafTask {
	
	private PacManBlackboard pacManBB;
	

	public FindAvailablePills(Blackboard blackboard, String name) {
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
		
		int[] pills = game.getPillIndices();
		int[] powerPills = game.getPowerPillIndices();		
		
		ArrayList<Integer> targets = new ArrayList<Integer>();
		
		for( int i=0; i < pills.length; i++ )			//check which pills are available			
			if( game.isPillStillAvailable(i) )
				targets.add( pills[i] );
		
		for( int i=0; i < powerPills.length; i++ )		//check with power pills are available
			if( game.isPowerPillStillAvailable(i) )
				targets.add( powerPills[i] );				
		
		int[] targetsArray=new int[targets.size()];		//convert from ArrayList to array
		
		for( int i=0; i<targetsArray.length; i++ )
			targetsArray[i]=targets.get(i);
		
		if( targetsArray.length > 0 ) {
			
			System.out.println( "FOUND PILLS TO GO AFTER" );
		
			pacManBB.availablePillIndices = targetsArray;
			control.finishWithSuccess();
			
		} else {
			
			System.out.println( "FOUND no pills TO GO AFTER" );
			
			control.finishWithFailure();
		}
		
	}

}

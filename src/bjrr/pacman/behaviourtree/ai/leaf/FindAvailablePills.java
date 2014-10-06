package bjrr.pacman.behaviourtree.ai.leaf;

import java.util.ArrayList;

import bjrr.pacman.behaviourtree.PacManBlackboard;
import bjrr.pacman.behaviourtree.ai.Blackboard;
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
		
		ArrayList<Integer> powerTargets = new ArrayList<Integer>();
		ArrayList<Integer> targets = new ArrayList<Integer>();
		
		for( int i=0; i < pills.length; i++ )			//check which pills are available			
			if( game.isPillStillAvailable(i) )
				targets.add( pills[i] );
		
		for( int i=0; i < powerPills.length; i++ )		//check with power pills are available
			if( game.isPowerPillStillAvailable(i) ) {
				targets.add( powerPills[i] );
				powerTargets.add( powerPills[i] );
			}
								
		
		int[] targetsArray=new int[targets.size()];		//convert from ArrayList to array
		int[] powerTargetsArray = new int[powerTargets.size()];
		
		for( int i=0; i<targetsArray.length; i++ )
			targetsArray[i]=targets.get(i);
		
		for( int i=0; i<powerTargetsArray.length; i++ ) 
			powerTargetsArray[i] = powerTargets.get(i);
		
		if( targetsArray.length > 0 ) {
			
//			System.out.println( "FOUND PILLS TO GO AFTER" );
		
			pacManBB.allAvailablePillIndices = targetsArray;
			pacManBB.availablePowerPillIndices = powerTargetsArray;
			
			control.finishWithSuccess();
			
		} else {
			
//			System.out.println( "FOUND no pills TO GO AFTER" );
			
			control.finishWithFailure();
		}
		
	}

}

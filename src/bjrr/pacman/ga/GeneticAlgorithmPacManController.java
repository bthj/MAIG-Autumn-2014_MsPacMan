package bjrr.pacman.ga;

import bjrr.pacman.behaviourtree.PacManBlackboard;
import bjrr.pacman.behaviourtree.ai.composite.ParentTaskController;
import bjrr.pacman.behaviourtree.ai.composite.Priority;
import bjrr.pacman.behaviourtree.ai.composite.Sequence;
import bjrr.pacman.behaviourtree.ai.leaf.FindAvailablePills;
import bjrr.pacman.behaviourtree.ai.leaf.FindClosestEdibleGhost;
import bjrr.pacman.behaviourtree.ai.leaf.GoAfterClosesAvailablePill;
import bjrr.pacman.behaviourtree.ai.leaf.GoAfterEdibleGhost;
import bjrr.pacman.behaviourtree.ai.leaf.IsGhostTooClose;
import bjrr.pacman.behaviourtree.ai.leaf.RunAwayFromDanger;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GeneticAlgorithmPacManController extends Controller<MOVE> {
	
	private PacManBlackboard pacManBB;
	private Priority rootPrioritySelector;
	
	
	
	public GeneticAlgorithmPacManController() {
		
		super();
		
		PacManGeneForBehaviourTree gene = new PacManGeneForBehaviourTree();
		// set gene values obtained from evolution:
		gene.setMinGhostDistance(5);
		gene.setMaxPowerPillDistance(15);
		gene.setPowerPillWalkAwayDistance(60);
		
		initializeBehaviourTreeFromGene(gene);
	}
	
	public GeneticAlgorithmPacManController( PacManGeneForBehaviourTree gene ) {
		
		super();
		
		initializeBehaviourTreeFromGene(gene);
	}

	private void initializeBehaviourTreeFromGene(PacManGeneForBehaviourTree gene) {
		pacManBB = new PacManBlackboard( 
				gene.getMinGhostDistance(), 
				gene.getMaxPowerPillDistance(), 
				gene.getPowerPillWalkAwayDistance() );
		
		Sequence avoidanceSequence = new Sequence(pacManBB, "Sequence: Avoidance");
		((ParentTaskController)avoidanceSequence.getControl()).add( 
				new IsGhostTooClose(pacManBB, "Task: Is ghost too close") );
		((ParentTaskController)avoidanceSequence.getControl()).add( 
				new RunAwayFromDanger(pacManBB, "Task: Run away from danger") );
		
		Sequence ghostEatingSequence = new Sequence(pacManBB, "Sequence: Eat ghosts");
		((ParentTaskController)ghostEatingSequence.getControl()).add( 
				new FindClosestEdibleGhost(pacManBB, "Task: Find closest edible ghost") );
		((ParentTaskController)ghostEatingSequence.getControl()).add( 
				new GoAfterEdibleGhost(pacManBB, "Task: Go after closest edible ghost") );
		
		Sequence pillEatingSequence = new Sequence(pacManBB, "Sequence: Eat pills");
		((ParentTaskController)pillEatingSequence.getControl()).add( 
				new FindAvailablePills(pacManBB, "Task: Find available pills") );
		((ParentTaskController)pillEatingSequence.getControl()).add( 
				new GoAfterClosesAvailablePill(pacManBB, "Task: Go after closest available pill") );
		
		rootPrioritySelector = new Priority(pacManBB, "rootPriorityComposite");
		((ParentTaskController)rootPrioritySelector.getControl()).add( avoidanceSequence );
		((ParentTaskController)rootPrioritySelector.getControl()).add( ghostEatingSequence );
		((ParentTaskController)rootPrioritySelector.getControl()).add( pillEatingSequence );
	
		rootPrioritySelector.getControl().safeStart();
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {

		pacManBB.game = game;
		
		do {
			rootPrioritySelector.run();
		} while( !rootPrioritySelector.getControl().finished() );
		
		rootPrioritySelector.getControl().reset();
		
		return pacManBB.move;
	}

}

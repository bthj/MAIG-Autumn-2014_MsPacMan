package bjrr.pacman.mcts;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

import bjrr.pacman.ga.GeneticAlgorithmPacManController;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

/**
 * Monte Carlo Tree Search algorithm implementing UCT method
 * Run main method to test
 * 
 * @author D.Vitonis
 * @modified A. Hartzen
 * 
 * 
 * @modified Björn Þór Jónsson - bthj (AKA bjrr)
 * 			-> adapting the tutorial code for PacMan.
 */
public class UCT {
	
	private Random random = new Random();
	
	/*
	 * Ghost controller to get ghost moves when advancing the game.
	 */
	private StarterGhosts ghostCtrl;
	
	/*
	 * Controller based on a Behaviour Tree with attributes 
	 * controlling strategic movements whose values were 
	 * evolved with a Genetic Algorithm. 
	 */
	private GeneticAlgorithmPacManController geneticAlgorithmPacManController;
	
	/*
	 * Determines whether the default policy of random PacMan moves
	 * is used during playouts or whether the GeneticAlgorithmPacManController
	 * is used for the simulation.
	 */
	private boolean useDefaultPolicy;
	
	/*
	 * rootNode is the starting point of the present state
	 */
	Node rootNode;
	
	/*
	 * currentNode refers to the node we work at every step
	 */
	Node currentNode;
	
	/*
	 * Exploration coefficient
	 */
	private float C = (float) (1.0/Math.sqrt(2));
	
	/*
	 * Computational limit
	 */
	protected final int maxIterations = 50;
	
	/*
	 * How many iterations of the playout stretegy (DefaultPolicy)
	 * should be performed.
	 * 0 is taken to mean no limit.
	 */
	private int playoutIterationsLimit;
	
	
	/**
	 * Constructor
	 * Initialize the maze game
	 */
	public UCT( int playoutIterationsLimit, boolean useDefaultPolicy ) {
		
		ghostCtrl = new StarterGhosts();
		geneticAlgorithmPacManController = 
				new GeneticAlgorithmPacManController();
		this.playoutIterationsLimit = playoutIterationsLimit;
		this.useDefaultPolicy = useDefaultPolicy;
	}
	
	/**
	 * run the UCT search and find the optimal action for the root node state
	 * @return
	 * @throws InterruptedException
	 */
	public MOVE runUCT( Game game, long timeDue ) {
	
        /*
         * Create root node with the present state
         */
        rootNode = new Node( game.copy() );
        
        /*
         * Apply UCT search inside computational budget limit (default=100 iterations) 
         */

        int iterations = 0;
        
        /*
         * We'll stop iterating when the estimated execution time
         * of the next iteration will go over the time limit.
         * We'll add one millisecond for any possible overhead
         */
        while( System.currentTimeMillis() + 1 < timeDue ) {
//        while(!Terminate(iterations)){

        	/*
        	 * UCT algorithm
        	 */
        	
        	// Selection
//        	System.out.println("Selection");
        	TreePolicy();
        	
        	// Expansion
//        	System.out.println("Expansion");
        	if( ! FullyExpanded(currentNode) ) {

        		Expand();
        	}
        	
        	// Simulation
//        	System.out.println("Simulation");
        	float reward;
        	if( this.useDefaultPolicy ) {
        		reward = DefaultPolicy();        		
        	} else {
        		reward = GeneticBehaviourTreePolicy(timeDue);        		
        	}
        	
        	// Backpropagation
//        	System.out.println("Backpropagation");
        	Backpropagate(reward);
        	
        	iterations++;
        }
        
        
        /*
         * Get the action that directs to the best node
         */
        currentNode = rootNode;
        
        //rootNode is the one we are working with 
        //and we apply the exploitation of it to find the child with the highest average reward
        MOVE bestAction = MOVE.NEUTRAL;
        // bthj:
        float highestReward = -Float.MAX_VALUE;
        
        for( Node oneChild : currentNode.children ) {
//        	System.out.println( "one child reward: " + oneChild.reward );
        	if( oneChild.reward > highestReward ) {
        		highestReward = oneChild.reward;
        		bestAction = oneChild.parentAction;
        	}
        }
        
//        System.out.println( "Iteration count " + iterations + ", bestAction: " + bestAction );
        
        return bestAction;
	}
	
	/**
	 * Expand the nonterminal nodes with one available child. 
	 * Chose a node to expand with BestChild(C) method
	 */
	private void TreePolicy() {
		currentNode = rootNode;
		
		while( FullyExpanded(currentNode) && currentNode.children.size() > 0 ) {
    		// node is fully expanded and non-terminal; has children
			
    		BestChild( C );
    	}
	}
	
	/**
	 * Simulation of the game. Choose random actions up until the game is over 
	 * TODO: depth limit
	 * @return reward Total score from the Game framework at the end state
	 */
	private float DefaultPolicy() {
		
		Game gameStateCopy = currentNode.state.copy();
		
//		long startTime = System.currentTimeMillis();
		int playoutIteration = 0;
		while( !TerminalState(gameStateCopy, playoutIteration) ) {
			playoutIteration++;
			
			MOVE pacmanMove = RandomAction(gameStateCopy);
			EnumMap<GHOST,MOVE> ghostMoves = RandomGhostAction(gameStateCopy);
			gameStateCopy.advanceGame(pacmanMove, ghostMoves);
		}
//		System.out.println( "One DefaultPolicy took " + 
//				(System.currentTimeMillis() - startTime) + " milliseconds." );
		
		return gameStateCopy.getScore();
	}
	
	private float GeneticBehaviourTreePolicy( long timeDue ) {
		
		Game gameStateCopy = currentNode.state.copy();
		
		int playoutIteration = 0;
		while( !TerminalState(gameStateCopy, playoutIteration) ) {
			playoutIteration++;
			
			MOVE pacmanMove = geneticAlgorithmPacManController.getMove(
					gameStateCopy, timeDue );
			EnumMap<GHOST,MOVE> ghostMoves = RandomGhostAction(gameStateCopy);
			gameStateCopy.advanceGame(pacmanMove, ghostMoves);
		}
		return gameStateCopy.getScore();
	}

	/**
	 * Assign the received reward to every parent of the parent up to the rootNode
	 * Increase the visited count of every node included in backpropagation
	 * @param reward
	 */
	private void Backpropagate( float reward ) {
//		System.out.println( "reward: " + reward );
		while( currentNode != null ) {
			
			currentNode.update(reward);
			currentNode = currentNode.parent;
		}
	}
	
	/**
	 * Check if the node is fully expanded
	 * @param nt
	 * @return
	 */
	private boolean FullyExpanded(Node nt) {
		// bthj: 
		return UntriedAction(nt) == null;
	}

	/**
	 * Check if the state is the end of the game
	 * @param state
	 * @return
	 */
	private boolean TerminalState(Game gameState, int playoutIteration) {
		
		return gameState.gameOver() || 
				(this.playoutIterationsLimit > 0 && 
						playoutIteration >= this.playoutIterationsLimit);
	}

	/**
	 * Choose the best child according to the UCT value
	 * Assign it as a currentNode
	 * @param c Exploration coefficient
	 */
	private void BestChild(float c) {
		Node nt = currentNode;
		
		// bthj:
		int bestChildIndex = -1;
		float bestChildValue = - Float.MAX_VALUE;
		for( int i=0; i < nt.children.size(); i++ ) {
			
			float oneChildUCTValue = UCTvalue( nt.children.get(i), c );
			if( oneChildUCTValue > bestChildValue ) {
				bestChildValue = oneChildUCTValue;
				bestChildIndex = i;
			}
		}
		
		Node bestChild = nt.children.get(bestChildIndex);

		currentNode = bestChild;
	}

	/**
	 * Calculate UCT value for the best child choosing
	 * 
	 * 
	 * @param n child node of currentNode
	 * @param c Exploration coefficient
	 * @return
	 */
	private float UCTvalue(Node n, float c) {
//		bthj:
//		Referring to http://mcts.ai/code/java.html for this calculation
		
		/*
		 * Referring to http://mcts.ai/code/java.html for this calculation
		 * we can calculate the UCT value like so
		 */
//		float uctValue = (float) (n.reward / (n.timesvisited + c) + 
//				Math.sqrt( Math.log(n.timesvisited+1) / (n.timesvisited + c)) + 
//				random.nextDouble() * c); 
		/*
		 * Or referring to the slides 
		 * v_i + C * sqrt( 2 * ln( N(v) ) / N(v') )
		 * we can do the calculation like:
		 */
		float uctValue = (float) (n.reward / n.timesvisited + 
				c * Math.sqrt(2 * Math.log(n.timesvisited+1) / n.timesvisited));
		
		if( Float.isNaN(uctValue) ) uctValue = 0;
		
		return uctValue;
	}

	/**
	 * Expand the current node by adding new child to the currentNode
	 */
	private void Expand() {		
		/*
		 * Choose untried action
		 */
		MOVE action = UntriedAction(currentNode);
		
		/*
		 * Create a child, set its fields and add it to currentNode.children
		 */
		Game gameCopy = currentNode.state.copy();
		// let's just allow the ghost controller to use 10 milliseconds.
		gameCopy.advanceGame(
				action, ghostCtrl.getMove(gameCopy, 10) );
		Node child = new Node( gameCopy );
		
		child.parentAction = action;
		child.parent = currentNode;
		currentNode.children.add(child);
		
		currentNode = child;
	}

	/**
	 * Returns the first untried action of the node
	 * @param n
	 * @return
	 */
	private MOVE UntriedAction(Node n) {
		moveOptions:
		for( MOVE oneMove : MOVE.values() ) {
			if( oneMove != MOVE.NEUTRAL ) {
				
				for( int i=0; i < n.children.size(); i++ ) {
					
					if( n.children.get(i).parentAction == oneMove ) {
						continue moveOptions;
					}
				}
				if( isValidMove(oneMove, n) ) {
					return oneMove;
				}
			}
		}
		return null;
	}
	private boolean isValidMove( MOVE move, Node n ) {
		boolean isValid = false;
		MOVE[] possibleMoves = n.state.getPossibleMoves(
				n.state.getPacmanCurrentNodeIndex(),
				n.state.getPacmanLastMoveMade() );
		for( MOVE onePossibleMove : possibleMoves ) {
			
			if( move == onePossibleMove ) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}


	/**
	 * Used in game simulation to pick random action for the agent
	 * @param state st
	 * @return action
	 */
	private MOVE RandomAction( Game gameState ) {
		
		MOVE[] possibleMoves = gameState.getPossibleMoves( 
				gameState.getPacmanCurrentNodeIndex() );
		
		int moveIndex = random.nextInt( possibleMoves.length );
				
		return possibleMoves[ moveIndex ];
	}
	
	/**
	 * Used in game simulation to pick random action for the ghost
	 * @param state st
	 * @return action
	 */
	private EnumMap<GHOST,MOVE> RandomGhostAction( Game gameState ) {
		
		/*
		 * We'll use the StarterGhost controller here,
		 * instead of doing something random for each ghost manually
		 */
		return ghostCtrl.getMove(gameState, 10);
	}
	
	/**
	 * UCT maze solving test
	 * @param args
	 * @throws InterruptedException 
	 */
//	public static void main(String[] args) throws InterruptedException {
//
//		UCT uct = new UCT();
//		
//		while(true){
//            // PRINT MAP
//			uct.maze.printMap();
//            // CHECK IF WON OR LOST, THEN RESET
//            if(uct.maze.isGoalReached()){
//                System.out.println("GOAL REACHED");
//                uct.maze.resetMaze();
//                return;
//            }
//            
//            if(uct.maze.isAvatarDead(uct.maze.map)){
//                System.out.println("AVATAR DEAD");
//                uct.maze.resetMaze();
//                return;
//            }
//            
//            //FIND THE OPTIMAL ACTION VIA UTC
//            int bestAction = uct.runUCT();
//            
//            //ADVANCE THE GAME WITH MOVES OF AGENT AND GHOST
//            uct.maze.goToNextState(bestAction);
//            int bestGhostAction = uct.random.nextInt(4);
//            while (!uct.maze.isValidGhostMove(bestGhostAction)){
//            	bestGhostAction = uct.random.nextInt(4);
//            }
//            uct.maze.goToNextGhostState(bestGhostAction);
//            
//            //TRACK THE GAME VISUALY
//            Thread.sleep(1000);
//        }
//		
//	}

}


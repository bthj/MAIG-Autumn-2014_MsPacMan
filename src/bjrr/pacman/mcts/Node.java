package bjrr.pacman.mcts;

import java.util.ArrayList;
import java.util.List;

import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Class to store node information, e.g.
 * state, children, parent, accumulative reward, visited times
 * @author dariusv
 * @modified A. Hartzen
 *
 * @modified Björn Þór Jónsson - bthj (AKA bjrr)
 */
public class Node{
	
	public Game state;
	public List<Node> children = new ArrayList<Node>();
	public Node parent = null;
	public MOVE parentAction = MOVE.NEUTRAL;
	public float reward =0;
	public int timesvisited = 0;
	
	
	Node(Game state){
		this.state = state;
	}
	
	
	// bthj:
	public void update( float reward ) {
		
		timesvisited++;
		this.reward += reward;
	}
}

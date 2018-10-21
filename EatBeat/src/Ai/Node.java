package Ai;

import java.util.ArrayList;

import android.graphics.Point;
// enum Will_Player{P1, P2};
public class Node {
	public String[][] State;
	public long Level;
	public ArrayList<Node> Children;
	public Node Parent;
	public long Count;
	public long Cost;
	public String Will_Play_On;
	public Point Target_Position;
	public Point Current_Position;
	public Node(String[][] state){
		this.State = state;
		this.Children = new ArrayList<Node>();
	}
}

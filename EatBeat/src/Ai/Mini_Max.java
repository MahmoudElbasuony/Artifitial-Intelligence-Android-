package Ai;



import java.util.ArrayList;
import android.graphics.Point;


public class Mini_Max {
	
	
	public Node Will_Play_On_Node;
	public String Computer;
	public String Person;
	public Node Root;
	public long Max_Level;
	public long Permit_Count = 7; //to prevent infinity 
	public long level_limit ; // 2 without root 
	public ArrayList<Node> Leaves = new ArrayList<Node>();
	public Mini_Max(String[][] initialState, String comp,String pers, String turn){
		this.Root = new Node(initialState);
		this.Root.Level=0;
		this.Computer = comp;
		this.Person = pers;
		this.Root.Will_Play_On = turn;
	    Will_Play_On_Node = this.Root;
	}
	public void Start_Build()
	{
		 BuildTree(Root.Will_Play_On,Root);
		 for (Node node : Leaves) {
			 Assign_Leaf_Cost(node);
		 }
		 Assign_Parents_Cost();
	}
	public void Assign_Parents_Cost()
	{
		long max_level = Max_Level;
		while (max_level>=1) 
		{
			
			ArrayList<Node> matched_level = new ArrayList<Node>();
			for (Node node : Leaves) {
				if(node.Level==max_level)
				  matched_level.add(node);
			} 
			
			for (int i = 0; i < matched_level.size(); i++) {
				Node Parent =  matched_level.get(i).Parent;
				if(Parent.Will_Play_On.equals(Person)) // max
				{
					Node Max = Parent.Children.get(0);
					for (Node child : Parent.Children) {
						if(child.Cost>Max.Cost)
						{
							Max = child;
						}
					}
					Parent.Cost = Max.Cost;
					Leaves.removeAll(Parent.Children);
					matched_level.removeAll(Parent.Children);
					Leaves.add(Parent);
					
					
				}
				else if(Parent.Will_Play_On.equals(Computer)) // min
				{
					
					Node Min = Parent.Children.get(0);
					for (Node child : Parent.Children) {
						if(child.Cost<Min.Cost)
						{
							Min = child;
						}
					}
					Parent.Cost = Min.Cost;
					Leaves.removeAll(Parent.Children);
					matched_level.removeAll(Parent.Children);
					Leaves.add(Parent);
					
				}			
				
			}
			max_level--;
		}
	}
	public void BuildTree(String turn,Node Root){
		this.level_limit = 2; // without root   / 3 levels  for performance 
		this.Root =  Root;
		this.Root.Level  = 0;
		Node node = Root;
		
		for(int i =0;i<node.State.length; i++)
			for(int j= 0; j<node.State.length; j++)
			{
				if(node.State[i][j].equals(turn))
				{
					Create_Children(node, new Point(i,j), get_Available_Positions(new Point(i,j), node) );
				}
			}
		this.level_limit-=1;
		ArrayList<Node> children = Root.Children; 
		if(children.size()!=0)
		{
			
			while (this.level_limit>=1&&children.size()!=0) { // loop until reach 1
				
				ArrayList<Node>   created = new ArrayList<Node>();   // combination array 
				for (Node child : children) 
				{
					int count=0;
					for(int i =0;i<child.State.length; i++)
						for(int j= 0; j<child.State.length; j++)
						{
							if(child.State[i][j].equals(child.Will_Play_On))
							{
								count++;
								Create_Children(child, new Point(i,j), get_Available_Positions(new Point(i,j), child) );
							}
						}
					if(count==0) Leaves.add(child); // for any level leaf node 
					created.addAll(child.Children);
				}
				children = created;
				
				this.level_limit--;
				
			}
			Leaves.addAll(children);// for last level
	
		}
		
	}
	public ArrayList<Point> get_Available_Positions(Point currentPoint, Node Current){
		ArrayList<Point> Available_Points = new ArrayList<Point>();
         
    	// select around the given button
    	int row = currentPoint.x;
    	int col = currentPoint.y;
    	
    	//     [[Around]<-[Next]<-(([Current]))->[Next]->[Around]]
    	int next[] = { row-1,col+1,row+1,col-1   }; // top 0 - right 1  - bottom 2  - left 3
    	int around[] = { row-2,col+2,row+2,col-2  }; // top 0  - right 1  - bottom 2  - left 3 
    	for (int i = 0; i < 4; i++) 
    	{
    		if(next[i]>=0&&next[i]<=Current.State.length-1)  // next if no negative or bigger length
        	{
    			if(i==0||i==2)// top || bottom
    			{
    				if((Current.State[next[i]][col]).equals("")&&!(Current.State[next[i]][col]).equals("N"))
    				{
    					Available_Points.add(new Point(next[i], col));
    				}
    			}
        	   else if(i==1||i==3) // right || left
        	   {
        		   if(Current.State[row][next[i]].equals("")&&!Current.State[row][next[i]].equals("N"))
        		   {
        			   Available_Points.add(new Point(row, next[i]));
        		   }
        	   }
        	}
    		if(around[i]>=0&&around[i]<=Current.State.length-1) // around if no negative or bigger length
    		{
    			if(i==0||i==2)// top || bottom
    			{
    				if((Current.State[around[i]][col]).equals("")&&!(Current.State[around[i]][col]).equals("N"))
    				{
    					Available_Points.add(new Point(around[i], col));
    				}
    			}
        	   else if(i==1||i==3) // right || left
        	   {
        		   if(Current.State[row][around[i]].equals("")&&!Current.State[row][around[i]].equals("N"))
        		   {
        			   Available_Points.add(new Point(row,around[i]));
        		   }
        	   }
    			
    		}
    	
		}
  
		return Available_Points;
	}
	public void Create_Children(Node node,Point current_position,ArrayList<Point> available_points)
	{
		
          
		for (int i = 0; i < available_points.size(); i++) {
			Node child = Perform_Intended_Action(available_points.get(i),current_position,node);
			if(child!=null)
			{
				child.Parent = node;
				child.Will_Play_On = child.Parent.Will_Play_On.equals("A")?"B":"A";
				child.Level=node.Level+1;
				if(Max_Level<child.Level) Max_Level = child.Level; // to get the max level 

				if(Check_Similar_Count(child))
				{
					if(child.Parent.Count<Permit_Count)
					{

						child.Count= child.Parent.Count+1;
						node.Children.add(child);


					}
					else return;
				}
				else 
				{

					node.Children.add(child);
				}
			}
		}
		if(node.Children.size()==0&&!Leaves.contains(node))
		{
			Leaves.add(node);
		}
		if(node.Children.size()!=0)
		{
			Leaves.remove(node);
		}

	}
	public String[][] Copy_Matrix(Node node)
	{
		String[][] new_matrix = new String[node.State.length][node.State.length];
		for (int i = 0; i < new_matrix.length; i++) {
			for (int j = 0; j < new_matrix.length; j++) {
				new_matrix[i][j] = node.State[i][j];
			}
		}
		return new_matrix;
	}
	public boolean Check_Similar_Count(Node node)
	{
		Node parent = node.Parent;
		long count1=0; // child A
		int count2=0; // parent   B
		int count11=0; // parent A
		int count22=0; // child   B
		for (int i = 0; i < node.State.length; i++) {
			for (int j = 0; j < node.State.length; j++) {
				if(node.State[i][j].equals(Computer)) count1++;
				if(parent.State[i][j].equals(Person)) count2++;
				if(node.State[i][j].equals(Person)) count11++;
				if(parent.State[i][j].equals(Computer)) count22++;
			}
		}
		if(count1==count11&&count2==count22) return true;
		else return false;
	}
	public Node Perform_Intended_Action(Point Target_Position,Point Current_Position,Node node)
	{
		Node child;
		// if move button
		if(Target_Position.y+2<=node.State.length-1&&Target_Position.y+2==Current_Position.y&&Target_Position.x==Current_Position.x) //move left 
		{
			String[][] state = Copy_Matrix(node);
			Move(Target_Position, Current_Position, state);
			child = new Node(state);
			child.Target_Position = Target_Position;
			child.Current_Position = Current_Position;
			return child;
		}
		else if(Target_Position.y-2>=0&&Target_Position.y-2==Current_Position.y&&Target_Position.x==Current_Position.x) // move right
		{
			String[][] state = Copy_Matrix(node);
			Move(Target_Position, Current_Position, state);
			child = new Node(state);
			child.Target_Position = Target_Position;
			child.Current_Position = Current_Position;
			return child;
		}
		else if(Target_Position.x+2<=node.State.length-1&&Target_Position.y==Current_Position.y&&Target_Position.x+2==Current_Position.x) // top
		{
			String[][] state = Copy_Matrix(node);
			Move(Target_Position, Current_Position, state);
			child = new Node(state);
			child.Target_Position = Target_Position;
			child.Current_Position = Current_Position;
			return child;
		}
		else if(Target_Position.y==Current_Position.y&&Target_Position.x-2==Current_Position.x&&Target_Position.x-2>=0)// bottom
		{ 
			String[][] state = Copy_Matrix(node);
			Move(Target_Position, Current_Position, state);
			child = new Node(state);
			child.Target_Position = Target_Position;
			child.Current_Position = Current_Position;
			return child;
		}
		else // if create buttons 
		{

			if(Target_Position.y+1<=node.State.length-1&&Target_Position.y+1==Current_Position.y&&Target_Position.x==Current_Position.x) // left 
			{
				String[][] state = Copy_Matrix(node);
				Create(Target_Position, Current_Position, state);
				child = new Node(state);
				child.Target_Position = Target_Position;
				child.Current_Position = Current_Position;
				return child;

			}
			else if(Target_Position.y-1>=0&&Target_Position.y-1==Current_Position.y&&Target_Position.x==Current_Position.x) //  right
			{
				String[][] state = Copy_Matrix(node);
				Create(Target_Position, Current_Position, state);
				child = new Node(state);
				child.Target_Position = Target_Position;
				child.Current_Position = Current_Position;
				return child;

			}
			else if(Target_Position.x+1<=node.State.length-1&&Target_Position.y==Current_Position.y&&Target_Position.x+1==Current_Position.x) // top
			{
				String[][] state = Copy_Matrix(node);
				Create(Target_Position, Current_Position, state);
				child = new Node(state);
				child.Target_Position = Target_Position;
				child.Current_Position = Current_Position;
				return child;

			}
			else if(Target_Position.x-1>=0&&Target_Position.y==Current_Position.y&&Target_Position.x-1==Current_Position.x)// bottom
			{
				String[][] state = Copy_Matrix(node);
				Create(Target_Position, Current_Position, state);
				child = new Node(state);
				child.Target_Position = Target_Position;
				child.Current_Position = Current_Position;
				return child;
			}
		}
    	return null;
	}
	public boolean Evolve_Play_Pointer(String[][] state , Point Target , Point Current)
	{
		boolean evoloved = false;
		
		if(Will_Play_On_Node.Children.size()==0)
		{
			BuildTree(Will_Play_On_Node.Will_Play_On, Will_Play_On_Node);
		}
		
		for (Node node : Will_Play_On_Node.Children) {
			boolean res = Check_Similar_Array(state, node.State);
			if(res&&Check_Similar_Point(Target, node.Target_Position)&&
					Check_Similar_Point(Current, node.Current_Position))
			{
				Will_Play_On_Node = node;
				evoloved = true;
				break;
			}
		}
		
		return evoloved;
	}
	public Node Get_Best_Action()
	{

		Node Best_Node = null ;

		if(Will_Play_On_Node.Children.size()==0)
		{
			BuildTree(Will_Play_On_Node.Will_Play_On, Will_Play_On_Node);
		}
		
		// best choose
		if(Will_Play_On_Node!=null&&Will_Play_On_Node.Children.size()!=0)
		{
			for (Node node : Will_Play_On_Node.Children) {
				if(node.Cost==Will_Play_On_Node.Cost)
				{
					Best_Node = node;
					Will_Play_On_Node = node;
					break;
				}
			}
		}
		
		return Best_Node;
	}
	public boolean Check_Similar_Point(Point x,Point y)
	{
		boolean similar = false;
		if(x.x==y.x&&x.y==y.y) return true;
		return similar;
	}
	public boolean Check_Similar_Array(String[][] state1 , String[][] state2)
	{
		boolean similar = true;
		for (int i = 0; i < state2.length; i++) {
			for (int j = 0; j < state2.length; j++) {
				if(!state1[i][j].equals(state2[i][j])) {similar= false; break;}
			}
			if(!similar) break;
		}
		return similar;
	}
	public void Move(Point Target_Position,Point Current_Position,String[][] child_state)
    {
    	// you previously swap two button from ui now we need to swap them from matrix
    	String Temp = child_state[Target_Position.x][Target_Position.y];
    	child_state[Target_Position.x][Target_Position.y] = child_state[Current_Position.x][Current_Position.y];
    	child_state[Current_Position.x][Current_Position.y] = Temp;
    	Change(child_state[Target_Position.x][Target_Position.y],Target_Position,child_state);
    	
    }
	public void Create(Point Target_Position,Point Current_Position,String[][] child_state)
    {
    	child_state[Target_Position.x][Target_Position.y]= child_state[Current_Position.x][Current_Position.y];
    	Change(child_state[Current_Position.x][Current_Position.y],Target_Position,child_state);
    }
    public void Change(String current, Point target_location,String[][] State){
    	
    	//select around this button
    	int r = target_location.x;
    	int c = target_location.y;
    	int next_buttons[] = {r-1,c+1,r+1,c-1}; // top 0 , right 1 , bottom 2 , left 3

    	for(int i=0;i<4;i++){
    		if(next_buttons[i] >= 0 && next_buttons[i] <= State.length-1){
    			if(i == 0 || i == 2){ // top , bottom
    			    
    				if(!State[next_buttons[i]][c].equals("")&&!State[next_buttons[i]][c].equals("N")){
    					if(! State[next_buttons[i]][c].equals(current)){
    						 State[next_buttons[i]][c]=current;
                        
     					}
    				}
    			}
    			else if(i == 1 || i == 3){ // right , left
    				if(! State[r][next_buttons[i]].equals("")&&! State[r][next_buttons[i]].equals("N")){
    					if(! State[r][next_buttons[i]].equals(current)){
    						 State[r][next_buttons[i]]=current;
     					}
    					
    				}
    				
    			}
    		}
    	
    	}
    	
    	
    }
	public void Assign_Leaf_Cost(Node node)
	{
		int count1=0; // computer
		int count2=0; // person
		for (int i = 0; i < node.State.length; i++) {
			for (int j = 0; j < node.State.length; j++) {
				if(node.State[i][j].equals(Computer)) count1++;
				else if(node.State[i][j].equals(Person)) count2++;
			}
		}
		node.Cost = count2-count1;
	}
    
    
}

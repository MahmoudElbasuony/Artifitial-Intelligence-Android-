package EBluetooth;

import java.io.Serializable;
import java.util.ArrayList;

public class EMessage  implements Serializable
{
	private static final long serialVersionUID = 1L;

	
	public int Target_X ;
	public int Target_Y ;
	
	public int Current_X ;
	public int Current_Y ;

	
	public String Perform_Action;

	public String Level;
	public EMessage(int Target_X ,int Target_Y, int Current_X ,int Current_Y,String Turn) {
		this.Current_X = Current_X;
		this.Current_Y = Current_Y;
		this.Target_X = Target_X;
		this.Target_Y = Target_Y;	
	}
	public EMessage(String Perform_Action,ArrayList<Object> params) {
		this.Perform_Action = Perform_Action;
	}
	public EMessage(String Level)
	{
		this.Level = Level;
	}
	
}

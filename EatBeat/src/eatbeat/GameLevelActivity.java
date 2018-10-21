package eatbeat;

import java.util.ArrayList;
import java.util.Locale;

import com.example.eatbeat.R;

import Ai.Mini_Max;
import Ai.Node;
import EBluetooth.EBBluetoothManager;
import EBluetooth.ECommunicationManager;
import EBluetooth.EMessage;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameLevelActivity extends Activity implements OnClickListener 
{
	// variables 

	MediaPlayer M_create_sound,M_move_sound,m_music;
	RelativeLayout Container ; // this is the container that holds buttons
	int Square_Length; // the square edge used to animate by square length to left or to right
	Drawable Default_Color;  // default background to set for new created button
	public static Button[][] Buttons; // array of buttons 
	public static final int Row_Buttons_Number = 7; // number of rows and columns 
	ArrayList<Button> Selected; // the current selected button around the current
	Button Current; // the current button you want to move or copy
	Button Target; // the button you need to move or create to
	public static String Player1="P1"; // Player1 name     // by default is computer A
	public static String Player2="P2"; // player 2 name 
	public static String Current_Player; // current player 
	TextView P1_Score;
	TextView P2_Score;
	ImageView Player1_Logo;
	ImageView Player2_Logo;
	ArrayList<Button>Player1_Score;
	ArrayList<Button>Player2_Score;
	TextToSpeech speaker;
	boolean animating ;
	boolean still_opened=true;
	Status status;
	Status Win_Status;
	Mini_Max algo;
	Node prs=null;  // present person turn pointer in decision tree
	Node cp = null; // present computer turn pointer in decision tree
	public static int[] images = { R.drawable.photo3, R.drawable.photo4 , R.drawable.photo , R.drawable.photo2,R.drawable.photo5,R.drawable.photo6,R.drawable.photo7,R.drawable.photo8};  // images[first_img_indx] player1 , images[second_img_indx] player2
	public enum Status { Player1_Win, Player2_Win, Draw , Still_Can_Play , Finish }   
	public boolean Play_First ;
	public static int img_random_indx;
	public static int first_img_indx;
	public static int second_img_indx;
	public static Activity Context;

	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
		@Override
	
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_level_layout);
		Initialize(); // just creates and initializes  buttons array and put them on container		
	}

	public void Initialize()
	{
		

		M_create_sound=MediaPlayer.create(GameLevelActivity.this, R.raw.a);
		M_move_sound=MediaPlayer.create(GameLevelActivity.this, R.raw.b);
		m_music=MediaPlayer.create(GameLevelActivity.this, R.raw.m);

		speaker = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

			@Override
			public void onInit(int status) {
				if(status != TextToSpeech.ERROR) {
					speaker.setLanguage(Locale.US);
				}
			}});
		speaker.setOnUtteranceProgressListener(new UtteranceProgressListener() {

			@Override
			public void onStart(String arg0) {
				m_music.pause();
			}

			@Override
			public void onError(String arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDone(String arg0) {
			}


		});


		P1_Score = (TextView)findViewById(R.id.p1score);
		P2_Score = (TextView)findViewById(R.id.p2score);


		Player1_Score = new ArrayList<Button>();
		Player2_Score = new ArrayList<Button>();




		Container = ((RelativeLayout)findViewById(R.id.Parent));
		// change the height of container to be the height of the screen
		Container.getLayoutParams().height= getResources().getDisplayMetrics().heightPixels;
		// back color of container
		//Change_Color(Color.BLACK);
		Buttons = new Button[Row_Buttons_Number][Row_Buttons_Number];
		Square_Length = Create_Matrix(Row_Buttons_Number);

		Selected = new ArrayList<Button>();

		if(PlayWithActivity.Mode.equals("p2c"))
			Current_Player = Player2;
		else if(PlayWithActivity.Mode.equals("p2rp"))
		{
			if(ECommunicationManager.client!=null)
			{
				Current_Player = Player1;
				Play_First = true;
			}
			else 
			{
				Current_Player = Player2;
				Play_First = false;
			}
		}
		else if(PlayWithActivity.Mode.equals("p2p"))
			Current_Player = Player1;

		img_random_indx = (int)(Math.random()*8);
		if(PlayWithActivity.Mode.equals("p2rp"))
		{
			first_img_indx = 0;
			second_img_indx = 1;
		}
		else 
		{
			first_img_indx = img_random_indx;
			second_img_indx = Math.abs((img_random_indx-1));
		}
		// this means it has image 
		// button in matrix to use first

		Buttons[1][1].setTag("has_image"+Player1);
		Buttons[1][1].setBackgroundResource(images[first_img_indx]);  // button of my choice 8,8
		Buttons[Row_Buttons_Number-2][Row_Buttons_Number-2].setBackgroundResource(images[second_img_indx]);
		Buttons[Row_Buttons_Number-2][Row_Buttons_Number-2].setTag("has_image"+Player2);

		Player1_Logo = (ImageView)findViewById(R.id.player1_logo);
		Player2_Logo = (ImageView)findViewById(R.id.player2_logo);
		Player1_Logo.setBackgroundResource(images[first_img_indx]);
		Player2_Logo.setBackgroundResource(images[second_img_indx]);

		Bundle data = getIntent().getExtras();
		int level = data.getInt("level");
		Levels_Drawer.setLevel(level);
        Check_Score();

		if(PlayWithActivity.Mode.equals("p2c"))
		{
			Check_Score();
			P2_Score.setText("You : "+Player2_Score.size());
			P1_Score.setText("Comp : "+Player1_Score.size());
		}
		if(PlayWithActivity.Mode.equals("p2c"))
		{
			String[][] state = Get_String_Matix(Buttons);
			algo  = new Mini_Max(state, "A", "B", "B");
			new Thread(new Runnable() {

				@Override
				public void run() {

					algo.Start_Build();
					runOnUiThread(new Runnable() {	        		
						@Override
						public void run() {


							Toast.makeText(GameLevelActivity.this, "Let's Go", Toast.LENGTH_SHORT).show();
						}
					});

				}
			}).start();


		}
		if(PlayWithActivity.Mode.equals("p2rp"))
		{
			still_opened=true; // still opened  and can recieve
			Recieve();
		}


		Context = this;
		if(LoadingScreenActivity.option2==1)
		{

			m_music.setLooping(true);
			m_music.start();

		}
        
	}

	@Override
	public void onBackPressed() {

		if(PlayWithActivity.Mode.equals("p2rp"))
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(ECommunicationManager.server!=null)
				{
					ECommunicationManager.server.Send(new EMessage("close",null));
				}
				else if(ECommunicationManager.client!=null)
				{
					ECommunicationManager.client.Send(new EMessage("close",null));
				}
				EBBluetoothManager.communication.Destroy_Connection();
			}
		}).start();
	    still_opened=false;
		m_music.stop();
		this.finish();
		
		
	}
	
	public String[][] Get_String_Matix(Button[][] buttons)
	{
		String[][] str_m = new String[buttons.length][buttons.length];
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons.length; j++) {
				if(buttons[i][j].getVisibility()== Button.VISIBLE)
				{
					if(buttons[i][j].getTag().equals("has_image"+Player1))
						str_m[i][j] = "A";
				    if(buttons[i][j].getTag().equals("has_image"+Player2))
						str_m[i][j] = "B";
				    if(buttons[i][j].getTag().equals(""))
				    	str_m[i][j] = "";
				}
				else str_m[i][j] = "N";
			}
		}
		return str_m;
	}

    public void Change_Color(int color)
	{
    	// initially set the background color
		Container.setBackgroundColor(color);
	}

    public int  Create_Matrix(int Edge)
    {
    	///////////////////////////////////////////////////////////////////////////
    	/////  don't try to understand it is complex algorithm and need a lot of documentation
	    RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
	    int width = getResources().getDisplayMetrics().widthPixels; // height of screen 
	    int height = getResources().getDisplayMetrics().heightPixels-150;
	    int distribut_padding = Math.abs(width-height);  // this is distributed difference between width and height to make them equivelent 
	    Container.setPadding(0, distribut_padding/2 , 0, distribut_padding/2);  
	    int  one_width =   width /   Edge; //  assumed number (***-10) this because we added 10 down to width to prevent margin between buttons 
	    int edgecopy = one_width;   // before adding 10 if not here the margin between buttons will be preserved
	    para.width=  one_width;
	    para.height= (height-distribut_padding)/Edge;
	    
        /////////////////////////////////////////////////////////////////////////
	    for (int i = 0; i < Edge; i++) 
	    {  // Edge here is the number of rows
	    	for (int j = 0; j < Edge ; j++) { // Edge here is the number of columns
	    	    final Button b = new Button(this);
	    		b.setLayoutParams(para);
	    		b.setX(j*edgecopy);
	    		b.setY(i*edgecopy);
	    		b.setTag(""); // means doesn't have any back image 
	    	    Default_Color = b.getBackground();
	    		b.setOnClickListener(this);
	    		Buttons[i][j]= b;
	    		Container.addView(b);  
			}
		}

	   
	    return edgecopy; // edge is the square Edge
    }
    
    @Override
	
    public void onClick(View v) {
		

    	final Button but = (Button)v;  // clicked button
    	but.bringToFront(); // bring to front of others before move

    	/////////////////////////////////////////////////////////////////////////////
    	// if button has animation now don't do any thing
    	if(Has_Back_Image(but)&&!animating)
    	{
    		animating = true;
    		v.animate().rotationBy(360).setDuration(1000)
    		.scaleXBy(1f).setDuration(500).scaleYBy(1).setDuration(500).withEndAction(new Runnable() {
    			@Override
    			public void run() {
    				but.animate().scaleXBy(-1).setDuration(500).scaleYBy(-1).setDuration(500).withEndAction(new Runnable() {
						@Override
						public void run() {
							animating = false;
							
						}
					});
    			}
    		});
    	}
    	// if their are any selected button clear selection  
    	if(Selected.size()>0)
    		Unselect_Arround(Selected);

    	// current no equal null and not self 
    	if(Current!=null&&Current!=but)
    	{

    		Target = but;
    		Point Target_Position = Get_Postion_Matrix(Target);
    		Point Current_Position = Get_Postion_Matrix(Current);
    		if(Current_Position!=null&&Target_Position!=null)
    			if(Target.getTag().toString().equals(""))
    			{
    				
    				if(Current.getTag().toString().equals("has_image"+Current_Player))
    				{
                        if(PlayWithActivity.Mode.equals("p2rp"))
                        {
    					   
    					   if(Play_First)
    					   {
    						   Send_Play(Target_Position, Current_Position);
    						   Perform_Intended_Action(Target_Position, Current_Position);
    						   Play_First= !Play_First;
    					   }
    					   else
    					   {
    						   m_music.pause();
    						   speaker.speak("This is n't your Turn !", TextToSpeech.QUEUE_FLUSH, null);
    						   Toast.makeText(v.getContext(), "This is n't your Turn !", Toast.LENGTH_SHORT).show();
    						   try 
    						   {
    							   Thread.sleep(1000);
    							   m_music.seekTo(m_music.getCurrentPosition());
    							   m_music.start();
    						   } 
    						   catch (InterruptedException e) {}
    					   }
    					   
                        }
                        else
    					     Perform_Intended_Action(Target_Position, Current_Position);
                        
    				}
    				else 
    				{ 
    					m_music.pause();
    					if(PlayWithActivity.Mode.equals("p2c")&&Current_Player.equals(Player2))
    					{
    						speaker.speak( "This is Your Turn !", TextToSpeech.QUEUE_FLUSH, null);
    						Toast.makeText(v.getContext(), "This is Your Turn !", Toast.LENGTH_SHORT).show();
    					}
    					else
    					{
    						speaker.speak( "This isn't your Turn !", TextToSpeech.QUEUE_FLUSH, null);
    						Toast.makeText(v.getContext(), "This isn't your Turn !", Toast.LENGTH_SHORT).show();
    					}
    					Current=null; Target=null;  
    					try 
    					{
    						Thread.sleep(1000);
    						m_music.seekTo(m_music.getCurrentPosition());
    						m_music.start();
    					} 
    					catch (InterruptedException e) {}
    				
    				}  
    			}
    			else
    			{ Current=null; Target=null;  }
    	}
    	// current equal null and here you clicked on target
    	else if(Current==null&&Has_Back_Image(but))
    	{
    		Current = but;
    		Point Current_Position = Get_Postion_Matrix(Current);
    		Select_Around(Current_Position);  
    	}
    	// current equal null and hasn't image so it will be discarded
    	else if(Current==null&&!Has_Back_Image(but))
    	{
    		Current = null; Target=null;
    	}
    	else if(Current!=null) // self click
    	{
    		// if self is clicked again then select around it
    		Point Current_Position = Get_Postion_Matrix(Current);
    		Select_Around(Current_Position);   	
    		Current=but;
    	}

  	}
    
    public Point Get_Postion_Matrix(Button B)
    {
    	// returns the row and column 
       for (int i = 0; i < Buttons.length; i++) {
		for (int j = 0; j < Buttons.length; j++) {
			if(Buttons[i][j]== B)
				return new Point(i,j); // Row , Column
		}
	   }
       return null; // can't be found this rarely returned
    }
   
    public void Select_Around(Point Matrix_Location)
    {    	
    	// select around the given button
    	int row = Matrix_Location.x;
    	int col = Matrix_Location.y;
    	
    	//     [[Around]<-[Next]<-(([Current]))->[Next]->[Around]]
    	int next[] = { row-1,col+1,row+1,col-1   }; // top 0 - right 1  - bottom 2  - left 3
    	int around[] = { row-2,col+2,row+2,col-2  }; // top 0  - right 1  - bottom 2  - left 3 
    	for (int i = 0; i < 4; i++) 
    	{
    		if(next[i]>=0&&next[i]<=Buttons.length-1)  // next if no negative or bigger length
        	{
    			if(i==0||i==2)// top || bottom
    			{
    				if(!Has_Back_Image(Buttons[next[i]][col]))
    				{
    					Buttons[next[i]][col].setBackgroundColor(Color.parseColor("#757575"));
    					Selected.add(Buttons[next[i]][col]);
    				}
    			}
        	   else if(i==1||i==3) // right || left
        	   {
        		   if(!Has_Back_Image( Buttons[row][next[i]]))
        		   {
        			   Buttons[row][next[i]].setBackgroundColor(Color.parseColor("#757575"));
        			   Selected.add(Buttons[row][next[i]]);
        		   }
        	   }
        	}
    		if(around[i]>=0&&around[i]<=Buttons.length-1) // around if no negative or bigger length
    		{
    			if(i==0||i==2)// top || bottom
    			{
    				if(!Has_Back_Image(Buttons[around[i]][col]))
    				{
    					Buttons[around[i]][col].setBackgroundColor(Color.parseColor("#9E9E9E"));
    					Selected.add(Buttons[around[i]][col]);
    				}
    			}
        	   else if(i==1||i==3) // right || left
        	   {
        		   if(!Has_Back_Image( Buttons[row][around[i]]))
        		   {
        			   Buttons[row][around[i]].setBackgroundColor(Color.parseColor("#9E9E9E"));
        			   Selected.add(Buttons[row][around[i]]);
        		   }
        	   }
    			
    		}
    	
		}
    	if(!Check_If_Player_Has_Move(Matrix_Location))
    	{
    	    //  no movement available 
    		//Toast.makeText(this, "can't move", Toast.LENGTH_SHORT).show();
    		// can put warning music here
    	}
    	
    }
    
    public boolean Has_Back_Image(Button B)
    {
    	// check if it has a background image 
    	return !B.getTag().toString().equals("");
    }
    
    public void Unselect_Arround(ArrayList<Button> selected)
	{
    	// Unselect the button around 
		for (Button button : selected) {
			button.setBackground(Default_Color);
		}
	}
 
	public void update_person_computer(Point Target_Position,Point Current_Position)
	{
   		if(Current_Player.equals(Player2))
		{
   			if(!algo.Evolve_Play_Pointer(Get_String_Matix(Buttons), Target_Position, Current_Position))
   			{
   				return;
   			}
   			Current_Player = Player1 ;
		}
		else Current_Player = Player2 ;
		
		
	    if( Current_Player.equals(Player1))
	    {
	    	 Node Best = algo.Get_Best_Action();
	    	 if(Best!=null)
	    	 {
	    		 Target = Buttons[Best.Target_Position.x][Best.Target_Position.y];
	    		 Current = Buttons[Best.Current_Position.x][Best.Current_Position.y];
	    		 Perform_Intended_Action(Best.Target_Position, Best.Current_Position);
	    	 }
	    	 else return;
	    }
	    
		
	}
    
    public void Perform_Intended_Action(Point Target_Position,Point Current_Position)
    {
    	
    	
    	
    	
    	//move 4 sound
    	// if move button
    	if(Target_Position.y+2<=Buttons.length-1&&Target_Position.y+2==Current_Position.y&&Target_Position.x==Current_Position.x) //move left 
    	{
    		 
    		 
    		Current.animate().xBy(-Square_Length*2).setDuration(500).rotationYBy(360).setDuration(500);
    		Target.animate().xBy(Square_Length*2).setDuration(500);
    		Switch_Buttons(Target_Position, Current_Position);
    		if(PlayWithActivity.Mode.equals("p2p"))
        		  Current_Player =	Current_Player.equals(Player2)? Player1: Player2;
    	    
    			
    		 
     	 
    	}
    	else if(Target_Position.y-2>=0&&Target_Position.y-2==Current_Position.y&&Target_Position.x==Current_Position.x) // move right
    	{
    		 
    		Current.animate().xBy(Square_Length*2).setDuration(500).rotationYBy(360).setDuration(500);
    		Target.animate().xBy(-Square_Length*2).setDuration(500);
    		Switch_Buttons(Target_Position, Current_Position);
    		if(PlayWithActivity.Mode.equals("p2p"))
    			Current_Player =	Current_Player.equals(Player2)? Player1: Player2;
    		 
    	}
    	else if(Target_Position.x+2<=Buttons.length-1&&Target_Position.y==Current_Position.y&&Target_Position.x+2==Current_Position.x) // top
    	{
    		 
    	 
    		Current.animate().yBy(-Square_Length*2).setDuration(500).rotationXBy(360).setDuration(500);
    		Target.animate().yBy(Square_Length*2).setDuration(500);
    		Switch_Buttons(Target_Position, Current_Position);
    		if(PlayWithActivity.Mode.equals("p2p"))
    			Current_Player =	Current_Player.equals(Player2)? Player1: Player2;
    		 
    	}
    	else if(Target_Position.y==Current_Position.y&&Target_Position.x-2==Current_Position.x&&Target_Position.x-2>=0)// bottom
    	{
     
    		Current.animate().yBy(Square_Length*2).setDuration(500).rotationXBy(360).setDuration(500);
    		Target.animate().yBy(-Square_Length*2).setDuration(500);
    		Switch_Buttons(Target_Position, Current_Position);
    		if(PlayWithActivity.Mode.equals("p2p"))
    			Current_Player =	Current_Player.equals(Player2)? Player1: Player2;
    	}
    	else // if create buttons 
    	{
    		 
    		
    	    final Button but = Create_Button();
    	    
    	    //create music
 
    	    
    		but.bringToFront();
    		if(Target_Position.y+1<=Buttons.length-1&&Target_Position.y+1==Current_Position.y&&Target_Position.x==Current_Position.x) // left 
        	{
        		Container.addView(but);
        		but.animate().scaleXBy(-0.5f).scaleYBy(-0.5f).setDuration(700).y(Current.getY()).x(Current.getX()-Square_Length).setDuration(500).withEndAction(new Runnable() {
					
					@Override
					public void run() {
						but.animate().rotationYBy(360).scaleXBy(0.5f).scaleYBy(0.5f).setDuration(500);
						
					}
				});
        		but.setVisibility(Button.VISIBLE);
        		Replace_Delete(Target_Position,Current_Position,but);
        		if(PlayWithActivity.Mode.equals("p2p"))
        			Current_Player =	Current_Player.equals(Player2)? Player1: Player2;
        	}
        	else if(Target_Position.y-1>=0&&Target_Position.y-1==Current_Position.y&&Target_Position.x==Current_Position.x) //  right
        	{
        		Container.addView(but);
        		but.animate().scaleXBy(-0.5f).scaleYBy(-0.5f).setDuration(700).y(Current.getY()).x(Current.getX()+Square_Length).setDuration(500).withEndAction(new Runnable() {
					@Override
					public void run() {
						but.animate().rotationYBy(360).scaleXBy(0.5f).scaleYBy(0.5f).setDuration(500);
						
					}
				});
        		but.setVisibility(Button.VISIBLE);
        		Replace_Delete(Target_Position,Current_Position,but);
        		if(PlayWithActivity.Mode.equals("p2p"))
        			Current_Player =	Current_Player.equals(Player2)? Player1: Player2;
        		 
        	}
        	else if(Target_Position.x+1<=Buttons.length-1&&Target_Position.y==Current_Position.y&&Target_Position.x+1==Current_Position.x) // top
        	{
        		Container.addView(but);
        		but.animate().scaleXBy(-0.5f).scaleYBy(-0.5f).setDuration(700).y(Current.getY()-Square_Length).x(Current.getX()).setDuration(500).withEndAction(new Runnable() {
					
					@Override
					public void run() {
						but.animate().rotationXBy(360).scaleXBy(0.5f).scaleYBy(0.5f).setDuration(500);
						
					}
				});
        		but.setVisibility(Button.VISIBLE);
        		Replace_Delete(Target_Position,Current_Position,but);
        		if(PlayWithActivity.Mode.equals("p2p"))
        			Current_Player =	Current_Player.equals(Player2)? Player1: Player2;

        	}
        	else if(Target_Position.x-1>=0&&Target_Position.y==Current_Position.y&&Target_Position.x-1==Current_Position.x)// bottom
        	{
        		Container.addView(but);
        		but.animate().scaleXBy(-0.5f).scaleYBy(-0.5f).setDuration(700).y(Current.getY()+Square_Length).x(Current.getX()).setDuration(500).withEndAction(new Runnable() {
					
					@Override
					public void run() {
						but.animate().rotationXBy(360).scaleXBy(0.5f).scaleYBy(0.5f).setDuration(500);
						
					}
				});
        		but.setVisibility(Button.VISIBLE);
        		Replace_Delete(Target_Position,Current_Position,but);
        		if(PlayWithActivity.Mode.equals("p2p"))
        			Current_Player =	Current_Player.equals(Player2)? Player1: Player2;
        		
        	}
    		
    		but.destroyDrawingCache();
    		
    		Current=null;
    		Target=null;
    	}
    	
    }

    public void Switch_Buttons(final Point Target_Position,final Point Current_Position)
    {
    	if(LoadingScreenActivity.option1==1)
    	{
         	MediaPlayer.create(this, R.raw.a).start();
    	}
    	// you previously swap two button from ui now we need to swap them from matrix
    	Button Temp = Buttons[Target_Position.x][Target_Position.y];
    	Buttons[Target_Position.x][Target_Position.y] = Buttons[Current_Position.x][Current_Position.y];
    	Buttons[Current_Position.x][Current_Position.y] = Temp;
    	//bring current to be at top of others 
    	Current.bringToFront();
    	//and also bring target to be at top of others
    	Target.bringToFront();
    	Change(Current,Target_Position);
    	Check_Score();
    	Current=null; // no current now because it is moved successfully 
    	Target= null;// no current now because it is moved successfully 
    	if(PlayWithActivity.Mode.equals("p2c"))
    	new Thread(new Runnable() {

    		@Override
    		public void run() {

    			try {
    				Thread.sleep(1000);
    				runOnUiThread(new Runnable() {
    					public void run() {
    						if(Current_Player.equals(Player2)&&Win_Status!=Status.Player1_Win||Win_Status!=Status.Player2_Win||Win_Status!=Status.Draw)
    						update_person_computer(Target_Position, Current_Position);
    					}
    				});
    			} catch (InterruptedException e) {}


    		}
    	}).start();
    	
    }

    public void Replace_Delete(final Point Target_Position,final Point Current_Position,final Button New)
    {
    	// delete the target and put new button instead
    	Buttons[Target_Position.x][Target_Position.y].animate().scaleXBy(-0.7f).scaleYBy(-0.7f).setDuration(500).withEndAction(new Runnable() {
			@Override
			public void run() {
				Container.removeView(Buttons[Target_Position.x][Target_Position.y]);
				Buttons[Target_Position.x][Target_Position.y] = New;
				//Convert_Around(Buttons[Target_Position.x][Target_Position.y]);
				Change(Buttons[Target_Position.x][Target_Position.y],Target_Position);
				Check_Score();
				if(PlayWithActivity.Mode.equals("p2c"))
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						
						try {
							Thread.sleep(1000);
							runOnUiThread(new Runnable() {
								public void run() {
									if(Current_Player.equals(Player2) && Win_Status!=Status.Player1_Win||Win_Status!=Status.Player2_Win||Win_Status!=Status.Draw)
									update_person_computer(Target_Position, Current_Position);
								}
							});
						} catch (InterruptedException e) {}
						
						
					}
				}).start();
				
			}});    	
    }
    
    public Button Create_Button()
    {
    	if(LoadingScreenActivity.option1==1)
    	{
    		MediaPlayer.create(this, R.raw.b).start();
    	}
    	Button but = new Button(this);
    	but.setX(-100);
    	but.setY(-100);
    	but.setBackground(Current.getBackground()); // means the same image
    	but.setTag(Current.getTag()+"");
    	but.setLayoutParams(Current.getLayoutParams());
    	but.setVisibility(Button.INVISIBLE);
    	but.setOnClickListener(this);
       return but;
    }

    public void Change(final Button btn, Point location){
    	
    	//select around this button
    	int r = location.x;
    	int c = location.y;
    	int next_buttons[] = {r-1,c+1,r+1,c-1};

    	for(int i=0;i<4;i++){
    		if(next_buttons[i] >= 0 && next_buttons[i] <= Buttons.length-1){
    			if(i == 0 || i == 2){ // top , bottom
    				final Button b1 = Buttons[next_buttons[i]][c];
    				if(Has_Back_Image(b1)){
    					if(!b1.getTag().toString().equals(btn.getTag().toString())){
    						
							b1.setTag(btn.getTag().toString());
                            b1.animate().scaleXBy(-1f).scaleYBy(-1f).setDuration(500).withEndAction(new Runnable() {
 								
 								@Override
 								public void run() {
 									b1.animate().scaleXBy(1f).scaleYBy(1f).setDuration(500);
 									b1.setBackground(btn.getBackground());
 									Check_Score();
 								}
 							});
                            
     					}
    				}
    			}
    			else if(i == 1 || i == 3){ // right , left
    				final Button b2 = Buttons[r][next_buttons[i]];
    				if(Has_Back_Image( Buttons[r][next_buttons[i]])){
    					if(!b2.getTag().toString().equals(btn.getTag().toString())){
    						
    						b2.setTag(btn.getTag().toString());
                            b2.animate().scaleXBy(-1f).scaleYBy(-1f).setDuration(500).withEndAction(new Runnable() {
 								
 								@Override
 								public void run() {
 									b2.animate().scaleXBy(1f).scaleYBy(1f).setDuration(500);
 									b2.setBackground(btn.getBackground());
 									Check_Score();
 									
 								}
 							});
     					}
    					
    				}
    				
    			}
    		}
    	
    	}
    	
    	
    }

    public Boolean Check_If_Player_Has_Move(Point Player_Position)
    {
    	int row = Player_Position.x;  // row
    	int col = Player_Position.y; // col
    	if((col-1>=0&&Buttons[row][col-1].getTag().toString().equals("")&&Buttons[row][col-1].getVisibility()==Button.VISIBLE) // left-1
    			||
    			(col-2>=0&&Buttons[row][col-2].getTag().toString().equals("")&&Buttons[row][col-2].getVisibility()==Button.VISIBLE) // left-2
    			||
    			(col+1<=Buttons.length-1&&Buttons[row][col+1].getTag().toString().equals("")&&Buttons[row][col+1].getVisibility()==Button.VISIBLE) // right-1
    			||
    			(col+2<=Buttons.length-1&&Buttons[row][col+2].getTag().toString().equals("")&&Buttons[row][col+2].getVisibility()==Button.VISIBLE) // right-2
    			||
    			(row-1>=0&&Buttons[row-1][col].getTag().toString().equals("")&&Buttons[row-1][col].getVisibility()==Button.VISIBLE) // top-1
    			||
    			(row-2>=0&&Buttons[row-2][col].getTag().toString().equals("")&&Buttons[row-2][col].getVisibility()==Button.VISIBLE) // top-2
    			||
    			(row+1<=Buttons.length-1&&Buttons[row+1][col].getTag().toString().equals("")&&Buttons[row+1][col].getVisibility()==Button.VISIBLE) // bottom-1
    			||
    			(row+2<=Buttons.length-1&&Buttons[row+2][col].getTag().toString().equals("")&&Buttons[row+2][col].getVisibility()==Button.VISIBLE) // bottom-2
    			) 
    	{
    		return true;
    	}
    	return false;
    }
    
    
    
    //****** check who win and the consequence from that ********
    
    public void Check_Score_Algorithm(){


    	for (int i = 0; i < Row_Buttons_Number; i++) {
    		for (int j = 0; j < Row_Buttons_Number; j++) {
    			if (Buttons[i][j].getTag().toString().equals("has_image"+Player2)&&Buttons[i][j].getVisibility()!=Button.INVISIBLE) {
    				Player2_Score.add(Buttons[i][j]);
    			}
    			else if(Buttons[i][j].getTag().toString().equals("has_image"+Player1)&&Buttons[i][j].getVisibility()!=Button.INVISIBLE)
    			{
    				Player1_Score.add(Buttons[i][j]);
    			}
    		}
    	}


    	P1_Score.setText("P1 : "+Player1_Score.size());
    	if(PlayWithActivity.Mode.equals("p2c"))
    	{
    		P1_Score.setText("Comp : "+Player1_Score.size());
    		P2_Score.setText("You : "+Player2_Score.size());
    	}
    	else 
    		P2_Score.setText("P2 : "+Player2_Score.size());
    	if(status!=Status.Finish)
    		Check_Win();

    }

    public void Check_Score()
    {
    	Player1_Score.clear();
    	Player2_Score.clear();
    	Check_Score_Algorithm();
    }

    public Status Check_Win()
    {

    	if(Player1_Score.size()==0) status= Status.Player2_Win;
    	if(Player2_Score.size()==0) status= Status.Player1_Win;
    	boolean player1_can_move=false;
    	boolean player2_can_move=false;
    	for (int i = 0; i < Buttons.length; i++) {
    		for (int j = 0; j < Buttons.length; j++) {
    			if(Buttons[i][j].getTag().toString().equals("has_image"+Player1)&&Buttons[i][j].getVisibility()!=Button.INVISIBLE)
    			{
    				if(Check_If_Player_Has_Move(Get_Postion_Matrix(Buttons[i][j]))){
    					player1_can_move=true;
    					break;
    				}
    			}

    		}
    		if(player1_can_move) break;
    	}

    	for (int i = 0; i < Buttons.length; i++) {
    		for (int j = 0; j < Buttons.length; j++) {
    			if(Buttons[i][j].getTag().toString().equals("has_image"+Player2)&&Buttons[i][j].getVisibility()!=Button.INVISIBLE)
    			{
    				if(Check_If_Player_Has_Move(Get_Postion_Matrix(Buttons[i][j]))){
    					player2_can_move=true;
    					break;
    				}
    			}

    		}
    		if(player2_can_move) break;
    	}

    	// if the player1 or* player2 can't move 
    	if(!player1_can_move||!player2_can_move)
    	{
    		status = Player1_Score.size()==Player2_Score.size()?Status.Draw:Player1_Score.size()>Player2_Score.size()?Status.Player1_Win:Status.Player2_Win;
    	}


    	// use win algorithm 
    	if(status== Status.Player1_Win){
            if(PlayWithActivity.Mode.equals("p2c"))
            	Do_After_win("Computer win "+Player1_Score.size()+" : "+Player2_Score.size());
            else 
            	Do_After_win("Player 1 win "+Player1_Score.size()+" : "+Player2_Score.size());
    		LoadingScreenActivity.db.insertData("Player1",Player1_Score.size());
    		status  = Status.Finish;
    	}
    	else if(status== Status.Player2_Win){

    		if(PlayWithActivity.Mode.equals("p2c"))
    			Do_After_win("You win "+Player2_Score.size()+" : "+Player1_Score.size());   
            else 
            	Do_After_win("Player 2 win "+Player2_Score.size()+" : "+Player1_Score.size());   

    
    		LoadingScreenActivity.db.insertData("Player2",Player2_Score.size());
    		status  = Status.Finish;

    	}
    	else if(status== Status.Draw)
    	{	
    		Do_After_win( "Draw "+Player2_Score.size()+" : "+Player1_Score.size());
    		status  = Status.Finish;
    	}



    	return status;



    }

    public void Do_After_win(String s){




    	if(LoadingScreenActivity.next_level<9)
    	{
    		LoadingScreenActivity.next_level++;
    		//    unblock level  in database
    		LoadingScreenActivity.db.update_Levels(LoadingScreenActivity.next_level+1, "avail");
    	}
    	else LoadingScreenActivity.next_level=0;

    	AlertDialog.Builder alertbuild = new AlertDialog.Builder(this);
    	alertbuild.setTitle("Competitive result");
    	alertbuild.setMessage(s).setCancelable(false).setPositiveButton("Menu", new DialogInterface.OnClickListener() {

    		@Override
    		public void onClick(DialogInterface Dialog, int id) {


    			m_music.stop();
    			PlayWithActivity.Context.finish();
    			finish();
    		}

    	}).setNegativeButton("Next", new DialogInterface.OnClickListener() {

    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			m_music.stop();
    			Intent pass = new Intent(GameLevelActivity.this,GameLevelActivity.class);
    			pass.putExtra("level", LoadingScreenActivity.next_level);
    			startActivity(pass);
    			finish();   	

    		}

    	});
    	if(!PlayWithActivity.Mode.equals("p2rp"))
    	{
    		AlertDialog ad = alertbuild.create();
    		ad.show();
    	}
    	else
    	{
    		Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    		if(ECommunicationManager.server!=null)
    			ECommunicationManager.server.Send(new EMessage("close",null));
    		else 
    			ECommunicationManager.client.Send(new EMessage("close",null));
    		m_music.stop();
    		finish();
    	}

    }

    
    
    //****** these are intermediate methods between UI & Bluetooth
    
	public void Send_Play(final Point Targ_Position,final Point Cur_Position)
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				if(ECommunicationManager.server!=null)
				{
				    
					ECommunicationManager.server.Send(new EMessage(Targ_Position.x ,Targ_Position.y,Cur_Position.x ,Cur_Position.y, Current_Player.equals(Player1)?Player2:Player1 ));
					
				}
				else if(ECommunicationManager.client!=null)
				{
					ECommunicationManager.client.Send(new EMessage(Targ_Position.x ,Targ_Position.y,Cur_Position.x ,Cur_Position.y,Current_Player.equals(Player1)?Player2:Player1));
				}
				
				
			}
		}).start();
		
	}
	
	public void Recieve()
	{
	   	new Thread(new Runnable() {
			
			@Override
			public void run() {

				while (still_opened) {



					if(ECommunicationManager.server!=null)
					{
						final  EMessage msg  =	(EMessage)ECommunicationManager.server.Recieve();
						if(msg!=null)
						{
 
							runOnUiThread(new Runnable() {
								public void run() {

									if(msg.Perform_Action!=null&&msg.Perform_Action.equals("close"))
									{
										EBBluetoothManager.communication.Destroy_Connection();
										still_opened=false;
										m_music.stop();
										GameLevelActivity.this.finish();
									}
									else
									{
										Current = Buttons[msg.Current_X][msg.Current_Y];
										Target = Buttons[msg.Target_X][msg.Target_Y];
										Play_First = !Play_First;
										Perform_Intended_Action(new Point(msg.Target_X,msg.Target_Y),new Point(msg.Current_X,msg.Current_Y));
									}
								}
							});
						}
						}
						else if(ECommunicationManager.client!=null)
						{
							final  EMessage msg  =	(EMessage)ECommunicationManager.client.Recieve();
							if(msg!=null)
							{
			 
								runOnUiThread(new Runnable() {
									public void run() {
										if(msg.Perform_Action!=null&&msg.Perform_Action.equals("close"))
										{
											EBBluetoothManager.communication.Destroy_Connection();
											still_opened=false;
											m_music.stop();
											GameLevelActivity.this.finish();
										}
										else
										{
											Current = Buttons[msg.Current_X][msg.Current_Y];
											Target = Buttons[msg.Target_X][msg.Target_Y];
											Play_First = !Play_First;
											Perform_Intended_Action(new Point(msg.Target_X,msg.Target_Y),new Point(msg.Current_X,msg.Current_Y));
										}
									}
								});
							}
						}
					}
				}
			
		}).start();
	  
		
	}

	

}
















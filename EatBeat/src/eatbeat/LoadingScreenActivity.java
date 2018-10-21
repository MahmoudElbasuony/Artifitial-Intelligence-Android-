package eatbeat;

import java.util.Timer;
import java.util.TimerTask;

import com.example.eatbeat.R;

import EatBeat_DataBase.DatabaseHelper;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.widget.ProgressBar;

public class LoadingScreenActivity extends Activity{
	int time =3000;
	int active=0;
	ProgressBar b;
	public static int option1;
	public static int option2;
	public static DatabaseHelper db; 
	 public static int next_level = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_screen_layout);
		
		b =(ProgressBar)findViewById(R.id.progressBar1);
       
		active+=1;
		
		
		final Intent i=new Intent(this,StartGameActivity.class);

		TimerTask task=new TimerTask(){

			@Override
			public void run() {
				db = new DatabaseHelper(LoadingScreenActivity.this);
				Cursor cursor = db.viewAllOptions();

				if(cursor.getCount()>0)
				{

					cursor.moveToNext();
					option1 = cursor.getInt(1);
					cursor.moveToNext();
					option2 = cursor.getInt(1);


				}
				else 
				{
					LoadingScreenActivity.db.insertOptionData(1);
					LoadingScreenActivity.db.insertOptionData(1);
					option1=1;
					option2=1;
				}				
				
				// TODO Auto-generated method stub
				startActivity(i);
				finish();
			}

		};
		Timer splash=new Timer();
		splash.schedule(task, time);
	   

	}

}



package eatbeat;

import android.os.Bundle;

import java.util.ArrayList;

import com.example.eatbeat.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartGameActivity extends Activity implements View.OnClickListener{
	
	public  final GameLevelActivity game = new GameLevelActivity();
	Button start,con,opt,about;
	int c=1;
	int x;
	

//	MediaPlayer mpm,mps;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_continue_game_layout);
		start =(Button)findViewById(R.id.start_new_game_btn);
		con=(Button)findViewById(R.id.scores_btn);
		opt=(Button)findViewById(R.id.options_btn);
		about=(Button)findViewById(R.id.aboutinfo_btn);
		start.setOnClickListener(this);
		con.setOnClickListener(this);
		opt.setOnClickListener(this);
		about.setOnClickListener(this);
		
		Toast.makeText(this, LoadingScreenActivity.option1+"", Toast.LENGTH_SHORT).show();
	}
 

	@Override
	public void onClick(View v) {
		 
		switch (v.getId()) {
		case R.id.start_new_game_btn:
			Intent i = new Intent(this,PlayWithActivity.class);
			startActivity(i);
			break;
		case R.id.options_btn:
			Intent intent=new Intent(getApplicationContext(),ChooseOptionActivity.class);
			startActivityForResult(intent, c);
			break;
 		case R.id.aboutinfo_btn:
			Intent k= new Intent(this, AboutInfoActivity.class); 
			startActivity(k);
			break;
 		case R.id.scores_btn:
			Cursor res = LoadingScreenActivity.db.viewScoreData();
			ArrayList<Integer> ll = new ArrayList<Integer>();
			ArrayList<String> ls = new ArrayList<String>();
			int c = 0;
			if(res.getCount()>0)
			while(res.moveToNext() && c < 10){
				ll.add(res.getInt(0));
				c++;
			}
		
			for(int x : ll){
				if(!ls.contains(String.valueOf(x)))
				   ls.add(String.valueOf(x));
			}
			//Toast.makeText(v.getContext(), ls.size(), Toast.LENGTH_SHORT).show();
			Intent in = new Intent(StartGameActivity.this, ScoresActivity.class);
	        in.putExtra("mylist", ls);		
  	        startActivity(in);
			break;
		}
	}

}

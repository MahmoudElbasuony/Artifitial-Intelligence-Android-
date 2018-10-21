package eatbeat;


import com.example.eatbeat.R;

import EBluetooth.ECommunicationManager;
import EBluetooth.EMessage;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class LevelChooseActivity extends Activity {

	ListView levels;
	String[] values;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_level);
		levels = (ListView) findViewById(R.id.level_list);
		values = new String[10];
		Cursor  lvs = LoadingScreenActivity.db.viewAllLevels();
		
		if(lvs.getCount()==0)
		{
			for (int i = 0; i < 10; i++) {
				if(i==0)
					LoadingScreenActivity.db.insertLevelData("avail");
				else LoadingScreenActivity.db.insertLevelData("not avail");
			}
			 lvs = LoadingScreenActivity.db.viewAllLevels();
		}

			
			for (int i = 0; i < values.length; i++) {
				lvs.moveToNext();
				values[i] = "Level "+(i+1)+" "+lvs.getString(1);
				 
			}
		
		ArrayAdapter<String> AD = new ArrayAdapter<String>(this,R.layout.makers,values);
		levels.setAdapter(AD);
		levels.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View v,final int level,
					long arg3) {
				if(values[level].equals(("Level "+(level+1)+" avail")))
				{
					Intent t = new Intent(v.getContext(),GameLevelActivity.class);
					if(PlayWithActivity.Mode.equals("p2rp"))
						new Thread(new Runnable() {

							@Override
							public void run() {
								ECommunicationManager.client.Send(new EMessage(level+""));

							}
						}).start();

					t.putExtra("level", level);
					LevelChooseActivity.this.finish();
					startActivity(t);
				}
				else
					Toast.makeText(LevelChooseActivity.this, "This Level Isn't Available", Toast.LENGTH_SHORT).show();
				
			}
		});
	}


}

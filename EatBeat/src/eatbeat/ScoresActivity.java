package eatbeat;

import java.util.ArrayList;
import com.example.eatbeat.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ScoresActivity extends Activity {
	ListView lv;
	Intent inn ;
	ArrayList<String> myList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scores);
		inn = getIntent();
		show();
	}
	
	public void show(){	
		lv = (ListView)findViewById(R.id.scores_list);
		myList = ((ArrayList<String>)inn.getStringArrayListExtra("mylist"));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.makers, myList);		
		lv.setAdapter(adapter);
		
	}

 
}

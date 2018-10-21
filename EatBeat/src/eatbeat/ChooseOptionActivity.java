package eatbeat;

import com.example.eatbeat.R;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;


public class ChooseOptionActivity extends Activity {
	CheckBox b1,b2;
	Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    
		setContentView(R.layout.choose_option_layout);
		b1=(CheckBox)findViewById(R.id.checkBox1);
		b2=(CheckBox)findViewById(R.id.checkBox2);
		btn=(Button)findViewById(R.id.Save);
		b1.setText("Music");
		b2.setText("Sound");
		Cursor cursor = LoadingScreenActivity.db.viewAllOptions();
		
		if(cursor.getCount()>0)
		{
		
			cursor.moveToNext();
			LoadingScreenActivity.option1 = cursor.getInt(1);
			cursor.moveToNext();
			LoadingScreenActivity.option2 = cursor.getInt(1);

			if(LoadingScreenActivity.option1==1)
				b1.setChecked(true);
			else b1.setChecked(false);

			if(LoadingScreenActivity.option2==1)
				b2.setChecked(true);
			else b2.setChecked(false);
		
		}
		else 
		{
			LoadingScreenActivity.db.insertOptionData(1);
			LoadingScreenActivity.db.insertOptionData(1);
		}
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(b1.isChecked())
				{
					LoadingScreenActivity.db.update_options(1,1);
					LoadingScreenActivity.option1=1;
				}
				else 
				{
					LoadingScreenActivity.db.update_options(1,0);
					LoadingScreenActivity.option1=0;
				}

				if(b2.isChecked())
				{
					LoadingScreenActivity.db.update_options(2,1);
					LoadingScreenActivity.option2=1;
				}
				else 
				{
					LoadingScreenActivity.db.update_options(2,0);
					LoadingScreenActivity.option2 = 0;
				}

				finish();
			}
		});
       
	}

	 

 

}

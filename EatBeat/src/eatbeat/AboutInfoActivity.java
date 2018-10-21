package eatbeat;


import com.example.eatbeat.R;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AboutInfoActivity extends Activity  {
ListView lst;
String []names={"HanySabry","Mahmoud Elbasouny","Mouhamed Ashour","Mohamed Ali"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_info_layout);
		listView();
	}
	public void listView()
	{
		lst=(ListView)findViewById(R.id.developer_list);
		ArrayAdapter<String> adp=new ArrayAdapter<String>(this, R.layout.makers,names);
		lst.setAdapter(adp);
	}
	 
 

}

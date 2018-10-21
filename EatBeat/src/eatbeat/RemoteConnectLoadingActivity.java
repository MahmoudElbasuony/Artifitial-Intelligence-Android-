package eatbeat;

import com.example.eatbeat.R;

import eatbeat.LevelChooseActivity;

import EBluetooth.EBBluetoothManager;
import EBluetooth.ECommunicationManager;
import android.os.Bundle;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class RemoteConnectLoadingActivity extends Activity {
    
	public static Activity Context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote__connect__load_layout);
		Context = this;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try
				{
					EBBluetoothManager.communication = new ECommunicationManager(EBBluetoothManager.Choosed_Device);
					ECommunicationManager.client.connect();
					Intent in = new Intent(RemoteConnectLoadingActivity.this,LevelChooseActivity.class);
					RemoteConnectLoadingActivity.this.startActivity(in);
					RemoteConnectLoadingActivity.this.finish();
				}
				catch(Exception e)
				{ Toast.makeText(RemoteConnectLoadingActivity.this, "Sorry Can't Connect !", Toast.LENGTH_SHORT).show();}


			}
		}).start();
		
	}

 

}

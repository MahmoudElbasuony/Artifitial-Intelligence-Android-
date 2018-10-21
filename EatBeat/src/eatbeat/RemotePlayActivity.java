package eatbeat;

import com.example.eatbeat.R;

import EBluetooth.EBBluetoothManager;
import EBluetooth.ECommunicationManager;
import android.app.Activity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class RemotePlayActivity extends Activity  {
	
     Button start_ply_with,listen_btn;
	ToggleButton discover;
	ListView discovered_devices_list;
	ArrayAdapter<String> arr_adapter;
    public static EBBluetoothManager blue_manager;
    public static Activity remote_activity; // reference to this activity
  //Toast.makeText(tog.getContext(), "checked", Toast.LENGTH_SHORT).show();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discover_device_layout);
		start_ply_with = (Button)findViewById(R.id.Start_ply);
		listen_btn = (Button)findViewById(R.id.listen_btn);
		discover = (ToggleButton)findViewById(R.id.discover);
		discovered_devices_list = (ListView)findViewById(R.id.discovered_devices_list);
		discovered_devices_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked);
		discovered_devices_list.setAdapter(arr_adapter);
		remote_activity = this;
		discover.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(EBBluetoothManager.bluetooth_supported)
				{

					ToggleButton tog = (ToggleButton)v;
					if(tog.isChecked())
					{
						// scan 
						if(!EBBluetoothManager.blue_adapter.isEnabled())
							EBBluetoothManager.blue_adapter.enable();
						blue_manager.Scan();
					}
					else
					{
						// stop scan
						blue_manager.Stop_Scan();
					}
				}
				
			}
		});
	    discovered_devices_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				   SparseBooleanArray selected=	 discovered_devices_list.getCheckedItemPositions();
				   if(selected.get(position))
				   {
					   String[] name_mac = discovered_devices_list.getItemAtPosition(position).toString().split("\n");
					   if(blue_manager.Set_Choosed_Device(name_mac.length>1&&name_mac[1]!=null&&name_mac[1]!=""?name_mac[1]:null))
						   start_ply_with.setVisibility(Button.VISIBLE);
					   else
					   {
						   start_ply_with.setVisibility(Button.INVISIBLE);
						   Toast.makeText(v.getContext(), "Invalid device", Toast.LENGTH_SHORT).show();
					   }
				   }
			}
		});
	    start_ply_with.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(EBBluetoothManager.bluetooth_supported)
				{
					blue_manager.Stop_Scan();
					ECommunicationManager.server=null; // prevent connect with listenig
					blue_manager.Start();
				}
			}
		});
	    listen_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(EBBluetoothManager.bluetooth_supported)
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						EBBluetoothManager.communication = new ECommunicationManager();
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								listen_btn.setText("Listening..."); 
								listen_btn.setEnabled(false);
								Toast.makeText(RemotePlayActivity.this, "Started Listening", Toast.LENGTH_SHORT).show();

							}
						});
						
						ECommunicationManager.server.start_listen();
						
						
					}
				}).start();
				
			}
		});
	    if(EBBluetoothManager.bluetooth_supported)
	    blue_manager = new EBBluetoothManager(this,arr_adapter);
	    
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

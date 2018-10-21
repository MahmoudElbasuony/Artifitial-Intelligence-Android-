package EBluetooth;
import java.util.ArrayList;
import java.util.UUID;

import eatbeat.RemoteConnectLoadingActivity;
import eatbeat.RemotePlayActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.Toast;
//Toast.makeText(context, "Doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
public class EBBluetoothManager
{
	public static UUID my_uuid = UUID.fromString("31176e8e-07c6-11e6-b512-3e1d05defe78");
	public static EBDevice Choosed_Device;
	public static BluetoothAdapter blue_adapter;
	BroadcastReceiver Action_Reciever;
	public ArrayList<Pair<String, EBDevice>> devices;
	public Context context_of_ui;
	public ArrayAdapter<String> UI_Dev_str_arr_adpater;
	public static ECommunicationManager communication ;
	public static boolean bluetooth_supported=true;
	
	
	
	public EBBluetoothManager(Context context,ArrayAdapter<String> adapter)
    {
		this.context_of_ui = context;
		blue_adapter = BluetoothAdapter.getDefaultAdapter();
		
		if(blue_adapter!=null)// supports Bluetooth
		{
			if(!blue_adapter.isEnabled())
			{
				blue_adapter.enable();
			    Toast.makeText(context, "Bluetooth Enabled", Toast.LENGTH_SHORT).show();
			}
			devices = new ArrayList<Pair<String,EBDevice>>();
			if(adapter!=null)
			{
				UI_Dev_str_arr_adpater = adapter;
				Register_Events();
				get_Bonded();
			}
		}
		else
		{
			bluetooth_supported= false;
			Toast.makeText(context, "Doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
		}
	
	}
	public void Register_Events()
	{
		Action_Reciever = new BroadcastReceiver() {
			@Override
			public void onReceive(Context con, Intent in) {
				if(in.getAction()== BluetoothDevice.ACTION_FOUND)
				{
					BluetoothDevice device = (BluetoothDevice)in.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					if(!(UI_Dev_str_arr_adpater.getPosition(device.getName()+"\n"+device.getAddress())>=0))
					{
						UI_Dev_str_arr_adpater.add(device.getName()+"\n"+device.getAddress());
						devices.add(new Pair<String, EBDevice>(device.getAddress(),new EBDevice(device.getAddress(), device.getName(), device)));
						UI_Dev_str_arr_adpater.notifyDataSetChanged();
					}
				}
				else if(in.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
				{
					if(in.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE,0) == BluetoothDevice.BOND_BONDED)
					{
					    
						Toast.makeText(con,"Paired", Toast.LENGTH_SHORT).show();
						if(ECommunicationManager.server==null)
						Start_Activity();
						
					} 
				}
			}
		};
        context_of_ui.registerReceiver(Action_Reciever, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		//context.registerReceiver(Action_Reciever, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
		//context.registerReceiver(Action_Reciever, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
		context_of_ui.registerReceiver(Action_Reciever, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
	}
	public void Start_Activity()
	{
		
		Intent in = new Intent(context_of_ui,RemoteConnectLoadingActivity.class);
		context_of_ui.startActivity(in);
		RemotePlayActivity.remote_activity.finish();
	}
 	public boolean Start()
	{
		if(Choosed_Device!=null&&bluetooth_supported)
		{
			if(Choosed_Device.device.getBondState()!= BluetoothDevice.BOND_BONDED)
	         {
				Choosed_Device.device.setPairingConfirmation(true);
				Choosed_Device.device.createBond();

	         }
	         else  // already bonded
	         {
	        	 
	        	Toast.makeText(context_of_ui,"already Paired", Toast.LENGTH_SHORT).show();
	        	if(ECommunicationManager.server==null)
	        	Start_Activity();
	         }
			 return true;
		}
		else
		{
			Toast.makeText(context_of_ui, "Can't Pair With this device!", Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	public void Scan()
	{
		if(bluetooth_supported)
		{
			UI_Dev_str_arr_adpater.clear();
			get_Bonded();
			blue_adapter.startDiscovery();
		}
	}
	public void Stop_Scan()
	{
		if(bluetooth_supported)
		{
			blue_adapter.cancelDiscovery();
		}
	}
	public boolean Set_Choosed_Device(String Mac)
	{
		if(Mac != null)
		{
			
			Choosed_Device = null;
			for (Pair<String, EBDevice> dev : devices) 
			{
				
				if(dev.first.equals(Mac))
				{
					Choosed_Device = dev.second;
					return true;
				}
			}
			return false;
		}
		else
		{
			Toast.makeText(context_of_ui, "Invalid device", Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	public void get_Bonded()
    {   
		UI_Dev_str_arr_adpater.clear();
    	for (BluetoothDevice device : blue_adapter.getBondedDevices()) {
    		if(!(UI_Dev_str_arr_adpater.getPosition(device.getName()+"\n"+device.getAddress())>=0))
    		{
    			UI_Dev_str_arr_adpater.add(device.getName()+"\n"+device.getAddress());
    			UI_Dev_str_arr_adpater.notifyDataSetChanged();
    			devices.add(new Pair<String, EBDevice>(device.getAddress(),new EBDevice(device.getAddress(), device.getName(), device)));
    		}
		}
    }

}

package EBluetooth;

import android.bluetooth.BluetoothDevice;

public class EBDevice 
{
     public BluetoothDevice device;
     public String Mac_Address;
     public String Name;
     public EBDevice(String Mac,String Name, BluetoothDevice device) {
		 this.Name= Name;
		 this.Mac_Address = Mac;
		 this.device = device;
	}
}

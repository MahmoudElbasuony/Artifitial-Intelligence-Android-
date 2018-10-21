package EBluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eatbeat.GameLevelActivity;
import eatbeat.RemotePlayActivity;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;

public class EClient 
{
      public static InputStream Input_Channel;
      public static OutputStream Output_Channel;
      public static BluetoothSocket server;
      public static EBDevice choosed;
      
      public EClient(EBDevice choosed_dev)
      {
    	  choosed = choosed_dev;
    	  try 
    	  {
    		  if(choosed!=null)
    		  server = choosed.device.createRfcommSocketToServiceRecord(EBBluetoothManager.my_uuid);
    	  } 
    	  catch (IOException e) {}
	  }
      public void connect()
      {
         if(server!=null)
         {
        	 try
        	 {
        		 server.connect();
        		 Input_Channel = server.getInputStream();
        		 Output_Channel = server.getOutputStream();
        	 }
        	 catch (IOException e) 
        	 { 
        		 try {
        			 server.close();
        		 } catch (IOException e1) {}
        	 }
         }
      }
	  public void Send(EMessage message)
	  {
		  if(Output_Channel!=null)
		  {
			  try 
			  {
				  Output_Channel.write(ECommunicationManager.Serialize(message));
				  Output_Channel.flush();
			  } 
			  catch (IOException e) {}
		  }
		   
	  }
	  public EMessage Recieve()
	  {
         
		 EMessage msg = null;
		 if(Input_Channel!=null)
		 {
			 try
			 {
					 byte[] buffer = new byte[1024];
					 Input_Channel.read(buffer);
					 msg = ECommunicationManager.Deserialize(buffer);
					
					 if(msg!=null)
					 {
						 if(msg.Level!=null&&!msg.Level.equals(""))
						 {
							 if(GameLevelActivity.Context!=null)
							 {
								 GameLevelActivity.Context.finish();
							 }
							 RemotePlayActivity.remote_activity.finish();
							 Intent t = new Intent(RemotePlayActivity.remote_activity,GameLevelActivity.class);
							 t.putExtra("level", Integer.parseInt(msg.Level));
							 RemotePlayActivity.remote_activity.startActivity(t);
							 return msg;
						 }
						 else
						 return msg;
					 }
				 
			 }
			 catch(Exception e){ }
		 }
		 return msg;
	  }
	  public void Release()
      {
   	   try
   	   {
   		   Input_Channel.close();
   		   Output_Channel.close();
   		   server.close();
   	   }
   	   catch(Exception e)
   	   {

   	   }
      }
}

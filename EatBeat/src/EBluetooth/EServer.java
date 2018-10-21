package EBluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eatbeat.GameLevelActivity;
import eatbeat.RemotePlayActivity;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

public class EServer 
{
      public static BluetoothServerSocket listener;
      public static BluetoothSocket client;
      public static InputStream Input_Channel;
      public  static OutputStream Output_Channel;
      public EServer()
      {
    	  try 
    	  {
    		  listener = EBBluetoothManager.blue_adapter.listenUsingRfcommWithServiceRecord("EatBeat", EBBluetoothManager.my_uuid);

    	  } 
    	  catch (IOException e) {}
	  }
      public void start_listen()
      {
         if(listener!=null)
         {
        	 while (true) {
				 
        		 try 
        		 {
        			 client =  listener.accept();
        			 Input_Channel = client.getInputStream();
        			 Output_Channel = client.getOutputStream();
        			 if(client!=null)
        			 {
        				 new Thread( new Runnable() {
							public void run() {
								while (!level_opened) 
								{
									Recieve();
								}
							}
						}).start();
                         
        				 listener.close();
        				 break;
        			 }
        		 }
        		 catch (IOException e) {}
			}
        	 
         }
      }
	  public void Send(EMessage message)
	  {
		  if(Output_Channel!=null)
		  {
			  try 
			  {
				  byte[] n = ECommunicationManager.Serialize(message);
				  Output_Channel.write(n);
				  Output_Channel.flush();
			  } 
			  catch (IOException e) {}
		  }
		   
	  }
	  boolean level_opened;
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
							 level_opened = true;
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
    		   client.close();
    	   }
    	   catch(Exception e)
    	   {

    	   }
       }
	  
}

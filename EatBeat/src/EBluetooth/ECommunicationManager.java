package EBluetooth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ECommunicationManager 
{
	public static  EServer server;
	public static  EClient client;
	public ECommunicationManager(EBDevice choosed) {
		
		client = new EClient(choosed);
	}
	public ECommunicationManager() {
		server = new EServer();
	}

	public static byte[] Serialize(EMessage msg) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os  = new ObjectOutputStream(out);
		os.writeObject(msg);
		return out.toByteArray();
	}
	public static EMessage Deserialize(byte[] msg) throws IOException, ClassNotFoundException 
	{
		ByteArrayInputStream in = new ByteArrayInputStream(msg);
		ObjectInputStream is = new ObjectInputStream(in);
		return (EMessage)is.readObject();
	}
	public void Destroy_Connection()
	{
	 if(server!=null)
	 {
		 server.Release();
		 server=null;
	 }
	 if(client!=null)
	 {
		 client.Release();
		 client=null;
	 }
	 
	}
}

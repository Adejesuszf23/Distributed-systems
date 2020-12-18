import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

public class ClienteMulticast{
	static byte[] get_message(MulticastSocket socket,int longitud) throws IOException{
		byte[] buffer = new byte[longitud];
		DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
		socket.receive(packet);
		return buffer;
	}

	public static void main(String args[]) throws Exception{
		InetAddress group = InetAddress.getByName("230.0.0.0");
		MulticastSocket socket = new MulticastSocket(50000);
		socket.joinGroup(group);
		System.out.println("Waiting for datagram...");
		byte aux[] = get_message(socket,10);
		System.out.println(new String(aux,"UTF-8"));

		byte buffer[] = get_message(socket,5*8);
		ByteBuffer bytebuffer = ByteBuffer.wrap(buffer);
		for(int i = 0; i<5 ;i++)
			System.out.println(bytebuffer.getDouble());

		socket.leaveGroup(group);
		socket.close();
	}
}
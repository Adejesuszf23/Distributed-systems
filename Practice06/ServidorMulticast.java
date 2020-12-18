import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class ServidorMulticast{
	static void send_message(byte buffer[],String ipaddress,int port) throws IOException{
		DatagramSocket socket = new DatagramSocket();
		InetAddress group = InetAddress.getByName(ipaddress);
		DatagramPacket packet = new DatagramPacket(buffer,buffer.length,group,port);
		socket.send(packet);
		socket.close();
	}

	public static void main(String args[]) throws Exception{
		send_message("Hola mundo".getBytes(),"230.0.0.0",50000);

		ByteBuffer bytebuffer = ByteBuffer.allocate(5*8);
		bytebuffer.putDouble(1.1);
		bytebuffer.putDouble(1.2);
		bytebuffer.putDouble(1.3);
		bytebuffer.putDouble(1.4);
		bytebuffer.putDouble(1.5);
		send_message(bytebuffer.array(),"230.0.0.0",50000);
	}
}
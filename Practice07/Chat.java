import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

class Chat{
	static class Worker extends Thread{
		@Override
		public void run(){
			try{
				while(true){
					InetAddress group = InetAddress.getByName("230.0.0.0");
					MulticastSocket socket = new MulticastSocket(50000);
					socket.joinGroup(group);
					byte aux[] = recibe_mensaje(socket,1024);
					String[] parts = new String(aux,"UTF-8").split(":");
					System.out.print("\n"+parts[0]+" escribe: "+parts[1]);
					System.out.print("\nEscribe el mensaje: ");
					socket.leaveGroup(group);
					socket.close();
				}
			}catch(Exception except){
				System.err.println(except.toString());
			}
		}
	}

	public static void main(String args[])throws Exception{
		Worker w = new Worker();
		w.start();
		String nombre = args[0];
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Escribe el mensaje: ");
		while(true){
			String message = nombre+":"+buffer.readLine();
			envia_mensaje(message.getBytes(),"230.0.0.0",50000);
		}
	}

	static void envia_mensaje(byte buffer[],String ipaddress,int port) throws IOException{
		DatagramSocket socket = new DatagramSocket();
		InetAddress group = InetAddress.getByName(ipaddress);
		DatagramPacket packet = new DatagramPacket(buffer,buffer.length,group,port);
		socket.send(packet);
		socket.close();
	}

	static byte[] recibe_mensaje(MulticastSocket socket,int longitud) throws IOException{
		byte[] buffer = new byte[longitud];
		DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
		socket.receive(packet);
		return buffer;
	}
}
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.net.ServerSocket;

class Token{
	static DataInputStream entrada;
	static DataOutputStream salida;
	static boolean first_time = true;
	static String ipaddress;
	static long token = 0;
	static int nodo;

	static class Worker extends Thread{
		public void run(){
			try{
				ServerSocket servidor = new ServerSocket(50000);
				Socket conexion = servidor.accept();
				entrada = new DataInputStream(conexion.getInputStream());
			}catch(Exception except){
				System.err.println(except.toString());
			}
		}
	}

	public static void main(String args[]) throws Exception{
		if(args.length != 2){
			System.err.println("Parametro 1: numero de nodo\nParametro 2: IP siguiente");
			System.exit(1);
		}
		nodo = Integer.valueOf(args[0]);
		ipaddress = args[1];
		Worker w = new Worker();
		w.start();
		Socket conexion = null;
		while(true){
			try{
				conexion = new Socket(ipaddress,50000);
				break;
			}catch(Exception except){
				System.err.println(except.toString());
				Thread.sleep(500);
			}
		}
		salida = new DataOutputStream(conexion.getOutputStream());
		w.join();
		while(true){
			if(nodo == 0){
				if(first_time)
					first_time = false;
				else
					token = entrada.readLong();
			}else{
				token = entrada.readLong();
			}
			token += 1;
			System.out.println("Token: "+token);
			salida.writeLong(token);
		}
	}
}
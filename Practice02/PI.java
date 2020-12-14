import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.lang.Thread;
import java.net.ServerSocket;

class PI{
	static Object lock = new Object();
	static double pi = 0;
	
	static class Worker extends Thread{
		Socket conexion;
		
		Worker(Socket conexion){
			this.conexion = conexion;
		}

		public void run(){
			//Algoritmo 1
			try{
				DataInputStream entrada = new DataInputStream(conexion.getInputStream());
				DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
				double x = entrada.readDouble();
				synchronized(lock){
            	pi = x + pi;
            }
            salida.close();
            entrada.close();
            conexion.close();
			}catch(Exception except){
				System.err.println("Thread Error: "+except.toString());
			}
		}
	}

	public static void main(String[] args) throws Exception{
		if (args.length != 1){
			System.err.println("Uso:");
			System.err.println("java PI <nodo>");
			System.exit(0);
		}

		int nodo = Integer.valueOf(args[0]);
		if(nodo == 0){
			//Algoritmo 2
			ServerSocket servidor = new ServerSocket(50000);
			Worker w[] = new Worker[3];
			int i = 0;
			while(i<3){
				Socket conexion = servidor.accept();
				w[i] = new Worker(conexion);
				w[i].start();
				i = i + 1;
			}

			double suma = 0.0;
			i = 0;
			while(i<10000000){
				suma = 4.0/(8*i+1)+suma;
				i = i + 1;
			}
			
			synchronized(lock){
				pi = suma + pi;
			}
			
			i = 0;
			while(i<3){
				w[i].join();
				i = i + 1;
			}

			System.out.println("Valor de pi: "+pi);
		}else{
			//Algoritmo 3
			Socket conexion = null;
			while(true){
				try{
					conexion = new Socket("localhost",50000);
					break;
				}catch(Exception except){
					Thread.sleep(100);
					System.err.println("Connection Error: "+except.toString());
				}
			}
			DataInputStream entrada = new DataInputStream(conexion.getInputStream());
			DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
			Double suma = 0.0;
			int i = 0;
			while(i<10000000){
				suma = 4.0/(8*i+2*(nodo-1)+3)+suma;
				i = i + 1;
			}
			suma = nodo%2 == 0?suma:-suma;
			salida.writeDouble(suma);
			salida.close();
			entrada.close();
			conexion.close();
		}
	}
}
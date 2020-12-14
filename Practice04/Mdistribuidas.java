import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.lang.Thread;
import java.nio.ByteBuffer;

class Mdistribuidas{
	static int N = 1000;
	static int A[][] = new int[N][N];
	static int B[][] = new int[N][N];
	static int R[][] = new int[N][N];

	public static int checksum(int matriz[][]){
		int check = 0;
		for(int i = 0; i<matriz.length ;i++)
			for(int j = 0; j<matriz[i].length ;j++)
				check += matriz[i][j];
		return check;
	}

	public static int[][] transpuesta(int matriz[][]){
		for(int i = 0; i<matriz.length ;i++){
			for(int j = 0; j<i;j++){
				int x = matriz[i][j];
				matriz[i][j] = matriz[j][i];
				matriz[j][i] = x;
			}
		}
		return matriz;
	}

	public static int[][] multiplica(int A[][],int B[][],int C[][]){
		for(int i = 0; i<A.length ;i++){
			for(int j = 0; j<B[i].length/2 ;j++){
				for(int k = 0; k<A[0].length ;k++)
					C[i][j] += A[i][k]*B[j][k];
			}
		}
		return C;
	}

	public static void imprimir(int matriz[][]){
		for(int i = 0; i<matriz.length ;i++){
			for(int j = 0; j<matriz[i].length ;j++)
				System.out.print(matriz[i][j]+"\t");
			System.out.println("");
		}
	}

	static class Worker extends Thread{
		Socket conexion;
		Worker(Socket conexion){
			this.conexion = conexion;
		}
		
		public void run(){
			try{
				for(int i = 0; i<N ;i++){
					for(int j = 0; j<N ;j++){
						A[i][j] = 2*i+j;
						B[i][j] = 2*i-j;
					}
				}
				B = transpuesta(B);
				DataInputStream entrada = new DataInputStream(conexion.getInputStream());
				DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
				int nodo = entrada.readInt();
				switch(nodo){
					case 1:
						for(int i = 0; i<N/2 ;i++){
							for(int j = 0; j<N ;j++){
								salida.writeInt(A[i][j]);
								salida.writeInt(B[i][j]);
							}
						}
						for(int i = 0; i<N/2 ;i++)
							for(int j = 0; j<N/2 ;j++)
								R[i][j] = entrada.readInt();
					break;
					case 2:
						for(int i = 0; i<N/2 ;i++){
							for(int j = 0; j<N ;j++){
								salida.writeInt(A[i][j]);
								salida.writeInt(B[i+(N/2)][j]);
							}
						}
						for(int i = 0; i<N/2 ;i++)
							for(int j = 0; j<N/2 ;j++)
								R[i][j+(N/2)] = entrada.readInt();						
					break;
					case 3:
						for(int i = 0; i<N/2 ;i++){
							for(int j = 0; j<N ;j++){
								salida.writeInt(A[i+(N/2)][j]);
								salida.writeInt(B[i][j]);
							}
						}
						for(int i = 0; i<N/2 ;i++)
							for(int j = 0; j<N/2 ;j++)
								R[i+(N/2)][j] = entrada.readInt();
					break;
					case 4:
						for(int i = 0; i<N/2 ;i++){
							for(int j = 0; j<N ;j++){
								salida.writeInt(A[i+(N/2)][j]);
								salida.writeInt(B[i+(N/2)][j]);
							}
						}
						for(int i = 0; i<N/2 ;i++)
							for(int j = 0; j<N/2 ;j++)
								R[i+(N/2)][j+(N/2)] = entrada.readInt();
					break;					
				}
				entrada.close();
				salida.close();
				conexion.close();
			}catch(Exception except){
				System.err.println(except.toString());
			}
		}
	}

	public static void main(String args[]) throws Exception{
		long t1 = System.currentTimeMillis();
		if(args.length != 1)
			System.exit(0);
		int nodo = Integer.parseInt(args[0]);
		if(nodo == 0){ //Servidor
			System.out.println("Nodo servidor");
			ServerSocket servidor = new ServerSocket(50000);
			Worker w[] = new Worker[4];
			
			for(int i = 0; i<4 ;i++){
				Socket conexion = servidor.accept();
				w[i] = new Worker(conexion);
				w[i].start();
			}
			
			for(int i = 0; i<4 ;i++)
				w[i].join();
			
			System.out.println("Checksum = "+checksum(R));

			if(N == 4){
				System.out.println("\nMatriz A"); imprimir(A);
				System.out.println("\nMatriz B"); imprimir(B);
				System.out.println("\nMatriz C"); imprimir(R);
			}
		}else{ //Cliente
			Socket conexion = null;
			while(true){
				try{
					conexion = new Socket("localhost",50000);
					break;
				}catch(Exception except){
					System.err.println(except.toString());
					Thread.sleep(100);
				}
			}
			System.out.println("Nodo "+nodo);
			DataInputStream entrada = new DataInputStream(conexion.getInputStream());
			DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
			salida.writeInt(nodo);
			int aux_A[][] = new int[N/2][N];
			int aux_B[][] = new int[N/2][N];
			int aux_C[][] = new int[N/2][N/2];
			for(int i = 0; i<N/2 ;i++)
				for(int j = 0; j<N ;j++){
					aux_A[i][j] = entrada.readInt();
					aux_B[i][j] = entrada.readInt();
				}
			aux_C = multiplica(aux_A,aux_B,aux_C);
			switch(nodo){
				case 1: System.out.println("Multiplica A1xB1"); break;
				case 2: System.out.println("Multiplica A1xB2"); break;
				case 3: System.out.println("Multiplica A2xB1"); break;
				case 4: System.out.println("Multiplica A2xB2"); break;
			}
			if(N == 4){
				System.out.println("\nMatriz A"); imprimir(aux_A);
				System.out.println("\nMatriz B"); imprimir(aux_B);
				System.out.println("\nMatriz C"); imprimir(aux_C);
			}
			for(int i = 0; i<N/2 ;i++)
				for(int j = 0; j<N/2 ;j++)
					salida.writeInt(aux_C[i][j]);
			entrada.close();
			salida.close();
			conexion.close();
		}
		long t2 = System.currentTimeMillis();
		System.out.println("Tiempo: "+(t2-t1)+"ms");
	}
}
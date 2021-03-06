import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;

class Servidor{
  // lee del DataInputStream todos los bytes requeridos

  static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception{
    while (longitud > 0){
      int n = f.read(b,posicion,longitud);
      posicion += n;
      longitud -= n;
    }
  }

  public static void main(String[] args) throws Exception{
    ServerSocket servidor = new ServerSocket(50000);

    Socket conexion = servidor.accept();

    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());

    // recibe un entero de 32 bits
    int n = entrada.readInt();
    System.out.println(n);

    // recibe un numero punto flotante
    double x = entrada.readDouble();
    System.out.println(x);

    // recibe una cadena
    byte[] buffer = new byte[4];
    read(entrada,buffer,0,4);
    System.out.println(new String(buffer,"UTF-8"));

    // envia una cadena
    salida.write("HOLA".getBytes());

    //Recibe 10000 numeros punto flotante readDouble
    double read = 0.0;
    long startTime = System.nanoTime();
    for(int i = 0; i < 10000; i++)
      read = entrada.readDouble();
    long endTime = System.nanoTime();
    System.out.println("Time servidor readDouble: "+(endTime-startTime)/1000000+"ms");


    //Recibe 10000 numeros punto flotante ByteBuffer
    startTime = System.nanoTime();
    byte[] a = new byte[10000*8];
    read(entrada,a,0,10000*8);
    ByteBuffer b = ByteBuffer.wrap(a);
    endTime = System.nanoTime();
    System.out.println("Time servidor ByteBuffer: "+(endTime-startTime)/1000000+"ms");

    salida.close();
    entrada.close();
    conexion.close();
  }
}
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;

class Cliente{
  // lee del DataInputStream todos los bytes requeridos

  static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception{
    while (longitud > 0){
      int n = f.read(b,posicion,longitud);
      posicion += n;
      longitud -= n;
    }
  }

  public static void main(String[] args) throws Exception{
    Socket conexion = new Socket("localhost",50000);

    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());

    // enva un entero de 32 bits
    salida.writeInt(123);

    // envia un numero punto flotante
    salida.writeDouble(1234567890.1234567890);

    // envia una cadena
    salida.write("hola".getBytes());

    // recibe una cadena
    byte[] buffer = new byte[4];
    read(entrada,buffer,0,4);
    System.out.println(new String(buffer,"UTF-8"));

    //Envío 10000 numeros punto flotante con writeDouble
    long startTime = System.nanoTime();
    for(int i = 0; i<10000; i++)
      salida.writeDouble(Double.valueOf(i+1));
    long endTime = System.nanoTime();
    System.out.println("Time cliente writeDouble: "+(endTime-startTime)/1000000+"ms");

    //Envío 10000 numeros punto flotante ByteBuffer
    startTime = System.nanoTime();
    ByteBuffer b = ByteBuffer.allocate(10000*8);
    for(int i = 0; i<10000; i++)
      b.putDouble(Double.valueOf(i+1));
    byte[] a = b.array();
    salida.write(a);
    endTime = System.nanoTime();
    System.out.println("Time cliente ByteBuffer: "+(endTime-startTime)/1000000+"ms");

    salida.close();
    entrada.close();
    conexion.close();    
  }
}

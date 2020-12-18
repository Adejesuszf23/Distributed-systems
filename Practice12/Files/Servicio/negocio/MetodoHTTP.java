import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MetodoHTTP{
	static class Usuario{
		String email;
		String nombre;
		String apellido_paterno;
		String apellido_materno;
		String fecha_nacimiento;
		String telefono;
		String genero;
		byte[] foto;

		Usuario(String email,String nombre,String apellido_paterno,String apellido_materno,String fecha_nacimiento,String telefono,String genero,byte[] foto){
			this.email = email;
			this.nombre = nombre;
			this.apellido_paterno = apellido_paterno;
			this.apellido_materno = apellido_materno;
			this.fecha_nacimiento = fecha_nacimiento;
			this.telefono = telefono;
			this.genero = genero;
			this.foto = foto;
		}
	}

	public static void main(String args[]){		
		int opt = 1;
		do{
			URL url;
			String parametros = "";
			HttpURLConnection conexion;
			System.out.println("\nMENU");
			System.out.println("a. Alta usuario\nb. Consulta usuario\nc. Borra usuario");
			System.out.print("d. Borra todos los usuarios\ne. Salir\n\nOption: ");
			Scanner teclado = new Scanner(System.in);
			Gson json = new GsonBuilder().create();
			switch(teclado.nextLine()){
				case "a":					
					System.out.print("\nEmail: "); String email = teclado.nextLine();
					System.out.print("Nombre: "); String nombre = teclado.nextLine();
					System.out.print("Apellido paterno: "); String apaterno = teclado.nextLine();
					System.out.print("Apellido materno: "); String amaterno = teclado.nextLine();
					System.out.print("Fecha de nacimiento: "); String fnacimiento = teclado.nextLine();
					System.out.print("Telefono: "); String telefono = teclado.nextLine();
					System.out.print("Genero: "); String genero = teclado.nextLine();
					byte[] foto = null;
					String sjson = json.toJson(new Usuario(email,nombre,apaterno,amaterno,fnacimiento,telefono,genero,foto));
					try{
						url = new URL("http://13.65.7.178:8080/Servicio/rest/ws/alta");
						conexion = (HttpURLConnection)url.openConnection();
						conexion.setDoOutput(true);
						conexion.setRequestMethod("POST");
						conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
						parametros = "usuario="+URLEncoder.encode(sjson,"UTF-8");
						OutputStream os = conexion.getOutputStream();
						os.write(parametros.getBytes());
						os.flush();
						if(conexion.getResponseCode() != HttpURLConnection.HTTP_OK)
							throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
						System.out.println("Usuario registrado!\n");
						conexion.disconnect();
					}catch(Exception except){
						System.err.println(except.toString());
					}
				break;
				case "b":
					System.out.print("Email: "); String emailconsulta = teclado.nextLine();
					try{					
						url = new URL("http://13.65.7.178:8080/Servicio/rest/ws/consulta");
						conexion = (HttpURLConnection)url.openConnection();
						conexion.setDoOutput(true);
						conexion.setRequestMethod("POST");
						conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
						parametros = "email="+URLEncoder.encode(emailconsulta,"UTF-8");
						OutputStream os = conexion.getOutputStream();
						os.write(parametros.getBytes());
						os.flush();
						if(conexion.getResponseCode() != HttpURLConnection.HTTP_OK)
							throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
						BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
						String respuesta = br.readLine();
						if(respuesta!=null){
							Usuario user = (Usuario)json.fromJson(respuesta,Usuario.class);
							System.out.println("\nEmail: "+user.email+"\nNombre: "+user.nombre+"\nApellido paterno: "+user.apellido_paterno);
							System.out.println("Apellido materno: "+user.apellido_materno+"\nFecha de nacimiento: "+user.fecha_nacimiento);
							System.out.println("Telefono: "+user.telefono+"\nGenero: "+user.genero);
						}else{
							System.out.println("Usuario no encontrado!\n");
						}
						conexion.disconnect();
					}catch(Exception e){
						System.err.println(e.getMessage());
					}
	    		break;
	    		case "c":
					System.out.print("Email: "); String emailelimina = teclado.nextLine();
					try{					
						url = new URL("http://13.65.7.178:8080/Servicio/rest/ws/borra");
						conexion = (HttpURLConnection)url.openConnection();
						conexion.setDoOutput(true);
						conexion.setRequestMethod("POST");
						conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
						parametros = "email="+URLEncoder.encode(emailelimina,"UTF-8");
						OutputStream os = conexion.getOutputStream();
						os.write(parametros.getBytes());
						os.flush();
						if(conexion.getResponseCode() != HttpURLConnection.HTTP_OK)
							throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
						System.out.println("Usuario eliminado!\n");
						conexion.disconnect();
					}catch(Exception e){
						System.err.println(e.getMessage());
					}
	    		break;
	    		case "d":
					try{					
						url = new URL("http://13.65.7.178:8080/Servicio/rest/ws/borrar_usuarios");
						conexion = (HttpURLConnection)url.openConnection();
						conexion.setDoOutput(true);
						conexion.setRequestMethod("POST");
						conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
						if(conexion.getResponseCode() != HttpURLConnection.HTTP_OK)
							throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
						System.out.println("Usuarios eliminadoe!\n");
						conexion.disconnect();
					}catch(Exception e){
						System.err.println(e.getMessage());
					}
	    		break;
	    		case "e":
	    			opt = 0;
	    		break;
	    	}
    	}while(opt == 1);
	}
}
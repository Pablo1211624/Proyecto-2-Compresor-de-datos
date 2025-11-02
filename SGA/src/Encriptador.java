import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Encriptador {

    private byte generarClave(char c)
    {
        int p = 5;//numero pequeño
        int g = 100000019;//numero primo grande

        int valorcontra = (int) c;
        int clave = (int) (Math.pow(p, valorcontra) % g);
    
        return (byte) clave;
    }

    public void Encriptar(String entrada, String salida, String password, String NombreSalida) throws IOException
    {
        File origen = new File(entrada);
        File carpeta = new File(salida);
        if(!carpeta.exists()&&!carpeta.mkdirs())
        {
            throw new IOException("No se pudo crear la carpeta de salida: " + carpeta.getAbsolutePath());
        }

        File SalidaCarpeta = new File(carpeta, NombreSalida);
        FileInputStream sc = new FileInputStream(origen);
        FileOutputStream pw = new FileOutputStream(SalidaCarpeta);

        int indexcontra = 0; //para recorrer la contraseña

            int i;
            while((i = sc.read())!=-1)
            {
                char clave = password.charAt(indexcontra);
                indexcontra = (indexcontra + 1) % password.length(); //avanzamos dentro de la contraseña y si finaliza volvemos al principio
                int claveint = generarClave(clave) & 0xFF;
                int cifrado = (char)(i ^ claveint) & 0xFF; //se combina los dos bytes para distorcionar el texto
                pw.write(cifrado);
            }
        sc.close();
        pw.close();

        System.out.println("Archivo encriptado creado: " + SalidaCarpeta.getAbsolutePath());
    }

    public void Desencriotacion(String entrada, String salida, String password, String extension) throws IOException
    {
        File ArchivoEncriptado = new File(entrada);
        File carpeta = new File(salida);
        if(!carpeta.exists()&&!carpeta.mkdirs())
        {
            throw new IOException("No se pudo crear la carpeta de salida: " + carpeta.getAbsolutePath());
        }

        File SalidaCarpeta = new File(carpeta, extension);
        FileInputStream sc = new FileInputStream(ArchivoEncriptado);
        FileOutputStream pw = new FileOutputStream(SalidaCarpeta);
        int indexcontra = 0;
        int i;

            while((i=sc.read())!=-1)
            {
                char clave = password.charAt(indexcontra);
                indexcontra = (indexcontra+1)%password.length();
                int claveInt = generarClave(clave)& 0xFF;
                int decifrado = (char)(i^claveInt)& 0xFF;
                pw.write(decifrado);
            }
        sc.close();
        pw.close();
        System.out.println("Archivo desencriptado creado: " + SalidaCarpeta.getAbsolutePath());
    }
}


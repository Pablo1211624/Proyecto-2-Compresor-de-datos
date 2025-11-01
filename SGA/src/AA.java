import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AA { //Es el que gestiona los archivos y carpetas

    //Atributos
    private boolean esArchivo;
    private boolean esCarpeta;
    private ArrayList<File> archivos; //Por si se trata de una carpeta
    private Compresor compresor;
    private boolean esCarpetaSalida; 

    //Constructor
    public AA(){
        this.esArchivo = false;
        this.esCarpeta = false;
        this.archivos = new ArrayList<>();
        this.compresor = new Compresor();
        this.esCarpetaSalida = false;
    }

    ///Getters
    public boolean getArchivo(){
        return this.esArchivo;
    }

    public boolean getCarpeta(){
        return this.esCarpeta;
    }

    public ArrayList<File> getArchivos(){
        if(this.esCarpeta) {
            return this.archivos;
        } else {
            return null;
        }
    }
    
    ///Métodos
    public void verificarRuta(String ruta){

        File archivo = new File(ruta);

        if(archivo.exists()){
            if(archivo.isFile()){
                this.esArchivo = true;
                this.esCarpeta = false; ///Dejarlos para evitar erroes en la lógica OJOOOOO
            }
            else if(archivo.isDirectory()){
                this.esCarpeta = true;
                this.esArchivo = false;
                obtenerArchivos(archivo);
            }
            else {
                System.err.println("La ruta existe pero no es carpeta ni archivo...\n");
            }
        } else {
            System.err.println("La ruta no existe...\n");
        }
    }

    public void obtenerArchivos(File carpeta){
        
        File[] archivosCargados = carpeta.listFiles();

        if(archivosCargados != null){

            for(File archivo : archivosCargados){
                if(archivo.isFile()){
                    this.archivos.add(archivo);
                }
            }
        } else {
            System.err.println("No se pudieron listar los archivos de la carpeta...\n");
        }
    }

    public Boolean verificarRutaSalida(String ruta){ //Trabajará con carpetas para guardar en lugares distintos

        File archivo = new File(ruta);

        //Verificar si existe
        if(archivo.exists()){
            if(archivo.isDirectory()){
                this.esCarpetaSalida = true;
                return this.esCarpetaSalida;
            }
            else {
                this.esCarpetaSalida = false;
                return this.esCarpetaSalida;
            }
        }
        else {
            archivo.mkdirs();
            this.esCarpetaSalida = true;
            return this.esCarpetaSalida;
        }

    }
    //Necesario para cambiar la extensión de un archivo
    private String cambiarExtension(String ruta, String nuevaExtension) {
        int punto = ruta.lastIndexOf(".");
        if (punto != -1 && punto > ruta.lastIndexOf(File.separator)) {
            return ruta.substring(0, punto) + nuevaExtension;
        } else {
            return ruta + nuevaExtension; // Si no tiene extensión
        }
    }

    private String nombreArchivo(String ruta, String extension){
        int separacion = ruta.lastIndexOf("\\");
        if(separacion != -1 && separacion == ruta.lastIndexOf(File.separator)) {
            String nombreOriginal = ruta.substring(separacion + 1, ruta.length());
            String nombreFinal = cambiarExtension(nombreOriginal, extension);
            return nombreFinal;
        }
        else {
            return null;
        }
    }

    private String cambiarSalida(String ruta, String nombre) {
        if(ruta != null && nombre != null){
            return ruta + "\\" + nombre;
        }
        else {
            System.err.println("Algun error con la ruta y el nombre destino que estan en Null...\n");
            return null;
        }
    }

    public void comprimir(String entrada, String salida){
        
        this.esArchivo = false;
        this.esCarpeta = false;
        this.esCarpetaSalida = false;
        ///Va comprimir dependiendo si es archivo o carpeta
        verificarRuta(entrada);
        Boolean salidaCorrecta = verificarRutaSalida(salida);

        if(!salidaCorrecta){
            System.err.println("Algun error con la direccion destino porque no es correcta...\n");
            return;
        }

        if(esArchivo){

            String nombreArchivo = nombreArchivo(entrada, ".cmp");
            String destino = cambiarSalida(salida, nombreArchivo); //Esto es solo para un archivo - Compresión            

            try{
                this.compresor.compresionArchivo(entrada, destino, nombreArchivo);
                System.out.println("\n--------------------\n");
            } 
            catch(IOException e){
                e.printStackTrace();
                System.err.println("Algun error con la compresion de un archivo...\n");
            }

        } else if (esCarpeta){

            for(File archivoRecorrido : this.archivos){

                String nombreArchivo = nombreArchivo(archivoRecorrido.getAbsolutePath(), ".cmp");
                String destino = cambiarSalida(salida, nombreArchivo);
                String rutaOriginal = archivoRecorrido.getAbsolutePath();
                /*String destinoCarpeta = cambiarExtension(rutaOriginal, ".cmp");*/

                try {
                    this.compresor.compresionArchivo(rutaOriginal, destino, nombreArchivo);
                } catch (IOException e){
                    e.printStackTrace();
                    System.err.println("Algun error con la compresion de los archivos en una carpeta...\n");
                }
            }
            System.out.println("\n--------------------\n");
        } else {
            System.err.println("Verificar algun error en la verificacion de la ruta...\n");
        }
    }

    //Método para descomprimir
    public void descomprimir(String entrada, String salida){

        ///Va descomprimir dependiendo si es archivo o carpeta
        verificarRuta(entrada);

        Boolean salidaCorrecta = verificarRutaSalida(salida);
        if(!salidaCorrecta){
            System.err.println("Algun error con la direccion destino porque no es correcta...\n");
            return;
        }

        if(esArchivo){

            String nombreArchivo = nombreArchivo(entrada, ".txt");
            String nombreArchivoBusqueda = nombreArchivo(entrada, ".cmp");
            String destino = cambiarSalida(salida, nombreArchivo);
            ///String destino = cambiarExtension(ruta, ".txt");

            try{
                this.compresor.descompresionArchivo(entrada, destino, nombreArchivoBusqueda);
                System.out.println("\n--------------------\n");
            } 
            catch(IOException e){
                e.printStackTrace();
                System.err.println("Algun error con la descompresion del archivo...\n");
            }

        } else if (esCarpeta){

            for(File archivoRecorrido : this.archivos){

                String nombreArchivo = nombreArchivo(archivoRecorrido.getAbsolutePath(), ".txt");
                String nombreArchivoBusqueda = nombreArchivo(archivoRecorrido.getAbsolutePath(), ".cmp");
                String destino = cambiarSalida(salida, nombreArchivo);
                String rutaOriginal = archivoRecorrido.getAbsolutePath();

                try {
                    this.compresor.descompresionArchivo(rutaOriginal, destino, nombreArchivoBusqueda);
                } catch (IOException e){
                    e.printStackTrace();
                    System.err.println("Algun error con la descompresion de los archivos en carpeta...\n");
                }
            }
            System.out.println("\n--------------------\n");
        } else {
            System.err.println("Verificar algun error en la descompresion de la ruta...\n");
        }
    }

}
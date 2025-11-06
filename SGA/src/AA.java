import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AA { //Es el que gestiona los archivos y carpetas

    //Atributos
    private boolean esArchivo;
    private boolean esCarpeta;
    private ArrayList<File> archivos; //Por si se trata de una carpeta
    private Compresor compresor;
    private boolean esCarpetaSalida; 
    private Encriptador encriptador;

    //Constructor
    public AA(){
        this.esArchivo = false;
        this.esCarpeta = false;
        this.archivos = new ArrayList<>();
        this.compresor = new Compresor();
        this.esCarpetaSalida = false;
        this.encriptador = new Encriptador();
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
            String nombreArchivoBusqueda = Paths.get(entrada).getFileName().toString();
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

                if (!archivoRecorrido.getName().toLowerCase().endsWith(".cmp")) continue;
                String nombreArchivo = nombreArchivo(archivoRecorrido.getAbsolutePath(), ".txt");
                String nombreArchivoBusqueda = archivoRecorrido.getName();
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


    private String baseName(File f) {
        String name = f.getName();
        int dot = name.lastIndexOf('.');
        return (dot >= 0) ? name.substring(0, dot) : name;
    }

    public void Encriptar(String entrada, String salida,String contraseña)
    {
        //reseteamos los valores
        this.esArchivo = false;
        this.esCarpeta = false;
        verificarRuta(entrada);

        if (esArchivo)
        {
            try
            {
                File entradaA = new File((entrada));
                String nombre = baseName(entradaA) + ".enc";
                this.encriptador.Encriptar(entrada, salida, contraseña, nombre);
            } catch (IOException e)
            {
                e.printStackTrace();
                System.out.println("Error al encriptar el archivo..\n");
            }
        }
        else if (esCarpeta)
        {
            for(File f : this.archivos)
            {
                try
                {
                    String nombre = baseName(f) + ".enc";
                    this.encriptador.Encriptar(f.getAbsolutePath(), salida, contraseña, nombre);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                    System.out.println("Error en la ecriptacion de: " + f.getAbsolutePath() +"\n");
                }
            }
        }
        else
        {
            System.out.println("Verifica que la ruta sea correcta.\n");
        }
    }

    public void Desencriptar(String entrada, String salida, String contraseña)
    {
        this.esArchivo = false;
        this.esCarpeta = false;
        verificarRuta(entrada);

        if(esArchivo)
        {
            try
            {
                this.encriptador.Desencriotacion(entrada, salida, contraseña, ".txt");
            }catch(IOException e)
            {
                e.printStackTrace();
                System.out.println("Error al desencriptar.\n");
            }
        }
        else if (esCarpeta)
        {
            for(File f : this.archivos)
            {
                try
                {
                    this.encriptador.Desencriotacion(f.getAbsolutePath(), salida, contraseña, ".txt");
                }catch(IOException e)
                {
                    e.printStackTrace();
                    System.out.println("Error al desencriptar: " + f.getAbsolutePath() + "\n");
                }
            }
        }
    }

    public void ComrpimiryEncriptar(String entrada, String salida, String password)
    {
        long inicioTiempo = System.currentTimeMillis();
        this.esArchivo = false;
        this.esCarpeta = false;
        this.esCarpetaSalida = false;
        verificarRuta(entrada);
        boolean Salidaok1 = verificarRutaSalida(salida);

        if(!Salidaok1)
        {
            System.err.println("La carpeta de salida no es válida.\n");
            return;
        }

        if(esArchivo)
        {
            File archivoOrig = new File(entrada);
            long tamanoOriginal = archivoOrig.length();
            try{
            String Nombrecmp = nombreArchivo(entrada, ".cmp");
            String rutaCmp = cambiarSalida(salida, Nombrecmp);

            this.compresor.compresionArchivo(entrada, rutaCmp, Nombrecmp);

            String nombreEc = nombreArchivo(entrada, ".ec");
            this.encriptador.Encriptar(rutaCmp, salida, password, nombreEc);
            File archivoFinal = new File(salida, nombreEc);
            long tamanoFinal = archivoFinal.length(); 
            long finTiempo = System.currentTimeMillis();
            long tiempoTotal = finTiempo - inicioTiempo;
            Logger.guardarOperacion("Comprimir+Encriptar", entrada, tiempoTotal, archivoFinal.getAbsolutePath(), tamanoOriginal, tamanoFinal);

            }catch(IOException e)
            {
                e.printStackTrace();
                System.err.println("Error en comprimir+encriptar (archivo).\n");
            }
        }
        else if (esCarpeta)
        {
            for (File f : this.archivos)
            {
                try
                {
                    String rutaOriginal = f.getAbsolutePath();
                    String nombreCmp = nombreArchivo(rutaOriginal, ".cmp");
                    String rutaCmp   = cambiarSalida(salida, nombreCmp);

                    this.compresor.compresionArchivo(rutaOriginal, rutaCmp, nombreCmp);

                    String nombreEc = nombreArchivo(rutaOriginal, ".ec");
                    this.encriptador.Encriptar(rutaCmp, salida, password, nombreEc);
                    System.out.println("Archivo final .ec en: " + cambiarSalida(salida, nombreEc));
                }catch(IOException e)
                {
                    e.printStackTrace();
                    System.err.println("Error en desencriptar+descomprimir: " + f.getAbsolutePath() + "\n");
                }
            }
        }
        else
        {
            System.err.println("Ruta de entrada inválida.\n");
        }
    }

    public void DesencriptaryDescromprimir(String entrada, String salida, String password)
    {
        long inicioTiempo = System.currentTimeMillis();
        this.esArchivo = false;
        this.esCarpeta = false;
        this.esCarpetaSalida = false;
        verificarRuta(entrada);
        boolean Salidaok1 = verificarRutaSalida(salida);

        if(!Salidaok1)
        {
            System.err.println("La carpeta de salida no es válida.\n");
            return;
        }

        if(esArchivo)
        {
            try{
                File archivoOrig = new File(entrada);
                long tamanoOriginal = archivoOrig.length();
                File archivoEc = new File(entrada);
                String nombreBase = baseName(archivoEc);
                
                String nombreCmpExacto = nombreBase + ".cmp";
                String rutaCmpTemporal = cambiarSalida(salida, nombreCmpExacto);

                this.encriptador.Desencriotacion(entrada, salida, password, nombreCmpExacto);

                String nombreFinal = nombreBase + ".txt";
                String rutaFinal = cambiarSalida(salida, nombreFinal);

                this.compresor.descompresionArchivo(rutaCmpTemporal, rutaFinal, nombreCmpExacto);
                System.out.println("Archivo descomprimido en: " + rutaFinal);
                long finTiempo = System.currentTimeMillis(); 
                long tiempoTotal = finTiempo - inicioTiempo;
                File archivoFinal = new File(rutaFinal);
                long tamanoFinal = archivoFinal.length();
                Logger.guardarOperacion("Desencriptar+Descomprimir", entrada, tiempoTotal, archivoFinal.getAbsolutePath(), tamanoOriginal, tamanoFinal);
           
                }catch(IOException e)
                {
                    e.printStackTrace();
                    System.err.println("Error en desencriptar+descomprimir (archivo).\n");
                }
        }
        else if (esCarpeta)
        {
            for (File f : this.archivos)
            {
                try
                {
                    String rutaArchivoEc = f.getAbsolutePath();
                    String nombreBase = baseName(f);
                
                    String nombreCmpExacto = nombreBase + ".cmp";
                    String rutaCmpTemporal = cambiarSalida(salida, nombreCmpExacto);

                    this.encriptador.Desencriotacion(rutaArchivoEc, salida, password, nombreCmpExacto);

                    String nombreFinal = nombreBase + ".txt";
                    String rutaFinal = cambiarSalida(salida, nombreFinal);

                    this.compresor.descompresionArchivo(rutaCmpTemporal, rutaFinal, nombreCmpExacto);
                    System.out.println("Archivo descomprimido en: " + rutaFinal);
                }catch(IOException e)
                {
                    e.printStackTrace();
                    System.err.println("Error en desencriptar+descomprimir: " + f.getAbsolutePath() + "\n");
                }
            }
        }
        else
        {
            System.err.println("Ruta de entrada inválida.\n");
        }
    }

}

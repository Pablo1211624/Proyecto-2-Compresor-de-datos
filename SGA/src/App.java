public class App {
    public static void main(String[] args) throws Exception {
        
        AA admin = new AA();

        //Primero probamos la compresión de archivo único
        admin.comprimir("C:\\Users\\Tony Alexander\\Documents\\Cuarto Semestre\\Estructura de Datos II\\Proyectos\\SGA\\prueba.txt", "C:\\Users\\Tony Alexander\\Documents\\Cuarto Semestre\\Estructura de Datos II\\Proyectos\\SGA\\Carpeta Compresiones");

        ///Compresión de archivos en una carpeta
        admin.comprimir("C:\\Users\\Tony Alexander\\Documents\\Cuarto Semestre\\Estructura de Datos II\\Proyectos\\SGA\\Carpeta Archivos", "C:\\Users\\Tony Alexander\\Documents\\Cuarto Semestre\\Estructura de Datos II\\Proyectos\\SGA\\Carpeta Compresiones");
        
        //Pruebas de descompresión
        admin.descomprimir("C:\\Users\\Tony Alexander\\Documents\\Cuarto Semestre\\Estructura de Datos II\\Proyectos\\SGA\\Carpeta Compresiones", "C:\\Users\\Tony Alexander\\Documents\\Cuarto Semestre\\Estructura de Datos II\\Proyectos\\SGA\\Carpeta Descompresiones");
    }
}

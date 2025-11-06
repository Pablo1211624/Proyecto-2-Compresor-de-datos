public class App {
    public static void main(String[] args) throws Exception {
        Menu menu = new Menu();
        menu.menuP();

        AA admin = new AA();
        String entrada = "C:\\Users\\bronc\\OneDrive - Universidad Rafael Landivar\\Documentos\\Landivar\\Tareas 2025\\4to semetre 2025\\Estructura de datos II\\Proyecto2\\Proyecto-2-Compresor-de-datos\\SGA\\Carpeta Archivos";
        String salida1 = "C:\\Users\\bronc\\OneDrive - Universidad Rafael Landivar\\Documentos\\Landivar\\Tareas 2025\\4to semetre 2025\\Estructura de datos II\\Proyecto2\\Proyecto-2-Compresor-de-datos\\SGA\\Carpeta Compresiones";
        String salida5 = "C:\\Users\\bronc\\OneDrive - Universidad Rafael Landivar\\Documentos\\Landivar\\Tareas 2025\\4to semetre 2025\\Estructura de datos II\\Proyecto2\\Proyecto-2-Compresor-de-datos\\SGA\\Carpeta Descompresiones";
        String salida = "C:\\Users\\bronc\\OneDrive - Universidad Rafael Landivar\\Documentos\\Landivar\\Tareas 2025\\4to semetre 2025\\Estructura de datos II\\Proyecto2\\Proyecto-2-Compresor-de-datos\\SGA\\Carpeta Encriptados";
        String salida2 = "C:\\Users\\bronc\\OneDrive - Universidad Rafael Landivar\\Documentos\\Landivar\\Tareas 2025\\4to semetre 2025\\Estructura de datos II\\Proyecto2\\Proyecto-2-Compresor-de-datos\\SGA\\Carpeta Desencriptados";
        String salida3 = "C:\\Users\\bronc\\OneDrive - Universidad Rafael Landivar\\Documentos\\Landivar\\Tareas 2025\\4to semetre 2025\\Estructura de datos II\\Proyecto2\\Proyecto-2-Compresor-de-datos\\SGA\\Carpeta Compreso y Encriptado";
        String salida4 = "C:\\Users\\bronc\\OneDrive - Universidad Rafael Landivar\\Documentos\\Landivar\\Tareas 2025\\4to semetre 2025\\Estructura de datos II\\Proyecto2\\Proyecto-2-Compresor-de-datos\\SGA\\Carpeta Desencriptado y descomprimido";

        //admin.comprimir(entrada, salida1);

        //admin.comprimir("C:\\Users\\Tony Alexander\\Documents\\Cuarto Semestre\\Estructura de Datos II\\Proyectos\\SGA\\Carpeta Archivos", "C:\\Users\\Tony Alexander\\Documents\\Cuarto Semestre\\Estructura de Datos II\\Proyectos\\SGA\\Carpeta Compresiones");
        
        //Pruebas de descompresión
        //admin.descomprimir("C:\\Users\\Tony Alexander\\Documents\\Cuarto Semestre\\Estructura de Datos II\\Proyectos\\SGA\\Carpeta Compresiones", "C:\\Users\\Tony Alexander\\Documents\\Cuarto Semestre\\Estructura de Datos II\\Proyectos\\SGA\\Carpeta Descompresiones");
        
        //admin.Encriptar(entrada, salida, "1234");
        //admin.Desencriptar(salida, salida2, "1234");
        //admin.comprimir(entrada, salida1);
        //admin.descomprimir(salida1, salida5);
        admin.ComrpimiryEncriptar(entrada, salida3, "1234");
        admin.DesencriptaryDescromprimir(salida3, salida4, "1234");
        

    }
}

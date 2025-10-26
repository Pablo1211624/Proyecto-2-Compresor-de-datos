package Proyecto2;
import java.io.IOException;


public class Proyecto2 {
    public static void main(String[] args) {
        compresor comprimidor = new compresor();
        try {
            comprimidor.compresionArchivo("C:\\Users\\bronc\\OneDrive - Universidad Rafael Landivar\\Documentos\\Landivar\\Tareas 2025\\4to semetre 2025\\Estructura de datos II\\Proyecto2\\Proyecto2\\prueba1.text", "C:\\Users\\bronc\\OneDrive - Universidad Rafael Landivar\\Documentos\\Landivar\\Tareas 2025\\4to semetre 2025\\Estructura de datos II\\Proyecto2\\Proyecto2\\prueba1.cmp");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

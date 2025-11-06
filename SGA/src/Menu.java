import java.util.Scanner;

public class Menu {
    private AA accion;
    Scanner scanner=new Scanner(System.in);

    public Menu(){
        this.accion=new AA();
    }

        String entrada = "C:\\Users\\bronc\\OneDrive - Universidad Rafael Landivar\\Documentos\\Landivar\\Tareas 2025\\4to semetre 2025\\Estructura de datos II\\Proyecto2\\Proyecto-2-Compresor-de-datos\\SGA\\Carpeta Archivos";
        String salida1 = "C:\\Users\\bronc\\OneDrive - Universidad Rafael Landivar\\Documentos\\Landivar\\Tareas 2025\\4to semetre 2025\\Estructura de datos II\\Proyecto2\\Proyecto-2-Compresor-de-datos\\SGA\\Carpeta Compresiones";
        String salida5 = "C:\\Users\\bronc\\OneDrive - Universidad Rafael Landivar\\Documentos\\Landivar\\Tareas 2025\\4to semetre 2025\\Estructura de datos II\\Proyecto2\\Proyecto-2-Compresor-de-datos\\SGA\\Carpeta Descompresiones";
        String salida = "C:\\Users\\bronc\\OneDrive - Universidad Rafael Landivar\\Documentos\\Landivar\\Tareas 2025\\4to semetre 2025\\Estructura de datos II\\Proyecto2\\Proyecto-2-Compresor-de-datos\\SGA\\Carpeta Encriptados";
        String salida2 = "C:\\Users\\bronc\\OneDrive - Universidad Rafael Landivar\\Documentos\\Landivar\\Tareas 2025\\4to semetre 2025\\Estructura de datos II\\Proyecto2\\Proyecto-2-Compresor-de-datos\\SGA\\Carpeta Desencriptados";
        String salida3 = "C:\\Users\\bronc\\OneDrive - Universidad Rafael Landivar\\Documentos\\Landivar\\Tareas 2025\\4to semetre 2025\\Estructura de datos II\\Proyecto2\\Proyecto-2-Compresor-de-datos\\SGA\\Carpeta Compreso y Encriptado";
        String salida4 = "C:\\Users\\bronc\\OneDrive - Universidad Rafael Landivar\\Documentos\\Landivar\\Tareas 2025\\4to semetre 2025\\Estructura de datos II\\Proyecto2\\Proyecto-2-Compresor-de-datos\\SGA\\Carpeta Desencriptado y descomprimido";
        String password="1234";


    private void comprimirArchivos(){
        System.out.println("Comprimiendo: " + entrada + " a " + salida1);
        accion.comprimir(entrada, salida1);
    }

    private void descomprimirArchivos(){
        System.out.println("Descomprimiendo: " + salida1 + " a " + salida5);
        accion.descomprimir(salida1, salida5);
    }

    private void encriptarArchivos(){
        System.out.println("Encriptando: " + entrada + " a " + salida);
        accion.Encriptar(entrada, salida, password);
    }

    private void desencriptarArchivos(){
        System.out.println("Desencriptando: " + salida + " a " + salida2);
        accion.Desencriptar(salida, salida2, password);
    }

    private void comprimirEncriptarArchivos(){
        System.out.println("Comprimiendo y encriptando: " + entrada + " a " + salida3);
        accion.ComrpimiryEncriptar(entrada, salida3, password);
    }

    private void descomprimirDesencriptarArchivos(){
        System.out.println("Descomprimiendo y desencriptando: " + salida3 + " a " + salida4);
        accion.DesencriptaryDescromprimir(salida3, salida4, password);
    }
    public void menuP(){
        int opcion=0;

        while(opcion!=7){
            System.out.println("|°°°|°°°°°|MENU SISTEMA DE GESTION DE ARCHIVOS|°°°°°|°°°|");
            System.out.println("1. Compresion de archivos");
            System.out.println("2. Descompresion de archivos");
            System.out.println("3. Encriptacion de archivos");
            System.out.println("4. Desencriptacion de archivos");
            System.out.println("5. Compresion y encriptacion de archivos");
            System.out.println("6. Desompresion y desencriptacion de archivos");
            System.out.println("7. salir");

            System.out.print("Elija una opcion para realizar: ");
            opcion= scanner.nextInt();
            scanner.nextLine();

            switch(opcion){
                case 1:
                comprimirArchivos();
                break;
                case 2:
                descomprimirArchivos();
                break;
                case 3:
                encriptarArchivos();
                break;
                case 4:
                desencriptarArchivos();
                break;
                case 5:
                comprimirEncriptarArchivos();
                break;
                case 6:
                descomprimirDesencriptarArchivos();
                break;
                case 7:
                break;
                default:
                System.out.println("La opcion ingresada no existe, intentarlo nuevamente");
                break;
                
            }


    }
    }
}

import java.util.Scanner;

public class Menu {
    private AA accion;
    Scanner scanner=new Scanner(System.in);

    public Menu(){
        this.accion=new AA();
    }

    private void comprimirArchivos(){
        System.out.println("Ingrese la ruta actual del archivo por comprimir (que este dentro de la carpeta del proyecto): ");
        String entrada = scanner.nextLine();
        System.out.println("Ingrese el nombre/ruta de salida del archivo");
        String salida = scanner.nextLine();
        accion.comprimir(entrada, salida);
    }

        private void descomprimirArchivos(){
        System.out.println("Ingrese la ruta actual del archivo por descomprimir (que este dentro de la carpeta del proyecto): ");
        String entrada = scanner.nextLine();
        System.out.println("Ingrese el nombre/ruta de salida del archivo");
        String salida = scanner.nextLine();
        accion.descomprimir(entrada, salida);
    }

        private void encriptarArchivos(){
        System.out.println("Ingrese la ruta actual del archivo por encriptar (que este dentro de la carpeta del proyecto): ");
        String entrada = scanner.nextLine();
        System.out.println("Ingrese el nombre/ruta de salida del archivo");
        String salida = scanner.nextLine();
        System.out.println("Ingrese la clave para poder encriptar");
        String clave = scanner.nextLine();
        accion.Encriptar(entrada, salida, clave);
    }
       private void desencriptarArchivos(){
        System.out.println("Ingrese la ruta actual del archivo por desencriptar (que este dentro de la carpeta del proyecto): ");
        String entrada = scanner.nextLine();
        System.out.println("Ingrese el nombre/ruta de salida del archivo");
        String salida = scanner.nextLine();
        System.out.println("Ingrese la clave para poder desencriptar");
        String clave = scanner.nextLine();
        accion.Desencriptar(entrada, salida, clave);
    }
        private void comprimirEncriptarArchivos(){
        System.out.println("Ingrese la ruta actual del archivo por comprimir y encriptar (que este dentro de la carpeta del proyecto): ");
        String entrada = scanner.nextLine();
        System.out.println("Ingrese el nombre/ruta de salida del archivo");
        String salida = scanner.nextLine();
        System.out.println("Ingrese la clave para poder comprimir y encriptar");
        String clave = scanner.nextLine();
        accion.ComrpimiryEncriptar(entrada, salida, clave);
    }
        private void descomprimirDesencriptarArchivos(){
        System.out.println("Ingrese la ruta actual del archivo por descomprimir y desencriptar (que este dentro de la carpeta del proyecto): ");
        String entrada = scanner.nextLine();
        System.out.println("Ingrese el nombre/ruta de salida del archivo");
        String salida = scanner.nextLine();
        System.out.println("Ingrese la clave para poder descomprimir y desencriptar");
        String clave = scanner.nextLine();
        accion.DesencriptaryDescromprimir(entrada, salida, clave);
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

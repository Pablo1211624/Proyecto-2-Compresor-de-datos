import java.io.Serializable;
public class AD implements Serializable { //Clase auxiliar para guardar Huffman y realiza la decodificación
    
    //Atributos
    NodoHuffman raiz;
    String nombreArchivo;

    //Constructor
    public AD(NodoHuffman raizEntrante, String nombreA){
        this.raiz = raizEntrante;
        this.nombreArchivo = nombreA;
    }

    //Algunos otros métodos necesarios
    public NodoHuffman getRaiz(){
        return this.raiz;
    }

    public String getNombreA(){
        return this.nombreArchivo;
    }

    public boolean verificacion(String nombre){ //Para realizar la verificación de que es el mismo archivo

        if(nombreArchivo.equals(nombre)){
            return true;
        }
        return false;
    }
}


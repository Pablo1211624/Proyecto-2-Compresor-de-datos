import java.io.Serializable;

public class NodoHuffman implements Comparable<NodoHuffman>, Serializable{
    
    //Atributos
    String tokens; 
    int frecuencia; 
    NodoHuffman izquierda; 
    NodoHuffman derecha; 
    //Constructor
    NodoHuffman(String s, int f) ///Cuando es hoja
    { 
        this.tokens=s; 
        this.frecuencia=f; 
        this.izquierda=null; 
        this.derecha=null; 
    } 
    
    NodoHuffman(NodoHuffman izq, NodoHuffman der) //Cuando es nodo interno
    { 
        this.tokens = null; 
        this.frecuencia= izq.frecuencia + der.frecuencia; 
        this.izquierda = izq; this.derecha=der; } 
        boolean isLeaf() { return tokens != null; }
        @Override public int compareTo(NodoHuffman o) 
        { 
            return Integer.compare(this.frecuencia, o.frecuencia); 
        }
}


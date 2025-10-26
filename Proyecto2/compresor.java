package Proyecto2;
import java.util.*;
import java.io.*;

class NodoHuffman implements Comparable<NodoHuffman>
{
    String tokens; 
    int frecuencia; 
    NodoHuffman izquierda; 
    NodoHuffman derecha; 
    NodoHuffman(String s, int f) 
    { 
        this.tokens=s; 
        this.frecuencia=f; 
        this.izquierda=null; 
        this.derecha=null; 
    } 
    
    NodoHuffman(NodoHuffman izq, NodoHuffman der) 
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

public class compresor {
    int WindowZise;
    int Lookahead_buffersize;
    NodoHuffman raizhuffman = null;
    Map<String, Integer> frecuencias;
    PriorityQueue<NodoHuffman> nodos;

    public compresor()
    {
        this.WindowZise = 6452342;
        this.Lookahead_buffersize = 35435342;
        this.frecuencias = new HashMap<>();
        this.nodos = new PriorityQueue<>();
    }

    public List<int[]> Lz77_compress(String texto)
    {
        List<int[]> compress_data = new ArrayList<>();
        int i = 0;

        while(i<texto.length())
        {
            int search_window_start = Math.max(0, i-WindowZise);
            String search_window = texto.substring(search_window_start, i);

            int lookahead_end = Math.min(texto.length(), i + Lookahead_buffersize);
            String Lookahead_buffer = texto.substring(i,lookahead_end);

            int best_match_offset = 0;
            int best_match_lenght = 0;

            for (int j = 1; j<Lookahead_buffer.length();++j)
            {
                String substring_to_mach = Lookahead_buffer.substring(0,j);

                if (best_match_lenght > 0) 
                {
                    search_window = texto.substring(search_window_start, i) + Lookahead_buffer.substring(0, j-1);
                }
                int positiion_in_search_window = search_window.indexOf(substring_to_mach);
                
                if (positiion_in_search_window != -1)
                {
                    best_match_offset = i - search_window_start - positiion_in_search_window;
                    best_match_lenght = j;
                }
                else
                {
                    break;
                }
            }
            if (best_match_lenght > 0)
            {
                int next_char_index = i + best_match_lenght;
                char next_char;
                if (next_char_index < texto.length())
                {
                    next_char = texto.charAt(next_char_index);
                }
                else
                {
                    next_char = '\0';
                }
                compress_data.add(new int[]{best_match_offset, best_match_lenght, next_char});
                i += best_match_lenght +1 ;
            }
            else
            {
                compress_data.add(new int[]{0, 0, texto.charAt(i)});
                i += 1;
            }
        }
        return compress_data;
    }

    public static String Lz77_descompress(List<int[]> compress_data)
    {
        StringBuilder decompress_data = new StringBuilder();
        for (int[] data : compress_data)
        {
            int offset = data[0];
            int lengt = data[1];
            char next_char = (char) data[2];
        

        if (offset > 0 && lengt > 0)
        {
            int start = decompress_data.length() - offset;
            for (int i = 0; i < lengt; i++)
            {
                decompress_data.append(decompress_data.charAt(start + i));
            }
        }
        if (next_char != '\0')
        {
            decompress_data.append(next_char);
        }
    }
        return decompress_data.toString();
    }

    //vamos a convertir los tres datos del lz77 [0,0,character] a strings para que huffman los lea
    public List<String> convertiraString(List<int[]> lz77comprimido)
    {
        List<String> tokens = new ArrayList<>();
        for (int[] data : lz77comprimido)
        {
            int offset = data[0];
            int lengt = data[1];
            char next_char = (char) data[2];
        

            if (offset == 0 && lengt == 0)
            {
                //no tiene referencia
                tokens.add("character:" + next_char);
            }else
            {
                //este si es una referencia
                tokens.add("Ref:" + offset + "," + lengt);
                if (next_char != '\0')
                {
                    tokens.add("character:"+ next_char);
                }
            }
        }
        return tokens;
    }

    //y este metodo trae los datos de string a estilo LZ77
    public List<int[]> Stringalz77(List<String> tokens)
    {
        List<int[]> Lz77convertidos = new ArrayList<>();
        int i = 0;
        while (i < tokens.size()) {
            String linea = tokens.get(i);
            if(linea.startsWith("character:"))
            {
                //este no tiene referencias osea es 0,0,character
                char caracter = linea.charAt(10);
                Lz77convertidos.add(new int[]{0,0,caracter});
                i++;
            }
            else if (linea.startsWith("Ref:"))
            {
                String referencias = linea.substring(4);
                String[] partes = referencias.split(",");
                int offset = Integer.parseInt(partes[0]);
                int lengt = Integer.parseInt(partes[1]);

                char caracter = '\0';
                if (i+1 < tokens.size() && tokens.get(i+1).startsWith("character:"))
                {
                    caracter = tokens.get(i+1).charAt(10);
                    i+=2;
                }
                Lz77convertidos.add(new int[]{offset,lengt,caracter});
            }
        }

        return Lz77convertidos;
    }

        public NodoHuffman ArbolHuffman(PriorityQueue<NodoHuffman> nodos)
    {
        while(nodos.size()>1)
        {
            NodoHuffman A = nodos.poll();
            NodoHuffman B = nodos.poll();
            NodoHuffman padre = new NodoHuffman(A, B);
            nodos.offer(padre);
        }
        return nodos.poll();
    }

    public void CodigoHuffman(NodoHuffman nodo, String codigo, Map<String,String> codigos)
    {
        if (nodo==null) return;

        if (nodo.isLeaf())
        {
            codigos.put(nodo.tokens, codigo.isEmpty()? "0": codigo);
        }

        CodigoHuffman(nodo.izquierda, codigo+"0", codigos);
        CodigoHuffman(nodo.derecha, codigo+"1", codigos);
    }
    
    public String codificarCadena(List<String> texto, Map<String,String> codigos)
    {
        StringBuilder resultados = new StringBuilder();
        for(String t: texto)
        {
            resultados.append(codigos.get(t));
        }
        return resultados.toString();
    }
    //este metodo va a leer el archivo y utilizar tanto huffman como lz77 para comprimir
    public void compresionArchivo(String entrada, String salida) throws IOException
    {
        if (!salida.toLowerCase().endsWith(".cmp")) {
        salida = salida + ".cmp";
        }
        this.nodos.clear();
        this.frecuencias.clear();

        File archivo = new File(entrada);
        //leemos todo el archivo
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), "UTF-8"))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (!first) sb.append('\n');
                sb.append(line);
                first = false;
            }
        }
        
        String data = sb.toString();
        //convertimos el mensaje a tokens tipo lz77
        List<int[]> Lztokens = Lz77_compress(data);
        //los tokens los convertimos a strings para huffman
        List<String> stringTokens = convertiraString(Lztokens);
        //empezamos a comprimir con huffman con los tokens ya convertidos
        for(String t: stringTokens)
        {
            this.frecuencias.put(t, frecuencias.getOrDefault(t, 0)+1);
        }

        for(Map.Entry<String,Integer> entry: frecuencias.entrySet())
        {
            this.nodos.offer(new NodoHuffman(entry.getKey(), entry.getValue()));
        }

        raizhuffman = ArbolHuffman(this.nodos);
        Map<String,String> codificado = new HashMap<>();
        CodigoHuffman(raizhuffman, "", codificado);
        //aqui esta la cadena final en huffman
        String cadena = codificarCadena(stringTokens, codificado);

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(salida), "UTF-8"))) {
            pw.println(cadena);
        }

        long originalSizeBytes = data.getBytes("UTF-8").length;
        long compressedSizeBytes = new File(salida).length(); // .cmp como texto
        double ratio = (double)compressedSizeBytes / (double)originalSizeBytes;
        double ahorro = 100.0 * (1.0 - ratio);

        System.out.println("Archivo original : " + entrada +
        " (" + originalSizeBytes + " bytes)");
        System.out.println("Archivo .cmp     : " + salida +
        " (" + compressedSizeBytes + " bytes)");
        System.out.println("Tasa compresión  : " + String.format("%.3f", ratio));
        System.out.println("Ahorro           : " + String.format("%.2f", ahorro) + "%");
    }

}

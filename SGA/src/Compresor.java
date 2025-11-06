import java.util.*;
import java.io.*;
public class Compresor { //Comprimir y descomprimir utilizando LZ77 y Huffman
    
    ///Atributos
    int WindowZise;
    int Lookahead_buffersize;
    NodoHuffman raizhuffman = null;
    Map<Byte, Integer> frecuenciaBytes; // Se cambió a frecuencias de bytes
    Map<String, Integer> frecuencias; 
    PriorityQueue<NodoHuffman> nodos;
    ArrayList<AD> registro; //Para guardar los árboles

    ///Constructor
    public Compresor()
    {
        this.WindowZise = 4096;
        this.Lookahead_buffersize = 256;
        this.frecuencias = new HashMap<>();
        this.frecuenciaBytes = new HashMap<>();
        this.nodos = new PriorityQueue<>();
        this.registro = cargarRegistro();
    }

    //Métodos para LZ77
    public List<int[]> Lz77_compress(String texto)
    {
        List<int[]> compress_data = new ArrayList<>();
        int i = 0;

        while(i < texto.length())
        {
            int search_window_start = Math.max(0, i - WindowZise);
            int lookahead_end = Math.min(texto.length(), i + Lookahead_buffersize);
            
            //Variables para la mejor coincidencia encontrada
            int best_match_offset = 0;
            int best_match_length = 0;
            int best_match_position = -1;
            
            //Buscar la coincidencia más larga en la ventana de búsqueda
            if (search_window_start < i) {
                
                //Buscar desde la posición más antigua hacia la más reciente
                for (int search_pos = search_window_start; search_pos < i; search_pos++) {
                    int match_length = 0; //Num caracteres
                    
                    //Comparar caracteres mientras coincidan y no se salga de los límites
                    while (search_pos + match_length < i && i + match_length < lookahead_end && texto.charAt(search_pos + match_length) == texto.charAt(i + match_length)) {
                        match_length++;
                    }
                    
                    //Si esta coincidencia es mejor que la anterior, guardarla
                    if (match_length > best_match_length) {
                        best_match_length = match_length;
                        best_match_offset = i - search_pos; // Distancia hacia atrás
                        best_match_position = search_pos;
                    }
                }
            }
            
            //Decidir si usar la coincidencia encontrada o un carácter literal
            if (best_match_length >= 3) { // Solo usar coincidencias de 3+ caracteres (más eficiente)
                
                //Obtener el siguiente carácter después de la coincidencia
                int next_char_index = i + best_match_length;
                char next_char; 
                if (next_char_index < texto.length()) {
                    next_char = texto.charAt(next_char_index);
                } else {
                    next_char = '\0'; // Carácter nulo al final del texto
                }
                compress_data.add(new int[]{best_match_offset, best_match_length, next_char});
                
                //Avanzar posición: longitud de coincidencia + 1 (por el siguiente carácter)
                i += best_match_length + (next_char != '\0' ? 1 : 0);
                
            } else {
                // No hay coincidencia útil, usar carácter literal
                compress_data.add(new int[]{0, 0, texto.charAt(i)});
                i++; // Avanzar solo una posición
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
        
            if (offset > 0 && lengt > 0){
                int start = decompress_data.length() - offset;
                if (start >= 0) {
                    for (int i = 0; i < lengt; i++)
                    {
                        decompress_data.append(decompress_data.charAt(start + i));
                    }
                }
            }
            if (next_char != '\0'){
                decompress_data.append(next_char);
            }
        }
        return decompress_data.toString();
    }
    
    //Convertir tokens LZ77 a bytes para Huffman
    public List<Byte> convertirABytes(List<int[]> lz77comprimido)
    {
        List<Byte> bytes = new ArrayList<>();
        
        for (int[] data : lz77comprimido)
        {
            int offset = data[0];
            int length = data[1];
            char next_char = (char) data[2];
            
            if (offset == 0 && length == 0) {
                bytes.add((byte) 255); // Marcador para carácter literal
                bytes.add((byte) (next_char & 0xFF)); // El carácter en sí
                
            } else {
                // REFERENCIA LZ77: Codificar offset y length como bytes
                bytes.add((byte) 254);
                
                bytes.add((byte) (offset >> 8));   // Byte alto del offset
                bytes.add((byte) (offset & 0xFF)); // Byte bajo del offset
                
                // Codificar length (máximo 256, cabe en 1 byte)
                bytes.add((byte) length);
                
                // Agregar siguiente carácter si existe
                if (next_char != '\0') {
                    bytes.add((byte) (next_char & 0xFF));
                } else {
                    bytes.add((byte) 0); // Marcador de fin
                }
            }
        }
        return bytes;
    }

    //Convertir bytes de vuelta a tokens LZ77
    public List<int[]> bytesALz77(List<Byte> bytes)
    {
        List<int[]> Lz77convertidos = new ArrayList<>();
        int i = 0;
        
        while (i < bytes.size()) {
            byte marcador = bytes.get(i);
            
            if (marcador == (byte) 255) {
                if (i + 1 < bytes.size()) {
                    // Convertir byte a char correctamente
                    char caracter = (char) (bytes.get(i + 1) & 0xFF);
                    Lz77convertidos.add(new int[]{0, 0, caracter});
                    i += 2; // Avanzar marcador + carácter
                } else {
                    break; // Datos corruptos
                }
                
            } else if (marcador == (byte) 254) {
                //Referencia
                if (i + 4 < bytes.size()) {
                    // Reconstruir offset desde 2 bytes
                    int offset = ((bytes.get(i + 1) & 0xFF) << 8) | (bytes.get(i + 2) & 0xFF);
                    
                    // Obtener length
                    int length = bytes.get(i + 3) & 0xFF;
                    
                    // Obtener siguiente carácter
                    char next_char = (char) (bytes.get(i + 4) & 0xFF);
                    if (next_char == 0) {
                        next_char = '\0'; // Convertir marcador de fin
                    }
                    
                    Lz77convertidos.add(new int[]{offset, length, next_char});
                    i += 5; // Avanzar todos los bytes de la referencia
                } else {
                    break; // Datos corruptos
                }
            } else {
                // Marcador desconocido o byte corrupto, saltar
                i++;
            }
        }
        return Lz77convertidos;
    }

    //Huffan para bytes
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

    //Generar códigos para bytes
    public void CodigoHuffmanBytes(NodoHuffman nodo, String codigo, Map<Byte, String> codigos)
    {
        if (nodo == null) return;

        if (nodo.isLeaf())
        {
            // Convertir el token string a byte para el mapa
            if (nodo.tokens != null && !nodo.tokens.isEmpty()) {
                byte b = (byte) Integer.parseInt(nodo.tokens);
                codigos.put(b, codigo.isEmpty() ? "0" : codigo);
            }
        }

        CodigoHuffmanBytes(nodo.izquierda, codigo + "0", codigos);
        CodigoHuffmanBytes(nodo.derecha, codigo + "1", codigos);
    }
    
    // Crear árbol de Huffman desde frecuencias de bytes
    public NodoHuffman crearArbolDesdeBytes(List<Byte> datos)
    {
        //Contar frecuencias de cada byte
        Map<Byte, Integer> frecuencias = new HashMap<>();
        for (Byte b : datos) {
            frecuencias.put(b, frecuencias.getOrDefault(b, 0) + 1);
        }
        
        //Crear nodos hoja para cada byte
        PriorityQueue<NodoHuffman> nodos = new PriorityQueue<>();
        for (Map.Entry<Byte, Integer> entry : frecuencias.entrySet()) {
            //Convertir byte a string para compatibilidad con NodoHuffman
            String token = String.valueOf(entry.getKey().intValue());
            nodos.offer(new NodoHuffman(token, entry.getValue()));
        }
        //Construir árbol
        return ArbolHuffman(nodos);
    }
    
    private void guardarRegistro(ArrayList<AD> registro){

        String directorio = System.getProperty("user.dir"); //Directorio actual
        String NArchivo = directorio + "\\data_compresion.dat";
        File archivo = new File(NArchivo);

        try (FileOutputStream escritor = new FileOutputStream(archivo); ObjectOutputStream serializador = new ObjectOutputStream(escritor)) {
            serializador.writeObject(registro); //Serializar tabla de decodificación
        } catch (IOException e) {
            System.err.println("Error al guardar el registro de los arboles...\n");
                e.printStackTrace();
        }
            
    }

    @SuppressWarnings("unchecked")
    private ArrayList<AD> cargarRegistro(){
        String directorio = System.getProperty("user.dir"); //Directorio actual
        String NArchivo = directorio + "\\data_compresion.dat";
        File archivo = new File(NArchivo);

        // Si el archivo no existe o está vacío, devolver lista vacía
        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>();
        }

        try(FileInputStream lector = new FileInputStream(archivo); ObjectInputStream deserializador = new ObjectInputStream(lector)){
            
            Object obj = deserializador.readObject();
            if (obj instanceof ArrayList<?>) {
                return (ArrayList<AD>) obj; //Por el problema del casteo
            }
            return new ArrayList<>();
        } catch (FileNotFoundException e){
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar el registro: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public byte[] codificarCadena(List<String> texto, Map<String, String> codigos){

        //Recolectar los codigos - En String
        StringBuilder resultados = new StringBuilder();
        for(String t: texto)
        {
            resultados.append(codigos.get(t));
        }

        //Convertir string de bits a bytes
        String bitString = resultados.toString();
        int longitud = (bitString.length() + 7 ) / 8;
        byte[] bytes = new byte[longitud+1];

        int padding = (8 - (bitString.length() % 8)) % 8;

        for(int i = 0; i < padding; i++)
        {
            bitString += "0";
        }

        //Primer byte es la cantidad de bits agregados
        bytes[0] = (byte) padding;

        StringBuilder bloque = new StringBuilder();
        int sumador = 1;

        for(int i = 0; i < bitString.length(); i++){
            bloque.append(bitString.charAt(i));
            
            if(bloque.length() == 8){
                int b = Integer.parseInt(bloque.toString(), 2);
                bytes[sumador] = (byte) b;
                sumador++;
                bloque.setLength(0);
            }
        }
        
        //Procesar último bloque si queda algo
        if(bloque.length() > 0){
            int b = Integer.parseInt(bloque.toString(), 2);
            bytes[sumador] = (byte) b;
        }

        return bytes;
    }
    
    //Dar código
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
    
    // Métodos de conversión
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
                tokens.add("character:" + next_char);
            }else
            {
                tokens.add("Ref:" + offset + "," + lengt);
                if (next_char != '\0')
                {
                    tokens.add("character:"+ next_char);
                }
            }
        }
        return tokens;
    }

    public List<int[]> Stringalz77(List<String> tokens)
    {
        List<int[]> Lz77convertidos = new ArrayList<>();
        int i = 0;
        while (i < tokens.size()) {
            String linea = tokens.get(i);
            if(linea.startsWith("character:"))
            {
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
                } else {
                    i++;
                }
                Lz77convertidos.add(new int[]{offset,lengt,caracter});
            }
        }
        return Lz77convertidos;
    }

    //este metodo va a leer el archivo y utilizar tanto huffman como lz77 para comprimir - Compresor directo
    public void compresionArchivo(String entrada, String salida, String nombreArchivo) throws IOException
    {
        long inicioTiempo = System.currentTimeMillis();

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
        
        //Convertir texto a tokens LZ77
        List<int[]> Lztokens = Lz77_compress(data);
        
        //Convertir tokens LZ77 a strings
        List<String> stringTokens = convertiraString(Lztokens);
        
        //Crear frecuencias para Huffman
        for(String t: stringTokens)
        {
            this.frecuencias.put(t, frecuencias.getOrDefault(t, 0)+1);
        }

        for(Map.Entry<String,Integer> entry: frecuencias.entrySet())
        {
            this.nodos.offer(new NodoHuffman(entry.getKey(), entry.getValue())); 
        }

        raizhuffman = ArbolHuffman(this.nodos);
        
        //Guardar árbol en registro para posterior decodificación
        AD aux = new AD(raizhuffman, nombreArchivo);
        this.registro.add(aux);
        guardarRegistro(registro);
        
        //Generar códigos de Huffman
        Map<String,String> codificado = new HashMap<>();
        CodigoHuffman(raizhuffman, "", codificado);
        
        //Codificar usando método original
        byte[] cadena = codificarCadena(stringTokens, codificado);

        //Guardar solo los datos comprimidos
        try (FileOutputStream escritor = new FileOutputStream(salida)) {
            escritor.write(cadena);
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

        long finTiempo = System.currentTimeMillis();
        long tiempoTotal = finTiempo - inicioTiempo;
        Logger.guardarOperacion("Compresion", entrada, tiempoTotal, salida, originalSizeBytes, compressedSizeBytes);
    }

    //Método para descomprimir
    public void descompresionArchivo(String entrada, String salida, String nombre) throws IOException {
        long inicioTiempo = System.currentTimeMillis();

        boolean arbolEncontrado = false; //Bandera para la descompresión
        NodoHuffman raiz = null;
        ArrayList<AD> temp = cargarRegistro();

        //Traer la raíz de huffman si el nombre coincide con el registro
        for(AD aux : temp){
            if(aux.getNombreA().equals(nombre)){
                arbolEncontrado = true;
                raiz = aux.getRaiz();
            }
        }
        //Validación
        if(!arbolEncontrado){
            System.out.printf("No se encontro la raiz huffman del archivo: %s\n", nombre);
            return;
        }

        //Se crea nuevo archivo para leer
        File archivo = new File(entrada);
        long tamanoOriginal = archivo.length();
        byte[] bytesComprimidos;

        try(FileInputStream lector = new FileInputStream(archivo)){
            bytesComprimidos = lector.readAllBytes();
        }
        
        //Convertir bytes a string binario
        int padding = bytesComprimidos[0] & 0xFF;
        StringBuilder binario = new StringBuilder();
        
        for(int i = 1; i < bytesComprimidos.length; i++){
            String byteBinario = String.format("%8s", Integer.toBinaryString(bytesComprimidos[i] & 0xFF)).replace(' ', '0');
            binario.append(byteBinario);
        }
        
        //Remover padding
        if(padding > 0 && binario.length() >= padding){
            binario.setLength(binario.length() - padding);
        }        
        //Decodificar binario a tokens usando árbol de Huffman
        List<String> tokens = new ArrayList<>();
        NodoHuffman actual = raiz;

        for(int j = 0; j < binario.length(); j++){
            char bit = binario.charAt(j);

            if(bit == '0'){
                actual = actual.izquierda;
            }
            else if(bit == '1'){
                actual = actual.derecha;
            }

            // Si se llega a un nodo hoja 
            if(actual.isLeaf()){
                tokens.add(actual.tokens);
                actual = raiz;
            }
        }
        
        //Convertir strings de vuelta a tokens LZ77
        List<int[]> datosLZ = Stringalz77(tokens);
        
        //Descomprimir usando LZ77
        String infoDescomprimida = Lz77_descompress(datosLZ);

        try(PrintWriter escritor = new PrintWriter(new OutputStreamWriter(new FileOutputStream(salida), "UTF-8"))){
            escritor.println(infoDescomprimida);
        }

        long finTiempo = System.currentTimeMillis();
        long tiempoTotal = finTiempo - inicioTiempo;
        File archivoDescomprimido = new File(salida);
        long tamanoNuevo = archivoDescomprimido.length();
        Logger.guardarOperacion("Descompresion", entrada, tiempoTotal, salida, tamanoOriginal, tamanoNuevo);

    }
}
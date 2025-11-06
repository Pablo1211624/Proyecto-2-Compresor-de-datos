import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public static void guardarOperacion(String operacion,String archivoOriginal, long tiempoMs,String archivoNuevo, long tamanoOriginal, long tamanoNuevo){
        
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = formatoFecha.format(new Date());
        String tasaStr = "-";
        if (operacion.contains("Compresion") && tamanoOriginal > 0) {
            double tasa = (double) tamanoNuevo / tamanoOriginal;
            tasaStr = String.format("%.3f", tasa);
        }
        String tamañoOrigStr = String.format("%.1f KB", tamanoOriginal / 1024.0);
        String tamañoNuevoStr = String.format("%.1f KB", tamanoNuevo / 1024.0);
        String lineaLog = String.format("[%s] %-15s | %-25s | %5d ms | Tasa: %-6s | %8s : %8s | %s",
                timestamp,
                operacion,
                new File(archivoOriginal).getName(),
                tiempoMs,
                tasaStr,
                tamañoOrigStr,
                tamañoNuevoStr,
                new File(archivoNuevo).getName());

        System.out.println(lineaLog);
    }
} 

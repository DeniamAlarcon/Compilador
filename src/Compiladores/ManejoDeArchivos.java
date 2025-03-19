
package Compiladores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eduardo Gallegos
 */
public class ManejoDeArchivos {
    public static void guardarArchivo(String texto){
        FileWriter fichero=null;
        PrintWriter pw=null;
       
        try {
            fichero= new FileWriter("C:\\Users\\Deniam\\OneDrive\\Escritorio\\archivo.txt");
            pw= new PrintWriter(fichero);
            pw.println(texto);
        } catch (IOException ex) {
            Logger.getLogger(ManejoDeArchivos.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(fichero!=null){
                try {
                    fichero.close();
                } catch (IOException ex) {
                    Logger.getLogger(ManejoDeArchivos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    public static String cargarArchivo(){
        String cadena="";
        File archivo=null;
        FileReader fr=null;
        BufferedReader br=null;
        archivo=new File("C:\\Users\\Deniam\\OneDrive\\Escritorio\\archivo.txt");
        try {
            fr=new FileReader(archivo);
            br=new BufferedReader(fr);
            String linea;
            while((linea=br.readLine())!=null){
                cadena=cadena+linea+"\n";
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManejoDeArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManejoDeArchivos.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(fr!=null){
                try {
                    fr.close();
                } catch (IOException ex) {
                    Logger.getLogger(ManejoDeArchivos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return cadena;
    }
    
}

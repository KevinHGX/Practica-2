/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testann;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import lombok.Data;

/**
 *
 * @author ACER
 */

@Data
public class NuevosDatos {
    
    private File fichero;
    private Scanner line = null;
    private ArrayList<String> nuevosDatos = new ArrayList<>();
    
    public NuevosDatos(String _nameFile){
        fichero = new File(_nameFile); 
    }
    
    public void extraerDatos(){
        try{
            System.out.println("Leemos el contenido de nuevos datos a modificar...");
            line = new Scanner(fichero);
            
            while(line.hasNextLine()){
                String date = line.nextLine();//
                nuevosDatos.add(date);
            }
            
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }finally{
            try{
                if(line != null){
                    line.close();
                }
            }catch(Exception ex){
                System.out.println("Error: "+ex.getMessage());
            }
        }
    }
}

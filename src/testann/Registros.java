/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testann;

import java.util.Hashtable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author ACER
 */
@Data
@AllArgsConstructor
public class Registros { //PARTE SINCRONIZADA

    private Hashtable<Integer, String> tablaRfc;
    private Hashtable<Integer, String> tablaNombre;
    private Hashtable<Integer, String> tablaDireccion;
    private Hashtable<Integer, String> tablaApellidos;
    
    private int numLectores = 0;
    private boolean hayEscritor = false;

    //Constructor
    public Registros() {
        this.tablaNombre = new Hashtable<>();
        this.tablaApellidos = new Hashtable<>();
        this.tablaDireccion = new Hashtable<>();
        this.tablaRfc = new Hashtable<>();
    }

    //Metodos caso 2 Escritores
    public void updateNombre(int _id, String _nombre) {
        this.tablaNombre.remove(_id);
        this.tablaNombre.put(_id, _nombre);
    }

    public void updateApellidos(int _id, String _apellidos) {
        this.tablaApellidos.remove(_id);
        this.tablaApellidos.put(_id, _apellidos);
    }
    
    public void updateDireccion(int _id, String _direccion) {
        this.tablaDireccion.remove(_id);
        this.tablaDireccion.put(_id, _direccion);
    }
      
    public void InsertarRegistro(int _id, String _nombres, String _apellidos, String _rfcs, String _direcciones) {
        this.tablaNombre.put(_id, _nombres);
        this.tablaApellidos.put(_id, _apellidos);
        this.tablaRfc.put(_id, _rfcs);
        this.tablaDireccion.put(_id, _direcciones);
    }
    
    //testing 
    public void showRegistro(){
        System.out.println(tablaNombre);
        System.out.println(tablaApellidos);
        System.out.println(tablaDireccion);
        System.out.println(tablaRfc);
    }
    
    //Metodos de Lectura
    public void mostrarDatos(int _id){
        System.out.println("Nombre: "+this.tablaNombre.get(_id));
        System.out.println("Apellido: "+this.tablaApellidos.get(_id));
        System.out.println("RFC: "+this.tablaRfc.get(_id));
        System.out.println("Direccion: "+this.tablaDireccion.get(_id));
    }
    
    public void leer(int clave){
    //protocolo deentrada
        synchronized (this){
            while (hayEscritor)
                try{
                    System.out.println("Lector >> waiting...");
                    wait();
                }catch(Exception e){}
            numLectores++;
        }
        
        //mostrarDatos(clave);
        
        synchronized (this){
            numLectores--;
            if (numLectores==0) notifyAll();
        }
    }
    
    synchronized public void escribir(int _clave,String _nombre,String _apellido,String _direccion){
    //protocolo de entrada
        synchronized (this){
            while (hayEscritor || (numLectores>0))
                try{
                    System.out.println("Escritor >> waiting...");
                    wait();
                }catch(Exception e){e.printStackTrace();}
    
           hayEscritor=true;
        }
        /*
        updateNombre(_clave,_nombre);
        updateApellidos(_clave,_apellido);
        updateDireccion(_clave,_direccion);
        */
    
        synchronized (this){
            hayEscritor=false;
            notifyAll();
        }
    }
    
    synchronized public void comprobarClave(int _clave){
        if(!tablaNombre.containsKey(_clave)){
            try{
                while(true){
                    System.out.println("Clave no encontrada en espera...");
                    wait();
                    System.out.println(">>THREAD FREE<<");
                    break;
                }           
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    
    synchronized public void Notificar(){
        notifyAll();
    }
    
    synchronized public void bloquear(String name){
        try{
            while(true){
                System.out.println("Thread :: "+name+" Waiting...");
                wait();//hilo en espera
                //System.out.println("Hilo: "+name+" liberado"); 
                break;
            }            
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

}

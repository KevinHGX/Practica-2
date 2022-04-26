/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testann;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/**
 *
 * @author ACER
 */
public class BaseDatos extends Thread{
    
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    
    private File fichero;
    private static Scanner line = null;
    private Registros temporal;
    private static int contador = 1; 
    private static boolean flag = true,termino = false, flagt = false;
    private static int NumHilos = 0; 
    
    Semaphore semaforo = new Semaphore(1);
    Semaphore sem = new Semaphore(1);
    
    public BaseDatos(String _nameFile,Registros _temporal){
        fichero = new File(_nameFile);
        this.temporal = _temporal;
    }
    
    //aplicando hilos
    public BaseDatos(String _nameFile,Registros _temporal,String _name){
        super(_name);
        fichero = new File(_nameFile);
        this.temporal = _temporal;
    }
    
    public void extraerDatos(){
        try{
            System.out.println("Leemos el contenido...");
            line = new Scanner(fichero);
            
            while(line.hasNextLine()){
                String date = line.nextLine();//
                String[] newStr = date.split(" ");
                temporal.InsertarRegistro((Integer.parseInt(newStr[0])),newStr[1], newStr[2] , newStr[3] , newStr[4]);
            }
            
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }finally{
            System.out.println("Comprobamos que termino");
            try{
                if(line != null){
                    line.close();
                }
            }catch(Exception ex){
                System.out.println("Error: "+ex.getMessage());
            }
        }
    }
    
    
    //concurrente
    @Override
    public void run(){
        try{
                try{
                
                    semaforo.acquire();

                    System.out.println(ANSI_CYAN+"Thread<"+getName()+"> quiere registrar"+ANSI_WHITE);
                    
                    NumHilos++;

                        System.out.println("NumHilos::"+NumHilos);
                    if(flag){
                        System.out.println("Leemos el contenido...");
                        line = new Scanner(fichero);
                        flag = false;
                    }

                    while(contador <= 10){
                        if(line.hasNextLine()){
                        String date = line.nextLine();//
                        String[] newStr = date.split(" ");
                        System.out.println(ANSI_RED+"Dato>> "+newStr[0]+ANSI_WHITE);
                        temporal.InsertarRegistro((Integer.parseInt(newStr[0])),newStr[1], newStr[2] , newStr[3] , newStr[4]);
                        contador++;
                        }
                    }

                    contador = 1;
                    semaforo.release();

                    esUltimo();

                    Thread.sleep(1000);
                
                }catch(InterruptedException e){
                        System.out.println("Thread Interrupted"+e);
                }
            
            
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }finally{
            System.out.println("Comprobamos que termino");
            try{
                sem.acquire();
                
                if(flagt == false){
                    System.out.println("Comprobamos que termino");
                    try{
                        if(line != null){
                            line.close();
                        }
                    }catch(Exception ex){
                        System.out.println("Error: "+ex.getMessage());
                    }
                    sem.release();
                    flagt = true;
                    System.out.println("Cerramos Fichero");
                    System.out.println("Notificamos");
                    this.temporal.Notificar();
                }
                
                
            }catch(InterruptedException e){
                System.out.println("Thread Interrupted "+e);
            }
        
        }
    }
    
    private void esUltimo(){
        System.out.println("Funcion es Ultimo:"+NumHilos);
        if(NumHilos == 10){
            NumHilos = 0;
            contador++;
            System.out.println("Contador: "+contador);
            this.temporal.Notificar();
        }else{
            this.temporal.bloquear(getName());
        }
    }
    
    synchronized private int NumHilosIniciales(){
        return Counter.counterHilosBD++;
    }
    
}

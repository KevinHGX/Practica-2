/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testann;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Semaphore;

/**
 *
 * @author ACER
 */
public class Escritor_Test2 extends Thread{
    
    private static final int MIN_VALOR_INT = 1000;
    private static final int MAX_VALOR_INT = 1100;
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    
    private Registros registros;//Recurso Compartido
    private final ArrayList<String> nuevosDatos;
    private int iteracion = 0;
    private static int contador = 1;
    private static int numH=0;
    
    int nuevosHilos = 3;
    
    //Limitar entradas de hilos a 3;
    static Semaphore semaforo = new Semaphore(3);
    static Semaphore asignacion = new Semaphore(1);
    static Semaphore asignacionBanderas = new Semaphore(1);
    
    private int num_R1,num_R2,num_R3,opcion = 0;
    private static boolean flag = true;
    
    static private int[] banderas = {0,0,0};
    //                              1     2     3                    1
    //                              2     3     1   <<- invertir     2
    //                              3     1     2                    3
    
    public Escritor_Test2(String _name,Registros _registros,ArrayList<String> _stack){
        super(_name);
        this.registros = _registros;
        this.nuevosDatos = _stack;
        this.iteracion = _stack.size();
    }
    
    @Override
    public void run() {
        
        try{
            semaforo.acquire();
            
            NumHilosIniciales();// counter = 3
            
            System.out.println(ANSI_RED+"Intentando Escribir <"+getName()+">"+ANSI_WHITE);
            //bloquearTodo();
            for(int i = 0; i < 2 ;i++){
                //Extraccion de valores 
                //Thread.sleep(2000);
                while(contador <= 3 && flag){
                    //System.out.println("Contador: "+contador);
                    try{
                        asignacion.acquire();
                        Thread.sleep(1000);
                        //TimeUnit.SECONDS.sleep(1);
                        switch(verificarBanderas(contador)){
                            case 1 ->{//
                                System.out.println(ANSI_CYAN+"->"+ANSI_WHITE+"Hilo : "+getName()+" case 1");
                                
                                asignacion.release();
                                esUltimoHilo();
                                banderas[0] = 0;
                                
                            }//TablaNombre
                            case 2 ->{
                                System.out.println(ANSI_RED+"->"+ANSI_WHITE+"Hilo : "+getName()+" case 2");
                                
                                asignacion.release();
                                esUltimoHilo();
                                banderas[1] = 0;
                                
                            }//TablaApellido
                            case 3 ->{
                                System.out.println(ANSI_YELLOW+"->"+ANSI_WHITE+"Hilo : "+getName()+" case 3");
                                
                                asignacion.release();
                                esUltimoHilo();
                                banderas[2] = 0;
                                
                            }//TablaDireccion
                        }
                        Thread.sleep(1000);
                        
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
                
                asignacion.acquire();
                numH++;
                //System.out.println("numH ->:"+numH);
                Thread.sleep(1000);
                if(numH == 3){
                    System.out.println(ANSI_YELLOW+"TERMINO LA >> "+(i+1)+" VUELTA"+ANSI_WHITE);
                    flag = true;
                    numH = 0; 
                    this.registros.Notificar();
                    asignacion.release();
                    System.out.println(ANSI_YELLOW+"-----------------------------------------"+ANSI_WHITE);
                }else{
                    asignacion.release();
                    this.registros.bloquear(getName());
                }
                
            }
            
            System.out.println(ANSI_CYAN+"El Escritor<thread "+getName()+"> a Terminado"+ANSI_WHITE);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        
        NumHilosTerminan();
        synchronized(this){
            semaforo.release();
        }
            
        //notificar a 3 nuevos hilos
        //notificarNuevosHilos();
            
    }
    //--------------------------------------------------------
    private int verificarBanderas(int _contador){
        
        if(_contador == 2){
            opcion = baseDatosLibreInverso();
        }else{
            opcion = baseDatosLibre();
        }
        return opcion;
    }
    
    //     ->>
    private int baseDatosLibre(){
        int aux = 0;
        System.out.println(banderas[0]+"|"+banderas[1]+"|"+banderas[2]);
        for(int i = 0;i < banderas.length;i++){
            if(banderas[i] == 0){
                //verificar que no este dentro                      
                banderas[i] = 1;
                aux = (i+1);
                System.out.println("tabla Libre: "+i+" >> "+banderas[i]);
                break;
            }
        }
        return aux;
    }
    
    //     <<-
    private int baseDatosLibreInverso(){
        int aux = 0;
        System.out.println(banderas[0]+"|"+banderas[1]+"|"+banderas[2]);
        for(int i = banderas.length-1;i > -1;i--){
            if(banderas[i] == 0){
                //verificra que no he cruzado
                banderas[i] = 1;
                aux = (i+1);
                //System.out.println("Tabla Libre: "+i+" >> "+banderas[i]);
                break;
            }
        }
        return aux;
    }
    
    //--------------------------------------------------------  
    synchronized private void NumHilosIniciales(){
        Counter.counter++;
    }
    
    synchronized private void NumHilosTerminan(){
        Counter.counter--;
    }
    
    public void esUltimoHilo(){
        if(Counter.counter == 1){
            contador++;
            if(contador == 4){// termino las 3 vueltas
                this.contador = 1;
                this.flag = false;
            }
            //System.out.println("Iniciamos");
            this.registros.Notificar();
            //System.out.println("Notificamos");
            Counter.counter = 3;
        }else{
            Counter.counter--;
            this.registros.bloquear(getName());
            //System.out.println("flag:: "+flag);
        }
    }
    //--------------------------------------------------------
    
    private int numeroRandom(){
        Random rand = new Random();
        return MIN_VALOR_INT + rand.nextInt((MAX_VALOR_INT-MIN_VALOR_INT)+1);
    }
}

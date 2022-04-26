/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testann;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 *
 * @author ACER
 */
public class Escritor_Test4 extends Thread{
    
    public static final int NUM_MAX_OP = 110;
    private static final int MIN_VALOR_INT = 1000;
    private static final int MAX_VALOR_INT = 1100;
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_YELLOW = "\u001B[33m";
 
    private Registros recurso;
    private ArrayList<String> nuevosDatos;
    private Semaphore semaforo = new Semaphore(1);
    
    public Escritor_Test4(String _name,Registros _recurso,ArrayList<String> _nuevosDatos){
        super(_name);
        this.recurso =_recurso;
        this.nuevosDatos = _nuevosDatos;
    }
    
    @Override
    public void run (){
        
            System.out.println (ANSI_YELLOW+"Escritor<"+getName()+"> quiere escribir"+ANSI_WHITE);
            for(int i = 1;i < NUM_MAX_OP;i++){
                String[] Datos = nuevosDatos.get(i).split(" ");

                //System.out.println ("Escritor<"+getName()+"> quiere escribir");
                recurso.escribir(numeroRandom(),Datos[0],Datos[1],Datos[2]);
                //System.out.println ("Escritor<"+getName()+"> ha terminado");
            }
        
        
        System.out.println(ANSI_CYAN+">>   ESCRITOR<"+getName()+"> A TERMINADO   <<"+ANSI_WHITE);
        
   
    }
    
    private int numeroRandom(){
        Random rand = new Random();
        return MIN_VALOR_INT + rand.nextInt((MAX_VALOR_INT-MIN_VALOR_INT)+1);
    }
}

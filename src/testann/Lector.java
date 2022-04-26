/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testann;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 *
 * @author ACER
 */
public class Lector extends Thread {
    public static final int NUM_MAX_OP = 110;
    private static final int MIN_VALOR_INT = 1000;
    private static final int MAX_VALOR_INT = 1100;
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    
    private Registros recurso;
    
    public Lector(String _name,Registros _recurso){
        super(_name);
        this.recurso = _recurso;
    }
    
    @Override
    public void run (){
        
        System.out.println (ANSI_YELLOW+"Lector <"+getName()+"> quiere leer"+ANSI_WHITE);
            for(int i = 1;i < NUM_MAX_OP;i++){
                //System.out.println ("Lector <"+getName()+"> quiere leer");
                recurso.leer(numeroRandom());
                //System.out.println ("Lector <"+getName()+"> ha terminado de leer");
            }
        
        System.out.println(ANSI_RED+"--- LECTOR <"+getName()+"> A TERMIANDO ---"+ANSI_WHITE);
        
    }
    
    private int numeroRandom(){
        Random rand = new Random();
        return MIN_VALOR_INT + rand.nextInt((MAX_VALOR_INT-MIN_VALOR_INT)+1);
    }
        
}

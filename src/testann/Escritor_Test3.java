/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testann;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author ACER
 */
public class Escritor_Test3 extends Thread{
    private static final int MIN_VALOR_INT = 1000;
    private static final int MAX_VALOR_INT = 1100;
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_WHITE = "\u001B[37m";
    
    private Registros registros;//Recurso Compartido
    private final ArrayList<String> nuevosDatos;
    private final Semaphore semaforo;
    
    private int iteracion;
    private long start,end;
    
    public Escritor_Test3(String _name,Registros _registros,ArrayList<String> _stack,Semaphore _semaforo){
        super(_name);
        this.registros = _registros;
        this.nuevosDatos = _stack;
        this.semaforo = _semaforo;
    }
    
    @Override
    public void run() {
        System.out.println(ANSI_RED+"Intentando Escribir <"+getName()+">"+ANSI_WHITE);
        
        try {
            //bloquearTodo();
            
            modificar();
          
            // Puedes utilizar otras unidades de medida como MILLISECONDS, MICROSECONDS, etc. Solo recuerda aumentar el valor máximo del random
            TimeUnit.SECONDS.sleep(5);
            System.out.println(ANSI_RED+"Thread >> "+getName()+" ha terminado"+ANSI_WHITE);
            
        } catch (InterruptedException e) {
            // Si lo que quieres es imprimir la excepción debes utilizar printStackTrace() para ver la traza completa.
            e.printStackTrace();
        } finally {
            // En caso que la ejecución del programa falle se deben liberar los recursos en finally
            // para garantizar que los recursos siempre son liberados después de ser adquiridos.
            //liberarTodo();
        }
    }
    
    private void bloquearTodo() throws InterruptedException {
        this.semaforo.acquire(Main.NUM_HILOS_MAX);
    }

    private void liberarTodo() {
        this.semaforo.release(Main.NUM_HILOS_MAX);
    }
    
    private void modificar(){
        this.iteracion = nuevosDatos.size();// 110
        for(int i = 0;i < iteracion; i++){
            String[] Datos = nuevosDatos.get(i).split(" ");
            //generar mi id random dentro del rango 1000 1100
            int num = numeroRandom(); 
            synchronized(registros){
                this.registros.comprobarClave(num);
                this.registros.updateNombre(num,Datos[0]);
                this.registros.updateApellidos(num, Datos[1]);
                this.registros.updateDireccion(num, Datos[2]);
            }
            //System.out.println(ANSI_RED+"Hilo: "+getName()+" Ha escrito: "+(i+1)+ANSI_WHITE);
        }
    }
    
    private int numeroRandom(){
        Random rand = new Random();
        return MIN_VALOR_INT + rand.nextInt((MAX_VALOR_INT-MIN_VALOR_INT)+1);
    }
}

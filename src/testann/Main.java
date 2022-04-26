/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package testann;

import java.util.concurrent.Semaphore;
import java.util.Scanner;

/**
 *
 * @author ACER
 */
public class Main {

    public static final int NUM_HILOS_MAX = 10;
    public static final int NUM_HILOS_LE = 5;
    public static Registros recursoCompartido = new Registros();
    public static NuevosDatos archivoNuevosDatos;
    static Scanner input = new Scanner(System.in);
    
    /**
     * @param args the command line arguments
     */
    public static void Inicializacion(){
        BaseDatos archivoBaseDatos = new BaseDatos("BaseDatos.txt",recursoCompartido);
        archivoNuevosDatos = new NuevosDatos("NuevosDatos.txt");
        
        archivoBaseDatos.extraerDatos();//Inicializacion de tabla Hash
        archivoNuevosDatos.extraerDatos();
    } 
    
    public static void InicializarNuevosDatos(){
        archivoNuevosDatos = new NuevosDatos("NuevosDatos.txt");
        archivoNuevosDatos.extraerDatos();
    } 
    
    public static void main(String[] args) {
        // TODO code application logic here
        int opcion;
        Semaphore semaforo = new Semaphore(NUM_HILOS_MAX);
        Semaphore semaforon = new Semaphore(1);
        
        System.out.println("Practica 2");
        System.out.println("1.- Sincronizacion a nivel de base de datos");
        System.out.println("2.- Sincronizacion individual de base de datos");
        System.out.println("3.- Sincronizacion Registro y modificacion");
        System.out.println("4.- Lectura y Escritura");// NUM_HILOS_MAX = 5
        System.out.println("Opcion>> ");
        
        opcion = input.nextInt();
        
        switch(opcion){
            case 1 -> {
                Inicializacion();
                
                System.out.println("Test 1 : Sincronizacion a nivel de base de datos");
                for(int i = 1; i<=NUM_HILOS_MAX; i++){
                    new Escritor_Test1("Escritor_" + i, recursoCompartido, archivoNuevosDatos.getNuevosDatos() ,semaforo).start();
                }
            }
            case 2 -> {
                Inicializacion();
                
                System.out.println("Test 2 : Sincronizacion individual a nivel base de datos");
                for(int i=1; i<=NUM_HILOS_MAX; i++){
                    new Escritor_Test2("Escritor_" + i, recursoCompartido, archivoNuevosDatos.getNuevosDatos()).start();
                }
            }
            case 3 -> {
                InicializarNuevosDatos();
                
                System.out.println("Test 3 : Sincronizacion de registro y modificacion");
                for(int i=1;i<=NUM_HILOS_MAX;i++){
                    new BaseDatos("BaseDatos.txt",recursoCompartido,"Registro"+i).start();
                    new Escritor_Test3("Escritor"+i,recursoCompartido,archivoNuevosDatos.getNuevosDatos(),semaforon).start();
                }
                
            }
            case 4 -> {
                Inicializacion();
                System.out.println("Lectura y escritura");
                
                //Lectores
                for(int i=1;i <= NUM_HILOS_LE;i++){
                    new Lector("Hilo"+i,recursoCompartido).start();
                    new Escritor_Test4("Hilo"+i,recursoCompartido,archivoNuevosDatos.getNuevosDatos()).start();
                }
            }
            default -> {
                System.out.println("ERROR : opcion invalida");
            }       
        }
        //info.showRegistro();
    }
    
}

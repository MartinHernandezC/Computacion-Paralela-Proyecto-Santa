package paralelassantap1;

import java.util.Random;

public class Santa implements Runnable{
    
    public static IGrafica grafica;
    public static Random rand = new Random(System.nanoTime());
    private int persona; //Identificar quien es cada uno
    private int estado;
    private int id;
    public static int cantidadDue=3+rand.nextInt(8);
    public static int numHilos=10+cantidadDue;
    private static int ren=0;
    private static int due=0;
    //Para mostrar si están despiertos todos los hilos 
    public static int[]estadoHilo=new int[20];
    private static Object lock=new Object();
 
    public Santa(int persona, int id, int estado){
        this.persona=persona;
        this.id=id;
        this.estado=estado;
    }

    public void run(){
        while(true){
            if(persona==0){ //0 santa, 1 reno, 2 duende
                claus(id);
            }else if(persona==1){
                ren(id,estado);
            }else if(persona==2){
                duen(id);
            }
        }
    }
    
    //Actualizar el estado de cada hilo
    private void actualizarH(int id,int s){
        grafica.actualizarH(id, s);
        estadoHilo[id]=s;
    }    
    
    private void duen(int id){
        synchronized(lock){
            try{
                if(ren!=9&&due!=3){
                    if(estadoHilo[id]==1){//Si el estado del hilo es 1 (no ocupa ayuda) no hace nada.
                        due++;
                        System.out.println("el duendecillo "+(id-8)+" se encuentra en aprietos.");
                        actualizarH(id,2);
                        Thread.sleep(1000);
                    }
                    if(due==3){
                        lock.notifyAll();
                        for(int i=9;i<numHilos-1;i++){
                        actualizarH(i,1);
                        }   
                    }
                }
                lock.wait();
            }catch(Exception ex){}   
        }
    }

    private void ren(int id, int estado) {
        synchronized(lock){
            try{
                if(ren!=9&&due!=3){
                    if(estadoHilo[id]==1){//Si el estado del hilo es 1 (vacaciones) no hace nada.
                        ren++;
                        System.out.println("el reno "+(id+1)+" volvió de sus vacas.");
                        actualizarH(id,2);
                        Thread.sleep(1000);
                    }
                    if(ren==9){
                        lock.notifyAll();
                        for(int i=0;i<9;i++){
                        actualizarH(i,1);
                        }
                    }
                }
            lock.wait();
            }catch(Exception ex){}
        }
    }
    
    private void claus(int id) {
        synchronized(lock){
            try{
                if(ren==9){
                    System.out.println("Santa se va a ir con los renos");
                    ren=0;
                    actualizarH(id, 1);
                    for(int i=0;i<9;i++){
                    actualizarH(i,1);
                    }
                    Thread.sleep(3000);
                    System.out.println("Santa se va a la meme");
                }else if(due==3){
                    System.out.println("Santa se va a ir con los duendecillos");
                    actualizarH(id, 3);
                    due=0;
                    Thread.sleep(2000);
                    for(int i=9;i<numHilos-1;i++){
                    actualizarH(i,1);//cuando está dormido el hilo de los duentes ocupan ayuda
                    }
                    System.out.println("Santa se va a la meme");
                }
                actualizarH(id, 2);
                lock.notifyAll();
                lock.wait();    
            }catch(Exception ex){      
            }
        }
    }
 
    public static void main(String[] args) {  
        grafica =new IGrafica();
        Thread[] hilos = new Thread[20];
        for(int i=0; i < hilos.length;i++){
            Runnable runnable = null;   
            if(i==(19)){
                runnable = new Santa(0,19,2);
                grafica.actualizarH(i, 2);
                estadoHilo[i]=2;
            }else if(i<9){
                runnable = new Santa(1,i,1);
                grafica.actualizarH(i, 1);
                estadoHilo[i]=1;
            }else if(i<numHilos-1){
                runnable = new Santa(2,i,1);
                grafica.actualizarH(i, 1);
                estadoHilo[i]=1;
            }
            hilos[i] = new Thread(runnable);
            hilos[i].start();
        }
        for(int i =0;i<hilos.length;i++){
            try{
                hilos[i].join();
            }catch(Exception ex){    
            }
        }  
    } 
}

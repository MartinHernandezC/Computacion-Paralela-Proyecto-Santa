package paralelassantap1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class IGrafica extends JFrame implements Runnable{
    
    private Image fondo;
    private BufferedImage buffer;
    private Thread hilo;
    int numHilos=20;
    int[] estado=new int[numHilos];
    
    public IGrafica(){
        for(int i=0;i<numHilos;i++){
            estado[i]=0;
        }

        setTitle("Ventana");
        setSize(1200,800);
        setLayout(null);

        hilo = new Thread((Runnable)this);
        hilo.start();

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public synchronized void actualizarH(int id,int s){
        estado[id]=s;
    }
    
    public void paint(Graphics g){
        if(fondo == null){
        fondo = createImage(getWidth(),getHeight());
        Graphics gbuffer = fondo.getGraphics();  
        }
        update(g);
    }
    
    @Override
    public void update(Graphics g){
        int puntoy=10;
        int puntox=-9;
        
        g.setClip(0, 0,getWidth(),getHeight());
        buffer=(BufferedImage) createImage(getWidth(),getHeight());
        Graphics gbuffer=buffer.getGraphics();
        gbuffer.setClip(0, 0,getWidth(),getHeight());
        gbuffer.drawImage(fondo,0,0,this);
        
        ImageIcon elsanta=new ImageIcon(getClass().getResource("santa.png"));
        ImageIcon elsantad=new ImageIcon(getClass().getResource("santador.jpg"));
        ImageIcon elsantat=new ImageIcon(getClass().getResource("santatrini.png"));
        ImageIcon fondo=new ImageIcon(getClass().getResource("fondo.jpg"));
        ImageIcon renito=new ImageIcon(getClass().getResource("renito.png"));
        ImageIcon duendecillo=new ImageIcon(getClass().getResource("duendecillo.png"));
        ImageIcon duendecillod=new ImageIcon(getClass().getResource("duendecillod.jpg"));

        gbuffer.drawImage(fondo.getImage(), 0, 0, 1200, 800, this);

        if(estado[numHilos-1]==3){
            gbuffer.drawImage(elsanta.getImage(),950,600,200,200,this);
        }else if(estado[numHilos-1]==2){
            gbuffer.drawImage(elsantad.getImage(),950,600,200,200,this);
        }else if(estado[numHilos-1]==1){
            gbuffer.drawImage(elsantat.getImage(),350,100,500,500,this);
        }
        
        for(int i=0;i<9;i++){
            if(estado[i]==2){
                gbuffer.drawImage(renito.getImage(), 10+(i*100), 500, 100, 100, this);
            }
        }
        
        for(int i=9;i<numHilos-1;i++){
            if(estado[i]==2){
                gbuffer.drawImage(duendecillo.getImage(), 10+((i-9)*100), 650, 80, 80, this);
            }else if(estado[i]==1){
                gbuffer.drawImage(duendecillod.getImage(), 10+((i-9)*100), 650, 80, 80, this);
            }
        }
        g.drawImage(buffer, 0, 0, this);
        }

    @Override
    public void run(){
        while(true){
            try{
                repaint();
                Thread.sleep(16);
            }catch(InterruptedException ex){ 
            }
        }
    }
}

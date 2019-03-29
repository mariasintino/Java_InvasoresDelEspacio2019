/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Timer;

/**
 *
 * @author Maria
 */
public class VentanaJuego extends javax.swing.JFrame {
    public static Label lbl_puntuacion = new Label();//puntuacion
    static int ANCHOPANTALLA = 800;
    static int ALTOPANTALLA = 600;

    //numero de marcianos que van a aparecer
    int filas = 4;
    int columnas = 7;
    int puntuacion = 0;
    
    BufferedImage buffer = null;

    Nave miNave = new Nave();
    Disparo miDisparo = new Disparo();
    //Marciano miMarciano = new Marciano();
    Marciano[][] listaMarcianos = new Marciano[filas][columnas];
    boolean direccionMarcianos = false;
    boolean gameOver = false;
    //el contador sirve para decidir qué imagen del marciano toca poner
    int contador = 0;
    //imagen para cargar el spritesheet con todos los sprites del juego
    BufferedImage plantilla = null;
    Image [][] imagenes ;
    Image [][] imagenesPistola;
    Image [][] imagenesBala;
    Image fondo;
    
    Timer temporizador = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            bucleDelJuego();
        }
    });

    /**
     * Creates new form VentanaJuego
     */
    public VentanaJuego() {
        initComponents();
        setTitle("Space Invaders");
        setLocationRelativeTo(null);
        Font font1;
        Color color1;
        Color color2;
        font1 = new Font("Courier New", Font.BOLD, 40);
        color1 = new Color(255, 255, 255);
        color2 = new Color(0, 0, 0);
        lbl_puntuacion.setFont(font1);
        lbl_puntuacion.setForeground(color1);
        lbl_puntuacion.setBackground(color2);
        // 1º Ancho
        // 2º alto
        // 3º tamaño de ancho de la caja del numero
        // 4º tamaño del alto de la caja del numero
        lbl_puntuacion.setBounds(700, 500, 100, 45);
        lbl_puntuacion.setText("0");
        jPanel1.add(lbl_puntuacion);
        
        reproduce ("/sonidos/babyshark.wav");
        
        //para cargar el archivo de imagenes: 
        // 1º, el nombre del archivo
        // 2º filas que tiene el spritesheet
        // 3º columnas que tiene el spritesheet
        // 4º lo que mide de ancho el sprite en el spritesheet
        // 5º lo que mide de alto el sprite en el spritesheet
        // 6º para cambiar el tamaño de los sprites
        imagenes = cargaImagenes("/imagenes/pajaro.png", 1, 2, 320, 320, 5);
        imagenesPistola = cargaImagenes("/imagenes/zorro.png", 1, 1, 320, 320, 4); 
        imagenesBala = cargaImagenes("/imagenes/bala.png", 1, 1, 126, 370, 5);
        
        try{
          fondo = ImageIO.read(getClass().getResource("/imagenes/fondo.jpg"));
      }
      catch (IOException ex){
      
      }
        miDisparo.imagen = imagenesBala[0][0];
        setSize(ANCHOPANTALLA, ALTOPANTALLA);
        buffer = (BufferedImage) jPanel1.createImage(ANCHOPANTALLA, ALTOPANTALLA);
        buffer.createGraphics();

        temporizador.start();

        //inicializo la posición inicial de la nave
        miNave.imagen = imagenesPistola[0][0];
        miNave.x = ANCHOPANTALLA / 2 - miNave.imagen.getWidth(this) / 2;
        miNave.y = ALTOPANTALLA - miNave.imagen.getHeight(this) - 40;
        
        //inicializo el array de marcianos
        //os reto a que hagais esto usando mods (es decir, usando el bucle for anidado)
        
        //1 parametro: numero de la fila de marcianos que estoy creando
        //2º parametro: fila dentro del spritesheet del marciano que quiero pintar
        //3º parametro: columna dentro del spritesheet del marciano que quiero pintar
        creaFilaDeMarcianos(0, 0, 0);
        creaFilaDeMarcianos(1, 0, 0);
        creaFilaDeMarcianos(2, 0, 0);
        creaFilaDeMarcianos(3, 0, 0);
//        creaFilaDeMarcianos(4, 0, 2);
//        creaFilaDeMarcianos(5, 0, 2);
//        creaFilaDeMarcianos(6, 0, 2);
//        creaFilaDeMarcianos(7, 0, 2);        
    }
  private void finPartida (Graphics2D muerto)throws IOException{
      try{
          Image gameOver1 = ImageIO.read(getClass().getResource("/imagenes/gameOver.gif"));
          muerto.drawImage(gameOver1, 0, 0,ANCHOPANTALLA,ALTOPANTALLA,null);
      }
      catch (IOException ex){
      
      }
  }
  
  private void ganaPartida (Graphics2D win)throws IOException{
      try{
          Image victoria = ImageIO.read(getClass().getResource("/imagenes/win.jpg"));
          win.drawImage(victoria, 0, 0,ANCHOPANTALLA,ALTOPANTALLA,null);
      }
      catch (IOException ex){
      
      }
  }
    
  private void creaFilaDeMarcianos(int numeroFila, int spriteFila, int spriteColumna){
      for (int j = 0; j < columnas; j++) {
          listaMarcianos[numeroFila][j] = new Marciano();
          listaMarcianos[numeroFila][j].imagen1 = imagenes[spriteFila][spriteColumna];
          listaMarcianos[numeroFila][j].imagen2 = imagenes[spriteFila][spriteColumna + 1];
          listaMarcianos[numeroFila][j].x = j * (15 + listaMarcianos[numeroFila][j].imagen1.getWidth(null));
          listaMarcianos[numeroFila][j].y = numeroFila * (10 + listaMarcianos[numeroFila][j].imagen1.getHeight(null));
      }
  }  
    
  private void reproduce (String cancion){
      try{
          Clip clip = AudioSystem.getClip();
          clip.open (AudioSystem.getAudioInputStream(getClass().getResource(cancion)));
          clip.loop(0);
          Thread one = new Thread(){
              public void run(){
                  while (clip.getFramePosition()<clip.getFrameLength())
                      Thread.yield();
              }
          };
          one.start();
      }catch (Exception e) {
          
      }
  }
  
    /*
    este método va a servir para crear el array de imagenes con todas las imagenes
    del spritesheet. Devolverá un array de dos dimensiones con las imágenes colocadas
    tal y como están en el spritesheet
    */
    private Image[][] cargaImagenes(String nombreArchivoImagenes, 
                                       int numFilas ,int numColumnas, int ancho, int alto, int escala){
        try {
            plantilla = ImageIO.read(getClass().getResource(nombreArchivoImagenes));
        } catch (IOException ex) { }
        Image [][] arrayImagenes = new Image[numFilas][numColumnas];
        
        //cargo las imagenes de forma individual en cada imagen del array de imagenes
        for (int i=0; i<numFilas; i++){
            for (int j=0; j<numColumnas; j++){
                arrayImagenes[i][j] = plantilla.getSubimage(j*ancho, i*alto, ancho, alto);
                arrayImagenes[i][j] = arrayImagenes[i][j].getScaledInstance(ancho/escala, ancho /escala, Image.SCALE_SMOOTH);
            }
        }
        
        return arrayImagenes;
        
        
//        //la última fila del spritesheet sólo mide 32 de alto, así que hay que hacerla aparte
//        for (int j=0; j<4; j++){
//            imagenes[20 + j] = plantilla.getSubimage(j*64, 5*64, 64, 32);
//        }
//
//        //la última columna del spritesheet sólo mide 32 de ancho, así que hay que hacerla aparte
//    
//        imagenes[24] = plantilla.getSubimage(4*64, 2*64, 32, 64);
//        imagenes[24] = imagenes[24].getScaledInstance(16, 32, Image.SCALE_SMOOTH);
//   
    }
    
    private void bucleDelJuego() {
        //se encarga del redibujado de los objetos en el jPanel1
        //primero borro todo lo que hay en el buffer
        contador++;
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
        if(!gameOver){
            
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);
        g2.drawImage(fondo, 0, 0,ANCHOPANTALLA,ALTOPANTALLA,null);
        ///////////////////////////////////////////////////////
        //redibujaremos aquí cada elemento
        g2.drawImage(miDisparo.imagen, miDisparo.x, miDisparo.y, null);
        g2.drawImage(miNave.imagen, miNave.x, miNave.y, null);
        
        pintaMarcianos(g2);
        chequeaColision();
        miNave.mueve();
        miDisparo.mueve();
        
        if(puntuacion == 140){
             try {
                ganaPartida (g2);
            }
            catch(IOException ex){
                
            }
        }
        }
        else {
            try {
                finPartida (g2);
            }
            catch(IOException ex){
                
            }
        }
        /////////////////////////////////////////////////////////////
        //*****************   fase final, se dibuja ***************//
        //*****************   el buffer de golpe sobre el Jpanel***//

        g2 = (Graphics2D) jPanel1.getGraphics();
        g2.drawImage(buffer, 0, 0, null);

    }

    private void chequeaColision(){
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloMiNave = new Rectangle2D.Double();
        
        rectanguloDisparo.setFrame( miDisparo.x, 
                                    miDisparo.y,
                                    miDisparo.imagen.getWidth(null),
                                    miDisparo.imagen.getHeight(null));
        rectanguloMiNave.setFrame( miNave.x, 
                                    miNave.y,
                                    miNave.imagen.getWidth(null),
                                    miNave.imagen.getHeight(null));
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (listaMarcianos[i][j].vivo) {
                    rectanguloMarciano.setFrame(listaMarcianos[i][j].x,
                                                listaMarcianos[i][j].y,
                                                listaMarcianos[i][j].imagen1.getWidth(null),
                                                listaMarcianos[i][j].imagen1.getHeight(null)
                                                );
                    if (rectanguloDisparo.intersects(rectanguloMarciano)){
                        listaMarcianos[i][j].vivo = false;
                        miDisparo.posicionaDisparo(miNave);
                        miDisparo.y = 1000;
                        miDisparo.disparado = false;
                        puntuacion = puntuacion +5;
                        lbl_puntuacion.setText("" + puntuacion);
                    }
                    if(rectanguloMarciano.intersects(rectanguloMiNave)){
                        gameOver = true;
                    }
                }
            }
        }
    }
    
    private void cambiaDireccionMarcianos() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                listaMarcianos[i][j].y +=10;
                listaMarcianos[i][j].setvX(listaMarcianos[i][j].getvX()* -1);
            }
        }
    }
    
    private void pintaMarcianos(Graphics2D _g2) {

        int anchoMarciano = listaMarcianos[0][0].imagen1.getWidth(null);
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (listaMarcianos[i][j].vivo) {
                    listaMarcianos[i][j].mueve();
                    //chequeo si el marciano ha chocado contra la pared para cambiar la dirección 
                    //de todos los marcianos
                    if (listaMarcianos[i][j].x + anchoMarciano == ANCHOPANTALLA || listaMarcianos[i][j].x == 0) {
                        direccionMarcianos = true;
                    }
                    if (contador < 50) {
                        _g2.drawImage(listaMarcianos[i][j].imagen1,
                                listaMarcianos[i][j].x,
                                listaMarcianos[i][j].y,
                                null);
                    } else if (contador < 100) {
                        _g2.drawImage(listaMarcianos[i][j].imagen2,
                                listaMarcianos[i][j].x,
                                listaMarcianos[i][j].y,
                                null);
                    } else {
                        contador = 0;
                    }
                }
            }
        }
        if (direccionMarcianos ){
            cambiaDireccionMarcianos();
            direccionMarcianos = false;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                miNave.setPulsadoIzquierda(true);
                break;
            case KeyEvent.VK_RIGHT:
                miNave.setPulsadoDerecha(true);
                break;
            case KeyEvent.VK_SPACE:
                miDisparo.posicionaDisparo(miNave);
                miDisparo.disparado = true;
                reproduce ("/sonidos/disparo.wav");
                break;
        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                miNave.setPulsadoIzquierda(false);
                break;
            case KeyEvent.VK_RIGHT:
                miNave.setPulsadoDerecha(false);
                break;
        }
    }//GEN-LAST:event_formKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaJuego().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}

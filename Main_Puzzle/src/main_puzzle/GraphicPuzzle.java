package main_puzzle;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.util.Random;

public class GraphicPuzzle extends javax.swing.JFrame {
    private JButton[][] piezas = new JButton[3][3];
    private int[][] tablero = {
        {1,2,3},
        {4,5,6},
        {7,8,0} //0 representa el espacio vacío auxiliar en el tablero
    };

    //datos del modo inteligente
    private java.util.List<int[][]> solucionInteligente;
    private int pasoActual = 0;
    
    //control del árbol
    private int profundidadMaxima = 30;
    private int limiteNodos = 100000;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GraphicPuzzle.class.getName());

    /**
     * Creates new form GraphicPuzzle
     */
    public GraphicPuzzle() {
        initComponents();
        piezas[0][0] = jButton1;
        piezas[0][1] = jButton2;
        piezas[0][2] = jButton3;
        piezas[1][0] = jButton4;
        piezas[1][1] = jButton5;
        piezas[1][2] = jButton6;
        piezas[2][0] = jButton7;
        piezas[2][1] = jButton8;
        piezas[2][2] = jButton9;

        //buttonSugerirJugada.addActionListener(e -> sugerirJugada()); // botón "sugerir jugada"
        //buttonAutomatico.addActionListener(e -> ejecutarAutomatico()); puede usarse despues
        
        actualizarTablero();

        
    }
    
    //Actualizar el tablero cuando se mueve una pieza
    private void actualizarTablero(){
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                if(tablero[i][j] == 0){
                    piezas[i][j].setText("");
                }else{
                    piezas[i][j].setText(String.valueOf(tablero[i][j]));
                }
            }
        }
    }
    
    //Poner las fichas en desorden
    private void mezclar(){
        Random r = new java.util.Random();
        
        for(int k=0; k<100; k++){
            int i = r.nextInt(3);
            int j = r.nextInt(3);
            moverFicha(i, j);
        }
    }
    
    //Mover ficha
    private void moverFicha(int i, int j){
        int filaVacia = -1;
        int colVacia = -1;
        
        //Buscar donde esta el 0
        for(int x=0; x<3; x++){
            for(int y=0; y<3; y++){
                if(tablero[x][y] == 0){
                    filaVacia = x;
                    colVacia = y;
                }
            }
        }
        
        //Ver si esta al lado de la ficha que se quiere mover
        if((Math.abs(filaVacia - i)) + Math.abs(colVacia - j) == 1){
            //Intercambiamos las fichas
            tablero[filaVacia][colVacia] = tablero[i][j];
            tablero[i][j] = 0;
            
            actualizarTablero();
            
            if(resuelto()){
                JOptionPane.showMessageDialog(this, "Has ganado!!!");
            }
        }
    }
    
    private boolean resuelto(){
        int cont = 1;
        
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                
                if(i == 2 && j == 2){
                    return tablero[i][j] == 0;
                }
                
                if(tablero[i][j] != cont++){
                    return false;
                }
            }
        }
        
        return true;
    }

    private void resolverInteligente() {

        int[][] meta = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        };
    
        String profundidadTexto = JOptionPane.showInputDialog(this, "Ingresa la profundidad máxima del árbol:", String.valueOf(profundidadMaxima));
    
        if (profundidadTexto == null) {
            return;
        }
    
        String limiteTexto = JOptionPane.showInputDialog(this, "Ingresa el límite máximo de nodos:", String.valueOf(limiteNodos)
        );
    
        if (limiteTexto == null) {
            return;
        }
    
        try {
            profundidadMaxima = Integer.parseInt(profundidadTexto);
            limiteNodos = Integer.parseInt(limiteTexto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Debes ingresar números válidos.");
            return;
        }
    
        if (profundidadMaxima <= 0 || limiteNodos <= 0) {
            JOptionPane.showMessageDialog(this, "La profundidad y el límite de nodos deben ser mayores a 0.");
            return;
        }
    
        SolverAEstrella solver = new SolverAEstrella();
    
        ResultadoBusqueda resultado = solver.resolver(tablero, meta, profundidadMaxima, limiteNodos);
    
        if (!resultado.isEncontrada()) {
            JOptionPane.showMessageDialog(this, resultado.getMensaje() + "\nNodos generados: " + resultado.getNodosGenerados() + "\nNodos explorados: " + resultado.getNodosExplorados());
            return;
        }
    
        solucionInteligente = resultado.getCamino();
        pasoActual = 0;
    
        JOptionPane.showMessageDialog(this, resultado.getMensaje() + "\nMovimientos: " + (solucionInteligente.size() - 1) + "\nNodos generados: " + resultado.getNodosGenerados() + "\nNodos explorados: " + resultado.getNodosExplorados());
    }

    private void mostrarSiguientePaso() {
    
        if(solucionInteligente == null || solucionInteligente.isEmpty()){
            JOptionPane.showMessageDialog(this, "Primero debes ejecutar Resolver Inteligente.");
            return;
        }
    
        if(pasoActual < solucionInteligente.size()){
            tablero = copiarTableroLocal(solucionInteligente.get(pasoActual));
            actualizarTablero();
            pasoActual++;
        }else{
            JOptionPane.showMessageDialog(this, "Ya se mostró toda la solución.");
        }
    }
    
    /* por si se usa despues 
    
    private void ejecutarAutomatico(){
    
        if (solucionInteligente == null || solucionInteligente.isEmpty()){
            JOptionPane.showMessageDialog(this, "Primero debes ejecutar Resolver Inteligente.");
            return;
        }
    
        pasoActual = 0;
    
        javax.swing.Timer timer = new javax.swing.Timer(700, e ->{
    
            if (pasoActual < solucionInteligente.size()) {
                tablero = copiarTableroLocal(solucionInteligente.get(pasoActual));
                actualizarTablero();
                pasoActual++;
            } else {
                ((javax.swing.Timer) e.getSource()).stop();
                JOptionPane.showMessageDialog(this, "Solución terminada.");
            }
        });
    
        timer.start();
    }
    */
    private int[][] copiarTableroLocal(int[][] original) {
    
        int[][] copia = new int[3][3];
    
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                copia[i][j] = original[i][j];
            }
        }
    
        return copia;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton12 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        buttonSugerirJugada = new javax.swing.JButton();
        buttonResolverInteligente = new javax.swing.JButton();
        buttonSiguientePaso = new javax.swing.JButton();

        jButton12.setText("jButton12");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.GridLayout(3, 3));

        jButton1.setText("jButton1");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("jButton2");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setText("jButton3");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton4.setText("jButton4");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jButton5.setText("jButton5");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        jButton6.setText("jButton6");
        jButton6.addActionListener(this::jButton6ActionPerformed);

        jButton7.setText("jButton7");
        jButton7.addActionListener(this::jButton7ActionPerformed);

        jButton8.setText("jButton8");
        jButton8.addActionListener(this::jButton8ActionPerformed);

        jButton9.setText("jButton9");
        jButton9.addActionListener(this::jButton9ActionPerformed);

        jButton10.setText("Juego Nuevo");
        jButton10.addActionListener(this::jButton10ActionPerformed);

        buttonSugerirJugada.setText("Sugerir Jugada");

        buttonResolverInteligente.setText("Modo Inteligente");
        buttonResolverInteligente.addActionListener(this::buttonResolverInteligenteActionPerformed);

        buttonSiguientePaso.setText("Siguiente Paso");
        buttonSiguientePaso.addActionListener(this::buttonSiguientePasoActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(buttonResolverInteligente))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonSiguientePaso)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                        .addComponent(buttonSugerirJugada)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton10)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton7)
                            .addComponent(jButton4)
                            .addComponent(jButton1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2)
                            .addComponent(jButton5)
                            .addComponent(jButton8))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3)
                            .addComponent(jButton6)
                            .addComponent(jButton9))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(165, 165, 165))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2)
                            .addComponent(jButton3))
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4)
                            .addComponent(jButton5)
                            .addComponent(jButton6))
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton7)
                            .addComponent(jButton8)
                            .addComponent(jButton9))
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton10)
                    .addComponent(buttonSugerirJugada)
                    .addComponent(buttonResolverInteligente)
                    .addComponent(buttonSiguientePaso))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        moverFicha(0,0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        moverFicha(0,1);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        moverFicha(0,2);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        moverFicha(1,0);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        moverFicha(1,1);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        moverFicha(1,2);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        moverFicha(2,0);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        moverFicha(2,1);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        moverFicha(2,2);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        mezclar();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void buttonResolverInteligenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResolverInteligenteActionPerformed
        resolverInteligente();
    }//GEN-LAST:event_buttonResolverInteligenteActionPerformed

    private void buttonSiguientePasoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSiguientePasoActionPerformed
        mostrarSiguientePaso();
    }//GEN-LAST:event_buttonSiguientePasoActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new GraphicPuzzle().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonResolverInteligente;
    private javax.swing.JButton buttonSiguientePaso;
    private javax.swing.JButton buttonSugerirJugada;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}

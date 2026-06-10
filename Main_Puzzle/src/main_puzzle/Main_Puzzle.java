package main_puzzle;

public class Main_Puzzle {

    public static void main(String[] args) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GraphicPuzzle().setVisible(true);
            }
        });
    }
    
}

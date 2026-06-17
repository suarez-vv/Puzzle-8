package main_puzzle;

public class NodoPuzzle implements Comparable<NodoPuzzle> {

    int[][] tablero;
    NodoPuzzle padre;
    int g; //movimientos desde el inicio
    int h; //distancia Manhattan
    int f; //g + h

    public NodoPuzzle(int[][] tablero, NodoPuzzle padre, int g, int h){
        this.tablero = tablero;
        this.padre = padre;
        this.g = g;
        this.h = h;
        this.f = g + h;
    }

    public int[][] getTablero() {
        return tablero;
    }

    public NodoPuzzle getPadre() {
        return padre;
    }

    public int getG() {
        return g;
    }

    public int getH() {
        return h;
    }

    public int getF() {
        return f;
    }

    @Override
    public int compareTo(NodoPuzzle otro){
        return Integer.compare(this.f, otro.f);
    }
}

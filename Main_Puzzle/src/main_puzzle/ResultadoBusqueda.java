package main_puzzle;

import java.util.List;

public class ResultadoBusqueda {

    private List<int[][]> camino;
    private int nodosGenerados;
    private int nodosExplorados;
    private boolean encontrada;
    private String mensaje;

    public ResultadoBusqueda(List<int[][]> camino, int nodosGenerados, int nodosExplorados, boolean encontrada, String mensaje) {
        this.camino = camino;
        this.nodosGenerados = nodosGenerados;
        this.nodosExplorados = nodosExplorados;
        this.encontrada = encontrada;
        this.mensaje = mensaje;
    }

    public List<int[][]> getCamino() {
        return camino;
    }

    public int getNodosGenerados() {
        return nodosGenerados;
    }

    public int getNodosExplorados() {
        return nodosExplorados;
    }

    public boolean isEncontrada() {
        return encontrada;
    }

    public String getMensaje() {
        return mensaje;
    }
}

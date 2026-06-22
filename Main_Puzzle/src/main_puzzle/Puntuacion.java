package main_puzzle;

public class Puntuacion {

    private String alias;
    private int puntos;
    private String fecha;

    public Puntuacion(String alias, int puntos, String fecha) {
        this.alias = alias;
        this.puntos = puntos;
        this.fecha = fecha;
    }

    public String getAlias() {
        return alias;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getFecha() {
        return fecha;
    }

    public void sumarPuntos(int puntosNuevos, String fechaNueva) {
        puntos += puntosNuevos;
        fecha = fechaNueva;
    }
}

package main_puzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class SolverAEstrella {

    public ResultadoBusqueda sugerirJugada(int[][] inicio, int[][] meta, int profundidadMaxima, int limiteNodos) {

        PriorityQueue<NodoPuzzle> abiertos = new PriorityQueue<>();
        HashSet<String> cerrados = new HashSet<>();

        int nodosGenerados = 0;
        int nodosExplorados = 0;

        if (!esSolucionable(inicio, meta)) {
            return new ResultadoBusqueda(
                    null,
                    nodosGenerados,
                    nodosExplorados,
                    false,
                    "Este tablero no puede llegar a la meta."
            );
        }

        int hInicial = calcularManhattan(inicio, meta);
        NodoPuzzle nodoInicial = new NodoPuzzle(copiarTablero(inicio), null, 0, hInicial);
        NodoPuzzle mejorNodo = nodoInicial;

        abiertos.add(nodoInicial);
        nodosGenerados++;

        while (!abiertos.isEmpty()) {

            NodoPuzzle actual = abiertos.poll();
            String claveActual = convertirAString(actual.getTablero());

            if (cerrados.contains(claveActual)) {
                continue;
            }

            cerrados.add(claveActual);
            nodosExplorados++;

            if (actual.getH() < mejorNodo.getH()
                    || (actual.getH() == mejorNodo.getH() && actual.getF() < mejorNodo.getF())) {
                mejorNodo = actual;
            }

            if (sonIguales(actual.getTablero(), meta)) {
                return new ResultadoBusqueda(
                        reconstruirCamino(actual),
                        nodosGenerados,
                        nodosExplorados,
                        true,
                        "Se encontro una ruta completa."
                );
            }

            if (actual.getG() >= profundidadMaxima) {
                continue;
            }

            List<int[][]> sucesores = generarSucesores(actual.getTablero());

            for (int[][] sucesor : sucesores) {

                String claveSucesor = convertirAString(sucesor);

                if (!cerrados.contains(claveSucesor)) {

                    if (nodosGenerados >= limiteNodos) {
                        return new ResultadoBusqueda(
                                reconstruirCamino(mejorNodo),
                                nodosGenerados,
                                nodosExplorados,
                                false,
                                "Se alcanzo el limite de nodos, se muestra la mejor sugerencia encontrada."
                        );
                    }

                    int g = actual.getG() + 1;
                    int h = calcularManhattan(sucesor, meta);

                    abiertos.add(new NodoPuzzle(sucesor, actual, g, h));
                    nodosGenerados++;
                }
            }
        }

        return new ResultadoBusqueda(
                reconstruirCamino(mejorNodo),
                nodosGenerados,
                nodosExplorados,
                false,
                "No se encontro la solucion completa, se muestra la mejor sugerencia encontrada."
        );
    }

    public ResultadoBusqueda resolver(int[][] inicio, int[][] meta, int profundidadMaxima, int limiteNodos) {

        PriorityQueue<NodoPuzzle> abiertos = new PriorityQueue<>();
        HashSet<String> cerrados = new HashSet<>();

        int nodosGenerados = 0;
        int nodosExplorados = 0;

        if (!esSolucionable(inicio, meta)) {
            return new ResultadoBusqueda(
                    null,
                    nodosGenerados,
                    nodosExplorados,
                    false,
                    "El tablero inicial no puede llegar a la meta indicada."
            );
        }

        int hInicial = calcularManhattan(inicio, meta);
        NodoPuzzle nodoInicial = new NodoPuzzle(copiarTablero(inicio), null, 0, hInicial);

        abiertos.add(nodoInicial);
        nodosGenerados++;

        while (!abiertos.isEmpty()) {

            NodoPuzzle actual = abiertos.poll();
            String claveActual = convertirAString(actual.getTablero());

            if (cerrados.contains(claveActual)) {
                continue;
            }

            cerrados.add(claveActual);
            nodosExplorados++;

            if (sonIguales(actual.getTablero(), meta)) {
                List<int[][]> camino = reconstruirCamino(actual);

                return new ResultadoBusqueda(
                        camino,
                        nodosGenerados,
                        nodosExplorados,
                        true,
                        "Solución encontrada."
                );
            }

            // Control del árbol por profundidad.
            // Si el nodo ya llegó a la profundidad máxima, no genera hijos.
            if (actual.getG() >= profundidadMaxima) {
                continue;
            }

            List<int[][]> sucesores = generarSucesores(actual.getTablero());

            for (int[][] sucesor : sucesores) {

                String claveSucesor = convertirAString(sucesor);

                if (!cerrados.contains(claveSucesor)) {

                    //Control del árbol por cantidad de nodos generados.
                    if(nodosGenerados >= limiteNodos) {
                        return new ResultadoBusqueda(
                                null,
                                nodosGenerados,
                                nodosExplorados,
                                false,
                                "Se alcanzó el límite de nodos generados."
                        );
                    }

                    int g = actual.getG() + 1;
                    int h = calcularManhattan(sucesor, meta);

                    NodoPuzzle nuevoNodo = new NodoPuzzle(sucesor, actual, g, h);

                    abiertos.add(nuevoNodo);
                    nodosGenerados++;
                }
            }
        }

        return new ResultadoBusqueda(
                null,
                nodosGenerados,
                nodosExplorados,
                false,
                "No se encontró solución con la profundidad indicada."
        );
    }

    private List<int[][]> generarSucesores(int[][] tablero) {

        List<int[][]> sucesores = new ArrayList<>();

        int filaCero = -1;
        int colCero = -1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == 0) {
                    filaCero = i;
                    colCero = j;
                }
            }
        }

        int[][] movimientos = {
            {-1, 0}, //arriba
            {1, 0},  //abajo
            {0, -1}, //izquierda
            {0, 1}   //derecha
        };

        for (int[] movimiento : movimientos) {

            int nuevaFila = filaCero + movimiento[0];
            int nuevaCol = colCero + movimiento[1];

            if (nuevaFila >= 0 && nuevaFila < 3 && nuevaCol >= 0 && nuevaCol < 3) {

                int[][] nuevoTablero = copiarTablero(tablero);

                nuevoTablero[filaCero][colCero] = nuevoTablero[nuevaFila][nuevaCol];
                nuevoTablero[nuevaFila][nuevaCol] = 0;

                sucesores.add(nuevoTablero);
            }
        }

        return sucesores;
    }

    private int calcularManhattan(int[][] actual, int[][] meta) {

        int distancia = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                int valor = actual[i][j];

                if (valor != 0) {
                    int[] posicionMeta = buscarPosicion(meta, valor);

                    distancia += Math.abs(i - posicionMeta[0]) + Math.abs(j - posicionMeta[1]);
                }
            }
        }

        return distancia;
    }

    private int[] buscarPosicion(int[][] tablero, int valor) {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (tablero[i][j] == valor) {
                    return new int[]{i, j};
                }
            }
        }

        return new int[]{-1, -1};
    }

    private boolean sonIguales(int[][] a, int[][] b) {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (a[i][j] != b[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    private String convertirAString(int[][] tablero) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(tablero[i][j]);
            }
        }

        return sb.toString();
    }

    private int[][] copiarTablero(int[][] original) {

        int[][] copia = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                copia[i][j] = original[i][j];
            }
        }

        return copia;
    }

    private List<int[][]> reconstruirCamino(NodoPuzzle nodoFinal) {

        List<int[][]> camino = new ArrayList<>();

        NodoPuzzle actual = nodoFinal;

        while (actual != null) {
            camino.add(actual.getTablero());
            actual = actual.getPadre();
        }

        Collections.reverse(camino);

        return camino;
    }

    private boolean esSolucionable(int[][] inicio, int[][] meta) {

        int[] arregloInicio = convertirAArreglo(inicio);
        int[] arregloMeta = convertirAArreglo(meta);

        int inversionesInicio = contarInversiones(arregloInicio);
        int inversionesMeta = contarInversiones(arregloMeta);

        return inversionesInicio % 2 == inversionesMeta % 2;
    }

    private int[] convertirAArreglo(int[][] tablero) {

        int[] arreglo = new int[9];
        int indice = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                arreglo[indice] = tablero[i][j];
                indice++;
            }
        }

        return arreglo;
    }

    private int contarInversiones(int[] arreglo) {

        int inversiones = 0;

        for (int i = 0; i < arreglo.length; i++) {
            for (int j = i + 1; j < arreglo.length; j++) {

                if (arreglo[i] != 0 && arreglo[j] != 0 && arreglo[i] > arreglo[j]) {
                    inversiones++;
                }
            }
        }

        return inversiones;
    }
}

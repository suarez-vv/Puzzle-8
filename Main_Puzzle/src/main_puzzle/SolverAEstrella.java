package main_puzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class SolverAEstrella {

    public List<int[][]> resolver(int[][] inicio, int[][] meta) {
        PriorityQueue<NodoPuzzle> abiertos = new PriorityQueue<>();
        HashSet<String> cerrados = new HashSet<>();

        int hInicial = calcularManhattan(inicio, meta);
        NodoPuzzle nodoInicial = new NodoPuzzle(copiarTablero(inicio), null, 0, hInicial);

        abiertos.add(nodoInicial);

        while (!abiertos.isEmpty()){
            NodoPuzzle actual = abiertos.poll();

            if (sonIguales(actual.tablero, meta)){
                return reconstruirCamino(actual);
            }

            String claveActual = convertirAString(actual.tablero);

            if (cerrados.contains(claveActual)){
                continue;
            }

            cerrados.add(claveActual);

            List<int[][]> sucesores = generarSucesores(actual.tablero);

            for(int[][] sucesor : sucesores){
                String claveSucesor = convertirAString(sucesor);

                if(!cerrados.contains(claveSucesor)){
                    int g = actual.g + 1;
                    int h = calcularManhattan(sucesor, meta);

                    NodoPuzzle nuevoNodo = new NodoPuzzle(sucesor, actual, g, h);
                    abiertos.add(nuevoNodo);
                }
            }
        }

        return null;
    }

    private List<int[][]> generarSucesores(int[][] tablero){
        List<int[][]> sucesores = new ArrayList<>();

        int filaCero = -1;
        int colCero = -1;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(tablero[i][j] == 0){
                    filaCero = i;
                    colCero = j;
                }
            }
        }

        int[][] movimientos ={
            {-1, 0}, //arriba
            {1, 0},  //abajo
            {0, -1}, //izquierda
            {0, 1}   //derecha
        };

        for(int[] mov : movimientos){
            int nuevaFila = filaCero + mov[0];
            int nuevaCol = colCero + mov[1];

            if(nuevaFila >= 0 && nuevaFila < 3 && nuevaCol >= 0 && nuevaCol < 3){
                int[][] nuevoTablero = copiarTablero(tablero);

                nuevoTablero[filaCero][colCero] = nuevoTablero[nuevaFila][nuevaCol];
                nuevoTablero[nuevaFila][nuevaCol] = 0;

                sucesores.add(nuevoTablero);
            }
        }

        return sucesores;
    }

    private int calcularManhattan(int[][] actual, int[][] meta){
        int distancia = 0;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                int valor = actual[i][j];

                if(valor != 0){
                    int[] posicionMeta = buscarPosicion(meta, valor);

                    distancia += Math.abs(i - posicionMeta[0]) + Math.abs(j - posicionMeta[1]);
                }
            }
        }

        return distancia;
    }

    private int[] buscarPosicion(int[][] tablero, int valor){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(tablero[i][j] == valor){
                    return new int[]{i, j};
                }
            }
        }

        return null;
    }

    private boolean sonIguales(int[][] a, int[][] b{
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(a[i][j] != b[i][j]){
                    return false;
                }
            }
        }

        return true;
    }

    private String convertirAString(int[][] tablero){
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                sb.append(tablero[i][j]);
            }
        }

        return sb.toString();
    }

    private int[][] copiarTablero(int[][] original){
        int[][] copia = new int[3][3];

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                copia[i][j] = original[i][j];
            }
        }

        return copia;
    }

    private List<int[][]> reconstruirCamino(NodoPuzzle nodoFinal){
        List<int[][]> camino = new ArrayList<>();

        NodoPuzzle actual = nodoFinal;

        while(actual != null){
            camino.add(actual.tablero);
            actual = actual.padre;
        }

        Collections.reverse(camino);

        return camino;
    }
}

package main_puzzle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ManejadorPuntuaciones {

    private final File archivo = new File("puntuaciones.txt");
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void guardarPuntuacion(String alias, int puntos) throws IOException {
        List<Puntuacion> puntuaciones = leerPuntuaciones();
        String fechaActual = LocalDateTime.now().format(formatoFecha);
        alias = alias.replace(";", "").trim();

        if (alias.isEmpty()) {
            alias = "Jugador";
        }

        Puntuacion jugadorEncontrado = buscarJugador(puntuaciones, alias);

        if (jugadorEncontrado == null) {
            puntuaciones.add(new Puntuacion(alias, puntos, fechaActual));
        } else {
            // Si el alias ya existe, se acumulan los puntos como pide el proyecto.
            jugadorEncontrado.sumarPuntos(puntos, fechaActual);
        }

        guardarLista(puntuaciones);
    }

    public List<Puntuacion> leerPuntuacionesOrdenadas() throws IOException {
        List<Puntuacion> puntuaciones = leerPuntuaciones();

        Collections.sort(puntuaciones, new Comparator<Puntuacion>() {
            @Override
            public int compare(Puntuacion p1, Puntuacion p2) {
                return Integer.compare(p2.getPuntos(), p1.getPuntos());
            }
        });

        return puntuaciones;
    }

    private Puntuacion buscarJugador(List<Puntuacion> puntuaciones, String alias) {
        for (Puntuacion puntuacion : puntuaciones) {
            if (puntuacion.getAlias().equalsIgnoreCase(alias)) {
                return puntuacion;
            }
        }

        return null;
    }

    private List<Puntuacion> leerPuntuaciones() throws IOException {
        List<Puntuacion> puntuaciones = new ArrayList<>();

        if (!archivo.exists()) {
            return puntuaciones;
        }

        BufferedReader lector = new BufferedReader(new FileReader(archivo));
        String linea;

        while ((linea = lector.readLine()) != null) {
            String[] datos = linea.split(";");

            if (datos.length == 3) {
                try {
                    String alias = datos[0];
                    int puntos = Integer.parseInt(datos[1]);
                    String fecha = datos[2];

                    puntuaciones.add(new Puntuacion(alias, puntos, fecha));
                } catch (NumberFormatException ex) {
                    // Si una linea esta mal escrita, se ignora para que el reporte pueda abrir.
                }
            }
        }

        lector.close();
        return puntuaciones;
    }

    private void guardarLista(List<Puntuacion> puntuaciones) throws IOException {
        BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo));

        for (Puntuacion puntuacion : puntuaciones) {
            escritor.write(puntuacion.getAlias() + ";" + puntuacion.getPuntos() + ";" + puntuacion.getFecha());
            escritor.newLine();
        }

        escritor.close();
    }
}

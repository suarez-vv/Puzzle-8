# Puzzle-Proyecto_ProgramacionIII

Proyecto de Programación III — ISC 4to semestre  
Universidad Autónoma de Aguascalientes

Juego del Puzzle de 8 piezas hecho en Java con interfaz Swing. Incluye solución automática con A*, sugerencia de jugada y sistema de puntuaciones guardado en archivo.

---

## Requisitos

- Java JDK 8 o superior
- NetBeans IDE 12 o superior
- No requiere instalaciones externas

---

## Cómo ejecutar

**Desde NetBeans:**

1. Clona el repositorio
2. Abre la carpeta `Main_Puzzle` como proyecto en NetBeans
3. Presiona F6 o el botón Run

**Desde terminal:**

```bash
javac -d out Main_Puzzle/src/main_puzzle/*.java
java -cp out main_puzzle.Main_Puzzle
```

El archivo `puntuaciones.txt` se genera automáticamente la primera vez que se guarda una puntuación.

---

## Cómo jugar

Al abrir la app el tablero ya aparece mezclado en el nivel 1. Haz clic en cualquier ficha adyacente al espacio vacío para moverla. El objetivo es ordenar las fichas del 1 al 8 con el espacio vacío en la esquina inferior derecha.

El juego tiene 3 niveles. Al completar cada uno el tablero se mezcla con más movimientos y la puntuación se acumula.

| Nivel | Movimientos de mezcla |
|-------|-----------------------|
| 1     | 50                    |
| 2     | 100                   |
| 3     | 150                   |

---

## Funciones especiales

**Sugerir jugada**  
Muestra el mejor movimiento siguiente. Pide una profundidad de búsqueda (default: 30) y actualiza el tablero con el estado sugerido.

**Resolver inteligente**  
Calcula la secuencia óptima de movimientos usando A*. Pide profundidad máxima (default: 30) y límite de nodos (default: 100,000). Una vez calculado, usa el botón "Siguiente Paso" para avanzar paso a paso.

Si el tablero está muy desordenado puede aumentar el límite de nodos, aunque esto incrementa el tiempo de cálculo.

---

## Puntuaciones

```
Puntos por nivel = 1000 - (movimientos * 10)
Minimo = 100 puntos
```

Al terminar los 3 niveles el juego pide un alias y guarda el total en `puntuaciones.txt`. Si el alias ya existe los puntos se acumulan. Al final se muestra el ranking de mayor a menor.

---

## Estructura del proyecto

```
Puzzle-Proyecto_ProgramacionIII/
├── Main_Puzzle/
│   └── src/
│       └── main_puzzle/
│           ├── Main_Puzzle.java           # Punto de entrada
│           ├── GraphicPuzzle.java         # Interfaz y logica del juego
│           ├── GraphicPuzzle.form         # Formulario NetBeans
│           ├── SolverAEstrella.java       # Algoritmo A*
│           ├── NodoPuzzle.java            # Nodo del arbol de busqueda
│           ├── ResultadoBusqueda.java     # Resultado del solver
│           ├── Puntuacion.java            # Modelo de puntuacion
│           └── ManejadorPuntuaciones.java # Lectura/escritura de puntuaciones.txt
├── puntuaciones.txt
├── build.xml
└── README.md
```
## Documentación

La documentación técnica del proyecto se encuentra disponible en:

* `docs/Reporte-Puzzle-8.pdf`

## Integrantes

- Venegas Aída
- Zermeño Paola
- Suárez Vladimir

Programación III — 4to semestre Grupo B — Junio 2026


import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase principal del sistema de juego
 */
public class SistemaJuego {
    private ArrayList<Corredor> corredores;
    private ArrayList<String> historial;
    private ManejadorArchivos manejadorArchivos;
    private Scanner scanner;

    /**
     * Constructor del sistema de juego
     */
    public SistemaJuego() {
        this.corredores = new ArrayList<>();
        this.historial = new ArrayList<>();
        this.manejadorArchivos = new ManejadorArchivos();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Inicia el sistema de juego
     */
    public void iniciar() {
        cargarDatos();
        mostrarMenu();
    }

    /**
     * Carga los datos iniciales desde los archivos
     */
    private void cargarDatos() {
        System.out.println("---------------------------------------------");
        System.out.println("    GARFIELD KART 2");
        System.out.println("---------------------------------------------");

        corredores = manejadorArchivos.cargarCorredores();
        manejadorArchivos.cargarHistorial(corredores);

    }

    /**
     * Muestra el menu principal y gestiona las opciones
     */
    private void mostrarMenu() {
        boolean salir = false;

        while (!salir) {
            System.out.println("---------------------------------------------");
            System.out.println("Menú Principal");
            System.out.println("---------------------------------------------");
            System.out.println("1. Nueva Carrera");
            System.out.println("2. Modo Torneo");
            System.out.println("3. Ver Estadisticas");
            System.out.println("4. Guardar y Salir");
            System.out.println("---------------------------------------------");
            System.out.print("Seleccione una opcion: ");

            try {
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        iniciarNuevaCarrera();
                        break;
                    case 2:
                        iniciarTorneo();
                        break;
                    case 3:
                        verEstadisticas();
                        break;
                    case 4:
                        guardarYSalir();
                        salir = true;
                        break;
                    default:
                        System.out.println("Opcion invalida. Intente nuevamente.");
                }
            } catch (Exception e) {
                System.out.println("Error: Debe ingresar un numero.");
                scanner.nextLine();
            }
        }
    }

    /**
     * Inicia una nueva carrera individual
     */
    private void iniciarNuevaCarrera() {
        System.out.println("---------------------------------------------");
        System.out.println("Nueva Carrera");
        System.out.println("---------------------------------------------");

        // Seleccionar corredor del jugador
        Corredor jugador = seleccionarCorredor();
        if (jugador == null) {
            return;
        }

        // Seleccionar oponentes aleatorios
        ArrayList<Corredor> oponentes = seleccionarOponentesAleatorios(jugador);

        // Crear y ejecutar carrera
        Carrera carrera = new Carrera();
        carrera.agregarParticipante(jugador);
        for (Corredor oponente : oponentes) {
            carrera.agregarParticipante(oponente);
        }

        Corredor[] resultados = carrera.simular();

        // Registrar en historial
        registrarCarreraEnHistorial(resultados);

        System.out.println("Presiona Enter para seguir...");
        scanner.nextLine();
    }

    /**
     * Inicia el modo torneo
     */
    private void iniciarTorneo() {
        System.out.println("---------------------------------------------");
        System.out.println("Modo Torneo");
        System.out.println("---------------------------------------------");
        System.out.println("Se jugaran 4 rondas");

        // Seleccionar corredor del jugador
        Corredor jugador = seleccionarCorredor();
        if (jugador == null) {
            return;
        }

        // Seleccionar oponentes fijos para el torneo
        ArrayList<Corredor> oponentes = seleccionarOponentesAleatorios(jugador);

        // Arrays para almacenar posiciones de todos los participantes
        int[][] posicionesPorRonda = new int[4][3]; // 4 rondas, 3 corredores
        Corredor[] participantes = {jugador, oponentes.get(0), oponentes.get(1)};

        // Jugar 4 rondas
        for (int ronda = 1; ronda <= 4; ronda++) {
            System.out.println("---------------------------------------------");
            System.out.println("          RONDA " + ronda + " DE 4");
            System.out.println("---------------------------------------------");

            // Crear y ejecutar carrera
            Carrera carrera = new Carrera();
            for (Corredor participante : participantes) {
                carrera.agregarParticipante(participante);
            }

            Corredor[] resultados = carrera.simular();

            // Registrar en historial
            registrarCarreraEnHistorial(resultados);

            // Guardar posiciones de todos los participantes
            for (int i = 0; i < resultados.length; i++) {
                for (int j = 0; j < participantes.length; j++) {
                    if (resultados[i].equals(participantes[j])) {
                        posicionesPorRonda[ronda - 1][j] = i + 1;
                        break;
                    }
                }
            }

            if (ronda < 4) {
                System.out.println("Presiona Enter para pasar a la siguiente ronda...");
                scanner.nextLine();
            }
        }

        // Mostrar resultados finales del torneo con podio
        mostrarResultadosTorneo(participantes, posicionesPorRonda);

        System.out.println("Presiona Enter para seguir...");
        scanner.nextLine();
    }

    /**
     * Muestra los resultados finales del torneo con podio
     */
    private void mostrarResultadosTorneo(Corredor[] participantes, int[][] posicionesPorRonda) {
        System.out.println("---------------------------------------------");
        System.out.println("Resultados Finales!");
        System.out.println("---------------------------------------------");

        // Calcular promedios de cada corredor
        double[] promedios = new double[3];

        for (int i = 0; i < participantes.length; i++) {
            System.out.println("Corredor: " + participantes[i].getNombre());
            System.out.println("Posiciones por ronda:");

            int suma = 0;
            for (int ronda = 0; ronda < 4; ronda++) {
                System.out.println("  Ronda " + (ronda + 1) + ": " + posicionesPorRonda[ronda][i] + "° lugar");
                suma += posicionesPorRonda[ronda][i];
            }

            promedios[i] = (double) suma / 4;
            System.out.printf("Promedio de posicion: %.2f\n\n", promedios[i]);
        }

        // Crear indices para ordenar
        Integer[] indices = {0, 1, 2};

        // Ordenar indices por promedio (menor promedio = mejor)
        for (int i = 0; i < indices.length - 1; i++) {
            for (int j = 0; j < indices.length - i - 1; j++) {
                if (promedios[indices[j]] > promedios[indices[j + 1]]) {
                    Integer temp = indices[j];
                    indices[j] = indices[j + 1];
                    indices[j + 1] = temp;
                }
            }
        }

        // Mostrar podio final
        System.out.println("---------------------------------------------");
        System.out.println("Podio Final");
        System.out.println("---------------------------------------------");

        String[] medallas = {"ORO", "PLATA", "BRONCE"};
        for (int i = 0; i < 3; i++) {
            int idx = indices[i];
            System.out.printf("%d° lugar - %s: %s (Promedio: %.2f)\n",
                    (i + 1), medallas[i], participantes[idx].getNombre(), promedios[idx]);
        }

        System.out.println("---------------------------------------------");
    }

    /**
     * Selecciona un corredor de la lista ordenada alfabeticamente
     */
    private Corredor seleccionarCorredor() {
        // Ordenar corredores alfabeticamente
        ArrayList<Corredor> corredoresOrdenados = new ArrayList<>(corredores);
        ordenarCorredoresAlfabeticamente(corredoresOrdenados);

        System.out.println("Seleccione su corredor:");
        for (int i = 0; i < corredoresOrdenados.size(); i++) {
            System.out.println((i + 1) + ". " + corredoresOrdenados.get(i));
        }

        System.out.print("Ingrese el numero del corredor: ");
        try {
            int seleccion = scanner.nextInt();
            scanner.nextLine();

            if (seleccion >= 1 && seleccion <= corredoresOrdenados.size()) {
                Corredor seleccionado = corredoresOrdenados.get(seleccion - 1);
                System.out.println("Has seleccionado a: " + seleccionado.getNombre());
                return seleccionado;
            } else {
                System.out.println("Seleccion invalida.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error en la seleccion.");
            scanner.nextLine();
            return null;
        }
    }

    /**
     * Ordena la lista de corredores alfabeticamente
     */
    private void ordenarCorredoresAlfabeticamente(ArrayList<Corredor> lista) {
        for (int i = 0; i < lista.size() - 1; i++) {
            for (int j = 0; j < lista.size() - i - 1; j++) {
                if (lista.get(j).getNombre().compareTo(lista.get(j + 1).getNombre()) > 0) {
                    Corredor temp = lista.get(j);
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, temp);
                }
            }
        }
    }

    /**
     * Selecciona dos oponentes aleatorios diferentes al jugador
     */
    private ArrayList<Corredor> seleccionarOponentesAleatorios(Corredor jugador) {
        ArrayList<Corredor> oponentes = new ArrayList<>();
        ArrayList<Corredor> disponibles = new ArrayList<>();

        // Crear lista de corredores disponibles (sin el jugador)
        for (Corredor corredor : corredores) {
            if (!corredor.equals(jugador)) {
                disponibles.add(corredor);
            }
        }

        // Seleccionar dos oponentes aleatorios
        while (oponentes.size() < 2 && disponibles.size() > 0) {
            int indice = (int)(Math.random() * disponibles.size());
            oponentes.add(disponibles.remove(indice));
        }

        return oponentes;
    }

    /**
     * Registra una carrera en el historial
     */
    private void registrarCarreraEnHistorial(Corredor[] resultados) {
        StringBuilder registro = new StringBuilder();

        for (int i = 0; i < resultados.length; i++) {
            registro.append(resultados[i].getNombre());
            if (i < resultados.length - 1) {
                registro.append(",");
            }
        }

        registro.append(",");

        for (int i = 0; i < resultados.length; i++) {
            registro.append(i + 1);
            if (i < resultados.length - 1) {
                registro.append(",");
            }
        }

        historial.add(registro.toString());
    }

    /**
     * Muestra las estadisticas del sistema
     */
    private void verEstadisticas() {
        Estadisticas estadisticas = new Estadisticas(corredores);
        estadisticas.mostrarEstadisticas();

        System.out.println("Presione Enter para seguir...");
        scanner.nextLine();
    }

    /**
     * Guarda los datos y sale del programa
     */
    private void guardarYSalir() {
        System.out.println("---------------------------------------------");
        System.out.println("Guardando Datos");
        System.out.println("---------------------------------------------");

        manejadorArchivos.guardarHistorial(historial);

        System.out.println("Gracias por jugar Garfield Kart 2!");
    }
}

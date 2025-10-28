import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase principal del sistema de juego
 * Controla el flujo del programa y coordina las diferentes funcionalidades
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
        System.out.println("\n========================================");
        System.out.println("    GARFIELD KART 2: TODO A LA DERIVA");
        System.out.println("========================================\n");
        System.out.println("Cargando datos del sistema...\n");

        corredores = manejadorArchivos.cargarCorredores();
        manejadorArchivos.cargarHistorial(corredores);

        System.out.println("\nSistema iniciado correctamente.\n");
    }

    /**
     * Muestra el menu principal y gestiona las opciones
     */
    private void mostrarMenu() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n========================================");
            System.out.println("              MENU PRINCIPAL");
            System.out.println("========================================");
            System.out.println("1. Nueva Carrera");
            System.out.println("2. Modo Torneo");
            System.out.println("3. Ver Estadisticas");
            System.out.println("4. Guardar y Salir");
            System.out.println("========================================");
            System.out.print("Seleccione una opcion: ");

            try {
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer

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
                        System.out.println("\nOpcion invalida. Intente nuevamente.");
                }
            } catch (Exception e) {
                System.out.println("\nError: Debe ingresar un numero.");
                scanner.nextLine(); // Limpiar buffer en caso de error
            }
        }
    }

    /**
     * Inicia una nueva carrera individual
     */
    private void iniciarNuevaCarrera() {
        System.out.println("\n========================================");
        System.out.println("            NUEVA CARRERA");
        System.out.println("========================================\n");

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

        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    /**
     * Inicia el modo torneo
     */
    private void iniciarTorneo() {
        System.out.println("\n========================================");
        System.out.println("            MODO TORNEO");
        System.out.println("========================================\n");
        System.out.println("Se jugaran 4 rondas consecutivas\n");

        // Seleccionar corredor del jugador
        Corredor jugador = seleccionarCorredor();
        if (jugador == null) {
            return;
        }

        // Array para almacenar posiciones en cada ronda
        int[] posicionesJugador = new int[4];

        // Jugar 4 rondas
        for (int ronda = 1; ronda <= 4; ronda++) {
            System.out.println("\n========================================");
            System.out.println("          RONDA " + ronda + " DE 4");
            System.out.println("========================================");

            // Seleccionar oponentes aleatorios para esta ronda
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

            // Guardar posicion del jugador
            for (int i = 0; i < resultados.length; i++) {
                if (resultados[i].equals(jugador)) {
                    posicionesJugador[ronda - 1] = i + 1;
                    break;
                }
            }

            if (ronda < 4) {
                System.out.println("\nPresione Enter para continuar a la siguiente ronda...");
                scanner.nextLine();
            }
        }

        // Mostrar resultados finales del torneo
        mostrarResultadosTorneo(jugador, posicionesJugador);

        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    /**
     * Muestra los resultados finales del torneo
     * @param jugador Corredor del jugador
     * @param posiciones Array con las posiciones en cada ronda
     */
    private void mostrarResultadosTorneo(Corredor jugador, int[] posiciones) {
        System.out.println("\n========================================");
        System.out.println("      RESULTADOS FINALES DEL TORNEO");
        System.out.println("========================================\n");

        System.out.println("Corredor: " + jugador.getNombre());
        System.out.println("\nPosiciones por ronda:");

        int suma = 0;
        for (int i = 0; i < posiciones.length; i++) {
            System.out.println("  Ronda " + (i + 1) + ": " + posiciones[i] + "° lugar");
            suma += posiciones[i];
        }

        double promedio = (double) suma / posiciones.length;
        System.out.printf("\nPromedio de posicion: %.2f\n", promedio);

        // Determinar clasificacion
        if (promedio <= 1.5) {
            System.out.println("Clasificacion: ORO - Excelente desempeno!");
        } else if (promedio <= 2.0) {
            System.out.println("Clasificacion: PLATA - Buen desempeno!");
        } else if (promedio <= 2.5) {
            System.out.println("Clasificacion: BRONCE - Desempeno regular");
        } else {
            System.out.println("Clasificacion: PARTICIPANTE");
        }

        System.out.println("========================================");
    }

    /**
     * Selecciona un corredor de la lista ordenada alfabeticamente
     * @return Corredor seleccionado o null si se cancela
     */
    private Corredor seleccionarCorredor() {
        // Ordenar corredores alfabeticamente
        ArrayList<Corredor> corredoresOrdenados = new ArrayList<>(corredores);
        ordenarCorredoresAlfabeticamente(corredoresOrdenados);

        System.out.println("Seleccione su corredor:\n");
        for (int i = 0; i < corredoresOrdenados.size(); i++) {
            System.out.println((i + 1) + ". " + corredoresOrdenados.get(i));
        }

        System.out.print("\nIngrese el numero del corredor: ");
        try {
            int seleccion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            if (seleccion >= 1 && seleccion <= corredoresOrdenados.size()) {
                Corredor seleccionado = corredoresOrdenados.get(seleccion - 1);
                System.out.println("\nHas seleccionado a: " + seleccionado.getNombre());
                return seleccionado;
            } else {
                System.out.println("\nSeleccion invalida.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("\nError en la seleccion.");
            scanner.nextLine(); // Limpiar buffer
            return null;
        }
    }

    /**
     * Ordena la lista de corredores alfabeticamente
     * @param lista Lista a ordenar
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
     * @param jugador Corredor del jugador
     * @return Lista con dos oponentes
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
     * @param resultados Array con los corredores ordenados por posicion
     */
    private void registrarCarreraEnHistorial(Corredor[] resultados) {
        StringBuilder registro = new StringBuilder();

        // Formato: corredor1,corredor2,corredor3,posicion1,posicion2,posicion3
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

        System.out.println("Presione Enter para continuar...");
        scanner.nextLine();
    }

    /**
     * Guarda los datos y sale del programa
     */
    private void guardarYSalir() {
        System.out.println("\n========================================");
        System.out.println("         GUARDANDO DATOS...");
        System.out.println("========================================\n");

        manejadorArchivos.guardarHistorial(historial);

        System.out.println("\nGracias por jugar Garfield Kart 2!");
        System.out.println("Hasta pronto!\n");
    }
}

import java.util.ArrayList;

/**
 * Clase que representa una carrera en el juego
 * Maneja la logica de simulacion de una carrera individual
 */
public class Carrera {
    private ArrayList<Corredor> participantes;
    private static final int META = 300;
    private static final int PAUSA_TURNOS = 1000; // Milisegundos

    /**
     * Constructor de la carrera
     */
    public Carrera() {
        this.participantes = new ArrayList<>();
    }

    /**
     * Agrega un corredor a la carrera
     * @param corredor Corredor a agregar
     */
    public void agregarParticipante(Corredor corredor) {
        corredor.reiniciarPosicion();
        participantes.add(corredor);
    }

    /**
     * Simula la carrera completa
     * @return Array con los corredores ordenados por posicion final
     */
    public Corredor[] simular() {
        int turno = 1;
        boolean carreraTerminada = false;

        System.out.println("\n========================================");
        System.out.println("      INICIANDO CARRERA - META: " + META + "m");
        System.out.println("========================================\n");

        mostrarParticipantes();

        // Simular turnos hasta que alguien llegue a la meta
        while (!carreraTerminada) {
            System.out.println("\n--- TURNO " + turno + " ---");

            for (Corredor corredor : participantes) {
                // Activar power-up aleatoriamente
                if (!corredor.tienePowerUpActivo() && Math.random() < 0.15) {
                    corredor.activarPowerUp();
                    System.out.println(corredor.getNombre() + " activo su power-up: " +
                            corredor.getPowerUp().getNombre() + "!");
                }

                int avance = corredor.avanzar();
                System.out.println(corredor.getNombre() + " avanzo " + avance + "m " +
                        "(Total: " + corredor.getPosicionActual() + "m)" +
                        (corredor.tienePowerUpActivo() ? " [POWER-UP ACTIVO]" : ""));

                // Verificar si llego a la meta
                if (corredor.getPosicionActual() >= META) {
                    carreraTerminada = true;
                }
            }

            turno++;

            // Pausa entre turnos para visualizacion
            try {
                Thread.sleep(PAUSA_TURNOS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Determinar posiciones finales
        return determinarPosiciones();
    }

    /**
     * Muestra los participantes de la carrera
     */
    private void mostrarParticipantes() {
        System.out.println("Participantes:");
        for (int i = 0; i < participantes.size(); i++) {
            System.out.println((i + 1) + ". " + participantes.get(i));
        }
    }

    /**
     * Determina las posiciones finales de los corredores
     * @return Array con los corredores ordenados por posicion
     */
    private Corredor[] determinarPosiciones() {
        Corredor[] posiciones = participantes.toArray(new Corredor[0]);

        // Ordenar por posicion actual (mayor a menor)
        for (int i = 0; i < posiciones.length - 1; i++) {
            for (int j = 0; j < posiciones.length - i - 1; j++) {
                if (posiciones[j].getPosicionActual() < posiciones[j + 1].getPosicionActual()) {
                    Corredor temp = posiciones[j];
                    posiciones[j] = posiciones[j + 1];
                    posiciones[j + 1] = temp;
                }
            }
        }

        // Mostrar resultados
        System.out.println("\n========================================");
        System.out.println("           RESULTADOS FINALES");
        System.out.println("========================================");
        for (int i = 0; i < posiciones.length; i++) {
            System.out.println((i + 1) + "° lugar: " + posiciones[i].getNombre() +
                    " (" + posiciones[i].getPosicionActual() + "m)");
            posiciones[i].registrarCarrera(i + 1);
        }
        System.out.println("========================================\n");

        return posiciones;
    }

    /**
     * Obtiene la lista de participantes
     * @return Lista de participantes
     */
    public ArrayList<Corredor> getParticipantes() {
        return participantes;
    }
}
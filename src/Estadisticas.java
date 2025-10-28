import java.util.ArrayList;

/**
 * Clase que maneja las estadisticas del juego
 * Procesa y muestra informacion sobre los corredores
 */
public class Estadisticas {
    private ArrayList<Corredor> corredores;

    /**
     * Constructor de estadisticas
     * @param corredores Lista de corredores del sistema
     */
    public Estadisticas(ArrayList<Corredor> corredores) {
        this.corredores = corredores;
    }

    /**
     * Muestra todas las estadisticas del sistema
     */
    public void mostrarEstadisticas() {
        System.out.println("\n========================================");
        System.out.println("         ESTADISTICAS GENERALES");
        System.out.println("========================================\n");

        mostrarEstadisticasIndividuales();
        mostrarEstadisticasGlobales();
    }

    /**
     * Muestra las estadisticas de cada corredor
     */
    private void mostrarEstadisticasIndividuales() {
        System.out.println("--- ESTADISTICAS POR CORREDOR ---\n");

        for (Corredor corredor : corredores) {
            System.out.println("Corredor: " + corredor.getNombre());
            System.out.println("  Carreras jugadas: " + corredor.getCarrerasJugadas());
            System.out.println("  Victorias: " + corredor.getVictorias());
            System.out.printf("  Posicion promedio: %.2f\n", corredor.getPosicionPromedio());
            System.out.printf("  Porcentaje de victorias: %.2f%%\n\n", corredor.getPorcentajeVictorias());
        }
    }

    /**
     * Muestra las estadisticas globales del sistema
     */
    private void mostrarEstadisticasGlobales() {
        System.out.println("--- ESTADISTICAS GLOBALES ---\n");

        Corredor conMasVictorias = obtenerCorredorConMasVictorias();
        if (conMasVictorias != null) {
            System.out.println("Personaje con mas victorias: " + conMasVictorias.getNombre() +
                    " (" + conMasVictorias.getVictorias() + " victorias)");
        }

        Corredor mejorPorcentaje = obtenerCorredorConMejorPorcentaje();
        if (mejorPorcentaje != null) {
            System.out.printf("Personaje con mejor porcentaje de victorias: %s (%.2f%%)\n",
                    mejorPorcentaje.getNombre(), mejorPorcentaje.getPorcentajeVictorias());
        }

        Corredor masUsado = obtenerCorredorMasUsado();
        if (masUsado != null) {
            System.out.println("Personaje mas usado: " + masUsado.getNombre() +
                    " (" + masUsado.getCarrerasJugadas() + " carreras)");
        }

        System.out.println("\n========================================\n");
    }

    /**
     * Obtiene el corredor con mas victorias
     * @return Corredor con mas victorias
     */
    private Corredor obtenerCorredorConMasVictorias() {
        Corredor mejor = null;
        int maxVictorias = 0;

        for (Corredor corredor : corredores) {
            if (corredor.getVictorias() > maxVictorias) {
                maxVictorias = corredor.getVictorias();
                mejor = corredor;
            }
        }

        return mejor;
    }

    /**
     * Obtiene el corredor con mejor porcentaje de victorias
     * @return Corredor con mejor porcentaje
     */
    private Corredor obtenerCorredorConMejorPorcentaje() {
        Corredor mejor = null;
        double maxPorcentaje = 0.0;

        for (Corredor corredor : corredores) {
            if (corredor.getCarrerasJugadas() > 0 &&
                    corredor.getPorcentajeVictorias() > maxPorcentaje) {
                maxPorcentaje = corredor.getPorcentajeVictorias();
                mejor = corredor;
            }
        }

        return mejor;
    }

    /**
     * Obtiene el corredor mas usado por los jugadores
     * @return Corredor mas usado
     */
    private Corredor obtenerCorredorMasUsado() {
        Corredor masUsado = null;
        int maxCarreras = 0;

        for (Corredor corredor : corredores) {
            if (corredor.getCarrerasJugadas() > maxCarreras) {
                maxCarreras = corredor.getCarrerasJugadas();
                masUsado = corredor;
            }
        }

        return masUsado;
    }
}

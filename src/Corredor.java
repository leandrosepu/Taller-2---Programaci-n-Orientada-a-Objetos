/**
 * Clase que representa a un corredor en el juego
 * Contiene la informacion y estadisticas del corredor
 */
public class Corredor implements Competidor {
    private String nombre;
    private int velocidadBase;
    private PowerUp powerUp;
    private int posicionActual;
    private int turnosPowerUp;
    private boolean powerUpActivo;

    // Estadisticas
    private int carrerasJugadas;
    private int victorias;
    private int sumaPosiciones;

    /**
     * Constructor del corredor
     */
    public Corredor(String nombre, int velocidadBase, PowerUp powerUp) {
        this.nombre = nombre;
        this.velocidadBase = velocidadBase;
        this.powerUp = powerUp;
        this.posicionActual = 0;
        this.turnosPowerUp = 0;
        this.powerUpActivo = false;
        this.carrerasJugadas = 0;
        this.victorias = 0;
        this.sumaPosiciones = 0;
    }

    /**
     * Avanza el corredor en la pista
     */
    @Override
    public int avanzar() {
        // Factor aleatorio entre -30 y 30
        int factorAleatorio = (int)(Math.random() * 61) - 30;
        int velocidadTotal = velocidadBase + factorAleatorio;

        // Si el power-up esta activo, suma su bonus
        if (powerUpActivo) {
            velocidadTotal += powerUp.getBonus();
            turnosPowerUp--;
            if (turnosPowerUp <= 0) {
                powerUpActivo = false;
            }
        }

        // Asegurar que la velocidad no sea negativa
        if (velocidadTotal < 0) {
            velocidadTotal = 0;
        }

        posicionActual += velocidadTotal;
        return velocidadTotal;
    }

    /**
     * Activa el power-up del corredor
     */
    @Override
    public void activarPowerUp() {
        if (!powerUpActivo) {
            powerUpActivo = true;
            turnosPowerUp = powerUp.getDuracion();
        }
    }

    /**
     * Verifica si el power-up esta activo
     */
    @Override
    public boolean tienePowerUpActivo() {
        return powerUpActivo;
    }

    /**
     * Reinicia la posicion del corredor para una nueva carrera
     */
    public void reiniciarPosicion() {
        this.posicionActual = 0;
        this.powerUpActivo = false;
        this.turnosPowerUp = 0;
    }

    /**
     * Registra una carrera jugada
     */
    public void registrarCarrera(int posicion) {
        carrerasJugadas++;
        sumaPosiciones += posicion;
        if (posicion == 1) {
            victorias++;
        }
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public int getVelocidadBase() {
        return velocidadBase;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public int getPosicionActual() {
        return posicionActual;
    }

    public int getCarrerasJugadas() {
        return carrerasJugadas;
    }

    public int getVictorias() {
        return victorias;
    }

    public double getPosicionPromedio() {
        if (carrerasJugadas == 0) {
            return 0.0;
        }
        return (double) sumaPosiciones / carrerasJugadas;
    }

    public double getPorcentajeVictorias() {
        if (carrerasJugadas == 0) {
            return 0.0;
        }
        return ((double) victorias / carrerasJugadas) * 100;
    }

    @Override
    public String toString() {
        return nombre + " (Velocidad: " + velocidadBase + ", PowerUp: " + powerUp.getNombre() + ")";
    }
}

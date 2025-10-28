/**
 * Interfaz que define el comportamiento de un competidor
 * Establece las acciones basicas que puede realizar un corredor
 */
public interface Competidor {
    /**
     * Avanza el corredor en la pista
     * @return Distancia avanzada
     */
    int avanzar();

    /**
     * Activa el power-up especial del corredor
     */
    void activarPowerUp();

    /**
     * Verifica si el power-up esta activo
     * @return true si el power-up esta activo, false en caso contrario
     */
    boolean tienePowerUpActivo();
}

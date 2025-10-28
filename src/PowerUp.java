/**
 * Clase que representa un power-up en el juego
 * Define las caracteristicas de cada poder especial
 */
public class PowerUp {
    private String nombre;
    private int bonus;
    private int duracion;

    /**
     * Constructor del power-up
     * @param nombre Nombre del power-up
     * @param bonus Bonus de velocidad que otorga
     * @param duracion Duracion en turnos del efecto
     */
    public PowerUp(String nombre, int bonus, int duracion) {
        this.nombre = nombre;
        this.bonus = bonus;
        this.duracion = duracion;
    }

    /**
     * Obtiene el nombre del power-up
     * @return Nombre del power-up
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el bonus de velocidad
     * @return Bonus de velocidad
     */
    public int getBonus() {
        return bonus;
    }

    /**
     * Obtiene la duracion del efecto
     * @return Duracion en turnos
     */
    public int getDuracion() {
        return duracion;
    }

    @Override
    public String toString() {
        return nombre + " (+" + bonus + " velocidad por " + duracion + " turnos)";
    }
}

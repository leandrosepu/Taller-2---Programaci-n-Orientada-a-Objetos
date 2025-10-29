import java.io.*;
import java.util.ArrayList;

/**
 * Clase que maneja la lectura y escritura de archivos
 */
public class ManejadorArchivos {
    private static final String ARCHIVO_PERSONAJES = "personajes.csv";
    private static final String ARCHIVO_HISTORIAL = "historial.txt";

    /**
     * Carga los corredores
     */
    public ArrayList<Corredor> cargarCorredores() {
        ArrayList<Corredor> corredores = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_PERSONAJES))) {
            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                // Saltar la primera linea
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] datos = linea.split(",");
                if (datos.length >= 3) {
                    String nombre = datos[0].trim();
                    int velocidad = Integer.parseInt(datos[1].trim());
                    String nombrePowerUp = datos[2].trim();

                    // Crear power-up basado en el nombre
                    PowerUp powerUp = crearPowerUp(nombrePowerUp);

                    Corredor corredor = new Corredor(nombre, velocidad, powerUp);
                    corredores.add(corredor);
                }
            }

           // System.out.println("Corredores cargados exitosamente: " + corredores.size()); //

        } catch (FileNotFoundException e) {
            System.out.println("Archivo " + ARCHIVO_PERSONAJES + " no encontrado.");
            crearArchivoPersonajesDefault();
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de personajes: " + e.getMessage());
        }

        return corredores;
    }

    /**
     * Crea un power-up basado en el nombre
     */
    private PowerUp crearPowerUp(String nombre) {
        // Asignar bonus y duracion segun el tipo de power-up
        int bonus = 20;
        int duracion = 2;

        // Variaciones segun el nombre
        if (nombre.toLowerCase().contains("turbo") || nombre.toLowerCase().contains("nitro")) {
            bonus = 30;
            duracion = 1;
        } else if (nombre.toLowerCase().contains("boost")) {
            bonus = 25;
            duracion = 2;
        } else if (nombre.toLowerCase().contains("cafe") || nombre.toLowerCase().contains("coffee")) {
            bonus = 20;
            duracion = 3;
        }

        return new PowerUp(nombre, bonus, duracion);
    }

    /**
     * Crea un archivo de personajes por defecto si no existe
     */
    private void crearArchivoPersonajesDefault() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_PERSONAJES))) {
            pw.println("Nombre,Velocidad,PowerUp");
            pw.println("Garfield,45,Lasagna Turbo");
            pw.println("Odie,50,Crazy Boost");
            pw.println("Nermal,48,Cute Power");
            pw.println("Arlene,47,Pink Speed");
            pw.println("Jon,42,Coffee Rush");
            System.out.println("Archivo creado con datos por defecto.");
        } catch (IOException e) {
            System.out.println("Error al crear archivo: " + e.getMessage());
        }
    }

    /**
     * Carga el historial desde el archivo
     */
    public void cargarHistorial(ArrayList<Corredor> corredores) {
        File archivo = new File(ARCHIVO_HISTORIAL);
        if (!archivo.exists()) {
            System.out.println("Archivo no encontrado. Se creara al guardar.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_HISTORIAL))) {
            String linea;
            int carrerasCargadas = 0;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 6) {
                    // Formato: corredor1,corredor2,corredor3,posicion1,posicion2,posicion3
                    String[] nombres = {datos[0].trim(), datos[1].trim(), datos[2].trim()};
                    int[] posiciones = {
                            Integer.parseInt(datos[3].trim()),
                            Integer.parseInt(datos[4].trim()),
                            Integer.parseInt(datos[5].trim())
                    };

                    // Actualizar estadisticas de cada corredor
                    for (int i = 0; i < nombres.length; i++) {
                        Corredor corredor = buscarCorredor(corredores, nombres[i]);
                        if (corredor != null) {
                            corredor.registrarCarrera(posiciones[i]);
                        }
                    }

                    carrerasCargadas++;
                }
            }


        } catch (IOException e) {
            System.out.println("Error al leer el historial: " + e.getMessage());
        }
    }

    /**
     * Busca un corredor por nombre
     */
    private Corredor buscarCorredor(ArrayList<Corredor> corredores, String nombre) {
        for (Corredor corredor : corredores) {
            if (corredor.getNombre().equals(nombre)) {
                return corredor;
            }
        }
        return null;
    }

    /**
     * Guarda el historial en el archivo
     */
    public void guardarHistorial(ArrayList<String> historial) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_HISTORIAL))) {
            for (String registro : historial) {
                pw.println(registro);
            }
            System.out.println("Historial guardado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar el historial: " + e.getMessage());
        }
    }
}

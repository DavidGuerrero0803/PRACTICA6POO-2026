package uabc.david.practica6poo2026;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * La clase Betweenle es la principal que coordina la lógica del juego:
 * Configura e inicia una partida.
 * Se encarga de validar los intentos del jugador antes de pasarlos al procesador de ronda.
 * Gestiona las pistas disponibles.
 * Expone el estado del juego para mandarlo a la interfaz de usuario.
 */
public class Betweenle {
    private Diccionario diccionario;
    private ProcesadorRonda rondaActual;
    private int letrasDificil;
    private boolean partidaActiva;

    /**
     * Crea una instancia del juego con la longitud de palabra para el modo difícil.
     * @param letrasDificil Número de letras para el nivel difícil.
     */
    public Betweenle(int letrasDificil) {
        this.letrasDificil = letrasDificil;
        this.diccionario = null;
        this.rondaActual = null;
        this.partidaActiva = false;
    }

    /**
     * Devuelve la ronda actualmente en juego.
     * @return La ronda activa de la partida.
     */
    public ProcesadorRonda getRondaActual() {
        return rondaActual;
    }

    /**
     * Determina la longitud de la palabra según la dificultad elegida.
     * @param dificultad Nivel de dificultad: "facil", "intermedio" o "dificil".
     * @return Cantidad de letras que tendrá la palabra secreta.
     */
    public int getLongitudDificultad(String dificultad) {
        if (dificultad.equals("facil")) {
            return 5;
        }
        if (dificultad.equals("intermedio")) {
            return 6;
        }
        // En modo difícil, se usa la longitud personalizada por el jugador.
        return letrasDificil;
    }

    /**
     * Devuelve el historial de intentos del jugador, mostrando las palabras en mayúsculas.
     * @return ArrayList con las palabras usadas.
     */
    public ArrayList<String> getHistorial() {
        return rondaActual.getHistorialIntentos()
                // Se abre el flujo de intentos realizados por el jugador.
                .stream()
                // map() se encarga de tomar la entrada de cada intento.
                // La lambda devuelve el historial de cada palabra en mayúscula.
                .map(entrada -> {
                    String[] historialPalabras = entrada.split("\\|");
                    String palabra = historialPalabras[0];
                    return palabra.toUpperCase();
                })
                // Todos los datos procesados del flujo se guardan en un ArrayList.
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Regresa un texto con el estado actual del rango y los intentos restantes.
     * Muestra el límite superior, el límite inferior y su proximidad en decimal.
     * @return Cadena de texto con el estado del juego.
     */
    public String getEstado() {
        boolean sinIntentos = rondaActual.getHistorialIntentos().isEmpty();

        // Para cubrir las proximidades al momento de iniciar una partida,
        // se ocultan con un símbolo de interrogación, hasta que haya al menos una palabra ingresada.
        String etiquetaSuperior = sinIntentos ? "?" : String.valueOf(rondaActual.getProximidadSuperior());
        String etiquetaInferior = sinIntentos ? "?" : String.valueOf(rondaActual.getProximidadInferior());

        // Una vez empiezan a haber palabras, las aproximaciones decimales se van a otro String.
        String aproxSuperior = String.format("%-2s", etiquetaSuperior);
        String aproxInferior = String.format("%-2s", etiquetaInferior);

        // Se regresa entonces el formato (interfaz) principal del juego.
        return  aproxSuperior + "  [ " + rondaActual.getLimiteSuperior().toUpperCase()
                + " ]\n" + "\n"
                + aproxInferior + "  [ " + rondaActual.getLimiteInferior().toUpperCase() + " ]\n"
                + "Intentos restantes: " + rondaActual.getIntentosRestantes();
    }

    /**
     * Configura e inicia una nueva partida: carga el diccionario,
     * selecciona aleatoriamente una palabra secreta y crea el procesador de ronda.
     * @param idioma Idioma del diccionario: "español" o "inglés".
     * @param dificultad Nivel de dificultad: "facil", "intermedio" o "dificil".
     * @param intentos Número de intentos que tendrá el jugador en esta partida.
     * @return true si la partida inició correctamente, false si no
     * existen palabras con la longitud requerida en el diccionario.
     */
    public boolean iniciarPartida(String idioma, String dificultad, int intentos) {

        // Se reutiliza el diccionario si ya está cargado para el mismo idioma.
        if (this.diccionario == null || !this.diccionario.getIdioma().equals(idioma)) {
            this.diccionario = new Diccionario(idioma);
            String rutaArchivo = idioma.equals("español") ?
                    "src/main/java/uabc/david/practica6poo2026/espanol.txt" :
                    "src/main/java/uabc/david/practica6poo2026/ingles.txt";
            this.diccionario.cargarArchivo(rutaArchivo);
        }

        int longitud = getLongitudDificultad(dificultad);

        // Se obtiene el arreglo de palabras con la longitud correcta, y ya ordenada alfabéticamente.
        ArrayList<String> palabrasOrdenadas = diccionario.obtenerPalabrasOrdenadas(longitud);
        if (palabrasOrdenadas.isEmpty()) {
            // Si no hay palabras con dicha longitud, la partida no puede comenzar.
            return false;
        }

        // Se elige una palabra secreta aleatoria de entre las disponibles.
        Random valorAleatorio = new Random();
        String palabraSecreta = palabrasOrdenadas.get(valorAleatorio.nextInt(palabrasOrdenadas.size()));

        // Se crea el procesador de ronda con todos los datos de la partida actual.
        rondaActual = new ProcesadorRonda(palabraSecreta, intentos, dificultad, palabrasOrdenadas);
        partidaActiva = true;
        return true;
    }

    /**
     * Se encarga de validar y procesar el intento del jugador.
     * @param intento La palabra ingresada por el jugador.
     * @return Una cadena que describe el resultado.
     */
    public String procesarIntento(String intento) {
        String palabraIngresada = Diccionario.limpiarAcentos(intento);

        // Se valida si quedan intentos del jugador.
        if (!rondaActual.tieneIntentos()) {
            return "sin intentos";
        }

        // La palabra debe tener exactamente el número de letras de la dificultad elegida.
        if (palabraIngresada.length() != rondaActual.getLongitudPalabra()) {
            return "longitud";
        }

        // La palabra debe existir en el diccionario.
        if (!diccionario.existeLaPalabra(palabraIngresada)) {
            return "no encontrada";
        }

        // La palabra debe estar dentro del rango actual.
        if (!rondaActual.estaEnRango(palabraIngresada)) {
            return "fuera de rango";
        }

        // Si pasa todas las validaciones, la ronda se procesa.
        String resultado = rondaActual.realizarIntento(palabraIngresada);

        // La partida termina si el jugador acertó o si ya no tiene más intentos.
        if (resultado.equals("correcto") || !rondaActual.tieneIntentos()) {
            partidaActiva = false;
        }

        return resultado;
    }

    /**
     * Agrega una palabra nueva al diccionario, al archivo de texto y la ingresa
     * dinámicamente en la ronda en curso posicionándola alfabéticamente.
     * @param palabra La palabra a agregar.
     */
    public void agregarPalabraAlDiccionario(String palabra) {
        diccionario.agregarPalabra(palabra);

        // Si hay una ronda activa, se agrega la palabra en la lista local.
        if (rondaActual != null) {
            rondaActual.agregarPalabraEnRonda(palabra);
        }
    }

    /**
     * Indica si la partida sigue en curso.
     * @return true si el jugador aún no ha ganado ni perdido.
     */
    public boolean juegoActivo() {
        return partidaActiva;
    }

    /**
     * Gestiona la pista solicitada por el jugador.
     * @param opcionPista Número de pista elegida (1, 2 o 3).
     * @return Mensaje con el resultado de la pista, o mensaje de error si no es válida.
     */
    public String pedirPista(int opcionPista) {
        // Solo se permite una pista por partida completa.
        if (rondaActual.pistaUtilizada()) {
            return "pista utilizada";
        }

        // Para las pistas 1 y 2 se necesita que el jugador haya hecho algún intento previo.
        if (opcionPista == 1 || opcionPista == 2) {
            if (rondaActual.getHistorialIntentos().isEmpty()) {
                return "requiere intento";
            }
        }

        // Pista encargada de mover el límite superior un 1%.
        if (opcionPista == 1) {
            String nuevoLimite = rondaActual.recorrerPalabraArriba();
            if (nuevoLimite.equals("demasiado cerca")) {
                return "demasiado cerca";
            }
            return "El límite superior ahora es: " + nuevoLimite.toUpperCase();
        }

        // Pista encargada de mover el límite inferior un 1%.
        if (opcionPista == 2) {
            String nuevoLimite = rondaActual.recorrerPalabraAbajo();
            if (nuevoLimite.equals("demasiado cerca")) {
                return "demasiado cerca";
            }
            return "El límite inferior ahora es: " + nuevoLimite.toUpperCase();
        }

        // Pista encargada de mostrar la primera letra de la palabra secreta.
        if (opcionPista == 3) {
            String letra = rondaActual.pistaLetraInicial();
            return "La palabra empieza con la letra: " + letra.toUpperCase();
        }

        return "opción inválida";
    }

}
